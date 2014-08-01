/*
 *@author
 *This page define the familiar mode
 *
 */

define(function(require, exports, module){
    "use strict";
    var oop=require("../lib/oop");
    var JavaScriptMode = require("./javascript").Mode;
    var FamiliarHighlightRules = require("./familiar_highlight_rules").FamiliarHighlightRules;
    
    
    var Mode = function(){
        JavaScriptMode.call(this);
        this.HighlightRules = FamiliarHighlightRules;
    };
    oop.inherits(Mode, JavaScriptMode);
    
    (function() {
        
        this.createWorker = function(session){
            return null;
        };
        
        this.$id="ace/mode/familiar";
    }).call(Mode.prototype);
    
    exports.Mode=Mode;   
});
