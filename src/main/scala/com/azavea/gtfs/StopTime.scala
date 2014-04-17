package com.azavea.gtfs

import com.github.nscala_time.time.Imports._

/**
 * A stop on trip at specific date and time
 * @param rec GTFS stoptime record used to generate this stop
 * @param arrival date and time of arrival
 * @param departure date and time of departure
 */
class StopTime(
  rec: StopTimeRec,
  val arrival: LocalDateTime,
  val departure: LocalDateTime
) {
  //TODO - def stop: Stop = rec.stop
  def stop_id: StopId = rec.stop_id
  def sequence: Int = rec.stop_sequence
}
