package com.mss.ediscv.util;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CacheManager {
    private static Logger logger = LogManager.getLogger(CacheManager.class.getName());

    private static Map<String, Object> cache;

    private CacheManager() {
    }

    public static Map<String, Object> getCache() throws ServiceLocatorException {
        try {
            if (cache == null) {
                cache = Collections.synchronizedMap(new HashMap<String, Object>());
            }
        } catch (Exception exception) {
            
            LoggerUtility.log(logger, "Exception occurred in DataServiceLocator method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            throw new ServiceLocatorException(exception);
        }
        return cache;
    }
}