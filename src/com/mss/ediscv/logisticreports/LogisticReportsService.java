/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.logisticreports;

import com.mss.ediscv.util.ServiceLocatorException;
import java.util.ArrayList;

/**
 * @author miracle
 */
public interface LogisticReportsService {

    public ArrayList<LogisticReportsBean> getDocumentList(LogisticReportsAction logisticreportsAction) throws ServiceLocatorException;
}
