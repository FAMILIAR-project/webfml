var language = "familiar";
$(function() {
        //alert("plop");
        //return the tutorial of the language
        jsRoutes.controllers.WebFMLInterpreter.getTutorialInMarkdown(language).ajax({
            //if works
            success : function(data){
               
                var k = (marked(data));
                var p = k.replace(/<pre>/g,"<pre class='line-numbers language-"+language+"'>");
                var c = p.replace(/<code>/g,"<code class='language-"+language+"'>");
                //put in the div the content
                document.getElementById('tutorial').innerHTML=c;
            },
            //if error
            error : function(data) {  
                $('tutorial').html('Error'+ data) ; 
            }
        });
        
        //alert("plop2");
    }                  
);