package cn.ancono.math

import cn.ancono.math.numberModels.api.RealCalculator
import cn.ancono.math.set.Interval

abstract class MathObjectRealExtend<T>(mc: RealCalculator<T>) : AbstractMathObjectReal<T>(mc) {
    protected operator fun T.plus(y: T) = mc.add(this, y)
    protected operator fun T.minus(y: T) = mc.subtract(this, y)
    protected operator fun T.times(y: T) = mc.multiply(this, y)
    protected operator fun T.div(y: T) = mc.divide(this, y)
    protected operator fun T.times(y: Long) = mc.multiplyLong(this, y)
    protected operator fun T.div(y: Long) = mc.divideLong(this, y)
    protected operator fun T.compareTo(y: T) = mc.compare(this, y)
    protected operator fun T.unaryMinus() = mc.negate(this)
    protected operator fun T.rangeTo(y: T) = Interval.closedInterval(this, y, mc)!!

}
