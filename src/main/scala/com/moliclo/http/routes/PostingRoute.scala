package com.moliclo.http.routes

import akka.http.scaladsl.model.StatusCodes
import scala.concurrent.ExecutionContext.Implicits.global

import akka.http.scaladsl.server.Directives._
import com.moliclo.models.PostingModel

import org.json4s.native.{Json, Serialization}
import org.json4s._

/**
  * Created by Hyeonmin on 2017-04-22.
  */
trait PostingRoute extends PostingModel {

  implicit val formats = Serialization.formats(NoTypeHints)

  var postingRoute = pathPrefix("posting") {
    pathEndOrSingleSlash {
      get {
        //        http://localhost:9001/v1/posting?page=0&other=1
        parameters('page.as[Int], 'other.as[Int]) { (page, other) =>
          complete(getPostingTotal.map { total =>
              getPostings(page).map { postingList =>
                var result = Map[String, Any]()
                result += "total" -> total
                result += "postingList" -> postingList
                Json(formats).write(result)
              }
            })
        }
      }
    } ~
    pathPrefix(IntNumber) { id =>
      get {
        complete("Posting Id = " + id.toString)
      }
    }
  }

}
