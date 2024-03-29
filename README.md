# Ancono

An object-oriented and functional view of mathematics besides a math library on JVM platform.


---

## Introduction

Ancono aims to provide a programming view of mathematics, as well as a wide range of basic and advanced math tools,
focusing both on simplicity and efficiency. It is also good to use it for studying purpose.

This library contains the following modules:

1. Math functions:

   Basic math functions including GCD, LCM, mod, power and mod, mod inverse, Chinese remainder, factoring, primitive
   root and more.

2. Numbers:

   Ancono implements fraction, complex, integers in finite fields and so on.

   Users can choose their desired number models and operate the numbers with build-in methods. It is also possible for
   users to customize or add new number models, and they can be used with full compatibility.

   See examples <a href="#numbers">here</a>.

3. Polynomials, multinomials and expression:

   Ancono enables users to operate polynomials and multinomials over a field (or over some kind of ring). Methods of
   polynomial algebra, gcd, remainder, factorizing over Z/p and more are provided.

   General expression internally stored as abstract syntax tree is also supported. Expressions can be simplified using
   simplification rules. Currently, basic simplification rules are implemented, and user can also add extensions if
   necessary.

   See examples: <a href="#polynomials">polynomial</a>, <a href="#expressions">expression</a>.

4. Linear algebra

   Ancono supports computations of matrices and vectors over a ring or over a field. Linear spaces and linear
   transformations are also included. A tensor class that is

   like numpy's ndarray is also provided.

   Matrix: determinant, rank, solution space, null space, characteristic polynomial, adjoint, QR, LU and Cholesky
   decomposition, Echelon, Frobenious normal forms and so on. Special methods for matrix over a ring or UFD are also
   provided.

   Vector: inner product, outer product(in R^3), linear independent sets, Schmidt method and more.

   Tensor: tensor algebra, inner, wedge, einsum, slicing, reshaping and so on.

   See examples <a href="#linearAlg">here</a>.

5. Abstract algebra

   A general framework of interfaces is defined. There are also some methods for finite groups and finite fields.

   See examples <a href="#finiteGroups">here</a>.

6. Plane analytic geometry

   Line, triangle, conic sections (including circle, ellipse, hyperbola and parabola) and affine transformations are
   provided in Ancono. Useful methods are available, including computing the intersection points, computing the four
   kinds of centers of a triangle and so much more.

   See examples <a href="#planeAG">here</a>.

7. Number theory and Combinations

   Ancono provides useful tools for number theory and combination, such as enumerating primes, factorization and
   permutations.

   See examples <a href="#finiteGroups">here</a>.

8. Logic

   With Ancono, user can construct and operate formulas of proposition logic and first order logic.

   See examples <a href="#logic">here</a>.

9. Calculus

   Utilities for derivatives, Taylor series, limits, differential forms and more are provided.

   See examples <a href="#calculus">here</a>.

10. Probability

    Probability spaces and random variables. Ancono provides a framework resembling the mathematic view of random
    variable. Stochastic processes are also included.

    See examples <a href="#prob">here</a>.

11. Differential geometry

    Curve and plane in 3-dimensional Euclidean space, computing curvature, torsion, Frenet frame, fundamental forms ...

    See examples <a href="#dgeometry">here</a>.

12. Graph theory

    Graphs, priority-first search, connected components, Euclidean cycle and so on.

This project is still being developed, more features will be available in the future.

## Number models

Number model is a core concept in this library, it is like a generic type for objects but with additional set of
operations.

Generally speaking, we treat mathematical objects in different ways in different situations. For example, the number `1`
can be viewed as an integer, a rational number, a real number, a complex number or even a constant polynomial, depending
on the implicit context. In addition, in different cases we define different sets of operations on them. For example, we
can compare two real numbers (in natural order) but not two complex numbers. On the other hand, for computational and
complexity reasons, even the same mathematical object have to be treated differently in programming, such as `int`
,`long`, and `BigInteger`. Therefore, there does not exist a most general way to treat those objects, and we call a
concrete implementation for such an object in programming as a 'number model'.

Since we can use the same type in programming to represent different mathematical objects
(for example, `int` for both natural numbers and integers), we separate the number model type from the operations on it.
The latter is provided in a hierarchy of calculator interfaces, while the former can be arbitrary. We use the name '
number model' to refer to both the type and the operations.

Math objects (in Ancono the class `MathObject`) are usually based on number models. For example, we have matrices of
integers, rational numbers or so on. Although the number model can be different, the operations(or properties) defined
in math objects are in general the same (for example the matrix multiplication). When creating a math object, a
calculator is generally required.

Various number models are defined in Ancono, such as fraction, complex and expression. Generally, you can get the
corresponding calculator by calling the static method `calculator()`. Implementations of calculators of built-in number
types(`int`, `long`, `double`) are provided in `Calculators`.

If user want to use an external number model, simply implement the corresponding calculator interface and pass the
instance of the calculator when needed. Then, it can be used just as other number models.

### Calculator

Calculator interface defines a set of basic operations that might be used. Ancono uses a calculator to define all the
operations other than creates general interface for all number models, because multiple kinds of operations can be
defined on a number model class, and some number classes may be unmodifiable (such as primitive types).

With concepts in abstract algebra and corresponding interfaces, we have a more delicate structure for calculators. The
hierarchy structure can be listed below:

1. `EqualPredicate` to define a equivalence relation.

2. `SemigroupCalculator` to define a binary operation `apply(x,y)`.

   `AbelSemiGroupCal` and `MulSemiGroupCal` are another versions of the interface with the operation named `add`
   and `multiply`. And in `AbelSemiGroupCal` the operation is commutative.

3. `MonoidCalculator` to provide the identity element for the operation `apply(x,y)`.

   The corresponding abelian and multiplicative versions are `AbelMonoidCal` and `MulMonoidCal`.

4. `GroupCalculator` to provide the inversion of the operation, now the number model forms a group with respect to the
   calculator.

   The corresponding abelian and multiplicative versions are `AbelGroupCal` and `MulGroupCal`.

5. `RingCalculator` The number model now can add and multiply. Addition corresponds to the operation in the group, which
   should be commutative.

6. `UnitRingCalculator` to provide the multiplicative identity.

7. `DivisionRingCalculator`  to provide the multiplicative inverse.

8. `FieldCalculator` The number model now forms a field with respect to the calculator.

9. `QuotientCalculator` specializes the field to be quotient field and `RealCalculator` specializes the field to be
   real, while providing primary functions like `exp` and `sin`.

There are also extra interfaces for number models with special structure, such as `EUDCalculator`(calculator for
Euclidean domain) and `FunctionCalculator`(provides differential).

(To be detailed)

### Math Object

The abstract class `MathObject` is the superclass of almost all Math objects in Ancono. The generic parameter of a
`MathObject` represents the type of number model that it uses. A math object always holds the
corresponding `MathCalculator` and one can get it via method `getMathCalculator()`.

(To be detailed)

## Examples

#### <a name="numbers">Using number models</a>:

**Fraction**:

```java
var a=Fraction.of("1/2");
System.out.println(a);
var b=Fraction.ONE;
var c=a.add(b);
c=c.subtract(Fraction.ZERO);
c=c.multiply(a);
c=c.add(1);
System.out.println(c);
//Result: 7/4
```

With operator overloading in Kotlin, the code can be simplified as:

```kotlin
val a = Fraction.of("1/2")
val b = Fraction.ONE;
var c = ((a + b) - Fraction.ZERO) * a + 1
println(c)
```

**Complex**:

```java
var cal=Calculators.doubleCal();
Complex<Double> z1=Complex.real(1.0,cal);
z1=z1.squareRoot();
Complex<Double> z2=Complex.of(1.0,2.0,cal);
Complex<Double> z3=z1.multiply(z2);
System.out.println(z3);
//Result: (1.0)+(2.0)i
```

Mobius transformation and extended complex:

```java
var mc=Calculators.doubleDev(); // double calculator with deviation
var one=Complex.one(mc);
var i=Complex.i(mc);
var n_i=i.negate();
var f=MobiusTrans.Companion.to01Inf(one,i,n_i);
//creates a Mobius transformation that maps 1,i,-1 to 0,1,inf
System.out.println(f);
System.out.println(f.apply(i)); // 1
System.out.println(f.inverse().apply(Complex.inf(mc))); // -i
```

**Calculations in Z mod p**

```java
var mc=Calculators.intModP(29);
System.out.println();
var matrix=Matrix.of(mc,2,2,
                     1,2,
                     3,4);
System.out.println("M = ");
matrix.printMatrix();
System.out.println("Inverse of M in Z mod 29 is");
var inv=matrix.inverse();
inv.printMatrix();
System.out.println("Check their product:");
Matrix.multiply(matrix,inv).printMatrix();
```

#### <a name="polynomials">Polynomials:</a>

**Multiplication**:

```java
var cal=Calculators.doubleCal();
// we use double as the type of the coefficient of the polynomials
var f=Polynomial.of(cal,1.0,1.0,2.0,3.0); // 1 + x + 2x^2 + 3 x^3
var g=Polynomial.binomialPower(2.0,3,cal); // (x-2)^3
System.out.println("f(x) = "+f);
System.out.println("g(x) = "+g);
var h=f.multiply(g);
System.out.println("f(x)g(x) = "+h);
```

**Greatest common divisor**:

```java
var calInt=Calculators.integer();
var cal=Fraction.getCalculator();
var f=Polynomial.of(calInt,1,2,1).mapTo(Fraction::of,cal); // 1 + 2x + x^2
var g=Polynomial.of(calInt,-2,-1,1).mapTo(Fraction::of,cal); // -2 - x + x^2
System.out.println("f(x) = "+f);
System.out.println("g(x) = "+g);
var h=f.gcd(g);
System.out.println("gcd(f(x),g(x)) = "+h);
```

**Greatest common divisor of multinomials**:

```
var f = Multinomial.parse("x^2+2xy+y^2"); // = (x+y)^2
var g = Multinomial.parse("x^2+xy+xz+yz"); // = (x+y)(x+z)
var h = Multinomial.gcd(f,g);
System.out.println("f = " + f);
System.out.println("g = " + g);
System.out.println("gcd(f,g) = " + h);
```

**Polynomials on other fields:**

```
var mc = Calculators.intModP(2); // calculator for field Z_2
var g = Polynomial.parse("x^11 + x^10 + x^6 + x^5 + x^4 + x^2 +1", mc, Integer::parseInt);
System.out.println("g(x) = " + g);
var p = Polynomial.parse("x^23+1", mc, Integer::parseInt);
var pc = Polynomial.getCalculator(mc);
var h = pc.divideToInteger(p, g);
System.out.println("The inverse of g(x) in Z2[x]/(x^23+1) is: " +h);
```

#### <a name="expressions">Expression</a>

```java
var cal=Expression.getCalculator();
var f1=cal.parse("(x^2+3x+2)/(x+1)+sin(Pi/2)+exp(t)");
System.out.println(f1);
var f2=cal.parse("y+1");
System.out.println(f2);
var f3=cal.divide(f1,f2);
System.out.println(f3);
```

#### <a name="linearAlg">Linear Algebra</a>

**Matrix:**

```java
var cal=Calculators.integer();
var calFrac=Fraction.getCalculator();
var m1=Matrix.of(2,2,cal,
                 1,2,
                 4,5)
    .mapTo(calFrac,Fraction::of);
var m2=Matrix.of(2,2,cal,
                 3,-6,
                 -4,8)
    .mapTo(calFrac,Fraction::of);
var m3=m1.multiply(m2);
System.out.println(m3);
var det=m3.det();
var rank=m3.rank();
System.out.println("Det of the matrix: "+det);
System.out.println("Rank of the matrix: "+rank);
```

Kernel and image: (in Kotlin)

```kotlin
val p = 3
val n = 5
val matrix = Matrix(n, n, Calculators.intModP(p)) { _, _ ->
    Random.nextInt(0, p)
} // create a random n * n matrix 
val kernel = matrix.kernel()
val image = matrix.image()
println(matrix)
println(kernel)
println(image)
println(kernel.rank + image.rank) // = n
```

Matrix polynomial:

```java
var mc=Fraction.getCalculator();
        var m=MatrixSup.parseFMatrix("1 1 0\n0 1 0\n0 0 2");
        System.out.println("M = \n"+m);
        var f=m.charPoly();
        System.out.println("Characteristic polynomial f(x) = "+f);
        var matrixCal=Matrix.calculatorFor(m);
var fm = f.mapTo(matrixCal, x -> Matrix.diag(x, 3, mc));
        var result=fm.compute(m);
        System.out.println("f(M) = \n"+result); // 
```

**Vector:**

```
var mc = Calculators.doubleDev();
var u = SVector.valueOf(1., 2., 3., mc).unitVector();
var v = SVector.valueOf(2., 3., 4., mc);
// SVector: vector in space (R^3), outer product is supported
System.out.println("u = " + u);
System.out.println("v = " + v);
System.out.println("u+v = " + u.add(v));
System.out.println("<u,v> = " + u.innerProduct(v));
System.out.println("u × v = " + u.outerProduct(v));
```

**Vector basis:**

```
//import static cn.ancono.math.numberModels.Fraction.of;
var mc = Fraction.getCalculator();
var v1 = Vector.of(mc, of(1), of(2), of(3));
var v2 = Vector.of(mc, of(2), of(4), of(6));
var v3 = Vector.of(mc, of(1), of(2), of(5));
var basis = VectorBasis.generate(v1, v2, v3);
System.out.println("The basis is generated by: " + v1 + "," + v2 + "," + v3);
System.out.println("Its rank is " + basis.getRank());
var v = Vector.of(mc, Fraction.ONE, of(2), of(0));
System.out.println("Reducing the vector " + v);
System.out.println("Can reduce? " + basis.canReduce(v));
var coe = basis.reduce(v);
System.out.println("Coefficients = " + coe);
```

#### <a name="finiteGroups">Finite groups</a>

```java
var G=PermutationGroup.symmetricGroup(4); // S_4
var H=PermutationGroup.generateFrom(
    Permutations.swap(4,0,1),
    Permutations.swap(4,2,3));
var H1=G.normalizer(H);
System.out.println(H1.getSet());
System.out.println(G.indexOf(H1));
```

#### <a name="planeAG">Plane Analytic Geometry</a>

**Triangle**:

```java
var mc=Expression.getCalculator();
var str="x1,y1,x2,y2,x3,y3".split(","); // coordinates
var A=Point.valueOf(mc.parse(str[0]),mc.parse(str[1]),mc);
var B=Point.valueOf(mc.parse(str[2]),mc.parse(str[3]),mc);
var C=Point.valueOf(mc.parse(str[4]),mc.parse(str[5]),mc);
var triangle=Triangle.fromVertex(A,B,C);

var G=triangle.centerG(); //gravity center
var area=triangle.area(); // area of the triangle
System.out.println(G);
System.out.println(area);
```

#### <a name="calculus">Calculus</a>

**Computing limit**:

```java
var mc=Expression.getCalculator();
var expr=mc.parse("sin(x)/x");
var result=Limit.limitOf(expr,LimitProcess.Companion.toZero(mc),mc);
System.out.println("as x -> 0, lim sin(x)/x = "+result);
//result = 1
```

**Integrating rational function**:(in Kotlin)

```kotlin
val mc = ExprCalculator.instance
val mcl = Calculators.longExact()
val nume = Polynomial.parse("x", mcl, String::toLong)
val deno = Polynomial.parse("x^3 + 1", mcl, String::toLong)

println("${MathSymbol.INTEGRAL} ($nume) / ($deno) dx =")
val integral = Calculus.intRational(nume, deno, mc, "x")
println(integral)
```

#### <a name="logic">Logic</a>

**Proposition logic (written in Kotlin)**:

```kotlin
val formula = (p implies q) and (q implies r) implies (q implies r)
println(formula)
println("Is tautology: ${formula.isTautology}")
println("Main disjunctive norm: ${formula.toMainDisjunctiveNorm()}")
println("Conjunctive norm: ${formula.toConjunctiveNorm()}")
println("Is equivalent to T: ${formula valueEquals T}")
```

#### <a name="prob">Probability</a>

**Random variables**: (written in Kotlin)

```kotlin
val X = RandomVariables.normal(0.0,1.0)
val Y = RandomVariables.constant(1.0)
println(X.getAsSequence().take(5).toList()) // random numbers from normal dist.
val Z = Y * X - X // random variable algebra
println(Z.getAsSequence().take(5).toList()) // all zeros
```

Stochastic process:

```kotlin
/*
 This example estimates the hitting times of simple symmetric random walk
 */
val ssrw = StochasticProcesses.simpleRandomWalk()
val T0 = ssrw.hittingTimeOf(-4)
val T1 = ssrw.hittingTimeOf(6)
val T = T0 min T1 // T = min(T0, T1), also stopping time
val X_T = ssrw.rvAt(T)
println("E(X_T) = " + X_T.estimateExpectation()) // ~ 0 by optional stopping theorem
val p1 = (T0 lessThan T1).estimateExpectation() // T0 lessThan T1: creates a indicator r.v.
val p2 = (T1 lessThan T0).estimateExpectation()
println(p1) // ~ 0.6 = b / (b-a)
println(p2) // ~ 0.4 = a / (b-a)
println(T.estimateExpectation()) // ~ 4 * 6 = |ab| =  24
```

#### <a name="dgeometry">Differential geometry</a>

**Computing curvature and torsion of a curve**: (written in Kotlin)

```kotlin
val t = mc.parse("t")
val r1 = makeCurve("t+Sqr3*sin(t), 2cos(t),Sqr3*t-sin(t)") // a helper method
val r2 = makeCurve("2cos(t/2),2sin(t/2),-t")
println("r1:")
println(r1.curvature(t))
println(r1.torsion(t))
println("r2:")
println(r2.curvature(t))
println(r2.torsion(t))
```

**Computing coefficients of fundamental forms of a surface**:

```kotlin
val expr = "a*cos(u)cos(v),a*cos(u)sin(v),a*sin(u)"
val r = makeSurface(expr) // a helper method
val u = mc.parse("u")
val v = mc.parse("v")
// coefficients of the first fundamental form
println(r.E(u, v))
println(r.F(u, v))
println(r.G(u, v))
```


More samples are available in the `samples` folder.

## Usage
Users can utilize this library by simply import the jar file downloaded from `build` or from the 
[release page](https://github.com/140378476/Ancono/releases). 

## Language
Ancono is written by both Java and [Kotlin](https://kotlinlang.org) and you can use it with Java only. However, some 
extra features (such as operator override) are only possible if you use Kotlin. 

## Dependencies
Core:
* Java: JDK 17
* Kotlin: 1.4

Test:

* JUnit 4

## Related project

[AnconoGraphic](https://github.com/140378476/AnconoGraphic) provides graphic extension of this library.

## Development

Project Ancono welcomes anyone to join in the development.

Modules to be added in future: Calculators for finite field `GF(p^n)`, computational number theory and computational
algebraic geometry, numeric methods(interpolation, integration, optimization,...) and more unit tests.

**Note**: Further improvement of this proejct requires more professional and advanced knowledge in the related
mathematical(computer algebra or more) fields, but I am not currently working on these areas(statistics and ML, instead)
, so this project may be **suspended indefinitely**. Even though, I will try to update this project occasionally for bug
fixes and minor updates.



