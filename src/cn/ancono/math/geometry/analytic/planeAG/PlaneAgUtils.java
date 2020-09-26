/**
 *
 */
package cn.ancono.math.geometry.analytic.planeAG;

import cn.ancono.math.geometry.analytic.planeAG.curve.SubstituableCurve;

import java.awt.geom.Point2D;
import java.util.function.DoubleFunction;
import java.util.function.ToDoubleFunction;

/**
 * The utility class for planeAG.
 * @author liyicheng
 *
 */
public final class PlaneAgUtils {

    /**
     *
     */
    private PlaneAgUtils() {
    }

    /**
     * Returns the area of triangle <i>ABC</i>, the
     * area may be negate.
     * @param A
     * @param B
     * @param C
     * @return
     */
    public static <T> T area(Point<T> A, Point<T> B, Point<T> C) {
        Triangle<T> tri = new Triangle<>(A.getMathCalculator(), A, B, C);
        return tri.areaPN();
    }

    public static <T> T angleCos(Point<T> A, Point<T> O, Point<T> B) {
        return PVector.vector(O, A).angleCos(PVector.vector(O, B));
    }



}
