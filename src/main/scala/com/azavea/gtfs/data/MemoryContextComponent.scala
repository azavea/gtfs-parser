package com.azavea.gtfs.data

import com.azavea.gtfs._
import com.azavea.gtfs.util.DateIterator
import com.github.nscala_time.time.Imports._
import org.joda.time.Days


/**
 * This is going to be backed into a memory DAO that is backed by hash tables
 *
 * The intent of context is convenience of reference. Everything done here to should be doable "nicely" enough
 * without the import. Thus most of the functionality should be baked into the DAO components
 */
trait MemoryContextComponent { self: GtfsData =>
  object context {
    implicit class RichRoute(r: Route) {
      def getTrips: Seq[Trip] = tripsByRoute(r.id)

      /** Get Trip template records that are active for this route on a day 
       * note: This is not the same as all trips that will actually happen */
      def getTripsOn(dt: LocalDate): Seq[Trip] = {
        for {
          s <- service
          trip <- s.getTripsOn(dt) if trip.route_id == r.id
        } yield {
          trip
        }
      }

      /**
       * Trips that are scheduled to START between two times
       */
      def getScheduledTripsBetween(a: LocalDateTime, b: LocalDateTime): Seq[ScheduledTrip] = {
        for {
          dt <- DateIterator(a, b)
          s <- service
          trip <- s.getTripsOn(dt) if trip.route_id == r.id
          st <- trip(dt) if st.starts.isAfter(a)
        } yield {
          st
        }
      }.toSeq

      /** Fully resole Trip templates to Scheduled Trips for this route */
      def getScheduledTripsOn(dt: LocalDate): Seq[ScheduledTrip] =
        getTripsOn(dt).map(_.apply(dt)).flatten
    }

    implicit class RichStopTime(st: StopTime) {
      def getStop: Option[Stop] = stopsById.get(st.stop_id)
    }

    implicit class RichService(service: Service){
      def getTripsOn(dt: LocalDate): Seq[Trip] = {
        if (service.activeOn(dt))
          tripsByService(service.id)
        else
          Nil
      }
    }

    implicit class RichTrip(trip: Trip){
      def getShape: Option[TripShape] = for (sid <- trip.shape_id) yield shapesById(sid)
    }
  }
}
