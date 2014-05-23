import sbt._
import sbt.Keys._

object GtfsparserBuild extends Build {

  lazy val gtfsparser = Project(
    id = "gtfs-parser",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "gtfs-parser",
      organization := "com.azavea",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.3",
      // add other settings here
      libraryDependencies ++=
        Seq(
          "org.scalaz" %% "scalaz-core" % "7.0.6",
          "org.scalatest" % "scalatest_2.10" % "2.1.0" % "test",
          "com.github.nscala-time" %% "nscala-time" % "0.8.0",
          "com.typesafe.slick" %% "slick" % "2.0.1",
          "org.slf4j" % "slf4j-nop" % "1.6.4",
          "postgresql" % "postgresql" % "9.1-901.jdbc4",

          "commons-io" % "commons-io" % "2.4",

          "com.azavea.geotrellis" %% "geotrellis-feature" % "0.10.0-SNAPSHOT",
          "com.azavea.geotrellis" %% "geotrellis-proj4" % "0.10.0-SNAPSHOT",
          "com.azavea.geotrellis" %% "geotrellis-slick" % "0.10.0-SNAPSHOT",

          "org.joda" % "joda-convert" % "1.5",
          "com.github.tototoshi" %% "slick-joda-mapper" % "1.0.1"
        )
    )
  )

}
