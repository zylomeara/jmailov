package main.java.enums;

import org.opencv.imgproc.Imgproc;

public enum MorphTransformsType {
    MORPH_DILATE(Imgproc.MORPH_DILATE),
    MORPH_OPEN(Imgproc.MORPH_OPEN),
    MORPH_ERODE(Imgproc.MORPH_ERODE),
    MORPH_GRADIENT(Imgproc.MORPH_GRADIENT),
    MORPH_BLACKHAT(Imgproc.MORPH_BLACKHAT),
    MORPH_CLOSE(Imgproc.MORPH_CLOSE);

    private final int typeNum;

    MorphTransformsType(int typeNum) {
        this.typeNum = typeNum;
    }

    public int get() {
        return typeNum;
    }
}
