package thirdstage.exercise.springboot.redis;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface RangeScanableIndex<K> extends Index<K>{


  public Pair<K, String>[] findEntries(@Nonnull K from, @Nonnull K to);

  public Pair<K, String>[] findEntires(@Nonnull K from, boolean includesForm, @Nonnull K to, boolean includesTo);



}
