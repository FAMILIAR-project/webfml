name := "FMLApp"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
//  jdbc,
//  anorm,
  cache
)

play.Project.playScalaSettings
//publishArtifact in packageSrc := false

mappings in Universal ++=
  (baseDirectory.value / "repository" * "*" get) map
    (x => x -> ("repository/" + x.getName))