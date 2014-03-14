package com.azavea.gtfs

import com.azavea.gtfs.RouteType.RouteType

case class Route(
  route_id: RouteId,
  route_short_name: String,
  route_long_name: String,
  route_type: RouteType,
  agency_id: AgencyId = "",
  route_desc: String = "",
  route_url: String = "",
  route_color: String = "",
  route_text_color: String =""
)

object RouteType extends Enumeration {
  type RouteType = Value
  val Tram, Subway, Rail, Bus, Ferry, Cablecar, Gondola, Funicular  = Value
}
