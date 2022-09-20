package thirdstage.exercise.spring.security.crypto;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordEncoderTest{

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Test
  public void testEncode(){

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    String passwd = "sto202205";

    logger.info("Plain : {}, Encoded: {}", passwd, encoder.encode(passwd));

  }




}
