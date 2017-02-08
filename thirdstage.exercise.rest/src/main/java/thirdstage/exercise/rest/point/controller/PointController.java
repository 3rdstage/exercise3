package thirdstage.exercise.rest.point.controller;

import org.apache.commons.lang3.Validate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import thirdstage.exercise.rest.point.entity.PointStamp;

@Api("Point")
@RestController
@RequestMapping(value = "/points", 
      produces = {MediaType.APPLICATION_JSON_VALUE}, 
      consumes = {MediaType.APPLICATION_JSON_VALUE})
public class PointController{
   
   @ApiOperation("Get current available point of the specified owner")
   @RequestMapping(method = RequestMethod.GET, value="/{ownerId}")
   public PointStamp findPointByOwner(@PathVariable final String ownerId){
      Validate.isTrue(ownerId == null, "The owner ID should be specified.");
      
      
      
      
      return new PointStamp();
   }

}
