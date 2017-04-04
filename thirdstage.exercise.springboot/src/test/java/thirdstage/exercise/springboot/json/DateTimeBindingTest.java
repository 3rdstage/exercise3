package thirdstage.exercise.springboot.json;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;
import thirdstage.exercise.springboot.json.ActivityValue.Type;
import thirdstage.exercise.springboot.json.CallValue.Direction;

/**
 * Tests some aspects of date-time in Java including Java 8 date-time API.
 *
 * @author Sangmoon Oh
 * @since 2017-03-03
 *
 * @see <a href='https://en.wikipedia.org/wiki/ISO_8601'>ISO 8601</a>
 * @see <a href='https://tools.ietf.org/html/rfc3339'>RFC 3339 - Date and Time on the Internet: Timestamps</a>
 * @see <a href='https://tools.ietf.org/html/rfc5545#section-3.3.5'>RFC 5545 (iCalendar) > Date-Time</a>
 * @see <a href='http://swagger.io/specification/#data-types-12'>Swagger Built-in Datatypes</a>
 */
@RunWith(SpringRunner.class)
@JsonTest
public class DateTimeBindingTest{

  private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private JacksonTester<CallValue> callParser;

  @Autowired
  private JacksonTester<ActivityValue> activityParser;

  @Autowired
  private JacksonTester<DateRangeValue> dateRangeParser;

  /**
   * This test case is expected to be run with '-Duser.timezone=+03:00' at command-line
   */
  @BeforeClass
  public static void beforeClass(){
    TimeZone defaultZone = TimeZone.getDefault();

    int offset = defaultZone.getRawOffset();

    //this use-case is expected to be run with user.timezone=GMT+03:00
    Assert.assertEquals(offset, 3*3600*1000);
  }

  /**
   * Proves that there are some equivalent names or IDs for timezone showing 'GMT+09:00' = 'GMT+09' = 'Asia/Seoul'
   */
  @Test
  public void testTimezoneEquality(){
    final String[] zoneIds = {"GMT+09:00", "GMT+09", "Asia/Seoul"};

    final int cnt = zoneIds.length;
    final TimeZone[] zones = new TimeZone[cnt];
    final Calendar[] cals = new Calendar[cnt];
    final Date[] dates = new Date[cnt];

    for(int i = 0; i < cnt; i++){
      zones[i] = TimeZone.getTimeZone(zoneIds[i]);
      cals[i] = GregorianCalendar.getInstance(zones[i]);
      cals[i].set(2017, 3, 1, 2, 30, 0);
      cals[i].set(Calendar.MILLISECOND, 0);
      dates[i] = cals[i].getTime();

      logger.info("Unix time for 2017-03-01T02:30:00.000 in {} : {}", zoneIds[i], dates[i].getTime());
      try{ Thread.sleep(20); }
      catch(Exception ex){ }
    }
    //Same timestamps from 'GMT+09:00' and 'GMT+09' are equal
    Assert.assertEquals(dates[0], dates[1]);
    Assert.assertEquals(dates[0].getTime(), dates[1].getTime());

    //Same timestamps from 'GMT+09:00' and 'Asia/Seoul' are equal
    Assert.assertEquals(dates[0], dates[2]);
    Assert.assertEquals(dates[0].getTime(), dates[2].getTime());

  }

  /**
   * Proves that the 'Calendar' object created without milli-second specified would have non-zero milli-second.
   */
  @Test
  public void testCalendarMillisecondUnspecified(){

    final String[] zoneIds = {"GMT+09:00", "GMT+09", "Asia/Seoul"};

    final int cnt = zoneIds.length;
    final TimeZone[] zones = new TimeZone[cnt];
    final Calendar[] cals = new Calendar[cnt];
    final Date[] dates = new Date[cnt];

    for(int i = 0; i < cnt; i++){
      zones[i] = TimeZone.getTimeZone(zoneIds[i]);
      cals[i] = GregorianCalendar.getInstance(zones[i]);
      cals[i].set(2017, 3, 1, 2, 30, 0);

      dates[i] = cals[i].getTime();

      logger.info("Unix time for 2017-03-01T02:30:00 in {} : {}", zoneIds[i], dates[i].getTime());
      try{ Thread.sleep(20); }
      catch(Exception ex){ }
    }

    //Calendars without millisecond specified would have different millisecond values
    Assert.assertNotEquals(dates[0], dates[1]);
    Assert.assertNotEquals(dates[0].getTime(), dates[1].getTime());

    //Calendars without millisecond specified would have different millisecond values
    Assert.assertNotEquals(dates[0], dates[2]);
    Assert.assertNotEquals(dates[0].getTime(), dates[2].getTime());
  }


  /**
   * Proves that the instance of {@link java.time.LocalDate} and the instance of {@link java.time.LocalDateTime}
   * are NOT equal.
   */
  @Test
  public void testLocalDateTimeAndLocalDateEquality(){

    LocalDateTime dt1 = LocalDateTime.of(2017, 3, 1, 0, 0, 0);
    LocalDate d1 = LocalDate.of(2017,  3, 1);

    Assert.assertNotEquals(dt1, d1);

  }

  /**
   * Proves that the Spring MVC built-in JSON parser based on Jackson can parse ISO-8601 local date-time strings (e.g. '2017-02-24T13:00:00')
   * or UTC data-time strings (e.g. '2017-02-24T13:00:00') into {@link java.util.Date} object.
   *
   * @throws Exception
   */
  @Test
  public void testLocalOrUtcDateTimeStringIntoDate() throws Exception{

    String callJson = "{\"id\" : \"1000\", \"direction\" : \"INBOUND\", \"userId\" : \"100\", \"startAt\" : \"2017-02-24T13:00:00Z\", \"endAt\" : \"2017-02-24T13:30:00\"}";

    CallValue call = callParser.parse(callJson).getObject();

    Assert.assertEquals(call.getId(), "1000");
    Assert.assertEquals(call.getDirection(), Direction.INBOUND);
    Assert.assertEquals(call.getUserId(), "100");

    final TimeZone tz = TimeZone.getTimeZone("GMT+09"); // Asia/Seoul
    final Calendar c1 = GregorianCalendar.getInstance(tz);

    c1.set(2017, 1, 24, 22, 0, 0); // 2017-02-24T13:00:00Z = 2017-02-24T22:00:00+09:00
    c1.set(Calendar.MILLISECOND, 0); //Without exact millisecond defined, c1 would have non-zero milliseconds
    final Date d1 = c1.getTime();
    logger.info("Timezone offset : {}", d1.getTimezoneOffset()); // Note that this value is -540 NOT 540

    //The timezone for the date would be the default timezone
    //which is specified by the 'user.timezone' system property at JVM bootstrap command line.
    //The timezone for the date is irrelevant to the timezone of calendar from which it is derived.
    Assert.assertNotEquals(-1*9*60, d1.getTimezoneOffset()); // the timezone of calendar : +09:00
    Assert.assertEquals(-1*3*60, d1.getTimezoneOffset()); // the JVM bootstrap timezone : +03:00

    final Calendar c2 = GregorianCalendar.getInstance(tz);
    c2.set(2017, 1, 24, 22, 30, 0); // 2017-02-24T13:00:00Z = 2017-02-24T22:00:00+09:00
    c2.set(Calendar.MILLISECOND, 0);
    final Date d2 = c2.getTime();

    logger.info("Unix time for call.startAt : {}", call.getStartAt().getTime());
    logger.info("Unix time for d1 : {}", d1.getTime());

    Assert.assertEquals(d1, call.getStartAt());
    Assert.assertEquals(d2, call.getEndAt());
  }

  /**
   * Proves that the Spring MVC built-in JSON parser based on Jackson can parse ISO-8601 date-time strings with offset
   * like '2017-02-24T13:00:00+09:00' or '2017-02-24T13:30:00+0900' into {@link java.util.Date} object.
   *
   * @throws Exception
   */
  @Test
  public void testNonlocalDateTimeStringIntoDate() throws Exception{

    String callJson = "{\"id\" : \"1000\", \"direction\" : \"INBOUND\", \"userId\" : \"100\", \"startAt\" : \"2017-02-24T13:00:00+09:00\", \"endAt\" : \"2017-02-24T13:30:00+0900\"}";

    CallValue call = callParser.parse(callJson).getObject();

    Assert.assertEquals(call.getId(), "1000");
    Assert.assertEquals(call.getDirection(), Direction.INBOUND);
    Assert.assertEquals(call.getUserId(), "100");

    final TimeZone tz = TimeZone.getTimeZone("GMT+09"); // Asia/Seoul
    final Calendar c1 = GregorianCalendar.getInstance(tz);
    c1.set(2017, 1, 24, 13, 0, 0); // 2017-02-24T13:00:00+09:00
    c1.set(Calendar.MILLISECOND, 0); //Without exact millisecond defined, c1 would have non-zero milliseconds
    final Date d1 = c1.getTime();
    logger.info("Timezone offset : {}", d1.getTimezoneOffset()); // Note that this value is -540 NOT 540

    //The timezone for the date would be the default timezone
    //which is specified by the 'user.timezone' system property at JVM bootstrap command line.
    //The timezone for the date is irrelevant to the timezone of calendar from which it is derived.
    Assert.assertNotEquals(-1*9*60, d1.getTimezoneOffset());
    Assert.assertEquals(-1*3*60, d1.getTimezoneOffset());

    final Calendar c2 = GregorianCalendar.getInstance(tz);
    c2.set(2017, 1, 24, 13, 30, 0); // 2017-02-24T13:00:00Z = 2017-02-24T22:00:00+09:00
    c2.set(Calendar.MILLISECOND, 0);
    final Date d2 = c2.getTime();

    logger.info("Unix time for call.startAt : {}", call.getStartAt().getTime());
    logger.info("Unix time for d1 : {}", d1.getTime());

    Assert.assertEquals(d1, call.getStartAt());
    Assert.assertEquals(d2, call.getEndAt());
  }

  /**
   * Proves that the Spring MVC built-in JSON parser based on Jackson can <strong>NOT</strong> parse RFC5545 UTC date-time strings
   * like '20170224T130000Z' into {@link java.util.Date} object. Acutally '20170224T130000Z' is also valid ISO 8601
   * representation but is NOT valid RFC 3339 date-time representation.
   *
   * @throws Exception
   */
  @Test(expected=com.fasterxml.jackson.databind.exc.InvalidFormatException.class)
  public void testCompactDateTimeStringIntoDate() throws Exception{

    String callJson = "{\"id\" : \"1000\", \"direction\" : \"INBOUND\", \"userId\" : \"100\", \"startAt\" : \"20170224T130000Z\", \"endAt\" : \"20170224T133000Z\"}";

    //Default JSON parser couldn't parse 20170224T130000Z into date type field.
    CallValue call = callParser.parse(callJson).getObject();

    Assert.assertEquals(call.getId(), "1000");
    Assert.assertEquals(call.getDirection(), Direction.INBOUND);
    Assert.assertEquals(call.getUserId(), "100");

    final TimeZone tz = TimeZone.getTimeZone("GMT+09"); // Asia/Seoul
    final Calendar c1 = GregorianCalendar.getInstance(tz);
    c1.set(2017, 1, 24, 22, 0, 0); // 2017-02-24T13:00:00+09:00
    c1.set(Calendar.MILLISECOND, 0); //Without exact millisecond defined, c1 would have non-zero milliseconds
    final Date d1 = c1.getTime();
    logger.info("Timezone offset : {}", d1.getTimezoneOffset()); // Note that this value is -540 NOT 540

    //The timezone for the date would be the default timezone
    //which is specified by the 'user.timezone' system property at JVM bootstrap command line.
    //The timezone for the date is irrelevant to the timezone of calendar from which it is derived.
    Assert.assertNotEquals(-1*9*60, d1.getTimezoneOffset());
    Assert.assertEquals(-1*3*60, d1.getTimezoneOffset());

    final Calendar c2 = GregorianCalendar.getInstance(tz);
    c2.set(2017, 1, 24, 13, 30, 0); // 2017-02-24T13:00:00Z = 2017-02-24T22:00:00+09:00
    c2.set(Calendar.MILLISECOND, 0);
    final Date d2 = c2.getTime();

    logger.info("Unix time for call.startAt : {}", call.getStartAt().getTime());
    logger.info("Unix time for d1 : {}", d1.getTime());

    Assert.assertEquals(d1, call.getStartAt());
    Assert.assertEquals(d2, call.getEndAt());
  }

  /**
   * Proves that the Spring MVC built-in JSON parser based on Jackson can parse ISO 8601 local date-time strings (e.g. '2017-02-24T13:00:00')
   * into {@link java.time.LocalDateTime} object.
   *
   * @throws Exception
   */
  @Test
  public void testLocalDateTimeStringIntoLocalDateTime() throws Exception{

    String activityJson = "{\"id\" : \"1000\", \"type\" : \"WALK\", \"amount\" : 10000, \"from\" : \"2017-02-24T13:00:00\", \"to\" : \"2017-02-24T13:30:00\"}";

    ActivityValue activity = activityParser.parse(activityJson).getObject();

    Assert.assertEquals("1000", activity.getId());
    Assert.assertEquals(Type.WALK, activity.getType());
    Assert.assertEquals(Integer.valueOf(10000), activity.getAmount());

    LocalDateTime d1 = LocalDateTime.of(2017, 2, 24, 13, 0, 0);
    LocalDateTime d2 = LocalDateTime.of(2017, 2, 24, 13, 30, 0);

    Assert.assertEquals(d1, activity.getFrom());
    Assert.assertEquals(d2, activity.getTo());
  }

  /**
   * Proves that the Spring MVC built-in JSON parser based on Jackson can parse ISO 8601 local date-time strings (e.g. '2017-02-24T13:00:00')
   * or UTC data-time strings (e.g. '2017-02-24T13:00:00') into {@link java.time.LocalDateTime} object.
   *
   * @throws Exception
   */
  @Test
  public void testUtcDateTimeStringIntoLocalDateTime() throws Exception{

    String activityJson = "{\"id\" : \"1000\", \"type\" : \"WALK\", \"amount\" : 10000, \"from\" : \"2017-02-24T13:00:00Z\", \"to\" : \"2017-02-24T13:30:00Z\"}";

    ActivityValue activity = activityParser.parse(activityJson).getObject();

    Assert.assertEquals("1000", activity.getId());
    Assert.assertEquals(Type.WALK, activity.getType());
    Assert.assertEquals(Integer.valueOf(10000), activity.getAmount());

    LocalDateTime d1 = LocalDateTime.of(2017, 2, 24, 13, 0, 0);
    LocalDateTime d2 = LocalDateTime.of(2017, 2, 24, 13, 30, 0);

    Assert.assertEquals(d1, activity.getFrom());
    Assert.assertEquals(d2, activity.getTo());
  }

  /**
   * Proves that the Spring MVC built-in JSON parser based on Jackson can NOT parse ISO 8601 non-local date-time strings
   * (e.g. '2017-02-24T13:00:00+09:00', '2017-02-24T13:30:00+0900') into {@link java.time.LocalDateTime} object.
   * <p>
   * Maybe the non-local date-time string should be binded with {@link java.time.OffsetDateTime}
   *
   * @throws Exception
   */
  @Test(expected=com.fasterxml.jackson.databind.exc.InvalidFormatException.class)
  public void testNonlocalDateTimeStringIntoLocalDateTime() throws Exception{

    String activityJson = "{\"id\" : \"1000\", \"type\" : \"WALK\", \"amount\" : 10000, \"from\" : \"2017-02-24T13:00:00+09:00\", \"to\" : \"2017-02-24T13:30:00+09:00\"}";

    ActivityValue activity = activityParser.parse(activityJson).getObject();

    Assert.assertEquals("1000", activity.getId());
    Assert.assertEquals(Type.WALK, activity.getType());
    Assert.assertEquals(Integer.valueOf(10000), activity.getAmount());

    LocalDateTime d1 = LocalDateTime.of(2017, 2, 24, 13, 0, 0);
    LocalDateTime d2 = LocalDateTime.of(2017, 2, 24, 13, 30, 0);

    Assert.assertEquals(d1, activity.getFrom());
    Assert.assertEquals(d2, activity.getTo());
  }

  /**
   * Proves that the Spring MVC built-in JSON parser based on Jackson can parse ISO 8601 local date strings (e.g. '2017-02-24')
   * into {@link java.time.LocalDate} object.
   *
   * @throws Exception
   */
  @Test
  public void testLocalDateStringIntoLocalDate() throws Exception{

    String rangeJson = "{\"from\" : \"2017-02-01\", \"to\" : \"2017-02-28\"}";

    DateRangeValue range = dateRangeParser.parse(rangeJson).getObject();

    LocalDate d1 = LocalDate.of(2017, 2, 1);
    LocalDate d2 = LocalDate.of(2017, 2, 28);

    Assert.assertEquals(d1, range.getFrom());
    Assert.assertEquals(d2, range.getTo());
  }


  /**
   * Proves that the Spring MVC built-in JSON parser based on Jackson can <strong>NOT</strong> parse date strings with timezone designator (e.g. '2017-02-24Z')
   * into {@link java.time.LocalDate} object. Actually '2017-02-24Z' is NOT valid ISO 3601 representation.
   *
   * @throws Exception
   */
  @Test(expected=com.fasterxml.jackson.databind.exc.InvalidFormatException.class)
  public void testUtcDateStringIntoLocalDate() throws Exception{

    String rangeJson = "{\"from\" : \"2017-02-01Z\", \"to\" : \"2017-02-28Z\"}";

    DateRangeValue range = dateRangeParser.parse(rangeJson).getObject();

    LocalDate d1 = LocalDate.of(2017, 2, 1);
    LocalDate d2 = LocalDate.of(2017, 2, 28);

    Assert.assertEquals(d1, range.getFrom());
    Assert.assertEquals(d2, range.getTo());
  }

  /**
   * Proves that the Spring MVC built-in JSON parser based on Jackson can <strong>NOT</strong> parse date strings with timezone designator (e.g. '2017-02-01+09:00')
   * into {@link java.time.LocalDate} object. Actually '2017-02-01+09:00' is NOT valid ISO 3601 representation.
   *
   * @throws Exception
   */
  @Test(expected=com.fasterxml.jackson.databind.exc.InvalidFormatException.class)
  public void testNonlocalDateStringIntoLocalDate() throws Exception{

    String rangeJson = "{\"from\" : \"2017-02-01+09:00\", \"to\" : \"2017-02-28+09:00\"}";

    DateRangeValue range = dateRangeParser.parse(rangeJson).getObject();

    LocalDate d1 = LocalDate.of(2017, 2, 1);
    LocalDate d2 = LocalDate.of(2017, 2, 28);

    Assert.assertEquals(d1, range.getFrom());
    Assert.assertEquals(d2, range.getTo());
  }

}
