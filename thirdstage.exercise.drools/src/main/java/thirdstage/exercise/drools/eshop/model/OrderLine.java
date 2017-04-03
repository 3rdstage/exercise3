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

public class OrderLine{
  
  private Item item;
  
  private int quantity;
  
  public Item getItem(){ return this.item; }
  
  public void setItem(Item item){ this.item = item; }
  
  public int getQuantity(){ return this.quantity; }
  
  public void setQuantity(int num){ this.quantity = num; }

}
