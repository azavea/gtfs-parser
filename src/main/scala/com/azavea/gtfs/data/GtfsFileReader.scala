package com.azavea.gtfs.data

import com.azavea.gtfs._
import com.github.nscala_time.time.Imports._

/**
 * Reads GTFS data from .txt files
 * @param dir directory containing the files
 */
class GtfsFileReader(dir:String) extends GtfsReader {
  override def getStops = {
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
  }.toTraversable


  override def getRoutes: Traversable[Route] = {
    for (r <- Csv.fromPath(dir + "/routes.txt"))
    yield {
      Route(
        route_id = r("route_id"),
        agency_id = r("agency_id"),
        route_short_name = r("route_short_name"),
        route_long_name = r("route_long_name"),
        route_desc = r("route_desc"),
        route_type = RouteType(r("route_type").toInt),
        route_url = r("route_url"),
        route_color = r("route_color"),
        route_text_color = r("route_text_color")
      )
    }
  }.toTraversable


  override def getStopTimes = {
    def handleTime(s: String) = if (s == "") null else String2Duration(s)
    for (st <- Csv.fromPath(dir + "/stop_times.txt"))
    yield {
      StopTimeRec(
        stop = null,
        stop_id = st("stop_id"),
        trip_id = st("trip_id"),
        stop_sequence = st("stop_sequence").toInt,
        arrival_time = handleTime(st("arrival_time")),
        departure_time = handleTime(st("departure_time")),
        shape_dist_traveled = st.getOrElse("shape_dist_traveled","0").toDouble
      )
    }
  }.toTraversable


  override def getTrips = {
    for (t <- Csv.fromPath(dir + "/trips.txt"))
    yield {
      TripRec(
        trip_id = t("trip_id"),
        service_id = t("service_id"),
        route_id = t("route_id"),
        trip_headsign = t("trip_headsign"),
        stopTimes = Nil
      )
    }
  }.toTraversable


  def getCalendar = {
    for (c <- Csv.fromPath(dir + "/calendar.txt"))
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
  }.toTraversable


  def getCalendarDates = {
    for (c <- Csv.fromPath(dir + "/calendar_dates.txt"))
    yield {
      CalendarDateRec(
        service_id = c("service_id"),
        date = c("date"),
        exception = if (c("exception_type") == "1") 'Add else 'Remove
      )
    }
  }.toTraversable


  override def getFrequencies = {
    for (f <- Csv.fromPath(dir + "/frequencies.txt"))
    yield {
      Frequency(
        trip_id = f("trip_id"),
        start_time = f("start_time"),
        end_time = f("end_time"),
        headway = f("headway_secs").toInt.seconds
      )
    }
  }.toTraversable
}
