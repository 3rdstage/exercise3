package thirdstage.exercise.rest.point.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;

@Api("Point")
@RestController
@RequestMapping(value = "/points", 
      produces = {MediaType.APPLICATION_JSON_VALUE}, 
      consumes = {MediaType.APPLICATION_JSON_VALUE})
public class PointControllers{

}
