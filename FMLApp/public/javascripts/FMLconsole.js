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
 
			  // ranking lists
			  $('#ksynthesis').append('ranking list:')
			  var rl = data['rankinglist']
              $('#ksynthesis').append('<ul>')
              for (var i=0; i<rl.length; i++) {
            	  $('#ksynthesis').append('<li>' + rl[i].feature + ': ' + rl[i].parents + '</li>')
              }
              $('#ksynthesis').append('</ul>')
                         
              // clusters
              $('#ksynthesis').append('clusters:')
              $('#ksynthesis').append('<ul>')
              var clusters = data['clusters']
              for (var i=0; i<clusters.length; i++) {
            	  $('#ksynthesis').append('<li>' + clusters[i] + '</li>')
              }
              $('#ksynthesis').append('</ul>')
              
               // cliques
              $('#ksynthesis').append('cliques:')
              $('#ksynthesis').append('<ul>')
              var cliques = data['cliques']
              for (var i=0; i<cliques.length; i++) {
            	  $('#ksynthesis').append('<li>' + cliques[i] + '</li>')
              }
              $('#ksynthesis').append('</ul>')
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



