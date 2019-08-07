package thirdstage.exercise.springboot.redis;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public abstract class AbstractIndexInRedis<K> extends AbstractIndex<K>{


  protected RedisTemplate<String, String> redisTemplate;


  public AbstractIndexInRedis(@NotBlank String dataName, @NotBlank String indexName, @Autowired RedisTemplate<String, String> template) {
    super(dataName, indexName);
    this.redisTemplate = template;
  }

}
