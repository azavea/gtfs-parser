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

  object stops {
    val query = TableQuery[Stops]

    def delete(id: String)(implicit session: Session): Boolean =
      query.filter(_.id === id).delete > 0

    def getById(id: String)(implicit session: Session): Option[Stop] =
      queryById(id).firstOption

    lazy val queryById = for {
      id <- Parameters[String]
      e <- query if e.id === id
    } yield e

    def insert(entity: Stop)(implicit session: Session): Boolean = {
      query.forceInsert(entity) == 1
    }

    def update(entity: Stop)(implicit session: Session): Boolean = {
      queryById(entity.id).update(entity) == 1
    }

  }
}

