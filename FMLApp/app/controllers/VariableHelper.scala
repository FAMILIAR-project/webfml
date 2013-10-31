package controllers

trait VariableHelper {
  
    def mkVariableURL(varID : String) : String = {
      val urlID = "variables/" + varID
      <a href={urlID}>{varID}</a>.toString
   }

}