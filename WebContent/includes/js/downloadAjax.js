
function gridDownload(sheetType,dwdType) {
 
    window.location="../download/gridDownload.action?downloadType="+dwdType+"&sheetType="+sheetType;
}

function gridDashboardDownload(sheetType,dwdType,inbound,outbound) {
  //  alert(inbound);
   // alert(outbound);
    if(inbound==''&&outbound=='')
    {
         alert("No INBOUND and OUTBOUND data to generate");        
    }
    
    
    else
    {
         window.location="../download/gridDownload.action?downloadType="+dwdType+"&sheetType="+sheetType+"&inbound="+inbound+"&outbound="+outbound;
    }
}

/*function gridDownloadReports(sheetType,dwdType) {
 
    window.location="../download/gridDownloadReports.action?downloadType="+dwdType+"&sheetType="+sheetType;
}*/


function grid1DashboardDownload(sheetType,dwdType,yaxis,xaxis) {
   
  var yearmonth=document.forms["documentForm"]["yearmonth"].value;
  if(sheetType=="dash"||sheetType=="dash1")
  var businessFlows=document.forms["documentForm"]["businessFlows"].value;
  else
  var businessFlows=document.forms["documentForm"]["tpPartnerId"].value;
  var direction=document.forms["documentForm"]["direction"].value;
  var docType=document.forms["documentForm"]["docType"].value;
  var fromYear=document.forms["documentForm"]["fromYear"].value;
  var fromMonth=document.forms["documentForm"]["fromMonth"].value;
  var formData=yearmonth+","+businessFlows+","+direction+","+docType+","+fromYear+","+fromMonth;
  //alert(formData);
    if(yaxis==''&&xaxis=='') {
         alert("No yaxis and xaxis data to generate");        
    }
    
    
    else
    {
         window.location="../download/gridDownload.action?downloadType="+dwdType+"&sheetType="+sheetType+"&yaxis="+yaxis+"&xaxis="+xaxis+"&formData="+formData;
         //alert(xaxis);
    }
}
