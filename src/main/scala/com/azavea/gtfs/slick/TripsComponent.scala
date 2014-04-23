package com.azavea.gtfs.slick

import com.azavea.gtfs._
import com.github.nscala_time.time.Imports._


trait TripsComponent {this: Profile with StopsComponent =>
  import profile.simple._

  class Trips(tag: Tag) extends Table[(String, String, String, Option[String])](tag, "gtfs_trips") {
    def id = column[String]("trip_id")
    def service_id = column[String]("service_id")
    def route_id = column[String]("route_id")
    def trip_headsign = column[Option[String]]("trip_headsign")

    def * = (id, service_id, route_id, trip_headsign)
  }
  val tripsTable = TableQuery[Trips]

  class StopTimes(tag: Tag) extends Table[StopTime](tag, "gtfs_stop_times") {
    def stop_id = column[String]("stop_id")
    def trip_id = column[String]("trip_id")
    def stop_sequence = column[Int]("stop_sequence")
    def arrival_time = column[Period]("arrival_time")
    def departure_time = column[Period]("departure_time")
    def shape_dist_traveled = column[Option[Double]]("shape_dist_traveled")

    def stop = foreignKey("STOP_FK", stop_id, stopsTable)(_.id)
    def trip = foreignKey("TRIP_FK", trip_id, tripsTable)(_.id)

    def * = (stop_id, trip_id, stop_sequence, arrival_time, departure_time, shape_dist_traveled) <>
      (StopTime.tupled, StopTime.unapply)
  }
  val stopTimesTable = TableQuery[StopTimes]

  class Frequencies(tag: Tag) extends Table[Frequency](tag, "gtfs_frequencies") {
    def trip_id = column[String]("trip_id")
    def start_time = column[Period]("start_time")
    def end_time = column[Period]("end_time")
    def headway = column[Duration]("headway_secs")

    def trip = foreignKey("TRIP_FK", trip_id, tripsTable)(_.id)

    def * = (trip_id, start_time, end_time, headway) <> (Frequency.tupled, Frequency.unapply)
  }
  val frequencyTable = TableQuery[Frequencies]


  object trips {
    lazy val tripById = for {
      tripId <- Parameters[String]
      trip <- tripsTable if trip.id === tripId
    } yield trip

    lazy val tripsByRouteId = for {
      id <- Parameters[String]
      trip <- tripsTable if trip.route_id === id
    } yield trip

    lazy val tripsByServiceId = for {
      id <- Parameters[String]
      trip <- tripsTable if trip.service_id === id
    } yield trip

    lazy val stopsByTripId = for {
      tripId <- Parameters[String]
      stop <- stopTimesTable if stop.trip_id === tripId
    } yield stop

    lazy val freqByTripId = for {
      tripId <- Parameters[String]
      freq <- frequencyTable if freq.trip_id === tripId
    } yield freq

    private def buildTrip(tup: (String, String, String, Option[String]))(implicit session: Session): Trip = {
      tup match {
        case (id, sid, rid, head) =>
          Trip(id = id, service_id = sid, route_id = rid, trip_headsign = head,
            stopTimes = stopsByTripId(id).list.sortBy(_.stop_sequence),
            frequency = freqByTripId(id).firstOption
          )
      }
    }

    def get(id: TripId)(implicit session: Session): Option[Trip] = {
      tripById(id).firstOption.map { buildTrip }
    }
    def apply(id: TripId)(implicit session: Session) = get(id)

    def getForRoute(routeId: RouteId)(implicit session: Session): List[Trip] = {
      tripsByRouteId(routeId).list.map { buildTrip }
    }

    def getForServices(sids: Seq[ServiceId])(implicit session: Session): List[Trip] = {
      { for {
        trip <- tripsTable if trip.service_id inSet sids
      } yield trip }.list.map { buildTrip }
    }

    def delete(tripId: TripId)(implicit session: Session): Boolean = {
      stopsByTripId(tripId).delete
      freqByTripId(tripId).delete
      tripById(tripId).delete
      false //TODO - need this to be a real return
    }

    def insert(trip: Trip)(implicit session: Session): Boolean = {
      tripsTable.forceInsert((trip.id, trip.service_id, trip.route_id, trip.trip_headsign))
      trip.frequency match {
        case Some(freq: Frequency) => frequencyTable.forceInsert(freq)
        case None =>
      }
      stopTimesTable.forceInsertAll(trip.stopTimes: _*)
      false //TODO - need this to be a real return
    }

    def update(trip: Trip)(implicit session: Session): Boolean = ???
  }
}
