package com.azavea.gtfs

import com.github.nscala_time.time.Imports._

import org.scalatest._
import data._

class ParserSpec extends FlatSpec with Matchers {

  val data = new GtfsData("data/test")

  "GtfsData" should "return calendar records" in {
    val rec = data.getCalendar()

    rec should not be empty
  }

  it should "contain some calendar records" in {
    data.calendar should not be empty
  }

  it should "contain calendar dates records" in {
    data.exceptions should not be empty
  }

  it should "contain trip frequencies" in {
    data.frequencies should not be empty
  }

  it should "contain stop times" in {
    data.stopTimes should not be empty
  }

  it should "contain stops" in {
    data.stops should not be empty
  }

}
