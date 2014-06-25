/**
 *
 *
 *
 *
 *
 *
 *
 */
function FMLEditorCtrl($scope, $rootScope) {
	//when we click on the execute button we execute this function
	$scope.cmd = function () {
		console.log("here");
		var idToGet = editor.getSession().getValue();
		jsRoutes.controllers.WebFMLInterpreter.interpret(idToGet).ajax({
			success : function(data) {  
				$rootScope.$broadcast('variables', data)
                                $('#msgid').html('Error...<div class="alert alert-danger">' + data + '</div>') ; 
			},
		        error : function(data) {  
				$('#msgid').html('Error...<div class="alert alert-danger">' + data + '</div>') ; 
			},
		        beforeSend : function(event, jqxhr, settings) {
				$('#msgid').html('<img src="assets/images/ajax-loader.gif" />') ; 
			},
			complete : function(jqxhr, textstatus) {
				$('#msgid').html('') ;		   
			}
		});
	}
}