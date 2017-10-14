package thirdstage.exercise.akka.wordanalysis.mappedrouter;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import akka.cluster.Member;

public class RoleBaseNodeIdResolver implements NodeIdResolver{

   @Override
   public String resolveNodeId(Member member){
      // TODO Auto-generated method stub
      return null;
   }

}
