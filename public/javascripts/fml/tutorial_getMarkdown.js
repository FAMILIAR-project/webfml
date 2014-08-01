/**
 *Load the markdown files used for the 
 *tutorial
 */
$(function() {
        //return the introduction of the language
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
                //add a link in the menu to load the chapters
                var res = (marked(data));
                
                //regex ta have the name of the chapters
                var myRegex = new RegExp(/\w*.md/g);//[a-zA-Z]*[0-9]*.md
                //match the chapters
                var chapters = res.match(myRegex);
                
                //split res to have a line by line 
                var parts=res.split(/\n/);
                //will contain the result of the first for
                var b=[];
                var temp;
                //put the first line (title) of the file into b
                b[0]=parts[0];
                
                /*
                 *change the href to a onclick on every link of the menu
                 */
                for (var i=0;i<chapters.length;i++) {
                    //replace the word
                    //b[i+1]=parts[i+1].replace('<a href="'+chapters[i]+'"','<a onclick=getChap('+'"'+chapters[i]+'"'+','+'"'+language+'"'+');') + " " ;
                    temp=parts[i+1].replace('<a href="'+chapters[i]+'"','<button type="button" class="btn btn-primary" onclick=getChap('+'"'+chapters[i]+'"'+','+'"'+language+'"'+');') + " " ;
                    b[i+1] = temp.replace("</a>","</button>");
                }                
                var result="";
                /*
                 *delete the ',' of the tab
                 */
                for (var k =0; k<b.length;k++) {
                    result+=b[k]+" ";
                }
                //put the result into the menu div
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