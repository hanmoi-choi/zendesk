version      := "0.1-SNAPSHOT"
organization := "zendesk"
scalaVersion := "2.13.1"
name         := "zendesk"

scalacOptions ++= Seq(
  "-encoding",
  "UTF-8", // source files are in UTF-8
  "-deprecation", // warn about use of deprecated APIs
  "-unchecked", // warn about unchecked type parameters
  "-feature", // warn about misused language features
  "-language:higherKinds", // allow higher kinded types without `import scala.language.higherKinds`
  "-Xlint", // enable handy linter warnings
  "-Xfatal-warnings"      // turn compiler warnings into errors
)

scalacOptions in Test ++= Seq("-Yrangepos")

val catVersion = "2.0.0"
val circeVersion = "0.12.3"
val spec2Verion = "4.8.1"

libraryDependencies ++= Seq(
  // Parser Combinator
  "com.lihaoyi" %% "fastparse" % "2.2.2",
  // cats
  "org.typelevel" %% "cats-core"   % catVersion,
  "org.typelevel" %% "cats-effect" % catVersion,
  // circe, JSON parser
  "io.circe" %% "circe-core"    % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser"  % circeVersion,

  // Joda time
  "joda-time" % "joda-time" % "2.10.5",

  // Test
  "org.specs2" %% "specs2-core"                % spec2Verion % "test",
  "org.specs2" %% "specs2-matcher"             % spec2Verion % "test",
  "org.specs2" %% "specs2-scalacheck"          % spec2Verion % "test",
  "com.47deg" %% "scalacheck-toolbox-datetime" % "0.3.1"     % "test"
)

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)
