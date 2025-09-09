ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "scala-essentioal-training",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % "2.8.5",
      "com.typesafe.akka" %% "akka-stream" % "2.8.5",
      "com.typesafe.akka" %% "akka-http" % "10.5.2",
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.5.2",
      "org.scalatest" %% "scalatest" % "3.2.17" % Test,
      "com.typesafe.akka" %% "akka-http-testkit" % "10.5.2" % Test,
      "com.typesafe.akka" %% "akka-testkit" % "2.8.5" % Test,
      "org.mockito" % "mockito-core" % "5.6.0" % Test,
      "org.scalatestplus" %% "mockito-4-6" % "3.2.15.0" % Test,
      "com.sksamuel.elastic4s" %% "elastic4s-client-esjava" % "8.11.0",
      "com.sksamuel.elastic4s" %% "elastic4s-json-jackson" % "8.11.0",
      "com.sksamuel.elastic4s" %% "elastic4s-testkit" % "8.11.0" % Test,
      "com.sksamuel.elastic4s" %% "elastic4s-effect-cats" % "8.11.0" % Test,
      "org.scalamock" %% "scalamock" % "5.2.0" % Test
    )
  )
