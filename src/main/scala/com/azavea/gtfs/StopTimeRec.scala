package com.azavea.gtfs

import com.github.nscala_time.time.Imports._

case class StopTimeRec(
  stop_id: String,
  trip_id: TripId,
  stop_sequence: Int,
  arrival_time: Duration,
  departure_time: Duration,
  shape_dist_traveled: Double = 0,
  stop: Stop = null
) {
  /** Use given date to calucate arrival and departure time */
  def toStopTime(dt: LocalDateTime, offset: Duration = 0.seconds): StopTime = {
    new StopTime(this,
      dt + arrival_time - offset,
      dt + departure_time - offset
    )
  }

  def toStopTime(dt: LocalDate): StopTime =
    this.toStopTime(dt.toLocalDateTime(LocalTime.Midnight))

  /**
   * Travel time between departure from this stop and arrival at the other
   * Warning: This relationship is not symmetric and only valid for stops on the same trip
   */
  def timeTo(that: StopTimeRec): Duration =
    that.arrival_time - this.departure_time
}