package main.java;


import javafx.util.Pair;
import main.java.ImageAPI.ImageAPI;
import main.java.constants.Constants;
import main.java.enums.BlurType;
import main.java.enums.LabNumberType;
import main.java.enums.MorphTransformsType;
import main.java.utils.ConfigurationUtil;
import main.java.utils.InitializeCVLib;
import org.apache.log4j.Logger;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

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

                int imageType = imageTypeLab2.toLowerCase().equals("gray")
                    ? Imgcodecs.IMREAD_GRAYSCALE
                    : Imgcodecs.IMREAD_COLOR;

                ImageAPI img = new ImageAPI(imagePathLab2, imageType);
                int channelCount = imageTypeLab2.equals("gray") ? 1 : 3;

                for (int i = 0; i < channelCount; i++) {
                    int num = i + 1;
                    img.resetChannel(num);
                    img.saveImage(String.format("%sreset_at_%s.jpeg", dstDirPathOfLab2, num));
                    img.resetChanges();
                }
            } else if (chosenLab.equals(LabNumberType.THIRD.get())) {
                String dstDirPathOfLab3 = String.format("%s3/", destinationPath);
                File directory = new File(dstDirPathOfLab3);
                if (!directory.exists())
                    directory.mkdir();

                // Blur filter
                String filterImagePathLab3 = ConfigurationUtil.getConfigurationEntry(Constants.LAB_3_FILTER_IMAGE_PATH);
                String filterImageTypeLab3 = ConfigurationUtil.getConfigurationEntry(Constants.LAB_3_FILTER_IMAGE_TYPE);

                int filterImageType = filterImageTypeLab3.toLowerCase().equals("gray")
                    ? Imgcodecs.IMREAD_GRAYSCALE
                    : Imgcodecs.IMREAD_COLOR;

                ImageAPI filterImg = new ImageAPI(filterImagePathLab3, filterImageType);
                Arrays.stream(BlurType.values())
                    .flatMap(blurType ->
                        IntStream
                            .range(3, 10)
                            .filter(i -> i % 2 != 0)
                            .mapToObj(i -> new Pair<>(blurType, i))
                    )
                    .forEach(pair -> {
                        BlurType blurType = pair.getKey();
                        int kernelSize = pair.getValue();
                        String imageName = String.format("%sx%s_blur_%s.jpeg", kernelSize, kernelSize, blurType);
                        filterImg.blur(blurType, kernelSize);
                        filterImg.saveImage(String.format("%s%s", dstDirPathOfLab3, imageName));
                        filterImg.resetChanges();
                    });

                // Morphology
                String morphImagePathLab3 = ConfigurationUtil.getConfigurationEntry(Constants.LAB_3_MORPHOLOGY_IMAGE_PATH);
                String morphImageTypeLab3 = ConfigurationUtil.getConfigurationEntry(Constants.LAB_3_MORPHOLOGY_IMAGE_TYPE);

                int morphImageType = morphImageTypeLab3.toLowerCase().equals("gray")
                    ? Imgcodecs.IMREAD_GRAYSCALE
                    : Imgcodecs.IMREAD_COLOR;

                ImageAPI morphImg = new ImageAPI(morphImagePathLab3, morphImageType);
                Arrays.stream(MorphTransformsType.values())
                    .flatMap(morphType ->
                        IntStream
                            .range(3, 10)
                            .filter(i -> i % 2 != 0)
                            .mapToObj(i -> new Pair<>(morphType, i))
                    )
                    .forEach(pair -> {
                        MorphTransformsType morphType = pair.getKey();
                        int kernelSize = pair.getValue();
                        String imageName = String.format("%sx%s_morphology_%s.jpeg", kernelSize, kernelSize, morphType);
                        morphImg.morphTransform(morphType, kernelSize);
                        morphImg.saveImage(String.format("%s%s", dstDirPathOfLab3, imageName));
                        morphImg.resetChanges();
                    });

            } else if (chosenLab.equals(LabNumberType.FOURTH.get())) {

            } else {
                LOG.info(String.format("Parameter not found: %s", chosenLab));
            }
        }
    }
}
