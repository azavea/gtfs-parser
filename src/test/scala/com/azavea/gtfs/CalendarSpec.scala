package com.azavea.gtfs

import com.github.nscala_time.time.Imports._

import org.scalatest._
import data._

class CalendarSpec extends FlatSpec with Matchers {

  val data = new GtfsData("data/test")
  val cal = new Calendar(data)

  "Calendar" should "return active services" in {
    val saturday = new LocalDate(2006,07,8)
    val monday = new LocalDate(2006,7,10)

    val satService = cal.getServiceFor(saturday)
    satService should contain ("WE")
    satService should not contain ("WD")

    val monService = cal.getServiceFor(monday)
    monService should not contain ("WE")
    monService should contain ("WD")
  }

  it should "consider calendar_dates.txt" in {
    //service_id,monday,tuesday,wednesday,thursday,friday,saturday,sunday,start_date,end_date
    //WE,0,0,0,0,0,1,1,20060701,20060731
    //WD,1,1,1,1,1,0,0,20060701,20060731

    //service_id, date,     exception_type
    //WD,         20060703, 2
    //WE,         20060703, 1
    //WD,         20060704, 2
    //WE,         20060704, 1
    val dt = new LocalDate(2006,07,03)

    //Previous test is the normal case, this is the oposite
    val satService = cal.getServiceFor(dt)
    satService should contain ("WE")
    satService should not contain ("WD")
  }
}
