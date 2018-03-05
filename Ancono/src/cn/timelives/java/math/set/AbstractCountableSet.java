/**
 * 
 */
package cn.timelives.java.math.set;

import java.math.BigInteger;
import java.util.function.Function;

import cn.timelives.java.math.numberModels.MathCalculator;

/**
 *
 *
 */
public abstract class AbstractCountableSet<T> extends AbstractMathSet<T> implements CountableSet<T>{
	
	
	/**
	 * @param mc
	 */
	protected AbstractCountableSet(MathCalculator<T> mc) {
		super(mc);
	}
	
	
	
	
	
	@Override
	public BigInteger sizeAsBigInteger() {
		return BigInteger.valueOf(size());
	}
	
	@Override
	public abstract <N> AbstractCountableSet<N> mapTo(Function<T, N> mapper, MathCalculator<N> newCalculator);
}
