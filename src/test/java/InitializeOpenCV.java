package test.java;

import main.java.ImageAPI;
import org.junit.Test;
import org.opencv.core.Mat;

public class InitializeOpenCV {
    private ImageAPI instanceImageAPI;



    @Test
    public void showWhiteBlackImage() throws Exception {
        instanceImageAPI = new ImageAPI();
        Mat srcImage = instanceImageAPI.processImage(
                "/home/artem1y3/IdeaProjects/jmailov/src/main/resources/images/",
                "color.jpg",
//                "example.jpg",
                3
        );
        instanceImageAPI.showImage(srcImage);
        System.out.println(1);
    }

    @Test
    public void initialize() throws Exception {
        instanceImageAPI = new ImageAPI();

//        System.out.println(instanceImageAPI.getVersion());
        instanceImageAPI.HelloCV();
    }
}
