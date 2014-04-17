package com.azavea.gtfs

import com.github.nscala_time.time.Imports._

import org.scalatest._
import data._

class CalendarSpec extends FlatSpec with Matchers {
  val data = new GtfsTestReader().toGtfsData
  val cal = new Service(data.calendar, data.calendarDates)

  "Calendar" should "return active services" in {
    val sunday = new LocalDate(2013,1,6)
    val monday = new LocalDate(2013,1,7)

    monday.getDayOfWeek should equal (1)
    sunday.getDayOfWeek should equal (7)

    cal.getServiceFor(monday) should contain ("SR1")
    cal.getServiceFor(sunday) should not contain ("SR1")
  }

  it should "consider calendar_dates.txt" in {
    //this date is an exception, S1 does not run on weekends
    val saturday = new LocalDate(2013,1,5)
    saturday.getDayOfWeek should equal (6)
    cal.getServiceFor(saturday) should contain ("S1")
  }
}
