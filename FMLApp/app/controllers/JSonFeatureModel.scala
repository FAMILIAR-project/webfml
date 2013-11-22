package controllers

import play.api.libs.json.Json
import fr.familiar.variable.FeatureModelVariable
import play.api.libs.json.JsString

class JSonFeatureModel (fmv: FeatureModelVariable ) {
  
  
  def toJSon() = {
    Json.toJson(JsString(""))
    
       
  }

}