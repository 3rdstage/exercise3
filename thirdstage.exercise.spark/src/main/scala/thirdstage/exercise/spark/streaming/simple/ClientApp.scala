package thirdstage.exercise.spark.streaming.simple

import java.net.ServerSocket
import java.net.Socket
import java.io.OutputStream
import java.io.PrintWriter
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Writer

object ClientApp {

  def main(args: Array[String]) {

    println("Defining new Socket")

    var os: OutputStream = null
    var out: java.io.PrintWriter = null
    var in: java.io.BufferedReader = null
    try {
      val server = new ServerSocket(9087)
      println("Waiting client")

      val client = server.accept()
      println("Connection accepted")

      os = client.getOutputStream()
      out = new PrintWriter(os, true)
      in = new BufferedReader(new InputStreamReader(System.in))
      while (true) {
        println("Waiting for user to input some data")
        var data = in.readLine()
        println("Data received and now writing it to socket.")
        out.println(data)
      }

    } catch {
      case ex:Exception => ex.printStackTrace(System.err)
    } finally {
      if (in != null) {
        try { in.close() } catch { case ex:Exception => }
      }
      if (out != null) {
        try { out.close() } catch{ case ex:Exception => }
      }
    }

  }

}