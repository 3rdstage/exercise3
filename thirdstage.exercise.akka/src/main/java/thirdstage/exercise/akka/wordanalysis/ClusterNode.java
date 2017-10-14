package thirdstage.exercise.akka.wordanalysis;

public interface ClusterNode{

   void start() throws Exception;

   void start(boolean allowsLocalRoutees) throws Exception;

   void stop() throws Exception;

}
