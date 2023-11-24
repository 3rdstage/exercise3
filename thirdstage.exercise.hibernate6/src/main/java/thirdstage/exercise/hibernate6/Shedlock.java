package thirdstage.exercise.hibernate6;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.dialect.MariaDBDialect;
import org.hibernate.dialect.PostgreSQLDialect;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table
@Comment("see https://github.com/lukas-krecan/ShedLock")
public class Shedlock {
  
  @Id
  @Column(length = 64)
  @Comment("lock name")
  private String name;

  @Column(nullable = false, precision = 3)
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime lockUntil;

  @Column(nullable = false, precision = 3)
  @Temporal(TemporalType.TIMESTAMP)
  @DialectOverride.ColumnDefault(dialect = PostgreSQLDialect.class,
    override = @ColumnDefault("CURRENT_TIMESTAMP(3)"))
  @DialectOverride.ColumnDefault(dialect = MariaDBDialect.class,
    override = @ColumnDefault("CURRENT_TIMESTAMP()"))
  private LocalDateTime lockedAt;
  
  @Column(nullable = false, length = 255)
  private String lockedBy;

}
