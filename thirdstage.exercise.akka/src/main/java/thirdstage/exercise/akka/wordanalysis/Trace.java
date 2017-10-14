package thirdstage.exercise.akka.wordanalysis;

import javax.annotation.concurrent.Immutable;
import javax.xml.bind.annotation.XmlElement;

@Immutable
public class Trace implements java.io.Serializable{

   private static final long serialVersionUID = 1L;

   private final String sentenceId;

   @XmlElement(name="sentence-id")
   public String getSentenceId(){ return this.sentenceId; }

   private final String sourceId;

   @XmlElement(name="source-id")
   public String getSourceId(){ return this.sourceId; }

   private final String systemAddress;

   @XmlElement(name="system-addr")
   public String getSystemAddress(){ return this.systemAddress; }

   private final int systemPort;

   @XmlElement(name="system-port")
   public int getSystemPort(){ return this.systemPort; }

   private final String timestamp;

   @XmlElement(name="timestamp")
   public String getTimeStamp(){ return this.timestamp; }

   public Trace(String sentenceId, String sourceId, String addr, int port, String timestamp){
      this.sentenceId = sentenceId;
      this.sourceId = sourceId;
      this.systemAddress = addr;
      this.systemPort = port;
      this.timestamp = timestamp;

   }






}
