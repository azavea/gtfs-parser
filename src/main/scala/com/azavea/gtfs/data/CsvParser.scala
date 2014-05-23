package com.azavea.gtfs.data

import java.io.{FileInputStream, InputStreamReader, BufferedReader}
import java.nio.charset.Charset
import org.apache.commons.io.input._

class CsvParser[T](
    private val reader:BufferedReader
  ) extends Iterator[String=>Option[String]] {

  def parse(s: String): Array[String] =
    CsvParser.lineRegex.findAllIn(s).matchData.map(_.subgroups).flatten.toArray

  val head = parse(reader.readLine()).map(_.trim)
  val idx = head.zip(0 until head.length).toMap

  var line: String = null
  var rec:Array[String] = null

  val getCol: String=>Option[String] = {name: String =>
    idx.get(name) match {
      case Some(index) => Some(rec(index))
      case None => None
    }
  }

  override def hasNext: Boolean = {
    line = reader.readLine()
    line != null
  }

  override def next() = {
    rec = parse(line)
    getCol
  }
}

object CsvParser {
  val lineRegex = """(?:^|,)"?((?<=")[^"]*|[^,"]*)"?(?=,|$)""".r
  def fromPath[T](path:String) = {
    new CsvParser(
      new BufferedReader( new InputStreamReader(
        new BOMInputStream(new FileInputStream(path), false)))
    )
  }

}


