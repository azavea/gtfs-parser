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
  val stops: mutable.HashMap[StopId, Stop] = mutable.HashMap.empty
    reader.getStops.foreach(s => stops(s.stop_id) = s)
  println("parsed stops")
  val routes: Array[Route] =
    reader.getRoutes.toArray
  println("parsed routes")



  val shit = new mutable.ArrayBuffer[StopTimeRec](5000000)
  println("no ok!")
  reader.getStopTimes.foreach(s => shit += s)
  println("ok?")
  val stopTimes: Array[StopTimeRec] = shit.toArray

  println("parsed stop times")
  val frequencies: Map[TripId, Frequency] =
    reader.getFrequencies.map(f => f.trip_id -> f).toMap
  println("parsed freqs")
  val trips: Array[TripRec] =
    reader.getTrips.map(clean).toArray
  println("parsed trips")
  val tripById: Map[TripId, TripRec] =
    trips.map(t => t.trip_id -> t).toMap
  println("parsed trips by id")
  val tripsByService: Map[ServiceId, Array[TripRec]] =
    trips.groupBy(_.service_id).withDefaultValue(Array.empty)
  println("parsed trips by service id")
  val tripsByRoute: Map[ServiceId, Array[TripRec]] =
    trips.groupBy(_.route_id).withDefaultValue(Array.empty)
  println("parsed trips by route")
  val calendar: Array[CalendarRec] =
    reader.getCalendar.toArray
  println("parsed calendar")
  val calendarDates: Array[CalendarDateRec] =
    reader.getCalendarDates.toArray
  println("parsed calendar dates")

  def clean(t: TripRec):TripRec = {
    println("Clean Trip: " + t.trip_id)
    val f = frequencies.get(t.trip_id)
    val st = stopTimes.filter(_.trip_id == t.trip_id).sortBy(_.stop_sequence)
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