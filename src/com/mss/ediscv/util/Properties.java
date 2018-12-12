
package com.mss.ediscv.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Properties {

    static java.util.Properties props = new java.util.Properties();
    static FileInputStream fis = null;
    static FileOutputStream fos = null;
    private static Logger logger = LogManager.getLogger(Properties.class.getName());

    public static String getProperty(String property) {
        try {
            fis = null;
            props.clear();
            fis = new FileInputStream(ReplyProperties.getProperty("PROP_FILE_LOCATION"));
            props.load(fis);
            return props.getProperty(property);
        } catch (Exception exception) {
           LoggerUtility.log(logger, "Exception occurred in get Property method :: " + exception.getMessage(), Level.ERROR, exception.getCause());
            return '!' + property + '!';
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ioException) {
                    LoggerUtility.log(logger, "IOException occurred in get Property method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
                }
            }
        }
    }
    
     public static void setProperty(String property, String value) {
        try {
            fis = null;
            fos = null;
            props.clear();
            fis = new FileInputStream(ReplyProperties.getProperty("PROP_FILE_LOCATION"));
            props.load(fis);
            fis.close();

            fos = new FileOutputStream(ReplyProperties.getProperty("PROP_FILE_LOCATION"));
            props.setProperty(property, value);
            props.store(fos, null);
            fos.close();

        } catch (IOException io) {
            LoggerUtility.log(logger, " :: IO Exception occurred in set Property:: " + io.getMessage(), Level.ERROR, io.getCause());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    LoggerUtility.log(logger, " :: IOException occurred in FileInputStream set Property:: " + ex.getMessage(), Level.ERROR, ex.getCause());
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {
                    LoggerUtility.log(logger, " :: IOException occurred in FileOutputStream set Property:: " + ex.getMessage(), Level.ERROR, ex.getCause());
                }
            }
        }
    }
}

