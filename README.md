Presentation of WebFML project
==============================================


If you’re familiar with FAMILIAR, then WebFML is basically just an online IDE for FAMILIAR development. It allows you to create, modify and execute your favorite feature models in real time.

This is a web text editor, interacting through Javascript and AJAX requests with a FAMILIAR virtual machine on a Java / Scala server. So the main process is executed on the server rather than the browser, making WebFML a really lite web application.

## Current status
We have a basic version of FAMILIAR environment with
* a textual editor (very basic) for specifying scripts
* a console to interact (very basic again) - see the console paragraph
* way to execute a script
* way to reset
* (partially) the logics for handling a "ksynthesis" session

It works with the Play! framework 2.2.0 (http://www.playframework.com/documentation/2.2.0), the Scala version. We also rely on some Javascripts (ACE editor and jqconsole). More details in the dedicated page.




## Technical architecture

The server-side is based  on a Java / Scala web application including the Play Framework. 

Play allow us to take care of all the technical details inherent to a web application. Like MVC architecture, redirections, request status, etc.

For more informations about play : https://www.playframework.com/

The server application is interacting with a familiar virtual machine, which is executing the code received from the browser text editor and keeping in memory the feature models created, so you can display / modify them when you want.





### Class diagram :

![Alt text](/doc/img/general_architecture_zoom.png)


The client-side of WebFML is based on Javascript, with the AngularJS framework. It is basically just interacting with our server, sending him AJAX requests to execute, display, modify Feature Models.

Important controllers can be found in /public/javascripts/fml

Checkout the working schema :


![Alt text](/doc/img/familiar.jpg)






## Use-case example

Here's a little example of what happens when you perform an action on the web interface (here, in the console part).


![Alt text](/doc/img/application_working.png)




Clicking on “Display” calls the displayVariable() method of the FMLVariables.js class (located in the public/javascripts/fml package), which results in the calling of the variable() function in the java class WebFMLInterpreter, through an Ajax request.

This function uses a class from the Familiar API to obtain the description on the variable passed as a an argument.

Then, the response is passed to the javascript controller, which displays this description in the console.

![Alt text](/doc/img/application_working_2.png)




Installation
=======================================================================


# WebFML - INSTALL


## Software Install :


-The first step consists of installing Eclipse Luna with pre-implemented Xtext (https://eclipse.org/Xtext/download.html), which makes domain-specific languages building and programming easier (our installation was made under JRE/JDK versions 1.7.0_71 and 1.7.0_72).
### FeatureIDE from : 
http://wwwiti.cs.uni-magdeburg.de/iti_db/research/featureide/deploy/plugins/

Copy these plugins (take the latest version) in the “dropins” folder of your Eclipse installation:
* .de.ovgu.featureide.core
* .de.ovgu.featureide.fm.core
* .de.ovgu.featureide.fm.ui
* .de.ovgu.featureide.ui

### Scala :
	Help > Install new software
![Alt text](/doc/img/1_install_scala.png)


Click on “add” :

![Alt text](/doc/img/2_install_scala.png)

In the location field, paste the following link:
http://download.scala-ide.org/nightly-scala-ide-luna-211x
Select all of the three available items in the window that appears next :

![Alt text](/doc/img/3_install_scala.png)



# Download the framework PLAY :
http://downloads.typesafe.com/play/2.2.4/play-2.2.4.zip
In the event of a “Java JDK cannot be found” error, an environment variable named “JAVA_HOME” needs to be initialized (Create a new system variable with the path to the Java install repertory such as “C:\Program Files\Java\jdkx.x.x_xx\”)
Extract the content of the zip file in any repertory of your choice then launch the “activator.bat” command.

# Download MAVEN version 3.1.1 :
http://maven.apache.org/download.cgi
* Create the folder : “Apache Software Foundation” in C:/Program Files/
* Unzip the Maven archive in C:/Program Files/Apache Software Foundation/
* Create a new system variable “M2_HOME” with the value :
C:\Program Files\Apache Software Foundation\apache-maven-3.11.
* Create a new system variable  “M2” with the value :%M2_HOME%\bin
* Add %M2% to the variable “PATH”
* make sure that the system variable “JAVA_HOME” has C:\Program Files\Java\jdk1.7.0_71 as value (or 1.7.0_72)
* In the command-line interface, launch the “mvn -version” command to make sure that Maven has been successfully installed and all new system variables are found and properly used.


# Familiar - INSTALL

Clone the familiar project in Eclipse : https://github.com/FAMILIAR-project/familiar-language
You get access to the git perspective by clicking on the button on the top right and choosing “Git”, then click on “Clone a Git repository” :

![Alt text](/doc/img/4_install_familiar.png)


Choose only Master branch :

![Alt text](/doc/img/5_install_familiar.png)


With a right-click on the familiar-language local repository, choose “Import projects” with the “Importing existing projects” option selected

![Alt text](/doc/img/6_install_familiar.png)


In the list of projects to import, the “FML3rdPartiesMisc” and “FML3rdPartiesForSynthesis“ projects are copied twice with one the paths containing the other one, so make sure you only take each of these projects once.






## WebFML :

To clone the WebFML project in Eclipse, we proceed as we did above with the FAMILIAR project; except that in step 3, the “Import as general project” option is the one needed.

##How to generate the Jars file :
 
* A Maven build has been added to the webfml folder, verify that there’s a pom.xml file in the project.
* In the command-line inteface, go to familiar-language\familiar.root and launch the  mvn install command.
* Do the same with familiar-language\familiar.standalone and mvn package respectively
* Copy the FAMILIAR-Standalone-1.0.10-jar-with-dependencies file from familiar-language\familiar.standalone\target to webfml\lib (replace if needed)


## Run the Application :

* Run the consol with (“cmd”) in file search assistant on Windows.
* Be placed on the webfml folder, execute "play"
![Alt text](/doc/img/play.png)
* Execute command : "compile"
![Alt text](/doc/img/play-compile.png)
* Launch the app with command : "run"
* In your browser go to: http://127.0.0.1:9000/ide/familiar
 

