package com.azavea.gtfs.data

import com.azavea.gtfs._
import com.github.nscala_time.time.Imports._

case class CalendarRec(
  service_id: ServiceId,
  start_date: LocalDate,
  end_date: LocalDate,
  week: Array[Boolean]
) {
  require(service_id != "", "Service ID is required")
  require(start_date <= end_date, "Time must flow forward")
  require(week.length == 7, "Week must contain 7 days")

  def activeOn(dt: LocalDate): Boolean = week(dt.getDayOfWeek - 1)
  def activeOn(weekDay: Int): Boolean = week(weekDay - 1)
}
