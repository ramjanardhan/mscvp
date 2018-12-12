/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.inv;

import com.mss.ediscv.util.ServiceLocatorException;
import java.util.ArrayList;

/**
 * @author miracle
 */
public interface InvoiceService {

    public ArrayList<InvoiceBean> buildinvoiceQuery(InvoiceAction invoicebean) throws ServiceLocatorException;
}
