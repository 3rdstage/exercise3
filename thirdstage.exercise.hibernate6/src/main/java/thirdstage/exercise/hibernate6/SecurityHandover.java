package thirdstage.exercise.hibernate6;

import java.time.LocalDateTime;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.dialect.MariaDBDialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "scrty_handover",
  indexes = {
    @Index(name = "scrty_handover_fk1_idx", columnList = "scrty_id"),
    @Index(name = "scrty_handover_fk2_idx", columnList = "chain_id,sender_addr"),
    @Index(name = "scrty_handover_fk3_idx", columnList = "chain_id,recipient_addr"),
    @Index(name = "scrty_handover_fk3_idx", columnList = "trace_id"),
    @Index(name = "scrty_handover_idx1", columnList = "handover_at")
  })
@Comment("issuance, redeem, or transfer (including forced) of security balances")
public class SecurityHandover {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  @Comment("auto generated surrogate key")
  private int id;

  @ManyToOne
  @JoinColumn(name = "scrty_id", referencedColumnName = "id",
    foreignKey = @ForeignKey(name = "scrty_handover_fk1"))
  @Comment("security identifier")
  private Security security;

  @Column(length = 50)
  @Comment("hand over type - typical types are TRANSFER, ISSUE, REDEEM, FORCED_TRANSFER and FORCED_REDEEM")
  private String type;

  @ManyToOne
  @JoinColumns(
    value = {
      @JoinColumn(name = "chain_id", nullable = false,
        referencedColumnName = "chain_id"),
      @JoinColumn(name = "sender_addr", nullable = true,
        referencedColumnName = "addr")
    },
    foreignKey = @ForeignKey(name = "scrty_handover_fk2")
  )
  @Comment(on = "chain_id", value = "chain where the security resides")
  @Comment(on = "sender_addr", value = "sender address in 40 length hexadecimal with 0x prefix - NULL in case of issuance types")
  private Account sender;
  
  @ManyToOne
  @JoinColumns(
    value = {
      @JoinColumn(name = "chain_id", nullable = false,
        referencedColumnName = "chain_id"),
      @JoinColumn(name = "recipient_addr", nullable = true,
        referencedColumnName = "addr")
    },
    foreignKey = @ForeignKey(name = "scrty_handover_fk3")
  )
  @Comment(on = "recipient_addr", value = "recipient address in 40 length hexadecimal with 0x prefix - NULL in case of redemption types")
  private Account recipient;

  
  @Column(nullable = false)
  @Comment("amount of tokens transferred, issued or redeemed")
  private int amount;

  @Column(length = 66)
  @JdbcTypeCode(SqlTypes.CHAR)
  @DialectOverride.Check(dialect = PostgreSQLDialect.class,
    override = @Check(name = "scrty_handover_chk_tx_hash", 
                      constraints = "addr ~ '(?ec)^0x[0-9a-f]+$'"))
  @DialectOverride.Check(dialect = MariaDBDialect.class,
    override = @Check(name = "scrty_handover_chk_tx_hash",
                      constraints = "`addr` RLIKE '^(?-i)0x[0-9a-f]+$'"))
  @Comment("Ethereum transaction hash (in 64 length hexadecimal with 0x prefix)")
  private String txHash;

  @JoinColumn(name = "trace_id",
    foreignKey = @ForeignKey(name = "scrty_handover_fk4"))
  @Comment("to trace async task for this handover")
  private Task task;

  @Column(precision = 3)
  @Temporal(TemporalType.TIMESTAMP)
  @DialectOverride.ColumnDefault(dialect = PostgreSQLDialect.class,
    override = @ColumnDefault("CURRENT_TIMESTAMP(3)"))
  @DialectOverride.ColumnDefault(dialect = MariaDBDialect.class,
    override = @ColumnDefault("CURRENT_TIMESTAMP()"))
  @Comment("when this hand over occurred (completion time is preferred over starting time)")
  private LocalDateTime handoverAt;
}
