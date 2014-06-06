// console handling 
// FIXME updating the UI

function FMLConsoleCtrl($scope, $rootScope) {
	
//}
//$(function() {

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
            	  $rootScope.$broadcast('ksynthesis', command);
              }
              /*
              GENERAL case
              */
              else {
		  try {
		      
		      jsRoutes.controllers.WebFMLInterpreter.evalPrompt(command).ajax({
			  success : function(data) {
			      jqconsole.Write(data['lastVar'] + '\n');
			      $rootScope.$broadcast('variables', data)
			  },
			  error : function(data) {
			      jqconsole.Write('Error...' + data['lastVar'] + '\n');
			      
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
//      });
}
