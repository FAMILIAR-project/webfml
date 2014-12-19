function FMLConfigureCtrl($scope, $rootScope) {
	$scope.varIDs = [];
	$scope.lastVar = '';
	$scope.arrayConfigure = new Array();
	
	$scope.$on('configure',function(event, id) {
		try {
			jsRoutes.controllers.WebFMLInterpreter.configure(id).ajax({
				success : function(data) {
					$scope.lastVar = data;
					$scope.openConf(id);
					$scope.$apply();
				},
				error : function(data) {
					$('#lastValueFML').html('Error...<div class="alert alert-danger">' + data + '</div>');
				},
				beforeSend : function(event, jqxhr, settings) {
					$('#loader').html('<img src="assets/images/ajax-loader.gif" />'); 	
				},
				complete : function(data, jqxhr, textstatus) {
					$('#wait').html('');	
				}
			});		
		} catch (e) {
			jqconsole.Write('ERROR : '+ e.message + '\n');
		}
		
	});
	
	/**
	 * Create new tab with fmv name and number of this occurence.
	 */
	$scope.openConf = function(id) {
		var tabTitle = "";
		var tabContent = "";
		
		if ($scope.arrayConfigure[id] == null) {
			$scope.arrayConfigure[id] = 1; 
			tabTitle = '<li id="tabConf'+id+'"><a href="#configure-tab" data-toggle="tab">'+id+'</a></li>';
			tabContent = '<div class="tab-pane" id="configure-tab" ng-controller="FMLConfigureCtrl">';
		} else {
			$scope.arrayConfigure[id] += 1;
			tabTitle = '<li id="tabConf'+id+'['+$scope.arrayConfigure[id]+']"><a href="#configure-tab" data-toggle="tab">'+id+'('+$scope.arrayConfigure[id]+') </a></li>';
			tabContent = '<div id="tabConf'+id+'['+$scope.arrayConfigure[id]+']" class="tab-pane" id="configure-tab" ng-controller="FMLConfigureCtrl">';
		}
		//Complete content of tab with tree representation
		tabContent += "<div><a ng-click='testcloseConf()'>close</a>"
		//Close tabContent and append result to html
		tabContent += "</div>";
		$('ul.nav.nav-tabs').append(tabTitle);
		$('.tab-content').append(tabContent);
	}
	
	$scope.testcloseConf = function() {
		alert("blabla");
	}
	
	
	$scope.$on('closeConf',function(event, id) {
		try {
			alert("blabla");	
		} catch (e) {
			jqconsole.Write('ERROR : '+e.message + '\n');
		}
		
	});
	
	
}
