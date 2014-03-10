package com.azavea.gtfs

import com.github.nscala_time.time.Imports._

import org.scalatest._
import data._

class GtfsSpec extends FlatSpec with Matchers {

  val data = new GtfsData("data/test")

  "GTFS" should "be able to find a trip" in {
    val gtfs = new Gtfs(data)

    val ret = gtfs.getTripsOn(new LocalDate(2006,07,02))
    ret should not be empty
  }
}
