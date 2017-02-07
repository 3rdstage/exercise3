package thirdstage.exercise.rest.roll.controller;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hibernate.validator.constraints.NotEmpty;
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
@RequestMapping(value = "/users", 
      produces = {MediaType.APPLICATION_JSON_VALUE}, 
      consumes = {MediaType.APPLICATION_JSON_VALUE})
public class UserController{

   @ApiOperation("List all users including invalid users")
   @RequestMapping(method = RequestMethod.GET)
   public List<User> findAllUsers(){
      return new ArrayList<>();
   }
   
   @ApiOperation("Find a user of the specified ID")
   @RequestMapping(method = RequestMethod.GET, value = "/{userId}")
   public User findUserById(@PathVariable("userId") String id){
      return new User();
   }
   
   @ApiOperation("List all valid users")
   @RequestMapping(method = RequestMethod.GET, value = "/valid")
   public List<User> findValidUsers(){
      return new ArrayList<>();
   }

   @ApiOperation("List all invalid users")
   @RequestMapping(method = RequestMethod.GET, value = "/invalid")
   public List<User> findInvalidUsers(){
      return new ArrayList<>();
   }
   
   @ApiOperation("Add a new user")
   @RequestMapping(method = RequestMethod.POST)
   public User addUser(User user){
      Validate.isTrue(user != null, "The user to add should be specified");
      
      return new User();
   }
   
   @ApiOperation("Make a user invalid")
   @RequestMapping(method = RequestMethod.PATCH, value="{userId}/invalid")
   public User setUserInvalid(@NotEmpty @PathVariable("userId") String id){
      Validate.isTrue(StringUtils.isNotEmpty(id), "The user ID should be specified");
      
      return new User();
   }

   @ApiOperation("Make a user valid")
   @RequestMapping(method = RequestMethod.PATCH, value="{userId}/valid")
   public User setUserValid(@NotEmpty @PathVariable("userId") String id){
      Validate.isTrue(StringUtils.isNotEmpty(id), "The user ID should be specified");
      
      return new User();
   }
   
   
}
