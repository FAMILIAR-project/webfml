
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
object demoSubstitution2 extends BaseScalaTemplate[play.api.templates.HtmlFormat.Appendable,Format[play.api.templates.HtmlFormat.Appendable]](play.api.templates.HtmlFormat) with play.api.templates.Template0[play.api.templates.HtmlFormat.Appendable] {

    /**/
    def apply():play.api.templates.HtmlFormat.Appendable = {
        _display_ {

Seq[Any](format.raw/*2.1*/("""<!DOCTYPE HTML>
<html>
  <head>
  <script type="text/javascript" src=""""),_display_(Seq[Any](/*5.40*/routes/*5.46*/.Application.javascriptRoutes)),format.raw/*5.75*/(""""></script>
    <style>
      body """),format.raw/*7.12*/("""{"""),format.raw/*7.13*/("""
        margin: 0px;
        padding: 0px;
      """),format.raw/*10.7*/("""}"""),format.raw/*10.8*/("""
      #buttons """),format.raw/*11.16*/("""{"""),format.raw/*11.17*/("""
        position: absolute;
        left: 10px;
        top: 0px;
      """),format.raw/*15.7*/("""}"""),format.raw/*15.8*/("""
      button """),format.raw/*16.14*/("""{"""),format.raw/*16.15*/("""
        margin-top: 10px;
        display: block;
      """),format.raw/*19.7*/("""}"""),format.raw/*19.8*/("""
    </style>
    <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
  </head>
  <body>
    <div id="container"></div>
    <div id="buttons">
      <button id="ftE">
       E
      </button>
      <button id="ftF">
        F
      </button>

      <button id="ftB">
        B
      </button>
     
       <button id="ftC">
        C
      </button>

       <button id="ftD">
        D
      </button>
 
    </div>
    <script src="http://d3lp1msu2r81bx.cloudfront.net/kjs/js/lib/kinetic-v4.5.4.min.js"></script>
    <script defer="defer">
        var stage = new Kinetic.Stage("""),format.raw/*48.39*/("""{"""),format.raw/*48.40*/("""
          container: 'container',
          width: 600,
          height: 200
        """),format.raw/*52.9*/("""}"""),format.raw/*52.10*/(""");
        var commonLayer = new Kinetic.Layer();
   
        var rectCommon = new Kinetic.Rect("""),format.raw/*55.43*/("""{"""),format.raw/*55.44*/("""
          x: stage.getWidth() / 4,
          y: stage.getHeight() / 4,
          width: 250,
          height: 100,
          fill: 'green',
          stroke: 'black',
          strokeWidth: 2,
          visible: true
        """),format.raw/*64.9*/("""}"""),format.raw/*64.10*/(""");

        var rect1 = new Kinetic.Rect("""),format.raw/*66.38*/("""{"""),format.raw/*66.39*/("""
          x: rectCommon.getPosition().x + rectCommon.getWidth(),
          y: rectCommon.getPosition().y,
          width: 50,
          height: 30,
          fill: 'red',
          stroke: 'black',
          strokeWidth: 2,
          visible: false
        """),format.raw/*75.9*/("""}"""),format.raw/*75.10*/(""");

       var circle1 =  new Kinetic.Circle("""),format.raw/*77.42*/("""{"""),format.raw/*77.43*/("""
        x: stage.getWidth() / 2,
        y: rectCommon.getPosition().y,
        radius: 20,
        fill: 'blue',
        stroke: 'black',
        strokeWidth: 2,
        visible: false
        """),format.raw/*85.9*/("""}"""),format.raw/*85.10*/(""");

     
    
        // add the shape to the layer
        commonLayer.add(rectCommon);

        commonLayer.add(rect1);
        commonLayer.add(circle1);

      

        // add the layer to the stage
        stage.add(commonLayer);

//  E feature
var bE = 0 ;
var elementE = document.getElementById('ftE');
elementE.addEventListener('click', function() """),format.raw/*103.47*/("""{"""),format.raw/*103.48*/("""

jsRoutes.controllers.WebFMLConfigurator.applySelection('E').ajax("""),format.raw/*105.66*/("""{"""),format.raw/*105.67*/("""
                  success : function(data) """),format.raw/*106.44*/("""{"""),format.raw/*106.45*/("""
                    updateUIJSon(data);
		          """),format.raw/*108.13*/("""}"""),format.raw/*108.14*/(""",
                  error : function(data) """),format.raw/*109.42*/("""{"""),format.raw/*109.43*/("""
                     alert('Error...' + data + '\n');
                     
                  """),format.raw/*112.19*/("""}"""),format.raw/*112.20*/("""
                  """),format.raw/*113.19*/("""}"""),format.raw/*113.20*/(""");
if (bE == 0) """),format.raw/*114.14*/("""{"""),format.raw/*114.15*/(""" 
	bE = 1 ; 
	this.style.color = 'green' ; 
	"""),format.raw/*117.2*/("""}"""),format.raw/*117.3*/("""
else """),format.raw/*118.6*/("""{"""),format.raw/*118.7*/(""" 
	if (bC == 1) """),format.raw/*119.15*/("""{"""),format.raw/*119.16*/("""
		alert("C requires E to be selected");
	"""),format.raw/*121.2*/("""}"""),format.raw/*121.3*/("""
	else """),format.raw/*122.7*/("""{"""),format.raw/*122.8*/("""
		bE = 0 ; 
		this.style.color = 'black' ; 
	"""),format.raw/*125.2*/("""}"""),format.raw/*125.3*/("""
"""),format.raw/*126.1*/("""}"""),format.raw/*126.2*/("""
updateUI() ;

"""),format.raw/*129.1*/("""}"""),format.raw/*129.2*/(""", false); 



//  F feature
var bF = 0 ;
var elementF = document.getElementById('ftF');
elementF.addEventListener('click', function() """),format.raw/*136.47*/("""{"""),format.raw/*136.48*/("""



if (bF == 0) """),format.raw/*140.14*/("""{"""),format.raw/*140.15*/(""" bF = 1 ; this.style.color = 'green' ; """),format.raw/*140.54*/("""}"""),format.raw/*140.55*/("""
else """),format.raw/*141.6*/("""{"""),format.raw/*141.7*/(""" bF = 0 ; this.style.color = 'black' ; """),format.raw/*141.46*/("""}"""),format.raw/*141.47*/("""
updateUI() ;

"""),format.raw/*144.1*/("""}"""),format.raw/*144.2*/(""", false); 



//  B feature
var bB = 0 ;
var elementB = document.getElementById('ftB');

//  C feature
var bC = 0 ;
var elementC = document.getElementById('ftC');
// elementC.disabled = true;

//  D feature
var bD = 0 ;
var elementD = document.getElementById('ftD');
// elementD.disabled = true;



var defaultFragmentBCD = '<div id="phBCD">Nothing</div>' ;
var fragmentB = '<div id="phB">Hey this is B !</div>' ;
var fragmentC = '<div id="phC">Hey this is C !</div>' ;
var fragmentD = '<div id="phD">Hey this is D !</div>' ; 
		
		
elementB.addEventListener('click', function() """),format.raw/*170.47*/("""{"""),format.raw/*170.48*/("""


if (bB == 0) """),format.raw/*173.14*/("""{"""),format.raw/*173.15*/(""" 
		bB = 1 ; 
		elementB.style.color = 'green' ; 
        elementC.disabled = false;
		elementD.disabled = false;

      
		bC = 0;
                elementC.style.color = 'black';
                       
		bD = 0;
		elementD.style.color = 'black';
		
"""),format.raw/*186.1*/("""}"""),format.raw/*186.2*/("""
else """),format.raw/*187.6*/("""{"""),format.raw/*187.7*/("""
                              
	   bB = 0 ; 

	   elementB.style.color = 'black' ;
	   if (bC == 1) """),format.raw/*192.18*/("""{"""),format.raw/*192.19*/("""
			bC = 0;
			elementC.style.color = 'black' ;
           """),format.raw/*195.12*/("""}"""),format.raw/*195.13*/("""
	   else """),format.raw/*196.10*/("""{"""),format.raw/*196.11*/("""
			bD = 0;
			elementD.style.color = 'black' ;
                        bB = 0;
				elementB.style.color = 'black';
"""),format.raw/*201.1*/("""}"""),format.raw/*201.2*/("""

				
	   elementC.disabled = true;
	   elementD.disabled = true;

"""),format.raw/*207.1*/("""}"""),format.raw/*207.2*/("""
updateUI() ;
"""),format.raw/*209.1*/("""}"""),format.raw/*209.2*/(""" , false);


elementC.addEventListener('click', function() """),format.raw/*212.47*/("""{"""),format.raw/*212.48*/("""


                if (bC == 0) """),format.raw/*215.30*/("""{"""),format.raw/*215.31*/("""


				bC = 1 ; 
				elementC.style.color = 'green' ; 
							
				// C -> E
				bE = 1;
				elementE.style.color = 'green' ; 
				
				bD = 0;
				elementD.style.color = 'black';
                                bB = 0;
				elementB.style.color = 'black';

					
		"""),format.raw/*231.3*/("""}"""),format.raw/*231.4*/("""
		else """),format.raw/*232.8*/("""{"""),format.raw/*232.9*/("""
                      
				bC = 0 ;
                               	elementC.style.color = 'black' ;
                               
				// E shouldn't be hidden since C->E but !C doesn't imply !E
		"""),format.raw/*238.3*/("""}"""),format.raw/*238.4*/("""
		updateUI() ;
		"""),format.raw/*240.3*/("""}"""),format.raw/*240.4*/(""", false);
		
elementD.addEventListener('click', function() """),format.raw/*242.47*/("""{"""),format.raw/*242.48*/("""

		if (bD == 0) """),format.raw/*244.16*/("""{"""),format.raw/*244.17*/("""

				bD = 1 ; 
				elementD.style.color = 'green' ; 
						
				bC = 0;
				elementC.style.color = 'black';
      	                        bB = 0;
				elementB.style.color = 'black';

   
		"""),format.raw/*255.3*/("""}"""),format.raw/*255.4*/("""
		else """),format.raw/*256.8*/("""{"""),format.raw/*256.9*/("""
                        bD = 0 ;
			elementD.style.color = 'black' ; 
			
		"""),format.raw/*260.3*/("""}"""),format.raw/*260.4*/("""
		updateUI() ;
		"""),format.raw/*262.3*/("""}"""),format.raw/*262.4*/(""" , false);


function updateUI() """),format.raw/*265.21*/("""{"""),format.raw/*265.22*/("""

// object substituion B, C, D, 

	if (bC == 0) """),format.raw/*269.15*/("""{"""),format.raw/*269.16*/("""
			$('#phC').remove();			
   		"""),format.raw/*271.6*/("""}"""),format.raw/*271.7*/("""
else """),format.raw/*272.6*/("""{"""),format.raw/*272.7*/(""" // bC == 1
$('#ph').html(fragmentC);
	
"""),format.raw/*275.1*/("""}"""),format.raw/*275.2*/("""
        if (bD == 0) """),format.raw/*276.22*/("""{"""),format.raw/*276.23*/("""
                        $('#phD').remove();
 		
		"""),format.raw/*279.3*/("""}"""),format.raw/*279.4*/("""
	else """),format.raw/*280.7*/("""{"""),format.raw/*280.8*/(""" // bD = 1
	        $('#ph').html(fragmentD) ; 
        """),format.raw/*282.9*/("""}"""),format.raw/*282.10*/("""


        if (bB == 1) """),format.raw/*285.22*/("""{"""),format.raw/*285.23*/(""" 
                $('#ph').html(fragmentB);
        """),format.raw/*287.9*/("""}"""),format.raw/*287.10*/("""
        else """),format.raw/*288.14*/("""{"""),format.raw/*288.15*/(""" // bB == 0
           $('#phB').remove();
        """),format.raw/*290.9*/("""}"""),format.raw/*290.10*/("""

// object existence E, F

        if (bE == 1) """),format.raw/*294.22*/("""{"""),format.raw/*294.23*/("""
	rect1.show();
       """),format.raw/*296.8*/("""}"""),format.raw/*296.9*/("""
else """),format.raw/*297.6*/("""{"""),format.raw/*297.7*/("""
	rect1.hide();
       """),format.raw/*299.8*/("""}"""),format.raw/*299.9*/("""

 if (bF == 1) """),format.raw/*301.15*/("""{"""),format.raw/*301.16*/("""
circle1.show() ;
"""),format.raw/*303.1*/("""}"""),format.raw/*303.2*/("""
else
circle1.hide() ; 


commonLayer.draw();
"""),format.raw/*309.1*/("""}"""),format.raw/*309.2*/("""


function updateUIJSon(jsData) """),format.raw/*312.31*/("""{"""),format.raw/*312.32*/("""


var sels = jsData ['selected'] ; 
for (i = 0 ; i < sels.length ; i++) """),format.raw/*316.37*/("""{"""),format.raw/*316.38*/("""
      // sels[i] 
      var el = document.getElementById('ft' + sels[i]) ;
      el.style.color = 'orange' ; 
"""),format.raw/*320.1*/("""}"""),format.raw/*320.2*/("""

var desels = jsData ['deselected'] ; 
for (j = 0 ; j < desels.length ; j++) """),format.raw/*323.39*/("""{"""),format.raw/*323.40*/("""
      // desels[j] 
      var el = document.getElementById('ft' + desels[j]) ;
      el.style.color = 'red' ;
"""),format.raw/*327.1*/("""}"""),format.raw/*327.2*/("""

"""),format.raw/*329.1*/("""}"""),format.raw/*329.2*/("""
      </script>

<div id="ph"> <!-- place holder -->
</div>

  </body>
</html>
"""))}
    }
    
    def render(): play.api.templates.HtmlFormat.Appendable = apply()
    
    def f:(() => play.api.templates.HtmlFormat.Appendable) = () => apply()
    
    def ref: this.type = this

}
                /*
                    -- GENERATED --
                    DATE: Wed Dec 04 17:07:44 CET 2013
                    SOURCE: /home/gbecan/git/webfml/FMLApp/app/views/demoSubstitution2.scala.html
                    HASH: 6bcaa7197f6dbe2326bf42a1105f9fd899a14ad9
                    MATRIX: 649->2|758->76|772->82|822->111|886->148|914->149|994->202|1022->203|1067->220|1096->221|1200->298|1228->299|1271->314|1300->315|1387->375|1415->376|2061->994|2090->995|2208->1086|2237->1087|2364->1186|2393->1187|2656->1423|2685->1424|2756->1467|2785->1468|3080->1736|3109->1737|3184->1784|3213->1785|3443->1988|3472->1989|3876->2364|3906->2365|4004->2434|4034->2435|4108->2480|4138->2481|4222->2536|4252->2537|4325->2581|4355->2582|4482->2680|4512->2681|4561->2701|4591->2702|4637->2719|4667->2720|4743->2768|4772->2769|4807->2776|4836->2777|4882->2794|4912->2795|4984->2839|5013->2840|5049->2848|5078->2849|5155->2898|5184->2899|5214->2901|5243->2902|5289->2920|5318->2921|5488->3062|5518->3063|5568->3084|5598->3085|5666->3124|5696->3125|5731->3132|5760->3133|5828->3172|5858->3173|5904->3191|5933->3192|6567->3797|6597->3798|6645->3817|6675->3818|6967->4082|6996->4083|7031->4090|7060->4091|7195->4197|7225->4198|7316->4260|7346->4261|7386->4272|7416->4273|7565->4394|7594->4395|7696->4469|7725->4470|7769->4486|7798->4487|7889->4549|7919->4550|7983->4585|8013->4586|8322->4867|8351->4868|8388->4877|8417->4878|8651->5084|8680->5085|8728->5105|8757->5106|8847->5167|8877->5168|8925->5187|8955->5188|9185->5390|9214->5391|9251->5400|9280->5401|9389->5482|9418->5483|9466->5503|9495->5504|9560->5540|9590->5541|9672->5594|9702->5595|9764->5629|9793->5630|9828->5637|9857->5638|9928->5681|9957->5682|10009->5705|10039->5706|10121->5760|10150->5761|10186->5769|10215->5770|10301->5828|10331->5829|10387->5856|10417->5857|10499->5911|10529->5912|10573->5927|10603->5928|10684->5981|10714->5982|10796->6035|10826->6036|10879->6061|10908->6062|10943->6069|10972->6070|11025->6095|11054->6096|11101->6114|11131->6115|11179->6135|11208->6136|11288->6188|11317->6189|11382->6225|11412->6226|11518->6303|11548->6304|11691->6419|11720->6420|11830->6501|11860->6502|12003->6617|12032->6618|12064->6622|12093->6623
                    LINES: 22->2|25->5|25->5|25->5|27->7|27->7|30->10|30->10|31->11|31->11|35->15|35->15|36->16|36->16|39->19|39->19|68->48|68->48|72->52|72->52|75->55|75->55|84->64|84->64|86->66|86->66|95->75|95->75|97->77|97->77|105->85|105->85|123->103|123->103|125->105|125->105|126->106|126->106|128->108|128->108|129->109|129->109|132->112|132->112|133->113|133->113|134->114|134->114|137->117|137->117|138->118|138->118|139->119|139->119|141->121|141->121|142->122|142->122|145->125|145->125|146->126|146->126|149->129|149->129|156->136|156->136|160->140|160->140|160->140|160->140|161->141|161->141|161->141|161->141|164->144|164->144|190->170|190->170|193->173|193->173|206->186|206->186|207->187|207->187|212->192|212->192|215->195|215->195|216->196|216->196|221->201|221->201|227->207|227->207|229->209|229->209|232->212|232->212|235->215|235->215|251->231|251->231|252->232|252->232|258->238|258->238|260->240|260->240|262->242|262->242|264->244|264->244|275->255|275->255|276->256|276->256|280->260|280->260|282->262|282->262|285->265|285->265|289->269|289->269|291->271|291->271|292->272|292->272|295->275|295->275|296->276|296->276|299->279|299->279|300->280|300->280|302->282|302->282|305->285|305->285|307->287|307->287|308->288|308->288|310->290|310->290|314->294|314->294|316->296|316->296|317->297|317->297|319->299|319->299|321->301|321->301|323->303|323->303|329->309|329->309|332->312|332->312|336->316|336->316|340->320|340->320|343->323|343->323|347->327|347->327|349->329|349->329
                    -- GENERATED --
                */
            