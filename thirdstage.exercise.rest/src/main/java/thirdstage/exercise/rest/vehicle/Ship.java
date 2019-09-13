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

public class Ship extends Vehicle{
  
  private int cabins;

  public int getCabins(){
    return cabins;
  }

  public void setCabins(int cabins){
    this.cabins = cabins;
  }

}
