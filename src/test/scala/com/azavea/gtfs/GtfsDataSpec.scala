package com.azavea.gtfs

import com.github.nscala_time.time.Imports._

import org.scalatest._
import data._

class GtfsDataSpec extends FlatSpec with Matchers {

  val data = new GtfsData(new GtfsTestReader)

  "GtfsTestData" should "have a spec" in {
    //fail()
  }
}
