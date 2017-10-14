package thirdstage.exercise.akka.wordanalysis.mappedrouter;

import org.testng.Assert;
import org.testng.annotations.Test;
import thirdstage.exercise.akka.wordanalysis.mappedrouter.Key;

public class KeyTest {

   @Test
   public void testEquals() {

      Key<String> k1 = new Key<String>("A");
      Key<String> k2 = new Key<String>("B");
      Key<String> k3 = new Key<String>("A");

      Assert.assertNotEquals(k1, k2);
      Assert.assertEquals(k1, k3);

   }

   @Test(expectedExceptions={IllegalArgumentException.class})
   public void testConsturctorWithNull(){
      Key<String> key = new Key<String>(null);
   }

   @Test
   public void testHashCode() {
      Key<String> k1 = new Key<String>("A");
      Key<String> k2 = new Key<String>("B");
      Key<String> k3 = new Key<String>("A");

      Assert.assertNotEquals(k1.hashCode(), k2.hashCode());
      Assert.assertEquals(k1.hashCode(), k3.hashCode());
   }
}
