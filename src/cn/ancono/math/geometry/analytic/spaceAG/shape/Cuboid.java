package cn.ancono.math.geometry.analytic.spaceAG.shape;

import cn.ancono.math.MathCalculator;

public abstract class Cuboid<T> extends Parallelepiped<T> {

    protected Cuboid(MathCalculator<T> mc) {
        super(mc, 4);
    }

}
