package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json
import scala.collection.JavaConversions._
import fr.familiar.interpreter.FMLAssertionError
import fr.familiar.interpreter.FMLBasicInterpreter
import fr.familiar.interpreter.FMLFatalError
import fr.familiar.variable.FeatureModelVariable
import fr.familiar.variable.Variable
import fr.familiar.interpreter.FMLExtendedInterpreter
import java.io.File
import java.io.BufferedWriter
import java.io.FileWriter
import play.api.templates.Html
import play.api.libs.json.JsValue
import play.api.libs.json.JsNull
import play.api.libs.json.JsNull
import play.api.libs.json.JsArray
import play.api.libs.json.JsBoolean
import play.api.libs.json.JsString
import fr.familiar.operations.heuristics.InteractiveFMSynthesizer
import fr.familiar.operations.heuristics.metrics.SimmetricsMetric
import fr.familiar.operations.heuristics.metrics.MetricName
import fr.familiar.operations.heuristics.metrics.SmithWatermanMetric

object WebFMLInterpreter extends Controller with VariableHelper { 
  
  //val workspaceDir = "/Users/macher1/Documents/FMLTestRepository/"
  val workspaceDir = "/home/gbecan/workspaces/workspace_familiar/webfml/"
 
  
  val interp = new FMLBasicInterpreter()
  val KSYNTHESIS_INTERACTIVE_CMD = "ksynthesis --interactive" 
  
   def interpret(fmlCommand: String) = Action {
    	request =>    	   	
		try {
		    interp.reset() 
			val lastVar = interp.eval(fmlCommand);
			
		    val allVarIDs = interp.getAllIdentifiers() ;
			val rs : scala.xml.Elem = 
 		      <p>
			  <ul class="list-unstyled">
				{allVarIDs.map(varID => <li>{mkVariableURL(varID)}</li>)}
				</ul>
		      <p id="lastValueFML" class="alert alert-success"> {lastVar.getIdentifier() + " = " + lastVar.getValue()} </p> 
		    </p>
			Ok(Json.toJson(rs.toString));
		}
		catch {
			case e  @ (_ : FMLAssertionError | _: FMLFatalError) => 
			  val error = <div class="alert alert-danger">{e.getMessage()}</div>
			  Ok(Json.toJson(error.toString))  

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
        val synthesizer = new InteractiveFMSynthesizer(fmv, parentHeuristic, null, clusterHeuristic, clusterThreshold)
        
        // Ranking lists
//        val ig = synthesizer.getImplicationGraph()
//        val rl = ig.vertices().map(ft => Map (ft -> ig.outgoingEdges(ft).map(e => ig.getTarget(e))))
        val rankingList = synthesizer.getParentCandidates().map(pc => (pc.getKey() -> pc.getValue().toSeq)).toMap

        // Clusters
        val clusters = synthesizer.getSimilarityClusters().map(c => c.toSet).toSet
        
        // Cliques
        val cliques = synthesizer.getCliques().map(c => c.toSet).toSet
        
        // TODO previsualization
        
         Ok (Json.toJson(Map ("id" -> Json.toJson(assignID), 
          "targetID" -> Json.toJson(targetID),
//          "rankinglist" -> Json.toJson(rl)
          "rankinglist" -> Json.toJson(rankingList.map(pc => Json.toJson(Map(
              "feature" -> Json.toJson(pc._1), 
              "parents" -> Json.toJson(pc._2))))),
          "clusters" -> Json.toJson(clusters),
          "cliques" -> Json.toJson(cliques)
          )
          )) 
      }
      else {
        Ok ("Error, variable " + targetID + " is not a formula or feature model") // can't remember how Error are properly handled in Play!
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
      val source = scala.io.Source.fromFile(workspaceDir + filename)
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
		  .filter(f => """.*\.fml$""".r.findFirstIn(f.getName).isDefined)
		  .sortBy(f => mkProperName(f))
	// TODO JSON 
		  
	 
		  
	  Ok(Json.toJson(Seq(_fileListToJSON(new File(workspaceDir)))))
	  //Ok(_fileListToJSON(new File(workspaceDir)))
	      // <ul>{ files.map(f => <li> { mkFMLFileName(f) } </li>) } </ul>.toString)
   }
    
    def _fileListToJSON(file : File) : JsValue  = {
      if (!file.isDirectory()) {
        if (""".*\.fml$""".r.findFirstIn(file.getName).isDefined)
        	Json.toJson(Map ("label" -> Json.toJson(file.getName()),
        	 "leaf" -> JsBoolean(true),
        	 "type" -> JsString("check"),
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
      f.getAbsolutePath.replaceFirst(workspaceDir, "")
    }
   
   def recursiveListFiles(f: File): Array[File] = {
		   val these = f.listFiles
		   these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
   }
  
 

}
