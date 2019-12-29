version := "0.1-SNAPSHOT"
organization := "zendesk"
scalaVersion := "2.13.1"
name := "zendesk"

scalacOptions ++= Seq(
  "-encoding", "UTF-8", // source files are in UTF-8
  "-deprecation", // warn about use of deprecated APIs
  "-unchecked", // warn about unchecked type parameters
  "-feature", // warn about misused language features
  "-language:higherKinds", // allow higher kinded types without `import scala.language.higherKinds`
  "-Xlint" // enable handy linter warnings
  //  "-Xfatal-warnings"      // turn compiler warnings into errors
)

scalacOptions in Test ++= Seq("-Yrangepos")

val catVersion = "2.0.0"
val circeVersion = "0.12.3"

libraryDependencies ++= Seq(
  // cats
  "org.typelevel" %% "cats-core" % catVersion,
  "org.typelevel" %% "cats-effect" % catVersion,

  // circe, JSON parser
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,

  // Joda time
  "joda-time" % "joda-time" % "2.10.5",

  // Test
  "org.specs2" %% "specs2-core" % "4.8.1" % "test",
  "org.specs2" %% "specs2-matcher" % "4.8.1" % "test",
  "org.specs2" %% "specs2-scalacheck" % "4.8.1" % "test",
  "com.47deg" %% "scalacheck-toolbox-datetime" % "0.3.1" % "test"

)

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)