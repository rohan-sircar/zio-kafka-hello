// The simplest possible sbt build file is just one line:

scalaVersion := "3.1.1"

resolvers += Resolver.mavenLocal

lazy val root = (project in file("."))
  .settings(
    name := "hello-world",
    organization := "wow.doge.hello",
    version := "1.0",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio-kafka" % "2.0.0-M2",
      "ch.qos.logback" % "logback-classic" % "1.2.11"
    ),
    scalacOptions ++= Seq(
      "-encoding",
      "UTF-8",
      "-deprecation",
      "-feature",
      "-unchecked",
      // "-Xlint",
      // "-Ywarn-numeric-widen",
      // "-Ymacro-annotations",
      // "utf-8", // Specify character encoding used by source files.
      "-explaintypes" // Explain type errors in more detail.
    ),
    javacOptions ++= Seq("-source", "11", "-target", "11"),
    fork := true
  )
