package thirdstage.exercise.akka.wordstats;

import javax.annotation.concurrent.Immutable;

@Immutable
public class StatsRequest{

   private final String user;

   private final String text;

   private final String date;

   public StatsRequest(String user, String text, String date){
      this.user = user;
      this.text = text;
      this.date = date;
   }

   public String getUser(){ return this.user; }

   public String getText(){ return this.text; }

   public String getDate(){ return this.date; }

}
