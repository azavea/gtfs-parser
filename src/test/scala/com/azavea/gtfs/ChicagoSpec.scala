package com.azavea.gtfs

import org.scalatest._
import com.azavea.gtfs.data.{CsvParser}
import com.github.nscala_time.time.Imports._

class ChicagoSpec extends FlatSpec with Matchers {

  "Chicago" should "have gtfs" in {
      val gtfs = Gtfs.fromFile("data/chicago")

      println("STARTING THE COMPRESSION ...")
      val t2 = TripRec.compress(gtfs.trips.filter(_.frequency == None), 5)

      println("New Trip Count: " + t2.length)
  }
}
