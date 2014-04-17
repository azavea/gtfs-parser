package com.azavea.gtfs.data

import com.azavea.gtfs._
import com.github.nscala_time.time.Imports._


/**
 * Reads GTFS data from .txt files
 * @param dir directory containing the files
 */
class GtfsFileReader(dir:String) extends GtfsReader {
  //TODO - This is a weird hack, really the parser should return Option[String] and required fields should just .get
  implicit class RichString(s: String){
    def asOpt = if (s == "") None else Some(s)
  }


  override def getStops = {
    for (s <- CsvParser.fromPath(dir + "/stops.txt"))
    yield {
      Stop(
        id = s("stop_id"),
        stop_name = s("stop_name"),
        stop_desc = s("stop_desc"),
        stop_lat = s("stop_lat").toDouble,
        stop_lon = s("stop_lon").toDouble
      )
    }
  }


  override def getRoutes = {
    for (r <- CsvParser.fromPath(dir + "/routes.txt"))
    yield {
      Route(
        id = r("route_id"),
        agency_id = r("agency_id").asOpt,
        route_short_name = r("route_short_name"),
        route_long_name = r("route_long_name"),
        route_desc = r("route_desc").asOpt,
        route_type = RouteType(r("route_type").toInt),
        route_url = r("route_url").asOpt,
        route_color = r("route_color").asOpt,
        route_text_color = r("route_text_color").asOpt
      )
    }
  }


  override def getStopTimes = {
    for (s <- CsvParser.fromPath(dir + "/stop_times.txt"))
    yield {
      StopTimeRec(
        stop_id = s("stop_id"),
        trip_id = s("trip_id"),
        stop_sequence = s("stop_sequence").toInt,
        arrival_time = s("arrival_time"),
        departure_time = s("departure_time"),
        shape_dist_traveled = s("shape_dist_traveled").toDouble
        //TODO -- stop = null
      )
    }
  }


  override def getTrips = {
    for (t <- CsvParser.fromPath(dir + "/trips.txt"))
    yield {
      TripRec(
        id = t("trip_id"),
        service_id = t("service_id"),
        route_id = t("route_id"),
        trip_headsign = t("trip_headsign").asOpt,
        stopTimes = Nil
      )
    }
  }


  def getCalendar = {
    for (c <- CsvParser.fromPath(dir + "/calendar.txt"))
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
  }


  def getCalendarDates = {
    for (c <- CsvParser.fromPath(dir + "/calendar_dates.txt"))
    yield {
      CalendarDateRec(
        service_id = c("service_id"),
        date = c("date"),
        exception = if (c("exception_type") == "1") 'Add else 'Remove
      )
    }
  }


  override def getFrequencies = {
    for (f <- CsvParser.fromPath(dir + "/frequencies.txt"))
    yield {
      Frequency(
        trip_id = f("trip_id"),
        start_time = f("start_time"),
        end_time = f("end_time"),
        headway = f("headway_secs").toInt.seconds
      )
    }
  }
}
