package thirdstage.exercise.hibernate6;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity(name = "AccountType")
@Table(name = "AccountType")
public class AccountType extends Code {


}
