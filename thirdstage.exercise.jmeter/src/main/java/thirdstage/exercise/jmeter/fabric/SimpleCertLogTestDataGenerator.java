package thirdstage.exercise.jmeter.fabric;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.commons.codec.digest.Sha2Crypt;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.Validate;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.LoggerFactory;

public class SimpleCertLogTestDataGenerator{

  private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

  protected static final String[] bankCodes = {"KB", "SHINHAN", "WOORI", "HANA", "NH", "KDB"};

  /**
   * @param num
   * @param baseId
   * @param idStep
   * @param output
   */
  public void generate(long num, long baseId, int idStep, @NotBlank String output){

    Validate.isTrue(num > 0, "The number of data to generate should be positive.");
    Validate.isTrue(baseId > 0, "Base ID should be positive number.");
    Validate.isTrue(idStep > 0, "ID increment step should be positive number.");

    RandomUtils ru = new RandomUtils();
    RandomStringUtils rsu = new RandomStringUtils();
    Sha2Crypt crypt = new Sha2Crypt();
    String salt ="fabric";

    try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(output)))){

      long id;
      byte[] cert;
      int certLen; //length of cert in character
      String certHash;
      String bankCode;
      String expireAt;
      for(long i = 0; i < num; i++){
        id = baseId + i * idStep;
        cert = rsu.randomAscii(100).getBytes("ISO-8859-1");
        certHash = crypt.sha256Crypt(cert);
        bankCode = bankCodes[ru.nextInt(0, bankCodes.length)];
        expireAt = LocalDate.of(2018, ru.nextInt(1, 13), ru.nextInt(1, 29)).format(DateTimeFormatter.BASIC_ISO_DATE);

        writer.print(id);
        writer.print(",");
        writer.print(certHash);
        writer.print(",");
        writer.print(bankCode);
        writer.print(",");
        writer.println(expireAt);
      }

      this.logger.info("Successfully finish to generate {} test data into {}", num, output);
    } catch(Exception ex){
      this.logger.error("Fail to create or write output file at {}", output, ex);
      throw new RuntimeException("Fail to create or write output file", ex);
    }
  }

  public static void printUsage(){

  }

}
