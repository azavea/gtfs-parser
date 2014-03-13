package com.azavea.gtfs

import com.github.nscala_time.time.Imports._

case class TripGenerator(
  trip: TripRec,
  frequency: Option[Frequency]
) {
  def apply(date: LocalDate): Stream[Trip] = {
    frequency match {
      case Some(frequency) =>
        val offset = trip.stopTimes.head.arrival_time
        for (dt: LocalDateTime <- frequency.toStream(date))
        yield
          new Trip(
            rec = trip,
            stops = trip.stopTimes.map{ _.toStopTime(dt, offset) }
          )
      case None =>
        Stream(new Trip(
          rec = trip,
          stops = trip.stopTimes.toStream.map{ _.toStopTime(date)}
        ))
    }
  }
}
