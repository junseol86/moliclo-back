package com.moliclo.models.v1

import java.sql.Date
import com.moliclo.utils.DatabaseConfig
import scala.concurrent.Future


/**
  * Created by Hyeonmin on 2017-04-16.
  */

trait PostingModel extends DatabaseConfig with UserModel {

  import driver.api._

  case class Posting(idx: Int, author: Int, title: String, thumbnail: Option[String], prev_post: Int, prev_card: Int, sts: String, created: Date, updated: Date)

  class Postings(tag: Tag) extends Table[Posting](tag, "mol_posting") {
    def idx = column[Int]("idx", O.PrimaryKey, O.AutoInc)
    def author = column[Int]("author")
    def title = column[String]("title")
    def thumbnail = column[Option[String]]("thumbnail")
    def prev_post = column[Int]("prev_post")
    def prev_card = column[Int]("prev_card")
    def sts = column[String]("sts")
    def created = column[Date]("created")
    def updated = column[Date]("updated")

    def * = (idx, author, title, thumbnail, prev_post, prev_card, sts, created, updated) <> ((Posting.apply _).tupled, Posting.unapply)
  }

  val postings = TableQuery[Postings]
  val postings_x_users = for {
    (p, u) <- postings join users on (_.author === _.idx)
  } yield (p, u)

  //  메인화면의 썸네일
  def postingForThumb(arg: (Posting, User)): Map[String, Any] = {
    var postingForThumbMap = Map[String, Any]()
    postingForThumbMap += "idx" -> arg._1.idx
    postingForThumbMap += "author" -> arg._1.author
    postingForThumbMap += "title" -> arg._1.title
    postingForThumbMap += "posting_thumbnail" -> arg._1.thumbnail
    postingForThumbMap += "updated" -> arg._1.updated.toString
    postingForThumbMap += "user_name" -> arg._2.user_name
    postingForThumbMap += "user_thumbnail" -> arg._2.thumbnail
    postingForThumbMap
  }

  // 포스팅의 컨텐츠를 읽을 때
  def postingForRead(arg: (Posting, User)): Map[String, Any] = {
    var postingForReadMap = Map[String, Any]()
    postingForReadMap += "idx" -> arg._1.idx
    postingForReadMap += "author" -> arg._1.author
    postingForReadMap += "title" -> arg._1.title
    postingForReadMap += "prev_post" -> arg._1.prev_post
    postingForReadMap += "prev_card" -> arg._1.prev_card
    postingForReadMap += "updated" -> arg._1.updated
    postingForReadMap += "user_name" -> arg._2.user_name
    postingForReadMap += "user_thumbnail" -> arg._2.thumbnail
    postingForReadMap
  }

  def getPostings(page: Int, perPage: Int): Future[Seq[(Posting, User)]] =
    db.run {
      postings_x_users
        .filter(_._1.sts === "O")
        .drop(page * perPage)
        .take(perPage)
        .sortBy(postings => postings._1.idx.desc)
        .result
    }
  def getPostingTotal: Future[Int] =
    db.run(postings.length.result)

  def getAPosting(idx: Int): Future[Seq[(Posting, User)]] =
    db.run {
      postings_x_users
        .filter(_._1.idx === idx)
        .result
    }

}

