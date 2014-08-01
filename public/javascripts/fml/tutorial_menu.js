/**
 *Return the correct chapter
 *param : name : name of the chapter,
 *        language : the current language
 */
function getChap(name,language){
    //read all the file in the name repo
        jsRoutes.controllers.WebFMLInterpreter.getChapter(name,language).ajax({
            //if works
            success : function(data){
                //put in the div the content
                document.getElementById('tutorial').innerHTML= (marked(data));
            },
            //if error
            error : function(data) {  
                $('tutorial').html('Error'+ data) ; 
            }
        });
}