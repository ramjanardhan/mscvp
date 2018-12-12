package com.mss.ediscv.shipment;

import com.mss.ediscv.util.ServiceLocatorException;
import java.util.List;

/**
 * @author miracle
 */
public interface ShipmentService {

    public List<ShipmentBean> buildshipmentSQuery(ShipmentSearchAction shipmentSearchbean) throws ServiceLocatorException;
}
