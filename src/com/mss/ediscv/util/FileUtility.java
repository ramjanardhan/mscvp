/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author miracle
 */
public class FileUtility {
private static Logger logger = LogManager.getLogger(FileUtility.class.getName());
    private static FileUtility _instance;

    public static FileUtility getInstance() {
        if (_instance == null) {
            _instance = new FileUtility();
        }
        return _instance;
    }

    public String copyFiles(ArrayList<File> selected, File destinationDirectory) {
        String fileCopyStatus = "Error";
        for (File file : selected) {
            try {
                if (!destinationDirectory.exists()) {
                    destinationDirectory.mkdirs();
                }
                FileUtils.copyFileToDirectory(file, destinationDirectory);
                fileCopyStatus = "Success";
            } catch (IOException ioException) {
               LoggerUtility.log(logger, "IOException occurred in copyFiles method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
            }
        }
        return fileCopyStatus;
    }

    public String copyMapFiles(Map files) {
        String fileCopyStatus = "Error";
        Iterator it = files.entrySet().iterator();
        while (it.hasNext()) {
            try {
                Map.Entry pairs = (Map.Entry) it.next();
                File destinationDirectory = (File) pairs.getValue();
                File srcFile = (File) pairs.getKey();
                if (!destinationDirectory.exists()) {
                    destinationDirectory.mkdirs();
                }
                FileUtils.copyFileToDirectory(srcFile, destinationDirectory);
                fileCopyStatus = "Success";
            } catch (IOException ioException) {
               LoggerUtility.log(logger, "IOException occurred in copyMapFiles method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
            }
        }
        return fileCopyStatus;
    }

    public String copyPreMapFiles(Map files) {
        String fileCopyStatus = "Error";
        Iterator iterator = files.entrySet().iterator();
        while (iterator.hasNext()) {
            try {
                        Map.Entry pairs = (Map.Entry) iterator.next();
                File destinationDirectory = (File) pairs.getValue();
                File srcFile = (File) pairs.getKey();
                if (!destinationDirectory.exists()) {
                    destinationDirectory.mkdirs();
                }
                FileUtils.copyFileToDirectory(srcFile, destinationDirectory);
                String newFile = destinationDirectory + "\\" + srcFile.getName();
                File renameFile = new File(newFile);
                int mid = renameFile.getName().lastIndexOf(".");
                String fname = renameFile.getName().substring(0, mid);
                String ext = renameFile.getName().substring(mid + 1, renameFile.getName().length());
                String newName = fname + "_Reprocess." + ext;
                renameFile.renameTo(new File(destinationDirectory + "\\" + newName));
                fileCopyStatus = "Success";
                if (fileCopyStatus.equals("Success")) {
                    fileCopyStatus = "Success";
                } else {
                    fileCopyStatus = "error";
                }
            } catch (IOException ioException) {
                LoggerUtility.log(logger, "IOException occurred in copyPreMapFiles method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
            }
        }
        return fileCopyStatus;
    }

    public String copyPostMapFiles(Map files) {
        String fileCopyStatus = "Error";
        Iterator iterator = files.entrySet().iterator();
        while (iterator.hasNext()) {
            try {
                Map.Entry pairs = (Map.Entry) iterator.next();
                File destinationDirectory = (File) pairs.getValue();
                File srcFile = (File) pairs.getKey();
                if (!destinationDirectory.exists()) {
                    destinationDirectory.mkdirs();
                }
                FileUtils.copyFileToDirectory(srcFile, destinationDirectory);
                fileCopyStatus = "Success";
                if (fileCopyStatus.equals("Success")) {
                    fileCopyStatus = "Success";
                } else {
                    fileCopyStatus = "error";
                }
            } catch (IOException ioException) {
                LoggerUtility.log(logger, "IOException occurred in copyPostMapFiles method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
            }
        }
        return fileCopyStatus;
    }

    public String loadTenderCopyPostMapFiles(Map files) {
        String fileCopyStatus = "Error";
        Iterator iterator = files.entrySet().iterator();
        while (iterator.hasNext()) {
            try {
                Map.Entry pairs = (Map.Entry) iterator.next();
                File destinationDirectory = (File) pairs.getValue();
                File srcFile = (File) pairs.getKey();
                if (!destinationDirectory.exists()) {
                    destinationDirectory.mkdirs();
                }
                FileUtils.copyFileToDirectory(srcFile, destinationDirectory);
                fileCopyStatus = "Success";
                if (fileCopyStatus.equals("Success")) {
                    fileCopyStatus = "Success";
                } else {
                    fileCopyStatus = "error";
                }
            } catch (IOException ioException) {
               LoggerUtility.log(logger, "IOException occurred in loadTenderCopyPostMapFiles method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
            }
        }
        return fileCopyStatus;
    }

    public String loadTenderCopyPreMapFiles(Map files) {
        String fileCopyStatus = "Error";
        Iterator iterator = files.entrySet().iterator();
        while (iterator.hasNext()) {
            try {
                Map.Entry pairs = (Map.Entry) iterator.next();
                File destinationDirectory = (File) pairs.getValue();
                File srcFile = (File) pairs.getKey();
                if (!destinationDirectory.exists()) {
                    destinationDirectory.mkdirs();
                }
                FileUtils.copyFileToDirectory(srcFile, destinationDirectory);
                String newFile = destinationDirectory + "\\" + srcFile.getName();
                File renameFile = new File(newFile);
                int mid = renameFile.getName().lastIndexOf(".");
                String fname = renameFile.getName().substring(0, mid);
                String ext = renameFile.getName().substring(mid + 1, renameFile.getName().length());
                String newName = fname + "_Reprocess." + ext;
                renameFile.renameTo(new File(destinationDirectory + "\\" + newName));
                fileCopyStatus = "Success";
                if (fileCopyStatus.equals("Success")) {
                    loadPreprocessBatchFile();
                    fileCopyStatus = "Success";
                } else {
                    fileCopyStatus = "error";
                }
            } catch (IOException ioException) {
               LoggerUtility.log(logger, "IOException occurred in loadTenderCopyPreMapFiles method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
            }
        }
        return fileCopyStatus;
    }

    public void processBatchFile() {
        String resubmitFilePath = com.mss.ediscv.util.Properties.getProperty("RESUBMT_PATH");
        try {
            String[] array = {"cmd", "/C", "start", "/min", resubmitFilePath};
            Runtime.getRuntime().exec(array);
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in processBatchFile method:: " + exception.getMessage(), Level.ERROR,exception.getCause());
        }
    }

    public void postprocessBatchFile() {
        String retransmitFilePath = com.mss.ediscv.util.Properties.getProperty("RETRANSMIT_PATH");
        try {
            String[] array = {"cmd", "/C", "start", "/min", retransmitFilePath};
            Runtime.getRuntime().exec(array);
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in postprocessBatchFile method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
    }

    public void loadPreprocessBatchFile() {
        String filePath = "C:\\SI5.2\\Resubmit\\L_ReSubmit.bat";
        try {
            String[] array = {"cmd", "/C", "start", "/min", filePath};
            Runtime.getRuntime().exec(array);
        } catch (Exception exception) {
           LoggerUtility.log(logger, "Exception occurred in loadPreprocessBatchFile method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
    }
}