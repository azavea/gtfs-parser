package com.azavea.gtfs.slick

import com.azavea.gtfs._
import geotrellis.vector._
import geotrellis.slick.Projected

trait ShapesComponent {this: Profile =>
  import profile.simple._
  import gis._

  class Shapes(tag: Tag) extends Table[TripShape](tag, "gtfs_shape_geoms") {
    def id = column[String]("shape_id", O.PrimaryKey)
    def geom = column[Projected[Line]](geomColumnName)

    def * = (id, geom)  <> (TripShape.tupled, TripShape.unapply)
  }
  def shapesTable = TableQuery[Shapes]

  object shapes {
    def all(implicit session: Session): List[TripShape] =
      shapesTable.list

    def delete(id: String)(implicit session: Session): Boolean =
      shapesTable.filter(_.id === id).delete > 0

    def get(id: String)(implicit session: Session): Option[TripShape] =
      queryById(id).firstOption
    def apply(id: String)(implicit session: Session) = get(id)

    lazy val queryById = for {
      id <- Parameters[String]
      e <- shapesTable if e.id === id
    } yield e

    def insert(entity: TripShape)(implicit session: Session): Boolean = {
      shapesTable.forceInsert(entity) == 1
    }

    def update(entity: TripShape)(implicit session: Session): Boolean = {
      queryById(entity.id).update(entity) == 1
    }

  }
}

