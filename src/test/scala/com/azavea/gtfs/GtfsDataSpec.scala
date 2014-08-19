package com.azavea.gtfs

import com.github.nscala_time.time.Imports._

import org.scalatest._
import data._

class GtfsDataSpec extends FlatSpec with Matchers {

  val data = GtfsData.fromReader(new GtfsTestReader)

  "GtfsTestData" should "have a context" in {
    import data.context._
    //everything below is happening through the context

    //I can get all the trips from route, maigc!
    val r1Trips = data.routes(0).getTrips
    val t1 = r1Trips.head
    
    //I can get a stop from stoptimes
    val stop = t1.stopTimes.head.getStop

    //We just used a service calendar to find all the trips for it on a day
    val liveTrip = data.service.head.getTripsOn(new LocalDate(2013,1,5))
  }
}
