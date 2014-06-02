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

function updateWorkspace() {
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
}

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
function displayWorkspace(filespecification) {
$('#myTreeView').html('');
YUI().use(
  'aui-tree-view',
  function(Y) {
    var tview = new Y.TreeViewDD(
      { 
        boundingBox: '#myTreeView',
	  children: filespecification, 
	  on: {
	  	 lastSelectedChange: function(event) {
	       var nodeId = event.newVal.get('id');
	       var node = tview.getNodeById(nodeId);

	       if (node.isLeaf()) 
	       	loadFile (mkCompleteName(node)); 
		}
       }
       }
    ) ; 
    
    tview.render();
  }
);
}



function mkCompleteName (n) {
	if (!n)
	   return "" ; 
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
		//test : if the folder exist yet.
		if (name) {
			//code
			//if the folder exist
			//alert("Folder exist delete the previous folder or change the name");
		}else{
			//call the scala function with the parameter
			jsRoutes.controllers.WebFMLInterpreter.createFolder(name).ajax({
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
		}
		
	}else{
		//just a message to the user
		alert("If you don't give a name to a folder that will not work :(");	
	}
	
}

/**
 *Function which create a new file in a specific folder
 */
function createFile() {
	//code
}

