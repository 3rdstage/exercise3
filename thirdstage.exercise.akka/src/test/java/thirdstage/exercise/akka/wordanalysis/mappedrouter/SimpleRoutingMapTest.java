package thirdstage.exercise.akka.wordanalysis.mappedrouter;

import org.testng.annotations.Test;
import thirdstage.exercise.akka.wordanalysis.mappedrouter.Key;
import thirdstage.exercise.akka.wordanalysis.mappedrouter.SimpleRoutingMap;

public class SimpleRoutingMapTest {

   @Test
   public void testPut() {
      String pathBase = "akka.tcp://127.0.0.1@2550:";
      SimpleRoutingMap<String> routingMap = new SimpleRoutingMap<String>();
      routingMap.putPath(new Key<String>("1"), pathBase + "2550/user/analysisService");
      routingMap.putPath(new Key<String>("2"), pathBase + "2551/user/analysisService");

   }
}
