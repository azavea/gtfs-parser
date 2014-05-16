package com.azavea.gtfs

import com.github.nscala_time.time.Imports._
import scala.Some
import scala.collection.mutable
import scala.collection.mutable.{ListBuffer, ArrayBuffer}
import com.azavea.gtfs.util.{Run, RunLength}
import geotrellis.feature._

/**
 * An abstract trip, detailing the sequence and time of the stops but not the date
 *
 * @param service_id service calendar to which this trip is subject
 * @param route_id route to which this trip belongs
 * @param trip_headsign
 * @param stopTimes sequences of stops and times of thos stops
 * @param frequency contains information how to repeat this trip in a given day
 */
case class Trip (
  id: String,
  service_id: ServiceId,
  route_id: RouteId,
  trip_headsign: Option[String],
  stopTimes: Seq[StopTime],
  frequency: Option[Frequency] = None,
  shape_id: Option[String] = None
) {
  def apply(date: LocalDate): Stream[ScheduledTrip] = {
    frequency match {
      case Some(frequency) =>
        val offset = stopTimes.head.arrival_time
        for (dt: LocalDateTime <- frequency.toStream(date))
        yield
          new ScheduledTrip(
            rec = this,
            stops = stopTimes.view.map{ _.toStopDateTime(dt, offset) }
          )
      case None =>
        Stream(new ScheduledTrip(
          rec = this,
          stops = stopTimes.view.map{ _.toStopDateTime(date)}
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
  def sameish(that: Trip): Boolean = {
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

object Trip {
  def bin(trips: Seq[Trip]): Array[Array[Trip]] = {
    val bins = ArrayBuffer(ArrayBuffer(trips.head))

    //go where you belong
    def belongs(t: Trip): Boolean = {
      for (b <- bins) {
        //sameishnes is transitive, so this works
        if (t sameish b.head) {
          b += t
          return true
        }
      }
      false
    }

    for (t <- trips.tail) {
      if (! belongs(t)) {
        bins += ArrayBuffer(t) //You're in a class of you own!
      }
    }

    bins.map(_.toArray).toArray
  }

  /**
   * Expresses trips with frequency when they are repeated
   * @param trips
   * @param threshold Number of trips repated with predictable headway before compressing
   * @return
   */
  def compress(trips: Seq[Trip], threshold: Int = 2): Array[Trip] = {
   println("Binning trips...")
   var compressed:Long = 0

   val bins = bin(trips)
   val ret = ArrayBuffer.empty[Trip]
    for (bin <- bins.map(_.sortBy(_.stopTimes.head.arrival_time.millis))) {
      if (bin.length < threshold) {
        ret ++= bin
      }else{
        val headways =
        {
          for( Array(a,b) <- bin.sliding(2) )
          yield b.stopTimes.head.arrival_time - a.stopTimes.head.arrival_time
        }.toList

        for (rl <- RunLength((headways.head :: headways) zip bin){(c1,c2) => c1._1 == c2._1}) {
          rl match {
            case Run(1, List((headway, trip))) =>
              ret += trip
            case Run(x, list) =>
              val min_time = list.map(_._2.stopTimes.head.arrival_time.millis).min
              val max_time = list.map(_._2.stopTimes.head.arrival_time.millis).max
              val model = list.head._2
              val headway = list.head._1
              val newTrip = model.copy(id = model.id + "+", frequency =
                Some(Frequency(model.id, Period.millis(min_time), Period.millis(max_time), headway.toStandardDuration)))
              ret += newTrip

              compressed += list.length - 1
          }
        }
      }
    }
    println("Total Compressed: " + compressed)
    ret.toArray
  }
}