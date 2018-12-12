package com.mss.ediscv.tp;

import com.mss.ediscv.util.ServiceLocatorException;
import java.util.ArrayList;

/**
 * @author miracle
 */
public interface TpService {

    public String addTP(TpAction tpAction) throws ServiceLocatorException;

    public ArrayList getTpList(TpAction tpAction) throws ServiceLocatorException;
}