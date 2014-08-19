package com.azavea.gtfs.slick

import com.azavea.gtfs._
import com.github.nscala_time.time.Imports._


/**
 * This is going to be backed into a DAO and when imported will augment model classes with useful methods.
 *
 * The intent of context is convenience of reference. Everything done here to should be doable "nicely" enough
 * without the import. Thus most of the functionality should be baked into the DAO components
 */
trait SlickContextComponent { self: Profile with TripsComponent with StopsComponent with ShapesComponent =>
  import profile.simple._

  object context {
    implicit class RichRoute(r: Route)(implicit s: Session) {
      def getTrips: List[Trip] = trips.getForRoute(r.id)

      def getTripsOn(dt: LocalDate)(implicit s: Session): Seq[Trip] = ???
    }

    implicit class RichStopTime(st: StopTime)(implicit s: Session) {
      def getStop: Option[Stop] = stops.get(st.stop_id)
    }

    implicit class RichService(service: Service)(implicit s: Session){
      def getTripsOn(dt: LocalDate): List[Trip] = {
        if (service.activeOn(dt))
          trips.getForService(service.id)
        else
          Nil
      }
    }

    implicit class RichTrip(trip: Trip)(implicit s: Session) {
      def getShape: Option[TripShape] = for (sid <- trip.shape_id) yield shapes.get(sid).get
    }
  }
}
