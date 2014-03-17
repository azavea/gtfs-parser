package com.azavea.gtfs.data

import com.azavea.gtfs._
import com.github.nscala_time.time.Imports._
import scala.collection.mutable
import com.azavea.gtfs.CalendarDateRec
import com.azavea.gtfs.Stop
import com.azavea.gtfs.TripRec
import com.azavea.gtfs.Route
import com.azavea.gtfs.StopTimeRec
import com.azavea.gtfs.CalendarRec
import com.azavea.gtfs.Frequency
import java.util

/**
 * Contains cleaned and indexed GTFS data
 * @param reader used to read in records during construction
 */
class GtfsData(reader: GtfsReader) {
  println("parsing stops")
  val stops =reader.getStops.map(s => s.stop_id -> s).toMap
  println("parsing routes")
  val routes = reader.getRoutes.toSeq
  println("parsing stop times...")
  val stopTimes = reader.getStopTimes.toSeq
  println("grouping stop times...")
  val stopTimesByTrip = stopTimes.groupBy(_.trip_id)
  println("reading freqs...")
  val frequencies: Map[TripId, Frequency] =
    reader.getFrequencies.map(f => f.trip_id -> f).toMap
  println("parsing trips...")
  val trips: Array[TripRec] =
    reader.getTrips.map(clean).toArray
  println("gouping trips by id ...")
  val tripById: Map[TripId, TripRec] =
    trips.map(t => t.trip_id -> t).toMap
  println("grouping trips by service...")
  val tripsByService: Map[ServiceId, Array[TripRec]] =
    trips.groupBy(_.service_id).withDefaultValue(Array.empty)
  println("grouping trips by route...")
  val tripsByRoute: Map[ServiceId, Array[TripRec]] =
    trips.groupBy(_.route_id).withDefaultValue(Array.empty)
  println("parsing calendar...")
  val calendar: Array[CalendarRec] =
    reader.getCalendar.toArray
  println("parsing calendar dates ...")
  val calendarDates: Array[CalendarDateRec] =
    reader.getCalendarDates.toArray

  def clean(t: TripRec):TripRec = {
    val f = frequencies.get(t.trip_id)
    val st = stopTimesByTrip(t.trip_id).sortBy(_.stop_sequence)
    val s = Interpolator.interpolate(st.toArray)(GtfsData.StopTimeInterp)
    t.copy(stopTimes = s, frequency = f)
  }

  def clean(st: StopTimeRec): StopTimeRec = {
    println(st.stop_id)
    st.copy(stop = stops(st.stop_id))
  }
}

object GtfsData {
  implicit val StopTimeInterp = new Interpolatable[StopTimeRec] {
    override def x(t1: StopTimeRec): Double =
      t1.shape_dist_traveled

    override def y(t1: StopTimeRec): Double =
      t1.arrival_time.getMillis.toDouble

    override def update(t: StopTimeRec, x: Double): StopTimeRec =
      t.copy(arrival_time = x.toInt.seconds, departure_time = x.toInt.seconds)

    override def missing(t: StopTimeRec): Boolean =
      t.arrival_time == null //nulls are bad, must exterminate nulls
  }
}