package thirdstage.exercise.springboot.json;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activity{

  enum Type{
    WALK,
    RUNNING
  }

  private String id;

  private Type type;

  private Integer amount;

  private LocalDateTime from;

  private LocalDateTime to;

  public String getId(){
    return id;
  }

  public Activity setId(final String id){
    this.id = id;
    return this;
  }

  public Type getType(){
    return type;
  }

  public Activity setType(final Type type){
    this.type = type;
    return this;
  }

  public Integer getAmount(){
    return amount;
  }

  public Activity setAmount(final Integer amount){
    this.amount = amount;
    return this;
  }

  public LocalDateTime getFrom(){
    return from;
  }

  public Activity setFrom(final LocalDateTime from){
    this.from = from;
    return this;
  }

  public LocalDateTime getTo(){
    return to;
  }

  public Activity setTo(final LocalDateTime to){
    this.to = to;
    return this;
  }



}
