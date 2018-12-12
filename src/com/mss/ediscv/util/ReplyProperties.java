package com.mss.ediscv.util;

import java.util.ResourceBundle;
import java.util.MissingResourceException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ReplyProperties {

    private static Logger logger = LogManager.getLogger(ReplyProperties.class.getName());
    private static final String BUNDLE_NAME = "com/mss/ediscv/config/mscvp";
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    public static String getProperty(String property) {
        try {
            return RESOURCE_BUNDLE.getString(property);
        } catch (MissingResourceException missingResourceException) {
            LoggerUtility.log(logger, "MissingResourceException occurred in Get Property Method:: " + missingResourceException.getMessage(), Level.ERROR, missingResourceException.getCause());
           return '!' + property + '!';
        }
    }
}

