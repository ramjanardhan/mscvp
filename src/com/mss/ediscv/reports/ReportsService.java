/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.reports;

import com.mss.ediscv.util.ServiceLocatorException;
import java.util.ArrayList;

/**
 * @author miracle
 */
public interface ReportsService {

    public ArrayList<ReportsBean> getDocumentList(ReportsAction reportsAction) throws ServiceLocatorException;
}
