package com.azavea.gtfs

import com.github.nscala_time.time.Imports._
import com.azavea.gtfs.data.{GtfsReader, GtfsFileReader, GtfsData}
import com.azavea.gtfs.RouteType.RouteType
import scala.collection.immutable.NumericRange
import com.github.nscala_time.time.Imports

/**
 * Transit system specified by GTFS
 */
class Gtfs(data: GtfsData) {
  val cal = new Service(data.calendar, data.calendarDates)

  def getTripsOn(dt: LocalDate): Seq[ScheduledTrip] = {
    for{
      service <- cal.getServiceFor(dt)
      generator <- data.tripsByService(service)
      trip <- generator(dt)
    } yield trip

    /**
     * This is not going to work like this anymore.
     *
     * You should be able to get this functionality out of a context
     */
  }

  /** Maximum number of stops in a trip in the route */
  def maxStopsForRoute(route: RouteId):  Int =
    data.tripsByRoute(route).maxBy(_.stopTimes.length).stopTimes.length

  def maxStopsByRoute(): Seq[(RouteId, Int)] =
    data.routes.map(r => r.id -> maxStopsForRoute(r.id))

  def maxStopsByMode(): Seq[(RouteType, Int)] = {
    data.routes
      .groupBy(_.route_type)
      .mapValues{ routes =>
        routes.map(r => maxStopsForRoute(r.id)).max
      }
      .toSeq
  }

  def tripsForPeriod(from: LocalDate, to: LocalDate): Seq[ScheduledTrip] = ???
}

