package thirdstage.exercise.akka.wordanalysis.mappedrouter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import akka.cluster.Member;

public interface NodeIdResolver{

   @Nullable
   public String resolveNodeId(@Nonnull Member member);

}
