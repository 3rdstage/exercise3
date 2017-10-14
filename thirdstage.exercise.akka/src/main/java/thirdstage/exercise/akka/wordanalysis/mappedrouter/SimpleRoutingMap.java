package thirdstage.exercise.akka.wordanalysis.mappedrouter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.LoggerFactory;

@Deprecated
public class SimpleRoutingMap<T extends java.io.Serializable> implements RoutingMap<T>{


   private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

   private final Map<Key<T>, String> map = new HashMap<Key<T>, String>();

   public SimpleRoutingMap(){ }

   public SimpleRoutingMap(@Nonnull Map<T, String> map){
      for(T key : map.keySet()){
         this.map.put(new Key<T>(key), map.get(key));
      }
   }

   @Override
   public boolean containsKey(@Nonnull Key<T> key){
      return this.map.containsKey(key);
   }

   @Override
   public String putPath(@Nonnull Key<T> key, @NotBlank String path){
      return this.map.put(key, path);
   }


   @Override
   public Set<Key<T>> getKeys(){
      return this.map.keySet();
   }

   @Override
   public String getPath(@NotBlank Key<T> key){
      return this.map.get(key);
   }
}
