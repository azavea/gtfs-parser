package com.azavea.gtfs

import com.github.nscala_time.time.Imports._

import org.scalatest._
import data._

class GtfsSpec extends FlatSpec with Matchers {

  val data = new GtfsData(new GtfsTestReader())
  val gtfs = new Gtfs(data)

  "GTFS" should "be able to find a trip" in {
    val ret = gtfs.getTripsOn(new LocalDate(2013,01,01))
    ret should not be empty
  }
}
