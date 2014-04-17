package com.azavea.gtfs.slick

import com.azavea.gtfs._
import com.github.nscala_time.time.Imports._

trait ServiceComponent {this: Profile =>
  import profile.simple._
  import jodaSupport._


  /*
  service_id: ServiceId,
  start_date: LocalDate,
  end_date: LocalDate,
  week: Array[Boolean]
   */
  class Calendars(tag: Tag)
    extends Table[CalendarRec](tag, "gtfs_calendar")
  {
    def id = column[String]("service_id", O.PrimaryKey)
    def start_date = column[LocalDate]("start_date")
    def end_date = column[LocalDate]("end_date")
    def monday = column[Int]("monday")
    def tuesday = column[Int]("tuesday")
    def wednesday = column[Int]("wednesday")
    def thursday = column[Int]("thursday")
    def friday = column[Int]("friday")
    def saturday = column[Int]("saturday")
    def sunday = column[Int]("sunday")

    def * = (id, start_date, end_date, monday, tuesday, wednesday, thursday, friday, saturday, sunday) <>
      (applyCalendar.tupled, unapplyCalendar)

    val applyCalendar: (String, LocalDate, LocalDate, Int, Int, Int, Int, Int, Int, Int) => CalendarRec =
      { case (sid, from, to, mon, tue, wed, th, fri, sat, sun) =>
        CalendarRec(sid, from, to, Array[Int](mon, tue, wed,th, fri, sat, sun).map(_ == 1))
      }

    val unapplyCalendar: CalendarRec => Option[(String, LocalDate, LocalDate, Int, Int, Int, Int, Int, Int, Int)] =
      { c =>
          val w = c.week.map{ if (_) 1 else 0 }
          Some((c.service_id, c.start_date, c.end_date, w(0), w(1), w(2), w(3), w(4), w(5), w(6)))
      }

  }

  class CalendarDates(tag: Tag) extends Table[CalendarDateRec](tag, "gtfs_calendar_dates") {
    def id = column[String]("service_id")
    def date = column[LocalDate]("date")
    def exception_type = column[Int]("exception_type")

    def * = (id, date, exception_type) <> (applyDates.tupled, unapplyDates)

    val applyDates: (String, LocalDate, Int) => CalendarDateRec =
      { case (id, date, t) => CalendarDateRec(id, date, if (t==1) 'Add else 'Remove) }

    val unapplyDates: CalendarDateRec => Option[(String, LocalDate, Int)] =
      { d => Some((d.service_id, d.date, if (d.exception == 'Add) 1 else 2))

      }

  }

  object service {
    val queryCalendars = TableQuery[Calendars]
    val queryCalendarDates = TableQuery[CalendarDates]

    //So this is shitty to get here are some other useful subsets
    // - service calendar between dates
    // - service calendar for a single service (lol?)
    def getFullService(implicit session: Session): Calendar = {
      new Calendar(queryCalendars.list, queryCalendarDates.list)
    }
  }
}

