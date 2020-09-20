package cn.ancono.math.algebra.linearAlgebra.space

import cn.ancono.math.MathCalculator
import cn.ancono.math.MathObject
import cn.ancono.math.MathObjectExtend
import cn.ancono.math.algebra.linearAlgebra.Vector
import cn.ancono.math.algebra.linearAlgebra.VectorBasis
import cn.ancono.math.numberModels.api.FlexibleNumberFormatter
import cn.ancono.math.set.MathSet
import java.lang.Integer.max
import java.util.function.Function


/*
 * Created by liyicheng at 2020-03-06 11:48
 */
/**
 * Describes a vector space on of vectors of type [T].
 * @author liyicheng
 */
class VectorSpace<T : Any>(override val basis: VectorBasis<T>) : MathObjectExtend<T>(basis.mathCalculator),
        IVectorSpace<T> {

    override val vectorLength: Int
        get() = basis.vectorLength

    private val vc: VectorSpaceCalculator<T> = Companion.getCalculator(vectorLength, mc)

    operator fun contains(v: Vector<T>): Boolean {
        return basis.canReduce(v)
    }

    override fun getCalculator(): VectorSpaceCalculator<T> {
        return vc
    }

    override fun getSet(): MathSet<Vector<T>> {
        return MathSet { v ->
            basis.canReduce(v)
        }
    }


    fun projectTo(nVectorLength: Int): VectorSpace<T> {
        return VectorSpace(basis.projectTo(nVectorLength))
    }


    override fun isBasis(vs: List<Vector<T>>): Boolean {
        return basis.equivalentTo(VectorBasis.generate(vs))
    }

    /**
     * Returns the intersection of this and [w].
     */
    fun intersect(w: VectorSpace<T>): VectorSpace<T> {
        val length = max(vectorLength, w.vectorLength)
        val b1 = this.basis.projectTo(length)
        val b2 = w.basis.projectTo(length)
        val basis = b1.intersect(b2)
        return VectorSpace(basis)
    }

    /**
     * Returns the complement space of the vector space.
     */
    fun complement(): VectorSpace<T> {
        val nBasis = basis.getVectorsAsMatrix().solutionSpace()
        return VectorSpace(nBasis)
    }


    override fun <N : Any> mapTo(mapper: Function<T, N>, newCalculator: MathCalculator<N>): MathObject<N> {
        return VectorSpace(basis.mapTo(mapper, newCalculator))
    }

    override fun valueEquals(obj: MathObject<T>): Boolean {
        if (obj !is VectorSpace) {
            return false
        }
        val b2 = obj.basis
        return basis.equivalentTo(b2)
    }

    override fun toString(nf: FlexibleNumberFormatter<T, MathCalculator<T>>): String {
        return basis.toString(nf)
    }

    companion object {
        /**
         * Gets a calculator for a vector space.
         */
        @JvmStatic
        fun <T : Any> getCalculator(vectorLength: Int, mc: MathCalculator<T>): VectorSpaceCalculator<T> {
            return VectorSpaceCalculatorImpl(vectorLength, mc)
        }
    }


}