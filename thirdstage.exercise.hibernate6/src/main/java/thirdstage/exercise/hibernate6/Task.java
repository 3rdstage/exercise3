package thirdstage.exercise.hibernate6;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(
  indexes = {
    @Index(name = "task_idx1", columnList = "type", unique = false ),
    @Index(name = "task_idx2", columnList = "started_at", unique = false),
    @Index(name = "task_idx3", columnList = "provider_code", unique = false)
})
@Comment("usually async task")
public class Task {

  @Id
  @Column(length = 60)
  @Comment("externally generated key to track this task")
  private String traceId;

  @Column(length = 60)
  @Comment("identifier to return to client for later result matching such as callback")
  private String asyncTaskId;
  
  @Column(length = 50)
  @Comment("task type")
  private String type;

  @Column(length = 30)
  private String providerCode;

  @Column(length = 50)
  @Comment("the way in which result is delivered to client for async operation, or NONE for sync operation")
  private String asyncMode;

  @Column(length = 50)
  private String state;

  @Column(name = "req_data")
  @JdbcTypeCode(SqlTypes.LONG32VARCHAR)
  @Comment("request data from the client")
  private String requestData;

  @Column(length = 500)
  private String callbackUrl;

  @Column
  @JdbcTypeCode(SqlTypes.LONG32VARCHAR)
  @Comment("data sent back to the client via the callback URL")
  private String callbackData;

  @Column
  @JdbcTypeCode(SqlTypes.LONG32VARCHAR)
  private String failReason;

  @Column(nullable = false, precision = 3)
  @Temporal(TemporalType.TIMESTAMP)
  @ColumnDefault("CURRENT_TIMESTAMP(3)")
  private LocalDateTime startedAt;

  @Column(precision = 3)
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime finishedAt;

}
