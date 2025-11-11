// give the user a nice default project!
inThisBuild(
  Seq(
    organization := "$organization$",
    scalaVersion := "$scalaVersion$",
    run / fork := true
  )
)

lazy val root = (project in file(".")).settings(
  name := "$name$"
)

libraryDependencies += "dev.cheleb" %% "zio-pravega" % "$zioPravegaVersion$"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.5.21"
