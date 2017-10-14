package thirdstage.exercise.akka.wordanalysis.mappedrouter;

import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.LoggerFactory;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigList;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;

public class KeyNodeMapHoconProvider implements KeyNodeMapProvider<String>{

   @Deprecated
   public final static String PATH_DEFAULT = "key-node-map";

   public final static String KEY_FOR_KEY = "key";

   public final static String KEY_FOR_NODE = "node";

   private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

   private final KeyNodeMap<String> keyNodeMap = new SimpleKeyNodeMap<String>();

   @Deprecated
   public KeyNodeMapHoconProvider(@Nonnull Config config){
      this(config, PATH_DEFAULT);
   }

   /**
    * The provided HOCON configuration should have key/node map data at the specified path
    * as a list of object that has 'key' and 'node' properties.
    *
    * @param config
    * @param path the path for the HOCON key that describe key/node map
    */
   public KeyNodeMapHoconProvider(@Nonnull Config config, @NotBlank String path){
      Validate.isTrue(config != null, "The config should be provided.");
      Validate.isTrue(StringUtils.isNotBlank(path), "The path for the key/node map datea in HOCON should be provided.");

      if(!config.hasPath(path)){
         this.logger.error("The provided HOCON configuration doesn't include key/node map data at the path of '{}'.", path);
         throw new IllegalStateException("The provided HOCON configuration doesn't include key/node map data at the specified path of '" + path + "'.");
      }

      ConfigList list = config.getList(path);
      Config config2 = null;
      String key = null;
      String nodeId = null;
      for(ConfigValue val : list){
         config2 = ((ConfigObject)val).toConfig();
         key = config2.getString(KEY_FOR_KEY);
         nodeId = config2.getString(KEY_FOR_NODE);
         this.logger.debug("Key/node map entry - key: {}, node: {}.", key, nodeId);

         this.keyNodeMap.putNodeId(new Key<String>(key), nodeId);
      }
      this.logger.info("Key/node map at '{}' has {} entries", path, list.size());

   }

   @Override @Nonnull
   public KeyNodeMap getKeyNodeMap(){
      return this.keyNodeMap;
   }

}
