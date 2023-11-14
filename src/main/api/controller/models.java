package main.api.controller;

import main.api.settings;

public class models {
    public static String size_converter(long byteSize) {
        if (byteSize == 0) {
            return "0 Bytes";
        }
        int index = (int) Math.floor(Math.log(byteSize) / Math.log(1024));
        double byte_zie = byteSize / Math.pow(1024, index);
        return String.format("%s %s", Math.round(byte_zie * 100.0) / 100.0, settings.size_name.get(index));
    }

    public static boolean authRequest(String authorization) {
        if (authorization == null || authorization.isBlank()) {
            return false;
        }
        boolean equal = authorization.length() == settings.token.length();
        if (!equal) {
            return false;
        }
        for (int i = 0; i < authorization.length(); i++) {
            if (authorization.charAt(i % authorization.length()) !=
                    settings.token.charAt(i % settings.token.length())) {
                equal = false;
                break;
            }
        }
        return equal;
    }
}
