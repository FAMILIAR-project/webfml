
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
    
 
<!-- begin ACE editor -->
<script src=""""),_display_(Seq[Any](/*7.15*/routes/*7.21*/.Assets.at("javascripts/ace-builds/src-noconflict/ace.js"))),format.raw/*7.79*/("""" type="text/javascript" charset="utf-8"></script>
<script src=""""),_display_(Seq[Any](/*8.15*/routes/*8.21*/.Assets.at("javascripts/ace-builds/src-noconflict/theme-twilight.js"))),format.raw/*8.90*/("""" type="text/javascript" charset="utf-8"></script>
<script src=""""),_display_(Seq[Any](/*9.15*/routes/*9.21*/.Assets.at("javascripts/ace-builds/src-noconflict/mode-javascript.js"))),format.raw/*9.91*/("""" type="text/javascript" charset="utf-8"></script>
<!-- end ACE editor (style) -->

<!-- begin styling CONSOLE -->
<style>
 
      .jqconsole """),format.raw/*15.18*/("""{"""),format.raw/*15.19*/("""
        padding: 10px;
        padding-bottom: 10px;
      """),format.raw/*18.7*/("""}"""),format.raw/*18.8*/("""
      .jqconsole-cursor """),format.raw/*19.25*/("""{"""),format.raw/*19.26*/("""
        background-color: #999;
      """),format.raw/*21.7*/("""}"""),format.raw/*21.8*/("""
      .jqconsole-blurred .jqconsole-cursor """),format.raw/*22.44*/("""{"""),format.raw/*22.45*/("""
        background-color: #666;
      """),format.raw/*24.7*/("""}"""),format.raw/*24.8*/("""
      .jqconsole-prompt """),format.raw/*25.25*/("""{"""),format.raw/*25.26*/("""
        color: #0d0;
      """),format.raw/*27.7*/("""}"""),format.raw/*27.8*/("""
      .jqconsole-old-prompt """),format.raw/*28.29*/("""{"""),format.raw/*28.30*/("""
        color: #0b0;
        font-weight: normal;
      """),format.raw/*31.7*/("""}"""),format.raw/*31.8*/("""
      .jqconsole-input """),format.raw/*32.24*/("""{"""),format.raw/*32.25*/("""
        color: #dd0;
      """),format.raw/*34.7*/("""}"""),format.raw/*34.8*/("""
      .jqconsole-old-input """),format.raw/*35.28*/("""{"""),format.raw/*35.29*/("""
        color: #bb0;
        font-weight: normal;
      """),format.raw/*38.7*/("""}"""),format.raw/*38.8*/("""
      .brace """),format.raw/*39.14*/("""{"""),format.raw/*39.15*/("""
        color: #00FFFF;
      """),format.raw/*41.7*/("""}"""),format.raw/*41.8*/("""
      .paran """),format.raw/*42.14*/("""{"""),format.raw/*42.15*/("""
        color: #FF00FF;
      """),format.raw/*44.7*/("""}"""),format.raw/*44.8*/("""
      .bracket """),format.raw/*45.16*/("""{"""),format.raw/*45.17*/("""
        color: #FFFF00;
      """),format.raw/*47.7*/("""}"""),format.raw/*47.8*/("""
      .jqconsole-composition """),format.raw/*48.30*/("""{"""),format.raw/*48.31*/("""
        background-color: red;
      """),format.raw/*50.7*/("""}"""),format.raw/*50.8*/("""
    </style>
 
<!-- end styling CONSOLE -->
 
<style type="text/css" media="screen">
 
 	body """),format.raw/*57.8*/("""{"""),format.raw/*57.9*/("""
 	background-color: #cacaca
 	"""),format.raw/*59.3*/("""}"""),format.raw/*59.4*/("""
 	 	
       
    .myConsole """),format.raw/*62.16*/("""{"""),format.raw/*62.17*/("""
	    position: absolute ; 
	    top : 5 ;
	  	right: 300 ;
	  	text-align: left; 
	  	width : 500px ;
	  	height: 250px ;   
	  	background-color: black;
	  	color: white ;
        border: 2px solid #CCC; 	
    """),format.raw/*72.5*/("""}"""),format.raw/*72.6*/("""
       
    #scriptInput """),format.raw/*74.18*/("""{"""),format.raw/*74.19*/(""" 
        position: relative;
        width: 600px;
        height: 250px;
    """),format.raw/*78.5*/("""}"""),format.raw/*78.6*/("""
    #ksynthesis """),format.raw/*79.17*/("""{"""),format.raw/*79.18*/(""" 
        position: absolute;
         bottom : 5 ;
	 left: 300 ;
    """),format.raw/*83.5*/("""}"""),format.raw/*83.6*/("""
 </style>
  


</head>
<body>
    
    <!--  input editor -->
<div id="FMLEditor">
	<div class="divFormForScriptInput">

<pre id="scriptInput">
"""),_display_(Seq[Any](/*96.2*/message)),format.raw/*96.9*/("""
</pre>
 
		<script>
			var editor ;
			window.onload = function() """),format.raw/*101.31*/("""{"""),format.raw/*101.32*/("""
			    editor = ace.edit("scriptInput");
			    editor.setTheme("ace/theme/twilight");
			    
			    var JavaScriptMode = ace.require("ace/mode/javascript").Mode;
			    editor.getSession().setMode(new JavaScriptMode());
			"""),format.raw/*107.4*/("""}"""),format.raw/*107.5*/(""";
			</script> 

	</div>
	<button id="webFmlCmd">Execute code</button>
	<button id="webFmlReset">Reset variables</button>
</div> 


<div id="msgid">
</div>	




<!--  console -->
<div id="console" class="myConsole">
</div>

<div id="ksynthesis">
</div>	



 
 </body>
""")))})),format.raw/*133.2*/("""


"""))}
    }
    
    def render(message:String): play.api.templates.HtmlFormat.Appendable = apply(message)
    
    def f:((String) => play.api.templates.HtmlFormat.Appendable) = (message) => apply(message)
    
    def ref: this.type = this

}
                /*
                    -- GENERATED --
                    DATE: Thu Oct 31 17:07:22 CET 2013
                    SOURCE: /Users/macher1/git/webfml/FMLApp/app/views/index.scala.html
                    HASH: d7a25ff26bf0118bf7f91965aa7c2a4ddea5365a
                    MATRIX: 556->1|667->18|704->21|743->52|782->54|865->102|879->108|958->166|1058->231|1072->237|1162->306|1262->371|1276->377|1367->447|1537->589|1566->590|1653->650|1681->651|1734->676|1763->677|1829->716|1857->717|1929->761|1958->762|2024->801|2052->802|2105->827|2134->828|2189->856|2217->857|2274->886|2303->887|2387->944|2415->945|2467->969|2496->970|2551->998|2579->999|2635->1027|2664->1028|2748->1085|2776->1086|2818->1100|2847->1101|2905->1132|2933->1133|2975->1147|3004->1148|3062->1179|3090->1180|3134->1196|3163->1197|3221->1228|3249->1229|3307->1259|3336->1260|3401->1298|3429->1299|3551->1394|3579->1395|3637->1426|3665->1427|3722->1456|3751->1457|3990->1669|4018->1670|4072->1696|4101->1697|4207->1776|4235->1777|4280->1794|4309->1795|4406->1865|4434->1866|4615->2012|4643->2019|4739->2086|4769->2087|5023->2313|5052->2314|5353->2583
                    LINES: 19->1|22->1|24->3|24->3|24->3|28->7|28->7|28->7|29->8|29->8|29->8|30->9|30->9|30->9|36->15|36->15|39->18|39->18|40->19|40->19|42->21|42->21|43->22|43->22|45->24|45->24|46->25|46->25|48->27|48->27|49->28|49->28|52->31|52->31|53->32|53->32|55->34|55->34|56->35|56->35|59->38|59->38|60->39|60->39|62->41|62->41|63->42|63->42|65->44|65->44|66->45|66->45|68->47|68->47|69->48|69->48|71->50|71->50|78->57|78->57|80->59|80->59|83->62|83->62|93->72|93->72|95->74|95->74|99->78|99->78|100->79|100->79|104->83|104->83|117->96|117->96|122->101|122->101|128->107|128->107|154->133
                    -- GENERATED --
                */
            