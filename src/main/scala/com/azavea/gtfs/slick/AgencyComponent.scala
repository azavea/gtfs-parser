package com.azavea.gtfs.slick

import com.azavea.gtfs._
import com.azavea.gtfs.RouteType.RouteType
import com.azavea.gtfs.Agency
import com.azavea.gtfs.Route

trait AgencyComponent {this: Profile =>
  import profile.simple._

  class Agencies(tag: Tag) extends Table[Agency](tag, "gtfs_agency") {
    def id = column[Option[String]]("agency_id", O.PrimaryKey)
    def name = column[String]("agency_name")
    def url = column[String]("agency_url")
    def timezone = column[String]("agency_timezone")
    def lang = column[Option[String]]("agency_lang")
    def phone = column[Option[String]]("agency_phone")
    def fare_url = column[Option[String]]("agency_fare_url")

    def * = (id, name, url, timezone, lang, phone, fare_url)  <>
      (Agency.tupled, Agency.unapply)
  }
  val agenciesTable = TableQuery[Agencies]

  object agencies {
    def all(implicit session: Session): List[Agency] =
      agenciesTable.list

    def delete(id: Option[String])(implicit session: Session): Boolean =
      agenciesTable.filter(_.id === id).delete > 0

    def get(id: Option[String])(implicit session: Session): Option[Agency] =
      queryById(id).firstOption
    def apply(id: Option[String])(implicit session: Session) = get(id)

    lazy val queryById = for {
      id <- Parameters[Option[String]]
      e <- agenciesTable if e.id === id
    } yield e

    def insert(entity: Agency)(implicit session: Session): Boolean = {
      agenciesTable.forceInsert(entity) == 1
    }

    def update(entity: Agency)(implicit session: Session): Boolean = {
      queryById(entity.id).update(entity) == 1
    }

  }
}

