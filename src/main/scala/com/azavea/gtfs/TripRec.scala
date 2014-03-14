package com.azavea.gtfs

import com.github.nscala_time.time.Imports._
import scala.Some

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
}
