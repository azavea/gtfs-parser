package com.azavea.gtfs.slick

import com.azavea.gtfs._

trait StopsComponent {this: Profile =>
  import profile.simple._

  class Stops(tag: Tag) extends Table[Stop](tag, "gtfs_stops") {
    def id = column[String]("stop_id", O.PrimaryKey)
    def name = column[String]("stop_name")
    def desc = column[String]("stop_desc")
    def lat = column[Double]("stop_lat")
    def lon = column[Double]("stop_lon")
    def * = (id, name, desc, lat, lon)  <> (Stop.tupled, Stop.unapply)
  }
  val stopsTable = TableQuery[Stops]

  object stops {
    def delete(id: String)(implicit session: Session): Boolean =
      stopsTable.filter(_.id === id).delete > 0

    def get(id: String)(implicit session: Session): Option[Stop] =
      queryById(id).firstOption

    lazy val queryById = for {
      id <- Parameters[String]
      e <- stopsTable if e.id === id
    } yield e

    def insert(stop: Stop)(implicit session: Session): Boolean = {
      stopsTable.forceInsert(stop) == 1
    }

    def update(stop: Stop)(implicit session: Session): Boolean = {
      queryById(stop.id).update(stop) == 1
    }

  }
}

