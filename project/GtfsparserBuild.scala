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
          "org.scalatest" % "scalatest_2.10" % "2.1.0" % "test",
          "com.github.nscala-time" %% "nscala-time" % "0.8.0"
        )
    )
  )

}
