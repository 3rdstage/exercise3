package thirdstage.exercise.springboot.redis;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sangmoon Oh
 *
 * @see https://en.wikipedia.org/wiki/Database_index
 * @see https://docs.oracle.com/en/database/oracle/oracle-database/19/cncpt/indexes-and-index-organized-tables.html
 * @see https://redis.io/topics/data-types-intro
 * @see https://docs.spring.io/spring-data/data-redis/docs/2.2.x/api/
 * @see https://docs.spring.io/spring-data/data-redis/docs/2.2.x/reference/html/
 */
@ThreadSafe
@ParametersAreNonnullByDefault
public interface IndexFacade{

  public void addSimpleIndex(
      @NotBlank String dataName, @NotBlank String indexName, IndexType type);

  public <K> void addSimpleIndexEntry(
      @NotBlank String dataName, @NotBlank String indexName, K indexKey, String indexValue);

  public <K> String[] findSimpleIndexEntries(
      @NotBlank String dataName, @NotBlank String indexName, K indexKey);

  public String[] findSimpleDateIndexEntries(
      @NotBlank String dataName, @NotBlank String indexName, LocalDateTime from, LocalDateTime to);

  public String[] findSimpleDateIndexEntries(
      @NotBlank String dataName, @NotBlank String indexName, String from, String to);

  public String[] findSimpleDateIndexEntries(
      @NotBlank String dataName, @NotBlank String indexName, long from, long to);




}
