/**
 *
 */
package cn.ancono.math.function;

import cn.ancono.math.property.Mappable;
import cn.ancono.math.set.MathSet;
import cn.ancono.math.set.MathSets;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

/**
 * Describes the function {@code f(x)} in the plane which only contains single variable. 
 * @author liyicheng
 *
 */
public interface SVFunction<T> extends MathFunction<T, T>, Mappable<T> {

    /**
     * Returns the result of {@code f(x)}.
     *
     * @param x variable x
     * @return {@code f(x)}
     */
    @Override
    T apply(T x);

    @Override
    @NotNull
    default <S> SVFunction<S> mapTo(@NotNull Bijection<T, S> mapper) {
        var domain = domain();
        var f = this;
        return new SVFunction<>() {
            @SuppressWarnings("SuspiciousNameCombination")
            private final MathSet<S> newDomain = x -> domain.contains(mapper.deply(x));

            @SuppressWarnings("SuspiciousNameCombination")
            @NotNull
            @Override
            public S apply(@NotNull S x) {
                return mapper.apply(f.apply(mapper.deply(x)));
            }

            @NotNull
            @Override
            public MathSet<S> domain() {
                return newDomain;
            }
        };
    }

    static <T> SVFunction<T> fromFunction(MathSet<T> domain, Function<T, T> f) {
        if (f instanceof SVFunction) {
            return (SVFunction<T>) f;
        }
        MathSet<T> dom = domain == null ? MathSets.universe() : domain;
        Objects.requireNonNull(f);
        return new SVFunction<>() {
            @NotNull
            @Override
            public T apply(@NotNull T x) {
                return f.apply(x);
            }

            @NotNull
            @Override
            public MathSet<T> domain() {
                return dom;
            }
        };
    }

}
