package com.azavea.gtfs.slick

import com.azavea.gtfs._
import org.scalatest._

import com.github.tototoshi.slick.PostgresJodaSupport._



import scala.slick.jdbc.JdbcBackend.{Database, Session}
import scala.slick.driver.{JdbcProfile, PostgresDriver}
import com.github.tototoshi.slick.PostgresJodaSupport

class AlphaSpec extends FlatSpec with Matchers {

  val dao = new DAO(PostgresDriver, PostgresJodaSupport)
  val db = Database.forURL("jdbc:postgresql:chicago_gtfs", driver = "org.postgresql.Driver")

  "One" should "be able to get a trip record from the database" in {

    db withSession { implicit session: Session =>
      println("TRIP", dao.trips.getById("426070238825"))

      println("ROUTE", dao.routes.getById("111A"))
    }

  }

}
