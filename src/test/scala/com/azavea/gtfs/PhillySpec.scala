package com.azavea.gtfs

import org.scalatest._
import com.azavea.gtfs.data.{GtfsData, CsvParser}
import com.github.nscala_time.time.Imports._
import com.azavea.gtfs.slick.DAO
import scala.slick.driver.PostgresDriver
import com.github.tototoshi.slick.PostgresJodaSupport
import scala.slick.jdbc.JdbcBackend._

class PhillySpec extends FlatSpec with Matchers {

  val dao = new DAO(PostgresDriver, PostgresJodaSupport)
  val db = Database.forURL("jdbc:postgresql:philly_gtfs", driver = "org.postgresql.Driver")

  "Philly" should "be able to move gtfs from file to DB" in {
      val data = GtfsData.fromFile("data/philly_rail")

      db withTransaction { implicit trans =>
        data.agencies.foreach { agency => dao.agencies.insert(agency) }
        data.routes.foreach { route => dao.routes.insert(route) }
        data.stops.foreach { stop => dao.stops.insert(stop) }
        data.trips.foreach { trip => dao.trips.insert(trip) }
        data.service.foreach { service => dao.service.insert(service) }
      }
  }
}
