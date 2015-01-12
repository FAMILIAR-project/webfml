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

#I. Installation
##1. Java environment

1. Download [JRE 7](http://www.oracle.com/technetwork/java/javase/downloads/java-se-jre-7-download-432155.html) and [JDK 7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html) (it must be the version 7)
2. Check if java is installed : shell prompt : >java –version
If it doesn’t work, you have to add the java path to environment variables (see How to add a path to the environment         variables). The java path must look like « C:\Program Files\Java\jdk1.7.0_71\bin »
3. Check if javac is correctly installed : shell prompt : >javac –version
If it doesn’t work, you have to add the javac.exe path to environment variables (see How to add a path to the environment    variables). The javac.exe path must look like « C:\Program Files\Java\jdk1.7.0_71\bin »
![alt tag](https://raw.githubusercontent.com/FAMILIAR-project/webfml/Groupe_Viallatte_Sprint_2/resources/readmePicture/cmdJavaVersion.PNG?token=AEbzWiXWuRJZ4L55v2LfXP1KQbo6Pbeiks5UvU-CwA%3D%3D)

##2. Eclipse environment
1. Download eclipse Luna on Xtext website
2. Download the latest version of each jar from this website :
* de.ovgu.featureide.core.jar
* de.ovgu.featureide.fm.core.jar
* de.ovgu.featureide.fm.ui.jar
* de.ovgu.featureide.ui.jar
Put those jar files in the folder eclipse\dropins



