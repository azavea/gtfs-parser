package com.azavea.gtfs.data

import com.azavea.gtfs._

trait GtfsReader {
  def getStops: Traversable[Stop]
  def getStopTimes: Traversable[StopTimeRec]
  def getTrips: Traversable[TripRec]
  def getRoutes: Traversable[Route]
  def getFrequencies: Traversable[Frequency]
  def getCalendar: Traversable[CalendarRec]
  def getCalendarDates: Traversable[CalendarDateRec]

  def toGtfsData:GtfsData = new GtfsData(this)
}