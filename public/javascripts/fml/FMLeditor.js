/**
 *
 *
 *
 *
 */

/**
 *Function which
 *param : $scope :
 */
function FMLEditorCtrl($scope, $rootScope) {
	/**
	 *Function wich send to the interpreter the code to execute
	 *and display the result
	 */
	$scope.cmd = function () {
	    var idToGet = editor.getSession().getValue();

		console.log("Starting interpretation...");
	    jsRoutes.controllers.FamiliarIDEController.isAuthentified().ajax({
                                success : function(data) {
                                    // no problem
                                    if (data["user"] == -1) {
                                    console.log("Not authentified...");
                                    initTemporarySession();
                                    }

                                },
                                error : function(data) {
									$('#msgid').html('Error in authentification...<div class="alert alert-danger">' + data + '</div>') ;

                                },
                                beforeSend : function(event, jqxhr, settings) {
																	      $('#loader').html('<img src="../assets/images/ajax-loader.gif" />') ;
                                },
                                complete : function(jqxhr, textstatus) {
                                        $('#loader').html('') ;
                                }
                            });

	    jsRoutes.controllers.WebFMLInterpreter.interpret(idToGet).ajax({
			success : function(data) {
				$rootScope.$broadcast('variables', data)
			},
		        error : function(data) {
				  		data["msgError"] = '<div class="alert alert-danger"><p>Unable to parse/interpret</p>' + data.responseJSON["msgError"] + '</div>';
							$rootScope.$broadcast('variables', data);
			},
		        beforeSend : function(event, jqxhr, settings) {
			        $('loader').html('<img src="../assets/images/ajax-loader.gif" />') ;
			},
		       complete : function(jqxhr, textstatus) {
			    $('#loader').html('') ;
		    }
	    });
	}

	$scope.reset = function() {
		jsRoutes.controllers.WebFMLInterpreter.reset().ajax({
			success : function(data) {
				data = { 'msgError' : '<div class="alert alert-success"><p>Resetting the environment: done!</p></div>' };
				$rootScope.$broadcast('variables', data);
			},
		    error : function(data) {
	        data["msgError"] = '<div><p>Impossible to reset...</p>' + data.responseJSON["msgError"] + '</div>';
					$rootScope.$broadcast('variables', data);
			},
		    beforeSend : function(event, jqxhr, settings) {
			        $('#wait').html('<img src="../assets/images/ajax-loader.gif" />') ;
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
							data = { 'msgError': '<div class="alert alert-success">' + filename + ' has been saved</div>' };
							$rootScope.$broadcast('variables', data);
						},
		        error : function(data) {
							data["msgError"] = '<div class="alert alert-danger">Unable to save file: ' + filename + '</div>';
							$rootScope.$broadcast('variables', data);
		      	},
		        beforeSend : function(event, jqxhr, settings) {
			        $('#wait').html('<img src="../assets/images/ajax-loader.gif" />') ;
			      },
		        complete : function(jqxhr, textstatus) {
			        $('#wait').html('') ;
		        }
	    });
	}

}


/**
 * Load file in the editor when we make a dbl-click on the file
 *
 */
function loadFile (filename) {
 jsRoutes.controllers.WebFMLInterpreter.loadFile(filename).ajax({
		success : function(data) {
			$('a[href="#editor"]').tab('show');
			$('#editor').addClass('active');
			$("a[href='#editor']").html('' + filename);
			$('#ksynthesis-tab').removeClass('active');
			editor.setValue (data, 1) ;

		},
					error : function(data) {
						data["msgError"] = '<div class="alert alert-danger"><p>Unable to load the file...</p>' + filename + '</div>';
						$rootScope.$broadcast('variables', data);
		},
					beforeSend : function(event, jqxhr, settings) {
						$('#wait').html('<img src="../assets/images/ajax-loader.gif" />') ;
		},
				 complete : function(jqxhr, textstatus) {
				$('#wait').html('') ;
				 }
	})
	;

}

function initTemporarySession() {

                    console.log("Creating temporary session");

                    jsRoutes.controllers.FamiliarIDEController.createTemporarySession().ajax({
                        success : function(data) {
                            depictUserInformation(data['id']);
                        },
                        error : function(data) {
													data["msgError"] = '<div class="alert alert-danger"><p>Impossible to create temporary sessions</p>' + data.responseJSON["msgError"] + '</div>';
													$rootScope.$broadcast('variables', data);
												},
                        beforeSend : function(event, jqxhr, settings) {
                                $('#wait').html('<img src="../assets/images/ajax-loader.gif" />') ;
                        },
                        complete : function(jqxhr, textstatus) {
                                $('#wait').html('') ;
                        }
                    });
	  }

function depictUserInformation(userid) {
            			console.log('Depicting UI: ' + userid);
            		    $('#userinformation').html ('You are <a>User_' + userid + '</a> <a onclick="deleteSession()">(logout)</a>');
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
		        $('#wait').html('<img src="../assets/images/ajax-loader.gif" />') ;
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
		    	$('#wait').html('<img src="../assets/images/ajax-loader.gif" />') ;
		},
	       complete : function(jqxhr, textstatus) {
		    $('#wait').html('') ;
	    }
	});
});

/*
 * Workspace
 * The tree view
 *
 */
/*
 *the current path
 */
var path="";
/*
 *the name of the current file
 */
var currentFileName="";
/**
 *Function which display the worksapce
 *this is a treeview from alloy framework
 */
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
		lastSelectedChange: function (event) {
			var nodeId = event.newVal.get('id');
			//treenode object
			var node = tview.getNodeById(nodeId);
			//type of the node
			if (node.isLeaf()){
				//file
				path= mkCompleteName(node);
				//return the name of the current node
				currentFileName= mkCompleteName(node);
				loadFile (mkCompleteName(node));
			} else {
				//directory
				path = mkCompleteName(node);
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
				$('#wait').html('<img src="../assets/images/ajax-loader.gif" />') ;
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
	/*in case of the user doesn't clic on a folder
	*we give a default value : "repository"
	*/
	if (path=="") {
		path="repository"; // horrible: TODO
	} else {
		//we slipt the name to know if we are on a file
		var res_path = path.split(".");
		//if they are something after the point
		if (res_path[1]) {
			alert("You're on a file ! You must clic on a directory before !");
		}else{
			/*
			*We receive the name of the file
			*/
			var name = prompt("You will create a file in this directory: "+ path + " please insert a name", "New File");
			//if the user give no name
			if (name == null || name == "") {
				alert("Name of file incorret !");
			} else {
				//split the name of the file
				var res = name.split(".");
				/*test the extention of the file
				*if the extention is not right
				*/
				if(res[1] != "fml" && res[1] != "dimacs"){
					//so the file extention are not good :'(
					alert("The file has not the right extention (e.g .fml or dimacs)"); // TODO
				} else {
						//the extention is okay
						if (name != null && name != "") {
							//display the name of the final path
							var fname = name; // TODO
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
									$('#wait').html('<img src="../assets/images/ajax-loader.gif" />') ;
								},
								complete : function(jqxhr, textstatus) {
								    $('#wait').html('') ;
								  }
							});

						}
						else {
							//the name not exist
							alert("If you don't give a name to the file that will not work :(");
						}
				}
			}
		}
	}

}
/**
 *Delete the current file or the current directory
 */
function deleteF() {
	//display a window which ask the user if this is the good choice
	if (confirm("Are you sure to delete "+path+" ")) {
		//split the path to know if this is a file or a directory
		var res = path.split(".");
		//if this is a file
		if (res[1]=="fml"|| res[1]=="dimacs") {
			//this is a file
			jsRoutes.controllers.WebFMLInterpreter.deleteFile(path).ajax({
				success : function(data) {
					//refresh the worspace
					updateWorkspace();
				},
				//if they are an error
				error : function(data) {
					$('#myTreeView').html('Unable to delete the file... <div class="alert alert-danger">' + data + '</div>') ;
				},
				//display a loader
				beforeSend : function(event, jqxhr, settings) {
					$('#wait').html('<img src="../assets/images/ajax-loader.gif" />') ;
				},
				complete : function(jqxhr, textstatus) {
				    $('#wait').html('') ;
				}
			});

		}else{
		//this is a directory
			jsRoutes.controllers.WebFMLInterpreter.deleteFolder(path).ajax({
				success : function(data) {
					//refresh the worspace
					updateWorkspace();
				},
				//if they are an error
				error : function(data) {
				$('#myTreeView').html('Unable to delete the directory... <div class="alert alert-danger">' + data + '</div>') ;
				},
				//display a loader
				beforeSend : function(event, jqxhr, settings) {
					$('#wait').html('<img src="../assets/images/ajax-loader.gif" />') ;
				},
				complete : function(jqxhr, textstatus) {
				    $('#wait').html('') ;
				}
			});
		}
	}

}
/**
 * JavaScript function which save the file
 */
function saveF() {
	//get the content from the editor
	var editor = ace.edit("editor");
	var content = editor.getSession().getValue();
	//display the result
	//test if the name of the current file is null or not.
	//if null that mean's the user doesn't click on a file
	if (currentFileName != null && currentFileName != "") {
		jsRoutes.controllers.WebFMLInterpreter.saveFile(currentFileName, content).ajax({
			success : function(data) {
				//refresh the worspace
				updateWorkspace();
				//display the sucess
				data["msgError"] = '<div class="success alert-success">File ' + currentFileName + ' saved!</div>';
				$rootScope.$broadcast('variables', data);
			},
			//if they are an error
			error : function(data) {
			$('#myTreeView').html('Unable to save the file... <div class="alert alert-danger">' + data + '</div>') ;
			},
			//display a loader
			beforeSend : function(event, jqxhr, settings) {
				$('#wait').html('<img src="../assets/images/ajax-loader.gif" />') ;
			},
			complete : function(jqxhr, textstatus) {
			    $('#wait').html('') ;
			}
		});



	} else {
		//display the unsucess
		data["msgError"] = '<div class="alert alert-danger">Unable to save the file ' + currentFileName + '</div>';
		$rootScope.$broadcast('variables', data);
	}

}
