package thirdstage.exercise.rest.roll.control;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import thirdstage.exercise.rest.roll.entity.User;

@Api("User")
@RestController
public class UserControl{

   @ApiOperation("Find a user of the specified ID")
   @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE}, value = "users/{userId}")
   public User getUserById(@PathVariable("userId") String id){
      return new User();
   }

}
