package com.moliclo

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.moliclo.http.HttpService

object ApplicationMain extends App with HttpService {
  implicit val system = ActorSystem("moliclo-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val port = 9001
  println(s"Server online at http://localhost:$port/\nPress RETURN to stop...")
  Http().bindAndHandle(routes, "0.0.0.0", port)
}