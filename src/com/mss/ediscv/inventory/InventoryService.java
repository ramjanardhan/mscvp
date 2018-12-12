
package com.mss.ediscv.inventory;

import com.mss.ediscv.util.ServiceLocatorException;
import java.util.List;

/**
 * @author miracle
 */
public interface InventoryService {

    public List<InventoryBean> buildInventorySearchQuery(InventoryAction inventoryAction) throws ServiceLocatorException;
}