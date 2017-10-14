package thirdstage.exercise.akka.wordstats3;


public class StatsHttpServerTest{


   public static void main(String[] args) throws Exception{

      int nettyPort = 2551;
      int httpPort = 8080;
      String appName = "wordstats";

      StatsHttpServer server = new StatsHttpServer(nettyPort, httpPort, appName);

      server.start();
   }


}
