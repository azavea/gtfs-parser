package com.azavea.gtfs

import com.github.nscala_time.time.Imports._
import com.azavea.gtfs.data.GtfsData

//The main interface to the GTFS data
class Gtfs(data: GtfsData) {
  val cal = new Calendar(data)
  val tripMaster = new TripMaster(data)

  def getTripsOn(dt: LocalDate): Seq[Trip] = {
    for{
      service <- cal.getServiceFor(dt)
      trip <- tripMaster(dt, service)
    } yield trip
  }.toSeq
}