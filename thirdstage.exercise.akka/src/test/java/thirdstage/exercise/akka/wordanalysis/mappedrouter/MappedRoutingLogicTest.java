package thirdstage.exercise.akka.wordanalysis.mappedrouter;

import java.util.ArrayList;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import akka.actor.ActorSystem;
import akka.routing.Routee;
import akka.routing.RoutingLogic;
import scala.collection.immutable.IndexedSeq;
import thirdstage.exercise.akka.wordanalysis.Sentence;
import thirdstage.exercise.akka.wordanalysis.mappedrouter.KeyedActorSelectionRoutee;
import thirdstage.exercise.akka.wordanalysis.mappedrouter.MappedRoutingLogic;

public class MappedRoutingLogicTest {

   private RoutingLogic logic = new MappedRoutingLogic();

   @BeforeClass
   public void beforeClass(){

   }


   @Test
   public void testSelect1() {
      ActorSystem system = ActorSystem.create("AnalysisSystem");

      List<Sentence> sentences = new ArrayList<Sentence>();

      sentences.add(new Sentence("101", "1", "She's got a smile that it seems to me"));
      sentences.add(new Sentence("102", "2", "Reminds me of childhood memories"));
      sentences.add(new Sentence("103", "3", "Where everything"));

      List<Routee> routees = new ArrayList<Routee>();
      routees.add(new KeyedActorSelectionRoutee("3",
            system.actorSelection("akka.tcp://AnalysisSystem@127.0.0.1:2551/users/WordAnalysis")));

      IndexedSeq<Routee> routeeSeq = akka.japi.Util.immutableIndexedSeq(routees);

      Sentence sentence = sentences.get(2);
      Routee routee = this.logic.select(sentence, routeeSeq);

      Assert.assertTrue(routee instanceof KeyedActorSelectionRoutee);
      Assert.assertEquals(sentence.getKey(), ((KeyedActorSelectionRoutee)routee).getKey());

   }
}
