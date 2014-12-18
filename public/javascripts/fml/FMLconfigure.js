function FMLConfigure($scope, $rootScope){
	

	/**
	 * Display the configuration tree for this variable in a new panel above the console
	 * 
	 * @param tree The description of the feature model, in json
	 */
	$scope.displayTreeConfiguration(tree){
		
		//Créer,le nouvel onglet (la div HTML associée)
		//Passer le json à JSTree pour qu'il affiche le 
		
	}
	
	
	/**
	 * Build a good formated json tree and then call the displayTree function to display it
	 * 
	 * @param featureModel the feature Model description, as returned by the server
	 */
	$scope.buildTree(featureModel){
		
		
		//Algo de transformation du json en quelque chose de bouffable par JSTree
		
	}
	
	
}