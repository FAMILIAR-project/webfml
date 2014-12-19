function FMLConfigure($scope, $rootScope){
	
	$scope.$on('FMLConfigure', function (event, id) {
		
		$scope.displayTreePanel(id);
		
		try {
			console.log("FMLConfigure");
			jsRoutes.controllers.WebFMLInterpreter.configureVariable(id).ajax({
		
				success : function(data) {  
					$scope.buildTree(data)
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
			});
		} catch (e) {
			jqconsole.Write('ERROR: ' + e.message + '\n');
		}
	}) ;
	
	/**
	 * Display the configuration tree for this variable in a new panel above the console
	 * 
	 * @param tree The description of the feature model, in json
	 */
	$scope.displayTreePanel = function(idVar){
		
		//Créer,le nouvel onglet (la div HTML associée)
		//Passer le json à JSTree pour qu'il affiche le 
		

		$('#tabs-hoster .nav-tabs').append('<li><a href="#variable'+idVar+'" data-toggle="tab">Variable' +idVar + '</a></li>');
		
		$('#tabs-hoster .tab-content').append('<div class="tab-pane" id="variable'+idVar+'">Bonour les cop1</div>');
		//Passer le json à JSTree pour qu'il affiche le 
		
	}
	
	
	/**
	 * Build a good formated json tree and then call the displayTree function to display it
	 * 
	 * @param featureModel the feature Model description, as returned by the server
	 */
	$scope.buildTree = function(featureModel){
		
		console.log("buildTree");
		console.log(featureModel);
		//Algo de transformation du json en quelque chose de bouffable par JSTree
		
	}
	
	
}