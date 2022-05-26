import Dependencies._

ThisBuild / organization := "app.instap"
ThisBuild / scalaVersion := "2.13.8"

lazy val `bacscan` =
  project
    .in(file("."))
    .settings(name := "bacscan")
    .settings(commonSettings)
    .settings(dependencies)
    .dependsOn(codeHouseBacnet4jWrapper)

lazy val codeHouseBacnet4jWrapper = {
  lazy val projectName = "bacnet4j-wrapper"
  lazy val branch = "sraster"
  lazy val repoUri = uri(s"https://github.com/Code-House/bacnet4j-wrapper.git#$branch")
  // lazy val repoUri = uri("git@github.com:Code-House/bacnet4j-wrapper.git#master")
  ProjectRef(repoUri, projectName)
}

lazy val commonSettings =
  compilerPlugins ++ commonScalacOptions ++ Seq(
    update / evictionWarningOptions := EvictionWarningOptions.empty
  )

lazy val compilerPlugins = Seq(
  addCompilerPlugin(com.olegpy.`better-monadic-for`),
  addCompilerPlugin(org.augustjune.`context-applied`),
  addCompilerPlugin(org.typelevel.`kind-projector`),
)

lazy val commonScalacOptions = Seq(
  Compile / console / scalacOptions := {
    (Compile / console / scalacOptions)
      .value
      .filterNot(_.contains("wartremover"))
      .filterNot(Scalac.Lint.toSet)
      .filterNot(Scalac.FatalWarnings.toSet) :+ "-Wconf:any:silent"
  },
  Test / console / scalacOptions :=
    (Compile / console / scalacOptions).value,
)

lazy val dependencies = Seq(
  libraryDependencies ++= Seq(
    // main dependencies
  ),
  libraryDependencies ++= Seq(
    com.github.alexarchambault.`scalacheck-shapeless_1.15`,
    org.scalacheck.scalacheck,
    org.scalatest.scalatest,
    org.scalatestplus.`scalacheck-1-16`,
    org.typelevel.`discipline-scalatest`,
  ).map(_ % Test),
)
