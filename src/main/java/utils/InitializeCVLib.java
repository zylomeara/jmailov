package main.java.utils;

import main.java.ImageAPI.ImageAPI;
import main.java.constants.Constants;
import org.apache.log4j.Logger;

import java.io.IOException;

public class InitializeCVLib {
    private static final Logger LOG = Logger.getLogger(InitializeCVLib.class);
    public static boolean isLoaded = false;

    public static void init() throws Exception {
        if (!isLoaded) {
            LOG.info("Checking OS.....");
            // init the API with curent os..
            switch (TypeOS.getOperatingSystemType()) {
                case LINUX:
                    System.load(ConfigurationUtil.getConfigurationEntry(Constants.CV_NATIVE_LIB_PATH_LINUX));
                    isLoaded = true;
                    LOG.debug("Loaded for Linux with path " + ConfigurationUtil.getConfigurationEntry(Constants.CV_NATIVE_LIB_PATH_LINUX));
                    break;
                case WINDOWS:
                    System.load(ConfigurationUtil.getConfigurationEntry(Constants.CV_NATIVE_LIB_PATH_WINDOWS));
                    isLoaded = true;
                    LOG.debug("Loaded for Windows with path " + ConfigurationUtil.getConfigurationEntry(Constants.CV_NATIVE_LIB_PATH_WINDOWS));
                    break;
                case MACOS:
                    System.load(ConfigurationUtil.getConfigurationEntry(Constants.CV_NATIVE_LIB_PATH_MAC_OS));
                    isLoaded = true;
                    LOG.debug("Loaded for Mac OS with path " + ConfigurationUtil.getConfigurationEntry(Constants.CV_NATIVE_LIB_PATH_MAC_OS));
                    break;
                case OTHER:
                    throw new Exception("Current OS does not support!!!!!");
                default:
                    throw new Exception("Your OS does not support!!!");
            }
            LOG.info("Properties are loaded");
        }
    }
}
