package com.azavea.gtfs

import com.github.nscala_time.time.Imports._
import com.azavea.gtfs.data.{RangeTripFrequency, TripRec, GtfsData}


/**
 * This object is responsible for generating trips tied to specific date using the GTFS trips as templates
 */
class TripMaster(data: GtfsData) {
  def apply(dt: LocalDate, service_id: ServiceId): Seq[Trip] = {
    for {
      t <- data.getTripsForService(service_id)
      freq <- data.getTripFrequency(t.trip_id)
    } yield {
      freq(t.stopTimes, dt).map { stopTimes =>
        Trip(
          t.trip_id,
          t.service_id,
          t.route_id,
          t.trip_headsign,
          stopTimes
        )
      }
    }
  }.flatten
}