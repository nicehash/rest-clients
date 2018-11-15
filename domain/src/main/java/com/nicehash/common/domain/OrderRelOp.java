package com.nicehash.common.domain;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.IntPredicate;

/**
 * Order relational operators.
 *
 * @author Peter Levart
 */
public enum OrderRelOp {
    GT(cmp -> cmp > 0),
    GE(cmp -> cmp >= 0),
    LT(cmp -> cmp < 0),
    LE(cmp -> cmp <= 0);

    private final IntPredicate cmpPredicate;

    OrderRelOp(IntPredicate cmpPredicate) {
        this.cmpPredicate = cmpPredicate;
    }

    public <T extends Comparable<T>> boolean apply(T v1, T v2) {
        return cmpPredicate.test(v1.compareTo(v2));
    }

    public <T, U extends Comparable<? super U>> Comparator<T> comparing(Function<? super T, ? extends U> keyExtractor) {
        return this == LT || this == LE
               ? Comparator.<T, U>comparing(keyExtractor).reversed()
               : Comparator.<T, U>comparing(keyExtractor);
    }
}
