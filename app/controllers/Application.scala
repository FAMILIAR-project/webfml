package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._
import views._
import play.api.libs.json.Json
import java.io.File
import play.api.templates.Html






object Application extends Controller {

  
 
   def index(language:String) = Action {
    	request =>
		val fmlDemo ="// your FAMILIAR code here!\n" + 
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
		var scriptToImport=""
		if(language=="familiar"){
			scriptToImport="<script>var language='familiar'</script> <script type='text/javascript' src='/assets/javascripts/ace-builds-master/src-noconflict/mode-familiar.js'></script>"
		}
		
		
		Ok(views.html.index(fmlDemo + "\n",Html(scriptToImport)))
    }
  
   /**
    * To access to the tutorial page
    * @param : name : name of the language
    */
   def tutorial(name:String) = Action {
	 var nameOfTheLanguage=""
     var btn=""
       
     if(name=="familiar"){
       
       nameOfTheLanguage = "<script>var language='familiar'</script> <script type='text/javascript' src='/assets/javascripts/ace-builds-master/src-noconflict/mode-familiar.js'></script>"
       btn="<button class='btn btn-info' id='webFmlCmd' ng-click='cmd()' ><i class='glyphicon glyphicon-play'></i> Execute FAMILIAR code</button>"
   
     }
     
     if(name=="vm"){
       
       nameOfTheLanguage = "<script>var language='vm'</script> <script type='text/javascript' src='/assets/javascripts/ace-builds-master/src-noconflict/mode-vm.js'></script>"
       /*
        * Disabled the button because VM has not interpreter and need one. 
        * @TODO : Change the ng-click='cmd()' --> for VM
        */
       btn="<button class='btn btn-info' id='webFmlCmd' disabled='disabled' ng-click='cmd()' ><i class='glyphicon glyphicon-play'></i> Execute VM code</button>"

     }
      
     
     
     val tuto = "//hello world !\n"
     Ok(views.html.tutorial(tuto,Html(nameOfTheLanguage),Html(btn)))/*, WebFMLInterpreter.tutorialToHtml())*/
   }
  /**
   *  HomePage
   */ 
   def homePage() = Action {
     Ok(views.html.homePage())
   }
   
   /**
    * Login page, access to the ide page
    */
   def loginPage()=Action{
     Ok(views.html.loginPage())
   }
   
 
   
   
       
        
    def javascriptRoutes = Action { implicit request =>
        import routes.javascript._
        Ok(
            Routes.javascriptRouter("jsRoutes")(
                WebFMLInterpreter.interpret,
                WebFMLInterpreter.configureVariable,
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
                WebFMLInterpreter.createFile,
                //delete folder
                WebFMLInterpreter.deleteFolder,
                //delete file
                WebFMLInterpreter.deleteFile,
                //save file
                WebFMLInterpreter.saveFile,
                //toJson
                //familiar
                WebFMLInterpreter.getAllKeywordToJson,
                WebFMLInterpreter.getAllClasswordToJson,
                WebFMLInterpreter.getAllConstantwordToJson,
                //vm
                WebFMLInterpreter.searchKeyword,
                //markdown
                WebFMLInterpreter.getTutorialInMarkdown,
                WebFMLInterpreter.getMenuInMarkdown,
                WebFMLInterpreter.getHeaderInMarkdown,
                WebFMLInterpreter.getChapter,
                //login for ide 
                FamiliarIDEController.receiveInformations
                
            )
        ).as("text/javascript");
    } 

}