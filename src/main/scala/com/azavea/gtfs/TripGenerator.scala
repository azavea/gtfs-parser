package com.azavea.gtfs

import com.github.nscala_time.time.Imports._

/**
 * Tuple of Trip and Frequency record,
 * can be asked to generate a trip or sequence of trips on specific date
 *
 * In case where Frequency is None, it will generate a sequence of One.
 */
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
            stops = trip.stopTimes.view.map{ _.toStopTime(dt, offset) }
          )
      case None =>
        Stream(new Trip(
          rec = trip,
          stops = trip.stopTimes.view.map{ _.toStopTime(date)}
        ))
    }
  }
}
