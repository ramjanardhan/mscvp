/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.po;

import com.mss.ediscv.util.ServiceLocatorException;
import java.util.List;

/**
 * @author miracle
 */
public interface PurchaseOrderService {

    public List<PurchaseOrderBean> buildPurchaseQuery(PurchaseOrderAction purchaseOrderAction) throws ServiceLocatorException;
}
