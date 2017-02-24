package thirdstage.exercise.springboot.json;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@JsonTest
public class JacksonMapperTest{

  @Autowired
  private JacksonTester<Call> callParser;

  @Test
  public void testCallDeserialize() throws Exception{

    String callJson = "{\"id\" : \"1000\", \"direction\" : \"INBOUND\", \"userId\" : \"100\", \"startAt\" : \"2017-02-24T13:00:00Z\", \"endAt\" : \"2017-02-24T13:30:00\"}";

    Call call = callParser.parse(callJson).getObject();

    Assert.assertEquals(call.getId(), "1000");

  }

}
