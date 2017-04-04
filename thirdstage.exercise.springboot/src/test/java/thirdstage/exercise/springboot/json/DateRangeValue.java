package thirdstage.exercise.springboot.json;

import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Immutable
public class DateRangeValue{

  private LocalDate from;

  private LocalDate to;

  //@NOTE For 'JsonCreator' refer http://www.cowtowncoder.com/blog/archives/2010/08/entry_409.html
  @JsonCreator
  public DateRangeValue(@JsonProperty("from") LocalDate from, @JsonProperty("to") LocalDate to){
    this.from = from;
    this.to= to;
  }

  public LocalDate getFrom(){
    return this.from;
  }

  public LocalDate getTo(){
    return this.to;
  }
}
