package thirdstage.exercise.truffle;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class ApplicationBasicTest{

  protected Logger logger = LoggerFactory.getLogger(this.getClass());

  private ConfigurableApplicationContext appl;

  private Lock lock = new ReentrantLock();

  @Before
  public void beforeClass() {

    if(this.appl == null) {
      this.lock.lock();
      try {
        this.appl = SpringApplication.run(thirdstage.exercise.truffle.Application.class);
      }finally {
        this.lock.unlock();
      }
    }
  }

  @Test
  public void testJustLoad() {

    Assert.assertNotNull(this.appl);

    String[] names = appl.getBeanDefinitionNames();
    logger.info("{} beans are defined for {}", names.length, appl.getDisplayName());
    if(this.logger.isDebugEnabled()) for(String name: names) logger.debug("     {}", name);

    Assert.assertTrue(names.length > 0);
  }


  @Test
  public void testProperties() {

    ConfigurableEnvironment env = this.appl.getEnvironment();

    String name = env.getProperty("spring.application.name");

    logger.info("spring.application.name : {}", name);
    logger.info("foo.bar : {}", env.getProperty("foo.bar"));
  }

  @Test
  public void testGetBeans() {
    ContractFacade facade = this.appl.getBean(ContractFacade.class);

    Assert.assertNotNull(facade);

    Assert.assertEquals(facade.getQuorumUrl(), "http://127.0.0.1:8054");
  }

}
