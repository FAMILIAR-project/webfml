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
  var sessions = new ListBuffer[Int]()
  var myCookie : Cookie = null
  /**
   * Return the id of the session
   */
  def getId():Int ={
    return id
  }
  /**
   * Return all the sessions
   */
  def getSessions(): List[Int] ={
    return sessions.toList
  }
  
  /**
   * 
   */
  def create(username : String, password : String){
    id = Random.nextInt(100)
    //while the number of the session is the same
    //generate an other one with a different limit
    while(sessions.contains(id)){
      id=Random.nextInt(400) 
    }
    //add the number of the session in the list
    sessions+=(id)
    //create the session
    println("Session Created: "+id)
    println(getSessions())
    check(username,password)
  }
  /**
   * Check the username and the password of the user
   */
  def check(username : String, password : String)=Action{
    if(username=="demo" && password =="demo"){
      //@TODO create a tempory user
      //@TODO create a directory for this user
      //create a temp session
      //myCookie = new Cookie("id",getId().toString(),Option(7200))
      
    }else{
      //check in the database if user exist
       //myCookie = new Cookie("id",getId().toString())
    }
    println("test") 
    Ok("Welcome").withSession("id"->id.toString)
   
  }
  /**
   * Destroy the session
   */
  def destroy(i:Int){
    
    sessions.remove(i)
    println("session destroyed")
    println(getSessions())
    //destroy the directory
    //@TODO destroy the directory
    Ok("Bye").withNewSession
  }
  
}