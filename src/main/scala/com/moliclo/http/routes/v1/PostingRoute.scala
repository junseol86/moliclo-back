package com.moliclo.http.routes.v1

import akka.http.scaladsl.server.Directives._
import com.moliclo.models.v1.{CardModel, PostingModel, UserModel}
import org.json4s._
import org.json4s.native.{Json, Serialization}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Hyeonmin on 2017-04-22.
  */
trait PostingRoute extends PostingModel with CardModel with UserModel {

  implicit val formats = Serialization.formats(NoTypeHints)

  var postingRoute = pathPrefix("postings") {
//    참조: http://doc.akka.io/docs/akka-http/10.0.1/scala/http/routing-dsl/directives/path-directives/pathEndOrSingleSlash.html
    pathEndOrSingleSlash {
      get {
        //        http://localhost:9001/v1/posting?page=0&other=1
        parameters('page.as[Int], 'per_page.as[Int]) { (page, per_page) =>
          complete(getPostingTotal.map { total =>
            getPostings(page, per_page).map { postingList =>
              val postingListMapped = postingList.map { item =>
                postingForThumb(item)
              }
              var result = Map[String, Any]()
              val isLast = (page + 1) * per_page >= total
              result += "isLast" -> isLast
              result += "postingList" -> postingListMapped
              Json(formats).write(result)
            }
          })
        }
      }
    }
  } ~
  pathPrefix("postings" / IntNumber) { idx =>
    pathEndOrSingleSlash {
      get {
        parameters('last.as[Int]) { (last) =>
          complete(getAPosting(idx).map { posting =>
            val postingMap = postingForRead(posting(0))
            getCardInPostingTotal(idx, last).map { cards =>
              var result = Map[String, Any]()
              result += "posting" -> postingMap
              result += "card_total" -> cards
              Json(formats).write(result)
            }
          })
        }

      }
    }
  }

  var postingRouteTest = pathPrefix("test") {
    pathEndOrSingleSlash {
      get {
        //        http://localhost:9001/v1/posting?page=0&other=1
        parameters('page.as[Int], 'other.as[Int]) { (page, other) =>
          complete(
            "ho"
          )
        }
      }
    }
  }

}
