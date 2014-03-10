package com.azavea.gtfs

import com.github.nscala_time.time.Imports._
import org.scalatest._
import data._

class FreqSpec extends FlatSpec with Matchers {
  val data = new GtfsData("data/test")
  val tm = new TripMaster(data)

  def printTrip(trip: Trip){
    println("TRIP: " + trip.trip_id)
    trip.stopTimes.foreach{ s=>
      println(s"\tSTOP ${s.stop_id}: ${s.arrivalTime} - ${s.departureTime}")
    }
  }

  "TripMaster" should "trip" in {
    val service_id = "WE"
    val dt = new LocalDate(2006, 7, 1)

    val trips = tm(dt, "WE")

    trips.map(_.trip_id) should contain only "AWE1"

    val stops = trips.map(_.stopTimes).flatten

    //first departure
    val s1 = StopTime(
      stop_id = "S1",
      arrivalTime = Some(new LocalDateTime("2006-07-01T05:30:00.000")),
      departureTime = Some(new LocalDateTime("2006-07-01T05:30:00.000"))
    )
    stops should contain (s1)

    //last departure
    val s2 = StopTime(
      stop_id = "S1",
      arrivalTime = Some(new LocalDateTime("2006-07-01T06:30:00.000")),
      departureTime = Some(new LocalDateTime("2006-07-01T06:30:00.000"))
    )
    stops should contain (s2)

    //past the limit
    val s3 = StopTime(
      stop_id = "S1",
      arrivalTime = Some(new LocalDateTime("2006-07-01T06:35:00.000")),
      departureTime = Some(new LocalDateTime("2006-07-01T06:35:00.000"))
    )
    stops should not contain(s3)

  }
}
