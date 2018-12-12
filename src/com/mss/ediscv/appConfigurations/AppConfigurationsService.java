package com.mss.ediscv.appConfigurations;

import com.mss.ediscv.util.ServiceLocatorException;

public interface AppConfigurationsService {

    public String getAppConfigurationsValues(AppConfigurationsAction appConfigurationsAction) throws ServiceLocatorException;

}
