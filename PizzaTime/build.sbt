lazy val root = (project in file("."))
  .settings(
    scalaVersion := "2.13.2",
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-target:jvm-1.8"
    ),
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    libraryDependencies ++= Seq(
      "junit" % "junit" % "4.12" % Test,
      "net.liftweb" %% "lift-json" % "3.4.1",
      "net.liftweb" %% "lift-json-ext" % "3.4.1",
      "com.novocode" % "junit-interface" % "0.11" % Test,
      "io.cucumber" % "cucumber-junit" % "6.0.0-RC2" % Test,
      "org.seleniumhq.selenium" % "selenium-java" % "4.0.0-alpha-5",
      "org.scalacheck" %% "scalacheck" % "1.14.3" % Test,
      "io.cucumber" %% "cucumber-scala" % "5.7.0",
      "org.scalactic" %% "scalactic" % "3.2.0-M4",
      "com.github.junitrunner" % "junitrunner" % "0.0.2",
      "org.scalatest" % "scalatest_2.13" % "3.1.2" % "test",
      "org.testfx" % "testfx-core" % "4.0.16-alpha" % Test,
      "org.testfx" % "testfx-junit" % "4.0.15-alpha" % Test,
    ),
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case _ => MergeStrategy.first
    },
    mainClass in Compile := Some("Main"),
    crossPaths := false, // https://github.com/sbt/junit-interface/issues/35
    Test / parallelExecution := false
  )
