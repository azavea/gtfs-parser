package com.azavea.gtfs

import com.github.nscala_time.time.Imports._

/**
 * Service availability calendar, combining both regular service and exceptions
 * Source: calendar.txt, calendar_dates.txt
 */
case class Service(recs: Seq[ServiceCalendar], exceptions: Seq[ServiceException]) {
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