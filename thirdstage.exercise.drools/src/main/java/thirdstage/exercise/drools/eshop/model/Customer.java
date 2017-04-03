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

public class Customer{
  
  public enum Category{
    SILVER,
    NA;
  }
  
  private long id;
  
  private String name; 
  
  private Category category;
  
  
  public long getId(){ return this.id; }
  
  public void setId(long id){ this.id = id; }
  
  public String getName(){ return this.name; }
  
  public void setName(String name){ this.name = name; }
  
  public Category getCategory(){ return this.category; }
  
  public void setCategory(Category cat){ this.category = cat; }
  
  

}
