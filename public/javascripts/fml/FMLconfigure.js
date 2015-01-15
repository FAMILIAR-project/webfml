function FMLConfigure($scope, $rootScope){
	
	$scope.configureVarId = "";
	
	/**
	 * Function called on click on Configure button in the IDE
	 * Building the tree for the selected FeatureVariable 
	 */
	$scope.$on('FMLConfigure', function (event, id) {
		
		$scope.configureVarId = id;
		
		$scope.displayTreePanel();
		
		try {
			console.log("FMLConfigure");
			jsRoutes.controllers.WebFMLInterpreter.configureVariable(id).ajax({
		
				success : function(data) {  
					//Parce received data into JSon, then building the tree with data in JSon format
					$scope.buildTree(JSON.parse(data));
				},
				error : function(data) {  
					alert("yolo");
					$('#lastValueFML').html('Error...<div class="alert alert-danger">' + data + '</div>') ; 
				},
				beforeSend : function(event, jqxhr, settings) {
					$('#variable'+$scope.configureVarId).html('<img src="../assets/images/ajax-loader.gif" />') ; 
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
		
		var window = $("#variable" +$scope.configureVarId) ;		
		
		if(window.length == 0){
			$('#tabs-hoster .nav-tabs').append('<li style="position:relative;" id="tabconfigure'+$scope.configureVarId+'"><a href="#tabvariable'+$scope.configureVarId+'" data-toggle="tab">Variable ' +$scope.configureVarId + '</a><img style="position:absolute;top:0;right:0;cursor:pointer;" id="close'+$scope.configureVarId+'" src="../assets/images/close-icon.gif" onclick="$(\'#tabconfigure'+$scope.configureVarId+'\').remove();$(\'#variable'+$scope.configureVarId+'\').remove();"/></li>');
		
			$('#tabs-hoster .tab-content').append('<div class="tab-pane" id="tabvariable'+$scope.configureVarId+'"><b>Configurateur du FeatureModel '+$scope.configureVarId+'</b><div id="variable'+$scope.configureVarId+'"></div></div>');
		}
	}
	
	
	/**
	 * Build a good formated json tree and then call the displayTree function to display it
	 * 
	 * @param featureModel the feature Model description, as returned by the server
	 */
	$scope.buildTree = function(featureModel){
		
		console.log(featureModel);
		
		//tree to build from featureModel
		var tree = {};
		var rootNode = featureModel.tree.children[0];
		
		tree.id = rootNode.id;
		tree.type = "TreeView";
		tree.label = rootNode.id;
		tree.expanded = true;
		tree.children = [];
		
		for(var i = 0; i < rootNode.children.length; i++)
			tree.children[i] = buildChild(rootNode.children[i]);
		
		$("#variable"+$scope.configureVarId).empty();
		
		console.log(tree);
		
		//Displaying the tree with built json
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
	
	/**
	 * Recursive method to build children of a node
	 */
	function buildChild(childDescription)
	{
		var node = {};
		
		node.id = childDescription.id;
		node.label = childDescription.id;
		
		if(childDescription.optionnal==true)
			node.type="check";
		
		//Different type of node depending on relation value given on JSon data
		switch(childDescription.relation)
		{
			case 16 :
				node.type = "radio";
				break;
			case 8 :
				node.type = "check";
				break;
		}
		
		var children = [];
		
		for(var i = 0; i < childDescription.children.length; i++)
			children[i] = buildChild(childDescription.children[i]);
		
		if(children.length == 0)
			node.leaf = true;
		else
			node.children = children;
		
		return node;
	}
	
	
}