package main.java;


import main.java.ImageAPI.ImageAPI;
import main.java.constants.Constants;
import main.java.enums.BlurType;
import main.java.enums.DirectionPyramidType;
import main.java.enums.LabNumberType;
import main.java.enums.MorphTransformsType;
import main.java.utils.ConfigurationUtil;
import main.java.utils.InitializeCVLib;
import org.apache.log4j.Logger;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
                        BlurType blurType = pair.getValue0();
                        int kernelSize = pair.getValue1();
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
                        MorphTransformsType morphType = pair.getValue0();
                        int kernelSize = pair.getValue1();
                        String imageName = String.format("%sx%s_morphology_%s.jpeg", kernelSize, kernelSize, morphType);
                        morphImg.morphTransform(morphType, kernelSize);
                        morphImg.saveImage(String.format("%s%s", dstDirPathOfLab3, imageName));
                        morphImg.resetChanges();
                    });
            } else if (chosenLab.equals(LabNumberType.FOURTH.get())) {
                String dstDirPathOfLab4 = String.format("%s4/", destinationPath);
                File directory = new File(dstDirPathOfLab4);
                if (!directory.exists())
                    directory.mkdir();

                // Recognize rects
                String recognizeImagePathLab4 = ConfigurationUtil.getConfigurationEntry(Constants.LAB_4_RECOGNIZE_RECT_IMAGE_PATH);
                String recognizeImageTypeLab4 = ConfigurationUtil.getConfigurationEntry(Constants.LAB_4_RECOGNIZE_RECT_IMAGE_TYPE);
                java.util.List<Integer> rangesWidthHeight = Arrays.stream(ConfigurationUtil
                    .getConfigurationEntry(Constants.LAB_4_RECOGNIZE_RECT_RANGE_WIDTH_HEIGHT_RECT)
                    .replaceAll("\\s", "")
                    .split(";"))
                    .flatMap(str -> Arrays.stream(str.split(",")))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

                int recognizeImageType = recognizeImageTypeLab4.toLowerCase().equals("gray")
                    ? Imgcodecs.IMREAD_GRAYSCALE
                    : Imgcodecs.IMREAD_COLOR;

                ImageAPI recognizeImg = new ImageAPI(recognizeImagePathLab4, recognizeImageType);

                recognizeImg.recognizeRectangles(
                    dstDirPathOfLab4,
                    new Pair<>(
                        new Pair<>(rangesWidthHeight.get(0), rangesWidthHeight.get(1)),
                        new Pair<>(rangesWidthHeight.get(2), rangesWidthHeight.get(3))
                    )
                );

                // Pyramid transforms
                String pyramidImagePathLab4 = ConfigurationUtil.getConfigurationEntry(Constants.LAB_4_PYRAMID_IMAGE_PATH);
                String pyramidImageTypeLab4 = ConfigurationUtil.getConfigurationEntry(Constants.LAB_4_PYRAMID_IMAGE_TYPE);

                int pyramidImageType = pyramidImageTypeLab4.toLowerCase().equals("gray")
                    ? Imgcodecs.IMREAD_GRAYSCALE
                    : Imgcodecs.IMREAD_COLOR;

                ImageAPI pyramidImg = new ImageAPI(pyramidImagePathLab4, pyramidImageType);

                String directionPyramidTypeConf = ConfigurationUtil.getConfigurationEntry(Constants.LAB_4_PYRAMID_DIRECTION);
                DirectionPyramidType directionPyramidType;

                switch (directionPyramidTypeConf.toLowerCase()) {
                    case "up": {
                        directionPyramidType = DirectionPyramidType.UP;
                        break;
                    }
                    case "down": {
                        directionPyramidType = DirectionPyramidType.DOWN;
                        break;
                    }
                    default:
                        throw new IllegalStateException("Unexpected value: " + directionPyramidTypeConf.toLowerCase());
                }

                int countIterates = Integer.parseInt(ConfigurationUtil.getConfigurationEntry(Constants.LAB_4_PYRAMID_COUNT_ITERATES));

                pyramidImg.pyramidTransform(directionPyramidType, countIterates);
                pyramidImg.saveImage(String.format("%spyr_%s_with_%s_count.jpeg", dstDirPathOfLab4, directionPyramidType, countIterates));
                pyramidImg.subtract();
                pyramidImg.saveImage(String.format("%spyr_subtract.jpeg", dstDirPathOfLab4));

                // FillFlood
                String fillFloodImagePathLab4 = ConfigurationUtil.getConfigurationEntry(Constants.LAB_4_FILL_FLOOD_IMAGE_PATH);
                String fillFloodImageTypeLab4 = ConfigurationUtil.getConfigurationEntry(Constants.LAB_4_FILL_FLOOD_IMAGE_TYPE);
                List<Integer> fillFloodPointCoords = Arrays.stream(ConfigurationUtil
                    .getConfigurationEntry(Constants.LAB_4_FILL_FLOOD_POINT_COORDINATES)
                    .replaceAll("\\s", "")
                    .split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

                List<Integer> fillFloodColor = Arrays.stream(ConfigurationUtil
                    .getConfigurationEntry(Constants.LAB_4_FILL_FLOOD_FILL_COLOR)
                    .replaceAll("\\s", "")
                    .split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

                List<Integer> rangeColor = Arrays.stream(ConfigurationUtil
                    .getConfigurationEntry(Constants.LAB_4_FILL_FLOOD_RANGE_COLOR)
                    .replaceAll("\\s", "")
                    .split(";"))
                    .flatMap(str -> Arrays.stream(str.split(",")))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

                int fillFloodImageType = fillFloodImageTypeLab4.toLowerCase().equals("gray")
                    ? Imgcodecs.IMREAD_GRAYSCALE
                    : Imgcodecs.IMREAD_COLOR;

                ImageAPI fillFloodImg = new ImageAPI(fillFloodImagePathLab4, fillFloodImageType);

                fillFloodImg.fillFlood(
                    new Pair<>(fillFloodPointCoords.get(0), fillFloodPointCoords.get(1)),
                    new Triplet<>(fillFloodColor.get(0), fillFloodColor.get(1), fillFloodColor.get(2)),
                    new Pair<>(
                        new Triplet<>(rangeColor.get(0),rangeColor.get(1),rangeColor.get(2)),
                        new Triplet<>(rangeColor.get(3),rangeColor.get(4),rangeColor.get(5))
                    )
                );

                fillFloodImg.saveImage(String.format("%sfill_flood.jpeg", dstDirPathOfLab4));
            } else {
                LOG.info(String.format("Parameter not found: %s", chosenLab));
            }
        }
    }
}
