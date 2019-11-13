package com.nicehash.clients.domain;

import java.util.Comparator;
import java.util.function.Function;

/**
 * Sort directions.
*/
public enum SortDirection {
    ASC {
        @Override
        public <T, U extends Comparable<? super U>> Comparator<T> comparing(Function<? super T, ? extends U> keyExtractor) {
            return Comparator.comparing(keyExtractor);
        }
    },
    DESC {
        @Override
        public <T, U extends Comparable<? super U>> Comparator<T> comparing(Function<? super T, ? extends U> keyExtractor) {
            return Comparator.<T, U>comparing(keyExtractor).reversed();
        }
    };

    /**
     * Accepts a function that extracts a {@link java.lang.Comparable
     * Comparable} sort key from a type {@code T}, and returns a {@code
     * Comparator<T>} that compares by that sort key in this sort direction.
     *
     * @param <T>          the type of element to be compared
     * @param <U>          the type of the {@code Comparable} sort key
     * @param keyExtractor the function used to extract the {@link
     *                     Comparable} sort key
     * @return a comparator that compares by an extracted key in this sort direction
     */
    public abstract <T, U extends Comparable<? super U>> Comparator<T> comparing(Function<? super T, ? extends U> keyExtractor);
}
