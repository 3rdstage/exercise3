package thirdstage.exercise.springboot.redis;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class EnumFieldIndexInRedis extends AbstractIndexInRedis<String> implements EnumFieldIndex{


  private ValueOperations<String, String> valueOps;

  public EnumFieldIndexInRedis(@NotBlank String dataName, @NotBlank String indexName, @Autowired RedisTemplate<String, String> template) {
    super(dataName, indexName, template);

    this.valueOps = this.redisTemplate.opsForValue();
  }


  @Override
  public Index<String> addEntry(String key, String value){
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String[] findValues(String key){
    // TODO Auto-generated method stub
    return null;
  }

}
