package cn.ancono.math.set;

import cn.ancono.math.AbstractMathObject;
import cn.ancono.math.algebra.abs.calculator.EqualPredicate;
import cn.ancono.math.algebra.abs.calculator.OrderPredicate;
import cn.ancono.math.numberModels.Calculators;
import cn.ancono.math.numberModels.api.NumberFormatter;
import cn.ancono.math.numberModels.api.RealCalculator;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

/**
 * Abstract interval is the superclass of all the interval. Interval is a set of real number which has clear
 * upper bound and downer bound, which is often shown as {@literal [a,b]}, {@literal (a,b)},  {@literal [a,b)} or
 * {@literal (a,b]}.
 * The interval contains a number if the number fits: {@code downerBound <= number <= upperBound}, but whether the
 * equal sign is suitable is dependent on the type of the interval.
 * <p>The mathematical restriction is that {@code downerBound < upperBound}, but this class also permits that
 * {@code downerBound <= upperBound}, but if {@code downerBound == upperBound}, the interval must be a closed interval.
 * <p>
 * The interval is immutable, any method that returns an interval as the result will always creates a new one.
 * <p>
 * The given calculator should implement the method {@link RealCalculator#compare(Object, Object)},
 * and only when the method {@code lengthOf()} is called, the method {@link RealCalculator#subtract(Object, Object)} is
 * needed.
 *
 * @param <T> the format of number to be stored
 * @author lyc
 */
public abstract class Interval<T> extends AbstractMathObject<T, OrderPredicate<T>> implements IntersectableSet<T, Interval<T>> {

    protected Interval(OrderPredicate<T> mc) {
        super(mc);
    }

    /**
     * Returns {@code true} if the given number is in the range of this interval.
     *
     * @param n a number
     * @return {@code true} if {literal  this ∋ n} , otherwise {@code false}.
     * @throws NullPointerException if n == null
     */
    public abstract boolean contains(T n);

    /**
     * Returns the upper bound of this interval,which means for any number {@code n in this} , {@code n <= upperBound}.
     * If this interval doesn't have an upper bound, {@code null} will be returned.
     *
     * @return the upper bound of this interval, or {@code null}
     */
    public abstract T upperBound();

    /**
     * Returns whether is upper bound is included in this interval.
     *
     * @return {@code true} if upper bound is includes, otherwise {@code false}
     */
    public abstract boolean isUpperBoundInclusive();

    public boolean isBoundedAbove() {
        return upperBound() != null;
    }

    /**
     * Returns the downer bound of this interval, which means for any number {@code n in this} , {@code n >=
     * downerBound}.
     * If this interval doesn't have an downer bound, {@code null} will be returned.
     *
     * @return the downer bound of this interval, or {@code null}
     */
    public abstract T downerBound();

    /**
     * Returns whether is downer bound is included in this interval.
     *
     * @return {@code true} if downer bound is includes, otherwise {@code false}
     */
    public abstract boolean isDownerBoundInclusive();

    public boolean isBoundedBelow() {
        return downerBound() != null;
    }

    public boolean isBounded() {
        return isBoundedAbove() && isBoundedBelow();
    }

    /**
     * Returns the length of this interval , the length of this interval is equal to {@code upperBound - downerBound}.
     * If either upper bound or downer bound does not exist, {@code null} will be returned.
     *
     * @return {@code upperBound - downerBound},or {@code null}
     */
    public abstract T lengthOf();

    /**
     * Returns a new interval that fits {@code downerBound = this.downerBound} and {@code upperBound = n}.Whether the
     * new
     * interval should include the number {@code n} is determined by this interval.For example , if this = [0,5) , then
     * {@code downerPart(4)} will be {@code [0,4)}.
     *
     * @param n a number
     * @return a new interval
     * @throws NullPointerException     if n == null
     * @throws IllegalArgumentException if {@code n<= downerBound}  or {@code n>= upperBound}
     * @see #downerPart(T, boolean)
     */
    public abstract Interval<T> downerPart(T n);

    /**
     * Returns a new interval that fits {@code downerBound = this.downerBound} and {@code upperBound = n}.Whether the
     * new
     * interval should include the number {@code n} is determined by {@code include}.For example , if this = [0,5) ,
     * then
     * {@code downerPart(4,true)} will be {@code [0,4]}.
     *
     * @param n       a number
     * @param include determines whether the bound should be inclusive
     * @return a new interval
     * @throws IllegalArgumentException if {@code n<= downerBound} or {@code n>= upperBound}
     */
    public abstract Interval<T> downerPart(T n, boolean include);

    /**
     * Returns a new interval that fits {@code upperBound = this.upperBound} and {@code downerBound = n}.Whether the new
     * interval should include the number {@code n} is determined by this interval.For example , if this = [0,5) , then
     * {@code upperPart(1)} will be {@code [1,5)}.
     *
     * @param n a number
     * @return a new interval
     * @throws NullPointerException     if n == null
     * @throws IllegalArgumentException if {@code n>= upperBound} or {@code n <= downerBound}
     * @see #upperPart(T, boolean)
     */
    public abstract Interval<T> upperPart(T n);

    /**
     * Returns a new interval that fits {@code upperBound = this.upperBound} and {@code downerBound = n}.Whether the
     * new
     * interval should include the number {@code n} is determined by {@code include}.For example , if this = [0,5) ,
     * then
     * {@code upperPart(1,false)} will be {@code (1,5)}.
     *
     * @param n       a number
     * @param include determines whether the bound should be inclusive
     * @return a new interval
     * @throws NullPointerException     if n == null
     * @throws IllegalArgumentException if {@code n>= upperBound} or {@code n <= downerBound}
     */
    public abstract Interval<T> upperPart(T n, boolean include);

    /**
     * Returns a new interval that {@code upperBound = n} and {@code downerBound = this.downerBound}.Whether the new
     * interval should include the number {@code n} is determined by this interval.
     *
     * @param n a number
     * @return a new interval
     * @throws IllegalArgumentException if {@code n <= upperBound}
     * @throws NullPointerException     if n == null
     */
    public abstract Interval<T> expandUpperBound(T n);

    /**
     * Returns a new interval that {@code upperBound = n} and {@code downerBound = this.downerBound}.Whether the new
     * interval should include the number {@code n} is determined by {@code include}.
     *
     * @param n       a number
     * @param include determines whether the bound should be inclusive
     * @return a new interval
     * @throws IllegalArgumentException if {@code n <= upperBound}
     * @throws NullPointerException     if n == null
     */
    public abstract Interval<T> expandUpperBound(T n, boolean include);


    /**
     * Returns a new interval that {@code downerBound = n} and {@code upperBound = this.upperBound}.Whether the new
     * interval should include the number {@code n} is determined by this interval.
     *
     * @param n a number
     * @return a new interval
     * @throws IllegalArgumentException if {@code n >= downerBound}
     * @throws NullPointerException     if n == null
     */
    public abstract Interval<T> expandDownerBound(T n);

    /**
     * Returns a new interval that {@code downerBound = n} and {@code upperBound = this.upperBound}.Whether the new
     * interval should include the number {@code n} is determined by {@code include}.
     *
     * @param n       a number
     * @param include determines whether the bound should be inclusive
     * @return a new interval
     * @throws IllegalArgumentException if {@code n >= downerBound}
     * @throws NullPointerException     if n == null
     */
    public abstract Interval<T> expandDownerBound(T n, boolean include);

    /**
     * Returns a identity type interval of {@code this}, whether  the new interval should be inclusive in upper or
     * downer bound is determined by {@code this}.
     *
     * @param downerBound the downer bound of the new interval
     * @param upperBound  the new downer bound of the new interval
     * @return a new interval
     * @throws IllegalArgumentException if {@code downerBound >= upperBound}
     */
    public abstract Interval<T> sameTypeInterval(T downerBound, T upperBound);


    /**
     * Returns {@code true} if the given interval is in the range of this.If {@code iv} has identity upper or downer
     * bound
     * with {@code this} and the bound in this is exclusive while in {@code iv} is inclusive, then {@literal iv ⊆  this}
     * is false,
     * so {@code false} will be returned.
     *
     * @param iv another interval
     * @return {@code true} if {@literal iv ⊆  this} , else {@code false}
     */
    public abstract boolean contains(Interval<T> iv);

    /**
     * Returns a new interval that equals to the intersect of {@code this} and {@code iv} ,
     * which is expressed as {@literal iv ∩ this} in mathematical. If the complement is empty
     * then {@code null} will be returned.
     *
     * @param iv a interval, or {@code null}
     * @return {@literal iv ∩ this} or {@code null} if the result is an empty set.
     */
    public abstract Interval<T> intersect(Interval<T> iv);


    @NotNull
    @Override
    public abstract <N> Interval<N> mapTo(@NotNull EqualPredicate<N> newCalculator, @NotNull Function<T, N> mapper);

    /**
     * Returns the mathematical expression of this interval. Like {@literal (0,2)} or {@literal [2,3)}.
     *
     * @return a String representing this interval.
     */
    @NotNull
    @Override
    public abstract String toString();

    /**
     * Returns the mathematical expression of this interval. Like {@literal (0,2)} or {@literal [2,3)}.
     *
     * @return a String representing this interval.
     */
    @NotNull
    @Override
    public abstract String toString(@NotNull NumberFormatter<T> nf);


    /**
     * Returns a closed interval between the two number, which must
     * meet the requirement that {@code downer<upper}
     *
     * @param downer the downer bound
     * @param upper  the upper bound
     * @param mc     a {@link RealCalculator}
     * @return [downer, upper]
     */
    public static <T> Interval<T> closedInterval(T downer, T upper, OrderPredicate<T> mc) {
        return instanceNonNull(downer, upper, true, true, mc);
    }

    /**
     * Returns an open interval between the two number, which must
     * meet the requirement that {@code downer<upper}
     *
     * @param downer the downer bound
     * @param upper  the upper bound
     * @param mc     a {@link RealCalculator}
     * @return (downer, upper)
     */
    public static <T> Interval<T> openInterval(T downer, T upper, OrderPredicate<T> mc) {
        return instanceNonNull(downer, upper, false, false, mc);
    }

    /**
     * Returns a left-open-right-closed interval between the two number, which must
     * meet the requirement that {@code downer<upper}
     *
     * @param downer the downer bound
     * @param upper  the upper bound
     * @param mc     a {@link RealCalculator}
     * @return (downer, upper]
     */
    public static <T> Interval<T> leftOpenRightClosed(T downer, T upper, OrderPredicate<T> mc) {
        return instanceNonNull(downer, upper, false, true, mc);
    }

    /**
     * Returns a left-closed-right-open interval between the two number, which must
     * meet the requirement that {@code downer<upper}
     *
     * @param downer the downer bound
     * @param upper  the upper bound
     * @param mc     a {@link RealCalculator}
     * @return [downer, upper)
     */
    public static <T> Interval<T> leftClosedRightOpen(T downer, T upper, OrderPredicate<T> mc) {
        return instanceNonNull(downer, upper, true, false, mc);
    }

    /**
     * Returns an interval from negative infinity to the upper bound.
     *
     * @param upper  the upper bound
     * @param closed determines whether this
     * @param mc     a {@link RealCalculator}
     * @return (- ∞, upper) or (-∞,upper]
     */
    public static <T> Interval<T> fromNegativeInf(T upper, boolean closed, OrderPredicate<T> mc) {
        return new IntervalI<>(mc, null, Objects.requireNonNull(upper),
                IntervalI.LEFT_OPEN_MASK | (closed ? 0 : IntervalI.RIGHT_OPEN_MASK));
    }

    /**
     * Returns an interval from the downer bound to positive infinity.
     *
     * @param downer the downer bound
     * @param closed determines whether this
     * @param mc     a {@link RealCalculator}
     * @return (downer, + ∞) or [downer,+∞)
     */
    public static <T> Interval<T> toPositiveInf(T downer, boolean closed, OrderPredicate<T> mc) {
        return new IntervalI<>(mc, Objects.requireNonNull(downer), null,
                IntervalI.RIGHT_OPEN_MASK | (closed ? 0 : IntervalI.LEFT_OPEN_MASK));
    }

    /**
     * Returns the interval representing the whole real number, whose downer bound
     * is negative infinity and upper bound is positive infinity.
     *
     * @param mc a {@link RealCalculator}
     * @return (- ∞, + ∞)
     */
    public static <T> Interval<T> universe(OrderPredicate<T> mc) {
        return new IntervalI<>(mc, null, null, IntervalI.BOTH_OPEN_MASK);
    }

    /**
     * Create a new Interval with the given arguments.
     *
     * @param mc              the  calculator, only compare methods will be used.
     * @param downer          the downer bound of this interval, or {@code null} to indicate unlimited.
     * @param upper           the upper bound of this interval, or {@code null} to indicate unlimited.
     * @param downerInclusive determines whether downer should be inclusive
     * @param upperInclusive  determines whether upper should be inclusive
     */
    public static <T> Interval<T> valueOf(T downer, T upper, boolean downerInclusive, boolean upperInclusive,
                                          OrderPredicate<T> mc) {
        return new IntervalI<>(mc, downer, upper, downerInclusive, upperInclusive);
    }

    /**
     * Returns the interval representing a single real number, whose downer bound
     * and upper bound are both {@code x}
     *
     * @return [x, x]
     */
    public static <T> Interval<T> single(T x, OrderPredicate<T> mc) {
        return new IntervalI<>(mc, x, x, 0);
    }

    /**
     * Returns the interval representing the positive numbers.
     *
     * @return {@literal (0,+∞)}
     */
    public static <T> Interval<T> positive(RealCalculator<T> mc) {
        return toPositiveInf(mc.getZero(), false, mc);
    }

    /**
     * Returns the interval representing the negative numbers.
     *
     * @return {@literal (-∞,0)}
     */
    public static <T> Interval<T> negative(RealCalculator<T> mc) {
        return fromNegativeInf(mc.getZero(), false, mc);
    }

    static <T> Interval<T> instanceNonNull(T a, T b, boolean dc, boolean uc, OrderPredicate<T> mc) {
        return new IntervalI<>(mc, Objects.requireNonNull(a), Objects.requireNonNull(b), dc, uc);
    }

    /**
     * Returns an interval of integer.
     *
     * @param downerBound inclusive
     * @param upperBound  exclusive
     */
    public static FiniteInterval<Integer> rangeOf(int downerBound, int upperBound) {
        return new FiniteInterval<>(Calculators.integer(), downerBound, upperBound - 1);
    }


}
