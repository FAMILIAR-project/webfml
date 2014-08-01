package controllers

import play.api.mvc._
import java.io.File
import scala.io.Source
import play.api.libs.json.Json

object VMInterpreter extends Controller{
	
  
  /**
   * Return all the keyword of vm's language
   */
  def searchKeyword()=Action{
    val i=0
    val path="public/tuto/vm/xtext/Vm.xtext"
    val myFile:File = new File(path)
    var result=""
    var res=""
    if(myFile.exists()){
      for(line<- Source.fromFile(myFile).getLines()){
        println(line)
        result=line
        //search all the word between ''
        //get these word in a tab
        //return the tab
        	
      }
    }
    Ok(Json.toJson(res))
  }
  
}