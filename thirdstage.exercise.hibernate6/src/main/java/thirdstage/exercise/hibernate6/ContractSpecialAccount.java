package thirdstage.exercise.hibernate6;

import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "contr_spcl_acct",
  indexes = {
    @Index(name = "contr_spcl_acct_fk2_idx", columnList = "chain_id,acct_addr")
  })
@IdClass(ChainAndAddress.class) // removable?
@Comment("account granted special privileges for a contract")
public class ContractSpecialAccount {

  @Id
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumns(
    value = {
      @JoinColumn(name = "chain_id", referencedColumnName = "chain_id"),
      @JoinColumn(name = "contr_addr", referencedColumnName = "addr")},
    foreignKey = @ForeignKey(name = "contr_spcl_acct_fk1"))
  @Comment(on = "contr_addr", value="contract address in 40 length hexadecimal with 0x prefix")
  private Contract contract;
  
  @Column(length = 50)
  @Comment("role of this special account")
  private String role;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumns(
    value = {
      @JoinColumn(name = "chain_id", referencedColumnName = "chain_id"),
      @JoinColumn(name = "acct_addr", referencedColumnName = "addr")},
    foreignKey = @ForeignKey(name = "contr_spcl_acct_fk2"))
  @Comment(on = "acct_addr", value = "granted account")
  private Account account;

  @Column(precision = 3)
  @Temporal(TemporalType.TIMESTAMP)
  @Comment("when this privilege is granted to the account")
  private LocalDateTime grantedAt;
  

}
