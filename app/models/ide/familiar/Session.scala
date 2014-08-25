package models.ide.familiar

import scala.util.Random
import play.api.mvc._
import controllers.routes
import scala.collection.mutable.ListBuffer
import play.api.Application



/**
 * Class which create session to access to the 
 * IDE
 */
 class Session extends Controller{

  var id:Int =0
  //var myCookie : Cookie = null
  /**
   * Return the id of the session
   */
  def getId():Int ={
    return id
  }
  
  /**
   * Create a session
   */
  def create(username : String, password : String, sessions : List[Session]){
    id = Random.nextInt(100)
    //while the number of the session is the same
    //generate an other one with a different limit
    while(sessions.contains(id)){
      id=Random.nextInt(400) 
    }
    //add the number of the session in the list
    //create the session 
    //Ok("Welcome").withSession("id"->id.toString)
    println("Session Created: "+id)
  }
  /**
   * Destroy the session
   */
  def destroy(){
    
    //sessions.remove(i)
    println("session destroyed")
    //println(getSessions())
    //destroy the directory
    //@TODO destroy the directory
    Ok("Bye").withNewSession
  }
  
}