/**
 *Function which send the code to the compiler
 *and displays the result.
 */
function FMLEditorCtrl($scope, $rootScope) {
	/*
	 *when we click on the button execute
	 */
	$scope.cmd = function () {
		/*
		* We get the code from the editor
		*/
	    var idToGet = editor.getSession().getValue();
	    jsRoutes.controllers.WebFMLInterpreter.interpret(idToGet).ajax({
			/*
			 *if the code is correctly compile
			 */
			success : function(data) {
				/*display the result*/
				$rootScope.$broadcast('variables', data)
				/*display nothing in this div*/
				$('#msgid').html('') ; 
			},
			/*
			*if error
			*/
		        error : function(data) {
				/*display nothing in this div*/
				$rootScope.$broadcast('variables','')
				/*display the result*/
				$('#msgid').html('Error...<div class="alert alert-danger">' + data.lastVar + '</div>') ; 
			},
		        beforeSend : function(event, jqxhr, settings) {
			        $('#wait').html('<img src="assets/images/ajax-loader.gif" />') ; 
			},
		       complete : function(jqxhr, textstatus) {
			    $('#wait').html('') ;		   
		    }
	    });
	}
}