ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

lazy val root = (project in file("."))
  .settings(
    name := "ScalaProject"
  )

libraryDependencies ++= Seq(
  "org.apache.lucene" % "lucene-core" % "9.5.0",
  "org.apache.lucene" % "lucene-analyzers-common" % "8.11.2",
  "org.apache.lucene" % "lucene-queryparser" % "9.5.0"
)

