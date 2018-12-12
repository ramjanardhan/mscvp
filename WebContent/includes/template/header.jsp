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

<!DOCTYPE html>
<meta charset="utf-8">
<title>Miracle Supply Chain Visibility Portal</title>
<!-- Tell the browser to be responsive to screen width -->
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<!-- Bootstrap 3.3.5 -->
<script type="text/javascript" src="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.js"></script>
<link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/bootstrap.min.css"/>'>
<!-- Font Awesome -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
<!-- Ionicons -->
<link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">
<!-- Theme style -->
<link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/AdminLTE.min.css"/>'>
<!-- AdminLTE Skins. Choose a skin from the css/skins
     folder instead of downloading all of them to reduce the load. -->
<link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/skins/_all-skins.min.css"/>'>
<!-- iCheck -->
<link rel="stylesheet" href='<s:url value="/includes/plugins/iCheck/flat/blue.css"/>'>
<!-- Morris chart -->
<link rel="stylesheet" href='<s:url value="/includes/plugins/morris/morris.css"/>'>
<!-- jvectormap -->
<link rel="stylesheet" href='<s:url value="/includes/plugins/jvectormap/jquery-jvectormap-1.2.2.css"/>'>
<!-- Date Picker -->
<link rel="stylesheet" href='<s:url value="/includes/plugins/datepicker/datepicker3.css"/>'>
<!-- Daterange picker -->
<link rel="stylesheet" href='<s:url value="/includes/plugins/daterangepicker/daterangepicker-bs3.css"/>'>
<!-- bootstrap wysihtml5 - text editor -->
<link rel="stylesheet" href='<s:url value="/includes/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css"/>'>
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script type="text/javascript" src='<s:url value="/includes/plugins/datepicker/moment.js"/>'></script>
<script type="text/javascript" src='<s:url value="/includes/plugins/datepicker/daterangepicker.js"/>'></script>
<link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
<script type="text/javascript" src='<s:url value="/includes/bootstrap/js/userdefined.js"/>'></script>
<script>
    function getPassword()
    {
        var oldpwd=document.getElementById("oldPwd").value;
        var newpwd=document.getElementById("newPwd").value;
        var cnfrmpwd=document.getElementById("confirmPwd").value;
             if(oldpwd==""){
           document.getElementById("responseString").innerHTML="<font style='color:red'>Please enter Old Paassword !!</font>";
                    return false; 
       }
          if(newpwd==""){
          // alert("Please enter New Paassword !!");
           document.getElementById("responseString").innerHTML="<font style='color:red'>Please enter New Paassword !!</font>";
                    return false; 
       }
          if(cnfrmpwd==""){
           //alert("Please enter Confirm Paassword !!");
            document.getElementById("responseString").innerHTML="<font style='color:red'>Please enter Confirm Paassword !!</font>";
                    return false; 
       }
       if(newpwd!=cnfrmpwd){
          // alert("Please enter New Password and Confirm equal !!");
         
            document.getElementById("responseString").innerHTML="<font style='color:red'>New Password and Confirm Password must be same!!</font>";
             document.getElementById("confirmPwd").value="";
           return false;
       }
        $("#loadingImage").show();
        var url="../ajax/changePassword.action?oldPwd="+oldpwd+"&newPwd="+newpwd+"&cnfrmPwd="+cnfrmpwd;
       
       
        request=getObject();
         
        request.onreadystatechange=getInfo;
        request.open("GET",url,true);
        request.send();
          
    }
    function getObject()
    {
        if(window.XMLHttpRequest)
        {
            return new XMLHttpRequest();
        }
        else
        {
            if(window.ActiveXObject("MicroSoft.XMLHTTP"))

            {
                return new ActiveXObject();
            }                        }
    }
    function getInfo()
    {
        if(request.readyState==4)
        {
            var val1=request.responseText;
            document.getElementById("responseString").innerHTML=val1;
             $("#loadingImage").hide();
        }
    }
</script>
<style>
    .headstyle{color:red;font-size: 25px;margin-left: 400px;padding: 6px 6px;font-family: fontAwesome;}
   
</style>
<div class="wrapper" style="padding-left: -230px;">
    <header class="main-header">
       
        <nav class="navbar navbar-static-top" role="navigation">
          <div>
            <!-- Sidebar toggle button-->
            <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
                <span class="sr-only">Toggle navigation</span>
              
            </a>
          <!-- <span  class="headstyle" displayText="beta version">Beta Version</span>
            <label style="text-align:center; font-size:20px;">beta version</label>-->
            <!--<div style="text-align: center;font-size: 12px;">beta version</div>-->
            <div class="navbar-custom-menu">
               
                <ul class="nav navbar-nav">
                    <!-- User Account: style can be found in dropdown.less -->
                    <li class="dropdown user user-menu">
                        
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                            <s:url id="image" action="renderImage" namespace="/user"> </s:url>
                              <i class=" glyphicon glyphicon-chevron-down"></i>
                      <!--         <img src="<s:property value="#image" />" width="17" height="17" class="
                      img-circle" alt="User Image"/>--%>
                            <!--                                <img src="../includes/img/user.png" class="img-circle" alt="User Image">-->
                            <span class="hidden-xs">
                                <%--       <s:if test="#session.loginId != null">
                                    <font color="white">&nbsp;<s:property value="#session.userName" /></font>
                                </s:if>
                                <s:else>
                                    <font color="white">&nbsp;Guest   &nbsp;</font>
                                </s:else> --%>
                            </span>
                        </a>
                        <ul class="dropdown-menu">
                            <!-- User image -->
                            <li class="user-header">
                                <s:url id="aImage" action="renderImage" namespace="/user"> </s:url>
                                <img src="<s:property value="#aImage" />" class="img-circle" alt="User Image"/><br>
                                <div>
                                    <strong><font color="white">&nbsp;<s:property value="#session.userName" /></font></strong> <br>    <s:if test="%{#session.loginId != null && #session.mscvpRole != null}">
                                        <font color="white"><s:property value="#session.mscvpRole"/></font>
                                    </s:if>
                                </div>
                            </li>
                            <!-- Menu Body -->
                            <!-- Menu Footer -->
                            <li class="user-footer">
                                <div class="pull-left">
                                    <a href="<s:url action="../user/getProfile.action"/>" class="btn btn-default btn-flat" >Profile</a>
                                </div>
                                <div class="pull-right">
                                    <s:if test="#session.loginId != null">
                                        <a href="<s:url value="../general/logout"/>" class="btn btn-default btn-flat"> Sign out</a> </s:if>
                                    </div>
                                </li>
                            </ul>
                        </li>
                        <!-- Control Sidebar Toggle Button -->
                        <li>
                            
                           
                             <a href="#" data-toggle="control-sidebar" role="button"><i class="fa fa-gears"></i></a>
                            </li> 
                    </ul>
                               
                </div>
          </div>            
            </nav>
            <!-- Logo -->
            <!-- Header Navbar: style can be found in header.less -->
        </header>
        <!-- Content Wrapper. Contains page content -->
    <!-- Control Sidebar -->
<%--    <aside class="control-sidebar control-sidebar-dark" id="sidebarDiv">
     Create the tabs 
     Tab panes 
     Home tab content 
    <div>
        <div class="col-sm-1"></div>
        <div class="col-sm-10">
            <div class="tab-pane active" id="control-sidebar-home-tab">
                <div class="sidebar-section">
                    <h2 class="text-light">Profile</h2>
                    <s:form cssClass="form-control-borderless"   name="resetPwdForm" id="resetPwdForm" theme="simple">
                       <div id="responseString"></div>

                        </div>
                        <div class="form-group">
                            <label for="side-profile-name">Name</label>
                            <s:textfield  name="sideprofilename" id="sideprofilename" cssClass="form-control" placeholder="John Doe" value="%{#session.userName}" readonly="true" />
                        </div>
                        <div class="form-group">
                            <label for="side-profile-email">Email</label>
                            <s:textfield id="side-profile-email" name="side-profile-email" cssClass="form-control" placeholder="john.doe@example.com"  value="%{#session.emailid}" readonly="true"/>
                        </div>
                        <div class="form-group">
                            <label for="oldPwd">Old Password</label>
                            <s:password cssClass="form-control" placeholder="OldPassword" name="oldPwd" id="oldPwd"/>
                        </div>

                        <div class="form-group">
                            <label for="newPwd">New Password</label>
                            <s:password cssClass="form-control" placeholder="NewPassword" name="newPwd" id="newPwd"/>
                        </div>
                        <div class="form-group">
                            <label for="confirmPwd">Confirm New Password</label>
                            <s:password cssClass="form-control" placeholder="ConfirmPassword" name="confirmPwd" id="confirmPwd"/>
                        </div><br>
                        <div class="form-group remove-margin">
                            <input type="button" value="Update" class="btn btn-primary col-sm-12" onclick="getPassword()"/>
                        </div>
                    </s:form>
                </div>
            </div>
        </div>    
         /.control-sidebar-menu 
</aside> /.control-sidebar --%>
<!-- Add the sidebar's background. This div must be placed
     immediately after the control sidebar -->

     <aside class="control-sidebar-bg control-sidebar control-sidebar-dark" id="sidebarDiv">
    <!-- Create the tabs -->
    <!-- Tab panes -->
    <!-- Home tab content -->
    <div>
        <div class="col-sm-1"></div>
        <div class="col-sm-10">
            <div class="tab-pane active" id="control-sidebar-home-tab">
                <div class="sidebar-section">
                    <h2 class="text-light">Profile</h2>
                    <s:form cssClass="form-control-borderless"   name="resetPwdForm" id="resetPwdForm" theme="simple">
                       <div id="responseString"></div>

                        </div>
                        <div class="form-group">
                            <label for="side-profile-name">Name</label>
                            <s:textfield  name="sideprofilename" id="sideprofilename" cssClass="form-control" placeholder="John Doe" value="%{#session.userName}" readonly="true" />
                        </div>
                        <div class="form-group">
                            <label for="side-profile-email">Email</label>
                            <s:textfield id="side-profile-email" name="side-profile-email" cssClass="form-control" placeholder="john.doe@example.com"  value="%{#session.emailid}" readonly="true"/>
                        </div>
                        <div class="form-group">
                            <label for="oldPwd">Old Password</label>
                            <s:password cssClass="form-control" placeholder="OldPassword" name="oldPwd" id="oldPwd"/>
                        </div>
                        <div id="loadingImage" align="center" style="display:none;z-index: 100px"><img  src="../includes/images/ajax-loader.gif" /></div>
                        <div class="form-group">
                            <label for="newPwd">New Password</label>
                            <s:password cssClass="form-control" placeholder="NewPassword" name="newPwd" id="newPwd"/>
                        </div>
                        <div class="form-group">
                            <label for="confirmPwd">Confirm New Password</label>
                            <s:password cssClass="form-control" placeholder="ConfirmPassword" name="confirmPwd" id="confirmPwd"/>
                        </div><br>
                        <div class="form-group remove-margin">
                            <input type="button" value="Update" class="btn btn-primary col-sm-12" onclick="getPassword()"/>
                        </div>
                    </s:form>
                </div>
            </div>
        </div>    
        <!-- /.control-sidebar-menu -->
</aside><!-- /.control-sidebar -->

</div>
