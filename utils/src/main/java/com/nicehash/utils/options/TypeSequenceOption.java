package com.nicehash.utils.options;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 * @author Ales Justin
 */
final class TypeSequenceOption<T> extends Option<Sequence<Class<? extends T>>> {

    private static final long serialVersionUID = -4328676629293125136L;

    private final transient Class<T> elementDeclType;
    private final transient ValueParser<Class<? extends T>> parser;

    TypeSequenceOption(final Class<?> declClass, final String name, final Class<T> elementDeclType) {
        super(declClass, name);
        if (elementDeclType == null) {
            throw msg.nullParameter("elementDeclType");
        }
        this.elementDeclType = elementDeclType;
        parser = Option.getClassParser(elementDeclType);
    }

    public boolean isSequence() {
        return true;
    }

    public Class<T> getType() {
        return elementDeclType;
    }

    public Sequence<Class<? extends T>> cast(final Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof Sequence) {
            return castSeq((Sequence<?>) o, elementDeclType);
        } else if (o instanceof Object[]) {
            return castSeq(Sequence.of((Object[]) o), elementDeclType);
        } else if (o instanceof Collection) {
            return castSeq(Sequence.of((Collection<?>) o), elementDeclType);
        } else {
            throw new ClassCastException("Not a sequence");
        }
    }

    @SuppressWarnings("unchecked")
    static <T> Sequence<Class<? extends T>> castSeq(Sequence<?> seq, Class<T> type) {
        for (Object o : seq) {
            ((Class<?>) o).asSubclass(type);
        }
        return (Sequence<Class<? extends T>>) seq;
    }

    public Sequence<Class<? extends T>> parseValue(final String string, final ClassLoader classLoader) throws IllegalArgumentException {
        final List<Class<? extends T>> list = new ArrayList<Class<? extends T>>();
        if (string.isEmpty()) {
            return Sequence.empty();
        }
        for (String value : string.split(",")) {
            list.add(parser.parseValue(value, classLoader));
        }
        return Sequence.of(list);
    }
}
