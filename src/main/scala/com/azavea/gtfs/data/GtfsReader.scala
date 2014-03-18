package com.azavea.gtfs.data

import com.azavea.gtfs._

trait GtfsReader {
  def getStops: Iterator[Stop]
  def getStopTimes: Iterator[StopTimeRec]
  def getTrips: Iterator[TripRec]
  def getRoutes: Iterator[Route]
  def getFrequencies: Iterator[Frequency]
  def getCalendar: Iterator[CalendarRec]
  def getCalendarDates: Iterator[CalendarDateRec]

  def toGtfsData:GtfsData = new GtfsData(this)
}