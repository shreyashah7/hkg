define(["hkg","departmentService","addMasterValue","dynamicForm"],function(a,b){a.register.controller("DepartmentController",["$rootScope","$scope","$http","DepartmentService","$filter","DynamicFormService","$timeout",function(d,f,j,h,i,e,g){f.searchRecords=[];
d.maskLoading();
d.mainMenu="manageLink";
d.childMenu="manageDepartment";
d.activateMenu();
f.entity="DEPARTMENTS.";
f.hasDepartmentAccess=d.canAccess("departmentAddEdit");
f.msgForDep="";
f.dbType={};
f.$watch("departmentTitle",function(k){console.log("watching1...."+k)
});
f.$watch("departmentTitle",function(k){console.log("watching2...."+k)
});
f.initializePage=function(){$("#scrollable-dropdown-menu.typeahead").typeahead("val","");
f.submitted=false;
f.departmentListOption=[];
f.isEditMode="false";
f.showSameDeptIdError=false;
f.statusDepartment="true";
f.displaySearchedDepartment="view";
f.departmentParent="";
f.departmentTitle="";
f.reload=true;
f.departmentName="";
f.departmentDescription="";
f.shiftRotationDays;
f.parentId=0;
f.selectedDept;
f.msgForDep="";
f.addDepartmentData={};
f.listOfModelsOfDateType=[];
f.selectedParent={id:"0",displayName:"None"};
var k=e.retrieveSectionWiseCustomFieldInfo("manageDepartment");
k.then(function(l){f.customTemplateDate=angular.copy(l.genralSection);
f.genralDepartmentTemplate=d.getCustomDataInSequence(f.customTemplateDate);
if(f.genralDepartmentTemplate!==null&&f.genralDepartmentTemplate!==undefined){angular.forEach(f.genralDepartmentTemplate,function(m){if(m.type!==null&&m.type!==undefined&&m.type==="date"){f.listOfModelsOfDateType.push(m.model)
}})
}},function(l){},function(l){});
f.initAddDepartmentForm=function(l){f.addDepartmentForm=l
};
d.maskLoading();
h.retrieveDepartment(function(m){console.log("data.."+JSON.stringify(m));
f.departmentList=[];
if(m!=null&&angular.isDefined(m)&&m.length>0){angular.forEach(m,function(n){f.departmentList.push(n)
})
}f.setRootOption();
f.addDepartmentForm.$setPristine();
for(var l=0;
l<f.departmentList.length;
l++){c(f.departmentList[l])
}if(f.selectedDepartment!==undefined&&f.selectedDepartment.currentNode!==undefined){f.selectedDepartment.currentNode=undefined
}f.addDepartmentForm.$dirty=false;
d.unMaskLoading()
},function(){d.unMaskLoading();
var m="Failed to retrieve department";
var l=d.error;
d.addMessage(m,l)
})
};
f.resetCustomFields=function(){f.addDepartmentData={};
var k=e.retrieveSectionWiseCustomFieldInfo("manageDepartment");
k.then(function(l){f.customTemplateDate=angular.copy(l.genralSection);
f.genralDepartmentTemplate=d.getCustomDataInSequence(f.customTemplateDate)
},function(l){},function(l){})
};
f.addDataInDepartment=function(){var k=0;
f.reload=false;
$("#scrollable-dropdown-menu.typeahead").typeahead("val","");
f.submitted=false;
if(angular.isDefined(f.selectedDepartment.currentNode)){f.selectedDepartment.currentNode.selected=undefined
}f.selectedParent={id:"0",displayName:"None"};
f.departmentListOption=[];
f.isEditMode=false;
f.showSameDeptIdError=false;
f.displaySearchedDepartment="view";
f.departmentParent="";
f.departmentTitle="";
f.departmentDescription="";
f.shiftRotationDays=null;
f.parentId=0;
f.selectedDept;
g(function(){f.resetCustomFields();
f.reload=true
},50);
f.setRootOption();
f.addDepartmentForm.$setPristine();
for(var k=0;
k<f.departmentList.length;
k++){c(f.departmentList[k])
}};
f.departmentListOption=[];
function c(l){if(l===null||l===undefined){return
}f.departmentListOption.push({id:l.id,displayName:l.displayName,description:l.description,children:l.children,parentId:l.parentId,parentName:l.parentName,companyId:l.companyId,isActive:l.isActive,showAllDepartment:l.showAllDepartment,addDepartmentData:l.departmentCustom});
if(l.children===null||l.children.length===0){return
}else{for(var k=0;
k<l.children.length>0;
k++){c(l.children[k])
}}}f.setRootOption=function(){f.departmentListDropdown=[];
f.departmentListDropdown.push({id:"0",displayName:"None"});
f.departmentListDropdown.displayName=d.translateValue("DPT_NM."+f.departmentListDropdown.displayName);
$.merge(f.departmentListDropdown,angular.copy(f.departmentList))
};
f.setSelectedParent=function(k){f.$on("$locationChangeStart",function(l){if(k!==null){if(!confirm("You have unsaved changes, go back?")){l.preventDefault()
}else{k===null
}}});
if(!angular.equals(k,{})){f.invalidParent=false;
f.selectedParent=k.currentNode;
if(f.selectedParent.displayName.length>50){f.selectedParent.displayName=f.selectedParent.displayName.substring(0,32).concat("---")
}f.parentId=k.currentNode.id
}};
f.addDepartment=function(){console.log("in add dep");
f.reload=false;
f.submitted=true;
console.log("add Dep error"+f.addDepartmentForm.$valid);
if(f.addDepartmentForm.$valid&&angular.isDefined(f.showSameDeptIdError)&&!f.showSameDeptIdError){var k={displayName:f.departmentTitle,parentId:f.parentId,description:f.departmentDescription,departmentCustom:f.addDepartmentData,shiftRotationDays:f.shiftRotationDays,dbType:f.dbType};
d.maskLoading();
h.addDepartment(k,function(l){d.unMaskLoading();
f.id=l.primaryKey;
if(l.result===true){var n=f.departmentTitle+" already exists";
var m=d.warning;
d.addMessage(n,m)
}else{f.submitted=false;
f.addDepartmentForm.$setPristine();
f.departmentParent=0;
f.departmentTitle="";
f.departmentDescription="";
f.shiftRotationDays=null;
f.selectedParent={id:"0",displayName:"None"};
f.departmentListOption=[];
f.initializePage();
f.reload=true
}},function(){d.unMaskLoading();
var m=" Department could not be added. Try again";
var l=d.error;
d.addMessage(m,l);
f.reload=true
})
}else{f.reload=true
}};
f.editDepartment=function(k){if(k.currentNode.isDefaultDep!==null&&k.currentNode.isDefaultDep===true){f.isDefaultDep=true
}else{f.isDefaultDep=false
}f.reload=false;
if(!angular.equals(k,{})){f.showSameDeptIdError=false;
f.addDepartmentForm.$invalid=false;
if(f.selectedDepartment.currentNode!==undefined){if(f.selectedDepartment.currentNode.id!==f.departmentList[0].id){f.departmentList[0].selected=undefined
}}f.department=angular.copy(f.selectedDepartment.currentNode);
h.addCustomDataToDepartmentDataBean(angular.copy(f.department),function(l){f.resetCustomFields();
f.addDepartmentData=angular.copy(l.departmentCustom);
if(!!f.addDepartmentData){angular.forEach(f.listOfModelsOfDateType,function(m){if(f.addDepartmentData.hasOwnProperty(m)){if(f.addDepartmentData[m]!==null&&f.addDepartmentData[m]!==undefined){f.addDepartmentData[m]=new Date(f.addDepartmentData[m])
}else{f.addDepartmentData[m]=""
}}})
}f.reload=true
});
f.isEditMode=true;
$("#scrollable-dropdown-menu.typeahead").typeahead("val","");
f.displaySearchedDepartment="view";
f.invalidParent=false;
if(angular.isDefined(f.department)){f.selectedParent.id=f.department.parentId;
f.selectedParent.displayName=f.department.parentName;
if(f.selectedParent.displayName.length>35){f.selectedParent.displayName=f.selectedParent.displayName.substring(0,35)
}f.departmentParent=f.department.parentId;
f.departmentTitle=f.department.deptName;
f.departmentName=angular.copy(f.departmentTitle);
f.parentId=f.department.parentId;
f.departmentDescription=f.department.description;
f.shiftRotationDays=f.department.shiftRotationDays;
f.statusDepartment=f.department.isActive.toString()
}f.departmentListDropdown=[];
f.departmentListDropdown.push({id:"0",displayName:"None"});
$.merge(f.departmentListDropdown,angular.copy(f.departmentList));
f.newDeptList=angular.copy(f.departmentListDropdown);
f.deleteSelectedNode(f.newDeptList);
if(angular.isDefined(f.addDepartmentForm)){f.addDepartmentForm.$setPristine()
}}};
f.deleteSelectedNode=function(m){if(m===null||m===undefined){return
}for(var l=0;
l<m.length;
l++){if(angular.isDefined(f.selectedDepartment.currentNode)&&f.selectedDepartment.currentNode.id===m[l].id){m.splice(l,1)
}else{for(var k=0;
k<m.length;
k++){if(m[k].children!==null){f.deleteSelectedNode(m[k].children)
}}}}f.departmentListDropdown=angular.copy(m)
};
f.editDepartmentFromSearchBox=function(k){if(!f.hasDepartmentAccess){alert("You Dont have right to access this feature")
}f.retrieveById(k);
f.newDeptList=angular.copy(f.departmentListDropdown);
f.deleteSelectedNode(f.newDeptList)
};
f.retrieveById=function(l){for(var k=0;
k<f.departmentListOption.length;
k++){if(l==f.departmentListOption[k].id){f.showSameDeptIdError=false;
f.addDepartmentForm.$invalid=false;
f.displaySearchedDepartment="view";
f.isEditMode=true;
f.invalidParent=false;
var m={id:l};
h.addCustomDataToDepartmentDataBean(m,function(n){f.addDepartmentData=n.departmentCustom;
if(!!f.addDepartmentData){angular.forEach(f.listOfModelsOfDateType,function(o){if(f.addDepartmentData.hasOwnProperty(o)){if(f.addDepartmentData[o]!==null&&f.addDepartmentData[o]!==undefined){f.addDepartmentData[o]=new Date(f.addDepartmentData[o])
}else{f.addDepartmentData[o]=""
}}})
}});
f.selectedDepartment.currentNode=angular.copy(f.departmentListOption[k]);
f.department=angular.copy(f.selectedDepartment.currentNode);
f.selectedParent.id=f.department.parentId;
f.selectedParent.displayName=f.department.parentName;
f.departmentParent=f.department.parentId;
f.departmentTitle=f.department.displayName;
f.departmentName=angular.copy(f.departmentTitle);
f.lastSelectedId=angular.copy(f.parentId);
f.parentId=f.department.parentId;
f.departmentDescription=f.department.description;
f.shiftRotationDays=f.department.shiftRotationDays;
f.statusDepartment=f.department.isActive.toString();
f.departmentListDropdown=[];
f.departmentListDropdown.push({id:"0",displayName:"None"});
$.merge(f.departmentListDropdown,angular.copy(f.departmentList))
}}};
f.onCancel=function(){f.reload=false;
$("#scrollable-dropdown-menu.typeahead").typeahead("val","");
f.addDepartmentForm.departmentTitle.$invalid=false;
f.submitted=false;
f.showSameDeptIdError=false;
f.searchDepName=null;
f.searchDepName="";
f.isEditMode=false;
f.invalidParent=false;
f.departmentParent=0;
f.parentId=0;
f.departmentTitle="";
f.departmentDescription="";
f.shiftRotationDays=null;
g(function(){f.resetCustomFields();
f.reload=true
},50);
f.setRootOption();
f.selectedParent={id:"0",displayName:"None"};
f.addDepartmentForm.$setPristine();
if(f.selectedDepartment.currentNode!==undefined){f.selectedDepartment.currentNode.selected=undefined
}};
f.saveDepartment=function(){console.log("save "+JSON.stringify(f.addDepartmentForm.$error));
f.submitted=true;
if(f.addDepartmentForm.$valid){if(f.statusDepartment==="false"){$("#departmentModal").modal("show")
}else{f.updateDepartment()
}f.addDepartmentForm.$setPristine()
}};
f.cancelRemove=function(){f.statusDepartment="true";
$("#departmentModal").modal("hide");
d.removeModalOpenCssAfterModalHide();
$("#departmentActiveUserModal").modal("hide");
d.removeModalOpenCssAfterModalHide();
$("#depRemoveModal").modal("hide");
d.removeModalOpenCssAfterModalHide()
};
f.updateDepartment=function(){f.reload=false;
f.submitted=true;
f.shouldUpdate=false;
if(!f.addDepartmentForm.$invalid&&angular.isDefined(f.showSameDeptIdError)&&!f.showSameDeptIdError){if(f.department.id!==f.parentId){var k={primaryKey:f.department.id};
var l={id:f.department.id,displayName:f.departmentTitle,parentId:f.parentId,description:f.departmentDescription,shiftRotationDays:f.shiftRotationDays,isActive:f.statusDepartment,departmentCustom:f.addDepartmentData,dbType:f.dbType};
$("#departmentModal").modal("hide");
d.removeModalOpenCssAfterModalHide();
$("#departmentActiveUserModal").modal("hide");
d.removeModalOpenCssAfterModalHide();
if(f.statusDepartment==="false"){d.maskLoading();
h.checkIfDepartmentIsPresentInAnyFeature(k,function(n){console.log("response..."+JSON.stringify(n));
console.log("ddd"+jQuery.isEmptyObject(n.data));
if(jQuery.isEmptyObject(n.data)){console.log("empty then reove   ");
h.deleteDepartment(k,function(){f.initializePage();
d.unMaskLoading()
},function(){var s="Department could not be removed. Try again.";
var r=d.error;
d.addMessage(s,r);
d.unMaskLoading()
})
}else{var m=["Shift","Department Configuration","Employees","Designation"];
var q=[];
for(var o in n.data){if(o==="Shift"){q.push(o)
}else{if(o==="Department Configuration"){q.push(o)
}else{if(o==="Employees"){q.push(o)
}else{if(o==="Designation"){q.push(o)
}}}}}f.depInFeature=q;
var p=[];
$.grep(m,function(r){if($.inArray(r,q)===-1){p.push(r)
}});
f.remainingFeature=p;
if(f.remainingFeature===null||f.remainingFeature===undefined||f.remainingFeature.length===0){f.msgForDep="Could not remove department as this department is associated with "+f.depInFeature
}else{f.msgForDep="Could not remove department as this department is associated with "+f.depInFeature+" , please verify "+f.remainingFeature+" also"
}$("#depRemoveModal").modal("show");
console.log("remaining "+f.msgForDep)
}d.unMaskLoading()
})
}else{h.updateDepartment(l,function(m){d.unMaskLoading();
if(f.selectedDepartment.currentNode.displayName!==f.departmentTitle){var o=f.departmentTitle+" already exists";
var n=d.warning
}else{f.submitted=false;
f.addDepartmentForm.$setPristine();
f.onCancel()
}f.initializePage();
f.reload=true
},function(){d.unMaskLoading();
var n=" Department could not be saved. Try again";
var m=d.error;
d.addMessage(n,m)
})
}}else{f.invalidParent=true
}}};
f.checkDepartmentNameExist=function(l){for(var k=0;
k<f.departmentListOption.length;
k++){if(f.selectedDepartment.currentNode!==undefined&&f.departmentTitle!==undefined&&f.selectedDepartment.currentNode.displayName.toUpperCase()===f.departmentTitle.toUpperCase()){f.showSameDeptIdError=false;
f.addDepartmentForm.$invalid=false;
if(!f.isEditMode){f.showSameDeptIdError=true;
f.addDepartmentForm.$invalid=true
}}else{if(l!==undefined){if(l.toUpperCase()===f.departmentListOption[k].displayName.toUpperCase()){f.showSameDeptIdError=true;
f.addDepartmentForm.$invalid=true;
break
}else{f.showSameDeptIdError=false;
f.addDepartmentForm.$invalid=false
}}}}};
f.getSearchedDepartmentRecords=function(l){f.searchRecords=[];
var k=$("#scrollable-dropdown-menu.typeahead").typeahead("val");
if(k.length>0){if(k.length<3){f.searchRecords=[]
}else{if(l!=null&&angular.isDefined(l)&&l.length>0){angular.forEach(l,function(m){m.displayName=d.translateValue("DPT_NM."+m.displayName);
f.searchRecords.push(m)
})
}}f.isEditMode=true;
f.displaySearchedDepartment="search";
f.searchDepName=null;
f.searchDepName=""
}};
f.selectedDepartment={};
d.unMaskLoading()
}])
});