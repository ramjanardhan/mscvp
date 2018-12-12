/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.shipment;

import java.sql.Timestamp;

/**
 * @author 
 */
public class ShipmentBean {

    private String asnNo;
    private String poNo;
    private String bolNo;
    private String isa;
    private String shipmentDate;
    private String pname;
    private String gsCtrl;
    private String stCtrl;
    private String direction;
    private String status;
    private Timestamp date_time_rec;
    private String ackStatus;
    private String file_id;
    private String reProcessStatus;

    /**
     * @return the asnNo
     */
    public String getAsnNo() {
        return asnNo;
    }

    /**
     * @param asnNo the asnNo to set
     */
    public void setAsnNo(String asnNo) {
        this.asnNo = asnNo;
    }

    /**
     * @return the poNo
     */
    public String getPoNo() {
        return poNo;
    }

    /**
     * @param poNo the poNo to set
     */
    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    /**
     * @return the bolNo
     */
    public String getBolNo() {
        return bolNo;
    }

    /**
     * @param bolNo the bolNo to set
     */
    public void setBolNo(String bolNo) {
        this.bolNo = bolNo;
    }

    /**
     * @return the isa
     */
    public String getIsa() {
        return isa;
    }

    /**
     * @param isa the isa to set
     */
    public void setIsa(String isa) {
        this.isa = isa;
    }

    /**
     * @return the shipmentDate
     */
    public String getShipmentDate() {
        return shipmentDate;
    }

    /**
     * @param shipmentDate the shipmentDate to set
     */
    public void setShipmentDate(String shipmentDate) {
        this.shipmentDate = shipmentDate;
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
     * @return the gsCtrl
     */
    public String getGsCtrl() {
        return gsCtrl;
    }

    /**
     * @param gsCtrl the gsCtrl to set
     */
    public void setGsCtrl(String gsCtrl) {
        this.gsCtrl = gsCtrl;
    }

    /**
     * @return the stCtrl
     */
    public String getStCtrl() {
        return stCtrl;
    }

    /**
     * @param stCtrl the stCtrl to set
     */
    public void setStCtrl(String stCtrl) {
        this.stCtrl = stCtrl;
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
     * @return the file_id
     */
    public String getFile_id() {
        return file_id;
    }

    /**
     * @param file_id the file_id to set
     */
    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public String getReProcessStatus() {
        return reProcessStatus;
    }

    public void setReProcessStatus(String reProcessStatus) {
        this.reProcessStatus = reProcessStatus;
    }
}
