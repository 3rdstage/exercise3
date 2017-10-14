package thirdstage.exercise.akka.wordanalysis.mappedrouter;

import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import org.hibernate.validator.constraints.NotBlank;

public interface KeyNodeMap<T extends java.io.Serializable>{

   @Nonnull public Set<Key<T>> getKeys();

   public String getNodeId(@Nonnull Key<T> key);

   public String putNodeId(@Nonnull Key<T> key, @NotBlank String nodeId);

   public boolean isEmpty();

   public boolean containsKey(@Nonnull Key<T> key);

   public Set<Map.Entry<Key<T>, String>> getEntrySet();

}
