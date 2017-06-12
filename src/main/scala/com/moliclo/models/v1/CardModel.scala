package com.moliclo.models.v1

import java.sql.Timestamp
import com.moliclo.utils.DatabaseConfig
import scala.concurrent.Future

/**
  * Created by Hyeonmin on 2017-05-25.
  */
trait CardModel extends DatabaseConfig with CommentModel {

  import driver.api._

  case class Card(idx: Int, posting: Int, card_type: String, cont_1: String, cont_2: Option[String], cont_3: Option[String], created: Timestamp, updated: Timestamp)

  class Cards(tag: Tag) extends Table[Card](tag, "mol_card") {
    def idx = column[Int]("idx", O.PrimaryKey, O.AutoInc)
    def posting = column[Int]("posting")
    def card_type = column[String]("card_type")
    def cont_1 = column[String]("cont_1")
    def cont_2 = column[Option[String]]("cont_2")
    def cont_3 = column[Option[String]]("cont_3")
    def created = column[Timestamp]("created")
    def updated = column[Timestamp]("updated")
    def * = (idx, posting, card_type, cont_1, cont_2, cont_3, created, updated) <> ((Card.apply _).tupled, Card.unapply)
  }

  val cards = TableQuery[Cards]
  val cards_x_comment_counts = for {
    (card, comment) <- cards join comments on (_.idx === _.card)
  } yield (card, comment)


  def cardInPosting(arg: (Int, Int, String, String, String, String, Timestamp, Int)): Map[String, Any] = {
    var result = Map[String, Any]()
    result += "idx" -> arg._1
    result += "posting" -> arg._2
    result += "card_type" -> arg._3
    result += "cont_1" -> arg._4
    result += "cont_2" -> arg._5
    result += "cont_3" -> arg._6
    result += "updated" -> arg._7.toString.replace(".0", "")
    result += "comment_count" -> arg._8
    result
  }

  def getCardInPostingTotal(posting: Int, last: Int): Future[Int] =
    db.run {
      cards
        .filter(card => card.posting === posting && (card.idx <= last || last == 0))
        .length
        .result
    }

  def getCards(posting: Int, page: Int, perPage: Int, last: Int): Future[Seq[(Int, Int, String, String, String, String, Timestamp, Int)]] = {
    db.run {
      sql"""SELECT
           card.idx, card.posting, card.card_type, card.cont_1, card.cont_2, card.cont_3, card.updated,
           COUNT(cmt.idx)
           AS count FROM mol_card card
           LEFT JOIN mol_comment cmt
           ON card.idx = cmt.card
           WHERE card.posting = ${posting}
           GROUP BY card.idx""".as[(Int, Int, String, String, String, String, Timestamp, Int)]
    }
  }

  //  SELECT card.*, COUNT(cmt.idx) AS COUNT FROM mol_card card LEFT JOIN mol_comment cmt ON card.idx = cmt.card GROUP BY card.idx
}
