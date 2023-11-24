package thirdstage.exercise.hibernate6;

import java.time.LocalDateTime;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.JdbcTypeCode;
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
@Table(indexes = {
  @Index(name = "security_uk1", unique = true, 
    columnList = "chain_id,contr_addr"),
  @Index(name = "security_fk2_idx", columnList = "chain_id,init_holder"),
  @Index(name = "security_fk3_idx", columnList = "chain_id,issuer"),
  @Index(name = "security_idx1", columnList = "name"),
  @Index(name = "security_idx2", columnList = "registered_at"),
  @Index(name = "security_idx3", columnList = "symbol"),
  @Index(name = "security_idx4", columnList = "provider_code")
})
@Comment("a financial instrument that holds some type of monetary value, such as stock, bond, or derivative")
public class Security {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  @Comment("auto generated surrogate key")
  private int id;


  @ManyToOne
  @JoinColumns(
    value = {
      @JoinColumn(name = "chain_id", referencedColumnName="chain_id"),
      @JoinColumn(name = "contr_addr", referencedColumnName="addr")
    },
    foreignKey = @ForeignKey(name = "security_fk1")
  )
  @Comment(on = "chain_id", value = "chain where this security resides")
  @Comment(on = "contr_addr", value = "address of contract instance for this security")
  private Contract contract;

  @Column(length = 200, nullable = false)
  @Comment("security name - usually according to EIP-20")
  private String name;

  @Column(length = 50)
  @Comment("security symbol - usually according to EIP-20")
  private String symbol;

  @ManyToOne
  @JoinColumns(
    value = {
      @JoinColumn(name = "chain_id", referencedColumnName="chain_id"),
      @JoinColumn(name = "issuer", referencedColumnName="addr")
    },
    foreignKey = @ForeignKey(name = "security_fk3")
  )  
  private Account issuer;

  @Column(length = 30)
  @Comment("tenant identifier")
  private String providerCode;

  @Column
  @DialectOverride.ColumnDefault(
    dialect = PostgreSQLDialect.class,
    override = @ColumnDefault("true")
  )
  @Comment("whether or not this security is issuable")
  private boolean isIssuable;

  @Column
  @DialectOverride.ColumnDefault(
    dialect = PostgreSQLDialect.class,
    override = @ColumnDefault("false")
  )
  @Comment("whether or not the contract for this security is paused - usually paused contracts are prevented to process any transaction.")
  private boolean isPaused;

  @Column
  @DialectOverride.Check(
    dialect = PostgreSQLDialect.class,
    override = @Check(name = "security_chk_cap", constraints = "cap >= 0")
  )
  @Comment("maximum supply for this security")
  private int cap;

  @Column
  @DialectOverride.Check(
    dialect = PostgreSQLDialect.class,
    override = @Check(
      name = "security_chk_init_supply", 
      constraints = "init_supply >= 0")
  )
  @Comment("the number of tokens issued alongside the security registration")
  private int initSupply;

  @ManyToOne
  @JoinColumns(
    value = {
      @JoinColumn(name = "chain_id", referencedColumnName="chain_id"),
      @JoinColumn(name = "init_holder", referencedColumnName="addr")
    },
    foreignKey = @ForeignKey(name = "security_fk2")
  )
  @Comment(on = "init_holder", value = "account who owns the initial supply")
  private Account initHolder;

  @Column
  @Comment("sum of current balances from all the holders of this security")
  private int supply;

  @Column
  @Comment("sum of current locked balances from all the holders of this security")
  private int lockedSupply;

  @Column(name = "descr")
  @JdbcTypeCode(SqlTypes.LONG32VARCHAR)
  @Comment("description for this security")
  private String description;

  @Column(precision = 3)
  @Temporal(TemporalType.TIMESTAMP)
  @DialectOverride.ColumnDefault(dialect = PostgreSQLDialect.class,
    override = @ColumnDefault("CURRENT_TIMESTAMP(3)"))
  @DialectOverride.ColumnDefault(dialect = MariaDBDialect.class,
    override = @ColumnDefault("CURRENT_TIMESTAMP()"))
  @Comment("when this security is registered")
  private LocalDateTime registeredAt;


  
  
}
