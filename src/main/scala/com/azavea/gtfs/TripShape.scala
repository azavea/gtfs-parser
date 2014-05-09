package com.azavea.gtfs

import geotrellis.feature._
import geotrellis.slick.Projected

case class TripShape(id: String, line: Projected[Line])

