package thirdstage.exercise.springboot.redis;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.tuple.Pair;

public interface DateTimeFieldIndex extends RangeScanableIndex<LocalDateTime>{


  public Pair<LocalDateTime, String>[] findEntires(long from, long to);

  public Pair<LocalDateTime, String>[] findEntries(long from, boolean includesFrom, long to, boolean includesTo);

  public Pair<LocalDateTime, String>[] findEntries(@NotBlank String from, @NotBlank String to);

  public Pair<LocalDateTime, String>[] findEntries(@NotBlank String from, boolean includesFrom, @NotBlank String to, boolean includesTo);

}
