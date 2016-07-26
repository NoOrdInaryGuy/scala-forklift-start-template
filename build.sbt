name := "scala-forklift-project"

addCommandAlias("mgm", "migration_manager/run")

addCommandAlias("mg", "migrations/run")


lazy val commonSettings = Seq(
  version := "1.0",
  scalaVersion := "2.11.7",
  scalacOptions += "-deprecation",
  scalacOptions += "-feature",
  resolvers += Resolver.sonatypeRepo("snapshots")
)

lazy val loggingDependencies = List(
  "org.slf4j" % "slf4j-nop" % "1.6.4" // <- disables logging
)

lazy val slickDependencies = List(
  "com.typesafe.slick" %% "slick" % "3.1.1"
)

lazy val dbDependencies = List(
  "com.typesafe.slick" %% "slick-hikaricp" % "3.1.1"
  ,"com.h2database" % "h2" % "1.4.190"
)

lazy val forkliftDependencies = List(
  "com.liyaos" %% "scala-forklift-slick" % "0.2.1"
)

lazy val appDependencies = dbDependencies ++ loggingDependencies

lazy val migrationsDependencies =
  dbDependencies ++ forkliftDependencies ++ loggingDependencies

lazy val migrationManagerDependencies = dbDependencies ++ forkliftDependencies

lazy val app = Project("app",
  file("app")).dependsOn(generatedCode).settings(
  commonSettings:_*).settings {
  libraryDependencies ++= appDependencies
}

lazy val migrationManager = Project("migration_manager",
  file("migration_manager")).settings(
  commonSettings:_*).settings {
  libraryDependencies ++= migrationManagerDependencies
}

lazy val migrations = Project("migrations",
  file("migrations")).dependsOn(
  generatedCode, migrationManager).settings(
  commonSettings:_*).settings {
  libraryDependencies ++= migrationsDependencies
}

lazy val tools = Project("git-tools",
  file("tools/git")).settings(commonSettings:_*).settings {
  libraryDependencies ++= forkliftDependencies ++ List(
    "com.liyaos" %% "scala-forklift-git-tools" % "0.2.1",
    "com.typesafe" % "config" % "1.3.0",
    "org.eclipse.jgit" % "org.eclipse.jgit" % "4.0.1.201506240215-r"
  )
}

lazy val generatedCode = Project("generate_code",
  file("generated_code")).settings(commonSettings:_*).settings {
  libraryDependencies ++= slickDependencies
}