define(["hkg","designationService","departmentService","leaveWorkflowService","activityFlowService","treeviewMultiselect.directive"],function(a){a.register.controller("DesignationCtrl",["$rootScope","$scope","Designation","DepartmentService","LeaveWorkflow","ActivityFlowService","$filter",function(j,k,l,c,b,i,g){j.maskLoading();
k.searchRecords=[];
j.mainMenu="manageLink";
j.childMenu="manageDesignation";
j.activateMenu();
k.validations=[];
k.combinedFieldListForSearch=[];
k.combinedFieldListForParent=[];
k.invoiceList=[];
k.lotList=[];
k.parcelList=[];
k.packetList=[];
k.coatedRoughList=[];
k.issueList=[];
k.planList=[];
k.diamondList=[];
k.fieldList=[];
k.clearSelectedSearchFields=function(){if(k.combinedFieldListForSearch!==null&&k.combinedFieldListForSearch!==undefined&&k.combinedFieldListForSearch.length>0){angular.forEach(k.combinedFieldListForSearch,function(m){m.ticked=false
})
}};
k.clearSelectedParentFields=function(){if(k.combinedFieldListForParent!==null&&k.combinedFieldListForParent!==undefined&&k.combinedFieldListForParent.length>0){angular.forEach(k.combinedFieldListForParent,function(m){m.ticked=false
})
}};
k.selectedFieldsForSearch=[];
k.clearSelectedSearchFields();
k.clearSelectedParentFields();
k.selectedFieldsForParent=[];
k.defaultDepartmentIds=[];
k.featureFieldMap={};
k.configureGoalSheet={};
k.multiselecttreeGoalSheet={};
k.selectedDepValuesGoalSheet=[];
k.entity="DESIGNATION.";
k.isEditing=false;
k.visibleFields={invoice_add:[{id:"Invoice",name:"Invoice"}],invoice_edit:[{id:"Invoice",name:"Invoice"}],invoice_add_edit:[{id:"Invoice",name:"Invoice"}],parcel_add:[{id:"Parcel",name:"Parcel"}],parcel_edit:[{id:"Parcel",name:"Parcel"}],lot_add:[{id:"Lot",name:"Lot"}],lot_edit:[{id:"Lot",name:"Lot"}],stock_sell:[{id:"Sell",name:"Sell"}],stock_transfer:[{id:"Transfer",name:"Transfer"}],stock_merge:[{id:"Lot",name:"Lot"},{id:"Packet",name:"Packet"}],stock_split:[{id:"Lot",name:"Lot"},{id:"Packet",name:"Packet"}],packet_add:[{id:"Packet",name:"Packet"}],packet_edit:[{id:"Packet",name:"Packet"}],allot:[{id:"Lot",name:"Lot"},{id:"Packet",name:"Packet"}],finalize:[{id:"Plan",name:"Plan"}],generate_barcode:[{id:"Lot",name:"Lot"},{id:"Packet",name:"Packet"},{id:"Plan",name:"Plan"},{id:"Issue",name:"Issue"}],generate_slip:[{id:"Lot",name:"Lot"},{id:"Packet",name:"Packet"}],issue_receive:[{id:"Lot",name:"Lot"},{id:"Packet",name:"Packet"},{id:"Issue",name:"Issue"}],write_service:[{id:"Plan",name:"Plan"}],print_static:[{id:"Lot",name:"Lot"},{id:"Packet",name:"Packet"}],print_dynamic:[{id:"Lot",name:"Lot"},{id:"Packet",name:"Packet"}],status_change:[{id:"Lot",name:"Lot"},{id:"Packet",name:"Packet"}],parcel_merge:[{id:"Parcel",name:"Parcel"}]};
k.$on("$viewContentLoaded",function(){k.retrieveAccessRightsForDesignation()
});
k.initAddDesignationForm=function(m){k.displaySearchedDesignation="view";
k.designationNameForRemove="";
k.designationForm=m
};
k.retrieveAccessRightsForDesignation=function(){k.isAccessCreate=true;
if(!j.canAccess("designationAdd")){k.isAccessCreate=false
}k.isAccessEdit=true;
if(!j.canAccess("designationEdit")){k.isAccessEdit=false
}};
k.retrieveAllDesignations=function(){$("#scrollable-dropdown-menu.typeahead").typeahead("val","");
j.maskLoading();
c.retrieveDepartment(function(m){k.departmentList=[];
k.departmentIdNameMap={};
k.designationWithDepartmentList=[];
if(m!=null&&angular.isDefined(m)&&m.length>0){k.convertTreeviewToNormalList(m)
}l.retrieveDesignations(function(n){k.designationList=[];
k.displaySearchedDesignation="view";
if(n!=null&&angular.isDefined(n)&&n.length>0){angular.forEach(n,function(o){o.displayName=j.translateValue("DESIG_NM."+o.displayName);
k.designationList.push(o);
k.designationWithDepartmentList.push({id:o.id,text:o.displayName+(k.departmentIdNameMap[o.associatedDepartment]==undefined?"":" ("+k.departmentIdNameMap[o.associatedDepartment]+")")})
})
}if(!k.designationList||k.designationList==null){k.designationList=[]
}k.designationForm.$dirty=false;
j.unMaskLoading();
k.retrieveAllSysFeatures()
},function(){j.unMaskLoading();
k.retrieveAllSysFeatures()
})
},function(){j.unMaskLoading();
var n="Failed to retrieve department";
var m=j.error;
j.addMessage(n,m)
})
};
k.cancelLink=function(){k.clearDesignationForm();
$("#designation").select2("val",[]);
$("#designation1").select2("val",[]);
$("#service").select2("val",[]);
$("#service1").select2("val",[]);
k.resetSelection()
};
k.resetSelection=function(){if(k.selectedDesignation!=undefined){k.selectedDesignation.selected=undefined
}if(k.selectedDesignation!=undefined&&k.selectedDesignation.currentNode!=undefined){k.selectedDesignation.currentNode.selected=undefined;
k.selectedDesignation.currentNode=undefined
}k.configureGoal={};
k.configureGoalSheet={};
k.selectedInStringIds=[];
k.selectedInStringIdsGoalSheet=[];
k.selectedInStringGoalSheet=[];
k.defaultDepartmentIds=[];
k.defaultDepartmentIdsGoalSheet=[];
k.listForTable=[];
k.listOfPermissionGoalSheet=[];
$("#designation").select2("val",[]);
$("#designation1").select2("val",[]);
$("#service").select2("val",[]);
$("#service1").select2("val",[])
};
k.getSearchedDesignationRecords=function(n){k.searchRecords=[];
var m=$("#scrollable-dropdown-menu.typeahead").typeahead("val");
if(m.length>0){if(m.length<3){k.searchRecords=[]
}else{if(n!=null&&angular.isDefined(n)&&n.length>0){angular.forEach(n,function(o){o.displayName=j.translateValue("DESIG_NM."+o.displayName);
k.searchRecords.push(o)
})
}}k.resetSelection();
k.displaySearchedDesignation="search"
}};
k.convertTreeviewToNormalList=function d(m){angular.forEach(m,function(o){if(o.children===null||o.children===undefined){o.displayName=j.translateValue("DPT_NM."+angular.copy(o.displayName));
k.departmentList.push(o);
k.departmentIdNameMap[o.id]=o.displayName
}else{var n=angular.copy(o);
n.children=null;
n.displayName=j.translateValue("DPT_NM."+angular.copy(n.displayName));
k.departmentList.push(n);
k.departmentIdNameMap[n.id]=n.displayName;
d(o.children)
}})
};
k.checkIfParentAvailable=function(o,m){var n={designationId:k.editingDesigId,parentId:o};
l.checkCircularDependency(n,function(p){if(p.data===true){m.$setValidity("circular",false)
}else{m.$setValidity("circular",true)
}},function(p){})
};
k.retrieveAllSysFeatures=function(){j.maskLoading();
l.retrieveSystemFeatures(function(m){j.unMaskLoading();
k.systemFeaturesList=m;
if(!k.systemFeaturesList||k.systemFeaturesList==null){k.systemFeaturesList=[]
}else{k.tempSystemFeatureList=[];
k.diamondSystemFeatureList=[];
k.reportSystemFeatureList=[];
angular.forEach(k.systemFeaturesList,function(n){if(n.type==="DMI"||n.type==="DEI"){if(n.type!=="DEI"){k.diamondSystemFeatureList.push(n)
}}else{if(n.type==="RMI"){k.reportSystemFeatureList.push(n)
}else{k.tempSystemFeatureList.push(n)
}}});
k.systemFeaturesList=[];
k.systemFeaturesList=angular.copy(k.tempSystemFeatureList);
k.retrieveAssociatedGoalPermission();
k.retrieveAssociatedGoalSheetPermission();
k.retrieveReportGroupNames()
}},function(){j.unMaskLoading()
})
};
k.retrieveReportGroupNames=function(){k.groupIds=[];
if(k.reportSystemFeatureList!==undefined){for(var n=0;
n<k.reportSystemFeatureList.length;
n++){if(k.reportSystemFeatureList[n].description!==null&&k.reportSystemFeatureList[n].description!==""&&k.reportSystemFeatureList[n].type==="RMI"){var m=false;
angular.forEach(k.groupIds,function(o){if(k.reportSystemFeatureList[n].description===o){m=true
}});
if(!m){k.groupIds.push(k.reportSystemFeatureList[n].description)
}}}}l.retrieveReportGroupNames(k.groupIds,function(r){k.groupFeatureMap=[];
k.defaultFeatures=[];
k.reportGroupIdNameMap=r.data;
if(!!r.data){for(var q in k.reportGroupIdNameMap){k.groupFeatures=[];
for(var p=0;
p<k.reportSystemFeatureList.length;
p++){var o=false;
if(k.reportSystemFeatureList[p].description!==null&&k.reportSystemFeatureList[p].type==="RMI"){if(k.reportSystemFeatureList[p].description===q){k.groupFeatures.push(k.reportSystemFeatureList[p])
}}else{angular.forEach(k.defaultFeatures,function(s){if(s.id===k.reportSystemFeatureList[p].id){o=true
}});
if(!o){k.defaultFeatures.push(k.reportSystemFeatureList[p])
}}}k.groupFeatureMap.push({groupName:k.reportGroupIdNameMap[q],features:k.groupFeatures})
}}else{for(var p=0;
p<k.reportSystemFeatureList.length;
p++){var o=false;
if(k.reportSystemFeatureList[p].description!==null&&k.reportSystemFeatureList[p].type==="RMI"){}else{angular.forEach(k.defaultFeatures,function(s){if(s.id===k.reportSystemFeatureList[p].id){o=true
}});
if(!o){k.defaultFeatures.push(k.reportSystemFeatureList[p])
}}}}if(k.defaultFeatures!==undefined&&k.defaultFeatures!==null&&k.defaultFeatures.length>0){k.groupFeatureMap.push({groupName:"Default",features:k.defaultFeatures})
}},function(){})
};
k.createDesignation=function(){k.submitted=true;
if(!k.designationForm.$invalid){k.createBothDesignation()
}};
k.createBothDesignation=function(){var t=k.diamondSystemFeatureList.length;
var p=[];
var q=k.systemFeaturesList.length;
for(var v=0;
v<q;
v++){if((k.systemFeaturesList[v].iteamAttributesList==null||k.systemFeaturesList[v].iteamAttributesList.length==0)&&k.systemFeaturesList[v].checked){p.push(k.systemFeaturesList[v].id)
}var o=k.systemFeaturesList[v].iteamAttributesList.length;
for(var u=0;
u<o;
u++){if(k.systemFeaturesList[v].iteamAttributesList[u].checked){p.push(k.systemFeaturesList[v].iteamAttributesList[u].id)
}}}for(var v=0;
v<t;
v++){if(k.diamondSystemFeatureList[v].checked){p.push(k.diamondSystemFeatureList[v].id)
}}var m=[];
var z={};
var w={};
var A={};
if(!!k.selectedDesignation.currentNode){z.designation=k.selectedDesignation.currentNode.id;
w.designation=k.selectedDesignation.currentNode.id;
A.designation=k.selectedDesignation.currentNode.id
}z.referenceType="S";
if(!!k.configureGoal.service){z.referenceInstance=k.configureGoal.service.toString()
}w.referenceType="R";
if(!!k.configureGoal.designation){w.referenceInstance=k.configureGoal.designation.toString()
}A.referenceType="D";
if(!!k.selectedInStringIds){A.referenceInstance=k.selectedInStringIds.toString()
}m.push(z);
m.push(w);
m.push(A);
var n=[];
var y={};
var r={};
var s={};
if(!!k.selectedDesignation.currentNode){y.designation=k.selectedDesignation.currentNode.id;
r.designation=k.selectedDesignation.currentNode.id;
s.designation=k.selectedDesignation.currentNode.id
}y.referenceType="S";
if(!!k.configureGoalSheet.service){y.referenceInstance=k.configureGoalSheet.service.toString()
}r.referenceType="R";
if(!!k.configureGoalSheet.designation){r.referenceInstance=k.configureGoalSheet.designation.toString()
}s.referenceType="D";
if(!!k.selectedInStringIdsGoalSheet){s.referenceInstance=k.selectedInStringIdsGoalSheet.toString()
}n.push(y);
n.push(r);
n.push(s);
var x={displayName:k.designationTitle,associatedDepartment:k.associatedDepartment,parentDesignation:k.parentDesignation,sysFeatureIdList:p,staticServicesMap:k.featureFieldMap,goalPermissions:m,goalSheetPermissions:n};
j.maskLoading();
l.create(x,function(B){j.unMaskLoading();
if(B.messages){if(B.messages.length>0){if(B.messages[0].responseCode==1){k.showIsDesigExist=true
}}}else{k.clearDesignationForm();
k.searchDesignation=null
}k.retrieveAllDesignations();
k.submitted=false;
k.configureGoal={};
k.configureGoalSheet={};
k.selectedInStringIds=[];
k.selectedInStringIdsGoalSheet=[];
k.selectedInString=[];
k.selectedInStringGoalSheet=[];
k.defaultDepartmentIds=undefined;
k.defaultDepartmentIdsGoalSheet=undefined;
k.listForTable=[];
k.listOfPermissionGoalSheet=[];
$("#designation").select2("val",[]);
$("#designation1").select2("val",[]);
$("#service").select2("val",[]);
$("#service1").select2("val",[]);
$("#scrollable-dropdown-menu.typeahead").typeahead("val","");
k.cancelConfigurationPopup();
k.cancelConfirmPopup()
},function(){j.unMaskLoading();
j.addMessage("Designation could not be saved. Try again",1)
})
};
k.retrieveDesignationById=function(m,n){if(!k.isAccessEdit){j.addMessage("You don't have the right to edit designation",1)
}else{l.retrieveDesignation({primaryKey:m},function(o){j.maskLoading();
k.designationNameForRemove=o.displayName;
k.fillDesignationForm(o.displayName,o.associatedDepartment,o.parentDesignation,o.sysFeatureIdList,m,n,o.staticServicesMap);
if(o.displayName==="HK Admin"||o.displayName==="Franchise Admin"){k.isDisabled=true
}else{k.isDisabled=false
}$("#scrollable-dropdown-menu.typeahead").typeahead("val","");
k.retrieveAssociatedGoalPermission();
k.retrieveAssociatedGoalSheetPermission();
j.unMaskLoading()
})
}};
k.deleteDesignationById=function(m){l.retrieveUsersCount({primaryKey:m},function(n){if(n.data>0){j.unMaskLoading();
$("#designationActiveUserModal").modal("show");
$("#confirmationPopUp").modal("hide");
j.removeModalOpenCssAfterModalHide()
}else{j.maskLoading();
l.deleteById({primaryKey:m},function(){k.retrieveAllDesignations();
k.hideConfirmationPopup();
k.clearDesignationForm();
$("#designation").select2("val",[]);
$("#service").select2("val",[]);
k.isEditing=false;
$("#scrollable-dropdown-menu.typeahead").typeahead("val","");
j.unMaskLoading()
},function(){j.addMessage("Designation could not be removed. Try again.",1);
j.unMaskLoading()
})
}})
};
k.fillDesignationForm=function(B,w,r,y,A,v,z){var x=k.copyDesignation;
if(k.isEditing){k.clearDesignationForm()
}if(v){k.searchDesignation=B
}else{k.searchDesignation=null
}k.designationStatus="Active";
if(k.isEditing){k.designationTitle=B
}k.associatedDepartment=w;
k.parentDesignation=r;
if(k.isCopy){k.copyDesignation=x
}if(k.isEditing){if(!k.isCopy){k.editingDesigId=A
}k.editingDesigTtile=B
}k.isDesigNameExist();
if(k.isEditing||k.isCopy){k.featureFieldMap=z
}var q=y.length;
var p=k.systemFeaturesList.length;
var n=k.diamondSystemFeatureList.length;
var m=k.reportSystemFeatureList.length;
var o;
for(var u=0;
u<q;
u++){for(var s=0;
s<p;
s++){if(k.systemFeaturesList[s].id==y[u]){k.systemFeaturesList[s].checked=true;
k.systemFeaturesList[s].configure=true
}o=k.systemFeaturesList[s].iteamAttributesList.length;
for(var t=0;
t<o;
t++){if(k.systemFeaturesList[s].iteamAttributesList[t].id==y[u]){k.systemFeaturesList[s].iteamAttributesList[t].checked=true;
k.systemFeaturesList[s].iteamAttributesList[t].configure=true
}}k.checkUncheckMI(k.systemFeaturesList[s])
}for(var s=0;
s<n;
s++){if(k.diamondSystemFeatureList[s].id==y[u]){k.diamondSystemFeatureList[s].checked=true;
k.diamondSystemFeatureList[s].configure=true
}o=k.diamondSystemFeatureList[s].iteamAttributesList.length;
for(var t=0;
t<o;
t++){if(k.diamondSystemFeatureList[s].iteamAttributesList[t].id==y[u]){k.diamondSystemFeatureList[s].iteamAttributesList[t].checked=true
}}k.checkUncheckMI(k.systemFeaturesList[s])
}for(var s=0;
s<m;
s++){if(k.reportSystemFeatureList[s].id==y[u]){k.reportSystemFeatureList[s].checked=true
}o=k.reportSystemFeatureList[s].iteamAttributesList.length;
for(var t=0;
t<o;
t++){if(k.reportSystemFeatureList[s].iteamAttributesList[t].id==y[u]){k.reportSystemFeatureList[s].iteamAttributesList[t].checked=true
}}}}};
k.isDesigNameExist=function(){if(k.designationTitle!==null&&k.designationTitle!==undefined&&k.designationTitle!==""){var n=k.designationList.length;
for(var m=0;
m<n;
m++){if((k.designationList[m].displayName).toLowerCase()==(k.designationTitle).toLowerCase()){if(k.isEditing){if(k.editingDesigTtile.toLowerCase()==k.designationTitle.toLowerCase()){k.showIsDesigExist=false
}else{k.showIsDesigExist=true
}}else{k.showIsDesigExist=true
}break
}else{k.showIsDesigExist=false
}}}};
k.editFromSearch=function(m){k.isShowEditForm(m,true,false,false)
};
k.isShowEditForm=function(o,m,n,p){k.displaySearchedDesignation="view";
if(o!==null&&o!==""&&o!==undefined){k.isEditing=m;
k.isCopy=n;
k.retrieveDesignationById(o,p)
}else{if(o==null||o==""||o==undefined&&n===true&&k.editingDesigId!==null){k.retrieveAllSysFeatures()
}}};
k.retrieveAssociatedGoalPermission=function(){k.configureGoal={};
k.listForTable=[];
if(!!k.selectedDesignation.currentNode){l.retrieveActiveGoalPermission({designation:k.selectedDesignation.currentNode.id},function(q){var o=q.data;
if(o!==null){var p=[];
angular.forEach(o,function(s){if(s.referenceType=="S"){p.push(s.referenceInstance)
}});
k.configureGoal.service=[];
var n=p.toString();
var m=n.split(",").map(function(s){return parseInt(s,10)
});
k.configureGoal.service=m;
$("#service").select2("val",m);
k.configureGoal.designationNames=[];
angular.forEach(k.configureGoal.service,function(s){angular.forEach(k.allServices,function(t){if(s==t.nodeId){if(k.configureGoal.designationNames.indexOf(t.designationName)==-1){k.configureGoal.designationNames.push(t.designationName)
}k.listForTable.push({id:t.nodeId,name:t.associatedServiceName+"("+t.activityName+")",type:"Activity-Service"})
}})
});
k.configureGoal.designationNamesInString=k.configureGoal.designationNames.toString();
var p=[];
angular.forEach(o,function(s){if(s.referenceType=="R"){p.push(s.referenceInstance)
}});
angular.forEach(k.designationList,function(s){angular.forEach(p,function(t){if(parseInt(t)===s.id){k.listForTable.push({id:s.id,name:s.displayName,type:"Designation"})
}})
});
k.configureGoal.designation=[];
var n=p.toString();
var r=n.split(",").map(function(s){return parseInt(s,10)
});
k.configureGoal.designation=r;
$("#designation").select2("val",r);
var p=[];
k.selectedInString=[];
angular.forEach(o,function(s){if(s.referenceType=="D"){p.push(s.referenceInstance);
k.depName="";
var t=k.getDepName(s.referenceInstance,k.multiselecttree.items);
k.selectedInString.push(t);
k.listForTable.push({id:s.referenceInstance,name:t,type:"Department"})
}});
k.defaultDepartmentIds=p;
k.selectedInStringIds=p
}},function(){console.log("Fail to retrieve...")
})
}};
k.retrieveAssociatedGoalSheetPermission=function(){k.configureGoalSheet={};
k.listOfPermissionGoalSheet=[];
if(!!k.selectedDesignation.currentNode){l.retrieveActiveGoalSheetPermission({designation:k.selectedDesignation.currentNode.id},function(q){var o=q.data;
if(o!==null){var p=[];
angular.forEach(o,function(s){if(s.referenceType=="S"){p.push(s.referenceInstance)
}});
k.configureGoalSheet.service=[];
var n=p.toString();
var m=n.split(",").map(function(s){return parseInt(s,10)
});
k.configureGoalSheet.service=m;
$("#service1").select2("val",m);
k.configureGoalSheet.designationNames=[];
angular.forEach(k.configureGoalSheet.service,function(s){angular.forEach(k.allServices,function(t){if(s==t.nodeId){if(k.configureGoalSheet.designationNames.indexOf(t.designationName)==-1){k.configureGoalSheet.designationNames.push(t.designationName)
}k.listOfPermissionGoalSheet.push({id:t.nodeId,name:t.associatedServiceName+"("+t.activityName+")",type:"Activity-Service"})
}})
});
k.configureGoalSheet.designationNamesInStringGoalSheet=k.configureGoalSheet.designationNames.toString();
var p=[];
angular.forEach(o,function(s){if(s.referenceType=="R"){p.push(s.referenceInstance)
}});
angular.forEach(k.designationList,function(s){angular.forEach(p,function(t){if(parseInt(t)===s.id){k.listOfPermissionGoalSheet.push({id:s.id,name:s.displayName,type:"Designation"})
}})
});
k.configureGoalSheet.designation=[];
var n=p.toString();
var r=n.split(",").map(function(s){return parseInt(s,10)
});
k.configureGoalSheet.designation=r;
$("#designation1").select2("val",r);
var p=[];
k.selectedInStringGoalSheet=[];
angular.forEach(o,function(s){if(s.referenceType=="D"){p.push(s.referenceInstance);
k.depName="";
var t=k.getDepName(s.referenceInstance,k.multiselecttree.items);
k.selectedInStringGoalSheet.push(t);
k.listOfPermissionGoalSheet.push({id:s.referenceInstance,name:t,type:"Department"})
}});
k.defaultDepartmentIdsGoalSheet=p;
k.selectedInStringIdsGoalSheet=p
}},function(){console.log("Fail to retrieve...")
})
}};
k.addClick=function(){k.displaySearchedDesignation="view";
k.isDisabled=false;
k.submitted=false;
k.isEditing=false;
k.searchDesignation=null;
k.editingDesigId=null;
k.clearDesignationForm();
$("#designation").select2("val",[]);
$("#designation1").select2("val",[]);
$("#service").select2("val",[]);
$("#service1").select2("val",[])
};
k.clearDesignationForm=function(){k.isDisabled=false;
k.submitted=false;
k.designationTitle=null;
k.associatedDepartment=undefined;
k.parentDesignation=undefined;
k.copyDesignation="";
k.selectedFieldsForSearch=[];
k.clearSelectedSearchFields();
k.clearSelectedParentFields();
k.selectedFieldsForParent=[];
k.commonList=[];
k.entitys=[];
k.designationStatus="";
k.showIsDesigExist=false;
$("#scrollable-dropdown-menu.typeahead").typeahead("val","");
var o=k.systemFeaturesList.length;
for(var n=0;
n<o;
n++){var p=k.systemFeaturesList[n].iteamAttributesList.length;
k.systemFeaturesList[n].checked=false;
k.systemFeaturesList[n].configure=false;
for(var m=0;
m<p;
m++){k.systemFeaturesList[n].iteamAttributesList[m].checked=false
}}var o=k.diamondSystemFeatureList.length;
for(var n=0;
n<o;
n++){var p=k.diamondSystemFeatureList[n].iteamAttributesList.length;
k.diamondSystemFeatureList[n].checked=false;
k.diamondSystemFeatureList[n].configure=false;
for(var m=0;
m<p;
m++){k.diamondSystemFeatureList[n].iteamAttributesList[m].checked=false
}}var o=k.reportSystemFeatureList.length;
for(var n=0;
n<o;
n++){var p=k.reportSystemFeatureList[n].iteamAttributesList.length;
k.reportSystemFeatureList[n].checked=false;
for(var m=0;
m<p;
m++){k.reportSystemFeatureList[n].iteamAttributesList[m].checked=false
}}if(!k.isEditing){k.searchDesignation=null
}k.designationForm.$setPristine();
k.configureGoal={};
k.configureGoalSheet={};
k.selectedInStringIds=[];
k.selectedInStringIdsGoalSheet=[];
k.selectedInStringGoalSheet=[];
k.defaultDepartmentIdsGoalSheet=[];
k.listForTable=[];
k.listOfPermissionGoalSheet=[]
};
k.updateDesignation=function(){var r=[];
var o=k.systemFeaturesList.length;
for(var n=0;
n<o;
n++){if(k.systemFeaturesList[n].checked){r.push(k.systemFeaturesList[n].id)
}var p=k.systemFeaturesList[n].iteamAttributesList.length;
for(var m=0;
m<p;
m++){if(k.systemFeaturesList[n].iteamAttributesList[m].checked){r.push(k.systemFeaturesList[n].iteamAttributesList[m].id)
}}}var o=k.diamondSystemFeatureList.length;
for(var n=0;
n<o;
n++){if(k.diamondSystemFeatureList[n].checked){r.push(k.diamondSystemFeatureList[n].id)
}var p=k.diamondSystemFeatureList[n].iteamAttributesList.length;
for(var m=0;
m<p;
m++){if(k.diamondSystemFeatureList[n].iteamAttributesList[m].checked){r.push(k.diamondSystemFeatureList[n].iteamAttributesList[m].id)
}}}var o=k.reportSystemFeatureList.length;
for(var n=0;
n<o;
n++){if(k.reportSystemFeatureList[n].checked){r.push(k.reportSystemFeatureList[n].id)
}var p=k.reportSystemFeatureList[n].iteamAttributesList.length;
for(var m=0;
m<p;
m++){if(k.reportSystemFeatureList[n].iteamAttributesList[m].checked){r.push(k.reportSystemFeatureList[n].iteamAttributesList[m].id)
}}}if(!k.designationForm.$invalid){var q={displayName:k.designationTitle,associatedDepartment:k.associatedDepartment,parentDesignation:k.parentDesignation,sysFeatureIdList:r,staticServicesMap:k.featureFieldMap,id:k.editingDesigId,goalPermissions:k.goalPermissionToConfigure,goalSheetPermissions:k.goalSheetPermissionToConfigure};
console.log("sending deisgnation///"+JSON.stringify(q));
j.maskLoading();
l.update(q,function(s){j.unMaskLoading();
if(s.messages){if(s.messages.length>0){if(s.messages[0].responseCode==1){k.showIsDesigExist=true
}}}else{k.clearDesignationForm();
k.isEditing=false;
k.searchDesignation=null;
k.editingDesigId=null
}k.submitted=false;
k.configureGoal={};
k.configureGoalSheet={};
k.selectedInStringIds=[];
k.selectedInStringIdsGoalSheet=[];
k.selectedInString=[];
k.selectedInStringGoalSheet=[];
k.defaultDepartmentIds=[];
k.defaultDepartmentIdsGoalSheet=[];
k.listForTable=[];
k.listOfPermissionGoalSheet=[];
$("#designation").select2("val",[]);
$("#designation1").select2("val",[]);
$("#service").select2("val",[]);
$("#service1").select2("val",[]);
k.cancelLink();
$("#scrollable-dropdown-menu.typeahead").typeahead("val","");
$("#notConfiguredPopup").modal("hide");
j.removeModalOpenCssAfterModalHide()
},function(){j.unMaskLoading();
j.addMessage("Designation could not be updated. Try again.",1)
})
}};
k.saveDesignation=function(){k.submitted=true;
if(k.designationStatus==="Active"){k.updateDesignation()
}else{k.showConfirmationPopup()
}};
k.showConfirmationPopup=function(){$("#confirmationPopUp").modal("show")
};
k.hideConfirmationPopup=function(){$("#confirmationPopUp").modal("hide");
j.removeModalOpenCssAfterModalHide();
$("#designationActiveUserModal").modal("hide");
j.removeModalOpenCssAfterModalHide()
};
k.isTextContains=function(m){if(k.searchSystemFeature!==null&&k.searchSystemFeature!==undefined&&k.searchSystemFeature!==""){if(m.toLowerCase().indexOf(k.searchSystemFeature.toLowerCase())>=0){return true
}else{return false
}}};
k.filterTypeahead=function(){var m=g("filter")(k.designationList,{name:k.searchDesignation});
if(m.length>0){k.isInValidSearchDesig=false
}else{k.isInValidSearchDesig=true
}};
k.checkUncheckIA=function(n){var o=n.iteamAttributesList.length;
for(var m=0;
m<o;
m++){n.iteamAttributesList[m].checked=n.checked
}if(!n.checked&&n.name=="goals_add_edit"){n.configure=false;
k.configureGoal={};
k.goalPermissionToConfigure=[];
k.selectedInStringIds=[];
k.selectedInString=[];
k.selectedDepValues=[];
k.defaultDepartmentIds=undefined;
k.listForTable=[];
f(k.multiselecttree.items);
$("#designation").select2("val",[]);
$("#service").select2("val",[])
}if(!n.checked&&n.name=="goalsheet_access"){n.configure=false
}};
var f=function(p,n){if(p!==undefined&&p!==null){for(var m=0;
m<p.length;
m++){var o=p[m];
o.isChecked=false;
if(o.items&&o.items!==null){f(o.items,n)
}}}};
k.checkUncheckMI=function(o){var m=0;
var p=o.iteamAttributesList.length;
if(p>0){for(var n=0;
n<p;
n++){if(o.iteamAttributesList[n].checked){m++
}}if(m===p){o.checked=o.iteamAttributesList[0].checked
}else{o.checked=false
}}};
k.checkUncheckDiamondIA=function(o){var p=o.iteamAttributesList.length;
for(var n=0;
n<p;
n++){o.iteamAttributesList[n].checked=o.checked
}if(o.configure&&!o.checked){o.configure=false;
for(var m in k.featureFieldMap){if(m==o.id){delete k.featureFieldMap[m]
}}}};
k.retrieveFieldsByFeature=function(){l.retrieveFieldsByFeature(function(m){angular.forEach(m.data,function(n){if(n.length>0){angular.forEach(n,function(o){k.fieldList.push(o)
})
}})
})
};
k.retrieveFieldsByFeature();
k.combinedFieldListForSearch=[];
k.configureFeature=function(r){console.log("diamond System featrure..."+JSON.stringify(r));
if(r.checked){k.diamondFeature=angular.copy(r);
var s=k.diamondFeature.menuLabel.split("_");
if(s[1]){var o=s[1]+" "+s[0];
k.labels=o
}else{var o=s[0];
k.labels=o
}k.type=k.diamondFeature.type;
k.featureEntity=s[0];
k.entityList=k.visibleFields[k.diamondFeature.menuLabel];
if(!!k.entityList){if(!!!k.entitys){k.entitys={}
}k.entitys.name=k.entityList[0].id
}var u="$scope.invoiceList";
k.invoiceList=[];
k.lotList=[];
k.parcelList=[];
k.packetList=[];
k.coatedRoughList=[];
k.issueList=[];
k.planList=[];
k.diamondList=[];
k.allotmentList=[];
k.transferList=[];
k.sellList=[];
k.selectedFieldsForSearch=[];
k.clearSelectedSearchFields();
k.clearSelectedParentFields();
k.selectedFieldsForParent=[];
k.combinedFieldListForSearch=[];
k.combinedFieldListForParent=[];
k.fieldListToSend=[];
k.fieldListToSend=angular.copy(k.fieldList);
angular.forEach(k.fieldList,function(v){if(v.entity==="Invoice"){k.invoiceList.push(v)
}else{if(v.entity==="Parcel"){k.parcelList.push(v)
}else{if(v.entity==="Packet"){k.packetList.push(v)
}else{if(v.entity==="Lot"){k.lotList.push(v)
}else{if(v.entity==="Coated Rough"){k.coatedRoughList.push(v)
}else{if(v.entity==="Issue"){k.issueList.push(v)
}else{if(v.entity==="Plan"){k.planList.push(v)
}else{if(v.entity==="Diamond"){k.diamondList.push(v)
}else{if(v.entity==="Allotment"){k.allotmentList.push(v)
}else{if(v.entity==="Transfer"){k.transferList.push(v)
}else{if(v.entity==="Sell"){k.sellList.push(v)
}}}}}}}}}}}});
if(k.invoiceList.length>0){k.combinedFieldListForSearch.push({modelName:"<strong>Invoice</strong>",multiSelectGroup:true});
if(k.invoiceList.length>0){angular.forEach(k.invoiceList,function(x){if(x.dbFieldName!==undefined){var w=x.dbFieldName.split("$");
if(w.length>0){var v=w[1];
if(v!=="IMG"&&v!=="UPD"){k.combinedFieldListForSearch.push(x)
}}}})
}}if(k.invoiceList.length!==0){k.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(k.parcelList.length>0){k.combinedFieldListForSearch.push({modelName:"<strong>Parcel</strong>",multiSelectGroup:true});
if(k.parcelList.length>0){angular.forEach(k.parcelList,function(x){if(x.dbFieldName!==undefined){var w=x.dbFieldName.split("$");
if(w.length>0){var v=w[1];
if(v!=="IMG"&&v!=="UPD"){k.combinedFieldListForSearch.push(x)
}}}})
}}if(k.parcelList.length!==0){k.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(k.lotList.length>0){k.combinedFieldListForSearch.push({modelName:"<strong>Lot</strong>",multiSelectGroup:true});
if(k.lotList.length>0){angular.forEach(k.lotList,function(x){if(x.dbFieldName!==undefined){var w=x.dbFieldName.split("$");
if(w.length>0){var v=w[1];
if(v!=="IMG"&&v!=="UPD"){k.combinedFieldListForSearch.push(x)
}}}})
}}if(k.lotList.length!==0){k.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(k.packetList.length>0){k.combinedFieldListForSearch.push({modelName:"<strong>Packet</strong>",multiSelectGroup:true});
if(k.packetList.length>0){angular.forEach(k.packetList,function(x){if(x.dbFieldName!==undefined){var w=x.dbFieldName.split("$");
if(w.length>0){var v=w[1];
if(v!=="IMG"&&v!=="UPD"){k.combinedFieldListForSearch.push(x)
}}}})
}}if(k.packetList.length!==0){k.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(k.issueList.length>0){k.combinedFieldListForSearch.push({modelName:"<strong>Issue</strong>",multiSelectGroup:true});
if(k.issueList.length>0){angular.forEach(k.issueList,function(x){if(x.dbFieldName!==undefined){var w=x.dbFieldName.split("$");
if(w.length>0){var v=w[1];
if(v!=="IMG"&&v!=="UPD"){k.combinedFieldListForSearch.push(x)
}}}})
}}if(k.issueList.length!==0){k.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(k.transferList.length>0){k.combinedFieldListForSearch.push({modelName:"<strong>Transfer</strong>",multiSelectGroup:true});
if(k.transferList.length>0){angular.forEach(k.transferList,function(x){if(x.dbFieldName!==undefined){var w=x.dbFieldName.split("$");
if(w.length>0){var v=w[1];
if(v!=="IMG"&&v!=="UPD"){k.combinedFieldListForSearch.push(x)
}}}})
}}if(k.transferList.length!==0){k.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(k.sellList.length>0){k.combinedFieldListForSearch.push({modelName:"<strong>Sell</strong>",multiSelectGroup:true});
if(k.sellList.length>0){angular.forEach(k.sellList,function(x){if(x.dbFieldName!==undefined){var w=x.dbFieldName.split("$");
if(w.length>0){var v=w[1];
if(v!=="IMG"&&v!=="UPD"){k.combinedFieldListForSearch.push(x)
}}}})
}}if(k.sellList.length!==0){k.combinedFieldListForSearch.push({multiSelectGroup:false})
}k.valList={Invoice:k.invoiceList,Parcel:k.parcelList,Lot:k.lotList,Packet:k.packetList,Issue:k.issueList,Plan:k.planList,Allotment:k.allotmentList,Transfer:k.transferList,Sell:k.sellList};
if(k.invoiceList.length>0){k.combinedFieldListForParent.push({modelName:"<strong>Invoice</strong>",multiSelectGroup1:true});
if(k.invoiceList.length>0){angular.forEach(k.invoiceList,function(v){k.combinedFieldListForParent.push(angular.copy(v))
})
}}if(k.invoiceList.length!==0){k.combinedFieldListForParent.push({multiSelectGroup1:false})
}if(k.parcelList.length>0){k.combinedFieldListForParent.push({modelName:"<strong>Parcel</strong>",multiSelectGroup1:true});
if(k.parcelList.length>0){angular.forEach(k.parcelList,function(v){k.combinedFieldListForParent.push(angular.copy(v))
})
}}if(k.parcelList.length!==0){k.combinedFieldListForParent.push({multiSelectGroup1:false})
}if(k.lotList.length>0){k.combinedFieldListForParent.push({modelName:"<strong>Lot</strong>",multiSelectGroup1:true});
if(k.lotList.length>0){angular.forEach(k.lotList,function(v){k.combinedFieldListForParent.push(angular.copy(v))
})
}}if(k.lotList.length!==0){k.combinedFieldListForParent.push({multiSelectGroup1:false})
}if(k.packetList.length>0){k.combinedFieldListForParent.push({modelName:"<strong>Packet</strong>",multiSelectGroup1:true});
if(k.packetList.length>0){angular.forEach(k.packetList,function(v){k.combinedFieldListForParent.push(angular.copy(v))
})
}}if(k.packetList.length!==0){k.combinedFieldListForParent.push({multiSelectGroup1:false})
}if(k.issueList.length>0){k.combinedFieldListForParent.push({modelName:"<strong>Issue</strong>",multiSelectGroup1:true});
if(k.issueList.length>0){angular.forEach(k.issueList,function(v){k.combinedFieldListForParent.push(angular.copy(v))
})
}}if(k.issueList.length!==0){k.combinedFieldListForParent.push({multiSelectGroup1:false})
}var s=k.diamondFeature.menuLabel.split("_");
var p=s[0];
angular.forEach(k.visibleFields[k.diamondFeature.menuLabel],function(v){if(!!v){if(!!!k.commonList||k.commonList.length<1){k.commonList=[];
k.commonList=angular.copy(k.valList[v.id])
}else{if(!!k.valList[v.id]){$.merge(k.commonList,k.valList[v.id])
}}}});
angular.forEach(k.commonList,function(v){v.selected="hide"
});
k.commonList.sort(e);
if(k.isEditing||k.isCopy||r.configure===true){k.editFieldList=[];
k.editFieldList=k.featureFieldMap[r.id];
angular.forEach(k.editFieldList,function(v){if(v.searchFlag){v.ticked=true;
k.selectedFieldsForSearch.push(v);
v.ticked=false
}angular.forEach(k.combinedFieldListForSearch,function(w){if(v.field===w.field&&v.searchFlag){w.ticked=true
}})
});
angular.forEach(k.editFieldList,function(v){if(v.parentViewFlag){v.ticked=true;
k.selectedFieldsForParent.push(v);
v.ticked=false
}angular.forEach(k.combinedFieldListForParent,function(w){if(v.field===w.field&&v.parentViewFlag){w.ticked=true
}})
})
}if(!k.isEditing&&!k.isCopy&&r.configure===false){var n=k.diamondSystemFeatureList.length;
for(var t in k.featureFieldMap){for(var q=0;
q<n;
q++){if(t===k.diamondSystemFeatureList[q].id&&r.configure===false){var m=k.diamondSystemFeatureList[q].menuLabel.split("_");
if(m[0]===s[0]){k.editFieldList=[];
k.editFieldList=k.featureFieldMap[t];
angular.forEach(k.editFieldList,function(v){if(v.searchFlag){v.ticked=true;
k.selectedFieldsForSearch.push(v);
v.ticked=false
}angular.forEach(k.combinedFieldListForSearch,function(w){if(v.field===w.field&&v.searchFlag){w.ticked=true
}})
});
angular.forEach(k.editFieldList,function(v){if(v.parentViewFlag){v.ticked=true;
k.selectedFieldsForParent.push(v);
v.ticked=false
}angular.forEach(k.combinedFieldListForParent,function(w){if(v.field===w.field&&v.parentViewFlag){w.ticked=true
}})
})
}}}}}$("#configurePopUp").modal("show")
}};
k.setSearchField=function(m){k.selectedFieldsForSearch=angular.copy(m)
};
k.setParentField=function(m){k.selectedFieldsForParent=angular.copy(m)
};
k.cancelConfirmPopup=function(){$("#notConfiguredPopup").modal("hide");
j.removeModalOpenCssAfterModalHide()
};
k.cancelConfigurationPopup=function(){h=0;
k.nextPage=false;
k.selectedFieldsForSearch=[];
k.clearSelectedSearchFields();
k.clearSelectedParentFields();
k.selectedFieldsForParent=[];
k.commonList=[];
k.entitys=[];
for(var n=0;
n<k.systemFeaturesList.length;
n++){k.systemFeaturesList[n].configure=false
}$("#configurePopUp").modal("hide");
j.removeModalOpenCssAfterModalHide();
if(k.diamondFeature&&(k.featureFieldMap[k.diamondFeature.id]!==undefined&&k.featureFieldMap[k.diamondFeature.id]!==null)&&k.featureFieldMap[k.diamondFeature.id].length>0){for(var m=0;
m<k.featureFieldMap[k.diamondFeature.id].length;
m++){delete k.featureFieldMap[k.diamondFeature.id][m].ticked
}}};
k.setNextPage=function(){k.nextPage=true;
if(k.isEditing){if(k.editFieldList){for(var n=0;
n<k.editFieldList.length;
n++){for(var m=0;
m<k.commonList.length;
m++){if(k.editFieldList[n].field===k.commonList[m].field){if(k.editFieldList[n].readonlyFlag){k.commonList[m].selected="view only"
}else{if(k.editFieldList[n].editableFlag){k.commonList[m].selected="view and edit"
}else{k.commonList[m].selected="hide"
}}k.commonList[m].sequenceNo=k.editFieldList[n].sequenceNo;
k.commonList[m].isRequired=k.editFieldList[n].isRequired
}}}k.commonList.sort(e)
}}};
k.setPreviousPage=function(){k.nextPage=false
};
function e(m,n){if(m.sequenceNo===""||m.sequenceNo===null){return 1
}if(n.sequenceNo===""||n.sequenceNo===null){return -1
}return m.sequenceNo-n.sequenceNo
}k.sortSequence=function(){k.commonList.sort(e)
};
k.savePermission=function(){for(var n=0;
n<k.commonList.length;
n++){for(var m=0;
m<k.fieldListToSend.length;
m++){if(k.commonList[n].field===k.fieldListToSend[m].field){if(k.commonList[n].selected==="hide"){}else{if(k.commonList[n].selected==="view only"){k.fieldListToSend[m].readonlyFlag=true
}else{if(k.commonList[n].selected==="view and edit"){k.fieldListToSend[m].editableFlag=true
}}}k.fieldListToSend[m].sequenceNo=k.commonList[n].sequenceNo;
k.fieldListToSend[m].isRequired=k.commonList[n].isRequired
}}}for(var n=0;
n<k.selectedFieldsForSearch.length;
n++){for(var m=0;
m<k.fieldListToSend.length;
m++){if(k.selectedFieldsForSearch[n].field===k.fieldListToSend[m].field){k.fieldListToSend[m].searchFlag=true
}}}for(var n=0;
n<k.selectedFieldsForParent.length;
n++){for(var m=0;
m<k.fieldListToSend.length;
m++){if(k.selectedFieldsForParent[n].field===k.fieldListToSend[m].field){k.fieldListToSend[m].parentViewFlag=true
}}}k.featureFieldMap[k.diamondFeature.id]=k.fieldListToSend;
var o=k.diamondSystemFeatureList.length;
for(var n=0;
n<o;
n++){if(k.diamondSystemFeatureList[n].id===k.diamondFeature.id&&k.diamondSystemFeatureList[n].checked){k.diamondSystemFeatureList[n].configure=true
}}k.cancelConfigurationPopup();
$("#configurePopUp").modal("hide");
j.removeModalOpenCssAfterModalHide();
h=0
};
k.commonListDtOptions={aoColumnDefs:[{bSortable:false,aTargets:[-2,-3]}]};
var h=0;
k.fieldTableContent=function(){h++;
if(h<=0){k.commonList=[];
angular.forEach(k.visibleFields[k.diamondFeature.menuLabel],function(o){if(!!o){if(!!!k.commonList||k.commonList.length<1){k.commonList=[];
k.commonList=angular.copy(k.valList[o.id])
}else{$.merge(k.commonList,k.valList[o.id])
}}});
angular.forEach(k.commonList,function(o){o.selected="hide";
o.isRequired=false
});
if(k.editFieldList){for(var n=0;
n<k.editFieldList.length;
n++){for(var m=0;
m<k.commonList.length;
m++){if(k.editFieldList[n].field===k.commonList[m].field){if(k.editFieldList[n].readonlyFlag){k.commonList[m].selected="view only"
}else{if(k.editFieldList[n].editableFlag){k.commonList[m].selected="view and edit"
}else{k.commonList[m].selected="hide"
}}k.commonList[m].sequenceNo=k.editFieldList[n].sequenceNo;
k.commonList[m].isRequired=k.editFieldList[n].isRequired
}}}}}k.entitys.filteredList=g("commonListFilter")(k.commonList,k.entitys.name)
};
a.register.filter("commonListFilter",function(){return function(m,q){if(angular.isDefined(m)&&angular.isDefined(q)){if(angular.isDefined(q)&&!!q){var n=[];
for(var o=0;
o<m.length;
o++){var p=m[o];
if((p.entity!==null&&p.entity.toLowerCase().indexOf(q.toLowerCase())>=0)){n.push(p)
}}return n
}}else{if(k.type==="DMI"){return m
}else{return null
}}}
});
a.register.filter("removeCurrent",function(){return function(n,m){var o=[];
if(n!==undefined&&n!==null){for(var p=0;
p<n.length;
p++){var q=n[p];
if(q.id!=m){o.push(q)
}}return o
}else{return null
}}
});
k.configureGoal={};
k.configureNonDiamondFeature=function(m){k.diamondGoalFeature=m;
if(m.checked){k.labels=m.menuLabel;
if(m.name==="goals_add_edit"){$("#configureNonDiamondPopUp").modal("show")
}else{if(m.name==="goalsheet_access"){$("#configureGoalSheetSheetPopUp").modal("show")
}}}};
k.cancelNonDiamondConfigurationPopup=function(){$("#configureNonDiamondPopUp").modal("hide");
j.removeModalOpenCssAfterModalHide()
};
k.cancelGoalSheetPopup=function(){$("#configureGoalSheetSheetPopUp").modal("hide");
j.removeModalOpenCssAfterModalHide()
};
k.autoCompleteApprover={allowClear:true,multiple:true,closeOnSelect:false,placeholder:"Select designation",initSelection:function(m,o){var n=[];
if(!!k.configureGoal.designation){angular.forEach(k.designationList,function(p){angular.forEach(k.configureGoal.designation,function(q){if(q===p.id){n.push({id:p.id,text:p.displayName})
}})
})
}o(n)
},query:function(o){var n=o.term;
k.names=[];
var p=function(q){if(q.length==0){o.callback({results:k.names})
}else{angular.forEach(q,function(r){k.names.push({id:r.id,text:r.displayName})
});
o.callback({results:k.names})
}};
var m=function(){};
if(o.term!=null&&o.term!=""){l.retrieveDesignationBySearch({q:o.term.trim()},p,m)
}}};
k.autoCompleteApproverGoalSheet={allowClear:true,multiple:true,closeOnSelect:false,placeholder:"Select designation",initSelection:function(m,o){var n=[];
if(!!k.configureGoalSheet.designation){angular.forEach(k.designationList,function(p){angular.forEach(k.configureGoalSheet.designation,function(q){if(q===p.id){n.push({id:p.id,text:p.displayName})
}})
})
}o(n)
},query:function(o){var n=o.term;
k.names=[];
var p=function(q){if(q.length==0){o.callback({results:k.names})
}else{angular.forEach(q,function(r){k.names.push({id:r.id,text:r.displayName})
});
o.callback({results:k.names})
}};
var m=function(){};
if(o.term!=null&&o.term!=""){l.retrieveDesignationBySearch({q:o.term.trim()},p,m)
}}};
k.setSelectedParent=function(m){k.$on("$locationChangeStart",function(n){if(m!==null){if(!confirm("You have unsaved changes, go back?")){n.preventDefault()
}else{m===null
}}});
if(!angular.equals(m,{})){k.invalidParent=false;
k.selectedParent=m.currentNode;
if(k.selectedParent.displayName.length>50){k.selectedParent.displayName=k.selectedParent.displayName.substring(0,32).concat("---")
}}};
k.initDept=function(){k.multiselecttree={text:"All Department",isChecked:false,items:[]};
k.departments=k.retrieveAllDepartments();
k.createWatchOnSelectedItemList()
};
k.initDeptGoalSheet=function(){k.multiselecttreeGoalSheet={text:"All Department",isChecked:false,items:[]};
k.departments=k.retrieveAllDepartments();
k.createWatchOnSelectedItemListGoalSheet()
};
k.retrieveAllDepartments=function(){var m=function(){var o="Failed to retrieve departments. Try again.";
var n=j.failure;
j.addMessage(o,n);
j.unMaskLoading()
};
b.retrieveDepartmentsCombine(function(n){k.multiselect=[];
if(n.tree3.items!=null&&angular.isDefined(n.tree3.items)&&n.tree3.items.length>0){angular.forEach(n.tree3.items,function(o){k.multiselect.push(o)
})
}k.multiselecttree.items=k.multiselect;
k.multiselecttreeGoalSheet.items=k.multiselect
},m)
};
k.createWatchOnSelectedItemList=function(){k.$watch("selectedItemList",function(o){if(k.selectedItemList!=undefined){k.selectedInString=[];
k.selectedInStringIds=[];
if(k.selectedItemList.items.length>0){k.selectedDepValues=[];
k.iterateChildOfTree(k.selectedItemList.items)
}else{k.selectedDepValues=[];
k.selectedInString=[];
k.selectedInStringIds=[];
k.configureGoal.isDepInvalid=true
}}if(!!k.selectedDepValues){if(!!k.listForTable){var m=$.map(k.listForTable,function(q,p){if(q.type=="Department"){return p
}})
}if(!!m){m.sort();
for(var n=0;
n<m.length;
n++){k.listForTable.splice(m[n]-n,1)
}}if(k.selectedDepValues.length>0){angular.forEach(k.selectedDepValues,function(p){k.selectedInString.push(p.text);
k.selectedInStringIds.push(p.id);
if(!!!k.listForTable){k.listForTable=[]
}k.listForTable.push({id:p.id,name:p.text,type:"Department"})
})
}}},true)
};
k.createWatchOnSelectedItemListGoalSheet=function(){k.$watch("selectedItemListGoalSheet",function(o){if(k.selectedItemListGoalSheet!=undefined){k.selectedInStringGoalSheet=[];
k.selectedInStringIdsGoalSheet=[];
if(k.selectedItemListGoalSheet.items.length>0){k.selectedDepValuesGoalSheet=[];
k.iterateChildOfTreeGoalSheet(k.selectedItemListGoalSheet.items)
}else{k.selectedDepValuesGoalSheet=[];
k.selectedInStringGoalSheet=[];
k.selectedInStringIdsGoalSheet=[];
k.configureGoalSheet.isDepInvalid=true
}}if(!!k.selectedDepValuesGoalSheet){if(!!k.listOfPermissionGoalSheet){var m=$.map(k.listOfPermissionGoalSheet,function(q,p){if(q.type=="Department"){return p
}})
}if(!!m){m.sort();
for(var n=0;
n<m.length;
n++){k.listOfPermissionGoalSheet.splice(m[n]-n,1)
}}if(k.selectedDepValuesGoalSheet.length>0){angular.forEach(k.selectedDepValuesGoalSheet,function(p){k.selectedInStringGoalSheet.push(p.text);
k.selectedInStringIdsGoalSheet.push(p.id);
if(!!!k.listOfPermissionGoalSheet){k.listOfPermissionGoalSheet=[]
}k.listOfPermissionGoalSheet.push({id:p.id,name:p.text,type:"Department"})
})
}}},true)
};
k.iterateChildOfTree=function(o){var n=o;
for(var m=0;
m<n.length;
m++){if(n[m].isChecked){k.selectedDepValues.push({id:n[m].id,text:n[m].text})
}if(n[m].items){k.iterateChildOfTree(n[m].items)
}}};
k.iterateChildOfTreeGoalSheet=function(o){var n=o;
for(var m=0;
m<n.length;
m++){if(n[m].isChecked){k.selectedDepValuesGoalSheet.push({id:n[m].id,text:n[m].text})
}if(n[m].items){k.iterateChildOfTreeGoalSheet(n[m].items)
}}};
k.clearTreeSelection=function(n){var m=n;
m.isChecked=false;
if(!!m.items&&m.items.length>0){k.clearTreeSelection(m.items)
}k.clearData=m
};
k.getDepName=function(p,o){var n=o;
for(var m=0;
m<n.length;
m++){if(n[m].id==p){k.depName=n[m].text
}else{if(n[m].items){k.getDepName(p,n[m].items)
}}}return k.depName
};
k.initSelectedChildsOfTree=function(p,o){var n=p;
for(var m=0;
m<n.length;
m++){if(o.indexOf(n[m].id)){k.selectedvals.push({id:n[m].id,text:n[m].text,isChecked:true})
}if(n[m].items){k.initSelectedChildsOfTree(n[m].items,o)
}}};
$("#service").on("select2-selecting",function(o){if(k.configureGoal.designationNames==null){k.configureGoal.designationNames=[]
}var m="";
var n=[];
if(Object.prototype.toString.call(k.configureGoal.service)==="[object Array]"){angular.forEach(k.configureGoal.service,function(p){n.push(p.id)
});
m=n.toString()
}else{m=k.configureGoal.service
}if(!!m){angular.forEach(k.allServices,function(p){angular.forEach(m.split(","),function(q){if(parseInt(q)==parseInt(p.nodeId)&&k.configureGoal.designationNames.indexOf(p.designationName)==-1){k.configureGoal.designationNames.push(p.designationName)
}})
})
}angular.forEach(k.allServices,function(p){if(p.nodeId==o.val){if(!!!m){k.configureGoal.designationNames.push(p.designationName)
}if(!!!k.listForTable){k.listForTable=[]
}k.listForTable.push({id:o.val,name:o.object.text,type:"Activity-Service"})
}});
k.configureGoal.designationNamesInString=k.configureGoal.designationNames.toString()
});
$("#service1").on("select2-selecting",function(o){if(k.configureGoalSheet.designationNames==null){k.configureGoalSheet.designationNames=[]
}var m="";
var n=[];
if(Object.prototype.toString.call(k.configureGoalSheet.service)==="[object Array]"){angular.forEach(k.configureGoalSheet.service,function(p){n.push(p.id)
});
m=n.toString()
}else{m=k.configureGoalSheet.service
}if(!!m){angular.forEach(k.allServices,function(p){angular.forEach(m.split(","),function(q){if(parseInt(q)==parseInt(p.nodeId)&&k.configureGoalSheet.designationNames.indexOf(p.designationName)==-1){k.configureGoalSheet.designationNames.push(p.designationName)
}})
})
}angular.forEach(k.allServices,function(p){if(p.nodeId==o.val){if(!!!m){k.configureGoalSheet.designationNames.push(p.designationName)
}if(!!!k.listOfPermissionGoalSheet){k.listOfPermissionGoalSheet=[]
}k.listOfPermissionGoalSheet.push({id:o.val,name:o.object.text,type:"Activity-Service"})
}});
k.configureGoalSheet.designationNamesInStringGoalSheet=k.configureGoalSheet.designationNames.toString()
});
$("#service").on("select2-removing",function(o){angular.forEach(k.allServices,function(p){if(p.nodeId==o.val){var r=$.map(k.listForTable,function(t,s){if(t.id==o.val&&t.name==o.choice.text&&t.type=="Activity-Service"){return s
}});
var q=r[0];
if(q>-1){k.listForTable.splice(q,1)
}}});
var m="";
var n=[];
k.configureGoal.designationNames=[];
if(Object.prototype.toString.call(k.configureGoal.service)==="[object Array]"){angular.forEach(k.configureGoal.service,function(p){n.push(p.id)
});
m=n.toString()
}else{m=k.configureGoal.service
}if(!!m){m=m.split(",");
m.pop(o.val);
m=m.toString();
angular.forEach(k.allServices,function(p){angular.forEach(m.split(","),function(q){if(parseInt(q)==parseInt(p.nodeId)&&k.configureGoal.designationNames.indexOf(p.designationName)==-1){k.configureGoal.designationNames.push(p.designationName)
}})
})
}k.configureGoal.designationNamesInString=k.configureGoal.designationNames.toString()
});
$("#service1").on("select2-removing",function(o){angular.forEach(k.allServices,function(p){if(p.nodeId==o.val){var r=$.map(k.listOfPermissionGoalSheet,function(t,s){if(t.id==o.val&&t.name==o.choice.text&&t.type=="Activity-Service"){return s
}});
var q=r[0];
if(q>-1){k.listOfPermissionGoalSheet.splice(q,1)
}}});
var m="";
var n=[];
k.configureGoalSheet.designationNames=[];
if(Object.prototype.toString.call(k.configureGoalSheet.service)==="[object Array]"){angular.forEach(k.configureGoalSheet.service,function(p){n.push(p.id)
});
m=n.toString()
}else{m=k.configureGoalSheet.service
}if(!!m){m=m.split(",");
m.pop(o.val);
m=m.toString();
angular.forEach(k.allServices,function(p){angular.forEach(m.split(","),function(q){if(parseInt(q)==parseInt(p.nodeId)&&k.configureGoalSheet.designationNames.indexOf(p.designationName)==-1){k.configureGoalSheet.designationNames.push(p.designationName)
}})
})
}k.configureGoalSheet.designationNamesInStringGoalSheet=k.configureGoalSheet.designationNames.toString()
});
$("#designation").on("select2-selecting",function(m){if(!!!k.listForTable){k.listForTable=[]
}k.listForTable.push({id:m.val,name:m.object.text,type:"Designation"})
});
$("#designation").on("select2-removing",function(o){var n=$.map(k.listForTable,function(q,p){if(q.id==o.val&&q.name==o.choice.text&&q.type=="Designation"){return p
}});
var m=n[0];
if(m>-1){k.listForTable.splice(m,1)
}});
$("#designation1").on("select2-selecting",function(m){if(!!!k.listOfPermissionGoalSheet){k.listOfPermissionGoalSheet=[]
}k.listOfPermissionGoalSheet.push({id:m.val,name:m.object.text,type:"Designation"})
});
$("#designation1").on("select2-removing",function(o){var n=$.map(k.listOfPermissionGoalSheet,function(q,p){if(q.id==o.val&&q.name==o.choice.text&&q.type=="Designation"){return p
}});
var m=n[0];
if(m>-1){k.listOfPermissionGoalSheet.splice(m,1)
}});
k.autoCompleteApproverForService={allowClear:true,multiple:true,closeOnSelect:true,placeholder:"Select service",initSelection:function(m,o){var n=[];
i.retrieveServicesWithActivity({q:""},function(p){delete k.configureGoal.designationNamesInString;
if(!!k.configureGoal.service){var q=[];
angular.forEach(p.data,function(r){angular.forEach(k.configureGoal.service,function(s){if(parseInt(s)===parseInt(r.nodeId)){n.push({id:r.nodeId,text:r.associatedServiceName+"("+r.activityName+")"});
if(q.indexOf(r.designationName)==-1){q.push(r.designationName)
}}})
});
k.configureGoal.designationNamesInString=q.toString()
}o(n)
},function(){console.log("Failed In..")
})
},query:function(o){var n=o.term;
var p=function(q){k.names=[];
if(q.data.length==0){o.callback({results:k.names})
}else{angular.forEach(q.data,function(r){k.names.push({id:r.nodeId,text:r.associatedServiceName+"("+r.activityName+")"})
});
o.callback({results:k.names})
}};
var m=function(){};
if(o.term!=null&&o.term!=""){i.retrieveServicesWithActivity({q:o.term.trim()},p,m)
}else{o.callback({results:[]})
}}};
k.autoCompleteApproverForServiceGoalSheet={allowClear:true,multiple:true,closeOnSelect:true,placeholder:"Select service",initSelection:function(m,o){var n=[];
i.retrieveServicesWithActivity({q:""},function(p){delete k.configureGoalSheet.designationNamesInString;
if(!!k.configureGoalSheet.service){var q=[];
angular.forEach(p.data,function(r){angular.forEach(k.configureGoalSheet.service,function(s){if(parseInt(s)===parseInt(r.nodeId)){n.push({id:r.nodeId,text:r.associatedServiceName+"("+r.activityName+")"});
if(q.indexOf(r.designationName)==-1){q.push(r.designationName)
}}})
});
k.configureGoalSheet.designationNamesInStringGoalSheet=q.toString()
}o(n)
},function(){console.log("Failed In..")
})
},query:function(o){var n=o.term;
var p=function(q){k.names=[];
if(q.data.length==0){o.callback({results:k.names})
}else{angular.forEach(q.data,function(r){k.names.push({id:r.nodeId,text:r.associatedServiceName+"("+r.activityName+")"})
});
o.callback({results:k.names})
}};
var m=function(){};
if(o.term!=null&&o.term!=""){i.retrieveServicesWithActivity({q:o.term.trim()},p,m)
}else{o.callback({results:[]})
}}};
k.saveGoalPermission=function(){k.configureGoal.submitted=true;
if(k.roleForm.$valid){var n=true;
if(k.configureGoal.type===3&&k.selectedInStringIds==null){n=false
}if(n){var s=[];
var r={};
var m={};
var o={};
if(!!k.selectedDesignation.currentNode){r.designation=k.selectedDesignation.currentNode.id;
m.designation=k.selectedDesignation.currentNode.id;
o.designation=k.selectedDesignation.currentNode.id
}r.referenceType="S";
var q=[];
if(Object.prototype.toString.call(k.configureGoal.service)==="[object Array]"){angular.forEach(k.configureGoal.service,function(u){q.push(u.id)
});
r.referenceInstance=q.toString()
}else{r.referenceInstance=k.configureGoal.service
}m.referenceType="R";
var t=[];
if(Object.prototype.toString.call(k.configureGoal.designation)==="[object Array]"){angular.forEach(k.configureGoal.designation,function(u){t.push(u.id)
});
m.referenceInstance=t.toString()
}else{m.referenceInstance=k.configureGoal.designation
}o.referenceType="D";
if(!!k.selectedInStringIds){o.referenceInstance=k.selectedInStringIds.toString()
}s.push(r);
s.push(m);
s.push(o);
k.goalPermissionToConfigure=s;
$("#configureNonDiamondPopUp").modal("hide");
j.removeModalOpenCssAfterModalHide();
for(var p=0;
p<k.systemFeaturesList.length;
p++){if(k.systemFeaturesList[p].id===k.diamondGoalFeature.id&&k.systemFeaturesList[p].checked){k.systemFeaturesList[p].configure=true
}}}}};
k.saveGoalSheetPermission=function(){k.configureGoalSheet.submitted=true;
if(k.goalSheetForm.$valid){var n=true;
if(k.configureGoalSheet.type===3&&k.selectedInStringIdsGoalSheet==null){n=false
}if(n){var s=[];
var r={};
var m={};
var o={};
if(!!k.selectedDesignation.currentNode){r.designation=k.selectedDesignation.currentNode.id;
m.designation=k.selectedDesignation.currentNode.id;
o.designation=k.selectedDesignation.currentNode.id
}r.referenceType="S";
var q=[];
if(Object.prototype.toString.call(k.configureGoalSheet.service)==="[object Array]"){angular.forEach(k.configureGoalSheet.service,function(u){q.push(u.id)
});
r.referenceInstance=q.toString()
}else{r.referenceInstance=k.configureGoalSheet.service
}m.referenceType="R";
var t=[];
if(Object.prototype.toString.call(k.configureGoalSheet.designation)==="[object Array]"){angular.forEach(k.configureGoalSheet.designation,function(u){t.push(u.id)
});
m.referenceInstance=t.toString()
}else{m.referenceInstance=k.configureGoalSheet.designation
}o.referenceType="D";
if(!!k.selectedInStringIdsGoalSheet){o.referenceInstance=k.selectedInStringIdsGoalSheet.toString()
}s.push(r);
s.push(m);
s.push(o);
k.goalSheetPermissionToConfigure=s;
$("#configureGoalSheetSheetPopUp").modal("hide");
j.removeModalOpenCssAfterModalHide();
for(var p=0;
p<k.systemFeaturesList.length;
p++){if(k.systemFeaturesList[p].id===k.diamondGoalFeature.id&&k.systemFeaturesList[p].checked){k.systemFeaturesList[p].configure=true
}}}}};
k.cancelGoalConfigurationPopup=function(){$("#configureNonDiamondPopUp").modal("hide");
j.removeModalOpenCssAfterModalHide()
};
k.cancelGoalSheetPopup=function(){$("#configureGoalSheetSheetPopUp").modal("hide");
j.removeModalOpenCssAfterModalHide()
};
k.deletePermission=function(s){var p=k.listForTable.indexOf(s);
k.listForTable.splice(p,1);
if(s.type=="Activity-Service"){var o;
var q=[];
if(Object.prototype.toString.call(k.configureGoal.service)==="[object Array]"){angular.forEach(k.configureGoal.service,function(u){q.push(u.id)
});
o=q.toString()
}else{o=k.configureGoal.service
}var n=o.split(",");
var p=n.indexOf(s.id.toString());
if(p>-1){n.splice(p,1)
}var m=n.map(function(u){return parseInt(u,10)
});
k.configureGoal.service=m;
$("#service").select2("val",m)
}else{if(s.type=="Designation"){var r=[];
var o;
if(Object.prototype.toString.call(k.configureGoal.designation)==="[object Array]"){angular.forEach(k.configureGoal.designation,function(u){r.push(u.id)
});
o=r.toString()
}else{o=k.configureGoal.designation
}var n=o.split(",");
var p=n.indexOf(s.id.toString());
if(p>-1){n.splice(p,1)
}var m=n.map(function(u){return parseInt(u,10)
});
k.configureGoal.designation=m;
$("#designation").select2("val",m)
}else{if(s.type=="Department"){var t=[];
if(!!k.selectedInStringIds){var n=k.selectedInStringIds;
var p=n.indexOf(s.id);
if(p>-1){n.splice(p,1)
}k.selectedInString=[];
angular.forEach(n,function(u){t.push(u);
k.depName="";
k.selectedInString.push(k.getDepName(u,k.multiselecttree.items))
});
k.defaultDepartmentIds=t;
k.selectedInStringIds=t
}}}}};
k.deletePermissionGoalSheet=function(s){var p=k.listOfPermissionGoalSheet.indexOf(s);
k.listOfPermissionGoalSheet.splice(p,1);
if(s.type=="Activity-Service"){var o;
var q=[];
if(Object.prototype.toString.call(k.configureGoalSheet.service)==="[object Array]"){angular.forEach(k.configureGoalSheet.service,function(u){q.push(u.id)
});
o=q.toString()
}else{o=k.configureGoalSheet.service
}var n=o.split(",");
var p=n.indexOf(s.id.toString());
if(p>-1){n.splice(p,1)
}var m=n.map(function(u){return parseInt(u,10)
});
k.configureGoalSheet.service=m;
$("#service1").select2("val",m)
}else{if(s.type=="Designation"){var r=[];
var o;
if(Object.prototype.toString.call(k.configureGoalSheet.designation)==="[object Array]"){angular.forEach(k.configureGoalSheet.designation,function(u){r.push(u.id)
});
o=r.toString()
}else{o=k.configureGoalSheet.designation
}var n=o.split(",");
var p=n.indexOf(s.id.toString());
if(p>-1){n.splice(p,1)
}var m=n.map(function(u){return parseInt(u,10)
});
k.configureGoalSheet.designation=m;
$("#designation1").select2("val",m)
}else{if(s.type=="Department"){var t=[];
if(!!k.selectedInStringIdsGoalSheet){var n=k.selectedInStringIdsGoalSheet;
var p=n.indexOf(s.id);
if(p>-1){n.splice(p,1)
}k.selectedInStringGoalSheet=[];
angular.forEach(n,function(u){t.push(u);
k.depName="";
k.selectedInStringGoalSheet.push(k.getDepName(u,k.multiselecttree.items))
});
k.defaultDepartmentIdsGoalSheet=t;
k.selectedInStringIdsGoalSheet=t
}}}}};
k.dataTableOptions={columns:[null,null,{orderDataType:"dom-text-numeric"}],autoWidth:false};
k.retrieveAllDesignations()
}])
});