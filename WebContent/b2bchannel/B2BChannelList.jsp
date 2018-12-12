<!DOCTYPE html>
<%@page import="com.mss.ediscv.b2bchannel.B2BChannelBean"%>
<%@page import="com.mss.ediscv.tradingPartner.TradingPartnerBean"%>
<%@page import="com.mss.ediscv.tp.TpBean"%>
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
            function hide()
            {

                $('#hide-menu1').removeClass('show-menu');
            }
//            $('body,html').click(function(e){
//                $('#hide-menu1').removeClass('show-menu');
//            });

        </script> 



    </head>
    <%
        String check = null;
        if (request.getAttribute("check") != null) {
            check = request.getAttribute("check").toString();
        }
    %>
    <body class="hold-transition skin-blue sidebar-mini" onload="doOnLoad();
            setStyle('mainTp', 'b2bChannelList')">
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
                    <h1>B2B Channel Search<small>Manufacturing</small></h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                        <li class="active">  B2B Channel Search</li>
                    </ol>
                </s:if><s:elseif test="%{configFlowFlag == 'logistics'}">
                    <h1>B2B Channel Search<small>Logistics</small></h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-truck"></i>Logistics</a></li>
                        <li class="active">  B2B Channel Search</li>
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
                                                            <h3 class="box-title"> B2B Channel Search </h3>
                                                            <div class="box-tools pull-right">
                                                            </div>
                                                        </div> /.box-header -->
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
                                        <s:form action="../b2bchannel/b2BChannelListSearch.action" method="post" name="b2bchannelSearchForm" id="b2bchannelSearchForm" theme="simple">
                                            <s:hidden name="configFlowFlag" value="%{configFlowFlag}" id="configFlowFlag"/>
                                            <div class="form-group">
                                                <div class="row">
                                                    <div class="col-sm-12">
                                                        <div class="row">
                                                            <div class="col-sm-3"> <label for="partnerName ">Partner Name </label>
                                                                <s:textfield cssClass="form-control" name="partnerName" id="partnerName" tabindex="1" value="%{partnerName}"/>
                                                            </div>
                                                            <div  class="col-sm-3">
                                                                <label for="status">Status</label> 
                                                                <s:select headerKey="" cssClass="form-control" headerValue="Select Type" list="#@java.util.LinkedHashMap@{'ACTIVE':'ACTIVE','INACTIVE':'INACTIVE'}" name="status" id="status" value="%{status}" tabindex="13" />
                                                            </div>
                                                            <div  class="col-sm-3">
                                                                <label for="direction">Direction </label>  
                                                                <s:select headerKey="" cssClass="form-control" headerValue="Select Type" list="#@java.util.LinkedHashMap@{'INBOUND':'INBOUND','OUTBOUND':'OUTBOUND'}" name="direction" id="direction" value="%{direction}" tabindex="13" />
                                                            </div>
                                                            <div  class="col-sm-3">
                                                                <label for="protocol">Protocol </label>  
                                                                <s:select headerKey="" cssClass="form-control" headerValue="" list="#@java.util.LinkedHashMap@{'FTP Client GET':'FTP Client GET','FTP Client PUT':'FTP Client PUT','Inbound Mailbox Routing':'Inbound Mailbox Routing'}" name="protocol" id="protocol" value="%{protocol}" tabindex="13" />
                                                            </div>
                                                        </div>
                                                        <br>
                                                        <div class="row">
                                                            <div class="col-sm-3">
                                                                <label for="host">Host</label>
                                                                <s:textfield cssClass="form-control" name="host" id="host" value="%{host}" tabindex="4" />
                                                            </div>
                                                            <div class="col-sm-3">
                                                                <label for="userName">Username</label>
                                                                <s:textfield cssClass="form-control" name="userName" id="userName" value="%{userName}" tabindex="5" /> 
                                                            </div>
                                                            <div class="col-sm-3">
                                                                <label for="producerMailBox">Producer Mailbox</label>
                                                                <s:textfield cssClass="form-control" name="producerMailBox" id="producerMailBox" value="%{producerMailBox}" tabindex="5" /> 
                                                            </div>
                                                            <div class="col-sm-3">
                                                                <label for="consumerMailBox">Consumer Mailbox</label>
                                                                <s:textfield cssClass="form-control" name="consumerMailBox" id="consumerMailBox" value="%{consumerMailBox}" tabindex="5" /> 
                                                            </div>
                                                        </div>
                                                        <br>
                                                        <div class="row">
                                                            <div class="col-sm-3">
                                                                <label for="pollingCode">Polling Code</label>
                                                                <s:textfield cssClass="form-control" name="pollingCode" id="pollingCode" value="%{pollingCode}" tabindex="5" /> 
                                                            </div>
                                                            <div class="col-sm-3">
                                                                <label for="appId">App ID</label>
                                                                <s:textfield cssClass="form-control" name="appId" id="appId" value="%{appId}" tabindex="5" /> 
                                                            </div>
                                                            <div class="col-sm-3">
                                                                <label for="senderId">Sender Id</label>
                                                                <s:textfield cssClass="form-control" name="senderId" id="senderId" value="%{senderId}" tabindex="5" /> 
                                                            </div>
                                                            <div id="loadingAcoountSearch" class="loadingImg">
                                                                <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                                            </div>
                                                            <div class="col-sm-3">
                                                                <label for="receiverId">Receiver Id</label>
                                                                <s:textfield cssClass="form-control" name="receiverId" id="receiverId" value="%{receiverId}" tabindex="5" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div>
                                                    <br>
                                                    <div class="row">
                                                        <div class="col-sm-2"> <strong><input type="button" value="Add" class="btn btn-primary col-sm-12" tabindex="33" onclick="goToAddB2Bchannel();"/></strong></div>
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
                    <div class="col-sm-1"></div>
                </div>
            </section>
            <!--main content-->
            <!-- /.content-wrapper -->
            <s:if test="#session.b2bChannelList != null"> 
                <%--- GRid start --%><section class="content">
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="box">
                                <div class="box-header">
                                    <h3 class="box-title">Table</h3>
                                </div>
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
                                                            java.util.List list = (java.util.List) session.getAttribute(AppConstants.SES_B2BCHANNEL_LIST);
                                                            if (list.size() != 0) {
                                                                B2BChannelBean b2BChannelBean;
                                                        %>
                                                        <thead>  <tr>
                                                                <th>Action</th>
                                                                <th>PartnerName </th>
                                                                <th>Protocol</th>
                                                                <th>Site</th>
                                                                <th>Status </th>
                                                                <th>CreatedDate </th>
                                                                <th>ChangedDate </th>
                                                                <th>ChangedBy</th>
                                                            </tr></thead> <tbody>
                                                            <tr >
                                                                <%
                                                                    for (int i = 0; i < list.size(); i++) {
                                                                        b2BChannelBean = (B2BChannelBean) list.get(i);
                                                                        if (i % 2 == 0) {
                                                                            cssValue = "whiteStripe";
                                                                        } else {
                                                                            cssValue = "grayEditSelection";
                                                                        }
                                                                %>
                                                                <td style="text-align: left"><%-- <a href="#"> --%>
                                                                    <% int id = b2BChannelBean.getB2bChannelId();%>
                                                                    <s:url var="myUrl" action="../b2bchannel/b2bchannelEdit.action">
                                                                        <s:param name="b2bChannelId"><%=id%></s:param>
                                                                        <s:param name="configFlowFlag1" value="%{configFlowFlag}"></s:param>
                                                                    </s:url>
                                                                    <s:a href='%{#myUrl}' onmouseover="Tip('Click here to Edit Trading Partner.')" onmouseout="UnTip()">
                                                                        <img src="../includes/images/Edit.gif"></img>
                                                                    </s:a>
                                                                </td>
                                                                <td style="text-align: left">
                                                                    <%
                                                                        if (b2BChannelBean.getPartnerName() != null && !"".equals(b2BChannelBean.getPartnerName())) {%>
                                                                    <a href="#" onclick="getB2bDetailInformation('<%=b2BChannelBean.getB2bChannelId()%>')" onmouseover="Tip('Click here to view Detail Info.')" onmouseout="UnTip()"> 
                                                                        <% out.println(b2BChannelBean.getPartnerName());%>
                                                                    </a>
                                                                    <%
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td style="text-align: left">
                                                                    <%
                                                                        if (b2BChannelBean.getProtocol() != null && !"".equals(b2BChannelBean.getProtocol())) {
                                                                            out.println(b2BChannelBean.getProtocol());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td style="text-align: left">
                                                                    <%
                                                                        if (b2BChannelBean.getHost() != null && !"".equals(b2BChannelBean.getHost())) {
                                                                            out.println(b2BChannelBean.getHost());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td style="text-align: left">
                                                                    <%
                                                                        if (b2BChannelBean.getStatus() != null && !"".equals(b2BChannelBean.getStatus())) {
                                                                            out.println(b2BChannelBean.getStatus());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td style="text-align: left">
                                                                    <%
                                                                        if (b2BChannelBean.getCreatedDate() != null && !"".equals(b2BChannelBean.getCreatedDate())) {
                                                                            out.println(b2BChannelBean.getCreatedDate());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td> <td style="text-align: left">
                                                                    <%
                                                                        if (b2BChannelBean.getModifiedDate() != null && !"".equals(b2BChannelBean.getModifiedDate())) {
                                                                            out.println(b2BChannelBean.getModifiedDate());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td style="text-align: left">
                                                                    <%
                                                                        if (b2BChannelBean.getModifiedBy() != null && !"".equals(b2BChannelBean.getModifiedBy())) {
                                                                            out.println(b2BChannelBean.getModifiedBy());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                            </tr>
                                                            <%
                                                                }
                                                            } else {
                                                            %>
                                                            <tr><td>
                                                                    <%
                                                                            out.println("<img  border='0' align='top'  src='" + contextPath + "/includes/images/alert.gif'/><b> No Records Found to Display!</b>");
                                                                        }
                                                                    %>
                                                                </td>
                                                            </tr>
                                                    </table>

                                                </td>
                                            </tr>
                                            <tr>
                                            </tr>       
                                            </tbody>    
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
                                        <div class="col-sm-6"> <label class="labelw"> Partner Name  </label>
                                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="b2bpartnerName" name="b2bpartnerName"  readonly="true"/>
                                        </div>
                                        <div class="col-sm-6"><label class="labelw"> Status  </label>
                                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="b2bstatus" name="b2bstatus" readonly="true"/>
                                        </div>
                                    </div>
                                    <div class="row col-sm-12"> 
                                        <div class="col-sm-6"> <label class="labelw">  Direction  </label>
                                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="b2bdirection" name="b2bdirection" readonly="true"/>
                                        </div>
                                        <div class="col-sm-6"> <label class="labelw"> Protocol </label>
                                            <s:textfield   cssClass="form-control"  required="required" placeholder="" id="b2bprotocal" name="b2bprotocal" readonly="true"/>

                                        </div>
                                    </div>
                                    <div class="row col-sm-12">
                                        <div class="col-sm-6"> <label class="labelw"> Host </label>
                                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="b2bhost" name="b2bhost" readonly="true"/>
                                        </div>
                                        <div class="col-sm-6"> <label class="labelw"> UserName </label>
                                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="b2busername" name="b2busername" readonly="true"/>
                                        </div>
                                    </div>

                                    <div class="row col-sm-12 clear">
                                        <div class="col-sm-6"> <label class="labelw"> ProduceMailBox </label>
                                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="b2bproducermailbox" name="b2bproducermailbox" readonly="true"/>
                                        </div>
                                        <div class="col-sm-6"><label class="labelw"> ConsumerMailBox </label>
                                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="b2bconsumermailbox" name="b2bconsumermailbox" readonly="true"/>
                                        </div>
                                    </div>
                                    <div class="row col-sm-12 clear">
                                        <div class="col-sm-6"> <label class="labelw"> PoolingCode </label>
                                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="b2bpoolingcode" name="b2bpoolingcode" readonly="true"/>
                                        </div>
                                        <div class="col-sm-6"><label class="labelw"> AppId :</label>
                                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="b2bappid" name="b2bappid" readonly="true"/>
                                        </div>
                                    </div>
                                    <div class="row col-sm-12 clear">
                                        <div class="col-sm-6"> <label class="labelw"> SenderId </label>
                                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="b2bsenderid" name="b2bsenderid" readonly="true"/>
                                        </div>
                                        <div class="col-sm-6"><label class="labelw"> ReceiverId </label>
                                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="b2breceiverid" name="b2breceiverid" readonly="true"/>
                                        </div>
                                    </div>
                                    <div id="noresult"></div>
                                    <div class="row col-sm-12" style="margin-top:10px;">  <button type="button" class="btn btn-primary col-sm-11" style="margin-left:12px; " id="hide-menu" onclick="hide()" value="X">Close</button></div>    


                                </div>
                            </s:if> 
                        </div></div><!-- ./wrapper -->
                    <script type="text/javascript">
                        $(function () {
                            //$("#example1").DataTable();
                            $('#results').DataTable({
                                "paging": true,
                                "lengthChange": true,
                                "searching": true,
                                "ordering": true,
                                "info": true,
                                "autoWidth": false
                            });
                        });
                    </script>
                </div>
                <div>
                    <s:include value="../includes/template/footer.jsp"/>
                </div>   
                <script language="JavaScript" src='<s:url value="/includes/js/generalValidations.js"/>'></script>
                <script language="JavaScript" src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
                <script src='<s:url value="/includes/bootstrap/js/app.min.js"/>'></script>
                <script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
                <script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>
                <script type="text/javascript">
                        function doOnLoad()
                        {
                            var configFlowFlag = $('#configFlowFlag').val();
                            if (configFlowFlag == 'manufacturing') {
                                $("#manufacturing").addClass("active");
                                $("#b2bchannel").addClass("active");
                                $("#config").addClass("active");
                                $("#b2bchannel i").addClass("text-red");
                            } else if (configFlowFlag == 'logistics') {
                                $("#logistics").addClass("active");
                                $("#ltb2bchannel").addClass("active");
                                $("#ltconfig").addClass("active");
                                $("#ltb2bchannel i").addClass("text-red");
                            }
                            document.getElementById('loadingAcoountSearch').style.display = "none";
                        }

                        function goToAddB2Bchannel() {
                            var configFlowFlag = document.getElementById("configFlowFlag").value;
                            window.location = "../b2bchannel/addB2BChannel.action?configFlowFlag=" + configFlowFlag;
                        }
                </script>
                </body>
                </html>
