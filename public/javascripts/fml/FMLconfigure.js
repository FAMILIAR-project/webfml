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
					$scope.buildTree({
						  id: 'A',
						  parent : '',
						  optionnal : false,
						  relation : '',
						  relationTargetIds : [],
						  children : [  
						    {
						      id: 'B',
						      parent : 'A',
						      optionnal : false,
						      relation : '',
						      relationTargetIds : [],
						      children : [
						        {
						          id: 'D',
						          parent : 'B',
						          optionnal : false,
						          relation : '',
						          relationTargetIds : [],
						          children : [
						            {
						              id: 'F',
						              parent : 'D',
						              optionnal : false,
						              relation : '',
						              relationTargetIds : [],
						              children : []
						            }
						          ]
						        },
						        {
						          id: 'E',
						          parent : 'B',
						          optionnal : false,
						          relation : '',
						          relationTargetIds : [],
						          children : []
						        }
						      ]
						    },
						    {
						      id: 'C',
						      parent : 'A',
						      optionnal : true,
						      relation : '',
						      relationTargetIds : [],
						      children : []
						    }
						  ]
						});
					
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
		
			$('#tabs-hoster .tab-content').append('<div class="tab-pane" id="variable'+$scope.configureVarId+'"><b>Configurateur du FeatureModel '+$scope.configureVarId+'</b></div>');
		}
		
	}
	
	
	/**
	 * Build a good formated json tree and then call the displayTree function to display it
	 * 
	 * @param featureModel the feature Model description, as returned by the server
	 */
	$scope.buildTree = function(featureModel){
		
		console.log("buildTree");
		//Passer le json à JSTree pour qu'il affiche le 
		YUI().use(
				'aui-tree-view','node',function(Y) {
					
					
					var children = [recursiveCourse(featureModel)];
					
					treeRootNode = new Y.TreeNode( {
                        cache :false,
                        label : featureModel['id'],
                        id : featureModel['id'],
                        expanded :false,
                        draggable :false
                    });
					
					treeView = new Y.TreeViewDD(
							{
								boundingBox : '#variable'+$scope.configureVarId,
								children : [treeRootNode]
							});
					
					recursiveCourse(treeView,featureModel['children']);
					
					$scope.treeView.render();
		});
	}
	
	function recursiveCourse(treeView,nodeToCourse){
		
		YUI().use('aui-tree-view','node',function(Y) {
			$.each(nodeToCourse, function(key,node){
				if(node['optionnal']){
					var node =  new Y.TreeNodeCheck(
	                        {   id: node['id'],
	                            label: node['id'],
	                            type: 'check'
	                        }
	                    );
				}else{
					var node =  new Y.TreeNode(
	                        {   id: node['id'],
	                            label: node['id'],
	                            type: 'node'
	                        }
	                    );
				}
				
				treeView.getNodeById(node['parent']).appendChild(node);
				
			});
		});
			
	}
		
		
		
		
	/*	var nodeToReturn = { 'label' : ''};
		console.log(nodeToCourse);
		$.each(nodeToCourse], function(key,node){
 			console.log(node['id']); 
			nodeToReturn['label'] = node['id'];
			if(node['optionnal']){
				nodeToReturn['type'] = 'check';
			}
			if(!jQuery.isEmptyObject(node['children'])){
				nodeToReturn['expanded'] = true;
				nodeToReturn['children'] = [recursiveCourse(node['children'])];
			}else{
				nodeToReturn['leaf'] = true;
			}
		});
		console.log(nodeToReturn);
		return nodeToReturn;
	}*/
	
	 
}