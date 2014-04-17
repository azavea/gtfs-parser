package com.azavea.gtfs

/**
 * Trip that is scheduled to happen on a concrete date and time
 *
 * @param rec trips GTFS record that was used to generate this trip
 * @param stops list of stop times that have been resolved to specific date and time
 */
class ScheduledTrip (
  val rec: Trip,
  val stops: Seq[StopDateTime]
) {
  def trip_id = rec.id
}

