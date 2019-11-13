package com.nicehash.clients.util.options;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

final class SequenceOption<T> extends Option<Sequence<T>> {

    private static final long serialVersionUID = -4328676629293125136L;

    private final transient Class<T> elementType;
    private final transient ValueParser<T> parser;

    SequenceOption(final Class<?> declClass, final String name, final Class<T> elementType) {
        super(declClass, name);
        if (elementType == null) {
            throw msg.nullParameter("elementType");
        }
        this.elementType = elementType;
        parser = Option.getParser(elementType);
    }

    @Override
    public boolean isSequence() {
        return true;
    }

    public Class<?> getType() {
        return elementType;
    }

    public Sequence<T> cast(final Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof Sequence) {
            return ((Sequence<?>) o).cast(elementType);
        } else if (o instanceof Object[]) {
            return Sequence.of((Object[]) o).cast(elementType);
        } else if (o instanceof Collection) {
            return Sequence.of((Collection<?>) o).cast(elementType);
        } else {
            throw new ClassCastException("Not a sequence");
        }
    }

    public Sequence<T> parseValue(final String string, final ClassLoader classLoader) throws IllegalArgumentException {
        final List<T> list = new ArrayList<T>();
        if (string.isEmpty()) {
            return Sequence.empty();
        }
        for (String value : string.split(",")) {
            list.add(parser.parseValue(value, classLoader));
        }
        return Sequence.of(list);
    }
}
