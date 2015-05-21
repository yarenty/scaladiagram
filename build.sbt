organization := "net.invalidkeyword"

name := "scaladiagrams"

version := "1.0"

scalaVersion := "2.11.6"

scalacOptions ++= Seq("-unchecked", "-deprecation")

seq(assemblySettings: _*)

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
  "org.rogach" % "scallop_2.11" % "0.9.5",
  "org.scala-lang" % "scala-parser-combinators" % "2.11.0-M4"
)

resolvers += "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"


