/**
 * 
 */
package cn.timelives.java.math.planeAG.curve;

import java.util.function.Function;

import cn.timelives.java.math.FlexibleMathObject;
import cn.timelives.java.math.numberModels.MathCalculator;
import cn.timelives.java.math.numberModels.NumberFormatter;
import cn.timelives.java.math.planeAG.PAffineTrans;
import cn.timelives.java.math.planeAG.Point;

/**
 * TransformedCurve is a wrapper class to describe a PlaneCurve which is 
 * @author liyicheng
 *
 */
public final class TransformedCurve<T> extends AbstractPlaneCurve<T> {
	private final AbstractPlaneCurve<T> original;
	/**
	 * The transformation to original curve
	 */
	private final PAffineTrans<T> backward;
	/**
	 * @param mc
	 */
	protected TransformedCurve(MathCalculator<T> mc,AbstractPlaneCurve<T> original,PAffineTrans<T> bk) {
		super(mc);
		this.original = original;
		backward = bk;
	}
	
	public AbstractPlaneCurve<T> getOriginal(){
		return original;
	}
	/**
	 * Returns the transformation that should be performed before testing contains.
	 * @return
	 */
	public PAffineTrans<T> getBackwardTrans(){
		return backward;
	}
	
	/* (non-Javadoc)
	 * @see cn.timelives.java.math.planeAG.PlaneCurve#contains(cn.timelives.java.math.planeAG.Point)
	 */
	@Override
	public boolean contains(Point<T> p) {
		return original.contains(backward.apply(p));
	}
	/* (non-Javadoc)
	 * @see cn.timelives.java.math.FlexibleMathObject#mapTo(java.util.function.Function, cn.timelives.java.math.number_models.MathCalculator)
	 */
	
	@Override
	public <N> TransformedCurve<N> mapTo(Function<T, N> mapper, MathCalculator<N> newCalculator) {
		// this should be assured that the returned type is a plane curve of type N.
		AbstractPlaneCurve<N> npc = original.mapTo(mapper, newCalculator);
		return new TransformedCurve<N>(newCalculator, npc, backward.mapTo(mapper, newCalculator));
	}
	
	/* (non-Javadoc)
	 * @see cn.timelives.java.math.FlexibleMathObject#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		if(!(obj instanceof TransformedCurve)){
			return false;
		}
		TransformedCurve<?> tc = (TransformedCurve<?>)obj;
		return original.equals(tc.original) && backward.equals(tc.backward);
	}
	
	/* (non-Javadoc)
	 * @see cn.timelives.java.math.FlexibleMathObject#valueEquals(cn.timelives.java.math.FlexibleMathObject)
	 */
	@Override
	public boolean valueEquals(FlexibleMathObject<T> obj) {
		if(this == obj){
			return true;
		}
		if(!(obj instanceof TransformedCurve)){
			return false;
		}
		TransformedCurve<T> tc = (TransformedCurve<T>)obj;
		return original.valueEquals(tc.original) && backward.valueEquals(tc.backward);
	}
	/* (non-Javadoc)
	 * @see cn.timelives.java.math.FlexibleMathObject#valueEquals(cn.timelives.java.math.FlexibleMathObject, java.util.function.Function)
	 */
	@Override
	public <N> boolean valueEquals(FlexibleMathObject<N> obj, Function<N, T> mapper) {
		if(!(obj instanceof TransformedCurve)){
			return false;
		}
		TransformedCurve<N> tc = (TransformedCurve<N>)obj;
		return original.valueEquals(tc.original,mapper) && backward.valueEquals(tc.backward,mapper);
	}
	/* (non-Javadoc)
	 * @see cn.timelives.java.math.FlexibleMathObject#toString(cn.timelives.java.math.number_models.NumberFormatter)
	 */
	@Override
	public String toString(NumberFormatter<T> nf) {
		StringBuilder sb = new StringBuilder(64);
		sb.append("TransformedCurve:original=").append(original.toString(nf)).append(", inversed transformation=").append(backward.toString(nf));
		return sb.toString();
	}
	
	
	
}
