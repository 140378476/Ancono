package cn.ancono.math.geometry.differential

import cn.ancono.math.algebra.linear.AbstractVector
import cn.ancono.math.algebra.linear.Matrix
import cn.ancono.math.algebra.linear.Vector
import cn.ancono.math.function.AbstractSVFunction
import cn.ancono.math.function.DerivableFunction
import cn.ancono.math.function.DerivableSVFunction
import cn.ancono.math.function.NDerivableFunction
import cn.ancono.math.geometry.analytic.space.SVector
import cn.ancono.math.numberModels.api.RealCalculator
import cn.ancono.math.numberModels.api.plus


/**
 * Contains some utility methods for differential geometry.
 */
object DifferentialUtil {

    /**
     * Returns the matrix product of two derivable matrix function as a derivable function.
     */
    fun <T> matrixMultiply(f: DerivableFunction<T, out Matrix<T>>, g: DerivableFunction<T, out Matrix<T>>): DerivableFunction<T, Matrix<T>> {
        return DerivableFunction.multiply(f, g, { a, b -> a + b }, { a, b -> a * b })
    }

    /**
     * Returns the inner product of two derivable vector function as a derivable function.
     */
    fun <T> innerProduct(mc: RealCalculator<T>, f: DerivableFunction<T, out AbstractVector<T>>,
                         g: DerivableFunction<T, out AbstractVector<T>>
    ): DerivableSVFunction<T> {
        return DerivableFunction.multiplySV(f, g, mc::add, AbstractVector<T>::inner)
    }

    /**
     * Returns the outer product of two derivable vector function as a derivable function.
     */
    fun <T> outerProduct(f: DVFunction<T>, g: DVFunction<T>
    ): DVFunction<T> {
        return DerivableFunction.multiply(f, g, SVector<T>::add, SVector<T>::outerProduct)
    }

    /**
     * Returns the outer product of two derivable vector function as a derivable function.
     */
    fun <T> mixedProduct(f: DVFunction<T>, g: DVFunction<T>, h: DVFunction<T>, mc: RealCalculator<T>
    ): DerivableSVFunction<T> {
        return DerivableFunction.multiplyAllSV(listOf(f, g, h), { ls -> SVector.mixedProduct(ls[0], ls[1], ls[2]) }, { ls ->
            ls.reduce(mc::add)
        })
    }

    /**
     * Returns the length of a derivable vector function as a derivable function.
     */
    fun <T> length(mc: RealCalculator<T>, f: DerivableFunction<T, out AbstractVector<T>>): DerivableSVFunction<T> {
        val l2 = innerProduct(mc, f, f)
        val sqrt = AbstractSVFunction.sqrt(mc)
        return DerivableFunction.composeSV(l2, sqrt, mc)
    }

    /**
     * Returns a derivable function of `f(t)/|f(t)|`
     */
    fun <T> unitVector(mc: RealCalculator<T>, f: DerivableFunction<T, Vector<T>>): DerivableFunction<T, Vector<T>> {
        val len = length(mc, f)
        @Suppress("UNCHECKED_CAST")
        return DerivableFunction.divide(f, len, mc,
                Vector<T>::add,
                Vector<T>::subtract
        ) { k, v -> v.multiply(k) }
    }

    /**
     * Returns a derivable function of `f(t)/|f(t)|`
     */
    fun <T> unitVectorSpace(mc: RealCalculator<T>, f: DerivableFunction<T, SVector<T>>): DerivableFunction<T, SVector<T>> {
        val len = length(mc, f)
        @Suppress("UNCHECKED_CAST")
        return DerivableFunction.divide(f, len, mc,
                { v1, v2 -> v1.add(v2) },
                { v1, v2 -> v1.subtract(v2) },
                { k, v -> v.multiply(k) })
    }

    /**
     * Returns the inner product of two derivable vector function as a derivable function.
     */
    fun <T> innerProduct(mc: RealCalculator<T>, f: NDerivableFunction<T, out AbstractVector<T>>, g: NDerivableFunction<T, out AbstractVector<T>>
    ): NDerivableFunction<T, T> {
        return NDerivableFunction.multiply(f, g, mc::add, AbstractVector<T>::inner)
    }

    /**
     * Returns the outer product of two derivable vector function as a derivable function.
     */
    fun <T> outerProduct(f: NDerivableFunction<T, SVector<T>>, g: NDerivableFunction<T, SVector<T>>
    ): NDerivableFunction<T, SVector<T>> {
        return NDerivableFunction.multiply(f, g, SVector<T>::add, SVector<T>::outerProduct)
    }

    /**
     * Returns the length of a derivable vector function as a derivable function.
     */
    fun <T> length(mc: RealCalculator<T>, f: NDerivableFunction<T, out AbstractVector<T>>): NDerivableFunction<T, T> {
        val l2 = innerProduct(mc, f, f)
        val sqrt = AbstractSVFunction.sqrt(mc)
        return NDerivableFunction.compose(l2, sqrt, mc::add, mc::multiply)
    }

    /**
     * Returns a derivable function of `f(t)/|f(t)|`
     */
    fun <T> unitVectorSpace(mc: RealCalculator<T>, f: NDerivableFunction<T, SVector<T>>): NDerivableFunction<T, SVector<T>> {
        val len = length(mc, f)
        @Suppress("UNCHECKED_CAST")
        return NDerivableFunction.divide(f, len, mc,
                { v1, v2 -> v1.add(v2) },
                { v1, v2 -> v1.subtract(v2) },
                { k, v -> v.multiply(k) })
    }


}