<%@page import="com.mss.ediscv.editracking.TrackInOutBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.mss.ediscv.util.DataSourceDataProvider"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.mss.ediscv.util.AppConstants"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%-- <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> --%>
<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page buffer="50kb" autoFlush="true" %>
<html>
    <head>

        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>Miracle Supply Chain Visibility Portal</title>
        <!-- Tell the browser to be responsive to screen width -->
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <link rel="stylesheet" href='<s:url value="/includes/plugins/daterangepicker/daterangepicker.css"/>' type="text/css"> 
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script>
            function doOnLoad()
            {
                $("#editrackinginout").addClass("active");
                $("#manufacturing").addClass("active");
                $("#reports").addClass("active");
                $("#editrackinginout i").addClass("text-red");
                document.getElementById('loadingAcoountSearch').style.display = "none";
            }
        </script>
        <script type="text/javascript">
            $(function() {
                // $("#example1").DataTable();
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
            function resetvalues()
            {

                document.getElementById('docdatepickerfrom').value = "";
                document.getElementById('docdatepicker').value = "";
                document.getElementById('reportrange').value = "";

                $('#gridDiv').hide();

            }
        </script>
        <style>



            /*plus_minus for grid */
            .plus_minus
            {
                background-image: url('../includes/images/plus.png'); 
                width: 14px; 
                background-size: 14px 14px; 
                height: 14px; 
                margin-bottom: -14px;
            }
            .plus_minus_anchor
            {
                position: relative;
                left: 40px;
            }
            .CSSTableGenerator {
                margin:0px;padding:0px;
                /**width:100%;*/
                box-shadow: 10px 10px 5px #888888;
                border:1px solid #000000;

                -moz-border-radius-bottomleft:0px;
                -webkit-border-bottom-left-radius:0px;
                border-bottom-left-radius:0px;

                -moz-border-radius-bottomright:0px;
                -webkit-border-bottom-right-radius:0px;
                border-bottom-right-radius:0px;

                -moz-border-radius-topright:0px;
                -webkit-border-top-right-radius:0px;
                border-top-right-radius:0px;

                -moz-border-radius-topleft:0px;
                -webkit-border-top-left-radius:0px;
                border-top-left-radius:0px;
            }.CSSTableGenerator table{
                width:100%;
                height:100%;
                margin:0px;padding:0px;
            }.CSSTableGenerator tr:last-child td:last-child {
                -moz-border-radius-bottomright:0px;
                -webkit-border-bottom-right-radius:0px;
                border-bottom-right-radius:0px;
            }
            .CSSTableGenerator table tr:first-child td:first-child {
                -moz-border-radius-topleft:0px;
                -webkit-border-top-left-radius:0px;
                border-top-left-radius:0px;
            }
            .CSSTableGenerator table tr:first-child td:last-child {
                -moz-border-radius-topright:0px;
                -webkit-border-top-right-radius:0px;
                border-top-right-radius:0px;
            }.CSSTableGenerator tr:last-child td:first-child{
                -moz-border-radius-bottomleft:0px;
                -webkit-border-bottom-left-radius:0px;
                border-bottom-left-radius:0px;
            }.CSSTableGenerator tr:hover td{

            }
            .CSSTableGenerator tr:nth-child(odd){ background-color:#e5e5e5; }
            .CSSTableGenerator tr:nth-child(even)    { background-color:#ffffff; }.CSSTableGenerator td{
                vertical-align:middle;


                border:1px solid #000000;
                border-width:0px 1px 1px 0px;
                text-align:center;
                padding:6px;
                font-size:12px;
                font-family:Verdana;
                font-weight:normal;
                color:#000000;
            }.CSSTableGenerator tr:last-child td{
                border-width:0px 1px 0px 0px;
            }.CSSTableGenerator tr td:last-child{
                border-width:0px 0px 1px 0px;
            }.CSSTableGenerator tr:last-child td:last-child{
                border-width:0px 0px 0px 0px;
            }
            .CSSTableGenerator tr:first-child td{
                background:-o-linear-gradient(bottom, #cccccc 5%, #b2b2b2 100%);	background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #cccccc), color-stop(1, #b2b2b2) );
                background:-moz-linear-gradient( center top, #cccccc 5%, #b2b2b2 100% );
                filter:progid:DXImageTransform.Microsoft.gradient(startColorstr="#cccccc", endColorstr="#b2b2b2");	background: -o-linear-gradient(top,#cccccc,b2b2b2);

                background-color:#cccccc;
                border:1px solid #000000;
                text-align:center;
                border-width:0px 0px 1px 1px;
                font-size:12px;
                font-family:Verdana;
                font-weight:bold;
                color:#000000;
            }
            .CSSTableGenerator tr:first-child:hover td{
                background:-o-linear-gradient(bottom, #cccccc 5%, #b2b2b2 100%);	background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #cccccc), color-stop(1, #b2b2b2) );
                background:-moz-linear-gradient( center top, #cccccc 5%, #b2b2b2 100% );
                filter:progid:DXImageTransform.Microsoft.gradient(startColorstr="#cccccc", endColorstr="#b2b2b2");	background: -o-linear-gradient(top,#cccccc,b2b2b2);

                background-color:#cccccc;
            }
            .CSSTableGenerator tr:first-child td:first-child{
                border-width:0px 0px 1px 0px;
            }
            .CSSTableGenerator tr:first-child td:last-child{
                border-width:0px 0px 1px 1px;
            }

        </style>
        <style>
            .row{
                  margin-bottom: 5px; 
            }
            </style>
    </head>

    <%--<body onload="doOnLoad();initDateTime('docdatepickerfrom','docdatepicker','<%=check %>');setStyle('docRep','');">  --%>

    <body class="hold-transition skin-blue sidebar-mini" onload="doOnLoad();">
        <script type="text/javascript" src='<s:url value="/includes/js/wz_tooltip.js"/>'></script>

        <div>
            <s:include value="../includes/template/header.jsp"/>       

        </div>
        <div>
            <s:include value="../includes/template/sidemenu.jsp"/>
        </div>	

        <!-- Start Special Centered Box -->

        <!-- end Special Centered Box -->



        <div id="sidebar_container">


            <div id="detail_box" style="display: none;"> 
                <div class="sidebar">
                    <h3>Detail Information</h3>
                    <div class="sidebar_item">

                        <div id="loadingImage" align="center"><img  src="../includes/images/ajax-loader.gif" /></div>

                        <h5 id="detailInformation"></h5>

                    </div>
                </div>


                <div class="sidebar_base"></div>
            </div>
        </div>

        <!-- Content Wrapper. Contains page content -->
        <div class="content-wrapper">
            <!-- Content Header (Page header) -->  
            <section class="content-header">
                <h1>
                    EDI Tracking IN/OUT
                    <!--                    <small>Manufacturing</small>-->
                </h1>
                <!--                <ol class="breadcrumb">
                                    <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                                    <li class="active">EDI Tracking IN/OUT</li>
                                </ol>-->
            </section>
           
            <section class="content">
                <div class="row">
                    <div class="col-sm-12"> 

                        <!--box-->
                        <div class="box box-primary">
                            <div class="box-header with-border">
                                <%String contextPath = request.getContextPath();
                                %> 
                                <div class="box-tools pull-right">

                                </div>
                            </div><!-- /.box-header -->
                            <div class="box-body">
                                <div id="text">

                                    <!-- /.col (right) -->
                                    <!-- First Row -->

                                    <s:form action="../reports/trackInOutSearch.action" method="post" name="trackInOutForm" id="trackInOutForm" theme="simple">

                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-3"> <label>Date Range</label><font style="color:red">*</font>
                                                        <s:textfield name="reportrange"  id="reportrange" cssClass="form-control pull-left"   value="%{reportrange}"  tabindex="1"/> 
                                                </div>
                                                <div id="loadingAcoountSearch" class="loadingImg">
                                                    <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                                </div>
                                                <s:hidden id="docdatepickerfrom" name="docdatepickerfrom" />
                                                <s:hidden id="docdatepicker" name="docdatepicker"/>

                                            </div>
                                           
                                            <div class="row">
                                                <div class="col-sm-2"><s:submit value="Search"  onclick="return Date1();" cssClass="btn btn-primary col-sm-12" tabindex="2"/></div>
                                                <div class="col-sm-2">   <strong><input type="button" value="Reset" class="btn btn-primary col-sm-12"  onclick="return resetvalues();" tabindex="3"/></strong></div>

                                            </div>
                                            <s:hidden name="sampleValue" id="sampleValue" value="2"/>

                                        </s:form>
                                    </div>
                                </div></div>




                        </div>



                    </div>
                    <%--  out.print("contextPath-->"+contextPath); --%>
                </div>
            </section>


            <div id="gridDiv">
                <s:if test="#session.documentReportList != null"> 
                    <%--- GRid start --%>
                    <section class="content">



                        <div class="row">
                            <div class="col-xs-12">
                                <div class="box">
<!--                                    <div class="box-header">
                                        <h3 class="box-title">Table</h3>
                                    </div> /.box-header -->
                                    <div class="box-body">

                                        <div class="content_item">
                                            <%!String cssValue = "whiteStripe";
                                                int resultsetTotal;
                                                int total = 0;
                                                int inboundTotal = 0;
                                                int outboundTotal = 0;
                                                List docTypeList = new ArrayList();

                                            %>
                                            <%
                                                try {
                                                    docTypeList = DataSourceDataProvider.getInstance().getDocumentTypeList("M");
                                                } catch (Exception e) {
                                                }
                                            %>
                                            <div style="overflow-x:auto;">                 

                                                <table align="left" width="100%"
                                                       border="0" cellpadding="0" cellspacing="0" >
                                                    <tr>
                                                        <td style="background-color: white;">


                                                            <%
                                                                java.util.List list = (java.util.List) session.getAttribute(AppConstants.SES_DOCREPORT_LIST);

                                                                if (list.size() != 0) {

                                                                    TrackInOutBean trackInOutBean;

                                                                    trackInOutBean = (TrackInOutBean) list.get(0);
                                                                    ArrayList inboundList = trackInOutBean.getInboundList();
                                                                    ArrayList outboundList = trackInOutBean.getOutboundList();
                                                                    ArrayList docType = trackInOutBean.getDocumentTypeList();
                                                                    ArrayList dateMonth = trackInOutBean.getDateMonth();
                                                                    ArrayList dateMonthdocType = trackInOutBean.getDateMonthdocType();
                                                                    int inbounddocTotal = 0;
                                                                    int outbounddocTotal = 0;
                                                                    int docTotal = 0;
                                                                    int allTotal = 0;
                                                                    System.out.println("doctypelist=" + docType + "dateMonthdocType=" + dateMonthdocType + "dateMonth" + dateMonth + "inbountList=" + inboundList + "outboundList=" + outboundList);
                                                                    if (!docType.isEmpty() && !dateMonthdocType.isEmpty()) {
                                                            %>
                                                            <div id="track_in_out">EDI Tracking IN\OUT</div>
                                                        </td>
                                                    <tr><td>
                                                            <table align="left" id="results" border="0" cellpadding="0" cellspacing="0" class="table table-bordered table-hover">
                                                                <tr>
                                                                    <th >TRANS TYPE</th> 
                                                                    <th >Direction</th>

                                                                </tr>
                                                                <%
                                                                    for (int i = 0; i < docType.size(); i++) {
                                                                        if (i % 2 == 0) {
                                                                            cssValue = "whiteStripe";
                                                                        } else {
                                                                            cssValue = "grayEditSelection";
                                                                        }
                                                                %>
                                                                <tr>
                                                                    <td rowspan="2" id="doctype<%=docType.get(i)%>" style="text-align: left;">
                                                                        <%
                                                                            if (inboundList.contains(docType.get(i)) && outboundList.contains(docType.get(i))) {
                                                                        %>
                                                                        <a class="plus_minus_anchor" href="javascript:toggle('<%=docType.get(i)%>',4,'total<%=i%>')"  >
                                                                            <div id="total<%=i%>" class="plus_minus"></div>
                                                                        </a>
                                                                        <%
                                                                        } else {
                                                                        %>
                                                                        <a class="plus_minus_anchor" href="javascript:toggle('<%=docType.get(i)%>',3,'total<%=i%>')" >
                                                                            <div id="total<%=i%>" class="plus_minus" ></div>
                                                                        </a>
                                                                        <%
                                                                            }
                                                                        %>
                                                                        <span style="margin-left: 60px;">
                                                                            <%
                                                                                out.println(docType.get(i).toString().trim());
                                                                            %></span>
                                                                    </td>
                                                                </tr>
                                                                <%
                                                                    if (inboundList.contains(docType.get(i))) {
                                                                %>
                                                                <tr id="inbound<%=docType.get(i)%>" style="display: none">
                                                                    <td>Inbound</td>
                                                                </tr>
                                                                <%
                                                                    }
                                                                    if (outboundList.contains(docType.get(i))) {
                                                                %>
                                                                <tr id="outbound<%=docType.get(i)%>" style="display: none">
                                                                    <td>Outbound</td>
                                                                </tr> 
                                                                <%
                                                                    }
                                                                %>
                                                                <tr><th>Total</th></tr>
                                                                        <%
                                                                            }
                                                                        %>
                                                                <tr><td colspan="2" >Total</td></tr>
                                                                <%-- <td >Status</td>--%>
                                                            </table>
                                                        </td><td>
                                                            <%
                                                                for (int j = 0; j < dateMonthdocType.size(); j++) {
                                                            %>
                                                            <table align="left" id="results" border="0" cellpadding="0" cellspacing="0" class="table table-bordered table-hover" style="border-left: 0px;">

                                                                <%
                                                                    ArrayList temp = (ArrayList) dateMonthdocType.get(j);
                                                                    ArrayList olddoctype = new ArrayList();
                                                                    int total = 0;
                                                                    //System.out.println("list"+temp.get(0));
                                                                %>
                                                                <tr>
                                                                    <th><%=temp.get(0)%></th>
                                                                </tr>
                                                                <%
                                                                    for (int i = 0; i < docType.size(); i++) {
                                                                        for (int l = 1; l < temp.size(); l = l + 4) {
                                                                            System.out.println("temp=" + temp.size() + "l=" + l);
                                                                            System.out.println("temp=" + temp.get(l));
                                                                            if (temp.get(l).equals(docType.get(i))) {
                                                                                System.out.println("doc type" + docType.get(i) + "temp=" + temp.get(l));
                                                                                if (inboundList.contains(docType.get(i))) {
                                                                %>
                                                                <tr class="inboundvalue<%=docType.get(i)%>" style="display: none"> 
                                                                    <td ><%=temp.get(l + 1)%></td>
                                                                </tr>
                                                                <%
                                                                    }
                                                                    if (outboundList.contains(docType.get(i))) {
                                                                %>
                                                                <tr class="outboundvalue<%=docType.get(i)%>" style="display: none"> 
                                                                    <td ><%=temp.get(l + 2)%></td>
                                                                </tr>
                                                                <%
                                                                    }
                                                                %>
                                                                <tr> 
                                                                    <td><%=temp.get(l + 3)%></td>
                                                                </tr>
                                                                <%
                                                                    total = (Integer) temp.get(l + 3) + total;
                                                                } else {
                                                                    if (!temp.contains(docType.get(i)) && !olddoctype.contains((String) docType.get(i))) {
                                                                        olddoctype.add((String) docType.get(i));
                                                                        //System.out.println("else doc type"+docType.get(i)+"temp="+temp.get(l)+"olddoctype="+olddoctype);
                                                                        if (inboundList.contains(docType.get(i))) {
                                                                %>
                                                                <tr class="inboundvalue<%=docType.get(i)%>" style="display: none"> 
                                                                    <td >0</td>
                                                                </tr>
                                                                <%
                                                                    }
                                                                    if (outboundList.contains(docType.get(i))) {
                                                                %>
                                                                <tr class="outboundvalue<%=docType.get(i)%>" style="display: none">  
                                                                    <td >0</td>
                                                                </tr>
                                                                <%
                                                                    }
                                                                %>
                                                                <tr> 
                                                                    <td >0</td>
                                                                </tr>
                                                                <%
                                                                                }
                                                                            }
                                                                        }
                                                                        // System.out.println("list"+temp.get(l));
                                                                    }
                                                                %>
                                                                <tr> 
                                                                    <td ><%=total%></td>
                                                                </tr>


                                                            </table></td><td>
                                                            <%
                                                                }
                                                            %>
                                                            <table align="left" id="results" border="0" cellpadding="0" cellspacing="0" class="	table table-bordered table-hover" style="border-left: 0px;">
                                                                <tr>
                                                                    <th>Total</th>
                                                                </tr>
                                                                <%
                                                                    for (int k = 0; k < docType.size(); k++) {
                                                                        for (int i = 0; i < dateMonthdocType.size(); i++) {
                                                                            ArrayList temp1 = (ArrayList) dateMonthdocType.get(i);
                                                                            for (int l = 1; l < temp1.size(); l = l + 4) {
                                                                                if (temp1.get(l).equals(docType.get(k))) {
                                                                                    inbounddocTotal = (Integer) temp1.get(l + 1) + inbounddocTotal;
                                                                                    outbounddocTotal = (Integer) temp1.get(l + 2) + outbounddocTotal;
                                                                                    docTotal = (Integer) temp1.get(l + 3) + docTotal;
                                                                                }
                                                                            }
                                                                        }
                                                                        if (inboundList.contains(docType.get(k))) {
                                                                %>
                                                                <tr class="inboundvalue<%=docType.get(k)%>" style="display: none"> 
                                                                    <td ><%=inbounddocTotal%></td>
                                                                </tr>
                                                                <%
                                                                    }
                                                                    if (outboundList.contains(docType.get(k))) {
                                                                %>
                                                                <tr class="outboundvalue<%=docType.get(k)%>" style="display: none"> 
                                                                    <td ><%=outbounddocTotal%></td>
                                                                </tr>
                                                                <%
                                                                    }
                                                                %>
                                                                <tr> 
                                                                    <td ><%=docTotal%></td>
                                                                </tr>
                                                                <%
                                                                        // System.out.println("doctype"+docType.get(j)+"inbound total"+inbounddocTotal+"outbound total"+outbounddocTotal+" total"+docTotal);
                                                                        allTotal = allTotal + docTotal;
                                                                        inbounddocTotal = 0;
                                                                        outbounddocTotal = 0;
                                                                        docTotal = 0;
                                                                    }
                                                                %>
                                                                <tr> 
                                                                    <td><%=allTotal%></td>
                                                                </tr>
                                                            </table></td><td>
                                                            <%
                                                            } else {
                                                            %>
                                                            <table align="left" id="results" width="690px"
                                                                   border="0" cellpadding="0" cellspacing="0" class="table table-bordered table-hover">
                                                                <tr><td>
                                                                        <%
                                                                            // String contextPath = request.getContextPath();
                                                                            // out.println("<img  border='0' align='top'  src='"+contextPath+"/includes/images/alert.gif'/><b> No Records Found to Display!</b>");
                                                                            out.println("<img  border='0' align='top'  src='" + contextPath + "/includes/images/alert.gif'/><b>No records found for the given search criteria. Please try a different search criteria!</b>");
                                                                        %>
                                                                    </td>
                                                                </tr></table></td><td>
                                                                <% }

                                                                } else {
                                                                %>
                                                            <table align="left" id="results" width="690px"
                                                                   border="0" cellpadding="0" cellspacing="0" class="table table-bordered table-hover">
                                                                <tr><td>
                                                                        <%
                                                                            // String contextPath = request.getContextPath();
                                                                            // out.println("<img  border='0' align='top'  src='"+contextPath+"/includes/images/alert.gif'/><b> No Records Found to Display!</b>");
                                                                            out.println("<img  border='0' align='top'  src='" + contextPath + "/includes/images/alert.gif'/><b>No records found for the given search criteria. Please try a different search criteria!</b>");
                                                                        %>
                                                                    </td>
                                                                </tr>
                                                                <% }%>



                                                            </table>
                                                        </td>

                                                    </tr>
                                                    <%
                                                        total = 0;
                                                        inboundTotal = 0;
                                                        outboundTotal = 0;
                                                        if (list.size() != 0) {

                                                            TrackInOutBean trackInOutBean;

                                                            trackInOutBean = (TrackInOutBean) list.get(0);
                                                            ArrayList docType = trackInOutBean.getDocumentTypeList();
                                                            ArrayList dateMonthdocType = trackInOutBean.getDateMonthdocType();
                                                            if (!docType.isEmpty() && !dateMonthdocType.isEmpty()) {
                                                    %>
                                                    <tr >
                                                        <td align="right" colspan="28" style="background-color: white;">
                                                            <div align="right" id="pageNavPosition"></div>
                                                        </td>
                                                    </tr> 
                                                    <%}
                                                        }%>
                                                    </tbody> </table>
                                            </div>
                                            <%-- Process butttons  start --%>
                                            <%
                                                if (list.size() != 0) {
                                                    TrackInOutBean trackInOutBean;

                                                    trackInOutBean = (TrackInOutBean) list.get(0);
                                                    ArrayList docType = trackInOutBean.getDocumentTypeList();
                                                    ArrayList dateMonthdocType = trackInOutBean.getDateMonthdocType();
                                                    if (!docType.isEmpty() && !dateMonthdocType.isEmpty()) {

                                            %>
                                            <table align="right">
                                                <tr>
                                                    <td  class="gnexcel-space" style="background-color: white;">
                                                        <strong><input type="button" class="btn btn-effect-ripple btn-primary" value="Generate Excel" onclick="return gridDownload('trackInOut', 'xls');" onmouseover="Tip('Click here to generate an excel Report.')" onmouseout="UnTip()" id="excel"/></strong>
                                                    </td>
                                                </tr>
                                            </table> 
                                            <%}
                                                }%>
                                            <%-- process buttons end--%>
                                            <%-- Grid End --%>

                                        </div>
                                    </div></div>
                            </div>
                    </section>
                </s:if> 


            </div> 
            <script language="javascript">
                function Date1()
                {
                    var date = document.trackInOutForm.reportrange.value;
                    if (date == '') {
                        alert("Please select Date Range");
                        return false;
                    }
                    var arr = date.split("-");
                    var x = arr[1].trim();
                    document.getElementById("docdatepickerfrom").value = arr[0];
                    document.getElementById("docdatepicker").value = x;
                }
                function toggle(docType, k, id) {
                    //  alert(document.getElementsByClassName("inboundvalue"+docType).length);
                    var inbound = document.getElementById("inbound" + docType);
                    var doctype = document.getElementById("doctype" + docType);
                    var inboundvalue = document.getElementsByClassName("inboundvalue" + docType);
                    var outboundvalue = document.getElementsByClassName("outboundvalue" + docType);
                    var outbound = document.getElementById("outbound" + docType);
                    //alert(ele1.length);
                    //alert("value"+ele3[0]);      
                    var id = document.getElementById(id);
                    //alert(text);
                    if (inbound != null && inbound != '') {
                        if (inbound.style.display == "none") {

                            inbound.removeAttribute("style");
                            doctype.rowSpan = k;
                            for (var i = 0; i < inboundvalue.length; i++) {
                                inboundvalue[i].removeAttribute("style");
                            }
                            //document.getElementById("toggleTd"+j).style.borderBottom="none";

                            // 	id.innerHTML = "-";
                            id.style.backgroundImage = "url('../includes/images/minus.png')";
                        }
                        else {

                            inbound.style.display = "none";
                            doctype.rowSpan = "2";
                            for (var i = 0; i < inboundvalue.length; i++) {
                                inboundvalue[i].style.display = "none";
                            }
                            //id.innerHTML = "+";
                            id.style.backgroundImage = "url('../includes/images/plus.png')";
                        }
                    }
                    if (outbound != null && outbound != '') {
                        if (outbound.style.display == "none") {

                            outbound.removeAttribute("style");
                            doctype.rowSpan = k;
                            for (var i = 0; i < outboundvalue.length; i++) {
                                outboundvalue[i].removeAttribute("style");
                            }
                            //document.getElementById("toggleTd"+j).style.borderBottom="none";

                            //	id.innerHTML = "-";
                            id.style.backgroundImage = "url('../includes/images/minus.png')";
                        }
                        else {

                            outbound.style.display = "none";
                            doctype.rowSpan = "2";
                            for (var i = 0; i < outboundvalue.length; i++) {
                                outboundvalue[i].style.display = "none";
                            }
                            //id.innerHTML = "+";
                            id.style.backgroundImage = "url('../includes/images/plus.png')";
                        }
                    }

                }

//                $(function() {
//
//                    function cb(start, end) {
//                        $('#reportrange span').html(start.format('MM/yyyy  HH:MM') + ' - ' + end.format('MM/yyyy HH:MM'));
//                    }
//                    cb(moment().subtract(29, 'days'), moment());
//
//                    $('#reportrange').daterangepicker({
//                        ranges: {
//                            'Today': [moment(), moment()],
//                            'Last 7 Days': [moment().subtract(6, 'days'), moment()],
//                            'Last 30 Days': [moment().subtract(29, 'days'), moment()],
//                            'This Month': [moment().startOf('month'), moment().endOf('month')],
//                            'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
//                            'Transactions Until': [moment()]
//                        },
//                        timePicker: true,
//                        timePicker24Hour: true,
//                        timePickerIncrement: 1,
//                        locale: {
//                            format: 'MM/yyyy HH:mm'
//                        }
//                    }, cb);
//
//                });

            </script>

        </div>

        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div>

        <script>
            $('input[name="daterange"]').daterangepicker();
        </script>

        <script type="text/javascript" src='<s:url value="../includes/js/DateValidation.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/downloadAjax.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/overlay.js"/>'></script>

        <!-- jQuery 2.1.4 -->
        <script src='<s:url value="../includes/plugins/jQuery/jQuery-2.1.4.min.js"/>'></script>
        <!-- jQuery UI 1.11.4 -->
        <script src="https://code.jquery.com/ui/1.11.4/jquery-ui.min.js"></script>
        <!-- Resolve conflict in jQuery UI tooltip with Bootstrap tooltip -->
        <script>
            $.widget.bridge('uibutton', $.ui.button);
        </script>
        <!-- Bootstrap 3.3.5 -->
        <script src='<s:url value="../includes/bootstrap/js/bootstrap.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/daterangepicker/daterangepicker.js"/>'></script>
        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
    </body>
</html>