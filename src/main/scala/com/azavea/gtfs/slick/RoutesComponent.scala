package com.azavea.gtfs.slick

import com.azavea.gtfs._
import com.azavea.gtfs.RouteType.RouteType

trait RoutesComponent {this: Profile =>
  import profile.simple._

  class Routes(tag: Tag) extends Table[Route](tag, "gtfs_routes") {
    def id = column[String]("route_id", O.PrimaryKey)
    def short_name = column[String]("route_short_name")
    def long_name = column[String]("route_long_name")
    def route_type = column[RouteType]("route_type")
    def agency_id = column[Option[String]]("agency_id")
    def route_desc = column[Option[String]]("route_desc")
    def route_url = column[Option[String]]("route_url")
    def route_color = column[Option[String]]("route_color")
    def route_text_color = column[Option[String]]("route_text_color")

    def * = (id, short_name, long_name, route_type, agency_id, route_desc, route_url, route_color, route_text_color)  <>
      (Route.tupled, Route.unapply)
  }
  val routesTable = TableQuery[Routes]

  object routes {
    def all(implicit session: Session): List[Route] =
      routesTable.list

    def delete(id: String)(implicit session: Session): Boolean =
      routesTable.filter(_.id === id).delete > 0

    def get(id: String)(implicit session: Session): Option[Route] =
      queryById(id).firstOption
    def apply(id: String)(implicit session: Session) = get(id)

    lazy val queryById = for {
      id <- Parameters[String]
      e <- routesTable if e.id === id
    } yield e

    def insert(entity: Route)(implicit session: Session): Boolean = {
      routesTable.forceInsert(entity) == 1
    }

    def update(entity: Route)(implicit session: Session): Boolean = {
      queryById(entity.id).update(entity) == 1
    }

  }
}

