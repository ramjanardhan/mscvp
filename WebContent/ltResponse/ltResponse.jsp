
<%@page import="java.util.Map"%>
<%@page import="com.mss.ediscv.ltResponse.LtResponseBean"%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page buffer="50kb" autoFlush="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%@page import="com.mss.ediscv.doc.DocRepositoryBean"%>
<%@ taglib uri="/WEB-INF/tlds/dbgrid.tld" prefix="grd"%>
<%@ page import="com.freeware.gridtag.*"%>
<%@page import="java.sql.Connection"%>
<%@  page import="com.mss.ediscv.util.AppConstants"%>
<%@ page import="com.mss.ediscv.util.ConnectionProvider"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import = "java.util.ResourceBundle" %>
<%--<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>--%>
<%@page buffer="50kb" autoFlush="true" %>

<!DOCTYPE html>
<html class=" js canvas canvastext geolocation crosswindowmessaging no-websqldatabase indexeddb hashchange historymanagement draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow opacity cssanimations csscolumns cssgradients no-cssreflections csstransforms no-csstransforms3d csstransitions  video audio localstorage sessionstorage webworkers applicationcache svg smil svgclippaths   fontface">
    <head>
        <title>Miracle Supply Chain Visibility portal</title>

        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta http-equiv="pragma" content="no-cache" />
        <meta http-equiv="cache-control" content="no-cache" />
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">
        <link rel="stylesheet" href='<s:url value="/includes/plugins/daterangepicker/daterangepicker.css"/>' type="text/css"> 
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script type="text/javascript">
            function doOnLoad()
            {
                $("#ltresponse").addClass("active");
                $("#logistics").addClass("active");
                $("#ltresponse i").addClass("text-red");
                document.getElementById('loadingAcoountSearch').style.display = "none";
            }
        </script>
        <script type="text/javascript">
            function hide()
            {
                $('#hide-menu1').removeClass('show-menu');
            }
            //            $('body,html').click(function(e){
            //                $('#hide-menu1').removeClass('show-menu');
            //               
            //            });

            $(function () {
                // $("#example1").DataTable();
                $('#results').DataTable({
                    "paging": true,
                    "lengthChange": true,
                    "searching": true,
                    "ordering": true,
                    "info": true,
                    "autoWidth": false,
                    "scrollY": 300,
                    "scrollX": true,
                    order: [[0, 'desc']]
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
    <body onload="checkCorr();
            doOnLoad()" class="hold-transition skin-blue sidebar-mini">
        <script type="text/javascript" src='<s:url value="/includes/js/wz_tooltip.js"/>'></script>
        <div>
            <s:include value="../includes/template/header.jsp"/>
        </div>
        <div>
            <s:include value="../includes/template/sidemenu.jsp"/>
        </div>
        <div class="content-wrapper">
            <section class="content-header">
                <h1>
                    Response
                    <!--<small>Logistics</small>-->
                </h1>
                <!--                <ol class="breadcrumb">
                                    <li><a href="#"><i class="fa fa-truck"></i>Logistics</a></li>
                                    <li class="active">Response</li>
                                </ol>-->
            </section>
            <section class="content">

                <div class="box box-primary">

                    <div class="box-body">
                        <div id="text">


                            <div  style="alignment-adjust:central;" >
                                <%String contextPath = request.getContextPath();
                                %>

                                <s:form action="../ltResponse/doSearchltResponse.action" method="post" name="ltResponseForm" id="ltResponseForm" theme="simple">
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <div class="row">
                                                    <div class="col-sm-3"><label>Database&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;</label>
                                                        <s:radio cssClass="myRadio" id="database" name="database" value="%{database}" list="#@java.util.LinkedHashMap@{'MSCVP':'LIVE','ARCHIVE':'ARCHIVE'}"/>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-sm-4"> <label>Date Range</label>
                                                        <s:textfield name="reportrange"  id="reportrange" cssClass="form-control pull-left"   value="%{reportrange}" onchange="Date1();" tabindex="1"/> 
                                                    </div>

                                                    <script type="text/javascript">
        function Date1() {
            var date = document.ltResponseForm.reportrange.value;
            var arr = date.split("-");
            var x = arr[1].trim();
            document.getElementById("datepickerfrom").value = arr[0];
            document.getElementById("datepickerTo").value = x;
        }
                                                    </script>

                                                    <s:hidden id="datepickerfrom" name="datepickerfrom" />
                                                    <s:hidden id="datepickerTo" name="datepickerTo"/>
                                                    <div  class="col-sm-4">

                                                        <label>Document Type</label> 
                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="docTypeList" name="docType" id="docType" value="%{docType}" tabindex="2"/>
                                                    </div>


                                                    <div class="col-sm-4">
                                                        <label for="status">Status</label>
                                                        <s:select headerKey="-1" headerValue="Select Type" cssClass="form-control" list="{'Success','Error','Warning'}" name="status" id="status" value="%{status}" tabindex="7" /> 
                                                    </div>



                                                </div>

                                                <div class="row">
                                                    <!--                                                    <div  class="col-sm-3">
                                                                                                            <label>Sender Name</label>  
                                                    <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="senderNameList" name="senderName" id="senderName" value="%{senderName}" tabindex="4" />
                                                </div>
                                                <div class="col-sm-3">
                                                    <label>Receiver Name</label>
                                                    <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="receiverNameList" name="receiverName" id="receiverName" value="%{receiverName}" tabindex="6" />
                                                </div>-->
                                                    <!--                                                        <div class="col-sm-3">
                                                                                                                <label for="ackStatus">Ack Status</label>
                                                    <%--<s:select headerKey="-1" headerValue="Select Type" cssClass="form-control" list="{'Overdue','Accepted','Rejected'}" name="ackStatus" id="ackStatus" value="%{ackStatus}"  /> --%>
                                                </div>-->


                                                    <div  class="col-sm-4">
                                                        <label>Sender </label>  
                                                        <s:textfield onchange="setSenderId();"  cssClass="form-control" list="senderIdMap" name="docSen" id="docSen" value="%{senderId}" tabindex="5"/>
                                                        <%Map<String, String> senderIdMap = (Map) session.getAttribute(AppConstants.SENDERSID_MAP);

                                                        %>
                                                        <datalist id="senderIdMap">
                                                            <% for (Map.Entry<String, String> entry : senderIdMap.entrySet()) {%>
                                                            <option value="<%=entry.getValue()%>"/>
                                                            <%}%>
                                                        </datalist> 
                                                        <s:hidden name="senderId" id="senderId" />
                                                        
                                                    </div>
                                                    <div class="col-sm-4">
                                                        <label>Receiver </label>
                                                        <s:textfield onchange="setReceiverId();"  cssClass="form-control" list="receiverIdMap" name="docRec" id="docRec" value="%{receiverId}" tabindex="6"/>
                                                        <%Map<String, String> receiverIdMap = (Map) session.getAttribute(AppConstants.RECEIVERSId_MAP);

                                                        %>
                                                        <datalist id="receiverIdMap">
                                                            <% for (Map.Entry<String, String> entry : receiverIdMap.entrySet()) {%>
                                                            <option value="<%=entry.getValue()%>"/>
                                                            <%}%>
                                                        </datalist> 
                                                        <s:hidden name="receiverId" id="receiverId" />
                                                    </div>

                                                </div>
                                                <div class="row">
                                                    <div class="col-sm-3">
                                                        <label for="corrattribute">Correlation</label>
                                                        <s:select headerKey="-1" headerValue="Select Attribute" cssClass="form-control" list="correlationList" name="corrattribute" id="corrattribute" value="%{corrattribute}" tabindex="8" />
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <label for="corrvalue">Value</label>
                                                        <s:textfield cssClass="form-control" name="corrvalue" id="corrvalue" value="%{corrvalue}" tabindex="9"/>
                                                    </div>
                                                    <div class="col-sm-3"><br>
                                                        <button  type="button" id="addButton" name="addButton" value="Add Div" class="btn btn-success"   style="margin-top:6px ;" tabindex="10"><i class="fa fa-plus"></i></button>
                                                        &nbsp; <label>Add Filter</label>
                                                    </div>
                                                </div>
                                                <script>

                                                </script>                                      

                                                <script>
                                                    var count = 0;
                                                </script>                                          

                                                <div id="corr" style="display: none">
                                                    <div class="row">
                                                        <div class="col-sm-3">
                                                            <label for="corrattribute1">Correlation</label>
                                                            <s:select headerKey="-1" headerValue="Select Attribute" cssClass="form-control" list="correlationList" name="corrattribute1" id="corrattribute1" value="%{corrattribute1}" tabindex="11"/>
                                                        </div>
                                                        <div class="col-sm-3">
                                                            <label for="corrvalue1">Value</label>
                                                            <s:textfield cssClass="form-control" name="corrvalue1" id="corrvalue1" value="%{corrvalue1}" tabindex="12"/>
                                                        </div>
                                                        <div class="col-sm-2"><br>
                                                            <button  type="button" id="removeButton1" name="removeButton1" value="Remove Div" class="btn btn-warning"   style="margin-top:6px ;" tabindex="14"><i class="fa fa-minus"></i></button>
                                                            &nbsp; <label>Remove Filter</label>
                                                        </div>
                                                    </div>
                                                </div>

                                            </div>
                                        </div>
                                        <div id="loadingAcoountSearch" class="loadingImg">
                                            <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                        </div>

                                        <span id="span1">
                                        </span>
                                        <div class="row">

                                            <div class="col-sm-2"><s:submit value="Search"  onclick="return checkCorrelation();" cssClass="btn btn-primary col-sm-12" tabindex="13"/></div>

                                            <div class="col-sm-2"><strong><input type="button" value="Reset"  tabindex="14" class="btn btn-primary col-sm-12" onclick="return resetvalues();"/></strong></div>

                                            <s:hidden name="sampleValue" id="sampleValue" value="2"/>

                                        </s:form>
                                    </div>
                                </div>
                            </div>
                        </div></div>
            </section>
            <div id="gridDiv">     

                <s:if test="#session.ltResponseList != null"> 
                    <%--- GRid start --%>
                    <section class="content">
                        <div class="row">
                            <div class="col-xs-12">
                                <div class="box">
                                    <!--                                    <div class="box-header">
                                                                            <h3 class="box-title">Table</h3>
                                                                        </div> /.box-header -->
                                    <div class="box-body">
                                        <%!String cssValue = "whiteStripe";
                                            int resultsetTotal;%>
                                        <div style="overflow-x:auto;">      
                                            <table align="left" width="100%"
                                                   border="0" cellpadding="0" cellspacing="0" >
                                                <tr>
                                                    <td style="background-color: white;">
                                                        <div style="overflow-x:auto;">      
                                                            <table id="results"  class="table table-bordered table-hover">
                                                                <%
                                                                    java.util.List list = (java.util.List) session.getAttribute(AppConstants.SES_LTRESPONSE_LIST);

                                                                    if (list.size() != 0) {
                                                                        LtResponseBean ltResponseBean;
                                                                %>
                                                                <thead><tr>
                                                                        <%-- <td >ISA #</td>
                                                                         <td >File Format</td>
                                                                         <td>Direction</td>
                                                                         <td >Date</td>
                                                                         <td>Status</td>  --%>
                                                                        <th >FileFormat</th> 
                                                                        <th >InstanceId</th>
                                                                        <th >OrderId</th>
                                                                        <th >Shipment</th>


                                                                        <%--   <th >DATETIME</th>
                                                                            <th >ISA #</th>  --%>

                                                                        <%-- <th >DOC_ORIGIN</th> --%>
                                                                        <th >TransType</th>
                                                                        <th >Direction</th>

                                                                        <th >Status</th>


                                                                    </tr>
                                                                </thead>
                                                                <tbody>
                                                                    <tr>

                                                                        <%
                                                                            for (int i = 0; i < list.size(); i++) {
                                                                                ltResponseBean = (LtResponseBean) list.get(i);

                                                                                if (i % 2 == 0) {
                                                                                    cssValue = "whiteStripe";
                                                                                } else {
                                                                                    cssValue = "grayEditSelection";
                                                                                }
                                                                        %>
                                                                        <td>
                                                                            <%
                                                                                if (ltResponseBean.getFileType() != null && !"".equals(ltResponseBean.getFileType())) {
                                                                                    out.println(ltResponseBean.getFileType());
                                                                                } else {
                                                                                    out.println("-");
                                                                                }
                                                                            %>

                                                                        </td>
                                                                        <td>
                                                                            <%
                                                                                if (ltResponseBean.getFileId() != null && !"".equals(ltResponseBean.getFileId())) {
                                                                                    out.println(ltResponseBean.getFileId());
                                                                                } else {
                                                                                    out.println("-");
                                                                                }
                                                                            %>

                                                                        </td>

                                                                        <td><a style="color: deepskyblue" href="javascript:getLtResponseDetails('<%=ltResponseBean.getFileId()%>','<%=ltResponseBean.getRefId()%>');">
                                                                                <%
                                                                                    if (ltResponseBean.getRefId() != null && !"".equals(ltResponseBean.getRefId())) {

                                                                                        out.println(ltResponseBean.getRefId());

                                                                                    } else {
                                                                                        out.println("-");
                                                                                    }

                                                                                %>
                                                                            </a>     
                                                                        </td>



                                                                        <td>
                                                                            <%                                                                                if (ltResponseBean.getShipmentId() != null && !"".equals(ltResponseBean.getShipmentId())) {
                                                                                    out.println(ltResponseBean.getShipmentId());
                                                                                } else {
                                                                                    out.println("-");
                                                                                }


                                                                            %>

                                                                        </td>
                                                                        <td>
                                                                            <%                                                                                if (ltResponseBean.getTransType() != null && !"".equals(ltResponseBean.getTransType())) {
                                                                                    out.println(ltResponseBean.getTransType());
                                                                                } else {
                                                                                    out.println("-");
                                                                                }
                                                                            %>

                                                                        </td>
                                                                        <td>
                                                                            <%
                                                                                if (ltResponseBean.getDirection() != null && !"".equals(ltResponseBean.getDirection())) {
                                                                                    out.println(ltResponseBean.getDirection());
                                                                                } else {
                                                                                    out.println("-");
                                                                                }
                                                                            %>

                                                                        </td>  


                                                                        <td>
                                                                            <%
                                                                                if (ltResponseBean.getStatus() != null && !"".equals(ltResponseBean.getStatus())) {
                                                                                    if (ltResponseBean.getStatus().equalsIgnoreCase("ERROR")) {
                                                                                        out.println("<font color='red'>" + ltResponseBean.getStatus().toUpperCase() + "</font>");
                                                                                    } else if (ltResponseBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                                                                                        out.println("<font color='green'>" + ltResponseBean.getStatus().toUpperCase() + "</font>");
                                                                                    } else {
                                                                                        out.println("<font color='orange'>" + ltResponseBean.getStatus().toUpperCase() + "</font>");
                                                                                    }
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
                                                                                    // String contextPath = request.getContextPath();
                                                                                    // out.println("<img  border='0' align='top'  src='"+contextPath+"/includes/images/alert.gif'/><b> No Records Found to Display!</b>");
                                                                                    out.println("<img  border='0' align='top'  src='" + contextPath + "/includes/images/alert.gif'/><b>No records found for the given search criteria. Please try a different search criteria!</b>");
                                                                                }

                                                                            %>
                                                                        </td>
                                                                    </tr>

                                                            </table>
                                                    </td>
                                                </tr>
                                                <%                                                    if (list.size() != 0) {
                                                %>
                                                <tr>
                                                    <!--                                                                        <td align="right" colspan="28" style="background-color: white;">
                                                                                                                                <div align="right" id="pageNavPosition"></div>-->
                                                    </td>
                                                </tr> 

                                                <% }%>   </tbody>
                                            </table>
                                        </div>

                                        <%-- Process butttons  start --%>
                                        <%
                                            if (list.size() != 0) {
                                        %>
                                        <table align="right">
                                            <tr>
                                                <td class="gnexcel-space" style="background-color: white;">
                                                    <strong><input type="button" value="Generate Excel" class="btn btn-effect-ripple btn-primary" onclick="return gridDownload('ltResponse', 'xls');" onmouseover="Tip('Click here to generate an excel Report.')" onmouseout="UnTip()" id="excel"/></strong>
                                                </td>
                                            </tr>
                                        </table> 
                                    </div>



                                </div>
                                <%}%>
                                <%-- process buttons end--%>
                                <%-- Grid End --%>
                            </div>
                        </div></div>
            </section>

            <div id="hide-menu1" class="hide-menu message">

                <div class="row col-sm-12">

                    <br>
                    <div class="col-sm-6"> <label class="labelw"> Instance Id </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="resInstanceid" name="resInstanceid" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw"> Shipment # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="resShipment" name="resShipment" readonly="true"/>
                    </div>
                </div>
                <div class="row col-sm-12"> 
                    <div class="col-sm-6"> <label class="labelw"> Document Type </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="resFiletype" name="resFiletype" readonly="true" />
                    </div>
                    <div class="col-sm-6"> <label class="labelw">Transaction Type </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="resTransactiontype" name="resTransactiontype"  readonly="true"/>
                    </div>
                </div> 
                <div class="row col-sm-12"> 
                    <div class="col-sm-6"> <label class="labelw"> OrderId </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="resReference" name="resReference" readonly="true"/>
                    </div>
                </div>

                <div id="senderinfo"><br>
                    <div class="row col-sm-12">
                        <div class="col-sm-6"> <h4 style="color: deepskyblue">Sender Info :</h4></div>
                        <div class="col-sm-6"></div>
                        <div class="col-sm-6"></div>

                    </div>
                    <br>
                    <div class="row col-sm-12">
                        <div class="col-sm-6"> <label class="labelw">  Id </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="resSenderid" name="resSenderid" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw"> Name </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="resSendername" name="resSendername" readonly="true"/>
                        </div>
                    </div>
                </div>
                <br>
                <div id="receiverinfo"><br>
                    <div class="row col-sm-12">
                        <div class="col-sm-6"> <h4 style="color: deepskyblue">Receiver Info:</h4></div>
                        <div class="col-sm-6"></div>
                        <div class="col-sm-6"></div>
                    </div>
                    <br>
                    <div class="row col-sm-12 clear">
                        <div class="col-sm-6"> <label class="labelw">  Id </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="resReceiverid" name="resReceiverid" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw"> Name </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="resReceivername" name="resReceivername" readonly="true"/>
                        </div>
                    </div>
                </div>
                <div class="row col-sm-12 clear">
                    <div class="col-sm-6"> <label class="labelw">  ISA #   </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="resIsa" name="resIsa" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw"> GS # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="resGs" name="resGs" readonly="true"/>
                    </div>
                </div>

                <div class="row col-sm-12"  >
                    <div class="col-sm-6"> <label class="labelw"> ST #   </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="resSt" name="resSt" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">ISA Date</label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="resIsadate" name="resIsadate" readonly="true"/>
                    </div>

                    <div class="col-sm-6"> <label class="labelw">ISA Time </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="resIsatime" name="resIsatime" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw"> STATUS </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="resStatus" name="resStatus" readonly="true"/>
                    </div>

                </div>

                <div class="row col-sm-12" style="margin-top:10px;">
                    <div class="col-sm-6"> <label class="labelw">Pre Translation</label></div>
                    <div class="col-sm-6">   <div id="resPreTranslation" ></div></div>
                </div>
                <div class="row col-sm-12" >
                    <div class="col-sm-6"> <label class="labelw">Post Translation </label></div>
                    <div class="col-sm-6"> <div id="resPostTranslation"></div></div>
                </div>
                <div class="row col-sm-12" >
                    <div class="col-sm-6"> <label class="labelw">997 AckFile</label></div>
                    <div class="col-sm-6"> <div id="resAckfileid"></div></div>
                </div>

                <div class="row col-sm-12" id="errorDiv" style="display: none">
                    <div class="col-sm-6"> <label class="labelw"> Error&nbsp;Message </label></div>
                    <div class="col-sm-6" id="resErrormessage" style="color: red"></div>
                </div>

                <div class="row col-sm-12" id="errorReportDiv" style="display: none">
                    <div class="col-sm-6"> <label class="labelw">  Error&nbsp;Report </label></div>
                    <div class="col-sm-6"><div id="ErrReport"></div></div>
                </div>

                <div id="noresult"></div>
                <div class="col-sm-12" style="margin-top:10px;">  <button type="button" class="btn btn-primary col-sm-11" id="hide-menu" onclick="hide()" value="X">Close</button></div>
            </div>
        </div>
    </s:if> 
</div>



</div>
<div>
    <s:include value="../includes/template/footer.jsp"/>
</div>
<script>
    $('input[name="daterange"]').daterangepicker();
</script>
<script language="JavaScript" src='<s:url value="/includes/js/DateValidation.js"/>'></script>
<script language="JavaScript" src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
<script language="JavaScript" src='<s:url value="/includes/js/downloadAjax.js"/>'></script>
<!-- Bootstrap 3.3.5 -->
<script src='<s:url value="../includes/plugins/daterangepicker/daterangepicker.js"/>'></script>
<script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
<script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
<script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>

<script type="text/javascript">

    //    function getDetails(fileId, refId)
    //    {
    //        //  alert("hiiii");    
    //
    //        getLtResponseDetails(fileId, refId);
    //    }
    function checkCorrelation() {
        var db = document.forms["ltResponseForm"]["database"].value;
        if (db == '') {
            alert("Please select Database!!!");
            return false;
        }
        var corrattr = document.getElementById('corrattribute').value;
        var corrval = document.getElementById('corrvalue').value;

        var corrattr1 = document.getElementById('corrattribute1').value;
        var corrval1 = document.getElementById('corrvalue1').value;
        if ((corrattr != "-1") && (corrval == "")) {
            alert("Please enter Correlation Value !!");
            return false;
        }
        if ((corrattr == "-1") && (corrval != "")) {
            alert("Please select Correlation !!");
            return false;
        }

        if ((corrattr1 != "-1") && (corrval1 == "")) {
            alert("Please enter Correlation Value !!");
            return false;
        }
        if ((corrattr1 == "-1") && (corrval1 != "")) {
            alert("Please select Correlation !!");
            return false;
        }




    }
    function resetvalues()
    {
        $('#hide-menu1').removeClass('show-menu');
        // $('.myRadio').attr('checked', false);
        document.getElementById('datepickerfrom').value = "";
        document.getElementById('datepickerTo').value = "";
        document.getElementById('reportrange').value = "";
        document.getElementById('docSen').value = "";

        document.getElementById('docRec').value = "";
        //  document.getElementById('receiverName').value = "";
        document.getElementById('senderId').value = ""
        document.getElementById('receiverId').value = "";
        document.getElementById('corrattribute').value = "-1";
        document.getElementById('corrvalue').value = "";
        document.getElementById('docType').value = "-1";
        document.getElementById('corrattribute1').value = "-1";
        document.getElementById('corrvalue1').value = "";
        document.getElementById('reportrange').value = "";
        document.getElementById('status').value = "-1";
        var elements = document.getElementsByName('database');
        elements[0].checked = true;
        $('#gridDiv').hide();
        $('#corr').hide();
        count = 0;
    }

    function checkCorr()
    {
        var value1 = document.getElementById("corrattribute1").value;

        if (value1 != "-1")
            document.getElementById("corr").style.display = "block";
        else
            document.getElementById("corr").style.display = "none";

    }

    $("#addButton").click(function () {
        count++;
        if (count == 1) {
            document.getElementById("corr").style.display = "block";
        } else {
            if (document.getElementById("corr").style.display == "none") {
                count = 0;
            } else {
                count = 1;
                alert('Limit exceeded.... cannot add more fields !!');
            }
        }
    })
    $("#removeButton1").click(function () {
        document.getElementById('corrattribute1').value = "-1";
        document.getElementById('corrvalue1').value = "";
        document.getElementById("corr").style.display = "none";
        count = count - parseInt("1");
    })


    function setSenderId() {
        var id = document.getElementById("docSen").value;
        var arr = id.split("(");
        document.getElementById("docSenderId").value = arr[1].slice(0, -1);
        //alert(document.getElementById("docSenderId").value);
    }
    function setReceiverId() {
        var id = document.getElementById("docRec").value;
        var arr = id.split("(");
        document.getElementById("docBusId").value = arr[1].slice(0, -1);
        //alert(document.getElementById("docBusId").value);
    }



</script>

</body>


</html>
