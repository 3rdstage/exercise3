package thirdstage.exercise.rest.vehicle;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "Car", parent = Vehicle.class)
public class Car extends Vehicle{

  public static final String TYPE = "CAR";

  private int price;

  private int length;

  private int width;

  private int height;

  public String getType() {
    return TYPE;
  }

  @ApiModelProperty
  public int getPrice(){
    return price;
  }

  public void setPrice(int price){
    this.price = price;
  }

  @ApiModelProperty
  public int getLength(){
    return length;
  }

  public void setLength(int length){
    this.length = length;
  }

  @ApiModelProperty
  public int getWidth(){
    return width;
  }

  public void setWidth(int width){
    this.width = width;
  }

  @ApiModelProperty
  public int getHeight(){
    return height;
  }

  public void setHeight(int height){
    this.height = height;
  }




}
