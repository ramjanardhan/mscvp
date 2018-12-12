/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.schdular;

import com.mss.ediscv.util.ServiceLocatorException;
import java.util.ArrayList;

/**
 * @author miracle
 */
public interface SchdularService {

    public ArrayList<SchdularBean> getSchdularList(SchdularAction schdularAction) throws ServiceLocatorException;

    public SchdularAction schdularEdit(SchdularAction schdularAction) throws ServiceLocatorException;

    public String SchdularAdd(SchdularAction schdularAction) throws ServiceLocatorException;

    public String updateSchdular(SchdularAction schdularAction) throws ServiceLocatorException;

    public boolean getAuthdownloadUsercheck(SchdularAction schdularAction) throws ServiceLocatorException;

    public String SchdularRecordPath(SchdularAction schdularAction) throws ServiceLocatorException;

    public String SchdularEmailids(SchdularAction schdularAction) throws ServiceLocatorException;
}