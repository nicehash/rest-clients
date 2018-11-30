package com.nicehash.clients.util.options;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 * @author Ales Justin
 */
final class TypeOption<T> extends Option<Class<? extends T>> {

    private static final long serialVersionUID = 2449094406108952764L;

    private final transient Class<T> type;
    private final transient ValueParser<Class<? extends T>> parser;

    TypeOption(final Class<?> declClass, final String name, final Class<T> type) {
        super(declClass, name);
        if (type == null) {
            throw msg.nullParameter("type");
        }
        this.type = type;
        parser = getClassParser(type);
    }

    public boolean isSequence() {
        return false;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    public Class<? extends T> cast(final Object o) {
        return ((Class<?>) o).asSubclass(type);
    }

    public Class<? extends T> parseValue(final String string, final ClassLoader classLoader) throws IllegalArgumentException {
        return parser.parseValue(string, classLoader);
    }
}
