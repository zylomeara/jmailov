package main.java;

import main.java.utils.ConfigurationUtil;
import main.java.Constants;
import static main.java.enums.OSType.*;
import main.java.TypeOS;
import org.apache.log4j.Logger;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

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
                System.load(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_NATIVE_LIB_MAC_OS));
                LOG.debug("Loaded for Windows with path " + ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_NATIVE_LIB_MAC_OS));
                break;
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
}
