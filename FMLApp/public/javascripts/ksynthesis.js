function KSynthesisCtrl($scope) {

	$scope.heuristics = ["Smith Waterman (Simmetrics)","Levenshtein (Simmetrics","Wu & Palmer (WordNet)","Path Length (WordNet)","Wikipedia Miner"];
    
    
	$scope.rankingLists = [];
	
	$scope.clusters = [];
	$scope.selectedCluster = {};
	$scope.clusterSelectedParent = {};
	$scope.clusterPossibleParents = [];
	$scope.clusterSelectedFeatures = [];
	
	$scope.cliques = [];

	
	
	$scope.$on('ksynthesis', function (event, command) {
		 try {
			 
			  jsRoutes.controllers.WebFMLInterpreter.ksynthesis(command).ajax({
	                      success : function(data) {
				  jqconsole.Write('Synthesising in progress... over ' + data['targetID'] + '\n');
				  $scope.updateSynthesisInformation(data)
				  
	              },
	              error : function(data) {
	            	  jqconsole.Write('Error...' + data + '\n');
                  },
                  beforeSend : function(event, jqxhr, settings) {
                	  $('#wait').html('<img src="assets/images/ajax-loader.gif" />') ;
                  },
	              complete : function(jqxhr, textstatus) {
	            	  $('#wait').html('') ;		   
	              }
			  });
			  
		 } catch (e) {
			 jqconsole.Write('ERROR: ' + e.message + '\n');
		 }
	              
	 });
	
	$scope.selectParent = function (child, parent) {
		if (child!=parent) {
			jsRoutes.controllers.WebFMLInterpreter.selectParent(child, parent).ajax({
		         success : function(data) {
		        	$scope.updateSynthesisInformation(data)
		         },
		         error : function(data) {
		        	 jqconsole.Write('Error...' + data + '\n');
		         },
		         beforeSend : function(event, jqxhr, settings) {
		        	 $('#wait').html('<img src="assets/images/ajax-loader.gif" />') ;
		         },
		         complete : function(jqxhr, textstatus) {
		        	 $('#wait').html('') ;		   
		         }
			 })	
		}
	};
	
	$scope.selectCluster = function (cluster) {
		$scope.selectedCluster = cluster;
		$scope.clusterSelectedFeatures = cluster.slice();
		$scope.clusterPossibleParents = getCommonParents($scope.clusterSelectedFeatures, $scope.rankingLists);
	};
	
	$scope.selectClusterFeature = function (feature) {
		var idx = $scope.clusterSelectedFeatures.indexOf(feature);

	    // is currently selected
	    if (idx > -1) {
	      $scope.clusterSelectedFeatures.splice(idx, 1);
	    }

	    // is newly selected
	    else {
	      $scope.clusterSelectedFeatures.push(feature);
	    }
	    
	    $scope.clusterPossibleParents = getCommonParents($scope.clusterSelectedFeatures, $scope.rankingLists);
	}
	
	$scope.selectClusterParent = function (cluster, parent) {
		for (var i = 0; i < cluster.length; i++) {
			$scope.selectParent(cluster[i], parent);
		}
	}
	
	$scope.updateSynthesisInformation = function (data) {
		  $scope.rankingLists = data['rankingList'];
		  $scope.clusters = data['clusters'];
		  $scope.cliques = data['cliques'];
		  var fm = data['fm'] ;
		  mkFMPreview('#fmpreview', fm);
		  $scope.$apply();
	};
	
	$scope.completeFM = function() {
		jsRoutes.controllers.WebFMLInterpreter.completeFM().ajax({
	         success : function(data) {
	        	$scope.updateSynthesisInformation(data)
	         },
	         error : function(data) {
	        	 jqconsole.Write('Error...' + data + '\n');
	         },
	         beforeSend : function(event, jqxhr, settings) {
	        	 $('#wait').html('<img src="assets/images/ajax-loader.gif" />') ;
	         },
	         complete : function(jqxhr, textstatus) {
	        	 $('#wait').html('') ;		   
	         }
		 });
	};
}

function getCommonParents(features, rankingLists) {
	var commonParents = [];
	
	if (features.length > 0) {
		// get the possible parents of the first feature
		commonParents = getParentCandidates(features[0], rankingLists);

		// iterate through the other features and compute the intersection of possible parents
		for (var i = 1; i < features.length; i++) {
			var parentCandidates = getParentCandidates(features[i], rankingLists);
			var newCommonParents = []
			for (var j = 0; j < parentCandidates.length; j++) {
				if(commonParents.indexOf(parentCandidates[j]) > -1) {
					newCommonParents.push(parentCandidates[j]);
				}
			}
			commonParents = newCommonParents;
		}
	}
    
    return commonParents;
}

function getParentCandidates(feature, rankingLists) {
	var parentCandidates = [];
	for (var i = 0; i < rankingLists.length; i++) {
		if (rankingLists[i].feature == feature) {
			parentCandidates = rankingLists[i].parents.slice();
			break;
		}
	}
	parentCandidates.push(feature);
	return parentCandidates;
}

function mkFMPreview(divid, fm) {
	$(divid).html('');
	
	// Create the graph
    var g = new dagreD3.Digraph();
    
    for (var i=0; i < fm.nodes.length; i++) {
    	g.addNode(fm.nodes[i], {label: fm.nodes[i]});
    }
    
    for (var i=0; i < fm.edges.length; i++) {
    	var edge = fm.edges[i]
    	g.addEdge(null, edge.source, edge.target, {});
    }
    
    // Render
    var layout = dagreD3.layout()
    .rankDir("BT")
    .nodeSep(25);
    
    var graph = d3.select(divid)
	.append('svg')
	
	graph.attr('height', '300');
	
	graph.append('g')
		.attr('transform', 'translate(20,20)');
    
    var renderer = new dagreD3.Renderer();
    renderer.layout(layout).run(g, d3.select("svg g"));
    
    // Change style
    d3.select('defs')
    	.append('svg:marker')
    		.attr('id', 'marker-mandatory')
    		.attr('markerWidth', '10')
    		.attr('markerHeight', '10')
    		.attr('refX', '5')
    		.attr('refY', '5')
    		.append('svg:circle')
    			.attr('cx', '5')
    			.attr('cy', '5')
    			.attr('r', '3')
    			.attr('stroke', 'black')
    			.attr('stroke-width', '1')
    			.attr('fill', 'black');
    
    d3.select('defs')
		.append('svg:marker')
			.attr('id', 'marker-optional')
			.attr('markerWidth', '10')
			.attr('markerHeight', '10')
			.attr('refX', '5')
			.attr('refY', '5')
			.append('svg:circle')
				.attr('cx', '5')
				.attr('cy', '5')
				.attr('r', '3')
				.attr('stroke', 'black')
				.attr('stroke-width', '1')
				.attr('fill', 'white');
  

    d3.selectAll('.edge path').attr('marker-start', '') // url(#marker-optional)
    d3.selectAll('.edge path').attr('marker-end', '')
    
    graph.call(d3.behavior.zoom().on("zoom", function() {
        var ev = d3.event;
        graph.select("g")
          .attr("transform", "translate(" + ev.translate + ") scale(" + ev.scale + ")");
      }));
}