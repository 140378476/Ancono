package cn.timelives.java.math.spaceAG.shape;

import java.util.function.Function;

import cn.timelives.java.math.FlexibleMathObject;
import cn.timelives.java.math.numberModels.MathCalculator;
import cn.timelives.java.math.spaceAG.SPoint;
/**
 * TODO :unfinished
 * @author liyicheng
 *
 * @param <T>
 */
public final class Cylinder<T> extends SpaceObject<T> {
	
	//a cylinder has it 
	
	protected Cylinder(MathCalculator<T> mc) {
		super(mc);
	}
	
	@Override
	public boolean isInside(SPoint<T> p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOnSurface(SPoint<T> p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(SPoint<T> p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <N> Cylinder<N> mapTo(Function<T, N> mapper, MathCalculator<N> newCalculator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean valueEquals(FlexibleMathObject<T> obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <N> boolean valueEquals(FlexibleMathObject<N> obj, Function<N, T> mapper) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see cn.timelives.java.utilities.math.spaceAG.shape.SpaceObject#volume()
	 */
	@Override
	public T volume() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see cn.timelives.java.utilities.math.spaceAG.shape.SpaceObject#surfaceArea()
	 */
	@Override
	public T surfaceArea() {
		// TODO Auto-generated method stub
		return null;
	}

}
