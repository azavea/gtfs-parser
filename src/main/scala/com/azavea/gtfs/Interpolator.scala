package com.azavea.gtfs


/**
 * Typeclass for Interpolator
 */
trait Interpolatable[T] {
  def x(t: T): Double
  def y(t: T): Double
  def missing(t: T): Boolean
  def update(t: T, x: Double): T
  def slope(t1: T, t2: T): Double =
    (y(t2) - y(t1)) / (x(t2) - x(t1))
}

object Interpolator {
  /** Sequence of items that is assumed to be increasing linearly */
  def interpolate[A](arr: Array[A])(implicit s: Interpolatable[A]): Array[A] = {
    def fillForward(i: Int): Int = {
      var j = i
      while (s.missing(arr(j))) j += 1   //find next filled
      val m = s.slope(arr(i-1), arr(j))  //assume a linear relation
      for (k <- i until j) {                //fill forward
        arr(k) = s.update(
          arr(k),
          s.y(arr(k-1)) + (s.x(arr(k)) - s.x(arr(k-1))) * m
        )
      }
      j
    }

    var i = 0
    while (i < arr.length) {
      if (s.missing(arr(i))) {
        i = fillForward(i)
      }else{
        i += 1
      }
    }

    arr
  }
}
