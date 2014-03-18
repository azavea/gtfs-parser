package com.azavea.gtfs.util


case class Run[T](count: Int, bucket: List[T])

object RunLength {
  def apply[T](list: Seq[T])(implicit equalBy: (T,T)=>Boolean): List[Run[T]] = {
    list match {
      //base case
      case x :: Nil => Run(1, x :: Nil) :: Nil
      //result depends on the tail
      case x :: tail =>
        apply(tail) match {
          case Run(c, list) :: tail if equalBy(list.head, x) =>
            Run(c+1, x :: list) :: tail // update the head
          case rest =>
            Run(1, x :: Nil) :: rest    // append to the head
        }
    }
  }
}
