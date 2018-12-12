/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.dashboard;

import com.mss.ediscv.util.ServiceLocatorException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.json.JSONArray;

/**
 *
 * @author miracle
 */
public interface DashBoardService {

    //Daily Stats View
    public JSONArray getDailyEdiStats(char flag) throws ServiceLocatorException;

    public ArrayList getDailyStatsEDIDocuments(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) throws ServiceLocatorException;

    public JSONArray getDailyFailureRate(char flag) throws ServiceLocatorException;

    public ArrayList getTopTpEdiInboundCount(char flag) throws ServiceLocatorException;

    public ArrayList getTopTpEdiOutboundCount(char flag) throws ServiceLocatorException;

    public ArrayList getDSVIBMonthlyVolumes(char flag) throws ServiceLocatorException;

    public ArrayList getDSVOBMonthlyVolumes(char flag) throws ServiceLocatorException;

    public int getMonthlyIbVolumes(char flag) throws ServiceLocatorException;

    public int getMonthlyObVolumes(char flag) throws ServiceLocatorException;

    public JSONArray getEdiTransactionStats(char flag);

    public JSONArray getEdiDocumentsVolume(char flag);

    public JSONArray getEdiTradingPartnerVolume(char flag);

    public boolean getTransactionalVolumes(ArrayList timeInervalArray, SimpleDateFormat sdf,DashBoardAction dashBoardAction) throws ServiceLocatorException;
    
    public ArrayList getEDIDocuments204IB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) throws ServiceLocatorException;

    public ArrayList getEDIDocuments204OB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) throws ServiceLocatorException;

    public ArrayList getEDIDocuments990IB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) throws ServiceLocatorException;

    public ArrayList getEDIDocuments990OB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) throws ServiceLocatorException;

    public ArrayList getEDIDocuments214IB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) throws ServiceLocatorException;

    public ArrayList getEDIDocuments214OB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) throws ServiceLocatorException;

    public ArrayList getEDIDocuments210IB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) throws ServiceLocatorException;

    public ArrayList getEDIDocuments210OB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) throws ServiceLocatorException;

    public ArrayList getEDIDocuments850IB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) throws ServiceLocatorException;

    public ArrayList getEDIDocuments850OB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) throws ServiceLocatorException;

    public ArrayList getEDIDocuments810IB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) throws ServiceLocatorException;

    public ArrayList getEDIDocuments810OB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) throws ServiceLocatorException;

    public ArrayList getEDIDocuments856IB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) throws ServiceLocatorException;

    public ArrayList getEDIDocuments856OB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) throws ServiceLocatorException;

    public ArrayList getEDIDocuments855IB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) throws ServiceLocatorException;

    public ArrayList getEDIDocuments855OB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) throws ServiceLocatorException;

    public ArrayList getEdiBusinessYearsTrendsResult(DashBoardAction dashBoardAction) throws ServiceLocatorException;

    public ArrayList getEdiBusinessMonthsTrendsResult(DashBoardAction dashBoardAction) throws ServiceLocatorException;

    public ArrayList getTpBusinessYearsTrendsResult(DashBoardAction dashBoardAction) throws ServiceLocatorException;

    public ArrayList getTpBusinessMonthsTrendsResult(DashBoardAction dashBoardAction) throws ServiceLocatorException;

}
