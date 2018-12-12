  /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.lfc;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author miracle
 */
public class LtResponsesBean {

    private String fileId;
    private String isaNum;
    private String shipmentid;
    private String fileType;
    private String file_origin;
    private String tran_type;
    private String file_path;
    private String ackStatus;
    private String direction;
    private Timestamp datetime;
    private String status;
    private String secval;
    private String reProcessStatus;
    private String res;
    private String senderId;
    private String recId;
    private String senName;
    private String recName;
    private String bolNumber;
    private String isaCtrlNum;
    private String isaDate;
    private String isaTime;
    private String preFile;
    private String postTranFile;
    private String ackFile;
    private String poNumber;
    private String errorMessage;
    private String gsCtrlNum;
    private String stCtrlNum;

    public LtResponsesBean() {
        System.out.println("I am inside LtResponsesBean default constructor...");
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

    public String getSenName() {
        return senName;
    }

    public void setSenName(String senName) {
        this.senName = senName;
    }

    public String getRecName() {
        return recName;
    }

    public void setRecName(String recName) {
        this.recName = recName;
    }

    public String getBolNumber() {
        return bolNumber;
    }

    public void setBolNumber(String bolNumber) {
        this.bolNumber = bolNumber;
    }

    public String getIsaCtrlNum() {
        return isaCtrlNum;
    }

    public void setIsaCtrlNum(String isaCtrlNum) {
        this.isaCtrlNum = isaCtrlNum;
    }

    public String getIsaDate() {
        return isaDate;
    }

    public void setIsaDate(String isaDate) {
        this.isaDate = isaDate;
    }

    public String getIsaTime() {
        return isaTime;
    }

    public void setIsaTime(String isaTime) {
        this.isaTime = isaTime;
    }

    public String getPreFile() {
        return preFile;
    }

    public void setPreFile(String preFile) {
        this.preFile = preFile;
    }

    public String getPostTranFile() {
        return postTranFile;
    }

    public void setPostTranFile(String postTranFile) {
        this.postTranFile = postTranFile;
    }

    public String getAckFile() {
        return ackFile;
    }

    public void setAckFile(String ackFile) {
        this.ackFile = ackFile;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getIsaNum() {
        return isaNum;
    }

    public void setIsaNum(String isaNum) {
        this.isaNum = isaNum;
    }

    public String getShipmentid() {
        return shipmentid;
    }

    public void setShipmentid(String shipmentid) {
        this.shipmentid = shipmentid;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFile_origin() {
        return file_origin;
    }

    public void setFile_origin(String file_origin) {
        this.file_origin = file_origin;
    }

    public String getTran_type() {
        return tran_type;
    }

    public void setTran_type(String tran_type) {
        this.tran_type = tran_type;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getAckStatus() {
        return ackStatus;
    }

    public void setAckStatus(String ackStatus) {
        this.ackStatus = ackStatus;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSecval() {
        return secval;
    }

    public void setSecval(String secval) {
        this.secval = secval;
    }

    public String getReProcessStatus() {
        return reProcessStatus;
    }

    public void setReProcessStatus(String reProcessStatus) {
        this.reProcessStatus = reProcessStatus;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getGsCtrlNum() {
        return gsCtrlNum;
    }

    public void setGsCtrlNum(String gsCtrlNum) {
        this.gsCtrlNum = gsCtrlNum;
    }

    public String getStCtrlNum() {
        return stCtrlNum;
    }

    public void setStCtrlNum(String stCtrlNum) {
        this.stCtrlNum = stCtrlNum;
    }
    
    /**
     * @return the fileId
     */
}
