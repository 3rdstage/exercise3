package thirdstage.exercise.akka.wordstats3;


public class StatsHttpServerTest2{

   public static void main(String[] args) throws Exception{

      int[] nettyPorts = {2551, 2552};
      int httpPort = 8080;
      String appName = "wordstats";

      StatsHttpServer server = new StatsHttpServer(nettyPorts, httpPort, appName);

      server.start();
   }


}
