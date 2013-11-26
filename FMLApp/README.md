# webfml (Play application)

It works with the Play! framework 2.2.0 (http://www.playframework.com/documentation/2.2.0). 
Specifically the Scala version.

Currently, we have a basic version of the FAMILIAR environment:

 * a textual editor (very basic) for specifying scripts => we rely on the ACE project (http://ace.c9.io/#nav=about)
 * a console to interact (very basic again) => we rely on the jq-console project (https://github.com/replit/jq-console/) 
 * way to execute a FAMILIAR script
 * way to reset the "variables environment" 
 * (partially) the logics for handling a "ksynthesis" session
 
## Eclipse, Scala, Play, FAMILIAR, etc

You need Eclipse, FAMILIAR installed, Scala support and Play! 

### Execution 

Checkout the FMLApp project. 
This is simply an Eclipse projet.
You obviously need an Eclipse with a Scala plugin (http://scala-ide.org/). We are using version 2.10.2. 

Then you need to generate a "jar" of FAMILIAR (i.e., you need FAMILIAR). 
For doing that, right click on the project FAMILIAR, Export, Runnable Jar File...
As destination set: 
FMLApp/lib/FML-1.2.jar
and select "Extract required libraries into generated JAR" 


Once done, you can execute play in the directory of FMLApp 
 * (optional) set up the preferences for your IDE (Eclipse, IntelliJ, etc.) => http://www.playframework.com/documentation/2.2.1/IDE
 * compile the project and run the server

```
macher:FMLApp macher1$ pwd
/Users/macher1/git/webfml/FMLApp
macher:FMLApp macher1$ ~/Downloads/play-2.2.0/play
[FMLApp] $ eclipse
...
[FMLApp] $ ~run

--- (Running the application from SBT, auto-reloading is enabled) ---

[info] play - Listening for HTTP on /0:0:0:0:0:0:0:0:9000

(Server started, use Ctrl+D to stop and go back to the console...)

[info] Compiling 9 Scala sources and 1 Java source to /Users/macher1/git/webfml/FMLApp/target/scala-2.10/classes...
[success] Compiled in 12s
```
(~ is for having a compilation every time you modify the source) 

Then you can check that it works:
http://127.0.0.1:9000/



## Organization of the code

For those unfamiliar with a Play application:
 * app/controllers define classes that are likely to compute complex stuff (here resides the FAMILIAR interpreter for instance)
 * app/views define the views (templates like, mix of HTML/CSS/JS and Scala) 
 * public/javascripts JS files that are managing the UI interactions and handle the AJAX requests
  * FMLconsole.js  (for the interaction with the console)
  * FML-callback.js (for the ACE editor)

We use Bower to download the last versions of the Javascripts libraries (ACE, bootstrap, jquery-migrate, etc.) 
http://bower.io/
```
MacBook-Pro-de-Mathieu-3:javascripts macher1$ bower install jquery-migrate
bower install bootstrap
bower install ace
``` 
 
As  the browser method of jq-console is deprecated (see http://api.jquery.com/jQuery.browser/) https://github.com/jquery/jquery-migrate/ is also needed
 
 
We cannot use Bower for getting jq-console:
 
```
 git clone https://github.com/replit/jq-console
```

 




