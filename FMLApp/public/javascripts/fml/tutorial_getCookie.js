var language="";
$(function(){
      //get the language in the cookie
      language = document.cookie;
      //split the languague because language=vm
      var res = language.split("=");
      console.log(res);
      //set to the language var the good language name
      language=res[1];
      console.log(language);
      //test if the language have the correct name
      if (language=="vm") {
            console.log(language);
            
            
      }
      
      if (language=="familiar") {
            console.log(language);
            
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
      document.getElementById("head").innerHTML=" - How to use " + language.toUpperCase(); 
});