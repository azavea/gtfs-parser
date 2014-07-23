package com.azavea.gtfs

import org.scalatest._
import com.azavea.gtfs.data.{GtfsData, CsvParser}
import com.github.nscala_time.time.Imports._
import com.azavea.gtfs.slick.DAO
import scala.slick.jdbc.JdbcBackend._
import geotrellis.slick._
import geotrellis.proj4._
import geotrellis.vector.reproject._
import geotrellis.slick._

class PhillySpec extends FlatSpec with Matchers {

  val dao = new DAO
  val db = Database.forURL("jdbc:postgresql:gtfs", driver = "org.postgresql.Driver")

  "Philly" should "be able to move gtfs from file to DB" in {
      val data = GtfsData.fromFile("data/asheville")

      db withTransaction { implicit trans =>
        import dao.context._

        //The following requires profile to get the "delete" invokers
        import dao.profile.simple._
        dao.shapesTable.delete
        dao.calendarsTable.delete
        dao.calendarDatesTable.delete
        dao.stopTimesTable.delete
        dao.frequencyTable.delete
        dao.tripsTable.delete
        dao.routesTable.delete
        dao.agenciesTable.delete
        dao.stopsTable.delete

        data.agencies.foreach { agency => dao.agencies.insert(agency) }
        /**
         * WARNING: We have to wrap a geometry in a ProjectedLine so we can associate an SRID with it
         */
        data.shapes.foreach { shape =>
          val ns = shape.copy(
            line = shape.line.reproject(LatLng, WebMercator)(3857)
          )
          dao.shapes.insert(ns)
        }
        data.routes.foreach { route => dao.routes.insert(route) }
        data.stops.foreach { stop => dao.stops.insert(stop) }
        data.trips.foreach { trip => dao.trips.insert(trip) }
        data.service.foreach { service => dao.service.insert(service) }
      }
  }

  it should "be able to query with an innerJoin" in {
    db withSession { implicit s =>
      //need this line to call the .list invoker
      import dao.profile.simple._
      import dao.gis._

      val q = {
         for {
           trip <- dao.tripsTable
           route <- trip.route
         } yield (trip.id, route.id)
      }
      val list = q.list
      //println(q.selectStatement)
      //println(list)
      list should not be empty
    }
  }

  it should "be able to do groupBy queries like: max trip length by route" in {
    db withSession { implicit s =>
      //need this line to call the .list invoker
      import dao.profile.simple._
      import dao.gis._

      val q = {
        (for {
          t <- dao.tripsTable
          r <- t.route //this is possible because of foreignKey on Trips
          s <- t.shape
        } yield (r, s)).groupBy(_._1.id).map{case (rid, row) => (rid, row.map(_._2.geom.length).max)}
      }
      val list = q.list
      //println(q.selectStatement)
      //println(list)
      list should not be empty
    }
  }
}
