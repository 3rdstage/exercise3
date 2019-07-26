package thirdstage.exercise.springboot.redis;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.testng.Assert;
import org.testng.annotations.Test;

@SpringBootTest
public class RedisBasicTest2{

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

    Assert.assertEquals(fruit, "Water Melon");
  }

}
