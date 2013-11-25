// console handling 
// FIXME updating the UI
$(function() {
        // Creating the console.
        var header = 'FAMILIAR interpreter\n';
        window.jqconsole = $('#console').jqconsole(header, 'fml> ');

        // Abort prompt on Ctrl+Z.
        jqconsole.RegisterShortcut('Z', function() {
          jqconsole.AbortPrompt();
          handler();
        });
        
        // Move to line start Ctrl+A.
        jqconsole.RegisterShortcut('A', function() {
          jqconsole.MoveToStart();
          handler();
        });
        
        // Move to line end Ctrl+E.
        jqconsole.RegisterShortcut('E', function() {
          jqconsole.MoveToEnd();
          handler();
        });
        
        jqconsole.RegisterMatching('{', '}', 'brace');
        jqconsole.RegisterMatching('(', ')', 'paran');
        jqconsole.RegisterMatching('[', ']', 'bracket');
        // Handle a command.
        var handler = function(command) {
          if (command) {

             
              if (command.indexOf('ksynthesis --interactive') != -1) {
             
              try {
        
		  jsRoutes.controllers.WebFMLInterpreter.ksynthesis(command).ajax({
                      success : function(data) {
			  jqconsole.Write('Synthesising in progress... over ' + data['targetID'] + '\n');

              // ranking lists, clusters, previsualization here!
			  $('#ksynthesis').html('') // reset display
 
			  // preview
			  $('#ksynthesis').append('<div id="fmpreview" class="pull-right"><h3>Preview</h3>');
			  var fm = data['fm'] ;
			  mkFMPreview('#fmpreview', fm);
			  $('#ksynthesis').append('</div>');
			  
			  // ranking lists
			  $('#ksynthesis').append('<div id="rklist"><h3>Ranking list</h3>');
			  var rl = data['rankingList'] ;
	//		  mkTreeRK ('#ksynthesis', rl);             no time to fix...
			  $('#rklist').append('<ul>');
	      for (var i=0; i<rl.length; i++) {
                  
            	  $('#rklist').append('<li class="parentFt">' + rl[i].feature + '</li><ul>'); 
		  var pts =  rl[i].parents ; 
                  for (var j=0; j < pts.length; j++) {  
                      $('#rklist').append('<li style="list-style: none; padding-left: 5em; ">' + mkFeatureMenu(rl[i].feature, pts[j]) + '</li>'); 
                  }
                  $('#rklist').append('</ul>');
              }
			  $('#rklist').append('</ul>');
			  $('#ksynthesis').append('</div>');
                         
              // clusters
			  $('#ksynthesis').append('<h3>Clusters</h3>');
			  $('#ksynthesis').append('<ul>');
			  var clusters = data['clusters'];
              for (var i=0; i<clusters.length; i++) {
            	  $('#ksynthesis').append('<li>' + clusters[i] + '</li>');
              }
			  $('#ksynthesis').append('</ul>');
              
               // cliques
			  $('#ksynthesis').append('<h3>Cliques</h3>');
			  $('#ksynthesis').append('<ul>');
			  var cliques = data['cliques'];
              for (var i=0; i<cliques.length; i++) {
            	  $('#ksynthesis').append('<li>' + cliques[i] + '</li>');
              }
			  $('#ksynthesis').append('</ul>');
			  
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

		  
		  
              } catch (e) {
		  jqconsole.Write('ERROR: ' + e.message + '\n');
              }
                  
	      }
              /*
              GENERAL case
              */
              else {
		  try {
		      
		      jsRoutes.controllers.WebFMLInterpreter.evalPrompt(command).ajax({
			  success : function(data) {
			      jqconsole.Write(command + ' ===> ' + data + '\n');
			      // FIXME: update the UI and the list of variables for instance
			      // FIXME: JSON from the webserver that decouples two aspects: (1) the last variable value (2) the list of variables
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

		      
		      
		  } catch (e) {
		      jqconsole.Write('ERROR: ' + e.message + '\n');
		  }
	      }
          }
          jqconsole.Prompt(true, handler, function(command) {
        	  
             // Continue line if can't compile the command.
            
            try {
              Function(command);
            } catch (e) {
            	
            /*
              if (/[\[\{\(]$/.test(command)) {
                return 1;
              } else {
                return 0;
              }*/
            }
            return false;
          });
        };

        // Initiate the first prompt.
        handler();
      });

// divid: id of the element
// chspecification: children specification of the tree
function mkTreeRK(divid,chspecification) {
$(divid).html('');

YUI().use(
  'aui-tree-view',
  function(Y) {
    var tview = new Y.TreeViewDD(
      { 
        boundingBox: divid,
	  children: chspecification, 
	  after: {
	       lastSelectedChange: function(event) {
		   var nodeId = event.newVal.get('id');
		   var node = tview.getNodeById(nodeId); 
                   if (!node.isLeaf())
		       return ; 
                   var parentNode = node.get('parentNode') ; 
	           // display a contextual menu	
//		  Y.one(divid).placeAfter() ; 

/*
*/
// '<p>Select ' + node.get('label')  + ' as parent of ' + parentNode.get('label') + '?</p>');
	        },
	  }
      } 
    ) ; 


    tview.render();
  }
);
}

function mkFeatureMenu(child, parent) {
	return ''+
	'<div class="btn-group">' +
		'<button class="btn btn-default btn-xs dropdown-toggle customBtn" type=\"button\" id=\"dropdownMenu1\" data-toggle=\"dropdown\">' + 
			parent + 
			'<span class=\"caret\"></span>'+
		'</button>'+
		'<ul class="dropdown-menu pull-right" role=\"menu\" aria-labelledby=\"dropdownMenu1\">'+
			'<li role=\"presentation\">'+
				'<a role=\"menuitem\" tabindex=\"-1\" href=\"#\">'+
					'Select this parent'+
				'</a>'+
			'</li>'+
			'<li role=\"presentation\">'+
				'<a role=\"menuitem\" tabindex=\"-1\" href=\"#\">Ignore this parent</a>'+
			'</li>'+
			'<li role=\"presentation\" class=\"divider\"></li>'+
			'<li role=\"presentation\">'+
				'<a role=\"menuitem\" tabindex=\"-1\" href=\"#\">Cancel</a>'+
			'</li>'+
		'</ul>'+
	'</div>'

}


function mkFMPreview(divid, fm) {

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

function selectParent(child, parent) {
	 jsRoutes.controllers.WebFMLInterpreter.selectParent(child, parent);
}
