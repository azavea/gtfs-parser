package com.azavea.gtfs

import com.azavea.gtfs.data._
import com.github.nscala_time.time.Imports._

/**
 * User: eugene
 * Date: 3/13/14
 */
class GtfsTestReader extends GtfsReader{
  def getStops = List(
    Stop("S1", "Stop 1", "First Stop", 0, 0),
    Stop("S2", "Stop 1", "First Stop", 10, 10),
    Stop("S3", "Stop 1", "First Stop", 10, 20)
  )

  override def getRoutes: Traversable[Route] = List(
    Route("R1","Route 1", "The one true route", RouteType.Funicular)
  )

  def getTrips = List(
    TripRec("T1","SR1","R1","Go Home",Nil)
  )

  def getStopTimes = List(
    StopTimeRec("S1","T1", 1, 0.seconds, 1.minute),
    StopTimeRec("S2","T1", 1, 10.minutes, 11.minutes),
    StopTimeRec("S3","T1", 1, 15.minutes, 16.minutes)
  )

  def getCalendar = List(
    CalendarRec(
      service_id = "SR1",
      start_date = "20130101",
      end_date = "20140101",
      week = Array(true, true, true, true, true, false, false)
    )
  )

  def getCalendarDates = List(
    CalendarDateRec("S1", new LocalDate(2013,1,5), 'Add) //Saturday
  )

  def getFrequencies = List(
    Frequency("T1", start_time = 10.hours, end_time = 10.hours + 13.minutes, headway = 300.seconds)
  )
}
