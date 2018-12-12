/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function defaultHide(){
    var test = document.getElementById("tppageId").value;
    if(test==0){
      
    }else{
              
                
        //document.getElementById("commId").;
        document.getElementById("commId").readOnly = true;
               
    }
        
}

function checkThirdTpValues() {
    var id = document.getElementById("commId").value;
    var name = document.getElementById("commName").value;
     
    if((id==null)||(id=="")){
        alert("Please Enter Comm Id!"); 
        return false;
    }else if((name==null)||(name=="")){
        alert("Please enter Comm Name!");
        return false;
    }
    else {
        return true;
    }
}
function resetvalues()
{
    document.getElementById('commId').value="";
    document.getElementById('commName').value="";
    document.getElementById('tradingPartnerName').value="";
    document.getElementById('contactName').value="";
    document.getElementById('bvrUdiCommId').value="";
    document.getElementById('bvrUdiName').value="";
    document.getElementById('phno').value="";
    document.getElementById('email').value="";
    document.getElementById('address').value="";
    document.getElementById('city').value="";
    document.getElementById('state').value="-1";
    document.getElementById('zip').value="";
    document.getElementById('network').value="-1";
    document.getElementById('url').value="";
    document.getElementById('basic').checked=false;
    document.getElementById('soq').checked=false;
    document.getElementById('store').checked=false;
    document.getElementById('master').checked=false;
   
    document.tradingPartnerForm.developing[0].checked = true;
    document.tradingPartnerForm.developing[1].checked = false;
 
    
    document.getElementById('vendorNo').value="";
    document.getElementById('deptNo').value="";
    document.getElementById('orderDuns').value="";
    document.getElementById('shipDuns').value="";
    document.getElementById('payDuns').value="";
    document.getElementById('buyerName').value="";
    document.getElementById('buyerPhone').value="";
    document.getElementById('buyerEmail').value="";
    document.getElementById('csName').value="";
    document.getElementById('csPhone').value="";
    document.getElementById('csEmail').value="";
    document.getElementById('notes').value="";
    $('#detail_box').hide();
    
    showinit();
//$('#gridDiv').hide();
   
}

function checkTpValues() {
    var ediCommId = document.getElementById("commId").value;
    var ediCommName = document.getElementById("commName").value;
    var network = document.getElementById("network").value;
    var url = document.getElementById("url").value;
    if((ediCommId == "") || (ediCommName == "")) {
        alert ("Please Enter Mandatory Fields!");
        return false;
    }
    if((network == "AS2")&&(url == "")) {
        alert ("URL Required when network is AS2!");
        return false;
    }
    return true;
    
}

function getTpList(){
    var tpid = document.getElementById("tpid").value;
    var tpname = document.getElementById("tpname").value;
    location.href = "../tradingPartner/backToSearchList?tpid="+tpid+"&tpname="+tpname;
    return true;
}

/** tp validations ***/

function noteValidate(){
    var note= document.tradingPartnerForm.notes;
    
    
    if (note.value != null && (note.value != "")) {
        if(note.value.replace(/^\s+|\s+$/g,"").length>1000){
            
            str = new String(document.tradingPartnerForm.notes.value);
            document.tradingPartnerForm.notes.value=str.substring(0,1000);
            
            alert("The note must be less than 1000 characters");
            
       
           
        }
        document.tradingPartnerForm.notes.focus();
        return (false);
    }
  
    return (true);
};

function fieldLengthValidator(element) {
    var i=0;
    var k=0;
    //alert("In Field Length validator ESCV");
    if(element.value!=null&&(element.value!="")) {
        if(element.id=="url") { 
            i=50;
        }
        //alert(element.id);
        if(element.id == 'commId'|| element.id == 'email' || element.id == 'csEmail' || element.id == 'buyerEmail'){
            i=50;  
        }
        if(element.id == 'zip'){
            i=10;
            validatenumber(element);
        }
        if(element.id == 'phno'||element.id == 'csPhone'||element.id == 'buyerPhone'){
            k=1;
            i=15; 
            validatenumber(element);
        }
        if(element.id == 'commName' || element.id == 'tradingPartnerName' || element.id == 'contactName'|| 
            element.id == 'commName' || element.id == 'buyerName' || element.id == 'csName' || element.id == 'bvrUdiName' || element.id == 'address' || element.id == 'city' || element.id == 'vendorNo' || element.id == 'shipDuns' || element.id == 'payDuns' || element.id == 'deptNo') { 
            i=50;
        }
        if(element.id == 'orderDuns'||element.id == 'bvrUdiCommId'){
            i=50;
        }
        if(k==1){
            
            if(element.value.replace(/^\s+|\s+$/g,"").length<=9) {
                str=new String(element.value);
                element.value=str.substring(0,i);
            
                alert("The "+element.id+" must be greater than 9 characters");
                element.value="";
                element.focus();
                return false;
            }
            
            if(element.value.replace(/^\s+|\s+$/g,"").length>i) {
                str=new String(element.value);
                element.value=str.substring(0,i);
                alert("The "+element.id+" must be less than "+i+" characters");
                element.focus();
                return false;
            }
        }else{
            if(element.value.replace(/^\s+|\s+$/g,"").length>i) {
                str=new String(element.value);
                element.value=str.substring(0,i);
                alert("The "+element.id+" must be less than "+i+" characters");
                element.focus();
                return false;
            }
        }
        return true;
        
    }
}

function doAddPartner(){
    //alert("doAddPartner called");
    var partnerName=document.getElementById("partnerName").value;
    // alert('partnerName----'+partnerName);
    var partnerIdentifier=document.getElementById("partnerIdentifier").value;
    //alert('partnerIdentifier----'+partnerIdentifier);
    if(partnerName.trim()=="" || partnerName.trim()== null){
        document.getElementById("resultMessage").innerHTML="<font color='red'>Please fill the Partner Name !!</font>";
        return false;
    }else if(partnerIdentifier.trim()=="" || partnerIdentifier.trim()== null){
        document.getElementById("resultMessage").innerHTML="<font color='red'>Please fill the Partner Identifier !!</font>";
        return false;
    }else{
        return true;
    }
  
}

function doAddRouting(){
    // alert("doAddRouting called");
    var name=document.getElementById("name").value;
    //alert('name----'+name);
    var acceptorLookupAlias=document.getElementById("acceptorLookupAlias").value;
    //alert('acceptorLookupAlias----'+acceptorLookupAlias);
    if(name.trim()=="" || name.trim()== null){
        document.getElementById("resultMessage").innerHTML="<font color='red'>Please fill the Name!</font>";
        return false;
    }else if(acceptorLookupAlias.trim()=="" || acceptorLookupAlias.trim()== null){
        document.getElementById("resultMessage").innerHTML="<font color='red'>Please fill the AcceptorLookupAlias!</font>";
        return false;
    }else{
        return true;
    }
  
}


function doAddB2BChannel()
{
    
    var partnerName=document.getElementById("partnerName").value;
    var direction=document.getElementById("direction").value;
    var protocol=document.getElementById("protocol").value;
    var host=document.getElementById("host").value;
    var userName=document.getElementById("userName").value;
    var producerMailbox=document.getElementById("producerMailBox").value;
    var consumerMailbox=document.getElementById("consumerMailBox").value;
    var pollingCode=document.getElementById("pollingCode").value;
    var appId=document.getElementById("appId").value;
    var senderId=document.getElementById("senderId").value;
    var receiverId=document.getElementById("receiverId").value;
    
    if(partnerName.trim() =="" || partnerName.trim() ==null){
        document.getElementById("resultMessage").innerHTML="<font color='red'>Please enter the partner name!</font>";
        return false;
    }

    else if(direction.trim() =="" || direction.trim() ==null){
        document.getElementById("resultMessage").innerHTML="<font color='red'>Please enter the direction!</font>";
        return false;
    }
    else if(protocol.trim() =="" || protocol.trim() ==null){
        document.getElementById("resultMessage").innerHTML="<font color='red'>Please enter the protocol!</font>";
        return false;
    }
    else if(host.trim() =="" || host.trim() ==null){
        document.getElementById("resultMessage").innerHTML="<font color='red'>Please enter the host!</font>";
        return false;
    }
    else if(userName.trim() =="" || userName.trim() ==null){
        document.getElementById("resultMessage").innerHTML="<font color='red'>Please enter the user name!</font>";
        return false;
    }
    else if(producerMailbox.trim() =="" || producerMailbox.trim() ==null){
        document.getElementById("resultMessage").innerHTML="<font color='red'>Please enter the producer mailbox!</font>";
        return false;
    }
    else if(consumerMailbox.trim() =="" || consumerMailbox.trim() ==null){
        document.getElementById("resultMessage").innerHTML="<font color='red'>Please enter the consumer mailbox!</font>";
        return false;
    }
    else if(pollingCode.trim() =="" || pollingCode.trim() ==null){
        document.getElementById("resultMessage").innerHTML="<font color='red'>Please enter the polling code!</font>";
        return false;
    }
    else if(appId.trim() =="" || appId.trim() ==null){
        document.getElementById("resultMessage").innerHTML="<font color='red'>Please enter the application Id!</font>";
        return false;
    }
    else if(senderId.trim() =="" || senderId.trim() ==null){
        document.getElementById("resultMessage").innerHTML="<font color='red'>Please enter the sender Id!</font>";
        return false;
    }
    else if(receiverId.trim() =="" || receiverId.trim() ==null){
        document.getElementById("resultMessage").innerHTML="<font color='red'>Please enter the receiver Id!</font>";
        return false;
    }
    else{
        return true;
    }
}
 
