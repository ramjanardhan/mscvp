/**
 *
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.mss.ediscv.lfc;

import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.LifecycleUtility;
import com.mss.ediscv.util.ServiceLocatorException;
import java.util.ArrayList;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class LifecycleServiceImpl implements LifecycleService {

    private static Logger logger = LogManager.getLogger(LifecycleServiceImpl.class.getName());
    private LifecycleBeans lifecycleBeans;
    private ArrayList<PoLifecycleBean> poLifecycleBeanList;
    private ArrayList<AsnLifecycleBean> asnLifecycleBeanList;
    private ArrayList<InvoiceLifecycleBean> invoiceLifecycleBeanList;
    private ArrayList<PaymentLifecycleBean> PaymentLifecycleBeanList;
    private ArrayList<LtTenderBean> loadTenderBeanList;
    private ArrayList<LtTenderBean> ltshipLifecycleBeanList;
    private ArrayList<LtTenderBean> ltInvoiceLifecycleBeanList;
    private ArrayList<LtTenderBean> ltResponseLifecycleBeanList;

    public void buildLifeCycleBeans(LifecycleAction lifecycleAction, HttpServletRequest httpServletRequest) throws ServiceLocatorException {
        lifecycleBeans = new LifecycleBeans();
        try {
        String Ponum = lifecycleAction.getPoNumber();
        String database = lifecycleAction.getDatabase();
        LifecycleUtility lifecycleUtility = new LifecycleUtility();
        poLifecycleBeanList = lifecycleUtility.addPoLifeCycleBean(Ponum, database);
        httpServletRequest.getSession(false).setAttribute(AppConstants.LFC_SES_PO_LIST, poLifecycleBeanList);
        asnLifecycleBeanList = lifecycleUtility.addAsnLifecycleBean(Ponum, database);
        httpServletRequest.getSession(false).setAttribute(AppConstants.LFC_SES_ASN_LIST, asnLifecycleBeanList);
        invoiceLifecycleBeanList = lifecycleUtility.addInvoiceLifecycleBean(Ponum, database);
        httpServletRequest.getSession(false).setAttribute(AppConstants.LFC_SES_INVOICE_LIST, invoiceLifecycleBeanList);
        PaymentLifecycleBeanList = lifecycleUtility.addPaymentLifecycleBean(Ponum, database);
        httpServletRequest.getSession(false).setAttribute(AppConstants.LFC_SES_PAYMENT_LIST, PaymentLifecycleBeanList);
        }catch (Exception exception) {
             LoggerUtility.log(logger, "Exception occurred in buildLifeCycleBeans method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        }

    public void buildLtLifeCycleBeans(LifecycleAction lifecycleAction, HttpServletRequest httpServletRequest) throws ServiceLocatorException {
        lifecycleBeans = new LifecycleBeans();
        try{
        String shipmentNum = lifecycleAction.getShipmentNumber();
         String database = lifecycleAction.getDatabase();
        LifecycleUtility lifecycleUtility = new LifecycleUtility();
        loadTenderBeanList = lifecycleUtility.getLtLoadtender(shipmentNum, database);
        ltshipLifecycleBeanList = lifecycleUtility.getLtShipment(shipmentNum, database);
        ltInvoiceLifecycleBeanList = lifecycleUtility.getLtInvoice(shipmentNum, database);
        ltResponseLifecycleBeanList = lifecycleUtility.getLtResponse(shipmentNum, database);

        ArrayList LfcList = new ArrayList();
        LfcList.addAll(loadTenderBeanList);
        LfcList.addAll(ltshipLifecycleBeanList);
        LfcList.addAll(ltInvoiceLifecycleBeanList);
        LfcList.addAll(ltResponseLifecycleBeanList);
        httpServletRequest.getSession(false).setAttribute(AppConstants.LFC_SES_LTTENDER_LIST, LfcList);
        }catch (Exception exception) {
             LoggerUtility.log(logger, "Exception occurred in buildLtLifeCycleBeans method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
    }
}
