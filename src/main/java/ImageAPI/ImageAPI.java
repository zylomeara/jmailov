package main.java.ImageAPI;

import main.java.enums.BlurType;
import main.java.enums.MorphTransformsType;
import main.java.utils.CVUtils;
import main.java.utils.InitializeCVLib;
import org.apache.log4j.Logger;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.stream.IntStream;

public class ImageAPI {
    private static final Logger LOG = Logger.getLogger(ImageAPI.class);
    private final Mat srcImage;
    private final Mat dstImage;

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
        showImage("");
    }

    public void saveImage(String imageName) {
        CVUtils.saveImage(dstImage, imageName);
    }

    public void resetChanges() {
        int size = srcImage.cols() * srcImage.rows() * srcImage.channels();
        byte[] buffer = new byte[size];
        srcImage.get(0, 0, buffer);
        dstImage.put(0, 0, buffer);
        LOG.debug("Reset changes to source image");
    }

    // Morphology transforms
    public void morphTransform(MorphTransformsType morphType, int kernelSize) {
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(kernelSize, kernelSize));

        Imgproc.morphologyEx(dstImage.clone(), dstImage, morphType.get(), kernel);
        LOG.debug(String.format("Image was transformed by %s with kernel (%sx%s)", morphType, kernelSize, kernelSize));
    }

    // Filtration
    public void blur(BlurType type, int kernelSize) {
        assert kernelSize % 2 != 0 : "Need an odd number";

        switch (type) {
            case AVERAGE:
                Imgproc.blur(dstImage.clone(), dstImage, new Size(kernelSize, kernelSize));
                break;
            case GAUSSIAN:
                Imgproc.GaussianBlur(dstImage.clone(), dstImage, new Size(kernelSize, kernelSize), 0);
                break;
            case MEDIAN:
                Imgproc.medianBlur(dstImage.clone(), dstImage, kernelSize);
                break;
            case BILATERAL:
                Imgproc.bilateralFilter(dstImage.clone(), dstImage, -1, kernelSize, kernelSize);
                break;
        }
        LOG.info(String.format("Image blurred by %s with radius: %s", type, kernelSize));
    }
}
