package cn.ancono.math.geometry.analytic.plane;

import cn.ancono.math.IMathObject;
import cn.ancono.math.algebra.abs.calculator.EqualPredicate;
import cn.ancono.math.geometry.analytic.plane.curve.AbstractPlaneCurve;
import cn.ancono.math.geometry.analytic.plane.curve.SubstituableCurve;
import cn.ancono.math.numberModels.CalculatorUtils;
import cn.ancono.math.numberModels.Calculators;
import cn.ancono.math.numberModels.api.NumberFormatter;
import cn.ancono.math.numberModels.api.RealCalculator;
import cn.ancono.math.numberModels.api.Simplifiable;
import cn.ancono.math.numberModels.api.Simplifier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

/**
 * A segment consists of two points namely {@code A} and {@code B}.
 *
 * @param <T>
 */
public final class Segment<T> extends AbstractPlaneCurve<T> implements Simplifiable<T, Segment<T>>, SubstituableCurve<T> {
    final Line<T> line;
    final Point<T> A, B;
    final PVector<T> v;
    //determines whether the x coordinate of the direct vector is zero
    final boolean xZero;

    Segment(Line<T> line, Point<T> A, Point<T> B, PVector<T> v, RealCalculator<T> mc) {
        super(mc);
        this.line = line;
        this.A = A;
        this.B = B;
        this.v = v;
        xZero = mc.isZero(v.x);
    }

    @NotNull
    @Override
    public <N> Segment<N> mapTo(@NotNull EqualPredicate<N> newCalculator, @NotNull Function<T, N> mapper) {
        var nline = line.mapTo(newCalculator, mapper);
        var nA = A.mapTo(newCalculator, mapper);
        var nB = B.mapTo(newCalculator, mapper);
        var nv = v.mapTo(newCalculator, mapper);
        return new Segment<>(nline, nA, nB, nv, (RealCalculator<N>) newCalculator);
    }

    @Override
    public boolean valueEquals(@NotNull IMathObject<T> obj) {
        if (!(obj instanceof Segment)) {
            return false;
        }
        Segment<T> seg = (Segment<T>) obj;
        return line.valueEquals(seg.line) && A.valueEquals(seg.A) && B.valueEquals(seg.B);
    }


    @Override
    public String toString(@NotNull NumberFormatter<T> nf) {
        return "Segment:A" + A.toString(nf) + "-B" + B.toString(nf);
    }

    @Override
    public boolean contains(Point<T> p) {
        if (!line.contains(p)) {
            return false;
        }
        if (xZero) {
            return Calculators.oppositeSign(A.y, B.y, p.y, getMc());
        }
        return Calculators.oppositeSign(A.x, B.x, p.x, getMc());
    }

    @Override
    public AbstractPlaneCurve<T> transform(PAffineTrans<T> trans) {
        return super.transform(trans);
    }

    /**
     * Gets the line of this segment.
     *
     * @return the line
     */
    public Line<T> getLine() {
        return line;
    }

    /**
     * Gets the endpoint A.
     *
     * @return A
     */
    public Point<T> getA() {
        return A;
    }

    /**
     * Gets the endpoint B.
     *
     * @return B
     */
    public Point<T> getB() {
        return B;
    }


    /**
     * Returns the length of this segment.
     *
     * @return
     */
    public T length() {
        return v.norm();
    }

    /**
     * Returns the square of the length.
     *
     * @return
     */
    public T lengthSq() {
        return v.normSq();
    }

    @Override
    public Segment<T> simplify() {
        var nline = line.simplify();
        return new Segment<>(nline, A, B, v, getMc());
    }

    @Override
    public Segment<T> simplify(Simplifier<T> sim) {
        var nline = line.simplify(sim);
        return new Segment<>(nline, A, B, v, getMc());
    }


    /**
     * Returns the square of the distance of the point as the substituting result.
     *
     * @param x
     * @param y
     * @return
     */
    @Override
    public T substitute(T x, T y) {
        return distanceSq(x, y);
    }

    /**
     * Returns the minimum of the distance of a point on this segment and the point {@code p}.
     *
     * @return
     */
    public T distance(Point<T> p) {
        return getMc().squareRoot(distanceSq(p));
    }

    /**
     * Returns the square of {@code distance(p)}
     *
     * @return
     */
    public T distanceSq(Point<T> p) {
        Point<T> protection = line.projection(p);
        if (contains(protection)) {
            return line.distanceSq(p);
        }
        T d1 = A.distanceSq(p),
                d2 = B.distanceSq(p);
        return CalculatorUtils.min(d1, d2, getMc());
    }

    public T distanceSq(T x, T y) {
        return distanceSq(Point.valueOf(x, y, getMc()));
    }

    public T distance(T x, T y) {
        return distance(Point.valueOf(x, y, getMc()));
    }


    /**
     * Creates a segment whose endpoints are A and B(the order is considered).
     *
     * @param A
     * @param B
     * @param <T>
     * @return
     */
    public static <T> Segment<T> twoPoints(Point<T> A, Point<T> B) {
        if (A.valueEquals(B)) {
            throw new IllegalArgumentException("A==B");
        }
        RealCalculator<T> mc = (RealCalculator<T>) A.getCalculator();
        Line<T> line = Line.twoPoint(A, B, mc);
        PVector<T> v = A.directVector(B);
        return new Segment<>(line, A, B, v, mc);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Segment<?> segment = (Segment<?>) o;
        return Objects.equals(A, segment.A) &&
                Objects.equals(B, segment.B);
    }

    @Override
    public int hashCode() {
        return Objects.hash(A, B);
    }


//    public static void main(String[] args){
//        MathCalculator<Double> mc = Calculators.getCalculatorDoubleDev();
//        var A = Point.valueOf(0d,0d,mc);
//        var B = Point.valueOf(1d,0d,mc);
//        var P = Point.valueOf(2d,1d,mc);
//        var AB = twoPoints(A,B);
//        print(AB.distanceSq(P));
//    }
}
