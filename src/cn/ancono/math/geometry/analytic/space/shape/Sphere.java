/**
 *
 */
package cn.ancono.math.geometry.analytic.space.shape;

import cn.ancono.math.IMathObject;
import cn.ancono.math.MathObjectReal;
import cn.ancono.math.algebra.abs.calculator.EqualPredicate;
import cn.ancono.math.algebra.linear.LinearEquationSolution;
import cn.ancono.math.algebra.linear.Matrix;
import cn.ancono.math.algebra.linear.Vector;
import cn.ancono.math.function.MathFunction;
import cn.ancono.math.geometry.analytic.space.Line;
import cn.ancono.math.geometry.analytic.space.SPoint;
import cn.ancono.math.numberModels.api.RealCalculator;
import cn.ancono.utilities.Printer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

;

/**
 * @author liyicheng
 *
 */
public final class Sphere<T> extends SpaceObject<T> {
    /**
     * Describes the relation between two Spheres.
     * @author lyc
     *
     */
    public enum Relation {
        /**
         * Relation that one Sphere contains another Sphere and they don't intersect with each other.
         */
        INCLUDE,
        /**
         * Relation that two Spheres intersect only at one point and
         * one Sphere contains another Sphere.
         */
        INSCRIBED,
        /**
         * Relation that two Spheres intersect at a circle.
         */
        INTERSECT,
        /**
         * Relation that two Spheres intersect only at one point and they are
         * separate.
         */
        CIRCUMSCRIBED,
        /**
         * Relation that two Spheres don't intersect with each other and they are
         * separate.
         */
        DISJOINT;

    }

    private final T r2;
    private T r;
    private final SPoint<T> o;

    /**
     * @param mc
     */
    protected Sphere(RealCalculator<T> mc, T r, T r2, SPoint<T> o) {
        super(mc);
        this.r = r;
        this.o = o;
        this.r2 = r2;
    }

    @NotNull
    @Override
    public RealCalculator<T> getCalculator() {
        return (RealCalculator<T>) super.getCalculator();
    }

    /**
     * Gets the radius of this sphere
     *
     * @return
     */
    public T getRadius() {
        if (r == null) {
            r = getCalculator().squareRoot(r2);
        }
        return r;
    }

    /**
     * Gets the center of this sphere.
     * @return
     */
    public SPoint<T> getCenter() {
        return o;
    }

    /**
     * Gets the square of the radius of this sphere.
     * @return
     */
    public T getRadiusSquare() {
        return r2;
    }

    private T surfaceArea;

    /**
     * Returns the surface area of this sphere, constant value <tt>PI</tt> is needed.
     */
    @Override
    public T surfaceArea() {
        if (surfaceArea == null) {
            surfaceArea = getCalculator().multiplyLong(getCalculator().multiply(r2,
                    getCalculator().constantValue(RealCalculator.STR_PI)), 2l);
        }
        return surfaceArea;
    }

    private T volume;

    /**
     * Returns the volume of this sphere, constant value <tt>PI</tt> is needed.
     */
    @Override
    public T volume() {
        if (volume == null) {
            volume = getCalculator().divideLong(getCalculator().multiplyLong(getCalculator().multiply(
                    getCalculator().multiply(r2, getRadius()), getCalculator().constantValue(RealCalculator.STR_PI)), 4l), 3l);
        }
        return volume;
    }

    /**
     * Gets the diameter of this sphere.
     * @return the diameter of this sphere.
     * @see Sphere#getRadius()
     */
    public T getDiameter() {
        return getCalculator().multiplyLong(getRadius(), 2l);
    }

    /**
     * Returns the distance of the point to the center of this sphere
     * @param p
     * @return
     */
    public T distanceCenter(SPoint<T> p) {
        return o.distance(p);
    }

    /**
     * Returns the square of the distance of the point to the center of this sphere
     * @param p
     * @return
     */
    public T distanceCenterSq(SPoint<T> p) {
        return o.distanceSq(p);
    }

    /**
     * Returns the minimum distance from the point to the surface of this sphere.
     * @param p
     * @return
     */
    public T distanceSurface(SPoint<T> p) {
        return getCalculator().abs(getCalculator().subtract(o.distance(p), getRadius()));
    }

    /* (non-Javadoc)
     * @see cn.ancono.cn.ancono.utilities.math.spaceAG.shape.SpaceObject#isInside(cn.ancono.cn.ancono.utilities.math.spaceAG.SPoint)
     */
    @Override
    public boolean isInside(SPoint<T> p) {
        return getCalculator().compare(o.distanceSq(p), r2) < 0;
    }

    /* (non-Javadoc)
     * @see cn.ancono.cn.ancono.utilities.math.spaceAG.shape.SpaceObject#isOnSurface(cn.ancono.cn.ancono.utilities.math.spaceAG.SPoint)
     */
    @Override
    public boolean isOnSurface(SPoint<T> p) {
        return getCalculator().compare(o.distanceSq(p), r2) == 0;
    }

    /* (non-Javadoc)
     * @see cn.ancono.cn.ancono.utilities.math.spaceAG.SpacePointSet#contains(cn.ancono.cn.ancono.utilities.math.spaceAG.SPoint)
     */
    @Override
    public boolean contains(SPoint<T> p) {
        return getCalculator().compare(o.distanceSq(p), r2) <= 0;
    }

    private T square(T x) {
        return getCalculator().multiply(x, x);
    }

    /**
     * Assume {@code d} is the distance of the center of this sphere to a chord,
     * return the length of the chord.If the distance is larger than the radius,
     * {@code null} will be returned.
     * @param d distance
     * @return the length of the chord,or {@code null}
     */
    public T getChordLength(T d) {
        T d2 = square(d);
        if (getCalculator().compare(d2, r2) > 0) {
            return null;
        }
        return getCalculator().multiplyLong(getCalculator().squareRoot(getCalculator().subtract(r2, d2)), 2l);
    }

    /**
     * Assume {@code d2} is the square of the distance of the center of this sphere to a chord,
     * return the square of the length of the chord.
     * @param d2 square of distance
     * @return the square of the length of the chord
     */
    public T getChordLengthSq(T d2) {
        return getCalculator().multiplyLong(getCalculator().subtract(r2, d2), 4l);
    }

    /**
     * Get the corresponding central angle of the chord whose length is {@code cl}.
     * If {@code cl} is larger than the diameter of this sphere,{@code null} will be
     * returned.
     * @param cl the length of chord
     * @param arccos the math function arccos
     * @return the angle of
     */
    public T getCentralAngle(T cl, MathFunction<T, T> arccos) {
        T an = getCircumAngle(cl, arccos);
        if (an == null) {
            return null;
        }
        return getCalculator().multiplyLong(an, 2l);
    }

    /**
     * Get the corresponding circumference angle of the chord whose length is {@code cl}.
     * If {@code cl} is larger than the diameter of this sphere,{@code null} will be
     * returned.
     * @param cl the length of chord
     * @param arccos the math function arccos
     * @return the angle of
     */
    public T getCircumAngle(T cl, MathFunction<T, T> arccos) {
        T l_2 = getCalculator().divideLong(cl, 2l);
        if (getCalculator().compare(l_2, getRadius()) > 0) {
            return null;
        }
        return arccos.apply(getCalculator().divide(l_2, r));
    }

    /**
     * Determines the relation of the given point and this sphere.The relation may be
     * <i>inside</i>,<i>on</i>,<i>outside</i>.
     * @param p a point
     * @return {@code -1} if {@code p} is inside this sphere,
     * {@code 0} if {@code p} is on this sphere,
     * or {@code 1} if {@code p} is outside this sphere.
     */
    public int relation(SPoint<T> p) {
        T dis = o.distanceSq(p);
        return getCalculator().compare(dis, square(getRadius()));
    }

    /**
     * Determines the relation of the given line and this sphere.The relation may be
     * <i>intersect</i>,<i>tangent</i>,<i>disjoint</i>.
     * @param l a line
     * @return {@code -1,0,1} for <i>intersect</i>,<i>tangent</i>,<i>disjoint</i>.
     */
    public int relation(Line<T> l) {
        T d2 = l.distanceSq(o);
        return getCalculator().compare(d2, r2);
    }

    /**
     * Determines the relation of the given line and this sphere.The relation may be
     * <i>include</i>,<i>inscribed</i>,<i>intersect</i>,<i>circumscribed</i>,<i>disjoint</i>.
     * If the two spheres are the identity,the relation <i>circumscribed</i> will be returned.
     * @param c a sphere.
     * @return the relation of the spheres.
     */
    public Relation relation(Sphere<T> c) {
        //first compute the distance of two centers.
        T dis = c.o.distance(c.o);
        T rs = getCalculator().add(getRadius(), c.getRadius());
        int t = getCalculator().compare(dis, rs);
        if (t > 0) {
            return Relation.DISJOINT;
        } else if (t == 0) {
            return Relation.CIRCUMSCRIBED;
        }
        //o1o2 < r1+r2
        T rd = getCalculator().abs(getCalculator().subtract(r, c.r));
        t = getCalculator().compare(dis, rd);
        if (t > 0) {
            return Relation.INTERSECT;
        } else if (t == 0) {
            return Relation.INSCRIBED;
        } else {
            return Relation.INCLUDE;
        }
    }

    /* (non-Javadoc)
     * @see cn.ancono.cn.ancono.utilities.math.FlexibleMathObject#mapTo(java.util.function.Function, cn.ancono.cn.ancono.utilities.math.MathCalculator)
     */
    @NotNull
    @Override
    public <N> Sphere<N> mapTo(@NotNull EqualPredicate<N> newCalculator, @NotNull Function<T, N> mapper) {
        Sphere<N> sp = new Sphere<N>((RealCalculator<N>) newCalculator,
                r == null ? null : mapper.apply(r)
                , mapper.apply(r2), o.mapTo(newCalculator, mapper));
        if (volume != null) {
            sp.volume = mapper.apply(volume);
        }
        if (surfaceArea != null) {
            sp.surfaceArea = mapper.apply(surfaceArea);
        }
        return sp;
    }

    /* (non-Javadoc)
     * @see cn.ancono.cn.ancono.utilities.math.FlexibleMathObject#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Sphere) {
            Sphere<?> sp = (Sphere<?>) obj;
            return sp.o.equals(o) && r2.equals(sp.r2);
        }
        return false;
    }

    /* (non-Javadoc)
     * @see cn.ancono.cn.ancono.utilities.math.FlexibleMathObject#hashCode()
     */
    @Override
    public int hashCode() {
        return o.hashCode() + 37 * r2.hashCode();
    }

    /* (non-Javadoc)
     * @see cn.ancono.cn.ancono.utilities.math.FlexibleMathObject#valueEquals(cn.ancono.cn.ancono.utilities.math.FlexibleMathObject)
     */
    @Override
    public boolean valueEquals(@NotNull IMathObject<T> obj) {
        if (obj instanceof Sphere) {
            Sphere<T> sp = (Sphere<T>) obj;
            return o.valueEquals(sp.o) && getCalculator().isEqual(r2, sp.r2);
        }
        return false;
    }


    /**
     * Returns
     * <pre>
     * Sphere(x,y,z) R=$r
     * </pre>
     * or
     * <pre>
     * Sphere(x,y,z) R2=$r2
     * </pre>
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Sphere").append(o.toString());
        if (r == null) {
            sb.append(" R2=").append(r2);
        } else {
            sb.append(" R=").append(r);
        }
        return sb.toString();
    }

    /**
     * Creates a sphere with its center point and its radius.
     * <p>The {@link RealCalculator} will be taken from the first parameter of {@link MathObjectReal}
     *
     * @param p a point
     * @param r the radius, must be positive.
     * @return a new sphere
     */
    public static <T> Sphere<T> centerRadius(SPoint<T> p, T r) {
        RealCalculator<T> mc = (RealCalculator<T>) p.getCalculator();
        if (mc.compare(r, mc.getZero()) <= 0) {
            throw new IllegalArgumentException("r<=0");
        }
        return new Sphere<T>(mc, r, mc.multiply(r, r), p);
    }

    /**
     * Creates a sphere with its center point and its radius' square.
     * <p>The {@link RealCalculator} will be taken from the first parameter of {@link MathObjectReal}
     * @param p a point
     * @param r2 square of the radius, must be positive.
     * @return a new sphere
     */
    public static <T> Sphere<T> centerRadiusSq(SPoint<T> p, T r2) {
        RealCalculator<T> mc = (RealCalculator<T>) p.getCalculator();
        if (mc.compare(r2, mc.getZero()) <= 0) {
            throw new IllegalArgumentException("r<=0");
        }
        return new Sphere<T>(mc, null, r2, p);
    }

    /**
     * Creates a sphere with four points, they must not be on the identity plane.
     * <p>The {@link RealCalculator} will be taken from the first parameter of {@link MathObjectReal}
     * @param a
     * @param b
     * @param c
     * @param d
     * @return a new sphere
     */
    @SuppressWarnings("unchecked")
    public static <T> Sphere<T> fourPoints(SPoint<T> a, SPoint<T> b, SPoint<T> c, SPoint<T> d) {
        RealCalculator<T> mc = (RealCalculator<T>) a.getCalculator();
        SPoint<T>[] ps = new SPoint[]{a, b, c, d};
        T[][] mat = (T[][]) new Object[3][4];
        for (int i = 0; i < 3; i++) {
            T dx = mc.subtract(ps[i].getX(), ps[i + 1].getX());
            T dy = mc.subtract(ps[i].getY(), ps[i + 1].getY());
            T dz = mc.subtract(ps[i].getZ(), ps[i + 1].getZ());
            T dx2 = mc.multiply(dx, mc.add(ps[i].getX(), ps[i + 1].getX()));
            T dy2 = mc.multiply(dy, mc.add(ps[i].getY(), ps[i + 1].getY()));
            T dz2 = mc.multiply(dz, mc.add(ps[i].getZ(), ps[i + 1].getZ()));
            T w = mc.add(dx2, mc.add(dy2, dz2));
            w = mc.divideLong(w, 2l);
            mat[i][0] = dx;
            mat[i][1] = dy;
            mat[i][2] = dz;
            mat[i][3] = w;
        }

        Printer.printMatrix(mat);
        LinearEquationSolution<T> sov = Matrix.solveLinearExpanded(Matrix.of(mat, mc));
        if (sov.isInfinite()) {
            throw new IllegalArgumentException("Four points identity plane");
        }
        Vector<T> vec = sov.getSpecial();
        SPoint<T> o = SPoint.valueOf(vec.get(0), vec.get(1), vec.get(2), mc);
        T r2 = a.distanceSq(o);
        return new Sphere<T>(mc, null, r2, o);
    }
//	public static void main(String[] args) {
//		SPointGenerator<Double> sg = new SPointGenerator<>(Calculators.getCalculatorDoubleDev());
//		SPoint<Double> a =sg.of(0d,0d,0d),
//				b = sg.of(1d, 1d, 0d),
//				c = sg.of(0d, 1d, 1d),
//				d = sg.of(1d, 0d, 1d);
//		Sphere<Double> sp = fourPoints(a, b, c, d);
//		Printer.print(sp);
//	}
}
