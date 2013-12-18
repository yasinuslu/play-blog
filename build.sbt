name := "bbm473-blog"

version := "1.0-SNAPSHOT"

resolvers ++= Seq()

libraryDependencies ++= Seq(
  javaCore,
  javaJdbc,
  javaEbean,
  javaJpa,
  cache,
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.scala-tools" % "scala-stm_2.9.1" % "0.3",
  "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final"
)

play.Project.playJavaSettings
