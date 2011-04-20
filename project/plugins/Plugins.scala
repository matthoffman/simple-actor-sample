import sbt._
 
class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
    val akkaRepo = "Akka Repo" at "http://akka.io/repository"
    val akkaPlugin = "se.scalablesolutions.akka" % "akka-sbt-plugin" % "1.0"
}

// vim: set ts=4 sw=4 et:
