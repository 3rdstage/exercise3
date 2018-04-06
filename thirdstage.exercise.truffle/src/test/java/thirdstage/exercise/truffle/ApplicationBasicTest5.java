package thirdstage.exercise.truffle;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
//import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.web3j.quorum.Quorum;
  
//@RunWith(JUnitPlatform.class)
@TestInstance(Lifecycle.PER_CLASS)
class ApplicationBasicTest5{

  protected Logger logger = LoggerFactory.getLogger(this.getClass());

  private ConfigurableApplicationContext appl;

  private Lock lock = new ReentrantLock();

  @BeforeAll
  public void beforeAll() {

    this.appl = SpringApplication.run(thirdstage.exercise.truffle.Application.class);
  }

  @Test
  public void testJustLoad() {

    Assertions.assertNotNull(this.appl);

    String[] names = appl.getBeanDefinitionNames();
    logger.info("{} beans are defined for {}", names.length, appl.getDisplayName());
    if(this.logger.isDebugEnabled()) for(String name: names) logger.debug("     {}", name);

    Assertions.assertTrue(names.length > 0);
  }

  @Test
  public void testProperties() {

    ConfigurableEnvironment env = this.appl.getEnvironment();

    String name = env.getProperty("spring.application.name");

    logger.info("spring.application.name : {}", name);
    logger.info("foo.bar : {}", env.getProperty("foo.bar"));
  }

  @Test
  public void testGetQuorumBeans() {
    Quorum defaultQr = this.appl.getBean("defaultQuorum", Quorum.class);
    Assertions.assertNotNull(defaultQr);
    
    Quorum fallbackQr = this.appl.getBean("fallbackQuorum", Quorum.class);
    Assertions.assertNotNull(fallbackQr);
  }


}
