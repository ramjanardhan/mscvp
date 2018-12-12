<%@page import="com.mss.ediscv.routing.RoutingBean"%>
<%@page import="com.mss.ediscv.tradingPartner.TradingPartnerBean"%>
<%@page import="com.mss.ediscv.tp.TpBean"%>
<%-- <%@ page contentType="text/html" pageEncoding="UTF-8"%> --%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
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
                    $("#routing").addClass("active");
                    $("#config").addClass("active");
                    $("#routing i").addClass("text-red");
                } else if (configFlowFlag == 'logistics') {
                    $("#logistics").addClass("active");
                    $("#ltrouting").addClass("active");
                    $("#ltconfig").addClass("active");
                    $("#ltrouting i").addClass("text-red");
                }
                document.getElementById('loadingAcoountSearch').style.display = "none";
            }

            function goToAddRouting() {
                var configFlowFlag = document.getElementById("configFlowFlag").value;
                //alert(configFlowFlag);
                window.location = "../routing/addRouting.action?configFlowFlag=" + configFlowFlag;
            }

            function hide()
            {
                $('#hide-menu1').removeClass('show-menu');
            }



        </script>



        <meta charset="utf-8">

        <title>Miracle Supply Chain Visibility Portal</title>
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

    <body class="hold-transition skin-blue sidebar-mini" onload="doOnLoad();"> 
        <script type="text/javascript" src='<s:url value="/includes/js/wz_tooltip.js"/>'></script>

        <div>
            <s:include value="../includes/template/header.jsp"/>
        </div>
        <div>
            <s:include value="../includes/template/sidemenu.jsp"/>
        </div>
        <!-- Content Wrapper. Contains page content -->
        <div class="content-wrapper">
            <!-- Content Header (Page header) -->


            <!-- Main content --> 
            <section class="content-header">
                <s:if test="%{configFlowFlag == 'manufacturing'}">
                    <h1>Routing Search<small>Manufacturing</small></h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                        <li class="active">  Routing Search</li>
                    </ol>
                </s:if><s:elseif test="%{configFlowFlag == 'logistics'}">
                    <h1>Routing Search<small>Logistics</small></h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-truck"></i>Logistics</a></li>
                        <li class="active">  Routing Search</li>
                    </ol>
                </s:elseif>
            </section>   
            <br>

            <section class="content">

                <div class="box box-primary">
                    <!--                    <div class="box-header with-border">
                                            <h3 class="box-title">Routing Search</h3>
                                            <div class="box-tools pull-right">
                    
                                            </div>
                                        </div>  -->
                    <div class="box-body">
                        <div id="text">
                            <div  style="alignment-adjust:central;" >
                                <%String contextPath = request.getContextPath();
                                %>



                                <s:form action="../routing/routingSearch.action" method="post" name="routingSearchForm" id="routingSearchForm" theme="simple">
                                    <s:hidden name="configFlowFlag" value="%{configFlowFlag}" id="configFlowFlag"/>
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <div class="row">
                                                    <div class="col-sm-3"> <label>Name </label>
                                                        <s:textfield cssClass="form-control" name="name" id="name" tabindex="1" value="%{name}"/>
                                                    </div>



                                                    <div  class="col-sm-3">

                                                        <label>Status</label> 
                                                        <s:select list="{'ACTIVE','INACTIVE'}" name="status" id="status" value="%{status}" tabindex="13" cssClass="form-control"/>
                                                    </div>

                                                    <div  class="col-sm-3">
                                                        <label>Acceptor Lookup Alias </label>  
                                                        <s:textfield cssClass="form-control" name="acceptorLookupAlias" id="acceptorLookupAlias" tabindex="2" value="%{acceptorLookupAlias}" />
                                                    </div>

                                                    <div  class="col-sm-3">
                                                        <label>Envelope </label>  
                                                        <s:select headerKey="" cssClass="form-control" headerValue="Select Type" list="#@java.util.LinkedHashMap@{'DIFFERED':'DIFFERED','IMMEDIATE':'IMMEDIATE'}" name="envelope" id="envelope" value="%{envelope}" tabindex="13" />
                                                    </div>


                                                </div>
                                                <br>
                                                <div class="row">
                                                    <div class="col-sm-3">
                                                        <label>Internal Route Email</label>
                                                        <s:textfield cssClass="form-control" name="internalRouteEmail" id="internalRouteEmail" value="%{internalRouteEmail}" tabindex="4" />
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <label>Destination Mailbox </label>
                                                        <s:textfield cssClass="form-control" name="destMailBox" id="destMailBox" value="%{destMailBox}" tabindex="5" /> 
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <label for="ackStatus">System Type</label>
                                                        <s:select list="#@java.util.LinkedHashMap@{'Not Applicable':'Not Applicable'}" cssClass="form-control" name="systemType" id="systemType" value="%{systemType}" tabindex="13" />
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <label for="status">Direction* </label>
                                                        <s:select list="#@java.util.LinkedHashMap@{'INBOUND':'INBOUND','OUTBOUND':'OUTBOUND'}" cssClass="form-control"  name="direction" id="direction" value="%{direction}" tabindex="13" />
                                                    </div>
                                                </div>
                                                <div id="loadingAcoountSearch" class="loadingImg">
                                                    <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                                </div>

                                            </div>


                                        </div>
                                        <br>
                                        <div class="row">

                                            <div class="col-sm-2"> <strong><input type="button" value="Add" class="btn btn-primary col-sm-12" tabindex="33" onclick="goToAddRouting();"/></strong></div>
                                            <div class="col-sm-2"><s:submit value="Search"   cssClass="btn btn-primary col-sm-12" tabindex="16"/></div>


                                            </td>
                                            <s:hidden name="sampleValue" id="sampleValue" value="2"/>

                                        </s:form>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>



                    <!-- Control Sidebar -->
                    <!-- /.control-sidebar -->
                    <!-- Add the sidebar's background. This div must be placed
                         immediately after the control sidebar -->


                    <!-- ./wrapper -->

            </section>

            <s:if test="#session.routingList != null"> 
                <%--- GRid start --%><section class="content">
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
                                                            java.util.List list = (java.util.List) session.getAttribute(AppConstants.SES_ROUTING_LIST);

                                                            if (list.size() != 0) {
                                                                RoutingBean routingBean;
                                                        %>
                                                        <thead>  <tr>
                                                                <th >Action</th>
                                                                <th >Name </th>
                                                                    <%--   <td >TP INBOUND PATH</td>
                                                                       <td >TP OUTBOUND PATH</td>  --%>
                                                                <th>AcceptorLookupAlias</th>
                                                                    <%-- <td>Envelope</td> --%>
                                                                <th>Direction </th>
                                                                <th>InternalRouteEmail </th>
                                                                <th>DestinationMailBox </th>
                                                                <th>SystemType</th>
                                                                <th>Status</th>
                                                                    <%--   <td>CreatedDate</td>
                                                                         <td>Changeddate</td> --%>
                                                            </tr></thead> <tbody>
                                                            <tr >

                                                                <%
                                                                    for (int i = 0; i < list.size(); i++) {
                                                                        routingBean = (RoutingBean) list.get(i);

                                                                        if (i % 2 == 0) {
                                                                            cssValue = "whiteStripe";
                                                                        } else {
                                                                            cssValue = "grayEditSelection";
                                                                        }
                                                                %>
                                                                <td style="text-align: left"><%-- <a href="#"> --%>
                                                                    <%
                                                                        int id = routingBean.getRoutingId();
                                                                    %>
                                                                    <s:url var="myUrl" action="../routing/routingEdit.action">
                                                                        <s:param name="routingId"><%=id%></s:param>
                                                                        <s:param name="configFlowFlag1" value="%{configFlowFlag}"></s:param>
                                                                    </s:url>

                                                                    <s:a href='%{#myUrl}' onmouseover="Tip('Click here to Edit Trading Partner.')" onmouseout="UnTip()">
                                                                        <img src="../includes/images/Edit.gif">
                                                                    </s:a>

                                                                    <%--  </a> --%>
                                                                </td>

                                                                <td style="text-align: left">
                                                                    <%
                                                                        if (routingBean.getName() != null && !"".equals(routingBean.getName())) {%>
                                                                    <a href="#" onclick="getRoutingDetails('<%=routingBean.getRoutingId()%>')" onmouseover="Tip('Click here to view Detail Info.')" onmouseout="UnTip()"> 
                                                                        <%
                                                                            out.println(routingBean.getName());
                                                                        %>
                                                                    </a> 
                                                                    <%
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>

                                                                <td style="text-align: left">
                                                                    <%
                                                                        if (routingBean.getAcceptorLookupAlias() != null && !"".equals(routingBean.getAcceptorLookupAlias())) {
                                                                            out.println(routingBean.getAcceptorLookupAlias());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <%-- <td style="text-align: left">
                                                                    <%
                                                            out.println(routingBean.getEnvelope());
                                                            %>
                                                                </td> --%>
                                                                <td style="text-align: left">
                                                                    <%
                                                                        if (routingBean.getDirection() != null && !"".equals(routingBean.getDirection())) {
                                                                            out.println(routingBean.getDirection());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>

                                                                <td style="text-align: left">
                                                                    <%
                                                                        if (routingBean.getInternalRouteEmail() != null && !"".equals(routingBean.getInternalRouteEmail())) {
                                                                            out.println(routingBean.getInternalRouteEmail());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td> <td style="text-align: left">
                                                                    <%
                                                                        if (routingBean.getDestMailBox() != null && !"".equals(routingBean.getDestMailBox())) {
                                                                            out.println(routingBean.getDestMailBox());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td> <td style="text-align: left">
                                                                    <%
                                                                        if (routingBean.getSystemType() != null && !"".equals(routingBean.getSystemType())) {
                                                                            out.println(routingBean.getSystemType());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td> <td style="text-align: left">
                                                                    <%
                                                                        if (routingBean.getStatus() != null && !"".equals(routingBean.getStatus())) {
                                                                            out.println(routingBean.getStatus());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td><%-- <td style="text-align: left">
                                                                    <%
                                                            out.println(routingBean.getCreatedDate());
                                                            %>
                                                                </td> <td style="text-align: left">
                                                                    <%
                                                            out.println(routingBean.getChangedDate());
                                                            %> 
                                                                </td>  --%>
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
                                            </tr>    </tbody>       
                                        </table>
                                    </div>
                                    <%-- Process butttons  start --%>
                                    <table align="left" 
                                           width="100%" border="0" cellpadding="0"
                                           cellspacing="0">


                                    </table>
                                    <%-- process buttons end--%>
                                    <%-- Grid End --%>

                                </div></section>

                                <div id="hide-menu1" class="hide-menu message ">

                                    <div class="row col-sm-12">

                                        <br>
                                        <div class="col-sm-6"> <label class="labelw"> Roter&nbsp;Name </label>
                                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="rrouterName" name="rrouterName"  readOnly="true"/>
                                        </div>
                                        <div class="col-sm-6"> <label class="labelw">Status </label>
                                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="rrouterStatus" name="rrouterStatus" readOnly="true"/>
                                        </div>
                                    </div>
                                    <div class="row col-sm-12"> 
                                        <div class="col-sm-6"> <label class="labelw">Acceptor Lookup Alias </label>
                                            <<s:textfield  cssClass="form-control"  required="required" placeholder="" id="racceptorLookUpAlias" name="racceptorLookUpAlias" readonly="true"/>
                                        </div>
                                        <div class="col-sm-6"> <label class="labelw">Internal Route Email </label>
                                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="rinternalRouteEmail" name="rinternalRouteEmail"  readonly="true"/>
                                        </div>
                                    </div>
                                    <div class="row col-sm-12">
                                        <div class="col-sm-6"> <label class="labelw">DestMailBox </label>
                                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="rdestMailBox" name="rdestMailBox" readonly="true"/>
                                        </div>
                                        <div class="col-sm-6"> <label class="labelw">SystemType </label>
                                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="rsystemType" name="rsystemType" readonly="true"/>
                                        </div>
                                    </div>
                                    <br>
                                    <div class="row col-sm-12">
                                        <div class="col-sm-6"> <label class="labelw"> Direction </label>
                                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="rdirection" name="rdirection" readonly="true"/>
                                        </div>
                                        <div class="col-sm-6"> <label class="labelw">Envelope </label>
                                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="renvelope" name="renvelope" readonly="true"/>
                                        </div>
                                    </div>

                                    <div class="row col-sm-12 clear">
                                        <div class="col-sm-6"> <label class="labelw">  CreatedDate  </label>
                                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="rcreatedDate" name="rcreatedDate" readonly="true"/>
                                        </div>
                                        <div class="col-sm-6"> <label class="labelw"> ChangedDate  </label>
                                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="rchangedDate" name="rchangedDate" readonly="true"/>
                                        </div>
                                    </div>
                                    <br><br>
                                    <br><br>
                                    <div id="noresult"></div>
                                    <div class="row col-sm-12" style="margin-top:10px;">  <button type="button" class="btn btn-primary col-sm-11" style="margin-left:12px; " id="hide-menu" onclick="hide()" value="X">Close</button></div>    

                                </div>

                            </s:if> 




                        </div></div>      
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
                <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
                <script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
                <script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>



                </body>


                </html>
