package thirdstage.exercise.hibernate6;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.dialect.PostgreSQLDialect;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "scrty_holder",
  indexes = {
    @Index(name = "scrty_holder_fk2_idx", columnList = "chain_id, addr")
  })
@Comment("a holder who owns shares (in other words, has balances) for a security")
public class SecurityHolder {

  @Id
  @ManyToOne
  @JoinColumn(name = "scrty_id", 
    referencedColumnName = "id",
    foreignKey = @ForeignKey(name = "scrty_holder_fk1"))
  @Comment("security identifier")
  private Security security;

  @Id
  @JoinColumns(
    value = {
      @JoinColumn(name = "chain_id", referencedColumnName = "chain_id"),
      @JoinColumn(name = "addr", referencedColumnName = "addr")
    },
    foreignKey = @ForeignKey(name = "scrty_holder_fk2")
  )
  @Comment(on = "chain_id", value = "chain where the security resides")
  @Comment(on = "addr", value = "holder address in 40 length hexadecimal with 0x prefix")
  private Account holder;

  @Column(nullable = false)
  @Comment("amount of tokens owned by this holder")
  private int balance;

  @Column
  @Comment("amount of locked tokens for this holder")
  private int lockedBalance;

  @Column(nullable = false)
  @DialectOverride.ColumnDefault(
    dialect = PostgreSQLDialect.class,
    override = @ColumnDefault("false")
  )
  @Comment("whether or not the balance is locked (not transferrable by or on behalf of the owner)")
  private boolean isLocked;
}
