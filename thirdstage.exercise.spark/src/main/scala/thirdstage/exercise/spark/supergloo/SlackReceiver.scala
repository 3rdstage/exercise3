package thirdstage.exercise.spark.supergloo

import org.apache.spark.streaming.receiver.Receiver
import org.apache.spark.storage.StorageLevel
import org.apache.spark.Logging
import org.jfarcand.wcs.WebSocket
import scalaj.http.Http
import org.jfarcand.wcs.TextListener
import scala.util.parsing.json.JSON



class SlackReceiver(token: String) extends Receiver[String](StorageLevel.MEMORY_ONLY) 
  with Runnable with Logging{
  
  private val slackUrl = "https://slack.com/api/rtm.start"
  
  @transient
  private var thread : Thread = _
  
  override def onStart(): Unit = {
    this.thread = new Thread(this)
    this.thread.start()   
  }
  
  override def onStop(): Unit = {
    this.thread.interrupt()
  }
  
  override def run():Unit = {
    this.receive()
  }
  
  private def receive(): Unit = {
    val ws = WebSocket().open(webSocketUrl())
    ws.listener(new TextListener{
      override def onMessage(msg: String){
        store(msg)
      }
    })
  }
  
  private def webSocketUrl(): String = {
    val resp = Http(this.slackUrl).param("token", token).asString.body
    JSON.parseFull(resp).get.asInstanceOf[Map[String, Any]].get("url").get.toString
  }
}