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

## Setting up Eclipse

 * Download Play! : http://www.playframework.com/
 * Install Play! as follows : e.g., http://www.playframework.com/documentation/2.2.1/Installing
 * In webfml directory, start play (in the console enter the command play), in Play! console enter the command ''eclipse'' : http://www.playframework.com/documentation/2.2.1/IDE

## Setting up the installation

```
Familiar build
--------------
 * git clone git@github.com:FAMILIAR-project/familiar-language.git
 * mvn clean install -DskipTests from familiar.root/
 * mvn clean install from familiar.standalone/

KSynthesis build
-----------------
 * git clone https://github.com/gbecan/FOReverSE-KSynthesis.git
 * mvn clean install from KSynthesis/

WebFML build
----------------
 * git clone git@github.com:FAMILIAR-project/webfml.git
 * install Play [https://www.playframework.com/download] 
 * mvn clean install -DskipTests -DPLAY2_HOME=/path/to/play-2.2.4/
```

You are ready to work
We need also to "manually" download FeatureIDE jars (or mavenize FeatureIDE):
http://wwwiti.cs.uni-magdeburg.de/iti_db/research/featureide/deploy/plugins/
```
wget http://wwwiti.cs.uni-magdeburg.de/iti_db/research/featureide/deploy/plugins/de.ovgu.featureide.core_2.7.5.201512021815.jar
and also:
 de.ovgu.featureide.fm.core_2.7.5.201512021815.jar
 de.ovgu.featureide.fm.ui_2.7.5.201512021815.jar
 de.ovgu.featureide.ui_2.7.5.201512021815.jar
 
 and finally: org.sat4j.core.jar
```

## Compile and Run

 * In your source directory open the terminal
 * start play (command : play)
 * to compile type the command compile 
 * to run type the command run
 * go to the url : localhost:9000
 

 
 
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

 




