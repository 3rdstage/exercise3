package thirdstage.exercise.akka.wordanalysis.mappedrouter;

import akka.routing.Routee;

public interface KeyedRoutee<T extends java.io.Serializable> extends Keyed<T>, Routee{

}
