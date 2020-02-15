package main.java;

import main.java.enums.OSType;

import java.util.Locale;

public class TypeOS {
    public static OSType getOperatingSystemType() {
        String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if ((OS.contains("mac")) || (OS.contains("darwin"))) {
            return OSType.MACOS;
        } else if (OS.contains("win")) {
            return OSType.WINDOWS;
        } else if (OS.contains("nux")) {
            return OSType.LINUX;
        } else {
            return OSType.OTHER;
        }
    }
}
