package com.azavea.gtfs.data

import java.io.{FileInputStream, InputStreamReader, BufferedReader}
import java.nio.charset.Charset

class CsvParser[T](
    private val reader:BufferedReader
  ) extends Iterator[String=>String] {

  def parse(s: String): Array[String] =
    CsvParser.lineRegex.findAllIn(s).matchData.map(_.subgroups).flatten.toArray

  val head = parse(reader.readLine())
  val idx = head.zip(0 until head.length).toMap

  var line: String = null
  var rec:Array[String] = null

  val getCol: String=>String = {name: String =>
    idx.get(name) match {
      case Some(index) => rec(index)
      case _ => ""
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
      new BufferedReader(
        new InputStreamReader(
          new FileInputStream(path),
            Charset.forName("UTF-8")))
    )
  }

}


