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

public interface IndexFacade{

  public void addSimpleIndex(String record, String indexName, IndexType type);

  public void addSimpleIndexEntry(String record, String indexName, String indexKey, String indexValue);

  public String[] findSimpleIndexEntries(String record, String indexName, String indexKey);

  public String[] findSimpleDateIndexEntries(String record, String indexName, String from, String to);



}
