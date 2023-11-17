package thirdstage.exercise.hibernate6;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Code {

  @Id
  @Column(length = 30)
  @Comment("code")
  private String code;

  @Column(length = 200, nullable = false)
  @Comment("name")
  private String name;

  @Column(name = "descr", nullable = true)
  @Comment("description")
  private String description;



}
