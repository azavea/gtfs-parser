package com.azavea.gtfs.data

import com.azavea.gtfs._

trait GtfsReader {
  def getStops: Iterator[Stop]
  def getStopTimes: Iterator[StopTime]
  def getTrips: Iterator[Trip]
  def getRoutes: Iterator[Route]
  def getFrequencies: Iterator[Frequency]
  def getCalendar: Iterator[ServiceCalendar]
  def getCalendarDates: Iterator[ServiceException]

  def toGtfsData:GtfsData = new GtfsData(this)
}