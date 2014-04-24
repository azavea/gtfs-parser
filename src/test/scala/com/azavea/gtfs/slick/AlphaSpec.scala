package com.azavea.gtfs.slick

import com.azavea.gtfs._
import org.scalatest._

import scala.slick.jdbc.JdbcBackend.{Database, Session}
import com.github.nscala_time.time.Imports._
import scala.slick.driver.PostgresDriver
import com.github.tototoshi.slick.PostgresJodaSupport
import com.azavea.gtfs.data.GtfsData

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
    }
  }

  it should "be able to insert a trip" in {
    val trip1 = Trip("T3","SR1","1",None,
      List(
        StopTime("1","T3", 1, 0.seconds, 1.minute),
        StopTime("10","T3", 2, 10.minutes, 11.minutes),
        StopTime("100","T3", 3, 15.minutes, 16.minutes)
      )
    )
    db withSession {
      implicit session: Session =>
        dao.trips.insert(trip1)
    }
  }

  it should "be able to pull trips on a given day from a service" in {
    db withSession { implicit s: Session =>
      import dao.context._
      val dt = new LocalDate(2014,1,5)
      val service = dao.service.full
      val ids = service.filter(_.activeOn(dt)).map(_.id)
      val trips = service.map(_.getTripsOn(dt)).flatten
      println(trips.length)
    }
  }
}
