package com.azavea.gtfs.slick

import com.azavea.gtfs._

trait SlickContextComponent { self: Profile with TripsComponent =>
  import profile.simple._

  object context {
    implicit class RichRoute(r: Route) {
      def getTrips(implicit s: Session): List[Trip] = trips.getForRoute(r.id)
    }
  }
}
