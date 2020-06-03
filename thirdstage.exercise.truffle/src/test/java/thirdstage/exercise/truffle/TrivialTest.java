package thirdstage.exercise.truffle;

import java.util.List;
import java.io.File;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrivialTest {

  final private Logger logger = LoggerFactory.getLogger(this.getClass());


  @Test
  public void testStringFormatWithBigInteger() {

    long n1 = 100_000_000_000L;
    BigInteger n2 = BigInteger.valueOf(10_000_000L);

    String str = String.format("n1: %,d, n2: %,d", n1, n2);
    this.logger.info(str);

  }


  @Test
  public void testClassLoaderGetSystemResource() throws Exception{

    final String loc = "thirdstage/exercise/truffle/test-account.csv";

    final URL url = ClassLoader.getSystemResource(loc);
    final URI uri = url.toURI();
    final File file = new File(uri);


    this.logger.info("The URL : {}", url.toString());
    this.logger.info("The URI : {}", uri.toString());
    this.logger.info("The File : {}", file.getAbsolutePath());



  }

}
