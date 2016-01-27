package controllers

import java.io.BufferedWriter
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.FileSystems
import java.io.FileWriter

import scala.Array.canBuildFrom
import scala.collection.JavaConversions
import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.asScalaSet
import scala.collection.immutable.List
import scala.collection.mutable.ListBuffer
import scala.io.Source

import org.apache.commons.io.FileUtils

import foreverse.ksynthesis.Heuristic
import foreverse.ksynthesis.InteractiveFMSynthesizer
import foreverse.ksynthesis.metrics.AlwaysZeroMetric
import foreverse.ksynthesis.metrics.LevenshteinMetric
import foreverse.ksynthesis.metrics.PathLengthMetric
import foreverse.ksynthesis.metrics.RandomMetric
import foreverse.ksynthesis.metrics.SmithWatermanMetric
import foreverse.ksynthesis.metrics.WuPalmerMetric
import fr.familiar.interpreter.FMLAssertionError
import fr.familiar.interpreter.FMLBasicInterpreter
import fr.familiar.interpreter.FMLFatalError
import fr.familiar.variable.FeatureModelVariable
import gsd.synthesis.FeatureEdge
import play.api.libs.json.JsArray
import play.api.libs.json.JsBoolean
import play.api.libs.json.JsNull
import play.api.libs.json.JsString
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.Controller

import play.api.templates.Html

import play.api.Play.current
import play.Logger


object WebFMLInterpreter extends Controller with VariableHelper {

  val workspaceDir = "repository/"  // "/Users/macher1/Documents/FMLTestRepository/"
  //  val workspaceDir = "/home/gbecan/workspaces/workspace_familiar/webfml/"
  //val workspaceDir = "/home/leiko/dev/webfml/FMLApp/"

  //val interp = new FMLBasicInterpreter()
  val KSYNTHESIS_INTERACTIVE_CMD = "ksynthesis --interactive"
  var synthesizer : InteractiveFMSynthesizer = _
  var heuristics : Map[String, Heuristic] = Map.empty

  /**
   *  Function which send the compile result
   *  @param : fmlCommand : the code
   *  @return : result in json format
   */
  def interpret(fmlCommand: String) = Action {
    request =>
      try {
        val interp = FamiliarIDEController.mkInterpreter(request.session)
        interp.reset() // in any case
        val lastVar = interp.eval(fmlCommand);

        val allVarIDs = interp.getAllIdentifiers() ;
        val rs : scala.xml.Elem =
          <p>
            <ul class="unstyled">
              {allVarIDs.map(varID => <li>{mkVariableURL(varID)}</li>)}
            </ul>
            <p id="lastValueFML" class="alert alert-success"> {lastVar.getIdentifier() + " = " + lastVar.getValue()} </p>
          </p>

        request.session.get("id").foreach { user =>
          Logger.info("Interpreting for user (ID): " + user)
        }

//        Ok(Json.toJson(rs.toString));
          Ok(Json.toJson(Map(
        		  "varIDs" -> Json.toJson(allVarIDs.toList),
        		  "lastVar" -> Json.toJson(lastVar.getIdentifier() + " = " + lastVar.getValue())
          )))
      }
      catch {
        case e  @ (_ : FMLAssertionError | _: FMLFatalError | _: Exception) =>
          BadRequest(Json.toJson(Map("msgError" -> e.getStackTraceString)))
          /* Ok(Json.toJson(Map(
        		  "varIDs" -> Json.toJson(""),
        		  "lastVar" -> Json.toJson(e.getStackTraceString)
          )))*/

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
        val interp = FamiliarIDEController.mkInterpreter(request.session)
        val lastVar = interp.eval(fmlCommand)
        val allVarIDs = interp.getAllIdentifiers() ;

        if (lastVar != null)
          Ok(Json.toJson(Map(
        		  "varIDs" -> Json.toJson(allVarIDs.toList),
        		  "lastVar" -> Json.toJson(lastVar.getIdentifier() + " = " + lastVar.getValue())
          )))
        else
          Ok(Json.toJson(Map(
        		  "varIDs" -> Json.toJson(allVarIDs.toList),
        		  "lastVar" -> Json.toJson("")
          )))
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
      val interp = FamiliarIDEController.mkInterpreter(request.session)
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

    	// Ranking lists
	    val rankingLists = synthesizer.getParentCandidates().map(pc => (pc.getKey() -> pc.getValue().toSeq)).toMap
	    val originalRankingLists = synthesizer.getOriginalParentCandidates().map(pc => (pc.getKey() -> pc.getValue().toSeq)).toMap

	    // Clusters
	    val clusters = synthesizer.getSimilarityClusters().map(c => c.toSet).toSet

	    // Cliques
	    val cliques = synthesizer.getCliques().map(c => c.toSet).toSet

	    // FM preview
	    val fm = synthesizer.getFeatureModelVariable()
	    val diagram = fm.getFm().getDiagram()
	    diagram.addEdge(diagram.getTopVertex(), diagram.getBottomVertex(), FeatureEdge.HIERARCHY)

	    val roots = synthesizer.getImplicationGraph().reduceCliques().roots().iterator().next();
	    Map("fm" -> fmToJson(fm),
	      "rankingLists" -> Json.toJson(rankingLists.map(pc => Json.toJson(Map(
		      "feature" -> Json.toJson(pc._1),
		      "parents" -> Json.toJson(pc._2),
		      "parentInFM" -> Json.toJson(getParent(pc._1, fm)),
		      "originalParents" -> Json.toJson(originalRankingLists(pc._1)),
		      "isPossibleRoot" -> Json.toJson(roots.contains(pc._1))
		  )))),
	      "clusters" -> Json.toJson(
	          clusters.map(c =>
	            c.map(f =>
	              Json.toJson(Map(
	                		  "name" -> Json.toJson(f),
	                		  "parentInFM" -> Json.toJson(getParent(f, fm))
          ))))),
	      "cliques" -> Json.toJson(
	          cliques.map(c =>
	            c.map(f =>
	              Json.toJson(Map(
	                		  "name" -> Json.toJson(f),
	                		  "parentInFM" -> Json.toJson(getParent(f, fm))
          )))))
	    )

    }


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

  def getParent(feature : String, fm : FeatureModelVariable) : String = {
    val diagram = fm.getFm().getDiagram();
    try {
	    val parents = diagram.parents(diagram.findVertex(feature))
	    if (parents.size() == 1 ) {
	      parents.iterator().next().getFeature()
	    } else {
	      null
	    }
    } catch {
      case _ : Throwable => null
    }

  }

  def featureModelToJson (id : String) = Action {
    request =>
      val interp = FamiliarIDEController.mkInterpreter(request.session)
     val v = interp.eval(id)
      if (v.isInstanceOf[FeatureModelVariable]) {
        val fmv = v.asInstanceOf[FeatureModelVariable]
        Ok (new JSonFeatureModel(fmv).toJSon())
      }
      else {
        Ok ("Error, variable " + id + " is not a feature model!") // can't remember how Error are properly handled in Play!
      }
  }


  def variable (id : String) = Action { request =>
    val interp = FamiliarIDEController.mkInterpreter(request.session)
    Ok(Json.toJson(interp.eval(id).getValue()));
  }


  def reset () = Action {request =>
    val interp = FamiliarIDEController.mkInterpreter(request.session)
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
    val files = recursiveListFiles(play.api.Play.getFile(workspaceDir)) // new File(workspaceDir))
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
      ){
         Json.toJson(Map ("label" -> Json.toJson(file.getName()),
          "leaf" -> JsBoolean(true),
          //add name
          //"name" -> JsString(file.getName()),
          //
          "type" -> JsString("file"),
          "id" -> JsString("fml" + file.getName()))) //"fml" + file.getName())) mkProperName(file)))
      }else{
         JsNull
      }

    }
    else {
      val files = file.listFiles
      if (!files.isEmpty) {
        val json = files.map(f =>
          _fileListToJSON(f)
        )
        // "children:" + json.filter(j => !j.isInstanceOf[JsNull]).map() + " label:" + file.getName()
        //json.map(j => Json.toJson(j)) // filter(j => !j.isInstanceOf[JsNull]).
        Json.toJson(Map ("label" -> Json.toJson(file.getName()),
            //add name
            //"name" -> JsString(file.getName()),
            //
            "children" -> JsArray(json.filter(j => j match {case JsNull => false; case _ => true})),
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



  def selectParent(children : List[String], parent : String) = Action {
    synthesizer.selectParentOfCluster(JavaConversions.setAsJavaSet(children.toSet), parent)
    Ok(Json.toJson(synthesizerInformationToJSON(synthesizer)))
  }

  def ignoreParent(child : String, parent : String) = Action {
    synthesizer.ignoreParent(child, parent)
    Ok(Json.toJson(synthesizerInformationToJSON(synthesizer)))
  }

  def completeFM() = Action {
    synthesizer.computeCompleteFeatureModel();
    Ok(Json.toJson(synthesizerInformationToJSON(synthesizer)))
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

      var ok = true
      val wu = new WuPalmerMetric
      ok = ok && wu.init(new File("resources/WordNet-3.0/wordnet_properties.xml"))
      heuristics += wu.getName() -> wu
      val pl = new PathLengthMetric
      ok = ok && pl.init(new File("resources/WordNet-3.0/wordnet_properties.xml"))
      heuristics += pl.getName() -> pl
//      println(ok)

      Ok(Json.toJson(Map(
        "heuristics" -> Json.toJson(heuristics.keys),
        "defaultRankingListHeuristic" -> Json.toJson(sw.getName()),
        "defaultClusteringHeuristic" -> Json.toJson(sw.getName()),
        "defaultThreshold" -> Json.toJson(0.5)
      )))
    } else {
      Ok(Json.toJson(Map(
        "heuristics" -> Json.toJson(heuristics.keys),
        "defaultRankingListHeuristic" -> Json.toJson(""),
        "defaultClusteringHeuristic" -> Json.toJson(""),
        "defaultThreshold" -> Json.toJson(0)
      )))
    }
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

  def undo() = Action {
    synthesizer.undo()
    Ok(Json.toJson(synthesizerInformationToJSON(synthesizer)))
  }

  def redo() = Action {
    synthesizer.redo()
    Ok(Json.toJson(synthesizerInformationToJSON(synthesizer)))
  }

  def saveToVar() = Action { request =>
    val interp = FamiliarIDEController.mkInterpreter(request.session)
    var lastVarValue = ""
    if (synthesizer != null) {
	    val fm = synthesizer.getFeatureModelVariable()
	    val diagram = fm.getFm().getDiagram()

	    if (fm.isValid() && !diagram.children(diagram.getTopVertex()).isEmpty()) {
//		    val command = fm.getIdentifier() + " = FM(" + fm + ")"
//		    val lastVar = interp.eval(command)
	    	interp.addOrReplaceVariable(fm.getIdentifier(), fm)
	    	val lastVar = fm
		    lastVarValue = lastVar.getIdentifier() + " = " + lastVar.getValue()
	    }
    }
    val allVarIDs = interp.getAllIdentifiers() ;

    Ok(Json.toJson(Map(
	        		  "varIDs" -> Json.toJson(allVarIDs.toList),
	        		  "lastVar" -> Json.toJson(lastVarValue)
	)))

  }

  def setRoot(root : String) = Action {
    synthesizer.setRoot(root)
    Ok(Json.toJson(synthesizerInformationToJSON(synthesizer)))
  }


  /**
   * This function create a folder in the workspace
   * @author galexand
   * @param : name of the folder and the path
   */
  def createFolder(name : String) = Action {
    //create a folder if another one with the same name doesn't exist
    val path : String = name /*workspaceDir+"/"+*/
    val d : File = new File(path)
    //execute
    val res = d.mkdirs()
    Ok(Json.toJson(Map("Work" -> 1)))
  }

  /**
   * Delete the directory and all the files which are included in
   * @param : name : the name of the directory
   */
  def deleteFolder(name : String)= Action{
    val direc : File = new File(name)
    println(name)
    FileUtils.deleteDirectory(direc)
    //list of directory in this directory

    //all the files in the directory
    //val fs : Array[File] = direc.listFiles()
    //new fil which receive a file in the loop
    //val f : File = null
    /*for each file in the array of file
     *f receive the next file
     */
    //for (f<-fs){
      //delete the file
      //println(f.getName())
      //if(f.isDirectory()==false){
        //f.delete()
      //}

    //}
    //delete the directory
    //direc.delete()
	Ok(Json.toJson(Map("Work" -> 1)))
  }


  /**
   * TODO code is horrible
   * Create a file in a specific folder
   * @param : name : the path and the name of the file
   */
 def createFile(name : String)= Action {
   // TODO: by session!
   // TODO: in folders
   // no control!
   val fileName = "/Users/macher1/Documents/SANDBOX/FML-from-scratch/webfml/" + workspaceDir + name
   Logger.info("Creating a file " + fileName)
   Files.createFile(
      FileSystems.getDefault()
                .getPath(fileName))
   /*
   val of = play.api.Play.getFile(workspaceDir + name)
   if(of == null)
      new File(workspaceDir + name).createNewFile */

   Ok("")

/*
	 //split the string
	 var k : Array[String] = name.split("/")
	 var i = 0
	 var p = ""
	 var myFile=""
	 //if they are something means we write a path
	 if(k(1) != "" || k(1) != null){
		 //now we need the path to create all the subfolder
		 for(i <- 0 until k.size){
		   //we must know if this is the name of the file
		   if(k(i).contains(".fml")){
		     //get the file name
		     myFile=k(i)
		   } else {
		     //add '/' separator to create a good path
		     p+=k(i)+"/"
		   }
		 }
		 //create the subfolder
		 val d : File = new File(p)
		 //execute
		 val res = d.mkdirs()
		 //create the file
		 val mf : File = new File(p+myFile)
		 //execute
		 mf.createNewFile()
	 } else {
		 //define a file
		 val f : File = new File(name)
		 //create the file
		 f.createNewFile()
	 }

	 Ok(Json.toJson(Map("Work" -> 1)))
   */
  }

 /**
  * Delete the file which have the name : name
  * @param : name : the name of the file
  */
 def deleteFile(name : String)= Action{
   val f : File = new File(name)
   //delete the file
   f.delete()
   Ok(Json.toJson(Map("Work" -> 1)))
 }
 /**
  * Update the value of the file and save it
  * @param : name : the name of file
  * @param : content : the content to save
  */
 def saveFile(name : String, content : String) = Action {
   //create a new file with the name
   val f : File = new File(name)
   //create a file writer with the previous file
   val fw : FileWriter = new FileWriter(f.getAbsoluteFile())
   //cretae a buffered writter which will update the content of the file
   val bw : BufferedWriter = new BufferedWriter(fw)
   //write the content
   bw.write(content)
   //close the buffer
   bw.close()
   Ok(Json.toJson(Map("Work" -> 1 )))
 }
 /**
  * Function which send all the
  * keywords of the familiar language
  */
 def getAllKeywordToJson() = Action {
	 //we create an array to stock the words
   val tab : Array[String]= Array("setDiff", "setUnion", "aggregateMerge", "ksynthesis", "slice", "over", "including",
       "cores", "serialize", "constraint", "constraints", "removeConstraint", "addConstraint",
       "deads", "setUnion", "setIntersection", "to", "falseOptionals", "excluding", "constraints",
       "withMapping", "map", "cleanup", "Set", "asFM", "addConstraint", "aggregate",
       "isConflicting", "autoSelect", "setIsEmpty", "isComplete", "isNull",
       "isExisting", "diff", "counting", "removeFeature", "renameFeature", "isValid", "requires",
                         "implies", "excludes", "FeatureModel", "Feature", "String",
                         "operator", "configuration", "select", "deselect", "size", "unselect",
                         "min", "max", "random", "not", "==", "else", "configs", "set", "setAdd",
                         "exit", "extract", "setEmpty", "size", "export", "hide", "strConcat",
                         "renameFeature", "and", "or", "mand", "opt", "xor", "or", "str_concat", "eq", "neq", "do",
                         "neq", "merge", "union", "sunion", "intersection", "whichfm", "name", "run",
                         "into", "valid?", "hide", "parent", "removeFeature", "parameter", "then", "if",
                         "end", "is_null", "root", "with", "print_var", "print", "children", "foreach", "in",
                         "selectedF", "deselectedF", "unselectedF", "removeVariable",
                         "println", "copy", "into", "FM", "as", "assert", "size", "insert", "compare")

   // TODO: import directly all the words from the Xtext grammar
	 //parse to json the previous tab
	 val myJsonArray : JsValue = Json.toJson(tab)
	 //"send" it
	 Ok(myJsonArray)
 }
 /**
  * Function which send all the class word of the
  * familiar language
  */
 def getAllClasswordToJson()=Action{
   //we create an array to stock the words
   val tabClass:Array[String] = new Array[String](5)
   tabClass(0)="FM"
   //parse to json the previous tab
   val myJsonArray : JsValue = Json.toJson(tabClass)
   //"send" it
   Ok(myJsonArray)
 }
 /**
  * Function which "send" all the constant word which are
  * use in Familiar (e.g : null)
  */
 def getAllConstantwordToJson()=Action{
   //we create an array to stock the words
   val tabConstant: Array[String] = new Array[String](5)
   tabConstant(0)="null"
   //parse to json the previous tab
   val myJsonArray : JsValue = Json.toJson(tabConstant)
   //"send" it
   Ok(myJsonArray)
 }
 /**
  * Send in HTML format a tutorial write in markdown
  * @param nameOfTheLanguage : name of the language of the tutorial
  */
 def getTutorialInMarkdown(language : String) = Action{
   //we get the file with the name
   val path: String = "public/tuto/"+language+"/chapters/"+"/introduction.md"
   val myFile: File = new File(path)
   var res: Html = null
   if(myFile.exists()){
      var out : String =Source.fromFile(myFile).getLines.mkString("\n")
      //convert into Html
      res= Html(out)
   }
   //return res
   Ok(res)
 }

 /**
  *Load the header for the tutorial part of the current language
  *and return this header into html format
  * @param language : the current language used in the app
  */
 def getHeaderInMarkdown(language : String) = Action{
   //we get the file with the name
   val path: String = "public/tuto/"+language+"/header.md"
   val myFile: File = new File(path)
   var res: Html = null
   if(myFile.exists()){
      var out : String =Source.fromFile(myFile).getLines.mkString("\n")
      //convert into Html
      res= Html(out)
   }
   //return res
   Ok(res)
 }

 /**
  *Load the menu in markdown and return this menu into HTML format
  *@param language : the current language used in the app
  */
 def getMenuInMarkdown(language : String) = Action{
   //we get the file with the name
   val path: String = "public/tuto/"+language+"/menu.md"
   val myFile: File = new File(path)
   var res: Html = null
   if(myFile.exists()){
      var out : String =Source.fromFile(myFile).getLines.mkString("\n")
      //convert into Html
      res= Html(out)
   }
   //return res
   Ok(res)
 }

 /**
  * Return the correct chapter
  * @param name : the name of the chapter
  * @param language : the current language of the app
  */
 def getChapter(name : String, language : String) = Action{

   val path: String = "public/tuto/"+language+"/chapters/"+name
   val myFile: File = new File(path)
   var res: Html = null
   if(myFile.exists()){
      var out : String =Source.fromFile(myFile).getLines.mkString("\n")
      //convert into Html
      res= Html(out)
   }
   //return the html code
   Ok(res)
 }
 /**
  *
  * @TODO : DELETE
  *
  */
 def getAllChapters(langage : String) = Action{
	  val path: String = "public/tuto/"+langage+"/chapters"
	  val myFiles: File = new File(path)
	  var myFile:File = null
	  var res: String = null
	  for(myFile<-myFiles.listFiles()){
	    println(myFile.getName())
	    res+=myFile.getName()
	  }
	  Ok(res)
 }


   /**
   * Return all the keyword of vm's language
   */
  def searchKeyword()=Action{
    val path="public/tuto/vm/xtext/Vm.xtext"
    val myFile:File = new File(path)
    //search all characters between ''
    val pattern = "'.*?'".r
    //List myList = new List()
    var lst  = new ListBuffer[String]()
    //if the file exist
    if(myFile.exists()){
      //read line by line
      for(line<- Source.fromFile(myFile).getLines()){
        //create an interator
    	 for (literal <- pattern.findAllIn(line)) {
    	  // Add the literal to the list of keywords if it is not whitespace
    	  if (!literal.matches("\\s*")) {
    	    //replace the ' to nothing
    	    lst+=literal.replace("'", "")
    	  }
    	 }
      }

    }
    //convert to a list
    var k =lst.toList.distinct
    //return the list
    Ok(Json.toJson(k))
  }

}
