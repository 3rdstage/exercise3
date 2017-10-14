package thirdstage.exercise.storm.calc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import thirdstage.exercise.storm.calc.SumTaskResult.TaskStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

public class SumTaskResultTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Test
	public void testSerDeWithJackson() throws Exception{

		SumTaskResult result = new SumTaskResult(1, TaskStatus.SUCCESS, 5050L);

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JaxbAnnotationModule());
		String resultStr = mapper.writeValueAsString(result);

		logger.info("The JSON representation of the result : {}", resultStr);

		SumTaskResult result2 = mapper.readValue(resultStr, result.getClass());

		Assert.assertEquals(result2.getNo(), result.getNo());
		Assert.assertEquals(result2.getStatus(), result.getStatus());
		Assert.assertEquals(result2.getSum(), result.getSum());
	}
}
