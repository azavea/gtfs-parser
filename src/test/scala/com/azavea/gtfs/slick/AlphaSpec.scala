package com.azavea.gtfs.slick

import com.azavea.gtfs._
import org.scalatest._

import scala.slick.jdbc.JdbcBackend.{Database, Session}
import scala.slick.driver.PostgresDriver
import com.github.tototoshi.slick.PostgresJodaSupport

class AlphaSpec extends FlatSpec with Matchers {
  val dao = new DAO(PostgresDriver, PostgresJodaSupport)
  val db = Database.forURL("jdbc:postgresql:chicago_gtfs", driver = "org.postgresql.Driver")

  "One" should "be able to get a trip record from the database" in {
    db withSession { implicit session: Session =>
      dao.trips("426070238825")
      dao.routes("111A")
      dao.service.full

      val trips = dao.trips
      val trip = trips("426070238825")

      dao.routes("111A").get
    }
  }

  it should "find context convenient to use" in {
    
    db withSession { implicit session: Session =>
      import dao.context._  
      
      val route = dao.routes("111A").get

      val trips = route.getTrips

      println(trips)

    }
  }
}
