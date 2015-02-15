import NativePackagerKeys._
import com.typesafe.sbt.SbtNativePackager._

name := """kennerspiel"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

maintainer in Docker := "Philihp Busby <philihp@gmail.com>"

dockerBaseImage := "dockerfile/java:oracle-java8"

dockerExposedPorts in Docker := Seq(9000)

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "com.philihp" % "weblabora" % "2.0.2"
)

//play.Project.playJavaSettings
