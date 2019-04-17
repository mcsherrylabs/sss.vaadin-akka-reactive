version := "0.4-SNAPSHOT"

scalaVersion := "2.12.6"

val vaadinVer = "8.5.2"

resolvers += "stepsoft" at "http://nexus.mcsherrylabs.com/repository/releases"

resolvers += "stepsoft-snapshots" at "http://nexus.mcsherrylabs.com/repository/snapshots"

updateOptions := updateOptions.value.withGigahorse(false)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.+",
  "com.vaadin" % "vaadin-server" % vaadinVer,
  "com.vaadin" % "vaadin-themes" % vaadinVer,
  "com.vaadin" % "vaadin-push" % vaadinVer,
  "com.vaadin" % "vaadin-client-compiler" % vaadinVer,
  "com.vaadin" % "vaadin-client-compiled" % vaadinVer,
  "com.mcsherrylabs" %% "sss-ancillary" % "1.5-SNAPSHOT"
)
