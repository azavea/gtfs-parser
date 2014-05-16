package com.azavea.gtfs.data

import com.azavea.gtfs._
import com.github.nscala_time.time.Imports._


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
