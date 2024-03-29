package thirdstage.exercise.springboot.redis;

import javax.annotation.Resource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisBasicTest{

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Resource(name="redisTemplate")
  private ListOperations<String, String> listOps;


  @Test
  public void testListOps1() {

    listOps.leftPush("title", "Matrix");
    listOps.leftPush("state", "Closed");

    String title = listOps.leftPop("title");

    Assert.assertEquals(title, "Matrix");
  }




}
