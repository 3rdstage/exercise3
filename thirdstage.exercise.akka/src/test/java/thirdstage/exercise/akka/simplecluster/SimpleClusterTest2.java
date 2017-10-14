package thirdstage.exercise.akka.simplecluster;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.slf4j.LoggerFactory;


public class SimpleClusterTest2{

   private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SimpleClusterTest2.class);

   public static void main(String... args) throws Exception{

      String javaPath = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";

      final List<String> cmd0 = new ArrayList<String>();
      cmd0.add(javaPath);
      //command.add("-verbose");
      cmd0.add("-cp");
      cmd0.add(System.getProperty("java.class.path"));
      cmd0.add("thirdstage.exercise.akka.simplecluster.SimpleClusterTest"); // main class


      final String[] ports = {"2551", "2552", "0"};
      final List<String>[] commands = (List<String> []) new List[ports.length];

      for(int i = 0, n = ports.length; i < n; i++){
         commands[i] = new ArrayList<String>(cmd0);
         commands[i].add(ports[i]);
      }

      final ProcessBuilder[] builders = new ProcessBuilder[ports.length];
      final Process[] processes = new Process[ports.length];
      final ProcessDestroyWatcher[] watchers = new ProcessDestroyWatcher[ports.length];

      for(int i = 0, n = ports.length; i < n; i++){
         builders[i] = new ProcessBuilder(commands[i]);
         //builders[i].inheritIO();
         //builders[i].redirectErrorStream(true);
         builders[i].redirectOutput(Redirect.INHERIT);
         builders[i].redirectError(Redirect.INHERIT);
         builders[i].environment();

         processes[i] = builders[i].start();
         //watchers[i] = new ProcessDestroyWatcher(processes[i]);
         //watchers[i].start();
      }

      Runtime.getRuntime().addShutdownHook(new Thread(){
         @Override
         public void run(){

            logger.info("Starting shoutdown-hook");

            for(int i = 0, n = ports.length; i < n; i++){
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


      System.out.println("Press [Enter] key to end.");
      int cnt;
      while((cnt = System.in.available()) < 1){
         Thread.currentThread().sleep(500);
      }
      while(cnt-- > 0) System.in.read();
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
         this.process.waitFor();
         this.logger.info("The process terminated.");
      }catch(Exception ex){
         this.logger.error("Fail to wait the process", ex);
      }

   }


}
