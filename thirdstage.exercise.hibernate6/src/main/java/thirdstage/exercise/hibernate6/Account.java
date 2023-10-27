package thirdstage.exercise.hibernate6;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@IdClass(ChainAndAddress.class)
@Table(name = "eth_acct")
public class Account {

  @Id
  @JoinColumn
  @Comment("chain in which this account is opened")
  private Chain chain;

  @Id
  @Column(name = "addr", length = 42)
  @Comment("account address in 40 length hexadecimal with 0x prefix")
  private String address;
  
  @Column(length = 30)
  @Comment("tenant identifier")
  private String providerCode = "ST_API";

  @Column(length = 60)
  @Comment("to trace sync or async interactions with external key custody service when creating this account or generating key-pairs")
  private String traceId;

  @JoinColumn(name = "type_cd")
  @Comment("address type from code table - usually one of EOA, Proxy or other application defined types")
  private AccountType type;


}
