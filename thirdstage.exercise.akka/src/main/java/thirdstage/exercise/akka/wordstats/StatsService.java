package thirdstage.exercise.akka.wordstats;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.ConsistentHashingRouter.ConsistentHashableEnvelope;
import akka.routing.FromConfig;
import thirdstage.exercise.akka.wordstats.StatsMessages.StatsJob;
import thirdstage.exercise.akka.wordstats.StatsMessages.StatsJobFailed;
import thirdstage.exercise.akka.wordstats.StatsMessages.StatsResult;
import thirdstage.exercise.akka.wordstats.StatsMessages.StatsTask;


/**
 * @author Sangmoon Oh
 * @since 2016-04-15
 * @see <a href="http://www.lightbend.com/activator/template/akka-sample-cluster-java">Akka Cluster Samples with Java</a>
 */
public class StatsService extends UntypedActor{

   private final LoggingAdapter logger = Logging.getLogger(this.getContext().system(), this);

   ActorRef jobTracer = getContext().actorOf(Props.create(JobTracer.class), "jobTracer");

   ActorRef workerRouter = getContext().actorOf(
         FromConfig.getInstance().props(Props.create(StatsWorker.class)), "workerRouter");

   @Override
   public void onReceive(Object message) throws Exception{
      if(message instanceof StatsJob){
         StatsJob job = (StatsJob)message;
         this.logger.debug("Received a job - Job ID: {}, Text: '{}'", job.getId(), job.getText());

         String traceLog = new StringBuilder().append("<job id=\"").append(job.getId()).append("\"/>").toString();
         this.jobTracer.tell(traceLog, getSelf());

         if("".equals(job.getText())){
            this.unhandled(message);
         }else{
            final String[] words = job.getText().split(" ");

            ActorRef aggregator = this.getContext().actorOf(
                  Props.create(StatsAggregator.class, words.length, this.getSelf()));

            StatsTask task = null;
            for(int i = 0, n = words.length; i < n; i++){
               task = new StatsTask(job.getId(), i + 1, words[i]);
               this.workerRouter.tell(new ConsistentHashableEnvelope(task, words[i]), aggregator);
            }
         }
      }else if(message instanceof StatsResult){
         this.logger.info("Stats result : {}", message.toString());
      }else if(message instanceof StatsJobFailed){
         this.logger.info("Stats failed : {}", message.toString());
      }else {
         this.unhandled(message);
      }
   }

}
