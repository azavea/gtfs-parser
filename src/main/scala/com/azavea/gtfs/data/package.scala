package com.azavea.gtfs

import com.github.nscala_time.time.Imports._

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

  implicit def String2OptionalDuration(s: String):Option[Duration] = {
    if (s == ""){
      None
    }else{
      Some(String2Duration(s))
    }
  }

  implicit def String2Duration(s: String):Duration = {
    val chunks = s.split(":").map(_.toInt)
    require(chunks.length == 3)
    chunks(0).hours + chunks(1).minutes + chunks(2).seconds
  }


  val dateRegex = """(\d{4})(\d{2})(\d{2})""".r
  implicit def String2LocalDate(s: String):LocalDate = {
    val dateRegex(year, month, day) = s
    new LocalDate(year.toInt, month.toInt, day.toInt)
  }
}
