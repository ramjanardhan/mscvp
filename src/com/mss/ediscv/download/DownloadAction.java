/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.download;

import com.opensymphony.xwork2.Action;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author This Class.... ENTER THE DESCRIPTION HERE
 */
public class DownloadAction implements Action, ServletRequestAware, ServletResponseAware {

    private static Logger logger = LogManager.getLogger(DownloadAction.class.getName());
    private String contentDisposition = "FileName=inline";
    public InputStream inputStream;
    public OutputStream outputStream;
    private HttpServletRequest httpServletRequest;
    private int id;
    private String attachmentLocation;
    private String fileName;
    private String locationAvailable;

    public DownloadAction() {
    }

    @Override
    public String execute() throws Exception {
        return null;
    }

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        String responseString = "";
        try {
            this.setAttachmentLocation(httpServletRequest.getParameter("locationAvailable").trim());
            httpServletResponse.setContentType("application/force-download");
            String fileLocation=getAttachmentLocation().replace("*", "\\");
            File file = new File(fileLocation);
            if (file.exists()) {
                fileName = file.getName();
                inputStream = new FileInputStream(file);
                outputStream = httpServletResponse.getOutputStream();
                httpServletResponse.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
                int noOfBytesRead = 0;
                byte[] byteArray = new byte[1024];
                while ((inputStream != null) && ((noOfBytesRead = inputStream.read(byteArray)) != -1)) {
                    outputStream.write(byteArray, 0, noOfBytesRead);
                }
                responseString = "downLoaded!!";
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(responseString);
            } else {
                throw new FileNotFoundException("File not found");
            }
        } catch (FileNotFoundException ex) {
            try {
                httpServletResponse.sendRedirect("../general/exception.action?exceptionMessage='No File found'");
            } catch (IOException ioException) {
                LoggerUtility.log(logger, "IOException occurred in DownloadAction method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
            }
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in DownloadAction method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException ioException) {
                LoggerUtility.log(logger, "finally IOException occurred in DownloadAction method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAttachmentLocation() {
        return attachmentLocation;
    }

    public void setAttachmentLocation(String attachmentLocation) {
        this.attachmentLocation = attachmentLocation;
    }

    public String getLocationAvailable() {
        return locationAvailable;
    }

    public void setLocationAvailable(String locationAvailable) {
        this.locationAvailable = locationAvailable;
    }
}
