package thirdstage.exercise.hibernate6;

import java.time.LocalDate;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@IdClass(ChainAndAddress.class)
@Table(name = "eth_acct")
public class Account {

  @Id
  @ManyToOne
  @JoinColumn(name = "chain_id")
  @Comment("chain in which this account is opened")
  private Chain chain;

  @Id
  @Column(name = "addr", length = 42)
  @JdbcTypeCode(SqlTypes.CHAR)
  @Check(name = "eth_acct_chk_addr", constraints = "addr ~ '^(?-i)0x[0-9a-f]+$'")
  @Comment("account address in 40 length hexadecimal with 0x prefix")
  private String address;
  
  @Column(length = 30)
  @Comment("tenant identifier")
  private String providerCode = "ST_API";

  @Column(length = 60)
  @Comment("to trace sync or async interactions with external key custody service when creating this account or generating key-pairs")
  private String traceId;

  @ManyToOne
  @JoinColumn(name = "type_cd")
  @Comment("address type from code table - usually one of EOA, Proxy or other application defined types")
  private AccountType type;

  @Column(name = "pub_key", length = 130)
  @JdbcTypeCode(SqlTypes.CHAR)
  private String publicKey;

  @Column(name = "veiled_prv_key", length = 300)
  private String veiledPrivateKey;

  @Column
  private boolean isValid;

  @Column
  @JdbcTypeCode(SqlTypes.TIMESTAMP)
  @ColumnDefault("LOCALTIMESTAMP")
  private LocalDate createdAt;

  @Column
  @JdbcTypeCode(SqlTypes.TIMESTAMP)
  private LocalDate invalidAt;




}
