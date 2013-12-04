
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

<html ng-app>
    <head>
        <title>"""),_display_(Seq[Any](/*7.17*/title)),format.raw/*7.22*/("""</title>
        <link rel="stylesheet" media="text/css" href=""""),_display_(Seq[Any](/*8.56*/routes/*8.62*/.Assets.at("stylesheets/main.css"))),format.raw/*8.96*/("""">
        <link rel="shortcut icon" type="image/png" href=""""),_display_(Seq[Any](/*9.59*/routes/*9.65*/.Assets.at("images/favicon.png"))),format.raw/*9.97*/("""">
        <script src=""""),_display_(Seq[Any](/*10.23*/routes/*10.29*/.Assets.at("javascripts/jquery.min.js"))),format.raw/*10.68*/("""" type="text/javascript"></script>
        <script src=""""),_display_(Seq[Any](/*11.23*/routes/*11.29*/.Assets.at("javascripts/jquery-migrate.min.js"))),format.raw/*11.76*/("""" type="text/javascript"></script>
 		<script src=""""),_display_(Seq[Any](/*12.18*/routes/*12.24*/.Application.javascriptRoutes)),format.raw/*12.53*/("""" type="text/javascript"></script>
		<script src=""""),_display_(Seq[Any](/*13.17*/routes/*13.23*/.Assets.at("javascripts/angular-1.2.2/angular.min.js"))),format.raw/*13.77*/("""" type="text/javascript"></script>
 	    
 	    <link rel="stylesheet" media="screen" href=""""),_display_(Seq[Any](/*15.52*/routes/*15.58*/.Assets.at("stylesheets/bootstrap.min.css"))),format.raw/*15.101*/("""">
        
        """),_display_(Seq[Any](/*17.10*/content)),format.raw/*17.17*/("""
    
</html>
"""))}
    }
    
    def render(title:String,content:Html): play.api.templates.HtmlFormat.Appendable = apply(title)(content)
    
    def f:((String) => (Html) => play.api.templates.HtmlFormat.Appendable) = (title) => (content) => apply(title)(content)
    
    def ref: this.type = this

}
                /*
                    -- GENERATED --
                    DATE: Wed Dec 04 17:07:43 CET 2013
                    SOURCE: /home/gbecan/git/webfml/FMLApp/app/views/mainFML.scala.html
                    HASH: cecba94cf599f219bde58bac13c7ac959fbea46a
                    MATRIX: 563->1|687->31|782->91|808->96|907->160|921->166|976->200|1072->261|1086->267|1139->299|1200->324|1215->330|1276->369|1369->426|1384->432|1453->479|1541->531|1556->537|1607->566|1694->617|1709->623|1785->677|1914->770|1929->776|1995->819|2052->840|2081->847
                    LINES: 19->1|22->1|28->7|28->7|29->8|29->8|29->8|30->9|30->9|30->9|31->10|31->10|31->10|32->11|32->11|32->11|33->12|33->12|33->12|34->13|34->13|34->13|36->15|36->15|36->15|38->17|38->17
                    -- GENERATED --
                */
            