package com.azavea.gtfs.slick


trait Unique {
  val id: String
}

trait CrudComponent{ this: Profile =>
  import profile.simple._

  abstract class IndexedTable[T](tag: Tag, name:String) extends Table[T](tag, name){
    def id:Column[String]
  }

  trait Crud[T <: IndexedTable[A], A <: Unique] {

    val query: TableQuery[T]

    def count()(implicit session: Session): Int = query.length.run

    def all()(implicit session: Session): List[A] = query.list

    def delete(id: String)(implicit session: Session): Boolean = query.filter(_.id === id).delete > 0

    def getById(id: String)(implicit session: Session): Option[A] = queryById(id).firstOption

    lazy val queryById = for {
      id <- Parameters[String]
      e <- query if e.id === id
    } yield e

    def insert(entity: A)(implicit session: Session): Boolean = {
      //GTFS specifies string PKs. This means they can not be auto-incremented
      query.forceInsert(entity) == 1
    }

    def update(entity: A)(implicit session: Session): Boolean = {
      queryById(entity.id).update(entity) == 1
    }
  }
}