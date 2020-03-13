package main.java;

import main.java.ImageAPI.ImageAPI;
import org.opencv.imgcodecs.Imgcodecs;

public class Main {
    public static void main(String[] args) throws Exception {
        ImageAPI instanceImageAPI  = new ImageAPI();
        instanceImageAPI.loadImage(
                "color.jpg",
//                "example.jpg",
                0,
                Imgcodecs.CV_LOAD_IMAGE_COLOR
//                Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE
        );
        instanceImageAPI.showImage();
    }
}
