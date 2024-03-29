package thirdstage.exercise.drools.eshop.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Item{
  
  public enum Category{
    LOW_RANGE, HIGH_RANGE;
  }

  private long id;
  
  private String name; 
  
  private double cost;
  
  private double salePrice;
  
  private Category category;
  
  public Item(final String name, double cost){
    this.name = name;
    this.cost = cost;
  }
  
  public long getId(){ return this.id; }
  
  public void setId(long id){ this.id = id; }
  
  public String getName(){ return this.name; }

  public double getCost(){
    return cost;
  }

  public void setCost(double cost){
    this.cost = cost;
  }
  
  public double getSalePrice(){
    return salePrice;
  }
  
  public void setSalePrice(double price){
    this.salePrice = price;
  }

  public Category getCategory(){
    return category;
  }

  public void setCategory(Category category){
    this.category = category;
  }
  
  
  
  
}
