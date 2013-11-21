// @SOURCE:/Users/macher1/git/webfml/FMLApp/conf/routes
// @HASH:1040d4ccc9061a3ce4e4013f05340bbb62f64122
// @DATE:Thu Nov 21 18:58:43 CET 2013


import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._


import Router.queryString

object Routes extends Router.Routes {

private var _prefix = "/"

def setPrefix(prefix: String) {
  _prefix = prefix
  List[(String,Routes)]().foreach {
    case (p, router) => router.setPrefix(prefix + (if(prefix.endsWith("/")) "" else "/") + p)
  }
}

def prefix = _prefix

lazy val defaultPrefix = { if(Routes.prefix.endsWith("/")) "" else "/" }


// @LINE:6
private[this] lazy val controllers_Application_index0 = Route("GET", PathPattern(List(StaticPart(Routes.prefix))))
        

// @LINE:7
private[this] lazy val controllers_WebFMLInterpreter_variable1 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("variable/"),DynamicPart("id", """[^/]+""",true))))
        

// @LINE:8
private[this] lazy val controllers_WebFMLInterpreter_interpret2 = Route("POST", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("interpret"))))
        

// @LINE:9
private[this] lazy val controllers_WebFMLInterpreter_evalPrompt3 = Route("POST", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("evalPrompt"))))
        

// @LINE:10
private[this] lazy val controllers_WebFMLInterpreter_ksynthesis4 = Route("POST", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("ksynthesis"))))
        

// @LINE:11
private[this] lazy val controllers_WebFMLInterpreter_reset5 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("reset"))))
        

// @LINE:12
private[this] lazy val controllers_WebFMLConfigurator_configurator6 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("configurator"))))
        

// @LINE:13
private[this] lazy val controllers_WebFMLConfigurator_applySelection7 = Route("POST", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("applySelection"))))
        

// @LINE:16
private[this] lazy val controllers_WebFMLInterpreter_loadFile8 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("loadFile/"),DynamicPart("id", """[^/]+""",true))))
        

// @LINE:17
private[this] lazy val controllers_WebFMLInterpreter_saveAs9 = Route("POST", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("saveAs"))))
        

// @LINE:18
private[this] lazy val controllers_WebFMLInterpreter_listFiles10 = Route("POST", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("listFiles"))))
        

// @LINE:20
private[this] lazy val controllers_Application_javascriptRoutes11 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("assets/javascripts/routes"))))
        

// @LINE:23
private[this] lazy val controllers_Assets_at12 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("assets/"),DynamicPart("file", """.+""",false))))
        
def documentation = List(("""GET""", prefix,"""controllers.Application.index"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """variable/$id<[^/]+>""","""controllers.WebFMLInterpreter.variable(id:String)"""),("""POST""", prefix + (if(prefix.endsWith("/")) "" else "/") + """interpret""","""controllers.WebFMLInterpreter.interpret(s:String)"""),("""POST""", prefix + (if(prefix.endsWith("/")) "" else "/") + """evalPrompt""","""controllers.WebFMLInterpreter.evalPrompt(s:String)"""),("""POST""", prefix + (if(prefix.endsWith("/")) "" else "/") + """ksynthesis""","""controllers.WebFMLInterpreter.ksynthesis(s:String)"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """reset""","""controllers.WebFMLInterpreter.reset"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """configurator""","""controllers.WebFMLConfigurator.configurator"""),("""POST""", prefix + (if(prefix.endsWith("/")) "" else "/") + """applySelection""","""controllers.WebFMLConfigurator.applySelection(s:String)"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """loadFile/$id<[^/]+>""","""controllers.WebFMLInterpreter.loadFile(id:String)"""),("""POST""", prefix + (if(prefix.endsWith("/")) "" else "/") + """saveAs""","""controllers.WebFMLInterpreter.saveAs(content:String, filename:String)"""),("""POST""", prefix + (if(prefix.endsWith("/")) "" else "/") + """listFiles""","""controllers.WebFMLInterpreter.listFiles()"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """assets/javascripts/routes""","""controllers.Application.javascriptRoutes"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """assets/$file<.+>""","""controllers.Assets.at(path:String = "/public", file:String)""")).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
  case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
  case l => s ++ l.asInstanceOf[List[(String,String,String)]] 
}}
      

def routes:PartialFunction[RequestHeader,Handler] = {

// @LINE:6
case controllers_Application_index0(params) => {
   call { 
        invokeHandler(controllers.Application.index, HandlerDef(this, "controllers.Application", "index", Nil,"GET", """ Home page""", Routes.prefix + """"""))
   }
}
        

// @LINE:7
case controllers_WebFMLInterpreter_variable1(params) => {
   call(params.fromPath[String]("id", None)) { (id) =>
        invokeHandler(controllers.WebFMLInterpreter.variable(id), HandlerDef(this, "controllers.WebFMLInterpreter", "variable", Seq(classOf[String]),"GET", """""", Routes.prefix + """variable/$id<[^/]+>"""))
   }
}
        

// @LINE:8
case controllers_WebFMLInterpreter_interpret2(params) => {
   call(params.fromQuery[String]("s", None)) { (s) =>
        invokeHandler(controllers.WebFMLInterpreter.interpret(s), HandlerDef(this, "controllers.WebFMLInterpreter", "interpret", Seq(classOf[String]),"POST", """""", Routes.prefix + """interpret"""))
   }
}
        

// @LINE:9
case controllers_WebFMLInterpreter_evalPrompt3(params) => {
   call(params.fromQuery[String]("s", None)) { (s) =>
        invokeHandler(controllers.WebFMLInterpreter.evalPrompt(s), HandlerDef(this, "controllers.WebFMLInterpreter", "evalPrompt", Seq(classOf[String]),"POST", """""", Routes.prefix + """evalPrompt"""))
   }
}
        

// @LINE:10
case controllers_WebFMLInterpreter_ksynthesis4(params) => {
   call(params.fromQuery[String]("s", None)) { (s) =>
        invokeHandler(controllers.WebFMLInterpreter.ksynthesis(s), HandlerDef(this, "controllers.WebFMLInterpreter", "ksynthesis", Seq(classOf[String]),"POST", """""", Routes.prefix + """ksynthesis"""))
   }
}
        

// @LINE:11
case controllers_WebFMLInterpreter_reset5(params) => {
   call { 
        invokeHandler(controllers.WebFMLInterpreter.reset, HandlerDef(this, "controllers.WebFMLInterpreter", "reset", Nil,"GET", """""", Routes.prefix + """reset"""))
   }
}
        

// @LINE:12
case controllers_WebFMLConfigurator_configurator6(params) => {
   call { 
        invokeHandler(controllers.WebFMLConfigurator.configurator, HandlerDef(this, "controllers.WebFMLConfigurator", "configurator", Nil,"GET", """""", Routes.prefix + """configurator"""))
   }
}
        

// @LINE:13
case controllers_WebFMLConfigurator_applySelection7(params) => {
   call(params.fromQuery[String]("s", None)) { (s) =>
        invokeHandler(controllers.WebFMLConfigurator.applySelection(s), HandlerDef(this, "controllers.WebFMLConfigurator", "applySelection", Seq(classOf[String]),"POST", """""", Routes.prefix + """applySelection"""))
   }
}
        

// @LINE:16
case controllers_WebFMLInterpreter_loadFile8(params) => {
   call(params.fromPath[String]("id", None)) { (id) =>
        invokeHandler(controllers.WebFMLInterpreter.loadFile(id), HandlerDef(this, "controllers.WebFMLInterpreter", "loadFile", Seq(classOf[String]),"GET", """ Workspace management """, Routes.prefix + """loadFile/$id<[^/]+>"""))
   }
}
        

// @LINE:17
case controllers_WebFMLInterpreter_saveAs9(params) => {
   call(params.fromQuery[String]("content", None), params.fromQuery[String]("filename", None)) { (content, filename) =>
        invokeHandler(controllers.WebFMLInterpreter.saveAs(content, filename), HandlerDef(this, "controllers.WebFMLInterpreter", "saveAs", Seq(classOf[String], classOf[String]),"POST", """""", Routes.prefix + """saveAs"""))
   }
}
        

// @LINE:18
case controllers_WebFMLInterpreter_listFiles10(params) => {
   call { 
        invokeHandler(controllers.WebFMLInterpreter.listFiles(), HandlerDef(this, "controllers.WebFMLInterpreter", "listFiles", Nil,"POST", """""", Routes.prefix + """listFiles"""))
   }
}
        

// @LINE:20
case controllers_Application_javascriptRoutes11(params) => {
   call { 
        invokeHandler(controllers.Application.javascriptRoutes, HandlerDef(this, "controllers.Application", "javascriptRoutes", Nil,"GET", """""", Routes.prefix + """assets/javascripts/routes"""))
   }
}
        

// @LINE:23
case controllers_Assets_at12(params) => {
   call(Param[String]("path", Right("/public")), params.fromPath[String]("file", None)) { (path, file) =>
        invokeHandler(controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String]),"GET", """ Map static resources from the /public folder to the /assets URL path""", Routes.prefix + """assets/$file<.+>"""))
   }
}
        
}

}
     