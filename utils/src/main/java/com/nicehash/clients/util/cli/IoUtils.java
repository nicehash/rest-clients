package com.nicehash.clients.util.cli;

import java.io.InputStream;
import java.io.OutputStream;

public class IoUtils {

    public static void copyBytes(InputStream is, OutputStream os, boolean flush) {
        int c;
        try {
            while ((c = is.read()) != -1) {
                os.write(c);
                if (flush) {
                    os.flush();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("I/O error", e);
        }
    }
}
