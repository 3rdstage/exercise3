package thirdstage.exercise.akka.wordanalysis.mappedrouter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import akka.cluster.Member;

/**
 * @author Sangmoon Oh
 * @since 2016-05-30
 */
public class PortBaseNodeIdResolver implements NodeIdResolver{

   //@TODO Consider more to set member parameter nullable
   @Override @Nullable
   public String resolveNodeId(@Nonnull Member member){
      Validate.isTrue(member != null, "Member shouldn't be null.");

      String id = null;
      if(!(member.address().port().isEmpty())){
         id = String.valueOf(member.address().port().get());
      }

      return id;
   }

}
