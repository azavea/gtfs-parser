package com.azavea.gtfs

import math._
import geotrellis.slick._
import geotrellis.vector._

case class Stop (
  id: String,
  stop_name: String,
  stop_desc: Option[String],
  stop_lat: Double,
  stop_lon: Double,
  geom: Projected[Point]
) {

  /** Euclidean distance between stops*/
  def -(that: Stop): Double =
    sqrt(pow(this.stop_lat - that.stop_lat, 2) + pow(this.stop_lon - that.stop_lon, 2))
}

