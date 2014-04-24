package com.azavea.gtfs.slick

import com.azavea.gtfs._
import com.github.nscala_time.time.Imports._


/**
 * This is going to be backed into a DAO and when imported will augment model classes with useful methods.
 *
 * The intent of context is convenience of reference. Everything done here to should be doable "nicely" enough
 * without the import. Thus most of the functionality should be baked into the DAO components
 */
trait SlickContextComponent { self: Profile with TripsComponent with StopsComponent =>
  import profile.simple._

  object context {
    implicit class RichRoute(r: Route) {
      def getTrips(implicit s: Session): List[Trip] = trips.getForRoute(r.id)
    }

    implicit class RichStopTime(st: StopTime) {
      def getStop(implicit s: Session): Option[Stop] = stops.get(st.stop_id)
    }

    implicit class RichService(service: Service){
      def getTripsOn(dt: LocalDate)(implicit s: Session): List[Trip] = {
        if (service.activeOn(dt))
          trips.getForService(service.id)
        else
          Nil
      }
    }
  }
}
