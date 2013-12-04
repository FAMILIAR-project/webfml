// @SOURCE:/home/gbecan/git/webfml/FMLApp/conf/routes
// @HASH:32c4b8af1752d2f9915de36a4b3cd18040f3b326
// @DATE:Wed Dec 04 17:07:41 CET 2013

import Routes.{prefix => _prefix, defaultPrefix => _defaultPrefix}
import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._


import Router.queryString


// @LINE:31
// @LINE:28
// @LINE:26
// @LINE:25
// @LINE:24
// @LINE:21
// @LINE:20
// @LINE:19
// @LINE:18
// @LINE:17
// @LINE:16
// @LINE:15
// @LINE:14
// @LINE:13
// @LINE:12
// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:8
// @LINE:7
// @LINE:6
package controllers {

// @LINE:21
// @LINE:20
class ReverseWebFMLConfigurator {
    

// @LINE:20
def configurator(): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "configurator")
}
                                                

// @LINE:21
def applySelection(s:String): Call = {
   Call("POST", _prefix + { _defaultPrefix } + "applySelection" + queryString(List(Some(implicitly[QueryStringBindable[String]].unbind("s", s)))))
}
                                                
    
}
                          

// @LINE:26
// @LINE:25
// @LINE:24
// @LINE:19
// @LINE:18
// @LINE:17
// @LINE:16
// @LINE:15
// @LINE:14
// @LINE:13
// @LINE:12
// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:8
// @LINE:7
class ReverseWebFMLInterpreter {
    

// @LINE:16
def getHeuristics(): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "getHeuristics")
}
                                                

// @LINE:7
def variable(id:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "variable/" + implicitly[PathBindable[String]].unbind("id", dynamicString(id)))
}
                                                

// @LINE:11
def selectParent(child:String, parent:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "selectParent" + queryString(List(Some(implicitly[QueryStringBindable[String]].unbind("child", child)), Some(implicitly[QueryStringBindable[String]].unbind("parent", parent)))))
}
                                                

// @LINE:14
def undo(): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "undo")
}
                                                

// @LINE:26
def listFiles(): Call = {
   Call("POST", _prefix + { _defaultPrefix } + "listFiles")
}
                                                

// @LINE:15
def redo(): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "redo")
}
                                                

// @LINE:24
def loadFile(id:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "loadFile/" + implicitly[PathBindable[String]].unbind("id", dynamicString(id)))
}
                                                

// @LINE:12
def ignoreParent(child:String, parent:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "ignoreParent" + queryString(List(Some(implicitly[QueryStringBindable[String]].unbind("child", child)), Some(implicitly[QueryStringBindable[String]].unbind("parent", parent)))))
}
                                                

// @LINE:18
def setClusteringParameters(heuristicName:String, threshold:Double): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "setClusteringParameters" + queryString(List(Some(implicitly[QueryStringBindable[String]].unbind("heuristicName", heuristicName)), Some(implicitly[QueryStringBindable[Double]].unbind("threshold", threshold)))))
}
                                                

// @LINE:9
def evalPrompt(s:String): Call = {
   Call("POST", _prefix + { _defaultPrefix } + "evalPrompt" + queryString(List(Some(implicitly[QueryStringBindable[String]].unbind("s", s)))))
}
                                                

// @LINE:25
def saveAs(content:String, filename:String): Call = {
   Call("POST", _prefix + { _defaultPrefix } + "saveAs" + queryString(List(Some(implicitly[QueryStringBindable[String]].unbind("content", content)), Some(implicitly[QueryStringBindable[String]].unbind("filename", filename)))))
}
                                                

// @LINE:19
def reset(): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "reset")
}
                                                

// @LINE:17
def setRankingListsHeuristic(heuristicName:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "setRankingListsHeuristic" + queryString(List(Some(implicitly[QueryStringBindable[String]].unbind("heuristicName", heuristicName)))))
}
                                                

// @LINE:13
def completeFM(): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "completeFM")
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
                          

// @LINE:31
class ReverseAssets {
    

// @LINE:31
def at(file:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[PathBindable[String]].unbind("file", file))
}
                                                
    
}
                          

// @LINE:28
// @LINE:6
class ReverseApplication {
    

// @LINE:6
def index(): Call = {
   Call("GET", _prefix)
}
                                                

// @LINE:28
def javascriptRoutes(): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "assets/javascripts/routes")
}
                                                
    
}
                          
}
                  


// @LINE:31
// @LINE:28
// @LINE:26
// @LINE:25
// @LINE:24
// @LINE:21
// @LINE:20
// @LINE:19
// @LINE:18
// @LINE:17
// @LINE:16
// @LINE:15
// @LINE:14
// @LINE:13
// @LINE:12
// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:8
// @LINE:7
// @LINE:6
package controllers.javascript {

// @LINE:21
// @LINE:20
class ReverseWebFMLConfigurator {
    

// @LINE:20
def configurator : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.WebFMLConfigurator.configurator",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "configurator"})
      }
   """
)
                        

// @LINE:21
def applySelection : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.WebFMLConfigurator.applySelection",
   """
      function(s) {
      return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "applySelection" + _qS([(""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("s", s)])})
      }
   """
)
                        
    
}
              

// @LINE:26
// @LINE:25
// @LINE:24
// @LINE:19
// @LINE:18
// @LINE:17
// @LINE:16
// @LINE:15
// @LINE:14
// @LINE:13
// @LINE:12
// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:8
// @LINE:7
class ReverseWebFMLInterpreter {
    

// @LINE:16
def getHeuristics : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.WebFMLInterpreter.getHeuristics",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "getHeuristics"})
      }
   """
)
                        

// @LINE:7
def variable : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.WebFMLInterpreter.variable",
   """
      function(id) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "variable/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("id", encodeURIComponent(id))})
      }
   """
)
                        

// @LINE:11
def selectParent : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.WebFMLInterpreter.selectParent",
   """
      function(child,parent) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "selectParent" + _qS([(""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("child", child), (""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("parent", parent)])})
      }
   """
)
                        

// @LINE:14
def undo : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.WebFMLInterpreter.undo",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "undo"})
      }
   """
)
                        

// @LINE:26
def listFiles : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.WebFMLInterpreter.listFiles",
   """
      function() {
      return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "listFiles"})
      }
   """
)
                        

// @LINE:15
def redo : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.WebFMLInterpreter.redo",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "redo"})
      }
   """
)
                        

// @LINE:24
def loadFile : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.WebFMLInterpreter.loadFile",
   """
      function(id) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "loadFile/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("id", encodeURIComponent(id))})
      }
   """
)
                        

// @LINE:12
def ignoreParent : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.WebFMLInterpreter.ignoreParent",
   """
      function(child,parent) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "ignoreParent" + _qS([(""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("child", child), (""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("parent", parent)])})
      }
   """
)
                        

// @LINE:18
def setClusteringParameters : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.WebFMLInterpreter.setClusteringParameters",
   """
      function(heuristicName,threshold) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "setClusteringParameters" + _qS([(""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("heuristicName", heuristicName), (""" + implicitly[QueryStringBindable[Double]].javascriptUnbind + """)("threshold", threshold)])})
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
                        

// @LINE:25
def saveAs : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.WebFMLInterpreter.saveAs",
   """
      function(content,filename) {
      return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "saveAs" + _qS([(""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("content", content), (""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("filename", filename)])})
      }
   """
)
                        

// @LINE:19
def reset : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.WebFMLInterpreter.reset",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "reset"})
      }
   """
)
                        

// @LINE:17
def setRankingListsHeuristic : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.WebFMLInterpreter.setRankingListsHeuristic",
   """
      function(heuristicName) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "setRankingListsHeuristic" + _qS([(""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("heuristicName", heuristicName)])})
      }
   """
)
                        

// @LINE:13
def completeFM : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.WebFMLInterpreter.completeFM",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "completeFM"})
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
              

// @LINE:31
class ReverseAssets {
    

// @LINE:31
def at : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Assets.at",
   """
      function(file) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file)})
      }
   """
)
                        
    
}
              

// @LINE:28
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
                        

// @LINE:28
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
        


// @LINE:31
// @LINE:28
// @LINE:26
// @LINE:25
// @LINE:24
// @LINE:21
// @LINE:20
// @LINE:19
// @LINE:18
// @LINE:17
// @LINE:16
// @LINE:15
// @LINE:14
// @LINE:13
// @LINE:12
// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:8
// @LINE:7
// @LINE:6
package controllers.ref {


// @LINE:21
// @LINE:20
class ReverseWebFMLConfigurator {
    

// @LINE:20
def configurator(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLConfigurator.configurator(), HandlerDef(this, "controllers.WebFMLConfigurator", "configurator", Seq(), "GET", """""", _prefix + """configurator""")
)
                      

// @LINE:21
def applySelection(s:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLConfigurator.applySelection(s), HandlerDef(this, "controllers.WebFMLConfigurator", "applySelection", Seq(classOf[String]), "POST", """""", _prefix + """applySelection""")
)
                      
    
}
                          

// @LINE:26
// @LINE:25
// @LINE:24
// @LINE:19
// @LINE:18
// @LINE:17
// @LINE:16
// @LINE:15
// @LINE:14
// @LINE:13
// @LINE:12
// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:8
// @LINE:7
class ReverseWebFMLInterpreter {
    

// @LINE:16
def getHeuristics(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLInterpreter.getHeuristics(), HandlerDef(this, "controllers.WebFMLInterpreter", "getHeuristics", Seq(), "GET", """""", _prefix + """getHeuristics""")
)
                      

// @LINE:7
def variable(id:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLInterpreter.variable(id), HandlerDef(this, "controllers.WebFMLInterpreter", "variable", Seq(classOf[String]), "GET", """""", _prefix + """variable/$id<[^/]+>""")
)
                      

// @LINE:11
def selectParent(child:String, parent:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLInterpreter.selectParent(child, parent), HandlerDef(this, "controllers.WebFMLInterpreter", "selectParent", Seq(classOf[String], classOf[String]), "GET", """""", _prefix + """selectParent""")
)
                      

// @LINE:14
def undo(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLInterpreter.undo(), HandlerDef(this, "controllers.WebFMLInterpreter", "undo", Seq(), "GET", """""", _prefix + """undo""")
)
                      

// @LINE:26
def listFiles(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLInterpreter.listFiles(), HandlerDef(this, "controllers.WebFMLInterpreter", "listFiles", Seq(), "POST", """""", _prefix + """listFiles""")
)
                      

// @LINE:15
def redo(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLInterpreter.redo(), HandlerDef(this, "controllers.WebFMLInterpreter", "redo", Seq(), "GET", """""", _prefix + """redo""")
)
                      

// @LINE:24
def loadFile(id:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLInterpreter.loadFile(id), HandlerDef(this, "controllers.WebFMLInterpreter", "loadFile", Seq(classOf[String]), "GET", """ Workspace management """, _prefix + """loadFile/$id<[^/]+>""")
)
                      

// @LINE:12
def ignoreParent(child:String, parent:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLInterpreter.ignoreParent(child, parent), HandlerDef(this, "controllers.WebFMLInterpreter", "ignoreParent", Seq(classOf[String], classOf[String]), "GET", """""", _prefix + """ignoreParent""")
)
                      

// @LINE:18
def setClusteringParameters(heuristicName:String, threshold:Double): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLInterpreter.setClusteringParameters(heuristicName, threshold), HandlerDef(this, "controllers.WebFMLInterpreter", "setClusteringParameters", Seq(classOf[String], classOf[Double]), "GET", """""", _prefix + """setClusteringParameters""")
)
                      

// @LINE:9
def evalPrompt(s:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLInterpreter.evalPrompt(s), HandlerDef(this, "controllers.WebFMLInterpreter", "evalPrompt", Seq(classOf[String]), "POST", """""", _prefix + """evalPrompt""")
)
                      

// @LINE:25
def saveAs(content:String, filename:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLInterpreter.saveAs(content, filename), HandlerDef(this, "controllers.WebFMLInterpreter", "saveAs", Seq(classOf[String], classOf[String]), "POST", """""", _prefix + """saveAs""")
)
                      

// @LINE:19
def reset(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLInterpreter.reset(), HandlerDef(this, "controllers.WebFMLInterpreter", "reset", Seq(), "GET", """""", _prefix + """reset""")
)
                      

// @LINE:17
def setRankingListsHeuristic(heuristicName:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLInterpreter.setRankingListsHeuristic(heuristicName), HandlerDef(this, "controllers.WebFMLInterpreter", "setRankingListsHeuristic", Seq(classOf[String]), "GET", """""", _prefix + """setRankingListsHeuristic""")
)
                      

// @LINE:13
def completeFM(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.WebFMLInterpreter.completeFM(), HandlerDef(this, "controllers.WebFMLInterpreter", "completeFM", Seq(), "GET", """""", _prefix + """completeFM""")
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
                          

// @LINE:31
class ReverseAssets {
    

// @LINE:31
def at(path:String, file:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String]), "GET", """ Map static resources from the /public folder to the /assets URL path""", _prefix + """assets/$file<.+>""")
)
                      
    
}
                          

// @LINE:28
// @LINE:6
class ReverseApplication {
    

// @LINE:6
def index(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.index(), HandlerDef(this, "controllers.Application", "index", Seq(), "GET", """ Home page""", _prefix + """""")
)
                      

// @LINE:28
def javascriptRoutes(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.javascriptRoutes(), HandlerDef(this, "controllers.Application", "javascriptRoutes", Seq(), "GET", """""", _prefix + """assets/javascripts/routes""")
)
                      
    
}
                          
}
        
    