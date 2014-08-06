##Basic Variability Modeling  

Welcome to the tutorial part of the familiar project's webiste.
This part will explain how the language works.
For a better comprehension of the language you have an editor
on the right part to execute some code. to execute your code you have
just to click on the button **execute Familiar code**.

You will find some code part, like this below, to see the result of
the code.
```vm
//I'm a code part
```
You have to click on the button **Run** to put he code
on the editor.


###A first exemple


```vm

```

####1. Requirements

    Install the VM plugins in Eclipse and have at least a basic understanding of mobile phones.
    Create a vm file inside a new project

####2. Introduction

This tutorial will teach you the basic constructs provided by the VM language to model the variability of a configurable system or a family of products. You could read only this section 3.4. ("Final model") and skip the next sections if you already master basic boolean feature modeling.
####3. The mobile phones family

The example that we will follow during this tutorial is a family of very basic mobile phones [1] implemented as a product line. Each member of this family is a particular mobile phone characterized by an unique combination of features that distinguishes it from other members. This family presents the six situations that we encoded in VM:
####3.1. Basic model information

The name is the only required information for the mobile phone family. Optionally, we can include the more information using the tags: '@version', '@description', '@author', '@email', '@organization', '@publication', and '@date'.

In VM: @name "Mobile Phone"
####3.2. Relationships

Next, we will analyze the dependencies between the features of the mobile phone family and encode that information in VM.

    All phones include support for calls.

In VM: Mobile_Phone { Calls }

    Displaying information in a mobile phone is shown using either a basic, color, or high resolution screen.

In VM: Screen { oneOf { Basic Colour High_resolution } }

    Software for mobile phones may optionally include support for GPS and multimedia devices.

In VM: ? GPS ? Media

    Software for mobile phones include multimedia devices such as camera, MP3 player or both of them.

In VM: ? Media { someOf { Camera MP3 } } The four situations described before are represented as relationships of dependency between mobile phone family features. Therefore, the relationships: block for our example is:

In VM: Relationships: Mobile_Phone { Calls ? GPS Screen { oneOf { Basic Colour High_resolution } } ? Media { someOf { Camera MP3 } } }
####3.3. Basic Constraints

There are other relationships between features that may be difficult to express using the patterns shown in the previous subsection.

    Mobile phones including a camera must include support for a high resolution screen.

In VM: Camera requires High_resolution

    GPS and basic screen are incompatible features.

In VM: GPS excludes Basic

The two situations described before are represented as constraints between mobile phone family features. Therefore, the constraints: block for our example is:

In VM: Constraints: Camera requires High_resolution GPS excludes Basic
####3.4. Final model

The VM editor can help to write the same information encoded in Sections 3.1 to 3.2 in a more readable way. The next figure shows three blocks in VM, model information, relationships, and constraints.

```vm
@name "Mobile Phone"
Relationships:
Mobile_Phone{
	Calls
	?GPS
	SCREEN{
		oneOf{
			Basic
			Colour
			High_Resolution
		}	
	}
	? Media{
		someOf{
			camera
			MP3
		}
	}
}

Constraints:
GPS excludes Basic
Camera requires High_Resolution
```

[1] David Benavides, Sergio Segura and Antonio Ruiz Cortés. Automated Analysis of Feature Models 20 Years Later: A Literature Review (http://dx.doi.org/10.1016/j.is.2010.01.001). Information Systems. Elsevier. 2010. 35(6). September 2010.
