package thirdstage.exercise.akka.wordanalysis.mappedrouter;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Generic/wrapper type for a key used to provide uniqueness and identify an object from others.
 *
 * @author Sangmoon Oh
 *
 * @param <T> the type of key core
 */
@Immutable
public class Key<T extends java.io.Serializable> implements java.io.Serializable{

   private static final long serialVersionUID = 1L;

   private final T key;

   public Key(@Nonnull T key){
      Validate.isTrue(key != null, "The key should be non-null value");
      this.key = key;
   }

   public T get(){ return this.key; }

   @Override
   public boolean equals(Object obj){
      if(obj == null) return false;
      if(!(obj instanceof Key)) return false;

      return EqualsBuilder.reflectionEquals(this.key, ((Key)obj).get());
   }

   @Override
   public int hashCode(){
      return HashCodeBuilder.reflectionHashCode(this.key);
   }

   @Override
   public String toString(){
      return this.key.toString();
   }

}
