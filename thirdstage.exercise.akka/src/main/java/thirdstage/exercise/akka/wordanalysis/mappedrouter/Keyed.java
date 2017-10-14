package thirdstage.exercise.akka.wordanalysis.mappedrouter;

import javax.annotation.Nonnull;

public interface Keyed<T extends java.io.Serializable>{

   @Nonnull public Key<T> getKey();

}
