/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.po;

import java.sql.Timestamp;

/**
 * @author
 */
public class PurchaseOrderBean {

    private String po;
    private String so;
    private String sapIdoc;
    private String poDate;
    private String shipDate;
    private String poStatus;
    private String isaControl;
    private String itemQty;
    private String fileId;
    private String gsControlNumber;
    private String direction;
    private String status;
    private String pname;
    private String stctrlnum;
    private String reProcessStatus;
    private String ackStatus;
    private Timestamp date_time_rec;
     private String TransactionType;
    /**
     * @return the po
     */
    public String getPo() {
        return po;
    }

    /**
     * @param po the po to set
     */
    public void setPo(String po) {
        this.po = po;
    }

    /**
     * @return the so
     */
    public String getSo() {
        return so;
    }

    /**
     * @param so the so to set
     */
    public void setSo(String so) {
        this.so = so;
    }

    /**
     * @return the sapIdoc
     */
    public String getSapIdoc() {
        return sapIdoc;
    }

    /**
     * @param sapIdoc the sapIdoc to set
     */
    public void setSapIdoc(String sapIdoc) {
        this.sapIdoc = sapIdoc;
    }

    /**
     * @return the poDate
     */
    public String getPoDate() {
        return poDate;
    }

    /**
     * @param poDate the poDate to set
     */
    public void setPoDate(String poDate) {
        this.poDate = poDate;
    }

    /**
     * @return the shipDate
     */
    public String getShipDate() {
        return shipDate;
    }

    /**
     * @param shipDate the shipDate to set
     */
    public void setShipDate(String shipDate) {
        this.shipDate = shipDate;
    }

    /**
     * @return the poStatus
     */
    public String getPoStatus() {
        return poStatus;
    }

    /**
     * @param poStatus the poStatus to set
     */
    public void setPoStatus(String poStatus) {
        this.poStatus = poStatus;
    }

    /**
     * @return the isaControl
     */
    public String getIsaControl() {
        return isaControl;
    }

    /**
     * @param isaControl the isaControl to set
     */
    public void setIsaControl(String isaControl) {
        this.isaControl = isaControl;
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
     * @return the gsControlNumber
     */
    public String getGsControlNumber() {
        return gsControlNumber;
    }

    /**
     * @param gsControlNumber the gsControlNumber to set
     */
    public void setGsControlNumber(String gsControlNumber) {
        this.gsControlNumber = gsControlNumber;
    }

    /**
     * @return the direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(String direction) {
        this.direction = direction;
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
     * @return the stctrlnum
     */
    public String getStctrlnum() {
        return stctrlnum;
    }

    /**
     * @param stctrlnum the stctrlnum to set
     */
    public void setStctrlnum(String stctrlnum) {
        this.stctrlnum = stctrlnum;
    }

    /**
     * @return the reProcessStatus
     */
    public String getReProcessStatus() {
        return reProcessStatus;
    }

    /**
     * @param reProcessStatus the reProcessStatus to set
     */
    public void setReProcessStatus(String reProcessStatus) {
        this.reProcessStatus = reProcessStatus;
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

    public String getTransactionType() {
        return TransactionType;
    }

    public void setTransactionType(String TransactionType) {
        this.TransactionType = TransactionType;
    }
    
}
