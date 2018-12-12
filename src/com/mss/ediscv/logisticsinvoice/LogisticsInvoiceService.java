/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.logisticsinvoice;

import com.mss.ediscv.util.ServiceLocatorException;
import java.util.ArrayList;

/**
 * @author miracle
 */
public interface LogisticsInvoiceService {

    public ArrayList<LogisticsInvoiceBean> buildLogInvoiceQuery(LogisticsInvoiceAction logisticsDocAction) throws ServiceLocatorException;
}
