package com.moliclo.http

import akka.http.scaladsl.server.Directives._
import com.moliclo.http.routes.PostingRoute
import com.moliclo.utils.CorsSupport

trait HttpService extends CorsSupport with PostingRoute {

  val routes =
    pathPrefix("v1") {
      corsHandler {
        postingRoute
      }
    }

}
