package com.azavea.gtfs

import com.github.nscala_time.time.Imports._
import org.joda.time.format.PeriodFormatterBuilder

package object data {

  implicit def String2OptionalLocalTime(s: String):Option[LocalTime] = {
    if (s == ""){
      None
    }else{
      val chunks = s.split(":").map(_.toInt)
      require(chunks.length == 3)
      Some(new LocalTime(chunks(0), chunks(1), chunks(2)))
    }
  }

  implicit def String2OptionalDouble(s: String):Option[Double] = {
    if (s == "") None else Some(s.toDouble)
  }

  val periodFormatter = new PeriodFormatterBuilder()
    .appendHours().appendSuffix(":")
    .appendMinutes().appendSuffix(":")
    .appendSeconds()
    .toFormatter

  implicit def String2Duration(s: String):Period = {
      if (s == "") return null
      periodFormatter.parsePeriod(s)
  }

  val dateRegex = """(\d{4})(\d{2})(\d{2})""".r
  implicit def String2LocalDate(s: String):LocalDate = {
    val dateRegex(year, month, day) = s
    new LocalDate(year.toInt, month.toInt, day.toInt)
  }
}
