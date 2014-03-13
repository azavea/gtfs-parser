package com.azavea.gtfs

/**
 * @param rec trips GTFS record that was used to generate this trip
 * @param stops list of stop times that have been resolved to specific date and time
 */
class Trip(
  val rec: TripRec,
  val stops: Seq[StopTime]
) {
  def trip_id = rec.trip_id
}

