/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.inv;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author miracle
 */
public class InvoiceBean {

    private String invNumber;
    private String poNumber;
    private String itemQty;
    private String invAmount;
    private Date invDate;
    private String pname;
    private String status;
    private String ackStatus;
    private String fileId;
    private Timestamp date_time_rec;
    private String reProcessStatus;
    private String direction;

    /**
     * @return the invNumber
     */
    public String getInvNumber() {
        return invNumber;
    }

    /**
     * @param invNumber the invNumber to set
     */
    public void setInvNumber(String invNumber) {
        this.invNumber = invNumber;
    }

    /**
     * @return the poNumber
     */
    public String getPoNumber() {
        return poNumber;
    }

    /**
     * @param poNumber the poNumber to set
     */
    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    /**
     * @return the itemQty
     */
    public String getItemQty() {
        return itemQty;
    }

    /**
     * @param itemQty the itemQty to set
     */
    public void setItemQty(String itemQty) {
        this.itemQty = itemQty;
    }

    /**
     * @return the invAmount
     */
    public String getInvAmount() {
        return invAmount;
    }

    /**
     * @param invAmount the invAmount to set
     */
    public void setInvAmount(String invAmount) {
        this.invAmount = invAmount;
    }

    /**
     * @return the pname
     */
    public String getPname() {
        return pname;
    }

    /**
     * @param pname the pname to set
     */
    public void setPname(String pname) {
        this.pname = pname;
    }

    /**
     * @return the ackStatus
     */
    public String getAckStatus() {
        return ackStatus;
    }

    /**
     * @param ackStatus the ackStatus to set
     */
    public void setAckStatus(String ackStatus) {
        this.ackStatus = ackStatus;
    }

    /**
     * @return the fileId
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * @param fileId the fileId to set
     */
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    /**
     * @return the invDate
     */
    public Date getInvDate() {
        return invDate;
    }

    /**
     * @param invDate the invDate to set
     */
    public void setInvDate(Date invDate) {
        this.invDate = invDate;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the date_time_rec
     */
    public Timestamp getDate_time_rec() {
        return date_time_rec;
    }

    /**
     * @param date_time_rec the date_time_rec to set
     */
    public void setDate_time_rec(Timestamp date_time_rec) {
        this.date_time_rec = date_time_rec;
    }

    public String getReProcessStatus() {
        return reProcessStatus;
    }

    public void setReProcessStatus(String reProcessStatus) {
        this.reProcessStatus = reProcessStatus;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
