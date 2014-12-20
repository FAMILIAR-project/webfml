function FMLConfigure($scope, $rootScope){
	
	$scope.configureVarId = "";
	
	$scope.$on('FMLConfigure', function (event, id) {
		
		$scope.configureVarId = id;
		
		$scope.displayTreePanel();
		
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
	$scope.displayTreePanel = function(){
		
		//Créer,le nouvel onglet (la div HTML associée)
		//Passer le json à JSTree pour qu'il affiche le 
		var fenetre = $("#variable" +$scope.configureVarId) ;		
		
		if(fenetre.length == 0){
			$('#tabs-hoster .nav-tabs').append('<li><a href="#variable'+$scope.configureVarId+'" data-toggle="tab">Variable' +$scope.configureVarId + '</a></li>');
		
			$('#tabs-hoster .tab-content').append('<div class="tab-pane" id="variable'+$scope.configureVarId+'">Bonjour les cop1</div>');
		}
		
	}
	
	
	/**
	 * Build a good formated json tree and then call the displayTree function to display it
	 * 
	 * @param featureModel the feature Model description, as returned by the server
	 */
	$scope.buildTree = function(featureModel){
		
		console.log("buildTree");
		console.log(featureModel);
		
		//Passer le json à JSTree pour qu'il affiche le 
		YUI().use(
				  'aui-tree-view',
				  function(Y) {
				    new Y.TreeViewDD(
				      {
				        boundingBox: '#variable'+$scope.configureVarId,
				        children: [
				          {
				            children: [
				              {label: 'Watermelon', leaf: true, type: 'check'},
				              {label: 'Apricot', leaf: true, type: 'check'},
				              {label: 'Pineapple', leaf: true, type: 'check'},
				              {label: 'Kiwi', leaf: true, type: 'check'},
				              {label: 'Orange', leaf: true, type: 'check'},
				              {label: 'Pomegranate', leaf: true, type: 'check'}
				            ],
				            expanded: true,
				            label: 'Checkboxes'
				          },
				          {
				            children: [
				              {label: 'Watermelon', leaf: true, type: 'radio'},
				              {label: 'Apricot', leaf: true, type: 'radio'},
				              {label: 'Pineapple', leaf: true, type: 'radio'},
				              {label: 'Kiwi', leaf: true, type: 'radio'},
				              {label: 'Orange', leaf: true, type: 'radio'},
				              {label: 'Pomegranate', leaf: true, type: 'radio'}
				            ],
				            expanded: true,
				            label: 'Radio'
				          },
				          {
				            children: [
				              {label: 'Watermelon', leaf: true, type: 'task'},
				              {label: 'Apricot', leaf: true, type: 'task'},
				              {label: 'Pineapple', leaf: true,  type: 'task'},
				              {label: 'Kiwi', leaf: true, type: 'task'},
				              {label: 'Orange', leaf: true, type: 'task'},
				              {label: 'Pomegranate', leaf: true,  type: 'task'}
				            ],
				            expanded: true,
				            label: 'Task',
				            type: 'task'
				          }
				        ]
				      }
				    ).render();
				  }
				);
		
		//Algo de transformation du json en quelque chose de bouffable par JSTree
		
	}
	
	
}