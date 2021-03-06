/**
 * 2017-11-25
 */
package cn.ancono.math.numberModels.expression.simplification;

import cn.ancono.math.numberModels.expression.ExprCalculator;
import cn.ancono.math.numberModels.expression.Node;
import cn.ancono.math.numberModels.expression.SimpleStrategy;
import cn.ancono.math.numberModels.expression.anno.AllowModify;

/**
 * A simplification strategy deals with the simplification of expressions. To apply a strategy, it should be
 * registered to a calculator which will invoke the simplifying method when necessary. <p>
 * <h3>Implement notes:</h3>
 * It is recommended to implement this interface by extending {@link SimpleStrategy}.<p>
 * The method of simplification is {@link #simplifyNode(Node, ExprCalculator)}, which accepts a node and an ExprCalculator.
 * The return value of the method should be either a node, or {@code null} if no simplification has been done. The
 * returned node will be treated as fully simplified, and no further simplification will be applied by the calculator. Therefore,
 * if other simplification is possible, {@link ExprCalculator#simplify(Node, int)} should be called.(which is delegated for users in
 * {@link SimpleStrategy}.)
 *
 * @author liyicheng
 * 2017-11-25 18:19
 */
public interface SimplificationStrategy {

    /**
     * Simplifies the node, returns a non-null value as the substitution
     * for the original node if any simplification is done(returning the original one is also acceptable), otherwise
     * returns {@code null} if NO simplification is done.<p>
     * If the expression node should be simplified again, then corresponding methods can be called from the ExprCalculator.
     *
     * @param node a node
     * @param mc   an ExprCalculator to support the simplification
     * @return a node or {@code null}
     */
    public Node simplifyNode(@AllowModify Node node, ExprCalculator mc);

}
