package thirdstage.exercise.akka.simplecluster;

import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.MemberRemoved;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.ClusterEvent.UnreachableMember;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @author Sangmoon Oh
 *
 * @see <a href='http://www.lightbend.com/activator/template/akka-sample-cluster-java'>Akka Cluster Samples with Java</a>
 */
public class SimpleClusterListener extends UntypedActor{

   private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
   private Cluster cluster = Cluster.get(getContext().system());

   @Override
   public void preStart(){
      cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(), MemberEvent.class, UnreachableMember.class);
   }

   @Override
   public void postStop(){
      cluster.unsubscribe(getSelf());
   }


   @Override
   public void onReceive(Object msg){
      if(msg instanceof MemberUp){
         MemberUp ev = (MemberUp)msg;
         logger.info("{} is up at {}", ev.member(), this.toString());
      }else if(msg instanceof UnreachableMember){
         UnreachableMember ev = (UnreachableMember) msg;
         logger.info("Member is detected as unreachable : {}", ev.member());
      }else if(msg instanceof MemberRemoved){
         MemberRemoved ev = (MemberRemoved)msg;
         logger.info("Member is removed : {}", ev.member());
      }else if(msg instanceof MemberEvent){
         //ignore
      }else{
         this.unhandled(msg);
      }

   }

}
