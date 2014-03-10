package com.azavea.gtfs.data

case class Stop (
  stop_id: String,
  stop_name: String,
  stop_desc: String,
  stop_lat: Double,
  stop_lon: Double
)

