package thirdstage.exercise.rest.vehicle;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/vehicle")
public class VehicleController{


  @PostMapping
  @ApiOperation(value = "Add a new vehicle.")
  public void addVehicle(@RequestBody @ApiParam Vehicle vehicle) {


  }

}
