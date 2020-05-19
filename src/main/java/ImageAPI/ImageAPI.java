package main.java.ImageAPI;

import main.java.constants.Constants;
import main.java.utils.CVUtils;
import main.java.utils.InitializeCVLib;
import main.java.utils.TypeOS;
import main.java.enums.ChannelsEnum;
import main.java.utils.ConfigurationUtil;
import org.apache.log4j.Logger;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.stream.IntStream;

public class ImageAPI {
    private static final Logger LOG = Logger.getLogger(ImageAPI.class);
    private Mat srcImage;
    private Mat dstImage;

    public ImageAPI(String imagePath, int colorType) throws Exception {
        InitializeCVLib.init();
        this.srcImage = Imgcodecs.imread(imagePath, colorType);
        this.dstImage = srcImage.clone();
        LOG.info("Image loaded: " + imagePath);
    }

    public void resetChannel(int channelNum) {
        int totalBytes = (int) (dstImage.total() * dstImage.elemSize());
        byte[] buffer = new byte[totalBytes];
        dstImage.get(0, 0, buffer);

        IntStream.range(0, buffer.length)
                .forEach(i -> {
                    if (i % 3 == channelNum - 1 && channelNum != 0) {
                        buffer[i] = 0;
                    }
                });

        dstImage.put(0, 0, buffer);
    }

    public void showImage(String title) {
        CVUtils.showImage(dstImage, title);
    }
    public void showImage() {
        CVUtils.showImage(dstImage, "");
    }

    public void saveImage(String imageName) {
        CVUtils.saveImage(dstImage, imageName);
    }
}
