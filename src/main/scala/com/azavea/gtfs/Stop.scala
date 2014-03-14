package com.azavea.gtfs

import math._

case class Stop (
  stop_id: StopId,
  stop_name: String,
  stop_desc: String,
  stop_lat: Double,
  stop_lon: Double
) {

  /** Euclidean distance between stops*/
  def -(that: Stop): Double =
    sqrt(pow(this.stop_lat - that.stop_lat, 2) + pow(this.stop_lon - that.stop_lon, 2))
}

