package com.azavea.gtfs.slick

import collection.mutable


class Cache[K, V](f: K => V) {
  val map = mutable.HashMap.empty[K,V]

  def apply(key: K) = {
    map.get(key) match {
      case Some(value) =>
        value
      case None =>
        val value = f(key)
        map(key) = value
        value
    }
  }

  def clear = map.clear()

  def update(key: K, value: V) = map(key) = value
}
