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
        <link rel="stylesheet" href='<s:url value="../includes/bootstrap/css/userdefined.css"/>'>
        <link rel="stylesheet" href='<s:url value="../includes/css/highcharts.css"/>'>
        <script type="text/javascript">
            function doOnLoad() {
                var type=document.getElementById("flag").value;
               // alert(type);
                if(type=="L"){
                $("#dashboardLM").addClass("active");
                $("#dashboardL").addClass("active");
                $("#LdailyStatsView").addClass("active");
                $("#LdailyStatsView").css('font-weight', 'bold');
                $("#LdailyStatsView i").addClass("text-red");
            }else{
                $("#dashboardLM").addClass("active");
                $("#dashboardM").addClass("active");
                $("#MdailyStatsView").addClass("active");
                $("#MdailyStatsView").css('font-weight', 'bold');
                $("#MdailyStatsView i").addClass("text-red");
            }
                document.getElementById('loadingAcoountSearch').style.display = "none";
               // $(".highcharts-background").attr("fill", "#B0C4DE");
               // $(".highcharts-plot-background").attr("fill", "#B0C4DE");
                //$(".highcharts-point highcharts-color-0").attr("fill", "#B0C4DE");
            }
        </script>
    </head>
    <body  class="hold-transition skin-blue sidebar-mini" onload="doOnLoad();">
        <div> <s:include value="../includes/template/header.jsp"/> </div>
        <div> <s:include value="../includes/template/sidemenu.jsp"/> </div>
        <div class="content-wrapper col-sm-12" style="padding-left: 0px">
            <s:hidden name="xAxisTimeInterval" id="xAxisTimeInterval" value="%{xAxisTimeInterval}"/>
            <s:hidden name="baseValue" id="baseValue" value="%{baseValue}"/>
            <s:hidden name="dailyEdiStats" id="dailyEdiStats" value="%{dailyEdiStats}"/>
            <s:hidden name="dailyStatsEdiDocuments" id="dailyStatsEdiDocuments" value="%{dailyStatsEdiDocuments}"/>
            <s:hidden name="dailyStatsViewInboundCount" id="dailyStatsViewInboundCount" value="%{dailyStatsViewInboundCount}"/>
            <s:hidden name="dailyStatsViewOutboundCount" id="dailyStatsViewOutboundCount" value="%{dailyStatsViewOutboundCount}"/>
            <s:hidden name="monthlyVolumeXAxisValues" id="monthlyVolumeXAxisValues" value="%{monthlyVolumeXAxisValues}"/>
            <s:hidden name="topTpEdiPartnersInboundCount" id="topTpEdiPartnersInboundCount" value="%{topTpEdiPartnersInboundCount}"/>
            <s:hidden name="topTpEdiPartnersOutboundCount" id="topTpEdiPartnersOutboundCount" value="%{topTpEdiPartnersOutboundCount}"/>
            <s:hidden name="top10EdiTpPartners" id="top10EdiTpPartners" value="%{top10EdiTpPartners}"/>
            <s:hidden name="dailyFailureRate" id="dailyFailureRate" value="%{dailyFailureRate}"/>
            <s:hidden name="monthlyValumeIbCount" id="monthlyValumeIbCount" value="%{monthlyValumeIbCount}"/>
            <s:hidden name="monthlyValumeObCount" id="monthlyValumeObCount" value="%{monthlyValumeObCount}"/>
            <s:hidden name="flag" id="flag" value="%{flag}"/>
            <div id="loadingAcoountSearch" class="loadingImg">
                <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
            </div>
            <section class="container-fluid">
                <!--                <button> <span class="glyphicon glyphicon-refresh" onclick="myFunction()"></span></button>-->
                <div class="col-sm-12" style="padding-bottom: 10px;padding-left: 0px;margin-top: 10px">
                    <div class="col-sm-4">
                        <div id="EdiStats" style="height: 275px"></div>
                    </div>
                    <div class="col-sm-6">
                        <div id="EdiTransHours" style="height: 275px"></div>
                    </div>
                </div>
                <br>
               
                <div class="col-sm-12" style="padding-bottom: 10px;padding-left: 0px">
                      
                    <div class="col-sm-10">
                        <div id="MonthlyValumes" style="height: 275px;"></div>
                    </div>
                </div>
                <div class="col-sm-12" style="padding-bottom: 10px;padding-left: 0px">
                  
                    <div class="col-sm-5">
                        <div id="DailyFailureRate" style="height: 275px;"></div>
                    </div>
                    <div class="col-sm-5">
                        <div id="TopTpEdiValumes" style="height: 275px;">
                        </div>
                    </div>
                    
                </div>
                <br>
            </section>
        </div>
        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div>
        <script src='<s:url value="../includes/bootstrap/js/bootstrap.min.js"/>'></script>
        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>

        <script type="text/javascript">
//                    function myFunction() {
//                        location.reload();
//                    }

        var xAxisTimeInterval = document.getElementById('xAxisTimeInterval').value;
        var xAxisTime = xAxisTimeInterval.split(',');
       // alert("x axis time---"+xAxisTime);

        var xAxisLabel = 'Hours';
        var yAxisLabel = 'Transactional Volume';

        var baseValue = document.getElementById('baseValue').value;

        // EDI STATS    
        var dailyEdiStats = [{data: JSON.parse(document.getElementById('dailyEdiStats').value)}];
       // alert("dailyEdiStats-----------"+dailyEdiStats);
       // alert("dailyEdiStats1------------"+document.getElementById('dailyEdiStats').value);
        Highcharts.chart('EdiStats', {
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: 'pie'
            },
            title: {
                text: 'EDI VOLUMES'
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
            series: dailyEdiStats
        });

        //EdiTransHours
        var dailyStatsEdiDocuments = document.getElementById('dailyStatsEdiDocuments').value;
        var dailyStatsEdiDocumentsHeight = Math.max.apply(Math, JSON.parse(dailyStatsEdiDocuments));
        //alert("dailyStatsEdiDocuments----"+dailyStatsEdiDocuments);
        //alert("xAxisTime"+xAxisTime);
   
        var tp1chart = Highcharts.chart('EdiTransHours', {
            title: {
                text: 'EDI TRANS / HOUR',
            },
            chart: {
                marginRight: 80 // like left
            },
            tooltip: {
                pointFormat: ' {point.y} '
            },
            xAxis: {
                categories: xAxisTime,
                title: {
                    text: xAxisLabel
                }
            },
            yAxis: [{
                    lineWidth: 1,
                    max: dailyStatsEdiDocumentsHeight,
                    min: 0,
                    title: {
                        text: yAxisLabel
                    },
                    plotLines: [{
                            color: 'blue', // Color value
                            dashStyle: 'longdashdot', // Style of the plot line. Default to solid
                            value: baseValue, // Value of where the line will appear
                            width: 1 // Width of the line    
                        }]
                }],
            series: [{
                    showInLegend: false,
                    data: JSON.parse(dailyStatsEdiDocuments),
                    zones: [{
                            value: baseValue,
                            color: '#FF0000'
                        }, {
                            color: '#90ed7d'
                        }]
                }]
        });

 
        //DailyFailureRate
        Highcharts.chart('DailyFailureRate', {
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: 'pie'
            },
            // green(completed) yellow(pending)  red(failed) 
            // colors: ['#c1f823', '#fe6838'],
            title: {
                text: 'DAILY FAILURE RATE'
            },
            tooltip: {
                pointFormat: ' {point.y} %'
            },
            plotOptions: {
                pie: {
                    size: 150,
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true,
                        //format: '<b>{point.name}</b>: {point.y} ',
                        style: {
                            color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                        }
                    },
                    showInLegend: true
                }
            },
            legend: {
                //                align: 'right',
                //                verticalAlign: 'top',
                //                layout: 'vertical',
                x: 0,
                y: 100
            },
            //series: [{data: [{name: 'Sucess', y: 500}, {name: 'Failure', y: 4000}]}]
            series: [{data: JSON.parse(document.getElementById("dailyFailureRate").value)}]

        });

        //MonthlyValumes   
        var ibcount = document.getElementById("dailyStatsViewInboundCount").value;
        //alert(ibcount);
        var obcount = document.getElementById("dailyStatsViewOutboundCount").value;
        //alert(obcount);
        var xaxisvalues = document.getElementById("monthlyVolumeXAxisValues").value;
        //alert("xaxisvalues---"+xaxisvalues);
        var xAxisMonth = xaxisvalues.split(',');
        //alert("xAxisMonth"+xAxisMonth);
        Highcharts.chart('MonthlyValumes', {
            chart: {
                type: 'column'
            },
            title: {
       text: 'MONTHLY VOLUMES  <p><span style="border: 2px solid black;color: #7cb5ec;font-size: 15px;">IB Count:</span><s:property value="monthlyValumeIbCount" />   <span style="font-size: 15px;">OB Count: <s:property value="monthlyValumeObCount" /></span>'
            },
            xAxis: {
                categories: xAxisMonth,
                crosshair: true
               //crosshair: { color:'red' }
            },
            yAxis: {
                min: 0,
                title: {
                    text: yAxisLabel
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                        '<td style="padding:0"><b>{point.y}</b></td></tr>',
                footerFormat: '</table>',
                shared: true,
                useHTML: true
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                }
            },
            legend: {
                //layout: 'vertical',
                align: 'right',
                verticalAlign: 'top',
               // x: -40,
               // y: 80,
                floating: true,
               // borderWidth: 1,
               // backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
               // shadow: true
            },
            series: [{name: 'IB', data: JSON.parse(ibcount)},
                {name: 'OB', data: JSON.parse(obcount)}]
//                     series: [{name: 'IN', data: [49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]},
//                         {name: 'OUT', data: [83.6, 78.8, 98.5, 93.4, 106.0, 84.5, 105.0, 104.3, 91.2, 83.5, 106.6, 92.3]}]
        });


        //TopTpEdiValumes
        var xaxisTpvalues = document.getElementById("top10EdiTpPartners").value;
        var xAxisTp = xaxisTpvalues.split(',');
        Highcharts.chart('TopTpEdiValumes', {
            chart: {
                type: 'bar'
            },
            title: {
                text: 'TOP 10 EDI TP BY VOLUMES'
            },
            xAxis: {
                categories: xAxisTp
            },
            yAxis: {
                min: 0,
                title: {
                    text: null
                }
            },
            //    tooltip: {top10TpPartners
            //        valueSuffix: ' millions'
            //    },
            plotOptions: {
                bar: {
                    dataLabels: {
                        enabled: false
                    },
                }
            },
    
            series: [{name: 'IB', data: JSON.parse(document.getElementById("topTpEdiPartnersOutboundCount").value)},
                {name: 'OB', data: JSON.parse(document.getElementById("topTpEdiPartnersInboundCount").value)}]
//            series: [{name: 'OUT', data: [49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1]},
//                {name: 'IN', data: [83.6, 78.8, 98.5, 93.4, 106.0, 84.5, 105.0, 104.3, 91.2, 83.5]}]
        });


        </script>
    </body>
</html>