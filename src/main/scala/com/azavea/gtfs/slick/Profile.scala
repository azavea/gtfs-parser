package com.azavea.gtfs.slick

import scala.slick.driver.{JdbcDriver, JdbcProfile}
import com.github.tototoshi.slick.GenericJodaSupport

import com.github.nscala_time.time.Imports._
import org.joda.time.format.PeriodFormatterBuilder
import com.azavea.gtfs.RouteType
import com.azavea.gtfs.RouteType.RouteType

trait Profile {
  val profile: JdbcProfile
  val jodaSupport: GenericJodaSupport

  import profile.simple._

  /** Periods are expressed in HH:MM:SS */
  implicit val periodColumnType = {
    val formatter = new PeriodFormatterBuilder()
      .appendHours().appendSuffix(":")
      .appendMinutes().appendSuffix(":")
      .appendSeconds()
      .toFormatter

    MappedColumnType.base[Period, String](
      { period => period.toString(formatter) },
      { text => formatter.parsePeriod(text) }
    )
  }

  implicit val durationColumnType =
    MappedColumnType.base[Duration, Int](
      { duration => duration.getStandardSeconds.toInt },
      { int => int.seconds }
    )

  implicit val routeTypeColumnType =
    MappedColumnType.base[RouteType, Int](
      { rt => rt.id },
      { int => RouteType(int) }
    )

}