package main.java;

import main.java.ImageAPI.ImageAPI;
import org.opencv.imgcodecs.Imgcodecs;

public class Main {
    public static void main(String[] args) throws Exception {
        ImageAPI instanceImageAPI  = new ImageAPI();
        instanceImageAPI.loadImage(
                "color.jpg",
                0,
                Imgcodecs.IMREAD_COLOR
        );
        instanceImageAPI.showImage();
    }
}
