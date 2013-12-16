name := "bbm473-blog"

version := "1.0-SNAPSHOT"

resolvers ++= Seq()

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.scala-tools" % "scala-stm_2.9.1" % "0.3"
)

play.Project.playJavaSettings
