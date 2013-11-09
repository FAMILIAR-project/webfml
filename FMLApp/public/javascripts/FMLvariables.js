function displayVariable(id) {

 jsRoutes.controllers.WebFMLInterpreter.variable(id).ajax({
		success : function(data) {  
			$('#lastValueFML').html('' + data + '') ; 
		},
	        error : function(data) {  
			$('#lastValueFML').html('Error...<div class="alert alert-danger">' + data + '</div>') ; 
		},
	        beforeSend : function(event, jqxhr, settings) {
		        $('#wait').html('<img id="wait" src="assets/images/ajax-loader.gif" />') ; 
		},
	       complete : function(jqxhr, textstatus) {
		    $('#wait').html('') ;		   
	       }
 }) ; 

}

