package com.azavea.gtfs

import com.github.nscala_time.time.Imports._

/**
 * Represents a stop time in a sequence of stops in a trip
 * @param arrival_time arrival time as offset from midnight on a given day
 * @param departure_time departure time as offset from midnight on a given day
 * @param shape_dist_traveled how much of the trip LineString has been travelled
 */
case class StopTime(
  stop_id: String,
  trip_id: String,
  stop_sequence: Int,
  arrival_time: Period,
  departure_time: Period,
  shape_dist_traveled: Double = 0
) {
  /** Use given date to calculate arrival and departure time */
  def toStopTime(dt: LocalDateTime, offset: Period = 0.seconds): StopDateTime = {
    new StopDateTime(this,
      dt + arrival_time - offset,
      dt + departure_time - offset
    )
  }

  def toStopTime(dt: LocalDate): StopDateTime =
    this.toStopTime(dt.toLocalDateTime(LocalTime.Midnight))

  /**
   * Travel time between departure from this stop and arrival at the other
   * Warning: This relationship is not symmetric and only valid for stops on the same trip
   */
  def timeTo(that: StopTime): Period = {
    if (that.trip_id != trip_id) throw new IllegalArgumentException("Can't compare stop times on different trips")
    that.arrival_time - this.departure_time
  }
}