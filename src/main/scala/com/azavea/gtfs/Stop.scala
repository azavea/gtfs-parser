package com.azavea.gtfs

case class Stop (
  stop_id: StopId,
  stop_name: String,
  stop_desc: String,
  stop_lat: Double,
  stop_lon: Double
)

