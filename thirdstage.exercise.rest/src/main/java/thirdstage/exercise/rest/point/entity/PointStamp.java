package thirdstage.exercise.rest.point.entity;

import java.util.Date;
import javax.validation.constraints.Min;
import org.hibernate.validator.constraints.NotEmpty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PointStamp{
   
   private String ownerId;
   
   private long point;
   
   private Date timestamp;
   
   @ApiModelProperty(required = true)
   public String getOwnerId(){
      return this.ownerId;
   }
   
   public PointStamp setOwnerId(@NotEmpty final String id){
      this.ownerId = id;
      return this;
   }
   
   @ApiModelProperty(required = true)
   public long getPoint(){
      return this.point;
   }
   
   public PointStamp setPoint(final long point){
      this.point = point;
      return this;
   }

   @ApiModelProperty(required = false)
   public Date getTimestamp(){
      return this.timestamp;
   }
   
   public PointStamp setTimestamp(final Date timestamp){
      this.timestamp = timestamp;
      return this;
   }
}
