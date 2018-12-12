/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.payments;

import com.mss.ediscv.util.ServiceLocatorException;
import java.util.List;

/**
 * @author miracle
 */
public interface PaymentsService {

    public List<PaymentBean> buildpaymentSQuery(PaymentsAction paymentsAction) throws ServiceLocatorException;
}
