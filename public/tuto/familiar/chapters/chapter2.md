# Composing your Compositions of Variability Models

This document presents: 
 * a comprehensive tutorial on feature model composition, showing the equivalence of various operators and mechanims offered by the FAMILIAR language
 * numerous examples (toy examples or based on the revisit of existing works)

The associated FAMILIAR scripts are located in the [git repository](../scriptsRepository/composition/) of the FAMILIAR scripts' repository. 

There is an [appendix section](#appendix) at the end of the document that demonstrates FAMILIAR sessions when executing the scripts.

Our ultimate goal is to provide solutions that fulfill the various needs of variability model composition.

##### Authors

 * Mathieu Acher (University of Rennes 1, Inria / Irisa, Triskell team)
 * Benoit Combemale (University of Rennes 1, Inria / Irisa, Triskell team)
 * Philippe Collet (University of Nice Sophia Antipolis)
 * Olivier Barais (University of Rennes 1, Inria / Irisa, Triskell team)
 * Philippe Lahire (University of Nice Sophia Antipolis)
 * Robert B. France (Colorado State University)

## A first example 

Let us consider the following FAMILIAR script: 
```familiar
MacBook-Pro-de-Mathieu-3:MODELS13 macher1$ cat testMODELSFirstExample.fml
fm1 = FM (S : [F1] F2 [F4] ; F2 : (F5|F6) ; ) // F1, F4 optionals, F5 and F6 mutually exclusive 
fm2 = FM (S : F1 F2 [F3] ; F2 : [F5] [F6] ; ) // F5, F6 still subfeatures of F2 but optionals, F1 mandatory 
fm3 = FM (S : [F1] F2 [F4] ; F2 : [F5] [F6] ; F5 -> F1 ; ) // F5 implies F1

// you can also play with TVL, featureide, SPLOT or other formats if you want
// serialize fm3 into featureide 

// logic-based
fm4 = merge union { fm1 fm2 fm3 }

// reference-based
fm5 = aggregateMerge union { fm1 fm2 fm3 }
// let S be the common root of fm1, fm2, fm3

// 0. without any slicing
fm6 = extract fm5.S

// 1. with slicing
s7 = { fm5.S } ++ fm5.S.*
fm7 = slice fm5 including s7

// 2. with local slicing
fm8 = ksynthesis fm5 over s7
```

The script loads three feature models fm1, fm2 and fm3 (the rest of the script will be explained in detail hereafter).
Note that fm1, fm2 and fm3 actually correspond to feature models used in the paper [3] "Supplier independent feature modelling" by Herman Hartmann, Tim Trew, Aart A. J. Matsinger, SPLC'09.

We want to compose the three feature models. 
In particular, we are targeting the following scenario. fm1, fm2 and fm3 are representing three product lines maintained by three different suppliers. 
We want to build a new product line offering every possible product offered by suppliers (no more, no less). 
In terms of feature modeling, we want to compute a new feature model (a composed feature model) that represents the union of input sets of configurations.

```familiar
fml> s1 = configs fm1
s1: (SET) {{S;F4;F2;F5};{S;F2;F5;F1};{F6;S;F2};{F6;S;F4;F2};{S;F2;F6;F1};{F6;F4;F2;S;F1};{S;F5;F2};{F1;F5;S;F4;F2}}
fml> s2 = configs fm2
s2: (SET) {{S;F1;F2;F5};{F5;F2;S;F1;F3};{S;F5;F1;F2;F6};{S;F6;F2;F1};{S;F2;F6;F1;F3};{F1;S;F3;F2};{F2;F1;S};{F2;F3;F6;F5;S;F1}}
fml> s3 = configs fm3
s3: (SET) {{F2;F4;F1;F6;S};{F5;F2;F1;S};{F6;F4;S;F2};{S;F1;F6;F2;F4;F5};{F1;F2;F4;F5;S};{F6;S;F2;F1;F5};{S;F1;F2};{F2;S};{F4;F2;S};{F2;F1;S;F4};{F6;F2;S};{F1;F6;F2;S}}
```

Likewise, each configuration authorized in the composed feature model corresponds to at least one configuration in either fm1, fm2 and fm3.
fm4 is such a feature model

```familiar
fml> fm4
fm4: (FEATURE_MODEL) S: (F4|F3)? [F1] F2 ; 
F2: [F6] [F5] ; 
(F3 -> F1);
```

```familiar
fml> s4 = configs fm4
s4: (SET) {{S;F1;F4;F2};{F4;S;F2};{F6;S;F1;F3;F2};{F2;F4;F6;S};{S;F5;F2;F4};{F3;F1;F2;S};{F2;F5;S;F1};{F6;F1;S;F2};{F2;F1;F5;S;F3};{F2;F1;F6;S;F4;F5};{S;F2;F1};{F3;S;F5;F2;F1;F6};{F6;S;F2};{F2;S;F5};{S;F6;F2;F4;F1};{F4;F2;F1;S;F5};{F2;S};{F1;F6;F5;F2;S}}
fml> size s4
res3: (INTEGER) 18
fml> counting fm4
res4: (DOUBLE) 18.0
```

### Merge operator

The magic is in the merge operator. 
The principle of the merge operator can be summarized as follows:
 * two features match iff they have the same name (F2 of fm1 matches with F2 of fm2 and F2 of fm3)
 * a new feature with the same "matched" name is created in the composed feature model (merging process)
 * depending on the sets of configuration we want (e.g., union) the variability information is synthesized accordingly. For instance, F3 and F4 are mutually exclusive (forming a Mutex-group, i.e., at most 1 of the features can be selected), F1 logically implies F3 or F2 is mandatory.
 * the feature hierarchy tries to retain as much as possible parent-child relationships in the input feature models. In particular, F5 and F6 are subfeatures of F2
 
The internal details of the merge implementation are also worth to describe. The principle is to encode each feature model as a formula and then computes a "composed" formula. 
This can be achieved by '''denoting''' into the Boolean logic the configuration semantics (e.g., of "union"). 
A hierarchy is then automatically selected and variability information is synthesized (more details can be found in ECMFA'10 paper or PhD thesis, see references [1] [2] below). 

### Reference-based approach 

The merge operator previously described is very suited when 
 * the maching and merging of features are rather basic (i.e., the matching strategy is one-to-one, based on names, and the composed feature model contains features with same names) 
 * the configuration semantics can be denoted in the Boolean logic
 
However there are practical situations in which the matching/merging strategy is much more complicated. 
Similar observations can be made for the configuration semantics: the "union" is not always what the users want. 
Moreover the merge operator looses the traceability with the input feature models. 

Therefore a natural idea is to **reference** input feature models. 
A ***composed view***, containing the features that are of interest for the composition, is specified. 
Then features of the view "reference" features of input feature models through (logical) constraints. 

Combined with advanced mechanisms such as slicing or local synthesis, we will show that we can actually implement a merge-like operator. 
Such compositional mechanisms are more general and should be preferred in case, as a user of FAMILIAR, you want to customize the matching/merging strategy as well as the configuration and ontological semantics. 

#### Aggregate 

To implement a reference-based approach, a natural approach is to execute the following steps:
 * define the composed view
 * define the set of constraints to establish correspondences with input features 
 * aggregate the composed view, the constraints as well as the input feature models together 

Using FAMILIAR, it can be implemented with something like: 
```familiar
cfm1 = copy fm1
cfm2 = copy fm2
cfm3 = copy fm3
fm5View = FM (S : [F1] [F2] [F3] [F4] ; F2 : [F5] [F6] ; )
csts = constraints (F1 -> (fm1_F1 or fm2_F1 or .. ; )
fm5bis = aggregate --renamings { cfm1 cfm2 cfm3 fm5View } withMapping csts
```

#### AggregateMerge

For implementing the union mode (or another like intersection), the constraints to be specified can be huge, thus making the process time-consuming and error-prone.
Another (more technical) problem is that the aggreagate operator assumes that features' names are unique (in order to make a distinction between features). 

Therefore we develop and provide another operator to automate this task, called **aggregateMerge**. 
It takes as input a set of feature models and a "mode" (e.g., union, intersection).

```familiar
// reference-based
fm5 = aggregateMerge union { fm1 fm2 fm3 }
```

It automatically generates (1) the view (2) the constraints and (3) aggregate both inputs with automatic renamings (see below on the previous example).

```familiar
fml> fm5
fm5: (FEATURE_MODEL) MergeCST: S InputFMs ; 
S: [F4] [F1] [F2] [F3] ; 
InputFMs: (fm3_S|fm1_S|fm2_S) ; 
F2: [F6] [F5] ; 
fm3_S: [fm3_F1] [fm3_F4] [fm3_F3] fm3_F2 ; 
fm2_S: fm2_F1 [fm2_F4] fm2_F2 [fm2_F3] ; 
fm1_S: [fm1_F1] [fm1_F4] [fm1_F3] fm1_F2 ; 
fm3_F2: [fm3_F5] [fm3_F6] ; 
fm2_F2: [fm2_F5] [fm2_F6] ; 
fm1_F2: (fm1_F5|fm1_F6) ; 
(fm3_F5 -> fm3_F1);
(F6 <-> ((fm3_F6 | fm2_F6) | fm1_F6));
!fm1_F3;
(F1 <-> ((fm3_F1 | fm2_F1) | fm1_F1));
(F2 <-> ((fm2_F2 | fm1_F2) | fm3_F2));
(F3 <-> ((fm3_F3 | fm2_F3) | fm1_F3));
(F4 <-> ((fm1_F4 | fm2_F4) | fm3_F4));
!fm2_F4;
!fm3_F3;
(S <-> ((fm2_S | fm1_S) | fm3_S));
(F5 <-> ((fm1_F5 | fm3_F5) | fm2_F5));
```

The interest of the aggregated feature model, here **fm5**, is that you have a ***semantically equivalent*** representation of the set of configurations. 
Indeed, the combinations of features authorized in the composed "view" of fm5 are exactly the same as in fm4, thus realizing the configuration semantics of union. 
It is straightforward to understand why: the constraints force the selection of at least one and at most one corresponding feature (if any)  in the input feature models. 

An example is given below. Selecting F3 in the view automatically selects the root of fm2 and deselects features of fm1 and fm3.
You can also notice that F2 is selected by default since F2 is included in every configuration of fm1, fm2 or fm3. 
You can play with c5 and verify that every valid configuration involving features of the view are also valid in fm4. 

```familiar
fml> c5 = configuration fm5
c5: (CONFIGURATION) selected: [MergeCST, S, F2, InputFMs]   deselected: [fm1_F3, fm2_F4, fm3_F3]
fml> select F3 in c5
res1: (BOOLEAN) true
fml> selectedF c5
res2: (SET) {fm2_F1;F3;fm2_F2;F2;S;F1;MergeCST;fm2_S;InputFMs;fm2_F3}
fml> deselectedF c5
res3: (SET) {fm2_F4;fm3_F4;fm3_F5;fm3_F1;F4;fm3_F3;fm1_F1;fm1_F4;fm1_F6;fm1_F2;fm3_F2;fm1_F5;fm1_S;fm3_F6;fm1_F3;fm3_S}
```

#### On the Quality of the View

A major problem with the previous solution is that features are all optionals in the "view" (we will see some other problems in the remainder).   

```familiar
fml> fm6 = extract fm5.S
fm6: (FEATURE_MODEL) S: [F4] [F1] [F2] [F3] ; 
F2: [F6] [F5] ;
```

As a result, you cannot use the view (fm6) independently. Furthermore the view contains anomalies, since the syntactical constructs are not conformant to the actual meaning. 
In particular, F2 is modeled as optional but is actually mandatory ; F3 and F4 are mutually exclusive ; F3 implies the selection of F1. 
It is clearly not the case in fm6 (see above). It is a very rough over-approximation of the actual configuration set. 
Therefore it not acceptable to exploit fm6 afterwards.

Two techniques are conceivable for improving the quality of the "view".

