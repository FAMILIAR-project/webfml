var language = 'familiar';
$(function() {
        //return the tutorial of the language
        jsRoutes.controllers.WebFMLInterpreter.getTutorialInMarkdown(language).ajax({
            //if works
            success : function(data){
                var k = (marked(data)).replace(/<pre>/g,"<pre class='line-numbers'>");//language-"+language+"'
                //var p = k.replace(/<pre>/g,"<pre class='line-numbers language-"+language+"'>");
                //var c = k.replace(/<code>/g,"<code class='language-"+language+"'>");
                //put in the div the content
                document.getElementById('tutorial').innerHTML=k;
                //add the highlight of the content
                Prism.highlightAll();
            },
            //if error
            error : function(data) {  
                $('tutorial').html('Error'+ data) ; 
            }
        });        
    }                  
);