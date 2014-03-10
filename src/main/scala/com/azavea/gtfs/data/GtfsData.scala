package com.azavea.gtfs.data

import com.azavea.gtfs._
import com.github.nscala_time.time.Imports._

class GtfsData(dir:String) {
  lazy val stops = {
    for (s <- Csv.fromPath(dir + "/stops.txt"))
    yield {
      Stop(
        stop_id = s("stop_id"),
        stop_name = s("stop_name"),
        stop_desc = s("stop_desc"),
        stop_lat = s("stop_lat").toDouble,
        stop_lon = s("stop_lon").toDouble
      )
    }
  }.toSeq
  lazy val stopsById = stops.groupBy(_.stop_id)


  lazy val stopTimes = {
    for (st <- Csv.fromPath(dir + "/stop_times.txt"))
    yield {
      StopTimeRec(
        stop_id = st("stop_id"),
        trip_id = st("trip_id"),
        stop_sequence = st("stop_sequence").toInt,
        arrival_time = st("arrival_time"),
        departure_time = st("departure_time")
      )
    }
  }.toSeq
  lazy val stopTimesByTripId = stopTimes.groupBy(_.trip_id)

  lazy val trips = {
    for (t <- Csv.fromPath(dir + "/trips.txt"))
    yield {
      TripRec(
        trip_id = t("trip_id"),
        service_id = t("service_id"),
        route_id = t("route_id"),
        trip_headsign = t("trip_headsign"),
        stopTimes = stopTimesByTripId(t("trip_id"))
      )
    }
  }.toSeq
  lazy val tripsById = trips.groupBy(_.trip_id)
  lazy val tripsByServiceId = trips.groupBy(_.service_id)

  /** calendars.txt */
  lazy val calendar = {
    for (c <- Csv.fromPath(dir + "/calendars.txt"))
    yield {
      CalendarRec(
        service_id = c("service_id"),
        start_date = c("start_date"),
        end_date = c("end_date"),
        week = Array(
          c("monday") == "1",
          c("tuesday") == "1",
          c("wednesday") == "1",
          c("thursday") == "1",
          c("friday") == "1",
          c("saturday") == "1",
          c("sunday") == "1"
        )
      )
    }
  }.toSeq
  def getCalendar() = calendar

  /** calendar_dates.txt */
  val exceptions = {
    for (c <- Csv.fromPath(dir + "/calendar_dates.txt"))
    yield {
      CalendarDateRec(
        service_id = c("service_id"),
        date = c("date"),
        exception = if (c("exception_type") == "1") 'Add else 'Remove
      )
    }
  }.toSeq
  def getCalendarDates() = exceptions


  lazy val frequencies = {
    for (f <- Csv.fromPath(dir + "/frequencies.txt"))
    yield {
      RangeTripFrequency(
        trip_id = f("trip_id"),
        start_time = f("start_time"),
        end_time = f("end_time"),
        headway = f("headway_secs").toInt.seconds
      )
    }
  }.toSeq
  lazy val frequenciesByTripId = frequencies.groupBy(_.trip_id)

  def getTrip(trip_id: String):TripRec =
    tripsById(trip_id).head

  def getTripsForService(service_id: String): Seq[TripRec] =
    tripsByServiceId(service_id)

  def getTripFrequency(trip_id: String): Seq[TripFrequency] =
    frequenciesByTripId.getOrElse(trip_id, List(UnitFrequency))
}
