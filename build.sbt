name := """moliclo_back"""

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(

  "com.typesafe.akka" %% "akka-actor" % "2.3.11",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.11" % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",

  "javax.inject" % "javax.inject" % "1",
  "org.json4s" %% "json4s-native" % "3.5.0",

  "com.typesafe.akka" %% "akka-http" % "10.0.5",
  "com.typesafe.slick" %% "slick" % "3.2.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.0",
  "mysql" % "mysql-connector-java" % "5.1.41"
)

enablePlugins(JavaAppPackaging)
enablePlugins(JavaServerAppPackaging)
