package com.azavea.gtfs.data

import com.azavea.gtfs._
import com.github.nscala_time.time.Imports._

/**
 * Contains cleaned and indexed GTFS data
 * @param reader used to read in records during construction
 */
class GtfsData(reader: GtfsReader) {
  val stops: Map[StopId, Stop] =
    reader.getStops.map(s => s.stop_id -> s).toMap

  val stopTimes: Map[TripId, Seq[StopTimeRec]] =
    reader.getStopTimes.map(clean).toSeq.groupBy(_.trip_id)

  val frequencies: Map[TripId, Frequency] =
    reader.getFrequencies.map(f => f.trip_id -> f).toMap

  val trips: Array[TripRec] =
    reader.getTrips.map(clean).toArray

  val tripById: Map[TripId, TripRec] =
    trips.map(t => t.trip_id -> t).toMap

  val tripsByServiceId: Map[ServiceId, Array[TripRec]] =
    trips.groupBy(_.service_id).withDefaultValue(Array.empty)

  val calendar: Array[CalendarRec] =
    reader.getCalendar.toArray

  val calendarDates: Array[CalendarDateRec] =
    reader.getCalendarDates.toArray

  def clean(t: TripRec):TripRec = {
    val f = frequencies.get(t.trip_id)
    val st = stopTimes(t.trip_id).sortBy(_.stop_sequence)
    val s = Interpolator.interpolate(st.toArray)(GtfsData.StopTimeInterp)
    t.copy(stopTimes = s, frequency = f)
  }

  def clean(st: StopTimeRec): StopTimeRec = {
    st.copy(stop = stops(st.stop_id))
  }
}

object GtfsData {
  def fromFile(dir: String) =
    new GtfsData(new GtfsFileReader(dir))

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