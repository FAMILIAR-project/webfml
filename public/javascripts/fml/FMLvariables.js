'use strict';

//, ['ngMaterial']);
// '$scope', '$rootScope', '$sce',

angular.module('fml').controller('FMLVariableCtrl', function ($scope, $rootScope, $sce) {

	$scope.varIDs = [];
	$scope.lastVar = '';
	$scope.configurator = '';



	$scope.fts = '';
	/*
	[
  {
    title: 'Computers',
    fts: [
      {
        title: 'Laptops',
        fts: [
          {
            title: 'Ultrabooks'
          },
          {
            title: 'Macbooks'
          }
        ]
      },

      {
        title: 'Desktops'
      },

      {
        title: 'Tablets',
        fts: [
          {
            title: 'Apple'
          },
          {
            title: 'Android'
          }
        ]
      }
    ]
  },

  {
    title: 'Printers'
  }

]; */

	$scope.infoconfiguration = '';

	$scope.ftselect = function (ft, configid) {


	      // TODO other kinds of status
		  if (ft.confstatus == 'selected')
			ft.confstatus = 'deselected';
		  else if (ft.confstatus == 'unselected')
			ft.confstatus = 'selected';
		  else // if (ft.confstatus == 'deselected')
				ft.confstatus = 'unselected';

		  $scope.infoconfiguration = '' + ft.title + " is now " + ft.confstatus;

          jsRoutes.controllers.WebFMLInterpreter.selection().ajax({

						data : JSON.stringify({ "confid": configid, "ft" : ft}),
						contentType : "application/json",
						processData: false,
						success : function(data) {

						$scope.configurator = data;
						$scope.fts = $scope.configurator["hfts"];
						console.log($scope.fts);
						$rootScope.$broadcast('variables', data);
          			},
          		        error : function(data) {
          				  		data["msgError"] = '<div class="alert alert-danger"><p>Unable to apply the configuration</p>' + data.responseJSON["msgError"] + '</div>';
          						$rootScope.$broadcast('variables', data);
          			},
          		        beforeSend : function(event, jqxhr, settings) {
          			        $('loader').html('<img src="../assets/images/ajax-loader.gif" />') ;
          			},
          		       complete : function(jqxhr, textstatus) {
          			    $('#loader').html('') ;
          		    }


         		 });

            // the state machine is discussable of course

	};

	$scope.displayVariable = function(id) {
		 jsRoutes.controllers.WebFMLInterpreter.variable(id).ajax({
				success : function(data) {
					$scope.lastVar = data;
					$scope.$apply();
				},
			        error : function(data) {
								$('#lastValueFML').html('Error... <div class="alert alert-danger">' + data + '</div>') ;
				},
			        beforeSend : function(event, jqxhr, settings) {
				        $('#loader').html('<img src="../assets/images/ajax-loader.gif" />') ;
				},
			       complete : function(jqxhr, textstatus) {
				    $('#loader').html('') ;
			       }
		 }) ;
	}



		$scope.configure = function(id) {
			 jsRoutes.controllers.WebFMLInterpreter.configure(id).ajax({
					success : function(data) {
						$scope.configurator = data;
						$scope.fts = $scope.configurator["hfts"];
						console.log($scope.fts);
					//	$scope.$apply();
						$rootScope.$broadcast('variables', data);
						// $('#configuratorsFML').html('<div><h3>Configurator</h3>' + data + '</div>') ;
					},
				  error : function(data) {

								//$scope.msgError =
								//$scope.$apply();
								data["msgError"] = '<div class="alert alert-danger">Unable to create a configurator: ' +
																		data.responseJSON["msgError"] + '</div>';
								$rootScope.$broadcast('variables', data);
					},
				        beforeSend : function(event, jqxhr, settings) {
					        $('#loader').html('<img src="../assets/images/ajax-loader.gif" />') ;
					},
				       complete : function(jqxhr, textstatus) {
					    $('#loader').html('') ;
				       }
			 }) ;
		}


	$scope.synthesize = function(id) {
		$rootScope.$broadcast('ksynthesis', 'ksynthesis --interactive ' + id)
	}



	$scope.$on('variables', function (event, data) {

		var varids = data["varIDs"];
		if (typeof varids !== 'undefined' && varids.length > 0) {
			$scope.varIDs = varids;
	    }

		var lvar = data["lastVar"];
		if (typeof lvar !== 'undefined' && lvar.length > 0) {
     		$scope.lastVar = lvar;
        }

		$scope.msgError = $sce.trustAsHtml(data["msgError"]);
		$scope.$apply();
	});



});
