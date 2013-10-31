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

object WebFMLInterpreter extends Controller with VariableHelper { 
  
  val interp = new FMLBasicInterpreter()
  val KSYNTHESIS_INTERACTIVE_CMD = "ksynthesis --interactive" 
  
   def interpret(fmlCommand: String) = Action {
    	request =>    	   	
		try {
		    interp.reset() 
			val lastVar = interp.eval(fmlCommand);
			
			var rs = "<b>" + lastVar.getIdentifier() + " = " + lastVar.getValue() + "</b></br>" ;
			
			rs = rs + "<h3 style=\"text-align: center ; \">Variables</h3>"
			rs = rs + "<ul>"
			val allVarIDs = interp.getAllIdentifiers() ; 
			allVarIDs.foreach(varID => rs = rs + "<li>" + mkVariableURL(varID) + "</li>\n")
			rs = rs + "</ul>"
			Ok(Json.toJson(rs));
		}
		catch {
			case e : FMLAssertionError => BadRequest(e.getMessage())  
			case e : FMLFatalError =>  BadRequest(e.getMessage())
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
        // FIXME *ranked* lists
        val ig = fmv.computeImplicationGraph()
        val rl = ig.vertices().map(ft => Map (ft -> ig.outgoingEdges(ft).map(e => ig.getTarget(e))))
            
        // TODO clusters
        // TODO previsualization
        
         Ok (Json.toJson(Map ("id" -> Json.toJson(assignID), 
          "targetID" -> Json.toJson(targetID),
          "rankinglist" -> Json.toJson(rl)
          )
          )) 
      }
      else {
        Ok ("Error, variable " + targetID + " is not a formula or feature model") // can't remember how Error are properly handled in Play!
      }
      
     
    
    
  }
   
   def variables (id : String) = Action {
    Ok(Json.toJson(interp.eval(id).getValue()));
  }
   
   
    def reset () = Action {request =>  
        interp.reset()
    	Ok("")
  }

  
 

}