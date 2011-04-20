import java.io.File
import java.util.jar.Attributes
import java.util.jar.Attributes.Name._
import sbt._
import sbt.CompileOrder._

/** 
 * This project definition largely copied from Dean Wampler's sample at https://github.com/deanwampler/AkkaWebSampleExercise
 */
class Project(info: ProjectInfo) extends DefaultProject(info) with AkkaProject {

  object Repositories {
    lazy val EmbeddedRepo         = MavenRepository("Embedded Repo", (info.projectPath / "embedded-repo").asURL.toString)
    lazy val LocalMavenRepo       = MavenRepository("Local Maven Repo", (Path.userHome / ".m2" / "repository").asURL.toString)
    lazy val AkkaRepo             = MavenRepository("Akka Repository", "http://akka.io/repository")
    lazy val ScalaTestRepo        = MavenRepository("ScalaTest Repository", "http://www.scala-tools.org/repo-releases")
  }

  // akkaActor is already defined by the akk sbt plugin.
  // val akkaActor      = akkaModule("actor")
  
  import Repositories._
  lazy val embeddedRepo            = EmbeddedRepo   // This is the only exception, because the embedded repo is fast!
  lazy val localMavenRepo          = LocalMavenRepo // Second exception, also fast! ;-)

  override def repositories = Set(LocalMavenRepo, AkkaRepo, EmbeddedRepo, ScalaTestRepo)
   
  lazy val AKKA_VERSION          = "1.0"

  val akkaTypedActor = akkaModule("typed-actor")

  val scalatest = "org.scalatest" % "scalatest" % "1.3"
  val junit = "junit" % "junit" % "4.8.2"



}
// vim: set ts=4 sw=4 et:
