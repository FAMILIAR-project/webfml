
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
object mainFML extends BaseScalaTemplate[play.api.templates.HtmlFormat.Appendable,Format[play.api.templates.HtmlFormat.Appendable]](play.api.templates.HtmlFormat) with play.api.templates.Template2[String,Html,play.api.templates.HtmlFormat.Appendable] {

    /**/
    def apply/*1.2*/(title: String)(content: Html):play.api.templates.HtmlFormat.Appendable = {
        _display_ {

Seq[Any](format.raw/*1.32*/("""

<!DOCTYPE html>

<html>
    <head>
        <title>"""),_display_(Seq[Any](/*7.17*/title)),format.raw/*7.22*/("""</title>
        <link rel="stylesheet" media="text/css" href=""""),_display_(Seq[Any](/*8.56*/routes/*8.62*/.Assets.at("stylesheets/main.css"))),format.raw/*8.96*/("""">
        <link rel="shortcut icon" type="image/png" href=""""),_display_(Seq[Any](/*9.59*/routes/*9.65*/.Assets.at("images/favicon.png"))),format.raw/*9.97*/("""">
        <script src=""""),_display_(Seq[Any](/*10.23*/routes/*10.29*/.Assets.at("javascripts/jquery-1.7.1.min.js"))),format.raw/*10.74*/("""" type="text/javascript"></script>

 		<script type="text/javascript" src=""""),_display_(Seq[Any](/*12.41*/routes/*12.47*/.Application.javascriptRoutes)),format.raw/*12.76*/(""""></script>
		<script src=""""),_display_(Seq[Any](/*13.17*/routes/*13.23*/.Assets.at("javascripts/FML-callback.js"))),format.raw/*13.64*/("""" type="text/javascript" charset="utf-8"></script>
 
 
 <!-- CONSOLE -->
  <link rel="stylesheet" type="text/css" href=""""),_display_(Seq[Any](/*17.49*/routes/*17.55*/.Assets.at("stylesheets/ansi.css"))),format.raw/*17.89*/("""">
 <script src=""""),_display_(Seq[Any](/*18.16*/routes/*18.22*/.Assets.at("javascripts/jqconsole-2.7.min.js"))),format.raw/*18.68*/(""""></script> 
 <script src=""""),_display_(Seq[Any](/*19.16*/routes/*19.22*/.Assets.at("javascripts/FMLconsole.js"))),format.raw/*19.61*/(""""></script>
 <!-- end CONSOLE -->

     
        """),_display_(Seq[Any](/*23.10*/content)),format.raw/*23.17*/("""
    
</html>
"""))}
    }
    
    def render(title:String,content:Html): play.api.templates.HtmlFormat.Appendable = apply(title)(content)
    
    def f:((String) => (Html) => play.api.templates.HtmlFormat.Appendable) = (title) => (content) => apply(title)(content)
    
    def ref: this.type = this

}
                /*
                    -- GENERATED --
                    DATE: Thu Oct 31 16:23:14 CET 2013
                    SOURCE: /Users/macher1/git/webfml/FMLApp/app/views/mainFML.scala.html
                    HASH: b98d0beb7064ec276445b6faac08213bfb00657b
                    MATRIX: 563->1|687->31|775->84|801->89|900->153|914->159|969->193|1065->254|1079->260|1132->292|1193->317|1208->323|1275->368|1387->444|1402->450|1453->479|1517->507|1532->513|1595->554|1752->675|1767->681|1823->715|1877->733|1892->739|1960->785|2024->813|2039->819|2100->858|2186->908|2215->915
                    LINES: 19->1|22->1|28->7|28->7|29->8|29->8|29->8|30->9|30->9|30->9|31->10|31->10|31->10|33->12|33->12|33->12|34->13|34->13|34->13|38->17|38->17|38->17|39->18|39->18|39->18|40->19|40->19|40->19|44->23|44->23
                    -- GENERATED --
                */
            