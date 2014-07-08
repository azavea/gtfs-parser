package com.azavea.gtfs

import com.azavea.gtfs.data._
import com.github.nscala_time.time.Imports._
import geotrellis.feature._
import geotrellis.slick._


/**
 * User: eugene
 * Date: 3/13/14
 */
class GtfsTestReader extends GtfsReader{
  implicit def seqToIterator[T](seq: Traversable[T]): Iterator[T] =
    seq.toIterator


  override def getAgencies: Iterator[Agency] = List(
    Agency(None, "THE", "http://", "EST")
  )

  def getStops = List(
    Stop("S1", "Stop 1", None, 0, 0, Point(0,0).withSRID(0)),
    Stop("S2", "Stop 2", None, 10, 10, Point(10,10).withSRID(0)),
    Stop("S3", "Stop 3", Some("Stop"), 10, 20, Point(10,20).withSRID(0))
  )

  override def getRoutes = List(
    Route("R1","Route 1", "The one true route", RouteType.Funicular)
  )

  def getTrips = List(
    Trip("T1","SR1","R1",None ,Nil)
  )

  def getStopTimes = List(
    StopTime("S1","T1", 1, 0.seconds, 1.minute),
    StopTime("S2","T1", 2, 10.minutes, 11.minutes),
    StopTime("S3","T1", 3, 15.minutes, 16.minutes)
  )

  def getCalendar = List(
    ServiceCalendar(
      service_id = "SR1",
      start_date = "20130101",
      end_date = "20140101",
      week = Array(true, true, true, true, true, false, false)
    )
  )

  def getCalendarDates = List(
    ServiceException("SR1", new LocalDate(2013,1,5), 'Add) //Saturday
  )

  def getFrequencies = List(
    Frequency("T1", start_time = 10.hours, end_time = 10.hours + 13.minutes, headway = 300.seconds)
  )

  def getShapes = Iterator.empty
}
