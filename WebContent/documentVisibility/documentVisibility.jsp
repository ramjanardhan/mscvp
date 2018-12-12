<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.mss.ediscv.documentVisibility.DocumentVisibilityBean"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page buffer="50kb" autoFlush="true" %>
<%@ taglib uri="/WEB-INF/tlds/dbgrid.tld" prefix="grd"%>
<%@ page import="com.freeware.gridtag.*"%>
<%@page import="java.sql.Connection"%>
<%@  page import="com.mss.ediscv.util.AppConstants"%>
<%@ page import="com.mss.ediscv.util.ConnectionProvider"%>
<%@ page import="java.sql.SQLException"%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Miracle Supply Chain Visibility Portal</title>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta http-equiv="pragma" content="no-cache" />
        <meta http-equiv="cache-control" content="no-cache" />
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <script type="text/javascript" src="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.js"></script>
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">
        <link rel="stylesheet" href='<s:url value="/includes/plugins/daterangepicker/daterangepicker.css"/>' type="text/css">
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script>
            function doOnLoad()
            {
                $("#dvdocrepository").addClass("active");
                // $("#documentvisibility").addClass("active");
                $("#dvdocrepository i").addClass("text-red");
                document.getElementById('loadingAcoountSearch').style.display = "none";
            }
        </script>

        <script type="text/javascript">

            /* close button script*/
            $(document).ready(function (c) {
                $('.alert-close').on('click', function (c) {
                    $('.message').fadeOut('slow', function (c) {
                        $('.message').remove();
                    });
                });
            });
        </script>
        <script>


            /*  $('#menu-button').click(function(e){
             alert("menu");
             e.stopPropagation();
             alert("menu3");
             $('#hide-menu').toggleClass('show-menu');
             });*/
            //                
            //            $('#hide-menu').click(function(e){
            //                $('#hide-menu1').removeClass('show-menu');
            //            });
            //

            function hide()
            {

                $('#hide-menu1').removeClass('show-menu');
            }
//            $('body,html').click(function(e){
//                $('#hide-menu1').removeClass('show-menu');
//            });

        </script>
        <style>
            .row{
                margin-bottom: 5px; 
            }
        </style>
    </head>

    <%!         String strStartGrid;
        String strEndGrid;
        String pathName;
        int resultCount = 0;
        int strIntStartGrid;
        int strIntEndGrid;
        List searchResult = null;
        int noOfPages = 0;
    %>
    <%
        int intCurr = 1;
        int intSortOrd = 0;
        String strTmp = null;
        String strSQL = null;
        String strSortCol = null;
        String strSortOrd = "ASC";
        boolean blnSortAsc = true;

        if (session.getAttribute(AppConstants.SES_LOG_DOC_LIST) != null) {

            strSQL = session.getAttribute(AppConstants.SES_LOG_DOC_LIST).toString();
        }
        Connection objCnn = null;
        Class objDrvCls = null;
        objCnn = ConnectionProvider.getInstance().getConnection();
        strTmp = request.getParameter("txtCurr");
        try {
            if (strTmp != null) {
                intCurr = Integer.parseInt(strTmp);
            }
        } catch (NumberFormatException NFEx) {
        }
        strSortCol = request.getParameter("txtSortCol");
        strSortOrd = request.getParameter("txtSortAsc");
        if (strSortCol == null) {
            strSortCol = "INSTANCEID";
        }
        if (strSortOrd == null) {
            strSortOrd = "ASC";
        }
        blnSortAsc = (strSortOrd.equals("ASC"));
        String check = null;
        if (request.getAttribute("check") != null) {
            check = request.getAttribute("check").toString();
        }

    %>

    <body class="hold-transition skin-blue sidebar-mini" onload="doOnLoad()">    
        <script type="text/javascript" src='<s:url value="/includes/js/wz_tooltip.js"/>'></script>
        <div>
            <s:include value="../includes/template/header.jsp"/>
        </div>
        <div>
            <s:include value="../includes/template/sidemenu.jsp"/>
        </div>
        <!-- Content Wrapper. Contains page content -->
        <div class="content-wrapper">
            <section class="content-header">
                <h1>Search Document Repository
                    <!--<small>Document Visibility</small>-->
                </h1>
                <!--                <ol class="breadcrumb">
                                    <li><a href="#"><i class="fa fa-book"></i>Document Visibility</a></li>
                                    <li class="active">Search Document Repository</li>
                                </ol>-->
            </section>



            <section class="content">

                <div class="box box-primary">
                    <!--                    <div class="box-header with-border">
                                            <h3 class="box-title">Search Document Repository</h3>
                                            <div class="box-tools pull-right">
                    
                                            </div>
                                        </div>  -->
                    <div class="box-body">
                        <div id="text">
                            <div  style="alignment-adjust:central;" >
                                <%String contextPath = request.getContextPath();
                                %>



                                <s:form action="../documentVisibility/docSearch.action" method="post" name="documentForm" id="documentForm" theme="simple">
                                    <s:hidden id="docdatepickerfrom" name="docdatepickerfrom" />
                                    <s:hidden id="docdatepicker" name="docdatepicker"/>
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <div class="row">
                                                    <div class="col-sm-3"> <label>Date Range</label>
                                                        <s:textfield name="reportrange"  id="reportrange" cssClass="form-control pull-left"   value="%{reportrange}" onchange="Date1();" tabindex="1" /> 
                                                    </div>
                                                    <script type="text/javascript">
        function Date1()
        {
            var date = document.documentForm.reportrange.value;
            var arr = date.split("-");
            var x = arr[1].trim();
            document.getElementById("docdatepickerfrom").value = arr[0];
            document.getElementById("docdatepicker").value = x;
        }
                                                    </script>
                                                    <div class="col-sm-3">
                                                        <label for="status">Status</label>

                                                        <s:select headerKey="-1" headerValue="Select Type" cssClass="form-control" list="{'Success','Error','Warning'}" name="status" id="status" value="%{status}" tabindex="2" /> 
                                                    </div>



<!--                                                    <div  class="col-sm-3">

                                                        <label>Sender Id</label> 
                                                        <s:textfield cssClass="form-control" name="docSenderId" id="docSenderId" value="%{docSenderId}" tabindex="3"/>

                                                    </div>

                                                    <div class="col-sm-3">
                                                        <label>Receiver Id</label>
                                                        <s:textfield cssClass="form-control" name="docReceiverId" id="docReceiverId" value="%{docBusId}" tabindex="4"/>

                                                    </div>-->
                                                        
                                                        
                                                        
                                                        <div  class="col-sm-3">
                                                        <label>Sender </label>  
                                                        <s:textfield onchange="setSenderId();"  cssClass="form-control" list="senderIdMap" name="docSen" id="docSen" value="%{docSenderId}" tabindex="5"/>
                                                        <%Map<String, String> senderIdMap = (Map) session.getAttribute(AppConstants.SENDERSID_MAP);

                                                        %>
                                                        <datalist id="senderIdMap">
                                                            <% for (Map.Entry<String, String> entry : senderIdMap.entrySet()) {%>
                                                            <option value="<%=entry.getValue()%>"/>
                                                            <%}%>
                                                        </datalist> 
                                                        <s:hidden name="docSenderId" id="docSenderId" />
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <label>Receiver </label>
                                                        <s:textfield onchange="setReceiverId();"  cssClass="form-control" list="receiverIdMap" name="docRec" id="docRec" value="%{docBusId}" tabindex="6"/>
                                                        <%Map<String, String> receiverIdMap = (Map) session.getAttribute(AppConstants.RECEIVERSId_MAP);

                                                        %>
                                                        <datalist id="receiverIdMap">
                                                            <% for (Map.Entry<String, String> entry : receiverIdMap.entrySet()) {%>
                                                            <option value="<%=entry.getValue()%>"/>
                                                            <%}%>
                                                        </datalist> 
                                                        <s:hidden name="docBusId" id="docBusId" />
                                                    </div>



                                                </div>

                                                <div id="loadingAcoountSearch" class="loadingImg">
                                                    <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                                </div>
                                                <div class="row">

                                                    <div class="col-sm-3">
                                                        <label for="ackStatus">Ack Status</label>
                                                        <s:select headerKey="-1" headerValue="Select Type" cssClass="form-control" list="{'Overdue','Accepted','Rejected'}" name="ackStatus" id="ackStatus" value="%{ackStatus}" tabindex="5"  /> 
                                                    </div>

                                                </div>



                                            </div>
                                        </div>


                                        <span id="span1">
                                        </span>
                                        <div class="row">

                                            <div class="col-sm-2"><s:submit value="Search"  onclick="return checkCorrelation();"   cssClass="btn btn-primary col-sm-12" tabindex="6"/></div>

                                            <div class="col-sm-2"><strong><input type="button" value="Reset"  tabindex="17" class="btn btn-primary col-sm-12" onclick="return resetvalues();" tabindex="7"/></strong></div>

                                            <s:hidden name="sampleValue" id="sampleValue" value="2"/>
                                        </div>
                                    </div>
                                </div>
                            </div></div></section>

                <div id="gridDiv">  

                    <s:if test="#session.searchResult != null"> 

                        <section class="content">

                            <div class="row">
                                <div class="col-xs-12">
                                    <div class="box">
                                        <!--                                    <div class="box-header">
                                                                                <h3 class="box-title">Table</h3>
                                                                            </div> /.box-header -->
                                        <div class="box-body">

                                            <div style="overflow-x:auto;">  
                                                <table align="center" width="710px"
                                                       border="1" cellpadding="0" cellspacing="0" class="table table-bordered table-hover">

                                                    <tbody>

                                                        <%
                                                            resultCount = 0;
                                                            if (session.getAttribute("searchResult") != null) {

                                                                searchResult = (List) session.getAttribute("searchResult");
                                                                //  out.println("searchResult size-->"+searchResult.size());
                                                                if (null != searchResult && searchResult.size() != 0) {
                                                                    resultCount = searchResult.size();
                                                                }
                                                            }
                                                        %>
                                                        <%
                                                            if (searchResult.size() != 0) {
                                                                noOfPages = Integer.parseInt(session.getAttribute("noOfPages").toString());
                                                        %>
                                                        <tr><td colspan="12">
                                                                <img src="/ediscv/includes/images/green.jpg"/>&nbsp;Success & Resubmitted&nbsp;&nbsp;

                                                                <img src="/ediscv/includes/images/blue.png"/>&nbsp;Success&nbsp;&nbsp;
                                                                <img src="/ediscv/includes/images/red.jpg"/>&nbsp;Error&nbsp;&nbsp; 


                                                                <img src="/ediscv/includes/images/pink.jpg"/>&nbsp;Error & Resubmitted&nbsp;&nbsp;
                                                            </td>
                                                        </tr>
                                                        <%}%> 

                                                    <input type="hidden" name="sec_lt_list" id="sec_lt_list" value="30"/> 


                                                    <tr class="gridHeader">

                                                        <th >SNO</th>
                                                        <th >InstanceId</th>
                                                        <th >FileType</th>

                                                        <th >Date&nbsp;Created</th>

                                                        <th >TransType</th>

                                                        <th >Sender Id</th>

                                                        <th >Receiver&nbsp;Id</th>

                                                        <th >IC&nbsp;#</th>
                                                        <th >FC&nbsp;#</th>
                                                        <th >MC&nbsp;#</th>

                                                    </tr>

                                                    <%

                                                        if (request.getAttribute("strStartGrid") != null) {
                                                            strStartGrid = request.getAttribute("strStartGrid").toString();
                                                            strIntStartGrid = Integer.parseInt(strStartGrid);
                                                        }

                                                        if (request.getAttribute("strEndGrid") != null) {
                                                            strEndGrid = request.getAttribute("strEndGrid").toString();
                                                            strIntEndGrid = Integer.parseInt(strEndGrid);
                                                        }

                                                    %>                                                    


                                                    <%                                                    if (session.getAttribute("searchResult") != null) {
                                                    %>
                                                    <input type="hidden" name="strIntStartGrid" id="strIntStartGrid" value="<%=strIntStartGrid%>"/> 
                                                    <input type="hidden" name="strIntEndGrid" id="strIntEndGrid" value="<%=strIntEndGrid%>"/> 
                                                    <%
                                                        List searchResult = (List) session.getAttribute("searchResult");

                                                        //resultCount = 0;
                                                        if (null != searchResult) {
                                                            resultCount = searchResult.size();
                                                        }

                                                        //if(request.getAttribute("strStartGrid") != "0"){
                                                        for (int i = strIntStartGrid, j = 0; i < strIntEndGrid; i++, j++) {
                                                            DocumentVisibilityBean documentVisibilityBean = (DocumentVisibilityBean) searchResult.get(i);

                                                    %>
                                                    <TR CLASS="gridRowEven">
                                                        <td ><%=i + 1%></td>
                                                        <td style="color:blue"><a style="color:#00B2FF;" href="javascript:getDetails('<%=documentVisibilityBean.getId()%>');">
                                                                <input type="hidden" name="Instance<%=i%>" id="Instance<%=i%>" value="<%=documentVisibilityBean.getInstanceId()%>"/>   
                                                                <input type="hidden" name="FileId<%=i%>" id="FileId<%=i%>" value="<%=documentVisibilityBean.getId()%>"/>  
                                                                <input type="hidden" name="text<%=i%>" id="text<%=i%>" value="<%=documentVisibilityBean.getTransaction_type()%>"/>
                                                                <%
                                                                    //out.println(logisticsDocBean.getFile_id());
                                                                    out.println(documentVisibilityBean.getInstanceId());
                                                                %>
                                                            </a>
                                                        </td>
                                                        <td >
                                                            <%
                                                                out.println(documentVisibilityBean.getFile_type());
                                                            %>

                                                        </td>                                  




                                                        <td>
                                                            <%
                                                                //out.println(logisticsDocBean.getDate_time_rec().toString().substring(0, logisticsDocBean.getDate_time_rec().toString().lastIndexOf(":")));
                                                                out.println(documentVisibilityBean.getDate_time_rec().toString().substring(2, documentVisibilityBean.getDate_time_rec().toString().lastIndexOf(":")));
                                                            %>

                                                        </td>   
                                                        <td >
                                                            <%
                                                                out.println(documentVisibilityBean.getTransaction_type());
                                                            %>

                                                        </td>
                                                        <td >

                                                            <%
                                                                out.println(documentVisibilityBean.getSenderId());
                                                            %>
                                                        </td>  



                                                        <td >


                                                            <%
                                                                out.println(documentVisibilityBean.getReceiverId());
                                                            %>
                                                        </td>

                                                        <td >
                                                            <%
                                                                out.println(documentVisibilityBean.getInterchange_ControlNo());
                                                            %>
                                                        </td>
                                                        <td>
                                                            <%
                                                                out.println(documentVisibilityBean.getFunctional_ControlNo());
                                                            %>
                                                        </td>
                                                        <td >
                                                            <%
                                                                out.println(documentVisibilityBean.getMessage_ControlNo());
                                                            %>
                                                        </td>

                                                    </TR>

                                                    <%
                                                        }
                                                    %>

                                                    <tr>
                                                        <td bgcolor="white" class="fieldLabelLeft" colspan="5">
                                                            <%if (strIntEndGrid != resultCount) {%>
                                                            Total Records : <%=resultCount%>&nbsp;Page <%=strIntEndGrid / 30%>  of <%=noOfPages%>
                                                            <%} else {%>
                                                            Total Records : <%=resultCount%>&nbsp;Page <%=noOfPages%>  of <%=noOfPages%>
                                                            <%}%>
                                                        </td>
                                                        <td colspan="4" align="right" bgcolor="white" ><%    if (searchResult.size() != 0) {%>


                                                            <strong><input type="button" name="First" id="First" value="<<" class="btn btn-effect-ripple btn-primary" 
                                                                           onclick="gridNext(this);" align="right"></strong>
                                                            <strong> <input type="button" name="Previous" id="Previous" value="<" class="btn btn-effect-ripple btn-primary"  
                                                                            onclick="gridNext(this);" align="right"> </strong>
                                                            (<%=strIntStartGrid + 1%> - <%=strIntEndGrid%> of <%=resultCount%>)
                                                            <strong><input type="button" name="Next" id="Next" value=">" class="btn btn-effect-ripple btn-primary" 
                                                                           onclick="gridNext(this);" align="right"></strong>
                                                            <strong><input type="button" name="Last" id="Last" value=">>" class="btn btn-effect-ripple btn-primary"  
                                                                           onclick="gridNext(this);" align="right"></strong>

                                                            <s:select list="pageList" name="pageNumber" id="pageNumber" headerKey="select" headerValue="select" onchange="goToPage();" />

                                                            <%}%>

                                                        </td>
                                                    </tr>
                                                    <%
                                                        }
                                                    %>

                                                    <input type="hidden" name="txtStartGrid" value="<%=strStartGrid%>"/>
                                                    <input type="hidden" name="txtEndGrid" value="<%=strEndGrid%>"/>
                                                    <input type="hidden" name="txtMaxGrid" value="<%=resultCount%>"/>

                                                    </tbody>
                                                </table>
                                                <div id="resubmitLoading" align="center" style="display:none">

                                                    <font color="red">Loading...Please wait..</font>
                                                </div>
                                            </div>
                                            <%
                                                if (searchResult.size() != 0) {
                                            %>
                                            <table align="right" border="0">
                                                <tr>
                                                    <td  class="gnexcel-space" style="background-color: white;">
                                                        <strong><input type="button" value="Generate Excel" class="btn btn-effect-ripple btn-primary" onclick="return gridDownload('docVisibility', 'xls');" onmouseover="Tip('Click here to generate an excel Report.')" onmouseout="UnTip()" id="excel"/></strong>
                                                    </td>
                                                </tr>
                                            </table> 
                                            <%}%>
                                        </div></div>
                                </div> </section>
                            </s:if>
                        </s:form>
            </div>



            <%-- Side box starts--%>


            <div id="hide-menu1" class="hide-menu message ">

                <div class="row col-sm-12">

                    <br>
                    <div class="col-sm-6"> <label class="labelw"> Instance Id </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="DocInstanceid" name="DocInstanceid" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">Parent File Id </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="DocParentFileId" name="DocParentFileId" readonly="true"/>
                    </div>
                </div>
                <div class="row col-sm-12"> <br>
                    <div class="col-sm-6"> <label class="labelw"> File Type </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="DocFileType" name="DocFileType" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">File Origin</label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="DocFileOrigin" name="DocFileOrigin" readonly="true"/>
                    </div>
                </div>
                <div class="row col-sm-12">
                    <div class="col-sm-6"> <label class="labelw"> Transaction Type </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="DocTranMessType" name="DocTranMessType" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">IC # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="DocInterchangeControlNo" name="DocInterchangeControlNo" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">SCAC # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="DocAppField0" name="DocAppField0" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">SPLCODE # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="DocAppField1" name="DocAppField1" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">BOLNUMBER # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="DocAppField2" name="DocAppField2" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">TOTALWEIGHT # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="DocAppField3" name="DocAppField3" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">CHARGE # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="DocAppField4" name="DocAppField4" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">TOTALPIECES # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="DocAppField5" name="DocAppField5" readonly="true"/>
                    </div>
                </div>
                <div class="col-sm-6"> <label class="labelw">IFLAG # </label>
                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="DocAppField6" name="DocAppField6" readonly="true"/>
                </div>

                <br>
                <div id="senderinfo">
                    <div class="row col-sm-12">
                        <div class="col-sm-6"> <h4 style="color:blue">Sender Info :</h4></div>
                        <div class="col-sm-6"></div>
                        <div class="col-sm-6"></div>

                    </div>
                    <br>
                    <div class="row col-sm-12">
                        <div class="col-sm-6"> <label class="labelw">  Id </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="DocSenderid" name="DocSenderid" readonly="true"/>
                        </div>

                    </div>
                </div>
                <br>
                <div id="receiverinfo">
                    <div class="row col-sm-12">
                        <div class="col-sm-6"> <h4 style="color:blue">Receiver Info:</h4></div>
                        <div class="col-sm-6"></div>
                        <div class="col-sm-6"></div>
                    </div>

                    <br>
                    <div class="row col-sm-12 clear">
                        <div class="col-sm-6"> <label class="labelw">  Id </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="DocReceiverid" name="DocReceiverid" readonly="true"/>
                        </div>

                    </div>

                </div>
                <div class="row col-sm-12 clear">
                    <div class="col-sm-6"> <label class="labelw"> FC # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="DocFunctionalControlNo" name="DocFunctionalControlNo" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw"> MC # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="DocMessageControlNo" name="DocMessageControlNo" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">  Date Time #</label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="DocDateTimeReceived" name="DocDateTimeReceived" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw"> Direction #</label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="DocDirection" name="DocDirection" readonly="true"/>
                    </div>

                    <div class="col-sm-6"> <label class="labelw">  Ack Status # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="DocAckStatus" name="DocAckStatus" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">Id</label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="DocId" name="DocId" readonly="true"/>
                    </div>

                    <div class="col-sm-6"> <label class="labelw">  ISA TIME # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="DocIsaTime" name="DocIsaTime" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">  ISA Date # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="DocIsaDate" name="DocIsaDate" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw"> STATUS </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="DocDetailInfoStatus" name="DocDetailInfoStatus" readonly="true"/>
                    </div>
                </div>
                <div class="row col-sm-12" >
                    <div class="col-sm-6"> <label class="labelw"> Error Message </label></div>
                    <div id="errormessage"></div>
                </div>
                <div id="noresult"></div>
                <div class="col-sm-12">  <button type="button" class="btn btn-primary col-sm-11" id="hide-menu" onclick="hide()" value="X">Close</button></div>
            </div>

            <%-- Side box ends--%>


        </div>
        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div>
        <script>
            $('input[name="daterange"]').daterangepicker();
        </script>
        <script language="JavaScript" src='<s:url value="/includes/js/DateValidation.js"/>'></script>
        <script language="JavaScript" src='<s:url value="/includes/js/GridNavigation.js"/>'></script>
        <script language="JavaScript" src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
        <script language="JavaScript" src='<s:url value="/includes/js/downloadAjax.js"/>'></script>

        <script src='<s:url value="../includes/plugins/daterangepicker/daterangepicker.js"/>'></script>
        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>

        <script>
            $(function () {
                $('#attach_box').click(function () {
                    $('#sec_box').show();
                    return false;
                });
            });
            $(function () {
                $('#detail_link').click(function () {
                    $('#detail_box').show();
                    return false;
                });
            });
            function demo() {
                $(function () {
                    $('#detail_box').show();
                    return false;
                });
            }
            function getDetails(val) {
                getDocVisibilityDetails(val);
            }
            function checkCorrelation() {
                var res = compareDates(document.getElementById('docdatepickerfrom').value, document.getElementById('docdatepicker').value);
                return res;
            }
            function resetvalues()
            {
                $('#hide-menu1').removeClass('show-menu');
                document.getElementById('docdatepickerfrom').value = "";
                document.getElementById('docdatepicker').value = "";
                document.getElementById('docSen').value = "";
                document.getElementById('docRec').value = "";
                document.getElementById('status').value = "-1";
                document.getElementById('ackStatus').value = "-1";
                document.getElementById('reportrange').value = "";
                $('#detail_box').hide();
                $('#gridDiv').hide();
            }
            function doNavigate(pstrWhere, pintTot)
            {
                var strTmp;
                var intPg;
                strTmp = document.documentForm.txtCurr.value;
                intPg = parseInt(strTmp);
                if (isNaN(intPg))
                    intPg = 1;

                if ((pstrWhere == 'F' || pstrWhere == 'P') && intPg == 1)
                {
                    alert("You are already viewing first page!");
                    return;
                }
                else if ((pstrWhere == 'N' || pstrWhere == 'L') && intPg == pintTot)
                {
                    alert("You are already viewing last page!");
                    return;
                }
                if (pstrWhere == 'F')
                    intPg = 1;
                else if (pstrWhere == 'P')
                    intPg = intPg - 1;
                else if (pstrWhere == 'N')
                    intPg = intPg + 1;
                else if (pstrWhere == 'L')
                    intPg = pintTot;

                if (intPg < 1)
                    intPg = 1;
                if (intPg > pintTot)
                    intPg = pintTot;
                document.documentForm.txtCurr.value = intPg;
                document.documentForm.submit();
            }
            function doSort(pstrFld, pstrOrd)
            {
                document.documentForm.txtSortCol.value = pstrFld;
                document.documentForm.txtSortAsc.value = pstrOrd;
                document.documentForm.submit();
            }
            function goToPage(element) {
                document.documentForm.txtCurr.value = element.options[element.selectedIndex].value;
                document.documentForm.submit();
            }
            function gridNext(c) {
                var b = c.id;
                //alert('hi----'+b);
                //alert("The alert -->"+document.documentForm.txtStartGrid.value);
                //alert('hi----'+b);
                var e = parseInt(document.documentForm.txtStartGrid.value);
                //alert('how----'+e);
                var a = parseInt(document.documentForm.txtEndGrid.value);
                //alert('who----'+a);
                var d = parseInt(document.documentForm.txtMaxGrid.value);
                //alert('Next'+d);
                if (b == "Next") {
                    // alert('Next'+b);
                    if (a < d)
                    {
                        document.location = "nextConsultantResume.action?startValue=" + e + "&endValue=" + a + "&button=" + b
                    } else {
                        if (a == d) {
                            alert("You are already viewing last page!")
                        }
                    }
                } else {
                    if (b == "Previous")
                    {
                        // alert('hi'+b);
                        if (e < d && e > 0) {
                            document.location = "nextConsultantResume.action?startValue=" + e + "&endValue=" + a + "&button=" + b
                        } else {
                            if (e == 0) {
                                alert("You are already viewing first page!")
                            }
                        }
                    } else {
                        if (b == "First") {
                            //alert('hi'+b);
                            if (e < d && e > 0) {
                                e = 0;
                                a = 30;
                                document.location = "nextConsultantResume.action?startValue=" + e + "&endValue=" + a + "&button=" + b
                            } else {
                                if (e == 0) {
                                    alert("You are already viewing first page!")
                                }
                            }
                        } else {
                            if (b == "Last") {
                                //alert('hi'+b);
                                if (a < d) {
                                    e = d - 30;
                                    a = d;
                                    document.location = "nextConsultantResume.action?startValue=" + e + "&endValue=" + a + "&button=" + b
                                } else {
                                    if (a == d) {
                                        alert("You are already viewing last page!")
                                    }
                                }
                            }
                        }
                    }
                }
            }
            function goToPage() {
                var pageNumber = document.getElementById('pageNumber').value;
                var b = "Select";
                var startValue = ((parseInt(pageNumber) - 1) * 30);
                var endValue = parseInt(startValue) + 30;
                document.location = "nextConsultantResume.action?startValue=" + startValue + "&endValue=" + endValue + "&button=" + b

            }
            
            
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
