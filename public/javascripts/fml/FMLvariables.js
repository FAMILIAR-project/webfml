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
	
	$scope.configure = function(id) {
		jsRoutes.controllers.WebFMLInterpreter.configure(id).ajax({
				success : function(data) {
					$scope.lastVar = data;
					$scope.showConf(data);
					$scope.$apply();
				},
				error : function(data) {
					$('#lastValueFML').html('Error...<div class="alert alert-danger">' + data + '</div>');
				},
				beforeSend : function(event, jqxhr, settings) {
					$('#loader').html('<img src="assets/images/ajax-loader.gif" />'); 	
				},
				complete : function(data, jqxhr, textstatus) {
					$('#wait').html('');	
				}
		});
	}
	
	$scope.showConf = function(data) {
			
		$('ul.nav.nav-tabs').append('<li><a href="#configure-tab" data-toggle="tab">'+data+'</a></li>');
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
