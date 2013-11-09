// @SOURCE:/Users/macher1/git/webfml/FMLApp/conf/routes
// @HASH:1040d4ccc9061a3ce4e4013f05340bbb62f64122
// @DATE:Fri Nov 08 10:58:17 CET 2013

import Routes.{prefix => _prefix, defaultPrefix => _defaultPrefix}
import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._


import Router.queryString


// @LINE:23
// @LINE:20
// @LINE:18
// @LINE:17
// @LINE:16
// @LINE:13
// @LINE:12
// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:8
// @LINE:7
// @LINE:6
package controllers {

// @LINE:13
// @LINE:12
class ReverseWebFMLConfigurator {
    

// @LINE:12
def configurator(): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "configurator")
}
                                                

// @LINE:13
def applySelection(s:String): Call = {
   Call("POST", _prefix + { _defaultPrefix } + "applySelection" + queryString(List(Some(implicitly[QueryStringBindable[String]].unbind("s", s)))))
}
                                                
    
}
                          

// @LINE:18
// @LINE:17
// @LINE:16
// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:8
// @LINE:7
class ReverseWebFMLInterpreter {
    

// @LINE:7
def variable(id:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "variable/" + implicitly[PathBindable[String]].unbind("id", dynamicString(id)))
}
                                                

// @LINE:18
def listFiles(): Call = {
   Call("POST", _prefix + { _defaultPrefix } + "listFiles")
}
                                                

// @LINE:16
def loadFile(id:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "loadFile/" + implicitly[PathBindable[String]].unbind("id", dynamicString(id)))
}
                                                

// @LINE:9
def evalPrompt(s:String): Call = {
   Call("POST", _prefix + { _defaultPrefix } + "evalPrompt" + queryString(List(Some(implicitly[QueryStringBindable[String]].unbind("s", s)))))
}
                                                

// @LINE:17
def saveAs(content:String, filename:String): Call = {
   Call("POST", _prefix + { _defaultPrefix } + "saveAs" + queryString(List(Some(implicitly[QueryStringBindable[String]].unbind("content", content)), Some(implicitly[QueryStringBindable[String]].unbind("filename", filename)))))
}
                                                

// @LINE:11
def reset(): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "reset")
}
                                                

// @LINE:10
def ksynthesis(s:String): Call = {
   Call("POST", _prefix + { _defaultPrefix } + "ksynthesis" + queryString(List(Some(implicitly[QueryStringBindable[String]].unbind("s", s)))))
}
                                                

// @LINE:8
def interpret(s:String): Call = {
   Call("POST", _prefix + { _defaultPrefix } + "interpret" + queryString(List(Some(implicitly[QueryStringBindable[String]].unbind("s", s)))))
}
                                                
    
}
                          

// @LINE:23
class ReverseAssets {
    

// @LINE:23
def at(file:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[PathBindable[String]].unbind("file", file))
}
                                                
    
}
                          

// @LINE:20
// @LINE:6
class ReverseApplication {
    

// @LINE:6
def index(): Call = {
   Call("GET", _prefix)
}
                                                

// @LINE:20
def javascriptRoutes(): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "assets/javascripts/routes")
}
                                                
    
}
                          
}
                  


// @LINE:23
// @LINE:20
// @LINE:18
// @LINE:17
// @LINE:16
// @LINE:13
// @LINE:12
// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:8
// @LINE:7
// @LINE:6
package controllers.javascript {

// @LINE:13
// @LINE:12
class ReverseWebFMLConfigurator {
    

// @LINE:12
def configurator : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.WebFMLConfigurator.configurator",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "configurator"})
      }
   """
)
                        

// @LINE:13
def applySelection : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.WebFMLConfigurator.applySelection",
   """
      function(s) {
      return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "applySelection" + _qS([(""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("s", s)])})
      }
   """
)
                        
    
}
              

// @LINE:18
// @LINE:17
// @LINE:16
// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:8
// @LINE:7
class ReverseWebFMLInterpreter {
    

// @LINE:7
def variable : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.WebFMLInterpreter.variable",
   """
      function(id) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "variable/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("id", encodeURIComponent(id))})
      }
   """
)
                        

// @LINE:18
def listFiles : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.WebFMLInterpreter.listFiles",
   """
      function() {
      return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "listFiles"})
      }
   """
)
                        

// @LINE:16
def loadFile : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.WebFMLInterpreter.loadFile",
   """
      function(id) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "loadFile/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("id", encodeURIComponent(id))})
      }
   """
)
                        

// @LINE:9
def evalPrompt : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.WebFMLInterpreter.evalPrompt",
   """
      function(s) {
      return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "evalPrompt" + _qS([(""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("s", s)])})
      }
   """
)
                        

// @LINE:17
def saveAs : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.WebFMLInterpreter.saveAs",
   """
      function(content,filename) {
      return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "saveAs" + _qS([(""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("content", content), (""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("filename", filename)])})
      }
   """
)
                        

// @LINE:11
def reset : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.WebFMLInterpreter.reset",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "reset"})
      }
   """
)
                        

// @LINE:10
def ksynthesis : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.WebFMLInterpreter.ksynthesis",
   """
      function(s) {
      return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "ksynthesis" + _qS([(""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("s", s)])})
      }
   """
)
                        

// @LINE:8
def interpret : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.WebFMLInterpreter.interpret",
   """
      function(s) {
      return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "interpret" + _qS([(""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("s", s)])})
      }
   """
)
                        
    
}
              

// @LINE:23
class ReverseAssets {
    

// @LINE:23
def at : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Assets.at",
   """
      function(file) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file)})
      }
   """
)
                        
    
}
              

// @LINE:20
// @LINE:6
class ReverseApplication {
    

// @LINE:6
def index : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.index",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + """"})
      }
   """
)
                        

// @LINE:20
def javascriptRoutes : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.javascriptRoutes",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/javascripts/routes"})
      }
   """
)
                        
    
}
              
}
        


// @LINE:23
// @LINE:20
// @LINE:18
// @LINE:17
// @LINE:16
// @LINE:13
// @LINE:12
// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:8
// @LINE:7
// @LINE:6
package controllers.ref {


// @LINE:13
// @LINE:12
class ReverseWebFMLConfigurator {
    

// @LINE:12
def configurator(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLConfigurator.configurator(), HandlerDef(this, "controllers.WebFMLConfigurator", "configurator", Seq(), "GET", """""", _prefix + """configurator""")
)
                      

// @LINE:13
def applySelection(s:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLConfigurator.applySelection(s), HandlerDef(this, "controllers.WebFMLConfigurator", "applySelection", Seq(classOf[String]), "POST", """""", _prefix + """applySelection""")
)
                      
    
}
                          

// @LINE:18
// @LINE:17
// @LINE:16
// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:8
// @LINE:7
class ReverseWebFMLInterpreter {
    

// @LINE:7
def variable(id:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLInterpreter.variable(id), HandlerDef(this, "controllers.WebFMLInterpreter", "variable", Seq(classOf[String]), "GET", """""", _prefix + """variable/$id<[^/]+>""")
)
                      

// @LINE:18
def listFiles(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLInterpreter.listFiles(), HandlerDef(this, "controllers.WebFMLInterpreter", "listFiles", Seq(), "POST", """""", _prefix + """listFiles""")
)
                      

// @LINE:16
def loadFile(id:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLInterpreter.loadFile(id), HandlerDef(this, "controllers.WebFMLInterpreter", "loadFile", Seq(classOf[String]), "GET", """ Workspace management """, _prefix + """loadFile/$id<[^/]+>""")
)
                      

// @LINE:9
def evalPrompt(s:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLInterpreter.evalPrompt(s), HandlerDef(this, "controllers.WebFMLInterpreter", "evalPrompt", Seq(classOf[String]), "POST", """""", _prefix + """evalPrompt""")
)
                      

// @LINE:17
def saveAs(content:String, filename:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLInterpreter.saveAs(content, filename), HandlerDef(this, "controllers.WebFMLInterpreter", "saveAs", Seq(classOf[String], classOf[String]), "POST", """""", _prefix + """saveAs""")
)
                      

// @LINE:11
def reset(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLInterpreter.reset(), HandlerDef(this, "controllers.WebFMLInterpreter", "reset", Seq(), "GET", """""", _prefix + """reset""")
)
                      

// @LINE:10
def ksynthesis(s:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLInterpreter.ksynthesis(s), HandlerDef(this, "controllers.WebFMLInterpreter", "ksynthesis", Seq(classOf[String]), "POST", """""", _prefix + """ksynthesis""")
)
                      

// @LINE:8
def interpret(s:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLInterpreter.interpret(s), HandlerDef(this, "controllers.WebFMLInterpreter", "interpret", Seq(classOf[String]), "POST", """""", _prefix + """interpret""")
)
                      
    
}
                          

// @LINE:23
class ReverseAssets {
    

// @LINE:23
def at(path:String, file:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String]), "GET", """ Map static resources from the /public folder to the /assets URL path""", _prefix + """assets/$file<.+>""")
)
                      
    
}
                          

// @LINE:20
// @LINE:6
class ReverseApplication {
    

// @LINE:6
def index(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.index(), HandlerDef(this, "controllers.Application", "index", Seq(), "GET", """ Home page""", _prefix + """""")
)
                      

// @LINE:20
def javascriptRoutes(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.javascriptRoutes(), HandlerDef(this, "controllers.Application", "javascriptRoutes", Seq(), "GET", """""", _prefix + """assets/javascripts/routes""")
)
                      
    
}
                          
}
        
    