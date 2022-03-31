ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "catsAndZIO"
  )

libraryDependencies += "org.typelevel" %% "cats-core" % "2.7.0"
libraryDependencies += "dev.zio" %% "zio-streams" % "1.0.13"