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

        <meta charset="utf-8">
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta http-equiv="pragma" content="no-cache" />
        <meta http-equiv="cache-control" content="no-cache" />
        <title>Miracle Supply Chain Visibility Portal</title>
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script type="text/javascript">
            function doOnLoad()
            {

                var configFlowFlag = $('#configFlowFlag').val();
                //alert('configFlowFlag'+configFlowFlag);
                if (configFlowFlag == 'manufacturing') {
                    $("#manufacturing").addClass("active");
                    $("#partner").addClass("active");
                    $("#config").addClass("active");
                    $("#partner i").addClass("text-red");
                } else if (configFlowFlag == 'logistics') {
                    $("#logistics").addClass("active");
                    $("#ltpartner").addClass("active");
                    $("#ltconfig").addClass("active");
                    $("#ltpartner i").addClass("text-red");
                }
                document.getElementById('loadingAcoountSearch').style.display = "none";

                // $("#config i").addClass("text-red");
            }

            function goToAddPartner() {
                var configFlowFlag = document.getElementById("configFlowFlag").value;
                //alert(configFlowFlag);
                window.location = "../partner/addPartner.action?configFlowFlag=" + configFlowFlag;
            }
            function resetvalues()
            {
                document.getElementById("partnerName").value = "";
                document.getElementById("status").value = "ACTIVE";
                document.getElementById("partnerIdentifier").value = "";
                document.getElementById("internalIdentifier").value = "";
                document.getElementById("applicationId").value = "";
                document.getElementById("countryCode").value = "";

                $('#gridDiv').hide();
                // alert("test");
            }
        </script>
        <script type="text/javascript">
            function hide()
            {

                $('#hide-menu1').removeClass('show-menu');
            }
            // $('html').click(function(e){
            //  $('#hide-menu1').removeClass('show-menu');
            //});
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

        <style>
            .row{
                margin-bottom: 5px; 
            }
        </style>

    </head>

    <%
        String check = null;
        if (request.getAttribute("check") != null) {
            check = request.getAttribute("check").toString();
        }

        //System.out.println("check-->"+check);
    %>
    <body class="hold-transition skin-blue sidebar-mini" onload="doOnLoad();
            setStyle('mainTp', 'partnerList')">

        <div>
            <s:include value="../includes/template/header.jsp"/>
        </div>
        <div>
            <s:include value="../includes/template/sidemenu.jsp"/>
        </div>


        <script type="text/javascript" src='<s:url value="/includes/js/wz_tooltip.js"/>'></script>
        <!-- Left side column. contains the logo and sidebar -->


        <!-- Content Wrapper. Contains page content -->
        <div class="content-wrapper">
            <!-- Content Header (Page header) -->

            <!--<nav>
                  <ul class="sf-menu" id="nav">
                   
            <%if (session.getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString().equals("2")) {%>
            <s:include value="/includes/template/orderToCashMenu.jsp"/>
            <%} else if (session.getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString().equals("3")) {%>
            <s:include value="/includes/template/logisticsMenu.jsp"/>
            <%}%>
            </ul> 
          </nav>--> 
            <!-- Main content --> 
            <s:if test="%{configFlowFlag == 'manufacturing'}">
                <section class="content-header">
                    <h1>Partner Search
                        <!--                        <small>Manufacturing</small>-->
                    </h1>
                    <!--                    <ol class="breadcrumb">
                                            <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                                            <li class="active">  Partner Search</li>
                                        </ol>-->
                </section>   
            </s:if><s:elseif test="%{configFlowFlag == 'logistics'}">
                <section class="content-header">
                    <h1>Partner Search</h1>
                    <!--                    <ol class="breadcrumb">
                                            <li><a href="#"><i class="fa fa-truck"></i>Logistics</a></li>
                                            <li class="active">  Partner Search</li>
                                        </ol>-->
                </section>
            </s:elseif>
            <section class="content">
                <div class="row">
                    <div class="col-sm-12"> 

                        <!--box-->
                        <div class="box box-primary">
                            <div class="box-header with-border">



                                <div class="box-tools pull-right">

                                </div>
                            </div><!-- /.box-header -->
                            <div class="box-body">
                                <div id="text">
                                    <!-- /.col (right) -->
                                    <!-- First Row -->

                                    <%
                                        if (request.getSession(false).getAttribute("responseString") != null) {
                                            String reqponseString = request.getSession(false).getAttribute("responseString").toString();
                                            request.getSession(false).removeAttribute("responseString");
                                            out.println(reqponseString);
                                        }
                                    %>
                                    <div  style="alignment-adjust:central;" >
                                        <%String contextPath = request.getContextPath();
                                        %>



                                        <s:form action="../partner/partnerSearch.action" method="post" name="partnerSearchForm" id="partnerSearchForm" theme="simple">
                                            <s:hidden name="configFlowFlag" value="%{configFlowFlag}" id="configFlowFlag"/>
                                            <div class="form-group">
                                                <div class="row">
                                                    <div class="col-sm-12">
                                                        <div class="row">
                                                            <div class="col-xs-3"> <label for="partnerName ">Partner Name </label>

                                                                <s:textfield cssClass="form-control" name="partnerName" id="partnerName"  value="%{partnerName}" onchange="fieldLengthValidator(this);" tabindex="1"/>
                                                            </div>



                                                            <div  class="col-xs-3">
                                                                <label for="status">Status</label> 
                                                                <s:select list="#@java.util.LinkedHashMap@{'ACTIVE':'ACTIVE','INACTIVE':'INACTIVE'}" name="status" id="status" value="%{status}"  cssClass="form-control" tabindex="2"/>
                                                            </div>

                                                            <div  class="col-xs-3">
                                                                <label for="internalIdentifier">Internal Identifier </label>  
                                                                <s:textfield cssClass="form-control" name="internalIdentifier" id="internalIdentifier"  value="%{internalIdentifier}" onchange="fieldLengthValidator(this);" tabindex="3"/>
                                                            </div>

                                                            <div  class="col-xs-3">
                                                                <label for="partnerIdentifier">Partner Identifier </label>  
                                                                <s:textfield cssClass="form-control" name="partnerIdentifier" id="partnerIdentifier"  value="%{partnerIdentifier}" onchange="fieldLengthValidator(this);makeUpperCase(this);" tabindex="4"/>
                                                            </div>
                                                            <div id="loadingAcoountSearch" class="loadingImg">
                                                                <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                                            </div>

                                                        </div>
                                                        <div class="row">
                                                            <div class="col-xs-3">
                                                                <label for="applicationId">Application ID</label>
                                                                <s:textfield cssClass="form-control" name="applicationId" id="applicationId" value="%{applicationId}"  onchange="fieldLengthValidator(this);" tabindex="5"/>
                                                            </div>
                                                            <div class="col-xs-3">
                                                                <label for="corrvalue">Country Code</label>
                                                                <s:textfield cssClass="form-control" name="countryCode" id="countryCode" value="%{countryCode}"  onchange="fieldLengthValidator(this);" tabindex="6"/>
                                                            </div>

                                                        </div>



                                                    </div>

                                                </div>
                                                <div>

                                                    <div class="row">
                                                        <div class="col-sm-2"> <strong><input type="button" value="Add" class="btn btn-primary col-sm-12" tabindex="7" onclick="goToAddPartner();"/></strong></div>
                                                        <div class="col-sm-2"><s:submit value="Search"  cssClass="btn btn-primary col-sm-12" tabindex="8"/></div>
                                                        <div class="col-sm-2"><strong><input type="button" value="Reset"  tabindex="9" class="btn btn-primary col-sm-12" onclick="return resetvalues();"/></strong></div>


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

            <div id="gridDiv">
                <s:if test="#session.partnerList != null"> 
                    <%--- GRid start --%>
                    <section class="content">
                        <div class="row">
                            <div class="col-xs-12">
                                <div class="box">
<!--                                    <div class="box-header">
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
                                                                java.util.List list = (java.util.List) session.getAttribute(AppConstants.SES_PARTNER_LIST);

                                                                if (list.size() != 0) {
                                                                    PartnerBean partnerBean;
                                                            %>
                                                            <thead>  <tr>
                                                                    <th >Action</th>
                                                                    <th >PartnerName </th>
                                                                    <th>InternalIdentifier</th>
                                                                    <th>PartnerIdentifier</th>
                                                                    <th>ApplicationId </th>
                                                                    <th>CountryCode </th>
                                                                    <th>Status </th>
                                                                    <th>CreatedDate </th> </tr></thead><tbody>
                                                                        <%--     <td>Changed Date</td>
                                                                             <td>ChangesUser </td> --%>



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
                                                                            String id = partnerBean.getPartnerIdentifier();
                                                                        %>
                                                                        <s:url var="myUrl" action="../partner/partnerEdit.action">
                                                                            <s:param name="partnerIdentifier"><%=id%></s:param>
                                                                            <s:param name="configFlowFlag1" value="%{configFlowFlag}"></s:param>
                                                                            <%--    <s:param name="tpname" value="%{tpname}"></s:param> --%>

                                                                        </s:url>

                                                                        <s:a href='%{#myUrl}' onmouseover="Tip('Click here to Edit Partner.')" onmouseout="UnTip()"><img src="../includes/images/Edit.gif"></s:a>

                                                                        <%--  </a> --%>
                                                                    </td>

                                                                    <td style="text-align: left">
                                                                        <%
                                                                            if (partnerBean.getPartnerName() != null && !"".equals(partnerBean.getPartnerName())) {%>
                                                                        <a href="#" onclick="getPartnerDetails('<%=partnerBean.getPartnerIdentifier()%>')" onmouseover="Tip('Click here to view Detail Info.')" onmouseout="UnTip()"> 
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
                                                                            if (partnerBean.getInternalIdentifier() != null && !"".equals(partnerBean.getInternalIdentifier())) {
                                                                                out.println(partnerBean.getInternalIdentifier());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>
                                                                    </td>
                                                                    <td style="text-align: left">
                                                                        <%
                                                                            if (partnerBean.getPartnerIdentifier() != null && !"".equals(partnerBean.getPartnerIdentifier())) {
                                                                                out.println(partnerBean.getPartnerIdentifier());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>
                                                                    </td>
                                                                    <td style="text-align: left">
                                                                        <%
                                                                            if (partnerBean.getApplicationId() != null && !"".equals(partnerBean.getApplicationId())) {
                                                                                out.println(partnerBean.getApplicationId());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>
                                                                    </td>
                                                                    <td style="text-align: left">
                                                                        <%
                                                                            if (partnerBean.getCountryCode() != null && !"".equals(partnerBean.getCountryCode())) {
                                                                                out.println(partnerBean.getCountryCode());
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
                                                                    </td> <td style="text-align: left">
                                                                        <%
                                                                            if (partnerBean.getCreatedDate() != null && !"".equals(partnerBean.getCreatedDate())) {
                                                                                out.println(partnerBean.getCreatedDate());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>
                                                                    </td><%-- <td style="text-align: left">
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
                                                    <!--               <td align="right" colspan="28" style="background-color: white;">
                                                                        <div align="right" id="pageNavPosition">hello</div>
                                                                 </td>-->
                                                </tr>   </tbody>        
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
                                            <div class="col-sm-6"> <label class="labelw"> Partner ID  </label>
                                                <s:textfield cssClass="form-control"  required="required" placeholder="" id="dpartnerId" name="dpartnerId"  readonly="true"/>
                                            </div>
                                            <div class="col-sm-6"><label class="labelw"> Partner Name  </label>
                                                <s:textfield cssClass="form-control"  required="required" placeholder="" id="dpartnerName" name="dpartnerName" readonly="true"/>
                                            </div>
                                        </div>
                                        <div class="row col-sm-12"> 
                                            <div class="col-sm-6"> <label class="labelw">  Internal Identifier  </label>
                                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="dinternalIdentifier" name="dinternalIdentifier" readonly="true"/>
                                            </div>
                                            <div class="col-sm-6"> <label class="labelw"> Application Id </label>
                                                <s:textfield   cssClass="form-control"  required="required" placeholder="" id="dapplicationId" name="dapplicationId" readonly="true"/>

                                            </div>
                                        </div>
                                        <div class="row col-sm-12">
                                            <div class="col-sm-6"> <label class="labelw"> State </label>
                                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="dcountryCode" name="dcountryCode" readonly="true"/>
                                            </div>
                                            <div class="col-sm-6"> <label class="labelw"> Created Date </label>
                                                <s:textfield cssClass="form-control"  required="required" placeholder="" id="dcreatedDate" name="dcreatedDate" readonly="true"/>
                                            </div>
                                        </div>

                                        <div class="row col-sm-12 clear">
                                            <div class="col-sm-6"> <label class="labelw"> Changed Date </label>
                                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="dchangedDate" name="dchangedDate" readonly="true"/>
                                            </div>
                                            <div class="col-sm-6"><label class="labelw">  Changed By </label>
                                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="dchangedBy" name="changedBy" readonly="true"/>
                                            </div>
                                        </div>

                                        <div id="noresult"></div>
                                        <div class="row col-sm-12" style="margin-top:10px;">  <button type="button" class="btn btn-primary col-sm-11" style="margin-left:12px; " id="hide-menu" onclick="hide()" value="X">Close</button></div>    


                                    </div>


                                </s:if> 
                            </div>
                        </div><!-- ./wrapper -->


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
