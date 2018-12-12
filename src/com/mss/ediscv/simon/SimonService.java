/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.simon;

import com.mss.ediscv.util.ServiceLocatorException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author janardhan
 */
public interface SimonService {

    public List getDetails(SimonAction simonAction);

    public ArrayList<SimonBean> buildPartnerQuery(SimonAction simonAction) throws ServiceLocatorException;

    public List getProcessFlows(SimonAction simonAction) throws ServiceLocatorException;
}
