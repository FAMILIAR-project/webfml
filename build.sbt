name := "FMLApp"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
//  jdbc,
//  anorm,
  cache
)

play.Project.playScalaSettings
doc in Compile <<= target.map(_ / "none") // don't generate API docs in dist