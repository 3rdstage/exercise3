package thirdstage.exercise.jmeter.fabric;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleCertLogTestDataGeneratorTest1{

  public static void main(String[] args){

    if(args.length != 4) printUsage();

    long num = Long.valueOf(args[0]); // the number of test data
    long baseId = Long.valueOf(args[1]); // the base or starting value of user ID
    int idStep = Integer.valueOf(args[2]); // the step of user ID increment
    String output = args[3];

    SimpleCertLogTestDataGenerator generator = new SimpleCertLogTestDataGenerator();

    generator.generate(num, baseId, idStep, output);

  }

  private static void printUsage(){

  }

}
