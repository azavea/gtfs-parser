package com.azavea.gtfs

import org.scalatest._
import com.azavea.gtfs.data.{GtfsData, CsvParser}
import java.io.File

class CsvParserSpec extends FunSpec with Matchers {

  describe("CSV Parser") {
    it("should parse all the GTFS files in 'data' folder") {
//      val dataDir = new File("data")
//
//      for (folder <- dataDir.listFiles if folder.isDirectory) {
//        println(s"PARSING: $folder")
//        val data = GtfsData.fromFile(folder.toString)
//      }

      val data = GtfsData.fromFile("data/shenghai")
      data.stopTimes.foreach(println)
    }
  }
}