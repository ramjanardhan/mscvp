package com.mss.ediscv.util;

import com.mss.ediscv.ajax.AjaxHandlerService;
import com.mss.ediscv.ajax.AjaxHandlerServiceImpl;
import com.mss.ediscv.appConfigurations.AppConfigurationsService;
import com.mss.ediscv.appConfigurations.AppConfigurationsServiceImpl;
import com.mss.ediscv.b2bchannel.B2BChannelService;
import com.mss.ediscv.b2bchannel.B2BChannelServiceImpl;
import com.mss.ediscv.dashboard.DashBoardService;
import com.mss.ediscv.dashboard.DashBoardServiceImpl;
import com.mss.ediscv.doc.DocRepositoryService;
import com.mss.ediscv.doc.DocRepositoryServiceImpl;
import com.mss.ediscv.documentVisibility.DocumentVisibilityService;
import com.mss.ediscv.documentVisibility.DocumentVisibilityServiceImpl;
import com.mss.ediscv.download.DownloadReportActionService;
import com.mss.ediscv.download.DownloadReportActionServiceImpl;
import com.mss.ediscv.editracking.TrackInOutService;
import com.mss.ediscv.editracking.TrackInOutServiceImpl;
import com.mss.ediscv.general.GeneralService;
import com.mss.ediscv.general.GeneralServiceImpl;
import com.mss.ediscv.griddownload.GridDownloadService;
import com.mss.ediscv.griddownload.GridDownloadServiceImpl;
import com.mss.ediscv.inv.InvoiceService;
import com.mss.ediscv.inv.InvoiceServiceImpl;
import com.mss.ediscv.inventory.InventoryService;
import com.mss.ediscv.inventory.InventoryServiceImpl;
import com.mss.ediscv.issues.IssuesService;
import com.mss.ediscv.issues.IssuesServiceImpl;
import com.mss.ediscv.lfc.LifecycleService;
import com.mss.ediscv.lfc.LifecycleServiceImpl;
import com.mss.ediscv.logisticeditracking.LogisticTrackInOutService;
import com.mss.ediscv.logisticeditracking.LogisticTrackInOutServiceImpl;
import com.mss.ediscv.logisticreports.LogisticReportsService;
import com.mss.ediscv.logisticreports.LogisticReportsServiceImpl;
import com.mss.ediscv.logisticsdoc.LogisticsDocService;
import com.mss.ediscv.logisticsdoc.LogisticsDocServiceImpl;
import com.mss.ediscv.logisticsinvoice.LogisticsInvoiceService;
import com.mss.ediscv.logisticsinvoice.LogisticsInvoiceServiceImpl;
import com.mss.ediscv.logisticsloadtendering.LogisticsLoadService;
import com.mss.ediscv.logisticsloadtendering.LogisticsLoadServiceImpl;
import com.mss.ediscv.logisticsshipment.LtShipmentService;
import com.mss.ediscv.logisticsshipment.LtShipmentServiceImpl;
import com.mss.ediscv.ltResponse.LtResponseService;
import com.mss.ediscv.ltResponse.LtResponseServiceImpl;
import com.mss.ediscv.partner.PartnerService;
import com.mss.ediscv.partner.PartnerServiceImpl;
import com.mss.ediscv.payments.PaymentsService;
import com.mss.ediscv.payments.PaymentsServiceImpl;
import com.mss.ediscv.po.PurchaseOrderService;
import com.mss.ediscv.po.PurchaseOrderServiceImpl;
import com.mss.ediscv.purge.PurgeService;
import com.mss.ediscv.purge.PurgeServiceImpl;
import com.mss.ediscv.reports.ReportsService;
import com.mss.ediscv.reports.ReportsServiceImpl;
import com.mss.ediscv.routing.RoutingService;
import com.mss.ediscv.routing.RoutingServiceImpl;
import com.mss.ediscv.schdular.SchdularService;
import com.mss.ediscv.schdular.SchdularServiceImpl;
import com.mss.ediscv.shipment.ShipmentService;
import com.mss.ediscv.shipment.ShipmentServiceImpl;
import com.mss.ediscv.simon.SimonService;
import com.mss.ediscv.simon.SimonServiceImpl;
import com.mss.ediscv.tp.TpService;
import com.mss.ediscv.tp.TpServiceImpl;
import com.mss.ediscv.tradingPartner.TradingPartnerService;
import com.mss.ediscv.tradingPartner.TradingPartnerServiceImpl;
import com.mss.ediscv.user.UserService;
import com.mss.ediscv.user.UserServiceImpl;
import com.mss.ediscv.utilities.CertMonitorService;
import com.mss.ediscv.utilities.CertMonitorServiceImpl;

/*
 * This Class Creates instances of all classes.
 */
public class ServiceLocator {

    private ServiceLocator() {
    }

    public static GeneralService getGeneralService() throws ServiceLocatorException {
        GeneralService generalService = new GeneralServiceImpl();
        return generalService;
    }

    public static ShipmentService getShipmentService() throws ServiceLocatorException {
        ShipmentService shipmentService = (ShipmentService) new ShipmentServiceImpl();
        return shipmentService;
    }

    public static InvoiceService getInvoiceService() throws ServiceLocatorException {
        InvoiceService invoiceService = new InvoiceServiceImpl();
        return invoiceService;
    }

    public static PaymentsService getPaymentService() throws ServiceLocatorException {
        PaymentsService paymentsService = new PaymentsServiceImpl();
        return paymentsService;
    }

    public static DocRepositoryService getDocumentService() throws ServiceLocatorException {
        DocRepositoryService docRepositoryService = new DocRepositoryServiceImpl();
        return docRepositoryService;
    }

    public static PurchaseOrderService getPurchaseService() throws ServiceLocatorException {
        PurchaseOrderService purchaseOrderService = new PurchaseOrderServiceImpl();
        return purchaseOrderService;
    }

    public static AjaxHandlerService getAjaxHandlerService() throws ServiceLocatorException {
        AjaxHandlerService ajaxHandlerService = new AjaxHandlerServiceImpl();
        return ajaxHandlerService;
    }

    public static TpService getTpService() throws ServiceLocatorException {
        TpService tpService = new TpServiceImpl();
        return tpService;
    }

    public static LifecycleService getLifeCycleService() throws ServiceLocatorException {
        LifecycleService lifecycleService = new LifecycleServiceImpl();
        return lifecycleService;
    }

    public static TradingPartnerService getTradingPartnerService() throws ServiceLocatorException {
        TradingPartnerService tradingPartnerService = new TradingPartnerServiceImpl();
        return tradingPartnerService;
    }

    public static UserService getUserService() throws ServiceLocatorException {
        UserService userService = new UserServiceImpl();
        return userService;
    }

    public static IssuesService getIssuesService() throws ServiceLocatorException {
        IssuesService issuesService = new IssuesServiceImpl();
        return issuesService;
    }

    public static LogisticsDocService getLogDocService() throws ServiceLocatorException {
        LogisticsDocService logisticsDocService = new LogisticsDocServiceImpl();
        return logisticsDocService;
    }

    public static LogisticsLoadService getLoadService() throws ServiceLocatorException {
        LogisticsLoadService logisticsLoadService = new LogisticsLoadServiceImpl();
        return logisticsLoadService;
    }

    public static LtResponseService getLtResponseService() throws ServiceLocatorException {
        LtResponseService ltResponseService = new LtResponseServiceImpl();
        return ltResponseService;
    }

    public static LogisticsInvoiceService getLogInvoiceService() throws ServiceLocatorException {
        LogisticsInvoiceService logisticsInvoiceService = new LogisticsInvoiceServiceImpl();
        return logisticsInvoiceService;
    }

    public static LtShipmentService getLogShipmentService() throws ServiceLocatorException {
        LtShipmentService ltShipmentService = new LtShipmentServiceImpl();
        return ltShipmentService;
    }

    public static PurgeService getPurgeService() throws ServiceLocatorException {
        PurgeService purgeService = new PurgeServiceImpl();
        return purgeService;
    }

    public static DocumentVisibilityService getDocumentVisibilityService() throws ServiceLocatorException {
        DocumentVisibilityService documentVisibilityService = new DocumentVisibilityServiceImpl();
        return documentVisibilityService;
    }

    public static PartnerService getPartnerService() throws ServiceLocatorException {
        PartnerService partnerService = new PartnerServiceImpl();
        return partnerService;
    }

    public static RoutingService getRoutingService() throws ServiceLocatorException {
        RoutingService routingService = new RoutingServiceImpl();
        return routingService;
    }

    public static B2BChannelService getB2BChannelService() throws ServiceLocatorException {
        B2BChannelService b2BChannelService = new B2BChannelServiceImpl();
        return b2BChannelService;
    }

    public static ReportsService getReportsService() throws ServiceLocatorException {
        ReportsService reportsService = new ReportsServiceImpl();
        return reportsService;
    }

    public static TrackInOutService getTrackInOutService() throws ServiceLocatorException {
        TrackInOutService trackInOutService = new TrackInOutServiceImpl();
        return trackInOutService;
    }

    public static SchdularService getSchdularService() throws ServiceLocatorException {
        SchdularService schdularService = new SchdularServiceImpl();
        return schdularService;
    }

    public static DownloadReportActionService getDownloadReportActionService() throws ServiceLocatorException {
        DownloadReportActionService downloadReportActionService = new DownloadReportActionServiceImpl();
        return downloadReportActionService;
    }

    public static GridDownloadService getGridDownloadService() throws ServiceLocatorException {
        GridDownloadService gridDownloadService = new GridDownloadServiceImpl();
        return gridDownloadService;
    }

    public static LogisticReportsService getLogisticReportsService() throws ServiceLocatorException {
        LogisticReportsService logisticreportsService = new LogisticReportsServiceImpl();
        return logisticreportsService;
    }

    public static LogisticTrackInOutService getLogisticTrackInOutService() throws ServiceLocatorException {
        LogisticTrackInOutService logistictrackInOutService = new LogisticTrackInOutServiceImpl();
        return logistictrackInOutService;
    }

    public static CertMonitorService getCertMonitorService() throws ServiceLocatorException {
        CertMonitorService certMonitorService = new CertMonitorServiceImpl();
        return certMonitorService;
    }

    public static InventoryService getInventoryService() throws ServiceLocatorException {
        InventoryService inventoryService = new InventoryServiceImpl();
        return inventoryService;
    }
        public static DashBoardService DashBoardService() throws ServiceLocatorException {
        DashBoardService dashBoardService = new DashBoardServiceImpl();
        return dashBoardService;
    }
        public static SimonService getSimonService() throws ServiceLocatorException {
            SimonService simonService=new SimonServiceImpl();
            return  simonService;
        }
       
        public static SimonService getDeliveryChannelList() throws ServiceLocatorException {
        SimonService simonService = new SimonServiceImpl();
        return simonService;
    }
        
       public static AppConfigurationsService getAppConfigurationsService() throws ServiceLocatorException {
        AppConfigurationsService appConfigurationsService = new AppConfigurationsServiceImpl();
        return appConfigurationsService;
    }
}
