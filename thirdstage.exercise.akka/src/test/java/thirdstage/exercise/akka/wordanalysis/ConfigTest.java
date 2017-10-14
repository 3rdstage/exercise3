package thirdstage.exercise.akka.wordanalysis;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigList;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;

public class ConfigTest{

   private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

   @Test
   public void testObjectArray(){

      String configStr = "key-node-map : [{key: 1, node: 2551}, {key: 2 , node: 2552}]";

      Config config = ConfigFactory.parseString(configStr);
      ConfigList list = config.getList("key-node-map");

      String[] keys = {"1", "2"};
      String[] nodes = {"2551", "2552"};
      String key = null;
      String nodeId = null;
      Config config2 = null;
      int cnt = 0;
      for(ConfigValue val : list){
         config2 = ((ConfigObject)val).toConfig();
         key = config2.getString("key");
         nodeId = config2.getString("node");

         Assert.assertTrue(ArrayUtils.contains(keys, key));
         Assert.assertTrue(ArrayUtils.contains(nodes, nodeId));

         logger.debug("element {} - key: {}, node: {}", cnt++, key, nodeId);
      }

      Assert.assertEquals(cnt, 2);

   }

}
