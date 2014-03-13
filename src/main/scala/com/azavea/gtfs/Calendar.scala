package com.azavea.gtfs

import com.github.nscala_time.time.Imports._

/**
 * Maps service availability onto a calendar.
 * Encapsulates records from calendar and calendar_dates
 */
class Calendar(recs: Iterable[CalendarRec], exceptions: Iterable[CalendarDateRec]) {
  /**
   * @param dt Date of inquiry
   * @return Sequence of ServiceIds that are active on the date
   */
  def getServiceFor(dt: LocalDate): Seq[ServiceId] = {
    //Find services that are active in calendar.txt
    val scheduled = recs
      .filter{ r=>
        r.start_date <= dt && r.end_date >= dt && r.activeOn(dt)
      }.map(_.service_id)
      .toSet

    //Merge in exceptions declared in calendar_dates.txt
    val except = exceptions.filter(e => e.date == dt)
    val add = except
      .filter{ _.addService }
      .map{ _.service_id }
      .toSet
    val remove = except
      .filter{ _.removeService }
      .map{_.service_id }
      .toSet

    (scheduled -- remove) ++ add
  }.toSeq
}