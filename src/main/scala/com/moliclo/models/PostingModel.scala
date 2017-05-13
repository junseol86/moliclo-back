package com.moliclo.models

import java.sql.Date
import javax.inject.Inject

import slick.jdbc.MySQLProfile._
import slick.driver.JdbcProfile
import slick.ast.BaseTypedType
import com.moliclo.utils.DatabaseConfig
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future


/**
  * Created by Hyeonmin on 2017-04-16.
  */

trait PostingModel extends DatabaseConfig {

  import driver.api._

  case class Posting(idx: Int, author: Int, title: String, p_from: Int, c_from: Int, p_to: Int, c_to: Int, sts: String, created: Date, modified: Date)
  val pageSize = 20

  class Postings(tag: Tag) extends Table[Posting](tag, "posting") {
    def idx = column[Int]("idx", O.PrimaryKey, O.AutoInc)
    def author = column[Int]("author")
    def title = column[String]("title")
    def p_from = column[Int]("p_from")
    def c_from = column[Int]("c_from")
    def p_to = column[Int]("p_to")
    def c_to = column[Int]("c_to")
    def sts = column[String]("sts")
    def created = column[Date]("modified")
    def modified = column[Date]("modified")

    def * = (idx, author, title, p_from, c_from, p_to, c_to, sts, created, modified) <> ((Posting.apply _).tupled, Posting.unapply)
  }


  val postings = TableQuery[Postings]
  def getPostings(page: Int): Future[Seq[Posting]] = db.run(postings.drop(page * pageSize).take(pageSize).sortBy(postings => postings.idx.desc).result)
//  def getPostings(page: Int) = db.run(postings.drop(page * pageSize).take(pageSize).sortBy(postings => postings.idx.desc).map(p => (p.author, p.idx)).result)
  def getPostingTotal: Future[Int] = db.run(postings.length.result)
}

