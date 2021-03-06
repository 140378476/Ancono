/*
 * 2017-12-10
 */
package test.math;

import cn.ancono.math.IMathObject;
import cn.ancono.math.numberModels.api.RealCalculator;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.function.Predicate;

/**
 * @author liyicheng
 * 2017-12-10 20:36
 *
 */
public final class TestUtils {

	/**
	 * 
	 */
	public TestUtils() {
	}
	
	
	public static <T> org.hamcrest.Matcher<T> isTrue(Predicate<T> f,String des){
		return new BaseMatcher<T>() {
			/*
			 * @see org.hamcrest.Matcher#matches(java.lang.Object)
			 */
			@SuppressWarnings("unchecked")
			@Override
			public boolean matches(Object item) {
				try {
				return f.test((T)item);
				}catch(ClassCastException es) {
					return false;
                }
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(des);
            }
        };
    }

    public static <T> org.hamcrest.Matcher<T> isZero(RealCalculator<T> mc) {
        return new BaseMatcher<T>() {
            /*
             * @see org.hamcrest.Matcher#matches(java.lang.Object)
             */
            @SuppressWarnings("unchecked")
            @Override
            public boolean matches(Object item) {
                try {
                    return mc.isZero((T) item);
                } catch (ClassCastException es) {
					return false;
				}
			}

			@Override
			public void describeTo(Description description) {
                description.appendText("zero");
            }
        };
    }

    public static <T> void assertMathEquals(T expected, T actual, RealCalculator<T> mc) {
        if (!mc.isEqual(expected, actual)) {
            throw new AssertionError("Expected <" + expected + ">, actual <" + actual + ">");
        }
    }

    public static <T> void assertValueEquals(IMathObject<T> expected, IMathObject<T> actual) {
        if (!expected.valueEquals(actual)) {
            throw new AssertionError("Expected <" + expected + ">, actual <" + actual + ">");
        }
    }

}
