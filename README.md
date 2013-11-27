# webfml


FAMILIAR goes to the web... 
The goal is to progressively migrate our (unrelated) tools to the web. 
The long term vision is to have an integrated solution at the end, incl.
 * reverse engineering, 
 * testing, 
 * advanced feature modeling 
 * model-based product lines.

Promises ? 

 * Better integration of tools
 * Ease of development (especially user interfaces)
 * Usable environements 
 * More visible impact 
 * Deployment facilities 

For all participants: experience with web dev. 

## Current status
 
 We have a basic version of FAMILIAR environment with 
  * a textual editor (very basic) for specifying scripts
  * a console to interact (very basic again)
  * way to execute a script
  * way to reset
  * (partially) the logics for handling a "ksynthesis" session 

It works with the Play! framework 2.2.0 (http://www.playframework.com/documentation/2.2.0), the Scala version. We also rely on some Javascripts (ACE editor and jqconsole). 
More details in the dedicated page. 

## Setting up in Eclipse

 * Download play : http://www.playframework.com/
 * Install play as follow : http://www.playframework.com/documentation/2.2.1/Installing
 * In webfml directory, start play (in the console enter the command play), in play enter the command eclipse : http://www.playframework.com/documentation/2.2.1/IDE
 * In the eclipse webfml project, create a folder lib
 * Export a runnable jar from this project  : https://github.com/FAMILIAR-project/familiar-language  in this destination set: FMLApp/lib/FML-1.2.jar and select "Extract required libraries into generated JAR"
 * You are ready to work

## Compile and Run

 * In your source directory open the terminal
 * start play (command : play)
 * to compile type the command compile 
 * to run type the command run
 * go to the url : localhost:9000
 
