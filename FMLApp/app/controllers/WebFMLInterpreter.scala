package controllers

import play.api.mvc._
import play.api.libs.json.Json
import scala.collection.JavaConversions._
import fr.familiar.interpreter.FMLAssertionError
import fr.familiar.interpreter.FMLBasicInterpreter
import fr.familiar.interpreter.FMLFatalError
import fr.familiar.variable.FeatureModelVariable
import java.io.File
import java.io.BufferedWriter
import java.io.FileWriter
import play.api.libs.json.JsValue
import play.api.libs.json.JsNull
import play.api.libs.json.JsArray
import play.api.libs.json.JsBoolean
import play.api.libs.json.JsString
import fr.familiar.operations.heuristics.InteractiveFMSynthesizer
import fr.familiar.operations.heuristics.metrics.SimmetricsMetric
import fr.familiar.operations.heuristics.metrics.MetricName
import fr.familiar.operations.heuristics.metrics.SmithWatermanMetric
import fr.familiar.gui.synthesis.KeyValue
import gsd.synthesis.FeatureEdge
import gsd.synthesis.FeatureType
import fr.familiar.operations.heuristics.Heuristic
import fr.familiar.operations.heuristics.metrics.AlwaysZeroMetric
import fr.familiar.operations.heuristics.metrics.RandomMetric
import fr.familiar.operations.heuristics.metrics.LevenshteinMetric

object WebFMLInterpreter extends Controller with VariableHelper {

  val workspaceDir = "repository/"   // "/Users/macher1/Documents/FMLTestRepository/"
  //  val workspaceDir = "/home/gbecan/workspaces/workspace_familiar/webfml/"
  //val workspaceDir = "/home/leiko/dev/webfml/FMLApp/"

  val interp = new FMLBasicInterpreter()
  val KSYNTHESIS_INTERACTIVE_CMD = "ksynthesis --interactive"
  var synthesizer : InteractiveFMSynthesizer = _
  var heuristics : Map[String, Heuristic] = Map.empty

  def interpret(fmlCommand: String) = Action {
    request =>
      try {
        interp.reset()
        val lastVar = interp.eval(fmlCommand);

        val allVarIDs = interp.getAllIdentifiers() ;
        val rs : scala.xml.Elem =
          <p>
            <ul class="unstyled">
              {allVarIDs.map(varID => <li>{mkVariableURL(varID)}</li>)}
            </ul>
            <p id="lastValueFML" class="alert alert-success"> {lastVar.getIdentifier() + " = " + lastVar.getValue()} </p>
          </p>
      
//        Ok(Json.toJson(rs.toString));
          Ok(Json.toJson(Map(
        		  "varIDs" -> Json.toJson(allVarIDs.toList),
        		  "lastVar" -> Json.toJson(lastVar.getIdentifier() + " = " + lastVar.getValue())
          )))
      }
      catch {
        case e  @ (_ : FMLAssertionError | _: FMLFatalError | _: Exception) =>
          val error = <div class="alert alert-danger">{e.getStackTraceString}</div>
//          Ok(Json.toJson(error.toString))
           Ok(Json.toJson(Map(
        		  "varIDs" -> Json.toJson(""),
        		  "lastVar" -> Json.toJson(e.getStackTraceString)
          )))

      }


  }

  /**
   * Important: we do not reset the environment (normal: we want to execute a prompt command) contrary to interpret
   * FIXME change a bit the names of the methods interpret/evalPrompt
   * FIXME update the UI of variables list  
   * @param fmlCommand
   * @return The value of the last command executed 
   */
  def evalPrompt (fmlCommand: String) = Action {
    request =>
      assert (!fmlCommand.contains(KSYNTHESIS_INTERACTIVE_CMD))
      try {
        val lastVar = interp.eval(fmlCommand)
        if (lastVar != null)
          Ok("" + lastVar.getValue())
        else
          Ok ("void")
      }
      catch {
        case e : FMLAssertionError => BadRequest(e.getMessage())
        case e : FMLFatalError =>  BadRequest(e.getMessage())
      }


  }

  def ksynthesis(fmlCommand: String) = Action {

    request =>
      assert (fmlCommand.contains(KSYNTHESIS_INTERACTIVE_CMD))

      val targetID = fmlCommand.substring(fmlCommand.indexOf(KSYNTHESIS_INTERACTIVE_CMD) + KSYNTHESIS_INTERACTIVE_CMD.length() + 1)
      var assignID = ""
      if (fmlCommand.indexOf("=") != -1)
        assignID = fmlCommand.substring(0, fmlCommand.indexOf("=")).trim()

      val v = interp.eval(targetID)
      if (v.isInstanceOf[FeatureModelVariable]) {
        val fmv = v.asInstanceOf[FeatureModelVariable]
        val parentHeuristic = new SmithWatermanMetric
        val clusterHeuristic = new SmithWatermanMetric
        val clusterThreshold = 0.5
        synthesizer = new InteractiveFMSynthesizer(fmv, parentHeuristic, null, clusterHeuristic, clusterThreshold)

        var jsonMap = Map ("id" -> Json.toJson(assignID),
          "targetID" -> Json.toJson(targetID))
        jsonMap =jsonMap ++ synthesizerInformationToJSON(synthesizer)

        Ok (Json.toJson(jsonMap))
      }
      else {
        Ok ("Error, variable " + targetID + " is not a formula or feature model") // can't remember how Error are properly handled in Play!
      }
  }
  
  def synthesizerInformationToJSON(synthesizer : InteractiveFMSynthesizer) : Map[String, JsValue] = {
    if (synthesizer == null) {
       Map.empty 
    } else {
	    val rankingList = synthesizer.getParentCandidates().map(pc => (pc.getKey() -> pc.getValue().toSeq)).toMap
	    val jsonRankingLists = rankingListsToJSON(rankingList)
	
	    // Clusters
	    val clusters = synthesizer.getSimilarityClusters().map(c => c.toSet).toSet
	
	    // Cliques
	    val cliques = synthesizer.getCliques().map(c => c.toSet).toSet
	    
	    // FM preview 
	    val fm = synthesizer.getFeatureModelVariable()
	    val diagram = fm.getFm().getDiagram()
	    diagram.addEdge(diagram.getTopVertex(), diagram.getBottomVertex(), FeatureEdge.HIERARCHY)
	    
	    Map("fm" -> fmToJson(fm),
	      "rankingList" -> Json.toJson(rankingList.map(pc => Json.toJson(Map(
	      "feature" -> Json.toJson(pc._1), 
	      "parents" -> Json.toJson(pc._2))))),
	      "clusters" -> Json.toJson(clusters),
	      "cliques" -> Json.toJson(cliques))  
    }
    
        
  }
  
  def rankingListsToJSON(rankingLists : Map[String, Seq[String]]) : JsValue = {
    Json.toJson(
        rankingLists.map(list => Map(
        		"label" -> Json.toJson(list._1),
        		"children" -> Json.toJson(list._2.map(child => Map("label" -> Json.toJson(child), 
        														"type" -> JsString("check"), 
        															"leaf" -> JsBoolean(true)
        		    
        		)))
    )))
  }

  def fmToJson(fm : FeatureModelVariable) : JsValue = {
    val diagram = fm.getFm().getDiagram()
    
    val jsonNodes = for (v <- diagram.vertices() 
        if !v.isTop() && !v.isBottom()) 
      yield Json.toJson(v.getFeature())
    
   val jsonEdges = for (e <- diagram.edges() 
       if e.getType() == FeatureEdge.HIERARCHY;
       if !diagram.getSource(e).isTop() && !diagram.getSource(e).isBottom();
     if !diagram.getTarget(e).isTop() && !diagram.getTarget(e).isBottom())
      yield Json.toJson(Map(
        "source" -> diagram.getSource(e).getFeature(),
        "target" -> diagram.getTarget(e).getFeature()
    ))
    
    val jsonFM = Json.toJson(Map(
        "nodes" -> jsonNodes,
        "edges" -> jsonEdges
    )) 
   
    jsonFM
  }
  
  def featureModelToJson (id : String) = Action {
    request =>
      
     val v = interp.eval(id)
      if (v.isInstanceOf[FeatureModelVariable]) {
        val fmv = v.asInstanceOf[FeatureModelVariable]
        Ok (new JSonFeatureModel(fmv).toJSon())
      }
      else {
        Ok ("Error, variable " + id + " is not a feature model!") // can't remember how Error are properly handled in Play!
      }
  }


  def variable (id : String) = Action {
    Ok(Json.toJson(interp.eval(id).getValue()));
  }


  def reset () = Action {request =>
    interp.reset()
    Ok("")
  }

  /*
   * Workspace management
   */

  def loadFile (filename : String) = Action {

    // security issues of course
    val workspaceBase = "" // new File (workspaceDir).getParent() + "/" 
    val source = scala.io.Source.fromFile(workspaceBase + filename)
    val lines = source.mkString
    source.close()
    Ok(Json.toJson(lines));
  }


  def saveAs (content : String, filename : String) = Action {

    // security issues of course
    if (filename.isEmpty())
      BadRequest("Filename is empty")
    else {
      val f = new File(workspaceDir + filename)
      val bw  = new BufferedWriter(new FileWriter(f))
      bw.write(content)
      bw.close()
      Ok("");
    }
  }

  def listFiles() = Action {
    val files = recursiveListFiles(new File(workspaceDir))
      .filter(f => (f.getName().endsWith("fml") || f.getName().endsWith("dimacs"))) //""".*\.fml$""".r.findFirstIn(f.getName).isDefined)
      .sortBy(f => mkProperName(f))
    // TODO JSON



    Ok(Json.toJson(Seq(_fileListToJSON(new File(workspaceDir)))))
    //Ok(_fileListToJSON(new File(workspaceDir)))
    // <ul>{ files.map(f => <li> { mkFMLFileName(f) } </li>) } </ul>.toString)
  }

  def _fileListToJSON(file : File) : JsValue  = {
    if (!file.isDirectory()) {
      if (""".*\.fml$""".r.findFirstIn(file.getName).isDefined || 
    		  """.*\.dimacs$""".r.findFirstIn(file.getName).isDefined    
      )
        Json.toJson(Map ("label" -> Json.toJson(file.getName()),
          "leaf" -> JsBoolean(true),
          "type" -> JsString("file"),
          "id" -> JsString("fml" + file.getName())
        ))
      else
        JsNull
    }
    else {
      val files = file.listFiles
      if (!files.isEmpty) {
        val json = files.map(f =>
          _fileListToJSON(f)
        )
        // "children:" + json.filter(j => !j.isInstanceOf[JsNull]).map() + " label:" + file.getName()
        //json.map(j => Json.toJson(j)) // filter(j => !j.isInstanceOf[JsNull]).
        Json.toJson(Map ("label" -> Json.toJson(file.getName()), "children" ->
          JsArray(json.filter(j => j match {case JsNull => false; case _ => true})),
          "expanded" -> JsBoolean(true)
        ))
      }
      else {
        Json.toJson(Map ("label" -> Json.toJson(file.getName()),
          "type" -> JsString("io")
        )
        )
      }
    }
  }

  def mkFMLFileName(f: File) = {
    val name =   mkProperName(f)
    val callback = "loadFile('" + name + "');"
    <a onclick={callback}>{name}</a>
  }

  // print the parent directory name if the parent directory is not the root of the workspace (relative)
  // FIXME
  def mkProperName(f : File) = {
    f.getAbsolutePath // .replaceFirst(workspaceDir, "")
  }

  def recursiveListFiles(f: File): Array[File] = {
    val these = f.listFiles
    these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
  }



  def selectParent(child : String, parent : String) = Action {
    synthesizer.selectParent(child, parent)
    Ok(Json.toJson(synthesizerInformationToJSON(synthesizer)))
  }
  
  def completeFM() = Action {
    val completedFM = synthesizer.computeCompleteFeatureModel()
    var synthesizerInfo = synthesizerInformationToJSON(synthesizer)
    synthesizerInfo = synthesizerInfo + ("fm" -> fmToJson(completedFM))
    Ok(Json.toJson(synthesizerInfo))
  }
  
  def getHeuristics() = Action {
    if (heuristics.isEmpty) {
      val zero = new AlwaysZeroMetric
      heuristics += zero.getName() -> zero
      val random = new RandomMetric
      heuristics += random.getName() -> random
      val sw = new SmithWatermanMetric
      heuristics += sw.getName() -> sw
      val lv = new LevenshteinMetric
      heuristics += lv.getName() -> lv
    }
    
    Ok(Json.toJson(heuristics.keys))
  }
  
  def setRankingListsHeuristic(heuristicName : String) = Action {
    
    val heuristic = if (heuristics.contains(heuristicName)) {
      heuristics(heuristicName)
    } else {
      new AlwaysZeroMetric
    }

    if (synthesizer != null) {
    	synthesizer.setParentSimilarityMetric(heuristic)  
    }
    
    Ok(Json.toJson(synthesizerInformationToJSON(synthesizer)))
  } 
  
  def setClusteringParameters(heuristicName : String, threshold : Double) = Action {
    val heuristic = if (heuristics.contains(heuristicName)) {
      heuristics(heuristicName)
    } else {
      new AlwaysZeroMetric
    }
    
    if (synthesizer != null) {
    	synthesizer.setClusteringParameters(heuristic, threshold)
    }
    
    Ok(Json.toJson(synthesizerInformationToJSON(synthesizer)))
  }

}
