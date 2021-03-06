/**
 * 2018-03-02
 */
package cn.ancono.math.property;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Describes objects that can be composed. For example, two functions can be composed to
 * a new function. The composing may be not commutative, so there are two kinds of composing:
 * {@link #compose(Composable)} and {@link #andThen(Composable)}, which correspond to the method
 * {@link Function#compose(Function)} and {@link Function#andThen(Function)}.
 * <p>
 * A composable type is naturally associative to the operation of composing. Therefore, for any composable
 * object, a semigroup can be defined.
 *
 * @author liyicheng
 * 2018-03-02 21:00
 * @see Function
 */
public interface Composable<S extends Composable<S>> {
    /**
     * Compose {@code this} and {@code before} as<br>
     * {@code this•before}
     *
     * @param before the object composed in right
     * @see Function#compose(Function)
     * @see #andThen(Composable)
     */
    @NotNull
    S compose(@NotNull S before);

    /**
     * Compose {@code after} and {@code this} as<br>
     * {@code after•this}
     *
     * @param after the object composed in left
     * @see Function#andThen(Function)
     * @see #compose(Composable)
     */
    @NotNull
    S andThen(@NotNull S after);
}
