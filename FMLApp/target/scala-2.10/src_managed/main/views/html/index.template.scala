
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

    """),_display_(Seq[Any](/*3.6*/mainFML("Welcome to FAMILIAR!")/*3.37*/ {_display_(Seq[Any](format.raw/*3.39*/("""

        <!-- facilities for FML variables -->
        <script src=""""),_display_(Seq[Any](/*6.23*/routes/*6.29*/.Assets.at("javascripts/FMLvariables.js"))),format.raw/*6.70*/(""""></script>

        <!-- EDITOR -->
        <script src=""""),_display_(Seq[Any](/*9.23*/routes/*9.29*/.Assets.at("javascripts/FMLeditor.js"))),format.raw/*9.67*/("""" type="text/javascript" charset="utf-8"></script>
        <!-- begin ACE editor -->
        <script src=""""),_display_(Seq[Any](/*11.23*/routes/*11.29*/.Assets.at("javascripts/ace-builds/src-noconflict/ace.js"))),format.raw/*11.87*/("""" type="text/javascript" charset="utf-8"></script>
        <script src=""""),_display_(Seq[Any](/*12.23*/routes/*12.29*/.Assets.at("javascripts/ace-builds/src-noconflict/theme-monokai.js"))),format.raw/*12.97*/("""" type="text/javascript" charset="utf-8"></script>
        <script src=""""),_display_(Seq[Any](/*13.23*/routes/*13.29*/.Assets.at("javascripts/ace-builds/src-noconflict/mode-javascript.js"))),format.raw/*13.99*/("""" type="text/javascript" charset="utf-8"></script>
        <!-- end ACE editor (style) -->
        <!-- end EDITOR -->

        <!-- CONSOLE -->
        <link rel="stylesheet" type="text/css" href=""""),_display_(Seq[Any](/*18.55*/routes/*18.61*/.Assets.at("stylesheets/ansi.css"))),format.raw/*18.95*/("""">
        <script src=""""),_display_(Seq[Any](/*19.23*/routes/*19.29*/.Assets.at("javascripts/jq-console/jqconsole.min.js"))),format.raw/*19.82*/(""""></script>
        <script src=""""),_display_(Seq[Any](/*20.23*/routes/*20.29*/.Assets.at("javascripts/FMLconsole.js"))),format.raw/*20.68*/(""""></script>
        <!-- end CONSOLE -->

        <!-- WORKSPACE -->
        <script src="http://cdn.alloyui.com/2.0.0/aui/aui-min.js"></script>
        <link href="http://cdn.alloyui.com/2.0.0/aui-css/css/bootstrap.min.css" rel="stylesheet">
        <!-- end WORKSPACE -->

        <link rel="stylesheet" type="text/css" href=""""),_display_(Seq[Any](/*28.55*/routes/*28.61*/.Assets.at("stylesheets/main.css"))),format.raw/*28.95*/("""">
        <link rel="stylesheet" type="text/css" href=""""),_display_(Seq[Any](/*29.55*/routes/*29.61*/.Assets.at("stylesheets/console.css"))),format.raw/*29.98*/("""">

        <body>
            <div class="navbar">
                <div class="navbar-inner">
                    <div class="container-fluid">
                        <a class="brand" href="#">Familiar</a>
                        <button id="toggle-console" class="btn btn-info"><i class="icon-pencil icon-white"></i> Toggle Console</button>
                        <div class="btn-group pull-right">
                            <button class="btn btn-info" id="webFmlCmd">Execute FAMILIAR code</button>
                            <button class="btn btn-info" id="webFmlReset">Reset variables' environment</button>
                            <button class="btn btn-info" id="webFmlSaveAs">Save as...</button>
                        </div>
                        <form class="navbar-form pull-right" style="margin-right: 5px">
                            <input type="text" class="form-control" id="fileInputName" placeholder="Enter filename...">
                        </form>
                    </div>
                </div>
            </div>

            <div class="container-fluid" id="main-container">
                <div class="row-fluid">
                    <div class="span3 scrollable" id="workspace">
                        <div class="pull-right" style="margin-right: 5px">
                            <button class="btn btn-small" onclick="updateWorkspace();"><i class="icon-refresh"></i></button>
                            <div id="wait"></div>
                        </div>
                        <div id="myTreeView"></div>
                    </div>

                    <div class="span6" id="tabs-hoster">
                        <ul class="nav nav-tabs">
                            <li class="active"><a href="#editor" data-toggle="tab">FML Editor</a></li>
                            <li><a href="#ksynthesis" data-toggle="tab">KSynthesis</a></li>
                        </ul>
                        <div class="tab-content">
                            <div class="tab-pane active" id="editor">"""),_display_(Seq[Any](/*65.71*/message)),format.raw/*65.78*/("""</div>
                            <div class="tab-pane" id="ksynthesis"></div>
                        </div>
                    </div>

                    <div class="span3 scrollable">
                        <div id="msgid"></div>
                    </div>
                </div>
            </div>

            <div id="console" class="hide in"></div>

            <script src=""""),_display_(Seq[Any](/*78.27*/routes/*78.33*/.Assets.at("javascripts/bootstrap.js"))),format.raw/*78.71*/("""" type="text/javascript"></script>
            <script>
                var editor;
                window.onload = function() """),format.raw/*81.44*/("""{"""),format.raw/*81.45*/("""
                    editor = ace.edit("editor");
                    editor.setTheme("ace/theme/monokai");

                    var JavaScriptMode = ace.require("ace/mode/javascript").Mode;
                    editor.getSession().setMode(new JavaScriptMode());
                """),format.raw/*87.17*/("""}"""),format.raw/*87.18*/(""";
            </script>
            <script>
                $(function () """),format.raw/*90.31*/("""{"""),format.raw/*90.32*/("""
                    $('#main-container').css('height', ($('body' ).outerHeight()-80)+'px');
                """),format.raw/*92.17*/("""}"""),format.raw/*92.18*/(""");

                $('#toggle-console').click(function () """),format.raw/*94.56*/("""{"""),format.raw/*94.57*/("""
                    if ($('#console').hasClass('hide')) """),format.raw/*95.57*/("""{"""),format.raw/*95.58*/("""
                        $('#console').removeClass('hide');
                    """),format.raw/*97.21*/("""}"""),format.raw/*97.22*/(""" else """),format.raw/*97.28*/("""{"""),format.raw/*97.29*/("""
                        $('#console').addClass('hide');
                    """),format.raw/*99.21*/("""}"""),format.raw/*99.22*/("""
                """),format.raw/*100.17*/("""}"""),format.raw/*100.18*/(""");
            </script>
        </body>
    """)))})),format.raw/*103.6*/("""


"""))}
    }
    
    def render(message:String): play.api.templates.HtmlFormat.Appendable = apply(message)
    
    def f:((String) => play.api.templates.HtmlFormat.Appendable) = (message) => apply(message)
    
    def ref: this.type = this

}
                /*
                    -- GENERATED --
                    DATE: Thu Nov 21 18:58:44 CET 2013
                    SOURCE: /Users/macher1/git/webfml/FMLApp/app/views/index.scala.html
                    HASH: 2fe969a250a3639982c979a599bc303c0d120e70
                    MATRIX: 556->1|667->18|708->25|747->56|786->58|891->128|905->134|967->175|1061->234|1075->240|1134->278|1277->385|1292->391|1372->449|1481->522|1496->528|1586->596|1695->669|1710->675|1802->745|2037->944|2052->950|2108->984|2169->1009|2184->1015|2259->1068|2329->1102|2344->1108|2405->1147|2770->1476|2785->1482|2841->1516|2934->1573|2949->1579|3008->1616|5079->3651|5108->3658|5531->4045|5546->4051|5606->4089|5761->4216|5790->4217|6096->4495|6125->4496|6228->4571|6257->4572|6394->4681|6423->4682|6510->4741|6539->4742|6624->4799|6653->4800|6761->4880|6790->4881|6824->4887|6853->4888|6958->4965|6987->4966|7033->4983|7063->4984|7141->5030
                    LINES: 19->1|22->1|24->3|24->3|24->3|27->6|27->6|27->6|30->9|30->9|30->9|32->11|32->11|32->11|33->12|33->12|33->12|34->13|34->13|34->13|39->18|39->18|39->18|40->19|40->19|40->19|41->20|41->20|41->20|49->28|49->28|49->28|50->29|50->29|50->29|86->65|86->65|99->78|99->78|99->78|102->81|102->81|108->87|108->87|111->90|111->90|113->92|113->92|115->94|115->94|116->95|116->95|118->97|118->97|118->97|118->97|120->99|120->99|121->100|121->100|124->103
                    -- GENERATED --
                */
            