name := """api-photos"""
organization := "com.agile.engine"

version := "1.0.0"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala, DockerPlugin)

scalaVersion := "2.12.10"

val akkaVersion = "2.5.26"

libraryDependencies ++= Seq(
  ws,
  guice,
  // Functional Programming
  "org.typelevel" %% "cats-core" % "1.6.1",
  //Redis
  "net.debasishg"            %% "redisclient"          % "3.5",
  //
  "com.typesafe.akka"        %% "akka-actor"           % akkaVersion,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
  "org.mockito"            % "mockito-core"        % "3.0.0" % Test
)

javaOptions in Universal ++= Seq(
  "-Dpidfile.path=/dev/null"
)
