package com.azavea.gtfs

import com.github.nscala_time.time.Imports._

/**
 * @param service_id
 * @param date Date of service
 * @param exception 'Add or 'Remove for addition/removal of service on date
 */
case class CalendarDateRec(
  service_id: ServiceId,
  date: LocalDate,
  exception: Symbol
) {
  def addService: Boolean = exception == 'Add
  def removeService: Boolean = exception == 'Remove
}