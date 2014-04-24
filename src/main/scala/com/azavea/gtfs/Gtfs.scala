package com.azavea.gtfs

import com.github.nscala_time.time.Imports._
import com.azavea.gtfs.data.{GtfsReader, GtfsFileReader, GtfsData}
import com.azavea.gtfs.RouteType.RouteType
import scala.collection.immutable.NumericRange
import com.github.nscala_time.time.Imports

/**
 * Transit system specified by GTFS
 * This class is going to be gone soon, repalced by some kind of DAO like interface into GtfsData
 */
class Gtfs(data: GtfsData) {
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

