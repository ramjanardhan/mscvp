
<!DOCTYPE HTML>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <title>Miracle Supply Chain Visibility Portal</title>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta http-equiv="pragma" content="no-cache" />
        <meta http-equiv="cache-control" content="no-cache" />
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/AdminLTE.min.css"/>' type="text/css">
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/bootstrap.css"/>' type="text/css">

        <style>
            .main-footer1{
                margin-left: 0px !important;
                margin-bottom: 0px !important;
                margin-top: 15% !important;
                font-family: 'Source Sans Pro';
            }


            .main-footer {
                bottom: 0;
                height: 61px;
                position: absolute;
                width: 100%;


            }
            .content-wrapper, .right-side, .main-footer{
                margin-bottom: 0px !important;
            }

            @media (min-width:320px) and (max-width:1024px){
                .align_message{
                    text-align: center;
                }
            }

        </style>

    </head>

    <body class="hold-transition skin-blue sidebar-mini">

        <!-- Content Wrapper. Contains page content -->
        <div class="row" style="margin-top:13%">
            <!-- Content Header (Page header) -->
            <div class="com-sm-12">
                <section class="content" >


                    <div class="error-page">
                        <h2 class="headline text-light-blue">500</h2>
                        <div class="error-content">
                            <br>
                            <h3><i class="fa fa-warning text-light-blue"></i> Oops! Something went wrong.</h3>
                            <p class="align_message">
                                We will work on fixing that right away.
                                Meanwhile, you may <a href="<s:url action='../general/login'/>">return to Log In</a> or try using the search form.
                            </p>

                        </div>
                    </div><!-- /.error-page -->

                </section><!-- /.content -->

            </div>
        </div>

        <!--  <footer class="main-footer1 fixed-bottom">
                                <div class="pull-right hidden-xs">
                                    <b>Version</b> 2.0
                               
                                <strong>Copyright &copy; 2016 <a href="http://www.miraclesoft.com">Miracle Software Systems, Inc.</a></strong> All rights reserved.
                            </footer>-->

        <footer class="main-footer fixed-bottom ltship_ftr " style="margin-left:0;">
            <div class="pull-right hidden-xs">
                <b>Version</b> 2.0
            </div>
            <strong> &copy; <%= new java.text.SimpleDateFormat("yyyy").format(new java.util.Date())%> <a href="http://www.miraclesoft.com">Miracle Software Systems, Inc.</a></strong> All rights reserved.
        </footer>

    </body>
</html>