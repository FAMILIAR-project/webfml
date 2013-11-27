function KSynthesisCtrl($scope) {

	$scope.rankingLists = [];
	$scope.clusters = [];
	$scope.cliques = [];
	
	$scope.$on('ksynthesis', function (event, command) {
		 try {
			 
			  jsRoutes.controllers.WebFMLInterpreter.ksynthesis(command).ajax({
	                      success : function(data) {
				  jqconsole.Write('Synthesising in progress... over ' + data['targetID'] + '\n');
				  
				  $scope.rankingLists = data['rankingList'];
				  $scope.clusters = data['clusters'];
				  $scope.cliques = data['cliques'];
				  var fm = data['fm'] ;
				  mkFMPreview('#fmpreview', fm);

				  $scope.$apply();
				  
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
	
	
	$scope.selectParent = function selectParent(child, parent) {
		 jsRoutes.controllers.WebFMLInterpreter.selectParent(child, parent).ajax({
	         success : function(data) {
	        	 mkFMPreview('#fmpreview', data)
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
    
    d3.select(divid)
	.append('svg')
	.append('g')
		.attr('transform', 'translate(20,20');
    
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
}