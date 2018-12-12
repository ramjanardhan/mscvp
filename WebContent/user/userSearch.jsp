
<%@page import="com.mss.ediscv.user.UserBean"%>
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
        <title>Miracle Supply Chain Visibility portal</title>
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script language="JavaScript" src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
        <script language="JavaScript" src='<s:url value="/includes/js/downloadAjax.js"/>'></script>
        <style type="text/css">
            .loadingImg {
                width: 100%;
                height: 100%;
                top: 0px;
                left: 0px;
                position: fixed;
                display: block;
                opacity: 0.7;
                background-color: #9999C2;
                z-index: 99;
                text-align: center;
            }

            #LoadingContent {
                position: absolute;
                top: 50%;
                left: 50%;
                z-index: 100;
            }
            #LoadingContent > img{
                width:150px;
            }
        </style>

        <script type="text/javascript">
            function doOnLoad()
            {
                $("#admintree").addClass("active");
                $("#searchusr").addClass("active");
                $("#searchusr i").addClass("text-red");

                document.getElementById('loadingAcoountSearch').style.display = "none";
            }
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

            $('.check_link').click(function ()
            {
                var thisCheck = $(this);
                if (thischeck.is(':checked'))
                {
                    $('#check_box').show();
                }
            });
            // New function to show the left grid




        </script>


    </head>
    <body class="hold-transition skin-blue sidebar-mini" onload="doOnLoad()">
        <div>
            <s:include value="../includes/template/header.jsp"/>
        </div>
        <div>
            <s:include value="../includes/template/sidemenu.jsp"/>
        </div>
        <script type="text/javascript" src='<s:url value="/includes/js/wz_tooltip.js"/>'></script>

        <!-- Content Wrapper. Contains page content -->
        <div class="content-wrapper">
            <!-- Content Header (Page header) -->


            <!-- Main content --> 
            <%
                if (request.getSession(false).getAttribute("responseString") != null) {
                    String reqponseString = request.getSession(false).getAttribute("responseString").toString();
                    request.getSession(false).removeAttribute("responseString");
                    out.println(reqponseString);
                }
            %>
            <div &nbsp; style="alignment-adjust:central;" >
                <%String contextPath = request.getContextPath();
                %>




                <section class="content-header">
                    <h1>
                        User Search

                    </h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-user"></i>Admin</a></li>
                        <li class="active"> User Search</li>
                    </ol>
                </section>
                <br>
                <section class="content">
                    <div class="row">
                        <div class="col-sm-12"> 

                            <!--box-->
                            <div class="box box-primary">
                                <div class="box-header with-border">
                                    <div class="box-tools pull-right">
                                        <%
                                            if (request.getAttribute("resultMessage") != null) {
                                                String reqponseString = request.getAttribute("resultMessage").toString();
                                                out.println(reqponseString);
                                            }
                                        %>

                                    </div>
                                </div><!-- /.box-header -->
                                <div class="box-body">
                                    <div id="text">
                                        <!-- /.col (right) -->
                                        <!-- First Row -->
                                        <div class="row">

                                            <div class="col-sm-12">
                                                <s:form action="../user/userSearchList.action" method="post" name="userSearchListForm" id="userSearchListForm" theme="simple">
                                                    <br>
                                                    <div class="row">
                                                        <div class="col-sm-3"> <label for="fname"> First Name </label>
                                                            <s:textfield cssClass="form-control" name="fname" id="fname" tabindex="1"  value="%{fname}"/>
                                                        </div>
                                                        <div class="col-sm-3"> <label for="lname"> Last Name </label>
                                                            <s:textfield cssClass="form-control" name="lname" id="lname" tabindex="2"  value="%{lname}"/>
                                                        </div>
                                                        <div class="col-sm-3"><label for="loginId">Log In Id</label>
                                                            <s:textfield cssClass="form-control" name="loginId" id="loginId" tabindex="3" value="%{loginId}"/>
                                                        </div>

                                                        <div class="col-sm-3"> <label for="status">Status</label>
                                                            <s:select cssClass="form-control" list="#@java.util.LinkedHashMap@{'A':'Active','I':'Inactive','T':'Terminated'}" headerKey="-1" headerValue="Select Status" name="status" id="status" tabindex="4"  value="%{status}"/>
                                                        </div>

                                                    </div>

                                                    <br>

                                                </div>
                                            </div>
                                        </div>

                                    </div><!-- /.box-body -->
                                    <div id="loadingAcoountSearch" class="loadingImg">
                                        <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                    </div>
                                    <div class="box-footer text-center">
                                        <div class="row">
                                            <div class="col-sm-2">
                                                <s:submit   value="Search" cssClass="btn btn-primary col-sm-12" tabindex="16"/>
                                            </div>
                                            <div class="col-sm-2"> 

                                            </div>
                                            <div class="col-sm-8"></div>
                                        </div>
                                    </div><!-- /.box-footer -->
                                </div><!-- /.box -->
                            </s:form>
                            <!--box-->


                        </div>
                        <div class="col-sm-1"></div>
                    </div>
                </section>
                <!--main content-->
            </div><!-- /.content-wrapper -->


            <s:if test="#session.userList != null"> 
                <%--- GRid start --%>
                <div class="content" id="gridDiv">

                    <%!String cssValue = "whiteStripe";
                        int resultsetTotal;%>
                    <section class="content">



                        <div class="row">
                            <div class="col-xs-12">
                                <div class="box">
                                    <!--                                    <div class="box-header">
                                                                            <h3 class="box-title">Table</h3>
                                                                        </div> /.box-header -->
                                    <div class="box-body">
                                        <!--                  <table id="results" class="table table-bordered table-hover">-->


                                        <div style="overflow-x:auto;">                 

                                            <table align="left" width="710px"
                                                   border="0" cellpadding="0" cellspacing="0">
                                                <tr>
                                                    <td style="background-color: white;">
                                                        <div style="overflow-x:auto;"> 
                                                            <table align="left" id="results" class="table table-bordered table-hover">
                                                                <%
                                                                    java.util.List list = (java.util.List) session.getAttribute(AppConstants.SES_USER_LIST);
                                                                    if (list.size() != 0) {
                                                                        UserBean userBean;
                                                                %>
                                                                <thead>     <tr>
                                                                        <th>#</th>
                                                                        <th>Login&nbspId </th>
                                                                        <th>Name </th>
                                                                        <th>Email</th>
                                                                        <th>Office&nbsp;Phone #</th>
                                                                            <%-- <td>Role</td> --%>
                                                                        <th>Status</th>
                                                                    </tr></thead>
                                                                <tbody>
                                                                    <tr >

                                                                        <%
                                                                            for (int i = 0; i < list.size(); i++) {
                                                                                userBean = (UserBean) list.get(i);

                                                                                if (i % 2 == 0) {
                                                                                    cssValue = "whiteStripe";
                                                                                } else {
                                                                                    cssValue = "grayEditSelection";
                                                                                }
                                                                        %>
                                                                        <%--  <td style="text-align: left">
                                                                                 <%
                                                                         out.println(i+1);
                                                                         %>
                                                                             </td>--%>
                                                                        <td style="text-align: left">
                                                                            <s:url var="myUrl" action="../user/getAssingnedFlows.action">
                                                                                <s:param name="userId"><%=userBean.getId()%></s:param>
                                                                            </s:url>
                                                                            <s:a href='%{#myUrl}' onmouseover="Tip('Click here to Assign flow(s).')" onmouseout="UnTip()">
                                                                                <%

                                                                                    out.println(i + 1);
                                                                                %>  
                                                                            </s:a>


                                                                        </td>
                                                                        <td style="text-align: left">
                                                                            <%
                                                                                String id = userBean.getId();
                                                                            %>
                                                                            <s:url var="myUrl" action="../user/userEdit.action">
                                                                                <s:param name="id"><%=id%></s:param>

                                                                                <s:param name="fname" value="%{fname}"></s:param>
                                                                                <s:param name="lname" value="%{lname}"></s:param> 
                                                                                <s:param name="loginId" value="%{loginId}"></s:param>
                                                                                <s:param name="status" value="%{status}"></s:param>

                                                                            </s:url>
                                                                            <s:a href='%{#myUrl}' onmouseover="Tip('Click here to Edit User Details.')" onmouseout="UnTip()">
                                                                                <%

                                                                                    out.println(userBean.getLoginId());
                                                                                %>  
                                                                            </s:a>

                                                                        </td>
                                                                        <td style="text-align: left"><%-- <a href="#"> --%>
                                                                            <%
                                                                                // String Name = userBean.getName(); 
                                                                                out.println(userBean.getName());
                                                                            %>


                                                                        </td>

                                                                        <td style="text-align: left">
                                                                            <%
                                                                                out.println(userBean.getEmail());
                                                                            %>
                                                                        </td>
                                                                        <td style="text-align: left">
                                                                            <%
                                                                                out.println(userBean.getOphno());
                                                                            %>
                                                                        </td>
                                                                        <%--    <td style="text-align: left">
                                                                             <%
                                                                     out.println(userBean.getRoleId());
                                                                     %>
                                                                         </td> --%>
                                                                        <td style="text-align: left">
                                                                            <%
                                                                                out.println(userBean.getStatus());
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
                                                                                    out.println("<img  border='0' align='top'  src='" + contextPath + "/includes/images/alert.gif'/><b> No Records Found to Display!</b>");
                                                                                }

                                                                            %>
                                                                        </td>
                                                                    </tr></tbody>
                                                            </table>

                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td align="right" colspan="28" style="background-color: white;">

                                                    </td>
                                                </tr>           
                                            </table>
                                        </div>
                                        <%-- Process butttons  start --%>
                                        <table align="left" 
                                               width="690px" border="0" cellpadding="0"
                                               cellspacing="0">


                                        </table>
                                        <%-- process buttons end--%>
                                        <%-- Grid End --%>

                                    </div>
                                </s:if> 



                            </div> 

                            <script>
        $(function () {
            $("#example1").DataTable();
            $('#results').DataTable({
                "paging": true,
                "lengthChange": false,
                "searching": false,
                "ordering": true,
                "info": true,
                "autoWidth": false,
                "scrollY": 300,
                "scrollX": true
            });
        });
                            </script>
                            <!--               <script type="text/javascript">
                                    var pager = new Pager('results', 10); 
                                    pager.init(); 
                                    pager.showPageNav('pager', 'pageNavPosition'); 
                                    pager.showPage(1);
                                    </script>-->
                            <%-- <div id="footer">
                               <h2><font color="white">&#169 2013 Miracle Software Systems, Inc. All rights reserved</font></h2>
                                      </div> --%>
                        </div>        

                    </div>  
            </div>


        </section>
    </div>          
    <div>
        <s:include value="../includes/template/footer.jsp"/>
    </div>
    <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
    <script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
    <script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>




</body>
</html>