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

  val trip1 = TripRec("T1","SR1","R1","Go Home",
    List(
      StopTimeRec("S1","T1", 1, 0.seconds, 1.minute),
      StopTimeRec("S2","T1", 1, 10.minutes, 11.minutes),
      StopTimeRec("S3","T1", 1, 15.minutes, 16.minutes)
    )
  )
  val trip2 = TripRec("T2","SR1","R1","Go Home Again",
    List(
      StopTimeRec("S1","T2", 1, 1.minute + 0.seconds, 1.minute + 1.minute),
      StopTimeRec("S2","T2", 1, 1.minute + 10.minutes, 1.minute + 11.minutes),
      StopTimeRec("S3","T2", 1, 1.minute + 15.minutes, 1.minute + 16.minutes)
    )
  )
  val trip3 = TripRec("T2","SR1","R1","Go Home Again",
    List(
      StopTimeRec("S1","T2", 1, 1.minute + 0.seconds, 1.minute + 1.minute),
      StopTimeRec("S2","T2", 1, 1.minute + 10.minutes, 1.hour + 11.minutes), //long break here
      StopTimeRec("S3","T2", 1, 1.hour + 15.minutes, 1.hour + 16.minutes)
    )
  )

  "TripRec" should "generate timed Trips" in {
    val trip = data.tripById("T1")
    val t = trip(new LocalDate(2013, 2, 2))
    t(0).stops(0).arrival.toLocalTime should equal (new LocalTime(10,00,00))
  }

  it should "recognize a shameish trip" in {
    assert(trip1 sameish trip2, "Two trips that differ only in time offset are sameish")
    assert(! (trip1 sameish trip3), "Two trips that differ in stop intervals are not sameish")
  }

  it should "know how to segregate" in {
    val bins = TripRec.bin(trip1 :: trip2 :: trip3 :: Nil)
    val big = bins.find(_.size == 2).get
    val small = bins.find(_.size == 1).get

    big should contain (trip1)
    big should contain (trip2)
    small should contain (trip3)
  }
}
