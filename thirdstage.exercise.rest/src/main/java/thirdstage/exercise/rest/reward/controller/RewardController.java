package thirdstage.exercise.rest.reward.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;

@Api("Reward")
@RestController
@RequestMapping(value = "/rewards", 
      produces = {MediaType.APPLICATION_JSON_VALUE}, 
      consumes = {MediaType.APPLICATION_JSON_VALUE})
public class RewardController{

}
