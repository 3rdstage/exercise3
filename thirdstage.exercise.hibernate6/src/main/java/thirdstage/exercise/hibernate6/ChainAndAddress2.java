package thirdstage.exercise.hibernate6;

import java.io.Serializable;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Embeddable
public class ChainAndAddress2  implements Serializable{

  @OneToOne
  @JoinColumn(name = "chain_id", nullable = false)
  private Chain chain;

  @Column(name = "addr", length = 42)
  @JdbcTypeCode(SqlTypes.CHAR)
  private String address; 
  
}
