				/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.download;

import java.io.InputStream;
import java.io.OutputStream;
import com.mss.ediscv.util.ServiceLocator;
import com.mss.ediscv.util.ServiceLocatorException;
import com.opensymphony.xwork2.Action;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author miracle
 */
public class DownloadReportAction implements Action, ServletRequestAware, ServletResponseAware {

    private static Logger logger = LogManager.getLogger(DownloadReportAction.class.getName());
    private int scheduleId;
    private String startDate;
    private String reportattachment;
    private String fileName;
    private String contentDisposition = "FileName=inline";
    public InputStream inputStream;
    public OutputStream outputStream;
    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;

    public String reportDownloads() {

        try {
            this.setScheduleId(Integer.parseInt(httpServletRequest.getParameter("scheduleId").toString()));
            startDate = httpServletRequest.getParameter("startDate").toString();
            this.setReportattachment(ServiceLocator.getGridDownloadService().getReportattachment(this.getScheduleId(), this.getStartDate()));
            httpServletResponse.setContentType("application/force-download");
            File file = new File(getReportattachment());
            fileName = file.getName();
            inputStream = new FileInputStream(file);
            outputStream = httpServletResponse.getOutputStream();
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            int noOfBytesRead = 0;
            byte[] byteArray = null;
            while (true) {
                byteArray = new byte[1024];
                noOfBytesRead = inputStream.read(byteArray);
                if (noOfBytesRead == -1) {
                    break;
                }
                outputStream.write(byteArray, 0, noOfBytesRead);
            }
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "Exception occurred in reportDownloads method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in reportDownloads method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in reportDownloads method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException ioException) {
                LoggerUtility.log(logger, "finally IOException occurred in reportDownloads method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
            }
        }
        return null;

    }

    @Override
    public String execute() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getReportattachment() {
        return reportattachment;
    }

    public void setReportattachment(String reportattachment) {
        this.reportattachment = reportattachment;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
