package com.nicehash.clients.domain;

import java.util.function.IntPredicate;

/**
 * Order relational operators.
 *
 * @author Peter Levart
 */
public enum OrderRelOp implements IntPredicate {
    GT(SortDirection.ASC, cmp -> cmp > 0, ">"),
    GE(SortDirection.ASC, cmp -> cmp >= 0, ">="),
    LT(SortDirection.DESC, cmp -> cmp < 0, "<"),
    LE(SortDirection.DESC, cmp -> cmp <= 0, "<=");

    private final SortDirection sortDirection;
    private final IntPredicate cmpPredicate;
    private final String operator;

    OrderRelOp(SortDirection sortDirection, IntPredicate cmpPredicate, String operator) {
        this.sortDirection = sortDirection;
        this.cmpPredicate = cmpPredicate;
        this.operator = operator;
    }

    /**
     * Tests the relational operator on the result of comparison.
     *
     * @param value the result of either {@link Comparable#compareTo(Object)} or
     *              {@link java.util.Comparator#compare(Object, Object)}
     * @return true if the result of comparison matches this relational operator
     */
    @Override
    public boolean test(int value) {
        return cmpPredicate.test(value);
    }

    /**
     * Applies the relational operator on a pair of {@link Comparable} arguments.
     *
     * @param v1  the 1st argument
     * @param v2  the 2nd argument
     * @param <T> the type of arguments
     * @return true if the result of comparison of the arguments matches this
     * relational operator
     */
    public <T extends Comparable<T>> boolean apply(T v1, T v2) {
        return cmpPredicate.test(v1.compareTo(v2));
    }

    /**
     * @return Typical sort direction needed when sorting the results that
     * match the relational operator in order to get the paging right.
     * <ul>
     * <li>{@link #GT} -> {@link SortDirection#ASC}</li>
     * <li>{@link #GE} -> {@link SortDirection#ASC}</li>
     * <li>{@link #LT} -> {@link SortDirection#DESC}</li>
     * <li>{@link #LE} -> {@link SortDirection#DESC}</li>
     * </ul>
     */
    public SortDirection sortDirection() {
        return sortDirection;
    }

    /**
     * Get string representation of this operator.
     *
     * <ul>
     * <li>{@link #GT} -> > </li>
     * <li>{@link #GE} -> >= </li>
     * <li>{@link #LT} -> < </li>
     * <li>{@link #LE} -> <= </li>
     * </ul>
     *
     * @return string representation of this operator
     */
    public String getOperator() {
        return operator;
    }
}
