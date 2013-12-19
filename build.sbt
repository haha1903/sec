name := "sec"

version := "1.0"

libraryDependencies ++= List(
  // use the right Slick version here:
  "com.typesafe.slick" %% "slick" % "2.0.0-M3",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.h2database" % "h2" % "1.3.170",
  "commons-dbcp" % "commons-dbcp" % "1.4",
  "org.specs2" %% "specs2" % "2.3.6" % "test"
)