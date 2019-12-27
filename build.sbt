version := "0.1-SNAPSHOT"
organization := "zendesk"
scalaVersion := "2.13.1"
name := "zendesk"

scalacOptions ++= Seq(
  "-encoding", "UTF-8",   // source files are in UTF-8
  "-deprecation",         // warn about use of deprecated APIs
  "-unchecked",           // warn about unchecked type parameters
  "-feature",             // warn about misused language features
  "-language:higherKinds",// allow higher kinded types without `import scala.language.higherKinds`
  "-Xlint",               // enable handy linter warnings
  "-Xfatal-warnings"      // turn compiler warnings into errors
)

libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0"

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)
