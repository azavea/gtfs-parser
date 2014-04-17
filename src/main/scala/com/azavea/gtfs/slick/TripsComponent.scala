package com.azavea.gtfs.slick

import com.azavea.gtfs._
import com.github.nscala_time.time.Imports._


trait TripsComponent {this: Profile =>
  import profile.simple._

  class Trips(tag: Tag) extends Table[(String, String, String, Option[String])](tag, "gtfs_trips") {
    def id = column[String]("trip_id")
    def service_id = column[String]("service_id")
    def route_id = column[String]("route_id")
    def trip_headsign = column[Option[String]]("trip_headsign")
    def * = (id, service_id, route_id, trip_headsign)
  }

  class StopTimes(tag: Tag) extends Table[StopTime](tag, "gtfs_stop_times") {
    def stop_id = column[String]("stop_id")
    def trip_id = column[String]("trip_id")
    def stop_sequence = column[Int]("stop_sequence")
    def arrival_time = column[Period]("arrival_time")
    def departure_time = column[Period]("departure_time")
    def shape_dist_traveled = column[Double]("shape_dist_traveled")
    def * = (stop_id, trip_id, stop_sequence, arrival_time, departure_time, shape_dist_traveled) <>
      (StopTime.tupled, StopTime.unapply)
  }

  class Frequencies(tag: Tag) extends Table[Frequency](tag, "gtfs_frequencies") {
    def trip_id = column[String]("trip_id")
    def start_time = column[Period]("start_time")
    def end_time = column[Period]("end_time")
    def headway = column[Duration]("headway_secs")
    def * = (trip_id, start_time, end_time, headway) <> (Frequency.tupled, Frequency.unapply)
  }

  object trips {
    val tripQuery = TableQuery[Trips]
    val stopTimesQuery = TableQuery[StopTimes]
    val frequencyQuery = TableQuery[Frequencies]

    lazy val tripById = for {
      tripId <- Parameters[String]
      trip <- tripQuery if trip.id === tripId
    } yield trip

    lazy val stopsByTripId = for {
      tripId <- Parameters[String]
      stop <- stopTimesQuery if stop.trip_id === tripId
    } yield stop

    lazy val freqByTripId = for {
      tripId <- Parameters[String]
      freq <- frequencyQuery if freq.trip_id === tripId
    } yield freq

    def getById(id: String)(implicit session: Session): Option[Trip] = {
      tripById(id).firstOption.map { case (id, sid, rid, head) =>
        Trip(id = id, service_id = sid, route_id = rid, trip_headsign = head,
          stopTimes = stopsByTripId(id).list.sortBy(_.stop_sequence),
          frequency = freqByTripId(id).firstOption
        )
      }
    }
  }
}
