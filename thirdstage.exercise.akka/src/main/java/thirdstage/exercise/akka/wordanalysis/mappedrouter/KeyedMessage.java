package thirdstage.exercise.akka.wordanalysis.mappedrouter;

/**
 * @author Sangmoon Oh
 * @since 2016-06-16
 *
 * @param <K> type of key core
 * @param <M> type of message
 */
public interface KeyedMessage<K extends java.io.Serializable, M> extends Keyed<K>{

   M getMessage();

}
