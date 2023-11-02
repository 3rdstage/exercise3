package thirdstage.exercise.hibernate6;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity(name = "contract_src")
@Table(
  indexes = {@Index(name = "contract_src_idx1", columnList = "type", unique = false )}
)
public class ContractSource {
    
  @Id
  private Integer id;

  @Column(nullable = false)
  @Comment("contract type such as ERC1400 ERC1400_FACTORY or et al")
  private String type;

  @Column(length = 200, nullable = false)
  @Comment("contract name")
  private String name;

  @Column(length = 40)
  private String srcCommitHash;

  @Column(length = 50)
  private String scrVer;

  @Basic
  @JdbcTypeCode(SqlTypes.CLOB)
  private String abi;

  @Column
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime createdAt;

}
