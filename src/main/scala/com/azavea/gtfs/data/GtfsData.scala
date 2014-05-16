package com.azavea.gtfs.data

import com.azavea.gtfs._
import com.github.nscala_time.time.Imports._
import scala.collection.mutable
import com.azavea.gtfs.ServiceException
import com.azavea.gtfs.Stop
import com.azavea.gtfs.Trip
import com.azavea.gtfs.Route
import com.azavea.gtfs.StopTime
import com.azavea.gtfs.ServiceCalendar
import com.azavea.gtfs.Frequency
import java.util
import com.azavea.gtfs.util.{Interpolator, Interpolatable}
import geotrellis.feature._
import geotrellis.slick._

/**
 * Contains cleaned and indexed GTFS data
 * @param reader used to read in records during construction
 */
class GtfsData(reader: GtfsReader) {
  println("parsing agencies")
  val agencies = reader.getAgencies.toArray
  println("parsing shapes...")
  val shapes = {
    reader.getShapes.toList.groupBy{_._1}.map { case (k, t) =>
      val sorted = t.sortBy(_._4)
      val points = sorted.map{r => Point(r._3, r._2)}
      TripShape(k, Line(points).withSRID(4326))
    }
  }
  var shapesById = shapes.map(s => s.id -> s).toMap
  println("parsing stops")
  val stops =reader.getStops.toArray
  val stopsById = reader.getStops.map(s => s.id -> s).toMap
  println("parsing routes")
  val routes = reader.getRoutes.toArray
  println("parsing stop times...")
  val stopTimes = reader.getStopTimes.toArray
  println("grouping stop times...")
  val stopTimesByTrip = stopTimes.groupBy(_.trip_id)
  println("reading freqs...")
  val frequencies: Map[TripId, Frequency] =
    reader.getFrequencies.map(f => f.trip_id -> f).toMap
  println("parsing trips...")
  val trips: Array[Trip] =
    reader.getTrips.map(clean).toArray
  println("gouping trips by id ...")
  val tripById: Map[TripId, Trip] =
    trips.map(t => t.id -> t).toMap
  println("grouping trips by service...")
  val tripsByService: Map[ServiceId, Array[Trip]] =
    trips.groupBy(_.service_id).withDefaultValue(Array.empty)
  println("grouping trips by route...")
  val tripsByRoute: Map[ServiceId, Array[Trip]] =
    trips.groupBy(_.route_id).withDefaultValue(Array.empty)

  val service = {
    println("parsing calendar...")
    val calendars = reader.getCalendar.toArray
    println("parsing calendar dates ...")
    val calendarDates = reader.getCalendarDates.toArray

    Service.collate(calendars, calendarDates).toArray
  }

  def clean(t: Trip):Trip = {
    val f = frequencies.get(t.id)
    val st = stopTimesByTrip(t.id).sortBy(_.stop_sequence)
    val s = Interpolator.interpolate(st.toArray)(GtfsData.StopTimeInterp)
    t.copy(stopTimes = s, frequency = f)
  }

  def clean(st: StopTime): StopTime = {
    println(st.stop_id)
    st
  }
}

object GtfsData {
  implicit val StopTimeInterp = new Interpolatable[StopTime] {
    override def x(t1: StopTime): Double =
      t1.shape_dist_traveled.get

    override def y(t1: StopTime): Double =
      t1.arrival_time.getMillis.toDouble

    override def update(t: StopTime, x: Double): StopTime =
      t.copy(arrival_time = x.toInt.seconds, departure_time = x.toInt.seconds)

    override def missing(t: StopTime): Boolean =
      t.arrival_time == null //nulls are bad, must exterminate nulls
  }

  def fromFile(dir: String) = new GtfsData(new GtfsFileReader(dir))
}