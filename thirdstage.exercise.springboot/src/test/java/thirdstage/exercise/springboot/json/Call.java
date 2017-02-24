package thirdstage.exercise.springboot.json;

import java.util.Date;

public class Call{

  public enum Direction{
    INBOUND, OUTBOUND;
  }

  private String id;

  private Direction direction;

  private String userId;

  private Date startAt;

  private Date endAt;

  public String getId(){
    return id;
  }

  public void setId(String id){
    this.id = id;
  }

  public Direction getDirection(){
    return direction;
  }

  public void setDirection(Direction direction){
    this.direction = direction;
  }

  public String getUserId(){
    return userId;
  }

  public void setUserId(String userId){
    this.userId = userId;
  }

  public Date getStartAt(){
    return startAt;
  }

  public void setStartAt(Date startAt){
    this.startAt = startAt;
  }

  public Date getEndAt(){
    return endAt;
  }

  public void setEndAt(Date endAt){
    this.endAt = endAt;
  }




}
