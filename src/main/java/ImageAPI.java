package main.java;

import main.java.utils.ConfigurationUtil;
import main.java.Constants;
import static main.java.enums.OSType.*;
import main.java.TypeOS;
import org.apache.log4j.Logger;
import org.opencv.core.Core;

public class ImageAPI {
    private static final Logger LOG = Logger.getLogger(ImageAPI.class);
    public ImageAPI() throws Exception {
        LOG.info("Checking OS.....");
        // init the API with curent os..
        switch (TypeOS.getOperatingSystemType()) {
            case LINUX:
                System.load(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_NATIVE_LIB_LINUX));
                LOG.info("Loaded with path " + ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_NATIVE_LIB_LINUX));
                break;
            case WINDOWS:
                System.load(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_NATIVE_LIB_WIN));
                LOG.info("Loaded with path " + ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_NATIVE_LIB_WIN));
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
}
