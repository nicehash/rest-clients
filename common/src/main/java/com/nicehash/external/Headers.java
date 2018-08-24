package com.nicehash.external;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An util class to pass any custom headers to services.
 *
 * @author Ales Justin
 */
public class Headers {
   private static final ThreadLocal<Map<String, List<String>>> TL = ThreadLocal.withInitial(HashMap::new);

   public static void setHeader(String name, String value) {
       TL.get().put(name, new ArrayList<>(Collections.singleton(value)));
   }

   public static void addHeader(String name, String value) {
       TL.get().compute(name, (key, values) -> {
           if (value == null) {
               values = new ArrayList<>();
           }
           values.add(value);
           return values;
       });
   }

   public static Map<String, List<String>> getHeaders() {
       return TL.get();
   }

   public static void clear() {
       TL.remove();
   }
}
