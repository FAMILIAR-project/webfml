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
import views._
import fr.familiar.parser.ConfigurationVariableBDDImpl
import fr.familiar.parser.FMBuilder
import play.api.libs.json.JsValue
import org.xtext.example.mydsl.fML.OpSelection

object WebFMLConfigurator extends Controller with VariableHelper { 
  
  val interp = new FMLBasicInterpreter()
  val configur = new ConfigurationVariableBDDImpl( 
      new FeatureModelVariable("", FMBuilder.getInternalFM("A : (B|C|D)? [E] [F] ; C -> E ; ")), "") 
  
   def configurator() = Action {
    	request =>    	   	
		Ok(views.html.demoSubstitution2.render)
       
    }
  
  def applySelection(jsonString : String) = Action {
    	request =>    
    	// val json: JsValue = Json.parse(jsonString)
    	
    	if (configur.getSelected().contains(jsonString)) {
    	  configur.applySelection("" + jsonString, OpSelection.DESELECT)
    	}
    	else if (configur.getDeselected().contains(jsonString) || 
    	    configur.getUnselected().contains(jsonString)  
    	) {
    	  configur.applySelection("" + jsonString, OpSelection.SELECT)
    	}
    	
    	val sels = configur.getSelected() ;
    	val deselecs = configur.getDeselected() ++ configur.getUnselected()
		Ok(Json.toJson(Map ("selected" -> sels.toList) ++ Map ("deselected" -> deselecs.toList)   
		))
		       
    }
 

}