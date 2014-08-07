/**
 *This page contains some options about the editor
 *more preciasly the theme of the editor. This theme define
 *color use in the editor.
 *
 *We can find the mode of the editor, here we have the familiar mode
 *but tou have just to change the name of the mode to load your language.
 */
var editor=ace.edit("editor");
window.onload = function() {
    /*
    *define the "mode" of the editor (e.g: color) you can change the value here (e.g : chrome, github...).
    *To find more theme you : ace-build-master/src-no-conflict/theme-xxx, you have just to put the xxx.
    */
    editor.setTheme("ace/theme/github");
    /*
    *define the mode of the editor with a special hilight
    *and for the correct language
    */
    var JavaScriptMode = ace.require("ace/mode/"+language+"").Mode;
    /*
    *Set the mode to the editor
    */
    editor.getSession().setMode(new JavaScriptMode());
};