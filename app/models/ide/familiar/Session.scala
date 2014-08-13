package models.ide.familiar

import scala.util.Random
import play.api.mvc._



/**
 * 
 */
class Session {
	
  def create(){
    /*var k = Random.nextInt(100)
    //create the session
    def index = Action {
      request => request.session.get("connected").map{
        user => k//("Hello" + user + "your key"+ k)
      }.getOrElse{
        Unauthorized("Not Connected")
      }
      Redirect("/ide/familiar")
    }*/
   //Ok("Go")
    println("Session Created")
  }
  
  
  def destroy(){
    println("session destroyed")
  }
  
}