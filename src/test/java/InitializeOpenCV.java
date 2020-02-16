package test.java;

import main.java.ImageAPI;
import org.junit.Test;

public class InitializeOpenCV {
    private ImageAPI instanceImageAPI;

    @Test
    public void initialize() throws Exception {
        instanceImageAPI = new ImageAPI();

//        System.out.println(instanceImageAPI.getVersion());
        instanceImageAPI.HelloCV();
    }

    @Test
    public void showWhiteBlackImage() throws Exception {
        instanceImageAPI = new ImageAPI();
        instanceImageAPI.processImage(
                "/home/artem1y3/IdeaProjects/jmailov/src/main/resources/images/",
                "color.jpg",
//                "example.jpg",
                3
        );
    }
}
