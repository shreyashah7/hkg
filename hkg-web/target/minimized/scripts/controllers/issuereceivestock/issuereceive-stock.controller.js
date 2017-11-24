define(["hkg","customFieldService","ngload!uiGrid","addMasterValue","dynamicForm","accordionCollapse","issuereceivestockService","lotService"],function(a){a.register.controller("IssueReceiveController",["$rootScope","$scope","DynamicFormService","CustomFieldService","IssueReceiveStockService","CenterCustomFieldService","$timeout","LotService","$filter",function(o,k,t,I,J,n,z,i,E){o.maskLoading();
o.mainMenu="stockLink";
o.childMenu="newIssueReceive";
k.entity="ISSUERECEIVE.";
o.activateMenu();
k.dataVals={roughFlag:true,lotFlag:true,packetFlag:true};
k.stockDtls=[];
k.IIStockDtls=[];
k.associatedDeptList=[];
k.designationList=[];
k.medium="";
k.directReceiveArray=[];
k.isUserInvalid=true;
k.selectorIds=[];
k.selectorIdsForIssue=[];
k.generalCustom={};
k.fieldDbType={};
var e=[];
var h=[];
var d=[];
var H=[];
var F={};
var A={};
var q={};
var L={};
var s=[];
var B={};
var M={id:"Parcels",displayName:"Parcels",parentId:0,parentName:"None"};
var j={id:"Lots",displayName:"Lots",parentId:0,parentName:"None"};
var p={id:"Packets",displayName:"Packets",parentId:0,parentName:"None"};
k.treeForCollect=[];
k.treeForIssue=[];
k.treeForReturn=[];
k.treeForRequest=[];
function w(){k.treeForCollect=[];
if(F.Parcels!==null&&F.Parcels!==undefined&&F.Parcels.length>0){var Q=[];
angular.forEach(F.Parcels,function(U){var V={id:U,displayName:U,parentId:"Parcels",parentName:"Parcels"};
Q.push(V)
});
var O=angular.copy(M);
O.children=Q;
O.displayName="Parcels ("+Q.length+")";
k.treeForCollect.push(O)
}if(F.Lots!==null&&F.Lots!==undefined&&F.Lots.length>0){var T=[];
angular.forEach(F.Lots,function(V){var U={id:V,displayName:V,parentId:"Lots",parentName:"Lots"};
T.push(U)
});
var S=angular.copy(j);
S.children=T;
S.displayName="Lots ("+T.length+")";
k.treeForCollect.push(S)
}if(F.Packets!==null&&F.Packets!==undefined&&F.Packets.length>0){var R=[];
angular.forEach(F.Packets,function(V){var U={id:V,displayName:V,parentId:"Lots",parentName:"Lots"};
R.push(U)
});
var P=angular.copy(p);
P.children=R;
P.displayName="Packets ("+R.length+")";
k.treeForCollect.push(P)
}}function r(){k.treeForIssue=[];
if(A.Parcels!==null&&A.Parcels!==undefined&&A.Parcels.length>0){var Q=[];
angular.forEach(A.Parcels,function(U){var V={id:U,displayName:U,parentId:"Parcels",parentName:"Parcels"};
Q.push(V)
});
var O=angular.copy(M);
O.children=Q;
O.displayName="Parcels ("+Q.length+")";
k.treeForIssue.push(O)
}if(A.Lots!==null&&A.Lots!==undefined&&A.Lots.length>0){var T=[];
angular.forEach(A.Lots,function(V){var U={id:V,displayName:V,parentId:"Lots",parentName:"Lots"};
T.push(U)
});
var S=angular.copy(j);
S.children=T;
S.displayName="Lots ("+T.length+")";
k.treeForIssue.push(S)
}if(A.Packets!==null&&A.Packets!==undefined&&A.Packets.length>0){var R=[];
angular.forEach(A.Packets,function(V){var U={id:V,displayName:V,parentId:"Lots",parentName:"Lots"};
R.push(U)
});
var P=angular.copy(p);
P.children=R;
P.displayName="Packets ("+R.length+")";
k.treeForIssue.push(P)
}}function b(){k.treeForReturn=[];
if(q.Parcels!==null&&q.Parcels!==undefined&&q.Parcels.length>0){var Q=[];
angular.forEach(q.Parcels,function(U){var V={id:U,displayName:U,parentId:"Parcels",parentName:"Parcels"};
Q.push(V)
});
var O=angular.copy(M);
O.children=Q;
O.displayName="Parcels ("+Q.length+")";
k.treeForReturn.push(O)
}if(q.Lots!==null&&q.Lots!==undefined&&q.Lots.length>0){var T=[];
angular.forEach(q.Lots,function(V){var U={id:V,displayName:V,parentId:"Lots",parentName:"Lots"};
T.push(U)
});
var S=angular.copy(j);
S.children=T;
S.displayName="Lots ("+T.length+")";
k.treeForReturn.push(S)
}if(q.Packets!==null&&q.Packets!==undefined&&q.Packets.length>0){var R=[];
angular.forEach(q.Packets,function(V){var U={id:V,displayName:V,parentId:"Lots",parentName:"Lots"};
R.push(U)
});
var P=angular.copy(p);
P.children=R;
P.displayName="Packets ("+R.length+")";
k.treeForReturn.push(P)
}}function v(){k.treeForRequest=[];
if(L.Parcels!==null&&L.Parcels!==undefined&&L.Parcels.length>0){var Q=[];
angular.forEach(L.Parcels,function(U){var V={id:U,displayName:U,parentId:"Parcels",parentName:"Parcels"};
Q.push(V)
});
var O=angular.copy(M);
O.children=Q;
O.displayName="Parcels ("+Q.length+")";
k.treeForRequest.push(O)
}if(L.Lots!==null&&L.Lots!==undefined&&L.Lots.length>0){var T=[];
angular.forEach(L.Lots,function(V){var U={id:V,displayName:V,parentId:"Lots",parentName:"Lots"};
T.push(U)
});
var S=angular.copy(j);
S.children=T;
S.displayName="Lots ("+T.length+")";
k.treeForRequest.push(S)
}if(L.Packets!==null&&L.Packets!==undefined&&L.Packets.length>0){var R=[];
angular.forEach(L.Packets,function(V){var U={id:V,displayName:V,parentId:"Lots",parentName:"Lots"};
R.push(U)
});
var P=angular.copy(p);
P.children=R;
P.displayName="Packets ("+R.length+")";
k.treeForRequest.push(P)
}}k.availableCollectStock=[];
k.format=o.dateFormat;
k.open=function(O){O.preventDefault();
O.stopPropagation()
};
i.retrieveFranchiseDetails(function(O){k.franchiseList=angular.copy(O)
});
k.payload={slipnoDate:new Date(),selectedFranchise:o.session.companyId};
k.dateOptions={};
var D=["II","RI","RQ","CL","IS","RC","RT"];
var N=["Inward","Outward"];
k.codeToType={IW:"Inward",OW:"Outward"};
var g={R:"Rough",L:"Lot",P:"Packet"};
var C={RQ:"Request",CL:"Collect",IS:"Issue",RC:"Receive",RT:"Return"};
k.codeToOperation={II:"Issue Direct",RI:"Receive Direct",RQ:"Request",CL:"Collect",IS:"Issue",RC:"Receieve"};
k.initForm=function(){J.retrieveModifiers(function(R){k.type=[];
k.operation=[];
k.configuredMediums=[];
if(R.Types!==null&&R.Types!==undefined){angular.forEach(R.Types,function(S){k.type.push({key:S,value:k.codeToType[S]})
});
if(k.type.length>0){k.dataVals.type=k.type[0]["key"]
}}if(R.Modes!==null&&R.Modes!==undefined){if(R.Modes.indexOf("DR")!==-1){k.operation.push({key:"II",value:"Issue Inward"});
k.operation.push({key:"RI",value:"Receive Inward"})
}if(R.Modes.indexOf("VS")!==-1){if(R.AccessRights!==null&&R.AccessRights!==null){angular.forEach(R.AccessRights,function(S){k.operation.push({key:S,value:C[S]})
})
}}var O={};
for(var Q=0;
Q<k.operation.length;
Q++){O[k.operation[Q].key]=k.operation[Q]
}var P=0;
for(var Q=0;
Q<D.length;
Q++){if(O[D[Q]]!==undefined&&O[D[Q]]!==null){k.operation[P++]=O[D[Q]]
}}O=undefined;
if(k.operation.length>0){k.dataVals.operation=k.operation[0]["key"]
}c(k.dataVals.operation)
}if(R.Medium!==null&&R.Medium!==undefined){angular.forEach(R.Medium,function(S){k.configuredMediums.push(g[S])
})
}if(k.dataVals.operation==="CL"){k.retrieveAvailableCollectStock()
}else{if(k.dataVals.operation==="IS"){k.retrieveAvailableIssueStock();
k.retrievePendingIssuedStock()()
}else{if(k.dataVals.operation==="RT"){k.retrieveAvailableReturnStock()()
}else{if(k.dataVals.operation==="RC"){k.retrievePendingReceivedStock()
}else{if(k.dataVals.operation==="RQ"){k.retrieveAvailableRequestStock()
}}}}}k.loadList()
})
};
k.loadList=function(){delete k.dataVals.userId;
delete k.displayName;
if(k.dataVals.type==="IW"){if(k.dataVals.designation!==undefined&&k.dataVals.designation!==null&&k.designationList.length!==0){k.userList=k.retrieveUsersByDesignation(k.dataVals.designation)
}if(k.designationList.length===0){J.retrieveDesignationByDept(o.session.department,function(O){if(O!==undefined&&O!==null&&O.length>0){k.designationList=O;
k.dataVals.designation=O[0].value.toString();
k.userList=k.retrieveUsersByDesignation(k.dataVals.designation)
}})
}k.medium=k.configuredMediums[k.configuredMediums.length-1]
}if(k.dataVals.type==="OW"){if(k.dataVals.department!==undefined&&k.dataVals.department!==null&&k.associatedDeptList.length!==0){k.userList=m(k.dataVals.department)
}if(k.associatedDeptList.length===0){J.retrieveAssociatedDepartments(null,function(P){if(P!==undefined&&P!==null&&P.length>0){var O=P.length;
while(O--){if(k.configuredMediums.indexOf(P[O].commonId)===-1){P.splice(O,1)
}}k.associatedDeptList=P;
if(P!==undefined&&P!==null&&P.length>0){k.dataVals.department=P[0].value;
k.userList=m(k.dataVals.department);
k.medium=P[0].commonId
}}})
}else{k.dataVals.department=k.associatedDeptList[0].value;
k.medium=k.associatedDeptList[0].commonId
}}};
k.setMedium=function(O){k.userList=m(O.value);
k.payload.assignee="";
k.issueReceiveForm.$setPristine();
k.medium=O.commonId;
delete k.selectedUserDropdown;
delete k.displayName
};
function K(R,Q,P,T){if(Q===undefined||Q===null){Q=angular.copy(k.payload.parcel)
}if(P===undefined||P===null){P=angular.copy(k.payload.lot)
}if(T===undefined||T===null){T=angular.copy(k.payload.packet)
}if((k.medium==="Rough"&&Q!==null&Q!==undefined&&Q!=="")||(k.medium==="Lot"&&P!==null&&P!==undefined&&P!=="")||(k.medium==="Packet"&&T!==null&&T!==undefined&&T!=="")){if((k.medium==="Rough"&&e.indexOf(Q)===-1)||(k.medium==="Lot"&&e.indexOf(P)===-1)||(k.medium==="Packet"&&e.indexOf(T)===-1)){var S={};
var O=true;
if(R==="Rough"){if(Q===null||Q===undefined||Q===""){O=false
}else{S.Parcel=Q
}}else{if(R==="Lot"){if(P===null||P===undefined||P===""){O=false
}else{S.Lot=P
}}else{if(R==="Packet"){if(T===null||T===undefined||T===""){O=false
}else{S.Packet=T
}}}}if(O){S.req_type=k.dataVals.operation;
o.maskLoading();
J.retrieveStockById(S,function(U){o.unMaskLoading();
if(U!==undefined&&U!==null){if(U.Message===null||U.Message===undefined){if(R==="Rough"){if(U.Invoice!==undefined&&U.Invoice!==null){k.payload.invoice=U.Invoice.invoiceNumber;
k.payload.invoiceId=U.Invoice.invoiceId
}if(U.Parcel!==undefined&&U.Parcel!==null){k.payload.parcel=U.Parcel.parcelNumber;
k.payload.parcelId=U.Parcel.parcelId;
e.push(angular.copy(U.Parcel.parcelNumber))
}k.payload.issueCarat=U.Parcel.issueCarat;
k.payload.issuePcs=U.Parcel.issuePcs
}else{if(R==="Lot"){if(U.Invoice!==undefined&&U.Invoice!==null){k.payload.invoice=U.Invoice.invoiceNumber;
k.payload.invoiceId=U.Invoice.invoiceId
}if(U.Parcel!==undefined&&U.Parcel!==null){k.payload.parcel=U.Parcel.parcelNumber;
k.payload.parcelId=U.Parcel.parcelId
}if(U.Lot!==undefined&&U.Lot!==null){k.payload.lot=U.Lot.lotNumber;
k.payload.lotId=U.Lot.lotId;
e.push(angular.copy(U.Lot.lotNumber))
}k.payload.issueCarat=U.Lot.issueCarat;
k.payload.issuePcs=U.Lot.issuePcs
}else{if(R==="Packet"){if(U.Invoice!==undefined&&U.Invoice!==null){k.payload.invoice=U.Invoice.invoiceNumber;
k.payload.invoiceId=U.Invoice.invoiceId
}if(U.Parcel!==undefined&&U.Parcel!==null){k.payload.parcel=U.Parcel.parcelNumber;
k.payload.parcelId=U.Parcel.parcelId
}if(U.Lot!==undefined&&U.Lot!==null){k.payload.lot=U.Lot.lotNumber;
k.payload.lotId=U.Lot.lotId
}if(U.Packet!==undefined&&U.Packet!==null){k.payload.packet=U.Packet.packetNumber;
k.payload.packetId=U.Packet.packetId;
e.push(angular.copy(U.Packet.packetNumber))
}k.payload.issueCarat=U.Packet.issueCarat;
k.payload.issuePcs=U.Packet.issuePcs
}}}y()
}else{o.addMessage(U.Message["Message"],1);
k.payload.parcel="";
k.payload.lot="";
k.payload.packet=""
}}},function(){o.unMaskLoading()
})
}}else{o.addMessage("Stock is already added",1);
l()
}}}function l(){k.payload={slipnoDate:new Date(),selectedFranchise:o.session.companyId};
k.submitted=false
}k.saveAll=function(){k.submitted=true;
k.stockDtls=[];
if(k.selectorIdsForRequest!==null&&k.selectorIdsForRequest!==undefined&&k.selectorIdsForRequest.length>0){angular.forEach(k.availableRequestStock,function(O){if(k.selectorIdsForRequest.indexOf(O.selectorId)!==-1){O.srcDeptId=o.session.department;
delete O.toRequest;
delete O.selectorId;
k.stockDtls.push(O);
O.type="Request";
O.requestType=k.dataVals.operation;
if(k.dataVals.type!==undefined&&k.dataVals.type!==null){if(k.dataVals.type==="OW"){O.destDeptId=k.dataVals.department;
O.stockDeptId=parseInt(k.associatedDeptList[0].label)
}else{O.destDeptId=O.srcDeptId;
O.destinationDesignationId=k.dataVals.designation
}}O.isActive=true;
O.modifier=k.dataVals.type;
if(k.dataVals.operation==="RQ"){var P=[];
P.push({categoryCustom:angular.copy(k.generalCustom)});
var Q=function(R){O.fieldValue=angular.copy(k.generalCustom);
O.fieldValueForUi=angular.copy(R[0].categoryCustom);
O.fieldDbType=angular.copy(k.fieldDbType);
k.stockDtls.push(angular.copy(O));
G()
};
t.convertorForCustomField(P,Q)
}}})
}if(k.stockDtls.length>0){angular.forEach(k.stockDtls,function(O){delete O.fieldValueForUi
});
J.saveAll(k.stockDtls,function(O){l();
H=[];
k.selectorIdsForRequest=[];
L={};
v();
e=[];
k.stockDtls=[];
k.retrieveAvailableRequestStock();
var Q="Stock requested successfully";
var P=o.success;
o.addMessage(Q,P);
x()
})
}};
k.tabChanged=function(O){x();
k.payload.parcel=[];
k.dataVals.roughFlag=false;
z(function(){k.dataVals.roughFlag=true
});
k.payload.lot=[];
k.dataVals.lotFlag=false;
z(function(){k.dataVals.lotFlag=true
});
k.payload.packet=[];
k.dataVals.packetFlag=false;
z(function(){k.dataVals.packetFlag=true
});
l();
k.submitted=false;
k.dataVals.operation=O;
e=[];
d=[];
k.receiveArray=[];
k.IIStockDtls=[];
k.stockDtls=[];
k.directReceiveArray=[];
delete k.destinationDeptIdForIssue;
h=[];
if(k.dataVals.operation==="CL"){k.retrieveAvailableCollectStock();
k.usersForCompany=[];
delete k.selectedUserDropdown;
k.isUserInvalid=true;
delete k.displayName;
k.treeForCollect=[];
F={};
k.selectorIds=[]
}else{if(k.dataVals.operation==="IS"){k.retrieveAvailableIssueStock();
k.retrievePendingIssuedStock();
k.usersForCompany=[];
delete k.selectedUserDropdown;
k.isUserInvalid=true;
delete k.displayName;
k.treeForIssue=[];
A={};
k.selectorIdsForIssue=[]
}else{if(k.dataVals.operation==="RT"){k.retrieveAvailableReturnStock();
k.treeForReturn=[];
q={};
s=[]
}else{if(k.dataVals.operation==="RC"){k.retrievePendingReceivedStock()
}else{if(k.dataVals.operation==="RQ"){k.retrieveAvailableRequestStock()
}}}}}c(k.dataVals.operation)
};
k.retrieveAvailableCollectStock=function(){h=[];
k.selectorIds=[];
F={};
w();
k.availableCollectStock=[];
var O={requestType:"CL"};
J.retrieveStockBySlip(O,function(P){if(P!==undefined&&P!==null&&P.length>0){angular.forEach(P,function(Q){if(Q.packetNumber!==undefined&&Q.packetNumber!==null){Q.selectorId="PAC"+Q.packetNumber
}else{if(Q.lotNumber!==undefined&&Q.lotNumber!==null){Q.selectorId="LOT"+Q.lotNumber
}else{if(Q.parcelNumber!==undefined&&Q.parcelNumber!==null){Q.selectorId="PAR"+Q.parcelNumber
}}}});
angular.forEach(P,function(Q){var R=[];
R.push({categoryCustom:angular.copy(Q.fieldValue)});
var S=function(T){Q.fieldValueForUi=angular.copy(T[0].categoryCustom);
k.availableCollectStock.push(angular.copy(Q))
};
t.convertorForCustomField(R,S)
})
}})
};
k.retrieveAvailableIssueStock=function(){d=[];
k.selectorIdsForIssue=[];
A={};
r();
delete k.destinationDeptIdForIssue;
k.availableIssueStock=[];
var O={requestType:"IS"};
J.retrieveStockBySlip(O,function(P){if(P!==undefined&&P!==null&&P.length>0){angular.forEach(P,function(Q){if(Q.packetNumber!==undefined&&Q.packetNumber!==null){Q.selectorId="PAC"+Q.packetNumber
}else{if(Q.lotNumber!==undefined&&Q.lotNumber!==null){Q.selectorId="LOT"+Q.lotNumber
}else{if(Q.parcelNumber!==undefined&&Q.parcelNumber!==null){Q.selectorId="PAR"+Q.parcelNumber
}}}});
angular.forEach(P,function(Q){var R=[];
R.push({categoryCustom:angular.copy(Q.fieldValue)});
var S=function(T){Q.fieldValueForUi=angular.copy(T[0].categoryCustom);
k.availableIssueStock.push(angular.copy(Q))
};
t.convertorForCustomField(R,S)
})
}})
};
k.retrieveAvailableReturnStock=function(){k.returnArray=[];
s=[];
q={};
b();
k.availableReturnStock=[];
var O={requestType:"RT"};
J.retrieveStockBySlip(O,function(P){if(P!==undefined&&P!==null&&P.length>0){angular.forEach(P,function(Q){if(Q.packetNumber!==undefined&&Q.packetNumber!==null){Q.selectorId="PAC"+Q.packetNumber
}else{if(Q.lotNumber!==undefined&&Q.lotNumber!==null){Q.selectorId="LOT"+Q.lotNumber
}else{if(Q.parcelNumber!==undefined&&Q.parcelNumber!==null){Q.selectorId="PAR"+Q.parcelNumber
}}}});
k.availableReturnStock=angular.copy(P)
}})
};
k.retreiveStockBySlip=function(){if(k.payload.slipnoDate!==null&&k.payload.slipnoNumber!==undefined&&k.payload.slipnoNumber!==null&&k.payload.slipnoNumber!==""){o.validations=[];
if(k.dataVals.operation==="RC"){k.receiveArray=[]
}else{if(k.dataVals.operation==="RI"){k.directReceiveArray=[]
}}var O={slipDate:k.payload.slipnoDate,slipNo:k.payload.slipnoNumber,requestType:k.dataVals.operation};
o.maskLoading();
J.retrieveStockBySlip(O,function(P){o.unMaskLoading();
if(P!==null&&P.length>0){if(k.dataVals.operation==="CL"){h=angular.copy(P)
}else{if(k.dataVals.operation==="RC"){angular.forEach(P,function(T){var U=[];
U.push({categoryCustom:angular.copy(T.fieldValue)});
var V=function(W){T.fieldValueForUi=angular.copy(W[0].categoryCustom);
k.receiveArray.push(angular.copy(T))
};
t.convertorForCustomField(U,V)
})
}else{if(k.dataVals.operation==="RI"){angular.forEach(P,function(T){T.maxCarat=angular.copy(T.receiveCarat);
T.maxPcs=angular.copy(T.receivePcs)
});
for(var Q=P.length-1;
Q>=0;
Q--){if(P[Q].maxCarat===0||P[Q].maxPcs===0){P.splice(Q,1)
}}if(P.length>0){angular.forEach(P,function(T){var U=[];
U.push({categoryCustom:angular.copy(T.fieldValue)});
var V=function(W){T.fieldValueForUi=angular.copy(W[0].categoryCustom);
k.directReceiveArray.push(angular.copy(T))
};
t.convertorForCustomField(U,V)
})
}else{var S="All stock is recieved for this slip number";
var R=o.failure;
o.addMessage(S,R);
l()
}}}}}else{var S="No record for specified slip number";
var R=o.failure;
o.addMessage(S,R);
l()
}},function(){o.unMaskLoading()
})
}};
function m(O){var P={destDeptId:O};
k.usersForCompany=[];
J.retrieveUsersByDepartment(P,function(Q){k.usersForCompany=angular.copy(Q);
k.usersForCompanyTemp=angular.copy(Q)
})
}k.retrieveUsersByDesignation=function(P){k.payload.assignee="";
k.issueReceiveForm.$setPristine();
var O=new Object();
O.search="";
O.desgIds=P;
n.retrieveusersByDesg(O,function(Q){k.userList=angular.copy(Q)
})
};
function y(){var Q={};
Q.invoiceId=k.payload.invoiceId;
if(k.payload.invoice!==null&&k.payload.invoice!==undefined&&k.payload.invoice!==""){Q.invoiceNumber=k.payload.invoice
}Q.parcelId=k.payload.parcelId;
if(k.payload.parcel!==null&&k.payload.parcel!==undefined&&k.payload.parcel!==""){Q.parcelNumber=k.payload.parcel
}Q.lotId=k.payload.lotId;
if(k.payload.lot!==null&&k.payload.lot!==undefined&&k.payload.lot!==""){Q.lotNumber=k.payload.lot
}Q.packetId=k.payload.packetId;
if(k.payload.packet!==null&&k.payload.packet!==undefined&&k.payload.packet!==""){Q.packetNumber=k.payload.packet
}Q.srcDeptId=o.session.department;
if(k.dataVals.operation!==undefined&&k.dataVals.operation!==null){if(k.dataVals.operation==="II"){Q.type="Issue Inward"
}else{if(k.dataVals.operation==="RI"){Q.type="Receive Inward"
}else{Q.type="Request"
}}Q.requestType=k.dataVals.operation
}if(k.dataVals.type!==undefined&&k.dataVals.type!==null){if(k.dataVals.type==="OW"){Q.destDeptId=k.dataVals.department;
Q.stockDeptId=parseInt(k.associatedDeptList[0].label)
}else{Q.destDeptId=Q.srcDeptId;
Q.destinationDesignationId=k.dataVals.designation
}}Q.issueCarat=k.payload.issueCarat;
Q.issuePcs=k.payload.issuePcs;
Q.isActive=true;
Q.modifier=k.dataVals.type;
if(k.dataVals.operation==="RQ"){var P=[];
P.push({categoryCustom:angular.copy(k.generalCustom)});
var R=function(S){Q.fieldValue=angular.copy(k.generalCustom);
Q.fieldValueForUi=angular.copy(S[0].categoryCustom);
Q.fieldDbType=angular.copy(k.fieldDbType);
k.stockDtls.push(angular.copy(Q));
G()
};
t.convertorForCustomField(P,R)
}else{if(k.dataVals.operation==="II"){var P=[];
P.push({categoryCustom:angular.copy(k.generalCustom)});
var R=function(S){Q.fieldValue=angular.copy(k.generalCustom);
Q.fieldValueForUi=angular.copy(S[0].categoryCustom);
Q.fieldDbType=angular.copy(k.fieldDbType);
k.IIStockDtls.push(angular.copy(Q));
G()
};
t.convertorForCustomField(P,R)
}}var O=angular.copy(k.payload.assignee);
l();
k.payload.assignee=O;
k.submitted=false
}k.collect=function(O){k.submitted=true;
if(O.$valid){h=[];
if(k.selectorIds!==null&&k.selectorIds!==undefined&&k.selectorIds.length>0){angular.forEach(k.availableCollectStock,function(P){if(k.selectorIds.indexOf(P.selectorId)!==-1){P.fieldValue=angular.copy(k.generalCustom);
P.fieldDbType=angular.copy(k.fieldDbType);
delete P.toCollect;
delete P.selectorId;
h.push(P)
}})
}if(h.length>0){o.maskLoading();
J.collect(h,function(P){o.unMaskLoading();
k.retrieveAvailableCollectStock();
var R="Collected successfully";
var Q=o.success;
o.addMessage(R,Q);
l();
h=[];
k.selectorIds=[];
F={};
e=[];
w()
},function(){o.unMaskLoading()
})
}}};
k.issue=function(O){k.submitted=true;
if(O.$valid){d=[];
if(k.selectorIdsForIssue!==null&&k.selectorIdsForIssue!==undefined&&k.selectorIdsForIssue.length>0){angular.forEach(k.availableIssueStock,function(P){if(k.selectorIdsForIssue.indexOf(P.selectorId)!==-1){delete P.toCollect;
delete P.selectorId;
d.push(P)
}})
}if(d!==undefined&&d!==null&&d.length>0){angular.forEach(d,function(P){P.destFranchiseId=k.payload.selectedFranchise;
P.issueTo=k.dataVals.userId;
P.fieldValue=angular.copy(k.generalCustom);
P.fieldDbType=angular.copy(k.fieldDbType)
});
o.maskLoading();
J.issue(d,function(P){o.unMaskLoading();
l();
d=[];
k.selectorIdsForIssue=[];
A={};
e=[];
r();
k.submitted=false;
k.retrieveAvailableIssueStock()
},function(){o.unMaskLoading()
})
}}};
k.receive=function(O){k.submitted=true;
if(O.$valid){if(k.receiveArray!==undefined&&k.receiveArray!==null&&k.receiveArray.length>0){o.maskLoading();
angular.forEach(k.receiveArray,function(P){P.fieldValue=angular.copy(k.generalCustom);
P.fieldDbType=angular.copy(k.fieldDbType)
});
J.receive(k.receiveArray,function(P){o.unMaskLoading();
var R="Received successfully";
var Q=o.success;
o.addMessage(R,Q);
l();
k.receiveArray=[];
k.retrievePendingReceivedStock()
},function(){o.unMaskLoading()
})
}}};
k.cancel=function(){e=[];
l();
if(k.dataVals.operation==="RQ"){k.stockDtls=[]
}else{if(k.dataVals.operation==="CL"){h=[];
k.selectorIds=[];
F={};
w();
k.retrieveAvailableCollectStock()
}else{if(k.dataVals.operation==="IS"){d=[];
k.selectorIdsForIssue=[];
A={};
r();
k.retrieveAvailableIssueStock();
delete k.destinationDeptIdForIssue
}else{if(k.dataVals.operation==="II"){k.IIStockDtls=[]
}else{if(k.dataVals.operation==="RI"){k.directReceiveArray=[]
}else{if(k.dataVals.operation==="RC"){k.receiveArray=[]
}else{if(k.dataVals.operation==="RT"){k.returnArray=[];
s=[];
q={};
b();
k.retrieveAvailableReturnStock()
}}}}}}}};
k.issueInward=function(O){k.submitted=true;
if(O.$valid&&k.dataVals.userId!==null&&k.dataVals.userId!==undefined){angular.forEach(k.IIStockDtls,function(P){delete P.fieldValueForUi;
P.issueTo=k.dataVals.userId;
if(k.dataVals.type!==undefined&&k.dataVals.type!==null){if(k.dataVals.type==="OW"){P.destDeptId=k.dataVals.department
}else{P.destDeptId=P.srcDeptId;
P.destinationDesignationId=k.dataVals.designation
}}});
o.maskLoading();
J.issueInward(k.IIStockDtls,function(){o.unMaskLoading();
l();
k.IIStockDtls=[];
e=[];
delete k.dataVals.userId;
delete k.displayName;
O.$setPristine();
x()
},function(){o.unMaskLoading()
})
}};
k.receiveInward=function(O){k.submitted=true;
if(O.$valid){if(k.directReceiveArray!==undefined&&k.directReceiveArray!==null&&k.directReceiveArray.length>0){angular.forEach(k.directReceiveArray,function(P){P.fieldValue=k.generalCustom;
P.fieldDbType=k.fieldDbType;
delete P.maxCarat;
delete P.maxPcs
});
o.maskLoading();
J.receiveInward(k.directReceiveArray,function(){o.unMaskLoading();
var Q="Received successfully";
var P=o.success;
o.addMessage(Q,P);
l();
k.directReceiveArray=[];
O.$setPristine()
},function(){o.unMaskLoading()
})
}}};
k.onSubmitFunc=function(Q,O,P){k.submitted=true;
if(P.$valid){if(Q.keyCode===13||Q.keyCode===9){o.validations=[];
if(k.dataVals.type==="IW"||(k.dataVals.type==="OW"&&k.medium===O)){if(O==="Parcel"){angular.element($("#parcel")).blur()
}else{if(O==="Lot"){angular.element($("#lot")).blur()
}else{if(O==="Packet"){angular.element($("#packet")).blur()
}}}if(k.dataVals.type==="IW"){if(k.payload.parcel!==undefined&&k.payload.parcel!==null&&k.payload.parcel!==""){k.medium="Rough";
K("Rough")
}else{if(k.payload.lot!==undefined&&k.payload.lot!==null&&k.payload.lot!==""){k.medium="Lot";
K("Lot")
}else{if(k.payload.packet!==undefined&&k.payload.packet!==null&&k.payload.packet!==""){k.medium="Packet";
K("Packet")
}}}}else{K(O)
}z(function(){if(O==="Parcel"){angular.element($("#parcel")).focus()
}else{if(O==="Lot"||O==="Packet"){angular.element($("#lot")).focus()
}}})
}}}};
k.onSubmitFuncSlipNo=function(O){k.submitted=true;
if(O.keyCode===13||O.keyCode===9){o.validations=[];
angular.element($("#slipnoNumber")).blur();
k.retreiveStockBySlip();
z(function(){angular.element($("#slipnoNumber")).focus()
})
}};
k.onSubmitFuncCollect=function(Q,P){var O="";
if(k.configuredMediums.indexOf("Packet")!==-1){O="Packet"
}else{if(k.configuredMediums.indexOf("Lot")!==-1){O="Lot"
}else{if(k.configuredMediums.indexOf("Rough")!==-1){O="Rough"
}}}if(Q.keyCode===13||Q.keyCode===9){o.validations=[];
if(O===P){u()
}}};
function u(){var T={};
var Q=true;
var S="";
if(k.payload.parcel!==null&&k.payload.parcel!==undefined&&k.payload.parcel!==""){Q=false;
T.Parcel=k.payload.parcel;
S="PAR"+k.payload.parcel
}else{if(k.payload.lot!==null&&k.payload.lot!==undefined&&k.payload.lot!==""){if(k.payload.packet!==null&&k.payload.packet!==undefined&&k.payload.packet!==""){Q=false;
T.Packet=k.payload.packet;
S="PAC"+k.payload.packet
}else{Q=false;
T.Lot=k.payload.lot;
S="LOT"+k.payload.lot
}}}if(!Q){if(k.dataVals.operation==="CL"){if(k.selectorIds.indexOf(S)===-1){T.req_type=k.dataVals.operation;
var P=false;
angular.forEach(k.availableCollectStock,function(V){if(P===false&&S===V.selectorId){V.toCollect=true;
P=true;
var W=[];
W.push({categoryCustom:angular.copy(k.generalCustom)});
var X=function(Y){V.fieldValue=angular.copy(k.generalCustom);
V.fieldDbType=angular.copy(k.fieldDbType);
G()
};
t.convertorForCustomField(W,X)
}});
if(P===true){if(T.Parcel!==undefined&&T.Parcel!==undefined){if(F.Parcels===undefined||F.Parcels===null){F.Parcels=[]
}F.Parcels.push(T.Parcel);
k.selectorIds.push("PAR"+T.Parcel)
}else{if(T.Lot!==undefined&&T.Lot!==undefined){if(F.Lots===undefined||F.Lots===null){F.Lots=[]
}F.Lots.push(T.Lot);
k.selectorIds.push("LOT"+T.Lot)
}else{if(T.Packet!==undefined&&T.Packet!==undefined){if(F.Packets===undefined||F.Packets===null){F.Packets=[]
}F.Packets.push(T.Packet);
k.selectorIds.push("PAC"+T.Packet)
}}}w()
}else{var R=o.failure;
o.addMessage("No stock available",R)
}}else{var U="Stock already added";
var R=o.failure;
o.addMessage(U,R)
}l()
}else{if(k.dataVals.operation==="IS"){if(k.selectorIdsForIssue.indexOf(S)===-1){T.req_type=k.dataVals.operation;
var P=false;
var O={};
angular.forEach(k.availableIssueStock,function(V){if(P===false&&S===V.selectorId){V.toCollect=true;
O=angular.copy(V);
var W=[];
W.push({categoryCustom:angular.copy(k.generalCustom)});
var X=function(Y){V.fieldValue=angular.copy(k.generalCustom);
V.fieldDbType=angular.copy(k.fieldDbType);
G()
};
t.convertorForCustomField(W,X);
P=true
}});
if(P===true){if(k.selectorIdsForIssue===undefined||k.selectorIdsForIssue===null||k.selectorIdsForIssue.length===0){k.destinationDeptIdForIssue=O.destDeptId;
m(k.destinationDeptIdForIssue)
}if(k.selectorIdsForIssue.length>0&&k.destinationDeptIdForIssue!==O.destDeptId){P=false
}if(P){if(T.Parcel!==undefined&&T.Parcel!==undefined){if(A.Parcels===undefined||A.Parcels===null){A.Parcels=[]
}A.Parcels.push(T.Parcel);
k.selectorIdsForIssue.push("PAR"+T.Parcel)
}else{if(T.Lot!==undefined&&T.Lot!==undefined){if(A.Lots===undefined||A.Lots===null){A.Lots=[]
}A.Lots.push(T.Lot);
k.selectorIdsForIssue.push("LOT"+T.Lot)
}else{if(T.Packet!==undefined&&T.Packet!==undefined){if(A.Packets===undefined||A.Packets===null){A.Packets=[]
}A.Packets.push(T.Packet);
k.selectorIdsForIssue.push("PAC"+T.Packet)
}}}r()
}else{var U="Destination department should be equal of all stock";
var R=o.failure;
o.addMessage(U,R)
}}else{var R=o.failure;
o.addMessage("No stock available",R)
}}else{var U="Stock already added";
var R=o.failure;
o.addMessage(U,R)
}l()
}else{if(k.dataVals.operation==="RT"){if(s.indexOf(S)===-1){T.req_type=k.dataVals.operation;
var P=false;
var O={};
angular.forEach(k.availableReturnStock,function(V){if(S===V.selectorId){V.toCollect=true;
O=angular.copy(V);
P=true
}});
if(P===true){if(P){if(T.Parcel!==undefined&&T.Parcel!==undefined){if(q.Parcels===undefined||q.Parcels===null){q.Parcels=[]
}q.Parcels.push(T.Parcel);
s.push("PAR"+T.Parcel)
}else{if(T.Lot!==undefined&&T.Lot!==undefined){if(q.Lots===undefined||q.Lots===null){q.Lots=[]
}q.Lots.push(T.Lot);
s.push("LOT"+T.Lot)
}else{if(T.Packet!==undefined&&T.Packet!==undefined){if(q.Packets===undefined||q.Packets===null){q.Packets=[]
}q.Packets.push(T.Packet);
s.push("PAC"+T.Packet)
}}}b()
}}else{var R=o.failure;
o.addMessage("No stock available",R)
}}else{var U="Stock already added";
var R=o.failure;
o.addMessage(U,R)
}l()
}}}}}k.addOrRemoveForCollect=function(Q){if(Q.toCollect===true){k.selectorIds.splice(k.selectorIds.indexOf(Q.selectorId),1);
if(Q.selectorId.substring(0,3)==="PAR"){F.Parcels.splice(F.Parcels.indexOf(Q.selectorId.substring(3)),1)
}else{if(Q.selectorId.substring(0,3)==="LOT"){F.Lots.splice(F.Lots.indexOf(Q.selectorId.substring(3)),1)
}else{if(Q.selectorId.substring(0,3)==="PAC"){F.Packets.splice(F.Packets.indexOf(Q.selectorId.substring(3)),1)
}}}w()
}else{var O=false;
angular.forEach(k.availableCollectStock,function(R){if(O===false&&Q.selectorId===R.selectorId){R.toCollect=true;
var S=[];
S.push({categoryCustom:angular.copy(k.generalCustom)});
var T=function(U){R.fieldValue=angular.copy(k.generalCustom);
R.fieldDbType=angular.copy(k.fieldDbType)
};
t.convertorForCustomField(S,T);
O=true
}});
if(O===true){if(Q.packetNumber!==undefined&&Q.packetNumber!==null){if(F.Packets===undefined||F.Packets===null){F.Packets=[]
}F.Packets.push(Q.packetNumber);
k.selectorIds.push("PAC"+Q.packetNumber)
}else{if(Q.lotNumber!==undefined&&Q.lotNumber!==null){if(F.Lots===undefined||F.Lots===null){F.Lots=[]
}F.Lots.push(Q.lotNumber);
k.selectorIds.push("LOT"+Q.lotNumber)
}else{if(Q.parcelNumber!==undefined&&Q.parcelNumber!==null){if(F.Parcels===undefined||F.Parcels===null){F.Parcels=[]
}F.Parcels.push(Q.parcelNumber);
k.selectorIds.push("PAR"+Q.parcelNumber)
}}}w()
}else{var P=o.failure;
o.addMessage("No stock available",P)
}}};
k.addOrRemoveForIssue=function(R){if(R.toCollect===true){k.selectorIdsForIssue.splice(k.selectorIdsForIssue.indexOf(R.selectorId),1);
if(R.selectorId.substring(0,3)==="PAR"){A.Parcels.splice(A.Parcels.indexOf(R.selectorId.substring(3)),1)
}else{if(R.selectorId.substring(0,3)==="LOT"){A.Lots.splice(A.Lots.indexOf(R.selectorId.substring(3)),1)
}else{if(R.selectorId.substring(0,3)==="PAC"){A.Packets.splice(A.Packets.indexOf(R.selectorId.substring(3)),1)
}}}r()
}else{var P=false;
var O={};
angular.forEach(k.availableIssueStock,function(S){if(P===false&&R.selectorId===S.selectorId){S.toCollect=true;
P=true;
O=angular.copy(S);
var T=[];
T.push({categoryCustom:angular.copy(k.generalCustom)});
var U=function(V){S.fieldValue=angular.copy(k.generalCustom);
S.fieldDbType=angular.copy(k.fieldDbType)
};
t.convertorForCustomField(T,U)
}});
if(P===true){if(k.selectorIdsForIssue===undefined||k.selectorIdsForIssue===null||k.selectorIdsForIssue.length===0){k.destinationDeptIdForIssue=O.destDeptId;
m(k.destinationDeptIdForIssue)
}if(k.selectorIdsForIssue.length>0&&k.destinationDeptIdForIssue!==O.destDeptId){P=false
}if(P){if(R.packetNumber!==undefined&&R.packetNumber!==null){if(A.Packets===undefined||A.Packets===null){A.Packets=[]
}A.Packets.push(R.packetNumber);
k.selectorIdsForIssue.push("PAC"+R.packetNumber)
}else{if(R.lotNumber!==undefined&&R.lotNumber!==null){if(A.Lots===undefined||A.Lots===null){A.Lots=[]
}A.Lots.push(R.lotNumber);
k.selectorIdsForIssue.push("LOT"+R.lotNumber)
}else{if(R.parcelNumber!==undefined&&R.parcelNumber!==null){if(A.Parcels===undefined||A.Parcels===null){A.Parcels=[]
}A.Parcels.push(R.parcelNumber);
k.selectorIdsForIssue.push("PAR"+R.parcelNumber)
}}}r()
}}else{var Q=o.failure;
o.addMessage("No stock available",Q)
}}};
k.addOrRemoveForRequest=function(R){if(R.toRequest===true){k.selectorIdsForRequest.splice(k.selectorIdsForRequest.indexOf(R.selectorId),1);
if(R.selectorId.substring(0,3)==="PAR"){L.Parcels.splice(L.Parcels.indexOf(R.selectorId.substring(3)),1)
}else{if(R.selectorId.substring(0,3)==="LOT"){L.Lots.splice(L.Lots.indexOf(R.selectorId.substring(3)),1)
}else{if(R.selectorId.substring(0,3)==="PAC"){L.Packets.splice(L.Packets.indexOf(R.selectorId.substring(3)),1)
}}}v()
}else{var P=false;
var O={};
angular.forEach(k.availableRequestStock,function(S){if(P===false&&R.selectorId===S.selectorId){S.toRequest=true;
P=true;
O=angular.copy(S)
}});
if(P===true){if(R.packetNumber!==undefined&&R.packetNumber!==null){if(L.Packets===undefined||L.Packets===null){L.Packets=[]
}L.Packets.push(R.packetNumber);
k.selectorIdsForRequest.push("PAC"+R.packetNumber)
}else{if(R.lotNumber!==undefined&&R.lotNumber!==null){if(L.Lots===undefined||L.Lots===null){L.Lots=[]
}L.Lots.push(R.lotNumber);
k.selectorIdsForRequest.push("LOT"+R.lotNumber)
}else{if(R.parcelNumber!==undefined&&R.parcelNumber!==null){if(L.Parcels===undefined||L.Parcels===null){L.Parcels=[]
}L.Parcels.push(R.parcelNumber);
k.selectorIdsForRequest.push("PAR"+R.parcelNumber)
}}}v()
}else{var Q=o.failure;
o.addMessage("No stock available",Q)
}}};
k.addOrRemoveForReturn=function(R){if(R.toCollect===true){s.splice(s.indexOf(R.selectorId),1);
if(R.selectorId.substring(0,3)==="PAR"){q.Parcels.splice(q.Parcels.indexOf(R.selectorId.substring(3)),1)
}else{if(R.selectorId.substring(0,3)==="LOT"){q.Lots.splice(q.Lots.indexOf(R.selectorId.substring(3)),1)
}else{if(R.selectorId.substring(0,3)==="PAC"){q.Packets.splice(q.Packets.indexOf(R.selectorId.substring(3)),1)
}}}b()
}else{var P=false;
var O={};
angular.forEach(k.availableReturnStock,function(S){if(R.selectorId===S.selectorId){S.toCollect=true;
P=true;
O=angular.copy(S)
}});
if(P===true){if(P){if(R.packetNumber!==undefined&&R.packetNumber!==null){if(q.Packets===undefined||q.Packets===null){q.Packets=[]
}q.Packets.push(R.packetNumber);
s.push("PAC"+R.packetNumber)
}else{if(R.lotNumber!==undefined&&R.lotNumber!==null){if(q.Lots===undefined||q.Lots===null){q.Lots=[]
}q.Lots.push(R.lotNumber);
s.push("LOT"+R.lotNumber)
}else{if(R.parcelNumber!==undefined&&R.parcelNumber!==null){if(q.Parcels===undefined||q.Parcels===null){q.Parcels=[]
}q.Parcels.push(R.parcelNumber);
s.push("PAR"+R.parcelNumber)
}}}b()
}}else{var Q=o.failure;
o.addMessage("No stock available",Q)
}}};
k.setSelectedParent=function(O){k.isUserInvalid=true;
if(!angular.equals(O,{})){if(O.currentNode!==null&&O.currentNode!==undefined&&O.currentNode.children!==null&&O.currentNode.children!==undefined&&O.currentNode.children.length>0){delete k.selectedUserDropdown;
delete k.displayName;
delete k.dataVals.userId;
k.usersForCompany=angular.copy(k.usersForCompanyTemp)
}else{k.isUserInvalid=false;
k.dataVals.userId=O.currentNode.id;
k.displayName=O.currentNode.displayName
}}};
k.reject=function(){k.submitted=true;
h=[];
if(k.selectorIds!==null&&k.selectorIds!==undefined&&k.selectorIds.length>0){angular.forEach(k.availableCollectStock,function(O){if(k.selectorIds.indexOf(O.selectorId)!==-1){delete O.toCollect;
delete O.selectorId;
h.push(O)
}})
}if(h.length>0){o.maskLoading();
J.reject(h,function(O){o.unMaskLoading();
k.retrieveAvailableCollectStock();
var Q="Rejected successfully";
var P=o.success;
o.addMessage(Q,P);
l();
h=[];
k.selectorIds=[];
F={};
e=[];
w()
},function(){o.unMaskLoading()
})
}};
k.returnStock=function(){k.returnArray=[];
if(s!==null&&s!==undefined&&s.length>0){angular.forEach(k.availableReturnStock,function(O){if(s.indexOf(O.selectorId)!==-1){delete O.toCollect;
delete O.selectorId;
k.returnArray.push(O)
}})
}if(k.returnArray!==undefined&&k.returnArray!==null&&k.returnArray.length>0){angular.forEach(k.returnArray,function(O){O.destFranchiseId=k.payload.selectedFranchise;
O.issueTo=k.dataVals.userId
});
o.maskLoading();
J.returnStock(k.returnArray,function(O){o.unMaskLoading();
var Q="Returned successfully";
var P=o.success;
o.addMessage(Q,P);
l();
k.returnArray=[];
s=[];
q={};
e=[];
b();
k.submitted=false;
k.retrieveAvailableReturnStock()
},function(){var P="Error while returning stock";
var O=o.failure;
o.addMessage(P,O);
o.unMaskLoading()
})
}};
k.retrievePendingIssuedStock=function(){k.pendingIssuedStock=[];
J.retrievePendingIssued({requestType:"IS"},function(O){k.pendingIssuedStock=angular.copy(O)
})
};
k.retrievePendingReceivedStock=function(){k.pendingReceiveStock=[];
J.retrievePendingIssued({requestType:"RC"},function(O){k.pendingReceiveStock=angular.copy(O)
})
};
function c(O){if(O!==undefined&&O!==null){k.templateFlag=false;
k.generalCustom={};
if(B[O]===undefined||B[O]===null){I.retrieveDesignationBasedFieldsBySection(["issueReceive",O],function(P){B[O]=angular.copy(P);
k.response=angular.copy(P);
G()
})
}else{k.response=angular.copy(B[O]);
G()
}}}function G(){k.templateFlag=false;
k.generalCustom={};
o.maskLoading();
var O=t.retrieveSectionWiseCustomFieldInfo("issue");
O.then(function(Q){var P=[];
Object.keys(k.response).map(function(R,S){angular.forEach(this[R],function(T){if(R==="Issue"){P.push({Issue:T})
}})
},k.response);
k.generalTemplate=Q.genralSection;
k.generalTemplate=t.retrieveCustomData(k.generalTemplate,P);
z(function(){k.generalTemplateForView=[];
if(k.generalTemplate!==null&&k.generalTemplate!==undefined&&k.generalTemplate.length>0){for(var R=k.generalTemplate.length-1;
R>=0;
R--){var S=angular.copy(k.generalTemplate[R]);
if(S.isViewFromDesignation===true){k.generalTemplateForView.push(S);
k.generalTemplate.splice(R,1)
}}}k.templateFlag=true;
o.unMaskLoading()
})
},function(P){},function(P){})
}k.franchiseChanged=function(){delete k.dataVals.userId
};
k.autoCompleteRough={multiple:true,closeOnSelect:true,placeholder:"Select Parcel",allowClear:true,initSelection:function(O,Q){var P=[];
O.on("change",function(X){var S=X.added;
var W=X.removed;
if(S!==undefined&&S!==null){k.payload.parcel=X.added.id;
if(k.dataVals.operation==="RQ"){var V={};
V.Parcel=k.payload.parcel;
var U="PAR"+k.payload.parcel;
f(V,U)
}else{k.medium="Rough";
K("Rough",X.added.id)
}}else{if(W!==undefined&&W!==null){e.splice(e.indexOf(W.id),1);
if(k.dataVals.operation==="RQ"){k.selectorIdsForRequest.splice(k.selectorIdsForRequest.indexOf("PAR"+W.id),1);
L.Parcels.splice(L.Parcels.indexOf(W.id),1);
v()
}else{if(k.dataVals.operation==="II"){for(var R=0;
R<k.IIStockDtls.length;
R++){var T=k.IIStockDtls[R];
if(T.parcelNumber===W.id){k.IIStockDtls.splice(R,1);
break
}}}}}}});
Q(P)
},formatResult:function(O){return O.text
},formatSelection:function(O){return O.text
},ajax:{url:o.appendAuthToken(o.centerapipath+"customfield/searchautogenerated"),dataType:"json",data:function(O,P){return{q:O,page_limit:10,page:P-1,field_name:"parcelId$AG$String"}
},results:function(R,Q){R=R.data;
var P=(Q*10)<R.total;
var O=[];
angular.forEach(R.records,function(T,S){O.push({id:T,text:T})
});
O=k.orderByName(O,["text"]);
return{results:O,more:P}
}}};
k.autoCompleteLot={multiple:true,closeOnSelect:true,placeholder:"Select Lot",allowClear:true,initSelection:function(O,Q){var P=[];
O.on("change",function(X){var S=X.added;
var W=X.removed;
if(S!==undefined&&S!==null){k.payload.lot=X.added.id;
if(k.dataVals.operation==="RQ"){var V={};
V.Lot=k.payload.lot;
var U="LOT"+k.payload.lot;
f(V,U)
}else{k.medium="Lot";
K("Lot",null,X.added.id)
}}else{if(W!==undefined&&W!==null){e.splice(e.indexOf(W.id),1);
if(k.dataVals.operation==="RQ"){k.selectorIdsForRequest.splice(k.selectorIdsForRequest.indexOf("LOT"+W.id),1);
L.Lots.splice(L.Lots.indexOf(W.id),1);
v()
}else{if(k.dataVals.operation==="II"){for(var R=0;
R<k.IIStockDtls.length;
R++){var T=k.IIStockDtls[R];
if(T.lotNumber===W.id){k.IIStockDtls.splice(R,1);
break
}}}}}}});
Q(P)
},formatResult:function(O){return O.text
},formatSelection:function(O){return O.text
},ajax:{url:o.appendAuthToken(o.centerapipath+"customfield/searchautogenerated"),dataType:"json",data:function(O,P){return{q:O,page_limit:10,page:P-1,field_name:"lotID$AG$String"}
},results:function(R,Q){R=R.data;
var O=(Q*10)<R.total;
var P=[];
angular.forEach(R.records,function(T,S){P.push({id:T,text:T})
});
P=k.orderByName(P,["text"]);
return{results:P,more:O}
}}};
k.autoCompletePacket={multiple:true,closeOnSelect:true,placeholder:"Select Packet",allowClear:true,initSelection:function(O,Q){var P=[];
O.on("change",function(X){var S=X.added;
var W=X.removed;
if(S!==undefined&&S!==null){k.payload.packet=X.added.id;
if(k.dataVals.operation==="RQ"){var V={};
V.Packet=k.payload.packet;
var U="PAC"+k.payload.packet;
f(V,U)
}else{k.medium="Packet";
K("Packet",null,null,X.added.id)
}}else{if(W!==undefined&&W!==null){e.splice(e.indexOf(W.id),1);
if(k.dataVals.operation==="RQ"){k.selectorIdsForRequest.splice(k.selectorIdsForRequest.indexOf("PAC"+W.id),1);
L.Packets.splice(L.Packets.indexOf(W.id),1);
v()
}else{if(k.dataVals.operation==="II"){for(var R=0;
R<k.IIStockDtls.length;
R++){var T=k.IIStockDtls[R];
if(T.packetNumber===W.id){k.IIStockDtls.splice(R,1);
break
}}}}}}});
Q(P)
},formatResult:function(O){return O.text
},formatSelection:function(O){return O.text
},ajax:{url:o.appendAuthToken(o.centerapipath+"customfield/searchautogenerated"),dataType:"json",data:function(O,P){return{q:O,page_limit:10,page:P-1,field_name:"packetID$AG$String"}
},results:function(R,Q){R=R.data;
var P=(Q*10)<R.total;
var O=[];
angular.forEach(R.records,function(T,S){O.push({id:T,text:T})
});
O=k.orderByName(O,["text"]);
return{results:O,more:P}
}}};
k.orderByName=E("orderBy");
k.retrieveAvailableRequestStock=function(){H=[];
k.selectorIdsForRequest=[];
L={};
v();
delete k.destinationDeptIdForRequest;
k.availableRequestStock=[];
var O={requestType:"RQ"};
J.retrieveStockBySlip(O,function(P){if(P!==undefined&&P!==null&&P.length>0){angular.forEach(P,function(Q){if(Q.packetNumber!==undefined&&Q.packetNumber!==null){Q.selectorId="PAC"+Q.packetNumber
}else{if(Q.lotNumber!==undefined&&Q.lotNumber!==null){Q.selectorId="LOT"+Q.lotNumber
}else{if(Q.parcelNumber!==undefined&&Q.parcelNumber!==null){Q.selectorId="PAR"+Q.parcelNumber
}}}k.availableRequestStock.push(angular.copy(Q))
})
}})
};
function f(R,Q){if(k.selectorIdsForRequest.indexOf(Q)===-1){R.req_type=k.dataVals.operation;
var O=false;
angular.forEach(k.availableRequestStock,function(T){if(O===false&&Q===T.selectorId){T.toRequest=true;
O=true;
var U=[];
U.push({categoryCustom:angular.copy(k.generalCustom)});
var V=function(W){T.fieldValue=angular.copy(k.generalCustom);
T.fieldDbType=angular.copy(k.fieldDbType);
G()
};
t.convertorForCustomField(U,V)
}});
if(O===true){if(R.Parcel!==undefined&&R.Parcel!==undefined){if(L.Parcels===undefined||L.Parcels===null){L.Parcels=[]
}L.Parcels.push(R.Parcel);
k.selectorIdsForRequest.push("PAR"+R.Parcel)
}else{if(R.Lot!==undefined&&R.Lot!==undefined){if(L.Lots===undefined||L.Lots===null){L.Lots=[]
}L.Lots.push(R.Lot);
k.selectorIdsForRequest.push("LOT"+R.Lot)
}else{if(R.Packet!==undefined&&R.Packet!==undefined){if(L.Packets===undefined||L.Packets===null){L.Packets=[]
}L.Packets.push(R.Packet);
k.selectorIdsForRequest.push("PAC"+R.Packet)
}}}v()
}else{var P=o.failure;
o.addMessage("No stock available",P)
}}else{var S="Stock already added";
var P=o.failure;
o.addMessage(S,P)
}l()
}function x(){k.dataVals.selectedparcel=[];
k.dataVals.selectedlot=[];
k.dataVals.selectedpacket=[];
$("#parcel").select2("val");
$("#lot").select2("val");
$("#packet").select2("val")
}o.unMaskLoading()
}])
});