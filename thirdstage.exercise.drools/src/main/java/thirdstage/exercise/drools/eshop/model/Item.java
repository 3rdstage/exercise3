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

  private int cost;
  
  private Category category;

  public int getCost(){
    return cost;
  }

  public void setCost(int cost){
    this.cost = cost;
  }

  public Category getCategory(){
    return category;
  }

  public void setCategory(Category category){
    this.category = category;
  }
  
  
}
