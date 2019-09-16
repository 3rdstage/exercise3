package thirdstage.exercise.rest.vehicle;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
    subTypes = {Car.class, Ship.class},
    discriminator = "type")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes({
    @Type(value = Car.class, name = Car.TYPE),
    @Type(value = Ship.class, name = Ship.TYPE)
})
abstract public class Vehicle{

  private String name;

  private String manufacturer;

  private String speed;

  private double weight;


  @ApiModelProperty(required = true)
  abstract public String getType();

  @ApiModelProperty
  public String getName(){
    return name;
  }

  public void setName(String name){
    this.name = name;
  }

  @ApiModelProperty
  public String getManufacturer(){
    return manufacturer;
  }

  public void setManufacturer(String manufacturer){
    this.manufacturer = manufacturer;
  }

  @ApiModelProperty
  public String getSpeed(){
    return speed;
  }

  public void setSpeed(String speed){
    this.speed = speed;
  }

  @ApiModelProperty
  public double getWeight(){
    return weight;
  }

  public void setWeight(double weight){
    this.weight = weight;
  }



}
