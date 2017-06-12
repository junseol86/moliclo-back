package com.moliclo.models.v1

import java.sql.Timestamp
import com.moliclo.utils.DatabaseConfig
import scala.concurrent.Future

/**
  * Created by Hyeonmin on 2017-06-11.
  */
trait CommentModel extends DatabaseConfig with UserModel {

  import driver.api._

  case class Comment(idx: Int, author: Int, posting: Int, card: Int, content: String, created: Timestamp)

  class Comments(tag: Tag) extends Table[Comment](tag, "mol_comment") {
    def idx = column[Int]("idx", O.PrimaryKey, O.AutoInc)
    def author = column[Int]("author")
    def posting = column[Int]("posting")
    def card = column[Int]("card")
    def content = column[String]("content")
    def created = column[Timestamp]("created")

    def * = (idx, author, posting, card, content, created) <> ((Comment.apply _).tupled, Comment.unapply)
  }

  val comments = TableQuery[Comments]

}
