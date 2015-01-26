package controllers

import play.api.mvc.Controller
import play.api.mvc._
import models.ide.familiar.ConcreteFamiliarIDEFactory
import models.ide.familiar.FamiliarIDEFactory
import fr.familiar.interpreter.FMLBasicInterpreter
import scala.collection.mutable.ListBuffer
import models.ide.familiar.Session

object FamiliarIDEController extends Controller{
	
  var mapInstanceOfFamiliar : Map [Session, FMLBasicInterpreter ] = Map()
  //list which contains all the the sessions
  var sessions = new ListBuffer[Session]()
  /**
	 * Function which receive information from the login
	 * page and create an interpreter and a session 
	 */
	def receiveInformations (login : String, password : String, language : String) = Action { request =>
     request.session.get("connected").map { user =>
      println("USER: " + user)
      Ok("Hello " + user)
    }.getOrElse {
      println("not a USER: ")
      Ok("Welcome!").withSession("connected" -> login)
      //Unauthorized("Oops, you are not connected")
    }



		var familiarInstance : FamiliarIDEFactory = new ConcreteFamiliarIDEFactory
		//an instane object of FamiliarInterpreter
		var inter = familiarInstance.createInterpreter()
		//a session object
		var ses = familiarInstance.createSession()
		//create a session
		ses.create(login,password,sessions.toList)
		//store
		sessions+=ses
		storeFamiliar(ses, inter)
		println("redirect to the ide")
		//check the user
		
		//try to redirect to the page of the IDE
		//Redirect("/ide/familiar", MOVED_PERMANENTLY)
		//Ok("")
    Ok("Welcome!").withSession("connected" -> login)
  }
	
	/**
	 * store the familiar into a map
	 */
	def storeFamiliar(aSession: Session, instanceLangage : FMLBasicInterpreter){
		
		//mapInstanceOfFamiliar+=(aSession,instanceLangage)
	}
	/**
	 * Return all the session 
	 */
	def getSessions(): List[Session] ={
		return sessions.toList
	}
	/**
	 * Check if they are one user who existed in the database
	 */
	def checkUser(log : String, pwd : String){
		//var request = "SELECT * FROM UserTab WHERE login="+log+"AND password="+pwd
		//println(request)
		var result=""
		if(result!=null){
		  //one user exist so we have to load the workspace or stock the repository name to load it
		}else if (log=="demo" && pwd=="demo") {
		  //create a tempory workspace
		  
  
		}
		  
		
		//execute the request
		
	}
	/**
	 * Destroy a session
	 * @param the id of the session
	 */
	def destroySession(sessionId:Int){
	  var mySess = sessions(sessionId)
	  mySess.destroy()
	  //remove the session of the map
	  mapInstanceOfFamiliar-=mySess
	}
	
	
	/*def storeVM(session: Session, instanceLangage : FMLBasicInterpreter){
	  
	}*/
}