package thirdstage.exercise.akka.wordanalysis.mappedrouter;

import javax.annotation.Nonnull;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.routing.ActorSelectionRoutee;

public class KeyedActorSelectionRoutee<T extends java.io.Serializable> extends ActorSelectionRoutee implements KeyedRoutee<T>{

   private final Key<T> key;

   @Override
   public Key<T> getKey(){ return this.key; }

   public KeyedActorSelectionRoutee(@Nonnull T key, @Nonnull ActorSelection selection){
      super(selection);
      this.key = new Key<T>(key);

   }

   public KeyedActorSelectionRoutee(@Nonnull Key<T> key, @Nonnull ActorSelection selection){
      super(selection);
      this.key = key;
   }

   @Override
   public void send(Object message, ActorRef sender){
      super.send(message, sender);
   }

}
