package cn.ancono.math.numberModels.structure

import cn.ancono.math.AbstractMathObject
import cn.ancono.math.IMathObject
import cn.ancono.math.algebra.abs.FiniteGroups
import cn.ancono.math.algebra.abs.calculator.*
import cn.ancono.math.algebra.abs.group.finite.AbstractFiniteGroup
import cn.ancono.math.geometry.analytic.space.SVector
import cn.ancono.math.numberModels.api.FieldNumberModel
import cn.ancono.math.numberModels.api.NumberFormatter
import cn.ancono.math.numberModels.api.RealCalculator
import java.util.function.Function


class Quaternion<T>(val a: T, val b: T, val c: T, val d: T, mc: FieldCalculator<T>)
    : AbstractMathObject<T, FieldCalculator<T>>(mc), FieldNumberModel<Quaternion<T>> {
    val tensor: T by lazy(LazyThreadSafetyMode.NONE) {
        mc.eval {
            a * a + b * b + c * c + d * d
        }
    }

    /**
     * Returns the real part as a quaternion.
     */
    fun real(): Quaternion<T> {
        return real(a, calculator)
    }

    override fun isZero(): Boolean {
        return calculator.eval { isZero(a) && isZero(b) && isZero(c) && isZero(d) }
    }

    operator fun plus(y: Quaternion<T>): Quaternion<T> {
        return calculator.eval {
            Quaternion(a + y.a, b + y.b, c + y.c, d + y.d, this)
        }
    }

    operator fun minus(y: Quaternion<T>): Quaternion<T> {
        return calculator.eval {
            Quaternion(a - y.a, b - y.b, c - y.c, d - y.d, this)
        }
    }

    operator fun times(y: Quaternion<T>): Quaternion<T> {
        /*
     *      / 	1 	i 	j 	k
            1 	1 	i 	j 	k
            i 	i 	−1 	k 	−j
            j 	j 	−k 	−1 	i
            k 	k 	j 	−i 	−1
     */
        return calculator.eval {
            val na = a * y.a - b * y.b - c * y.c - d * y.d
            val nb = b * y.a + a * y.b + c * y.d - d * y.c
            val nc = c * y.a + a * y.c - b * y.d + d * y.b
            val nd = d * y.a + a * y.d + b * y.c - c * y.b
            Quaternion(na, nb, nc, nd, this)
        }
    }

    override fun add(y: Quaternion<T>): Quaternion<T> {
        return plus(y)
    }

    override fun negate(): Quaternion<T> {
        return unaryMinus()
    }

    override fun multiply(y: Quaternion<T>): Quaternion<T> {
        return times(y)
    }


    operator fun unaryMinus(): Quaternion<T> {
        return calculator.eval {
            Quaternion(-a, -b, -c, -d, this)
        }
    }

    /**
     * Returns the reciprocal of this quaternion:
     * (a - bi - cj - dk) / tensor, tensor = a^2 + b^2 + c^2 + d^2
     */
    override fun reciprocal(): Quaternion<T> {
        return conj() / tensor
    }

    /**
     * Returns the norm of this: ||this||
     */
    fun norm(): T {
        return (calculator as RealCalculator).squareRoot(tensor)
    }

    /**
     * Returns the conjugate of this quaternion:
     * > a - bi - cj - dk
     */
    fun conj(): Quaternion<T> {
        return calculator.eval {
            Quaternion(a, -b, -c, -d, calculator)
        }
    }

    operator fun times(y: T): Quaternion<T> {
        return calculator.eval {
            Quaternion(y * a, y * b, y * c, y * d, calculator)
        }
    }

    operator fun div(y: T): Quaternion<T> {
        return calculator.eval {
            Quaternion(a / y, b / y, c / y, d / y, calculator)
        }
    }

    override fun multiply(n: Long): Quaternion<T> {
        val mc = calculator
        return Quaternion(mc.multiplyLong(a, n),
                mc.multiplyLong(b, n),
                mc.multiplyLong(c, n),
                mc.multiplyLong(d, n), mc)
    }

    operator fun times(k: Long): Quaternion<T> = multiply(k)


    fun divide(n: Long): Quaternion<T> {
        val mc = calculator
        return Quaternion(mc.divideLong(a, n),
                mc.divideLong(b, n),
                mc.divideLong(c, n),
                mc.divideLong(d, n), mc)
    }

    operator fun div(k: Long): Quaternion<T> = divide(k)

    operator fun div(y: Quaternion<T>): Quaternion<T> {
        return this * y.reciprocal()
    }

    operator fun get(i: Int): T {
        return when (i) {
            0 -> a
            1 -> b
            2 -> c
            3 -> d
            else -> throw IndexOutOfBoundsException(i)
        }
    }

    operator fun component1() = a
    operator fun component2() = b
    operator fun component3() = c
    operator fun component4() = d

    fun vectorPart(): SVector<T> = SVector.valueOf(b, c, d, calculator)

    override fun valueEquals(obj: IMathObject<T>): Boolean {
        if (obj !is Quaternion) {
            return false
        }
        val mc = calculator
        return mc.isEqual(a, obj.a) && mc.isEqual(b, obj.b) && mc.isEqual(c, obj.c) && mc.isEqual(d, obj.d)
    }

    override fun toString(nf: NumberFormatter<T>): String = calculator.run {
        if (isZero(a) && isZero(b) && isZero(c) && isZero(d)) {
            return "0"
        }
        return buildString {
            if (!isZero(a)) {
                append(nf.format(a))
                append("+")
            }
            if (!isZero(b)) {
                append("(${nf.format(b)})i+")
            }
            if (!isZero(c)) {
                append("(${nf.format(c)})j+")
            }
            if (!isZero(d)) {
                append("(${nf.format(d)})k+")
            }
            deleteCharAt(length - 1)
        }
    }

    /*
     *      × 	1 	i 	j 	k
            1 	1 	i 	j 	k
            i 	i 	−1 	k 	−j
            j 	j 	−k 	−1 	i
            k 	k 	j 	−i 	−1
     */
    override fun <N> mapTo(newCalculator: EqualPredicate<N>, mapper: Function<T, N>): Quaternion<N> {
        return Quaternion(mapper.apply(a), mapper.apply(b), mapper.apply(c), mapper.apply(d), newCalculator as FieldCalculator<N>)
    }

    companion object {

        fun <T> real(a: T, mc: FieldCalculator<T>): Quaternion<T> {
            return mc.zero.let { Quaternion(a, it, it, it, mc) }
        }

        fun <T> valueOf(a: T, b: T, c: T, d: T, mc: FieldCalculator<T>): Quaternion<T> {
            return Quaternion(a, b, c, d, mc)
        }

        fun <T> parse(str: String, mc: FieldCalculator<T>, deliminator: Regex = Regex(","), parser: (String) -> T): Quaternion<T> {
            val arr = deliminator.split(str).map(parser)
            return valueOf(arr[0], arr[1], arr[2], arr[3], mc)
        }

        fun <T> zero(mc: FieldCalculator<T>): Quaternion<T> {
            return mc.zero.let { Quaternion(it, it, it, it, mc) }
        }

        fun <T> one(mc: FieldCalculator<T>): Quaternion<T> {
            return mc.run { Quaternion(one, zero, zero, zero, mc) }
        }

        fun <T> baseI(mc: FieldCalculator<T>): Quaternion<T> {
            return mc.run { Quaternion(zero, one, zero, zero, mc) }
        }

        fun <T> baseJ(mc: FieldCalculator<T>): Quaternion<T> {
            return mc.run { Quaternion(zero, zero, one, zero, mc) }
        }

        fun <T> baseK(mc: FieldCalculator<T>): Quaternion<T> {
            return mc.run { Quaternion(zero, zero, zero, one, mc) }
        }


        fun <T> calculator(mc: FieldCalculator<T>): QuaternionCalculator<T> {
            return QuaternionCalculator(mc)
        }

        /**
         * Returns the quaternion eight-group, whose elements are `1,-1,i,j,k,-i,-j,-k` and
         * the group operation is multiplication.
         */
        fun <T> quaternionGroup(mc: FieldCalculator<T>): AbstractFiniteGroup<Quaternion<T>> {
            val qc = calculator(mc)
            val gc = qc.asGroupCalculator()
            val e = one(mc)
            val i = baseI(mc)
            val j = baseJ(mc)
            val k = baseK(mc)
            val nE = -e
            val nI = -i
            val nJ = -j
            val nK = -k
            return FiniteGroups.createGroupWithoutCheck(gc, e, i, j, k, nE, nI, nJ, nK)
        }


    }


}

class QuaternionCalculator<T>(val mc: FieldCalculator<T>) : DivisionRingCalculator<Quaternion<T>> {
//    override val isMultiplyCommutative: Boolean
//        get() = false

    override val isCommutative: Boolean
        get() = false

    override fun reciprocal(x: Quaternion<T>): Quaternion<T> {
        return x.reciprocal()
    }

    override fun divide(x: Quaternion<T>, y: Quaternion<T>): Quaternion<T> {
        return x / y
    }

    override val one: Quaternion<T> = Quaternion.real(mc.one, mc)
    override val zero: Quaternion<T> = Quaternion.zero(mc)

    override fun add(x: Quaternion<T>, y: Quaternion<T>): Quaternion<T> {
        return x + y
    }

    override fun negate(x: Quaternion<T>): Quaternion<T> {
        return -x
    }

    override fun subtract(x: Quaternion<T>, y: Quaternion<T>): Quaternion<T> {
        return x - y
    }

    override fun multiply(x: Quaternion<T>, y: Quaternion<T>): Quaternion<T> {
        return x * y
    }

    override fun isEqual(x: Quaternion<T>, y: Quaternion<T>): Boolean {
        return x.valueEquals(y)
    }

    override fun multiplyLong(x: Quaternion<T>, n: Long): Quaternion<T> {
        return x.multiply(n)
    }

    override fun divideLong(x: Quaternion<T>, n: Long): Quaternion<T> {
        return x.divide(n)
    }

}

//fun main(args: Array<String>) {
//    val mc: MathCalculator<Double> = Calculators.getCalculatorDouble()
//    val q = Quaternion.zero(mc)
//    val (a, b, c, d) = q
//
//}