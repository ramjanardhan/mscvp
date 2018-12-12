/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.schdular;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author miracle
 */
public class SchdularBean implements Serializable {

    private Timestamp date_time_rec;
    private String schtitle;
    private String schtype;
    private Timestamp sch_createdate;
    private Timestamp run_date;
    private Timestamp startdate;
    private Timestamp enddate;
    private String status;
    private String schStartdate;
    private String schEnddate;
    private Timestamp reportfromdate;
    private Timestamp reporttodate;
    private Timestamp schdatepicker;
    private String docdatepickerfrom;
    private String schhrFormat;
    private int id;

    public String getDocdatepickerfrom() {
        return docdatepickerfrom;
    }

    public void setDocdatepickerfrom(String docdatepickerfrom) {
        this.docdatepickerfrom = docdatepickerfrom;
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

    public Timestamp getSch_createdate() {
        return sch_createdate;
    }

    public void setSch_createdate(Timestamp sch_createdate) {
        this.sch_createdate = sch_createdate;
    }

    public String getSchtitle() {
        return schtitle;
    }

    public void setSchtitle(String schtitle) {
        this.schtitle = schtitle;
    }

    public String getSchtype() {
        return schtype;
    }

    public void setSchtype(String schtype) {
        this.schtype = schtype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getEnddate() {
        return enddate;
    }

    public void setEnddate(Timestamp enddate) {
        this.enddate = enddate;
    }

    public Timestamp getRun_date() {
        return run_date;
    }

    public void setRun_date(Timestamp run_date) {
        this.run_date = run_date;
    }

    public Timestamp getStartdate() {
        return startdate;
    }

    public void setStartdate(Timestamp startdate) {
        this.startdate = startdate;
    }

    public String getSchEnddate() {
        return schEnddate;
    }

    public void setSchEnddate(String schEnddate) {
        this.schEnddate = schEnddate;
    }

    public String getSchStartdate() {
        return schStartdate;
    }

    public void setSchStartdate(String schStartdate) {
        this.schStartdate = schStartdate;
    }

    public Timestamp getReportfromdate() {
        return reportfromdate;
    }

    public void setReportfromdate(Timestamp reportfromdate) {
        this.reportfromdate = reportfromdate;
    }

    public Timestamp getReporttodate() {
        return reporttodate;
    }

    public void setReporttodate(Timestamp reporttodate) {
        this.reporttodate = reporttodate;
    }

    public Timestamp getSchdatepicker() {
        return schdatepicker;
    }

    public void setSchdatepicker(Timestamp schdatepicker) {
        this.schdatepicker = schdatepicker;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSchhrFormat() {
        return schhrFormat;
    }

    public void setSchhrFormat(String schhrFormat) {
        this.schhrFormat = schhrFormat;
    }
}
