package com.moliclo.http.routes.v1

import akka.http.scaladsl.server.Directives._
import com.moliclo.models.v1.{CardModel}
import org.json4s._
import org.json4s.native.{Json, Serialization}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Hyeonmin on 2017-05-28.
  */
trait CardRoute extends CardModel {

  implicit val cardResultFormats = Serialization.formats(NoTypeHints)

  var cardRoute = pathPrefix("cards") {

    pathEndOrSingleSlash {
      get {
        parameters('posting.as[Int], 'page.as[Int], 'per_page.as[Int], 'last.as[Int]) { (posting, page, perPage, last) =>
          complete(getCards(posting, page, perPage, last).map { cards =>
            Json(cardResultFormats).write(cards)
          })
        }
      }
    }
  }

}
