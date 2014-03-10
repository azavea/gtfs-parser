package com.azavea.gtfs.data

import com.github.nscala_time.time.Imports._
import com.azavea.gtfs.StopTime

case class StopTimeRec(
  stop_id: String,
  trip_id: String,
  stop_sequence: Int,
  arrival_time: Option[Duration],
  departure_time: Option[Duration]
) {

  /** Use given arrival date-time and calculate departure based on the difference of arrival_time and departure_time */
  def toStopTime(arrive: LocalDateTime) = {
    val (new_arrival, new_departure) =
      (arrival_time, departure_time) match {
        case (Some(arrival), Some(departure)) =>
          Some(arrive) -> Some(arrive + (departure - arrival))

        case _ =>
          None -> None
      }

    StopTime(stop_id, new_arrival, new_departure)
  }

  /** Use given date to calucate arrival and departure time */
  def toStopTime(dt: LocalDate) = {
    StopTime(
      stop_id,
      arrival_time.map(dt.toLocalDateTime(LocalTime.Midnight) + _),
      departure_time.map(dt.toLocalDateTime(LocalTime.Midnight) + _)
    )
  }
}