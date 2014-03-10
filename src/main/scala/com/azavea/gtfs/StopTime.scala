package com.azavea.gtfs

import com.github.nscala_time.time.Imports._
import com.azavea.gtfs.data.{RangeTripFrequency, StopTimeRec}

case class StopTime(
  stop_id: String, //TODO: This should be a Stop Object
  arrivalTime: Option[LocalDateTime],
  departureTime: Option[LocalDateTime]
)
