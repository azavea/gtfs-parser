package com.azavea.gtfs

import org.scalatest._

class StopSpec extends FlatSpec with Matchers {
  val s1 = Stop("S1", "Stop 1", "First Stop", 0, 0)
  val s2 = Stop("S2", "Stop 1", "First Stop", 10, 10)
  val s3 = Stop("S3", "Stop 1", "First Stop", 10, 20)

  "Stop" should "know distance to another stop" in {
    (s2 - s1) should equal (math.sqrt(10*10 + 10*10))
    (s3 - s2) should equal (10.0)
  }

  it should "be commutative in distance calculation" in {
    (s2 - s1) should equal (s1 - s2)
    (s3 - s2) should equal (s2 - s3)
    (s3 - s1) should equal (s1 - s3)
  }
}
