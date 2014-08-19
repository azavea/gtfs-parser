package com.azavea.gtfs

import com.github.nscala_time.time.Imports._

/**
 * Represents a weekly service calendar between two dates
 * Source: calendar.txt
 *
 * @param service_id unique service identifier
 * @param start_date starting date of service (inclusive)
 * @param end_date ending date of service (inclusive)
 * @param week Array representing service availability on each day of the week
 */
case class ServiceCalendar(
  service_id: String,
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
