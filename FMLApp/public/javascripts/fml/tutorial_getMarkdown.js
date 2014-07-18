//@TODO : change the language name
//load all the markdown files
var language = 'familiar';
$(function() {
        //return the tutorial of the language
        jsRoutes.controllers.WebFMLInterpreter.getTutorialInMarkdown(language).ajax({
            //if works
            success : function(data){
                //put in the div the content
                document.getElementById('tutorial').innerHTML= (marked(data));
                //add the highlight of the content
                Prism.highlightAll();
            },
            //if error
            error : function(data) {  
                $('tutorial').html('Error'+ data) ; 
            }
        });
        //get the menu of the tutorial
        jsRoutes.controllers.WebFMLInterpreter.getMenuInMarkdown(language).ajax({
            //if works
            success : function(data){
                //put in the div the content
                //add a link in the menu to load the chapters
                var res = (marked(data));
                
                //match the name of the chapters
                var myRegex = new RegExp(/\w*.md/g);//[a-zA-Z]*[0-9]*.md
                var chapters = res.match(myRegex);//w -> al characters + * (1..n)
                
                //split res to have line by line
                var parts=res.split(/\n/);
                console.log(parts);
                var b=[];
                b[0]=parts[0];
        
                for (var i=0;i<chapters.length;i++) {
                    console.log(chapters[i]);
                    b[i+1]=parts[i+1].replace('<a href="'+chapters[i]+'"','<a onclick=getChap('+'"'+chapters[i]+'"'+','+'"'+language+'"'+');');
                }
                //button type ="button" class="btn btn-info"
                console.log(b);
                var result="";
                for (var k =0; k<b.length;k++) {
                    result+=b[k]+" ";
                }
                document.getElementById('menu_tuto').innerHTML=result;
                //add the highlight of the content
                Prism.highlightAll();
            },
            //if error
            error : function(data) {  
                $('tutorial').html('Error'+ data) ; 
            }
        });
        //get the header of the tutorial
        jsRoutes.controllers.WebFMLInterpreter.getHeaderInMarkdown(language).ajax({
            //if works
            success : function(data){
                //put in the div the content
                document.getElementById('introduction').innerHTML= (marked(data));
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