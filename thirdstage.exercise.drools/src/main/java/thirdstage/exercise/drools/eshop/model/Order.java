
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

public class Order{

  public enum OrderState{
    
    
  }
  
  private long id;
  
  private Customer customer;
  
  private List<OrderLine> orderLines;
  
  private OrderState state;
  
  private Discount discount;

}
