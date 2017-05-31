package com.moliclo.models.v1

import java.sql.Date
import com.moliclo.utils.DatabaseConfig
import scala.concurrent.Future

/**
  * Created by Hyeonmin on 2017-05-25.
  */
trait CardModel extends DatabaseConfig {

  import driver.api._

  case class Card(idx: Int, posting: Int, card_type: String, cont_1: String, cont_2: Option[String], cont_3: Option[String], created: Date, updated: Date)

  class Cards(tag: Tag) extends Table[Card](tag, "mol_card") {
    def idx = column[Int]("idx", O.PrimaryKey, O.AutoInc)
    def posting = column[Int]("posting")
    def card_type = column[String]("card_type")
    def cont_1 = column[String]("cont_1")
    def cont_2 = column[Option[String]]("cont_2")
    def cont_3 = column[Option[String]]("cont_3")
    def created = column[Date]("created")
    def updated = column[Date]("updated")

    def * = (idx, posting, card_type, cont_1, cont_2, cont_3, created, updated) <> ((Card.apply _).tupled, Card.unapply)
  }

  val cards = TableQuery[Cards]

  def getCardInPostingTotal(posting: Int, last: Int): Future[Int] =
    db.run {
      cards
        .filter(card => card.posting === posting && (card.idx <= last || last == 0))
        .length
        .result
    }

  def getCards(posting: Int, page: Int, perPage: Int, last: Int): Future[Seq[Card]] = {
    db.run {
      cards
        .filter(card => card.posting === posting && (card.idx <= last || last == 0))
        .drop(page * perPage)
        .take(perPage)
        .result
    }
  }
}
