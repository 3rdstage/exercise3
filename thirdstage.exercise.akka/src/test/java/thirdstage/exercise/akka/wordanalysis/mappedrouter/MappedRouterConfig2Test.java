package thirdstage.exercise.akka.wordanalysis.mappedrouter;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.Routee;
import akka.routing.Router;
import scala.collection.immutable.IndexedSeq;
import thirdstage.exercise.akka.wordanalysis.Sentence;

@Test(singleThreaded=true)
public class MappedRouterConfig2Test {

   private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

   @Test
   public void testRoutees1() {

      String systemName = "AnalysisSystem";
      String addr = "127.0.0.1";
      int port = 2551;

      ActorSystem system = ActorSystem.create(systemName);

      List<Sentence> sentences = new ArrayList<Sentence>();

      sentences.add(new Sentence("101", String.valueOf(port), "She's got a smile that it seems to me"));
      sentences.add(new Sentence("102", String.valueOf(port + 1), "Reminds me of childhood memories"));
      sentences.add(new Sentence("103", String.valueOf(port + 2), "Where everything"));

      String pathBase = "akka.tcp://" + systemName + "@" + addr + ":";
      String pathTail = "/users/analysisService";
      List<Routee> routees = new ArrayList<Routee>();
      for(Sentence snt : sentences){
         routees.add(new KeyedActorSelectionRoutee(snt.getSourceId(),
               system.actorSelection(pathBase + snt.getSourceId() + pathTail)));
      }

      Router router = (new MappedRouterConfig2(routees)).createRouter(system);

      Assert.assertEquals(router.routees().size(), routees.size());

      String key = String.valueOf(port + sentences.size());
      routees.add(new KeyedActorSelectionRoutee(key,
            system.actorSelection(pathBase + key + pathTail)));

      Assert.assertNotEquals(router.routees().size(), routees.size());
   }

   @Test
   public void testRoutees2() {

      String systemName = "WordAnalysis";
      String addr = "127.0.0.1";
      String name = "dummyService";
      List<String> keys = new ArrayList<String>();
      keys.add("2551");
      keys.add("2552");
      keys.add("2553");

      Config config = ConfigFactory.load();
      config = config.getConfig("wordanalysis").withFallback(config);
      config = ConfigFactory.parseString("akka.cluster.roles = [compute]").withFallback(config);
      config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + 2551).withFallback(config);

      ActorSystem system = ActorSystem.create("WordAnalysis", config);

      system.actorOf(Props.create(Props.EmptyActor.class), name);

      String pathBase = "akka.tcp://" + systemName + "@" + addr + ":";
      String pathTail = "/users/" + name;
      List<Routee> routees = new ArrayList<Routee>();
      for(String key: keys){
         routees.add(new KeyedActorSelectionRoutee(key,
               system.actorSelection(pathBase + key + pathTail)));
      }

      Router router = (new MappedRouterConfig2(routees)).createRouter(system);

      IndexedSeq<Routee> routees2 = router.routees();

      Assert.assertEquals(router.routees().size(), routees.size());

      KeyedActorSelectionRoutee routee1 = null;
      KeyedActorSelectionRoutee routee2 = null;
      for(int i = 0, n = routees.size(); i < n; i++){
         routee1 = (KeyedActorSelectionRoutee)(routees.get(i));
         routee2 = (KeyedActorSelectionRoutee)(routees2.apply(i));

         Assert.assertEquals(routee2.selection(), routee1.selection());
         logger.debug("actor selection 1: {}, {}, {}", routee1.selection().anchorPath().toString(),
               routee1.selection().pathString(), routee2.selection().toString());
         logger.debug("actor selection 2: {}, {}, {}", routee2.selection().anchorPath().toString(),
               routee2.selection().pathString(), routee2.selection().toString());

      }

   }
}
