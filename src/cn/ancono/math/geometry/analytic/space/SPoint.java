package cn.ancono.math.geometry.analytic.space;

import cn.ancono.math.AbstractMathObject;
import cn.ancono.math.IMathObject;
import cn.ancono.math.algebra.abs.calculator.EqualPredicate;
import cn.ancono.math.algebra.abs.calculator.FieldCalculator;
import cn.ancono.math.numberModels.api.NumberFormatter;
import cn.ancono.math.numberModels.api.RealCalculator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * Point is one of the most basic elements in the plane.A point has three dimensions:x,y,z.
 *
 * @param <T> the type of number
 * @author lyc
 */
public final class SPoint<T> extends SpacePointSet<T> {

    final T x, y, z;

    public SPoint(FieldCalculator<T> mc, T x, T y, T z) {
        super(mc);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @NotNull
    @Override
    public FieldCalculator<T> getCalculator() {
        return (FieldCalculator<T>) super.getCalculator();
    }

    /**
     * Get the X coordinate of this point.
     *
     * @return x
     */
    public T getX() {
        return x;
    }

    /**
     * Get the Y coordinate of this point.
     *
     * @return y
     */
    public T getY() {
        return y;
    }

    /**
     * Get the Z coordinate of this point.
     *
     * @return z
     */
    public T getZ() {
        return z;
    }

    /**
     * Get the vector representing this point.The returned vector will be a column
     * vector and the length of this vector will be 2.
     *
     * @return a vector
     */
    public SVector<T> getVector() {
        return new SVector<>(x, y, z, getCalculator());
    }

    /**
     * Return the square of the distance between this point and the given point.This method
     * is often better than calling {@code distance()} method.
     *
     * @param p another point
     * @return {@literal (x-p.x)^2 + (y-p.y)^2 + (z-p.z)^2}
     */
    @SuppressWarnings("SuspiciousNameCombination")
    public T distanceSq(SPoint<T> p) {
        var mc = getCalculator();
        T dx = mc.subtract(x, p.x);
        T dy = mc.subtract(y, p.y);
        T dz = mc.subtract(z, p.z);
        return mc.add(mc.add(mc.multiply(dx, dx), mc.multiply(dy, dy)), mc.multiply(dz, dz));
    }

    /**
     * Return the distance of {@code this} and the given point {@code p}.The operation
     * {@linkplain RealCalculator#squareRoot(Object)} is required when this method is called.
     * Make sure that the calculator implements the operation.
     *
     * @param p another point
     * @return the distance of {@code this} and {@code p}
     * @see SPoint#distance(SPoint)
     */
    public T distance(SPoint<T> p) {
        return ((RealCalculator<T>) getCalculator()).squareRoot(distanceSq(p));
    }

    /**
     * Create a vector that is equal to <i>thisP</i>.
     *
     * @param p another point
     * @return a column vector with two dimensions.
     */
    @SuppressWarnings("SuspiciousNameCombination")
    public SVector<T> directVector(SPoint<T> p) {
        T vx = getCalculator().subtract(p.x, x);
        T vy = getCalculator().subtract(p.y, y);
        T vz = getCalculator().subtract(p.z, z);
        return SVector.valueOf(vx, vy, vz, getCalculator());
    }

    /**
     * Returns a point M that <i>ThisM</i> = k<i>MP</i>,the direction is specific and negative {@code k} value
     * is permitted,but it should not be {@code -1}.<p>
     * The result will be {@code x = (kp.x+this.x)/(k+1)},
     * {@code y = (kp.y+this.y)/(k+1)},
     * {@code x = (kp.z+this.z)/(k+1)},
     *
     * @param p another point
     * @param k a number except -1.
     * @return the proportion point.
     */
    public SPoint<T> proportionPoint(SPoint<T> p, T k) {
        var mc = getCalculator();
        T de = mc.add(k, mc.getOne());
        T xN = mc.add(mc.multiply(k, p.x), x);
        T yN = mc.add(mc.multiply(k, p.y), y);
        T zN = mc.add(mc.multiply(k, p.z), z);
        xN = mc.divide(xN, de);
        yN = mc.divide(yN, de);
        zN = mc.divide(zN, de);
        return new SPoint<>(mc, xN, yN, zN);
    }

    /**
     * Returns a point of {@code this} moves by {@code v}.
     *
     * @return a new point
     */
    public SPoint<T> moveToward(SVector<T> v) {
        return new SPoint<>(getCalculator(), getCalculator().add(x, v.x), getCalculator().add(y, v.y), getCalculator().add(z, v.z));
    }

    /**
     * Returns a point of {@code this} moves by {@code v}.
     *
     * @return a new point
     */
    public SPoint<T> moveToward(T vx, T vy, T vz) {
        return new SPoint<>(getCalculator(), getCalculator().add(x, vx), getCalculator().add(y, vy), getCalculator().add(z, vz));
    }

    /**
     * Return the middle point of {@code this} and {@code p}.
     *
     * @param p another point
     * @return middle point
     */
    public SPoint<T> middle(SPoint<T> p) {
        var mc = getCalculator();
        T xm = mc.divideLong(mc.add(x, p.x), 2);
        T ym = mc.divideLong(mc.add(y, p.y), 2);
        T zm = mc.divideLong(mc.add(z, p.z), 2);
        return new SPoint<T>(mc, xm, ym, zm);
    }

    /**
     * Returns {@code this.valueEquals(p)}
     */
    @Override
    public boolean contains(SPoint<T> p) {
        var mc = getCalculator();
        return mc.isEqual(x, p.x) &&
                mc.isEqual(y, p.y) &&
                mc.isEqual(z, p.z);
    }

    @NotNull
    @Override
    public <N> SPoint<N> mapTo(@NotNull EqualPredicate<N> newCalculator, @NotNull Function<T, N> mapper) {
        return new SPoint<>((FieldCalculator<N>) newCalculator, mapper.apply(x), mapper.apply(y), mapper.apply(z));
    }


    private int hashCode = 0;

    @Override
    public int hashCode() {
        if (hashCode == 0) {
            int hash = 31 + x.hashCode();
            hash = hash * 31 + y.hashCode();
            hashCode = hash * 31 + z.hashCode();
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SPoint) {
            SPoint<?> sv = (SPoint<?>) obj;
            return x.equals(sv.x) && y.equals(sv.y) && z.equals(sv.z);
        }
        return false;
    }

    @Override
    public boolean valueEquals(@NotNull IMathObject<T> obj) {
        if (obj instanceof SPoint) {
            SPoint<T> s = (SPoint<T>) obj;
            return getCalculator().isEqual(x, s.x) &&
                    getCalculator().isEqual(y, s.y) &&
                    getCalculator().isEqual(z, s.z);
        }
        return false;
    }

    /**
     * Return the String expression of this point.The expression will
     * be like :
     * <pre>
     * (x,y,z)
     * </pre>
     */
    @Override
    public String toString() {
        return "(" +
                x + "," + y + "," + z + ")";
    }

    /**
     * Create a point with the given coordinates.
     *
     * @param x  X coordinate
     * @param y  Y coordinate
     * @param mc a MathCalculator
     * @return a new point
     */
    public static <T> SPoint<T> valueOf(T x, T y, T z, FieldCalculator<T> mc) {
        return new SPoint<>(mc, x, y, z);
    }

    /**
     * Returns the point (0,0).
     *
     * @param mc a {@link RealCalculator}
     * @return point (0,0)
     */
    public static <T> SPoint<T> pointO(FieldCalculator<T> mc) {
        return new SPoint<>(mc, mc.getZero(), mc.getZero(), mc.getZero());
    }

    /**
     * Create a point with the given vector coordinates.
     *
     * @param v a space vector
     * @return point(x, y, z)
     */
    public static <T> SPoint<T> valueOf(SVector<T> v) {
        return new SPoint<>((RealCalculator<T>) v.getCalculator(), v.getX(), v.getY(), v.getZ());
    }

    /**
     * Returns the *average of the points, the value of x,y,z will be the
     * corresponding average.
     *
     * @param points
     * @return
     */
    @SafeVarargs
    public static <T> SPoint<T> average(SPoint<T>... points) {
        var mc = points[0].getCalculator();
        final int num = points.length;
        var arr = new ArrayList<T>(points.length);
        for (SPoint<T> point : points) {
            arr.add(point.x);
        }
        T xm = mc.sum(arr);
        xm = mc.divideLong(xm, num);
        for (int i = 0; i < num; i++) {
            arr.set(i, points[i].y);
        }
        T ym = mc.sum(arr);
        ym = mc.divideLong(ym, num);
        for (int i = 0; i < num; i++) {
            arr.set(i, points[i].z);
        }
        T zm = mc.sum(arr);
        zm = mc.divideLong(zm, num);
        return new SPoint<T>(mc, xm, ym, zm);
    }

    public static class SPointGenerator<T> extends AbstractMathObject<T, FieldCalculator<T>> {

        /**
         * @param mc
         */
        public SPointGenerator(FieldCalculator<T> mc) {
            super(mc);
        }

        /**
         * Returns a point
         *
         * @param x
         * @param y
         * @param z
         * @return
         */
        public SPoint<T> of(T x, T y, T z) {
            return SPoint.valueOf(x, y, z, getCalculator());
        }

        /* (non-Javadoc)
         * @see cn.ancono.cn.ancono.utilities.math.FlexibleMathObject#mapTo(java.util.function.Function, cn.ancono.cn.ancono.utilities.math.MathCalculator)
         */
        @NotNull
        @Override
        public <N> SPointGenerator<N> mapTo(@NotNull EqualPredicate<N> newCalculator, @NotNull Function<T, N> mapper) {
            return new SPointGenerator<>((FieldCalculator<N>) newCalculator);
        }

        /* (non-Javadoc)
         * @see cn.ancono.cn.ancono.utilities.math.FlexibleMathObject#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof SPointGenerator) {
                return getCalculator().equals(((SPointGenerator<?>) obj).getCalculator());
            }
            return false;
        }

        /* (non-Javadoc)
         * @see cn.ancono.cn.ancono.utilities.math.FlexibleMathObject#hashCode()
         */
        @Override
        public int hashCode() {
            return getCalculator().hashCode();
        }

        @Override
        public boolean valueEquals(@NotNull IMathObject<T> obj) {
            return equals(obj);
        }


        /* (non-Javadoc)
         * @see cn.ancono.math.FlexibleMathObject#toString(cn.ancono.math.number_models.NumberFormatter)
         */
        @NotNull
        @Override
        public String toString(@NotNull NumberFormatter<T> nf) {
            return "SPointGenerator";
        }
    }

}
