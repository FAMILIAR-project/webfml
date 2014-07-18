Chapter 1 - How to use Familiar
=========

------


Lorem ipsum

```familiar
fm1 = FM (A: B [C] ; )
fm9 = FM (A : B ; )
fm2 = FM (A : B [C] ; )
fm3 = FM (A : B [E] ; )
fm4 = merge sunion fm*
fm5 = FM (A : J [K] [L] ; )

fm0 = merge sunion fm*

n0 = counting fm0
nTotal = 0
foreach (fm in fm*) do
   nfm = counting fm
   nTotal = nTotal + nfm
end

nTotal = nTotal + 1
n4 = counting fm4
n7 = counting fm0
fm0
mtx = computeMUTEXGroups fm0

```

```familiar
var k = p;
merge p
sunion k
fm2 = FM(A [B] C )
```


```familiar
var k = p;
merge p
sunion k
fm2 = FM(A [B] C )
```

> Written with [StackEdit](https://stackedit.io/).