package main.java;

import org.opencv.core.Core;

public class Main {
    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
    public static void main(String[] args) {
//        System.out.println(Core.NATIVE_LIBRARY_NAME);
        System.out.println(Core.VERSION);
    }
}
