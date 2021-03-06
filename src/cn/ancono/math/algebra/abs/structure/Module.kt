package cn.ancono.math.algebra.abs.structure

import cn.ancono.math.algebra.abs.calculator.ModuleCalculator
import cn.ancono.math.algebra.abs.ring.RingFromCal
import cn.ancono.math.set.MathSets

/**
 * A module over a ring is a generalization of the notion of vector space over a field,
 * wherein the corresponding scalars are the elements of an arbitrary given ring (with identity) and a multiplication
 * (on the left and/or on the right) is defined between elements of the ring and elements of the module.
 * Thus, a module, like a vector space, is an additive abelian group; a product is defined between elements of the ring
 * and elements of the module that is distributive over the addition operation of each parameter and is compatible with
 * the ring multiplication.
 *
 * See [Module](https://en.wikipedia.org/wiki/Module_(mathematics)) for more introduction.
 *
 * Created at 2018/9/20 19:22
 * @author  liyicheng
 *
 */
interface Module<R, V> : AbelianGroup<V> {

    val scalars: Ring<R>
        get() = RingFromCal(abelCal.scalarCalculator, MathSets.universe())

//    override fun getA(): ModuleCalculator<R, V>

    override fun getAbelCal(): ModuleCalculator<R, V>
}