package test.math.linear

import cn.ancono.math.algebra.linear.*
import cn.ancono.math.numberModels.Calculators
import cn.ancono.math.numberModels.Fraction
import cn.ancono.math.numberModels.Tensor
import org.junit.Test
import test.math.TestUtils.assertValueEquals
import kotlin.random.Random
import kotlin.test.assertEquals

/*
 * Created by liyicheng at 2020-03-10 13:36
 */
@Suppress("LocalVariableName")
class MatrixTest {

    @Test
    fun detTest() {
        val n = 10
        val A = Matrix(n, n, Calculators.doubleDev()) { _, _ ->
            Random.nextDouble()
        }
        assertEquals(MatrixImpl.det(A), MatrixImpl.detSlow(A), 0.0001)
    }

    @Test
    fun toHermitForm() {
        val cal = Calculators.integerExact()
        val m = MatrixSup.parseMatrix("[[1 2 3][4 5 6][7 8 9]]", cal) { s: String -> s.toInt() }
        var w = m.toSmithForm()
        w = w.applyAll { para: Int? -> cal.abs(para!!) }
        assertValueEquals(Matrix.diag(listOf(1, 3, 0), cal), w)
    }

    @Test
    fun toHermitForm2() {
        val cal = Calculators.integerExact()
        val m = Matrix.parse("""
            3 3 1 4
            0 1 0 0
            0 0 19 16
            0 0 0 3
        """.trimIndent(), cal, String::toInt)
        val (H, U) = MatrixUtils.toHermitFormU(m)
        val H0 = Matrix.parse("""
            3 0 1 1
            0 1 0 0
            0 0 19 1
            0 0 0 3
        """.trimIndent(), cal, String::toInt)
        val U0 = Matrix.parse("""
            1 -3 0 -1
            0 1 0 0 
            0 0 1 -5
            0 0 0 1
        """.trimIndent(), cal, String::toInt)
        assertValueEquals(H0, H)
        assertValueEquals(U0, U)
    }

    @Test
    fun toHermitForm3() {
        val cal = Calculators.integerExact()
        val m = Matrix.parse("""
            2 3 6 2
            5 6 1 6
            8 3 1 1
        """.trimIndent(), cal, String::toInt)
        val (H, U) = MatrixUtils.toHermitFormU(m)
        val H0 = Matrix.parse("""
            1 0 50 -11
            0 3 28 -2
            0 0 61 -13
        """.trimIndent(), cal, String::toInt)
        val U0 = Matrix.parse("""
            9 -5 1
            5 -2 0
            11 -6 1
        """.trimIndent(), cal, String::toInt)
//        println(U0 * m)
//        println(H0)
        assertValueEquals(H0, H)
        assertValueEquals(U0, U)
    }


    @Test
    fun modularInverse() {
        val mc = Calculators.intModN(26)
        val A = MatrixSup.parseMatrixD(
                """
            3 21 20
            4 15 23
            6 14 5
        """.trimIndent(), mc, Integer::parseInt
        )
        val result = MatrixSup.parseMatrixD(
                """
            13 21 23
            10  3 19
            24  8 13
        """.trimIndent(), mc, Integer::parseInt
        )
        val B = MatrixUtils.inverseInRing(A)
        assertValueEquals(result, B)

    }

    @Test
    fun inverseInEUD() {
        val mc = Calculators.integer()
        val A = MatrixSup.parseMatrixD(
                """
            3 4
            2 3
        """.trimIndent(), mc, Integer::parseInt)
        val C = MatrixUtils.inverseInEUD(A)
        val I = Matrix.identity(2, mc)
        assertValueEquals(I, A * C)
        assertValueEquals(I, C * A)
    }

    @Test
    fun gInverse() {
        val A = Matrix(4, 4, Fraction.calculator) { i, j ->
            Fraction.of(i + j + 1L)
        }
        val (L, R) = A.decompRank()
        assertValueEquals(A, L * R)

        val B = A.gInverse()
        assertValueEquals(A * B * A, A)
    }

    @Test
    fun qrAndKAN() {
        val mc = Calculators.doubleDev()
        val A = Matrix(5, 5, mc) { _, _ ->
            Random.nextDouble()
        }
        val (Q, R) = A.decompQR()
        assertValueEquals(Q * R, A)
        assertValueEquals(Matrix.identity(5, mc), Q.T * Q)
        assertValueEquals(Matrix.identity(5, mc), Q * Q.T)
        val (K, d, N) = A.decompKAN()
        assertValueEquals(K * Matrix.diag(d) * N, A)

        assertValueEquals(Matrix.identity(5, mc), K.T * K)
        assertValueEquals(Matrix.identity(5, mc), K * K.T)
    }

    @Test
    fun products() {
        val A = Matrix(5, 5, Calculators.doubleDev()) { _, _ ->
            Random.nextDouble()
        }
        val B = Matrix(5, 5, Calculators.doubleDev()) { _, _ ->
            Random.nextDouble()
        }
        assertValueEquals(Tensor.fromMatrix(A hadamard B), Tensor.fromMatrix(A) * Tensor.fromMatrix(B))
    }
}