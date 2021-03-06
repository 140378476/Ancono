/**
 * 2017-11-25
 */
package cn.ancono.math.numberModels.expression;

import cn.ancono.math.numberModels.Multinomial;

import java.util.function.Function;

;

/**
 * @author liyicheng
 * 2017-11-25 19:13
 */
public interface PolyFunctionM extends Function<Multinomial[], Multinomial> {
    /**
     * Returns the result of applying the function, or throws UnsupportedOperationException if the result cannot be
     * returned as a Multinomial.
     *
     * @param ps an array of Multinomial as parameters.
     * @return the result of applying the function
     */
    @Override
    Multinomial apply(Multinomial[] ps) throws UnsupportedOperationException;
}
