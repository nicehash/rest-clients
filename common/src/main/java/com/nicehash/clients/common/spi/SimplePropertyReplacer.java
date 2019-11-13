package com.nicehash.clients.common.spi;


public class SimplePropertyReplacer implements PropertyReplacer {
    public static final PropertyReplacer INSTANCE = new SimplePropertyReplacer();

    private SimplePropertyReplacer() {
    }

    @Override
    public String replace(String property) {
        if (property.startsWith("${") && property.endsWith("}")) {
            property = property.substring(2, property.length() - 1);
            int p = property.indexOf(":");
            String defaultValue = null;
            if (p != -1) {
                defaultValue = replace(property.substring(p + 1));
                property = property.substring(0, p);
            }
            return System.getProperty(property, defaultValue);
        }
        return property;
    }
}
