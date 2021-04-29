/**
 * 2018-01-25
 */
package test.math;

import cn.ancono.math.MathCalculator;
import cn.ancono.math.algebra.linear.LinearEquationSolution;
import cn.ancono.math.algebra.linear.Matrix;
import cn.ancono.math.algebra.linear.MatrixSup;
import cn.ancono.math.algebra.linear.Vector;
import cn.ancono.math.equation.SVPEquation;
import cn.ancono.math.geometry.analytic.plane.curve.ConicSection;
import cn.ancono.math.geometry.analytic.plane.curve.GeneralConicSection;
import cn.ancono.math.numberModels.Calculators;
import cn.ancono.math.numberModels.Fraction;
import cn.ancono.utilities.ArraySup;
import cn.ancono.utilities.structure.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author liyicheng 2018-01-25 17:38
 */
public class TestMatrix {

    /**
     *
     */
    public TestMatrix() {
    }

    MathCalculator<Long> mc = Calculators.longCal();
    MathCalculator<Double> mcd = Calculators.doubleDev();

    private <T> boolean isUpperTriangular(Matrix<T> m) {
        var mc = m.getMathCalculator();
        var n = Math.min(m.getRowCount(), m.getColumnCount());
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (!mc.isZero(m.get(i, j))) {
                    return false;
                }
            }
        }
        return true;
    }

    //	@Test
    @Test
    public void testEigenEquation() {
        Matrix<Long> mat = Matrix.of(new long[][]{{1, 0}, {0, 4}});
        SVPEquation<Long> equation = mat.charEquation(), expected = SVPEquation.quadratic(1L, -5L, 4L, mc);
        assertTrue("EigenEquation:", expected.valueEquals(equation));
        mat = Matrix.of(new long[][]{{1, 2}, {3, 4}});
        equation = mat.charEquation();
        expected = SVPEquation.quadratic(1L, -5L, -2L, mc);
        assertTrue("EigenEquation:", expected.valueEquals(equation));
        mat = Matrix.of(new long[][]{{1, 2, 3}, {3, 4, 5}, {4, 5, 6}});
        equation = mat.charEquation();
        expected = SVPEquation.valueOf(mc, 0L, -9L, -11L, 1L);
        assertTrue("EigenEquation:", expected.valueEquals(equation));
    }

    @Test
    public void testSolveEquation() {
        int row = 8;
        int column = 10;
        for (int i = 0; i < 100; i++) {
            double[][] mat = new double[row][];
            for (int j = 0; j < row; j++) {
                mat[j] = ArraySup.ranDoubleArr(column);
            }
            Matrix<Double> matrix = Matrix.of(mat).mapTo(mcd, x -> x);
            LinearEquationSolution<Double> solution = MatrixSup.solveLinearEquation(matrix);
            if (solution.getSolutionSituation() != LinearEquationSolution.Situation.EMPTY) {
                Vector<Double> base = solution.getSpecialSolution();
                Vector<Double>[] ks = solution.getBaseSolutions();
                if (ks != null) {
                    base = Vector.addAll(base, ks);
                }
                Matrix<Double> re1 = Matrix.multiply(matrix.subMatrix(0, 0, row - 1, column - 2), base),
                        re2 = matrix.subMatrix(0, column - 1, row - 1, column - 1);
                assertTrue(re1.valueEquals(re2));
            }
        }
    }

    //	@Test
    public void testSolveHomoEquation() {
        int row = 8;
        int column = 11;
        Vector<Double> zero = Vector.zeroVector(row, mcd);
        for (int i = 0; i < 100; i++) {
            double[][] mat = new double[row][];
            for (int j = 0; j < row; j++) {
                mat[j] = ArraySup.ranDoubleArr(column);
            }
            Matrix<Double> matrix = Matrix.of(mat).mapTo(mcd, x -> x);
            LinearEquationSolution<Double> solution = MatrixSup.solveHomo(matrix);
            if (solution.getSolutionSituation() != LinearEquationSolution.Situation.EMPTY) {
                Vector<Double> base = solution.getSpecialSolution();
                Vector<Double>[] ks = solution.getBaseSolutions();
                if (ks != null) {
                    base = Vector.addAll(base, ks);
                }
                Matrix<Double> re = Matrix.multiply(matrix, base);
                assertTrue(re.valueEquals(zero));
            }
        }
    }

    @Test
    public void testEigenVector() {
        Matrix<Double> mat = Matrix.of(new double[][]{
                {0, 1, 1},
                {1, 0, 1},
                {1, 1, 0}
        });
        List<Pair<Double, Vector<Double>>> list = mat.eigenvaluesAndVectors(x -> {
            return Arrays.asList(-1d, -1d, 2d);
        });
        assertEquals("", list.toString(), "[[-1.0,[1.0,-1.0,0.0]], [-1.0,[1.0,0.0,-1.0]], [2.0,[-1.0,-1.0,-1.0]]]");
    }

    @Test
    public void testNormalizeUsingMatrix() {
        ConicSection<Double> cs = GeneralConicSection.generalFormula(
                2d, -Math.sqrt(3), 1d,
                0d, 0d, -10d, mcd);
        ConicSection<Double> afterTrans = cs.toStandardForm();
        assertTrue("", mcd.isEqual(afterTrans.getA(), 2.5) && mcd.isEqual(afterTrans.getC(), 0.5));
        assertEquals("", cs.determineType(), ConicSection.Type.ELLIPSE);
    }

//    @Test
//    public void testDeterminant() {
//        Matrix<Integer> mat = Matrix.of(new int[][]{
//                {1, 2, 3, 4},
//                {0, 3, 4, 5},
//                {0, 0, 5, 6},
//                {0, 0, 0, 4}
//        });
//        assertEquals(mat.calDet().intValue(), 3 * 5 * 4);
//        var mat2 = Matrix.of(new double[][]{
//                {1, 2, 3, 4, 5},
//                {8, 0, 3, 4, 5},
//                {27, 1.3, 5, 5, 6},
//                {3, 4, 6, 7, 4},
//                {1, 11, 3, -4, 3}
//        });
//        assertTrue(Math.abs(mat2.calDet() - MatrixSup.fastDet(mat2)) < 0.00001);
//    }

    @Test
    public void testDecompositionLU() {
        Matrix<Fraction> A = Matrix.of(new int[][]{
                {1, 2, 3, 4},
                {2, 3, 4, 5},
                {3, 2, 5, 6},
                {6, -2, -8, 4}
        }).mapTo(Fraction.getCalculator(), Fraction::of);
        var t = A.decompLU();
        var P = t.getFirst();
        var L = t.getSecond();
        var U = t.getThird();
//        P.printMatrix();
//        L.printMatrix();
        assertTrue("L should be lower triangular.", isUpperTriangular(L.transpose()));
        assertTrue("U should be upper triangular.", isUpperTriangular(U));
//        U.printMatrix();
        var m1 = Matrix.multiply(P, A);
        var m2 = Matrix.multiply(L, U);
        assertTrue("PA = LU", m1.valueEquals(m2));
    }

    @Test
    public void testDecompCholesky() {
        var rd = new Random();
        Matrix<Double> B = Matrix.of(5, 5, Calculators.doubleDev(), (i, j) -> rd.nextDouble());
        var A = Matrix.multiply(B, B.transpose());
        var L = A.decompCholesky();
//        A.congruenceDiagForm().getFirst().printMatrix();
//        L.printMatrix();
//        A.printMatrix();
        var R = Matrix.multiply(L, L.transpose());
//        R.printMatrix();
//        var A = Matrix.of(new double[][]{
//                {4, -1, 1},
//                {-1, 4.25, 2.75},
//                {1, 2.75, 3.5}
//        });
//        var L = A.decompCholesky();
//        L.printMatrix();
//        var R = Matrix.multiply(L,L.transpose());
        assertTrue("A = LL^T", A.valueEquals(R));
    }

    @Test
    public void testDecompCholeskyD() {
        var rd = new Random();
        Matrix<Double> B = Matrix.of(5, 5, Calculators.doubleDev(), (i, j) -> rd.nextDouble());
        var A = Matrix.multiply(B, B.transpose());
        var p = A.decompCholeskyD();
        var L = p.getFirst();
        var D = Matrix.diag(p.getSecond());

//        A.congruenceDiagForm().getFirst().printMatrix();
//        L.printMatrix();
//        A.printMatrix();
        var R = Matrix.multiply(Matrix.multiply(L, D), L.transpose());

//        R.printMatrix();
//        var A = Matrix.of(new double[][]{
//                {4, -1, 1},
//                {-1, 4.25, 2.75},
//                {1, 2.75, 3.5}
//        });
//        var L = A.decompCholesky();
//        L.printMatrix();
//        var R = Matrix.multiply(L,L.transpose());
        assertTrue("A = LDL^T", A.valueEquals(R));
    }
}