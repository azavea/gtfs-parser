package com.azavea.gtfs.data

import com.azavea.gtfs._

trait GtfsReader {
  def getAgencies: Iterator[Agency]
  def getStops: Iterator[Stop]
  def getStopTimes: Iterator[StopTime]
  def getTrips: Iterator[Trip]
  def getRoutes: Iterator[Route]
  def getFrequencies: Iterator[Frequency]
  def getCalendar: Iterator[ServiceCalendar]
  def getCalendarDates: Iterator[ServiceException]
  //Since "shapes" will be rendered to lines, there is no point to have a dedicated class for this
  def getShapes: Iterator[(String, Double, Double, Int)]

  def toGtfsData:GtfsData = new GtfsData(this)
}