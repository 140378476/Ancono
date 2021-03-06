package cn.ancono.math.algebra.linear.mapping

import cn.ancono.math.AbstractMathObject
import cn.ancono.math.IMathObject
import cn.ancono.math.algebra.abs.calculator.*
import cn.ancono.math.algebra.linear.*
import cn.ancono.math.function.Bijection
import cn.ancono.math.function.SVFunction
import cn.ancono.math.function.invoke
import cn.ancono.math.numberModels.api.*
import cn.ancono.math.property.Composable
import org.jetbrains.annotations.NotNull
import java.util.function.Function

/**
 * Describes the linear mapping over field(of type) **K** and the element of the vector space is
 * vectors of a specific size. More specifically, this mapping accepts vectors of [dimSrc] size
 * and returns vectors of [dimDest] size.
 */
interface VLinearMapping<K> : ILinearMapping<K, Vector<K>, Vector<K>> {


    /**
     * The dimension(size) of vectors that this mapping accepts.
     */
    val dimSrc: Int

    /**
     * The dimension(size) of vectors that this mapping returns.
     */
    val dimDest: Int

    /**
     * The transformation matrix of this linear mapping under the standard vector base, which
     * is a matrix whose row count is [dimDest] and column count is [dimSrc].
     *
     * For any vector `α`, the coordinate representation under the standard vector base is
     * itself, so the result of applying this mapping to `α` is the same to left-multiplying vector
     * by the [transMatrix].
     * >f(**α**) = **M****α**, where f is this linear mapping and **M** is the transformation matrix.
     */
    val transMatrix: Matrix<K>

    /**
     * The kernel of this linear mapping, which is equal to the solution space of the transformation matrix.
     */
    val kernel: VectorBasis<K>
        get() = transMatrix.kernel()

    /**
     * The image of this linear mapping, which is equal to the column space of the transformation matrix.
     */
    val image: VectorBasis<K>
        get() = transMatrix.columnSpace()


    /**
     * Performs the transformation on the given vector. This method is equal to [apply].
     */
    fun transform(v: Vector<K>): Vector<K> = apply(v)

    /**
     * Performs this linear transformation to all the given vectors.
     */
    fun transformAll(vararg vs: Vector<K>): List<Vector<K>> = vs.map { transform(it) }

    /**
     * Performs this linear transformation to all the given vectors as a list.
     */
    fun transformAll(vs: List<Vector<K>>): List<Vector<K>> = vs.map { transform(it) }

    /**
     * Transform the given vector base.
     */
    fun transformBase(vectorBase: VectorBasis<K>): VectorBasis<K> =
            VectorBasis.generate(transformAll(vectorBase.vectors))

    /**
     * Performs this linear transformation to the column vectors in the matrix.
     */
    fun transformMatrix(matrix: Matrix<K>): Matrix<K> = transMatrix * matrix

}


/**
 * Describes linear transformation over field(of type) **K**.
 */
interface VLinearTrans<K> : VLinearMapping<K>, ILinearTrans<K, Vector<K>> {
    /**
     * The dimension of this linear transformation, that is, the size of
     * vector that this transformation accepts and returns.
     */
    val dimension: Int

    override val dimDest: Int
        get() = dimension
    override val dimSrc: Int
        get() = dimension


    /**
     * The transformation matrix of this linear transformation, an invertible matrix
     * whose size is [dimension].
     */
    override val transMatrix: Matrix<K>

    /**
     * Returns the determination of this linear transformation.
     */
    val det: K
        get() = transMatrix.det()
}


/*

val expandedMatrix: Matrix<K>
        get() {
            val tm = transMatrix
            val mc = tm.mathCalculator
            return Matrix.getBuilder(dimension + 1, dimension + 1, tm.mathCalculator)
                    .fillArea(0, 0, tm)
                    .fillColumnVector(dimension, 0, translationVector)
                    .fillRow(mc.zero, dimension)
                    .set(mc.one, dimension, dimension)
                    .build()

        }

 */

abstract class LinearMapping<T> internal constructor(
//        override val transMatrix: Matrix<T>,
        override val dimSrc: Int,
        override val dimDest: Int,
        mc: FieldCalculator<T>
) : AbstractMathObject<T, FieldCalculator<T>>(mc), VLinearMapping<T> {

    override val kernel: VectorBasis<T> by lazy { super.kernel }

    override val image: VectorBasis<T> by lazy { super.image }


    /**
     * Returns the transformation under the given base.
     */
    open fun transMatrixUnder(srcBase: FullVectorBasis<T>, destBase: FullVectorBasis<T>): Matrix<T> {
        require(srcBase.vectorLength == dimSrc)
        require(destBase.vectorLength == dimDest)
        val p = srcBase.transMatrixToStandard()
        val q = destBase.transMatrixFromStandard()
        return p * transMatrix * q
    }

    open fun add(g: LinearMapping<T>): LinearMapping<T> {
        return fromMatrix(transMatrix + g.transMatrix)
    }

    open fun negate(): LinearMapping<T> {
        return fromMatrix(-transMatrix)
    }

    open fun subtract(g: LinearMapping<T>): LinearMapping<T> {
        return fromMatrix(transMatrix - g.transMatrix)
    }

    open fun multiply(k: T): LinearMapping<T> {
        return fromMatrix(transMatrix.multiply(k))
    }

    override fun valueEquals(obj: IMathObject<T>): Boolean {
        if (obj !is LinearMapping) {
            return false
        }
        return calculator == obj.calculator &&
                transMatrix.valueEquals(obj.transMatrix) && dimSrc == obj.dimSrc && dimDest == obj.dimDest
    }

//    override fun <N> valueEquals(obj: MathObject<N>, mapper: Function<N, T>): Boolean {
//        if (obj !is LinearMapping) {
//            return false
//        }
//        return dimSrc == obj.dimSrc && dimDest == obj.dimDest && transMatrix.valueEquals(obj.transMatrix, mapper)
//    }

    override fun toString(nf: NumberFormatter<T>): String = buildString {
        appendLine("Linear mapping, dimSrc=$dimSrc, dimDest=$dimDest, matrix = ")
        appendLine(transMatrix.toString(nf))
    }

    override fun apply(x: Vector<T>): Vector<T> {
        requireVectorSize(dimSrc, x)
        return transMatrix * x
    }

    companion object {
        @JvmStatic
        fun <T> fromMatrix(mat: Matrix<T>): LinearMapping<T> {
            return DLinearMapping(mat)
        }

        @JvmStatic
        fun <T> zeroMapping(dimSrc: Int, dimDest: Int, mc: RealCalculator<T>): LinearMapping<T> {
            return DLinearMapping(Matrix.zero(dimDest, dimSrc, mc), dimSrc, dimDest, mc)
        }

        @JvmStatic
        fun <T> getCalculator(dimSrc: Int, dimDest: Int, mc: RealCalculator<T>): LinearMapCal<T> {
            return LinearMapCal(mc, dimSrc, dimDest);
        }

        fun <T> isLinearRelevant(f: LinearMapping<T>, g: LinearMapping<T>): Boolean {
            val m1 = f.transMatrix
            val m2 = g.transMatrix
            require(m1.isSameShape(m2))
            val mc = f.calculator
            val a = m1[0, 0]
            val b = m2[0, 0]
            for (i in 0 until m1.row) {
                for (j in 0 until m1.column) {
                    val t = mc.eval {
                        m1[i, j] * b == a * m2[i, j]
                    }
                    if (!t) {
                        return false
                    }
                }
            }
            return true
        }

    }


}

/**
 * Default matrix implementation for linear mapping
 */
internal class DLinearMapping<T> internal constructor(override val transMatrix: Matrix<T>, dimSrc: Int, dimDest: Int, mc: FieldCalculator<T>)
    : LinearMapping<T>(dimSrc, dimDest, mc) {

    internal constructor(transMatrix: Matrix<T>)
            : this(transMatrix, transMatrix.column, transMatrix.row, transMatrix.calculator as FieldCalculator<T>)

    override fun <N> mapTo(newCalculator: EqualPredicate<N>, mapper: Function<T, N>): LinearMapping<N> {
        return DLinearMapping(transMatrix.mapTo(newCalculator, mapper), dimSrc, dimDest, newCalculator as FieldCalculator<N>)
    }


}

abstract class LinearTrans<T>
internal constructor(
        dimension: Int,
        mc: FieldCalculator<T>
) : LinearMapping<T>(dimension, dimension, mc),
        VLinearTrans<T>,
        Composable<LinearTrans<T>>,
        Bijection<Vector<T>, Vector<T>>,
        VectorModel<T, LinearTrans<T>> {

    override val dimension: Int
        get() = super<LinearMapping>.dimSrc

    override val dimSrc: Int
        get() = super<LinearMapping>.dimSrc

    override val dimDest: Int
        get() = super<LinearMapping>.dimDest

    /**
     * Determines whether this linear transformation is invertible.
     */
    val isInvertible: Boolean
        get() = transMatrix.isInvertible()

    init {
        require(dimension >= 0)
    }

    override fun apply(x: Vector<T>): Vector<T> {
        requireVectorSize(dimension, x)
        return transMatrix * x
    }


    override fun compose(before: LinearTrans<T>): LinearTrans<T> {
        return DLinearTrans(this.transMatrix * before.transMatrix)
    }

    override fun andThen(after: LinearTrans<T>): LinearTrans<T> {
        return DLinearTrans(after.transMatrix * this.transMatrix)
    }

    override fun deply(y: @NotNull Vector<T>): Vector<T> {
        return transMatrix.inverse() * y
    }

    override fun inverse(): LinearTrans<T> {
        return DLinearTrans(transMatrix.inverse(), dimension, calculator)
    }


    override fun add(y: LinearTrans<T>): LinearTrans<T> {
        return DLinearTrans(transMatrix + y.transMatrix, dimension, calculator)
    }

    override fun negate(): LinearTrans<T> {
        return DLinearTrans(-transMatrix, dimension, calculator)
    }

    override fun multiply(k: T): LinearTrans<T> {
        return DLinearTrans(transMatrix.multiply(k), dimension, calculator)
    }

    override fun divide(k: T): LinearTrans<T> {
        return multiply(calculator.reciprocal(k))
    }

    override fun isLinearRelevant(v: LinearTrans<T>): Boolean {
        return isLinearRelevant(this, v)
    }

    /**
     * Returns the transformation under a new vector base of the space. The
     * returned matrix is similar to the original transformation matrix.
     */
    fun transMatrixUnder(base: FullVectorBasis<T>): Matrix<T> {
        val p = base.asMatrix()
        val pInv = p.inverse()
        return pInv * transMatrix * p

//        return transformMatrix(base.getVectorsAsMatrix())
    }


    override fun toString(nf: NumberFormatter<T>): String = buildString {
        appendLine("Linear trans, dim=$dimension, matrix = ")
        appendLine(transMatrix.toString(nf))
    }


    companion object {
        /**
         * Creates a linear transformation from a matrix, the matrix will
         * be consider as the transformation matrix under the standard base.
         */
        @JvmStatic
        fun <T> fromMatrix(mat: Matrix<T>): LinearTrans<T> {
            return DLinearTrans(mat)
        }

        /**
         * Creates a linear transformation based on the transformation matrix under the [base].
         * Assuming the given matrix is **A**, and the vector base's transformation matrix to the standard base
         * is **P**, then the actual transformation matrix is:
         * > P^-1 * A * P
         */
        @JvmStatic
        fun <T> underBase(transMatrix: Matrix<T>, base: FullVectorBasis<T>): LinearTrans<T> {
            val pInv = base.asMatrix()
            val p = pInv.inverse()
            val transMatUnderStandard = pInv * transMatrix * p
            return DLinearTrans(transMatUnderStandard)
        }

        /**
         * Creates a linear transformation from a function.
         */
        @JvmStatic
        fun <T> fromFunction(dim: Int, mc: RealCalculator<T>, f: SVFunction<Vector<T>>): LinearTrans<T> {
            val transMatrix = Matrix.fromVectors(Vector.unitVectors(dim, mc).map { f(it) })
            return fromMatrix(transMatrix)
        }

        @JvmStatic
        fun <T> zeroTrans(dim: Int, mc: RealCalculator<T>): LinearTrans<T> {
            return fromMatrix(Matrix.zero(dim, dim, mc))
        }

        @JvmStatic
        fun <T> getCalculator(dim: Int, mc: RealCalculator<T>): LinearTransCal<T> {
            return LinearTransCal(mc, dim)
        }

    }

}

class DLinearTrans<T> internal constructor(
        override val transMatrix: Matrix<T>,
        dimension: Int,
        mc: FieldCalculator<T>
) : LinearTrans<T>(dimension, mc) {

    internal constructor(transMatrix: Matrix<T>) : this(transMatrix, transMatrix.row, transMatrix.calculator as FieldCalculator<T>)


    override fun <N> mapTo(newCalculator: EqualPredicate<N>, mapper: Function<T, N>): LinearTrans<N> {
        val nMatrix = transMatrix.mapTo(newCalculator, mapper)
        return DLinearTrans(nMatrix, dimension, newCalculator as FieldCalculator<N>)
    }
}

class LinearMapCal<T>(val mc: RealCalculator<T>, val dimSrc: Int, val dimDest: Int) : LinearSpaceCalculator<T, LinearMapping<T>> {

    override val scalarCalculator: FieldCalculator<T>
        get() = mc

    override fun scalarMultiply(k: T, v: LinearMapping<T>): LinearMapping<T> {
        return v.multiply(k)
    }

    override fun add(x: LinearMapping<T>, y: LinearMapping<T>): LinearMapping<T> {
        return x.add(y)
    }

    override fun negate(x: LinearMapping<T>): LinearMapping<T> {
        return x.negate()
    }

    override fun isLinearDependent(u: LinearMapping<T>, v: LinearMapping<T>): Boolean {
        return LinearMapping.isLinearRelevant(u, v)
    }

    override val zero: LinearMapping<T>
        get() = LinearMapping.zeroMapping(dimSrc, dimDest, mc)

    override fun isEqual(x: LinearMapping<T>, y: LinearMapping<T>): Boolean {
        return x.valueEquals(y)
    }
}

class LinearTransCal<T>(val mc: RealCalculator<T>, val dim: Int) : AlgebraCalculator<T, LinearTrans<T>> {


    override val scalarCalculator: FieldCalculator<T>
        get() = mc

    override fun scalarMultiply(k: T, v: LinearTrans<T>): LinearTrans<T> {
        return v.multiply(k)
    }

    override fun isEqual(x: LinearTrans<T>, y: LinearTrans<T>): Boolean {
        return x.valueEquals(y)
    }

    override val zero: LinearTrans<T>
        get() = LinearTrans.zeroTrans(dim, mc)

    override fun add(x: LinearTrans<T>, y: LinearTrans<T>): LinearTrans<T> {
        return x.add(y)
    }

    override fun negate(x: LinearTrans<T>): LinearTrans<T> {
        return x.negate()
    }

    override fun multiply(x: LinearTrans<T>, y: LinearTrans<T>): LinearTrans<T> {
        return x.compose(y)
    }

    override fun isLinearDependent(u: LinearTrans<T>, v: LinearTrans<T>): Boolean {
        return u.isLinearRelevant(v)
    }
}


//abstract class AffineTrans<T>(mc: MathCalculator<T>, override val dimension: Int) : MathObjectExtend<T>(mc), VLinearTrans<T> {
//
//    override fun transform(v: Vector<T>): Vector<T> {
//        return Vector.multiplyToVector(transMatrix, v) + translationVector
//    }
//
//    override fun toString(nf: FlexibleNumberFormatter<T>): String = buildString {
//        appendln("Linear transformation, expanded matrix = ")
//        appendln(expandedMatrix.contentToString(nf))
//    }
//
//    override fun valueEquals(obj: MathObject<T>): Boolean {
//        if (obj !is LinearTrans) {
//            return false
//        }
//        return dimension == obj.dimension && transMatrix.valueEquals(obj.transMatrix)
//                && translationVector.valueEquals(obj.translationVector)
//    }
//
//    override fun <N> valueEquals(obj: MathObject<N>, mapper: Function<N, T>): Boolean {
//        if (obj !is LinearTrans) {
//            return false
//        }
//        return dimension == obj.dimension && transMatrix.valueEquals(obj.transMatrix, mapper)
//                && translationVector.valueEquals(obj.translationVector, mapper)
//    }
//
//    override fun <N> mapTo(mapper: Function<T, N>, newCalculator: MathCalculator<N>): LinearTrans<N> {
//        return DLinearTrans(newCalculator, dimension,
//                transMatrix.mapTo(mapper, newCalculator),
//                translationVector.mapTo(mapper, newCalculator))
//    }
//
//}

//
//internal class DLinearTrans<T>(mc: MathCalculator<T>, dimension: Int,
//                                     override val transMatrix: Matrix<T>, override val translationVector: Vector<T>) : LinearTrans<T>(mc, dimension) {
//
//
//    init {
//        require(dimension == transMatrix.rowCount && dimension == transMatrix.columnCount)
//        require(dimension == translationVector.size)
//    }
//
//    override val expandedMatrix : Matrix<T> by lazy { super.expandedMatrix }
//
//}

