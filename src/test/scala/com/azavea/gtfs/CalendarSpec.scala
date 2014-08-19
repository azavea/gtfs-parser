package com.azavea.gtfs

import com.github.nscala_time.time.Imports._

import org.scalatest._
import data._

class CalendarSpec extends FlatSpec with Matchers {
  val data = new GtfsTestReader().toGtfsData
  val cal = data.service.head

  "Calendar" should "return active services" in {
    val sunday = new LocalDate(2013,1,6)
    val monday = new LocalDate(2013,1,7)

    monday.getDayOfWeek should equal (1)
    sunday.getDayOfWeek should equal (7)

    cal.activeOn(monday) should equal (true)
    cal.activeOn(sunday) should equal (false)
  }

  it should "consider calendar_dates.txt" in {
    //this date is an exception, S1 does not run on weekends
    val saturday = new LocalDate(2013,1,5)
    saturday.getDayOfWeek should equal (6)
    cal.activeOn(saturday) should equal (true)
  }

  
  it should "filter philly rail by date" in {
    val phillyRail = GtfsData.fromFile("data/philly_rail")
    
    import phillyRail.context._
    
    val airRoute = phillyRail.routes.filter(_.id == "AIR").head

    val date = new LocalDate(2013,1,6)
    //These are just trips with time offsets
    val airTrips = airRoute.getTripsOn(date)

    //This will give me list of lists of trips resolved to a DateTime
    val timedTrips = airTrips.map(trip => trip(date))
    
    airTrips should not be empty
    timedTrips should not be empty
  }
}
