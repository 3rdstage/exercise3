package thirdstage.exercise.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatternSamples{

  final private Logger logger = LoggerFactory.getLogger(this.getClass());


  public String maskSubscriberNoOnly(final String concatenated) {

    if(concatenated == null || concatenated.length() == 0) return concatenated;

    final Pattern ptrn = Pattern.compile("\\d{3,}");
    final Matcher mtchr = ptrn.matcher(concatenated);

    int prevEnd = 0;
    String no = "", prevTail = "";
    final StringBuilder masked = new StringBuilder();
    while(mtchr.find()) {
      logger.debug("found: {}, {}, {}, {}, {}",
          mtchr.group(), mtchr.start(), mtchr.end(), concatenated.substring(prevEnd, mtchr.start()));

      no = mtchr.group();
      prevTail = concatenated.substring(prevEnd, mtchr.start());
      masked.append(prevTail).append(no.replaceAll("[0-4]", "*"));
      prevEnd = mtchr.end();
    }
    masked.append(concatenated.substring(prevEnd, concatenated.length()));

    logger.debug("unmaked: {}\nmasked: {}", concatenated, masked.toString());

    return masked.toString();
  }



}
