package thirdstage.exercise.akka.wordstats;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.slf4j.LoggerFactory;

public class StatsClusterTest{

   private static final org.slf4j.Logger logger = LoggerFactory.getLogger(StatsClusterTest.class);


   public static void main(String... args){

      final String javaPath = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
      final String mainClass = "thirdstage.exercise.akka.wordstats.StatsTest";
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
      }

      final ProcessBuilder[] builders = new ProcessBuilder[argsArry.length];
      final Process[] processes = new Process[argsArry.length];
      final ProcessDestroyWatcher[] watchers = new ProcessDestroyWatcher[argsArry.length];

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
         //builders[i].inheritIO();
         //builders[i].redirectErrorStream(true);
         builders[i].redirectOutput(Redirect.INHERIT);
         builders[i].redirectError(Redirect.INHERIT);
         builders[i].environment();

         try{
            processes[i] = builders[i].start();
            //watchers[i] = new ProcessDestroyWatcher(processes[i]); //block the parent process.
            //watchers[i].start();
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

class ProcessDestroyWatcher extends Thread{

   private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

   private final Process process;

   public ProcessDestroyWatcher(@Nonnull Process pr){
      this.process = pr;
   }

   @Override
   public void run(){

      try{
         this.process.waitFor();  //caused malfunction related to shutdown hook on the parent process.

         RuntimeMXBean mbean = ManagementFactory.getRuntimeMXBean();
         String name = mbean.getName();
         this.logger.info("The PID is : {}", name);

         this.logger.info("The process terminated.");
      }catch(Exception ex){
         this.logger.error("Fail to wait the process", ex);
      }
   }
}

