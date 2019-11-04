package thirdstage.exercise.lang;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MethodOverridingTest{




}


class Vehicle{


}

class Car extends Vehicle{

}

class Ship extends Vehicle{


}

interface VehicleTrader {

  public void sell(Vehicle v);


  public <T> void buy(T v);

}

class CarTrader implements VehicleTrader{

  //@Note the following method can not override the `super.sell(Vehicle v)` method
  //@Override
  //public void sell(Car c) {}

  @Override
  public void sell(Vehicle v) {}

  //@Note the following method can not override `super.buy(T v)` method
  //@Override
  //public void buy(Car c) {}

  @Override
  public <T> void buy(T v) {}

}

interface GenericVehicleTracer<T extends Vehicle>{

  public void sell(T v);

  public void buy(T v);
}

class CarTrader2 implements GenericVehicleTracer<Car>{

  @Override
  public void sell(Car v) {}

  @Override
  public void buy(Car v) {}
}

