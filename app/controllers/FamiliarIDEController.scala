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
  var sessions = new ListBuffer[Int]()
  /**
	 * Function which receive information from the login
	 * page and create an interpreter and a session 
	 */
	def receiveInformations(login:String, password:String,language:String)=Action{
  
		println(login)
		println(password)
		println(language)
    
		var familiarInstance : FamiliarIDEFactory = new ConcreteFamiliarIDEFactory
		//an instane object of FamiliarInterpreter
		var inter = familiarInstance.createInterpreter()
		//a session object
		var ses = familiarInstance.createSession()
		//create a session
		ses.create(login,password,sessions.toList)
		//get the Id of the current session
		var myId = ses.getId()
		//store
		sessions+=myId
		storeFamiliar(ses, inter)
		//ses.destroy(myId)
    
		var res="ok"
     
		Ok(res)
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
	def getSessions(): List[Int] ={
		return sessions.toList
	}
	
	
	
	/*def storeVM(session: Session, instanceLangage : FMLBasicInterpreter){
	  
	}*/
}