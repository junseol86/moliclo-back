package com.moliclo.utils

trait DatabaseConfig {
  val driver = slick.jdbc.MySQLProfile

  import driver.api._

  // 아래를 val이 아닌 def로 하면 mysql에서 too many connections 에러가 난다
  val db = Database.forConfig("mysql_db")
  implicit val session: Session = db.createSession()
}
