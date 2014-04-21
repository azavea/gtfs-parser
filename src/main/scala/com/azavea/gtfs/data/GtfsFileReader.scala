package com.azavea.gtfs.data

import com.azavea.gtfs._
import com.github.nscala_time.time.Imports._


/**
 * Reads GTFS data from .txt files
 * @param dir directory containing the files
 */
class GtfsFileReader(dir:String) extends GtfsReader {
  override def getStops = {
    for (s <- CsvParser.fromPath(dir + "/stops.txt"))
    yield {
      println(s("stop_id"), s("stop_lat"))
      Stop(
        id = s("stop_id").get,
        stop_name = s("stop_name").get,
        stop_desc = s("stop_desc").get,
        stop_lat = s("stop_lat").get.toDouble,
        stop_lon = s("stop_lon").get.toDouble
      )
    }
  }


  override def getRoutes = {
    for (r <- CsvParser.fromPath(dir + "/routes.txt"))
    yield {
      Route(
        id = r("route_id").get,
        agency_id = r("agency_id"),
        route_short_name = r("route_short_name").get,
        route_long_name = r("route_long_name").get,
        route_desc = r("route_desc"),
        route_type = RouteType(r("route_type").get.toInt),
        route_url = r("route_url"),
        route_color = r("route_color"),
        route_text_color = r("route_text_color")
      )
    }
  }


  override def getStopTimes = {
    for (s <- CsvParser.fromPath(dir + "/stop_times.txt"))
    yield {
      StopTime(
        stop_id = s("stop_id").get,
        trip_id = s("trip_id").get,
        stop_sequence = s("stop_sequence").get.toInt,
        arrival_time = s("arrival_time").get,
        departure_time = s("departure_time").get,
        shape_dist_traveled = s("shape_dist_traveled").map(_.toDouble)
      )
    }
  }


  override def getTrips = {
    for (t <- CsvParser.fromPath(dir + "/trips.txt"))
    yield {
      Trip(
        id = t("trip_id").get,
        service_id = t("service_id").get,
        route_id = t("route_id").get,
        trip_headsign = t("trip_headsign"),
        stopTimes = Nil
      )
    }
  }


  def getCalendar = {
    for (c <- CsvParser.fromPath(dir + "/calendar.txt"))
    yield {
      ServiceCalendar(
        service_id = c("service_id").get,
        start_date = c("start_date").get,
        end_date = c("end_date").get,
        week = Array(
          c("monday").get == "1",
          c("tuesday").get == "1",
          c("wednesday").get == "1",
          c("thursday").get == "1",
          c("friday").get == "1",
          c("saturday").get == "1",
          c("sunday").get == "1"
        )
      )
    }
  }


  def getCalendarDates = {
    for (c <- CsvParser.fromPath(dir + "/calendar_dates.txt"))
    yield {
      ServiceException(
        service_id = c("service_id").get,
        date = c("date").get.toLocalDate,
        exception = if (c("exception_type") == "1") 'Add else 'Remove
      )
    }
  }


  override def getFrequencies = {
    try {
      for (f <- CsvParser.fromPath(dir + "/frequencies.txt"))
      yield {
        Frequency(
          trip_id = f("trip_id").get,
          start_time = f("start_time").get,
          end_time = f("end_time").get,
          headway = f("headway_secs").get.toInt.seconds
        )
      }
    }catch {
      case e: java.io.FileNotFoundException => Iterator.empty
    }
  }
}
