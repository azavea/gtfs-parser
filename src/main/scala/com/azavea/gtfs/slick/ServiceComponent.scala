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
    extends Table[(String, LocalDate, LocalDate, Int, Int, Int, Int, Int, Int, Int)](tag, "gtfs_calendar")
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

    def * = (id, start_date, end_date, monday, tuesday, wednesday, thursday, friday, saturday, sunday)
  }

  class CalendarDates(tag: Tag) extends Table[(String, LocalDate, Int)](tag, "gtfs_calendar_dates") {
    def id = column[String]("service_id")
    def date = column[LocalDate]("date")
    def exception_type = column[Int]("exception_type")

    def * = (id, date, exception_type)
  }

  object service {
    val queryCalendars = TableQuery[Calendars]
    val queryCalendarDates = TableQuery[CalendarDates]

    //So this is shitty to get here are some other useful subsets
    // - service calendar between dates
    // - service calendar for a single service (lol?)

    def getFullService(implicit session: Session): Calendar = {
      val weeks =
        queryCalendars.list
        .map{ case (sid, from, to, mon, tue, wed, th, fri, sat, sun) =>
          CalendarRec(sid, from, to, Array(mon, tue, wed,th, fri, sat, sun).map(_ == 1))
        }

      val exceptions =
        queryCalendarDates.list
        .map{ case (sid, date, ex) =>
          CalendarDateRec(sid, date, if (ex == 1) 'Add else 'Remove)
        }

      new Calendar(weeks, exceptions)
    }






  }
}

