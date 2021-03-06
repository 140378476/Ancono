/**
 * 2018-02-28
 */
package cn.ancono.math.algebra.abs.structure;

import cn.ancono.math.algebra.abs.calculator.UnitRingCalculator;

/**
 * A unit ring is a ring in which the multiplication has an unit element: {@code 1}.
 *
 * @author liyicheng
 * 2018-02-28 18:47
 */
public interface UnitRing<T> extends Ring<T> {
    /**
     * Gets the unit element of this ring.
     *
     * @return {@code 1}
     */
    T unit();

    /**
     * Returns the unit ring's calculator.
     */
    @Override
    UnitRingCalculator<T> getAbelCal();

}
