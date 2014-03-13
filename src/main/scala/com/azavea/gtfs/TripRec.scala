package com.azavea.gtfs

case class TripRec(
  trip_id: TripId,
  service_id: ServiceId,
  route_id: RouteId,
  trip_headsign: String,
  stopTimes: Seq[StopTimeRec]
)
