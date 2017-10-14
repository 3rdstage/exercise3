package thirdstage.exercise.akka.wordcounter;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import junit.framework.Assert;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SimpleTest {

   private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

   private String pid;

   @BeforeClass
   public void beforeClass(){
      RuntimeMXBean mbean = ManagementFactory.getRuntimeMXBean();
      pid = mbean.getName();
      if(pid != null && pid.length() > 0){
         if(pid.contains("@")) pid = pid.substring(0, pid.indexOf("@"));
      }
      MDC.put("pid", pid);

   }

   @Test
   public void testUtf8ToIsoLatin1() throws Exception{
      String name = new String("홍길동");
      byte[] bytes = name.getBytes("UTF-8");

      System.out.print("'");
      for(int i = 0, n = bytes.length; i < n; i++){
         System.out.write(bytes, i, 1);
         System.out.print(" ");
      }
      System.out.println("'");

      System.out.print("'");
      byte[] bytes2 = new byte[1];
      for(int i = 0, n = bytes.length; i < n; i++){
         bytes2[0] = bytes[i];
         System.out.print(new String(bytes2, "ISO-8859-1"));
         System.out.print(" ");
      }
      System.out.println("'");

      String hex = Hex.encodeHexString(bytes);
      System.out.println(hex);

      String name2 = new String(bytes, "UTF-8");
      System.out.println(name2);

      String name3 = new String(bytes, "ISO-8859-1");
      System.out.println(name3);
   }


   @Test
   public void testGetBytesWithUtf8AndIsoLatin1() throws Exception{

      String str = "C";  //0x43
      Assert.assertEquals(1, str.getBytes("ISO-8859-1").length);
      Assert.assertEquals(1, str.getBytes("UTF-8").length);

      String[] strs = {"§", "©", "®", "°", "±"}; //0xA7, 0xA9, 0xAE, 0xB1
      for(int i = 0, n = strs.length; i < n; i++){
         Assert.assertEquals(1, strs[i].getBytes("ISO-8859-1").length);
         Assert.assertEquals(2, strs[i].getBytes("UTF-8").length);
      }
   }

   @Test
   public void testLogbackWithConfitionalConfig(){
      this.logger.info("Current log line is suppose to have PID as a 2nd item of line header.");
   }

}
