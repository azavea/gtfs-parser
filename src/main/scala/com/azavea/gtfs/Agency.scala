package com.azavea.gtfs

case class Agency(
  id: Option[String],
  agency_name: String,
  agency_url: String,
  agency_timezone: String,
  agency_lang: Option[String] = None,
  agency_phone: Option[String] = None,
  agency_fare_url: Option[String] = None
)

