package main.java.ImageAPI;

import main.java.enums.BlurType;
import main.java.enums.DirectionPyramidType;
import main.java.enums.MorphTransformsType;
import main.java.utils.CVUtils;
import main.java.utils.InitializeCVLib;
import org.apache.log4j.Logger;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    public void saveImage(String imagePath) {
        CVUtils.saveImage(dstImage, imagePath);
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

    // Segmentation
    public void pyramidTransform(DirectionPyramidType pyramidType, int count) {
        assert count > 0 : "Need a positive number";

        switch (pyramidType) {
            case UP: {
                IntStream.range(1, count).forEach(i -> Imgproc.pyrUp(dstImage, dstImage, new Size(dstImage.cols() * 2, dstImage.rows() * 2)));
                IntStream.range(1, count).forEach(i -> Imgproc.pyrDown(dstImage, dstImage, new Size(dstImage.cols() / 2, dstImage.rows() / 2)));
                break;
            }
            case DOWN: {
                IntStream.range(1, count).forEach(i -> Imgproc.pyrDown(dstImage, dstImage, new Size(dstImage.cols() / 2, dstImage.rows() / 2)));
                IntStream.range(1, count).forEach(i -> Imgproc.pyrUp(dstImage, dstImage, new Size(dstImage.cols() * 2, dstImage.rows() * 2)));
                break;
            }
        }

        Imgproc.resize(dstImage.clone(), dstImage, new Size(srcImage.cols(), srcImage.rows()));

        LOG.info(String.format("Image was transformed by %s with iterations %s", pyramidType, count));
    }

    public void subtract() {
        Core.subtract(srcImage, dstImage.clone(), dstImage);
        LOG.info("Received image subtract");
    }

    public void recognizeRectangles(
        String distPathForSaveResults,
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> widthHeightRange
    ) {
        // 1
        Mat grayImage = new Mat();
        Imgproc.cvtColor(srcImage, grayImage, Imgproc.COLOR_BGR2GRAY);
        LOG.debug("Image converted to gray");

        // 2
        Mat denoisingImage = new Mat();
        Photo.fastNlMeansDenoising(grayImage, denoisingImage);
        LOG.debug("Noise reduction image with gauss noise");

        // 3
        Mat histogramEqualizationImage = new Mat();
        Imgproc.equalizeHist(denoisingImage, histogramEqualizationImage);
        LOG.debug("Image processed by contrast using image histogram");

        // 4
        Mat morphologicalOpeningImage = new Mat();
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
        Imgproc.morphologyEx(histogramEqualizationImage, morphologicalOpeningImage, Imgproc.MORPH_RECT, kernel);
        LOG.debug("Image converted using morphological opening");

        // 5
        Mat subtractImage = new Mat();
        Core.subtract(histogramEqualizationImage, morphologicalOpeningImage, subtractImage);
        LOG.debug("Received subtract fragment");

        // 6
        // Классифицирует пиксели с помощью порогового значения. Если превышает - белый, иначе - черный
        Mat thresholdImage = new Mat();
        Imgproc.threshold(subtractImage, thresholdImage, 50, 255, Imgproc.THRESH_OTSU);
        thresholdImage.convertTo(thresholdImage, CvType.CV_8UC1);
        LOG.debug("Pixels are classified into black and white.");


        // 7
        Mat dilatedImage = new Mat();
        Imgproc.dilate(thresholdImage, dilatedImage, kernel);
        LOG.debug("Image was processed by dilation");

        // 8
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(dilatedImage, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        LOG.debug("Obtained external contours from the image");
        contours.sort(Collections.reverseOrder(Comparator.comparing(Imgproc::contourArea)));

        for (MatOfPoint contour : contours) {
            MatOfPoint2f point2f = new MatOfPoint2f();
            MatOfPoint2f approxContour2f = new MatOfPoint2f();
            MatOfPoint approxContour = new MatOfPoint();
            contour.convertTo(point2f, CvType.CV_32FC2);
            double arcLength = Imgproc.arcLength(point2f, true);
            Imgproc.approxPolyDP(point2f, approxContour2f, 0.03 * arcLength, true);
            approxContour2f.convertTo(approxContour, CvType.CV_32S);
            if (approxContour.toArray().length != 4) {
                continue;
            }
            Rect rect = Imgproc.boundingRect(approxContour);
            Mat submat = srcImage.submat(rect);
            Imgcodecs.imwrite(distPathForSaveResults + "recognize_result" + contour.hashCode() + ".jpg", submat);
        }
    }

    public void fillFlood(
        Pair<Integer, Integer> pointCoords,
        Triplet<Integer, Integer, Integer> fillColor,
        Pair<
            Triplet<Integer, Integer, Integer>,
            Triplet<Integer, Integer, Integer>
            > rangeColor
    ) {
        Point seedPoint = new Point(pointCoords.getValue0(), pointCoords.getValue1());
        Scalar newVal = new Scalar(fillColor.getValue0(), fillColor.getValue1(), fillColor.getValue2());
        Scalar loDiff = new Scalar(
            rangeColor.getValue0().getValue0(),
            rangeColor.getValue0().getValue1(),
            rangeColor.getValue0().getValue2()
        );
        Scalar upDiff = new Scalar(
            rangeColor.getValue1().getValue0(),
            rangeColor.getValue1().getValue1(),
            rangeColor.getValue1().getValue2()
        );
        Mat mask = new Mat();
        Rect rect = new Rect();

        Imgproc.floodFill(dstImage, mask, seedPoint, newVal, rect, loDiff, upDiff, Imgproc.FLOODFILL_FIXED_RANGE);
        LOG.debug(String.format(
            "Image is filled in color (%s, %s, %s) with point coordinates (%s, %s)",
            fillColor.getValue0(), fillColor.getValue1(), fillColor.getValue2(),
            pointCoords.getValue0(), pointCoords.getValue1()
            ));
    }
    public void fillFlood(
        Pair<Integer, Integer> pointCoords,
        Pair<
            Triplet<Integer, Integer, Integer>,
            Triplet<Integer, Integer, Integer>
            > rangeColor
    ) {
        int blue = (int) (Math.random() * 255);
        int green = (int) (Math.random() * 255);
        int red = (int) (Math.random() * 255);
        fillFlood(
            pointCoords,
            new Triplet<>(blue, green, red),
            rangeColor
        );
    }
}
