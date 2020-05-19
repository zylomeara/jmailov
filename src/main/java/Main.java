package main.java;


import main.java.ImageAPI.ImageAPI;
import main.java.constants.Constants;
import main.java.enums.LabNumberType;
import main.java.utils.ConfigurationUtil;
import main.java.utils.InitializeCVLib;
import org.apache.log4j.Logger;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;

public class Main {
    private static final Logger LOG = Logger.getLogger(ImageAPI.class);


    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            String chosenLab = args[0];
            String destinationPath = ConfigurationUtil.getConfigurationEntry(Constants.MEDIA_PATH_TO_DESTINATION_IMAGES);
            if (chosenLab.equals(LabNumberType.FIRST.get())) {
                InitializeCVLib.init();
                LOG.info("The library was successfully initialized.");
            } else if (chosenLab.equals(LabNumberType.SECOND.get())) {
                String dstDirPathOfLab2 = String.format("%s2/", destinationPath);
                File directory = new File(dstDirPathOfLab2);
                if (!directory.exists())
                    directory.mkdir();

                String imagePathLab2 = ConfigurationUtil.getConfigurationEntry(Constants.LAB_2_RESET_CHANNEL_IMAGE_PATH);
                String imageTypeLab2 = ConfigurationUtil.getConfigurationEntry(Constants.LAB_2_RESET_CHANNEL_IMAGE_TYPE);

                int imageType = imageTypeLab2.equals("gray")
                        ? Imgcodecs.IMREAD_GRAYSCALE
                        : Imgcodecs.IMREAD_COLOR;

                ImageAPI img = new ImageAPI(imagePathLab2, imageType);
                int channelCount = imageTypeLab2.equals("gray") ? 1 : 3;

                for (int i = 0; i < channelCount; i++) {
                    int num = i + 1;
                    img.resetChannel(num);
                    img.saveImage(String.format("%s/reset_at_%s.jpeg", dstDirPathOfLab2, num));
                    img.resetChanges();
                }
            } else if (chosenLab.equals(LabNumberType.THIRD.get())) {

            } else if (chosenLab.equals(LabNumberType.FOURTH.get())) {

            } else {
                LOG.info(String.format("Parameter not found: %s", chosenLab));
            }
        }
    }
}
