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

public class Vehicle{
  
  private String type;
  
  private String name;
  
  private String manufacturer;
  
  private String speed;
  
  private double weight;

  public String getType(){
    return type;
  }

  public void setType(String type){
    this.type = type;
  }

  public String getName(){
    return name;
  }

  public void setName(String name){
    this.name = name;
  }

  public String getManufacturer(){
    return manufacturer;
  }

  public void setManufacturer(String manufacturer){
    this.manufacturer = manufacturer;
  }

  public String getSpeed(){
    return speed;
  }

  public void setSpeed(String speed){
    this.speed = speed;
  }

  public double getWeight(){
    return weight;
  }

  public void setWeight(double weight){
    this.weight = weight;
  }
  
  

}
