package thirdstage.exercise.rest.roll.entity;

import java.util.Date;
import javax.xml.bind.annotation.XmlAttribute;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Sangmoon Oh
 *
 */
@ApiModel(value = "user", description = "...")
public class User{

   private String id;

   private String loginId;

   private String name;

   private boolean isValid;

   private boolean passwordExpired;

   private java.util.Date registeredAt;

   private java.util.Date expireAt;

   @ApiModelProperty(position = 1, required = true, value = "auto generated unique identifier for this user")
   @XmlAttribute(required = true)
   public String getId(){
      return this.id;
   }

   public User setId(String id){
      this.id = id;
      return this;
   }

   @ApiModelProperty(position = 2, required = true, value = "login ID or login Name for this user")
   @XmlAttribute(required = true)
   public String getLoginId(){
      return this.id;
   }

   public User setLoginId(String loginId){
      this.loginId = loginId;
      return this;
   }

   @ApiModelProperty(position = 3, required = true, value = "real name for this user")
   @XmlAttribute(required = true)
   public String getName(){
      return this.name;
   }

   public User setName(String name){
      this.name = name;
      return this;
   }

   @ApiModelProperty(name = "isValid", position = 4, required = false, value = "whether of not this user is valid and granted to access")
   public boolean isValid(){
      return this.isValid;
   }

   public User setValid(boolean flag){
      this.isValid = flag;
      return this;
   }

   public Date getRegisteredAt(){
      return this.registeredAt;
   }

   public User setResisteredAt(Date at){
      this.registeredAt = at;
      return this;
   }

}
