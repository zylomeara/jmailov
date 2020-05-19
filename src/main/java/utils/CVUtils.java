package main.java.utils;

import main.java.enums.ChannelsEnum;
import org.apache.log4j.Logger;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class CVUtils {
    private static final Logger LOG = Logger.getLogger(CVUtils.class);

    public static void showImage(Mat m, String title) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() == ChannelsEnum.Color.get()) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b);
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        ImageIcon icon = new ImageIcon(image);
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(image.getWidth(null) + 50, image.getHeight(null) + 50);
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        LOG.info("Image shown " + (title.length() != 0 ? "with title: $title" : ""));
    }

    public static void showImage(Mat m) {
        showImage(m, "");
    }

    public static void saveImage(Mat srcImage, String imagePath) {
        Imgcodecs.imwrite(imagePath, srcImage);
        LOG.info("Image saved: " + imagePath);
    }
}
