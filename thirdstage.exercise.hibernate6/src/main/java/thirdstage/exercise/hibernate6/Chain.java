
package thirdstage.exercise.hibernate6;

import java.time.LocalDate;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;

@Entity
@Comment("blockchain network")
public class Chain{
  
  @Id
  private Integer id;

  @Column(length = 200, nullable = false)
  @Comment("chain name")
  private String name;

  @Column(length = 300, nullable = true)
  @Comment("JSON RPC endpoint for this chain")
  private String endpoint;

  @Column(length = 300, nullable = true)
  @Comment("base URL of block explorer for this chain")
  private String explorerUrl;

  @Column(nullable = true)
  @Comment("indicates the validity of this chain - Invalid chain is not recommended to connect to")
  private boolean isValid = true;

  @Column(name = "descr", nullable = true)
  @Comment("description for this chain")
  private String description;

}
