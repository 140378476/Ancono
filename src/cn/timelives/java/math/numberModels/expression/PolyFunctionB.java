/**
 * 2017-11-25
 */
package cn.timelives.java.math.numberModels.expression;

import cn.timelives.java.math.exceptions.UnsupportedCalculationException;
import cn.timelives.java.math.numberModels.Multinomial;

import java.util.function.BinaryOperator;

/**
 * A double-parameter function
 * @author liyicheng
 * 2017-11-25 19:11
 *
 */
public interface PolyFunctionB extends BinaryOperator<Multinomial>{
	/**
	 * Returns the result of applying the function, or throws UnsupportedCalculationException if the result cannot be 
	 * returned as a Multinomial.
	 * @param p1 a Multinomial
	 * @param p2 another Multinomial
	 * @return the result of applying the function
	 */
	public Multinomial apply(Multinomial p1, Multinomial p2)throws UnsupportedCalculationException;
}
