function FMLConfigure($scope, $rootScope){
	
	$scope.configureVarId = "";
	$scope.treeView = null;
	$scope.treeRootNode = null;
	
	$scope.$on('FMLConfigure', function (event, id) {
		
		$scope.configureVarId = id;
		
		$scope.displayTreePanel();
		
		try {
			console.log("FMLConfigure");
			jsRoutes.controllers.WebFMLInterpreter.configureVariable(id).ajax({
		
				success : function(data) {  
					$scope.buildTree(JSON.parse(data));
					
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
	$scope.displayTreePanel = function(){
		
		//Créer,le nouvel onglet (la div HTML associée)
		//Passer le json à JSTree pour qu'il affiche le 
		var fenetre = $("#variable" +$scope.configureVarId) ;		
		
		if(fenetre.length == 0){
			$('#tabs-hoster .nav-tabs').append('<li style="position:relative;" id="tabconfigure'+$scope.configureVarId+'"><a href="#variable'+$scope.configureVarId+'" data-toggle="tab">Variable' +$scope.configureVarId + '</a><img style="position:absolute;top:0;right:0;" id="close'+$scope.configureVarId+'" src="http://www.onepmo.com/img/close-icon.gif"/></li>');
		
			$('#tabs-hoster .tab-content').append('<div class="tab-pane" id="variable'+$scope.configureVarId+'"><b>Configurateur du FeatureModel '+$scope.configureVarId+'</b></div>');
		}
		
		$("#close"+$scope.configureVarId).click(function(){
			
			$("#tabconfigure"+$scope.configureVarId).remove();
			$("#variable"+$scope.configureVarId).remove();
			
		});
		
	}
	
	
	/**
	 * Build a good formated json tree and then call the displayTree function to display it
	 * 
	 * @param featureModel the feature Model description, as returned by the server
	 */
	$scope.buildTree = function(featureModel){
		
		console.log(featureModel);
		
		var tree = {};
		var rootNode = featureModel.children[0];
		
		tree.id = rootNode.id;
		tree.type = "TreeView";
		tree.label = rootNode.id;
		tree.expanded = true;
		tree.children = [];
		
		for(var i = 0; i < rootNode.children.length; i++)
			tree.children[i] = buildChild(rootNode.children[i]);
		
		console.log(tree);
		
		$("#variable"+$scope.configureVarId).empty();
		
		//Passer le json à JSTree pour qu'il affiche le 
		YUI().use(
				'aui-tree-view','node',function(Y) {
					
					treeView = new Y.TreeViewDD(
							{
								boundingBox : '#variable'+$scope.configureVarId,
								children : [tree]
							});
					
					treeView.render();
		});
	}
	
	function buildChild(childDescription)
	{
		var node = {};
		
		node.id = childDescription.id;
		node.label = childDescription.id;
		
		if(childDescription.optionnal==true)
			node.type="check";
		
		switch(childDescription.relation)
		{
			case 16 :
				node.type = "radio";
				break;
			case 8 :
				node.type = "check";
				break;
		}
		
		node.children = [];
		
		for(var i = 0; i < childDescription.children.length; i++)
			node.children[i] = buildChild(childDescription.children[i]);
		
		return node;
	}
	
	 
}