package controllers

import controllers.FamiliarIDEController._
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
		val fmlDemo = "// your FAMILIAR code here!\n" +
					"fm1 = FM (DiverSE_School : Jhipster Xtext [FAMILIAR] [SIRIUS] Instructors ; \nInstructors : (Olivier | Mathieu | Thomas | Benoit); Mathieu <-> FAMILIAR ;" +
          " FAMILIAR -> !SIRIUS;  )\n" +
					"s1 = configs fm1\n" +
					"c1 = counting fm1\n\n\n" +
          "fm2 = slice fm1 excluding { Olivier Benoit }\n" +
          "fm3 = slice fm1 including { fm1.DiverSE_School fm1.Mathieu fm1.SIRIUS }\n"
          ;
		// <button class="btn" onclick="loadFile('fm1 = FM (A : [B] XXX ; )');">Load file</button>
		var scriptToImport=""
		if(language=="familiar"){
			scriptToImport="<script>var language='familiar'</script> <script type='text/javascript' src='/assets/javascripts/ace-builds-master/src-noconflict/mode-familiar.js'></script>"
		}

        var userid  = ""
     request.session.get("id").map { user =>
            userid = user
          }.getOrElse {
            userid = ""
          }

		Ok(views.html.index(fmlDemo + "\n", Html(scriptToImport), userid))
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
                FamiliarIDEController.receiveInformations,
                FamiliarIDEController.whoIAM,
                FamiliarIDEController.createTemporarySession,
                FamiliarIDEController.isAuthentified

            )
        ).as("text/javascript");
    }

}
