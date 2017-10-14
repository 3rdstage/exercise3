package thirdstage.exercise.akka.wordanalysis.mappedrouter;

import org.slf4j.LoggerFactory;
import akka.routing.Routee;
import akka.routing.RoutingLogic;
import scala.collection.immutable.IndexedSeq;

public class MappedRoutingLogic<T extends java.io.Serializable> implements RoutingLogic{

   private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());


   @Override
   public Routee select(Object msg, IndexedSeq<Routee> routees){
      if(!(msg instanceof Keyed<?>)){
         this.logger.error("Keyed type message should be provided. But {} type message is provided.",
               msg.getClass().getSimpleName());

         //@TODO Need more consideration on the choice among execption or NoRoutee
         return akka.routing.NoRoutee$.MODULE$;
      }

      Key<T> key = ((Keyed<T>)msg).getKey();
      if(key == null){
         this.logger.error("The provided key is null.");
         return akka.routing.NoRoutee$.MODULE$;
      }

      //@TODO Isn't it need to guide the routee?
      int size = routees.size();
      Routee routee = null;
      KeyedRoutee<T> keyedRoutee = null;
      for(int i = 0; i < size; i++){
         if(!routees.isDefinedAt(i)){ break;} // the basic guide on routees, but much fragile
         routee = routees.apply(i);
         if(!(routee instanceof KeyedRoutee<?>)){
            this.logger.warn("KeyedRoutee type routee should be provided. But {} type is provided.",
                  routee.getClass().getSimpleName());
            continue;
         }

         if(key.equals(((KeyedRoutee<T>)routee).getKey())){
            this.logger.debug("Found routee mapped to the key of {}.", key);
            keyedRoutee = (KeyedRoutee<T>)routee;
            break;
         }
      }

      if(keyedRoutee != null){ return keyedRoutee; }
      else{
         this.logger.warn("Can't find routee is mapped to the key of {}.", key);
         return akka.routing.NoRoutee$.MODULE$;
      }

   }

}
