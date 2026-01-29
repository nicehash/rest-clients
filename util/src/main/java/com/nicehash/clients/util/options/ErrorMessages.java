package com.nicehash.clients.util.options;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ErrorMessages {
    private static final Logger log = LoggerFactory.getLogger(ErrorMessages.class);

    static final ErrorMessages INSTANCE = new ErrorMessages();

    NullPointerException nullParameter(String parameter) {
        return new NullPointerException(String.format("Parameter %s is null!", parameter));
    }

    public IllegalArgumentException invalidOptionName(String name) {
        return new IllegalArgumentException("No such Option!");
    }

    public IllegalArgumentException optionClassNotFound(String className, ClassLoader classLoader) {
        return new IllegalArgumentException(String.format("No such class: %s", className));
    }

    public IllegalArgumentException noField(String fieldName, Class<?> clazz) {
        return new IllegalArgumentException(String.format("No such field %s on class %s", fieldName, clazz.getSimpleName()));
    }

    public IllegalArgumentException fieldNotAccessible(String fieldName, Class<?> clazz) {
        return new IllegalArgumentException(String.format("Field %s not accessible, class %s", fieldName, clazz.getSimpleName()));
    }

    public IllegalArgumentException fieldNotStatic(String fieldName, Class<?> clazz) {
        return new IllegalArgumentException(String.format("Field %s not static, class %s", fieldName, clazz.getSimpleName()));
    }

    public IllegalArgumentException invalidNullOption(String name) {
        return new IllegalArgumentException("No such option: " + name);
    }

    public IllegalArgumentException noOptionParser() {
        return new IllegalArgumentException("No Option parser!");
    }

    public IllegalArgumentException classNotFound(String clazz, ClassNotFoundException e) {
        return new IllegalArgumentException("Class not found: " + clazz);
    }

    public <T> IllegalArgumentException classNotInstance(String clazz, Class<T> argType) {
        return new IllegalArgumentException(String.format("Class %s not instance of %s", clazz, argType.getSimpleName()));
    }

    public void invalidOptionInProperty(String optionName, String name, IllegalArgumentException e) {
        log.warn(String.format("Invalid option %s in property %s, %s", optionName, name, e));
    }

    public IllegalArgumentException nullArrayIndex(String option, int i) {
        return new IllegalArgumentException(String.format("Null %s at %s", option, i));
    }
}
