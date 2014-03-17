package com.azavea.gtfs

import com.github.nscala_time.time.Imports._
import scala.Some
import scala.collection.mutable

case class TripRec(
  trip_id: TripId,
  service_id: ServiceId,
  route_id: RouteId,
  trip_headsign: String,
  stopTimes: Seq[StopTimeRec],
  frequency: Option[Frequency] = None
) {
  def apply(date: LocalDate): Stream[Trip] = {
    frequency match {
      case Some(frequency) =>
        val offset = stopTimes.head.arrival_time
        for (dt: LocalDateTime <- frequency.toStream(date))
        yield
          new Trip(
            rec = this,
            stops = stopTimes.view.map{ _.toStopTime(dt, offset) }
          )
      case None =>
        Stream(new Trip(
          rec = this,
          stops = stopTimes.view.map{ _.toStopTime(date)}
        ))
    }
  }

  /**
   * In order to be sameish two trips need to:
   * - belong to same service
   * - belong to same route
   * - have the same stops
   * - same travel time between stops
   * - same time at each stop

   * In general the two trips should be re-creatable from one trip and frequency record.
   */
  def sameish(that: TripRec): Boolean = {
    //This is a super-safe assumption, it is in fact possible to have two services that run on same days
    if (this.service_id != that.service_id) return false
    if (this.route_id != that.route_id) return false
    if (that.stopTimes.length != this.stopTimes.length) return false
    val l_start = this.stopTimes.head.arrival_time
    val r_start = that.stopTimes.head.arrival_time

    for ( (l,r) <- stopTimes zip that.stopTimes) {
      if (l.stop_id != r.stop_id) return false
      if (l.arrival_time - l_start != r.arrival_time - r_start) return false
      if (l.departure_time - l_start != r.departure_time - r_start) return false
    }
    true
  }
}

object TripRec {
  def bin(trips: Seq[TripRec]) = {
    val bins = mutable.Set(mutable.Set(trips.head))

    //go where you belong
    def belongs(t: TripRec): Boolean = {
      for (b <- bins) {
        if (t sameish b.head) {
          b += t
          return true
        }
      }
      false
    }

    for (t <- trips.tail) {
      if (! belongs(t)) {
        bins += mutable.Set(t) //You're in a class of you own!
      }
    }

    bins
  }
}