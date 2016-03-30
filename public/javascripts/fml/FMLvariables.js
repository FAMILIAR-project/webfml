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

		  /**
          * private method for updating values (confstatus) in $scope.fts
          * h is the JSON response
          * basic traversal of h (starting from "root" h[Ø]) and then we update fts
          * (again, we start the traversal with the root $scope.fts[0]
          *
          **/
          $scope.updateConfValues = function (h) {
          	 $scope.updateConfFt(h[0]);

          };

          $scope.updateConfFt = function (ft) {
            $scope.updateFt (ft); // update the feature itself
            var ch = ft.fts; // and then the children (if any)
            for (var c in ch) {
              $scope.updateConfFt(ch[c]);
            }
          };

          $scope.updateFt = function (ft) {
            $scope.seekAndUpdate(ft, $scope.fts[0]); // we seek the corresponding feature in $scope.fts
          };

          $scope.seekAndUpdate = function (ft, ft2) {
              if (ft2.title == ft.title)
                 ft2.confstatus = ft.confstatus;
              else {
                var ch2 = ft2.fts;
                for (var c in ch2) {
                  $scope.seekAndUpdate(ft, ch2[c]);
                }
              }
          };
          // end of conf updates

          jsRoutes.controllers.WebFMLInterpreter.selection().ajax({

						data : JSON.stringify({ "confid": configid, "ft" : ft}),
						contentType : "application/json",
						processData: false,
						success : function(data) {

						$scope.configurator = data;
						//$scope.fts = $scope.configurator["hfts"]; // OK but can change the hierarchy; what we want is to change the configuration status of each feature
						$scope.updateConfValues($scope.configurator["hfts"]);


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
