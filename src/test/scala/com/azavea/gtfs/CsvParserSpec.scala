package com.azavea.gtfs

import org.scalatest._
import com.azavea.gtfs.data.{GtfsData, CsvParser}
import com.github.nscala_time.time.Imports._
import com.azavea.gtfs.slick.DAO
import scala.slick.jdbc.JdbcBackend._
import geotrellis.slick._
import geotrellis.proj4._
import geotrellis.feature.reproject._
import geotrellis.slick._

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

      val data = GtfsData.fromFile("data/test")
    }
  }
}