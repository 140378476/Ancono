/**
 * 2018-02-28
 */
package cn.ancono.math.algebra.abs.calculator


import cn.ancono.math.algebra.abs.GroupCalculators
import cn.ancono.math.exceptions.ExceptionUtil

/**
 *
 *
 * @author liyicheng
 * 2018-02-28 19:01
 */
interface DivisionRingCalculator<T> : UnitRingCalculator<T>, MulGroupCal<T> {

    /**
     * Returns the multiplicative inverse of element `x`.
     * @param x a number
     * @return `x^-1`
     */
    override fun reciprocal(x: T): T

    /**
     * Returns the result of `x / y`, which is equal to
     * `x * reciprocal(y) `
     * @param x a number
     * @param y another number
     * @return `x / y`
     */
    override fun divide(x: T, y: T): T = multiply(x, reciprocal(y))

    /**
     * Always exact division.
     */
    override fun exactDivide(x: T, y: T): T {
        return divide(x, y)
    }

    /**
     * Returns the result of `x / n`. The default implement is
     * `divide(x,multiplyLong(getOne(),n))`
     * @param x a number
     * @param n a non-zero long
     * @return `x / n`
     */
    fun divideLong(x: T, n: Long): T {
        if (n == 0L) {
            ExceptionUtil.dividedByZero()
        }
        return divide(x, multiplyLong(one, n))
    }

    /**
     * Operator function divideLong.
     * @see divideLong
     */
    operator fun T.div(y: Long) = divideLong(this, y)

    override fun isUnit(x: T): Boolean {
        // non-zero element in a division ring is always invertible
        return !isZero(x)
    }
}


fun <T> DivisionRingCalculator<T>.asGroupCalculator() = GroupCalculators.asGroupCalculator(this)