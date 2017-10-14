package thirdstage.exercise.storm.calc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

public class SumJobRequestTest {

   private final Logger logger = LoggerFactory.getLogger(this.getClass());

   @Test
   public void testSerDeWithJackson1() throws Exception{

      SumJobRequest req = new SumJobRequest("R100", 1, 100, 1, 3, 100);

      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JaxbAnnotationModule());
      String reqStr = mapper.writeValueAsString(req);

      logger.info("The JSON representation of the request : {}", reqStr);

      SumJobRequest req2 = mapper.readValue(reqStr, req.getClass());

      Assert.assertEquals(req2.getId(), req.getId());
      Assert.assertEquals(req2.getFrom(), req.getFrom());
      Assert.assertEquals(req2.getTo(), req.getTo());
      Assert.assertEquals(req2.getStep(), req.getStep());
      Assert.assertEquals(req2.getDelay(), req.getDelay());
   }

}
