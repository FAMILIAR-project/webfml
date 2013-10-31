// text editor
// for executing a FML script and updating the UI
$(document).ready(function() {
	$('#webFmlCmd').click(function(e) {
	    var idToGet = editor.getSession().getValue();
	    jsRoutes.controllers.WebFMLInterpreter.interpret(idToGet).ajax({
		success : function(data) {  
			$('#msgid').html('<div>' + data + '</div>') ; 
		},
	        error : function(data) {  
			$('#msgid').html('Error...<div>' + data + '</div>') ; 
		},
	        beforeSend : function(event, jqxhr, settings) {
		        $('#msgid').html('<img id="wait" src="assets/images/ajax-loader.gif" />') ; 
		},
	       afterSend : function(event, jqxhr, settings) {
		   $('#wait').hide() ; 
	       }
	})
	;
	return false;
	   	    
	}); 

	$('#webFmlReset').click(function(e) {


	    jsRoutes.controllers.WebFMLInterpreter.reset().ajax({
		success : function(data) {  
		    $('#msgid').html('');
		},
	        error : function(data) {  
			$('#msgid').html('Impossible to reset...<div>' + data + '</div>') ; 
		},
	        beforeSend : function(event, jqxhr, settings) {
		        $('#msgid').html('<img id="wait" src="assets/images/ajax-loader.gif" />') ; 
		},
	       afterSend : function(event, jqxhr, settings) {
		   $('#wait').hide() ; 
	       }
	})
	;
	return false;
	   	    
	}); 
	  
});




