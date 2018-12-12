 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.griddownload;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPTableEvent;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.mss.ediscv.doc.DocRepositoryBean;
import com.mss.ediscv.documentVisibility.DocumentVisibilityBean;
import com.mss.ediscv.editracking.TrackInOutBean;
import com.mss.ediscv.inv.InvoiceBean;
import com.mss.ediscv.inventory.InventoryBean;
import com.mss.ediscv.logisticeditracking.LogisticTrackInOutBean;
import com.mss.ediscv.logisticreports.LogisticReportsBean;
import com.mss.ediscv.logisticsdoc.LogisticsDocBean;
import com.mss.ediscv.logisticsinvoice.LogisticsInvoiceBean;
import com.mss.ediscv.logisticsloadtendering.LogisticsLoadBean;
import com.mss.ediscv.logisticsshipment.LtShipmentBean;
import com.mss.ediscv.ltResponse.LtResponseBean;
import com.mss.ediscv.payments.PaymentBean;
import com.mss.ediscv.po.PurchaseOrderBean;
import com.mss.ediscv.reports.ReportsBean;
import com.mss.ediscv.shipment.ShipmentBean;
import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocator;
import com.mss.ediscv.util.ServiceLocatorException;
import com.opensymphony.xwork2.Action;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GridDownloadAction implements Action, ServletRequestAware, ServletResponseAware {
    
    private static Logger logger = LogManager.getLogger(GridDownloadAction.class.getName());
    private String contentDisposition = "FileName=inline";
    public InputStream inputStream;
    public OutputStream outputStream;
    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;
    private String fileName;
    private String downloadType;
    private String sheetType;
    private String inbound;
    private String outbound;
    private int scheduleId;
    private String reportattachment;
    private String startDate;
    private String xaxis;
    private String yaxis;
    private String formData;
    
    public GridDownloadAction() {
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
            String fileLocation = "";
            if (getSheetType().equals("document") && getDownloadType().equals("xls")) {
                fileLocation = docExcelDownload();
            } else if (getSheetType().equals("inventory") && getDownloadType().equals("xls")) {
                fileLocation = inventoryExcelDownload();
            } else if (getSheetType().equals("documentReport") && getDownloadType().equals("xls")) {
                fileLocation = docReportExcelDownload();
            } else if (getSheetType().equals("trackSummary") && getDownloadType().equals("xls")) {
                fileLocation = docReportTrackingSummaryExcelDownload();
            } else if (getSheetType().equals("trackInOut") && getDownloadType().equals("xls")) {
                fileLocation = docReportTrackingInOutExcelDownload();
            } else if (getSheetType().equals("trackInquiry") && getDownloadType().equals("xls")) {
                fileLocation = docReportTrackingInquiryExcelDownload();
            } else if (getSheetType().equals("po") && getDownloadType().equals("xls")) {
                fileLocation = poExcelDownload();
            } else if (getSheetType().equals("shipment") && getDownloadType().equals("xls")) {
                fileLocation = shipmentExcelDownload();
            } else if (getSheetType().equals("invoice") && getDownloadType().equals("xls")) {
                fileLocation = invoiceExcelDownload();
            } else if (getSheetType().equals("payment") && getDownloadType().equals("xls")) {
                fileLocation = paymentExcelDownload();
            } else if (getSheetType().equals("logisticsDoc") && getDownloadType().equals("xls")) {
                fileLocation = logisticsDocExcelDownload();
            } else if (getSheetType().equals("loadTendering") && getDownloadType().equals("xls")) {
                fileLocation = loadTenderingExcelDownload();
            } else if (getSheetType().equals("ltResponse") && getDownloadType().equals("xls")) {
                fileLocation = ltResponseExcelDownload();
            } else if (getSheetType().equals("ltShipment") && getDownloadType().equals("xls")) {
                fileLocation = ltShipmentExcelDownload();
            } else if (getSheetType().equals("ltInvoice") && getDownloadType().equals("xls")) {
                fileLocation = ltInvoiceExcelDownload();
            } else if (getSheetType().equals("docVisibility") && getDownloadType().equals("xls")) {
                fileLocation = docVisibilityExcelDownload();
            } else if (getSheetType().equals("dashboard") && getDownloadType().equals("xls")) {
                fileLocation = dashVisibilityExcelDownload(getInbound(), getOutbound());
            } else if (getSheetType().equals("dashboard") && getDownloadType().equals("pdf")) {
                fileLocation = dashVisibilityPdfDownload(getInbound(), getOutbound());
            } else if (getSheetType().equals("dash") && getDownloadType().equals("xls")) {
                fileLocation = dashVisibilityExcelDownload(getYaxis(), getXaxis(), getFormData());
            } else if (getSheetType().equals("dash") && getDownloadType().equals("pdf")) {
                fileLocation = dashVisibilityPdfDownload(getYaxis(), getXaxis(), getFormData());
            } else if (getSheetType().equals("dash1") && getDownloadType().equals("pdf")) {
                fileLocation = dash1VisibilityPdfDownload(getYaxis(), getXaxis(), getFormData());
            } else if (getSheetType().equals("dash1") && getDownloadType().equals("xls")) {
                fileLocation = dash1VisibilityExcelDownload(getYaxis(), getXaxis(), getFormData());
            } else if (getSheetType().equals("tpdash") && getDownloadType().equals("xls")) {
                fileLocation = tpdashVisibilityExcelDownload(getYaxis(), getXaxis(), getFormData());
            } else if (getSheetType().equals("tpdash") && getDownloadType().equals("pdf")) {
                fileLocation = tpdashVisibilityPdfDownload(getYaxis(), getXaxis(), getFormData());
            } else if (getSheetType().equals("tpdash1") && getDownloadType().equals("pdf")) {
                fileLocation = tpdash1VisibilityPdfDownload(getYaxis(), getXaxis(), getFormData());
            } else if (getSheetType().equals("tpdash1") && getDownloadType().equals("xls")) {
                fileLocation = tpdash1VisibilityExcelDownload(getYaxis(), getXaxis(), getFormData());
            } else if (getSheetType().equals("logistictrackInOut") && getDownloadType().equals("xls")) {
                fileLocation = logisticReportTrackingInOutExcelDownload();
            } else if (getSheetType().equals("logistictrackInquiry") && getDownloadType().equals("xls")) {
                fileLocation = logisticReportTrackingInquiryExcelDownload();
            } else if (getSheetType().equals("logistictrackSummary") && getDownloadType().equals("xls")) {
                fileLocation = logisticReportTrackingSummaryExcelDownload();
            } else if (getSheetType().equals("logisticsReport") && getDownloadType().equals("xls")) {
                fileLocation = logisticsReportsExcelDownload();
            }
            httpServletResponse.setContentType("application/force-download");
            File file = new File(fileLocation);
            Date date = new Date();
            fileName = DateUtility.getInstance().getCurrentMySqlDateTime1() + "_" + file.getName();
            if (file.exists()) {
                inputStream = new FileInputStream(file);
                outputStream = httpServletResponse.getOutputStream();
                httpServletResponse.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
                int noOfBytesRead = 0;
                byte[] byteArray = new byte[1024];
                while ((inputStream != null) && ((noOfBytesRead = inputStream.read(byteArray)) != -1)) {
                    outputStream.write(byteArray, 0, noOfBytesRead);
                }
                responseString = "downLoaded!!";
                httpServletResponse.setContentType(getDownloadType());
                httpServletResponse.getWriter().write(responseString);
            } else {
                throw new FileNotFoundException("File not found");
            }
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in setServletResponse method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } catch (FileNotFoundException fileNotFoundException) {
            try {
                httpServletResponse.sendRedirect("../general/exception.action?exceptionMessage='No File found'");
            } catch (IOException ioException) {
                LoggerUtility.log(logger, "IOException occurred in setServletResponse method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
            }
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in setServletResponse method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException ioException) {
                LoggerUtility.log(logger, "finally IOException occurred in setServletResponse method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
            }
        }
    }
    
    public String docExcelDownload() {
        String filePath = "";
        try {
            java.util.List list = (java.util.List) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_DOC_LIST);
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.docCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + File.separator + "DocRepository.xls");
            filePath = file.getAbsolutePath() + com.mss.ediscv.util.Properties.getProperty("os.compatability") + "DocRepository.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("DocRepository");
            HSSFRow row1;
            DocRepositoryBean docRepositoryBean = null;
            if (list.size() != 0) {
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                HSSFCellStyle cellStyle1 = workbook.createCellStyle();
                HSSFCellStyle cellStyle2 = workbook.createCellStyle();
                HSSFCellStyle cellStyle3 = workbook.createCellStyle();
                HSSFCellStyle cellStyleHead = workbook.createCellStyle();
                HSSFFont font1 = workbook.createFont();
                HSSFFont font2 = workbook.createFont();
                HSSFFont font3 = workbook.createFont();
                HSSFFont font4 = workbook.createFont();
                HSSFFont fontHead = workbook.createFont();
                fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                font4.setColor(HSSFColor.WHITE.index);
                cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
                cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cellStyle.setFont(font4);
                Date date = new Date();
                row1 = worksheet.createRow((short) 0);
                HSSFCell cellpo0 = row1.createCell((short) 0);
                HSSFCell cellpo1 = row1.createCell((short) 1);
                HSSFCell cellpo2 = row1.createCell((short) 2);
                HSSFCell cellpo3 = row1.createCell((short) 3);
                HSSFCell cellpo4 = row1.createCell((short) 4);
                HSSFCell cellpo5 = row1.createCell((short) 5);
                HSSFCell cellpo6 = row1.createCell((short) 6);
                HSSFCell cellpo7 = row1.createCell((short) 7);
                row1 = worksheet.createRow((short) 1);
                Cell cell = row1.createCell((short) 1);
                cell.setCellValue("Doc Repositry:-Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
                cellStyleHead.setFont(fontHead);
                cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cellStyleHead.setFillForegroundColor(HSSFColor.YELLOW.index);
                cellStyleHead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cell.setCellStyle(cellStyleHead);
                worksheet.addMergedRegion(CellRangeAddress.valueOf("B2:I2"));
                
                row1 = worksheet.createRow((short) 3);
                HSSFCell cella1 = row1.createCell((short) 1);
                cella1.setCellValue("Instance_Id");
                cella1.setCellStyle(cellStyle);
                HSSFCell cellb1 = row1.createCell((short) 2);
                cellb1.setCellValue("FileFormat");
                cellb1.setCellStyle(cellStyle);
                HSSFCell cellc1 = row1.createCell((short) 3);
                cellc1.setCellValue("Partner");
                cellc1.setCellStyle(cellStyle);
                HSSFCell celld1 = row1.createCell((short) 4);
                celld1.setCellValue("DateTime");
                celld1.setCellStyle(cellStyle);
                HSSFCell celle1 = row1.createCell((short) 5);
                celle1.setCellValue("Trans_Type");
                celle1.setCellStyle(cellStyle);
                HSSFCell cellf1 = row1.createCell((short) 6);
                cellf1.setCellValue("Direction");
                cellf1.setCellStyle(cellStyle);
                HSSFCell cellg1 = row1.createCell((short) 7);
                cellg1.setCellValue("Status");
                cellg1.setCellStyle(cellStyle);
                HSSFCell cellh1 = row1.createCell((short) 8);
                cellh1.setCellValue("Reprocess");
                cellh1.setCellStyle(cellStyle);
                
                for (int i = 0; i < list.size(); i++) {
                    docRepositoryBean = (DocRepositoryBean) list.get(i);
                    
                    row1 = worksheet.createRow((short) i + 4);
                    
                    HSSFCell cellA1 = row1.createCell((short) 1);
                    cellA1.setCellValue(docRepositoryBean.getFile_id());
                    
                    HSSFCell cellB1 = row1.createCell((short) 2);
                    cellB1.setCellValue(docRepositoryBean.getFile_origin());
                    
                    HSSFCell cellC1 = row1.createCell((short) 3);
                    cellC1.setCellValue(docRepositoryBean.getPname());
                    
                    HSSFCell cellD1 = row1.createCell((short) 4);
                    cellD1.setCellValue(docRepositoryBean.getDate_time_rec().toString().substring(0, docRepositoryBean.getDate_time_rec().toString().lastIndexOf(":")));
                    
                    HSSFCell cellE1 = row1.createCell((short) 5);
                    cellE1.setCellValue(docRepositoryBean.getTransaction_type());
                    
                    HSSFCell cellF1 = row1.createCell((short) 6);
                    cellF1.setCellValue(docRepositoryBean.getDirection());
                    
                    HSSFCell cellG1 = row1.createCell((short) 7);
                    if (docRepositoryBean.getStatus() != null) {
                        if (docRepositoryBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                            font1.setColor(HSSFColor.GREEN.index);
                            cellG1.setCellValue(docRepositoryBean.getStatus().toUpperCase());
                            cellStyle1.setFont(font1);
                            cellG1.setCellStyle(cellStyle1);
                        } else if (docRepositoryBean.getStatus().equalsIgnoreCase("ERROR")) {
                            
                            font2.setColor(HSSFColor.RED.index);
                            cellG1.setCellValue(docRepositoryBean.getStatus().toUpperCase());
                            cellStyle2.setFont(font2);
                            cellG1.setCellStyle(cellStyle2);
                        } else {
                            font3.setColor(HSSFColor.ORANGE.index);
                            cellG1.setCellValue(docRepositoryBean.getStatus().toUpperCase());
                            cellStyle3.setFont(font3);
                            cellG1.setCellStyle(cellStyle3);
                        }
                    }
                    HSSFCell cellH1 = row1.createCell((short) 8);
                    cellH1.setCellValue(docRepositoryBean.getReProcessStatus());
                }
            }
            
            worksheet.setColumnWidth(1, 5000);
            worksheet.setColumnWidth(2, 5000);
            worksheet.setColumnWidth(3, 6000);
            worksheet.setColumnWidth(4, 6000);
            worksheet.setColumnWidth(5, 5000);
            worksheet.setColumnWidth(6, 5000);
            worksheet.setColumnWidth(7, 5000);
            worksheet.setColumnWidth(8, 5000);
            
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in docExcelDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in docExcelDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in docExcelDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String inventoryExcelDownload() {
        String filePath = "";
        try {
            java.util.List list = (java.util.List) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_INVENTORY_LIST);
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.inventDocCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + com.mss.ediscv.util.Properties.getProperty("os.compatability") + "InventoryExcel.xls");
            filePath = file.getAbsolutePath() + com.mss.ediscv.util.Properties.getProperty("os.compatability") + "InventoryExcel.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("InventoryExcel");
            HSSFRow row1;
            InventoryBean inventoryBean = null;
            if (list.size() != 0) {
                
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                HSSFCellStyle cellStyle1 = workbook.createCellStyle();
                HSSFCellStyle cellStyle2 = workbook.createCellStyle();
                HSSFCellStyle cellStyle3 = workbook.createCellStyle();
                HSSFCellStyle cellStyleHead = workbook.createCellStyle();
                HSSFFont font1 = workbook.createFont();
                HSSFFont font2 = workbook.createFont();
                HSSFFont font3 = workbook.createFont();
                HSSFFont font4 = workbook.createFont();
                HSSFFont fontHead = workbook.createFont();
                fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                font4.setColor(HSSFColor.WHITE.index);
                cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
                cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cellStyle.setFont(font4);
                Date date = new Date();
                row1 = worksheet.createRow((short) 0);
                HSSFCell cellpo0 = row1.createCell((short) 0);
                HSSFCell cellpo1 = row1.createCell((short) 1);
                HSSFCell cellpo2 = row1.createCell((short) 2);
                HSSFCell cellpo3 = row1.createCell((short) 3);
                HSSFCell cellpo4 = row1.createCell((short) 4);
                HSSFCell cellpo5 = row1.createCell((short) 5);
                HSSFCell cellpo6 = row1.createCell((short) 6);
                HSSFCell cellpo7 = row1.createCell((short) 7);
                row1 = worksheet.createRow((short) 1);
                Cell cell = row1.createCell((short) 1);
                cell.setCellValue("Manufacturing inventory Reports:-Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
                cellStyleHead.setFont(fontHead);
                cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cellStyleHead.setFillForegroundColor(HSSFColor.YELLOW.index);
                cellStyleHead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cell.setCellStyle(cellStyleHead);
                worksheet.addMergedRegion(CellRangeAddress.valueOf("B2:I2"));
                row1 = worksheet.createRow((short) 3);
                HSSFCell cella1 = row1.createCell((short) 1);
                cella1.setCellValue("Instance_Id");
                cella1.setCellStyle(cellStyle);
                HSSFCell cellb1 = row1.createCell((short) 2);
                cellb1.setCellValue("Partner");
                cellb1.setCellStyle(cellStyle);
                HSSFCell cellc1 = row1.createCell((short) 3);
                cellc1.setCellValue("Direction");
                cellc1.setCellStyle(cellStyle);
                HSSFCell celld1 = row1.createCell((short) 4);
                celld1.setCellValue("Reporting_period");
                celld1.setCellStyle(cellStyle);
                HSSFCell celle1 = row1.createCell((short) 5);
                celle1.setCellValue("Reporting_date");
                celle1.setCellStyle(cellStyle);
                HSSFCell cellf1 = row1.createCell((short) 6);
                cellf1.setCellValue("Vendor_name");
                cellf1.setCellStyle(cellStyle);
                HSSFCell cellg1 = row1.createCell((short) 7);
                cellg1.setCellValue("Vendor_location");
                cellg1.setCellStyle(cellStyle);
                HSSFCell cellh1 = row1.createCell((short) 8);
                cellh1.setCellValue("Status");
                cellh1.setCellStyle(cellStyle);
                
                for (int i = 0; i < list.size(); i++) {
                    inventoryBean = (InventoryBean) list.get(i);
                    
                    row1 = worksheet.createRow((short) i + 4);
                    
                    HSSFCell cellA1 = row1.createCell((short) 1);
                    cellA1.setCellValue(inventoryBean.getFile_id());
                    
                    HSSFCell cellB1 = row1.createCell((short) 2);
                    cellB1.setCellValue(inventoryBean.getPname());
                    
                    HSSFCell cellC1 = row1.createCell((short) 3);
                    cellC1.setCellValue(inventoryBean.getDirection());
                    
                    HSSFCell cellD1 = row1.createCell((short) 4);
                    cellD1.setCellValue(inventoryBean.getReportingPeriod());
                    
                    HSSFCell cellE1 = row1.createCell((short) 5);
                    cellE1.setCellValue(inventoryBean.getReportingDate());
                    
                    HSSFCell cellF1 = row1.createCell((short) 6);
                    cellF1.setCellValue(inventoryBean.getVendorName());
                    
                    HSSFCell cellG1 = row1.createCell((short) 7);
                    cellG1.setCellValue(inventoryBean.getVendorLocation());
                    
                    HSSFCell cellH1 = row1.createCell((short) 8);
                    if (inventoryBean.getStatus() != null) {
                        if (inventoryBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                            cellH1.setCellValue(inventoryBean.getStatus().toUpperCase());
                        } else if (inventoryBean.getStatus().equalsIgnoreCase("ERROR")) {
                            cellH1.setCellValue(inventoryBean.getStatus().toUpperCase());
                        } else {
                            cellF1.setCellValue(inventoryBean.getStatus().toUpperCase());
                        }
                    }
                }
            }
            worksheet.setColumnWidth(1, 5000);
            worksheet.setColumnWidth(2, 5000);
            worksheet.setColumnWidth(3, 6000);
            worksheet.setColumnWidth(4, 6000);
            worksheet.setColumnWidth(5, 5000);
            worksheet.setColumnWidth(6, 5000);
            worksheet.setColumnWidth(7, 5000);
            worksheet.setColumnWidth(8, 5000);
            
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in invetoryExcelDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in invetoryExcelDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in invetoryExcelDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String poExcelDownload() {
        String filePath = "";
        try {
            java.util.List list = (java.util.List) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_PO_LIST);
            
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.poCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + com.mss.ediscv.util.Properties.getProperty("os.compatability") + "Po.xls");
            filePath = file.getAbsolutePath() + com.mss.ediscv.util.Properties.getProperty("os.compatability") + "Po.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("PurchaseOrder");
            HSSFRow row1;
            PurchaseOrderBean purchaseOrderBean = null;
            if (list.size() != 0) {
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                HSSFCellStyle cellStyle1 = workbook.createCellStyle();
                HSSFCellStyle cellStyle2 = workbook.createCellStyle();
                HSSFCellStyle cellStyle3 = workbook.createCellStyle();
                HSSFCellStyle cellStyleHead = workbook.createCellStyle();
                HSSFFont font1 = workbook.createFont();
                HSSFFont font2 = workbook.createFont();
                HSSFFont font3 = workbook.createFont();
                HSSFFont font4 = workbook.createFont();
                HSSFFont fontHead = workbook.createFont();
                fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                font4.setColor(HSSFColor.WHITE.index);
                cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
                cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cellStyle.setFont(font4);
                Date date = new Date();
                row1 = worksheet.createRow((short) 0);
                HSSFCell cellpo0 = row1.createCell((short) 0);
                HSSFCell cellpo1 = row1.createCell((short) 1);
                HSSFCell cellpo2 = row1.createCell((short) 2);
                HSSFCell cellpo3 = row1.createCell((short) 3);
                HSSFCell cellpo4 = row1.createCell((short) 4);
                HSSFCell cellpo5 = row1.createCell((short) 5);
                HSSFCell cellpo6 = row1.createCell((short) 6);
                HSSFCell cellpo7 = row1.createCell((short) 7);
                row1 = worksheet.createRow((short) 1);
                Cell cell = row1.createCell((short) 1);
                cell.setCellValue("Purchase Order:-Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
                cellStyleHead.setFont(fontHead);
                cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cellStyleHead.setFillForegroundColor(HSSFColor.YELLOW.index);
                cellStyleHead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cell.setCellStyle(cellStyleHead);
                worksheet.addMergedRegion(CellRangeAddress.valueOf("B2:I2"));
                row1 = worksheet.createRow((short) 3);
                HSSFCell cella1 = row1.createCell((short) 1);
                cella1.setCellValue("Instance_Id");
                cella1.setCellStyle(cellStyle);
                HSSFCell cellb1 = row1.createCell((short) 2);
                cellb1.setCellValue("PO #");
                cellb1.setCellStyle(cellStyle);
                HSSFCell cellc1 = row1.createCell((short) 3);
                cellc1.setCellValue("PO Date");
                cellc1.setCellStyle(cellStyle);
                HSSFCell celld1 = row1.createCell((short) 4);
                celld1.setCellValue("Partner");
                celld1.setCellStyle(cellStyle);
                HSSFCell celle1 = row1.createCell((short) 5);
                celle1.setCellValue("GS_Control #");
                celle1.setCellStyle(cellStyle);
                HSSFCell cellf1 = row1.createCell((short) 6);
                cellf1.setCellValue("Direction");
                cellf1.setCellStyle(cellStyle);
                HSSFCell cellg1 = row1.createCell((short) 7);
                cellg1.setCellValue("Status");
                cellg1.setCellStyle(cellStyle);
                HSSFCell cellh1 = row1.createCell((short) 8);
                cellh1.setCellValue("Reprocess");
                cellh1.setCellStyle(cellStyle);
                for (int i = 0; i < list.size(); i++) {
                    purchaseOrderBean = (PurchaseOrderBean) list.get(i);
                    
                    row1 = worksheet.createRow((short) i + 4);
                    
                    HSSFCell cellA1 = row1.createCell((short) 1);
                    cellA1.setCellValue(purchaseOrderBean.getFileId());
                    
                    HSSFCell cellB1 = row1.createCell((short) 2);
                    cellB1.setCellValue(purchaseOrderBean.getPo());
                    
                    HSSFCell cellC1 = row1.createCell((short) 3);
                    cellC1.setCellValue(purchaseOrderBean.getPoDate());
                    
                    HSSFCell cellD1 = row1.createCell((short) 4);
                    cellD1.setCellValue(purchaseOrderBean.getPname());
                    
                    HSSFCell cellE1 = row1.createCell((short) 5);
                    cellE1.setCellValue(purchaseOrderBean.getGsControlNumber());
                    
                    HSSFCell cellF1 = row1.createCell((short) 6);
                    cellF1.setCellValue(purchaseOrderBean.getDirection());
                    
                    HSSFCell cellG1 = row1.createCell((short) 7);
                    
                    if (purchaseOrderBean.getStatus() != null) {
                        if (purchaseOrderBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                            font1.setColor(HSSFColor.GREEN.index);
                            cellG1.setCellValue(purchaseOrderBean.getStatus().toUpperCase());
                            cellStyle1.setFont(font1);
                            cellG1.setCellStyle(cellStyle1);
                        } else if (purchaseOrderBean.getStatus().equalsIgnoreCase("ERROR")) {
                            font2.setColor(HSSFColor.RED.index);
                            cellG1.setCellValue(purchaseOrderBean.getStatus().toUpperCase());
                            cellStyle2.setFont(font2);
                            cellG1.setCellStyle(cellStyle2);
                        } else {
                            font3.setColor(HSSFColor.ORANGE.index);
                            cellG1.setCellValue(purchaseOrderBean.getStatus().toUpperCase());
                            cellStyle3.setFont(font3);
                            cellG1.setCellStyle(cellStyle3);
                        }
                    }
                    HSSFCell cellH1 = row1.createCell((short) 8);
                    cellH1.setCellValue(purchaseOrderBean.getReProcessStatus());
                }
            }
            worksheet.setColumnWidth(1, 5000);
            worksheet.setColumnWidth(2, 5000);
            worksheet.setColumnWidth(3, 6000);
            worksheet.setColumnWidth(4, 6000);
            worksheet.setColumnWidth(5, 5000);
            worksheet.setColumnWidth(6, 5000);
            worksheet.setColumnWidth(7, 5000);
            worksheet.setColumnWidth(8, 5000);
            
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in poExcelDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in poExcelDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in poExcelDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String shipmentExcelDownload() {
        String filePath = "";
        try {
            java.util.List list = (java.util.List) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_SHIPMENT_LIST);
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.shipmentCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + com.mss.ediscv.util.Properties.getProperty("os.compatability") + "Shipment.xls");
            filePath = file.getAbsolutePath() + com.mss.ediscv.util.Properties.getProperty("os.compatability") + "Shipment.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("Shipment");
            HSSFRow row1;
            ShipmentBean shipmentBean = null;
            if (list.size() != 0) {
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                HSSFCellStyle cellStyle1 = workbook.createCellStyle();
                HSSFCellStyle cellStyle2 = workbook.createCellStyle();
                HSSFCellStyle cellStyle3 = workbook.createCellStyle();
                HSSFCellStyle cellStyleHead = workbook.createCellStyle();
                HSSFFont font1 = workbook.createFont();
                HSSFFont font2 = workbook.createFont();
                HSSFFont font3 = workbook.createFont();
                HSSFFont font4 = workbook.createFont();
                HSSFFont fontHead = workbook.createFont();
                fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                font4.setColor(HSSFColor.WHITE.index);
                cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
                cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cellStyle.setFont(font4);
                Date date = new Date();
                row1 = worksheet.createRow((short) 0);
                HSSFCell cellpo0 = row1.createCell((short) 0);
                HSSFCell cellpo1 = row1.createCell((short) 1);
                HSSFCell cellpo2 = row1.createCell((short) 2);
                HSSFCell cellpo3 = row1.createCell((short) 3);
                HSSFCell cellpo4 = row1.createCell((short) 4);
                HSSFCell cellpo5 = row1.createCell((short) 5);
                HSSFCell cellpo6 = row1.createCell((short) 6);
                row1 = worksheet.createRow((short) 1);
                Cell cell = row1.createCell((short) 1);
                cell.setCellValue("Shipment :-Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
                cellStyleHead.setFont(fontHead);
                cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cellStyleHead.setFillForegroundColor(HSSFColor.YELLOW.index);
                cellStyleHead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cell.setCellStyle(cellStyleHead);
                worksheet.addMergedRegion(CellRangeAddress.valueOf("B2:H2"));
                row1 = worksheet.createRow((short) 3);
                HSSFCell cella1 = row1.createCell((short) 1);
                cella1.setCellValue("Instance_Id");
                cella1.setCellStyle(cellStyle);
                HSSFCell cellb1 = row1.createCell((short) 2);
                cellb1.setCellValue("ASN #");
                cellb1.setCellStyle(cellStyle);
                HSSFCell cellc1 = row1.createCell((short) 3);
                cellc1.setCellValue("Partner");
                cellc1.setCellStyle(cellStyle);
                HSSFCell celld1 = row1.createCell((short) 4);
                celld1.setCellValue("DateTime");
                celld1.setCellStyle(cellStyle);
                HSSFCell celle1 = row1.createCell((short) 5);
                celle1.setCellValue("Direction");
                celle1.setCellStyle(cellStyle);
                HSSFCell cellf1 = row1.createCell((short) 6);
                cellf1.setCellValue("Status");
                cellf1.setCellStyle(cellStyle);
                HSSFCell cellg1 = row1.createCell((short) 7);
                cellg1.setCellValue("ACK Status");
                cellg1.setCellStyle(cellStyle);
                
                for (int i = 0; i < list.size(); i++) {
                    shipmentBean = (ShipmentBean) list.get(i);
                    
                    row1 = worksheet.createRow((short) i + 4);
                    
                    HSSFCell cellA1 = row1.createCell((short) 1);
                    cellA1.setCellValue(shipmentBean.getFile_id());
                    
                    HSSFCell cellB1 = row1.createCell((short) 2);
                    cellB1.setCellValue(shipmentBean.getAsnNo());
                    
                    HSSFCell cellC1 = row1.createCell((short) 3);
                    cellC1.setCellValue(shipmentBean.getPname());
                    
                    HSSFCell cellD1 = row1.createCell((short) 4);
                    cellD1.setCellValue(shipmentBean.getDate_time_rec().toString().substring(0, shipmentBean.getDate_time_rec().toString().lastIndexOf(":")));
                    
                    HSSFCell cellE1 = row1.createCell((short) 5);
                    cellE1.setCellValue(shipmentBean.getDirection());
                    
                    HSSFCell cellF1 = row1.createCell((short) 6);
                    if (shipmentBean.getStatus() != null) {
                        if (shipmentBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                            font1.setColor(HSSFColor.GREEN.index);
                            cellF1.setCellValue(shipmentBean.getStatus().toUpperCase());
                            cellStyle1.setFont(font1);
                            cellF1.setCellStyle(cellStyle1);
                        } else if (shipmentBean.getStatus().equalsIgnoreCase("ERROR")) {
                            font2.setColor(HSSFColor.RED.index);
                            cellF1.setCellValue(shipmentBean.getStatus().toUpperCase());
                            cellStyle2.setFont(font2);
                            cellF1.setCellStyle(cellStyle2);
                        } else {
                            font3.setColor(HSSFColor.ORANGE.index);
                            cellF1.setCellValue(shipmentBean.getStatus().toUpperCase());
                            cellStyle3.setFont(font3);
                            cellF1.setCellStyle(cellStyle3);
                        }
                    }
                    HSSFCell cellG1 = row1.createCell((short) 7);
                    if (shipmentBean.getAckStatus() != null) {
                        if (shipmentBean.getAckStatus().equalsIgnoreCase("ACCEPTED")) {
                            font1.setColor(HSSFColor.GREEN.index);
                            cellG1.setCellValue(shipmentBean.getAckStatus().toUpperCase());
                            cellStyle1.setFont(font1);
                            cellG1.setCellStyle(cellStyle1);
                        } else if (shipmentBean.getAckStatus().equalsIgnoreCase("REJECTED")) {
                            font2.setColor(HSSFColor.RED.index);
                            cellG1.setCellValue(shipmentBean.getAckStatus().toUpperCase());
                            cellStyle2.setFont(font2);
                            cellG1.setCellStyle(cellStyle2);
                        } else {
                            font3.setColor(HSSFColor.ORANGE.index);
                            cellG1.setCellValue(shipmentBean.getAckStatus().toUpperCase());
                            cellStyle3.setFont(font3);
                            cellG1.setCellStyle(cellStyle3);
                        }
                    }
                }
            }
            worksheet.setColumnWidth(1, 5000);
            worksheet.setColumnWidth(2, 5000);
            worksheet.setColumnWidth(3, 6000);
            worksheet.setColumnWidth(4, 6000);
            worksheet.setColumnWidth(5, 5000);
            worksheet.setColumnWidth(6, 5000);
            worksheet.setColumnWidth(7, 5000);
            worksheet.setColumnWidth(8, 5000);
            
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in shipmentExcelDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in shipmentExcelDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in shipmentExcelDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String invoiceExcelDownload() {
        String filePath = "";
        try {
            java.util.List list = (java.util.List) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_INV_LIST);
            
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.invoiceCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + File.separator + "Invoice.xls");
            filePath = file.getAbsolutePath() + File.separator + "Invoice.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("Invoice");
            HSSFRow row1;
            InvoiceBean invoiceBean = null;
            
            if (list.size() != 0) {
                
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                HSSFCellStyle cellStyle1 = workbook.createCellStyle();
                HSSFCellStyle cellStyle2 = workbook.createCellStyle();
                HSSFCellStyle cellStyle3 = workbook.createCellStyle();
                HSSFCellStyle cellStyleHead = workbook.createCellStyle();
                HSSFFont font1 = workbook.createFont();
                HSSFFont font2 = workbook.createFont();
                HSSFFont font3 = workbook.createFont();
                HSSFFont font4 = workbook.createFont();
                HSSFFont fontHead = workbook.createFont();
                fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                font4.setColor(HSSFColor.WHITE.index);
                cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
                cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cellStyle.setFont(font4);
                Date date = new Date();
                row1 = worksheet.createRow((short) 0);
                HSSFCell cellpo0 = row1.createCell((short) 0);
                HSSFCell cellpo1 = row1.createCell((short) 1);
                HSSFCell cellpo2 = row1.createCell((short) 2);
                HSSFCell cellpo3 = row1.createCell((short) 3);
                HSSFCell cellpo4 = row1.createCell((short) 4);
                HSSFCell cellpo5 = row1.createCell((short) 5);
                HSSFCell cellpo6 = row1.createCell((short) 6);
                HSSFCell cellpo7 = row1.createCell((short) 7);
                HSSFCell cellpo8 = row1.createCell((short) 8);
                row1 = worksheet.createRow((short) 1);
                Cell cell = row1.createCell((short) 1);
                cell.setCellValue("Invoice :-Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
                cellStyleHead.setFont(fontHead);
                cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cellStyleHead.setFillForegroundColor(HSSFColor.YELLOW.index);
                cellStyleHead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cell.setCellStyle(cellStyleHead);
                worksheet.addMergedRegion(CellRangeAddress.valueOf("B2:J2"));
                
                row1 = worksheet.createRow((short) 3);
                
                HSSFCell cella1 = row1.createCell((short) 1);
                cella1.setCellValue("Instance_Id");
                cella1.setCellStyle(cellStyle);
                
                HSSFCell cellb1 = row1.createCell((short) 2);
                cellb1.setCellValue("Partner");
                cellb1.setCellStyle(cellStyle);
                HSSFCell cellc1 = row1.createCell((short) 3);
                cellc1.setCellValue("Invoice#");
                cellc1.setCellStyle(cellStyle);
                HSSFCell celld1 = row1.createCell((short) 4);
                celld1.setCellValue("PO#");
                celld1.setCellStyle(cellStyle);
                HSSFCell celle1 = row1.createCell((short) 5);
                celle1.setCellValue("Item Qty");
                celle1.setCellStyle(cellStyle);
                HSSFCell cellf1 = row1.createCell((short) 6);
                cellf1.setCellValue("Invoice Amount");
                cellf1.setCellStyle(cellStyle);
                HSSFCell cellg1 = row1.createCell((short) 7);
                cellg1.setCellValue("Invoice Date");
                cellg1.setCellStyle(cellStyle);
                HSSFCell cellh1 = row1.createCell((short) 8);
                cellh1.setCellValue("Status");
                cellh1.setCellStyle(cellStyle);
                HSSFCell celli1 = row1.createCell((short) 9);
                celli1.setCellValue("Ack Status");
                celli1.setCellStyle(cellStyle);
                
                for (int i = 0; i < list.size(); i++) {
                    invoiceBean = (InvoiceBean) list.get(i);
                    
                    row1 = worksheet.createRow((short) i + 4);
                    
                    HSSFCell cellA1 = row1.createCell((short) 1);
                    cellA1.setCellValue(invoiceBean.getFileId());
                    
                    HSSFCell cellB1 = row1.createCell((short) 2);
                    cellB1.setCellValue(invoiceBean.getPname());
                    
                    HSSFCell cellC1 = row1.createCell((short) 3);
                    cellC1.setCellValue(invoiceBean.getInvNumber());
                    
                    HSSFCell cellD1 = row1.createCell((short) 4);
                    cellD1.setCellValue(invoiceBean.getPoNumber());
                    
                    HSSFCell cellE1 = row1.createCell((short) 5);
                    cellE1.setCellValue(invoiceBean.getItemQty());
                    
                    HSSFCell cellF1 = row1.createCell((short) 6);
                    cellF1.setCellValue(invoiceBean.getInvAmount());
                    
                    HSSFCell cellG1 = row1.createCell((short) 7);
                    cellG1.setCellValue(invoiceBean.getDate_time_rec().toString().substring(0, invoiceBean.getDate_time_rec().toString().lastIndexOf(":")));
                    
                    HSSFCell cellH1 = row1.createCell((short) 8);
                    if (invoiceBean.getStatus() != null) {
                        if (invoiceBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                            
                            font1.setColor(HSSFColor.GREEN.index);
                            
                            cellH1.setCellValue(invoiceBean.getStatus().toUpperCase());
                            cellStyle1.setFont(font1);
                            cellH1.setCellStyle(cellStyle1);
                            
                        } else if (invoiceBean.getStatus().equalsIgnoreCase("ERROR")) {
                            font2.setColor(HSSFColor.RED.index);
                            cellH1.setCellValue(invoiceBean.getStatus().toUpperCase());
                            cellStyle2.setFont(font2);
                            cellH1.setCellStyle(cellStyle2);
                        } else {
                            font3.setColor(HSSFColor.ORANGE.index);
                            cellH1.setCellValue(invoiceBean.getStatus().toUpperCase());
                            cellStyle3.setFont(font3);
                            cellH1.setCellStyle(cellStyle3);
                        }
                    }
                    HSSFCell cellI1 = row1.createCell((short) 9);
                    if (invoiceBean.getAckStatus() != null) {
                        if (invoiceBean.getAckStatus().equalsIgnoreCase("ACCEPTED")) {
                            
                            font1.setColor(HSSFColor.GREEN.index);
                            cellI1.setCellValue(invoiceBean.getAckStatus().toUpperCase());
                            cellStyle1.setFont(font1);
                            cellI1.setCellStyle(cellStyle1);
                            
                        } else if (invoiceBean.getAckStatus().equalsIgnoreCase("REJECTED")) {
                            
                            font2.setColor(HSSFColor.RED.index);
                            cellI1.setCellValue(invoiceBean.getAckStatus().toUpperCase());
                            cellStyle2.setFont(font2);
                            cellI1.setCellStyle(cellStyle2);
                            
                        } else {
                            
                            font3.setColor(HSSFColor.ORANGE.index);
                            cellI1.setCellValue(invoiceBean.getAckStatus().toUpperCase());
                            cellStyle3.setFont(font3);
                            cellI1.setCellStyle(cellStyle3);
                        }
                    }
                }
            }
            worksheet.setColumnWidth(1, 5000);
            worksheet.setColumnWidth(2, 5000);
            worksheet.setColumnWidth(3, 6000);
            worksheet.setColumnWidth(4, 6000);
            worksheet.setColumnWidth(5, 5000);
            worksheet.setColumnWidth(6, 5000);
            worksheet.setColumnWidth(7, 5000);
            worksheet.setColumnWidth(8, 5000);
            worksheet.setColumnWidth(9, 5000);
            
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in invoiceExcelDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in invoiceExcelDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in invoiceExcelDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String paymentExcelDownload() {
        String filePath = "";
        try {
            java.util.List list = (java.util.List) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_PAYMENT_LIST);
            
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.paymentCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + com.mss.ediscv.util.Properties.getProperty("os.compatability") + "Payment.xls");
            filePath = file.getAbsolutePath() + com.mss.ediscv.util.Properties.getProperty("os.compatability") + "Payment.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("Payment");
            HSSFRow row1;
            PaymentBean paymentBean = null;
            
            if (list.size() != 0) {
                
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                HSSFCellStyle cellStyle1 = workbook.createCellStyle();
                HSSFCellStyle cellStyle2 = workbook.createCellStyle();
                HSSFCellStyle cellStyle3 = workbook.createCellStyle();
                HSSFCellStyle cellStyleHead = workbook.createCellStyle();
                HSSFFont font1 = workbook.createFont();
                HSSFFont font2 = workbook.createFont();
                HSSFFont font3 = workbook.createFont();
                HSSFFont font4 = workbook.createFont();
                HSSFFont fontHead = workbook.createFont();
                fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                font4.setColor(HSSFColor.WHITE.index);
                
                cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
                cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cellStyle.setFont(font4);
                Date date = new Date();
                row1 = worksheet.createRow((short) 0);
                HSSFCell cellpo0 = row1.createCell((short) 0);
                HSSFCell cellpo1 = row1.createCell((short) 1);
                HSSFCell cellpo2 = row1.createCell((short) 2);
                HSSFCell cellpo3 = row1.createCell((short) 3);
                HSSFCell cellpo4 = row1.createCell((short) 4);
                HSSFCell cellpo5 = row1.createCell((short) 5);
                HSSFCell cellpo6 = row1.createCell((short) 6);
                HSSFCell cellpo7 = row1.createCell((short) 7);
                HSSFCell cellpo8 = row1.createCell((short) 8);
                row1 = worksheet.createRow((short) 1);
                Cell cell = row1.createCell((short) 1);
                cell.setCellValue("Payment :-Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
                cellStyleHead.setFont(fontHead);
                cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cellStyleHead.setFillForegroundColor(HSSFColor.YELLOW.index);
                cellStyleHead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cell.setCellStyle(cellStyleHead);
                worksheet.addMergedRegion(CellRangeAddress.valueOf("B2:J2"));
                row1 = worksheet.createRow((short) 3);
                HSSFCell cella1 = row1.createCell((short) 1);
                cella1.setCellValue("Partner");
                cella1.setCellStyle(cellStyle);
                HSSFCell cellb1 = row1.createCell((short) 2);
                cellb1.setCellValue("Instance Id");
                cellb1.setCellStyle(cellStyle);
                HSSFCell cellc1 = row1.createCell((short) 3);
                cellc1.setCellValue("PO#");
                cellc1.setCellStyle(cellStyle);
                HSSFCell celld1 = row1.createCell((short) 4);
                celld1.setCellValue("Invoice#");
                celld1.setCellStyle(cellStyle);
                HSSFCell celle1 = row1.createCell((short) 5);
                celle1.setCellValue("Date");
                celle1.setCellStyle(cellStyle);
                HSSFCell cellf1 = row1.createCell((short) 6);
                cellf1.setCellValue("Cheque#");
                cellf1.setCellStyle(cellStyle);
                HSSFCell cellg1 = row1.createCell((short) 7);
                cellg1.setCellValue("Cheque Amount");
                cellg1.setCellStyle(cellStyle);
                HSSFCell cellh1 = row1.createCell((short) 8);
                cellh1.setCellValue("Status");
                cellh1.setCellStyle(cellStyle);
                HSSFCell celli1 = row1.createCell((short) 9);
                celli1.setCellValue("Ack Status");
                celli1.setCellStyle(cellStyle);
                
                for (int i = 0; i < list.size(); i++) {
                    paymentBean = (PaymentBean) list.get(i);
                    
                    row1 = worksheet.createRow((short) i + 4);
                    
                    HSSFCell cellA1 = row1.createCell((short) 1);
                    
                    cellA1.setCellValue(paymentBean.getReceiverName());
                    
                    HSSFCell cellB1 = row1.createCell((short) 2);
                    
                    cellB1.setCellValue(paymentBean.getFileId());
                    HSSFCell cellC1 = row1.createCell((short) 3);
                    
                    cellC1.setCellValue(paymentBean.getPonumber());
                    
                    HSSFCell cellD1 = row1.createCell((short) 4);
                    
                    cellD1.setCellValue(paymentBean.getInvNumber());
                    HSSFCell cellE1 = row1.createCell((short) 5);
                    
                    cellE1.setCellValue(paymentBean.getDate());
                    
                    HSSFCell cellF1 = row1.createCell((short) 6);
                    cellF1.setCellValue(paymentBean.getCheckNumber());
                    HSSFCell cellG1 = row1.createCell((short) 7);
                    cellG1.setCellValue(paymentBean.getCheckAmount());
                    HSSFCell cellH1 = row1.createCell((short) 8);
                    if (paymentBean.getStatus() != null) {
                        if (paymentBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                            
                            font1.setColor(HSSFColor.GREEN.index);
                            cellH1.setCellValue(paymentBean.getStatus().toUpperCase());
                            cellStyle1.setFont(font1);
                            cellH1.setCellStyle(cellStyle1);
                            
                        } else if (paymentBean.getStatus().equalsIgnoreCase("ERROR")) {
                            
                            font2.setColor(HSSFColor.RED.index);
                            cellH1.setCellValue(paymentBean.getStatus().toUpperCase());
                            cellStyle2.setFont(font2);
                            cellH1.setCellStyle(cellStyle2);
                            
                        } else {
                            
                            font3.setColor(HSSFColor.ORANGE.index);
                            cellH1.setCellValue(paymentBean.getStatus().toUpperCase());
                            cellStyle3.setFont(font3);
                            cellH1.setCellStyle(cellStyle3);
                            
                        }
                        
                    }
                    HSSFCell cellI1 = row1.createCell((short) 9);
                    if (paymentBean.getAckStatus() != null) {
                        if (paymentBean.getAckStatus().equalsIgnoreCase("ACCEPTED")) {
                            font1.setColor(HSSFColor.GREEN.index);
                            cellI1.setCellValue(paymentBean.getAckStatus().toUpperCase());
                            cellStyle1.setFont(font1);
                            cellI1.setCellStyle(cellStyle1);
                            
                        } else if (paymentBean.getAckStatus().equalsIgnoreCase("REJECTED")) {
                            
                            font2.setColor(HSSFColor.RED.index);
                            cellI1.setCellValue(paymentBean.getAckStatus().toUpperCase());
                            cellStyle2.setFont(font2);
                            cellI1.setCellStyle(cellStyle2);
                            
                        } else {
                            
                            font3.setColor(HSSFColor.ORANGE.index);
                            cellI1.setCellValue(paymentBean.getAckStatus().toUpperCase());
                            cellStyle3.setFont(font3);
                            cellI1.setCellStyle(cellStyle3);
                            
                        }
                        
                    }
                }
            }
            worksheet.setColumnWidth(1, 5000);
            worksheet.setColumnWidth(2, 5000);
            worksheet.setColumnWidth(3, 6000);
            worksheet.setColumnWidth(4, 6000);
            worksheet.setColumnWidth(5, 5000);
            worksheet.setColumnWidth(6, 5000);
            worksheet.setColumnWidth(7, 5000);
            worksheet.setColumnWidth(8, 5000);
            worksheet.setColumnWidth(9, 5000);
            
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in paymentExcelDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in paymentExcelDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in paymentExcelDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String logisticsDocExcelDownload() {
        String filePath = "";
        try {
            java.util.List list = (java.util.List) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LOG_DOC_LIST);
            
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.logisticsDocCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + com.mss.ediscv.util.Properties.getProperty("os.compatability") + "logisticsDoc.xls");
            filePath = file.getAbsolutePath() + com.mss.ediscv.util.Properties.getProperty("os.compatability") + "logisticsDoc.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("logisticsDoc");
            HSSFRow row1;
            LogisticsDocBean logisticsDocBean = null;
            if (list.size() != 0) {
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                HSSFCellStyle cellStyle1 = workbook.createCellStyle();
                HSSFCellStyle cellStyle2 = workbook.createCellStyle();
                HSSFCellStyle cellStyle3 = workbook.createCellStyle();
                HSSFCellStyle cellStyleHead = workbook.createCellStyle();
                HSSFFont font1 = workbook.createFont();
                HSSFFont font2 = workbook.createFont();
                HSSFFont font3 = workbook.createFont();
                HSSFFont font4 = workbook.createFont();
                HSSFFont fontHead = workbook.createFont();
                fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                font4.setColor(HSSFColor.WHITE.index);
                cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
                cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cellStyle.setFont(font4);
                Date date = new Date();
                row1 = worksheet.createRow((short) 0);
                HSSFCell cellpo0 = row1.createCell((short) 0);
                HSSFCell cellpo1 = row1.createCell((short) 1);
                HSSFCell cellpo2 = row1.createCell((short) 2);
                HSSFCell cellpo3 = row1.createCell((short) 3);
                HSSFCell cellpo4 = row1.createCell((short) 4);
                HSSFCell cellpo5 = row1.createCell((short) 5);
                HSSFCell cellpo6 = row1.createCell((short) 6);
                HSSFCell cellpo7 = row1.createCell((short) 7);
                row1 = worksheet.createRow((short) 1);
                Cell cell = row1.createCell((short) 1);
                cell.setCellValue("Logistics Document :-Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
                cellStyleHead.setFont(fontHead);
                cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cellStyleHead.setFillForegroundColor(HSSFColor.YELLOW.index);
                cellStyleHead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cell.setCellStyle(cellStyleHead);
                worksheet.addMergedRegion(CellRangeAddress.valueOf("B2:I2"));
                
                row1 = worksheet.createRow((short) 3);
                
                HSSFCell cella1 = row1.createCell((short) 1);
                cella1.setCellValue("FileFormat");
                cella1.setCellStyle(cellStyle);
                
                HSSFCell cellb1 = row1.createCell((short) 2);
                cellb1.setCellValue("InstanceId");
                cellb1.setCellStyle(cellStyle);
                HSSFCell cellc1 = row1.createCell((short) 3);
                cellc1.setCellValue("Partner");
                cellc1.setCellStyle(cellStyle);
                HSSFCell celld1 = row1.createCell((short) 4);
                celld1.setCellValue("DateTime");
                celld1.setCellStyle(cellStyle);
                HSSFCell celle1 = row1.createCell((short) 5);
                celle1.setCellValue("TransType");
                celle1.setCellStyle(cellStyle);
                HSSFCell cellf1 = row1.createCell((short) 6);
                cellf1.setCellValue("Direction");
                cellf1.setCellStyle(cellStyle);
                HSSFCell cellg1 = row1.createCell((short) 7);
                cellg1.setCellValue("Status");
                cellg1.setCellStyle(cellStyle);
                HSSFCell cellh1 = row1.createCell((short) 8);
                cellh1.setCellValue("Shipment");
                cellh1.setCellStyle(cellStyle);
                for (int i = 0; i < list.size(); i++) {
                    logisticsDocBean = (LogisticsDocBean) list.get(i);
                    
                    row1 = worksheet.createRow((short) i + 4);
                    
                    HSSFCell cellA1 = row1.createCell((short) 1);
                    
                    cellA1.setCellValue(logisticsDocBean.getFile_type());
                    
                    HSSFCell cellB1 = row1.createCell((short) 2);
                    
                    cellB1.setCellValue(logisticsDocBean.getFile_id());
                    HSSFCell cellC1 = row1.createCell((short) 3);
                    
                    cellC1.setCellValue(logisticsDocBean.getPname());
                    
                    HSSFCell cellD1 = row1.createCell((short) 4);
                    
                    cellD1.setCellValue(logisticsDocBean.getDate_time_rec().toString().substring(0, logisticsDocBean.getDate_time_rec().toString().lastIndexOf(":")));
                    HSSFCell cellE1 = row1.createCell((short) 5);
                    
                    cellE1.setCellValue(logisticsDocBean.getTransaction_type());
                    
                    HSSFCell cellF1 = row1.createCell((short) 6);
                    cellF1.setCellValue(logisticsDocBean.getDirection());
                    HSSFCell cellG1 = row1.createCell((short) 7);
                    if (logisticsDocBean.getStatus() != null) {
                        if (logisticsDocBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                            font1.setColor(HSSFColor.GREEN.index);
                            cellG1.setCellValue(logisticsDocBean.getStatus().toUpperCase());
                            cellStyle1.setFont(font1);
                            cellG1.setCellStyle(cellStyle1);
                            
                        } else if (logisticsDocBean.getStatus().equalsIgnoreCase("ERROR")) {
                            font2.setColor(HSSFColor.RED.index);
                            cellG1.setCellValue(logisticsDocBean.getStatus().toUpperCase());
                            cellStyle2.setFont(font2);
                            cellG1.setCellStyle(cellStyle2);
                            
                        } else {
                            
                            font3.setColor(HSSFColor.ORANGE.index);
                            cellG1.setCellValue(logisticsDocBean.getStatus().toUpperCase());
                            cellStyle3.setFont(font3);
                            cellG1.setCellStyle(cellStyle3);
                            
                        }
                        
                    }
                    HSSFCell cellH1 = row1.createCell((short) 8);
                    cellH1.setCellValue(logisticsDocBean.getShipmentId());
                    
                }
            }
            worksheet.setColumnWidth(1, 5000);
            worksheet.setColumnWidth(2, 5000);
            worksheet.setColumnWidth(3, 6000);
            worksheet.setColumnWidth(4, 6000);
            worksheet.setColumnWidth(5, 5000);
            worksheet.setColumnWidth(6, 5000);
            worksheet.setColumnWidth(7, 5000);
            worksheet.setColumnWidth(8, 5000);
            
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in logisticsDocExcelDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in logisticsDocExcelDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in logisticsDocExcelDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String loadTenderingExcelDownload() {
        String filePath = "";
        try {
            java.util.List list = (java.util.List) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LOAD_LIST);
            
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.loadTenderingCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + com.mss.ediscv.util.Properties.getProperty("os.compatability") + "loadTendering.xls");
            filePath = file.getAbsolutePath() + com.mss.ediscv.util.Properties.getProperty("os.compatability") + "loadTendering.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("loadTendering");
            HSSFRow row1;
            LogisticsLoadBean logisticsLoadBean = null;
            if (list.size() != 0) {
                
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                HSSFCellStyle cellStyle1 = workbook.createCellStyle();
                HSSFCellStyle cellStyle2 = workbook.createCellStyle();
                HSSFCellStyle cellStyle3 = workbook.createCellStyle();
                HSSFCellStyle cellStyleHead = workbook.createCellStyle();
                HSSFFont font1 = workbook.createFont();
                HSSFFont font2 = workbook.createFont();
                HSSFFont font3 = workbook.createFont();
                HSSFFont font4 = workbook.createFont();
                HSSFFont fontHead = workbook.createFont();
                fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                font4.setColor(HSSFColor.WHITE.index);
                cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
                cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cellStyle.setFont(font4);
                Date date = new Date();
                row1 = worksheet.createRow((short) 0);
                HSSFCell cellpo0 = row1.createCell((short) 0);
                HSSFCell cellpo1 = row1.createCell((short) 1);
                HSSFCell cellpo2 = row1.createCell((short) 2);
                HSSFCell cellpo3 = row1.createCell((short) 3);
                HSSFCell cellpo4 = row1.createCell((short) 4);
                HSSFCell cellpo5 = row1.createCell((short) 5);
                HSSFCell cellpo6 = row1.createCell((short) 6);
                HSSFCell cellpo7 = row1.createCell((short) 7);
                HSSFCell cellpo8 = row1.createCell((short) 8);
                row1 = worksheet.createRow((short) 1);
                Cell cell = row1.createCell((short) 1);
                cell.setCellValue("LoadTendering :-Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
                cellStyleHead.setFont(fontHead);
                cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cellStyleHead.setFillForegroundColor(HSSFColor.YELLOW.index);
                cellStyleHead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cell.setCellStyle(cellStyleHead);
                worksheet.addMergedRegion(CellRangeAddress.valueOf("B2:J2"));
                
                row1 = worksheet.createRow((short) 3);
                
                HSSFCell cella1 = row1.createCell((short) 1);
                cella1.setCellValue("FileFormat");
                cella1.setCellStyle(cellStyle);
                
                HSSFCell cellb1 = row1.createCell((short) 2);
                cellb1.setCellValue("InstanceId");
                cellb1.setCellStyle(cellStyle);
                HSSFCell cellc1 = row1.createCell((short) 3);
                cellc1.setCellValue("Partner");
                cellc1.setCellStyle(cellStyle);
                HSSFCell celld1 = row1.createCell((short) 4);
                celld1.setCellValue("DateTime");
                celld1.setCellStyle(cellStyle);
                HSSFCell celle1 = row1.createCell((short) 5);
                celle1.setCellValue("TransType");
                celle1.setCellStyle(cellStyle);
                HSSFCell cellf1 = row1.createCell((short) 6);
                cellf1.setCellValue("Direction");
                cellf1.setCellStyle(cellStyle);
                HSSFCell cellg1 = row1.createCell((short) 7);
                cellg1.setCellValue("Status");
                cellg1.setCellStyle(cellStyle);
                HSSFCell cellh1 = row1.createCell((short) 8);
                cellh1.setCellValue("Shipment");
                cellh1.setCellStyle(cellStyle);
                HSSFCell celli1 = row1.createCell((short) 9);
                celli1.setCellValue("Repocess");
                celli1.setCellStyle(cellStyle);
                
                for (int i = 0; i < list.size(); i++) {
                    logisticsLoadBean = (LogisticsLoadBean) list.get(i);
                    
                    row1 = worksheet.createRow((short) i + 4);
                    
                    HSSFCell cellA1 = row1.createCell((short) 1);
                    
                    cellA1.setCellValue(logisticsLoadBean.getFile_type());
                    
                    HSSFCell cellB1 = row1.createCell((short) 2);
                    
                    cellB1.setCellValue(logisticsLoadBean.getFile_id());
                    HSSFCell cellC1 = row1.createCell((short) 3);
                    
                    cellC1.setCellValue(logisticsLoadBean.getPname());
                    
                    HSSFCell cellD1 = row1.createCell((short) 4);
                    
                    cellD1.setCellValue(logisticsLoadBean.getDate_time_rec().toString().substring(0, logisticsLoadBean.getDate_time_rec().toString().lastIndexOf(":")));
                    HSSFCell cellE1 = row1.createCell((short) 5);
                    
                    cellE1.setCellValue(logisticsLoadBean.getTransaction_type());
                    
                    HSSFCell cellF1 = row1.createCell((short) 6);
                    cellF1.setCellValue(logisticsLoadBean.getDirection());
                    
                    HSSFCell cellG1 = row1.createCell((short) 7);
                    if (logisticsLoadBean.getStatus() != null) {
                        if (logisticsLoadBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                            
                            font1.setColor(HSSFColor.GREEN.index);
                            cellG1.setCellValue(logisticsLoadBean.getStatus().toUpperCase());
                            cellStyle1.setFont(font1);
                            cellG1.setCellStyle(cellStyle1);
                            
                        } else if (logisticsLoadBean.getStatus().equalsIgnoreCase("ERROR")) {
                            
                            font2.setColor(HSSFColor.RED.index);
                            cellG1.setCellValue(logisticsLoadBean.getStatus().toUpperCase());
                            cellStyle2.setFont(font2);
                            cellG1.setCellStyle(cellStyle2);
                            
                        } else {
                            
                            font3.setColor(HSSFColor.ORANGE.index);
                            cellG1.setCellValue(logisticsLoadBean.getStatus().toUpperCase());
                            cellStyle3.setFont(font3);
                            cellG1.setCellStyle(cellStyle3);
                            
                        }
                        
                    }
                    HSSFCell cellH1 = row1.createCell((short) 8);
                    cellH1.setCellValue(logisticsLoadBean.getShipmentId());
                    HSSFCell cellI1 = row1.createCell((short) 9);
                    cellI1.setCellValue(logisticsLoadBean.getReProcessStatus());
                    
                }
            }
            worksheet.setColumnWidth(1, 5000);
            worksheet.setColumnWidth(2, 5000);
            worksheet.setColumnWidth(3, 6000);
            worksheet.setColumnWidth(4, 6000);
            worksheet.setColumnWidth(5, 5000);
            worksheet.setColumnWidth(6, 5000);
            worksheet.setColumnWidth(7, 5000);
            worksheet.setColumnWidth(8, 5000);
            worksheet.setColumnWidth(9, 5000);
            
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in loadTenderingExcelDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in loadTenderingExcelDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in loadTenderingExcelDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String ltResponseExcelDownload() {
        String filePath = "";
        try {
            java.util.List list = (java.util.List) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LTRESPONSE_LIST);
            
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.ltResponseCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + com.mss.ediscv.util.Properties.getProperty("os.compatability") + "ltResponse.xls");
            filePath = file.getAbsolutePath() + com.mss.ediscv.util.Properties.getProperty("os.compatability") + "ltResponse.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("ltResponse");
            HSSFRow row1;
            LtResponseBean ltResponseBean = null;
            
            if (list.size() != 0) {
                
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                HSSFCellStyle cellStyle1 = workbook.createCellStyle();
                HSSFCellStyle cellStyle2 = workbook.createCellStyle();
                HSSFCellStyle cellStyle3 = workbook.createCellStyle();
                HSSFCellStyle cellStyleHead = workbook.createCellStyle();
                HSSFFont font1 = workbook.createFont();
                HSSFFont font2 = workbook.createFont();
                HSSFFont font3 = workbook.createFont();
                HSSFFont font4 = workbook.createFont();
                HSSFFont fontHead = workbook.createFont();
                fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                font4.setColor(HSSFColor.WHITE.index);
                cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
                cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cellStyle.setFont(font4);
                
                Date date = new Date();
                row1 = worksheet.createRow((short) 0);
                HSSFCell cellpo0 = row1.createCell((short) 0);
                
                HSSFCell cellpo1 = row1.createCell((short) 1);
                HSSFCell cellpo2 = row1.createCell((short) 2);
                
                HSSFCell cellpo3 = row1.createCell((short) 3);
                
                HSSFCell cellpo4 = row1.createCell((short) 4);
                HSSFCell cellpo5 = row1.createCell((short) 5);
                HSSFCell cellpo6 = row1.createCell((short) 6);
                
                row1 = worksheet.createRow((short) 1);
                Cell cell = row1.createCell((short) 1);
                cell.setCellValue("LT Response :-Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
                cellStyleHead.setFont(fontHead);
                cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cellStyleHead.setFillForegroundColor(HSSFColor.YELLOW.index);
                cellStyleHead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cell.setCellStyle(cellStyleHead);
                worksheet.addMergedRegion(CellRangeAddress.valueOf("B2:H2"));
                
                row1 = worksheet.createRow((short) 3);
                
                HSSFCell cella1 = row1.createCell((short) 1);
                cella1.setCellValue("FileFormat");
                cella1.setCellStyle(cellStyle);
                
                HSSFCell cellb1 = row1.createCell((short) 2);
                cellb1.setCellValue("InstanceId");
                cellb1.setCellStyle(cellStyle);
                HSSFCell cellc1 = row1.createCell((short) 3);
                cellc1.setCellValue("Partner");
                cellc1.setCellStyle(cellStyle);
                HSSFCell celld1 = row1.createCell((short) 4);
                celld1.setCellValue("TransType");
                celld1.setCellStyle(cellStyle);
                HSSFCell celle1 = row1.createCell((short) 5);
                celle1.setCellValue("Direction");
                celle1.setCellStyle(cellStyle);
                HSSFCell cellf1 = row1.createCell((short) 6);
                cellf1.setCellValue("Status");
                cellf1.setCellStyle(cellStyle);
                HSSFCell cellg1 = row1.createCell((short) 7);
                cellg1.setCellValue("Shipment");
                cellg1.setCellStyle(cellStyle);
                
                for (int i = 0; i < list.size(); i++) {
                    ltResponseBean = (LtResponseBean) list.get(i);
                    
                    row1 = worksheet.createRow((short) i + 4);
                    
                    HSSFCell cellA1 = row1.createCell((short) 1);
                    
                    cellA1.setCellValue(ltResponseBean.getFileType());
                    
                    HSSFCell cellB1 = row1.createCell((short) 2);
                    
                    cellB1.setCellValue(ltResponseBean.getFileId());
                    HSSFCell cellC1 = row1.createCell((short) 3);
                    
                    cellC1.setCellValue(ltResponseBean.getPartnerName());
                    
                    HSSFCell cellD1 = row1.createCell((short) 4);
                    
                    cellD1.setCellValue(ltResponseBean.getTransType());
                    HSSFCell cellE1 = row1.createCell((short) 5);
                    
                    cellE1.setCellValue(ltResponseBean.getDirection());
                    
                    HSSFCell cellF1 = row1.createCell((short) 6);
                    if (ltResponseBean.getStatus() != null) {
                        if (ltResponseBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                            
                            font1.setColor(HSSFColor.GREEN.index);
                            cellF1.setCellValue(ltResponseBean.getStatus().toUpperCase());
                            cellStyle1.setFont(font1);
                            cellF1.setCellStyle(cellStyle1);
                            
                        } else if (ltResponseBean.getStatus().equalsIgnoreCase("ERROR")) {
                            
                            font2.setColor(HSSFColor.RED.index);
                            cellF1.setCellValue(ltResponseBean.getStatus().toUpperCase());
                            cellStyle2.setFont(font2);
                            cellF1.setCellStyle(cellStyle2);
                            
                        } else {
                            
                            font3.setColor(HSSFColor.ORANGE.index);
                            cellF1.setCellValue(ltResponseBean.getStatus().toUpperCase());
                            cellStyle3.setFont(font3);
                            cellF1.setCellStyle(cellStyle3);
                            
                        }
                    }
                    
                    HSSFCell cellH1 = row1.createCell((short) 7);
                    cellH1.setCellValue(ltResponseBean.getShipmentId());
                }
            }
            worksheet.setColumnWidth(1, 5000);
            worksheet.setColumnWidth(2, 5000);
            worksheet.setColumnWidth(3, 6000);
            worksheet.setColumnWidth(4, 6000);
            worksheet.setColumnWidth(5, 5000);
            worksheet.setColumnWidth(6, 5000);
            worksheet.setColumnWidth(7, 5000);
            
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in ltResponseExcelDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in ltResponseExcelDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in ltResponseExcelDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String ltShipmentExcelDownload() {
        String filePath = "";
        try {
            java.util.List list = (java.util.List) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LTSHIPMENT_LIST);
            
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.ltShipmentCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + com.mss.ediscv.util.Properties.getProperty("os.compatability") + "ltShipment.xls");
            filePath = file.getAbsolutePath() + com.mss.ediscv.util.Properties.getProperty("os.compatability") + "ltShipment.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("ltShipment");
            HSSFRow row1;
            LtShipmentBean ltShipmentBean = null;
            if (list.size() != 0) {
                
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                HSSFCellStyle cellStyle1 = workbook.createCellStyle();
                HSSFCellStyle cellStyle2 = workbook.createCellStyle();
                HSSFCellStyle cellStyle3 = workbook.createCellStyle();
                HSSFCellStyle cellStyleHead = workbook.createCellStyle();
                HSSFFont font1 = workbook.createFont();
                HSSFFont font2 = workbook.createFont();
                HSSFFont font3 = workbook.createFont();
                HSSFFont font4 = workbook.createFont();
                HSSFFont fontHead = workbook.createFont();
                fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                font4.setColor(HSSFColor.WHITE.index);
                
                cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
                cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cellStyle.setFont(font4);
                Date date = new Date();
                row1 = worksheet.createRow((short) 0);
                HSSFCell cellpo0 = row1.createCell((short) 0);
                HSSFCell cellpo1 = row1.createCell((short) 1);
                HSSFCell cellpo2 = row1.createCell((short) 2);
                HSSFCell cellpo3 = row1.createCell((short) 3);
                HSSFCell cellpo4 = row1.createCell((short) 4);
                HSSFCell cellpo5 = row1.createCell((short) 5);
                HSSFCell cellpo6 = row1.createCell((short) 6);
                row1 = worksheet.createRow((short) 1);
                Cell cell = row1.createCell((short) 1);
                cell.setCellValue("LT Shipment :-Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
                cellStyleHead.setFont(fontHead);
                cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cellStyleHead.setFillForegroundColor(HSSFColor.YELLOW.index);
                cellStyleHead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cell.setCellStyle(cellStyleHead);
                worksheet.addMergedRegion(CellRangeAddress.valueOf("B2:H2"));
                
                row1 = worksheet.createRow((short) 3);
                
                HSSFCell cella1 = row1.createCell((short) 1);
                cella1.setCellValue("InstanceId");
                cella1.setCellStyle(cellStyle);
                
                HSSFCell cellb1 = row1.createCell((short) 2);
                cellb1.setCellValue("ASN#");
                cellb1.setCellStyle(cellStyle);
                HSSFCell cellc1 = row1.createCell((short) 3);
                cellc1.setCellValue("Partner");
                cellc1.setCellStyle(cellStyle);
                HSSFCell celld1 = row1.createCell((short) 4);
                celld1.setCellValue("DateTime");
                celld1.setCellStyle(cellStyle);
                HSSFCell celle1 = row1.createCell((short) 5);
                celle1.setCellValue("Direction");
                celle1.setCellStyle(cellStyle);
                HSSFCell cellf1 = row1.createCell((short) 6);
                cellf1.setCellValue("Status");
                cellf1.setCellStyle(cellStyle);
                HSSFCell cellg1 = row1.createCell((short) 7);
                cellg1.setCellValue("CarrierStatus");
                cellg1.setCellStyle(cellStyle);
                
                for (int i = 0; i < list.size(); i++) {
                    ltShipmentBean = (LtShipmentBean) list.get(i);
                    
                    row1 = worksheet.createRow((short) i + 4);
                    
                    HSSFCell cellA1 = row1.createCell((short) 1);
                    
                    cellA1.setCellValue(ltShipmentBean.getInstanceId());
                    
                    HSSFCell cellB1 = row1.createCell((short) 2);
                    
                    cellB1.setCellValue(ltShipmentBean.getAsnNum());
                    HSSFCell cellC1 = row1.createCell((short) 3);
                    
                    cellC1.setCellValue(ltShipmentBean.getPartner());
                    
                    HSSFCell cellD1 = row1.createCell((short) 4);
                    
                    cellD1.setCellValue(ltShipmentBean.getDateTime());
                    HSSFCell cellE1 = row1.createCell((short) 5);
                    
                    cellE1.setCellValue(ltShipmentBean.getDirection());
                    
                    HSSFCell cellG1 = row1.createCell((short) 6);
                    cellG1.setCellValue(ltShipmentBean.getCarrierStatus());
                    
                    HSSFCell cellF1 = row1.createCell((short) 7);
                    if (ltShipmentBean.getStatus() != null) {
                        if (ltShipmentBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                            
                            font1.setColor(HSSFColor.GREEN.index);
                            cellF1.setCellValue(ltShipmentBean.getStatus().toUpperCase());
                            cellStyle1.setFont(font1);
                            cellF1.setCellStyle(cellStyle1);
                            
                        } else if (ltShipmentBean.getStatus().equalsIgnoreCase("ERROR")) {
                            
                            font2.setColor(HSSFColor.RED.index);
                            cellF1.setCellValue(ltShipmentBean.getStatus().toUpperCase());
                            cellStyle2.setFont(font2);
                            cellF1.setCellStyle(cellStyle2);
                            
                        } else {
                            font3.setColor(HSSFColor.ORANGE.index);
                            cellF1.setCellValue(ltShipmentBean.getStatus().toUpperCase());
                            cellStyle3.setFont(font3);
                            cellF1.setCellStyle(cellStyle3);
                            
                        }
                    }
                    
                }
            }
            worksheet.setColumnWidth(1, 5000);
            worksheet.setColumnWidth(2, 5000);
            worksheet.setColumnWidth(3, 6000);
            worksheet.setColumnWidth(4, 6000);
            worksheet.setColumnWidth(5, 5000);
            worksheet.setColumnWidth(6, 5000);
            worksheet.setColumnWidth(7, 5000);
            
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in ltShipmentExcelDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in ltShipmentExcelDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in ltShipmentExcelDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String ltInvoiceExcelDownload() {
        String filePath = "";
        try {
            java.util.List list = (java.util.List) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LTINVOICE_LIST);
            
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.ltInvoiceCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + com.mss.ediscv.util.Properties.getProperty("os.compatability") + "ltInvoice.xls");
            filePath = file.getAbsolutePath() + com.mss.ediscv.util.Properties.getProperty("os.compatability") + "ltInvoice.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("ltInvoice");
            HSSFRow row1;
            LogisticsInvoiceBean logisticsInvoiceBean = null;
            
            if (list.size() != 0) {
                
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                HSSFCellStyle cellStyle1 = workbook.createCellStyle();
                HSSFCellStyle cellStyle2 = workbook.createCellStyle();
                HSSFCellStyle cellStyle3 = workbook.createCellStyle();
                HSSFCellStyle cellStyleHead = workbook.createCellStyle();
                HSSFFont font1 = workbook.createFont();
                HSSFFont font2 = workbook.createFont();
                HSSFFont font3 = workbook.createFont();
                HSSFFont font4 = workbook.createFont();
                HSSFFont fontHead = workbook.createFont();
                fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                font4.setColor(HSSFColor.WHITE.index);
                
                cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
                cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cellStyle.setFont(font4);
                Date date = new Date();
                row1 = worksheet.createRow((short) 0);
                HSSFCell cellpo0 = row1.createCell((short) 0);
                HSSFCell cellpo1 = row1.createCell((short) 1);
                HSSFCell cellpo2 = row1.createCell((short) 2);
                HSSFCell cellpo3 = row1.createCell((short) 3);
                HSSFCell cellpo4 = row1.createCell((short) 4);
                HSSFCell cellpo5 = row1.createCell((short) 5);
                HSSFCell cellpo6 = row1.createCell((short) 6);
                HSSFCell cellpo7 = row1.createCell((short) 7);
                row1 = worksheet.createRow((short) 1);
                Cell cell = row1.createCell((short) 1);
                cell.setCellValue("LT Invoice :-Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
                cellStyleHead.setFont(fontHead);
                cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cellStyleHead.setFillForegroundColor(HSSFColor.YELLOW.index);
                cellStyleHead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cell.setCellStyle(cellStyleHead);
                worksheet.addMergedRegion(CellRangeAddress.valueOf("B2:I2"));
                
                row1 = worksheet.createRow((short) 3);
                
                HSSFCell cella1 = row1.createCell((short) 1);
                cella1.setCellValue("InstanceId");
                cella1.setCellStyle(cellStyle);
                
                HSSFCell cellb1 = row1.createCell((short) 2);
                cellb1.setCellValue("Partner");
                cellb1.setCellStyle(cellStyle);
                HSSFCell cellc1 = row1.createCell((short) 3);
                cellc1.setCellValue("Invoice#");
                cellc1.setCellStyle(cellStyle);
                HSSFCell celld1 = row1.createCell((short) 4);
                celld1.setCellValue("PO#");
                celld1.setCellStyle(cellStyle);
                HSSFCell celle1 = row1.createCell((short) 5);
                celle1.setCellValue("Item Qty");
                celle1.setCellStyle(cellStyle);
                HSSFCell cellf1 = row1.createCell((short) 6);
                cellf1.setCellValue("Inv Amount");
                cellf1.setCellStyle(cellStyle);
                HSSFCell cellg1 = row1.createCell((short) 7);
                cellg1.setCellValue("Inv Date");
                cellg1.setCellStyle(cellStyle);
                HSSFCell cellh1 = row1.createCell((short) 8);
                cellh1.setCellValue("Status");
                cellh1.setCellStyle(cellStyle);
                
                for (int i = 0; i < list.size(); i++) {
                    logisticsInvoiceBean = (LogisticsInvoiceBean) list.get(i);
                    
                    row1 = worksheet.createRow((short) i + 4);
                    
                    HSSFCell cellA1 = row1.createCell((short) 1);
                    
                    cellA1.setCellValue(logisticsInvoiceBean.getInstanceId());
                    
                    HSSFCell cellB1 = row1.createCell((short) 2);
                    
                    cellB1.setCellValue(logisticsInvoiceBean.getPartner());
                    HSSFCell cellC1 = row1.createCell((short) 3);
                    
                    cellC1.setCellValue(logisticsInvoiceBean.getInvoiceNumber());
                    
                    HSSFCell cellD1 = row1.createCell((short) 4);
                    
                    cellD1.setCellValue(logisticsInvoiceBean.getPoNumber());
                    HSSFCell cellE1 = row1.createCell((short) 5);
                    
                    cellE1.setCellValue(logisticsInvoiceBean.getItemQty());
                    
                    HSSFCell cellF1 = row1.createCell((short) 6);
                    cellF1.setCellValue(logisticsInvoiceBean.getInvAmount());
                    HSSFCell cellG1 = row1.createCell((short) 7);
                    cellG1.setCellValue(logisticsInvoiceBean.getInvDate());
                    HSSFCell cellH1 = row1.createCell((short) 8);
                    if (logisticsInvoiceBean.getStatus() != null) {
                        if (logisticsInvoiceBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                            
                            font1.setColor(HSSFColor.GREEN.index);
                            cellH1.setCellValue(logisticsInvoiceBean.getStatus().toUpperCase());
                            cellStyle1.setFont(font1);
                            cellH1.setCellStyle(cellStyle1);
                            
                        } else if (logisticsInvoiceBean.getStatus().equalsIgnoreCase("ERROR")) {
                            
                            font2.setColor(HSSFColor.RED.index);
                            cellH1.setCellValue(logisticsInvoiceBean.getStatus().toUpperCase());
                            cellStyle2.setFont(font2);
                            cellH1.setCellStyle(cellStyle2);
                            
                        } else {
                            
                            font3.setColor(HSSFColor.ORANGE.index);
                            cellH1.setCellValue(logisticsInvoiceBean.getStatus().toUpperCase());
                            cellStyle3.setFont(font3);
                            cellH1.setCellStyle(cellStyle3);
                        }
                    }
                }
            }
            worksheet.setColumnWidth(1, 5000);
            worksheet.setColumnWidth(2, 5000);
            worksheet.setColumnWidth(3, 6000);
            worksheet.setColumnWidth(4, 6000);
            worksheet.setColumnWidth(5, 5000);
            worksheet.setColumnWidth(6, 5000);
            worksheet.setColumnWidth(7, 5000);
            worksheet.setColumnWidth(8, 5000);
            
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in ltInvoiceExcelDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in ltInvoiceExcelDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in ltInvoiceExcelDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String docVisibilityExcelDownload() {
        String filePath = "";
        try {
            java.util.List list = (java.util.List) httpServletRequest.getSession(false).getAttribute("searchResult");
            
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.docCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + com.mss.ediscv.util.Properties.getProperty("os.compatability") + "DocVisibility.xls");
            filePath = file.getAbsolutePath() + com.mss.ediscv.util.Properties.getProperty("os.compatability") + "DocVisibility.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("DocVisibility");
            HSSFRow row1;
            DocumentVisibilityBean documentVisibilityBean = null;
            
            if (list.size() != 0) {
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                HSSFCellStyle cellStyle1 = workbook.createCellStyle();
                HSSFCellStyle cellStyle2 = workbook.createCellStyle();
                HSSFCellStyle cellStyle3 = workbook.createCellStyle();
                HSSFCellStyle cellStyleHead = workbook.createCellStyle();
                HSSFFont font1 = workbook.createFont();
                HSSFFont font2 = workbook.createFont();
                HSSFFont font3 = workbook.createFont();
                HSSFFont font4 = workbook.createFont();
                HSSFFont fontHead = workbook.createFont();
                fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                font4.setColor(HSSFColor.WHITE.index);
                
                cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
                cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cellStyle.setFont(font4);
                Date date = new Date();
                row1 = worksheet.createRow((short) 0);
                HSSFCell cellpo0 = row1.createCell((short) 0);
                HSSFCell cellpo1 = row1.createCell((short) 1);
                HSSFCell cellpo2 = row1.createCell((short) 2);
                HSSFCell cellpo3 = row1.createCell((short) 3);
                HSSFCell cellpo4 = row1.createCell((short) 4);
                HSSFCell cellpo5 = row1.createCell((short) 5);
                HSSFCell cellpo6 = row1.createCell((short) 6);
                HSSFCell cellpo7 = row1.createCell((short) 7);
                row1 = worksheet.createRow((short) 1);
                Cell cell = row1.createCell((short) 1);
                cell.setCellValue("Doc Repositry:-Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
                cellStyleHead.setFont(fontHead);
                cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cellStyleHead.setFillForegroundColor(HSSFColor.YELLOW.index);
                cellStyleHead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cell.setCellStyle(cellStyleHead);
                worksheet.addMergedRegion(CellRangeAddress.valueOf("B2:J2"));
                
                row1 = worksheet.createRow((short) 3);
                
                HSSFCell cella1 = row1.createCell((short) 1);
                cella1.setCellValue("Instance_Id");
                cella1.setCellStyle(cellStyle);
                
                HSSFCell cellb1 = row1.createCell((short) 2);
                cellb1.setCellValue("FileType");
                cellb1.setCellStyle(cellStyle);
                HSSFCell cellc1 = row1.createCell((short) 3);
                cellc1.setCellValue("Date Created");
                cellc1.setCellStyle(cellStyle);
                HSSFCell celld1 = row1.createCell((short) 4);
                celld1.setCellValue("TransType");
                celld1.setCellStyle(cellStyle);
                HSSFCell celle1 = row1.createCell((short) 5);
                celle1.setCellValue("Sender Id");
                celle1.setCellStyle(cellStyle);
                HSSFCell cellf1 = row1.createCell((short) 6);
                cellf1.setCellValue("Receiver Id");
                cellf1.setCellStyle(cellStyle);
                HSSFCell cellg1 = row1.createCell((short) 7);
                cellg1.setCellValue("IC #");
                cellg1.setCellStyle(cellStyle);
                HSSFCell cellh1 = row1.createCell((short) 8);
                cellh1.setCellValue("FC #");
                cellh1.setCellStyle(cellStyle);
                HSSFCell celli1 = row1.createCell((short) 9);
                celli1.setCellValue("MC #");
                celli1.setCellStyle(cellStyle);
                
                for (int i = 0; i < list.size(); i++) {
                    documentVisibilityBean = (DocumentVisibilityBean) list.get(i);
                    
                    row1 = worksheet.createRow((short) i + 4);
                    
                    HSSFCell cellA1 = row1.createCell((short) 1);
                    
                    cellA1.setCellValue(documentVisibilityBean.getInstanceId());
                    
                    HSSFCell cellB1 = row1.createCell((short) 2);
                    
                    cellB1.setCellValue(documentVisibilityBean.getFile_type());
                    HSSFCell cellC1 = row1.createCell((short) 3);
                    
                    cellC1.setCellValue(documentVisibilityBean.getDate_time_rec().toString().substring(0, documentVisibilityBean.getDate_time_rec().toString().lastIndexOf(":")));
                    
                    HSSFCell cellD1 = row1.createCell((short) 4);
                    
                    cellD1.setCellValue(documentVisibilityBean.getTransaction_type());
                    HSSFCell cellE1 = row1.createCell((short) 5);
                    
                    cellE1.setCellValue(documentVisibilityBean.getSenderId());
                    
                    HSSFCell cellF1 = row1.createCell((short) 6);
                    cellF1.setCellValue(documentVisibilityBean.getReceiverId());
                    
                    HSSFCell cellG1 = row1.createCell((short) 7);
                    cellG1.setCellValue(documentVisibilityBean.getInterchange_ControlNo());
                    HSSFCell cellH1 = row1.createCell((short) 8);
                    cellH1.setCellValue(documentVisibilityBean.getFunctional_ControlNo());
                    
                    HSSFCell cellI1 = row1.createCell((short) 9);
                    cellI1.setCellValue(documentVisibilityBean.getMessage_ControlNo());
                }
            }
            worksheet.setColumnWidth(1, 5000);
            worksheet.setColumnWidth(2, 5000);
            worksheet.setColumnWidth(3, 6000);
            worksheet.setColumnWidth(4, 6000);
            worksheet.setColumnWidth(5, 5000);
            worksheet.setColumnWidth(6, 5000);
            worksheet.setColumnWidth(7, 5000);
            worksheet.setColumnWidth(8, 7000);
            worksheet.setColumnWidth(9, 7000);
            
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in docVisibilityExcelDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in docVisibilityExcelDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in docVisibilityExcelDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String docReportExcelDownload() {
        String filePath = "";
        try {
            java.util.List list = (java.util.List) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_DOCREPORT_LIST);
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.docCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + File.separator + "ManufacturingExcelReport.xls");
            filePath = file.getAbsolutePath() + File.separator + "ManufacturingExcelReport.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("DocRepository");
            HSSFRow row1;
            ReportsBean docRepositoryBean = null;
            
            if (list.size() != 0) {
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                HSSFCellStyle cellStyle1 = workbook.createCellStyle();
                HSSFCellStyle cellStyle2 = workbook.createCellStyle();
                HSSFCellStyle cellStyle3 = workbook.createCellStyle();
                HSSFCellStyle cellStyleHead = workbook.createCellStyle();
                HSSFFont font1 = workbook.createFont();
                HSSFFont font2 = workbook.createFont();
                HSSFFont font3 = workbook.createFont();
                HSSFFont font4 = workbook.createFont();
                HSSFFont fontHead = workbook.createFont();
                fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                font4.setColor(HSSFColor.WHITE.index);
                
                cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
                cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cellStyle.setFont(font4);
                Date date = new Date();
                row1 = worksheet.createRow((short) 0);
                HSSFCell cellpo0 = row1.createCell((short) 0);
                HSSFCell cellpo1 = row1.createCell((short) 1);
                HSSFCell cellpo2 = row1.createCell((short) 2);
                HSSFCell cellpo3 = row1.createCell((short) 3);
                HSSFCell cellpo4 = row1.createCell((short) 4);
                HSSFCell cellpo5 = row1.createCell((short) 5);
                HSSFCell cellpo6 = row1.createCell((short) 6);
                HSSFCell cellpo7 = row1.createCell((short) 7);
                HSSFCell cellpo8 = row1.createCell((short) 8);
                row1 = worksheet.createRow((short) 1);
                Cell cell = row1.createCell((short) 1);
                cell.setCellValue("Manufacturing Excel Reports:-Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
                cellStyleHead.setFont(fontHead);
                cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cellStyleHead.setFillForegroundColor(HSSFColor.YELLOW.index);
                cellStyleHead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cell.setCellStyle(cellStyleHead);
                worksheet.addMergedRegion(CellRangeAddress.valueOf("B2:J2"));
                row1 = worksheet.createRow((short) 3);
                
                HSSFCell cella1 = row1.createCell((short) 1);
                cella1.setCellValue("Instance_Id");
                cella1.setCellStyle(cellStyle);
                HSSFCell cellb1 = row1.createCell((short) 2);
                cellb1.setCellValue("FileFormat");
                cellb1.setCellStyle(cellStyle);
                HSSFCell cellc1 = row1.createCell((short) 3);
                cellc1.setCellValue("Partner");
                cellc1.setCellStyle(cellStyle);
                HSSFCell celld1 = row1.createCell((short) 4);
                celld1.setCellValue("DateTime");
                celld1.setCellStyle(cellStyle);
                HSSFCell celle1 = row1.createCell((short) 5);
                celle1.setCellValue("Trans_Type");
                celle1.setCellStyle(cellStyle);
                HSSFCell cellf1 = row1.createCell((short) 6);
                cellf1.setCellValue("Direction");
                cellf1.setCellStyle(cellStyle);
                HSSFCell cellg1 = row1.createCell((short) 7);
                cellg1.setCellValue("Status");
                cellg1.setCellStyle(cellStyle);
                HSSFCell cellh1 = row1.createCell((short) 8);
                cellh1.setCellValue("Reprocess");
                cellh1.setCellStyle(cellStyle);
                
                HSSFCell celli1 = row1.createCell((short) 9);
                celli1.setCellValue("ErrorMessage");
                celli1.setCellStyle(cellStyle);
                
                for (int i = 0; i < list.size(); i++) {
                    docRepositoryBean = (ReportsBean) list.get(i);
                    
                    row1 = worksheet.createRow((short) i + 4);
                    
                    HSSFCell cellA1 = row1.createCell((short) 1);
                    
                    cellA1.setCellValue(docRepositoryBean.getFile_id());
                    
                    HSSFCell cellB1 = row1.createCell((short) 2);
                    
                    cellB1.setCellValue(docRepositoryBean.getFile_origin());
                    HSSFCell cellC1 = row1.createCell((short) 3);
                    
                    cellC1.setCellValue(docRepositoryBean.getPname());
                    
                    HSSFCell cellD1 = row1.createCell((short) 4);
                    
                    cellD1.setCellValue(docRepositoryBean.getDate_time_rec().toString().substring(0, docRepositoryBean.getDate_time_rec().toString().lastIndexOf(":")));
                    HSSFCell cellE1 = row1.createCell((short) 5);
                    
                    cellE1.setCellValue(docRepositoryBean.getTransaction_type());
                    
                    HSSFCell cellF1 = row1.createCell((short) 6);
                    
                    cellF1.setCellValue(docRepositoryBean.getDirection());
                    
                    HSSFCell cellG1 = row1.createCell((short) 7);
                    
                    if (docRepositoryBean.getStatus() != null) {
                        if (docRepositoryBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                            
                            font1.setColor(HSSFColor.GREEN.index);
                            cellG1.setCellValue(docRepositoryBean.getStatus().toUpperCase());
                            cellStyle1.setFont(font1);
                            cellG1.setCellStyle(cellStyle1);
                            
                        } else if (docRepositoryBean.getStatus().equalsIgnoreCase("ERROR")) {
                            
                            font2.setColor(HSSFColor.RED.index);
                            cellG1.setCellValue(docRepositoryBean.getStatus().toUpperCase());
                            cellStyle2.setFont(font2);
                            cellG1.setCellStyle(cellStyle2);
                            
                        } else {
                            
                            font3.setColor(HSSFColor.ORANGE.index);
                            cellG1.setCellValue(docRepositoryBean.getStatus().toUpperCase());
                            cellStyle3.setFont(font3);
                            cellG1.setCellStyle(cellStyle3);
                        }
                    }
                    
                    HSSFCell cellH1 = row1.createCell((short) 8);
                    
                    cellH1.setCellValue(docRepositoryBean.getReProcessStatus());
                    
                    HSSFCell cellI1 = row1.createCell((short) 9);
                    
                    cellI1.setCellValue(docRepositoryBean.getErrorMessage());
                }
            }
            
            worksheet.setColumnWidth(1, 5000);
            worksheet.setColumnWidth(2, 5000);
            worksheet.setColumnWidth(3, 6000);
            worksheet.setColumnWidth(4, 6000);
            worksheet.setColumnWidth(5, 5000);
            worksheet.setColumnWidth(6, 5000);
            worksheet.setColumnWidth(7, 5000);
            worksheet.setColumnWidth(8, 5000);
            worksheet.setColumnWidth(9, 5000);
            
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in docReportExcelDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in docReportExcelDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in docReportExcelDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String dashVisibilityExcelDownload(String inbound, String outbound) {
        String filePath = "";
        try {
            StringTokenizer inboundst = new StringTokenizer(inbound, "@");
            StringTokenizer outboundst = new StringTokenizer(outbound, "@");
            List inbounddata = new ArrayList();
            String inboundvalue = null;
            String outboundvalue = null;
            while (inboundst.hasMoreTokens()) {
                inboundvalue = inboundst.nextToken();
                StringTokenizer inboundst1 = new StringTokenizer(inboundvalue, "$");
                while (inboundst1.hasMoreTokens()) {
                    inbounddata.add(inboundst1.nextToken());
                }
            }
            
            List outbounddata = new ArrayList();
            while (outboundst.hasMoreTokens()) {
                outboundvalue = outboundst.nextToken();
                StringTokenizer outboundst1 = new StringTokenizer(outboundvalue, "$");
                while (outboundst1.hasMoreTokens()) {
                    outbounddata.add(outboundst1.nextToken());
                }
            }
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.docCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + File.separator + "DashboardReport.xls");
            filePath = file.getAbsolutePath() + File.separator + "DashboardReport.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet my_sheet = workbook.createSheet("DashboardPiechartReport");
            HSSFSheet worksheet = workbook.createSheet("DashboardReportData");
            HSSFRow row1;
            DefaultPieDataset inbound_chart_data = new DefaultPieDataset();
            DefaultPieDataset outbound_chart_data = new DefaultPieDataset();
            String Inboundvalue = null;
            Number Inboundvalue2 = 0;
            String Inboundvalue1 = null;
            String Outboundvalue = null;
            String Outboundvalue1 = null;
            Number Outboundvalue2 = 0;
            Date date = new Date();
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            HSSFCellStyle cellStyleHead = workbook.createCellStyle();
            HSSFFont font4 = workbook.createFont();
            HSSFFont fontHead = workbook.createFont();
            fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font4.setColor(HSSFColor.WHITE.index);
            cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
            cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            cellStyle.setFont(font4);
            if (inbounddata.size() != 0 || outbounddata.size() != 0) {
                row1 = worksheet.createRow((short) 0);
                row1 = worksheet.createRow((short) 1);
                Cell cell = row1.createCell((short) 1);
                cell.setCellValue("Dashboard Reports:-Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
                cellStyleHead.setFont(fontHead);
                cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cellStyleHead.setFillForegroundColor(HSSFColor.YELLOW.index);
                cellStyleHead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cell.setCellStyle(cellStyleHead);
                worksheet.addMergedRegion(CellRangeAddress.valueOf("B2:E2"));
                int j = 0;
                row1 = worksheet.createRow((short) j + 3);
                if (inbounddata.size() != 0) {
                    HSSFCell cell1 = row1.createCell((short) 1);
                    cell1.setCellValue("PartnerName");
                    HSSFCell cell2 = row1.createCell((short) 2);
                    cell2.setCellValue("INBOUND DOC COUNT");
                }
                if (outbounddata.size() != 0) {
                    if (inbounddata.size() != 0) {
                        HSSFCell cell3 = row1.createCell((short) 3);
                        cell3.setCellValue("PartnerName");
                        HSSFCell cell4 = row1.createCell((short) 4);
                        cell4.setCellValue("OUTBOUND DOC COUNT");
                    } else {
                        HSSFCell cell3 = row1.createCell((short) 1);
                        cell3.setCellValue("PartnerName");
                        HSSFCell cell4 = row1.createCell((short) 2);
                        cell4.setCellValue("OUTBOUND");
                    }
                }
                for (int i = 0; i < inbounddata.size();) {
                    Inboundvalue = String.valueOf(inbounddata.get(i));
                    Inboundvalue1 = String.valueOf(inbounddata.get(i + 1));
                    row1 = worksheet.createRow((short) j + 4);
                    Inboundvalue2 = (Number) Integer.parseInt(Inboundvalue1);
                    HSSFCell cellA1 = row1.createCell((short) 1);
                    cellA1.setCellValue(Inboundvalue);
                    HSSFCell cellA2 = row1.createCell((short) 2);
                    cellA2.setCellValue(Inboundvalue1);
                    i = i + 2;
                    j = j + 1;
                    inbound_chart_data.setValue(Inboundvalue, Inboundvalue2);
                }
                
                j = 0;
                for (int i = 0; i < outbounddata.size();) {
                    Outboundvalue = String.valueOf(outbounddata.get(i));
                    Outboundvalue1 = String.valueOf(outbounddata.get(i + 1));
                    Outboundvalue2 = (Number) Integer.parseInt(Outboundvalue1);
                    if (inbounddata.size() != 0 && i < inbounddata.size()) {
                        row1 = worksheet.getRow((short) j + 4);
                        HSSFCell cellA1 = row1.createCell((short) 3);
                        cellA1.setCellValue(Outboundvalue);
                        HSSFCell cellA2 = row1.createCell((short) 4);
                        cellA2.setCellValue(Outboundvalue1);
                    } else {
                        row1 = worksheet.createRow((short) j + 4);
                        if (inbounddata.size() != 0) {
                            HSSFCell cellA1 = row1.createCell((short) 3);
                            cellA1.setCellValue(Outboundvalue);
                            HSSFCell cellA2 = row1.createCell((short) 4);
                            cellA2.setCellValue(Outboundvalue1);
                        } else {
                            HSSFCell cellA1 = row1.createCell((short) 1);
                            cellA1.setCellValue(Outboundvalue);
                            HSSFCell cellA2 = row1.createCell((short) 2);
                            cellA2.setCellValue(Outboundvalue1);
                        }
                    }
                    i = i + 2;
                    j = j + 1;
                    outbound_chart_data.setValue(Outboundvalue, Outboundvalue2);
                }
                
                worksheet.autoSizeColumn((short) 0);
                worksheet.autoSizeColumn((short) 1);
                worksheet.autoSizeColumn((short) 2);
                worksheet.autoSizeColumn((short) 3);
                worksheet.autoSizeColumn((short) 4);
                worksheet.autoSizeColumn((short) 5);
                worksheet.autoSizeColumn((short) 6);
                worksheet.autoSizeColumn((short) 7);
                worksheet.autoSizeColumn((short) 8);
                
            }
            int width = 480;
            
            int height = 350;
            
            float quality = 1;
            
            Drawing drawing = my_sheet.createDrawingPatriarch();
            Row row = my_sheet.createRow((short) 1);
            Cell cell = row.createCell((short) 5);
            cell.setCellValue("Dashboard Pie Charts Reports:-Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
            cellStyleHead.setFont(fontHead);
            cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell.setCellStyle(cellStyleHead);
            my_sheet.addMergedRegion(CellRangeAddress.valueOf("F2:N2"));
            if (inbounddata.size() != 0) {
                JFreeChart inboundPieChart = ChartFactory.createPieChart("Partner Inbound Transactions", inbound_chart_data, true, true, false);
                ByteArrayOutputStream inbound_out = new ByteArrayOutputStream();
                ChartUtilities.writeChartAsJPEG(inbound_out, quality, inboundPieChart, width, height);
                int inbound_picture_id = workbook.addPicture(inbound_out.toByteArray(), workbook.PICTURE_TYPE_JPEG);
                inbound_out.close();
                ClientAnchor inbound_anchor = new HSSFClientAnchor();
                inbound_anchor.setCol1(2);
                inbound_anchor.setRow1(5);
                Picture inbound_picture = drawing.createPicture(inbound_anchor, inbound_picture_id);
                inbound_picture.resize();
            }
            if (outbounddata.size() != 0) {
                ByteArrayOutputStream outbound_out = new ByteArrayOutputStream();
                JFreeChart outboundPieChart = ChartFactory.createPieChart("Partner Outbound Transactions", outbound_chart_data, true, true, false);
                ChartUtilities.writeChartAsJPEG(outbound_out, quality, outboundPieChart, width, height);
                int outbound_picture_id = workbook.addPicture(outbound_out.toByteArray(), workbook.PICTURE_TYPE_JPEG);
                outbound_out.close();
                ClientAnchor outbound_anchor = new HSSFClientAnchor();
                if (inbounddata.size() != 0) {
                    outbound_anchor.setCol1(10);
                    outbound_anchor.setRow1(5);
                } else {
                    outbound_anchor.setCol1(2);
                    outbound_anchor.setRow1(5);
                }
                Picture outbound_picture = drawing.createPicture(outbound_anchor, outbound_picture_id);
                outbound_picture.resize();
            }
            worksheet.setColumnWidth(1, 5000);
            worksheet.setColumnWidth(2, 5000);
            worksheet.setColumnWidth(3, 6000);
            worksheet.setColumnWidth(4, 6000);
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in dashVisibilityExcelDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in dashVisibilityExcelDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in dashVisibilityExcelDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String dashVisibilityPdfDownload(String inbound, String outbound) {
        String filePath = "";
        try {
            StringTokenizer inboundst = new StringTokenizer(inbound, "@");
            StringTokenizer outboundst = new StringTokenizer(outbound, "@");
            List inbounddata = new ArrayList();
            String inboundvalue = null;
            String outboundvalue = null;
            while (inboundst.hasMoreTokens()) {
                inboundvalue = inboundst.nextToken();
                StringTokenizer inboundst1 = new StringTokenizer(inboundvalue, "$");
                while (inboundst1.hasMoreTokens()) {
                    inbounddata.add(inboundst1.nextToken());
                }
            }
            List outbounddata = new ArrayList();
            while (outboundst.hasMoreTokens()) {
                outboundvalue = outboundst.nextToken();
                StringTokenizer outboundst1 = new StringTokenizer(outboundvalue, "$");
                while (outboundst1.hasMoreTokens()) {
                    outbounddata.add(outboundst1.nextToken());
                }
            }
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.docCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + File.separator + "DashboardReport.pdf");
            filePath = file.getAbsolutePath() + File.separator + "DashboardReport.pdf";
            DefaultPieDataset inbound_chart_data = new DefaultPieDataset();
            DefaultPieDataset outbound_chart_data = new DefaultPieDataset();
            String Inboundvalue = null;
            Number Inboundvalue2 = 0;
            String Inboundvalue1 = null;
            String Outboundvalue = null;
            String Outboundvalue1 = null;
            Number Outboundvalue2 = 0;
            PdfPTable inboundtable = new PdfPTable(2);
            PdfPTable outboundtable = new PdfPTable(2);
            inboundtable.setWidthPercentage(50.00f);
            outboundtable.setWidthPercentage(50.00f);
            if (inbounddata.size() != 0 || outbounddata.size() != 0) {
                inboundtable.addCell("PartnerName");
                inboundtable.addCell("INBOUND DOC COUNT");
                for (int i = 0; i < inbounddata.size();) {
                    Inboundvalue = String.valueOf(inbounddata.get(i));
                    Inboundvalue1 = String.valueOf(inbounddata.get(i + 1));
                    Inboundvalue2 = (Number) Integer.parseInt(Inboundvalue1);
                    i = i + 2;
                    inbound_chart_data.setValue(Inboundvalue, Inboundvalue2);
                    inboundtable.addCell(Inboundvalue);
                    inboundtable.addCell(Inboundvalue1);
                    inboundtable.completeRow();
                }
                
                outboundtable.addCell("PartnerName");
                outboundtable.addCell("OUTBOUND DOC COUNT");
                for (int i = 0; i < outbounddata.size();) {
                    Outboundvalue = String.valueOf(outbounddata.get(i));
                    Outboundvalue1 = String.valueOf(outbounddata.get(i + 1));
                    Outboundvalue2 = (Number) Integer.parseInt(Outboundvalue1);
                    i = i + 2;
                    outbound_chart_data.setValue(Outboundvalue, Outboundvalue2);
                    outboundtable.addCell(Outboundvalue);
                    outboundtable.addCell(Outboundvalue1);
                    outboundtable.completeRow();
                }
                
            }
            int width = 380;
            
            int height = 400;
            
            Date date = new Date();
            PdfWriter writer = null;
            Document document = new Document(PageSize.A3);
            writer = PdfWriter.getInstance(document, fileOut);
            document.open();
            Paragraph par = new Paragraph("Dashboard Pie Reports:-Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1(), FontFactory.getFont("Arial", 26f));
            par.setAlignment(Paragraph.ALIGN_CENTER);
            
            document.add(par);
            
            if (inbounddata.size() != 0) {
                
                JFreeChart inboundPieChart = ChartFactory.createPieChart("Partner Inbound Transactions", inbound_chart_data, true, true, false);
                PdfContentByte contentByte = writer.getDirectContent();
                PdfTemplate template = contentByte.createTemplate(width, height);
                Graphics2D graphics2d = template.createGraphics(width, height,
                        new DefaultFontMapper());
                Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, width,
                        height);
                
                inboundPieChart.draw(graphics2d, rectangle2d);
                
                graphics2d.dispose();
                if (outbounddata.size() != 0) {
                    contentByte.addTemplate(template, 30, 630);
                } else {
                    contentByte.addTemplate(template, 210, 630);
                }
            }
            if (outbounddata.size() != 0) {
                JFreeChart outboundPieChart = ChartFactory.createPieChart("Partner Outbound Transactions", outbound_chart_data, true, true, false);
                PdfContentByte contentByte = writer.getDirectContent();
                PdfTemplate template = contentByte.createTemplate(width, height);
                Graphics2D graphics2d = template.createGraphics(width, height,
                        new DefaultFontMapper());
                Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, width,
                        height);
                
                outboundPieChart.draw(graphics2d, rectangle2d);
                
                graphics2d.dispose();
                if (inbounddata.size() != 0) {
                    contentByte.addTemplate(template, 430, 630);
                } else {
                    contentByte.addTemplate(template, 210, 630);
                }
            }
            
            document.newPage();
            Paragraph par1 = new Paragraph("Dashboard Inbound and outbound Reports ", FontFactory.getFont("Arial", 26f));
            par1.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(par1);
            if (inbounddata.size() != 0) {
                Paragraph par2 = new Paragraph("INBOUND REPORTS ", FontFactory.getFont("Arial", 18f));
                par2.setSpacingBefore(20.00f);
                document.add(par2);
                inboundtable.setSpacingBefore(20.00f);
                inboundtable.setHorizontalAlignment(inboundtable.ALIGN_LEFT);
                document.add(inboundtable);
            }
            if (outbounddata.size() != 0) {
                Paragraph par3 = new Paragraph("OUTBOUND REPORTS ", FontFactory.getFont("Arial", 18f));
                par3.setSpacingBefore(20.00f);
                document.add(par3);
                outboundtable.setSpacingBefore(20.00f);
                outboundtable.setHorizontalAlignment(outboundtable.ALIGN_LEFT);
                document.add(outboundtable);
            }
            document.close();
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in dashVisibilityPdfDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in dashVisibilityPdfDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in dashVisibilityPdfDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String docReportTrackingSummaryExcelDownload() {
        String filePath = "";
        try {
            java.util.List list = (java.util.List) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_DOCREPORT_LIST);
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.docCreationPath"));
            int inboundTotal = 0;
            int outboundTotal = 0;
            double filesizeTotal = 0;
            int total = 0;
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + File.separator + "EDITrackingSummaryReport.xls");
            filePath = file.getAbsolutePath() + File.separator + "EDITrackingSummaryReport.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("EDITrackingSummaryReport");
            HSSFRow row1;
            TrackInOutBean trackInOutBean = null;
            
            if (list.size() != 0) {
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                
                HSSFCellStyle cellStyleHead = workbook.createCellStyle();
                
                HSSFFont font4 = workbook.createFont();
                HSSFFont fontHead = workbook.createFont();
                fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                font4.setColor(HSSFColor.WHITE.index);
                
                cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
                cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cellStyle.setFont(font4);
                Date date = new Date();
                
                row1 = worksheet.createRow((short) 1);
                Cell cell = row1.createCell((short) 0);
                cell.setCellValue("EDI Tracking Summary:-Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
                cellStyleHead.setFont(fontHead);
                cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cellStyleHead.setFillForegroundColor(HSSFColor.YELLOW.index);
                cellStyleHead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cell.setCellStyle(cellStyleHead);
                worksheet.addMergedRegion(CellRangeAddress.valueOf("A2:F2"));
                row1 = worksheet.createRow((short) 3);
                
                HSSFCell cella1 = row1.createCell((short) 0);
                cella1.setCellValue("TRANSACTION TYPE");
                cella1.setCellStyle(cellStyle);
                HSSFCell cellb1 = row1.createCell((short) 1);
                cellb1.setCellValue("Partner");
                cellb1.setCellStyle(cellStyle);
                HSSFCell cellc1 = row1.createCell((short) 2);
                cellc1.setCellValue("FILE_SIZE");
                cellc1.setCellStyle(cellStyle);
                HSSFCell celld1 = row1.createCell((short) 3);
                celld1.setCellValue("IN");
                celld1.setCellStyle(cellStyle);
                HSSFCell celle1 = row1.createCell((short) 4);
                celle1.setCellValue("OUT");
                celle1.setCellStyle(cellStyle);
                HSSFCell cellf1 = row1.createCell((short) 5);
                cellf1.setCellValue("Total");
                cellf1.setCellStyle(cellStyle);
                int j = 0;
                String trans_type = "";
                for (int i = 0; i < list.size(); i++) {
                    trackInOutBean = (TrackInOutBean) list.get(i);
                    row1 = worksheet.createRow((short) i + 5);
                    
                    if (trackInOutBean.getTransaction_type() != null && !"".equalsIgnoreCase(trackInOutBean.getTransaction_type())) {
                        trans_type = trackInOutBean.getTransaction_type();
                    }
                    HSSFCell cellB1 = row1.createCell((short) 0);
                    
                    cellB1.setCellValue(trackInOutBean.getPname());
                    
                    HSSFCell cellC1 = row1.createCell((short) 1);
                    
                    cellC1.setCellValue(trackInOutBean.getFilesizeTotal());
                    
                    HSSFCell cellD1 = row1.createCell((short) 2);
                    
                    cellD1.setCellValue(trackInOutBean.getInbound());
                    
                    HSSFCell cellE1 = row1.createCell((short) 3);
                    
                    cellE1.setCellValue(trackInOutBean.getOutbound());
                    HSSFCell cellF1 = row1.createCell((short) 4);
                    cellF1.setCellValue(trackInOutBean.getTotal());
                    if (trackInOutBean.getPname().equalsIgnoreCase("Total")) {
                        row1 = worksheet.getRow((short) j + 5);
                        HSSFCell cellA1 = row1.createCell((short) 0);
                        cellA1.setCellValue(trans_type);
                        worksheet.addMergedRegion(new CellRangeAddress(j + 5, i + 5, 0, 0));
                        j = i + 1;
                        inboundTotal = inboundTotal + trackInOutBean.getInbound();
                        outboundTotal = outboundTotal + trackInOutBean.getOutbound();
                        filesizeTotal = filesizeTotal + trackInOutBean.getFilesizeTotal();
                        total = total + trackInOutBean.getTotal();
                    }
                    
                }
                row1 = worksheet.createRow((short) list.size() + 5);
                
                HSSFCell cellA1 = row1.createCell((short) 0);
                cellA1.setCellValue("Total");
                HSSFCell cellB1 = row1.createCell((short) 1);
                cellB1.setCellValue("");
                HSSFCell cellC1 = row1.createCell((short) 2);
                cellC1.setCellValue(filesizeTotal);
                HSSFCell cellD1 = row1.createCell((short) 3);
                cellD1.setCellValue(inboundTotal);
                HSSFCell cellE1 = row1.createCell((short) 4);
                cellE1.setCellValue(outboundTotal);
                HSSFCell cellF1 = row1.createCell((short) 5);
                cellF1.setCellValue(total);
                worksheet.addMergedRegion(new CellRangeAddress(list.size() + 5, list.size() + 5, 0, 1));
                
                worksheet.autoSizeColumn((short) 0);
                worksheet.autoSizeColumn((short) 1);
                worksheet.autoSizeColumn((short) 2);
                worksheet.autoSizeColumn((short) 3);
                worksheet.autoSizeColumn((short) 4);
                worksheet.autoSizeColumn((short) 5);
                worksheet.autoSizeColumn((short) 6);
                worksheet.autoSizeColumn((short) 7);
                worksheet.autoSizeColumn((short) 8);
                worksheet.autoSizeColumn((short) 9);
                
            }
            worksheet.setColumnWidth(0, 6000);
            worksheet.setColumnWidth(1, 5000);
            worksheet.setColumnWidth(2, 5000);
            worksheet.setColumnWidth(3, 6000);
            worksheet.setColumnWidth(4, 6000);
            worksheet.setColumnWidth(5, 5000);
            
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in docReportTrackingSummaryExcelDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in docReportTrackingSummaryExcelDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in docReportTrackingSummaryExcelDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String docReportTrackingInOutExcelDownload() {
        String filePath = "";
        try {
            java.util.List list = (java.util.List) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_DOCREPORT_LIST);
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.docCreationPath"));
            
            int inbounddocTotal = 0;
            int outbounddocTotal = 0;
            int docTotal = 0;
            int allTotal = 0;
            
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + File.separator + "EDITrackinginoutReport.xls");
            filePath = file.getAbsolutePath() + File.separator + "EDITrackinginoutReport.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("EDITrackinginoutReport");
            HSSFRow row1;
            TrackInOutBean trackInOutBean = null;
            
            if (list.size() != 0) {
                trackInOutBean = (TrackInOutBean) list.get(0);
                ArrayList inboundList = trackInOutBean.getInboundList();
                ArrayList outboundList = trackInOutBean.getOutboundList();
                ArrayList docType = trackInOutBean.getDocumentTypeList();
                ArrayList dateMonth = trackInOutBean.getDateMonth();
                ArrayList dateMonthdocType = trackInOutBean.getDateMonthdocType();
                
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                HSSFCellStyle cellStyleHead = workbook.createCellStyle();
                HSSFFont font4 = workbook.createFont();
                HSSFFont fontHead = workbook.createFont();
                fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                font4.setColor(HSSFColor.WHITE.index);
                
                cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
                cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cellStyle.setFont(font4);
                Date date = new Date();
                row1 = worksheet.createRow((short) 1);
                Cell cell = row1.createCell((short) 1);
                cell.setCellValue("EDI Tracking IN/OUT:-Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
                cellStyleHead.setFont(fontHead);
                cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cellStyleHead.setFillForegroundColor(HSSFColor.YELLOW.index);
                cellStyleHead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cell.setCellStyle(cellStyleHead);
                worksheet.addMergedRegion(CellRangeAddress.valueOf("B2:I2"));
                row1 = worksheet.createRow((short) 3);
                
                HSSFCell cella1 = row1.createCell((short) 0);
                cella1.setCellValue("TRANSACTION TYPE");
                cella1.setCellStyle(cellStyle);
                HSSFCell cellb1 = row1.createCell((short) 1);
                cellb1.setCellValue("Direction");
                cellb1.setCellStyle(cellStyle);
                worksheet.autoSizeColumn((short) 0);
                worksheet.autoSizeColumn((short) 1);
                for (int i = 0; i < dateMonth.size(); i++) {
                    HSSFCell cellc1 = row1.createCell((short) i + 2);
                    cellc1.setCellValue((String) dateMonth.get(i));
                    cellc1.setCellStyle(cellStyle);
                    worksheet.autoSizeColumn((short) i + 2);
                }
                HSSFCell celld1 = row1.createCell((short) dateMonth.size() + 2);
                celld1.setCellValue("Total");
                celld1.setCellStyle(cellStyle);
                worksheet.autoSizeColumn((short) dateMonth.size() + 2);
                int k = 0;
                for (int i = 0; i < docType.size(); i++) {
                    int j = 0;
                    if (inboundList.contains(docType.get(i))) {
                        j = j + 1;
                    }
                    if (outboundList.contains(docType.get(i))) {
                        j = j + 1;
                    }
                    if (j == 1) {
                        row1 = worksheet.createRow((short) i + k + 4);
                        HSSFCell cellA1 = row1.createCell((short) 0);
                        cellA1.setCellValue((String) docType.get(i));
                        worksheet.addMergedRegion(new CellRangeAddress(i + k + 4, i + k + 5, 0, 0));
                        
                        if (inboundList.contains(docType.get(i))) {
                            HSSFCell cellB1 = row1.createCell((short) 1);
                            cellB1.setCellValue("Inbound");
                        }
                        if (outboundList.contains(docType.get(i))) {
                            HSSFCell cellC1 = row1.createCell((short) 1);
                            cellC1.setCellValue("Outbound");
                        }
                        row1 = worksheet.createRow((short) i + k + 5);
                        HSSFCell cellD1 = row1.createCell((short) 1);
                        cellD1.setCellValue("Total");
                        k = k + 1;
                    }
                    
                    if (j == 2) {
                        row1 = worksheet.createRow((short) i + k + 4);
                        HSSFCell cellA1 = row1.createCell((short) 0);
                        cellA1.setCellValue((String) docType.get(i));
                        worksheet.addMergedRegion(new CellRangeAddress(i + k + 4, i + k + 6, 0, 0));
                        
                        HSSFCell cellB1 = row1.createCell((short) 1);
                        cellB1.setCellValue("Inbound");
                        row1 = worksheet.createRow((short) i + k + 5);
                        HSSFCell cellC1 = row1.createCell((short) 1);
                        cellC1.setCellValue("Outbound");
                        row1 = worksheet.createRow((short) i + k + 6);
                        HSSFCell cellD1 = row1.createCell((short) 1);
                        cellD1.setCellValue("Total");
                        k = k + 2;
                    }
                    
                }
                row1 = worksheet.createRow((short) docType.size() + k + 4);
                HSSFCell cellA1 = row1.createCell((short) 0);
                cellA1.setCellValue("Total");
                worksheet.addMergedRegion(new CellRangeAddress(docType.size() + k + 4, docType.size() + k + 4, 0, 1));
                
                int inboundvalue = 0;
                int outboundvalue = 0;
                int inoutTotal = 0;
                for (int j = 0; j < dateMonthdocType.size(); j++) {
                    ArrayList temp = (ArrayList) dateMonthdocType.get(j);
                    ArrayList olddoctype = new ArrayList();
                    
                    int total = 0;
                    k = 0;
                    for (int i = 0; i < docType.size(); i++) {
                        
                        for (int l = 1; l < temp.size(); l = l + 4) {
                            int m = 0;
                            if (temp.get(l).equals(docType.get(i))) {
                                inboundvalue = (Integer) temp.get(l + 1);
                                outboundvalue = (Integer) temp.get(l + 2);
                                inoutTotal = (Integer) temp.get(l + 3);
                                if (inboundList.contains(docType.get(i))) {
                                    m = m + 1;
                                }
                                if (outboundList.contains(docType.get(i))) {
                                    m = m + 1;
                                }
                                if (m == 1) {
                                    row1 = worksheet.getRow((short) k + 4);
                                    if (inboundList.contains(docType.get(i))) {
                                        HSSFCell cellB1 = row1.createCell((short) j + 2);
                                        cellB1.setCellValue(inboundvalue);
                                        
                                    }
                                    if (outboundList.contains(docType.get(i))) {
                                        HSSFCell cellC1 = row1.createCell((short) j + 2);
                                        
                                        cellC1.setCellValue(outboundvalue);
                                        
                                    }
                                    row1 = worksheet.getRow((short) k + 5);
                                    HSSFCell cellD1 = row1.createCell((short) j + 2);
                                    
                                    cellD1.setCellValue(inoutTotal);
                                    k = k + 2;
                                }
                                
                                if (m == 2) {
                                    row1 = worksheet.getRow((short) k + 4);
                                    HSSFCell cellB1 = row1.createCell((short) j + 2);
                                    cellB1.setCellValue(inboundvalue);
                                    row1 = worksheet.getRow((short) k + 5);
                                    
                                    HSSFCell cellC1 = row1.createCell((short) j + 2);
                                    
                                    cellC1.setCellValue(outboundvalue);
                                    row1 = worksheet.getRow((short) k + 6);
                                    HSSFCell cellD1 = row1.createCell((short) j + 2);
                                    
                                    cellD1.setCellValue(inoutTotal);
                                    
                                    k = k + 3;
                                }
                                total = (Integer) temp.get(l + 3) + total;
                                
                            } else {
                                if (!temp.contains(docType.get(i)) && !olddoctype.contains((String) docType.get(i))) {
                                    olddoctype.add((String) docType.get(i));
                                    if (inboundList.contains(docType.get(i))) {
                                        m = m + 1;
                                    }
                                    if (outboundList.contains(docType.get(i))) {
                                        m = m + 1;
                                    }
                                    if (m == 1) {
                                        row1 = worksheet.getRow((short) k + 4);
                                        if (inboundList.contains(docType.get(i))) {
                                            HSSFCell cellB1 = row1.createCell((short) j + 2);
                                            cellB1.setCellValue(0);
                                        }
                                        if (outboundList.contains(docType.get(i))) {
                                            HSSFCell cellC1 = row1.createCell((short) j + 2);
                                            cellC1.setCellValue(0);
                                        }
                                        row1 = worksheet.getRow((short) k + 5);
                                        HSSFCell cellD1 = row1.createCell((short) j + 2);
                                        cellD1.setCellValue(0);
                                        k = k + 2;
                                    }
                                    
                                    if (m == 2) {
                                        
                                        row1 = worksheet.getRow((short) k + 4);
                                        HSSFCell cellB1 = row1.createCell((short) j + 2);
                                        cellB1.setCellValue(0);
                                        row1 = worksheet.getRow((short) k + 5);
                                        HSSFCell cellC1 = row1.createCell((short) j + 2);
                                        cellC1.setCellValue(0);
                                        row1 = worksheet.getRow((short) k + 6);
                                        HSSFCell cellD1 = row1.createCell((short) j + 2);
                                        cellD1.setCellValue(0);
                                        k = k + 3;
                                    }
                                }
                            }
                            
                        }
                    }
                    
                    row1 = worksheet.getRow((short) k + 4);
                    HSSFCell cellB1 = row1.createCell((short) j + 2);
                    cellB1.setCellValue(total);
                }
                int m = 0;
                for (int j = 0; j < docType.size(); j++) {
                    for (int i = 0; i < dateMonthdocType.size(); i++) {
                        ArrayList temp1 = (ArrayList) dateMonthdocType.get(i);
                        for (int l = 1; l < temp1.size(); l = l + 4) {
                            if (temp1.get(l).equals(docType.get(j))) {
                                inbounddocTotal = (Integer) temp1.get(l + 1) + inbounddocTotal;
                                outbounddocTotal = (Integer) temp1.get(l + 2) + outbounddocTotal;
                                docTotal = (Integer) temp1.get(l + 3) + docTotal;
                            }
                        }
                    }
                    k = 0;
                    if (inboundList.contains(docType.get(j))) {
                        k = k + 1;
                    }
                    if (outboundList.contains(docType.get(j))) {
                        k = k + 1;
                    }
                    if (k == 1) {
                        
                        row1 = worksheet.getRow((short) j + m + 4);
                        
                        if (inboundList.contains(docType.get(j))) {
                            HSSFCell cellB1 = row1.createCell((short) dateMonth.size() + 2);
                            cellB1.setCellValue(inbounddocTotal);
                        }
                        if (outboundList.contains(docType.get(j))) {
                            HSSFCell cellC1 = row1.createCell((short) dateMonth.size() + 2);
                            cellC1.setCellValue(outbounddocTotal);
                        }
                        row1 = worksheet.getRow((short) j + m + 5);
                        HSSFCell cellD1 = row1.createCell((short) dateMonth.size() + 2);
                        cellD1.setCellValue(docTotal);
                        m = m + 1;
                    }
                    
                    if (k == 2) {
                        row1 = worksheet.getRow((short) j + m + 4);
                        
                        HSSFCell cellB1 = row1.createCell((short) dateMonth.size() + 2);
                        cellB1.setCellValue(inbounddocTotal);
                        row1 = worksheet.getRow((short) j + m + 5);
                        HSSFCell cellC1 = row1.createCell((short) dateMonth.size() + 2);
                        cellC1.setCellValue(outbounddocTotal);
                        row1 = worksheet.getRow((short) j + m + 6);
                        HSSFCell cellD1 = row1.createCell((short) dateMonth.size() + 2);
                        cellD1.setCellValue(docTotal);
                        m = m + 2;
                    }
                    
                    allTotal = allTotal + docTotal;
                    inbounddocTotal = 0;
                    outbounddocTotal = 0;
                    docTotal = 0;
                }
                row1 = worksheet.getRow((short) docType.size() + m + 4);
                HSSFCell cellB1 = row1.createCell((short) dateMonth.size() + 2);
                cellB1.setCellValue(allTotal);
            }
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in docReportTrackingInOutExcelDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in docReportTrackingInOutExcelDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in docReportTrackingInOutExcelDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String docReportTrackingInquiryExcelDownload() {
        String filePath = "";
        try {
            java.util.List list = (java.util.List) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_DOCREPORT_LIST);
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.docCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + File.separator + "docEdiTrackingInquiryReport.xls");
            filePath = file.getAbsolutePath() + File.separator + "docEdiTrackingInquiryReport.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("EdiTrackingInquiry");
            HSSFRow row1;
            TrackInOutBean docRepositoryBean = null;
            
            if (list.size() != 0) {
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                HSSFCellStyle cellStyle1 = workbook.createCellStyle();
                HSSFCellStyle cellStyle2 = workbook.createCellStyle();
                HSSFCellStyle cellStyle3 = workbook.createCellStyle();
                HSSFCellStyle cellStyleHead = workbook.createCellStyle();
                HSSFFont font1 = workbook.createFont();
                HSSFFont font2 = workbook.createFont();
                HSSFFont font3 = workbook.createFont();
                HSSFFont font4 = workbook.createFont();
                HSSFFont fontHead = workbook.createFont();
                fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                font4.setColor(HSSFColor.WHITE.index);
                
                cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
                cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cellStyle.setFont(font4);
                Date date = new Date();
                row1 = worksheet.createRow((short) 0);
                HSSFCell cellpo0 = row1.createCell((short) 0);
                HSSFCell cellpo1 = row1.createCell((short) 1);
                HSSFCell cellpo2 = row1.createCell((short) 2);
                HSSFCell cellpo3 = row1.createCell((short) 3);
                
                HSSFCell cellpo4 = row1.createCell((short) 4);
                row1 = worksheet.createRow((short) 1);
                Cell cell = row1.createCell((short) 1);
                cell.setCellValue("Edi Tracking Inquiry:-Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
                cellStyleHead.setFont(fontHead);
                cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cellStyleHead.setFillForegroundColor(HSSFColor.YELLOW.index);
                cellStyleHead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cell.setCellStyle(cellStyleHead);
                worksheet.addMergedRegion(CellRangeAddress.valueOf("B2:F2"));
                
                row1 = worksheet.createRow((short) 3);
                
                HSSFCell cella1 = row1.createCell((short) 1);
                cella1.setCellValue("Trans_Type");
                cella1.setCellStyle(cellStyle);
                
                HSSFCell cellb1 = row1.createCell((short) 2);
                cellb1.setCellValue("Date Sent");
                cellb1.setCellStyle(cellStyle);
                HSSFCell cellc1 = row1.createCell((short) 3);
                cellc1.setCellValue("Date Acked");
                cellc1.setCellStyle(cellStyle);
                HSSFCell celld1 = row1.createCell((short) 4);
                celld1.setCellValue("ACK Code");
                celld1.setCellStyle(cellStyle);
                HSSFCell celle1 = row1.createCell((short) 5);
                celle1.setCellValue("Partner");
                celle1.setCellStyle(cellStyle);
                
                for (int i = 0; i < list.size(); i++) {
                    docRepositoryBean = (TrackInOutBean) list.get(i);
                    
                    row1 = worksheet.createRow((short) i + 4);
                    
                    HSSFCell cellA1 = row1.createCell((short) 1);
                    
                    cellA1.setCellValue(docRepositoryBean.getTransaction_type());
                    
                    HSSFCell cellB1 = row1.createCell((short) 2);
                    
                    cellB1.setCellValue(docRepositoryBean.getDate_time_rec().toString().substring(0, docRepositoryBean.getDate_time_rec().toString().lastIndexOf(":")));
                    HSSFCell cellC1 = row1.createCell((short) 3);
                    
                    cellC1.setCellValue(docRepositoryBean.getDate_time_rec().toString().substring(0, docRepositoryBean.getDate_time_rec().toString().lastIndexOf(":")));
                    
                    HSSFCell cellD1 = row1.createCell((short) 4);
                    
                    if (docRepositoryBean.getAckStatus() != null) {
                        if (docRepositoryBean.getAckStatus().equalsIgnoreCase("ACCEPTED")) {
                            
                            font1.setColor(HSSFColor.GREEN.index);
                            cellD1.setCellValue(docRepositoryBean.getAckStatus().toUpperCase());
                            cellStyle1.setFont(font1);
                            cellD1.setCellStyle(cellStyle1);
                            
                        } else if (docRepositoryBean.getAckStatus().equalsIgnoreCase("REJECTED")) {
                            
                            font2.setColor(HSSFColor.RED.index);
                            cellD1.setCellValue(docRepositoryBean.getAckStatus().toUpperCase());
                            cellStyle2.setFont(font2);
                            cellD1.setCellStyle(cellStyle2);
                            
                        } else {
                            
                            font3.setColor(HSSFColor.ORANGE.index);
                            cellD1.setCellValue(docRepositoryBean.getAckStatus().toUpperCase());
                            cellStyle3.setFont(font3);
                            cellD1.setCellStyle(cellStyle3);
                            
                        }
                    }
                    HSSFCell cellE1 = row1.createCell((short) 5);
                    
                    cellE1.setCellValue(docRepositoryBean.getPname());
                }
                worksheet.autoSizeColumn((short) 0);
                worksheet.autoSizeColumn((short) 1);
                worksheet.autoSizeColumn((short) 2);
                worksheet.autoSizeColumn((short) 3);
                worksheet.autoSizeColumn((short) 4);
                
            }
            worksheet.setColumnWidth(1, 5000);
            worksheet.setColumnWidth(2, 5000);
            worksheet.setColumnWidth(3, 6000);
            worksheet.setColumnWidth(4, 6000);
            worksheet.setColumnWidth(5, 5000);
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in docReportTrackingInquiryExcelDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in docReportTrackingInquiryExcelDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in docReportTrackingInquiryExcelDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String reportDownloads() {
        
        try {
            this.setScheduleId(Integer.parseInt(httpServletRequest.getParameter("Id").toString()));
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
            LoggerUtility.log(logger, "FileNotFoundException occurred in reportDownloads method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in reportDownloads method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in reportDownloads method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException ioException) {
                LoggerUtility.log(logger, "finally IOException occurred in reportDownloads method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
            }
        }
        return null;
        
    }
    
    public String logisticReportTrackingInOutExcelDownload() {
        String filePath = "";
        try {
            java.util.List list = (java.util.List) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_DOCREPORT_LIST);
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.docCreationPath"));
            int inbounddocTotal = 0;
            int outbounddocTotal = 0;
            int docTotal = 0;
            int allTotal = 0;
            
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + File.separator + "EDITrackinginoutlogisticReport.xls");
            filePath = file.getAbsolutePath() + File.separator + "EDITrackinginoutlogisticReport.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("EDITrackinginoutlogisticReport");
            HSSFRow row1;
            LogisticTrackInOutBean trackInOutBean = null;
            
            if (list.size() != 0) {
                trackInOutBean = (LogisticTrackInOutBean) list.get(0);
                ArrayList inboundList = trackInOutBean.getInboundList();
                ArrayList outboundList = trackInOutBean.getOutboundList();
                ArrayList docType = trackInOutBean.getDocumentTypeList();
                ArrayList dateMonth = trackInOutBean.getDateMonth();
                ArrayList dateMonthdocType = trackInOutBean.getDateMonthdocType();
                
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                HSSFCellStyle cellStyleHead = workbook.createCellStyle();
                HSSFFont font4 = workbook.createFont();
                HSSFFont fontHead = workbook.createFont();
                fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                font4.setColor(HSSFColor.WHITE.index);
                
                cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
                cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cellStyle.setFont(font4);
                Date date = new Date();
                row1 = worksheet.createRow((short) 1);
                Cell cell = row1.createCell((short) 1);
                cell.setCellValue("EDI Tracking IN/OUT:-Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
                cellStyleHead.setFont(fontHead);
                cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cell.setCellStyle(cellStyleHead);
                worksheet.addMergedRegion(CellRangeAddress.valueOf("B2:J2"));
                row1 = worksheet.createRow((short) 3);
                
                HSSFCell cella1 = row1.createCell((short) 0);
                cella1.setCellValue("TRANSACTION TYPE");
                cella1.setCellStyle(cellStyle);
                HSSFCell cellb1 = row1.createCell((short) 1);
                cellb1.setCellValue("Direction");
                cellb1.setCellStyle(cellStyle);
                worksheet.autoSizeColumn((short) 0);
                worksheet.autoSizeColumn((short) 1);
                for (int i = 0; i < dateMonth.size(); i++) {
                    HSSFCell cellc1 = row1.createCell((short) i + 2);
                    cellc1.setCellValue((String) dateMonth.get(i));
                    cellc1.setCellStyle(cellStyle);
                    worksheet.autoSizeColumn((short) i + 2);
                }
                HSSFCell celld1 = row1.createCell((short) dateMonth.size() + 2);
                celld1.setCellValue("Total");
                celld1.setCellStyle(cellStyle);
                worksheet.autoSizeColumn((short) dateMonth.size() + 2);
                int k = 0;
                for (int i = 0; i < docType.size(); i++) {
                    int j = 0;
                    if (inboundList.contains(docType.get(i))) {
                        j = j + 1;
                    }
                    if (outboundList.contains(docType.get(i))) {
                        j = j + 1;
                    }
                    if (j == 1) {
                        row1 = worksheet.createRow((short) i + k + 4);
                        HSSFCell cellA1 = row1.createCell((short) 0);
                        cellA1.setCellValue((String) docType.get(i));
                        worksheet.addMergedRegion(new CellRangeAddress(i + k + 4, i + k + 5, 0, 0));
                        
                        if (inboundList.contains(docType.get(i))) {
                            HSSFCell cellB1 = row1.createCell((short) 1);
                            cellB1.setCellValue("Inbound");
                        }
                        if (outboundList.contains(docType.get(i))) {
                            HSSFCell cellC1 = row1.createCell((short) 1);
                            cellC1.setCellValue("Outbound");
                        }
                        row1 = worksheet.createRow((short) i + k + 5);
                        HSSFCell cellD1 = row1.createCell((short) 1);
                        cellD1.setCellValue("Total");
                        k = k + 1;
                    }
                    
                    if (j == 2) {
                        row1 = worksheet.createRow((short) i + k + 4);
                        HSSFCell cellA1 = row1.createCell((short) 0);
                        cellA1.setCellValue((String) docType.get(i));
                        worksheet.addMergedRegion(new CellRangeAddress(i + k + 4, i + k + 6, 0, 0));
                        HSSFCell cellB1 = row1.createCell((short) 1);
                        cellB1.setCellValue("Inbound");
                        row1 = worksheet.createRow((short) i + k + 5);
                        HSSFCell cellC1 = row1.createCell((short) 1);
                        cellC1.setCellValue("Outbound");
                        row1 = worksheet.createRow((short) i + k + 6);
                        HSSFCell cellD1 = row1.createCell((short) 1);
                        cellD1.setCellValue("Total");
                        k = k + 2;
                    }
                    
                }
                row1 = worksheet.createRow((short) docType.size() + k + 4);
                HSSFCell cellA1 = row1.createCell((short) 0);
                cellA1.setCellValue("Total");
                worksheet.addMergedRegion(new CellRangeAddress(docType.size() + k + 4, docType.size() + k + 4, 0, 1));
                
                int inboundvalue = 0;
                int outboundvalue = 0;
                int inoutTotal = 0;
                for (int j = 0; j < dateMonthdocType.size(); j++) {
                    ArrayList temp = (ArrayList) dateMonthdocType.get(j);
                    ArrayList olddoctype = new ArrayList();
                    
                    int total = 0;
                    k = 0;
                    for (int i = 0; i < docType.size(); i++) {
                        
                        for (int l = 1; l < temp.size(); l = l + 4) {
                            int m = 0;
                            if (temp.get(l).equals(docType.get(i))) {
                                inboundvalue = (Integer) temp.get(l + 1);
                                outboundvalue = (Integer) temp.get(l + 2);
                                inoutTotal = (Integer) temp.get(l + 3);
                                if (inboundList.contains(docType.get(i))) {
                                    m = m + 1;
                                }
                                if (outboundList.contains(docType.get(i))) {
                                    m = m + 1;
                                }
                                if (m == 1) {
                                    row1 = worksheet.getRow((short) k + 4);
                                    if (inboundList.contains(docType.get(i))) {
                                        HSSFCell cellB1 = row1.createCell((short) j + 2);
                                        cellB1.setCellValue(inboundvalue);
                                        
                                    }
                                    if (outboundList.contains(docType.get(i))) {
                                        HSSFCell cellC1 = row1.createCell((short) j + 2);
                                        
                                        cellC1.setCellValue(outboundvalue);
                                        
                                    }
                                    row1 = worksheet.getRow((short) k + 5);
                                    HSSFCell cellD1 = row1.createCell((short) j + 2);
                                    
                                    cellD1.setCellValue(inoutTotal);
                                    k = k + 2;
                                }
                                
                                if (m == 2) {
                                    row1 = worksheet.getRow((short) k + 4);
                                    HSSFCell cellB1 = row1.createCell((short) j + 2);
                                    cellB1.setCellValue(inboundvalue);
                                    row1 = worksheet.getRow((short) k + 5);
                                    
                                    HSSFCell cellC1 = row1.createCell((short) j + 2);
                                    
                                    cellC1.setCellValue(outboundvalue);
                                    row1 = worksheet.getRow((short) k + 6);
                                    HSSFCell cellD1 = row1.createCell((short) j + 2);
                                    
                                    cellD1.setCellValue(inoutTotal);
                                    
                                    k = k + 3;
                                }
                                total = (Integer) temp.get(l + 3) + total;
                                
                            } else {
                                if (!temp.contains(docType.get(i)) && !olddoctype.contains((String) docType.get(i))) {
                                    olddoctype.add((String) docType.get(i));
                                    if (inboundList.contains(docType.get(i))) {
                                        m = m + 1;
                                    }
                                    if (outboundList.contains(docType.get(i))) {
                                        m = m + 1;
                                    }
                                    if (m == 1) {
                                        row1 = worksheet.getRow((short) k + 4);
                                        if (inboundList.contains(docType.get(i))) {
                                            HSSFCell cellB1 = row1.createCell((short) j + 2);
                                            cellB1.setCellValue(0);
                                        }
                                        if (outboundList.contains(docType.get(i))) {
                                            HSSFCell cellC1 = row1.createCell((short) j + 2);
                                            cellC1.setCellValue(0);
                                        }
                                        row1 = worksheet.getRow((short) k + 5);
                                        HSSFCell cellD1 = row1.createCell((short) j + 2);
                                        cellD1.setCellValue(0);
                                        k = k + 2;
                                    }
                                    
                                    if (m == 2) {
                                        
                                        row1 = worksheet.getRow((short) k + 4);
                                        HSSFCell cellB1 = row1.createCell((short) j + 2);
                                        cellB1.setCellValue(0);
                                        row1 = worksheet.getRow((short) k + 5);
                                        HSSFCell cellC1 = row1.createCell((short) j + 2);
                                        cellC1.setCellValue(0);
                                        row1 = worksheet.getRow((short) k + 6);
                                        HSSFCell cellD1 = row1.createCell((short) j + 2);
                                        cellD1.setCellValue(0);
                                        k = k + 3;
                                    }
                                }
                            }
                        }
                    }
                    
                    row1 = worksheet.getRow((short) k + 4);
                    HSSFCell cellB1 = row1.createCell((short) j + 2);
                    cellB1.setCellValue(total);
                }
                int m = 0;
                for (int j = 0; j < docType.size(); j++) {
                    for (int i = 0; i < dateMonthdocType.size(); i++) {
                        ArrayList temp1 = (ArrayList) dateMonthdocType.get(i);
                        for (int l = 1; l < temp1.size(); l = l + 4) {
                            if (temp1.get(l).equals(docType.get(j))) {
                                inbounddocTotal = (Integer) temp1.get(l + 1) + inbounddocTotal;
                                outbounddocTotal = (Integer) temp1.get(l + 2) + outbounddocTotal;
                                docTotal = (Integer) temp1.get(l + 3) + docTotal;
                            }
                        }
                    }
                    k = 0;
                    if (inboundList.contains(docType.get(j))) {
                        k = k + 1;
                    }
                    if (outboundList.contains(docType.get(j))) {
                        k = k + 1;
                    }
                    if (k == 1) {
                        
                        row1 = worksheet.getRow((short) j + m + 4);
                        
                        if (inboundList.contains(docType.get(j))) {
                            HSSFCell cellB1 = row1.createCell((short) dateMonth.size() + 2);
                            cellB1.setCellValue(inbounddocTotal);
                        }
                        if (outboundList.contains(docType.get(j))) {
                            HSSFCell cellC1 = row1.createCell((short) dateMonth.size() + 2);
                            cellC1.setCellValue(outbounddocTotal);
                        }
                        row1 = worksheet.getRow((short) j + m + 5);
                        HSSFCell cellD1 = row1.createCell((short) dateMonth.size() + 2);
                        cellD1.setCellValue(docTotal);
                        m = m + 1;
                    }
                    
                    if (k == 2) {
                        row1 = worksheet.getRow((short) j + m + 4);
                        
                        HSSFCell cellB1 = row1.createCell((short) dateMonth.size() + 2);
                        cellB1.setCellValue(inbounddocTotal);
                        row1 = worksheet.getRow((short) j + m + 5);
                        HSSFCell cellC1 = row1.createCell((short) dateMonth.size() + 2);
                        cellC1.setCellValue(outbounddocTotal);
                        row1 = worksheet.getRow((short) j + m + 6);
                        HSSFCell cellD1 = row1.createCell((short) dateMonth.size() + 2);
                        cellD1.setCellValue(docTotal);
                        m = m + 2;
                    }
                    
                    allTotal = allTotal + docTotal;
                    inbounddocTotal = 0;
                    outbounddocTotal = 0;
                    docTotal = 0;
                }
                row1 = worksheet.getRow((short) docType.size() + m + 4);
                HSSFCell cellB1 = row1.createCell((short) dateMonth.size() + 2);
                cellB1.setCellValue(allTotal);
            }
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in logisticReportTrackingInOutExcelDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in logisticReportTrackingInOutExcelDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in logisticReportTrackingInOutExcelDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String logisticReportTrackingInquiryExcelDownload() {
        String filePath = "";
        try {
            java.util.List list = (java.util.List) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_DOCREPORT_LIST);
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.docCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + File.separator + "LogisticEdiTrackingInquiryReport.xls");
            filePath = file.getAbsolutePath() + File.separator + "LogisticEdiTrackingInquiryReport.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("LogisticEdiTrackingInquiryReport");
            HSSFRow row1;
            LogisticTrackInOutBean docRepositoryBean = null;
            
            if (list.size() != 0) {
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                HSSFCellStyle cellStyle1 = workbook.createCellStyle();
                HSSFCellStyle cellStyle2 = workbook.createCellStyle();
                HSSFCellStyle cellStyle3 = workbook.createCellStyle();
                HSSFCellStyle cellStyleHead = workbook.createCellStyle();
                HSSFFont font1 = workbook.createFont();
                HSSFFont font2 = workbook.createFont();
                HSSFFont font3 = workbook.createFont();
                HSSFFont font4 = workbook.createFont();
                HSSFFont fontHead = workbook.createFont();
                fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                font4.setColor(HSSFColor.WHITE.index);
                
                cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
                cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cellStyle.setFont(font4);
                Date date = new Date();
                row1 = worksheet.createRow((short) 0);
                HSSFCell cellpo0 = row1.createCell((short) 0);
                HSSFCell cellpo1 = row1.createCell((short) 1);
                HSSFCell cellpo2 = row1.createCell((short) 2);
                HSSFCell cellpo3 = row1.createCell((short) 3);
                
                HSSFCell cellpo4 = row1.createCell((short) 4);
                row1 = worksheet.createRow((short) 1);
                Cell cell = row1.createCell((short) 1);
                cell.setCellValue("Edi Tracking Inquiry:-Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
                cellStyleHead.setFont(fontHead);
                cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cell.setCellStyle(cellStyleHead);
                worksheet.addMergedRegion(CellRangeAddress.valueOf("B2:F2"));
                
                row1 = worksheet.createRow((short) 3);
                
                HSSFCell cella1 = row1.createCell((short) 0);
                cella1.setCellValue("Trans_Type");
                cella1.setCellStyle(cellStyle);
                
                HSSFCell cellb1 = row1.createCell((short) 1);
                cellb1.setCellValue("Date Sent");
                cellb1.setCellStyle(cellStyle);
                HSSFCell cellc1 = row1.createCell((short) 2);
                cellc1.setCellValue("Date Acked");
                cellc1.setCellStyle(cellStyle);
                HSSFCell celld1 = row1.createCell((short) 3);
                celld1.setCellValue("ACK Code");
                celld1.setCellStyle(cellStyle);
                HSSFCell celle1 = row1.createCell((short) 4);
                celle1.setCellValue("Partner");
                celle1.setCellStyle(cellStyle);
                
                for (int i = 0; i < list.size(); i++) {
                    docRepositoryBean = (LogisticTrackInOutBean) list.get(i);
                    
                    row1 = worksheet.createRow((short) i + 4);
                    
                    HSSFCell cellA1 = row1.createCell((short) 0);
                    
                    cellA1.setCellValue(docRepositoryBean.getTransaction_type());
                    
                    HSSFCell cellB1 = row1.createCell((short) 1);
                    
                    cellB1.setCellValue(docRepositoryBean.getDate_time_rec().toString().substring(0, docRepositoryBean.getDate_time_rec().toString().lastIndexOf(":")));
                    HSSFCell cellC1 = row1.createCell((short) 2);
                    
                    cellC1.setCellValue(docRepositoryBean.getDate_time_rec().toString().substring(0, docRepositoryBean.getDate_time_rec().toString().lastIndexOf(":")));
                    
                    HSSFCell cellD1 = row1.createCell((short) 3);
                    
                    if (docRepositoryBean.getAckStatus() != null) {
                        if (docRepositoryBean.getAckStatus().equalsIgnoreCase("ACCEPTED")) {
                            
                            font1.setColor(HSSFColor.GREEN.index);
                            cellD1.setCellValue(docRepositoryBean.getAckStatus().toUpperCase());
                            cellStyle1.setFont(font1);
                            cellD1.setCellStyle(cellStyle1);
                            
                        } else if (docRepositoryBean.getAckStatus().equalsIgnoreCase("REJECTED")) {
                            
                            font2.setColor(HSSFColor.RED.index);
                            cellD1.setCellValue(docRepositoryBean.getAckStatus().toUpperCase());
                            cellStyle2.setFont(font2);
                            cellD1.setCellStyle(cellStyle2);
                            
                        } else {
                            
                            font3.setColor(HSSFColor.ORANGE.index);
                            cellD1.setCellValue(docRepositoryBean.getAckStatus().toUpperCase());
                            cellStyle3.setFont(font3);
                            cellD1.setCellStyle(cellStyle3);
                            
                        }
                    }
                    HSSFCell cellE1 = row1.createCell((short) 4);
                    cellE1.setCellValue(docRepositoryBean.getPname());
                }
                worksheet.autoSizeColumn((short) 0);
                worksheet.autoSizeColumn((short) 1);
                worksheet.autoSizeColumn((short) 2);
                worksheet.autoSizeColumn((short) 3);
                worksheet.autoSizeColumn((short) 4);
                
            }
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in logisticReportTrackingInquiryExcelDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in logisticReportTrackingInquiryExcelDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in logisticReportTrackingInquiryExcelDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String logisticReportTrackingSummaryExcelDownload() {
        String filePath = "";
        try {
            java.util.List list = (java.util.List) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_DOCREPORT_LIST);
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.docCreationPath"));
            int inboundTotal = 0;
            int outboundTotal = 0;
            double filesizeTotal = 0;
            int total = 0;
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + File.separator + "EDITrackinglogisticSummaryReport.xls");
            filePath = file.getAbsolutePath() + File.separator + "EDITrackinglogisticSummaryReport.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("EDITrackinglogisticSummaryReport");
            HSSFRow row1;
            LogisticTrackInOutBean trackInOutBean = null;
            
            if (list.size() != 0) {
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                
                HSSFCellStyle cellStyleHead = workbook.createCellStyle();
                
                HSSFFont font4 = workbook.createFont();
                HSSFFont fontHead = workbook.createFont();
                fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                // fontHead.setFontHeightInPoints((short)15);  //for font Size
                font4.setColor(HSSFColor.WHITE.index);
                
                cellStyle.setFillForegroundColor(HSSFColor.BLACK.index);
                cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cellStyle.setFont(font4);
                //start	
                Date date = new Date();
                
                row1 = worksheet.createRow((short) 1);
                Cell cell = row1.createCell((short) 0);
                cell.setCellValue("EDI Tracking Summary:-Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
                cellStyleHead.setFont(fontHead);
                cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cell.setCellStyle(cellStyleHead);
                worksheet.addMergedRegion(CellRangeAddress.valueOf("A2:F2"));
                //end
                row1 = worksheet.createRow((short) 3);
                
                HSSFCell cella1 = row1.createCell((short) 0);
                cella1.setCellValue("TRANSACTION TYPE");
                cella1.setCellStyle(cellStyle);
                //HSSFCellStyle cellStyle = workbook.createCellStyle(); 
                HSSFCell cellb1 = row1.createCell((short) 1);
                cellb1.setCellValue("Partner");
                cellb1.setCellStyle(cellStyle);
                HSSFCell cellc1 = row1.createCell((short) 2);
                cellc1.setCellValue("FILE_SIZE");
                cellc1.setCellStyle(cellStyle);
                HSSFCell celld1 = row1.createCell((short) 3);
                celld1.setCellValue("IN");
                celld1.setCellStyle(cellStyle);
                HSSFCell celle1 = row1.createCell((short) 4);
                celle1.setCellValue("OUT");
                celle1.setCellStyle(cellStyle);
                HSSFCell cellf1 = row1.createCell((short) 5);
                cellf1.setCellValue("Total");
                cellf1.setCellStyle(cellStyle);
                int j = 0;
                String trans_type = "";
                for (int i = 0; i < list.size(); i++) {
                    trackInOutBean = (LogisticTrackInOutBean) list.get(i);
                    row1 = worksheet.createRow((short) i + 5);
                    
                    if (trackInOutBean.getTransaction_type() != null && !"".equalsIgnoreCase(trackInOutBean.getTransaction_type())) {
                        trans_type = trackInOutBean.getTransaction_type();
                        //cellA1.setCellValue(); 
                    }
                    HSSFCell cellB1 = row1.createCell((short) 1);
                    
                    cellB1.setCellValue(trackInOutBean.getPname());
                    
                    HSSFCell cellC1 = row1.createCell((short) 2);
                    
                    cellC1.setCellValue(trackInOutBean.getFilesizeTotal());
                    
                    HSSFCell cellD1 = row1.createCell((short) 3);
                    
                    cellD1.setCellValue(trackInOutBean.getInbound());
                    
                    HSSFCell cellE1 = row1.createCell((short) 4);
                    
                    cellE1.setCellValue(trackInOutBean.getOutbound());
                    HSSFCell cellF1 = row1.createCell((short) 5);
                    cellF1.setCellValue(trackInOutBean.getTotal());
                    if (trackInOutBean.getPname().equalsIgnoreCase("Total")) {
                        //System.out.println("inside if i="+i+"j="+j+"doctype"+docRepositoryBean.getTransaction_type()+"celldoctype"+cellA1.getStringCellValue());CellRangeAddress.valueOf("A"+(j+5)+":A"+(i+5))
                        System.out.println("tans type" + trans_type);
                        row1 = worksheet.getRow((short) j + 5);
                        HSSFCell cellA1 = row1.createCell((short) 0);
                        cellA1.setCellValue(trans_type);
                        worksheet.addMergedRegion(new CellRangeAddress(
                                j + 5, //first row (0-based)
                                i + 5, //last row  (0-based)
                                0, //first column (0-based)
                                0 //last column  (0-based)
                        ));
                        j = i + 1;
                        
                        inboundTotal = inboundTotal + trackInOutBean.getInbound();
                        outboundTotal = outboundTotal + trackInOutBean.getOutbound();
                        filesizeTotal = filesizeTotal + trackInOutBean.getFilesizeTotal();
                        //  filesizeTotal1=filesizeTotal1+trackInOutBean.getFilesize1();
                        total = total + trackInOutBean.getTotal();
                    }
                    
                }
                row1 = worksheet.createRow((short) list.size() + 5);

                //worksheet.addMergedRegion()
                HSSFCell cellA1 = row1.createCell((short) 0);
                cellA1.setCellValue("Total");
                HSSFCell cellB1 = row1.createCell((short) 1);
                cellB1.setCellValue("");
                HSSFCell cellC1 = row1.createCell((short) 2);
                cellC1.setCellValue(filesizeTotal);
                HSSFCell cellD1 = row1.createCell((short) 3);
                cellD1.setCellValue(inboundTotal);
                HSSFCell cellE1 = row1.createCell((short) 4);
                cellE1.setCellValue(outboundTotal);
                HSSFCell cellF1 = row1.createCell((short) 5);
                cellF1.setCellValue(total);
                worksheet.addMergedRegion(new CellRangeAddress(
                        list.size() + 5, //first row (0-based)
                        list.size() + 5, //last row  (0-based)
                        0, //first column (0-based)
                        1 //last column  (0-based)
                ));
                
                worksheet.autoSizeColumn((short) 0);
                worksheet.autoSizeColumn((short) 1);
                worksheet.autoSizeColumn((short) 2);
                worksheet.autoSizeColumn((short) 3);
                worksheet.autoSizeColumn((short) 4);
                worksheet.autoSizeColumn((short) 5);
                worksheet.autoSizeColumn((short) 6);
                worksheet.autoSizeColumn((short) 7);
                worksheet.autoSizeColumn((short) 8);
                worksheet.autoSizeColumn((short) 9);
                
            }
            
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in logisticReportTrackingSummeryExcelDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in logisticReportTrackingSummeryExcelDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in logisticReportTrackingSummeryExcelDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String logisticsReportsExcelDownload() {
        String filePath = "";
        try {
            java.util.List list = (java.util.List) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LOG_DOC_LIST);
            
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.logisticsDocCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + com.mss.ediscv.util.Properties.getProperty("os.compatability") + "logisticsReport.xls");
            filePath = file.getAbsolutePath() + com.mss.ediscv.util.Properties.getProperty("os.compatability") + "logisticsReport.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("logisticsReport");
            HSSFRow row1;
            LogisticReportsBean logisticsDocBean = null;
            
            if (list.size() != 0) {
                
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                HSSFCellStyle cellStyle1 = workbook.createCellStyle();
                HSSFCellStyle cellStyle2 = workbook.createCellStyle();
                HSSFCellStyle cellStyle3 = workbook.createCellStyle();
                HSSFCellStyle cellStyleHead = workbook.createCellStyle();
                HSSFFont font1 = workbook.createFont();
                HSSFFont font2 = workbook.createFont();
                HSSFFont font3 = workbook.createFont();
                HSSFFont font4 = workbook.createFont();
                HSSFFont fontHead = workbook.createFont();
                fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                font4.setColor(HSSFColor.WHITE.index);
                
                cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
                cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cellStyle.setFont(font4);
                Date date = new Date();
                row1 = worksheet.createRow((short) 0);
                HSSFCell cellpo0 = row1.createCell((short) 0);
                HSSFCell cellpo1 = row1.createCell((short) 1);
                HSSFCell cellpo2 = row1.createCell((short) 2);
                HSSFCell cellpo3 = row1.createCell((short) 3);
                
                HSSFCell cellpo4 = row1.createCell((short) 4);
                HSSFCell cellpo5 = row1.createCell((short) 5);
                HSSFCell cellpo6 = row1.createCell((short) 6);
                HSSFCell cellpo7 = row1.createCell((short) 7);
                row1 = worksheet.createRow((short) 1);
                Cell cell = row1.createCell((short) 1);
                cell.setCellValue("Logistics Reports:-Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
                cellStyleHead.setFont(fontHead);
                cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cellStyleHead.setFillForegroundColor(HSSFColor.YELLOW.index);
                cellStyleHead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cell.setCellStyle(cellStyleHead);
                worksheet.addMergedRegion(CellRangeAddress.valueOf("B2:I2"));
                
                row1 = worksheet.createRow((short) 3);
                
                HSSFCell cella1 = row1.createCell((short) 1);
                cella1.setCellValue("FileFormat");
                cella1.setCellStyle(cellStyle);
                
                HSSFCell cellb1 = row1.createCell((short) 2);
                cellb1.setCellValue("InstanceId");
                cellb1.setCellStyle(cellStyle);
                HSSFCell cellc1 = row1.createCell((short) 3);
                cellc1.setCellValue("Partner");
                cellc1.setCellStyle(cellStyle);
                HSSFCell celld1 = row1.createCell((short) 4);
                celld1.setCellValue("DateTime");
                celld1.setCellStyle(cellStyle);
                HSSFCell celle1 = row1.createCell((short) 5);
                celle1.setCellValue("TransType");
                celle1.setCellStyle(cellStyle);
                HSSFCell cellf1 = row1.createCell((short) 6);
                cellf1.setCellValue("Direction");
                cellf1.setCellStyle(cellStyle);
                HSSFCell cellg1 = row1.createCell((short) 7);
                cellg1.setCellValue("Status");
                cellg1.setCellStyle(cellStyle);
                HSSFCell cellh1 = row1.createCell((short) 8);
                cellh1.setCellValue("FileName");
                cellh1.setCellStyle(cellStyle);
                
                for (int i = 0; i < list.size(); i++) {
                    logisticsDocBean = (LogisticReportsBean) list.get(i);
                    
                    row1 = worksheet.createRow((short) i + 4);
                    
                    HSSFCell cellA1 = row1.createCell((short) 1);
                    
                    cellA1.setCellValue(logisticsDocBean.getFile_type());
                    
                    HSSFCell cellB1 = row1.createCell((short) 2);
                    
                    cellB1.setCellValue(logisticsDocBean.getFile_id());
                    HSSFCell cellC1 = row1.createCell((short) 3);
                    
                    cellC1.setCellValue(logisticsDocBean.getPname());
                    
                    HSSFCell cellD1 = row1.createCell((short) 4);
                    
                    cellD1.setCellValue(logisticsDocBean.getDate_time_rec().toString().substring(0, logisticsDocBean.getDate_time_rec().toString().lastIndexOf(":")));
                    HSSFCell cellE1 = row1.createCell((short) 5);
                    
                    cellE1.setCellValue(logisticsDocBean.getTransaction_type());
                    
                    HSSFCell cellF1 = row1.createCell((short) 6);
                    cellF1.setCellValue(logisticsDocBean.getDirection());
                    HSSFCell cellG1 = row1.createCell((short) 7);
                    if (logisticsDocBean.getStatus() != null) {
                        if (logisticsDocBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                            
                            font1.setColor(HSSFColor.GREEN.index);
                            cellG1.setCellValue(logisticsDocBean.getStatus().toUpperCase());
                            cellStyle1.setFont(font1);
                            cellG1.setCellStyle(cellStyle1);
                            
                        } else if (logisticsDocBean.getStatus().equalsIgnoreCase("ERROR")) {
                            
                            font2.setColor(HSSFColor.RED.index);
                            cellG1.setCellValue(logisticsDocBean.getStatus().toUpperCase());
                            cellStyle2.setFont(font2);
                            cellG1.setCellStyle(cellStyle2);
                            
                        } else {
                            font3.setColor(HSSFColor.ORANGE.index);
                            cellG1.setCellValue(logisticsDocBean.getStatus().toUpperCase());
                            cellStyle3.setFont(font3);
                            cellG1.setCellStyle(cellStyle3);
                        }
                    }
                    HSSFCell cellH1 = row1.createCell((short) 8);
                    cellH1.setCellValue(logisticsDocBean.getFile_name());
                }
            }
            worksheet.setColumnWidth(1, 5000);
            worksheet.setColumnWidth(2, 5000);
            worksheet.setColumnWidth(3, 6000);
            worksheet.setColumnWidth(4, 6000);
            worksheet.setColumnWidth(5, 5000);
            worksheet.setColumnWidth(6, 5000);
            worksheet.setColumnWidth(7, 5000);
            worksheet.setColumnWidth(8, 5000);
            
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in logisticReportExcelDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in logisticReportExcelDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in logisticReportExcelDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String dashVisibilityPdfDownload(String ediBusinessYearTransTrends, String yearOfMonthlyVolumeXAxisValues, String formData) {
        logger.info("Entered into the ::::GridDownloadAction :::: dashVisibilityPdfDownload ");
        String filePath = "";
        String[] form1Data = formData.split(",");
        try {
            String ediString = (ediBusinessYearTransTrends.substring(1, ((ediBusinessYearTransTrends.length()) - 1)));
            StringTokenizer edist = new StringTokenizer(ediString, ",");
            StringTokenizer yearst = new StringTokenizer(yearOfMonthlyVolumeXAxisValues, ",");
            List edidata = new ArrayList();
            while (edist.hasMoreTokens()) {
                edidata.add(edist.nextToken().trim());
            }
            List yeardata = new ArrayList();
            while (yearst.hasMoreTokens()) {
                yeardata.add(yearst.nextToken().trim());
            }
            
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.doEdiBusinessYearTransTrendsCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + File.separator + "EdiBusinessYearTransTrends.pdf");
            filePath = file.getAbsolutePath() + File.separator + "EdiBusinessYearTransTrends.pdf";
            DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
            PdfPTable inboundtable = new PdfPTable(2);
            inboundtable.setWidthPercentage(50.00f);
            for (int i = 0; i < edidata.size(); i++) {
                String a = String.valueOf(edidata.get(i));
                int edi = Integer.parseInt(a);
                String b = (String) yeardata.get(i);
                line_chart_dataset.addValue(edi, form1Data[4], b);
            }
            int width = 510;
            int height = 370;
            Date date = new Date();
            PdfWriter writer = null;
            Document document = new Document(PageSize.A3);
            writer = PdfWriter.getInstance(document, fileOut);
            document.open();
            
            PdfPTable table1 = new PdfPTable(2);
            table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table1.setWidths(new int[]{1, 2});
            Image image1 = Image.getInstance("C://MSCVP_DEMO//User_Image//user-icon.png");
            Paragraph par = new Paragraph("Report Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1(), FontFactory.getFont("Arial", 12f));
            Paragraph ex = new Paragraph(" ");
            table1.addCell(image1);
            table1.addCell(ex);
            table1.addCell(ex);
            table1.addCell(par);
            document.add(table1);
            document.add(ex);
            
            PdfPTable table = new PdfPTable(1);
            table.setTableEvent(new BorderEvent());
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            
            Paragraph heading = new Paragraph("  Business Trend : Transaction Trend Report", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD, new CMYKColor(255, 255, 0, 0)));
            heading.setAlignment(Paragraph.ALIGN_CENTER);
            
            Paragraph line1 = new Paragraph("Report Type : " + form1Data[0], FontFactory.getFont(FontFactory.COURIER, 12, Font.BOLD, new CMYKColor(255, 255, 0, 0)));
            line1.setAlignment(Paragraph.ALIGN_CENTER);
            Paragraph line2 = new Paragraph("Business Flow : " + form1Data[1] + " Direction : " + form1Data[2], FontFactory.getFont(FontFactory.COURIER, 12, Font.BOLD, new CMYKColor(255, 255, 0, 0)));
            line2.setAlignment(Paragraph.ALIGN_CENTER);
            Paragraph line3 = new Paragraph("  Document Type : " + form1Data[3] + " Year : " + form1Data[4], FontFactory.getFont(FontFactory.COURIER, 12, Font.BOLD, new CMYKColor(255, 255, 0, 0)));
            line3.setAlignment(Paragraph.ALIGN_CENTER);
            
            table.addCell(heading);
            table.addCell(ex);
            table.addCell(line1);
            table.addCell(line2);
            table.addCell(line3);
            table.addCell(ex);
            
            document.add(table);
            
            PdfContentByte canvas = writer.getDirectContent();
            Rectangle rect = new Rectangle(710, 961, 102, 570);
            rect.setBorder(Rectangle.BOX);
            rect.setBorderWidth(1);
            canvas.rectangle(rect);
            
            if (edidata.size() != 0) {
                
                JFreeChart lineChartObject = ChartFactory.createLineChart(" ", " ", "Transactional Volumes", line_chart_dataset, PlotOrientation.VERTICAL, true, true, false);
                PdfContentByte contentByte = writer.getDirectContent();
                PdfTemplate template = contentByte.createTemplate(width, height);
                Graphics2D graphics2d = template.createGraphics(width, height, new DefaultFontMapper());
                Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, width, height);
                lineChartObject.draw(graphics2d, rectangle2d);
                graphics2d.dispose();
                contentByte.addTemplate(template, 160, 580);
            }
            for (int i = 0; i <= 22; i++) {
                document.add(ex);
            }
            
            PdfContentByte canvas1 = writer.getDirectContent();
            Rectangle rect1 = new Rectangle(710, 567, 102, 380);//(right,top,left,bottom)
            rect1.setBorder(Rectangle.BOX);
            rect1.setBorderWidth(1);
            canvas1.rectangle(rect1);
            
            int size = yeardata.size();
            int count = 0;
            for (int i = 0; i < yeardata.size(); i++) {
                
                String value = "";
                int edivalbefore = 0;
                for (int j = i; j < (i + 3) && size > 0; j++) {
                    int edival = Integer.parseInt(String.valueOf(edidata.get(j)));
                    String month = String.valueOf(yeardata.get(j));
                    if (edivalbefore > 0) {
                        value = value + "  " + month + " : " + edival;
                    } else {
                        value = value + " " + month + " : " + edival;
                    }
                    size--;
                    edivalbefore = edival;
                    count = count + edival;
                }
                Paragraph values = new Paragraph(value);
                i = i + 2;
                document.add(values);
                document.add(ex);
            }
            Paragraph totalcnt = new Paragraph(" TOTAL = " + count);
            document.add(totalcnt);
            document.close();
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in dashVisibilityPdfDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in dashVisibilityPdfDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in dashVisibilityPdfDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public class BorderEvent implements PdfPTableEvent {
        
        public void tableLayout(PdfPTable table, float[][] widths, float[] heights, int headerRows, int rowStart, PdfContentByte[] canvases) {
            try {
                float width[] = widths[0];
                float x1 = width[0];
                float x2 = width[width.length - 1];
                float y1 = heights[0];
                float y2 = heights[heights.length - 1];
                PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
                cb.rectangle(x1 - 10, y1, x2 - x1 - 10, y2 - y1);
                cb.stroke();
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in BorderEvent method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
    }
    
    public String dash1VisibilityPdfDownload(String ediBusinessMonthTransTrends, String monthlyOfDaysVolumeXAxisValues, String formData) {
        String filePath = "";
        String[] form1Data = formData.split(",");
        try {
            String ediString = (ediBusinessMonthTransTrends.substring(1, ((ediBusinessMonthTransTrends.length()) - 1)));
            StringTokenizer edist = new StringTokenizer(ediString, ",");
            StringTokenizer yearst = new StringTokenizer(monthlyOfDaysVolumeXAxisValues, ",");
            List edidata = new ArrayList();
            while (edist.hasMoreTokens()) {
                edidata.add(edist.nextToken().trim());
            }
            List yeardata = new ArrayList();
            while (yearst.hasMoreTokens()) {
                yeardata.add(yearst.nextToken().trim());
            }
            
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.doEdiBusinessMonthTransTrendsCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            String b = (String) yeardata.get(0);
            String[] month = b.split(" ");
            
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + File.separator + "EdiBusinessMonthExcelTrends.pdf");
            filePath = file.getAbsolutePath() + File.separator + "EdiBusinessMonthExcelTrends.pdf";
            DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
            PdfPTable inboundtable = new PdfPTable(2);
            inboundtable.setWidthPercentage(50.00f);
            for (int i = 0; i < edidata.size(); i++) {
                String a = String.valueOf(edidata.get(i));
                int edi = Integer.parseInt(a);
                String b1 = (String) yeardata.get(i);
                String[] aa = b1.split(" ");
                line_chart_dataset.addValue(edi, month[0] + " " + form1Data[4], aa[1]);
            }
            int width = 590;
            
            int height = 370;
            
            Date date = new Date();
            PdfWriter writer = null;
            Document document = new Document(PageSize.A3);
            writer = PdfWriter.getInstance(document, fileOut);
            document.open();
            
            PdfPTable table1 = new PdfPTable(2);
            table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table1.setWidths(new int[]{1, 2});
            Image image1 = Image.getInstance("C://MSCVP_DEMO//User_Image//user-icon.png");
            
            Paragraph par = new Paragraph("Report Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1(), FontFactory.getFont("Arial", 12f));
            par.setAlignment(Paragraph.ALIGN_CENTER);
            Paragraph ex = new Paragraph(" ");
            table1.addCell(image1);
            table1.addCell(ex);
            table1.addCell(ex);
            table1.addCell(par);
            document.add(table1);
            document.add(ex);
            PdfPTable table = new PdfPTable(1);
            table.setTableEvent(new BorderEvent());
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            
            Paragraph heading = new Paragraph("  Business Trend : Transaction Trend Report", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD, new CMYKColor(255, 255, 0, 0)));
            heading.setAlignment(Paragraph.ALIGN_CENTER);
            Paragraph line1 = new Paragraph(" Report Type : " + form1Data[0], FontFactory.getFont(FontFactory.COURIER, 12, Font.BOLD, new CMYKColor(255, 255, 0, 0)));
            line1.setAlignment(Paragraph.ALIGN_CENTER);
            Paragraph line2 = new Paragraph(" Business Flow : " + form1Data[1] + " Direction : " + form1Data[2], FontFactory.getFont(FontFactory.COURIER, 12, Font.BOLD, new CMYKColor(255, 255, 0, 0)));
            line2.setAlignment(Paragraph.ALIGN_CENTER);
            Paragraph line3 = new Paragraph(" Document Type : " + form1Data[3] + " Year : " + form1Data[4] + "  Month : " + month[0], FontFactory.getFont(FontFactory.COURIER, 12, Font.BOLD, new CMYKColor(255, 255, 0, 0)));
            line3.setAlignment(Paragraph.ALIGN_CENTER);
            
            table.addCell(heading);
            table.addCell(ex);
            table.addCell(line1);
            table.addCell(line2);
            table.addCell(line3);
            table.addCell(ex);
            
            document.add(table);
            
            PdfContentByte canvas = writer.getDirectContent();
            Rectangle rect = new Rectangle(710, 961, 102, 570);
            rect.setBorder(Rectangle.BOX);
            rect.setBorderWidth(1);
            canvas.rectangle(rect);
            
            if (edidata.size() != 0) {
                
                JFreeChart lineChartObject = ChartFactory.createLineChart(" ", " ", "Transactional Volumes", line_chart_dataset, PlotOrientation.VERTICAL, true, true, false);
                PdfContentByte contentByte = writer.getDirectContent();
                PdfTemplate template = contentByte.createTemplate(width, height);
                Graphics2D graphics2d = template.createGraphics(width, height, new DefaultFontMapper());
                Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, width, height);
                lineChartObject.draw(graphics2d, rectangle2d);
                graphics2d.dispose();
                contentByte.addTemplate(template, 110, 580);
            }
            for (int i = 0; i <= 22; i++) {
                document.add(ex);
            }
            PdfContentByte canvas1 = writer.getDirectContent();
            Rectangle rect1 = new Rectangle(710, 567, 102, 230);//(right,top,left,bottom)
            rect1.setBorder(Rectangle.BOX);
            rect1.setBorderWidth(1);
            canvas1.rectangle(rect1);
            
            int size = yeardata.size();
            int count = 0;
            for (int i = 0; i < yeardata.size(); i++) {
                int edivalbefore = 0;
                int mnthvalbefore = 0;
                String value = "";
                for (int j = i; j < (i + 4) && size > 0; j++) {
                    int edival = Integer.parseInt(String.valueOf(edidata.get(j)));
                    String months = String.valueOf(yeardata.get(j));
                    if (mnthvalbefore == 6) {
                        if (edivalbefore > 0) {
                            value = value + " " + months + " : " + edival;
                        } else {
                            value = value + " " + months + "  :  " + edival;
                        }
                    } else {
                        if (edivalbefore > 0) {
                            value = value + "  " + months + " :  " + edival;
                        } else {
                            value = value + "  " + months + " :  " + edival;
                        }
                    }
                    size--;
                    edivalbefore = edival;
                    mnthvalbefore = months.length();
                    count = count + edival;
                }
                Paragraph values = new Paragraph(value);
                i = i + 3;
                document.add(values);
                document.add(ex);
            }
            Paragraph totalcnt = new Paragraph(" TOTAL = " + count);
            document.add(totalcnt);
            document.close();
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in dash1VisibilityPdfDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in dash1VisibilityPdfDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in dash1VisibilityPdfDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String tpdashVisibilityPdfDownload(String tpBusinessYearTransTrends, String yearOfMonthlyVolumeXAxisValues, String formData) {
        logger.info("Entered into the ::::GridDownloadAction :::: tpdashVisibilityPdfDownload ");
        String filePath = "";
        String[] form1Data = formData.split(",");
        try {
            String ediString = (tpBusinessYearTransTrends.substring(1, ((tpBusinessYearTransTrends.length()) - 1)));
            StringTokenizer edist = new StringTokenizer(ediString, ",");
            StringTokenizer yearst = new StringTokenizer(yearOfMonthlyVolumeXAxisValues, ",");
            List edidata = new ArrayList();
            while (edist.hasMoreTokens()) {
                edidata.add(edist.nextToken().trim());
            }
            List yeardata = new ArrayList();
            while (yearst.hasMoreTokens()) {
                yeardata.add(yearst.nextToken().trim());
            }
            
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.doTpBusinessYearTransTrendsCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + File.separator + "tpBusinessYearTransTrends.pdf");
            filePath = file.getAbsolutePath() + File.separator + "tpBusinessYearTransTrends.pdf";
            DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
            PdfPTable inboundtable = new PdfPTable(2);
            inboundtable.setWidthPercentage(50.00f);
            for (int i = 0; i < edidata.size(); i++) {
                String a = String.valueOf(edidata.get(i));
                int edi = Integer.parseInt(a);
                String b = (String) yeardata.get(i);
                line_chart_dataset.addValue(edi, form1Data[4], b);
            }
            
            int width = 510;
            
            int height = 370;
            
            Date date = new Date();
            PdfWriter writer = null;
            Document document = new Document(PageSize.A3);
            writer = PdfWriter.getInstance(document, fileOut);
            document.open();
            
            PdfPTable table1 = new PdfPTable(2);
            table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table1.setWidths(new int[]{1, 2});
            Image image1 = Image.getInstance("C://MSCVP_DEMO//User_Image//user-icon.png");
            Paragraph par = new Paragraph(" Report Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1(), FontFactory.getFont("Arial", 12f));
            
            Paragraph ex = new Paragraph(" ");
            table1.addCell(image1);
            table1.addCell(ex);
            table1.addCell(ex);
            table1.addCell(par);
            document.add(table1);
            document.add(ex);
            
            PdfPTable table = new PdfPTable(1);
            table.setTableEvent(new BorderEvent());
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            
            Paragraph heading = new Paragraph(" Business Trend : Tp Trend Report", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD, new CMYKColor(255, 255, 0, 0)));
            heading.setAlignment(Paragraph.ALIGN_CENTER);
            
            Paragraph line1 = new Paragraph("Report Type : " + form1Data[0], FontFactory.getFont(FontFactory.COURIER, 12, Font.BOLD, new CMYKColor(255, 255, 0, 0)));
            line1.setAlignment(Paragraph.ALIGN_CENTER);
            Paragraph line2 = new Paragraph("Trading Partner : " + form1Data[1] + " Direction : " + form1Data[2], FontFactory.getFont(FontFactory.COURIER, 12, Font.BOLD, new CMYKColor(255, 255, 0, 0)));
            line2.setAlignment(Paragraph.ALIGN_CENTER);
            Paragraph line3 = new Paragraph(" Document Type : " + form1Data[3] + " Year : " + form1Data[4], FontFactory.getFont(FontFactory.COURIER, 12, Font.BOLD, new CMYKColor(255, 255, 0, 0)));
            line3.setAlignment(Paragraph.ALIGN_CENTER);
            
            table.addCell(heading);
            table.addCell(ex);
            table.addCell(line1);
            table.addCell(line2);
            table.addCell(line3);
            table.addCell(ex);
            
            document.add(table);
            
            PdfContentByte canvas = writer.getDirectContent();
            Rectangle rect = new Rectangle(710, 961, 102, 570);
            rect.setBorder(Rectangle.BOX);
            rect.setBorderWidth(1);
            canvas.rectangle(rect);
            
            if (edidata.size() != 0) {
                
                JFreeChart lineChartObject = ChartFactory.createLineChart(" ", " ", "Transcational Volumes", line_chart_dataset, PlotOrientation.VERTICAL, true, true, false);
                PdfContentByte contentByte = writer.getDirectContent();
                PdfTemplate template = contentByte.createTemplate(width, height);
                Graphics2D graphics2d = template.createGraphics(width, height, new DefaultFontMapper());
                Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, width, height);
                
                lineChartObject.draw(graphics2d, rectangle2d);
                
                graphics2d.dispose();
                contentByte.addTemplate(template, 160, 580);
            }
            
            for (int i = 0; i <= 22; i++) {
                document.add(ex);
            }
            
            PdfContentByte canvas1 = writer.getDirectContent();
            Rectangle rect1 = new Rectangle(710, 567, 102, 380);
            rect1.setBorder(Rectangle.BOX);
            rect1.setBorderWidth(1);
            canvas1.rectangle(rect1);
            
            int size = yeardata.size();
            int count = 0;
            for (int i = 0; i < yeardata.size(); i++) {
                
                String value = "";
                int edivalbefore = 0;
                for (int j = i; j < (i + 3) && size > 0; j++) {
                    int edival = Integer.parseInt(String.valueOf(edidata.get(j)));
                    String month = String.valueOf(yeardata.get(j));
                    if (edivalbefore > 0) {
                        value = value + " " + month + "  :   " + edival;
                    } else {
                        value = value + " " + month + "  :   " + edival;
                    }
                    size--;
                    edivalbefore = edival;
                    count = count + edival;
                }
                Paragraph values = new Paragraph(value);
                i = i + 2;
                document.add(values);
                document.add(ex);
                
            }
            Paragraph totalcnt = new Paragraph(" TOTAL = " + count);
            document.add(totalcnt);
            document.close();
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in tpdashVisibilityPdfDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in tpdashVisibilityPdfDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in tpdashVisibilityPdfDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String tpdash1VisibilityPdfDownload(String tpBusinessMonthTransTrends, String monthlyOfDaysVolumeXAxisValues, String formData) {
        String filePath = "";
        String[] form1Data = formData.split(",");
        try {
            String ediString = (tpBusinessMonthTransTrends.substring(1, ((tpBusinessMonthTransTrends.length()) - 1)));
            StringTokenizer edist = new StringTokenizer(ediString, ",");
            StringTokenizer yearst = new StringTokenizer(monthlyOfDaysVolumeXAxisValues, ",");
            List edidata = new ArrayList();
            while (edist.hasMoreTokens()) {
                edidata.add(edist.nextToken().trim());
            }
            List yeardata = new ArrayList();
            while (yearst.hasMoreTokens()) {
                yeardata.add(yearst.nextToken().trim());
            }
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.dotpBusinessMonthTransTrendsCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            
            String b = (String) yeardata.get(0);
            String[] month = b.split(" ");
            
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + File.separator + "EdiBusinessMonthtpTrends.pdf");
            filePath = file.getAbsolutePath() + File.separator + "EdiBusinessMonthtpTrends.pdf";
            DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
            PdfPTable inboundtable = new PdfPTable(2);
            inboundtable.setWidthPercentage(50.00f);
            for (int i = 0; i < edidata.size(); i++) {
                String a = String.valueOf(edidata.get(i));
                int edi = Integer.parseInt(a);
                String b1 = (String) yeardata.get(i);
                String[] aa = b1.split(" ");
                line_chart_dataset.addValue(edi, month[0] + " " + form1Data[4], aa[1]);
            }
            
            int width = 590;
            
            int height = 370;
            
            Date date = new Date();
            PdfWriter writer = null;
            Document document = new Document(PageSize.A3);
            writer = PdfWriter.getInstance(document, fileOut);
            document.open();
            
            PdfPTable table1 = new PdfPTable(2);
            table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table1.setWidths(new int[]{1, 2});
            
            Image image1 = Image.getInstance("C://MSCVP_DEMO//User_Image//user-icon.png");
            
            Paragraph par = new Paragraph(" Report Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1(), FontFactory.getFont("Arial", 12f));
            par.setAlignment(Paragraph.ALIGN_CENTER);
            
            Paragraph ex = new Paragraph(" ");
            table1.addCell(image1);
            table1.addCell(ex);
            table1.addCell(ex);
            table1.addCell(par);
            document.add(table1);
            document.add(ex);
            PdfPTable table = new PdfPTable(1);
            table.setTableEvent(new BorderEvent());
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            
            Paragraph heading = new Paragraph("  Business Trend : Tp Trend Report", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD, new CMYKColor(255, 255, 0, 0)));
            heading.setAlignment(Paragraph.ALIGN_CENTER);
            Paragraph line1 = new Paragraph(" Report Type : " + form1Data[0], FontFactory.getFont(FontFactory.COURIER, 12, Font.BOLD, new CMYKColor(255, 255, 0, 0)));
            line1.setAlignment(Paragraph.ALIGN_CENTER);
            Paragraph line2 = new Paragraph(" Trading Partner : " + form1Data[1] + " Direction : " + form1Data[2], FontFactory.getFont(FontFactory.COURIER, 12, Font.BOLD, new CMYKColor(255, 255, 0, 0)));
            line2.setAlignment(Paragraph.ALIGN_CENTER);
            Paragraph line3 = new Paragraph(" Document Type : " + form1Data[3] + " Year : " + form1Data[4] + "  Month : " + month[0], FontFactory.getFont(FontFactory.COURIER, 12, Font.BOLD, new CMYKColor(255, 255, 0, 0)));
            line3.setAlignment(Paragraph.ALIGN_CENTER);
            
            table.addCell(heading);
            table.addCell(ex);
            table.addCell(line1);
            table.addCell(line2);
            table.addCell(line3);
            table.addCell(ex);
            
            document.add(table);
            
            PdfContentByte canvas = writer.getDirectContent();
            Rectangle rect = new Rectangle(710, 961, 102, 570);
            rect.setBorder(Rectangle.BOX);
            rect.setBorderWidth(1);
            canvas.rectangle(rect);
            
            if (edidata.size() != 0) {
                
                JFreeChart lineChartObject = ChartFactory.createLineChart(" ", " ", "Transactional Volumes", line_chart_dataset, PlotOrientation.VERTICAL, true, true, false);
                PdfContentByte contentByte = writer.getDirectContent();
                PdfTemplate template = contentByte.createTemplate(width, height);
                Graphics2D graphics2d = template.createGraphics(width, height, new DefaultFontMapper());
                Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, width, height);
                lineChartObject.draw(graphics2d, rectangle2d);
                graphics2d.dispose();
                contentByte.addTemplate(template, 110, 580);
            }
            for (int i = 0; i <= 22; i++) {
                document.add(ex);
            }
            PdfContentByte canvas1 = writer.getDirectContent();
            Rectangle rect1 = new Rectangle(710, 567, 102, 230);
            rect1.setBorder(Rectangle.BOX);
            rect1.setBorderWidth(1);
            canvas1.rectangle(rect1);
            
            int size = yeardata.size();
            int count = 0;
            for (int i = 0; i < yeardata.size(); i++) {
                int edivalbefore = 0;
                int mnthvalbefore = 0;
                String value = "";
                for (int j = i; j < (i + 4) && size > 0; j++) {
                    int edival = Integer.parseInt(String.valueOf(edidata.get(j)));
                    String months = String.valueOf(yeardata.get(j));
                    if (mnthvalbefore == 6) {
                        if (edivalbefore > 0) {
                            value = value + " " + months + "  :  " + edival;
                        } else {
                            value = value + " " + months + "  :  " + edival;
                        }
                    } else {
                        if (edivalbefore > 0) {
                            value = value + " " + months + " :  " + edival;
                        } else {
                            value = value + " " + months + "  :  " + edival;
                        }
                    }
                    size--;
                    edivalbefore = edival;
                    mnthvalbefore = months.length();
                    count = count + edival;
                }
                Paragraph values = new Paragraph(value);
                i = i + 3;
                document.add(values);
                document.add(ex);
            }
            Paragraph totalcnt = new Paragraph(" TOTAL = " + count);
            document.add(totalcnt);
            document.close();
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in tpdash1VisibilityPdfDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in tpdash1VisibilityPdfDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in tpdash1VisibilityPdfDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String dashVisibilityExcelDownload(String ediBusinessYearTransTrends, String yearOfMonthlyVolumeXAxisValues, String formData) {
        String filePath = "";
        String[] form1Data = formData.split(",");
        try {
            String ediString = (ediBusinessYearTransTrends.substring(1, ((ediBusinessYearTransTrends.length()) - 1)));
            StringTokenizer edist = new StringTokenizer(ediString, ",");
            StringTokenizer yearst = new StringTokenizer(yearOfMonthlyVolumeXAxisValues, ",");
            List edidata = new ArrayList();
            while (edist.hasMoreTokens()) {
                edidata.add(edist.nextToken().trim());
            }
            List yeardata = new ArrayList();
            while (yearst.hasMoreTokens()) {
                yeardata.add(yearst.nextToken().trim());
            }
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.doexcelYearCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + File.separator + "ExcelEdiYear.xls");
            filePath = file.getAbsolutePath() + File.separator + "ExcelediYear.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet my_sheet = workbook.createSheet("ExcelediYearReport");
            HSSFRow row1;
            DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
            Date date = new Date();
            
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            HSSFCellStyle cellStyle1 = workbook.createCellStyle();
            HSSFCellStyle cellStyleHead = workbook.createCellStyle();
            HSSFFont font4 = workbook.createFont();
            HSSFFont fontHead = workbook.createFont();
            fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font4.setColor(HSSFColor.BLUE_GREY.index);
            cellStyle.setFont(font4);
            cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            
            InputStream is = new FileInputStream("C://MSCVP_DEMO//User_Image//user-icon.png");
            byte[] bytes = IOUtils.toByteArray(is);
            int pictureIndex = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            is.close();
            
            CreationHelper helper = workbook.getCreationHelper();
            Drawing drawingPatriarch = my_sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            
            anchor.setCol1(1);
            anchor.setRow1(2);
            Picture pict = drawingPatriarch.createPicture(anchor, pictureIndex);
            pict.resize(0.3);
            my_sheet.addMergedRegion(CellRangeAddress.valueOf("B3:P6"));
            
            Row row = my_sheet.createRow(6);
            Cell cell = row.createCell(1);
            cell.setCellValue("                                                                                                                                                                             Reports Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
            my_sheet.addMergedRegion(CellRangeAddress.valueOf("B7:P7"));
            
            Row rows = my_sheet.createRow(7);
            Cell cells = rows.createCell(1);
            cells.setCellValue("Business Trend : Transactional Trend Report");
            HSSFFont hSSFFont = workbook.createFont();
            hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
            hSSFFont.setFontHeightInPoints((short) 16);
            hSSFFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            hSSFFont.setColor(HSSFColor.BLUE_GREY.index);
            cellStyle.setFont(hSSFFont);
            cells.setCellStyle(cellStyle);
            my_sheet.addMergedRegion(CellRangeAddress.valueOf("B8:P8"));
            
            Row row2 = my_sheet.createRow(8);
            Cell cell2 = row2.createCell(1);
            cell2.setCellValue("                                                                                              Type : " + form1Data[0] + "\n                                                                  Business Flow : " + form1Data[1] + "                Direction : " + form1Data[2] + "\n                                                                 Document Type : " + form1Data[3] + "               Year : " + form1Data[4]);
            HSSFFont hSSFFont1 = workbook.createFont();
            hSSFFont1.setFontName(HSSFFont.FONT_ARIAL);
            hSSFFont1.setFontHeightInPoints((short) 12);
            hSSFFont1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            hSSFFont1.setColor(HSSFColor.BLUE_GREY.index);
            cellStyle1.setFont(hSSFFont1);
            cell2.setCellStyle(cellStyle1);
            my_sheet.addMergedRegion(CellRangeAddress.valueOf("B9:P12"));
            
            if (yeardata.size() != 0) {
                
                for (int i = 0; i < yeardata.size(); i++) {
                    String a = String.valueOf(edidata.get(i));
                    int edi = Integer.parseInt(a);
                    String b = (String) yeardata.get(i);
                    line_chart_dataset.addValue(edi, form1Data[4], b);
                }
                
                my_sheet.autoSizeColumn((short) 0);
                my_sheet.autoSizeColumn((short) 1);
                my_sheet.autoSizeColumn((short) 2);
                my_sheet.autoSizeColumn((short) 3);
                my_sheet.autoSizeColumn((short) 4);
                my_sheet.autoSizeColumn((short) 5);
                my_sheet.autoSizeColumn((short) 6);
                my_sheet.autoSizeColumn((short) 7);
                my_sheet.autoSizeColumn((short) 8);
                
            }
            
            int width = 950;
            int height = 450;
            float quality = 1;
            
            Drawing drawing = my_sheet.createDrawingPatriarch();
            if (yeardata.size() != 0) {
                
                JFreeChart lineChartObject = ChartFactory.createLineChart(" ", " ", "Transactional volumes", line_chart_dataset, PlotOrientation.VERTICAL, true, true, false);
                
                ByteArrayOutputStream inbound_out = new ByteArrayOutputStream();
                ChartUtilities.writeChartAsJPEG(inbound_out, quality, lineChartObject, width, height);
                int inbound_picture_id = workbook.addPicture(inbound_out.toByteArray(), workbook.PICTURE_TYPE_JPEG);
                inbound_out.close();
                ClientAnchor inbound_anchor = new HSSFClientAnchor();
                inbound_anchor.setCol1(1);
                inbound_anchor.setRow1(13);
                Picture inbound_picture = drawing.createPicture(inbound_anchor, inbound_picture_id);
                inbound_picture.resize();
                my_sheet.addMergedRegion(CellRangeAddress.valueOf("B13:P41"));
            }
            
            int size = yeardata.size();
            int count = 0;
            String value = "";
            
            for (int i = 0; i < yeardata.size(); i++) {
                
                int edivalbefore = 0;
                for (int j = i; j < (i + 3) && size > 0; j++) {
                    int edival = Integer.parseInt(String.valueOf(edidata.get(j)));
                    String month = String.valueOf(yeardata.get(j));
                    if (edivalbefore > 0) {
                        value = value + " " + month + "  :   " + edival;
                    } else {
                        value = value + " " + month + "  :   " + edival;
                    }
                    size--;
                    edivalbefore = edival;
                    count = count + edival;
                }
                value = value + "\n\n";
                i = i + 2;
            }
            value = value + "  TOTAL = " + count + "\n";
            Row rown = my_sheet.createRow(41);
            Cell celln = rown.createCell(1);
            celln.setCellValue(value);
            my_sheet.addMergedRegion(CellRangeAddress.valueOf("B42:P52"));
            
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in dashVisibilityExcelDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in dashVisibilityExcelDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in dashVisibilityExcelDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String dash1VisibilityExcelDownload(String ediBusinessMonthTransTrends, String MonthOfDaysVolumeXAxisValues, String formData) {
        String filePath = "";
        String[] form1Data = formData.split(",");
        try {
            String ediString = (ediBusinessMonthTransTrends.substring(1, ((ediBusinessMonthTransTrends.length()) - 1)));
            StringTokenizer edist = new StringTokenizer(ediString, ",");
            StringTokenizer yearst = new StringTokenizer(MonthOfDaysVolumeXAxisValues, ",");
            List edidata = new ArrayList();
            while (edist.hasMoreTokens()) {
                edidata.add(edist.nextToken().trim());
            }
            List monthdata = new ArrayList();
            while (yearst.hasMoreTokens()) {
                monthdata.add(yearst.nextToken().trim());
            }
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.doexcelmonthCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + File.separator + "ediExcelMonth.xls");
            filePath = file.getAbsolutePath() + File.separator + "ediExcelMonth.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet my_sheet = workbook.createSheet("ediExcelMonth");
            HSSFSheet worksheet = workbook.createSheet("ediExcelMonthBusiness");
            HSSFRow row1;
            DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
            Date date = new Date();
            
            String b = (String) monthdata.get(0);
            String[] month = b.split(" ");
            
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            HSSFCellStyle cellStyle1 = workbook.createCellStyle();
            HSSFCellStyle cellStyleHead = workbook.createCellStyle();
            HSSFFont font4 = workbook.createFont();
            HSSFFont fontHead = workbook.createFont();
            fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font4.setColor(HSSFColor.BLUE_GREY.index);
            cellStyle.setFont(font4);
            cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            
            InputStream is = new FileInputStream("C://MSCVP_DEMO//User_Image//user-icon.png");
            byte[] bytes = IOUtils.toByteArray(is);
            int pictureIndex = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            is.close();
            
            CreationHelper helper = workbook.getCreationHelper();
            Drawing drawingPatriarch = my_sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            
            anchor.setCol1(1);
            anchor.setRow1(2);
            Picture pict = drawingPatriarch.createPicture(anchor, pictureIndex);
            pict.resize(0.3);
            my_sheet.addMergedRegion(CellRangeAddress.valueOf("B3:P6"));
            
            Row row = my_sheet.createRow(6);
            Cell cell = row.createCell(1);
            cell.setCellValue("                                                                                                                                                                             Reports Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
            my_sheet.addMergedRegion(CellRangeAddress.valueOf("B7:P7"));
            
            Row rows = my_sheet.createRow(7);
            Cell cells = rows.createCell(1);
            cells.setCellValue(" Business Trend : Transactional Trend Report");
            HSSFFont hSSFFont = workbook.createFont();
            hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
            hSSFFont.setFontHeightInPoints((short) 16);
            hSSFFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            hSSFFont.setColor(HSSFColor.BLUE_GREY.index);
            cellStyle.setFont(hSSFFont);
            cells.setCellStyle(cellStyle);
            my_sheet.addMergedRegion(CellRangeAddress.valueOf("B8:P8"));
            
            Row row2 = my_sheet.createRow(8);
            Cell cell2 = row2.createCell(1);
            cell2.setCellValue("                                                                                                  Type : " + form1Data[0] + "\n                                                                 Business Flow : " + form1Data[1] + "                Direction : " + form1Data[2] + "\n                                                                 Document Type : " + form1Data[3] + "              Year : " + form1Data[4] + "  Month : " + month[0]);
            HSSFFont hSSFFont1 = workbook.createFont();
            hSSFFont1.setFontName(HSSFFont.FONT_ARIAL);
            hSSFFont1.setFontHeightInPoints((short) 12);
            hSSFFont1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            hSSFFont1.setColor(HSSFColor.BLUE_GREY.index);
            cellStyle1.setFont(hSSFFont1);
            cell2.setCellStyle(cellStyle1);
            my_sheet.addMergedRegion(CellRangeAddress.valueOf("B9:P12"));
            
            if (monthdata.size() != 0) {
                for (int i = 0; i < monthdata.size(); i++) {
                    String a = String.valueOf(edidata.get(i));
                    int edi = Integer.parseInt(a);
                    String b1 = (String) monthdata.get(i);
                    String[] aa = b1.split(" ");
                    line_chart_dataset.addValue(edi, month[0] + " " + form1Data[4], aa[1]);
                }
                
                my_sheet.autoSizeColumn((short) 0);
                my_sheet.autoSizeColumn((short) 1);
                my_sheet.autoSizeColumn((short) 2);
                my_sheet.autoSizeColumn((short) 3);
                my_sheet.autoSizeColumn((short) 4);
                my_sheet.autoSizeColumn((short) 5);
                my_sheet.autoSizeColumn((short) 6);
                my_sheet.autoSizeColumn((short) 7);
                my_sheet.autoSizeColumn((short) 8);
                
            }
            
            int width = 950;
            int height = 450;
            float quality = 1;
            Drawing drawing = my_sheet.createDrawingPatriarch();
            if (monthdata.size() != 0) {
                
                JFreeChart lineChartObject = ChartFactory.createLineChart(" ", " ", "Transactional Volumes", line_chart_dataset, PlotOrientation.VERTICAL, true, true, false);
                
                ByteArrayOutputStream inbound_out = new ByteArrayOutputStream();
                ChartUtilities.writeChartAsJPEG(inbound_out, quality, lineChartObject, width, height);
                int inbound_picture_id = workbook.addPicture(inbound_out.toByteArray(), workbook.PICTURE_TYPE_JPEG);
                inbound_out.close();
                ClientAnchor inbound_anchor = new HSSFClientAnchor();
                inbound_anchor.setCol1(1);
                inbound_anchor.setRow1(13);
                Picture inbound_picture = drawing.createPicture(inbound_anchor, inbound_picture_id);
                inbound_picture.resize();
                my_sheet.addMergedRegion(CellRangeAddress.valueOf("B13:P41"));
                
            }
            
            int size = monthdata.size();
            int count = 0;
            String value = "";
            for (int i = 0; i < monthdata.size(); i++) {
                int edivalbefore = 0;
                int mnthvalbefore = 0;
                for (int j = i; j < (i + 4) && size > 0; j++) {
                    int edival = Integer.parseInt(String.valueOf(edidata.get(j)));
                    String months = String.valueOf(monthdata.get(j));
                    if (mnthvalbefore == 6) {
                        if (edivalbefore > 0) {
                            value = value + " " + months + "  :  " + edival;
                        } else {
                            value = value + " " + months + "  :  " + edival;
                        }
                    } else {
                        if (edivalbefore > 0) {
                            value = value + " " + months + "  :  " + edival;
                        } else {
                            value = value + " " + months + "  :   " + edival;
                        }
                    }
                    size--;
                    edivalbefore = edival;
                    mnthvalbefore = months.length();
                    count = count + edival;
                }
                i = i + 3;
                value = value + "\n\n";
            }
            value = value + " TOTAL = " + count + "\n";
            Row rown = my_sheet.createRow(41);
            Cell celln = rown.createCell(1);
            celln.setCellValue(value);
            my_sheet.addMergedRegion(CellRangeAddress.valueOf("B42:P57"));
            
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in dash1VisibilityExcelDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in dash1VisibilityExcelDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in dash1VisibilityExcelDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String tpdashVisibilityExcelDownload(String tpYearTransTrends, String yearOfMonthlyVolumeXAxisValues, String formData) {
        String filePath = "";
        String[] form1Data = formData.split(",");
        try {
            String tpString = (tpYearTransTrends.substring(1, ((tpYearTransTrends.length()) - 1)));
            StringTokenizer edist = new StringTokenizer(tpString, ",");
            StringTokenizer yearst = new StringTokenizer(yearOfMonthlyVolumeXAxisValues, ",");
            List edidata = new ArrayList();
            while (edist.hasMoreTokens()) {
                edidata.add(edist.nextToken().trim());
            }
            List yeardata = new ArrayList();
            while (yearst.hasMoreTokens()) {
                yeardata.add(yearst.nextToken().trim());
            }
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.doexcelYearTpCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + File.separator + "ExcelTpYear.xls");
            filePath = file.getAbsolutePath() + File.separator + "ExcelTpYear.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet my_sheet = workbook.createSheet("ExcelTpYearReport");
            HSSFSheet worksheet = workbook.createSheet("ExcelTpYearData");
            HSSFRow row1;
            DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
            Date date = new Date();
            
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            HSSFCellStyle cellStyle1 = workbook.createCellStyle();
            HSSFCellStyle cellStyleHead = workbook.createCellStyle();
            HSSFFont font4 = workbook.createFont();
            HSSFFont fontHead = workbook.createFont();
            fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font4.setColor(HSSFColor.BLUE_GREY.index);
            cellStyle.setFont(font4);
            cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            
            InputStream is = new FileInputStream("C://MSCVP_DEMO//User_Image//user-icon.png");
            byte[] bytes = IOUtils.toByteArray(is);
            int pictureIndex = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            is.close();
            
            CreationHelper helper = workbook.getCreationHelper();
            Drawing drawingPatriarch = my_sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            
            anchor.setCol1(1);
            anchor.setRow1(2);
            Picture pict = drawingPatriarch.createPicture(anchor, pictureIndex);
            pict.resize(0.3);
            my_sheet.addMergedRegion(CellRangeAddress.valueOf("B3:P6"));
            
            Row row = my_sheet.createRow(6);
            Cell cell = row.createCell(1);
            cell.setCellValue("                                                                                                                                                                             Reports Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
            my_sheet.addMergedRegion(CellRangeAddress.valueOf("B7:P7"));
            Row rows = my_sheet.createRow(7);
            Cell cells = rows.createCell(1);
            cells.setCellValue(" Business trend : Tp Trend Report");
            HSSFFont hSSFFont = workbook.createFont();
            hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
            hSSFFont.setFontHeightInPoints((short) 16);
            hSSFFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            hSSFFont.setColor(HSSFColor.BLUE_GREY.index);
            cellStyle.setFont(hSSFFont);
            cells.setCellStyle(cellStyle);
            my_sheet.addMergedRegion(CellRangeAddress.valueOf("B8:P8"));
            
            Row row2 = my_sheet.createRow(8);
            Cell cell2 = row2.createCell(1);
            cell2.setCellValue("                                                                                                  Type : " + form1Data[0] + "\n                                                                  Trading Partner : " + form1Data[1] + "                Direction : " + form1Data[2] + "\n                                                                  Document Type : " + form1Data[3] + "                  Year : " + form1Data[4]);
            HSSFFont hSSFFont1 = workbook.createFont();
            hSSFFont1.setFontName(HSSFFont.FONT_ARIAL);
            hSSFFont1.setFontHeightInPoints((short) 12);
            hSSFFont1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            hSSFFont1.setColor(HSSFColor.BLUE_GREY.index);
            cellStyle1.setFont(hSSFFont1);
            cell2.setCellStyle(cellStyle1);
            my_sheet.addMergedRegion(CellRangeAddress.valueOf("B9:P12"));
            
            if (yeardata.size() != 0) {
                for (int i = 0; i < yeardata.size(); i++) {
                    String a = String.valueOf(edidata.get(i));
                    int edi = Integer.parseInt(a);
                    String b = (String) yeardata.get(i);
                    line_chart_dataset.addValue(edi, form1Data[4], b);
                }
                
                my_sheet.autoSizeColumn((short) 0);
                my_sheet.autoSizeColumn((short) 1);
                my_sheet.autoSizeColumn((short) 2);
                my_sheet.autoSizeColumn((short) 3);
                my_sheet.autoSizeColumn((short) 4);
                my_sheet.autoSizeColumn((short) 5);
                my_sheet.autoSizeColumn((short) 6);
                my_sheet.autoSizeColumn((short) 7);
                my_sheet.autoSizeColumn((short) 8);
                
            }
            int width = 950;
            int height = 450;
            
            float quality = 1;
            
            Drawing drawing = my_sheet.createDrawingPatriarch();
            
            if (yeardata.size() != 0) {
                JFreeChart lineChartObject = ChartFactory.createLineChart(" ", " ", "Transactional Volumes", line_chart_dataset, PlotOrientation.VERTICAL, true, true, false);
                ByteArrayOutputStream inbound_out = new ByteArrayOutputStream();
                ChartUtilities.writeChartAsJPEG(inbound_out, quality, lineChartObject, width, height);
                int inbound_picture_id = workbook.addPicture(inbound_out.toByteArray(), workbook.PICTURE_TYPE_JPEG);
                inbound_out.close();
                ClientAnchor inbound_anchor = new HSSFClientAnchor();
                inbound_anchor.setCol1(1);
                inbound_anchor.setRow1(13);
                Picture inbound_picture = drawing.createPicture(inbound_anchor, inbound_picture_id);
                inbound_picture.resize();
                my_sheet.addMergedRegion(CellRangeAddress.valueOf("B13:P41"));
            }
            int size = yeardata.size();
            int count = 0;
            String value = "";
            for (int i = 0; i < yeardata.size(); i++) {
                int edivalbefore = 0;
                for (int j = i; j < (i + 3) && size > 0; j++) {
                    int edival = Integer.parseInt(String.valueOf(edidata.get(j)));
                    String month = String.valueOf(yeardata.get(j));
                    if (edivalbefore > 0) {
                        value = value + " " + month + "  :  " + edival;
                    } else {
                        value = value + " " + month + "  :  " + edival;
                    }
                    size--;
                    edivalbefore = edival;
                    count = count + edival;
                }
                value = value + "\n\n";
                i = i + 2;
            }
            value = value + " TOTAL = " + count + "\n";
            Row rown = my_sheet.createRow(41);
            Cell celln = rown.createCell(1);
            celln.setCellValue(value);
            my_sheet.addMergedRegion(CellRangeAddress.valueOf("B42:P52"));
            
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in tpdashVisibilityExcelDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in tpdashVisibilityExcelDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in tpdashVisibilityExcelDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String tpdash1VisibilityExcelDownload(String tpMonthTransTrends, String MonthOfDaysVolumeXAxisValues, String formData) {
        String filePath = "";
        String[] form1Data = formData.split(",");
        try {
            String tpString = (tpMonthTransTrends.substring(1, ((tpMonthTransTrends.length()) - 1)));
            StringTokenizer edist = new StringTokenizer(tpString, ",");
            StringTokenizer yearst = new StringTokenizer(MonthOfDaysVolumeXAxisValues, ",");
            List edidata = new ArrayList();
            while (edist.hasMoreTokens()) {
                edidata.add(edist.nextToken().trim());
            }
            List monthdata = new ArrayList();
            while (yearst.hasMoreTokens()) {
                monthdata.add(yearst.nextToken().trim());
            }
            File file = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.doexcelmonthtpCreationPath"));
            if (!file.exists()) {
                file.mkdirs();
            }
            
            String b = (String) monthdata.get(0);
            String[] month = b.split(" ");
            
            FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath() + File.separator + "tpMonthExcel.xls");
            filePath = file.getAbsolutePath() + File.separator + "tpMonthExcel.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet my_sheet = workbook.createSheet("tpMonthExcel");
            HSSFSheet worksheet = workbook.createSheet("tptrendMonthExcel");
            HSSFRow row1;
            DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
            Date date = new Date();
            
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            HSSFCellStyle cellStyle1 = workbook.createCellStyle();
            HSSFCellStyle cellStyleHead = workbook.createCellStyle();
            HSSFFont font4 = workbook.createFont();
            HSSFFont fontHead = workbook.createFont();
            fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font4.setColor(HSSFColor.BLUE_GREY.index);
            cellStyle.setFont(font4);
            cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            
            InputStream is = new FileInputStream("C://MSCVP_DEMO//User_Image//user-icon.png");
            byte[] bytes = IOUtils.toByteArray(is);
            int pictureIndex = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            is.close();
            
            CreationHelper helper = workbook.getCreationHelper();
            Drawing drawingPatriarch = my_sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            
            anchor.setCol1(1);
            anchor.setRow1(2);
            Picture pict = drawingPatriarch.createPicture(anchor, pictureIndex);
            pict.resize(0.3);
            my_sheet.addMergedRegion(CellRangeAddress.valueOf("B3:P6"));
            
            Row row = my_sheet.createRow(6);
            Cell cell = row.createCell(1);
            cell.setCellValue(" Reports Created Date : " + DateUtility.getInstance().getCurrentMySqlDateTime1());
            my_sheet.addMergedRegion(CellRangeAddress.valueOf("B7:P7"));
            
            Row rows = my_sheet.createRow(7);
            Cell cells = rows.createCell(1);
            cells.setCellValue("Business trend : Tp Trend Report");
            HSSFFont hSSFFont = workbook.createFont();
            hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
            hSSFFont.setFontHeightInPoints((short) 16);
            hSSFFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            hSSFFont.setColor(HSSFColor.BLUE_GREY.index);
            cellStyle.setFont(hSSFFont);
            cells.setCellStyle(cellStyle);
            my_sheet.addMergedRegion(CellRangeAddress.valueOf("B8:P8"));
            
            Row row2 = my_sheet.createRow(8);
            Cell cell2 = row2.createCell(1);
            cell2.setCellValue("                                                                                                   Type : " + form1Data[0] + "\n                                                                   Trading Partner : " + form1Data[1] + "                Direction : " + form1Data[2] + "\n                                                                   Document Type : " + form1Data[3] + "                  Year : " + form1Data[4] + "  Month : " + month[0]);
            HSSFFont hSSFFont1 = workbook.createFont();
            hSSFFont1.setFontName(HSSFFont.FONT_ARIAL);
            hSSFFont1.setFontHeightInPoints((short) 12);
            hSSFFont1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            hSSFFont1.setColor(HSSFColor.BLUE_GREY.index);
            cellStyle1.setFont(hSSFFont1);
            cell2.setCellStyle(cellStyle1);
            my_sheet.addMergedRegion(CellRangeAddress.valueOf("B9:P12"));
            
            if (monthdata.size() != 0) {
                for (int i = 0; i < monthdata.size(); i++) {
                    String a = String.valueOf(edidata.get(i));
                    int edi = Integer.parseInt(a);
                    String b1 = (String) monthdata.get(i);
                    String[] aa = b1.split(" ");
                    line_chart_dataset.addValue(edi, month[0] + " " + form1Data[4], aa[1]);
                }
                
                my_sheet.autoSizeColumn((short) 0);
                my_sheet.autoSizeColumn((short) 1);
                my_sheet.autoSizeColumn((short) 2);
                my_sheet.autoSizeColumn((short) 3);
                my_sheet.autoSizeColumn((short) 4);
                my_sheet.autoSizeColumn((short) 5);
                my_sheet.autoSizeColumn((short) 6);
                my_sheet.autoSizeColumn((short) 7);
                my_sheet.autoSizeColumn((short) 8);
                
            }
            
            int width = 950;
            
            int height = 450;
            
            float quality = 1;
            
            Drawing drawing = my_sheet.createDrawingPatriarch();
            
            if (monthdata.size() != 0) {
                JFreeChart lineChartObject = ChartFactory.createLineChart(
                        " ", " ",
                        "Transactional Volumes",
                        line_chart_dataset, PlotOrientation.VERTICAL,
                        true, true, false);
                ByteArrayOutputStream inbound_out = new ByteArrayOutputStream();
                ChartUtilities.writeChartAsJPEG(inbound_out, quality, lineChartObject, width, height);
                int inbound_picture_id = workbook.addPicture(inbound_out.toByteArray(), workbook.PICTURE_TYPE_JPEG);
                inbound_out.close();
                ClientAnchor inbound_anchor = new HSSFClientAnchor();
                inbound_anchor.setCol1(1);
                inbound_anchor.setRow1(13);
                Picture inbound_picture = drawing.createPicture(inbound_anchor, inbound_picture_id);
                inbound_picture.resize();
                my_sheet.addMergedRegion(CellRangeAddress.valueOf("B13:P41"));
                
            }
            
            int size = monthdata.size();
            int count = 0;
            String value = "";
            for (int i = 0; i < monthdata.size(); i++) {
                int edivalbefore = 0;
                int mnthvalbefore = 0;
                for (int j = i; j < (i + 4) && size > 0; j++) {
                    int edival = Integer.parseInt(String.valueOf(edidata.get(j)));
                    String months = String.valueOf(monthdata.get(j));
                    if (mnthvalbefore == 6) {
                        if (edivalbefore > 0) {
                            value = value + "  " + months + " : " + edival;
                        } else {
                            value = value + " " + months + " : " + edival;
                        }
                    } else {
                        if (edivalbefore > 0) {
                            value = value + "  " + months + " : " + edival;
                        } else {
                            value = value + " " + months + " : " + edival;
                        }
                        
                    }
                    size--;
                    edivalbefore = edival;
                    mnthvalbefore = months.length();
                    count = count + edival;
                }
                i = i + 3;
                value = value + "\n\n";
            }
            value = value + "TOTAL = " + count + "\n";
            Row rown = my_sheet.createRow(41);
            Cell celln = rown.createCell(1);
            celln.setCellValue(value);
            my_sheet.addMergedRegion(CellRangeAddress.valueOf("B42:P57"));
            
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LoggerUtility.log(logger, "FileNotFoundException occurred in tpdash1VisibilityExcelDownload method:: " + fileNotFoundException.getMessage(), Level.ERROR, fileNotFoundException.getCause());
        } catch (IOException ioException) {
            LoggerUtility.log(logger, "IOException occurred in tpdash1VisibilityExcelDownload method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in tpdash1VisibilityExcelDownload method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return filePath;
    }
    
    public String getDownloadType() {
        return downloadType;
    }
    
    public void setDownloadType(String downloadType) {
        this.downloadType = downloadType;
    }
    
    public String getSheetType() {
        return sheetType;
    }
    
    public void setSheetType(String sheetType) {
        this.sheetType = sheetType;
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
    
    public String getXaxis() {
        return xaxis;
    }
    
    public void setXaxis(String xaxis) {
        this.xaxis = xaxis;
    }
    
    public String getYaxis() {
        return yaxis;
    }
    
    public void setYaxis(String yaxis) {
        this.yaxis = yaxis;
    }
    
    public String getFormData() {
        return formData;
    }
    
    public void setFormData(String formData) {
        this.formData = formData;
    }
    
    public String getInbound() {
        return inbound;
    }
    
    public void setInbound(String inbound) {
        this.inbound = inbound;
    }
    
    public String getOutbound() {
        return outbound;
    }
    
    public void setOutbound(String outbound) {
        this.outbound = outbound;
    }
    
}
