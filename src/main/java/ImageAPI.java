package main.java;

import main.java.utils.ConfigurationUtil;
import main.java.Constants;
import static main.java.enums.OSType.*;
import main.java.TypeOS;
import org.apache.log4j.Logger;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ImageAPI {
    private static final Logger LOG = Logger.getLogger(ImageAPI.class);
    public ImageAPI() throws Exception {
        LOG.info("Checking OS.....");
        // init the API with curent os..
        switch (TypeOS.getOperatingSystemType()) {
            case LINUX:
                System.load(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_NATIVE_LIB_LINUX));
                LOG.debug("Loaded for Linux with path " + ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_NATIVE_LIB_LINUX));
                break;
            case WINDOWS:
                System.load(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_NATIVE_LIB_WIN));
                LOG.debug("Loaded for Windows with path " + ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_NATIVE_LIB_WIN));
                break;
            case MACOS:
                throw new Exception("Mac OS does not support!!!!!!!!");
            case OTHER:
                throw new Exception("Current OS does not support!!!!!");
            default:
                throw new Exception("Your OS does not support!!!");
        }
        LOG.info("Properties are loaded");
    }

    public String getVersion() {
        return Core.VERSION;
    }

    public void HelloCV() {
        Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
        System.out.println("mat = " + mat.dump());
    }

    public void showImage(Mat m){
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if ( m.channels() > 1 ) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels()*m.cols()*m.rows();
        byte [] b = new byte[bufferSize];
        m.get(0,0, b);
        BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        ImageIcon icon=new ImageIcon(image);
        JFrame frame=new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(image.getWidth(null)+50, image.getHeight(null)+50);
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.pack();
//        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Mat processImage(String dirPath, String imageName, int numChn) {
        Mat srcImage = Imgcodecs.imread(dirPath + imageName);
        int totalBytes = (int) (srcImage.total() * srcImage.elemSize());
        byte buffer[] = new byte[totalBytes];
        srcImage.get(0, 0, buffer);
        for (int i = 0; i < totalBytes; i++) {
            if (i % numChn == 0) {
                buffer[i] = 0;
            }
        }
        srcImage.put(0, 0, buffer);
//        showImage(srcImage);
        return srcImage;
    }
}
