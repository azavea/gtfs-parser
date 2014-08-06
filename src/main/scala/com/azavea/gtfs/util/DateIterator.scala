package com.azavea.gtfs.util

import com.github.nscala_time.time.Imports._

object DateIterator{
  def apply(a: LocalDate, b: LocalDate) = new DateIterator(a, b)
  def apply(a: LocalDateTime, b: LocalDateTime) = new DateIterator(a.toLocalDate, b.toLocalDate)
}
class DateIterator(start: LocalDate, end: LocalDate) extends Iterator[LocalDate] {
  var curr = start

  override def hasNext: Boolean = curr <= end

  override def next(): LocalDate = {
    val ret = curr
    curr = curr.plusDays(1)
    ret
  }
}
