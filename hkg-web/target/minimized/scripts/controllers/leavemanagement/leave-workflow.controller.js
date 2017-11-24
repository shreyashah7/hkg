define(["hkg","leaveWorkflowService","departmentService","messageService","treeviewMultiselect.directive","addMasterValue","dynamicForm"],function(b,c,d,a){b.register.controller("LeaveWorkflowController",["$rootScope","$scope","$filter","LeaveWorkflow","$location","DepartmentService","DynamicFormService","Messaging","$timeout",function(o,q,m,e,k,f,p,n,g){o.maskLoading();
q.searchRecords=[];
o.mainMenu="manageLink";
o.childMenu="manageLeaveWorkflow";
o.activateMenu();
if(k.path()==="/manageleaveworkflow"){o.configureWorkFlow=false;
q.isDepInvalid=true
}q.entity="LEAVEWORKFLOW.";
q.message={};
q.editWorkflowDepName="";
q.message.messageType="";
q.submitted=false;
q.searchtooltip="Search";
q.workflow={};
q.editworkflow="";
q.editworkflow.departmentName="";
q.workflow.nameApprover={};
q.approvalMap=[];
q.$item=[];
q.isEditing=false;
q.isDefault=false;
q.selectedDepValues=[];
q.existingIds=[];
q.overrideDep=[];
q.selectedInString=[];
q.selectedInStringIds=[];
q.selectedDepartment={};
q.departmentName="";
q.displayLeaveWorkflowFlag="view";
q.approverInValid=true;
q.editapproverInValid=true;
q.workflowFlag=false;
q.reload=true;
q.listOfModelsOfDateType=[];
q.popover="<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td> \n '@R'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Designations</td></tr> <tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr> </table> ";
q.addLeaveWorkflowData={};
var h=p.retrieveSectionWiseCustomFieldInfo("manageLeaveWorkflow");
q.dbType={};
h.then(function(s){q.customTemplateDate=angular.copy(s.genralSection);
q.generalLeaveWorkflowTemplate=o.getCustomDataInSequence(q.customTemplateDate)
},function(s){},function(s){});
q.$on("$viewContentLoaded",function(){r()
});
q.resetCustomFields=function(t){q.listOfModelsOfDateType=[];
q.addLeaveWorkflowData={};
var s=p.retrieveSectionWiseCustomFieldInfo("manageLeaveWorkflow");
q.dbType={};
s.then(function(u){q.customTemplateDate=angular.copy(u.genralSection);
q.generalLeaveWorkflowTemplate=o.getCustomDataInSequence(q.customTemplateDate);
if(q.generalLeaveWorkflowTemplate!==null&&q.generalLeaveWorkflowTemplate!==undefined){angular.forEach(q.generalLeaveWorkflowTemplate,function(v){if(v.type!==null&&v.type!==undefined&&v.type==="date"){q.listOfModelsOfDateType.push(v.model)
}})
}if(!!t){t()
}},function(u){},function(u){})
};
function r(){q.isAccessWorkflow=true;
if(!o.canAccess("leaveWorkflowAddEdit")){q.isAccessWorkflow=false
}}q.changeDepartment=function(){if(q.selectedItemList!=null){q.workflow_form.$dirty=true
}};
q.changeMakeACopyOf=function(s){if(q.departmentListDropdown1!=null){q.workflow_form=s;
q.workflow_form.$dirty=true
}};
q.initDept=function(){q.multiselecttree={text:"All Dep",isChecked:false,items:[]};
q.departments=q.retrieveAllDepartments();
q.createWatchOnSelectedItemList()
};
q.autoCompleteApprover={multiple:true,closeOnSelect:false,placeholder:"Select approvers",initSelection:function(s,u){if(!q.isEditing){var t=[];
u(t)
}else{var t=[];
angular.forEach(q.approvalMap,function(v){t.push({id:v.value,text:v.label})
});
u(t)
}},formatResult:function(s){return s.text
},formatSelection:function(s){q.$item.push(s);
return s.text
},query:function(v){var u=v.term;
q.names=[];
var w=function(x){if(x.length==0){v.callback({results:q.names})
}else{q.names=x;
angular.forEach(x,function(y){q.names.push({id:y.value+":"+y.description,text:y.label})
});
v.callback({results:q.names})
}};
var s=function(){};
if(u.substring(0,2)=="@E"||u.substring(0,2)=="@e"){var t=v.term.slice(2);
e.retrieveUserList(t.trim(),w,s)
}else{if(u.substring(0,2)=="@R"||u.substring(0,2)=="@r"){var t=v.term.slice(2);
n.retrieveRoleList(t.trim(),w,s)
}else{if(u.length>0){var t=u;
e.retrieveUserList(t.trim(),w,s)
}else{v.callback({results:q.names})
}}}}};
q.retrieveAllDepartments=function(){if(!o.configureWorkFlow){o.maskLoading();
var s=function(){var u="Failed to retrieve departments. Try again.";
var t=o.failure;
o.addMessage(u,t);
o.unMaskLoading()
};
e.retrieveDepartmentsCombine(function(u){q.departmentListOption=[];
var v=u.tree1;
for(var t=0;
t<v.length;
t++){l(v[t])
}q.searchDepartmentList=angular.copy(q.departmentListOption);
q.departmentListOption=[];
if(q.selectedDepartment!==undefined&&q.selectedDepartment.currentNode!==undefined){q.selectedDepartment.currentNode=undefined
}q.departments1=u.tree2;
q.setDefaultWorkflowRootOption();
for(var t=0;
t<q.departments1.length;
t++){l(q.departments1[t])
}if(q.selectedDepartment!==undefined&&q.selectedDepartment.currentNode!==undefined){q.selectedDepartment.selected=undefined;
q.selectedDepartment.currentNode=undefined
}q.invalidParent1=false;
q.selectedParent1=q.departmentListDropdown1[0];
q.parentId1=q.departmentListDropdown1[0].id;
q.multiselect=[];
if(u.tree3.items!=null&&angular.isDefined(u.tree3.items)&&u.tree3.items.length>0){angular.forEach(u.tree3.items,function(w){q.multiselect.push(w)
})
}q.multiselecttree.items=q.multiselect;
o.unMaskLoading()
},s)
}};
q.departmentListOption=[];
function l(t){if(t===null||t===undefined){return
}q.departmentListOption.push({id:t.id,displayName:t.displayName,description:t.description,children:t.children,parentId:t.parentId,parentName:t.parentName,companyId:t.companyId,isActive:t.isActive,showAllDepartment:t.showAllDepartment});
if(t.children===null||t.children.length===0){return
}else{for(var s=0;
s<t.children.length>0;
s++){l(t.children[s])
}}}q.searchDepartmentList=[];
q.setRootOption=function(){q.departmentListDropdown=[];
q.departmentListDropdown.push({id:"0",displayName:"None"});
$.merge(q.departmentListDropdown,angular.copy(q.departments))
};
q.setDefaultWorkflowRootOption=function(){q.departmentListDropdown1=[];
q.departmentListDropdown1.push({id:"0",displayName:"None",children:null});
$.merge(q.departmentListDropdown1,angular.copy(q.departments1));
q.departmentListOptiontemp=[];
q.departmentListOptiontemp.push({id:0,displayName:"Default workflow",description:"default node",children:null,parentId:0,parentName:"None",companyId:0,isActive:true,showAllDepartment:null});
$.merge(q.departmentListOptiontemp,angular.copy(q.departments1));
q.departments1=angular.copy(q.departmentListOptiontemp);
q.departmentListOptiontemp=[];
if(angular.isDefined(q.workflow_form)){q.workflow_form.$dirty=false
}};
q.setSelectedParent1=function(u){if(u!=undefined){var t=u.currentNode.displayName;
q.invalidParent1=false;
q.selectedParent1=u.currentNode;
q.parentId1=u.currentNode.id;
for(var s=0;
s<q.departmentListDropdown1.length;
s++){if(q.lastSelectedOfDropdown1!==undefined){if(q.lastSelectedOfDropdown1.id===q.departmentListDropdown1[s].id){q.departmentListDropdown1[s].selected=null
}}}if(t=="None"){$("#approver").select2("data","");
q.list=[];
q.approverInValid=true
}else{q.setApprovers(false,q.parentId1,false)
}}};
q.openEditWorkflow=function(u,s,t){q.reload=false;
q.reload=false;
q.displayLeaveWorkflowFlag="view";
if(angular.isDefined(u)){if(!s){q.selectedDepartment.currentNode=angular.copy(u)
}q.searchDepId=u.id;
q.editworkflow=u;
if(u.id==0){q.isDefault=true
}else{q.isDefault=false
}q.editworkflow.departmentName=u.displayName;
q.parentId1=u.id;
q.isEditing=true;
q.editworkflow.status="true";
q.setApprovers(true,u.id,t)
}};
q.editWorkflow=function(s){q.displayLeaveWorkflowFlag="view";
q.workflowFlag=true;
q.parentId1=s;
q.searchDepId=s;
if(s==0){q.isDefault=true
}else{q.isDefault=false
}q.isEditing=true;
q.editworkflow.status="true";
q.setApprovers(true,s,false)
};
q.setApprovers=function(w,v,t){o.maskLoading();
var u=function(A){if(A.departmentName!=null){q.editWorkflowDepName=A.departmentName
}else{q.editWorkflowDepName=""
}if(A.id==null&&o.configureWorkFlow){q.isEditing=false;
q.submitted=false;
if(angular.isDefined(q.workflow_form)){q.workflow_form.$setPristine()
}}else{if(A.id==null){$("#approver").select2("data","");
$("#approver1").select2("data","");
q.list=[]
}else{$("#approver").select2("data","");
$("#approver1").select2("data","");
var B=A.approvalMap;
q.approvalMap=A.approvalMap;
var z="";
var x=[];
q.names=[];
var y=0;
angular.forEach(B,function(C){q.names.push({id:C.value,text:C.label});
y++;
x.push(C.value)
});
q.names.reverse();
$("#approver").select2("data",q.names);
$("#approver1").select2("data",q.names);
q.approverInValid=false;
if(q.selectedInString.length>0){q.workflow_form.$invalid=false
}x.reverse();
z=x.toString();
if(w){q.editworkflow.departmentName=A.departmentName;
q.editworkflow.nameApprover=angular.copy(z.toString())
}else{q.workflow.nameApprover=angular.copy(z.toString())
}}if(angular.isDefined(A.leaveWorkflowCustom)&&A.leaveWorkflowCustom!=null){q.resetCustomFields(function(){q.addLeaveWorkflowData=angular.copy(A.leaveWorkflowCustom);
angular.forEach(q.listOfModelsOfDateType,function(C){if(q.addLeaveWorkflowData.hasOwnProperty(C)){if(q.addLeaveWorkflowData[C]!==null&&q.addLeaveWorkflowData[C]!==undefined){q.addLeaveWorkflowData[C]=new Date(q.addLeaveWorkflowData[C])
}else{q.addLeaveWorkflowData[C]=""
}}});
q.reload=true
})
}}if(A.department==0){q.isDefault=true
}if(!t){q.submitted=true
}o.unMaskLoading()
};
var s=function(){var y="Failed to retrieve workflow detail. Try again.";
var x=o.failure;
o.addMessage(y,x);
o.unMaskLoading()
};
e.retrieveWorkflowByDepartmentId(v.toString(),u,s)
};
if(o.configureWorkFlow){var j={id:0};
q.openEditWorkflow(j,false,true);
q.submitted=false
}q.list=[];
q.$watch("workflow.nameApprover",function(){if(q.workflow.nameApprover!=undefined){if(typeof(q.workflow.nameApprover)=="string"){var s=[];
var u="";
var t=0;
angular.forEach(q.$item,function(v){var w=q.workflow.nameApprover.split(",");
$.each(w,function(z,C){if(C==v.id){var B=u.split(",");
var A=false;
angular.forEach(B,function(D){if(!A){if(D==v.id){A=true
}}});
if(!A){var y=v.id.split(":");
var x="";
if(y[1]=="R"){x="@Designation: "
}s.push({id:x+v.text,text:v.text,level:t,key:v.id});
if(u.length!=0){u=u+","
}u=u+v.id;
t++
}}})
});
q.list=angular.copy(s.reverse());
if(q.list.length>0){q.approverInValid=false;
if(q.selectedInString!==undefined&&q.selectedInString.length>0){q.workflow_form.$invalid=false
}}else{q.approverInValid=true;
if(angular.isDefined(q.workflow_form)){q.workflow_form.$invalid=true
}}}}else{q.workflow.nameApprover=""
}});
q.createWatchOnSelectedItemList=function(){q.$watch("selectedItemList",function(v){if(q.selectedItemList!=undefined){var w=[];
var t=[];
q.selectedInString=[];
q.selectedInStringIds=[];
if(q.selectedItemList.items.length>0){q.existingIds=[];
for(var s=0;
s<q.departmentListDropdown1.length;
s++){i(q.departmentListDropdown1[s])
}for(var s=0;
s<q.existingIds.length;
s++){var u=q.existingIds[s].id;
q.selectedDepValues=[];
if(s==0){q.iterateChildOfTree(q.selectedItemList.items,true,false,u,false,true);
t=angular.copy(q.selectedDepValues)
}q.selectedDepValues=[];
q.iterateChildOfTree(q.selectedItemList.items,true,true,u,false,true);
$.merge(w,angular.copy(q.selectedDepValues))
}}if(t.length>0){angular.forEach(t,function(x){q.selectedInString.push(x.text);
q.selectedInStringIds.push(x.id)
})
}q.isDepInvalid=false;
if(q.selectedInString.length==0&&!o.configureWorkFlow){q.isDepInvalid=true;
q.workflow_form.$invalid=true
}else{if(!angular.isDefined(q.list)||q.list.length==0){q.workflow_form.$invalid=true
}else{q.workflow_form.$invalid=false
}}q.selectedDepValues=[];
if(w.length>0){q.overrideDep=w;
$("#overrideModal").modal("show")
}}})
};
q.$watch("editworkflow.nameApprover",function(){if(q.editworkflow.nameApprover!=undefined){if(typeof(q.editworkflow.nameApprover)=="string"){var s=[];
var u="";
var t=0;
var v=q.editworkflow.nameApprover.split(",");
$.each(v,function(w,x){angular.forEach(q.$item,function(A){if(x==A.id){var C=u.split(",");
var B=false;
angular.forEach(C,function(D){if(!B){if(D==A.id){B=true
}}});
if(!B){var z=A.id.split(":");
var y="";
if(z[1]=="R"){y="@Designation: "
}s.push({id:y+A.text,text:A.text,level:t,key:A.id});
if(u.length!=0){u=u+","
}u=u+A.id;
t++
}}})
});
s.reverse();
q.list=angular.copy(s);
if(q.list.length>0){q.editapproverInValid=false
}else{q.editapproverInValid=true
}}}else{q.editworkflow.nameApprover=""
}});
q.clearData=function(s,t){q.reload=false;
q.setViewFlag("view");
q.isEditing=false;
q.submitted=false;
q.list=[];
q.workflow.nameApprover="";
q.editworkflow.nameApprover="";
q.editworkflow.status="";
q.isDefault=false;
q.invalidParent1=false;
q.selectedInString=[];
q.selectedInStringIds=[];
q.selectedDepValues=[];
q.existingIds=[];
q.overrideDep=[];
q.searchDepId=undefined;
if(q.selectedDepartment.currentNode!==undefined){q.selectedDepartment.selected=undefined;
q.selectedDepartment.currentNode.selected=undefined
}if(angular.isDefined(q.departmentListDropdown1)){q.selectedParent1=q.departmentListDropdown1[0];
q.parentId1=q.departmentListDropdown1[0].id
}$("#approver").select2("data","");
$("#approver1").select2("data","");
q.isDepInvalid=true;
q.approverInValid=true;
q.editapproverInValid=true;
if(angular.isDefined(s)){s.$invalid=true;
s.$setPristine()
}if(angular.isDefined(t)){t.$setPristine()
}if(o.configureWorkFlow){o.configureWorkFlow=false;
o.defaultWorkFlow=false
}q.searchtext=undefined;
g(function(){q.resetCustomFields();
q.reload=true
},50)
};
q.up=function(s){if(s!=0){var u=s;
var t=q.list[u];
q.list[u]=q.list[u-1];
q.list[u-1]=t
}};
q.down=function(s,v){if(v!=true){var u=s;
var t=q.list[u];
q.list[u]=q.list[u+1];
q.list[u+1]=t
}};
q.deleteRow=function(s,u){var t=q.isEditing;
q.list.splice(s,1);
if(t){$("#approver1").select2("data","")
}else{$("#approver").select2("data","")
}q.names=[];
angular.forEach(u,function(v){q.names.push({id:v.key,text:v.text})
});
if(t){$("#approver1").select2("data",q.names)
}else{$("#approver").select2("data",q.names)
}q.setInNameApprover()
};
q.setInNameApprover=function(){var s="";
angular.forEach(q.list,function(t){if(s.length!=0){s=s+","
}s=s+t.key
});
q.workflow.nameApprover=s.toString()
};
q.save={submit:function(s){q.submitted=true;
if(s.approver.$invalid||!s.$valid||q.isDepInvalid===true||q.approverInValid){}else{q.selectedIndex=undefined;
q.saveWorkflowDetail(s)
}},submitEdit:function(s){q.submitted=true;
if(q.editworkflow.status==="false"){$("#removeModal").modal("show")
}else{if(s.$valid){q.selectedIndex=undefined;
q.updateWorkflowDetail(s)
}}}};
q.hidePanel=function(){$("#removeModal").modal("hide");
o.removeModalOpenCssAfterModalHide()
};
q.noOverrideData=function(){q.checkOrUncheckAllItemsMultiselectTree(false,q.multiselecttree.items,true,q.existingIds);
if(q.selectedInString.length==0){q.isDepInvalid=true
}$("#overrideModal").modal("hide");
o.removeModalOpenCssAfterModalHide()
};
q.yesOverrideData=function(){$("#overrideModal").modal("hide");
o.removeModalOpenCssAfterModalHide()
};
q.removeWorkflow=function(s,t){var v=function(){q.retrieveAllDepartments();
q.clearData(s,t)
};
var u=function(){var x="Failed to remove workflow. Try again.";
var w=o.failure;
o.addMessage(x,w)
};
e.deleteWorkflow(q.parentId1,v,u);
$("#removeModal").modal("hide");
o.removeModalOpenCssAfterModalHide()
};
q.saveWorkflowDetail=function(s){q.reload=false;
o.maskLoading();
var t=[];
q.submitted=true;
var v="";
angular.forEach(q.list,function(x){v=v+x.key+","
});
if(angular.isDefined(q.selectedItemList)&&q.selectedItemList.items.length>0){q.selectedDepValues=[];
q.iterateChildOfTree(q.selectedItemList.items,true,false,null,true,false);
if(q.selectedDepValues.length>0){angular.forEach(q.selectedDepValues,function(x){t.push(x.id)
})
}q.selectedDepValues=[]
}q.workflowMap={departmentIds:t,approvers:v,leaveworkflowcustom:q.addLeaveWorkflowData,leaveworkflowdbtypes:q.dbType};
var w=function(){if(!o.configureWorkFlow){q.retrieveAllDepartments()
}q.clearData(s,undefined);
o.unMaskLoading()
};
var u=function(){var y="Failed to save wokflow detail. Try again.";
var x=o.failure;
o.addMessage(y,x);
o.unMaskLoading()
};
e.createWorkflow(q.workflowMap,w,u)
};
q.updateWorkflowDetail=function(s){o.maskLoading();
q.submitted=true;
var u="";
angular.forEach(q.list,function(w){u=u+w.key+","
});
q.workflowMap={departmentId:q.parentId1.toString(),approvers:u,leaveworkflowcustom:q.addLeaveWorkflowData,leaveworkflowdbtypes:q.dbType};
var v=function(){q.retrieveAllDepartments();
q.clearData(undefined,s);
o.unMaskLoading()
};
var t=function(){var x="Failed to update workflow. Try again.";
var w=o.failure;
o.addMessage(x,w);
o.unMaskLoading()
};
e.updateWorkflow(q.workflowMap,v,t)
};
q.iterateChildOfTree=function(y,t,s,z,x,w){var v=y;
for(var u=0;
u<v.length;
u++){if(t&&v[u].isChecked){if(s){if(v[u].id==z){if(w){q.selectedDepValues.push({id:v[u].id,text:v[u].text})
}else{if(x){q.selectedDepValues.push({id:v[u].id})
}else{q.selectedDepValues.push({text:v[u].text})
}}}}else{if(w){q.selectedDepValues.push({id:v[u].id,text:v[u].text})
}else{if(x){q.selectedDepValues.push({id:v[u].id})
}else{q.selectedDepValues.push({text:v[u].text})
}}}}if(v[u].items){q.iterateChildOfTree(v[u].items,t,s,z,x,w)
}}};
q.checkOrUncheckAllItemsMultiselectTree=function(u,t,s,w){t.isChecked=u;
if(t){for(var v=0;
v<t.length;
v++){if(!s){t[v].isChecked=u
}else{angular.forEach(w,function(x){if(x.id==t[v].id){t[v].isChecked=u;
angular.forEach(q.selectedInStringIds,function(z,y){if(x.id==z){q.selectedInStringIds.splice(y,1);
q.selectedInString.splice(y,1)
}})
}})
}if(t[v].items){q.checkOrUncheckAllItemsMultiselectTree(u,t[v].items,s,w)
}}}};
function i(t){if(t===null||t===undefined){return
}q.existingIds.push({id:t.id});
if(t.children===null||t.children.length===0){return
}else{for(var s=0;
s<t.children.length>0;
s++){i(t.children[s])
}}}q.getSearchedDepartment=function(t){var s=$("#scrollable-dropdown-menu.typeahead").typeahead("val");
if(s.length>0){if(s.length<3){q.searchRecords=[]
}else{q.searchRecords=[];
angular.forEach(t,function(u){q.searchRecords.push(u)
})
}q.setViewFlag("search")
}};
q.setViewFlag=function(s){if(s!=="search"){$("#scrollable-dropdown-menu.typeahead").typeahead("val","")
}q.displayLeaveWorkflowFlag=s
};
o.unMaskLoading()
}])
});