function FMLEditorCtrl($scope, $rootScope) {
	
	$scope.cmd = function () {
	    var idToGet = editor.getSession().getValue();
	    jsRoutes.controllers.WebFMLInterpreter.interpret(idToGet).ajax({
			success : function(data) {
				$rootScope.$broadcast('variables', data)
				//$('#msgid').html('<div class="alert alert-success">' + data.lastVar + '</div>') ; 
			},
		        error : function(data) {  
				$('#msgid').html('Error...<div class="alert alert-danger">' + data.lastVar + '</div>') ; 
			},
		        beforeSend : function(event, jqxhr, settings) {
			        $('#msgid').html('<img src="assets/images/ajax-loader.gif" />') ; 
			},
		       complete : function(jqxhr, textstatus) {
			    $('#wait').html('') ;		   
		    }
	    });
	}
}