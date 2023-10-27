package thirdstage.exercise.hibernate6;

import java.time.LocalDate;

import org.hibernate.annotations.Comment;
import org.hibernate.id.ForeignGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@IdClass(ChainAndAddress.class)
public class Contract {

  @Id
  @ManyToOne
  @JoinColumn(name = "chain_id", foreignKey = @ForeignKey(name = "contract_fk1"))
  @Comment("chain where this contract is deployed")
  private Chain chain;

  @Id
  @Column(name = "addr")
  @Comment("contract address in 40 length hexadecimal with 0x prefix")
  private String address;

  @ManyToOne
  @JoinColumn(name = "contr_src_id",
    foreignKey = @ForeignKey(name = "contract_fk2"))
  @Comment("contract source of this contract instance")
  private ContractSource source;

  @Column
  @Comment("when this contract deployed")
  private LocalDate deployedAt;

  @Column(name = "deployer_addr")
  @Comment("deployer who signed the contract deployment transaction")
  private String deployerAddress;
  
  @Column
  @Comment("deployment transaction hash in 64 length hexadecimal with 0x prefix")
  private String deployTxHash;

}


