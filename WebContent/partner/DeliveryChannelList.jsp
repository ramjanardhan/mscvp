<!DOCTYPE html>
<%@page import="com.mss.ediscv.partner.PartnerBean"%>
<%@page import="com.mss.ediscv.tradingPartner.TradingPartnerBean"%>
<%@page import="com.mss.ediscv.tp.TpBean"%>
<%-- <%@ page contentType="text/html" pageEncoding="UTF-8"%> --%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@page import="com.mss.ediscv.po.PurchaseOrderBean"%>
<%@page import="com.mss.ediscv.util.AppConstants"%>
<html>
    <head>

        <script type="text/javascript">
            function doOnLoad() {
                var configFlowFlag = $('#configFlowFlag').val();
                if (configFlowFlag == 'manufacturing') {
                    $("#manufacturing").addClass("active");
                    $("#deliverychannel").addClass("active");
                    $("#config").addClass("active");
                    $("#deliverychannel i").addClass("text-red");
                } else if (configFlowFlag == 'logistics') {
                    $("#logistics").addClass("active");
                    $("#ltdeliverychannel").addClass("active");
                    $("#ltconfig").addClass("active");
                    $("#ltdeliverychannel i").addClass("text-red");
                }
                document.getElementById('loadingAcoountSearch').style.display = "none";
            }
            // New function to show the left grid



            function goToAddDeliverChannelInfo() {
                var configFlowFlag = document.getElementById("configFlowFlag").value;
                window.location = "../partner/getDelChannelInf.action?configFlowFlag=" + configFlowFlag;
            }

            function hide()
            {

                $('#hide-menu1').removeClass('show-menu');
            }


        </script>

        <meta charset="utf-8">
        <title>Miracle Supply Chain Visibility Portal</title>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta http-equiv="pragma" content="no-cache" />
        <meta http-equiv="cache-control" content="no-cache" />
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
    </head>

    <%
        String check = null;
        if (request.getAttribute("check") != null) {
            check = request.getAttribute("check").toString();
        }

        //System.out.println("check-->"+check);
    %>
    <body class="hold-transition skin-blue sidebar-mini"  onload="doOnLoad();
                setStyle('mainTp', 'delChnInfo')" >
        <script type="text/javascript" src='<s:url value="/includes/js/wz_tooltip.js"/>'></script>
        <div>
            <s:include value="/includes/template/header.jsp"/>  


        </div>
        <div>

            <s:include value="/includes/template/sidemenu.jsp"/> 

        </div>

        <!-- Left side column. contains the logo and sidebar -->


        <!-- Content Wrapper. Contains page content -->
        <div class="content-wrapper">
            <!-- Content Header (Page header) -->


            <!-- Main content --> 
            <section class="content-header">
                <s:if test="%{configFlowFlag == 'manufacturing'}">
                    <h1>Delivery Channel Search
                        <!--                        <small>Manufacturing</small>-->
                    </h1>
                    <!--                    <ol class="breadcrumb">
                                            <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                                            <li class="active">  Delivery Channel Search </li>
                                        </ol>-->
                </s:if><s:elseif test="%{configFlowFlag == 'logistics'}">
                    <h1>Delivery Channel Search<small>Logistics</small>
                    </h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-truck"></i>Logistics</a></li>
                        <li class="active">  Delivery Channel Search </li>
                    </ol>
                </s:elseif>
            </section>
            <br>
            <section class="content">
                <div class="row">
                    <div class="col-sm-12"> 

                        <!--box-->
                        <div class="box box-primary">
                            <!--                            <div class="box-header with-border">
                                                            <h3 class="box-title"> Delivery Channel Search </h3>
                                                            <div class="box-tools pull-right">
                            
                                                            </div>
                                                        </div> /.box-header -->
                            <div class="box-body">
                                <div id="text">
                                    <!-- /.col (right) -->
                                    <!-- First Row -->
                                    <%                                        if (request.getSession(false).getAttribute("responseString") != null) {
                                            String reqponseString = request.getSession(false).getAttribute("responseString").toString();
                                            request.getSession(false).removeAttribute("responseString");
                                            out.println(reqponseString);
                                        }
                                    %>
                                    <div  style="alignment-adjust:central;" >
                                        <%String contextPath = request.getContextPath();
                                        %>



                                        <s:form action="../partner/deliveryChannelSearch.action" method="post" name="deliverChannelForm" id="deliverChannelForm" theme="simple">
                                            <s:hidden name="configFlowFlag" value="%{configFlowFlag}" id="configFlowFlag"/>
                                            <div class="form-group">
                                                <div class="row">
                                                    <div class="col-sm-12">
                                                        <div class="row">

                                                            <div class="col-sm-3"> <label for="partnerName ">Partner Name </label>
                                                                <s:select headerKey="" cssClass="form-control" headerValue="Select Type" list="partnerMap" name="partnerId" id="partnerId" value="%{partnerId}" tabindex="1" />
                                                            </div>

                                                            <div  class="col-sm-3">
                                                                <label for="routingName">Routing Name</label> 
                                                                <s:select headerKey="" cssClass="form-control" headerValue="Select Type" list="routerMap" name="routerId" id="routerId" value="%{routerId}" tabindex="1" />

                                                            </div>

                                                            <div  class="col-sm-3">
                                                                <label for="businessProcessName">Business Process Name </label>  
                                                                <s:select headerKey="" cssClass="form-control" headerValue="Select Type" list="businessProcessMap" name="businessProcessId" id="businessProcessId" value="%{businessProcessId}" tabindex="1" />

                                                            </div>

                                                            <div  class="col-sm-3">
                                                                <label for="translationMapName">Translation Map Name </label>  
                                                                <s:select headerKey="" cssClass="form-control" headerValue="Select Type" list="translationMap" name="translationId" id="translationId" value="%{translationId}" tabindex="1" />

                                                            </div>


                                                        </div>
                                                        <br>
                                                        <div class="row">
                                                            <div class="col-sm-3">
                                                                <label for="docextractmapName">Doc Extract Map Name</label>
                                                                <s:select headerKey="" cssClass="form-control" headerValue="Select Type" list="documentExtractMap" name="documentExtarctId" id="documentExtarctId" value="%{documentExtarctId}" tabindex="1" />

                                                            </div>
                                                            <div class="col-sm-3">
                                                                <label for="producermailBox">Producer Mail Box</label>
                                                                <s:select headerKey="" cssClass="form-control" headerValue="Select Type" list="producerMailMap" name="producerMailBoxId" id="producerMailBoxId" value="%{producerMailBoxId}" tabindex="1" />

                                                            </div>
                                                            <div id="loadingAcoountSearch" class="loadingImg">
                                                                <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                                            </div>

                                                            <div class="col-sm-3">
                                                                <label for="encoding">Encoding</label>
                                                                <s:select headerKey="" cssClass="form-control" headerValue="Select Type" list="encodingMailMap" name="encodingMailBoxId" id="encodingMailBoxId" value="%{encodingMailBoxId}" tabindex="1" />

                                                            </div>


                                                        </div>




                                                    </div>

                                                </div>
                                                <div>

                                                    <br>
                                                    <div class="row">

                                                        <div class="col-sm-2"> <strong><input type="button" value="Add" class="btn btn-primary col-sm-12" tabindex="33" onclick="goToAddDeliverChannelInfo();"/></strong></div>

                                                        <div class="col-sm-2"><s:submit value="Search"  cssClass="btn btn-primary col-sm-12" tabindex="16"/></div>



                                                        <s:hidden name="sampleValue" id="sampleValue" value="2"/>

                                                    </s:form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div><!-- /.box-body -->
                            <!-- /.box-footer -->
                        </div><!-- /.box -->

                        <!--box-->


                    </div>

                </div>
            </section>


            <!--main content-->
            <!-- /.content-wrapper -->

            <s:if test="#session.deliverChannelList != null"> 
                <%--- GRid start --%>

                <section class="content">
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="box">
                                <!--                                <div class="box-header">
                                                                    <h3 class="box-title">Table</h3>
                                                                </div>-->
                                <div class="box-body">
                                    <%!String cssValue = "whiteStripe";
                                        int resultsetTotal;%>
                                    <div style="overflow-x:auto;">                 

                                        <table align="left" width="100%"
                                               border="0" cellpadding="0" cellspacing="0">
                                            <tr>
                                                <td style="background-color: white;">

                                                    <table align="left" id="results" width="100%"
                                                           border="0" cellpadding="0" cellspacing="0" class="table table-bordered table-hover">
                                                        <%
                                                            java.util.List list = (java.util.List) session.getAttribute(AppConstants.SES_DELIVERY_CHANNEL_LIST);

                                                            if (list.size() != 0) {
                                                                PartnerBean partnerBean;
                                                        %>
                                                        <thead> <tr>
                                                                <th >Action</th>
                                                                <th >PartnerName </th>
                                                                <th>RoutingName</th>
                                                                <th>Sequence</th>
                                                                <th>BPName </th>
                                                                <th>TranslationMapName </th>
                                                                <th>Status </th>

                                                                <%--     <td>Changed Date</td>
                                                                     <td>ChangesUser </td> --%>


                                                            </tr></thead>
                                                        <tbody>
                                                            <tr >

                                                                <%
                                                                    for (int i = 0; i < list.size(); i++) {
                                                                        partnerBean = (PartnerBean) list.get(i);

                                                                        if (i % 2 == 0) {
                                                                            cssValue = "whiteStripe";
                                                                        } else {
                                                                            cssValue = "grayEditSelection";
                                                                        }
                                                                %>
                                                                <td style="text-align: left"><%-- <a href="#"> --%>
                                                                    <%
                                                                        int id = partnerBean.getDeliveryChannelId();
                                                                    %>
                                                                    <s:url var="myUrl" action="../partner/deliveryChannelEdit.action">
                                                                        <s:param name="deliveryChannelId"><%=id%></s:param>
                                                                        <s:param name="configFlowFlag1" value="%{configFlowFlag}"></s:param>
                                                                        <%--    <s:param name="tpid" value="%{tpid}"></s:param>
                                                                            <s:param name="tpname" value="%{tpname}"></s:param> --%>

                                                                    </s:url>

                                                                    <s:a href='%{#myUrl}' onmouseover="Tip('Click here to Edit DeliveryChannelInfo.')" onmouseout="UnTip()"><img src="../includes/images/Edit.gif"></s:a>

                                                                    <%--  </a> --%>
                                                                </td>

                                                                <td style="text-align: left">
                                                                    <% if (partnerBean.getPartnerName() != null && !"".equals(partnerBean.getPartnerName())) {%>
                                                                    <a href="#" onclick="getDeliveryChannelDetails('<%=partnerBean.getDeliveryChannelId()%>')" onmouseover="Tip('Click here to view Detail Info.')" onmouseout="UnTip()"> 
                                                                        <%
                                                                            out.println(partnerBean.getPartnerName());
                                                                        %></a>
                                                                        <%
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>
                                                                </td>

                                                                <td style="text-align: left">
                                                                    <%
                                                                        if (partnerBean.getRoutingName() != null && !"".equals(partnerBean.getRoutingName())) {
                                                                            out.println(partnerBean.getRoutingName());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td style="text-align: left">
                                                                    <%
                                                                        out.println(partnerBean.getSequence());
                                                                    %>
                                                                </td>
                                                                <td style="text-align: left">
                                                                    <%
                                                                        if (partnerBean.getBusinessProcessName() != null && !"".equals(partnerBean.getBusinessProcessName())) {
                                                                            out.println(partnerBean.getBusinessProcessName());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td style="text-align: left">
                                                                    <%
                                                                        if (partnerBean.getTranslationMapName() != null && !"".equals(partnerBean.getTranslationMapName())) {
                                                                            out.println(partnerBean.getTranslationMapName());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td> <td style="text-align: left">
                                                                    <%
                                                                        if (partnerBean.getStatus() != null && !"".equals(partnerBean.getStatus())) {
                                                                            out.println(partnerBean.getStatus());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td> <%-- <td style="text-align: left">
                                                                    <%
                                                            out.println(partnerBean.getChangedDate());
                                                            %>
                                                                </td> <td style="text-align: left">
                                                                    <%
                                                            out.println(partnerBean.getChangedBy());
                                                            %>
                                                                </td> --%>

                                                                <%-- <td>
                                                                     <%
                                                             out.println(tpBean.getTpInPath());
                                                             %>
                                                                 </td>
                                                                 
                                                                 <td>
                                                                     <%
                                                             out.println(tpBean.getTpOutPath());
                                                             %>
                                                                 </td>  --%>

                                                            </tr>
                                                            <%
                                                                }
                                                            } else {
                                                            %>
                                                            <tr><td>
                                                                    <%
                                                                            // String contextPath = request.getContextPath();
                                                                            out.println("<img  border='0' align='top'  src='" + contextPath + "/includes/images/alert.gif'/><b> No Records Found to Display!</b>");
                                                                        }

                                                                    %>
                                                                </td>
                                                            </tr>
                                                    </table>

                                                </td>
                                            </tr>
                                            <tr>
                                                <!--                                            <td align="right" colspan="28" style="background-color: white;">
                                                                                                <div align="right" id="pageNavPosition">hello</div>
                                                                                            </td>-->
                                            </tr>  
                                            </tbody>
                                        </table>
                                    </div>


                                </div>
                            </div>
                        </div>
                </section>
            </s:if> 

            <div id="hide-menu1" class="hide-menu message ">

                <div class="row col-sm-12">

                    <br>
                    <div class="col-sm-6"> <label class="labelw">Partner&nbsp;Name </label>
                        <s:textfield cssClass="form-control"  type="Text"  required="required" placeholder="" id="partnerName" name="partnerName" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">Routing Name </label>
                        <s:textfield type="Text"  cssClass="form-control"  required="required" placeholder="" id="routingName" name="routingName" readonly="true"/>
                    </div>
                </div>
                <div class="row col-sm-12"> 
                    <div class="col-sm-6"> <label class="labelw">BPName </label>
                        <s:textfield type="Text"  cssClass="form-control"  required="required" placeholder="" id="bpName" name="bpName" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">Trans Map Name </label>
                        <s:textfield type="Text"  cssClass="form-control"  required="required" placeholder="" id="translationMapName" name="translationMapName" readonly="true"/>
                    </div>
                </div>
                <div class="row col-sm-12">
                    <div class="col-sm-6"> <label class="labelw">Status </label>
                        <s:textfield type="Text"  cssClass="form-control"  required="required" placeholder="" id="deliveryStatus" name="deliveryStatus" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">  Encoding  </label>
                        <s:textfield type="Text"  cssClass="form-control"  required="required" placeholder="" id="encoding" name="encoding" readonly="true"/>
                    </div>
                </div>
                <br>

                <div class="row col-sm-12">
                    <div class="col-sm-6"> <label class="labelw">Prod Mail Box </label>
                        <s:textfield type="Text"  cssClass="form-control"  required="required" placeholder="" id="producerMailBox" name="producerMailBox" readonly="true"/>
                    </div>

                    <div class="col-sm-6"> <label class="labelw"> Sequence  </label>
                        <s:textfield type="Text"  cssClass="form-control"  required="required" placeholder="" id="sequence" name="sequence" readonly="true"/>
                    </div>
                </div>

                <div class="row col-sm-12 clear">
                    <div class="col-sm-6"> <label class="labelw">  Arch Flag  </label>
                        <s:textfield type="Text"  cssClass="form-control"  required="required" placeholder="" id="archiveFlag" name="archiveFlag" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw"> Arch Directory  </label>
                        <s:textfield type="Text"  cssClass="form-control"  required="required" placeholder="" id="archiveDirectory" name="archiveDirectory" readonly="true"/>
                    </div>
                </div>
                <div class="row col-sm-12 clear">
                    <div class="col-sm-6"> <label class="labelw">  Output FileName  </label>
                        <s:textfield type="Text"  cssClass="form-control"  required="required" placeholder="" id="outputFileName" name="outputFileName" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw"> Output Format  </label>
                        <s:textfield type="Text"  cssClass="form-control"  required="required" placeholder="" id="outputFormat" name="outputFormat" readonly="true"/>
                    </div>
                </div>
                <div class="row col-sm-12 clear">
                    <div class="col-sm-6"> <label class="labelw">Doc Extract Map Name </label>
                        <s:textfield type="Text"  cssClass="form-control"  required="required" placeholder="" id="documentExtraxctMapName" name="documentExtraxctMapName" readonly="true"/>
                    </div>

                </div>
                <div class="row">
                    <br><br><br><br>
                    <div id="noresult"></div>
                    `</div><div class="row">
                    <div class="row col-sm-12" style="margin-top:10px;">  <button type="button" class="btn btn-primary col-sm-11" style="margin-left:12px; " id="hide-menu" onclick="hide()" value="X">Close</button></div>    
                </div>
            </div>


        </div><!-- ./wrapper -->
        <script type="text/javascript">
            $(function () {
                //$("#example1").DataTable();
                $('#results').DataTable({
                    "paging": true,
                    "lengthChange": true,
                    "searching": true,
                    "ordering": true,
                    "info": true,
                    "autoWidth": false,
                    "scrollY": 300,
                    "scrollX": true
                });
            });
        </script>
    </div>

    <div>
        <s:include value="../includes/template/footer.jsp"/>
    </div>       
    <script language="JavaScript"  src='<s:url value="/includes/js/generalValidations.js"/>'></script>
    <script language="JavaScript"  src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
    <script src='<s:url value="/includes/bootstrap/js/app.min.js"/>'></script>
    <script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
    <script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>



</body>
</html>
