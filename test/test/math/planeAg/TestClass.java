/**
 * 
 */
package test.math.planeAg;

import cn.ancono.math.geometry.analytic.plane.PAffineTrans;
import cn.ancono.math.geometry.analytic.plane.PVector;
import cn.ancono.math.geometry.analytic.plane.Point;
import cn.ancono.math.geometry.analytic.plane.curve.ConicSection;
import cn.ancono.math.geometry.analytic.plane.curve.GeneralConicSection;
import cn.ancono.math.numberModels.Calculators;
import cn.ancono.math.numberModels.api.RealCalculator;
import org.junit.Test;

import static cn.ancono.utilities.Printer.print;
import static org.junit.Assert.assertTrue;

/**
 * @author liyicheng
 */
public class TestClass {
    /**
     *
     */
    public TestClass() {
    }

    RealCalculator<Double> mcd = Calculators.doubleDev();

    @Test
    public void testAffineTrans() {
        PAffineTrans<Double> pt = PAffineTrans.identity(mcd);
        ConicSection<Double> cs = GeneralConicSection.ellipse(1d, 2d, -3d, mcd);
        Point<Double> p = Point.valueOf(1d, 1d, mcd);
        print(cs);
        assertTrue("contains:", cs.contains(p));
        pt = pt.translate(PVector.valueOf(1d, 1d, mcd));
        cs = cs.transform(pt);
        print(pt);
		print(cs);
		print(pt.apply(p));
		print(cs.contains(pt.apply(p)));
		print("Test completed!");
	}
	
	
}
