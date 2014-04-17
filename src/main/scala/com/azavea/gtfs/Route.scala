package com.azavea.gtfs

import com.azavea.gtfs.RouteType.RouteType

case class Route(
  id: RouteId,
  route_short_name: String,
  route_long_name: String,
  route_type: RouteType,
  agency_id: Option[String] = None,
  route_desc: Option[String] = None,
  route_url: Option[String] = None,
  route_color: Option[String] = None,
  route_text_color: Option[String] = None
)

object RouteType extends Enumeration {
  type RouteType = Value
  val Tram, Subway, Rail, Bus, Ferry, Cablecar, Gondola, Funicular  = Value
}
