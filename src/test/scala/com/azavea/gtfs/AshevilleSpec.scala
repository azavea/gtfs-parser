package com.azavea.gtfs

import org.scalatest._
import com.azavea.gtfs.data.{CsvParser}
import com.github.nscala_time.time.Imports._

class AshevilleSpec extends FlatSpec with Matchers {

  "Asheville" should "have gtfs" in {
      val gtfs = Gtfs.fromFile("data/asheville")

      println(s"Max stops by mode:")
      gtfs.maxStopsByMode.foreach(println)

    println(s"Max stops by route:")
    gtfs.maxStopsByRoute.foreach(println)


  }
}
