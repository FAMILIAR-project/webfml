function FMLEditorCtrl($scope, $rootScope) {
	
	$scope.cmd = function () {
	    var idToGet = editor.getSession().getValue();
	    jsRoutes.controllers.WebFMLInterpreter.interpret(idToGet).ajax({
			success : function(data) {  
				$rootScope.$broadcast('variables', data)
			},
		        error : function(data) {  
				$('#msgid').html('Error...<div class="alert alert-danger">' + data + '</div>') ; 
			},
		        beforeSend : function(event, jqxhr, settings) {
			        $('#wait').html('<img src="assets/images/ajax-loader.gif" />') ; 
			},
		       complete : function(jqxhr, textstatus) {
			    $('#wait').html('') ;		   
		    }
	    });
	}
	
	$scope.reset = function() {
		 jsRoutes.controllers.WebFMLInterpreter.reset().ajax({
			success : function(data) {  
				$rootScope.$broadcast('variables', data)
			},
		        error : function(data) {  
				$('#msgid').html('Impossible to reset...<div>' + data + '</div>') ; 
			},
		        beforeSend : function(event, jqxhr, settings) {
			        $('#wait').html('<img src="assets/images/ajax-loader.gif" />') ; 
			},
		       complete : function(jqxhr, textstatus) {
			    $('#wait').html('') ;		   
	       }
		});
	}
	
	$scope.save = function () {
   	    var idToGet = editor.getSession().getValue();
        var filename = $('#fileInputName').val(); 

	    jsRoutes.controllers.WebFMLInterpreter.saveAs(idToGet, filename).ajax({
			success : function(data) {  
				$('#msgid').html('<div class="alert alert-success">' + filename + ' has been saved...</div>') ; 
			},
		        error : function(data) {  
				$('#msgid').html('<div class="alert alert-danger">Unable to save...</div>') ; 
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
/**
 *Load file in the editor when we make a dbl-click on the file
 *
 */
function loadFile(filename) {
 jsRoutes.controllers.WebFMLInterpreter.loadFile(filename).ajax({
		success : function(data) {  
			 editor.setValue (data, 1) ;
		},
	        error : function(data) {  
			$('#msgid').html('Unable to load the file..' + data) ; 
		},
	        beforeSend : function(event, jqxhr, settings) {
		        $('#wait').html('<img src="assets/images/ajax-loader.gif" />') ; 
		},
	       complete : function(jqxhr, textstatus) {
		    $('#wait').html('') ;		   
	       }
	})
	;

}

/**
 *Function which refresh the workspace
 */
function updateWorkspace() {
jsRoutes.controllers.WebFMLInterpreter.listFiles().ajax({
		//if success
		success : function(data) { 
			//we display the workspace	
		        displayWorkspace(data); 
		},
		
	        error : function(data) {  
			$('#myTreeView').html('Unable to load the list of files... <div class="alert alert-danger">' + data + '</div>') ; 
		},
	        beforeSend : function(event, jqxhr, settings) {
		        $('#wait').html('<img src="assets/images/ajax-loader.gif" />') ; 
		},
	       complete : function(jqxhr, textstatus) {
		    $('#wait').html('') ;		   
	       }
	})
	;
}
/**
 *Display the workspace at begining
 */
$(document).ready(function() { 
 
jsRoutes.controllers.WebFMLInterpreter.listFiles().ajax({
		success : function(data) {
		        displayWorkspace(data); 
		},
	        error : function(data) {  
			$('#myTreeView').html('Unable to load the list of files... <div class="alert alert-danger">' + data + '</div>') ; 
		},
	        beforeSend : function(event, jqxhr, settings) {
		        $('#wait').html('<img src="assets/images/ajax-loader.gif" />') ; 
		},
	       complete : function(jqxhr, textstatus) {
		    $('#wait').html('') ;		   
	       }
	})
	;
});

/*
 * Workspace
 * The tree view
 *
 */
var path="";
function displayWorkspace(filespecification) {
$('#myTreeView').html('');
YUI().use(
  'aui-tree-view',
  function(Y) {
    var tview = new Y.TreeViewDD(
      {
	
	//call the div where we put the tree
        boundingBox: '#myTreeView',
	children: filespecification, 
	  on: {
		lastSelectedChange: function(event) {
			var nodeId = event.newVal.get('id');
			//treenode object
			var node = tview.getNodeById(nodeId);
			//return just the label of the node
			//var nameOfMyNode = node.getAttrs().label;
			//console.log("NodeID "+nodeId);
			//console.log("Node "+node);
			//console.log("Name of the Node " +nameOfMyNode);
			//console.log("parentsOfNode "+parentsOfNode);
			//console.log("Node isLeaf: "+node.isLeaf());
			
			//type of the node
			var type ="";
			
			if (node.isLeaf()){
				type = "file";
				path= mkCompleteName(node);
				loadFile (mkCompleteName(node));
			}else{
				type = "directory";
				path = mkCompleteName(node);
				console.log("Voyons le nom: "+mkCompleteName(node));
			}
		}
       }
       }
    ) ; 
    
    tview.render();
  }
);
}
/**
 *Function which return the current path to create a file or a directory
 */
/*function getPath(){
	return path;
}*/

/**
 *Function which give the complete name of the node
 *(e.g: directory/ or directory/Try)
 *@param n : a node of the treeview
 */
function mkCompleteName (n) {
	if (!n){
	   return "" ; 
	}
	if (n.get('parentNode')) {
	 var p = mkCompleteName(n.get('parentNode')) ; 
	   if (p)
	   	  return p + "/" + n.get('label') ; 
	   else
	      return n.get('label') ; 
	}
}

/**
 * Function which create a new folder in the workspace
 */
function createFolder() {
	//open a window where we put the name of the folder
	var name= prompt("You will create a new folder, please insert a name","New Folder");
	//test if they something inside the inputbox
	if (name!=null && name!="") {
		var finalPath = path+"/"+name;
		alert(finalPath);
		//call the scala function with the parameter
		jsRoutes.controllers.WebFMLInterpreter.createFolder(finalPath).ajax({
			success : function(data) {
				//refresh the worspace
				updateWorkspace(); 
			},
			//if they are an error
			error : function(data) {  
				$('#myTreeView').html('Unable to create the folder... <div class="alert alert-danger">' + data + '</div>') ; 
			},
			//display a loader
			beforeSend : function(event, jqxhr, settings) {
				$('#wait').html('<img src="assets/images/ajax-loader.gif" />') ; 
			},
		       complete : function(jqxhr, textstatus) {
			    $('#wait').html('') ;		   
		       }
		});
		
	}else{
		//just a message to the user
		alert("If you don't give a name to the folder that will not work :(");	
	}
	
}


/**
 *Function which create a new file in a specific folder
 */
function createFile() {
	var name = prompt("You will create a file in this directory: "+path+" please insert a name","New File");
	//split the name of the file
	var res = name.split(".");
	/*test the extention of the file
	 *if the extention is not right
	 */
	console.log(res[1]);
	if(res[1]!="fml" && res[1]!="dimacs"){
		//so the file extention are not good :'( 
		alert("the file have not the right extention (e.g .fml or dimacs)");
	}else{
		//the extention is okay
		if (name!=null && name!="") {
			//display the name of the final path
			console.log(path +"/"+name);
			var fname = path+"/"+name;
			jsRoutes.controllers.WebFMLInterpreter.createFile(fname).ajax({
				success : function(data) {
					//refresh the worspace
					updateWorkspace(); 
				},
				//if they are an error
				error : function(data) {  
					$('#myTreeView').html('Unable to create the file... <div class="alert alert-danger">' + data + '</div>') ; 
				},
				//display a loader
				beforeSend : function(event, jqxhr, settings) {
					$('#wait').html('<img src="assets/images/ajax-loader.gif" />') ; 
				},
			       complete : function(jqxhr, textstatus) {
				    $('#wait').html('') ;		   
			       }	
			});
		}else{
			//the name not exist 
			alert("If you don't give a name to the file that will not work :(");	
		}
	}
	
}
/**
 *@TODO
 *
 *
 */
function deleteFile(args) {
	//code
}
/**
 *@TODO
 *
 *
 */
function deleteFolder(args) {
	//code
}

