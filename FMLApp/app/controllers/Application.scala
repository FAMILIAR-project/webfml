package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._
import views._
import play.api.libs.json.Json
import java.io.File




object Application extends Controller {

  
 
   def index() = Action {
    	request =>
		val fmlDemo = "// your FAMILIAR code here!\n" + 
					"fm1 = FM (A: B [C] ; )\n" + 
					"\n" + 
					"\n" + 
					"fm9 = FM (A : B ; )\n" + 
					"fm2 = FM (A : B [C] ; )\n" + 
					"fm3 = FM (A : B [E] ; )\n" + 
					"fm4 = merge sunion fm*\n" + 
					"fm5 = FM (A : J [K] [L] ; )\n" + 
					"\n" + 
					"fm0 = merge sunion fm*\n" + 
					"\n" + 
					"n0 = counting fm0\n" + 
					"nTotal = 0\n" + 
					"foreach (fm in fm*) do\n" + 
					"    nfm = counting fm\n" + 
					"    nTotal = nTotal + nfm\n" + 
					"end\n" + 
					"\n" + 
					"nTotal = nTotal + 1\n" + 
					"n4 = counting fm4\n" + 
					"n7 = counting fm0\n" + 
					"fm0\n" + 
					"mtx = computeMUTEXGroups fm0";
		// <button class="btn" onclick="loadFile('fm1 = FM (A : [B] XXX ; )');">Load file</button>
		Ok(views.html.index.render(fmlDemo + "\n"))
    }
  
  
   
 
   
   
       
        
    def javascriptRoutes = Action { implicit request =>
        import routes.javascript._
        Ok(
            Routes.javascriptRouter("jsRoutes")(
                WebFMLInterpreter.interpret,
                WebFMLInterpreter.reset,
                WebFMLInterpreter.evalPrompt,
                WebFMLInterpreter.ksynthesis,
                WebFMLInterpreter.variable,
                WebFMLInterpreter.loadFile,
                WebFMLInterpreter.saveAs,
                WebFMLInterpreter.listFiles,
                WebFMLConfigurator.applySelection,
                WebFMLInterpreter.selectParent,
                WebFMLInterpreter.ignoreParent,
                WebFMLInterpreter.completeFM,
                WebFMLInterpreter.undo,
                WebFMLInterpreter.redo,
                WebFMLInterpreter.saveToVar,
                WebFMLInterpreter.getHeuristics,
                WebFMLInterpreter.setRankingListsHeuristic,
                WebFMLInterpreter.setClusteringParameters,
                WebFMLInterpreter.setRoot,
                //create folder
                WebFMLInterpreter.createFolder,
                //create file
                WebFMLInterpreter.createFile
                
                
            )
        ).as("text/javascript");
    } 

}