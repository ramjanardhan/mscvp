 <%@page import="com.mss.ediscv.util.PasswordUtil"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page import="com.mss.ediscv.util.AppConstants" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>Miracle SCVP | Log in</title>
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <!-- Bootstrap 3.3.5 -->
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/bootstrap.min.css"/>' type="text/css">
        <!-- Font Awesome -->
        <link rel="stylesheet" href='<s:url value="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css"/>' type="text/css">
        <!-- Ionicons -->
        <link rel="stylesheet" href='<s:url value="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css"/>' type="text/css">
        <!-- Theme style -->
        <link rel="stylesheet" href="../includes/bootstrap/css/AdminLTE.min.css">
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/AdminLTE.min.css"/>' type="text/css">

        <!-- iCheck -->
        <link rel="stylesheet" href='<s:url value="/includes/plugins/iCheck/square/blue.css"/>' type="text/css">
    </head>
    <body class="hold-transition login-page">
        <div class="login-box">
            <div class="login-logo">
                <a href="http://www.miraclesoft.com">
                    <img class="img-responsive" src="../includes/img/miracle-logo-light.png" >
                </a>
            </div><!-- /.login-logo -->
            <div class="login-box-body">
                <p class="login-box-msg">Sign in to start your session</p>
                <div>
                     <%if (request.getAttribute(AppConstants.REQ_ERROR_INFO) != null) 
                out.println("<font size=2 color=red>"+request.getAttribute(AppConstants.REQ_ERROR_INFO).toString()+"</font>");%>
                </div>
                <s:form action="/general/loginCheck.action" method="post" name="userLoginForm" id="userLoginForm" theme="simple">
                    <div class="form-group has-feedback">
                        <!--                        <input type="email" class="form-control" placeholder="Email">-->  
                          <%
                     Cookie[] ca;
                     if(request.getCookies()!=null){
                             PasswordUtil passwordUtil=new PasswordUtil();
                            ca = request.getCookies();
                            
                            for (int i = 0; i < ca.length; i++) {
                               
                                System.out.println("size of array is " + ca.length);
                                System.out.print("Name : " + ca[i].getName() + ",  ");
                                System.out.print("Value: " + ca[i].getValue() + " <br/>");
                            }
                     
                            if (ca.length > 1) {
                                String name = ca[ca.length - 2].getName();

                        %>                   
                        <input type="text" class="form-control" name="loginId" id="loginId" placeholder="Email" value="<%=name%>" onkeypress="return handleEnter(this,event);"/>
                        <%} else {%>
                        <s:textfield cssClass="form-control" name="loginId" id="loginId" placeholder="UserId" onkeypress="return handleEnter(this,event);"/>
                        <%}
                     }else{%>
                      <s:textfield cssClass="form-control" name="loginId" id="loginId" placeholder="UserId" onkeypress="return handleEnter(this,event);"/>
                        <%}%>
                        <span class="glyphicon glyphicon-user form-control-feedback"></span>
                    </div>
                    <div class="form-group has-feedback">
                        <!--                        <input type="password" class="form-control" placeholder="Password">-->
                        <s:password cssClass="form-control" placeholder="Password" name="password" id="password"/>
                        <span class="glyphicon glyphicon-lock form-control-feedback"></span>
                    </div>
                    <div class="row">
                        <div class="col-xs-8">
                            <div class="checkbox icheck">

                                &nbsp; &nbsp;&nbsp; &nbsp;&nbsp;<s:checkbox name="remember" id="remember"/> Remember Me
                                <s:hidden name="checkboxvalue" id="checkboxvalue" value=""/>  
                            </div>
                        </div><!-- /.col -->
                        <div class="col-xs-4">
                            <!--                            <button type="submit" class="btn btn-primary btn-block btn-flat">Sign In</button>-->
                            <s:submit value="Sign In" cssClass="btn btn-primary btn-block btn-flat" onclick="checkboxstatus()"/>
                        </div><!-- /.col -->
                    </div>
                </s:form>

                <!-- /.social-auth-links -->

                <a href="" data-toggle="modal" data-target="#myModal">I forgot my password</a><br>
                <div class="modal fade" id="myModal" data-backdrop="static" data-keyword="false" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title" id="myModalLabel">Reset Password</h4>
                            </div>
                            <div class="modal-body" style="margin-top: -15px; margin-bottom: -25px;">
                                <div class="form-group has-feedback">
                                    <s:textfield  cssClass="form-control" name="userid" id="userid" placeholder="User ID"/>
                                    <span class="glyphicon glyphicon-user form-control-feedback"></span>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <div class="row">
                                    <!-- /.col -->
                                    <div class="col-xs-2">
                                        <button type="submit" class="btn btn-primary btn-block btn-flat" onclick="forgotpassword()">Submit</button>
                                    </div><!-- /.col -->                          <div id="load" style="display: none">Processing... please wait</div>         <div class="col-xs-9">
                                        <div id="msg" name="msg"></div> 
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div><!-- /.login-box-body -->
        </div><!-- /.login-box -->

        <!-- jQuery 2.1.4 -->
        <script language="JavaScript" src='<s:url value="/includes/plugins/jQuery/jQuery-2.1.4.min.js"></s:url>'></script>
        <!-- Bootstrap 3.3.5 -->
        <script language="JavaScript" src='<s:url value="/includes/bootstrap/js/bootstrap.min.js"></s:url>'></script>
        <!-- iCheck -->
        <script language="JavaScript" src='<s:url value="/includes/plugins/iCheck/icheck.min.js"></s:url>'></script>
        <script>
            $(function () {
                $('input').iCheck({
                    checkboxClass: 'icheckbox_square-blue',
                    radioClass: 'iradio_square-blue',
                    increaseArea: '20%' // optional
                });
            });
        </script>
        <script type="text/javascript">
            function checkboxstatus()
            {
                var variable=document.getElementById("remember").checked;
                                if(variable==true)
                {
                    document.getElementById("checkboxvalue").value ='1';
                }
                else
                {
                    document.getElementById("checkboxvalue").value ='0';   
                }
                var x=document.getElementById("checkboxvalue").value;
                 }
        
        </script>
        <script type="text/javascript">
            function forgotpassword()
            {
                document.getElementById("load").style.display='block';
                request=getObject();
                var userid=document.getElementById("userid").value;
                var url="../ajax/forgotpassword.action?userid="+userid;
                //  alert(url);
      
                request.onreadystatechange=getInfo;
                request.open("GET",url,true);
                request.send();
           
            }
            function getInfo()
            {
                if(request.readyState==4)
                {
                      document.getElementById("load").style.display='none';
                    var val1=request.responseText;
                    if(val1=="success")
                    {
                        document.getElementById("msg").innerHTML="Password successfully sent.... Check your email!!!";
                    }
                    else if(val1=="failure")
                    {
                        document.getElementById("msg").innerHTML="Can't send msg to your email!!!";
                    }
                    else
                    {
                        document.getElementById("msg").innerHTML="Can't send msg !!!";
                    }
                }
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
                    }                        
                }
            }
            
 
        </script>
    </body>
</html>
