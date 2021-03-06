package test.math.linear

import cn.ancono.math.algebra.linear.MatrixSup
import cn.ancono.math.algebra.linear.T
import org.junit.Test
import test.math.TestUtils

class QuadraticFormTest {
    @Test
    fun test1() {
        val str1 = """
        3 1 0 1
        1 3 -1 -1
        0 -1 3 1
        1 -1 1 3
    """.trimIndent()
        val A = MatrixSup.parseFMatrix(str1)
        val (J, P) = A.toCongDiagForm()
        TestUtils.assertValueEquals(P.T * A * P, J)
    }


}