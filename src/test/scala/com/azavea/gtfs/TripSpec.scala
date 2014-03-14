package com.azavea.gtfs

import com.github.nscala_time.time.Imports._
import org.scalatest._
import data._


class TripSpec extends FlatSpec with Matchers {
  val data = new GtfsData(new GtfsTestReader())

  def printTrip(trip: Trip){
    println(s"TRIP: ${trip.trip_id} ${trip.stops}")

    trip.stops.foreach{ s=>
      println(s"\tSTOP ${s.stop_id} ${s.arrival} - ${s.departure}")
    }
  }

  "TripRec" should "generate timed Trips" in {
    val trip = data.tripById("T1")

    val t = trip(new LocalDate(2013, 2, 2))

    t(0).stops(0).arrival.toLocalTime should equal (new LocalTime(10,00,00))
    //t.foreach(printTrip)
  }
}
