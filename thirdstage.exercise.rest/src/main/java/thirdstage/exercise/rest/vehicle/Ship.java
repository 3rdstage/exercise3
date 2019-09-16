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

@ApiModel(description = "Ship", parent = Vehicle.class)
public class Ship extends Vehicle{

  public static final String TYPE = "SHIP";

  private int cabins;

  public String getType() {
    return TYPE;
  }

  @ApiModelProperty
  public int getCabins(){
    return cabins;
  }

  public void setCabins(int cabins){
    this.cabins = cabins;
  }


}
