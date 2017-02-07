package thirdstage.exercise.rest.roll.control;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import thirdstage.exercise.rest.roll.entity.User;

@Api("User")
@RestController
@RequestMapping("/users")
public class UserController{

   @ApiOperation("Find a user of the specified ID")
   @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, value = "/{userId}")
   public User getUserById(@PathVariable("userId") String id){
      return new User();
   }

}
