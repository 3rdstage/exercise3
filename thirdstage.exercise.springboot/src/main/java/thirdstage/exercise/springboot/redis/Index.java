package thirdstage.exercise.springboot.redis;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Index<K>{

  public String getDataName();

  public String getName();

  public Index<K> addEntry(@Nonnull K key, @Nonnull String value);

  public String[] findValues(@Nonnull K key);



}
