var language="";
$(function(){
      language = document.cookie;
      var res = language.split("=");
      console.log(res);
      language=res[1];
      console.log(language);
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
      document.getElementById("head").innerHTML=" - How to use " + language; 
});