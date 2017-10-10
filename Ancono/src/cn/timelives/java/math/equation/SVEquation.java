package cn.timelives.java.math.equation;

import java.util.function.Function;

import cn.timelives.java.math.numberModels.MathCalculator;

/**
 * A single variable equation.The number of variable is one.
 * @author lyc
 *
 */
public abstract class SVEquation<T> extends Equation<T,T> implements SVCompareStructure<T>{

	protected SVEquation(MathCalculator<T> mc) {
		super(mc);
	}

	/**
	 * Determines whether {@code x} is the solution of this equation.
	 * @param x a number
	 * @return {@code true} if x is the solution of this equation.
	 */
	public boolean isSolution(T x) {
		return mc.isZero(compute(x));
	}
	
	
	/* (non-Javadoc)
	 * @see cn.timelives.java.math.Equation#mapTo(java.util.function.Function, cn.timelives.java.math.number_models.MathCalculator)
	 */
	@Override
	public abstract <N> SVEquation<N> mapTo(Function<T, N> mapper, MathCalculator<N> newCalculator);
}