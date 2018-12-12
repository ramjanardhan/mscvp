
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
        
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        


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
                $("#resetUsrPwd").addClass("active");
                $("#resetUsrPwd i").addClass("text-red");
                
                document.getElementById('loadingAcoountSearch').style.display="none";
            }
            $(function() {
                $('#attach_box').click(function() {
                    $('#sec_box').show();
                    return false;
                });        
            });
            $(function() {
                $('#detail_link').click(function() {
                    $('#detail_box').show();
                    return false;
                });        
            });
   
            $('.check_link').click (function ()
            {
                var thisCheck = $(this);
                if (thischeck.is (':checked'))
                {
                    $('#check_box').show();
                }
            });
            // New function to show the left grid

            function checkUserValues() {
    
    
                var loginId = document.getElementById("loginId").value;
                var newPwd = document.getElementById("newPwd").value;
                var confirmPwd = document.getElementById("confirmPwd").value;
    
    
                //if(((fname!=null)&&(fname!=""))&&((lname!=null)&&(lname!=""))&&((email!=null)&&(email!=""))&&((ophno!=null)&&(ophno!="")))
                if((loginId==null)||(loginId=="")) {
                    alert("Please enter User Login Id!");
                    return false;
                }
                if((newPwd==null)||(newPwd=="")) {
                    alert("Please enter New Password!");
                    return false;
                }
                if((confirmPwd==null)||(confirmPwd=="")) {
                    alert("Please enter Confirm Password!");
                    return false;
                }
                if((confirmPwd!=newPwd)) {
                    alert("New Password and Confirm Password must be equal!");
                    return false;
                }
                else
                {
                    return true;
                }
            }

            function resetvalues()
            {
                document.getElementById('loginId').value="";
                document.getElementById('newPwd').value="";
                document.getElementById('confirmPwd').value="";
    
                //$('#detail_box').hide();
                //$('#gridDiv').hide();
   
            }

        </script>


    </head>
    <body class="hold-transition skin-blue sidebar-mini" onload="doOnLoad()">
        <div id="wrapper">
            <div id="main">

                <div>
                    <s:include value="../includes/template/header.jsp"/>
                </div>
                <div>
                    <s:include value="../includes/template/sidemenu.jsp"/>
                </div>    <div id="site_content">
                    <!-- Content Wrapper. Contains page content -->
                    <div class="content-wrapper">
                        <!-- Content Header (Page header) -->


                        <!-- Main content --> 

                        <section class="content-header">
                            <h1>
                                Reset User Password

                            </h1>
                            <ol class="breadcrumb">
                                <li><a href="#"><i class="fa fa-user"></i>Admin</a></li>
                                <li class="active"> Reset Password</li>
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

                                            </div>
                                        </div><!-- /.box-header -->
                                        <div class="box-body">
                                            <%
                                                if (request.getAttribute("resultMessage") != null) {
                                                    String resultMessage = request.getAttribute("resultMessage").toString();
                                                    out.println(resultMessage);
                                                }
                                            %>
                                            <div style="alignment-adjust:central;" >
                                                <%String contextPath = request.getContextPath();
                                                %>
                                                <div id="text">
                                                    <!-- /.col (right) -->
                                                    <!-- First Row -->
                                                    <div class="row">
                                                        <div class="col-sm-12">

                                                            <s:form action="../user/updateUserPwd.action"  cssClass="form-horizontal" method="post" name="resetPwdForm" id="resetPwdForm" theme="simple">
                                                                <div class="form-group">
                                                                    <label for="loginId" class="col-sm-2">Login Id</label>
                                                                    <div class="col-sm-4">
                                                                        <s:textfield cssClass="form-control" placeholder="Login Id" name="loginId" id="loginId" />
                                                                    </div> 
                                                                    <div class="col-sm-3"></div>
                                                                    <div class="col-sm-3"></div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label for="newPwd" class="col-sm-2">New Password</label>
                                                                    <div class="col-sm-4">
                                                                        <s:password cssClass="form-control" placeholder="New Password" name="newPwd" id="newPwd" />
                                                                    </div>
                                                                    <div class="col-sm-3"></div>
                                                                    <div class="col-sm-3"></div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label for="confirmPwd" class="col-sm-2">Confirm Password</label>
                                                                    <div class="col-sm-4">
                                                                        <s:password cssClass="form-control" placeholder="Confirm Password" name="confirmPwd" id="confirmPwd" />
                                                                    </div>
                                                                    <div class="col-sm-3"></div>
                                                                    <div class="col-sm-3"></div>
                                                                </div>
                                                                    <div id="loadingAcoountSearch" class="loadingImg">
                                                            <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                                        </div>
                                                                <div class="row">
                                                                    <div class="col-sm-2"><s:submit value="Update" cssClass="btn btn-primary col-sm-12" ></s:submit></div>
                                                                        <div class="col-sm-6"></div>
                                                                        <div class="col-sm-4"></div>
                                                                    </div>
                                                            </s:form>

                                                        </div>


                                                    </div>
                                                </div>
                                            </div>

                                        </div><!-- /.box-body -->

                                    </div><!-- /.box -->

                                    <!--box-->


                                </div>
                                <div class="col-sm-1"></div>
                            </div>
                        </section>
                        <!--main content-->
                    </div><!-- /.content-wrapper -->


                    
                <div>
                    <s:include value="../includes/template/footer.jsp"/>
                </div>
            </div>        

        </div>  
        <!-- jQuery 2.1.4 -->
        <script src='<s:url value="../includes/plugins/jQuery/jQuery-2.1.4.min.js"/>'></script>
        <!-- Bootstrap 3.3.5 -->
        <script src='<s:url value="../includes/bootstrap/js/bootstrap.min.js"/>'></script>
        <!-- AdminLTE App -->
        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>

    </body>
</html>
