package com.azavea.gtfs.data

import java.io.{FileInputStream, InputStreamReader, BufferedReader}
import java.nio.charset.Charset
import org.apache.commons.io.input._
import org.apache.commons.csv._

class CsvParser[T](
    private val reader:BufferedReader
  ) extends Iterator[String=>Option[String]] {

  val lines = {
    val parser = new CSVParser(reader)
    parser.getAllValues
  }
  val head = lines(0)
  val idx = head.zip(0 until head.length).toMap

  var rec:Array[String] = null
  var curLine = 1

  val getCol: String=>Option[String] = {name: String =>
    idx.get(name) match {
      case Some(index) => Some(rec(index))
      case None => None
    }
  }

  override def hasNext: Boolean = {
    curLine < lines.length
  }

  override def next() = {
    rec = lines(curLine)
    curLine += 1
    getCol
  }
}

object CsvParser {
  def fromPath[T](path:String) = {
    new CsvParser(
      new BufferedReader( new InputStreamReader(
        new BOMInputStream(new FileInputStream(path), false), "UTF-8"))
    )
  }

}


