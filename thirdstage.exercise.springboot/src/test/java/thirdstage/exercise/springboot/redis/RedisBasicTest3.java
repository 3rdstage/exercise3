package thirdstage.exercise.springboot.redis;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.annotation.concurrent.ThreadSafe;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisBasicTest3{

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Resource(name="redisTemplate")
  private ListOperations<String, String> listOps;


  @Test
  public void testListOps1() {

    listOps.leftPush("fruits", "Orange");
    listOps.leftPush("fruits", "Water Melon");

    String fruit = listOps.leftPop("fruits");

    Assertions.assertEquals(fruit, "Water Melon");
  }

  @Test
  public void testListOps2() {

    String[] fruits = new String[] {"Orange", "Water Melon", "Kiwi", "Apple", "Banana"};

    for(String f: fruits) {
       listOps.leftPush("fruits", f);
    }

    Assertions.assertEquals(listOps.size("fruits"), fruits.length);

  }

}
