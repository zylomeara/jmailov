package test.java;

import main.java.ImageAPI.ChannelsEnum;
import main.java.ImageAPI.ImageAPI;
import org.junit.Test;

public class InitializeOpenCV {
    private ImageAPI instanceImageAPI;



    @Test
    public void showWhiteBlackImage() throws Exception {
        instanceImageAPI = new ImageAPI();
        instanceImageAPI.loadImage(
                "color.jpg",
//                "example.jpg",
                ChannelsEnum.Color
        );
        instanceImageAPI.showImage();
//        instanceImageAPI.saveImage("color.jpg");
    }

    @Test
    public void initialize() throws Exception {
        instanceImageAPI = new ImageAPI();

//        System.out.println(instanceImageAPI.getVersion());
        instanceImageAPI.HelloCV();
    }
}
