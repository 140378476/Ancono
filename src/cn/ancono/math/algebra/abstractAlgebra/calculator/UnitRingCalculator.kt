/**
 * 2018-02-28
 */
package cn.ancono.math.algebra.abstractAlgebra.calculator

import cn.ancono.math.algebra.abstractAlgebra.GroupCalculators

/**
 * @author liyicheng
 * 2018-02-28 19:05
 */
interface UnitRingCalculator<T : Any> : RingCalculator<T> {

    val one: T

    /**
     * Return `x ^ n` as defined in the multiplicative monoid.
     */
    @JvmDefault
    override fun pow(x: T, n: Long): T {
        return if (n == 0L) {
            one
        } else super.pow(x, n)
    }
}

fun <T : Any> UnitRingCalculator<T>.asMonoidCalculator() = GroupCalculators.asMonoidCalculator(this)