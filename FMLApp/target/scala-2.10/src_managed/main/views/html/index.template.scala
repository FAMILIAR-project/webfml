
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
        
        <!-- KSYNTHESIS -->
        <script src=""""),_display_(Seq[Any](/*29.23*/routes/*29.29*/.Assets.at("javascripts/dagre-d3/d3.v3.min.js"))),format.raw/*29.76*/(""""></script>
		<script src=""""),_display_(Seq[Any](/*30.17*/routes/*30.23*/.Assets.at("javascripts/dagre-d3/dagre-d3.min.js"))),format.raw/*30.73*/(""""></script>
		<script src=""""),_display_(Seq[Any](/*31.17*/routes/*31.23*/.Assets.at("javascripts/ksynthesis.js"))),format.raw/*31.62*/(""""></script>
		<!-- end KSYNTHESIS -->

        <link rel="stylesheet" type="text/css" href=""""),_display_(Seq[Any](/*34.55*/routes/*34.61*/.Assets.at("stylesheets/main.css"))),format.raw/*34.95*/("""">
        <link rel="stylesheet" type="text/css" href=""""),_display_(Seq[Any](/*35.55*/routes/*35.61*/.Assets.at("stylesheets/console.css"))),format.raw/*35.98*/("""">
        <link rel="stylesheet" type="text/css" href=""""),_display_(Seq[Any](/*36.55*/routes/*36.61*/.Assets.at("stylesheets/fmpreview.css"))),format.raw/*36.100*/("""">

        </head>
        <body>
            <div class="navbar">
                <div class="navbar-inner">
                    <div class="container-fluid">
                        <a class="brand" href="#">Familiar</a>
                        <button id="toggle-console" class="btn btn-info"><i class="icon-pencil icon-white"></i> Toggle Console</button>
                        <div class="btn-group pull-right" ng-controller="FMLEditorCtrl">
                            <button class="btn btn-info" id="webFmlCmd" ng-click="cmd()">Execute FAMILIAR code</button>
                            <button class="btn btn-info" id="webFmlReset" ng-click="reset()">Reset variables' environment</button>
                            <button class="btn btn-info" id="webFmlSaveAs" ng-click="save()">Save as...</button>
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
                            <li><a href="#ksynthesis-tab" data-toggle="tab">KSynthesis</a></li>
                        </ul>
                        <div class="tab-content">
                            <div class="tab-pane active" id="editor">"""),_display_(Seq[Any](/*73.71*/message)),format.raw/*73.78*/("""</div>
                            <div class="tab-pane" id="ksynthesis-tab" ng-controller="KSynthesisCtrl">
                            
	                            <div class="btn-toolbar" style="text-align:right">
			                        <div class="btn-group">
			                            <a class="btn" ng-click="undo()"><i class="icon-step-backward"></i> Undo</a>
			                            <a class="btn" ng-click="redo()"><i class="icon-step-forward"></i> Redo</a>
			                            <a class="btn" ng-click="completeFM()">Complete FM</a>
			                        </div>
			                        <select id="heuristic-selector" ng-model="selectedHeuristic" ng-options="h for h in heuristics"></select>
		                        </div>
		                        
		                        <div id="ksynthesis">
		                        	<div>
		                        		<h3>Preview</h3>	
		                        		<div id="fmpreview">
		                        			<svg height="300"></svg>
		                        		</div>
		                    		</div>
		                        
		                        	<div class="row-fluid">
		                        		<div class="span4">
				                        	<h3>Ranking Lists</h3>
				                        	<!-- Ranking lists for synthesis-->
				                        	<ul>
				                        		<li ng-repeat="rankingList in rankingLists | filter:"""),format.raw/*98.83*/("""{"""),format.raw/*98.84*/("""parentInFM:null"""),format.raw/*98.99*/("""}"""),format.raw/*98.100*/("""" class="parentFt">
				                        		<div ng-click="rankingList.expanded=!rankingList.expanded">
					                        		<i class="icon-plus" ng-if="!rankingList.expanded"></i>
					                        		<i class="icon-minus" ng-if="rankingList.expanded"></i>
					                        		"""),format.raw/*102.32*/("""{"""),format.raw/*102.33*/("""{"""),format.raw/*102.34*/("""rankingList.feature"""),format.raw/*102.53*/("""}"""),format.raw/*102.54*/("""}"""),format.raw/*102.55*/("""
				                        		</div>
				                        		<div ng-if="rankingList.expanded">
				                        			<ul>
					                        			<li ng-repeat="parent in rankingList.parents" style="list-style: none">
					                        				<div class="btn-group">
						                        				<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"> 
						                        					"""),format.raw/*109.36*/("""{"""),format.raw/*109.37*/("""{"""),format.raw/*109.38*/("""parent"""),format.raw/*109.44*/("""}"""),format.raw/*109.45*/("""}"""),format.raw/*109.46*/(""" 
						                        					<span class="caret"></span>
						                        				</button>
						                        				<ul class="dropdown-menu" role="menu">
						                        					<li role="presentation">
						                        						<a role="menuitem" tabindex="-1" ng-click='setParent(rankingList.feature,parent)'>
						                        							Select this parent
						                        						</a>
						                        					</li>
						                        					<li role="presentation">
						                        						<a role="menuitem" tabindex="-1" ng-click='ignoreParent(rankingList.feature,parent)'>Ignore this parent</a>
						                        					</li>
						                        					<li role="presentation" class="divider">
						                        					</li>
						                        					<li role="presentation">
						                        						<a role="menuitem" tabindex="-1" href="#">Cancel</a>
						                        					</li>
						                        				</ul>
					                        				</div>
					                        			</li>		                        		
					                        		</ul>
				                        		</li>
				                        		<div/>
				                        	</ul>
				                        	
				                        	<hr/>
				                        	<!-- Ranking lists for refactoring -->
				                        	<ul>
			                        		<li ng-repeat="rankingList in rankingLists | filter:"""),format.raw/*137.82*/("""{"""),format.raw/*137.83*/("""parentInFM:'!!'"""),format.raw/*137.98*/("""}"""),format.raw/*137.99*/("""" class="parentFt">
			                        		<div ng-click="rankingList.expanded=!rankingList.expanded">
				                        		<i class="icon-plus" ng-if="!rankingList.expanded"></i>
				                        		<i class="icon-minus" ng-if="rankingList.expanded"></i>
				                        		"""),format.raw/*141.31*/("""{"""),format.raw/*141.32*/("""{"""),format.raw/*141.33*/("""rankingList.feature"""),format.raw/*141.52*/("""}"""),format.raw/*141.53*/("""}"""),format.raw/*141.54*/("""
			                        		</div>
			                        		<div ng-if="rankingList.expanded">
			                        			<ul>
				                        			<li ng-repeat="parent in rankingList.originalParents" style="list-style: none">
				                        				<div class="btn-group">
				                        					<button ng-if="rankingList.parentInFM != parent" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"> 
					                        					"""),format.raw/*148.35*/("""{"""),format.raw/*148.36*/("""{"""),format.raw/*148.37*/("""parent"""),format.raw/*148.43*/("""}"""),format.raw/*148.44*/("""}"""),format.raw/*148.45*/(""" 
					                        					<span class="caret"></span>
					                        				</button>
					                        				
					                        				<button ng-if="rankingList.parentInFM == parent" type="button" class="btn btn-info dropdown-toggle" data-toggle="dropdown"> 
				                        						"""),format.raw/*153.35*/("""{"""),format.raw/*153.36*/("""{"""),format.raw/*153.37*/("""parent"""),format.raw/*153.43*/("""}"""),format.raw/*153.44*/("""}"""),format.raw/*153.45*/(""" 
				                        						<span class="caret"></span>
				                        					</button>
					                        				
					                        				<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
					                        					<li role="presentation">
					                        						<a role="menuitem" tabindex="-1" ng-click='setParent(rankingList.feature,parent)'>
					                        							Select this parent
					                        						</a>
					                        					</li>
					                        					<li role="presentation" class="divider">
					                        					</li>
					                        					<li role="presentation">
					                        						<a role="menuitem" tabindex="-1" href="#">Cancel</a>
					                        					</li>
					                        				</ul>
				                        				</div>
				                        			</li>		                        		
				                        		</ul>
			                        		</li>
			                        		<div/>
			                        	</ul>
				                        </div>
			                        
				                        <div class="span4">
				                        	<h3>Clusters</h3>
				                        	<ul>
			                        			<li ng-repeat="cluster in clusters" style="list-style: none">
		
			                        				 <div ng-if="cluster == selectedCluster">
			                        				 	<select class="span6" ng-model="clusterSelectedParent" ng-options="p for p in clusterPossibleParents"></select>
			                        				 	<button type="button" class="btn btn-success" ng-disabled='clusterSelectedParent==""' ng-click="setClusterParent(clusterSelectedFeatures,clusterSelectedParent)">
			                        				 	  <i class="icon-ok icon-white"></i>
			                        				 	</button>
			                        				 	<button type="button" class="btn btn-danger" ng-click="selectCluster([])">
			                        				 	  <i class="icon-remove icon-white"></i>
			                        				 	</button>
					                    				<ul>
						                        			<li ng-repeat="feature in cluster">
						                        				<input 
						                        					type="checkbox"  
						                        					value=""""),format.raw/*194.43*/("""{"""),format.raw/*194.44*/("""{"""),format.raw/*194.45*/("""feature"""),format.raw/*194.52*/("""}"""),format.raw/*194.53*/("""}"""),format.raw/*194.54*/(""""
						                        					ng-checked="clusterSelectedFeatures.indexOf(feature) > -1"
						                        					ng-click="selectClusterFeature(feature)"
						                        				>
						                        				"""),format.raw/*198.35*/("""{"""),format.raw/*198.36*/("""{"""),format.raw/*198.37*/("""feature"""),format.raw/*198.44*/("""}"""),format.raw/*198.45*/("""}"""),format.raw/*198.46*/("""
						                        			</li>
					                        			</ul>
				                    					
			                        			    </div>
			                        			    
			                        			    <div ng-if="cluster != selectedCluster" ng-click="selectCluster(cluster)">
			                        			    	"""),format.raw/*205.36*/("""{"""),format.raw/*205.37*/("""{"""),format.raw/*205.38*/("""cluster"""),format.raw/*205.45*/("""}"""),format.raw/*205.46*/("""}"""),format.raw/*205.47*/("""
			                        			    	<ul>
						                        			<li ng-repeat="feature in cluster">
						                        				"""),format.raw/*208.35*/("""{"""),format.raw/*208.36*/("""{"""),format.raw/*208.37*/("""feature"""),format.raw/*208.44*/("""}"""),format.raw/*208.45*/("""}"""),format.raw/*208.46*/("""
						                        			</li>
					                        			</ul>
			                        			    </div>
			                    					
			                        			</li>
			                        		</ul>
			                        	</div>
			                        	<div class="span4">
				                        	<h3>Cliques</h3>
				                        	<ul>
			                        			<li ng-repeat="clique in cliques" style="list-style: none">
				                       				 <div ng-if="clique == selectedCluster">
					                 				 	<select class="span6" ng-model="clusterSelectedParent" ng-options="p for p in clusterPossibleParents"></select>
					                 				 	<button type="button" class="btn btn-success" ng-disabled='clusterSelectedParent==""' ng-click="setClusterParent(clusterSelectedFeatures,clusterSelectedParent)">
					                 				 	  <i class="icon-ok icon-white"></i>
					                 				 	</button>
					                 				 	<button type="button" class="btn btn-danger" ng-click="selectCluster([])">
					                 				 	  <i class="icon-remove icon-white"></i>
					                 				 	</button>
					                    				<ul>
						                        			<li ng-repeat="feature in clique">
						                        				<input 
						                        					type="checkbox"  
						                        					value=""""),format.raw/*232.43*/("""{"""),format.raw/*232.44*/("""{"""),format.raw/*232.45*/("""feature"""),format.raw/*232.52*/("""}"""),format.raw/*232.53*/("""}"""),format.raw/*232.54*/(""""
						                        					ng-checked="clusterSelectedFeatures.indexOf(feature) > -1"
						                        					ng-click="selectClusterFeature(feature)"
						                        				>
						                        				"""),format.raw/*236.35*/("""{"""),format.raw/*236.36*/("""{"""),format.raw/*236.37*/("""feature"""),format.raw/*236.44*/("""}"""),format.raw/*236.45*/("""}"""),format.raw/*236.46*/("""
						                        			</li>
					                        			</ul>
				                 					
					                        		</div>
				                 			    
					                 			    <div ng-if="clique != selectedCluster" ng-click="selectCluster(clique)">
					                 			    	"""),format.raw/*243.31*/("""{"""),format.raw/*243.32*/("""{"""),format.raw/*243.33*/("""clique"""),format.raw/*243.39*/("""}"""),format.raw/*243.40*/("""}"""),format.raw/*243.41*/("""
					                 			    	<ul>
							                        			<li ng-repeat="feature in clique">
							                        				"""),format.raw/*246.36*/("""{"""),format.raw/*246.37*/("""{"""),format.raw/*246.38*/("""feature"""),format.raw/*246.45*/("""}"""),format.raw/*246.46*/("""}"""),format.raw/*246.47*/("""
							                        			</li>
						                        			</ul>
					                 			    </div>
			                        			</li>
			                        		</ul>
			                        	</div>
	                        		</div>
                                </div>
                                
                            </div>
                        </div>
                    </div>

                    <div class="span3 scrollable" ng-controller="FMLVariableCtrl">
                    
                        <div id="msgid"></div>
                        <div>
	                        <p>
	                        	<ul class="unstyled">
	                        		<li ng-repeat="varID in varIDs">
		                        		<div class="dropdown">
			    	                        <button type="button" class="btn dropdown-toggle sr-only" data-toggle="dropdown">
			    	                          """),format.raw/*269.35*/("""{"""),format.raw/*269.36*/("""{"""),format.raw/*269.37*/("""varID"""),format.raw/*269.42*/("""}"""),format.raw/*269.43*/("""}"""),format.raw/*269.44*/("""
			    	                          <span class="caret"></span>
			    	                        </button>
			    	                        <ul class="dropdown-menu" role="menu">
			    	                          <li role="presentation"><a role="menuitem" tabindex="-1" ng-click="displayVariable(varID)">Display</a></li>
			    	                          <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Edit</a></li>
			    	                          <li role="presentation"><a role="menuitem" tabindex="-1" ng-click="synthesize(varID)">Synthesize</a></li>
			    	                          <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Remove</a></li>
			    	                        </ul>
		    	                        </div>
	                        		</li>
	                        	</ul>
	                        	<p ng-if="varIDs" id="lastValueFML" class="alert alert-success">
	                        		"""),format.raw/*282.28*/("""{"""),format.raw/*282.29*/("""{"""),format.raw/*282.30*/("""lastVar"""),format.raw/*282.37*/("""}"""),format.raw/*282.38*/("""}"""),format.raw/*282.39*/("""
	                        	</p>
	                        </p>
	                   </div>
                    </div>
                </div>
            </div>

            <div id="console" class="hide in" ng-controller="FMLConsoleCtrl"></div>

            <script src=""""),_display_(Seq[Any](/*292.27*/routes/*292.33*/.Assets.at("javascripts/bootstrap.js"))),format.raw/*292.71*/("""" type="text/javascript"></script>
            <script>
                var editor;
                window.onload = function() """),format.raw/*295.44*/("""{"""),format.raw/*295.45*/("""
                    editor = ace.edit("editor");
                    editor.setTheme("ace/theme/monokai");

                    var JavaScriptMode = ace.require("ace/mode/javascript").Mode;
                    editor.getSession().setMode(new JavaScriptMode());
                """),format.raw/*301.17*/("""}"""),format.raw/*301.18*/(""";
            </script>
            <script>
                $(function () """),format.raw/*304.31*/("""{"""),format.raw/*304.32*/("""
                    $('#main-container').css('height', ($('body' ).outerHeight()-80)+'px');
                """),format.raw/*306.17*/("""}"""),format.raw/*306.18*/(""");

                $('#toggle-console').click(function () """),format.raw/*308.56*/("""{"""),format.raw/*308.57*/("""
                    if ($('#console').hasClass('hide')) """),format.raw/*309.57*/("""{"""),format.raw/*309.58*/("""
                        $('#console').removeClass('hide');
                    """),format.raw/*311.21*/("""}"""),format.raw/*311.22*/(""" else """),format.raw/*311.28*/("""{"""),format.raw/*311.29*/("""
                        $('#console').addClass('hide');
                    """),format.raw/*313.21*/("""}"""),format.raw/*313.22*/("""
                """),format.raw/*314.17*/("""}"""),format.raw/*314.18*/(""");
            </script>
            
            
        </body>
    """)))})),format.raw/*319.6*/("""


"""))}
    }
    
    def render(message:String): play.api.templates.HtmlFormat.Appendable = apply(message)
    
    def f:((String) => play.api.templates.HtmlFormat.Appendable) = (message) => apply(message)
    
    def ref: this.type = this

}
                /*
                    -- GENERATED --
                    DATE: Wed Dec 04 17:07:44 CET 2013
                    SOURCE: /home/gbecan/git/webfml/FMLApp/app/views/index.scala.html
                    HASH: 8e14ac5aa80fcb8f9bf90f2821a59325027cb260
                    MATRIX: 556->1|667->18|708->25|747->56|786->58|891->128|905->134|967->175|1061->234|1075->240|1134->278|1277->385|1292->391|1372->449|1481->522|1496->528|1586->596|1695->669|1710->675|1802->745|2037->944|2052->950|2108->984|2169->1009|2184->1015|2259->1068|2329->1102|2344->1108|2405->1147|2774->1480|2789->1486|2858->1533|2922->1561|2937->1567|3009->1617|3073->1645|3088->1651|3149->1690|3278->1783|3293->1789|3349->1823|3442->1880|3457->1886|3516->1923|3609->1980|3624->1986|3686->2025|5861->4164|5890->4171|7370->5623|7399->5624|7442->5639|7472->5640|7816->5955|7846->5956|7876->5957|7924->5976|7954->5977|7984->5978|8469->6434|8499->6435|8529->6436|8564->6442|8594->6443|8624->6444|10243->8034|10273->8035|10317->8050|10347->8051|10687->8362|10717->8363|10747->8364|10795->8383|10825->8384|10855->8385|11382->8883|11412->8884|11442->8885|11477->8891|11507->8892|11537->8893|11899->9226|11929->9227|11959->9228|11994->9234|12024->9235|12054->9236|14504->11657|14534->11658|14564->11659|14600->11666|14630->11667|14660->11668|14931->11910|14961->11911|14991->11912|15027->11919|15057->11920|15087->11921|15444->12249|15474->12250|15504->12251|15540->12258|15570->12259|15600->12260|15773->12404|15803->12405|15833->12406|15869->12413|15899->12414|15929->12415|17384->13841|17414->13842|17444->13843|17480->13850|17510->13851|17540->13852|17811->14094|17841->14095|17871->14096|17907->14103|17937->14104|17967->14105|18300->14409|18330->14410|18360->14411|18395->14417|18425->14418|18455->14419|18624->14559|18654->14560|18684->14561|18720->14568|18750->14569|18780->14570|19748->15509|19778->15510|19808->15511|19842->15516|19872->15517|19902->15518|20878->16465|20908->16466|20938->16467|20974->16474|21004->16475|21034->16476|21341->16746|21357->16752|21418->16790|21574->16917|21604->16918|21911->17196|21941->17197|22045->17272|22075->17273|22213->17382|22243->17383|22331->17442|22361->17443|22447->17500|22477->17501|22586->17581|22616->17582|22651->17588|22681->17589|22787->17666|22817->17667|22863->17684|22893->17685|22997->17757
                    LINES: 19->1|22->1|24->3|24->3|24->3|27->6|27->6|27->6|30->9|30->9|30->9|32->11|32->11|32->11|33->12|33->12|33->12|34->13|34->13|34->13|39->18|39->18|39->18|40->19|40->19|40->19|41->20|41->20|41->20|50->29|50->29|50->29|51->30|51->30|51->30|52->31|52->31|52->31|55->34|55->34|55->34|56->35|56->35|56->35|57->36|57->36|57->36|94->73|94->73|119->98|119->98|119->98|119->98|123->102|123->102|123->102|123->102|123->102|123->102|130->109|130->109|130->109|130->109|130->109|130->109|158->137|158->137|158->137|158->137|162->141|162->141|162->141|162->141|162->141|162->141|169->148|169->148|169->148|169->148|169->148|169->148|174->153|174->153|174->153|174->153|174->153|174->153|215->194|215->194|215->194|215->194|215->194|215->194|219->198|219->198|219->198|219->198|219->198|219->198|226->205|226->205|226->205|226->205|226->205|226->205|229->208|229->208|229->208|229->208|229->208|229->208|253->232|253->232|253->232|253->232|253->232|253->232|257->236|257->236|257->236|257->236|257->236|257->236|264->243|264->243|264->243|264->243|264->243|264->243|267->246|267->246|267->246|267->246|267->246|267->246|290->269|290->269|290->269|290->269|290->269|290->269|303->282|303->282|303->282|303->282|303->282|303->282|313->292|313->292|313->292|316->295|316->295|322->301|322->301|325->304|325->304|327->306|327->306|329->308|329->308|330->309|330->309|332->311|332->311|332->311|332->311|334->313|334->313|335->314|335->314|340->319
                    -- GENERATED --
                */
            