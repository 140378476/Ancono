/**
 * 2017-10-13
 */
package cn.ancono.math.function

import cn.ancono.math.IMathObject
import cn.ancono.math.algebra.abs.calculator.EqualPredicate
import cn.ancono.math.numberModels.api.NumberFormatter
import cn.ancono.math.numberModels.api.RealCalculator
import java.util.function.Function

/**
 * A combined function is a combination of two functions, f(x) and g(x).
 *
 * @author liyicheng
 * 2017-10-13 19:18
 */
abstract class CombinedFunction<T>
/**
 *
 */ internal constructor(protected open val f: AbstractSVFunction<T>, protected open val g: AbstractSVFunction<T>, mc: RealCalculator<T>?) : AbstractSVFunction<T>(mc!!) {
    /*
     * @see cn.ancono.math.function.AbstractSVFunction#mapTo(java.util.function.Function, cn.ancono.math.numberModels.api.MathCalculator)
     */
    abstract override fun <N> mapTo(newCalculator: EqualPredicate<N>, mapper: Function<T, N>): CombinedFunction<N>

    /**
     * Defines the combined function:
     * <pre>f(x) + g(x)</pre>
     *
     * @param <T>
     * @author liyicheng
     * 2017-10-13 19:25
    </T> */
    open class Add<T>
    /**
     * @param f
     * @param g
     * @param mc
     */
    internal constructor(f: AbstractSVFunction<T>, g: AbstractSVFunction<T>, mc: RealCalculator<T>?) : CombinedFunction<T>(f, g, mc) {
        /*
         * @see cn.ancono.math.function.SVFunction#apply(java.lang.Object)
         */
        override fun apply(x: T): T {
            return mc.add(f.apply(x), g.apply(x))
        }

        /*
         * @see cn.ancono.math.function.CombinedFunction#mapTo(java.util.function.Function, cn.ancono.math.numberModels.api.MathCalculator)
         */
        override fun <N> mapTo(newCalculator: EqualPredicate<N>, mapper: Function<T, N>): Add<N> {
            return Add(f.mapTo(newCalculator, mapper), g.mapTo(newCalculator, mapper), newCalculator as RealCalculator)
        }

        /*
         * @see cn.ancono.math.function.AbstractSVFunction#toString(cn.ancono.math.numberModels.api.NumberFormatter)
         */
        override fun toString(nf: NumberFormatter<T>): String {
            return f.toString(nf) + " + " + g.toString(nf)
        }

        /*
         * @see cn.ancono.math.FlexibleMathObject#valueEquals(cn.ancono.math.FlexibleMathObject)
         */
        override fun valueEquals(obj: IMathObject<T>): Boolean {
            if (obj !is Add<*>) {
                return false
            }
            val add = obj as Add<T>
            return f.valueEquals(add.f) && g.valueEquals(add.g)
        }
    }

//    class AddD<T>
//    /**
//     * @param f
//     * @param g
//     * @param mc
//     */
//    internal constructor(f: DerivableSVFunction<T>, g: DerivableSVFunction<T>, mc: MathCalculator<T>?) : Add<T>(f, g, mc), DerivableFunction<T,T> {
//
//        /*
//                 * @see cn.ancono.math.calculus.SDerivable#derive()
//                 */
//        override fun derive(): Add<T> {
//            if (f !is SDerivable<*, *> || g !is SDerivable<*, *>) {
//                throw UnsupportedOperationException("Not Deriable")
//            }
//            val fx = f as SDerivable<T, AbstractSVFunction<T>>
//            val gx = g as SDerivable<T, AbstractSVFunction<T>>
//            val f_ = fx.derive()
//            val g_ = gx.derive()
//            return if (f_ is SDerivable<*, *> && g_ is SDerivable<*, *>) {
//                AddD(f_, g_, mc)
//            } else Add(f_, g_, mc)
//        }
//    }

    /**
     * Defines the combined function:
     * <pre>f(x) - g(x)</pre>
     *
     * @param <T>
     * @author liyicheng
     * 2017-10-13 19:25
    </T> */
    open class Subtract<T>
    /**
     * @param f
     * @param g
     * @param mc
     */
    internal constructor(f: AbstractSVFunction<T>, g: AbstractSVFunction<T>, mc: RealCalculator<T>?) : CombinedFunction<T>(f, g, mc) {
        /*
         * @see cn.ancono.math.function.SVFunction#apply(java.lang.Object)
         */
        override fun apply(x: T): T {
            return mc.subtract(f.apply(x), g.apply(x))
        }

        /*
         * @see cn.ancono.math.function.CombinedFunction#mapTo(java.util.function.Function, cn.ancono.math.numberModels.api.MathCalculator)
         */
        override fun <N> mapTo(newCalculator: EqualPredicate<N>, mapper: Function<T, N>): Subtract<N> {
            return Subtract(f.mapTo(newCalculator, mapper), g.mapTo(newCalculator, mapper), newCalculator as RealCalculator)
        }

        /*
         * @see cn.ancono.math.function.AbstractSVFunction#toString(cn.ancono.math.numberModels.api.NumberFormatter)
         */
        override fun toString(nf: NumberFormatter<T>): String {
            return f.toString(nf) + " - " + g.toString(nf)
        }

        /*
         * @see cn.ancono.math.FlexibleMathObject#valueEquals(cn.ancono.math.FlexibleMathObject)
         */
        override fun valueEquals(obj: IMathObject<T>): Boolean {
            if (obj !is Subtract<*>) {
                return false
            }
            val add = obj as Subtract<T>
            return f.valueEquals(add.f) && g.valueEquals(add.g)
        }
    }

//    class SubtractD<T>
//    /**
//     * @param f
//     * @param g
//     * @param mc
//     */
//    internal constructor(f: AbstractSVFunction<T>, g: AbstractSVFunction<T>, mc: MathCalculator<T>?) : Subtract<T>(f, g, mc), SDerivable<T, Subtract<T>?> {
//        /*
//         * @see cn.ancono.math.calculus.SDerivable#derive()
//         */
//        override fun derive(): Subtract<T> {
//            if (f !is SDerivable<*, *> || g !is SDerivable<*, *>) {
//                throw UnsupportedOperationException("Not Deriable")
//            }
//            val fx = f as SDerivable<T, AbstractSVFunction<T>>
//            val gx = g as SDerivable<T, AbstractSVFunction<T>>
//            val f_ = fx.derive()
//            val g_ = gx.derive()
//            return if (f_ is SDerivable<*, *> && g_ is SDerivable<*, *>) {
//                SubtractD(f_, g_, mc)
//            } else Subtract(f_, g_, mc)
//        }
//    }

    /**
     * Defines the combined function:
     * <pre>f(x) * g(x)</pre>
     *
     * @param <T>
     * @author liyicheng
     * 2017-10-13 19:25
    </T> */
    open class Multiply<T>
    /**
     *
     */
    internal constructor(f: AbstractSVFunction<T>, g: AbstractSVFunction<T>, mc: RealCalculator<T>?) : CombinedFunction<T>(f, g, mc) {
        /*
         * @see cn.ancono.math.function.SVFunction#apply(java.lang.Object)
         */
        override fun apply(x: T): T {
            return mc.multiply(f.apply(x), g.apply(x))
        }

        /*
         * @see cn.ancono.math.function.CombinedFunction#mapTo(java.util.function.Function, cn.ancono.math.numberModels.api.MathCalculator)
         */
        override fun <N> mapTo(newCalculator: EqualPredicate<N>, mapper: Function<T, N>): Multiply<N> {
            return Multiply(f.mapTo(newCalculator, mapper), g.mapTo(newCalculator, mapper), newCalculator as RealCalculator)
        }

        /*
         * @see cn.ancono.math.function.AbstractSVFunction#toString(cn.ancono.math.numberModels.api.NumberFormatter)
         */
        override fun toString(nf: NumberFormatter<T>): String {
            return "(" + f.toString(nf) + ")*(" + g.toString(nf) + ")"
        }

        /*
         * @see cn.ancono.math.FlexibleMathObject#valueEquals(cn.ancono.math.FlexibleMathObject)
         */
        override fun valueEquals(obj: IMathObject<T>): Boolean {
            if (obj !is Multiply<*>) {
                return false
            }
            val add = obj as Multiply<T>
            return f.valueEquals(add.f) && g.valueEquals(add.g)
        }
    }

//    class MultiplyD<T>
//    /**
//     * @param f
//     * @param g
//     * @param mc
//     */
//    internal constructor(f: AbstractSVFunction<T>,g: AbstractSVFunction<T>, mc: MathCalculator<T>?) : Multiply<T>(f, g, mc), SDerivable<T, Add<T>?> {
//        /*
//         * @see cn.ancono.math.calculus.SDerivable#derive()
//         */
//        override fun derive(): Add<T> {
//            if (f !is SDerivable<*, *> || g !is SDerivable<*, *>) {
//                throw UnsupportedOperationException("Not Derivable")
//            }
//            val fx = f as SDerivable<T, AbstractSVFunction<T>>
//            val gx = g as SDerivable<T, AbstractSVFunction<T>>
//            val f_ = fx.derive()
//            val g_ = gx.derive()
//            if (f_ is SDerivable<*, *> && g_ is SDerivable<*, *>) {
//                val m1 = MultiplyD(f_, g, mc)
//                val m2 = MultiplyD(f, g_, mc)
//                return AddD(m1, m2, mc)
//            }
//            val m1 = Multiply(f_, g, mc)
//            val m2 = Multiply(f, g_, mc)
//            return Add(m1, m2, mc)
//        }
//    }

    /**
     * Defines the combined function:
     * <pre>f(x) / g(x)</pre>
     *
     * @param <T>
     * @author liyicheng
     * 2017-10-13 19:25
    </T> */
    open class Divide<T>
    /**
     * @param f
     * @param g
     * @param mc
     */
    internal constructor(f: AbstractSVFunction<T>, g: AbstractSVFunction<T>, mc: RealCalculator<T>?) : CombinedFunction<T>(f, g, mc) {
        /*
         * @see cn.ancono.math.function.SVFunction#apply(java.lang.Object)
         */
        override fun apply(x: T): T {
            return mc.divide(f.apply(x), g.apply(x))
        }

        /*
         * @see cn.ancono.math.function.CombinedFunction#mapTo(java.util.function.Function, cn.ancono.math.numberModels.api.MathCalculator)
         */
        override fun <N> mapTo(newCalculator: EqualPredicate<N>, mapper: Function<T, N>): Divide<N> {
            return Divide(f.mapTo(newCalculator, mapper), g.mapTo(newCalculator, mapper), newCalculator as RealCalculator)
        }

        /*
         * @see cn.ancono.math.function.AbstractSVFunction#toString(cn.ancono.math.numberModels.api.NumberFormatter)
         */
        override fun toString(nf: NumberFormatter<T>): String {
            return "(" + f.toString(nf) + ")/(" + g.toString(nf) + ")"
        }

        /*
         * @see cn.ancono.math.FlexibleMathObject#valueEquals(cn.ancono.math.FlexibleMathObject)
         */
        override fun valueEquals(obj: IMathObject<T>): Boolean {
            if (obj !is Divide<*>) {
                return false
            }
            val add = obj as Divide<T>
            return f.valueEquals(add.f) && g.valueEquals(add.g)
        }
    }


    /**
     * Defines the combined function:
     * <pre>g(f(x))</pre>
     *
     * @param <T>
     * @author liyicheng
     * 2017-10-13 19:25
    </T> */
    open class Combine<T>
    /**
     * @param f
     * @param g
     * @param mc
     */
    internal constructor(f: AbstractSVFunction<T>, g: AbstractSVFunction<T>, mc: RealCalculator<T>?) : CombinedFunction<T>(f, g, mc) {
        /*
         * @see cn.ancono.math.function.SVFunction#apply(java.lang.Object)
         */
        override fun apply(x: T): T {
            return g.apply(f.apply(x))
        }

        /*
         * @see cn.ancono.math.function.CombinedFunction#mapTo(java.util.function.Function, cn.ancono.math.numberModels.api.MathCalculator)
         */
        override fun <N> mapTo(newCalculator: EqualPredicate<N>, mapper: Function<T, N>): Combine<N> {
            return Combine(f.mapTo(newCalculator, mapper), g.mapTo(newCalculator, mapper), newCalculator as RealCalculator)
        }

        /*
         * @see cn.ancono.math.function.AbstractSVFunction#toString(cn.ancono.math.numberModels.api.NumberFormatter)
         */
        override fun toString(nf: NumberFormatter<T>): String {
            return "(" + f.toString(nf) + ")/(" + g.toString(nf) + ")"
        }

        /*
         * @see cn.ancono.math.FlexibleMathObject#valueEquals(cn.ancono.math.FlexibleMathObject)
         */
        override fun valueEquals(obj: IMathObject<T>): Boolean {
            if (obj !is Combine<*>) {
                return false
            }
            val add = obj as Combine<T>
            return f.valueEquals(add.f) && g.valueEquals(add.g)
        }
    }

//    class CombineD<T>
//    /**
//     * @param f
//     * @param g
//     * @param mc
//     */
//    internal constructor(f: AbstractSVFunction<T>, g: AbstractSVFunction<T>, mc: MathCalculator<T>?) : Combine<T>(f, g, mc), SDerivable<T, Multiply<T>?> {
//        /*
//         * @see cn.ancono.math.calculus.SDerivable#derive()
//         */
//        override fun derive(): Multiply<T> {
//            if (f !is SDerivable<*, *> || g !is SDerivable<*, *>) {
//                throw UnsupportedOperationException("Not Deriable")
//            }
//            val fx = f as SDerivable<T, AbstractSVFunction<T>>
//            val gx = g as SDerivable<T, AbstractSVFunction<T>>
//            val f_ = fx.derive()
//            val g_ = gx.derive()
//            return if (f_ is SDerivable<*, *> && g_ is SDerivable<*, *>) {
//                MultiplyD(f_, CombineD(f, g_, mc), mc)
//            } else Multiply(f_, Combine(f, g_, mc), mc)
//        }
//    }

    companion object {
        fun <T> add(f: AbstractSVFunction<T>, g: AbstractSVFunction<T>, mc: RealCalculator<T>): Add<T> {
            return Add(f, g, mc)
        }

        fun <T> subtract(f: AbstractSVFunction<T>, g: AbstractSVFunction<T>, mc: RealCalculator<T>): Subtract<T> {
            return Subtract(f, g, mc)
        }

        fun <T> multiply(f: AbstractSVFunction<T>, g: AbstractSVFunction<T>, mc: RealCalculator<T>): Multiply<T> {
            return Multiply(f, g, mc)
        }

        fun <T> divide(f: AbstractSVFunction<T>, g: AbstractSVFunction<T>, mc: RealCalculator<T>): Divide<T> {
            return Divide(f, g, mc)
        }

//        fun <T> addOfDerivable(f: AbstractSVFunction<T>, g: AbstractSVFunction<T>, mc: MathCalculator<T>): Add<T> {
//            return AddD(Objects.requireNonNull(f), Objects.requireNonNull(g), mc)
//        }

//        fun <T> combineOfDerivable(f: AbstractSVFunction<T>, g: AbstractSVFunction<T>, mc: MathCalculator<T>): CombineD<T> {
//            return CombineD(Objects.requireNonNull(f),
//                    Objects.requireNonNull(g), mc)
//        }
//
//        fun <T> multiplyOfDerivable(f: AbstractSVFunction<T>, g: AbstractSVFunction<T>, mc: MathCalculator<T>): MultiplyD<T> {
//            return MultiplyD(Objects.requireNonNull(f),
//                    Objects.requireNonNull(g), mc)
//        }
    }
}