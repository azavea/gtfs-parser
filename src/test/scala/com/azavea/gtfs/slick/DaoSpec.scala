package com.azavea.gtfs.slick

import com.azavea.gtfs._
import org.scalatest._

import scala.slick.jdbc.JdbcBackend.{Database, Session}
import com.github.nscala_time.time.Imports._
import scala.slick.driver.PostgresDriver
import com.github.tototoshi.slick.PostgresJodaSupport
import com.azavea.gtfs.data.GtfsData
import geotrellis.slick._

class DaoSpec extends FlatSpec with Matchers {
  val dao = new DAO
  val db = Database.forURL("jdbc:postgresql:gtfs", driver = "org.postgresql.Driver")

  "DAO" should "be able to insert a trip" in {
    val trip1 = Trip("T3","SR1","1",None,
      List(
        StopTime("1","T3", 1, 0.seconds, 1.minute),
        StopTime("10","T3", 2, 10.minutes, 11.minutes),
        StopTime("100","T3", 3, 15.minutes, 16.minutes)
      )
    )
    db withSession {
      implicit session: Session =>
        dao.trips.delete(trip1.id)
        dao.trips.insert(trip1)
    }
  }

  it should "be able to produce GtfsData" in {
    db withSession { implicit session: Session =>
      val data = dao.toGtfsData
      //we just inserted some up top, at least they should be there
      data.trips should not be empty
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
