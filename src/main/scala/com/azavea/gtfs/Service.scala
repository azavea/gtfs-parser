package com.azavea.gtfs

import com.github.nscala_time.time.Imports._

/**
 * Service availability calendar, combining both regular service and exceptions
 * Source: calendar.txt, calendar_dates.txt
 */
case class Service(week: ServiceCalendar, exceptions: Seq[ServiceException]) {
  def id = week.service_id

  def activeOn(dt: LocalDate) = {
    exceptions.find(_.date == dt) match {
      case Some(e) => e.addService    //check exception first
      case None => week.activeOn(dt)  //default
    }
  }
}

object Service {
   def collate(weeks: Seq[ServiceCalendar], dates: Seq[ServiceException]): Seq[Service] = {
    val datesMap = dates.groupBy(_.service_id)
    weeks.map { week =>
      Service(week, datesMap.getOrElse(week.service_id, Nil))
    }
  }
}