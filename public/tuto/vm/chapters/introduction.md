##Basic Variability Modeling  

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
