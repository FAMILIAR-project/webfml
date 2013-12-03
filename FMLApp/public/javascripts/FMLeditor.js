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

