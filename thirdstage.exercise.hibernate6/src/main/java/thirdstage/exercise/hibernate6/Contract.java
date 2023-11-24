package thirdstage.exercise.hibernate6;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.dialect.MariaDBDialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.id.ForeignGenerator;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@IdClass(ChainAndAddress.class)
public class Contract {

  @Id
  @ManyToOne
  @JoinColumn(name = "chain_id", foreignKey = @ForeignKey(name = "contract_fk1"))
  @Comment("chain where this contract is deployed")
  private Chain chain;

  @Id
  @Column(name = "addr", length=42)
  @JdbcTypeCode(SqlTypes.CHAR)
  @DialectOverride.Check(dialect = PostgreSQLDialect.class,
    override = @Check(name = "contract_chk_addr", 
                      constraints = "addr ~ '(?ec)^0x[0-9a-f]+$'"))
  @DialectOverride.Check(dialect = MariaDBDialect.class,
    override = @Check(name = "contract_chk_addr",
                      constraints = "`addr` RLIKE '^(?-i)0x[0-9a-f]+$'"))
  @Comment("contract address in 40 length hexadecimal with 0x prefix")
  private String address;

  @ManyToOne
  @JoinColumn(name = "contr_src_id",
    nullable = false,
    foreignKey = @ForeignKey(name = "contract_fk2"))
  @Comment("contract source of this contract instance")
  private ContractSource source;

  @Column(precision = 3)
  @Comment("when this contract deployed")
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime deployedAt;

  @Column(name = "deployer_addr", length = 42)
  @JdbcTypeCode(SqlTypes.CHAR)
  @Comment("deployer who signed the contract deployment transaction")
  private String deployerAddress;
  
  @Column(length = 66)
  @JdbcTypeCode(SqlTypes.CHAR)
  @Comment("deployment transaction hash in 64 length hexadecimal with 0x prefix")
  private String deployTxHash;

}


