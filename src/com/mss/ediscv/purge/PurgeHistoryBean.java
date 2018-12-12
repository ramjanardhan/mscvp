/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.purge;

import java.sql.Timestamp;

/**
 *
 * @author miracle
 */
public class PurgeHistoryBean {
    private String user;
    private int daysCount;
    private String transactionType;
    private String comments;
    private Timestamp archiveDate;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getDaysCount() {
        return daysCount;
    }

    public void setDaysCount(int daysCount) {
        this.daysCount = daysCount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Timestamp getArchiveDate() {
        return archiveDate;
    }

    public void setArchiveDate(Timestamp archiveDate) {
        this.archiveDate = archiveDate;
    }
 
    
}