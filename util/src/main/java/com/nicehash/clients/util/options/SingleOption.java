package com.nicehash.clients.util.options;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 * @author Ales Justin
 */
final class SingleOption<T> extends Option<T> {

    private static final long serialVersionUID = 2449094406108952764L;

    private final transient Class<T> type;
    private final transient ValueParser<T> parser;

    SingleOption(final Class<?> declClass, final String name, final Class<T> type) {
        this(declClass, name, type, false);
    }

    SingleOption(final Class<?> declClass, final String name, final Class<T> type, boolean required) {
        super(declClass, name, required);
        if (type == null) {
            throw msg.nullParameter("type");
        }
        this.type = type;
        parser = Option.getParser(type);
    }

    public boolean isSequence() {
        return false;
    }

    public Class<?> getType() {
        return type;
    }

    public T cast(final Object o) {
        return type.cast(o);
    }

    public T parseValue(final String string, final ClassLoader classLoader) throws IllegalArgumentException {
        return parser.parseValue(string, classLoader);
    }
}
