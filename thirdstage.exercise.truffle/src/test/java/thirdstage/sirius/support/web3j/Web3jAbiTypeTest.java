package thirdstage.sirius.support.web3j;

import java.util.List;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.datatypes.AbiTypes;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.generated.StaticArray3;
import org.web3j.abi.datatypes.generated.Uint200;

@TestInstance(Lifecycle.PER_CLASS)
public class Web3jAbiTypeTest{

  final private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Test
  public void testAbiTypesGetTypeWithUint() throws Exception{

    final Class<? extends Type> clazz = AbiTypes.getType("uint");
    Type<?> val = clazz.getConstructor(BigInteger.class).newInstance(BigInteger.valueOf(1000000));

    Assertions.assertTrue(val instanceof Uint);
  }

  @Test
  public void testAbiTypesGetTypeWithUint200() throws Exception{

    final Class<? extends Type> clazz = AbiTypes.getType("uint200");
    Type<?> val = clazz.getConstructor(BigInteger.class).newInstance(BigInteger.valueOf(1000000));

    Assertions.assertTrue(val instanceof Uint200);
  }


  @Test
  public void testStaticArrayUsingReflection() throws Exception{

    final int len = 3;

    final List<Uint> values = new ArrayList<>();
    values.add(new Uint200(BigInteger.valueOf(100)));
    values.add(new Uint200(BigInteger.valueOf(200)));
    values.add(new Uint200(BigInteger.valueOf(300)));

    final Class<? extends StaticArray> clazz =
        Class.forName("org.web3j.abi.datatypes.generated.StaticArray" + len)
        .asSubclass(StaticArray.class);

    final StaticArray arg = clazz.getConstructor(Class.class, List.class)
      .newInstance(AbiTypes.getType("uint"), values);

    this.logger.info("getTypeAsString: {}", arg.getTypeAsString());

    Assertions.assertTrue(arg instanceof StaticArray3);
    Assertions.assertEquals(3, arg.getValue().size());
    Assertions.assertTrue(arg.getValue().get(0) instanceof Uint200);

  }



}
