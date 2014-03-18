package com.azavea.gtfs

import org.scalatest._
import util._

class RunLengthSpec extends FlatSpec with Matchers {
  "RunLength" should "work" in {
    val list = List(1,2,2,2,4,2,2)

    val expected = List(
      Run(1,List(1)),
      Run(3,List(2,2,2)),
      Run(1,List(4)),
      Run(2,List(2,2))
    )
    RunLength(list) should equal (expected)
  }
}
