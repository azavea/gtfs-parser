package com.azavea.gtfs

import org.scalatest._
import com.azavea.gtfs.data.{CsvParser}
import com.github.nscala_time.time.Imports._

object StopTimeCol extends Enumeration {
  type StopTimeCol = Value
  val stop_headsign,pickup_type,shape_dist_traveled, trip_id,arrival_time,departure_time,stop_id,stop_sequence = Value
}

class ChicagoSpec extends FlatSpec with Matchers {

  "Chicago" should "have gtfs" in {
      val gtfs = Gtfs.fromFile("data/chicago")

    //trip_id=0,arrival_time=1,departure_time=2,stop_id=3,stop_sequence=4,
    // stop_headsign=5,pickup_type=6,shape_dist_traveled=7


//    implicit def String2Duration(s: String):Duration = {
//      val chunks = s.split(":").map(_.toInt)
//      require(chunks.length == 3)
//      chunks(0).hours + chunks(1).minutes + chunks(2).seconds
//    }
//
//    import StopTimeCol._
//
//    var count: Long = 0
//    for (s <- CsvParser.fromPath("data/chicago/stop_times.txt")) {
//      StopTimeRec(
//        stop_id = s("stop_id"),
//        trip_id = s("trip_id"),
//        stop_sequence = s("stop_sequence").toInt,
//        arrival_time = s("arrival_time"),
//        departure_time = s("departure_time"),
//        shape_dist_traveled = s("shape_dist_traveled").toDouble,
//        stop = null
//      )
//      count += 1
//      if (count % 10000 == 0) println(count)
//    }
  }
}
