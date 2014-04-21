package com.azavea.gtfs

import org.scalatest._
import com.azavea.gtfs.data.{GtfsData, CsvParser}
import com.github.nscala_time.time.Imports._

class AshevilleSpec extends FlatSpec with Matchers {

  "Asheville" should "have gtfs" in {
      val data = GtfsData.fromFile("data/asheville")
      val gtfs = new Gtfs(data)

      println(s"Max stops by mode:")
      gtfs.maxStopsByMode.foreach(println)

    println(s"Max stops by route:")
    gtfs.maxStopsByRoute.foreach(println)


  }
}
