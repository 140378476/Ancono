package cn.ancono.math.geometry.analytic.plane.curve;

import cn.ancono.math.IMathObject;
import cn.ancono.math.algebra.abs.calculator.EqualPredicate;
import cn.ancono.math.equation.SVPEquation;
import cn.ancono.math.equation.SVPEquation.QEquation;
import cn.ancono.math.function.MathFunction;
import cn.ancono.math.geometry.analytic.plane.Line;
import cn.ancono.math.geometry.analytic.plane.LineSup;
import cn.ancono.math.geometry.analytic.plane.Point;
import cn.ancono.math.numberModels.api.RealCalculator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

;

/**
 * Ellipse is a special kind of conic section,it is a set of point that
 * <pre>{p|d(p,f1)+d(p,f2)=2a}</pre>,where {@code a} is a constant and is equal to a half of the length of major axis.
 * <p>
 * In this class of Ellipse,the ellipse's center is always at the point O(0,0),and its foci are
 * on either coordinate axis X or Y. The reason why the ellipse is set like this is to reduce
 * the calculation.
 * <p>
 * In an ellipse,there are two foci,we usually call them {@code F1,F2},
 * and the distance between the two foci is equal to {@code 2c}.
 * The value {@code 2a} is equal to the length of major axis,
 * value {@code 2b} that {@code a^2-c^2=b^2} is the length of the semi-major axis.
 * There is also a boolean value to indicate whether the ellipse's foci are on X axis or Y axis.
 * <p>
 * The standard equation of this ellipse is
 * <pre>x^2/a^2 + y^2/b^2 = 1</pre>
 * or
 * <pre>x^2/b^2 + y^2/a^2 = 1</pre>
 * determined by whether foci are on X axis.
 *
 * @author lyc
 */
public final class EllipseV<T> extends EHSection<T> implements ClosedCurve<T> {

    protected EllipseV(RealCalculator<T> mc, T A, T C, T a, T b, T c, T a2, T b2, T c2, boolean onX) {
        super(mc, A, C, a, b, c, a2, b2, c2, onX);
    }

    @Override
    public List<Point<T>> vertices() {
        var mc = getMc();
        List<Point<T>> list = new ArrayList<>(2);
        T zero = mc.getZero();
        if (onX) {
            addVertices(mc, list, zero, a, b);
        } else {
            addVertices(mc, list, zero, b, a);
        }
        return list;
    }

    private void addVertices(RealCalculator<T> mc, List<Point<T>> list, T zero, T a, T b) {
        list.add(new Point<>(mc, a, zero));
        list.add(new Point<>(mc, mc.negate(a), zero));
        list.add(new Point<>(mc, zero, b));
        list.add(new Point<>(mc, zero, mc.negate(b)));
    }

    private int comp0 = 0;

    private void computeComp() {
        comp0 = -getMc().compare(substitute(Point.pointO(getMc())), getMc().getZero());
    }

    private int computeRelation(Point<T> p) {
        return getMc().compare(substitute(Point.pointO(getMc())), getMc().getZero());
    }

    /* (non-Javadoc)
     * @see cn.ancono.math.geometry.analytic.planeAG.ClosedCurve#isInside(cn.ancono.math.geometry.analytic.planeAG.Point)
     */
    @Override
    public boolean isInside(Point<T> p) {
        if (comp0 == 0) {
            computeComp();
        }
        return comp0 * computeRelation(p) < 0;
    }

    /* (non-Javadoc)
     * @see cn.ancono.math.geometry.analytic.planeAG.ClosedCurve#isOutside(cn.ancono.math.geometry.analytic.planeAG.Point)
     */
    @Override
    public boolean isOutside(Point<T> p) {
        if (comp0 == 0) {
            computeComp();
        }
        return comp0 * computeRelation(p) > 0;
    }

    /* (non-Javadoc)
     * @see cn.ancono.math.geometry.analytic.planeAG.ClosedCurve#relation()
     */
    @Override
    public int relation(Point<T> p) {
        if (comp0 == 0) {
            computeComp();
        }
        return comp0 * computeRelation(p);
    }

    /* (non-Javadoc)
     * @see cn.ancono.math.geometry.analytic.planeAG.ClosedCurve#computeArea()
     */
    @Override
    public T computeArea() {
        return getArea();
    }

    @Override
    public cn.ancono.math.geometry.analytic.plane.curve.ConicSection.Type determineType() {
        return ConicSection.Type.ELLIPSE;
    }

    /**
     * Computes the area of this ellipse,which is computed by the formula {@literal S = ��ab}<p>
     * This method requires the
     * constant value of {@literal pi} is available in the calculator.
     *
     * @return the area of this ellipse
     */
    public T getArea() {
        T pi = getMc().constantValue(RealCalculator.STR_PI);
        return getMc().multiply(pi, getMc().multiply(a, b));
    }

    /**
     * Computes the distance from point on this ellipse that is
     * {@code (p,y)} if this ellipse is on X axis,
     * or point {@code (x,p)} if this ellipse is on Y axis, to the left focus.
     *
     * @param p the coordinate X or Y of the point.
     * @return the distance
     */
    public T distanceFL(T p) {
        T x = getMc().multiply(getEccentricity(), p);
        return getMc().add(x, a);
    }

    /**
     * Computes the distance from point on this ellipse that is
     * {@code (p,y)} if this ellipse is on X axis,
     * or point {@code (x,p)} if this ellipse is on Y axis, to the right focus.
     *
     * @param p the coordinate X or Y of the point.
     * @return the distance
     */
    public T distanceFR(T p) {
        T x = getMc().multiply(getEccentricity(), p);
        return getMc().subtract(a, x);
    }

    /**
     * Computes the corresponding positive value x of the given value {@code y}
     *
     * @param y y coordinate of a point.
     * @return the x coordinate.
     */
    @Override
    public T computeX(T y) {
        if (onX) {
            return computeX0(y);
        } else {
            return computeY0(y);
        }
    }

    private T computeX0(T y) {
        T t = getMc().subtract(b2, square(y));
        t = getMc().squareRoot(t);
        t = getMc().multiply(getMc().divide(a, b), t);
        return t;
    }

    private T computeY0(T x) {
        T t = getMc().subtract(a2, square(x));
        t = getMc().squareRoot(t);
        t = getMc().multiply(getMc().divide(b, a), t);
        return t;
    }

    /**
     * Computes the corresponding positive value y of the given value {@code x }
     *
     * @param x x coordinate of a point.
     * @return the y coordinate.
     */
    @Override
    public T computeY(T x) {
        if (onX) {
            return computeY0(x);
        } else {
            return computeX0(x);
        }
    }

    /**
     * Returns both of the lines whose normal vector is {@code (a,b)} and is
     * tangent to this ellipse.
     *
     * @param a
     * @param b
     * @return a list of lines that {@code ax+by+c = 0}.
     */
    public List<Line<T>> directTanLine(T a, T b) {
        T c = directTanLineC(a, b);
        List<Line<T>> re = new ArrayList<>(2);
        re.add(Line.generalFormula(a, b, c, getMc()));
        re.add(Line.generalFormula(a, b, getMc().negate(c), getMc()));
        return re;
    }

    /**
     * Assume that the line {@code ax+by+c = 0}
     * is tangent line to this ellipse,this method will
     * return {@code |c|}.
     *
     * @param a
     * @param b
     * @return |c|
     */
    public T directTanLineC(T a, T b) {
        if (onX)
            return getMc().squareRoot(getMc().add(getMc().multiply(square(a), a2), getMc().multiply(square(b), b2)));
        else
            return getMc().squareRoot(getMc().add(getMc().multiply(square(a), b2), getMc().multiply(square(b), a2)));
    }

    /**
     * Returns the intersect point(s) of the {@code line} and this ellipse.
     *
     * @param line a line
     * @return a list of points, or empty
     */
    @Override
    public List<Point<T>> intersectPoints(Line<T> line) {
        //transform the line to y = kx+b.
        T k = line.slope();
        if (k == null) {
            //must be x = ...
            T x = line.getInterceptX();
            int t = getMc().compare(getMc().abs(x), onX ? a : b);

            if (t < 0) {
                //two point
                T y = computeY(x);
                List<Point<T>> list = new ArrayList<>(2);
                list.add(Point.valueOf(x, y, getMc()));
                list.add(Point.valueOf(x, getMc().negate(y), getMc()));
                return list;
            } else if (t == 0) {
                //y == 0
                List<Point<T>> list = new ArrayList<>(1);
                list.add(Point.valueOf(x, getMc().getZero(), getMc()));
                return list;
            } else {
                return Collections.emptyList();
            }
        }

        List<T> sol = createEquation0(line).solveR();
        List<Point<T>> re = new ArrayList<>(sol.size());
        for (T t : sol) {
            re.add(Point.valueOf(t, line.computeY(t), getMc()));
        }
        return re;
    }

    /**
     * Returns the chord length of the line in this ellipse. If this line doesn't not intersect with
     * this ellipse ,{@code null} will be returned,and if the line is a tangent line,0 will be returned.
     *
     * @param line
     * @return the chord length,or null
     */
    @Override
    public T chordLength(Line<T> line) {
        T k = line.slope();
        var mc = getMc();
        if (k == null) {
            T x = line.getInterceptX();
            int t = mc.compare(mc.abs(x), onX ? a : b);
            if (t < 0) {
                if (onX) {
                    return mc.squareRoot(mc.subtract(b2, mc.multiply(mc.divide(b2, a2), square(x))));
                } else {
                    return mc.squareRoot(mc.subtract(a2, mc.multiply(mc.divide(a2, b2), square(x))));
                }
            } else if (t == 0) {
                return mc.getZero();
            } else {
                return null;
            }
        }
        QEquation<T> equa = createEquation0(line);
        int rn = 2;
        try {
            rn = equa.getNumberOfRoots();
        } catch (UnsupportedOperationException ex) {
            //ignore
        }
        if (rn == 0) {
            return null;
        } else if (rn == 1) {
            return mc.getZero();
        }
        T re = mc.multiply(equa.delta(), mc.add(mc.getOne(), square(k)));
        re = mc.squareRoot(re);
        return mc.divide(re, equa.coeA());
    }

    /**
     * Returns square of the chord length of the line in this ellipse. If this line doesn't not intersect with
     * this ellipse ,{@code null} will be returned.
     *
     * @param line
     * @return the square of the chord length,or null
     */
    @Override
    public T chordLengthSq(Line<T> line) {
        T k = line.slope();
        var mc = getMc();
        if (k == null) {
            T x = line.getInterceptX();
            int t = mc.compare(mc.abs(x), onX ? a : b);
            if (t < 0) {
                if (onX) {
                    return mc.multiplyLong(mc.subtract(b2, mc.multiply(mc.divide(b2, a2), square(x))), 4l);
                } else {
                    return mc.multiplyLong(mc.subtract(a2, mc.multiply(mc.divide(a2, b2), square(x))), 4l);
                }
            } else if (t == 0) {
                return mc.getZero();
            } else {
                return null;
            }
        }
        QEquation<T> equa = createEquation0(line);
        int rn = 2;
        try {
            rn = equa.getNumberOfRoots();
        } catch (UnsupportedOperationException ex) {
            //ignore
        }
        if (rn == 0) {
            return null;
        } else if (rn == 1) {
            return mc.getZero();
        }
        T re = mc.multiply(equa.delta(), mc.add(mc.getOne(), square(k)));
        re = mc.divide(re, square(equa.coeA()));
        return re;
    }

    private QEquation<T> createEquation0(Line<T> line) {
        T k = line.slope();
        // solve an equation:
        T c = line.getInterceptY();
        T ea, eb, ec;

        var mc = getMc();

        if (onX) {
            ea = mc.add(b2, mc.multiply(a2, square(k)));
            eb = mc.multiplyLong(mc.multiply(a2, mc.multiply(k, c)), 2l);
            ec = mc.multiply(a2, mc.subtract(square(c), b2));
//			Printer.print("ec = "+ec);
        } else {
            ea = mc.add(a2, mc.multiply(b2, square(k)));
            eb = mc.multiplyLong(mc.multiply(b2, mc.multiply(k, c)), 2l);
            ec = mc.multiply(b2, mc.subtract(square(c), a2));
        }
        return SVPEquation.quadratic(ea, eb, ec, mc);
    }

    /**
     * Create the equation of {@code x} from the line and this ellipse.this method doesn't
     * require that {@code line} is not parallel to Y axis and if the situation occurs,
     * assume the line is {@code x = d}, then {@code (x-d)^2 = 0} will be returned.
     *
     * @param line a line.
     * @return an equation of {@code x} indicating the two intersect points of the line
     * and this ellipse.
     */
    @Override
    public QEquation<T> createEquationX(Line<T> line) {
        T k = line.slope();
        if (k == null) {
            return QEquation.perfectSquare(line.getInterceptX(), getMc());
        }
        return createEquation0(line);
    }

    /**
     * Create the equation of {@code y} from the line and this ellipse.this method doesn't
     * require that {@code line} is not parallel to X axis and if the situation occurs,
     * assume the line is {@code y = d}, then {@code (y-d)^2 = 0} will be returned.
     *
     * @param line a line.
     * @return an equation of {@code y} indicating the two intersect points of the line
     * and this ellipse.
     */
    @Override
    public QEquation<T> createEquationY(Line<T> line) {
        T a = line.getA();
        if (getMc().isZero(a)) {
            return QEquation.perfectSquare(line.getInterceptY(), getMc());
        }
        T b = line.getB();
        T k = getMc().negate(getMc().divide(b, a));
        // solve an equation:
        T c = line.getInterceptX();
        T ea, eb, ec;
        if (!onX) {
            ea = getMc().add(b2, getMc().multiply(a2, square(k)));
            eb = getMc().multiplyLong(getMc().multiply(a2, getMc().multiply(k, c)), 2l);
            ec = getMc().multiply(a2, getMc().subtract(square(c), b2));
//			Printer.print("ec = "+ec);
        } else {
            ea = getMc().add(a2, getMc().multiply(b2, square(k)));
            eb = getMc().multiplyLong(getMc().multiply(b2, getMc().multiply(k, c)), 2l);
            ec = getMc().multiply(b2, getMc().subtract(square(c), a2));
        }
        return SVPEquation.quadratic(ea, eb, ec, getMc());
    }

    /**
     * Returns a line that passes through the middle point of the chord.This method will not check whether the
     * given chord is actually intersect with this ellipse.
     *
     * @param chord
     * @return a line that passes through the middle point.
     */
    public Line<T> chordMPL(Line<T> chord) {
        //we use the formula that k1 * k2 = - b^2 / a^2
        T k = chord.slope();
        if (k == null) {
            //return X axis
            return LineSup.xAxis(getMc());
        }
        T k1 = getMc().negate(getMc().divide(getMc().divide(b2, a2), k));
        return Line.pointSlope(Point.pointO(getMc()), k1, getMc());
    }

    /**
     * Returns the point's distance to the left focus,which is calculated by the formula
     * {@code a-ex}.This point must be on the ellipse.
     *
     * @param p a point
     * @return the distance
     */
    @Override
    public T focusDL(Point<T> p) {
        if (!contains(p)) {
            throw new IllegalArgumentException("Point not on ellipse");
        }
        T t = onX ? p.x : p.y;
        return getMc().subtract(a, getMc().multiply(getEccentricity(), t));
    }

    /**
     * Returns the point's distance to the right focus,which is calculated by the formula
     * {@code a+ex}.This point must be on the ellipse.
     *
     * @param p a point
     * @return the distance
     */
    @Override
    public T focuseDR(Point<T> p) {
        if (!contains(p)) {
            throw new IllegalArgumentException("Point not on ellipse");
        }
        T t = onX ? p.x : p.y;
        return getMc().add(a, getMc().multiply(getEccentricity(), t));
    }

    /**
     * Returns the triangle's area whose vertices are the two foci and a point {@code P}on this ellipse,and
     * the size of the angle <i>F1PF2</i> is equal to {@code angle}(radical).This method will returns
     * the triangle's area and return {@code null} if the angle is too big.
     *
     * @param angle the angle
     * @param tan   a {@link MathFunction} of tangent
     * @return the area of the triangle.
     */
    public T trianlgeArea(T angle, MathFunction<T, T> tan) {
        angle = getMc().divideLong(angle, 2l);
        T tv = tan.apply(angle);
        try {
            if (getMc().compare(tv, getMc().getZero()) <= 0 || getMc().compare(tv, getMc().divide(c, b)) > 0) {
                throw new IllegalArgumentException("angle too big");
            }
        } catch (UnsupportedOperationException ex) {
            //ignore
        }
        return getMc().multiply(b2, tv);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Ellipse: x^2/(");
        if (onX) {
            sb.append(a2);
        } else {
            sb.append(b2);
        }
        sb.append(')')
                .append(" + y^2/(");
        if (!onX) {
            sb.append(a2);
        } else {
            sb.append(b2);
        }
        sb.append(") = 1");
        return sb.toString();
    }

    @Override
    public boolean valueEquals(@NotNull IMathObject<T> obj) {
        if (obj instanceof EllipseV) {
            EllipseV<T> ev = (EllipseV<T>) obj;
            if (ev.onX == onX) {
                return getMc().isEqual(ev.a, a) && getMc().isEqual(ev.b, b);
            }
            return false;
        }
        return super.valueEquals(obj);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EllipseV) {
            EllipseV<?> ev = (EllipseV<?>) obj;
            return ev.onX == onX && a.equals(ev.a) && b.equals(ev.b);
        }
        return false;
    }

    @NotNull
    @Override
    public <N> EllipseV<N> mapTo(@NotNull EqualPredicate<N> newCalculator, @NotNull Function<T, N> mapper) {
        EllipseV<N> nell = new EllipseV<N>((RealCalculator<N>) newCalculator, mapper.apply(A), mapper.apply(C)
                , mapper.apply(a), mapper.apply(b), mapper.apply(c)
                , mapper.apply(a2), mapper.apply(b2), mapper.apply(c2)
                , onX);


        nell.e = e == null ? null : mapper.apply(e);
        nell.f1 = f1 == null ? null : f1.mapTo(newCalculator, mapper);
        nell.f2 = f2 == null ? null : f2.mapTo(newCalculator, mapper);
        return nell;
    }

    /**
     * Creates a ellipse whose foci are {@code (-c,0),(c,0)}.
     * this ellipse
     *
     * @param a  half of the length of major axis
     * @param c  half of the distance between the two foci
     * @param mc a {@link RealCalculator}
     * @return a new EllipseV
     * @throws IllegalArgumentException if {@code a<c} or {@code c<=0}
     */
    public static <T> EllipseV<T> createEllipse(T a, T c, RealCalculator<T> mc) {
        return createEllipse(a, c, true, mc);
    }

    /**
     * Creates a ellipse whose foci are {@code (-c,0),(c,0)}.
     * this ellipse
     *
     * @param a   half of the length of major axis
     * @param c   half of the distance between the two foci
     * @param onX decides whether this ellipse's foci are on X axis.
     * @param mc  a {@link RealCalculator}
     * @return a new EllipseV
     * @throws IllegalArgumentException if {@code a<c} or {@code c<=0}
     */
    public static <T> EllipseV<T> createEllipse(T a, T c, boolean onX, RealCalculator<T> mc) {
        try {
            if (mc.compare(a, c) <= 0) {
                throw new IllegalArgumentException("a<c");
            }
            T zero = mc.getZero();
            if (mc.compare(c, zero) <= 0) {
                throw new IllegalArgumentException("c<=0");
            }
        } catch (UnsupportedOperationException ex) {
            //ignore it.
        }
        T a2 = mc.multiply(a, a);
        T c2 = mc.multiply(c, c);
        T b2 = mc.subtract(a2, c2);
        T b = mc.squareRoot(b2);
        return create0(a, b, c, a2, b2, c2, onX, mc);
    }

    static <T> EllipseV<T> create0(T a, T b, T c, T a2, T b2, T c2, boolean onX, RealCalculator<T> mc) {
        T A, C;
        if (onX) {
            A = mc.reciprocal(a2);
            C = mc.reciprocal(b2);
        } else {
            A = mc.reciprocal(b2);
            C = mc.reciprocal(a2);
        }

        return new EllipseV<>(mc, A, C,
                a, b, c,
                a2, b2, c2,
                onX);
    }

    /**
     * Creates a ellipse of
     * <pre>x^2/a^2 + y^2/b^2 = 1</pre>
     * This method requires the compare operation in {@code mc}.
     *
     * @param a  coefficient a
     * @param b  coefficient b
     * @param mc a {@link RealCalculator}
     * @return new EllipseV
     * @throws IllegalArgumentException if {@code a==b} or {@code a <= 0 || b <= 0}
     */
    public static <T> EllipseV<T> standardEquation(T a, T b, RealCalculator<T> mc) {

        int comp = mc.compare(a, b);
        if (comp > 0) {
            return standardEquation0(a, b, true, mc);
        } else if (comp == 0) {
            throw new IllegalArgumentException("a==b");
        } else {
            return standardEquation0(b, a, false, mc);
        }
    }

    private static <T> EllipseV<T> standardEquation0(T a, T b, boolean onX, RealCalculator<T> mc) {
        T a2 = mc.multiply(a, a);
        T b2 = mc.multiply(b, b);
        T c2 = mc.subtract(a2, b2);
        T c = mc.squareRoot(c2);
        return create0(a, b, c, a2, b2, c2, onX, mc);
    }


    /**
     * Creates an ellipse of
     * <pre>x^2/a^2 + y^2/b^2 = 1</pre> if {@code onX==true}
     * or <pre>y^2/a^2 + x^2/b^2 = 1</pre>
     *
     * @param a   coefficient a
     * @param b   coefficient b
     * @param onX decides the order of x y.
     * @param mc  a {@link RealCalculator}
     * @return new EllipseV
     * @throws IllegalArgumentException if {@code a==b} or {@code a <= 0 || b <= 0}
     */
    public static <T> EllipseV<T> standardEquation(T a, T b, boolean onX, RealCalculator<T> mc) {
        try {
            T zero = mc.getZero();
            if (mc.compare(a, zero) <= 0 || mc.compare(b, zero) <= 0) {
                throw new IllegalArgumentException("a <= 0 || b <= 0");
            }
            int t = mc.compare(a, b);
            if (t == 0) {
                throw new IllegalArgumentException("a == b");
            }
            if (onX ^ (t > 0)) {
                throw new IllegalArgumentException("a,b doesn't match argument onX");
            }
        } catch (UnsupportedOperationException ex) {
            //ignore
        }
        return standardEquation0(a, b, onX, mc);
    }

    /**
     * Creates an ellipse of
     * <pre>x^2/a2 + y^2/b2 = 1</pre> if {@code onX==true}
     * or <pre>y^2/a2 + x^2/b2 = 1</pre>
     *
     * @param a2  coefficient a2
     * @param b2  coefficient b2
     * @param onX decides the order of x y.
     * @param mc  a {@link RealCalculator}
     * @return new EllipseV
     * @throws IllegalArgumentException if {@code a2==b2}
     */
    public static <T> EllipseV<T> standardEquationSqrt(T a2, T b2, boolean onX, RealCalculator<T> mc) {
        try {
            int t = mc.compare(a2, b2);
            if (t == 0) {
                throw new IllegalArgumentException("a == b");
            }
            if (onX ^ (t > 0)) {
                throw new IllegalArgumentException("a,b doesn't match argument onX");
            }
        } catch (UnsupportedOperationException ex) {
            //ignore
        }
        T c2 = mc.subtract(a2, b2);
        T a = mc.squareRoot(a2);
        T b = mc.squareRoot(b2);
        T c = mc.squareRoot(c2);
        if (onX) {
            return create0(a, b, c, a2, b2, c2, onX, mc);
        } else {
            return create0(b, a, c, b2, a2, c2, onX, mc);
        }
    }

    /**
     * Creates an ellipse of
     * <pre>x^2/a2 + y^2/b2 = 1</pre> if {@code onX==true}
     * or <pre>y^2/a2 + x^2/b2 = 1</pre>
     *
     * @param a2 coefficient a2
     * @param b2 coefficient b2
     * @param mc a {@link RealCalculator}
     * @return new EllipseV
     * @throws IllegalArgumentException if {@code a2==b2}
     */
    public static <T> EllipseV<T> standardEquationSqrt(T a2, T b2, RealCalculator<T> mc) {
        boolean onX;
        int t = mc.compare(a2, b2);
        if (t == 0) {
            throw new IllegalArgumentException("a == b");
        } else {
            onX = t > 0;
        }
        T c2 = mc.subtract(a2, b2);
        T a = mc.squareRoot(a2);
        T b = mc.squareRoot(b2);
        T c = mc.squareRoot(c2);
        if (onX) {
            return create0(a, b, c, a2, b2, c2, onX, mc);
        } else {
            return create0(b, a, c, b2, a2, c2, onX, mc);
        }
    }

    /**
     * Creates an ellipse of
     * <pre>ax^2 + by^2 = d</pre>
     * the coefficient must match that
     * {@code a > 0 && b > 0 && c > 0 && a!=b}
     *
     * @param a
     * @param b
     * @param d
     * @return
     */
    public static <T> EllipseV<T> generalFormula(T a, T b, T d, RealCalculator<T> mc) {
        //compare the number
        if (mc.compare(a, mc.getZero()) <= 0
                || mc.compare(b, mc.getZero()) <= 0
                || mc.compare(d, mc.getZero()) <= 0) {
            throw new IllegalArgumentException("a<0||b<0||c<0");
        }
        int comp = mc.compare(a, b);
        if (comp == 0) {
            throw new IllegalArgumentException("a==b");
        } else if (comp < 0) {
            return general0(mc.divide(d, a), mc.divide(d, b), true, mc);
        } else {
            return general0(mc.divide(d, b), mc.divide(d, a), false, mc);
        }
    }

    private static <T> EllipseV<T> general0(T a2, T b2, boolean onX, RealCalculator<T> mc) {
        T a = mc.squareRoot(a2);
        T b = mc.squareRoot(b2);
        T c2 = mc.subtract(a2, b2);
        T c = mc.squareRoot(c2);
        return create0(a, b, c, a2, b2, c2, onX, mc);
    }

}
