package com.mss.ediscv.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class LoggerUtility {

    public static void log(Logger logger, String message, Level priority, Throwable throwable) {
        if (logger.isEnabled(priority)) {
            if (throwable != null) {
                logger.log(priority, message, throwable);
            } else {
                logger.log(priority, message);
            }
        }
    }

    public static void logPayload(Logger logger, String message, Level priority, Throwable throwable) {
        if (logger.isEnabled(priority)) {
            StringBuilder msgBuffer = new StringBuilder("");
            msgBuffer.append("\n##############################################################\n");
            msgBuffer.append(message);
            msgBuffer.append("\n##############################################################\n");
            if (throwable != null) {
                logger.log(priority, msgBuffer.toString(), throwable);
            } else {
                logger.log(priority, msgBuffer.toString());
            }
        }
    }

}
