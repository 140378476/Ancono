package cn.ancono.math.geometry.analytic.plane;

import cn.ancono.math.IMathObject;
import cn.ancono.math.algebra.abs.calculator.EqualPredicate;
import cn.ancono.math.equation.EquationSup;
import cn.ancono.math.function.MathFunction;
import cn.ancono.math.geometry.analytic.plane.curve.ClosedCurve;
import cn.ancono.math.geometry.analytic.plane.curve.ConicSection;
import cn.ancono.math.geometry.analytic.plane.curve.RectifiableCurve;
import cn.ancono.math.numberModels.api.RealCalculator;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

;

/**
 * A class that describes a circle in a plane.
 *
 * @param <T>
 * @author lyc
 */
public final class Circle<T> extends ConicSection<T> implements ClosedCurve<T>, RectifiableCurve<T> {
    /**
     * Describes the relation between two circles.
     *
     * @author lyc
     */
    public enum Relation {
        /**
         * Relation that one circles contains another circle and they don't intersect with each other.
         */
        INCLUDE,
        /**
         * Relation that two circles intersect only at one point and
         * one circle contains another circle.
         */
        INSCRIBED,
        /**
         * Relation that two circles intersect at two points.
         */
        INTERSECT,
        /**
         * Relation that two circles intersect only at one point and they are
         * separate.
         */
        CIRCUMSCRIBED,
        /**
         * Relation that two circles don't intersect with each other and they are
         * separate.
         */
        DISJOINT;

    }

    private final Point<T> o;
    /**
     * The radius of a circle,which may not be calculated.
     */
    private T r;
    private final T r2;

    /**
     * Give the center of the circle and the radius,create a circle.
     *
     * @param mc
     * @param ox
     * @param oy
     * @param r
     */
    protected Circle(RealCalculator<T> mc, T ox, T oy, T r) {
        super(mc, mc.getOne(),
                mc.getZero(),
                mc.getOne(),
                mc.multiplyLong(ox, -2),
                mc.multiplyLong(oy, -2),
                mc.subtract(mc.add(mc.multiply(ox, ox), mc.multiply(oy, oy)), mc.multiply(r, r)));
        o = new Point<>(mc, ox, oy);
        this.r = r;
        this.r2 = mc.multiply(r, r);
    }

    /**
     * Standard equation of a circle
     *
     * @param mc
     * @param D
     * @param E
     * @param F
     * @param b  identifier,useless.
     */
    protected Circle(RealCalculator<T> mc, T D, T E, T F, boolean b) {
        super(mc,
                mc.getOne(),
                mc.getZero(),
                mc.getOne(),
                D, E, F);
        o = new Point<>(mc, mc.divideLong(D, -2), mc.divideLong(E, -2));

        r2 = mc.add(mc.negate(F), mc.add(square(o.x), square(o.y)));

    }

    /**
     * Used for inner calls.
     *
     * @param mc
     * @param o
     * @param r2
     * @param D
     * @param E
     * @param F
     * @param r  optional
     */
    protected Circle(RealCalculator<T> mc, Point<T> o, T r2, T D, T E, T F, T r) {
        super(mc,
                mc.getOne(),
                mc.getZero(),
                mc.getOne(),
                D, E, F);
        this.o = o;
        this.r = r;
        this.r2 = r2;
    }

    /**
     * A so usually called square calculation.<p>
     * <b>Make sure the calculator has been initialized.</b>
     *
     * @param x
     * @return square of x
     */
    private T square(T x) {
        return getMc().multiply(x, x);
    }

    /* (non-Javadoc)
     * @see cn.ancono.math.geometry.analytic.planeAG.ConicSection#getType()
     */
    @Override
    public cn.ancono.math.geometry.analytic.plane.curve.ConicSection.Type determineType() {
        return ConicSection.Type.ELLIPSE;
    }

    /**
     * Get the center of this circle.
     *
     * @return the center of this circle.
     */
    public Point<T> getCenter() {
        return o;
    }

    /**
     * Gets the radius of this circle.This method usually requires the {@code squareRoot} method is
     * available in the math calculator.
     *
     * @return the radius of this circle.
     */
    public T getRadius() {
        if (r == null) {
            r = getMc().squareRoot(r2);
        }
        return r;
    }

    /**
     * Gets the diameter of this circle.This method usually requires the {@code squareRoot} method is
     * available in the math calculator.
     *
     * @return the diameter of this circle.
     * @see Circle#getRadius()
     */
    public T getDiameter() {
        return getMc().multiplyLong(getRadius(), 2L);
    }

    /**
     * Computes the corresponding arc length of the given angle.This method will not check whether
     * the angle is positive and smaller than {@code 2π}.
     *
     * @param angle the angle,radical
     * @return the length of the arc
     */
    public T getArcLength(T angle) {
        return getMc().multiply(angle, getRadius());
    }


    /**
     * Gets the area of this circle,which is calculated by the formula {@literal S = πr²}.This method requires the
     * constant value of {@literal pi} is available in the calculator.
     *
     * @return the area of this circle
     */
    public T getArea() {
        T pi = getMc().constantValue(RealCalculator.STR_PI);
        return getMc().multiply(pi, r2);
    }

    /**
     * Get the perimeter of this circle,which is equal to {@literal C = 2πr}.This method requires the
     * constant value of {@literal PI} is available in the calculator.
     *
     * @return the perimeter of this circle.
     */
    public T getPerimeter() {
        T pi = getMc().constantValue(RealCalculator.STR_PI);
        return getMc().multiply(getMc().multiplyLong(pi, 2l), getRadius());
    }

    /**
     * Assume {@code d} is the distance of the center of this circle to a chord,
     * return the length of the chord.If the distance is larger than the radius,
     * {@code null} will be returned.
     *
     * @param d distance
     * @return the length of the chord,or {@code null}
     */
    public T getChordLength(T d) {
        T d2 = square(d);
        if (getMc().compare(d2, r2) > 0) {
            return null;
        }
        return getMc().multiplyLong(getMc().squareRoot(getMc().subtract(r2, d2)), 2l);
    }

    /**
     * Assume {@code d2} is the square of the distance of the center of this circle to a chord,
     * return the square of the length of the chord.
     *
     * @param d2 square of distance
     * @return the square of the length of the chord
     */
    public T getChordLengthSq(T d2) {
        return getMc().multiplyLong(getMc().subtract(r2, d2), 4l);
    }

    /**
     * Get the corresponding central angle of the chord whose length is {@code cl}.
     * If {@code cl} is larger than the diameter of this circle,{@code null} will be
     * returned.
     *
     * @param cl     the length of chord
     * @param arccos the math function arccos
     * @return the angle of
     */
    public T getCentralAngle(T cl, MathFunction<T, T> arccos) {
        T an = getCircumAngle(cl, arccos);
        if (an == null) {
            return null;
        }
        return getMc().multiplyLong(an, 2l);
    }

    /**
     * Get the corresponding circumference angle of the chord whose length is {@code cl}.
     * If {@code cl} is larger than the diameter of this circle,{@code null} will be
     * returned.
     *
     * @param cl     the length of chord
     * @param arccos the math function arccos
     * @return the angle of
     */
    public T getCircumAngle(T cl, MathFunction<T, T> arccos) {
        T l_2 = getMc().divideLong(cl, 2l);
        if (getMc().compare(l_2, getRadius()) > 0) {
            return null;
        }
        return arccos.apply(getMc().divide(l_2, r));
    }

    /**
     * Determines the relation of the given point and this circle.The relation may be
     * <i>inside</i>,<i>on</i>,<i>outside</i>.
     *
     * @param p a point
     * @return {@code -1} if {@code p} is inside this circle,
     * {@code 0} if {@code p} is on this circle,
     * or {@code 1} if {@code p} is outside this circle.
     */
    @Override
    public int relation(Point<T> p) {
        T dis = o.distanceSq(p);
        return getMc().compare(dis, square(getRadius()));
    }

    /**
     * Determines the relation of the given line and this circle.The relation may be
     * <i>intersect</i>,<i>tangent</i>,<i>disjoint</i>.
     *
     * @param l a line
     * @return {@code -1,0,1} for <i>intersect</i>,<i>tangent</i>,<i>disjoint</i>.
     */
    public int relation(Line<T> l) {
        T d2 = l.distanceSq(o);
        return getMc().compare(d2, r2);
    }

    /**
     * Determines the relation of the given line and this circle.The relation may be
     * <i>include</i>,<i>inscribed</i>,<i>intersect</i>,<i>circumscribed</i>,<i>disjoint</i>.
     * If the two circles are the identity,the relation <i>circumscribed</i> will be returned.
     *
     * @param c a circle.
     * @return the relation of the circles.
     */
    public Relation relation(Circle<T> c) {
        //first compute the distance of two centers.
        T dis = c.o.distance(c.o);
        T rs = getMc().add(getRadius(), c.getRadius());
        int t = getMc().compare(dis, rs);
        if (t > 0) {
            return Relation.DISJOINT;
        } else if (t == 0) {
            return Relation.CIRCUMSCRIBED;
        }
        //o1o2 < r1+r2
        T rd = getMc().abs(getMc().subtract(r, c.r));
        t = getMc().compare(dis, rd);
        if (t > 0) {
            return Relation.INTERSECT;
        } else if (t == 0) {
            return Relation.INSCRIBED;
        } else {
            return Relation.INCLUDE;
        }
    }

    /**
     * Calculate the radical axis of point {@code p}.The line will be
     * <pre>(p.x-o.x)(x-o.x)+(p.y-o.y)(y-o.y) = 0</pre>
     * When {@code p} is on this circle,the line will be the tangent line,
     * when {@code p} is outside the circle,the line will be the connecting line of
     * the two points of tangency,
     * when {@code p} is inside the circle,the line has no direct geometric meaning,
     * but when {@code p} is the center of the circle,the line doesn't exist and {@code null} will
     * be returned.
     *
     * @param p a point.
     * @return a line or {@code null} if {@code p} is equal to the center of this circle.
     */
    public Line<T> radicalAxis(Point<T> p) {
        T a = getMc().subtract(p.x, o.x);
        T b = getMc().subtract(p.y, o.y);
        if (getMc().isZero(a) && getMc().isZero(b)) {
            return null;
        }
        T c = getMc().add(getMc().multiply(a, o.x), getMc().multiply(b, o.y));
        c = getMc().negate(getMc().add(c, r2));
        return Line.generalFormula(a, b, c, getMc());
    }

    /**
     * Returns a directed tangent line that has a direct vector (x,y).
     *
     * @param x
     * @param y
     * @return a list of two lines
     */
    public List<Line<T>> directedTanLine(T x, T y) {
        T t1 = getMc().add(getMc().multiply(o.x, x), getMc().multiply(o.y, y));
        T t2 = getMc().multiply(t1, t1);
        T ca = getMc().getOne();
        T cb = getMc().multiplyLong(t1, 2l);
        T cc = getMc().subtract(t2, getMc().multiply(r2, getMc().add(getMc().multiply(x, x), getMc().multiply(y, y))));
        List<T> c = EquationSup.INSTANCE.solveQuadraticR(ca, cb, cc, getMc());
        List<Line<T>> re = new ArrayList<>(2);
        re.add(Line.generalFormula(x, y, c.get(0), getMc()));
        re.add(Line.generalFormula(x, y, c.get(1), getMc()));
        return re;
    }


    /**
     * Get the intersect points of the line {@code l} and this circle.This method will
     * return a list of points and the number of the points are determined by the relation
     * of {@code l} and this circle.
     *
     * @param l a line
     * @return a list of intersect points,may contain 0,1 or 2 elements.
     */
    @Override
    public List<Point<T>> intersectPoints(Line<T> l) {
//		if(mc.isZero(l.b)){
//		//x = ?
//			T x = l.getInterceptX();
//			T a = B;
//			T b = E;
//			T c = mc.add(square(x), mc.add(mc.multiply(D, x), F));
//			List<T> so = MathUtils.solveEquation(a, b, c, mc);
//			List<Point<T>> list = new ArrayList<Point<T>>(so.size());
//			for(T t : so){
//				list.add(new Point<>(mc,x,t));
//			}
//			return list;
//		}
//		T b0_2 = square(l.b);
//		T a = mc.add(square(l.a), b0_2);
//		T b = mc.add(mc.multiplyLong(mc.multiply(l.a, l.c), 2l), 
//				mc.subtract(mc.multiply(D, b0_2), 
//						mc.multiply(E, mc.multiply(l.a, l.b))));
//		T c = mc.add(square(l.c), 
//				mc.subtract(mc.multiply(F, b0_2), mc.multiply(E, mc.multiply(l.b, l.c))));
//		List<T> so = MathUtils.solveEquation(a, b, c, mc);
//		List<Point<T>> list = new ArrayList<Point<T>>(so.size());
//		for(T t : so){
//			list.add(new Point<>(mc,t,l.computeY(t)));
//		}
        return super.intersectPoints(l);
    }

    /**
     * Returns the tangent lines of the point {@code p} to this circle.
     *
     * @param p
     * @return
     */
    public List<Line<T>> tangentLines(Point<T> p) {
        Line<T> ra = radicalAxis(p);
        if (contains(p)) {
            return Arrays.asList(ra);
        }
        List<Point<T>> ps = intersectPoints(ra);
        List<Line<T>> list = new ArrayList<>(ps.size());
        for (Point<T> t : ps) {
            list.add(Line.twoPoint(p, t, getMc()));
        }
        return list;
    }

    /**
     * Computes the intersect points of the two circles.The returned list may contain
     * {@code 0,1,2} elements ,which is determined by the two circles.If the two circles
     * are the identity,{@code null} will be returned because they have infinity intersect points.
     * <p>Generally speaking,
     * if two circles' relation is intersect ,the list will contain two points;if
     * two circles are tangent,the list will contain only one point;if the two circles
     * are separate,then the list will be empty.
     *
     * @param c another circle.
     * @return the list of points,or {@code null} if the two circle are the identity.
     */
    public List<Point<T>> intersectPoints(Circle<T> c) {
        //first check whether the two circles are the identity
        if (valueEquals(c)) {
            return null;
        }
        Line<T> sc = sharedChord(c);
        return intersectPoints(sc);
    }

    /**
     * Compute the line of the shared chord of {@code this} and {@code c}.Except checking
     * whether the two circles have the identity center,
     * this method just
     * simply calculate as the following formula:
     * <pre>(D-c.D)x + (E-c.E)y + F-c.F = 0</pre>
     * If the two circles have the identity center,{@code null} will be returned.
     *
     * @param c another circle
     * @return line of the shared chord
     */
    public Line<T> sharedChord(Circle<T> c) {
        if (o.valueEquals(c.o)) {
            return null;
        }
        T aL = getMc().subtract(D, c.D);
        T bL = getMc().subtract(E, c.E);
        T cL = getMc().subtract(F, c.F);
        return Line.generalFormula(aL, bL, cL, getMc());
    }


    /**
     * Calculate the chord length of the line {@code l} in this circle.This method is equal to
     * calculate the distance of the two intersect points.If {@code l} doesn't intersect with
     * this circle,{@code null} will be returned.
     *
     * @param l a line
     * @return the length of the chord,or {@code null}
     */
    public T chordLength(Line<T> l) {
        T len2 = chordLengthSq(l);
        try {
            if (getMc().compare(len2, getMc().getZero()) < 0) {
                return null;
            }
        } catch (UnsupportedOperationException ex) {
            //disable check
        }
        return getMc().squareRoot(len2);
    }

    /**
     * Calculate the square of the chord length of the line {@code l} in this circle.This method is equal to
     * calculate the square of the distance of the two intersect points.If {@code l} doesn't intersect with
     * this circle,a negative result will be returned.
     *
     * @param l a line
     * @return the square of the length of the chord,or {@code null}
     */
    public T chordLengthSq(Line<T> l) {
        T d2 = l.distanceSq(o);
        return getChordLengthSq(d2);
    }

    /**
     * Returns a circle that is symmetry to {@code this} by the line {@code l}.
     *
     * @param l a line
     * @return a circle
     */
    public Circle<T> symmetryCircle(Line<T> l) {
        Point<T> sp = l.symmetryPoint(o);
        Circle<T> c = centerAndRadiusSquare(sp, r2, getMc());
        c.r = r;
        return c;
    }

    /**
     * Returns a list of common tangent lines of this two circles only if the two circles' are not the identity
     * and their relation is
     * <i>inscribed</i>,<i>intersect</i>,<i>circumscribed</i> or <i>disjoint</i>.
     * <li>If relation is <i>disjoint</i>,the returned list will contain 4 lines.
     * <li>If relation is <i>circumscribed</i>,the returned list will contain 3 lines.
     * <li>If relation is <i>intersect</i>,the returned list will contain 2 lines.
     * <li>If relation is <i>inscribed</i>,the returned list will contain 1 line.
     * <li>If the statement is not as required,an empty list will be returned.
     *
     * @param cir another circle.
     * @return a list of lines.
     */
    public List<Line<T>> commonTangentLine(Circle<T> cir) {
        List<Line<T>> list = new ArrayList<>(4);
        T p = getMc().divide(getRadius(), cir.getRadius());
        Point<T> m1 = o.proportionPoint(cir.o, p);
        Point<T> m2 = o.proportionPoint(cir.o, getMc().negate(p));
        for (Line<T> l : tangentLines(m1)) {
            list.add(l);
        }
        for (Line<T> l : tangentLines(m2)) {
            list.add(l);
        }

        return list;
    }

    /**
     * Returns a list of outer common tangent lines of this two circles.
     *
     * @param cir a circle
     * @return a list of outer common tangent line
     * @see #commonTangentLine(Circle)
     */
    public List<Line<T>> outerCommonTangentLine(Circle<T> cir) {
        T p = getMc().divide(getRadius(), cir.getRadius());
        Point<T> m2 = o.proportionPoint(cir.o, getMc().negate(p));
        return tangentLines(m2);
    }

    /**
     * Returns a list of inner common tangent lines of this two circles.
     *
     * @param cir a circle
     * @return a list of inner common tangent lines
     * @see #commonTangentLine(Circle)
     */
    public List<Line<T>> innerCommonTangentLine(Circle<T> cir) {
        T p = getMc().divide(getRadius(), cir.getRadius());
        Point<T> m1 = o.proportionPoint(cir.o, p);
        return tangentLines(m1);
    }


    @Override
    public T substitute(T x, T y) {
        T re = getMc().add(getMc().multiply(x, x), getMc().multiply(y, y));
        re = getMc().add(re, getMc().multiply(x, D));
        re = getMc().add(re, getMc().multiply(y, E));
        return getMc().add(re, F);
    }


    /* (non-Javadoc)
     * @see cn.ancono.math.geometry.analytic.planeAG.ClosedCurve#computeArea()
     */
    @Override
    public T computeArea() {
        return getArea();
    }

    /* (non-Javadoc)
     * @see cn.ancono.math.geometry.analytic.planeAG.ClosedCurve#computeLength()
     */
    @Override
    public T computeLength() {
        return getPerimeter();
    }


    /**
     * Returns {@code this} because it is already the normalized form.
     */
    @Override
    public Pair<TransMatrix<T>, ConicSection<T>> normalizeAndTrans() {
        return new Pair<>(TransMatrix.identityTrans(getMc()), this);
    }

    /**
     * Returns {@code this} because it is already the normalized form.
     */
    @Override
    public ConicSection<T> normalize() {
        return this;
    }

    /**
     * Returns {@code this} because it is already the normalized form.
     */
    @Override
    public Pair<PAffineTrans<T>, ConicSection<T>> toStandardFormAndTrans() {
        return new Pair<>(PAffineTrans.identity(getMc()), this);
    }

    /**
     * Returns {@code this} because it is already the normalized form.
     */
    @Override
    public ConicSection<T> toStandardForm() {
        return this;
    }


    @NotNull
    @Override
    public <N> Circle<N> mapTo(@NotNull EqualPredicate<N> newCalculator, @NotNull Function<T, N> mapper) {
        Point<N> op = o.mapTo(newCalculator, mapper);
        N r2 = mapper.apply(this.r2);
        N r = this.r == null ? null : mapper.apply(this.r);
        N D = mapper.apply(this.D);
        N E = mapper.apply(this.E);
        N F = mapper.apply(this.F);
        return new Circle<N>((RealCalculator<N>) newCalculator, op, r2, D, E, F, r);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Circle) {
            Circle<?> c = (Circle<?>) obj;
            return o.equals(c.o) &&
                    r.equals(c.r);
        }
        return false;
    }


    @Override
    public boolean valueEquals(@NotNull IMathObject<T> obj) {
        if (obj instanceof Circle) {
            Circle<T> c = (Circle<T>) obj;
            return o.valueEquals(c.o) &&
                    getMc().isEqual(r, c.r);

        }
        return false;
    }


    /**
     * Create a circle with the given point {@code o} as its center and {@code r} as the radius.
     *
     * @param o  center of this circle
     * @param r  the radius of this circle,positive.
     * @param mc a {@link RealCalculator}
     * @return a new circle
     * @throws IllegalArgumentException if {@code r<=0}
     */
    public static <T> Circle<T> centerAndRadius(Point<T> o, T r, RealCalculator<T> mc) {
        try {
            if (mc.compare(r, mc.getZero()) <= 0) {
                throw new IllegalArgumentException("Not Positive Radius");
            }
        } catch (UnsupportedOperationException ex) {
            //disable range check
        }
        T r2 = mc.multiply(r, r);
        T D = mc.multiplyLong(o.x, -2);
        T E = mc.multiplyLong(o.y, -2);
        T F = mc.subtract(mc.add(mc.multiply(o.x, o.x), mc.multiply(o.y, o.y)), r2);
//		Printer.print(F);
        return new Circle<T>(mc, o.mapTo(mc, MathFunction.identity()), r2, D, E, F, r);
    }

    /**
     * Create a circle with the given point {@code o} as its center and {@code r} as the radius's square.
     *
     * @param o  center of this circle
     * @param r2 the square of the radius of this circle,positive.
     * @param mc a {@link RealCalculator}
     * @return a new circle
     * @throws IllegalArgumentException if {@code r<=0}
     */
    public static <T> Circle<T> centerAndRadiusSquare(Point<T> o, T r2, RealCalculator<T> mc) {
        try {
            if (mc.compare(r2, mc.getZero()) <= 0) {
                throw new IllegalArgumentException("Not Positive Radius");
            }
        } catch (UnsupportedOperationException ex) {
            //disable range check
        }
        T D = mc.multiplyLong(o.x, -2);
        T E = mc.multiplyLong(o.y, -2);
        T F = mc.subtract(mc.add(mc.multiply(o.x, o.x), mc.multiply(o.y, o.y)), r2);
//		Printer.print(F);
        return new Circle<T>(mc, o.mapTo(mc, MathFunction.identity()), r2, D, E, F, null);
    }


    /**
     * Create a circle that fits the general formula:
     * <pre>
     * x² + y² + Dx + Ey + F = 0
     * </pre>
     * The argument D,E,F are required that {@literal D²+E²-4F > 0}
     *
     * @param D  coefficient
     * @param E  coefficient
     * @param F  coefficient
     * @param mc a {@link RealCalculator}
     * @return a new circle
     * @throws IllegalArgumentException if {@literal D²+E²-4F <= 0}
     */
    public static <T> Circle<T> generalFormula(T D, T E, T F, RealCalculator<T> mc) {
        //check first.
        T delta = mc.subtract(mc.add(mc.multiply(D, D), mc.multiply(E, E)), mc.multiplyLong(F, 4l));
        try {
            if (mc.compare(delta, mc.getZero()) <= 0) {
                throw new IllegalArgumentException("Delta <= 0");
            }
        } catch (UnsupportedOperationException ex) {
            //disable range check
        }
        Point<T> o = new Point<>(mc, mc.divideLong(D, -2l), mc.divideLong(E, -2l));
        T r2 = mc.divideLong(delta, 4l);
        return new Circle<T>(mc, o, r2, D, E, F, null);
    }

    /**
     * Create a circle that cross the three points.The three points should not be in a line.
     *
     * @param p1 a point
     * @param p2 a point
     * @param p3 a point
     * @param mc a {@link RealCalculator}
     * @return a circle
     * @throws IllegalArgumentException if {@code p1,p2,p3} is on the identity line.
     */
    public static <T> Circle<T> threePoints(Point<T> p1, Point<T> p2, Point<T> p3, RealCalculator<T> mc) {
        Triangle<T> tri = Triangle.fromVertex(mc, p1.x, p1.y
                , p2.x, p2.y
                , p3.x, p3.y);
        Point<T> o = tri.centerO();
        T r2 = o.distanceSq(p1);
        T D = mc.multiplyLong(o.x, -2);
        T E = mc.multiplyLong(o.y, -2);
        T F = mc.subtract(mc.add(mc.multiply(o.x, o.x), mc.multiply(o.y, o.y)), r2);
        return new Circle<T>(mc, o, r2, D, E, F, null);
    }

    /**
     * Create a circle that cross the three points.The three points should not be in a line.
     *
     * @param p1 a point
     * @param p2 a point
     * @param p3 a point
     * @return a circle
     * @throws IllegalArgumentException if {@code p1,p2,p3} is on the identity line.
     */
    public static <T> Circle<T> threePoints(Point<T> p1, Point<T> p2, Point<T> p3) {
        RealCalculator<T> mc = (RealCalculator<T>) p1.getCalculator();
        Triangle<T> tri = Triangle.fromVertex(mc, p1.x, p1.y
                , p2.x, p2.y
                , p3.x, p3.y);
        Point<T> o = tri.centerO();
        T r2 = o.distanceSq(p1);
        T D = mc.multiplyLong(o.x, -2);
        T E = mc.multiplyLong(o.y, -2);
        T F = mc.subtract(mc.add(mc.multiply(o.x, o.x), mc.multiply(o.y, o.y)), r2);
        return new Circle<T>(mc, o, r2, D, E, F, null);
    }

    /**
     * Creates a circle whose diameter is the connecting line of point {@code p1} and {@code p2}.
     * The method will use the formula that:
     * <pre>(x-p1.x)(x-p2.x) + (y-p1.y)(y-p2.y)=0</pre>
     *
     * @param p1 a point
     * @param p2 a point
     * @param mc a {@link RealCalculator}
     * @return a circle
     * @throws IllegalArgumentException if {@code p1.valueEquals(p2)}
     */
    public static <T> Circle<T> diameterPoint(Point<T> p1, Point<T> p2, RealCalculator<T> mc) {
        if (p1.valueEquals(p2)) {
            throw new IllegalArgumentException("p1 = p2");
        }
        Point<T> o = p1.middle(p2);
        T D = mc.negate(mc.add(p1.x, p2.x));
        T E = mc.negate(mc.add(p1.y, p2.y));
        T F = mc.add(mc.multiply(p1.x, p2.x), mc.multiply(p1.y, p2.y));
        T r2 = o.distanceSq(p1);
        return new Circle<T>(mc, o, r2, D, E, F, null);
    }

    /**
     * Returns a circle whose center is the point {@code p} and is tangent to the line {@code l}.
     * The point {@code p} should not lie on {@code l}.
     *
     * @param l  a line
     * @param p  a point
     * @param mc a {@link RealCalculator}
     * @return a circle
     * @throws IllegalArgumentException if {@code l.containsPoint(p)}
     */
    public static <T> Circle<T> tangentCircle(Line<T> l, Point<T> p, RealCalculator<T> mc) {
        if (l.contains(p)) {
            throw new IllegalArgumentException("point is on the line");
        }
        Function<T, T> mapper = MathFunction.identity();
        l = l.mapTo(mc, mapper);
        p = p.mapTo(mc, mapper);
        T r2 = l.distanceSq(p);
        T D = mc.multiplyLong(p.x, -2);
        T E = mc.multiplyLong(p.y, -2);
        T F = mc.subtract(mc.add(mc.multiply(p.x, p.x), mc.multiply(p.y, p.y)), r2);
        return new Circle<T>(mc, p, r2, D, E, F, null);
    }


}
