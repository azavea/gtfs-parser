package com.azavea.gtfs

import com.github.nscala_time.time.Imports._
import com.azavea.gtfs.data.{GtfsFileReader, GtfsData}

/**
 * Transit system specified by GTFS
 */
class Gtfs(data: GtfsData) {
  val cal = new Calendar(data.calendar, data.calendarDates)

  def getTripsOn(dt: LocalDate): Seq[Trip] = {
    for{
      service <- cal.getServiceFor(dt)
      generator <- data.tripsByServiceId(service)
      trip <- generator(dt)
    } yield trip
  }
}

object Gtfs {
  def fromFile(dir: String): Gtfs =
    new Gtfs(GtfsData.fromFile(dir))
}