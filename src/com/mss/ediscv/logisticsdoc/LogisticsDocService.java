/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.logisticsdoc;

import com.mss.ediscv.util.ServiceLocatorException;
import java.util.ArrayList;

/**
 * @author miracle
 */
public interface LogisticsDocService {

    public ArrayList<LogisticsDocBean> buildDocumentQuery(LogisticsDocAction logisticsAction) throws ServiceLocatorException;
}
