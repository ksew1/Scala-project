ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

lazy val root = (project in file("."))
  .settings(
    name := "ScalaProject"
  )

libraryDependencies ++= Seq(
  "org.apache.lucene" % "lucene-core" % "9.5.0",
  "org.apache.lucene" % "lucene-analyzers-common" % "8.11.2",
  "org.apache.lucene" % "lucene-queryparser" % "9.5.0",
  "org.json4s" %% "json4s-core" % "4.0.6",
  "org.json4s" %% "json4s-native" % "4.0.6"
)
libraryDependencies += "org.scalafx" %% "scalafx" % "20.0.0-R31"
libraryDependencies += "org.jsoup" % "jsoup" % "1.14.2"

libraryDependencies ++= Seq(
  "com.softwaremill.sttp.client3" %% "core" % "3.3.13",
  "io.circe" %% "circe-generic" % "0.14.1",
  "io.circe" %% "circe-parser" % "0.14.1"
)
