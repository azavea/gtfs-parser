package com.azavea.gtfs

import com.github.nscala_time.time.Imports._
import com.azavea.gtfs.data.{GtfsReader, GtfsFileReader, GtfsData}
import com.azavea.gtfs.RouteType.RouteType
import scala.collection.immutable.NumericRange
import com.github.nscala_time.time.Imports

/**
 * Transit system specified by GTFS
 */
class Gtfs(reader: GtfsReader) extends GtfsData(reader) {
  val cal = new Calendar(calendar, calendarDates)

  def getTripsOn(dt: LocalDate): Seq[Trip] = {
    for{
      service <- cal.getServiceFor(dt)
      generator <- tripsByService(service)
      trip <- generator(dt)
    } yield trip
  }

  /** Maximum number of stops in a trip in the route */
  def maxStopsForRoute(route: RouteId):  Int =
    tripsByRoute(route).maxBy(_.stopTimes.length).stopTimes.length

  def maxStopsByRoute(): Seq[(RouteId, Int)] =
    routes.map(r => r.id -> maxStopsForRoute(r.id))

  def maxStopsByMode(): Seq[(RouteType, Int)] = {
    routes
      .groupBy(_.route_type)
      .mapValues{ routes =>
        routes.map(r => maxStopsForRoute(r.id)).max
      }
      .toSeq
  }

  def tripsForPeriod(from: LocalDate, to: LocalDate): Seq[Trip] = ???
}

object Gtfs {
  def fromFile(dir: String): Gtfs =
    new Gtfs(new GtfsFileReader(dir))
}