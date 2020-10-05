package cn.ancono.math.geometry.analytic.plane;


import cn.ancono.math.MathCalculator;
import cn.ancono.math.MathObject;
import cn.ancono.math.exceptions.UnsupportedCalculationException;
import cn.ancono.math.function.MathFunction;
import cn.ancono.math.geometry.analytic.plane.curve.AbstractPlaneCurve;
import cn.ancono.math.geometry.analytic.plane.curve.SubstituableCurve;
import cn.ancono.math.numberModels.api.FlexibleNumberFormatter;
import cn.ancono.math.numberModels.api.Simplifiable;
import cn.ancono.math.numberModels.api.Simplifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Line is a straight line in the plane.The general equation {@literal ax + by + c = 0} is used by this class to
 * indicate a line.
 * <h1>Create a line</h1>
 * There are many ways to create a new line,they are all listed below:
 * <ul>
 * <li>General Formula:Use the general formula to create a line.<p> <ul>
 * {@link #generalFormula(Object, Object, Object, MathCalculator)}
 * </ul><li>Two Point:Create a line that passes through the two points.<p> <ul>
 * {@link #twoPoint(Point, Point, MathCalculator)},<p>
 * {@link #twoPoint(Object, Object, Object, Object, MathCalculator)}
 * </ul><li>Point Direction:Create a line that passes through the point and has such a direction vector.<p> <ul>
 * {@link #pointDirection(Point, PVector)},<p>
 * {@link #pointDirection(Point, Object, Object, MathCalculator)},<p>
 * {@link #pointDirection(Object, Object, Object, Object, MathCalculator)}<p>
 * </ul><li>Point Slope:Create a line that passes through the point and has such a slope<p>
 * <ul>
 * {@link #pointSlope(Point, Object, MathCalculator)},<p>
 * {@link #pointSlope(Object, Object, Object, MathCalculator)}
 * </ul>
 * <li>Point Normal Vector:Create a line with the normal vector that passes through the point.<p>
 * <ul>
 * {@link #pointNormal(Point, PVector)},<p>
 * {@link #pointNormal(Object, Object, Object, Object, MathCalculator)}
 * </ul>
 * <li>Two Intercept:Use intercept in x axis and y axis to create a line.<p>
 * <ul>
 * {@link #xyIntercept(Object, Object, MathCalculator)}
 * </ul>
 * <li>Slope Intercept:Use the slope and the intercept to create a line.<p>
 * <ul>{@link #slopeIntercept(Object, Object, MathCalculator)}
 * </ul>
 * </ul>
 * <h2>Relations between two lines</h2>
 * As we all known, there are three possible relations between two straight lines, they are
 * <i>Parallel</i>,<i>Intersect</i>,<i>Coincide</i>.
 *
 * @param <T> the type of number
 * @author lyc
 */
public final class Line<T> extends AbstractPlaneCurve<T> implements Simplifiable<T, Line<T>>, SubstituableCurve<T> {
    final T a, b, c;

    /**
     * The relation between two lines.
     *
     * @author lyc
     */
    public enum Relation {
        /**
         * Parallel
         */
        PARALLEL,
        /**
         * Intersect
         */
        INTERSECT,
        /**
         * Coincide
         */
        COINCIDE;
    }


    /**
     * Create a new line of the equation {@literal ax + by + c = 0}.The coefficient won't be simplified.
     *
     * @param mc
     * @param a
     * @param b
     * @param c
     */
    protected Line(MathCalculator<T> mc, T a, T b, T c) {
        super(mc);
        if (mc.isZero(a)) {
            //a == 0
            if (mc.isZero(b))
                zeroV();
            try {
                //possible unsupported operation
                c = mc.divide(c, b);
                b = mc.getOne();
            } catch (UnsupportedCalculationException re) {
                //just ignore
            }
        } else if (mc.isZero(b)) {
            try {
                //possible unsupported operation
                c = mc.divide(c, a);
                a = mc.getOne();
            } catch (UnsupportedCalculationException re) {
                //just ignore
            }
        }
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * Do substitution with the given point p.
     *
     * @param p a point
     * @return {@literal a*p.x + b*p.y + c}
     */
    @Override
    public T substitute(Point<T> p) {
        T t = getMc().multiply(a, p.x);
        t = getMc().add(t, getMc().multiply(b, p.y));
        return getMc().add(t, c);
    }

    @Override
    public T substitute(T x, T y) {
        T t = getMc().multiply(a, x);
        t = getMc().add(t, getMc().multiply(b, y));
        return getMc().add(t, c);
    }

    /**
     * Determines whether the given point is on this line.
     *
     * @param p a point
     * @return {@code true} if {@code p} is on this line.
     */
    @Override
    public boolean contains(Point<T> p) {
        return getMc().isZero(substitute(p));
    }

    private boolean isPossibleParallel(Line<T> l) {
        return getMc().isEqual(getMc().multiply(a, l.b),
                getMc().multiply(b, l.a));
    }

    private boolean isPossibleEqual(Line<T> l) {
        if (getMc().isZero(a)) {
            //a = 0 so b != 0
            return getMc().isEqual(getMc().multiply(b, l.c), getMc().multiply(l.b, c));
        }
        //compare a
        //a != 0
        return getMc().isEqual(getMc().multiply(a, l.c), getMc().multiply(l.a, c));

//		return mc.isEqual(mc.multiply(b, l.c), mc.multiply(l.b, c));
    }

    /**
     * Assign the value x and try to calculate value y.If this line
     * the perpendicular to x axis,then null will be returned.
     *
     * @param x x coordinate
     * @return the corresponding y value
     */
    public T computeY(T x) {
        if (getMc().isZero(b)) {
            //x = ..
            return null;
        }
        //ax + by + c = 0
        //-> y = -1/b * (ax +c)
        T axc = getMc().add(c, getMc().multiply(x, a));
        return getMc().negate(getMc().divide(axc, b));
    }

    /**
     * Assign the value y and try to calculate value x.If this line
     * the perpendicular to y axis,then null will be returned.
     *
     * @param y y coordinate
     * @return the corresponding x value
     */
    public T computeX(T y) {
        if (getMc().isZero(a)) {
            //y= ..
            return null;
        }
        //ax + by + c = 0
        // -> x = -1/a * (by +c)
        T axc = getMc().add(c, getMc().multiply(y, b));
        return getMc().negate(getMc().divide(axc, a));
    }


    /**
     * Get the relation of this line and the given line.
     *
     * @param l another line
     * @return the relation of these two lines.
     */
    public Relation relationWith(Line<T> l) {
        if (isPossibleParallel(l)) {
            if (isPossibleEqual(l)) {
                return Relation.COINCIDE;
            }
            return Relation.PARALLEL;
        }
        return Relation.INTERSECT;
    }

    /**
     * Return {@code true} only if {@code this // l } and {@code this != l}.
     *
     * @param l another line
     * @return {@code true} only if {@code this // l } and {@code this != l}
     */
    public boolean isParallel(Line<T> l) {
        if (isPossibleParallel(l)) {
            if (isPossibleEqual(l) == false) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return {@code true} only if {@code this �� l}.
     *
     * @param l another line
     * @return {@code true} only if {@code this �� l}
     */
    public boolean isPerpendicular(Line<T> l) {
        T m = getMc().add(getMc().multiply(a, l.a), getMc().multiply(b, l.b));
        return getMc().isZero(m);
    }

    /**
     * Return the intersect point of this line and line {@code l}.If the relation
     * between {@code this} and {@code l} is <i>parallel</i> or <i>coincide</i>, null
     * will be returned.
     *
     * @param l a line
     * @return the intersect point of the two line, or null.
     */
    public Point<T> intersectPoint(Line<T> l) {
        T d = getMc().subtract(
                getMc().multiply(a, l.b),
                getMc().multiply(b, l.a));
        //d = a*l.b - b*l.a
        if (getMc().isZero(d)) {
            return null;
        }
        T xd = getMc().subtract(getMc().multiply(b, l.c), getMc().multiply(l.b, c));
        T yd = getMc().subtract(getMc().multiply(c, l.a), getMc().multiply(l.c, a));
        return new Point<T>(getMc(), getMc().divide(xd, d), getMc().divide(yd, d));
    }

    /**
     * Returns a parallel line of {@code this} that passes through the given point.
     * The return line will satisfy that {@code this.isParallel(l)==true}.
     *
     * @param p a point
     * @return a new line.
     */
    public Line<T> parallel(Point<T> p) {
        return parallel(p.x, p.y);
    }

    /**
     * Returns a parallel line of {@code this} that passes through the given point.
     * The return line will satisfy that {@code this.isParallel(l)==true}.
     *
     * @param x X coordinate of the point
     * @param y Y coordinate of the point
     * @return a new line.
     */
    public Line<T> parallel(T x, T y) {
        //create a line ax + by + c' = 0
        //and c' = -ax-by
        T c = getMc().negate(getMc().add(getMc().multiply(a, x), getMc().multiply(b, y)));
        return new Line<T>(getMc(), a, b, c);
    }

    /**
     * Returns a perpendicular line of this line which passes through the given point.
     * The returned line will satisfy that {@code this.isPerpendicular(l)==true}.
     *
     * @param p a point
     * @return a new line
     */
    public Line<T> perpendicular(Point<T> p) {
        return perpendicular(p.x, p.y);
    }

    /**
     * Returns a perpendicular line of this line which passes through the given point.
     * The returned line will satisfy that {@code this.isPerpendicular(l)==true}.
     *
     * @param x X coordinate of the point
     * @param y Y coordinate of the point
     * @return a new line
     */
    public Line<T> perpendicular(T x, T y) {
        // create a line that bx - ay + c' = 0
        // and c' = ay - bx
        T c = getMc().subtract(getMc().multiply(a, y), getMc().multiply(b, x));
        T b = getMc().negate(a);
        return new Line<T>(getMc(), this.b, b, c);
    }

    /**
     * Returns the symmetry point of the given point by this line.The given point and
     * the point returned will have
     * the identity distance to this line and their connecting line will be perpendicular
     * to this line.
     *
     * @param p a point
     * @return the symmetry point of the point {@code p}
     */
    public Point<T> symmetryPoint(Point<T> p) {

        return symmetryPoint(p.x, p.y);
    }

    /**
     * Returns the symmetry point of the given point by this line.The given point and
     * the point returned will have
     * the identity distance to this line and their connecting line will be perpendicular
     * to this line.
     *
     * @param x X coordinate of the point
     * @param y Y coordinate of the point
     * @return the symmetry point of the point {@code p}
     */
    public Point<T> symmetryPoint(T x, T y) {
        var mc = getMc();
        //use the pre-calculated formula.
        T a2 = mc.multiply(a, a);
        T b2 = mc.multiply(b, b);
        T d_b2a2 = mc.subtract(b2, a2);
        T ab2 = mc.multiplyLong(mc.multiply(a, b), 2);
        T c2 = mc.multiplyLong(c, 2);
        T d1 = mc.subtract(
                mc.subtract(
                        mc.multiply(d_b2a2, x),
                        mc.multiply(ab2, y)),
                mc.multiply(c2, a));
        d_b2a2 = mc.negate(d_b2a2);
        T d2 = mc.subtract(
                mc.subtract(
                        mc.multiply(d_b2a2, y),
                        mc.multiply(ab2, x)),
                mc.multiply(c2, b));
        T s_a2b2 = mc.add(a2, b2);
        d1 = mc.divide(d1, s_a2b2);
        d2 = mc.divide(d2, s_a2b2);
        return new Point<T>(mc, d1, d2);
    }

    /**
     * Return the symmetry line of {@code this} by the point{@code p}.The returned line
     * will be parallel to this line and the distances from the point to both line are
     * the identity.
     *
     * @param p a point
     * @return a symmetry line.
     */
    public Line<T> symmetryLine(Point<T> p) {
        return symmetryLine(p.x, p.y);
    }

    /**
     * Return the symmetry line of {@code this} by the point{@code p}.The returned line
     * will be parallel to this line and the distances from the point to both line are
     * the identity.
     *
     * @param x X coordinate of the point
     * @param y Y coordinate of the point
     * @return a symmetry line.
     */
    public Line<T> symmetryLine(T x, T y) {
        T cN = getMc().add(c,
                getMc().multiplyLong(
                        getMc().add(
                                getMc().multiply(a, x),
                                getMc().multiply(b, y)),
                        2));
        cN = getMc().negate(cN);
        return new Line<T>(getMc(), a, b, cN);
    }

    /**
     * Returns the symmetry line of {@code this} by line {@code l}.The returned line
     * will intersect with {@code l} at the identity point where {@code this} intersects
     * with {@code l} and the angle will be the identity.
     *
     * @param l a line
     * @return a symmetry line of {@code this}
     */
    public Line<T> symmetryLine(Line<T> l) {
        //we just choose a point not too near the intersect point and
        //get the symmetry point,then use two-point form to create the new line.
        Point<T> in = intersectPoint(l);
        if (in == null) {
            //no intersect point
            //parallel or coincide.
            T cN = getMc().subtract(getMc().multiplyLong(l.c, 2), c);
            return new Line<T>(getMc(), a, b, cN);
        }
        T x, y;
        if (getMc().isZero(b)) {
            x = in.x;
            y = System.currentTimeMillis() % 2 == 0 ? getMc().add(in.y, getMc().getOne()) : getMc().subtract(in.y, getMc().getOne());
        } else {
            x = System.currentTimeMillis() % 2 == 0 ? getMc().add(in.x, getMc().getOne()) : getMc().subtract(in.x, getMc().getOne());
            y = getMc().negate(getMc().divide(getMc().add(getMc().multiply(a, x), c), b));
        }
        Point<T> pass = l.symmetryPoint(x, y);
        return twoPoint(in, pass, getMc());
    }

    /**
     * Returns the symmetry line of {@code this} by the perpendicular line of {@code l}
     * that passes through the intersect point of {@code this} and {@code l}.If take {@code this} as
     * a light moves toward a plane mirror in the line {@code l},the line is
     * basically the reflection light's path,that's why this method is called {@code reflexLine}.
     * <p>If {@code this//l},{@code null} will be returned.
     *
     * @param l a line
     * @return the reflex line,or null
     */
    public Line<T> reflexLine(Line<T> l) {
        //find the intersect point first.
        Point<T> pi = intersectPoint(l);
        if (pi == null) {
            return null;
        }
        //actually,this two methods are calculating the identity line...
        return symmetryLine(l);
    }

    /**
     * Return square root of a^2+b^2
     *
     * @return
     */
    public T tensor() {
        T sum = getMc().add(getMc().multiply(a, a), getMc().multiply(b, b));
        return getMc().squareRoot(sum);
    }

    /**
     * Return of a^2+b^2
     *
     * @return
     */
    public T tensorSq() {
        T sum = getMc().add(getMc().multiply(a, a), getMc().multiply(b, b));
        return sum;
    }

    /**
     * Returns the bisector of the intersect angle of {@code this} and {@code l},the returned line will
     * pass through the intersect point of {@code this} and {@code l} and any point in this line will
     * have a identity distance to both {@code this} and {@code l}.
     *
     * @param l a line
     * @return bisector of the intersect angle
     */
    public Line<T> intersectAngleBisector(Line<T> l) {
        T tense1 = tensor();
        T tense2 = l.tensor();
        T aN = getMc().add(getMc().multiply(a, tense2), getMc().multiply(l.a, tense1));
        T bN = getMc().add(getMc().multiply(b, tense2), getMc().multiply(l.b, tense1));
        T cN = getMc().add(getMc().multiply(c, tense2), getMc().multiply(l.c, tense1));
        return new Line<T>(getMc(), aN, bN, cN);
    }

    /**
     * Rotate this line anticlockwise by an angle,which has of tan value of {@code tanValue}.
     * The angle must be in (-pi/2,p1/2),and negative tan value indicates a clockwise
     * rotating with the {@code |tanValue|}.
     * <p>The new line will passes through the point {@code p}.
     *
     * @param p        a point
     * @param tanValue the tan value of this angle
     * @return a rotated line.
     */
    public Line<T> rotateAngle(Point<T> p, T tanValue) {
        if (getMc().isZero(tanValue)) {
            return parallel(p);
        }
        T k = slope();
        if (k == null) {
            //perpendicular to x axis
            T kN = getMc().reciprocal(tanValue);
            return pointSlope(p.x, p.y, kN, getMc());
        }
        T t = getMc().subtract(getMc().getOne(), getMc().multiply(k, tanValue));
        if (getMc().isZero(t)) {
            //this should only happen when the new line is perpendicular to x axis
            return parallelY(p.x, getMc());
        }
        T kN = getMc().divide(getMc().add(tanValue, k), t);
        return pointSlope(p.x, p.y, kN, getMc());
    }

    /**
     * Return the square of the distance from the point {@code p} to this line.
     *
     * @param p a point
     * @return distance's square
     */
    public T distanceSq(Point<T> p) {
        return distanceSq(p.x, p.y);
    }

    /**
     * Return the square of the distance from the point {@code p} to this line.
     *
     * @param x X coordinate of the point
     * @param y Y coordinate of the point
     * @return distance's square
     */
    public T distanceSq(T x, T y) {
        T sub = substitute(x, y);
        sub = getMc().multiply(sub, sub);
        T a2b2 = getMc().add(getMc().multiply(a, a), getMc().multiply(b, b));
        return getMc().divide(sub, a2b2);
    }

    /**
     * Return the distance of this line to the point p.
     *
     * @param p a point
     * @return |a*p.x+b*p.y+c|/Sqrt(a^2+b^2)
     */
    public T distance(Point<T> p) {
        return distance(p.x, p.y);
    }

    /**
     * Return the distance of this line to the point p.
     *
     * @param x x coordinate of the point
     * @param y y coordinate of the point
     * @return |a*p.x+b*p.y+c|/Sqrt(a^2+b^2)
     */
    public T distance(T x, T y) {
        T sub = getMc().abs(substitute(x, y));
        T a2b2 = getMc().add(getMc().multiply(a, a), getMc().multiply(b, b));
        a2b2 = getMc().squareRoot(a2b2);
        return getMc().divide(sub, a2b2);
    }

    /**
     * Return the square of distance of this line and another
     * parallel line {@code l}.
     *
     * @param l a parallel line
     * @return the square of distance between two lines.
     */
    public T distanceLineSq(Line<T> l) {
        T a2b2 = getMc().add(getMc().multiply(a, a), getMc().multiply(b, b));
        T cd = getMc().subtract(c, l.c);
        cd = getMc().multiply(cd, cd);
        return getMc().divide(cd, a2b2);
    }

    /**
     * Return the distance of this line and another
     * parallel line {@code l}.
     *
     * @param l a parallel line
     * @return the distance between two lines.
     */
    public T distanceLine(Line<T> l) {
        T a2b2 = getMc().add(getMc().multiply(a, a), getMc().multiply(b, b));
        a2b2 = getMc().squareRoot(a2b2);
        return getMc().divide(getMc().abs(getMc().subtract(c, l.c)), a2b2);
    }

    /**
     * Returns true if the point is above this line.This method
     * is basically equal to {@code b*(ax+by+c) > 0}.If the point is
     * on this line or the line is perpendicular to x axis,{@code false} will
     * be returned.
     *
     * @param p a point
     * @return {@code true} if {@code p} is above {@code this}
     */
    public boolean isAbove(Point<T> p) {
        int bB = getMc().compare(b, getMc().getZero());
        int sub = getMc().compare(substitute(p.x, p.y), getMc().getZero());
        return (bB * sub) == 1;
    }

    /**
     * Returns the slope of this line if the slope
     * exists.(b!=0)
     *
     * @return the slope of this line, or {@code null} if the
     * slope doesn't exists.
     */
    public T slope() {
        if (getMc().isZero(b)) {
            return null;
        }
        return getMc().negate(getMc().divide(a, b));
    }

    /**
     * Returns one of the direction vector of this line.The vector
     * will have a length of two.
     *
     * @return a 2-dimension column vector
     */
    public PVector<T> directionVector() {
        T x = b;
        T y = getMc().negate(a);
        return PVector.valueOf(x, y, getMc());
    }

    /**
     * Returns one of the formal vector of this line.The vector
     * will have a length of two.
     *
     * @return a 2-dimension column vector
     */
    public PVector<T> normalVector() {
        return PVector.valueOf(a, b, getMc());
    }

    /**
     * Get the number of coefficient A in the equation.
     *
     * @return coefficient A
     */
    public T getA() {
        return a;
    }

    /**
     * Get the number of coefficient B in the equation.
     *
     * @return coefficient B
     */
    public T getB() {
        return b;
    }

    /**
     * Get the number of coefficient C in the equation.
     *
     * @return coefficient C
     */
    public T getC() {
        return c;
    }

    /**
     * Get the intercept in x axis.If this line is parallel to x axis,{@code null} will
     * be returned.
     *
     * @return the x coordinate of the intersect point of {@code this} and x axis.
     */
    public T getInterceptX() {
        if (getMc().isZero(a)) {
            return null;
        }
        return getMc().negate(getMc().divide(c, a));
    }

    /**
     * Get the intercept in y axis.If this line is parallel to y axis,{@code null} will
     * be returned.
     *
     * @return the y coordinate of the intersect point of {@code this} and y axis.
     */
    public T getInterceptY() {
        if (getMc().isZero(b)) {
            return null;
        }
        return getMc().negate(getMc().divide(c, b));
    }

    /**
     * Returns the intercept point in x axis:
     * <pre>(getInterceptX(),0)</pre>
     *
     * @return
     */
    public Point<T> getInterceptPointX() {
        T t = getInterceptX();
        if (t == null) {
            return null;
        }
        return Point.valueOf(t, getMc().getZero(), getMc());
    }

    /**
     * Returns the intercept point in y axis:
     * <pre>(0,getInterceptY())</pre>
     *
     * @return
     */
    public Point<T> getInterceptPointY() {
        T t = getInterceptY();
        if (t == null) {
            return null;
        }
        return Point.valueOf(getMc().getZero(), t, getMc());
    }

    /**
     * Compute the tan value of the intersect angle.The value will be in [0,+INF).
     * If these two line is perpendicular,{@code null} will be returned.
     *
     * @return the tan value of the intersect angle, or {@code null}
     */
    public T intersectTan(Line<T> l) {
        //use the formula :
        //|a1b2-a2b1|
        //----------- = tan(angle)
        // a1a2+b1b2
        return getMc().abs(intersectTanDirected(l));
    }

    /**
     * Compute the tan value of the intersect angle.The value will be in [0,+INF).
     * If these two line is perpendicular,{@code null} will be returned.
     *
     * @return the tan value of the intersect angle, or {@code null}
     */
    public T intersectTanDirected(Line<T> l) {
        // use the formula :
        // a1b2-a2b1
        // ----------- = tan(angle)
        // a1a2+b1b2
        T deno = getMc().add(getMc().multiply(a, l.a), getMc().multiply(b, l.b));
        if (getMc().isZero(deno)) {
            return null;
        }
        T nume = getMc().subtract(getMc().multiply(a, l.b), getMc().multiply(b, l.a));
        return getMc().divide(nume, deno);
    }

    /**
     * Calculate the intersect angle of {@code this} and {@code l} by using the math function
     * {@code arctan}.The function arctan determines the return value's type.
     *
     * @param l      another line
     * @param arctan math function arctan
     * @return the intersect angle of {@code this} and {@code l}
     */
    public <N> N intersectAngle(Line<T> l, MathFunction<T, N> arctan) {
        return arctan.apply(intersectTan(l));
    }

    /**
     * Returns the projection of the point to this line, which is the intersect point
     * of a perpendicular line that passes through {@code p} and {@code this}.
     *
     * @param p a point
     * @return the projection of {@code p}
     */
    public Point<T> projection(Point<T> p) {
        //( (B^2*x-ABy-AC)/(A^2+B^2) , (A^2*y-ABx-BC)/(A^2+B^2) )
        T a2 = getMc().multiply(a, a);
        T b2 = getMc().multiply(b, b);
        T tensor = getMc().add(a2, b2);
        T ab = getMc().multiply(a, b);
        T corx = getMc().subtract(getMc().multiply(b2, p.x),
                getMc().add(getMc().multiply(ab, p.y),
                        getMc().multiply(a, c)));
        T cory = getMc().subtract(getMc().multiply(a2, p.y),
                getMc().add(getMc().multiply(ab, p.x),
                        getMc().multiply(b, c)));
        corx = getMc().divide(corx, tensor);
        cory = getMc().divide(cory, tensor);
        return Point.valueOf(corx, cory, getMc());
    }


    @NotNull
    @Override
    public <N> Line<N> mapTo(@NotNull Function<T, N> mapper, @NotNull MathCalculator<N> newCalculator) {
        return new Line<>(newCalculator, mapper.apply(a), mapper.apply(b), mapper.apply(c));
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof Line) {
            Line<?> l = (Line<?>) obj;
            return a.equals(l.a) &&
                    b.equals(l.b) &&
                    c.equals(l.c);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = getMc().hashCode();
        hash = hash * 31 + a.hashCode();
        hash = hash * 31 + b.hashCode();
        hash = hash * 31 + c.hashCode();
        return hash;

    }

    @Override
    public String toString(@NotNull FlexibleNumberFormatter<T, MathCalculator<T>> nf) {
        StringBuilder sb = new StringBuilder();
        sb.append("Line: ");
        T z = getMc().getZero();
        T o = getMc().getOne();
        if (!getMc().isEqual(a, z)) {
            if (!getMc().isEqual(a, o)) {
                sb.append('(').append(nf.format(a, getMc())).append(')');
            }
            sb.append("x + ");
        }
        if (!getMc().isEqual(b, z)) {
            if (!getMc().isEqual(b, o)) {
                sb.append('(').append(nf.format(b, getMc())).append(')');
            }
            sb.append("y + ");
        }
        if (!getMc().isEqual(c, z))
            sb.append('(').append(nf.format(c, getMc())).append(")   ");
        sb.delete(sb.length() - 2, sb.length());
        sb.append("= 0");
        return sb.toString();
    }

    /**
     * Determines whether the two lines are the identity,the determinant is used in this method to
     * compare the two lines are the identity instead of just comparing their coefficients.
     *
     * @param obj
     * @param mapper
     * @return
     */
    @Override
    public <N> boolean valueEquals(@NotNull MathObject<N> obj, @NotNull Function<N, T> mapper) {
        if (obj instanceof Line) {
            //we just map the line to a new line type T
            Line<N> l = (Line<N>) obj;
            Line<T> ll = l.mapTo(mapper, getMc());
            return relationWith(ll) == Relation.COINCIDE;
        }
        return false;
    }

    @Override
    public boolean valueEquals(@NotNull MathObject<T> obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof Line) {
            Line<T> l = (Line<T>) obj;
            return relationWith(l) == Relation.COINCIDE;
        }
        return false;
    }

    @Override
    public Line<T> simplify() {
        return this;
    }

    @Override
    public Line<T> simplify(Simplifier<T> sim) {
        List<T> list = new ArrayList<>(3);
        list.add(a);
        list.add(b);
        list.add(c);
        list = sim.simplify(list);
        return generalFormula(list.get(0), list.get(1), list.get(2), getMathCalculator());
    }


    private static final void zeroV() throws IllegalArgumentException {
        throw new IllegalArgumentException("a = b = 0");
    }

    /**
     * Create a new line with the given arguments that is the coefficient of the general formula
     * of this line.The coefficients are required that {@code a!=0 || b!= 0}.
     * <p>The returned line will have a general formula like
     * <pre>
     * ax + by + c = 0
     * </pre>
     *
     * @param a  the coefficient of x
     * @param b  the coefficient of y
     * @param c  the constant coefficient
     * @param mc a math calculator
     * @return line {@code ax + by + c = 0}
     * @throws IllegalArgumentException if {@literal a = b = 0}
     */
    public static <T> Line<T> generalFormula(T a, T b, T c, MathCalculator<T> mc) {
        return new Line<T>(mc, Objects.requireNonNull(a),
                Objects.requireNonNull(b),
                Objects.requireNonNull(c));
    }

    /**
     * Create a new line with the given point {@code p} and the direction vector {@code v}.The
     * returned line will have a equation like
     * <pre>
     * (x - p.x) / v.x = (y - p.y) / v.y
     * </pre>
     * Which can be transfer to
     * <pre>
     * v.y * x - v.x * y + v.x * p.y - v.y * p.x = 0
     * </pre>
     *
     * <b>This is the point-direction form of a line.</b>
     *
     * @param p  a point
     * @param v  a 2-dimension vector,if the dimension of {@code v} is more than 2,than the remaining
     *           numbers will not be considered.
     * @param mc a math calculator
     * @return line {@code (x - p.x) / v.x = (y - p.y) / v.y}
     * @throws IllegalArgumentException if {@code |v| = 0}
     */
    public static <T> Line<T> pointDirection(Point<T> p, PVector<T> v, MathCalculator<T> mc) {
        return pointDirection(p.x, p.y, v.x, v.y, mc);
    }

    /**
     * Create a new line with the given point {@code p} and the direction vector {@code v}.The
     * returned line will have a equation like
     * <pre>
     * (x - px) / vx = (y - py) / vy
     * </pre>
     * Which can be transfer to
     * <pre>
     * vy * x - vx * y + vx * py - vy * px = 0
     * </pre>
     *
     * <b>This is the point-direction form of a line.</b>
     *
     * @param px x coordinate of the point
     * @param py y coordinate of the point
     * @param vx x coordinate of the vector
     * @param vy y coordinate of the vector
     * @param mc a math calculator
     * @return line {@code (x - px) / vx = (y - py) / vy}
     * @throws IllegalArgumentException if {@literal vx == vy == 0}
     */
    public static <T> Line<T> pointDirection(T px, T py, T vx, T vy, MathCalculator<T> mc) {
        T b = mc.negate(vx);
        T c = mc.subtract(mc.multiply(vx, py), mc.multiply(vy, px));
        return new Line<T>(mc, vy, b, c);
    }

    /**
     * Create a new line with the given point {@code p} and the direction vector {@code v}.The
     * returned line will have a equation like
     * <pre>
     * (x - p.x) / vx = (y - p.y) / vy
     * </pre>
     * Which can be transfer to
     * <pre>
     * vy * x - vx * y + vx * p.y - vy * p.x = 0
     * </pre>
     *
     * <b>This is the point-direction form of a line.</b>
     *
     * @param p  a point
     * @param vx x coordinate of the vector
     * @param vy y coordinate of the vector
     * @param mc a math calculator
     * @return line {@code (x - p.x) / vx = (y - p.y) / vy}
     * @throws IllegalArgumentException if {@literal vx == vy == 0}
     */
    public static <T> Line<T> pointDirection(Point<T> p, T vx, T vy, MathCalculator<T> mc) {
        return pointDirection(p.x, p.y, vx, vy, mc);
    }

    /**
     * Create a new line with the given point {@code p} and the direction vector {@code v}.The
     * returned line will have a equation like
     * <pre>
     * (x - p.x) / vx = (y - p.y) / vy
     * </pre>
     * Which can be transfer to
     * <pre>
     * vy * x - vx * y + vx * p.y - vy * p.x = 0
     * </pre>
     *
     * <b>This is the point-direction form of a line.</b>
     * <p>The MathCalculator of the line will be taken from the first argument of FlexibleMathObject.
     *
     * @param p  a point
     * @param vx x coordinate of the vector
     * @param vy y coordinate of the vector
     * @return line {@code (x - p.x) / vx = (y - p.y) / vy}
     * @throws IllegalArgumentException if {@literal vx == vy == 0}
     */
    public static <T> Line<T> pointDirection(Point<T> p, T vx, T vy) {
        return pointDirection(p.x, p.y, vx, vy, p.getMathCalculator());
    }

    /**
     * Create a new line by the two points.The returned line will have a equation like
     * <pre>
     * (x - p1.x) / (p1.x - p2.x) = (y - p2.y) / (p1.y - p2.y)
     * </pre>
     * Which can be transfer to
     * <pre>
     * (p1.y - p2.y)x + (p2.x - p1.x) y + p1.x * p2.y - p1.y * p2.x = 0
     * </pre>
     *
     * @param p1 a point
     * @param p2 another point
     * @param mc a math calculator
     * @return line {@literal (x - p1.x) / (p1.x - p2.x) = (y - p2.y) / (p1.y - p2.y)}
     * @throws IllegalArgumentException if{@code p1 = p2}
     */
    public static <T> Line<T> twoPoint(Point<T> p1, Point<T> p2, MathCalculator<T> mc) {
        return twoPoint(p1.x, p1.y, p2.x, p2.y, mc);
    }

    /**
     * Create a new line by the two points.The returned line will have a equation like
     * <pre>
     * (x - p1.x) / (p1.x - p2.x) = (y - p2.y) / (p1.y - p2.y)
     * </pre>
     * Which can be transfer to
     * <pre>
     * (p1.y - p2.y)x + (p2.x - p1.x) y + p1.x * p2.y - p1.y * p2.x = 0
     * </pre>
     * <p>The MathCalculator of the line will be taken from the first argument of FlexibleMathObject.
     *
     * @param p1 a point
     * @param p2 another point
     * @return line {@literal (x - p1.x) / (p1.x - p2.x) = (y - p2.y) / (p1.y - p2.y)}
     * @throws IllegalArgumentException if{@code p1 = p2}
     */
    public static <T> Line<T> twoPoint(Point<T> p1, Point<T> p2) {
        return twoPoint(p1.x, p1.y, p2.x, p2.y, p1.getMathCalculator());
    }

    /**
     * Create a new line by the two points.The returned line will have a equation like
     * <pre>
     * (x - x1) / (x1 - x2) = (y - y2) / (y1 - y2)
     * </pre>
     * Which can be transfer to
     * <pre>
     * (y1 - y2)x + (x2 - x1) y + x1 * y2 - y1 * x2 = 0
     * </pre>
     * <b>This is the point-point form of a line.</b>
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param mc a math calculator
     * @return line {@literal (x - x1) / (x1 - x2) = (y - y2) / (y1 - y2)}
     */
    public static <T> Line<T> twoPoint(T x1, T y1, T x2, T y2, MathCalculator<T> mc) {
        T a = mc.subtract(y1, y2);
        T b = mc.subtract(x2, x1);
        T c = mc.subtract(mc.multiply(x1, y2), mc.multiply(x2, y1));
        return new Line<T>(mc, a, b, c);
    }

    /**
     * Create a new line by the given point and the slope of the line.The returned line will have a equation
     * like <pre>
     * k * (x - x1) = y - y1
     * </pre>
     * which will be transfered to
     * <pre>
     * kx - y + y1 - k*x1 = 0
     * </pre>
     * <b>This is the point-slope form of a line.</b>
     *
     * @param x1 coordinate of the point
     * @param y1 coordinate of the point
     * @param k  the slope of this line
     * @param mc a math calculator
     * @return line {@literal k * (x - x1) = y - y1}
     */
    public static <T> Line<T> pointSlope(T x1, T y1, T k, MathCalculator<T> mc) {
        T b = mc.negate(mc.getOne());
        T c = mc.subtract(y1, mc.multiply(k, x1));
        return new Line<T>(mc, k, b, c);
    }

    /**
     * Create a new line by the given point and the slope of the line.The returned line will have a equation
     * like <pre>
     * k * (x - p.x) = y - p.y
     * </pre>
     * which will be transfered to
     * <pre>
     * kx - y + p.y - k*p.x = 0
     * </pre>
     * <b>This is the point-slope form of a line.</b>
     *
     * @param p  the point
     * @param k  the slope of this line
     * @param mc a math calculator
     * @return line {@literal k * (x - p.x) = y - p.y}
     */
    public static <T> Line<T> pointSlope(Point<T> p, T k, MathCalculator<T> mc) {
        return pointSlope(p.x, p.y, k, mc);
    }

    /**
     * Create a new line by the given point and the slope of the line.The returned line will have a equation
     * like <pre>
     * k * (x - p.x) = y - p.y
     * </pre>
     * which will be transfered to
     * <pre>
     * kx - y + p.y - k*p.x = 0
     * </pre>
     * <b>This is the point-slope form of a line.</b>
     * <p>The MathCalculator of the line will be taken from the first argument of FlexibleMathObject.
     *
     * @param p the point
     * @param k the slope of this line
     * @return line {@literal k * (x - p.x) = y - p.y}
     */
    public static <T> Line<T> pointSlope(Point<T> p, T k) {
        return pointSlope(p.x, p.y, k, p.getMathCalculator());
    }

    /**
     * Create a new line by the given slope and intercept of this line.The returned line
     * will have a equation like
     * <pre>
     * y  = kx + b
     * </pre>
     * which will be transfered to
     * <pre>
     * kx - y + b = 0
     * </pre>
     * <b>This is the slope-intercept form of a line.</b>
     *
     * @param k  slope
     * @param b  intercept
     * @param mc a math calculator
     * @return line {@literal y  = kx + b}
     */
    public static <T> Line<T> slopeIntercept(T k, T b, MathCalculator<T> mc) {
        T b0 = mc.negate(mc.getOne());
        return new Line<T>(mc, k, b0, b);
    }

    /**
     * Create a new line by the given point and the normal vector of this line.
     * The line will have a equation like
     * <pre>
     * vx * (x - xp) + vy * (y - yp) = 0
     * </pre>
     * which will be transfered to
     * <pre>
     * vx * x + vy * y - vx * xp - vy * yp = 0
     * </pre>
     *
     * @param xp coordinate of the point
     * @param yp coordinate of the point
     * @param vx X coordinate of the vector
     * @param vy Y coordinate of the vector
     * @param mc a math calculator
     *           <b>This is the point-normal_vector form of a line.</b>
     * @return line {@literal vx * (x - xp) + xy * (y - yp) = 0}
     * @throws IllegalArgumentException if {@code vx==0 && vy==0}
     */
    public static <T> Line<T> pointNormal(T xp, T yp, T vx, T vy, MathCalculator<T> mc) {
        T c = mc.negate(mc.add(mc.multiply(vx, xp), mc.multiply(vy, yp)));
        return new Line<T>(mc, vx, vy, c);
    }

    /**
     * Create a new line by the given point and the normal vector of this line.
     * The line will have a equation like
     * <pre>
     * v.x * (x - p.x) + v.y * (y - p.y) = 0
     * </pre>
     * which will be transfered to
     * <pre>
     * v.x * x + v.y * y - v.x * p.x - v.y * p.y = 0
     * </pre>
     * <b>This is the point-normal_vector form of a line.</b>
     *
     * @param p  a point
     * @param v  a 2-dimension vector,if the dimension of {@code v} is more than 2,than the remaining
     *           numbers will not be considered.
     * @param mc a math calculator
     * @return line {@literal v.x * (x - p.x) + v.y * (y - p.y) = 0}
     * @throws IllegalArgumentException if {@code |v|==0}
     */
    public static <T> Line<T> pointNormal(Point<T> p, PVector<T> v, MathCalculator<T> mc) {
        return pointNormal(p.x, p.y, v.x, v.y, mc);
    }

    /**
     * Create a new line by the given point and the normal vector of this line.
     * The line will have a equation like
     * <pre>
     * v.x * (x - p.x) + v.y * (y - p.y) = 0
     * </pre>
     * which will be transfered to
     * <pre>
     * v.x * x + v.y * y - v.x * p.x - v.y * p.y = 0
     * </pre>
     * <b>This is the point-normal_vector form of a line.</b>
     * <p>The MathCalculator of the line will be taken from the first argument of FlexibleMathObject.
     *
     * @param p a point
     * @param v a 2-dimension vector,if the dimension of {@code v} is more than 2,than the remaining
     *          numbers will not be considered.
     * @return line {@literal v.x * (x - p.x) + v.y * (y - p.y) = 0}
     * @throws IllegalArgumentException if {@code |v|==0}
     */
    public static <T> Line<T> pointNormal(Point<T> p, PVector<T> v) {
        return pointNormal(p.x, p.y, v.x, v.y, p.getMathCalculator());
    }

    /**
     * Create a new line according to intercept of the line in x axis and y axis.The
     * line will have a equation like
     * <pre>
     * x / xi + y / yi = 1
     * </pre>
     * which will be transfered to
     * <pre>
     * yi*x + xi*y - xi*yi = 0
     * </pre>
     * <b>This is the two-intercept form of a line.</b>
     *
     * @param xi intercept in x axis
     * @param yi intercept in y axis
     * @param mc a math calculator
     * @return line {@literal x / xi + y / yi = 1}
     * @throws IllegalArgumentException if {@code xi==yi==0}
     */
    public static <T> Line<T> xyIntercept(T xi, T yi, MathCalculator<T> mc) {
        return new Line<T>(mc, yi, xi, mc.negate(mc.multiply(xi, yi)));
    }

    /**
     * Create a new line that is parallel to y axis and passes through point (x0,0).
     * The line will have a equation like
     * <pre>
     * x = x0
     * </pre>
     * which will be transfered to
     * <pre>
     * x - x0 = 0
     * </pre>
     * <b>This is provided for convenience.</b>
     */
    public static <T> Line<T> parallelY(T x0, MathCalculator<T> mc) {
        return new Line<T>(mc, mc.getOne(), mc.getZero(), mc.negate(x0));
    }

    /**
     * Create a new line that is parallel to y axis and passes through point (y0,0).
     * The line will have a equation like
     * <pre>
     * y = y0
     * </pre>
     * which will be transfered to
     * <pre>
     * y - y0 = 0
     * </pre>
     * <b>This is provided for convenience.</b>
     */
    public static <T> Line<T> parallelX(T y0, MathCalculator<T> mc) {
        return new Line<T>(mc, mc.getZero(), mc.getOne(), mc.negate(y0));
    }

    /**
     * Return a new line of
     * <pre>
     *  y = kx
     * </pre>
     *
     * @param k
     * @param mc
     * @return
     */
    public static <T> Line<T> yEkx(T k, MathCalculator<T> mc) {
        return new Line<T>(mc, k, mc.negate(mc.getOne()), mc.getZero());
    }

    /**
     * Create a new line with the given point {@code p} and the direction vector {@code v}.The
     * returned line will have a equation like
     * <pre>
     * (x - p.x) / v.x = (y - p.y) / v.y
     * </pre>
     * Which can be transfer to
     * <pre>
     * v.y * x - v.x * y + v.x * p.y - v.y * p.x = 0
     * </pre>
     *
     * <b>This is the point-direction form of a line.</b>
     *
     * @param p a point
     * @param v a 2-dimension vector,if the dimension of {@code v} is more than 2,than the remaining
     *          numbers will not be considered.
     * @return line {@code (x - p.x) / v.x = (y - p.y) / v.y}
     * @throws IllegalArgumentException if {@code |v| = 0}
     */
    public static <T> Line<T> pointDirection(Point<T> p, PVector<T> v) {
        return pointDirection(p, v, p.getMathCalculator());
    }


//	public static void main(String[] args) {
//		MathCalculator<Fraction> mc = Fraction.getCalculator();
//		MathCalculator<Long> mct = Calculators.getCalculatorLong();
////		Point<Fraction> p = new Point<>(mct,0l,0l).mapTo(Fraction::valueOf, mc);
//		Line<Fraction> l1 = Line.generalFormula(1l, -1l, -2l, mct).mapTo(Fraction::valueOf, mc);
//		Line<Fraction> l2 = Line.generalFormula(3l, -1l, 3l, mct).mapTo(Fraction::valueOf, mc);
//		Printer.print(l1.symmetryLine(l2));
//	}


}