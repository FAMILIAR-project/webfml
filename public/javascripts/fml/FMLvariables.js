function FMLVariableCtrl($scope, $rootScope) {
	$scope.varIDs = [];
	$scope.lastVar = '';
	
	$scope.displayVariable = function(id) {
		 jsRoutes.controllers.WebFMLInterpreter.variable(id).ajax({
				success : function(data) {  
					$scope.lastVar = data;
					$scope.$apply();
				},
			        error : function(data) {  
					$('#lastValueFML').html('Error...<div class="alert alert-danger">' + data + '</div>') ; 
				},
			        beforeSend : function(event, jqxhr, settings) {
				        $('#loader').html('<img src="assets/images/ajax-loader.gif" />') ; 
				},
			       complete : function(jqxhr, textstatus) {
				    $('#wait').html('') ;		   
			       }
		 }) ; 
	}
	
	$scope.edit = function(id) {
		//Pour le test remplacer le data par des valeurs en dur
		console.log("$scope.edit");
	}
	
	$scope.synthesize = function(id) {
		$rootScope.$broadcast('ksynthesis', 'ksynthesis --interactive ' + id)
	}
	
	$scope.$on('variables', function (event, data) {
		$scope.varIDs = data["varIDs"];
		$scope.lastVar = data["lastVar"];
		$scope.$apply();
	});
	
	
}
