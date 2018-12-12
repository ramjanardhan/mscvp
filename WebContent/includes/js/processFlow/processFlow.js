

function  getProperties(data,operatorId){
	
	var flowDivClass=$('#flowDiv').attr('class');
	
	/*if(flowDivClass=='col-md-12'){
		$("#flowDiv").fadeIn("slow").removeClass("col-md-12");
		$("#flowDiv").fadeIn("slow").addClass("col-md-8");
	}*/
	$("#PropertiesDiv").slideDown(500);
	
	$("#tblProperties td").closest("tr").remove();
	 var rowCount = $("#tblProperties td").closest("tr").length;
	// alert(rowCount);
	 //alert(data);
	 
	 var obj = JSON.parse(data);
	 var v=0;
	// alert(Object.keys(obj).length);
         $("#propertiesTitle").html(obj["title"]+" Properties");
	 for (var key1 in obj) {
		
       //  result += "<option class=\"optionSelected\" value=\"" + key1 + "\">" + obj[key1] + "</option>";
v=v+1;
	//alert(key1);
		 console.log(v+"=="+key1+" =!= "+typeof(obj[key1]));  			
		 var val=obj[key1];
		 if(key1!="mapId" && key1!="inputs" && key1!="outputs"){
		 var trval="<tr><td style='text-transform: capitalize;'>"+key1+"</td>";
		if(obj[key1]!=undefined && obj[key1]!=null && typeof(obj[key1])=="object"){
			var obj2=obj[key1];
			 trval=trval+ "<td>"+Object.keys(obj2).length+"</td></tr>";
		}else{
		 trval=trval+ "<td>"+val+"</td></tr>";
		}
		 $('#tblProperties').append(trval);
     }
	 }
	 $("#operatorId").val(operatorId);
	 console.log("operatorId==="+operatorId)
	 if(operatorId.indexOf("process")>-1){
		 
		 $("#process_name").val(obj["title"]);
		 $("#direction").val(obj["direction"]);
		 $("#status").val(obj["status"]);
		 if(obj["outputs"]!=undefined){
			 $("#connections").val(Object.keys(obj["outputs"]).length);
			 }else{
				 $("#connections").val(0);
			 }
		 $("#tpName").val(obj["tpName"]);
		 $("#tpId").val(obj["tpId"]);
		 $("#tpSenderId").val(obj["tpSenderId"]);
		 $("#tpReceiverId").val(obj["tpReceiverId"]);
		 $("#transactionType").val(obj["transactionType"]);
		 $("#sourceMailBox").val(obj["sourceMailBox"]);
		 $("#description").val(obj["description"]);
                  $("#lookupAlias").val(obj["lookupAlias"]);
                  
                  	 if(obj["direction"]=="INBOUND"){
			 $('#lookupAlias').attr('readonly', true);
				
		 }else{
			 $('#lookupAlias').attr('readonly', false);
		 }
                  
		 $('#tblProperties').append("<tr><td></td><td align='right'><button type='button' onclick='showSourceModal();' class='btn btn-primary'>Edit</button></td>");
	 }
	
	 if(operatorId.indexOf("map")>-1){
		 $("#title").val(obj["title"]);
		 $("#mapStatus").val(obj["status"]);
		 $("#sequence").val(obj["sequence"]);
		 $("#mapType").val(obj["mapType"]);
		 $("#map").val(obj["map"]);
		 $("#multiple").val(obj["multiple"]);
		 if(obj["inputs"]!=undefined){
			 $("#inputs").val(Object.keys(obj["inputs"]).length);
			 }else{
				 $("#inputs").val(0);
			 }
			 if(obj["outputs"]!=undefined){
				 $("#outputs").val(Object.keys(obj["outputs"]).length);
				 }else{
					 $("#outputs").val(0);
				 }
			 $('#tblProperties').append("<tr><td></td><td align='right'><button type='button' onclick='showMapModal();' class='btn btn-primary'>Edit</button></td>");
	 }
	 if(operatorId.indexOf("target")>-1){
		 $("#targetTitle").val(obj["title"]);
		 $("#tagetStatus").val(obj["status"]);
		 $("#targetMailBox").val(obj["targetMailBox"]);
		 
		 if(obj["inputs"]!=undefined){
			 $("#targetInputs").val(Object.keys(obj["inputs"]).length);
			 }else{
				 $("#targetInputs").val(0);
			 }
		 $('#tblProperties').append("<tr><td></td><td align='right'><button type='button' onclick='showTargetMolal();' class='btn btn-primary'>Edit</button></td>");
	 }
	 
	 
}


function showMapModal() {
	 
	
	 $('#nodeModalButton').click(); 
	 
}

function showSourceModal() {
	 
	
	 $('#sourceModalButton').click(); 
	 
}

function showTargetMolal(){
	 $('#targetModalButton').click(); 
	
}
$('#generateSourceNode').click(function(){

	
	var operatorId=$("#operatorId").val();
	var title=$("#process_name").val();
	var direction=$("#direction").val();
	var outputs=$("#connections").val();
	var  status=$("#status").val();
	
	var tpName=$("#tpName").val();
	var tpId=$("#tpId").val();
	var tpSenderId=$("#tpSenderId").val();
	var tpReceiverId=$("#tpReceiverId").val();
	var transactionType=$("#transactionType").val();
	var sourceMailBox=$("#sourceMailBox").val();
	var description=$("#description").val();
        	var lookupAlias=$("#lookupAlias").val();
	if(tpName=="")
        {
             alert("Please enter tpName !!");
              return false;              
            
        }
        else if(tpId=="")
        {
            alert("Please enter tpId !!");
              return false;              
        }
        else if(tpSenderId=="")
        {
            alert("Please enter tpSenderId !!");
              return false;              
        }
        else if(tpReceiverId=="")
        {
            alert("Please enter tpReceiverId !!");
              return false;              
        }
         else if(transactionType=="")
        {
            alert("Please enter transactionType !!");
              return false;              
        }
        else if(sourceMailBox.trim()=="")
        {
            alert("Please enter sourceMailBox!!");
              return false;              
        }
        else if(direction=="OUTBOUND" && lookupAlias.trim()=="")
        {
            alert("Please enter lookupAlias!!");
              return false;              
        }
  else  {var data = $('#processFlow').flowchart('getData');
	
	data.operators[operatorId].properties['title']=title;
	
	var properties=data.operators[operatorId].properties;
	
	properties['direction']=direction;
	properties['status']=status;
	properties['tpName']=tpName;
	properties['tpId']=tpId;
	properties['tpSenderId']=tpSenderId;
	properties['tpReceiverId']=tpReceiverId;
	properties['transactionType']=transactionType;
	properties['sourceMailBox']=sourceMailBox;
	properties['description']=description;
        properties['lookupAlias']=lookupAlias;
	/*if(operatorId.includes("map")){
		var txt = $("#mapId option:selected").text();
		var val = $("#mapId option:selected").val();

		data.operators[operatorId].properties['mapId']=val;
		data.operators[operatorId].properties['mapType']=mapType;
	} */
	/*if(parseInt(inputs)>0){
		
			properties['inputs']={};
		
		var inputdata=properties['inputs'];
		for(var i=1;i<=parseInt(inputs);i++){
			var inputobj={};
			inputobj["label"]= "Input "+i;
	
			inputdata["input_"+i]=inputobj;
		}
	}else{
		if(properties['inputs']!=undefined){
			  delete properties['inputs'];
		}
	} */
	if(parseInt(outputs)>0){
		
			properties['outputs']={};
		
		var outputsdata=properties['outputs'];
		for(var i=1;i<=parseInt(outputs);i++){
			var outputobj={};
			outputobj["label"]= "Connection"+i;
		//	console.log(JSON.stringify(outputobj, null, 2));
			outputsdata["output_"+i]=outputobj;
		}
	}else{
		if(properties['outputs']!=undefined){
		  delete properties['outputs'];
	}
		
	}
	
	//console.log(JSON.stringify(inputs, null, 2));
	//console.log(inputs);
	
	var links=data['links'];
	var numberOfLinks=Object.keys(links).length;
	console.log("=============links=================");
	
	 for (var i in links) {
	
		console.log(JSON.stringify(links[i]));
		console.log(links[i]["fromOperator"]);
		var from=data.operators[links[i]["fromOperator"]].properties["outputs"][links[i]["fromConnector"]];
		console.log('from=='+from);
		if(from==undefined){
			delete links[i];
		}
		
	/*	var to=data.operators[links[i]["toOperator"]].properties["inputs"][links[i]["toConnector"]];
		if(to==undefined){
			delete links[i];
		}*/
	}
	// data = JSON.parse(myObj);
	 $('#processFlow').flowchart('setData', data);
	 data=JSON.stringify(data.operators[operatorId].properties, null, 2);
	 console.log(data);
	 getProperties(data,operatorId);
	
	 $('#sourceModalButton').click();
          return true;
         }
});



$('#generateNode').click(function(){
	//alert('hi');
	var operatorId=$("#operatorId").val();
	var title=$("#title").val();
	var inputs=$("#inputs").val();
	var outputs=$("#outputs").val();
	var  map=$("#map").val();
	 var mapType=$("#mapType").val();
	 var mapStatus=$("#mapStatus").val();
	 var sequence=$("#sequence").val();
	 var multiple=$("#multiple").val();
         if(map=="")
        {
             alert("Please enter map !!");
              return false;              
            
        }
        else
        {
	var data = $('#processFlow').flowchart('getData');
	
	
	var properties=data.operators[operatorId].properties;
	properties['title']=map;
	properties['map']=map;
	properties['mapType']=mapType;
	properties['status']=mapStatus;
	properties['sequence']=sequence;
	properties['multiple']=multiple;
	if(parseInt(inputs)>0){
		
			properties['inputs']={};
		
		var inputdata=properties['inputs'];
		for(var i=1;i<=parseInt(inputs);i++){
			var inputobj={};
			inputobj["label"]= "Input "+i;
		//	console.log(JSON.stringify(inputobj, null, 2));
			inputdata["input_"+i]=inputobj;
		}
	}else{
		if(properties['inputs']!=undefined){
			  delete properties['inputs'];
		}
	}
	if(parseInt(outputs)>0){
		
			properties['outputs']={};
		
		var outputsdata=properties['outputs'];
		for(var i=1;i<=parseInt(outputs);i++){
			var outputobj={};
			outputobj["label"]= "Output "+i;
		//	console.log(JSON.stringify(outputobj, null, 2));
			outputsdata["output_"+i]=outputobj;
		}
	}else{
		if(properties['outputs']!=undefined){
		  delete properties['outputs'];
	}
	}
	
	//console.log(JSON.stringify(inputs, null, 2));
	//console.log(inputs);
	
	var links=data['links'];
	var numberOfLinks=Object.keys(links).length;
	console.log("=============links=================");
	
	 for (var i in links) {
	
		console.log(JSON.stringify(links[i]));
		console.log(links[i]["fromOperator"]);
		var from=data.operators[links[i]["fromOperator"]].properties["outputs"][links[i]["fromConnector"]];
		console.log('from=='+from);
		if(from==undefined){
			delete links[i];
		}
		
		var to=data.operators[links[i]["toOperator"]].properties["inputs"][links[i]["toConnector"]];
		if(to==undefined){
			delete links[i];
		}
	}
	// data = JSON.parse(myObj);
	 $('#processFlow').flowchart('setData', data);
	 data=JSON.stringify(data.operators[operatorId].properties, null, 2);
	 console.log(data);
	 getProperties(data,operatorId);
	 $('#nodeModalButton').click();
          return true;
      }
});

$('#generateTargetNode').click(function(){
	//alert('hi');
	var operatorId=$("#operatorId").val();
	var title=$("#targetTitle").val();
	var inputs=$("#targetInputs").val();
	
	 var targetMailBox=$("#targetMailBox").val();
	 var status=$("#tagetStatus").val();
         
	if(targetMailBox==""){
             alert("Please enter mail path !!");
                            return false;
            
        }
	else{var data = $('#processFlow').flowchart('getData');
	
	
	var properties=data.operators[operatorId].properties;
	properties['title']=title;
	properties['targetMailBox']=targetMailBox;
	
	properties['status']=status;
	
	
	if(parseInt(inputs)>0){
		
			properties['inputs']={};
		
		var inputdata=properties['inputs'];
		for(var i=1;i<=parseInt(inputs);i++){
			var inputobj={};
			inputobj["label"]= "Input "+i;
		//	console.log(JSON.stringify(inputobj, null, 2));
			inputdata["input_"+i]=inputobj;
		}
	}else{
		if(properties['inputs']!=undefined){
			  delete properties['inputs'];
		}
	}
	
	
	//console.log(JSON.stringify(inputs, null, 2));
	//console.log(inputs);
	
	var links=data['links'];
	var numberOfLinks=Object.keys(links).length;
	console.log("=============links=================");
	
	 for (var i in links) {
	
		console.log(JSON.stringify(links[i]));
		console.log(links[i]["fromOperator"]);
	/*	var from=data.operators[links[i]["fromOperator"]].properties["outputs"][links[i]["fromConnector"]];
		console.log('from=='+from);
		if(from==undefined){
			delete links[i];
		}
		*/
		var to=data.operators[links[i]["toOperator"]].properties["inputs"][links[i]["toConnector"]];
		if(to==undefined){
			delete links[i];
		}
	}
	// data = JSON.parse(myObj);
	 $('#processFlow').flowchart('setData', data);
	 data=JSON.stringify(data.operators[operatorId].properties, null, 2);
	 console.log(data);
	 getProperties(data,operatorId);
	 $('#targetModalButton').click(); 
            return true;
         }    
     
});


var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope,$http) {
    
   
     $scope.mapList={};
       $scope.malboxList ={};
       $scope.malboxList2 ={};
  //alert("hi");
    $scope.getMapList=function(){
        
  
        
         $scope.mapList ={};
   
     if($scope.map.searchKey.length>0){
      
    	 $("#load1").show();
    	 $("#suggestionList1").hide();
    	 var userEmail = $("#map").val();
    	
    		var rno = Math.random();
    	
        url = "../ajax/doGetMapList.action?name="
		+ userEmail+"&rno="+rno;
       // alert(url)
         $http.post(url, {
        "SearchKey": $scope.map.searchKey
       
       
      }).then(function(response) {
    	
       
       
      console.log(JSON.stringify($scope.mapList));
      $("#load1").hide();
        
         
          if (JSON.stringify(response.data.isGetting) == "true") {
               $scope.mapList = response.data.mapList;
            
            $("#suggestionList1").show();
          } else if (JSON.stringify(response.data.isGetting) == "false") {
        	  $("#suggestionList1").hide();
         
          }
        
      })
      .catch(function(Error){
    	   console.log("Error"+Error);
      })
     }else{
    	   $("#suggestionList1").hide();
    	   $("#load1").hide();
     }
        
        //-----
    
}
    $scope.selectMap=function(map){
    
    	$("#map").val(map.name);
    	$("#suggestionList1").hide();
 
    $scope.mapList ={};
    }
    
     // for target mail box
    
       $scope.getTargetMailBoxList=function(){
        
  
        
         $scope.malboxList2 ={};
   
     if($scope.target.searchKey.length>0){
      
    	 $("#load3").show();
    	 $("#suggestionList3").hide();
    	 var name = $("#targetMailBox").val();
    	
    		var rno = Math.random();
    	
        url = "../ajax/doGetMailBoxList.action?name="
		+ name+"&rno="+rno;
       // alert(url)
         $http.post(url, {
        "SearchKey": $scope.target.searchKey
       
       
      }).then(function(response) {
    	
        
       
     
      $("#load3").hide();
        
         
          if (JSON.stringify(response.data.isGetting) == "true") {
            
             $scope.mailBoxList2 = response.data.mailBoxList;
            $("#suggestionList3").show();
          } else if (JSON.stringify(response.data.isGetting) == "false") {
        	  $("#suggestionList3").hide();
         
          }
        
      })
      .catch(function(Error){
    	   console.log("Error"+Error);
      })
     }else{
    	   $("#suggestionList3").hide();
    	   $("#load3").hide();
     }
        
        //-----
    
}
    $scope.selectTargetMailBox=function(mailBox2){
    
    	$("#targetMailBox").val(mailBox2.name);
    	$("#suggestionList3").hide();
 
    $scope.malboxList2 ={};
    }
    
    // for source mail box
    
       $scope.getSourceMailBoxList=function(){
        
  
        
         $scope.malboxList ={};
   
     if($scope.source.searchKey.length>2){
      
    	 $("#load2").show();
    	 $("#suggestionList2").hide();
    	 var name = $("#sourceMailBox").val();
    	
    		var rno = Math.random();
    	
        url = "../ajax/doGetMailBoxList.action?name="
		+ name+"&rno="+rno;
       // alert(url)
         $http.post(url, {
        "SearchKey": $scope.source.searchKey
       
       
      }).then(function(response) {
    	
        
       
     
      $("#load2").hide();
        
         
          if (JSON.stringify(response.data.isGetting) == "true") {
            
             $scope.mailBoxList = response.data.mailBoxList;
            $("#suggestionList2").show();
          } else if (JSON.stringify(response.data.isGetting) == "false") {
        	  $("#suggestionList2").hide();
         
          }
        
      })
      .catch(function(Error){
    	   console.log("Error"+Error);
      })
     }else{
    	   $("#suggestionList2").hide();
    	   $("#load2").hide();
     }
        
        //-----
    
}
    $scope.selectSourceMailBox=function(mailBox){
    
    	$("#sourceMailBox").val(mailBox.name);
    	$("#suggestionList2").hide();
 
    $scope.malboxList ={};
    }
    
    //Tp details
    
    
       $scope.getTpList=function(){
        
  
        
         $scope.TpList ={};
   
     if($scope.tp.searchKey.length>2){
      
    	 $("#load4").show();
    	 $("#suggestionList4").hide();
    	 var name = $("#tpName").val();
    	
    		var rno = Math.random();
    	
        url = "../ajax/doGetTpList.action?name="
		+ name+"&rno="+rno;
       // alert(url)
         $http.post(url, {
        "SearchKey": $scope.tp.searchKey
       
       
      }).then(function(response) {
    	
        
       
     
      $("#load4").hide();
        
         
          if (JSON.stringify(response.data.isGetting) == "true") {
            
             $scope.TpList = response.data.tpList;
            $("#suggestionList4").show();
          } else if (JSON.stringify(response.data.isGetting) == "false") {
        	  $("#suggestionList4").hide();
         
          }
        
      })
      .catch(function(Error){
    	   console.log("Error"+Error);
      })
     }else{
    	   $("#suggestionList4").hide();
    	   $("#load2").hide();
     }
        
        //-----
    
}
    $scope.selectTpName=function(tp){
    
    	$("#tpName").val(tp.name);
        $("#tpId").val(tp.id);
        
    	$("#suggestionList4").hide();
 
    $scope.TpList ={};
    }
});

function   getExistedFlow(){
	var processId=$('#processId').val();
	//alert('hi');
	var mainjson={};
	mainjson['processId']=processId;
	 var json = JSON.stringify(mainjson);
	 //alert(json);
    $("#load").show();
	 $.ajax({
	       
	     	url:'../ajax/getProcessFlow.action?random='+Math.random(),
	         data:{jsonData: json},
	         contentType: 'application/json',
	         type: 'GET',
	         context: document.body,
	         success: function(responseText) {
	        	 var json = $.parseJSON(responseText);
	        	 var data=JSON.parse(json["flowData"]);
	        	
	        	 console.log(data);
	        	 $('#processFlow').flowchart('setData', data);
	        	 $("#load").hide();
	               
	         },
	         error: function(e){
	        	 $("#load").hide();
	         }
	     });
}

var opertorTitleId=0;

function generateOperatorId(operator,numb){
	
	var data = $('#processFlow').flowchart('getData');
	
	console.log("exists=="+exists)
	while(true){
		console.log(operator +' '+numb);
		var exists=data.operators[operator+numb];
	if(exists==undefined){
		opertorTitleId=numb;
	var operatiorId=operator+numb+'';
		console.log('asd=='+operatiorId)
		return operatiorId;
	}else{
		numb+=1;
		
	}
	}
	
}	
	$('#saveFlow').click(function(){
		
		var data = $('#processFlow').flowchart('getData');
	//	alert( JSON.stringify(data));
		var process=[];
		var maps=[];
		var targets=[];
		var Operators=data.operators;
		var Links=data.links;
		var numberOfOperators=Object.keys(Operators).length;
		var numberOfLinks=Object.keys(Links).length;
		if(numberOfOperators<1){
			alert('Nothing to save');
			return false;
		}
		if(numberOfLinks<1){
			alert('There is no connection between nodes');
			return false;
		}
		console.log("numberOfOperators"+numberOfOperators);
		var keys=[];
		for (operator in Operators) {
		   
		
		    if(operator.indexOf('process')>-1){
		    	process.push(operator);
			}else  if(operator.indexOf('map')>-1){
				maps.push(operator);
			}else  if(operator.indexOf('target')>-1){
				targets.push(operator);
			}
			
		}
		console.log("process "+process);
		console.log("maps="+maps);
		console.log("targets "+targets);
		 for(var ids=0;ids<process.length;ids++){
			 console.log('loop'+process[ids]);
			
				 console.log("having");
				 var property = data.operators[process[ids]].properties;
				
			  console.log('property'+property["title"]);
			  console.log('property'+property["tpId"]);
			  console.log('property'+property["tpName"]);
                          if(property["tpName"].trim()==""){
                              alert('please enter tpname for '+property['title']);
                              return false;
                          }
                          if(property["tpId"].trim()==""){
                              alert('please enter tpId for '+property['title']);
                              return false;
                          }
                           if(property["tpSenderId"].trim()==""){
                              alert('please enter tpSenderId for '+property['title']);
                              return false;
                          }
                          if(property["tpReceiverId"].trim()==""){
                              alert('please enter tpReceiverId for '+property['title']);
                              return false;
                          }
                           if(property["transactionType"].trim()==""){
                              alert('please enter transactionType for '+property['title']);
                              return false;
                          }
                          if(property["sourceMailBox"].trim()==""){
                              alert('please enter sourceMailBox for '+property['title']);
                              return false;
                          }
                          
                          
         if(property["direction"].trim()=="OUTBOUND" && property["lookupAlias"].trim()=="")
        {
            alert('Please enter lookupAlias for '+property['title']);
              return false;              
        }  
                          
                    }
                    for(var ids=0;ids<maps.length;ids++){
			 console.log('loop'+maps[ids]);
				 console.log("having");
				 var property = data.operators[maps[ids]].properties;
				
                          if(property["map"].trim()==""){
                              alert('please enter map name for '+property['title']);
                              return false;
                          }
                          
                          
                          
                    }
                    for(var ids=0;ids<targets.length;ids++){
			 console.log('loop'+targets[ids]);
				 console.log("having");
				 var property = data.operators[targets[ids]].properties;
				
                          if(property["targetMailBox"].trim()==""){
                              alert('please enter targetMailBox for '+property['title']);
                              return false;
                          }
                          
                          
                          
                    }
		
		var links=data['links'];
		var numberOfLinks=Object.keys(links).length;
		console.log("=============links=================");
		var fromOperators=[];
		var fromOperatorConnectors=[];
		var toOperators=[];
		var toOperatorsConnectors=[];
		 for (var i in links) {
		
			console.log(JSON.stringify(links[i]));
			console.log(links[i]["fromOperator"]);
			var from=data.operators[links[i]["fromOperator"]].properties["outputs"][links[i]["fromConnector"]];
		//	console.log('from=='+from);
			fromOperators.push(links[i]["fromOperator"]);
			fromOperatorConnectors.push(links[i]["fromOperator"]+links[i]["fromConnector"]);
			var to=data.operators[links[i]["toOperator"]].properties["inputs"][links[i]["toConnector"]];
			toOperators.push(links[i]["toOperator"]);
			toOperatorsConnectors.push(links[i]["toOperator"]+links[i]["toConnector"]);
		}
		 
		 console.log("fromOperators=="+fromOperators);
		 console.log("toOperators=="+toOperators);
		 
		console.log("fromOperatorConnectors "+fromOperatorConnectors);
		console.log("toOperatorsConnectors "+toOperatorsConnectors);
		 for(var ids=0;ids<process.length;ids++){
			 console.log('loop'+process[ids]);
			 if(fromOperators.indexOf(process[ids])!=-1){
				 console.log("having");
				 var outs = data.operators[process[ids]].properties['outputs'];
				 for(var out in outs){
					 console.log(process[ids]+out);
				 if(fromOperatorConnectors.indexOf(process[ids]+out)==-1){
					var title=data.operators[process[ids]].properties['title'];
					var lable= data.operators[process[ids]].properties['outputs'][out]['label'];
					 alert("no link from "+title+" "+lable);
					 return false;
				 }
				 }
			 }else{
				 var title=data.operators[process[ids]].properties['title'];
				 alert("no link from "+title);
				 return false;
			 }
		 }
		 
		 for(var ids=0;ids<maps.length;ids++){
			 console.log('loop'+maps[ids]);
			 
			 if(toOperators.indexOf(maps[ids])!=-1){
				 console.log("having");
				 var inputs = data.operators[maps[ids]].properties['inputs'];
				 for(var input in inputs){
					 console.log(maps[ids]+input);
				 if(toOperatorsConnectors.indexOf(maps[ids]+input)==-1){
					var title=data.operators[maps[ids]].properties['title'];
					var lable= data.operators[maps[ids]].properties['inputs'][input]['label'];
					 alert("no  link to "+title+" "+lable);
					 return false;
				 }
				 }
			 }else{
				 var title=data.operators[maps[ids]].properties['title'];
				 alert("no input link to "+title);
				 return false;
			 }
			 
			 if(fromOperators.indexOf(maps[ids])!=-1){
				 console.log("having");
				 var outs = data.operators[maps[ids]].properties['outputs'];
				 for(var out in outs){
					 console.log(maps[ids]+out);
				 if(fromOperatorConnectors.indexOf(maps[ids]+out)==-1){
					var title=data.operators[maps[ids]].properties['title'];
					var lable= data.operators[maps[ids]].properties['outputs'][out]['label'];
					 alert("no link from "+title+" "+lable);
					 return false;
				 }
				 }
			 }else{
				 var title=data.operators[maps[ids]].properties['title'];
				 alert("no output link to "+title);
				 return false;
			 }
			 
			
		 }
		 
		 for(var ids=0;ids<targets.length;ids++){
			 console.log('loop'+targets[ids]);
			 
			 if(toOperators.indexOf(targets[ids])!=-1){
				 console.log("having");
				 var inputs = data.operators[targets[ids]].properties['inputs'];
				 for(var input in inputs){
					 console.log(targets[ids]+input);
				 if(toOperatorsConnectors.indexOf(targets[ids]+input)==-1){
					var title=data.operators[targets[ids]].properties['title'];
					var lable= data.operators[targets[ids]].properties['inputs'][input]['label'];
					 alert("no  link to "+title+" "+lable);
					 return false;
				 }
				 }
			 }else{
				 var title=data.operators[targets[ids]].properties['title'];
				 alert("no input link to "+title);
				 return false;
			 }
		
			
		 }
		 for(var i=0;i<fromOperators.length;i++){
			 if(fromOperators[i].indexOf('process')>-1){
				 if(toOperators[i].indexOf('map')<-1){
					 alert("source should be connect to map");
					 return false;
				 }
				 if(fromOperators[i]==toOperators[i]){
					 alert("source should not connect to itself");
					 return false;
				 }
			 }
			 if(fromOperators[i].indexOf('map')>-1){
				 if(fromOperators[i]==toOperators[i]){
					 alert("map should be connect to itself");
					 return false;
				 }
			 }
		 }
		 $("#saveFlow").attr("disabled", true);
		 
		 $("#load").show();
                // alert(JSON.stringify(data));
		 $.ajax({
		       
		     	url:'../ajax/saveProcessFlow.action?random='+Math.random(),
		         data:{jsonData: JSON.stringify(data)},
		         contentType: 'application/json',
		         type: 'GET',
		         context: document.body,
		         success: function(responseText) {
                             
                           //  alert("hee"+responseText)
                             
		        	 var json = $.parseJSON(responseText);
		        	 var result=json["result"];
		        	 if(result=='success'){
		        	 var data=json["flowData"];
		        	
		        	 console.log(data);
		        	 
		        	 $('#processFlow').flowchart('setData', data);
		        	 var processId=json["processId"];
		        	 $("#processId").val(processId);
		        	// $("#saveFlow").hide();
		        	 $("#saveFlow").attr("disabled", false);
		        	 $("#load").hide();
		        	 $("#resultMessage").html("Flow saved successfully!");
		        	  $(".alert").fadeIn(500);
		        	  
		        	  $(".alert").removeClass('alert-danger');
		        	  $(".alert").addClass('alert-success');
		        	 $(".alert").fadeOut(5000);
		        	 }
		        	 else{
		        		 $("#load").hide();
		        		 $(".alert").addClass('alert-danger');
		        		 $("#resultMessage").html("Something went wrong try again later!");
		        		 $("#saveFlow").attr("disabled", false);
			        	  $(".alert").fadeIn(500);
			             
			        	 $(".alert").fadeOut(3000);
		        	 }
		               
		         },
		         error: function(e){
		           
		         }
		     });
		 
		
});
	
	function openNav(){
		if( document.getElementById("mySidenav").style.width == "200px")
		 document.getElementById("mySidenav").style.width = "0px";
		else
			 document.getElementById("mySidenav").style.width = "200px";
	}
	function closeNav() {
	    document.getElementById("mySidenav").style.width = "0";
	}
        
        function setLookupAlias(){
	var direction=	$("#direction").val();
		if(direction=="OUTBOUND"){
			$('#lookupAlias').attr('readonly', false);
			$('#lookupAlias').val("");
		}else{
			$('#lookupAlias').attr('readonly', true);
			$('#lookupAlias').val("NA");
		}
	}
                 function fieldLengthValidator(element)
                     {
                        
                    var i=0;
                    if(element.id=="transactionType" || element.id=="lookupAlias")
                    {
                    i=50;
                    }
                    if(element.id=="tpName" || element.id=="tpId" || element.id=="tpSenderId" || element.id=="tpReceiverId")
                    {
                    i=100;
                    }
                    if(element.id=="process_name")
                    {
                    i=200;
                    }
                    if(element.id=="sourceMailBox" || element.id=="map" || element.id=="targetMailBox")
                    {
                    i=500;
                    }
                     if(element.id=="description")
                    {
                    i=1000;
                    }
                    
//                    if(element.value.length>i)
//                    {
//                    alert("You have entered more than "+i +" "+"characters for"+" "+element.id);
//                    return false;
//                    }
                    if (element.value.replace(/^\s+|\s+$/g, "").length > i)
                    {
                      
                    var  str = new String(element.value);
                    element.value = str.substring(0, i);
                    alert("You have entered more than "+i +" "+"characters for"+" "+element.id);
//                  document.getElementById('tpResultMessage').innerHTML = "<font color=red>The Value must be less than " + i + " characters</font>";
                    element.focus();
                    return false;
            }
                    return true;
     }
                    

    
