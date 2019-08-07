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
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sangmoon Oh
 *
 * Data = Record+
 * Record = Record ID, Field+
 * Field = Field Name, Field Value
 *
 * Index = Index Entry+
 * Index Entry = Key, Value
 *
 * Index Key = Field Value
 * Index Value = Record ID
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
      @NotBlank String dataName, @NotBlank String indexName, FieldType type);

  /**
   * @param <K> String, Integer, LocalDate
   * @param dataName
   * @param indexName
   * @param indexKey
   * @param indexValue
   */
  public <K> void addSimpleIndexEntry(
      @NotBlank String dataName, @NotBlank String indexName, K indexKey, String indexValue);

  public <K> Pair<K, String>[] findSimpleIndexEntries(
      @NotBlank String dataName, @NotBlank String indexName, K indexKey);


  public <K> Pair<K, String>[] findSimpleIndexEntries(
      @NotBlank String dataName, @NotBlank String indexName, K from, K to);


  public <K> Pair<K, String>[] findSimpleIndexEntires(
      @NotBlank String dataName, @NotBlank String indexName, K from, boolean includesFrom, K to, boolean includesTo);

}
