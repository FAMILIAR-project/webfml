package controllers

import controllers.WebFMLInterpreter._
import fr.familiar.parser.ConfigurationVariableBDDImpl
import fr.familiar.variable.{FeatureModelVariable, ConfigurationVariable}
import play.api.libs.json.Json
import play.api.mvc.Controller
import play.api.mvc._
import fr.familiar.interpreter.FMLBasicInterpreter
import scala.collection.mutable.ListBuffer
import scala.util.Random


object FamiliarIDEController extends Controller {



  var mapInstanceOfFamiliar : Map [Session, FMLBasicInterpreter ] = Map()




  def mkInterpreter(sess: Session) : FMLBasicInterpreter = {
    mapInstanceOfFamiliar.get(sess) match {
      case Some(s) => s
      case None => createNewInterpreter(sess)
    }

  }

  def createNewInterpreter(sess: Session) : FMLBasicInterpreter = {
    val interp = new FMLBasicInterpreter()
    storeFamiliar(sess, interp)
    interp
  }

  private def _mkInterpreter() : FMLBasicInterpreter = {
    new FMLBasicInterpreter()
  }





  /**
	 * Function which receive information from the login
	 * page and create an interpreter and a session
   * TODO language seems not needed here
	 */
	def receiveInformations(login:String, password:String, language: String) = Action {

    request =>
		//check the user
		if (!checkUser(login, password)) {
			BadRequest("Error")
		}
		else {
			//try to redirect to the page of the IDE
			//Redirect("/ide/familiar",MOVED_PERMANENTLY)
			Ok("Hey").withSession("id" -> genUniqueID(request).toString)
		}
 	}

	/**
	 * store the familiar into a map
	 */
	def storeFamiliar(aSession: Session, instanceLangage : FMLBasicInterpreter) = {
		mapInstanceOfFamiliar += (aSession -> instanceLangage)
	}

	/**
	 * Check if they are one user who existed in the database
	 */
	def checkUser(log : String, pwd : String) : Boolean = {
		//var request = "SELECT * FROM UserTab WHERE login="+log+"AND password="+pwd
	  if (log == "demo" && pwd== "demo") {
		  // TODO create a temporary workspace
  		 return true
		}
		false

		//execute the request

	}

	/**
	 * Destroy a session
	 */
	def destroySession = Action { request =>
	  var mySess = request.session

	  //remove the session of the map
	  mapInstanceOfFamiliar -= mySess

		Ok("Bye").withNewSession
	}


  def whoIAM = Action { request =>
    request.session.get("id").map { user =>
      println("Auth: ", user)
      Ok(Json.toJson(Map(
        "user" -> Json.toJson(user)
      ))).withNewSession
    }.getOrElse {
      Unauthorized("Oops, you are not connected")
    }
  }

  def genUniqueID (request : Request[AnyContent]) = {
    /*
    request.session.get("id").map {
      user => user
    }*/
    randomString(8)
  }


  def randomString(length : Integer) = {
    val r = new scala.util.Random
    val sb = new StringBuilder
    for (i <- 1 to length) {
      sb.append(r.nextInt(10))
    }
    sb.toString
  }

  def createTemporarySession = Action { request =>
    val kID = genUniqueID (request)
    println ("kID: ", kID)
    Ok(Json.toJson(Map(
      "id" -> Json.toJson(kID.toString)
    ))).withSession("id" -> kID.toString)
  }

  def isAuthentified = Action { request =>
    request.session.get("id").map { user =>
      Ok(Json.toJson(Map(
        "user" -> Json.toJson(user)
      )))
    }.getOrElse {
      Ok(Json.toJson(Map(
        "user" -> Json.toJson(-1)
      )))
    }

  }




}