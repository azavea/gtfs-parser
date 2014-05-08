package com.azavea.gtfs

import geotrellis.feature._
import geotrellis.slick.ProjectedLine

case class TripShape(id: String, projectedLine: ProjectedLine)

