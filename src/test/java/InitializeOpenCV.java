package test.java;

import main.java.ImageAPI;
import org.junit.Test;

public class InitializeOpenCV {
    private ImageAPI instanceImageAPI;

    @Test
    public void initialize() throws Exception {
        instanceImageAPI = new ImageAPI();

        System.out.println(instanceImageAPI.getVersion());
    }
}
