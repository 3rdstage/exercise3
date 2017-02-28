package thirdstage.exercise.springboot.json;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;
import thirdstage.exercise.springboot.json.Call.Direction;

@RunWith(SpringRunner.class)
@JsonTest
public class JacksonMapperTest{

  private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private JacksonTester<Call> callParser;

  @Test
  public void testCallDeserialize() throws Exception{

    String callJson = "{\"id\" : \"1000\", \"direction\" : \"INBOUND\", \"userId\" : \"100\", \"startAt\" : \"2017-02-24T13:00:00Z\", \"endAt\" : \"2017-02-24T13:30:00\"}";

    Call call = callParser.parse(callJson).getObject();

    Assert.assertEquals(call.getId(), "1000");
    Assert.assertEquals(call.getDirection(), Direction.INBOUND);
    Assert.assertEquals(call.getUserId(), "100");

    final TimeZone tz = TimeZone.getTimeZone("GMT+09"); // Asia/Seoul
    final Calendar c1 = Calendar.getInstance(tz);

    c1.set(2017, 1, 24, 22, 0, 0); // 2017-02-24T13:00:00Z = 2017-02-24T22:00:00+09:00
    final Date d1 = c1.getTime();
    final long os1 = d1.getTimezoneOffset(); //offset
    logger.info("Timezone offset : {}", os1);   // @TODO Why is this value -540 instead of 540

    final Calendar c2 = Calendar.getInstance(tz);
    c2.set(2017, 1, 24, 22, 30, 0); // 2017-02-24T13:00:00Z = 2017-02-24T22:00:00+09:00
    final Date d2 = c2.getTime();

    logger.info("Unix time for call.startAt : {}", call.getStartAt().getTime());
    logger.info("Unix time for d1 : {}", d1.getTime());

    Assert.assertEquals(d1, call.getStartAt());
    Assert.assertEquals(d2, call.getEndAt());
  }

}
