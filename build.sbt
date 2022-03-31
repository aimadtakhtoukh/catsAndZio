ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "catsAndZIO"
  )

libraryDependencies += "org.typelevel" %% "cats-core" % "2.7.0"
libraryDependencies += "dev.zio" %% "zio" % "1.0.13"
libraryDependencies += "dev.zio" %% "zio-interop-cats" % "3.2.9.1"