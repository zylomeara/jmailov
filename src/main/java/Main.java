package main.java;

import main.java.ImageAPI.ChannelsEnum;
import main.java.ImageAPI.ImageAPI;
import org.opencv.core.Core;

public class Main {
//    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
    public static void main(String[] args) throws Exception {
//        System.out.println(Core.NATIVE_LIBRARY_NAME);
//        System.out.println(Core.VERSION);
        ImageAPI instanceImageAPI  = new ImageAPI();
        instanceImageAPI.loadImage(
                "color.jpg",
//                "example.jpg",
                ChannelsEnum.Color
        );
        instanceImageAPI.showImage();
    }
}
