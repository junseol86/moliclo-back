package com.moliclo.models.v1

import java.sql.Date

import com.moliclo.utils.DatabaseConfig

import scala.concurrent.Future

/**
  * Created by Hyeonmin on 2017-05-13.
  */
trait UserModel extends DatabaseConfig {

  import driver.api._

  case class User(idx: Int, sns_id: String, user_name: String, thumbnail: String, sts: String, last_logged_in: Date, created: Date, updated: Date)

  class Users(tag: Tag) extends Table[User](tag, "mol_user") {
    def idx = column[Int]("idx", O.PrimaryKey, O.AutoInc)
    def sns_id = column[String]("sns_id")
    def user_name = column[String]("user_name")
    def thumbnail = column[String]("thumbnail")
    def sts = column[String]("sts")
    def last_logged_in = column[Date]("last_logged_in")
    def created = column[Date]("created")
    def updated = column[Date]("updated")

    def * = (idx, sns_id, user_name, thumbnail, sts, last_logged_in, created, updated) <> ((User.apply _).tupled, User.unapply)
  }

  val users = TableQuery[Users]
  def getUsers(page: Int): Future[Seq[User]] = db.run(users.sortBy(users => users.idx.desc).result)

}
