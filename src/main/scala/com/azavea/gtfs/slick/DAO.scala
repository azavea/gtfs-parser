package com.azavea.gtfs.slick

import scala.slick.driver.JdbcProfile
import com.github.tototoshi.slick.GenericJodaSupport

class DAO(override val profile: JdbcProfile, override val jodaSupport: GenericJodaSupport)
  extends Profile
  with StopsComponent
  with TripsComponent
  with RoutesComponent
  with ServiceComponent
