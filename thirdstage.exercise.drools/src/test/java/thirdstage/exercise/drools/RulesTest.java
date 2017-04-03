package thirdstage.exercise.drools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import thirdstage.exercise.drools.eshop.model.Item;
import thirdstage.exercise.drools.eshop.model.Item.Category;

@RunWith(JUnitPlatform.class)
public class RulesTest{
  
  private static KieServices kieServices;
  
  private static KieContainer kieContainer;
  
  @BeforeAll
  public static void beforeAll(){
    kieServices = KieServices.Factory.get();
    kieContainer = kieServices.getKieClasspathContainer();
  }
  
  
  @DisplayName("1 + 1 = 2")
  @Test
  public void testSimplePlus(){
    
    Assertions.assertEquals(2, 1 + 1);
  }
  
  
  @DisplayName("Test rule to set item category.")
  @Test
  public void testLowCostRule(){
    
    KieSession kSess = kieContainer.newKieSession();
    
    Item item = new Item("A", 123.0);
    Assertions.assertNull(item.getCategory());
    kSess.insert(item);
    int fired = kSess.fireAllRules();
    
    Assertions.assertTrue(fired > 0);
    Assertions.assertEquals(Category.LOW_RANGE, item.getCategory());
  }
  

}
