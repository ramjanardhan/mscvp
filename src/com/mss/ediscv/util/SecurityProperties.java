/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author miracle
 */
public class SecurityProperties {
    private static Logger logger = LogManager.getLogger(SecurityProperties.class.getName());

    private static final String BUNDLE_NAME = "com/mss/ediscv/config/security";
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    public static String getProperty(String property) {
        try {
            return RESOURCE_BUNDLE.getString(property);
        } catch (MissingResourceException missingResourceException) {
            
             LoggerUtility.log(logger, "MissingResourceException occurred in getProperty method:: " +missingResourceException.getMessage(), Level.ERROR, missingResourceException.getCause());
            return '!' + property + '!';
        }
    }
}