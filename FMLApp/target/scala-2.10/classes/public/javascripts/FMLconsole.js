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

                          // ranking lists
                          var rl = data['rankinglist'] // TODO
                          $('#ksynthesis').html('<em>' + rl + '</em>')
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



