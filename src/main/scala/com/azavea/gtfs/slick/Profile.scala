package com.azavea.gtfs.slick

import scala.slick.driver.JdbcProfile
import com.github.tototoshi.slick.GenericJodaSupport
import com.github.nscala_time.time.Imports._
import org.joda.time.format.PeriodFormatterBuilder

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

  implicit val durationColumntype =
    MappedColumnType.base[Duration, Int](
    { duration => duration.getStandardSeconds.toInt },
    { int => int.seconds }
  )

}