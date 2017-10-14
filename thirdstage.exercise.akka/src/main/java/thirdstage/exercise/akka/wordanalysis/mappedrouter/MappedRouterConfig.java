package thirdstage.exercise.akka.wordanalysis.mappedrouter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import org.slf4j.LoggerFactory;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.routing.CustomRouterConfig;
import akka.routing.Routee;
import akka.routing.Router;

/**
 * @author Sangmoon Oh
 *
 * @param <K>
 * @see MappedRouterConfig2
 */
@Deprecated
public class MappedRouterConfig<K extends java.io.Serializable> extends CustomRouterConfig{

   private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

   private final RoutingMap<K> routingMap;

   private final Map<String, ActorRef> actorMap = new HashMap<String, ActorRef>();


   public MappedRouterConfig(@Nonnull RoutingMap<K> map){
      this.routingMap = map;

   }


   @Override
   public Router createRouter(ActorSystem system){

      String path = null;
      ActorSelection selection = null;
      KeyedActorSelectionRoutee<K> routee = null;
      final List<Routee> routees = new ArrayList<Routee>();
      for(Key<K> key : this.routingMap.getKeys()){
         path = this.routingMap.getPath(key);
         selection = system.actorSelection(path);
         routee = new KeyedActorSelectionRoutee<K>(key.get(), selection);
         routees.add(routee);
      }

      return new Router(new MappedRoutingLogic<K>(), routees);

   }


}
