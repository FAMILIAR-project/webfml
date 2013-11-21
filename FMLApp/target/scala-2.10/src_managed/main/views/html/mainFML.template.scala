
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
        <script src=""""),_display_(Seq[Any](/*10.23*/routes/*10.29*/.Assets.at("javascripts/jquery.min.js"))),format.raw/*10.68*/("""" type="text/javascript"></script>
        <script src=""""),_display_(Seq[Any](/*11.23*/routes/*11.29*/.Assets.at("javascripts/jquery-migrate.min.js"))),format.raw/*11.76*/("""" type="text/javascript"></script>
 		<script src=""""),_display_(Seq[Any](/*12.18*/routes/*12.24*/.Application.javascriptRoutes)),format.raw/*12.53*/("""" type="text/javascript"></script>

 	    
 	    <link rel="stylesheet" media="screen" href=""""),_display_(Seq[Any](/*15.52*/routes/*15.58*/.Assets.at("stylesheets/bootstrap.min.css"))),format.raw/*15.101*/("""">
 	    
          	
        """),_display_(Seq[Any](/*18.10*/content)),format.raw/*18.17*/("""
    
</html>
"""))}
    }
    
    def render(title:String,content:Html): play.api.templates.HtmlFormat.Appendable = apply(title)(content)
    
    def f:((String) => (Html) => play.api.templates.HtmlFormat.Appendable) = (title) => (content) => apply(title)(content)
    
    def ref: this.type = this

}
                /*
                    -- GENERATED --
                    DATE: Thu Nov 21 18:58:44 CET 2013
                    SOURCE: /Users/macher1/git/webfml/FMLApp/app/views/mainFML.scala.html
                    HASH: fdf62a55f5c90f74e8b6e021e283feeab12b9c86
                    MATRIX: 563->1|687->31|775->84|801->89|900->153|914->159|969->193|1065->254|1079->260|1132->292|1193->317|1208->323|1269->362|1362->419|1377->425|1446->472|1534->524|1549->530|1600->559|1730->653|1745->659|1811->702|1878->733|1907->740
                    LINES: 19->1|22->1|28->7|28->7|29->8|29->8|29->8|30->9|30->9|30->9|31->10|31->10|31->10|32->11|32->11|32->11|33->12|33->12|33->12|36->15|36->15|36->15|39->18|39->18
                    -- GENERATED --
                */
            