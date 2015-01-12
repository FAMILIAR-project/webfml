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
1. Download eclipse Luna on [Xtext website](https://eclipse.org/Xtext/download.html)
2. Download the latest version of each jar from this [website](http://wwwiti.cs.uni-magdeburg.de/iti_db/research/featureide/deploy/plugins/) :
 * de.ovgu.featureide.core.jar
 * de.ovgu.featureide.fm.core.jar
 * de.ovgu.featureide.fm.ui.jar
 * de.ovgu.featureide.ui.jar
Put those jar files in the folder eclipse\dropins

![alt tag](https://raw.githubusercontent.com/FAMILIAR-project/webfml/Groupe_Viallatte_Sprint_2/resources/readmePicture/dropinFolder.PNG?token=AEbzWtH_FamQsvJvHstHMrbRaqdm_O-Pks5UvVJEwA%3D%3D)

3. Install Scala in your Eclipse
 * Help/Install new software...
 * work with :http://download.scala-ide.org/nightly-scala-ide-luna-211x
 * 
![alt tag](https://raw.githubusercontent.com/FAMILIAR-project/webfml/Groupe_Viallatte_Sprint_2/resources/readmePicture/eclipseInstallPlugin.PNG?token=AEbzWmu8dmvkeQic4wKgNUvv0dDz1ITwks5UvVK-wA%3D%3D)

![alt tag](https://raw.githubusercontent.com/FAMILIAR-project/webfml/Groupe_Viallatte_Sprint_2/resources/readmePicture/eclipseInstallPlugin2.PNG?token=AEbzWm3apaU9b7EjcIxhuRlysVvRtbR_ks5UvVN-wA%3D%3D)

 * select all -> next -> next -> accept the terms of the license agreements -> finish

##3. Play framework
1. Download [Play framework 2.2.4](https://www.playframework.com/download)
2. unzip it in a folder
3. you must have a read/write access in this folder and the path must not have a blank space or a special char
4. Add the play path to the environment variables (see How to add a path to the environment variables). The path must look like «C:\pathToYourFolder\play-2.2.4 »
5. Check if Play is correctly installed , shell prompt : >play help

![alt tag](https://raw.githubusercontent.com/FAMILIAR-project/webfml/Groupe_Viallatte_Sprint_2/resources/readmePicture/playHelp.PNG?token=AEbzWo9IgipJSx-geD0aB_gvbVPzZmgnks5UvVSCwA%3D%3D)

##4. Import the project Webfml
1. Open Eclipse
2. File -> import

![alt tag](https://github.com/FAMILIAR-project/webfml/blob/Groupe_Viallatte_Sprint_2/resources/readmePicture/javaImport.PNG)

3. Git/Projects from Git -> next

![alt tag](https://raw.githubusercontent.com/FAMILIAR-project/webfml/Groupe_Viallatte_Sprint_2/resources/readmePicture/javaImportGit.PNG?token=AEbzWrblz_q0Q6prJH9Mds2pXuU8E_9bks5UvVWmwA%3D%3D)

4. Clone URI -> next
5. URI : https://github.com/FAMILIAR-project/webfml
6. Authentification : enter your GitHub username and password -> next

![alt tag](https://raw.githubusercontent.com/FAMILIAR-project/webfml/Groupe_Viallatte_Sprint_2/resources/readmePicture/javaImportGit2.PNG?token=AEbzWs7o_AsCpUl8Tt3a0ZqVvgmZ1_rTks5UvVYOwA%3D%3D)

7. Select branch « master » only

![alt tag](https://raw.githubusercontent.com/FAMILIAR-project/webfml/Groupe_Viallatte_Sprint_2/resources/readmePicture/javaImportGit3.PNG?token=AEbzWv6KQv7Wc7S0ZD4rRuTsQlazVfOTks5UvVZDwA%3D%3D)

8. You can change where you want to import your project
9. When the download is over, select Import as general Project -> next

![alt tag](https://raw.githubusercontent.com/FAMILIAR-project/webfml/Groupe_Viallatte_Sprint_2/resources/readmePicture/javaImportGit4.PNG?token=AEbzWqM7w84FnnGaYs62UStaKjfjXWwiks5UvVarwA%3D%3D)

10. Next -> Finish

##5. Configure Eclipse with Scala
1. In the command console, change the directory to the Webfml directory (use the commande : >cd your webfml directory)
2. Execute : > play
3. Wait for the Play prompt $
4. Execute : $ eclipse

![alt tag](https://raw.githubusercontent.com/FAMILIAR-project/webfml/Groupe_Viallatte_Sprint_2/resources/readmePicture/playEclipse.PNG?token=AEbzWuq70iaHHabagWX4jm0xkef5QL6kks5UvVdJwA%3D%3D)

##6. Import the project Familiar-language
1. Open Eclipse
2. File -> import
3. Git/Projects from Git  next
4. Clone URI -> next
5. URI : https://github.com/FAMILIAR-project/familiar-language
6. Authentification : enter your GitHub username and password -> next
7. Select branch « master » only
8. You can change where you want to import your project
9. When the download is over, select Import existing Projects
10. **WARNING : Deselect the following projects**
  * **FML3rdPartiesOfSynthesis\bin**
  * **FML3rdPartiesMisc\bin**
  
![alt tag](https://raw.githubusercontent.com/FAMILIAR-project/webfml/Groupe_Viallatte_Sprint_2/resources/readmePicture/javaImportFamiliar.PNG?token=AEbzWrB-rIabGSPKx7P8qhjtmm3GTSOyks5UvVjtwA%3D%3D)

11. Finish

##7. Bugs‘ correction
At this step, you should see some errors. We are going to try to resolve them.
1. In this file : org.xtext.example.fml.tests\ META-INF\MANIFEST.MF
 * Delete the line 19 : « org.junit.runners » (twice in the file) (check issue #46 in familiar language project on GitHub)
 
![alt tag](https://raw.githubusercontent.com/FAMILIAR-project/webfml/Groupe_Viallatte_Sprint_2/resources/readmePicture/bugJUnitRunners.PNG?token=AEbzWgfTtyviBoYrMkV-FENeWAMtPIjiks5UvVluwA%3D%3D)

2. To resolve the '!' problem on FAMILIAR project :
 * right click on Familiar project/properties
 * java Build Path/Librairies
 * select JRE System Librairy and edit

![alt tag](https://raw.githubusercontent.com/FAMILIAR-project/webfml/Groupe_Viallatte_Sprint_2/resources/readmePicture/alternateJRE.PNG?token=AEbzWtPRsx2LzEFwiiSpLRqTqGots-ZPks5UvVn1wA%3D%3D)

 * alternate JRE : jre7

![alt tag](https://github.com/FAMILIAR-project/webfml/blob/Groupe_Viallatte_Sprint_2/resources/readmePicture/alternateJRE2.PNG)

 * Finish/OK
3. For the pom.xml errors
 * Choose 1 pom.xml with errors and open it
 * Hover on "parent" to display available solutions
 * Click on "discover new m2e connectors"

![alt tag](https://raw.githubusercontent.com/FAMILIAR-project/webfml/Groupe_Viallatte_Sprint_2/resources/readmePicture/bugPomXML.PNG?token=AEbzWqjo4Bof9Rn49UF0nqXXG69x0ionks5UvVqKwA%3D%3D)

![alt tag](https://raw.githubusercontent.com/FAMILIAR-project/webfml/Groupe_Viallatte_Sprint_2/resources/readmePicture/bugPomXML2.PNG?token=AEbzWt3V83-e289BImBvK-aQ6wun8WJSks5UvVq3wA%3D%3D)

 * Next -> I accept the terms of the license agreements -> finish
 * You should get a security warning, click ok
 * Restart Eclipse
4. To configure webfml with scala
 * Right click on webfml project
 * Scala -> Set the scala installation
 * Select latest 2.10 (dynamic)
5. Select all your projects and refresh

##8. Maven download
1. Download the latest version of [Maven apache](http://maven.apache.org/download.cgi)
2. unzip it
3. Add the Maven apache path in the environment variables (see How to add a path to the environment variables).
4. Create a new environment variable (see How to add a path to the environment variables)
 * name : JAVA_HOME 
 * path : PathOfYourFolder\jdk1.7.0_71\
5. To check if maven is correctly installed enter this command in your command console : >mvn –version

##9. Create the needed jar files to make the project runnable
1. In the command console, change your directory to your project familiar-language\familiar.root (using cd command)
2. Execute a command to create your jar files of each project from familiar : >mvn install
3. Change your directory to your project familiar-language\familiar.standalone
4. Execute the command : >mvn package
5. You have to get the generated jar file ‘ FAMILIAR-Standalone-1.0.10-jar-with-dependencies ’ in the familiar.standalone\target folder. Copy it in your webfml/lib project (this file should replace the old one).
6. Restart Eclipse

##10. Run the project
1. In the command console, change your path to webfml folder (using the command: cd)
2. Execute play :> play
3. Compile the project in play prompt :$compile
4. Run the project :$run
5. In your Internet browser enter: localhost:9000/ide/familiar

##11. How to add a path to the environment variables
1. Menu demarrer -> Panneau de configuration
2. In the search field search for “variables” and click on “modifier les variables d’environnement système”

![alt tag](https://raw.githubusercontent.com/FAMILIAR-project/webfml/Groupe_Viallatte_Sprint_2/resources/readmePicture/variablesEnvironnement.PNG?token=AEbzWufj_KddrQ2Mt8_JzQTE68kL2-40ks5UvVwXwA%3D%3D) 

3. Click on “variables d’environnement…” (1), search for the Path variable and click on “modifier…” (2) then add your folder path (3) end your path name with “;”

![alt tag](https://raw.githubusercontent.com/FAMILIAR-project/webfml/Groupe_Viallatte_Sprint_2/resources/readmePicture/variablesEnvironnement2.PNG?token=AEbzWiyNz2Ft_A9ccMev1I93wLe3VJSxks5UvVxMwA%3D%3D) 

![alt tag](https://raw.githubusercontent.com/FAMILIAR-project/webfml/Groupe_Viallatte_Sprint_2/resources/readmePicture/variablesEnvironnement3.PNG?token=AEbzWrobG_SzEYQBkpJoKXbpJHudq6-Fks5UvVxNwA%3D%3D) 

![alt tag](https://raw.githubusercontent.com/FAMILIAR-project/webfml/Groupe_Viallatte_Sprint_2/resources/readmePicture/variablesEnvironnement4.PNG?token=AEbzWrcYdnA0P_LOtdcufsNURpTQ9eyjks5UvVyDwA%3D%3D) 

You can also create a new environment variable

![alt tag](https://raw.githubusercontent.com/FAMILIAR-project/webfml/Groupe_Viallatte_Sprint_2/resources/readmePicture/variablesEnvironnement5.PNG?token=AEbzWvgTwUERn1u2s2cJhf32FPl0XI7Uks5UvVyEwA%3D%3D) 

#II. Used tools introduction
##1. Familiar
Familiar is a language used to manage (importing, exporting, composing, decomposing, editing, configuring, refactoring, reverse engineering, testing ) feature models and constraints. 
A feature model is a tree that represents all the possibility for a specific object. 
*Example : A website offers the possibility to create customized shoes. The customer can choose several options (color, height of the heels, shoelace, sole…). All of those choices build a conditional tree called feature model.*
With Familiar, we can manipulate these trees and the constraints between the different costumer’s choices. 
*Example : If the costumer selects shoes size 11, he may not have the possibility to choose thicker soles.*

##2. WebFML
The goal of WebFML is to view and manage a feature model using Familiar through a web interface.

##3. Scala
Scala is a programming language based on java language that inherits all java libraries. 
It gathers concepts from different languages to create an easier language to use. Scala is smaller in size because its syntax is very concise. Scala is more and more used by developers.

##4. Play
Play is a high-productivity Java and Scala web application framework that integrates the components and APIs you need for modern web application development. 
It allows to generate web pages easier without using JEE or Spring. It offers a lot of fonctionalities like a good bugs management , a quick compilation and several modules for data secure for example.

##5. Maven
Maven offers a standard way to build projects, a clear definition of what the project consists of, an easy way to publish project information and a way to share JARs across several projects. 
It allows a project to build using its project object model (POM) and a set of plugins that are shared by all projects using Maven, providing a uniform build system.

#III. Project architecture

![alt tag](https://raw.githubusercontent.com/FAMILIAR-project/webfml/Groupe_Viallatte_Sprint_2/resources/readmePicture/architecture.PNG?token=AEbzWqdCm12eu0mLo0ezJWNaGMpKHDfWks5UvV2PwA%3D%3D)

(1) Familiar is composed of several projects programmed in java used in WebFML

(2) Maven is used to gather all familiar projects into a jar file that will be added to WebFML project

(3) WebFML is programmed in Scala which is based on java language but it is lighter

(4) Play builds WebFML project

(5) Familiar GUI on the browser

Familiar web interface:

![alt tag](https://raw.githubusercontent.com/FAMILIAR-project/webfml/Groupe_Viallatte_Sprint_2/resources/readmePicture/interfaceWebFML.PNG?token=AEbzWtD71INKL_3G0pyoICidKEg_FdaJks5UvV3qwA%3D%3D)


  

