/**
 * 2017-10-06
 */
package cn.ancono.math.equation.inequation;

import cn.ancono.math.MathCalculator;
import cn.ancono.math.equation.SVCompareStructure;
import cn.ancono.math.equation.Type;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * @author liyicheng
 * 2017-10-06 09:22
 */
public abstract class SVInquation<T> extends Inequation<T, T> implements SVCompareStructure<T> {

    /**
     * @param mc
     * @param op
     */
    protected SVInquation(MathCalculator<T> mc, Type op) {
        super(mc, op);
    }

    /*
     * @see cn.ancono.math.equation.SVCompareStructure#isSolution(java.lang.Object)
     */
    @Override
    public boolean isSolution(T x) {
        return op.matches(compareZero(compute(x)));
    }

    @NotNull
    public abstract <N> SVInquation<N> mapTo(@NotNull MathCalculator<N> newCalculator, @NotNull Function<T, N> mapper);
}
