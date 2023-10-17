
package thirdstage.exercise.ebean.sto;


import io.ebean.Model;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Chain extends Model{
  
  @Id
  private Integer id;
}
