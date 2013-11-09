
package views.html

import play.templates._
import play.templates.TemplateMagic._

import play.api.templates._
import play.api.templates.PlayMagic._
import models._
import controllers._
import play.api.i18n._
import play.api.mvc._
import play.api.data._
import views.html._
/**/
object index extends BaseScalaTemplate[play.api.templates.HtmlFormat.Appendable,Format[play.api.templates.HtmlFormat.Appendable]](play.api.templates.HtmlFormat) with play.api.templates.Template1[String,play.api.templates.HtmlFormat.Appendable] {

    /**/
    def apply/*1.2*/(message: String):play.api.templates.HtmlFormat.Appendable = {
        _display_ {

Seq[Any](format.raw/*1.19*/("""

"""),_display_(Seq[Any](/*3.2*/mainFML("Welcome to FAMILIAR!")/*3.33*/ {_display_(Seq[Any](format.raw/*3.35*/("""

<!-- facilities for FML variables -->	    
<script src=""""),_display_(Seq[Any](/*6.15*/routes/*6.21*/.Assets.at("javascripts/FMLvariables.js"))),format.raw/*6.62*/(""""></script>

<!-- EDITOR -->		
<script src=""""),_display_(Seq[Any](/*9.15*/routes/*9.21*/.Assets.at("javascripts/FMLeditor.js"))),format.raw/*9.59*/("""" type="text/javascript" charset="utf-8"></script>
<!-- begin ACE editor -->
<script src=""""),_display_(Seq[Any](/*11.15*/routes/*11.21*/.Assets.at("javascripts/ace-builds/src-noconflict/ace.js"))),format.raw/*11.79*/("""" type="text/javascript" charset="utf-8"></script>
<script src=""""),_display_(Seq[Any](/*12.15*/routes/*12.21*/.Assets.at("javascripts/ace-builds/src-noconflict/theme-monokai.js"))),format.raw/*12.89*/("""" type="text/javascript" charset="utf-8"></script>
<script src=""""),_display_(Seq[Any](/*13.15*/routes/*13.21*/.Assets.at("javascripts/ace-builds/src-noconflict/mode-javascript.js"))),format.raw/*13.91*/("""" type="text/javascript" charset="utf-8"></script>
<!-- end ACE editor (style) -->
<!-- end EDITOR -->

<!-- CONSOLE -->
 <link rel="stylesheet" type="text/css" href=""""),_display_(Seq[Any](/*18.48*/routes/*18.54*/.Assets.at("stylesheets/ansi.css"))),format.raw/*18.88*/("""">
 <script src=""""),_display_(Seq[Any](/*19.16*/routes/*19.22*/.Assets.at("javascripts/jq-console/jqconsole.min.js"))),format.raw/*19.75*/(""""></script> 
 <script src=""""),_display_(Seq[Any](/*20.16*/routes/*20.22*/.Assets.at("javascripts/FMLconsole.js"))),format.raw/*20.61*/(""""></script>
<!-- end CONSOLE -->

<!-- WORKSPACE -->
<script src="http://cdn.alloyui.com/2.0.0/aui/aui-min.js"></script>
<link href="http://cdn.alloyui.com/2.0.0/aui-css/css/bootstrap.min.css" rel="stylesheet"></link>
<!-- end WORKSPACE -->

<!-- begin styling CONSOLE -->
<style>
 
      .jqconsole """),format.raw/*31.18*/("""{"""),format.raw/*31.19*/("""
        padding: 10px;
        padding-bottom: 10px;
      """),format.raw/*34.7*/("""}"""),format.raw/*34.8*/("""
      .jqconsole-cursor """),format.raw/*35.25*/("""{"""),format.raw/*35.26*/("""
        background-color: #999;
      """),format.raw/*37.7*/("""}"""),format.raw/*37.8*/("""
      .jqconsole-blurred .jqconsole-cursor """),format.raw/*38.44*/("""{"""),format.raw/*38.45*/("""
        background-color: #666;
      """),format.raw/*40.7*/("""}"""),format.raw/*40.8*/("""
      .jqconsole-prompt """),format.raw/*41.25*/("""{"""),format.raw/*41.26*/("""
        color: #0d0;
      """),format.raw/*43.7*/("""}"""),format.raw/*43.8*/("""
      .jqconsole-old-prompt """),format.raw/*44.29*/("""{"""),format.raw/*44.30*/("""
        color: #0b0;
        font-weight: normal;
      """),format.raw/*47.7*/("""}"""),format.raw/*47.8*/("""
      .jqconsole-input """),format.raw/*48.24*/("""{"""),format.raw/*48.25*/("""
        color: #dd0;
      """),format.raw/*50.7*/("""}"""),format.raw/*50.8*/("""
      .jqconsole-old-input """),format.raw/*51.28*/("""{"""),format.raw/*51.29*/("""
        color: #bb0;
        font-weight: normal;
      """),format.raw/*54.7*/("""}"""),format.raw/*54.8*/("""
      .brace """),format.raw/*55.14*/("""{"""),format.raw/*55.15*/("""
        color: #00FFFF;
      """),format.raw/*57.7*/("""}"""),format.raw/*57.8*/("""
      .paran """),format.raw/*58.14*/("""{"""),format.raw/*58.15*/("""
        color: #FF00FF;
      """),format.raw/*60.7*/("""}"""),format.raw/*60.8*/("""
      .bracket """),format.raw/*61.16*/("""{"""),format.raw/*61.17*/("""
        color: #FFFF00;
      """),format.raw/*63.7*/("""}"""),format.raw/*63.8*/("""
      .jqconsole-composition """),format.raw/*64.30*/("""{"""),format.raw/*64.31*/("""
        background-color: red;
      """),format.raw/*66.7*/("""}"""),format.raw/*66.8*/("""
    </style>
 
<!-- end styling CONSOLE -->


<style type="text/css" media="screen">

    #ksynthesis """),format.raw/*74.17*/("""{"""),format.raw/*74.18*/(""" 
       
    """),format.raw/*76.5*/("""}"""),format.raw/*76.6*/("""
    
     #console """),format.raw/*78.15*/("""{"""),format.raw/*78.16*/("""
        position: absolute ; 
        left: 0 ;
        width : 20% ;
        height: 100% ;   
        background-color: black;
        color: white ;
        border: 2px solid #CCC;     
        """),format.raw/*86.9*/("""}"""),format.raw/*86.10*/("""
    
     #editor """),format.raw/*88.14*/("""{"""),format.raw/*88.15*/(""" 
        position: absolute ;
        width: 30%;
    	height: 100%;
     """),format.raw/*92.6*/("""}"""),format.raw/*92.7*/("""
 </style>
  


</head>
<body>


<script src=""""),_display_(Seq[Any](/*101.15*/routes/*101.21*/.Assets.at("javascripts/bootstrap.js"))),format.raw/*101.59*/("""" type="text/javascript"></script>

	

<div class="container">
    <div class="span7">
      
       <!--  console -->
	   <div id="console">
	   </div>
    </div>
      
      
    <div class="span4">
    	
<div id="wait">    	
    	</div>
	    <div class="btn-group">
		  	<button class="btn" id="webFmlCmd">Execute FAMILIAR code</button>
		  	<button class="btn" id="webFmlReset">Reset variables' environment</button>
		  	<button class="btn" id="webFmlSaveAs">Save as...</button>
		  	<form class="form-inline" role="form">
	 		 <div class="form-group">
	     		<input type="text" class="form-control" id="fileInputName" placeholder="Enter filename...">
	  		</div>
			</form>
		</div>
	
	     <!--  input editor -->
			
		<div id="editor">
		"""),_display_(Seq[Any](/*132.4*/message)),format.raw/*132.11*/("""
		</div>
 
		<script>
			var editor ;
			window.onload = function() """),format.raw/*137.31*/("""{"""),format.raw/*137.32*/("""
			    editor = ace.edit("editor");
			    editor.setTheme("ace/theme/monokai");
			    
			    var JavaScriptMode = ace.require("ace/mode/javascript").Mode;
			    editor.getSession().setMode(new JavaScriptMode());
			"""),format.raw/*143.4*/("""}"""),format.raw/*143.5*/(""";
		</script> 
		
		

    </div>
    
     <div class="span8">
     
        
    	<div id="msgid">    	
    	</div>	
		
		
		
		
		<div id="ksynthesis">
		</div>	
 	</div>
 	
 	<div class="span10" id="workspace">
 	<button class="btn" onclick="updateWorkspace() ;">Refresh...</button>
 		 <div id="myTreeView"> 		 
 		 </div> 
 		 
    </div>
    
    
	

</div>

 
    



 
 </body>
""")))})),format.raw/*182.2*/("""


"""))}
    }
    
    def render(message:String): play.api.templates.HtmlFormat.Appendable = apply(message)
    
    def f:((String) => play.api.templates.HtmlFormat.Appendable) = (message) => apply(message)
    
    def ref: this.type = this

}
                /*
                    -- GENERATED --
                    DATE: Fri Nov 08 11:32:37 CET 2013
                    SOURCE: /Users/macher1/git/webfml/FMLApp/app/views/index.scala.html
                    HASH: 2a4f7e2a42b87ada7f640cd5f9fa8a257aada779
                    MATRIX: 556->1|667->18|704->21|743->52|782->54|876->113|890->119|952->160|1032->205|1046->211|1105->249|1232->340|1247->346|1327->404|1428->469|1443->475|1533->543|1634->608|1649->614|1741->684|1945->852|1960->858|2016->892|2070->910|2085->916|2160->969|2224->997|2239->1003|2300->1042|2628->1342|2657->1343|2744->1403|2772->1404|2825->1429|2854->1430|2920->1469|2948->1470|3020->1514|3049->1515|3115->1554|3143->1555|3196->1580|3225->1581|3280->1609|3308->1610|3365->1639|3394->1640|3478->1697|3506->1698|3558->1722|3587->1723|3642->1751|3670->1752|3726->1780|3755->1781|3839->1838|3867->1839|3909->1853|3938->1854|3996->1885|4024->1886|4066->1900|4095->1901|4153->1932|4181->1933|4225->1949|4254->1950|4312->1981|4340->1982|4398->2012|4427->2013|4492->2051|4520->2052|4651->2155|4680->2156|4721->2170|4749->2171|4797->2191|4826->2192|5051->2390|5080->2391|5127->2410|5156->2411|5258->2486|5286->2487|5370->2534|5386->2540|5447->2578|6231->3326|6261->3333|6359->3402|6389->3403|6637->3623|6666->3624|7085->4011
                    LINES: 19->1|22->1|24->3|24->3|24->3|27->6|27->6|27->6|30->9|30->9|30->9|32->11|32->11|32->11|33->12|33->12|33->12|34->13|34->13|34->13|39->18|39->18|39->18|40->19|40->19|40->19|41->20|41->20|41->20|52->31|52->31|55->34|55->34|56->35|56->35|58->37|58->37|59->38|59->38|61->40|61->40|62->41|62->41|64->43|64->43|65->44|65->44|68->47|68->47|69->48|69->48|71->50|71->50|72->51|72->51|75->54|75->54|76->55|76->55|78->57|78->57|79->58|79->58|81->60|81->60|82->61|82->61|84->63|84->63|85->64|85->64|87->66|87->66|95->74|95->74|97->76|97->76|99->78|99->78|107->86|107->86|109->88|109->88|113->92|113->92|122->101|122->101|122->101|153->132|153->132|158->137|158->137|164->143|164->143|203->182
                    -- GENERATED --
                */
            