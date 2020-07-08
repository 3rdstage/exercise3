package thirdstage.sirius.support.web3j;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;

/**
*
* Test ABI encoding and decoding of {@code Web3j}.
*
* Note that this is not Spring test
*
* @author Sangmoon Oh
*
*/
@SuppressWarnings({"unchecked", "rawtypes"})
@TestInstance(Lifecycle.PER_CLASS)
public class Web3jAbiEncodingTest{

  final private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Test
  public void testEventSignatureEncoding1() throws Exception{

    //event Minted(address indexed receiver, uint amount);
    final String signature = "Minted(address,uint256)";


    // Encoded event signature for 'Minted(address,uint256)'
    // JavaScript : web3.eth.abi.encodeEventSignature('Minted(address,uint256)')
    final String expected = "0x30385c845b448a36257a6a1716e6ad2e1bc2cbe333cde1e69fe849ad6511adfe";



    final TypeReference<?> t1 = TypeReference.create(Address.class, true);
    final TypeReference<?> t2 = TypeReference.create(Uint.class, false);

    final List<TypeReference<?>> types = new ArrayList<>();
    types.add(t1);
    types.add(t2);
    final Event ev = new Event("Minted", types);

    final String encodedEv = EventEncoder.encode(ev);
    final String encodedEv2 = EventEncoder.buildEventSignature(signature);

    this.logger.info("Encoded event signature:{}", encodedEv);

    Assertions.assertEquals(expected, encodedEv);
    Assertions.assertEquals(expected, encodedEv2);
  }

  @Test
  public void testEventSignatureEncoding2() throws Exception{

    //Example in Solidity official documentation : https://web3js.readthedocs.io/en/v1.2.7/web3-eth-abi.html#encodeeventsignature
    final String signature = "myEvent(uint256,bytes32)";

    // JavaScript : web3.eth.abi.encodeEventSignature('myEvent(uint256,bytes32)')
    final String expected = "0xf2eeb729e636a8cb783be044acf6b7b1e2c5863735b60d6daae84c366ee87d97";


    final TypeReference<?> t1 = TypeReference.create(Uint.class, false);
    final TypeReference<?> t2 = TypeReference.create(Bytes32.class, false);

    final List<TypeReference<?>> types = new ArrayList<>();
    types.add(t1);
    types.add(t2);
    final Event ev = new Event("myEvent", types);

    final String encodedEv = EventEncoder.encode(ev);
    final String encodedEv2 = EventEncoder.buildEventSignature(signature);

    this.logger.info("Encoded event signature:{}", encodedEv);

    Assertions.assertEquals(expected, encodedEv);
    Assertions.assertEquals(expected, encodedEv2);
  }


  /**
   * @throws Exception
   *
   * @see <a href='https://etherscan.io/tx/0x1e78f0b9d322ecad9863a12622f12c464d8876d0d5e43052185f1a0b77e3599c#eventlog'>0x06012c8cf97bead5deae237070f9587f8e7a266d</a>
   */
  @Test
  @DisplayName("Test ABI decoding using a event in tx '0x06012c8cf97bead5deae237070f9587f8e7a266d' in main net")
  public void testEventDecoding1() throws Exception{

    // The 1st event value of tx '0x06012c8cf97bead5deae237070f9587f8e7a266d' in main net
    // The event signature is : Birth (address owner, uint256 kittyId, uint256 matronId, uint256 sireId, uint256 genes)
    // Refere https://etherscan.io/tx/0x1e78f0b9d322ecad9863a12622f12c464d8876d0d5e43052185f1a0b77e3599c#eventlog

    final String data = "0x0000000000000000000000002f261a227480b7d1802433d05a92a27bab64503200000000000000000000000000000000000000000000000000000000001d46f90000000000000000000000000000000000000000000000000000000000101cfd000000000000000000000000000000000000000000000000000000000006497600004ad6841167304204214e38c4379cc1521ad7040128021388e84a9486bdad";

    final String v0 = "0x2f261a227480b7d1802433d05a92a27bab645032";
    final BigInteger v1 = BigInteger.valueOf(1918713);
    final BigInteger v2 = BigInteger.valueOf(1055997);
    final BigInteger v3 = BigInteger.valueOf(412022);
    final BigInteger v4 = new BigInteger("516512566630984113502851165067145229289269793217341920098813935041822125");



    final List<TypeReference<Type>> types = new ArrayList<>();
    types.add(TypeReference.makeTypeReference("address"));
    types.add(TypeReference.makeTypeReference("uint256"));
    types.add(TypeReference.makeTypeReference("uint256"));
    types.add(TypeReference.makeTypeReference("uint256"));
    types.add(TypeReference.makeTypeReference("uint256"));

    final List<Type> output = FunctionReturnDecoder.decode(data, types);

    Assertions.assertEquals(((Address)(output.get(0))).getValue(), v0);
    Assertions.assertEquals(((Uint256)(output.get(1))).getValue(), v1);
    Assertions.assertEquals(((Uint256)(output.get(2))).getValue(), v2);
    Assertions.assertEquals(((Uint256)(output.get(3))).getValue(), v3);
    Assertions.assertEquals(((Uint256)(output.get(4))).getValue(), v4);

  }

}
