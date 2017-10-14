package thirdstage.exercise.akka.wordstats3;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;

public class StatsHttpServerTest3{

   private static org.slf4j.Logger logger = LoggerFactory.getLogger(StatsHttpServerTest3.class);

   public static final int HTTP_PORT_DEFAULT = 8080;

   public static void main(String[] args) throws Exception{
      int httpPort = HTTP_PORT_DEFAULT;

      final String javaPath = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
      final String mainClass = "thirdstage.exercise.akka.wordstats3.StatsHttpServer";
      final String[] argsArry = {"2551", "2552"};
      final String[] sentences = {
            "She's got a smile that it seems to me remindes me of childhood memories.", //expected 4.21...
            "Where everything was as fresh as the bright blue sky.", //expected 4.4
            "Now and then when I see her face she takes me away to that special place.", //expected 3.625
            "And if I stared too long I'd probably break down and cry." //expected 3.83...
      };

      final List<String> cmd0 = new ArrayList<String>();
      cmd0.add(javaPath);
      //command.add("-verbose");
      cmd0.add("-cp");
      cmd0.add(System.getProperty("java.class.path"));

      final List<String>[] commands = (List<String> []) new List[argsArry.length];
      for(int i = 0, n = argsArry.length; i < n; i++){
         commands[i] = new ArrayList<String>(cmd0);
         commands[i].add("-Dsentence=" + sentences[i]);
         commands[i].add(mainClass);
         commands[i].add(argsArry[i]);
         if(i == 0) commands[i].add(String.valueOf(httpPort));
      }

      final ProcessBuilder[] builders = new ProcessBuilder[argsArry.length];
      final Process[] processes = new Process[argsArry.length];

      Runtime.getRuntime().addShutdownHook(new Thread(){
         @Override
         public void run(){

            logger.info("Starting shoutdown-hook");

            for(int i = 0, n = argsArry.length; i < n; i++){
               try{
                  processes[i].destroy();
               }catch(Throwable th){
                  logger.error("Fail to destroy the process", th);
               }

               try{ Thread.sleep(500); }catch(Exception ex){}
            }

            try{ Thread.sleep(1000); }
            catch(Exception ex){}
            logger.info("Finished shutdown-hook");
         }
      });

      for(int i = 0, n = argsArry.length; i < n; i++){
         builders[i] = new ProcessBuilder(commands[i]);
         builders[i].redirectOutput(Redirect.INHERIT);
         builders[i].redirectError(Redirect.INHERIT);
         builders[i].environment();

         try{
            processes[i] = builders[i].start();
         }catch(Exception ex){
            logger.error("Fail to start the process. : {}", commands[i], ex);
         }
      }

      System.out.println("Press [Enter] key to end.");

      int cnt;
      try{
         while((cnt = System.in.available()) < 1){
            Thread.sleep(500);
         }
         while(cnt-- > 0) System.in.read();
      }catch(Exception ex){
         logger.error("Fail to wait the input. The VM would be finished.");
      }
   }


}
