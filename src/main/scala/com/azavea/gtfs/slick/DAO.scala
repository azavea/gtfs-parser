package com.azavea.gtfs.slick

import com.azavea.gtfs._
import com.azavea.gtfs.data.GtfsData

class DAO
  extends Profile
  with StopsComponent
  with TripsComponent
  with ShapesComponent
  with RoutesComponent
  with AgencyComponent
  with ServiceComponent
  with SlickContextComponent
{
  import profile.simple._
  def toGtfsData(implicit session: Session) = {
    val dao = this
    new GtfsData {
      override val agencies: Array[Agency] =
        dao.agencies.all.toArray
      override val stops: Array[Stop] =
        dao.stops.all.toArray
      override val trips: Array[Trip] =
        dao.trips.all.toArray
      override val service: Array[Service] =
        dao.service.all.toArray
      override val shapes: Array[TripShape] =
        dao.shapes.all.toArray
      override val stopTimes: Array[StopTime] =
        dao.trips.allStopTimes.toArray
      override val frequencies: Map[TripId, Frequency] =
        dao.trips.allFrequencies.map{ f => f.trip_id -> f}.toMap
      override val routes: Array[Route] =
        dao.routes.all.toArray
    }
  }
}