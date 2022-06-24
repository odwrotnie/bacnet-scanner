import Dependencies._

ThisBuild / organization := "app.instap"
ThisBuild / scalaVersion := "2.13.8"

lazy val `bacscan` =
  project
    .in(file("."))
    .settings(name := "bacscan")
    .settings(commonSettings)
    .settings(dependencies)
    .settings(libraryDependencies ++= bacnetDependencies)
    .settings(
      assembly / assemblyJarName := "bacscan.jar"
    )

// lazy val codeHouseBacnet4jWrapper = {
//   lazy val projectName = "bacnet4j-wrapper"
//   lazy val branch = "sraster"
//   lazy val repoUri = uri(s"https://github.com/Code-House/bacnet4j-wrapper.git#$branch")
//   // lazy val repoUri = uri("git@github.com:Code-House/bacnet4j-wrapper.git#master")
//   ProjectRef(repoUri, projectName)
// }

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

ThisBuild / resolvers += "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository"
val BACNET4J_VERSION = "1.2.2-SNAPSHOT"
lazy val bacnetDependencies = Seq(
  "org.code-house.bacnet4j" % "api"              % BACNET4J_VERSION,
  "org.code-house.bacnet4j" % "ip"               % BACNET4J_VERSION,
  "org.code-house.bacnet4j" % "mstp"             % BACNET4J_VERSION,
  "org.code-house.bacnet4j" % "bacnet4j-wrapper" % BACNET4J_VERSION
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
    org.typelevel.`discipline-scalatest`
  ).map(_ % Test),
)
