var constraintTree = "";

//
function checkConstraints(elementName,tabConstraint) {
	var elThis = $('input[value="'+elementName+'"]');
	for (var i=0; i<tabConstraint.length; i++) {
		var elOther = "";
		if (tabConstraint[i].param1[0].name == elementName) {
			elOther = $('input[value="'+tabConstraint[i].param2[0].name+'"]');
		} else {
			elOther = $('input[value="'+tabConstraint[i].param1[0].name+'"]');
		}
		if (tabConstraint[i].cons.indexOf('&') > -1) {
			if ((tabConstraint[i].param1[0].specif != null && tabConstraint[i].param1[0].specif.indexOf('!') > -1 ) || (tabConstraint[i].param2[0].specif != null && tabConstraint[i].param2[0].specif.indexOf('!') > -1 )){
				if (elThis.is(':checked')) {
					if (elOther.is(':checked')) {
						if (elOther.is(':disabled')) {
							elOther.prop('disabled',false);	
							elOther.click();
							elOther.prop('disabled',true);
						} else {
							elOther.click();
						}
					}
				} else {
					if (!elOther.is(':checked')) {
						if (elOther.is(':disabled')) {
							elOther.prop('disabled',false);	
							elOther.click();
							elOther.prop('disabled',true);
						} else {
							elOther.click();
						}
					}
				}
			} else {
				if (elThis.is(':checked')) {
					if (!elOther.is(':checked')) {
						if (elOther.is(':disabled')) {
							elOther.prop('disabled',false);	
							elOther.click();
							elOther.prop('disabled',true);
						} else {
							elOther.click();
						}
					}
				} else {
					if (elOther.is(':checked')) {
						if (elOther.is(':disabled')) {
							elOther.prop('disabled',false);	
							elOther.click();
							elOther.prop('disabled',true);
						} else {
							elOther.click();
						}
					}
				}
				
			}
		} else if (tabConstraint[i].cons.indexOf('->') > -1){
			if (tabConstraint[i].param1[0].name == elementName) {
				if (tabConstraint[i].param1[0].specif.indexOf('!') > - 1) {
					if (!elThis.is(':checked')) {
						if (!elOther.is(':checked')) {
							if (elOther.is(':disabled')) {
								elOther.prop('disabled',false);	
								elOther.click();
								elOther.prop('disabled',true);
							} else {
								elOther.click();
							}
						}
					}
				} else if (tabConstraint[i].param2[0].specif.indexOf('!') > -1) {
					if (elThis.is(':checked')) {
						if (elOther.is(':checked')) {
							if (elOther.is(':disabled')) {
								elOther.prop('disabled',false);	
								elOther.click();
								elOther.prop('disabled',true);
							} else {
								elOther.click();
							}
						}
					}
				} else {
					if (elThis.is(':checked')) {
						if (!elOther.is(':checked')) {
							if (elOther.is(':disabled')) {
								elOther.prop('disabled',false);	
								elOther.click();
								elOther.prop('disabled',true);
							} else {
								elOther.click();
							}
						}
					}
				}
			}
		}
	}
}

//Function to enable or disable checkbox and radio button
function enableChildren(data) {
	var elThis = $('input[value="'+data.name+'"]');
	if (elThis.is(':checked')) {
		for (var i=0; i < data.children.length; i++){
			var el = "";
			if (data.specif.indexOf('o') > -1) {
				el = $('input[name="'+data.name+'Children"]');
			} else {
				el = $('input[name="'+data.children[i].name+'"]');
			}
			el.prop('disabled',false);	
		}
	} else {
		for (var i=0; i < data.children.length; i++){
			var el = "";
			if (data.specif.indexOf('o') > -1) {
				el = $('input[name="'+data.name+'Children"]');
			} else {
				el = $('input[name="'+data.children[i].name+'"]');
			}
			el.prop('disabled',true);
			el.prop('checked',false);
			enableChildren(data.children[i]);
		}
	}
}

//Function to close active tab, close configuration tab
function closeConf() {
	$(".active")[0].outerHTML="";
	$(".tab-pane.active")[0].outerHTML="";
	$('[name="editor"]').addClass('active');
}

//Function to hide or show parent hierarchy
function displayChildren(parentName) {
	var elChildren = $('.divChildren_'+parentName);
	var elParent = $('.div_'+parentName);
	if (elChildren.is(':visible')) {
		elChildren.hide();
		elParent.html('&#9658;');
	} else {
		elChildren.show();
		elParent.html('&#9660;');
	}
}

//Controller Configure with main function to create configuration tree / indent tree
function FMLConfigureCtrl($scope, $rootScope) {
	$scope.varIDs = [];
	$scope.lastVar = '';
	$scope.arrayConfigure = new Array();
	
	//Function Ajax to get Tree by this ID
	$scope.$on('configure',function(event, id) {
		try {
			jsRoutes.controllers.WebFMLInterpreter.configure(id).ajax({
				success : function(data) {
					$scope.lastVar = data;
					$scope.openConf(id,data);
					$scope.$apply();
				},
				error : function(data) {
					$('#lastValueFML').html('Error...<div class="alert alert-danger">' + data + '</div>');
				},
				beforeSend : function(event, jqxhr, settings) {
					$('#loader').html('<img src="assets/images/ajax-loader.gif" />'); 	
				},
				complete : function(data, jqxhr, textstatus) {
					$('#loader').html('');	
				}
			});		
		} catch (e) {
			jqconsole.Write('ERROR : '+ e.message + '\n');
		}
		
	});
	
	// Create new tab with fmv name and number of this occurence.
	$scope.openConf = function(id,data) {
		var tabTitle = "";
		var tabContent = "";
		
		if ($scope.arrayConfigure[id] == null) {
			$scope.arrayConfigure[id] = 1; 
			tabTitle = '<li id="tabConf'+id+'"><a href="#configure-tab'+id+'" data-toggle="tab">'+id+'</a></li>';
			tabContent = '<div class="tab-pane" id="configure-tab'+id+'" ng-controller="FMLConfigureCtrl">';
		} else {
			$scope.arrayConfigure[id] += 1;
			tabTitle = '<li id="tabConf'+id+'_'+$scope.arrayConfigure[id]+'"><a href="#configure-tab'+id+'_'+$scope.arrayConfigure[id]+'" data-toggle="tab">'+id+'('+$scope.arrayConfigure[id]+') </a></li>';
			tabContent = '<div class="tab-pane" id="configure-tab'+id+'_'+$scope.arrayConfigure[id]+'" ng-controller="FMLConfigureCtrl">';
		}
		
		tabContent += $scope.contentConf(id,data);
		$('ul.nav.nav-tabs').append(tabTitle);
		$('.tab-content').append(tabContent);
	}
	
	//Create content of new tab with indent tree transformed into checkbox and radio button
	$scope.contentConf = function(id,data) {
		var content = "<div>";
		var jsonParse = JSON.parse(data);
		constraintTree = jsonParse.constraint;
		//Complete content of tab with tree representation
		content += "<div style='float:left' class=content>"+$scope.recur(jsonParse.tree[0], 0, null, null, jsonParse.constraint)+"</div>";
		content += "<div style='float:right;cursor:pointer'><a onClick='closeConf()'>close</a></div>";
		//Close tabContent and append result to html
		content += "</div>";
		return content;
	}
	
	//Function to indent correctly each level of indent tree
	$scope.indent = function (level,children,parent) {
		var indent = "";
		for(i=0; i<level*4; i++) {
			indent += "&nbsp";
		}
		if (children) {
			indent += "<span class='div_"+parent+"' onClick='displayChildren(\""+parent+"\")'>&#9660</span>";
		}
		return indent;
	}
	
	//Function recursive to generate each level of indent tree in correctly content
	$scope.recur = function (data , level, parentName , parentSpecif , constraintTree) {
		var dataJson = JSON.stringify(data);
		var content = "";
		var hasChildren = (data.children.length != 0);
		
		//TODO Add constraintes problems
		var tabConstraints = [];
		for (i=0; i<constraintTree.length; i++) {
				if ((constraintTree[i].param1[0].name != null && constraintTree[i].param1[0].name.indexOf(data.name) > -1 ) || (constraintTree[i].param2[0].name != null && constraintTree[i].param2[0].name.indexOf(data.name) > -1 )) {
				tabConstraints.push(constraintTree[i]);
			}
		}
		tabConstraints = JSON.stringify(tabConstraints);
		

		if (parentSpecif != null && parentSpecif.indexOf('o') > - 1) {
			//TODO Add function to add new radio button if one is check
			if (parentSpecif.indexOf('+') > -1) {
				content = $scope.indent(level,hasChildren,data.name) + "<input ";
				if(hasChildren) {
					content += "onChange='enableChildren("+dataJson+");checkConstraints(\""+data.name+"\","+tabConstraints+");'";
				} else {
					content += "onChange='checkConstraints(\""+data.name+"\","+tabConstraints+")'";
				}
				
				content += "type='radio' name='"+parentName+"Children' value='"+data.name+"'";
				if(level != 0){
					content += "disabled";
				}
				content += " />";
				if (data.specif.indexOf('!') > -1) {
					content +=  "<i>" + data.name + "</i>";
				} else {
					content += data.name;
				}	
			} else {
				content = $scope.indent(level,hasChildren,data.name) + "<input ";
				if(hasChildren) {
					content += "onChange='enableChildren("+dataJson+");checkConstraints(\""+data.name+"\","+tabConstraints+");'";
				} else {
					content += "onChange='checkConstraints(\""+data.name+"\","+tabConstraints+")'";
				}
				content += "type='radio' name='"+parentName+"Children' value='"+data.name+"'";
				if(level != 0){
					content += "disabled";
				}
				content += " />";
				if (data.specif.indexOf('!') > -1) {
					content +=  "<i>" + data.name + "</i>";
				} else {
					content += data.name;
				}	
			}
		} else {
			content = $scope.indent(level,hasChildren,data.name) + "<input ";
			if(hasChildren) {
				content += "onChange='enableChildren("+dataJson+");checkConstraints(\""+data.name+"\","+tabConstraints+");'";
			} else {
				content += "onChange='checkConstraints(\""+data.name+"\","+tabConstraints+")'";
			}
			content += " type='checkbox' name ='"+data.name+"' value='"+data.name+"' ";
			if(level != 0){
				content += "disabled";
			}
			content += " />";
		
			if (data.specif.indexOf('!') > -1) {
				content += " <i>" + data.name + "</i>";
			} else {
				content += data.name;
			}
			
		}
		content += "<br />";
		if (hasChildren) {
			for (var i=0; i < data.children.length; i++){
				content += '<div class="divChildren_'+data.name+'">'+$scope.recur(data.children[i], level+1, data.name, data.specif, constraintTree)+'</div>';
			}	
 		}
		return content;
	}
}