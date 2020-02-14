package main.java;

import org.opencv.core.Core;

public class Cli {
    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
    public static void main(String[] args) {
        System.out.println(Core.VERSION);
    }
}
