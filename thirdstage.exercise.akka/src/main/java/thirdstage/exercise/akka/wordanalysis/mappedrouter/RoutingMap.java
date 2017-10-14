package thirdstage.exercise.akka.wordanalysis.mappedrouter;

import java.util.Set;
import javax.annotation.Nonnull;
import org.hibernate.validator.constraints.NotBlank;


//@TODO Consider to define RoutingMapBuilder to make the implementations to be immutable
/**
 * @author Sangmoon Oh
 *
 * @param <T>
 */
@Deprecated
public interface RoutingMap<T extends java.io.Serializable>{

   @Nonnull public Set<Key<T>> getKeys();

   public boolean containsKey(@Nonnull Key<T> key);

   public String getPath(@Nonnull Key<T> key);

   public String putPath(@Nonnull Key<T> key, @NotBlank String path);


}
