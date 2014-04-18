package com.azavea.gtfs.slick

import com.azavea.gtfs._
import com.github.nscala_time.time.Imports._

trait ServiceComponent {this: Profile =>
  import profile.simple._
  import jodaSupport._

  class Calendars(tag: Tag)
    extends Table[ServiceCalendar](tag, "gtfs_calendar")
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

    val applyCalendar: (String, LocalDate, LocalDate, Int, Int, Int, Int, Int, Int, Int) => ServiceCalendar =
      { case (sid, from, to, mon, tue, wed, th, fri, sat, sun) =>
        ServiceCalendar(sid, from, to, Array[Int](mon, tue, wed,th, fri, sat, sun).map(_ == 1))
      }

    val unapplyCalendar: ServiceCalendar => Option[(String, LocalDate, LocalDate, Int, Int, Int, Int, Int, Int, Int)] =
      { c =>
          val w = c.week.map{ if (_) 1 else 0 }
          Some((c.service_id, c.start_date, c.end_date, w(0), w(1), w(2), w(3), w(4), w(5), w(6)))
      }

  }

  class CalendarDates(tag: Tag) extends Table[ServiceException](tag, "gtfs_calendar_dates") {
    def id = column[String]("service_id")
    def date = column[LocalDate]("date")
    def exception_type = column[Int]("exception_type")

    def * = (id, date, exception_type) <> (applyDates.tupled, unapplyDates)

    val applyDates: (String, LocalDate, Int) => ServiceException =
      { case (id, date, t) => ServiceException(id, date, if (t==1) 'Add else 'Remove) }

    val unapplyDates: ServiceException => Option[(String, LocalDate, Int)] =
      { d => Some((d.service_id, d.date, if (d.exception == 'Add) 1 else 2))

      }

  }

  object service {
    val queryCalendars = TableQuery[Calendars]
    val queryCalendarDates = TableQuery[CalendarDates]


    /** Get full service ... for all time */
    def full(implicit session: Session): Service = {
      Service(queryCalendars.list, queryCalendarDates.list)
    }

    /** Get service calendar effective between two dates */
    def get(start: LocalDate, end: LocalDate)(implicit session: Session): Service = {
      val weeks = for {
        w <- queryCalendars if (w.start_date >= start && w.start_date <= end) || (w.end_date >= start && w.end_date <= end)
      } yield w

      val exceptions = for {
        e <- queryCalendarDates if e.date >= start && e.date <= end
      } yield e

      Service(weeks.list, exceptions.list)
    }

    /** Get service calendar for single service */
    def get(id: String)(implicit session: Session): Service = {
      val weeks = for {
        w <- queryCalendars if w.id === id
      } yield w

      val exceptions = for {
        e <- queryCalendarDates if e.id === id
      } yield e

      Service(weeks.list, exceptions.list)
    }
  }
}

