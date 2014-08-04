/**
*
*
*/
var language="";
$(function(){
      //get the language in the cookie
      language = document.cookie;
      //split the languague because language=vm
      var res = language.split("=");
      //set to the language var the good language name
      language=res[1];
      //test if the language have the correct name
      if (language=="vm") {
            //appeller le fichier javascript qui va bien (mode-vm.js)
            jsRoutes.controllers.WebFMLInterpreter.searchKeyword().ajax({
                  success : function(data){
                        console.log("sucess");
                  }
            });
            
      }
      /**
      *If the current language is Familiar
      */
      if (language=="familiar") {
            console.log(language);

            $.getScript('assets/javascripts/ace-builds-master/src-noconflict/mode-familiar.js',function(){
                  console.log("execute")
            });
      }
      /*var i;
      for(i = 0; i<res.length;i++){
            console.log(i);
            console.log(res.length);
            if (i==res.length) { 
                  if (res[i]=="language=familiar" || res[i]=="language=vm") {
                        var k = res[i].split("=");
                        console.log(k);
                        language=k[1];
                        console.log(language);
                  }
            }
            
      }*/
      /*
      *Call anothers scripts for the editor
      */
      /*$.getScript('assets/javascripts/fml/lib_editor.js');
      $.getScript('assets/javascripts/fml/autocompletion_options.js');*/
      /*
      *Put the correct name to the head of the webpage
      */
      document.getElementById("head").innerHTML=" - How to use " + language.toUpperCase(); 
});