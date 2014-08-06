package com.azavea.gtfs

import com.github.nscala_time.time.Imports._

/**
 * Represents a repetition of a trip in a time with headway
 * @param trip_id Related trip
 * @param start_time The time of the first trip as offset from midnight
 * @param end_time No trips may leave after this time as offset from midnight
 * @param headway delay between the start of the trips
 */
case class Frequency (
  trip_id: String,
  start_time: Period,
  end_time: Period,
  headway: Duration
) {

  def apply(dt: LocalDate): Iterator[LocalDateTime] = new Iterator[LocalDateTime] {
    var start = dt.toLocalDateTime(LocalTime.Midnight) + start_time
    val end   = dt.toLocalDateTime(LocalTime.Midnight) + end_time

    override def hasNext: Boolean = start <= end

    override def next(): LocalDateTime = {
      val ret = start
      start += headway
      ret
    }
  }

  def toStream(dt: LocalDate): Stream[LocalDateTime] = {
    def timeSteps(start: LocalDateTime, end: LocalDateTime, step: Duration): Stream[LocalDateTime] = {
      if (start <= end) //I'm introducing this Boolean that has nothing to do with the computation, ARGH!
        start #:: timeSteps(start + step, end, step)
      else
        Stream.empty
    }

    timeSteps(
      start = dt.toLocalDateTime(LocalTime.Midnight) + start_time,
      end   = dt.toLocalDateTime(LocalTime.Midnight) + end_time,
      step  = headway)
  }
}
