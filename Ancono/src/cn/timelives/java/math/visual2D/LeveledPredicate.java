/**
 * 2017-11-16
 */
package cn.timelives.java.math.visual2D;

import java.util.Arrays;
import java.util.Objects;

import cn.timelives.java.math.MathUtils;

/**
 * @author liyicheng
 * 2017-11-16 21:08
 *
 */
public class LeveledPredicate extends SimpleDrawPointPredicate {
	
	
	private final double[] levels;
	/**
	 * @param precision
	 */
	LeveledPredicate(double precision,double[] levels) {
		super(precision);
		this.levels = Objects.requireNonNull(levels);
	}
	
	
	/*
	 * @see cn.timelives.java.math.visual.SimpleDrawPointPredicate#shouldDraw(double[][], int, int)
	 */
	@Override
	public boolean shouldDraw(double[][] data, int x, int y) {
		boolean should = false;
		for(int i=0;i<levels.length;i++) {
			should = shouldDraw(data, x, y, levels[i]);
			if(should)
				break;
		}
		
		return should;
	}
	
	private boolean shouldDraw(double[][] data,int x,int y,double level) {
		boolean should = false;
		double cur = data[x][y] - level;
		double curAbs = Math.abs(cur);
		if (Math.abs(cur) <= precision) {
			should = true;
		}
		if (!should && cur != Double.NaN) {
			double f = data[x - 1][y] - level;
			if (f != Double.NaN && MathUtils.oppositeSignum(cur, f) && curAbs < Math.abs(f)) {
				should = true;
			}
			if (!should) {
				f = data[x + 1][y] - level;
				if (f != Double.NaN && MathUtils.oppositeSignum(cur, f) && curAbs < Math.abs(f)) {
					should = true;
				}
			}
			if (!should) {
				f = data[x][y - 1] - level;
				if (f != Double.NaN && MathUtils.oppositeSignum(cur, f) && curAbs < Math.abs(f)) {
					should = true;
				}
			}
			if (!should) {
				f = data[x][y + 1] - level;
				if (f != Double.NaN && MathUtils.oppositeSignum(cur, f) && curAbs < Math.abs(f)) {
					should = true;
				}
			}
		}
		return should;
	}
	
	/**
	 * Returns the corresponding level.
	 * @param data
	 * @param x
	 * @param y
	 * @return
	 */
	public int getDrawLevel(double[][] data,int x,int y) {
		for(int i=0;i<levels.length;i++) {
			if(shouldDraw(data, x, y, levels[i])) {
				return i;
			}
		}
		return -1;
	}
	
	public int getLevelNum() {
		return levels.length;
	}
	
	public static LeveledPredicate getLeveledPrecidate(double[] levels,double precision) {
		return new LeveledPredicate(precision, levels);
	}
	public static LeveledPredicate getLeveledPrecidate(double[] levels) {
		return getLeveledPrecidate(levels,DEFAULT_PRECISION);
	}
}
