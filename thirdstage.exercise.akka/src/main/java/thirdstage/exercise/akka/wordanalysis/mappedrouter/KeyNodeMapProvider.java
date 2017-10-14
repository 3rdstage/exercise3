package thirdstage.exercise.akka.wordanalysis.mappedrouter;

import javax.annotation.Nonnull;

/**
 * @author Sangmoon Oh
 * @since 2016-06-02
 */
public interface KeyNodeMapProvider<T extends java.io.Serializable>{

   @Nonnull KeyNodeMap<T> getKeyNodeMap();

}
