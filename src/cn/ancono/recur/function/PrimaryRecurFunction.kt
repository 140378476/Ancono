package cn.ancono.recur.function

import cn.ancono.recur.function.PrimaryFunctions.Add
import java.lang.IllegalArgumentException


/*
 * Created by liyicheng at 2020-03-03 17:49
 */
interface PrimaryRecurFunction : RecurFunction {
//    fun generateSequence() : GenerateSequence
}

sealed class InitialFunction : PrimaryRecurFunction

/**
 * Defines a zero function with 1 parameters.
 */
class ZeroFunction(override val paraCount: Int) : InitialFunction() {

    override fun apply(vararg paras: Int): Int {
        require(paras.size == paraCount)
        return 0
    }

    override fun apply(x: Int): Int {
        require(1 == paraCount)
        return 0
    }

    override fun apply(x1: Int, x2: Int): Int {
        require(2 == paraCount)
        return 0
    }

//    override fun generateSequence(): GenerateSequence {
//        return listOf(InitialStep(this))
//    }
}


//successive
object SuccessorFunction : InitialFunction() {
    override val paraCount: Int
        get() = 1

    override fun apply(vararg paras: Int): Int {
        require(paras.size == 1)
        return apply(paras[0])
    }

    override fun apply(x: Int): Int {
        return x + 1
    }

    override fun apply(x1: Int, x2: Int): Int {
        throw IllegalArgumentException()
    }

//    override fun generateSequence(): GenerateSequence {
//        return listOf(InitialStep(this))
//    }
}

/**
 * @param i the i-st element to project
 */
class ProjectionFunction(override val paraCount: Int, val i: Int) : InitialFunction() {

    override fun apply(vararg paras: Int): Int {
        require(paras.size == paraCount)
        return paras[i]
    }

}

/**
 * Describes the primary recursive function generated by combining some primary recursive functions.
 */
class PrimaryCombineFunction(paraCount: Int, override val f: PrimaryRecurFunction, override val gs: List<PrimaryRecurFunction>)
    : CombineFunction(paraCount, f, gs), PrimaryRecurFunction {
    //Created by lyc at 2020-03-04 11:42

}

/**
 * Describes a primary recursive function generated by primary recursion of [g] and [h], where the parameter count of [g]
 * is `paraCount-1` and the parameter count of [h] is `paraCount+1`
 * Assume `f` is this function, then
 *
 *     f(0,x_2,...,x_n) = g(x_2,...,x_n)
 *     f(x+1,x_2,...,x_n) = h(x,f(x,x_2,...,x_n),x_2,...,x_n)
 *
 */
class PrimaryRecursionFunction(override val paraCount: Int, val g: PrimaryRecurFunction, val h: PrimaryRecurFunction) : PrimaryRecurFunction {
    //Created by lyc at 2020-03-04 11:42
    override fun apply(vararg paras: Int): Int {
        require(paras.size == paraCount)
        return if (paras[0] == 0) {
            g(*paras.sliceArray(1 until paraCount))
        } else {
            val x = paras[0]
            paras[0]--
            val fx = this(*paras)
            val p = IntArray(paraCount + 1)
            p[0] = x - 1
            p[1] = fx
            System.arraycopy(paras, 1, p, 2, paraCount - 1)
            h(*p)
        }
    }
}

object PrimaryFunctions {
    val Id: PrimaryRecurFunction

    init {
        Id = PrimaryRecursionFunction(1,
                ZeroFunction(0),
                PrimaryCombineFunction(2, SuccessorFunction,
                        listOf(ProjectionFunction(2, 1))
                )
        )
    }

    val Add: PrimaryRecurFunction

    init {
        Add = PrimaryRecursionFunction(2,
                Id,
                PrimaryCombineFunction(2,
                        SuccessorFunction,
                        listOf(ProjectionFunction(3, 1)))
        )
    }
}

fun main() {
    println(Add(3, 2))
}