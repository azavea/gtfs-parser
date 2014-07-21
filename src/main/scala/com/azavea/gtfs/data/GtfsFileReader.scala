package com.azavea.gtfs.data

import com.azavea.gtfs._
import com.github.nscala_time.time.Imports._
import geotrellis.feature._
import geotrellis.slick._


/**
 * Reads GTFS data from .txt files
 * @param dir directory containing the files
 */
class GtfsFileReader(dir:String) extends GtfsReader {

  override def getAgencies: Iterator[Agency] = {
    try {
      for (s <- CsvParser.fromPath(dir + "/agency.txt"))
      yield {
        Agency(
          id = s("agency_id"),
          agency_name = s("agency_name").get,
          agency_url = s("agency_url").get,
          agency_timezone = s("agency_timezone").get,
          agency_lang = s("agency_lang"),
          agency_phone = s("agency_phone"),
          agency_fare_url = s("agency_fare_url")
        )
      }
    }catch {
      case e: java.io.FileNotFoundException => Iterator.empty
    }
  }

  override def getStops = {
    for (s <- CsvParser.fromPath(dir + "/stops.txt"))
    yield {
      val lat = s("stop_lat").get.toDouble
      val lng = s("stop_lon").get.toDouble
      Stop(
        id = s("stop_id").get,
        stop_name = s("stop_name").get,
        stop_desc = s("stop_desc"),
        stop_lat = lat,
        stop_lon = lng,
        geom = Point(lng, lat).withSRID(4326)
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
        shape_dist_traveled = s("shape_dist_traveled").flatMap{ _.trim match {
          case "" => None //We can have a column, but no value, sad
          case s => Some(s.toDouble)
        }}
      )
    }
  }

  override def getShapes = {
    try {
      for (f <- CsvParser.fromPath(dir + "/shapes.txt"))
      yield {
        (
          f("shape_id").get,
          f("shape_pt_lat").get.toDouble,
          f("shape_pt_lon").get.toDouble,
          f("shape_pt_sequence").get.toInt
        )
      }
    }catch {
      case e: java.io.FileNotFoundException => Iterator.empty
    }
  }

  override def getTrips = {
    for (t <- CsvParser.fromPath(dir + "/trips.txt"))
    yield {
      Trip(
        id = t("trip_id").get,
        service_id = t("service_id").get,
        route_id = t("route_id").get,
        shape_id = t("shape_id"),
        trip_headsign = t("trip_headsign"),
        stopTimes = Nil
      )
    }
  }


  def getCalendar = {
    try { //calendar can be empty if calendar_dates.txt is present
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
    }catch {
      case e: java.io.FileNotFoundException => Iterator.empty
    }
  }

  def getCalendarDates = {
    try { //this file is optional
      for (c <- CsvParser.fromPath(dir + "/calendar_dates.txt"))
      yield {
        ServiceException(
          service_id = c("service_id").get,
          date = c("date").get,
          exception = if (c("exception_type") == "1") 'Add else 'Remove
        )
      }
    }catch {
      case e: java.io.FileNotFoundException => Iterator.empty
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
