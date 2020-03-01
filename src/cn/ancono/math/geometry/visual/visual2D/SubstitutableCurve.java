/**
 * 2017-11-16
 */
package cn.ancono.math.geometry.visual.visual2D;

/**
 * @author liyicheng
 * 2017-11-16 20:59
 */
public interface SubstitutableCurve {

    /**
     * Computes the value of {@code f(x,y)} as double
     *
     * @param x
     * @param y
     */
    double compute(double x, double y);

}