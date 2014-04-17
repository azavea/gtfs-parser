package com.azavea.gtfs

import com.github.nscala_time.time.Imports._

/**
 * Represents an exception to the regular service on given date
 * Source: calendar_dates.txt
 * @param service_id
 * @param date Date of service
 * @param exception 'Add or 'Remove for addition/removal of service on date
 */
case class ServiceException(
  service_id: ServiceId,
  date: LocalDate,
  exception: Symbol
) {
  def addService: Boolean = exception == 'Add
  def removeService: Boolean = exception == 'Remove
}