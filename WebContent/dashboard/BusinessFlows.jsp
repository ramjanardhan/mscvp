<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page buffer="50kb" autoFlush="true" %>

<html class=" js canvas canvastext geolocation crosswindowmessaging no-websqldatabase indexeddb hashchange historymanagement draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow opacity cssanimations csscolumns cssgradients no-cssreflections csstransforms no-csstransforms3d csstransitions  video audio localstorage sessionstorage webworkers applicationcache svg smil svgclippaths   fontface">
    <head>
        <title>Miracle Supply Chain Visibility portal</title>
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
        <script type="text/javascript"  src='<s:url value="../includes/js/highCharts.js"/>'></script>
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <script language="JavaScript" src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
        <link rel="stylesheet" href='<s:url value="../includes/css/highcharts.css"/>'>
        <script type="text/javascript">
            function doOnLoad() {
                var type = document.getElementById("flag").value;
                if(type=="L"){
                $("#dashboardLM").addClass("active");
                $("#dashboardL").addClass("active");
                $("#LBusinessFlow").addClass("active");
                $("#LBusinessFlow").css('font-weight', 'bold');
                $("#LBusinessFlow i").addClass("text-red");
            }else{
                $("#dashboardLM").addClass("active");
                $("#dashboardM").addClass("active");
                $("#MBusinessFlow").addClass("active");
                $("#MBusinessFlow").css('font-weight', 'bold');
                $("#MBusinessFlow i").addClass("text-red");
            }
                $(".highcharts-background").attr("fill", "#E5E5E5");
            }
        </script>
    </head>
    <body  class="hold-transition skin-blue sidebar-mini" onload="doOnLoad();">
        <div> <s:include value="../includes/template/header.jsp"/> </div>
        <div> <s:include value="../includes/template/sidemenu.jsp"/> </div>

        <!-- ................................................................................ -->

        <div class="content-wrapper col-sm-12">

            <s:hidden name="ediTransactionStats" id="ediTransactionStats" value="%{ediTransactionStats}"/>
            <s:hidden name="ediDocumentsVolume" id="ediDocumentsVolume" value="%{ediDocumentsVolume}"/>
            <s:hidden name="ediTpVolume" id="ediTpVolume" value="%{ediTpVolume}"/>
            <s:hidden name="flag" id="flag" value="%{flag}"/>

            <div class="col-sm-10">
                <section style="padding-top:10px">
                    <div class="col-sm-4"> 
                        <div id="EDI_TRANSCATION_STATS" style="height:330px"></div>
                    </div>
                    <div class="col-sm-4"> 
                        <div id="EDI_VOLUMES_DOC" style="height:330px"></div>
                    </div>
                    <div class="col-sm-4"> 
                        <div id="EDI_VOLUMES_TP" style="height:330px"></div>
                    </div>
                </section>  
            </div>

        </div>
        <%--    <div id="footer">  --%>
        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div> 
        <%--   	</div> --%>
        <script src='<s:url value="../includes/bootstrap/js/bootstrap.min.js"/>'></script>
        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>

        <script>
        // EDI TRANSACTION STATS     
        //var ediTransactionStats = [{data: JSON.parse(document.getElementById('ediTransactionStats').value)}];
        var ediTransactionStats = document.getElementById('ediTransactionStats').value;
        //alert("ediTransactionStats----------"+ediTransactionStats);
        Highcharts.chart('EDI_TRANSCATION_STATS', {
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: 'pie'
            },
            // green(completed) yellow(pending)  red(failed)  
            // colors: ['#c1f823', '#FFFF00', '#fe6838'],
            title: {
                text: '<b>EDI TRANSACTION STATS</b>'
            },
            tooltip: {
                pointFormat: ' {point.y} '
            },
            plotOptions: {
                pie: {
                    size: 150,
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false,
                        //format: '<b>{point.name}</b>: {point.y} ',
                        style: {
                            color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                        }
                    },
                    showInLegend: true
                }

            },
            legend: {
                align: 'right',
                verticalAlign: 'top',
                layout: 'vertical',
                x: 0,
                y: 100
            },
            //series: ediTransactionStats
            series: [
                {
                    data: JSON.parse(ediTransactionStats),
                    type: 'pie',
                    animation: false,
                    point: {
                        events: {
                            click: function (event) {
                                //alert(this.name);
                                if (this.name == 'Pending') {
                                    pendingFailedDeatails(this.name, "EDI");
                                }
                                if (this.name == 'Failed') {
                                    pendingFailedDeatails(this.name, "EDI");
                                }
                            }
                        }
                    }
                }
            ]

        });
        // EDI VOLUMES BY DOCUMENTS     
        var ediDocumentsVolume = [{data: JSON.parse(document.getElementById('ediDocumentsVolume').value)}];
        // alert("ediDocumentsVolume-----"+ediDocumentsVolume);
        Highcharts.chart('EDI_VOLUMES_DOC', {
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: 'pie'
            },
            //colors: ['#33fff6', '#33a5ff', '#c1f823', '#c5c3c5'],
            title: {
                text: '<b>EDI VOLUMES BY DOCUMENTS</b>'
            },
            tooltip: {
                pointFormat: ' {point.y} '
            },
            plotOptions: {
                pie: {
                    size: 150,
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false,
                        // format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                        style: {
                            color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                        }
                    },
                    showInLegend: true
                }
            },
            legend: {
                align: 'right',
                verticalAlign: 'top',
                layout: 'vertical',
                x: 0,
                y: 100
            },
            series: ediDocumentsVolume
        }
        );
        // EDI VOLUME BY TOP TRADING PARTNERS     
        var ediTpVolume = [{data: JSON.parse(document.getElementById('ediTpVolume').value)}];
        // alert("ediTpVolume-----------"+ediTpVolume);
        Highcharts.chart('EDI_VOLUMES_TP', {
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: 'pie'
            },
            title: {
                text: '<b>EDI VOLUMES BY TOP TRADING PARTNERS</b>'
            },
            tooltip: {
                pointFormat: ' {point.y} '
            },
            plotOptions: {
                pie: {
                    size: 150,
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false,
                        // format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                        style: {
                            color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                        }
                    },
                    showInLegend: true
                }
            },
            legend: {
                align: 'right',
                verticalAlign: 'top',
                layout: 'vertical',
                x: 0,
                y: 100
            },
            series: ediTpVolume
        }
        );

        </script>

    </body>
</html>