package com.azavea.gtfs.data

import com.github.nscala_time.time.Imports._
import com.azavea.gtfs.StopTime


sealed abstract class TripFrequency {
  def apply(stopTimes: Seq[StopTimeRec], dt: LocalDate): Seq[Seq[StopTime]]
}


object UnitFrequency extends TripFrequency{
  def apply(stopTimes: Seq[StopTimeRec], dt: LocalDate): Seq[Seq[StopTime]] = {
    List(stopTimes.map(_.toStopTime(dt)))
  }
}

/**
 * Represents a repetition of a given trip given some day
 *
 * @param start_time Start time of repetition at first stop as offset from midnight
 * @param end_time End time of repetition at first stop as offset from midnight
 * @param headway Delay between each trip departure from first stop
 */
case class RangeTripFrequency (
    trip_id: String,
    start_time: Duration,
    end_time: Duration,
    headway: Duration
) extends TripFrequency  {
  def apply(stopTimes: Seq[StopTimeRec], dt: LocalDate): Seq[Seq[StopTime]] = {
    val times = stopTimes.sortBy(_.stop_sequence) //Mostly we need the first to be the first

    def timeSteps(start: LocalDateTime, end: LocalDateTime, step: Duration): Stream[LocalDateTime] = {
      if (start <= end) //I'm introducing this Bool that has nothing to do with the computation, UGH!
        start #:: timeSteps(start + step, end, step)
      else
        Stream.empty
    }
    val t: Option[Int] = Some(1)

    for {dt <- timeSteps(
                  start = dt.toLocalDateTime(LocalTime.Midnight) + start_time,
                  end   = dt.toLocalDateTime(LocalTime.Midnight) + end_time,
                  step  = headway)
    } yield {
      times.map{ _.toStopTime(arrive = dt) }
    }
  }
}