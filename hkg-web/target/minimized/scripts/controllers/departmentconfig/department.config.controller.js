define(["hkg","accordionCollapse","ruleService","designationService","departmentConfigService","ruleTemplate","designationTemplate","departmentFlowService"],function(a){a.register.controller("DeptConfigController",["$rootScope","$scope","RuleService","Designation","DepartmentConfigService","DepartmentFlowService","$filter","$q","$location",function(m,n,h,o,d,j,k,i,f){m.maskLoading();
n.config={};
n.loadMultiselect=false;
m.mainMenu="manageLink";
m.childMenu="configureDepartment";
m.activateMenu();
n.entity="CONFIG_DEPT.";
n.convertTreeviewToNormalList=function g(p){angular.forEach(p,function(r){if(r.children===null||r.children===undefined){n.departmentListToSelect.push(r)
}else{var q=angular.copy(r);
q.children=null;
n.departmentListToSelect.push(q);
g(r.children)
}})
};
n.initializePage=function(){m.maskLoading();
n.isDepartmentSelected=false;
n.loadMultiselect=false;
n.selectedDepartment=undefined;
n.config={};
n.config.flowMode="A";
n.config.status="A";
n.config.configSubmitted=false;
n.config.designationList=[];
n.associatedDeptListEntity=undefined;
n.config.displaySearchedDepartment="view";
n.config.ruleSet=[];
n.mediums=[{id:"Rough",text:"Rough"},{id:"Lot",text:"Lot"},{id:"Packet",text:"Packet"}];
if(n.configForm!==undefined&&n.configForm!==null){n.configForm.$setPristine()
}d.retrieveAllDepartments(function(p){m.unMaskLoading();
n.departmentList=p;
n.departmentListToSelect=[];
n.convertTreeviewToNormalList(p);
n.possibleAssociatedDepartments=angular.copy(n.departmentListToSelect);
n.loadMultiselect=true
},function(p){m.unMaskLoading()
});
j.retrieveDataForProcessFlow(function(p){n.departmentFlowGraph=p.data;
if(f.path()==="/printflow"){n.initDepartmentFlow()
}},function(p){})
};
$(window).resize(function(){setTimeout(function(){var p=document.getElementById("springydemo");
if(p!==null&&p!==undefined){if(p.width!==$("#springydemo").parent().width()){p.width=$("#springydemo").parent().width()
}}},300)
});
n.initDepartmentFlow=function(){n.config.displaySearchedDepartment="flow";
setTimeout(function(){var p=document.getElementById("springydemo");
p.height=500;
p.width=$("#springydemo").parent().width();
setTimeout(function(){var q=new Springy.Graph();
jQuery(function(){q.loadJSON(n.departmentFlowGraph);
var r=window.springy=jQuery("#springydemo").springy({graph:q})
})
})
},400)
};
function l(p,r){if(r!==undefined&&r!==null&&r.length>0){var q={};
angular.forEach(r,function(t){var s={designation:t.designation,feature:t.feature,searchFlag:t.searchFlag,parentViewFlag:t.parentViewFlag,readonlyFlag:t.readonlyFlag,editableFlag:t.editableFlag,isRequired:t.isRequired,sequenceNo:t.sequenceNo,entity:t.entityName,field:t.fieldId,sectionCode:t.sectionCode};
if(q[t.feature]===undefined||q[t.feature]===null){q[t.feature]=[]
}q[t.feature].push(s)
});
angular.forEach(q,function(t,s){angular.forEach(p.features,function(u){if(u.id==s){u.configure=true
}})
});
p.featureFieldMap=angular.copy(q)
}}function c(r,p){if(p!==null&&p!==undefined){var q={};
angular.forEach(p,function(s){if(s.designation===r.id){q[s.feature]=s
}});
return q
}else{return{}
}}n.fillEditMode=function(q){if(q!==null&&q!==undefined){n.loadDefaultRuleTemplate=false;
n.loadMultiselect=false;
if(q.ruleList!==null&&q.ruleList!==undefined){n.config.ruleSet=q.ruleList
}n.config.configId=q.configId;
n.config.assocStckDept=q.stockRoom;
n.config.flowMode=q.flowMode;
n.config.status=q.status;
n.config.noPhysicalDiamonds=q.noPhysicalDiamonds;
if(q.associatedDepartments!==null&&q.associatedDepartments.length>0){var u=null;
n.associatedDeptListEntity=q.associatedDepartments;
angular.forEach(q.associatedDepartments,function(v){if(u===null){u=v.department+""
}else{u+=","+v.department
}});
n.config.associatedDepartments=u
}if(n.config.designationList.length>0&&q.associatedDesignations!==null&&q.associatedDesignations.length>0){for(var p=0;
p<n.config.designationList.length;
p++){var t=n.config.designationList[p];
for(var r=0;
r<q.associatedDesignations.length;
r++){if(q.associatedDesignations[r].designation===t.id){t.level=q.associatedDesignations[r].level;
l(t,q.associatedDesignations[r].featureFieldPermissionDataBeans);
break
}}var s=c(t,q.roleFeatureModifiers);
t.featureModifierMap=s
}}setTimeout(function(){n.loadMultiselect=true
})
}};
function b(p,r,s,q){if(s===undefined||s===null){s=1
}if(r.indexOf(p)>-1){s=b(q[p],r,s+1,q)
}return s
}n.editDepartment=function(p){if(p.currentNode===null||p.currentNode===undefined){return
}if(n.selectedDepartment!==undefined&&n.selectedDepartment.id===p.currentNode.id){return
}n.isDepartmentSelected=true;
n.selectedDepartment=p.currentNode;
n.config={};
n.config.flowMode="A";
n.config.status="A";
n.config.configSubmitted=false;
n.config.designationList=[];
n.associatedDeptList=[];
n.associatedDeptListEntity=undefined;
n.config.ruleSet=[];
n.config.associatedDepartments="";
$("#scrollable-dropdown-menu.typeahead").typeahead("val","");
n.config.displaySearchedDepartment="view";
n.customLabel="";
var q=n.retrieveAllSysFeatures();
q.then(function(){m.maskLoading();
d.retrieveDesignationByDept(n.selectedDepartment.id,function(s){m.unMaskLoading();
var t=[];
var r={};
if(s!==undefined&&s!==null){angular.forEach(s,function(u){t.push(parseInt(u.value));
r[u.value]=u.otherId
})
}o.retrieveFeaturesSelectedForDesignation(t,function(u){if(u.data!==null&&u.data!==undefined){}else{u.data={}
}angular.forEach(s,function(x){var w=b(x.otherId,t,null,r);
var y={id:x.value,name:x.label,level:w,features:u.data[x.value],featureFieldMap:{},featureModifierMap:{}};
angular.forEach(y.features,function(v){v.checked=true
});
n.config.designationList.push(y)
});
d.retrieveConfigDetailByDepId(n.selectedDepartment.id,function(v){n.fillEditMode(v.data)
},function(v){m.addMessage("Some error occured, please try again",m.failure)
})
})
},function(r){m.unMaskLoading()
})
});
n.possibleAssociatedDepartments=[];
angular.forEach(n.departmentListToSelect,function(r){if(r.id!==n.selectedDepartment.id){n.possibleAssociatedDepartments.push(r)
}})
};
n.autoCompleteAssociteDept={allowClear:true,multiple:true,placeholder:"Select associated department",initSelection:function(q,s){if(n.config.associatedDepartments!==undefined&&n.config.associatedDepartments!==null&&n.config.associatedDepartments.length>0){if(n.config.associatedDepartments instanceof Object&&angular.isArray(n.config.associatedDepartments)){}else{var r=[];
var p=n.config.associatedDepartments.split(",");
angular.forEach(p,function(t){angular.forEach(n.possibleAssociatedDepartments,function(u){if(u.id==t){r.push({id:u.id,text:u.displayName})
}})
});
s(r)
}}},query:function(p){n.names=[];
angular.forEach(n.possibleAssociatedDepartments,function(q){if(p.term!==null&&p.term!==""&&q.displayName.toLowerCase().indexOf(p.term.toLowerCase().trim())!==-1){n.names.push({id:q.id,text:q.displayName})
}});
p.callback({results:n.names})
}};
function e(){$("#associatedDept").on("select2-selecting",function(p){angular.forEach(n.possibleAssociatedDepartments,function(q){if(q.id===p.val){if(n.associatedDeptList===null||n.associatedDeptList===undefined){n.associatedDeptList=[]
}n.associatedDeptList.push({id:p.val,name:p.object.text})
}})
});
$("#associatedDept").on("select2-removing",function(p){angular.forEach(n.possibleAssociatedDepartments,function(q){if(q.id===p.val){var s=$.map(n.associatedDeptList,function(u,t){if(u.id===p.val&&u.name===p.choice.text){return t
}});
var r=s[0];
if(r>-1){n.associatedDeptList.splice(r,1)
}}})
})
}n.$watch("config.associatedDepartments",function(u){if(u!==undefined&&u!==null){if(u instanceof Object&&angular.isArray(u)){var p="";
angular.forEach(u,function(v){p+=v.id+","
});
p=p.substring(0,p.length-1);
n.config.associatedDepartments=p
}else{if(u.length>0){var t=u.split(",");
var r=[];
angular.forEach(t,function(v){r.push(parseInt(v))
});
var q=[];
for(var s=n.associatedDeptList.length-1;
s>=0;
s--){if(r.indexOf(n.associatedDeptList[s].id)===-1){n.associatedDeptList.splice(s,1)
}else{q.push(n.associatedDeptList[s].id)
}}angular.forEach(r,function(v){if(q.indexOf(v)===-1){angular.forEach(n.possibleAssociatedDepartments,function(w){if(v===w.id){var y=null;
if(n.associatedDeptListEntity!==undefined){for(var x=0;
x<n.associatedDeptListEntity.length;
x++){if(n.associatedDeptListEntity[x].department===v){y=n.associatedDeptListEntity[x];
break
}}}if(y===null){n.associatedDeptList.push({id:w.id,name:w.displayName,ruleSet:[]})
}else{n.associatedDeptList.push({id:w.id,name:w.displayName,ruleSet:y.rules===null?[]:y.rules,isDefault:y.isDefaultDept===null?undefined:y.isDefaultDept,medium:y.medium})
}}})
}})
}else{n.associatedDeptList=[]
}}}else{n.associatedDeptList=[]
}});
n.hideshow=function(p){if(!document.getElementById){return
}var q=document.getElementById(p);
if(q.style.display==="block"){q.style.display="none"
}else{q.style.display="block"
}};
n.retrieveAllSysFeatures=function(){var p=i.defer();
var q=p.promise;
m.maskLoading();
o.retrieveSystemFeatures(function(r){m.unMaskLoading();
n.systemFeaturesList=r;
if(!n.systemFeaturesList||n.systemFeaturesList===null){n.systemFeaturesList=[]
}else{n.tempSystemFeatureList=[];
n.diamondSystemFeatureList=[];
n.reportSystemFeatureList=[];
angular.forEach(n.systemFeaturesList,function(s){if(s.type==="DMI"||s.type==="DEI"){n.diamondSystemFeatureList.push(s)
}else{if(s.type==="RMI"){n.reportSystemFeatureList.push(s)
}else{n.tempSystemFeatureList.push(s)
}}});
n.systemFeaturesList=[];
n.systemFeaturesList=angular.copy(n.tempSystemFeatureList)
}p.resolve()
},function(){m.unMaskLoading();
p.reject()
});
return q
};
n.$watch("showRightColumn",function(p){if(p===true){$("#right-column").stop().slideDown(700);
$("#left-column").removeClass("col-xs-12");
$("#left-column").addClass("col-xs-9")
}else{$("#right-column").stop().slideUp(500).hide();
$("#left-column").removeClass("col-xs-9");
$("#left-column").addClass("col-xs-12")
}});
n.checkDefaultDepartment=function(p,q){if(p!==undefined&&q===true){angular.forEach(n.associatedDeptList,function(r){r.isDefault=false
});
n.associatedDeptList[p].isDefault=true
}};
n.scrollToElement=function(s){var p=$("html,body"),q=$("#"+s),r=$("#header");
if(q){p.animate({scrollTop:q.offset().top-p.offset().top+p.scrollTop()-r.height()})
}};
n.cancelDefaultWaringPopup=function(){$("#defaultWarningPopup").modal("hide");
m.removeModalOpenCssAfterModalHide()
};
n.cancelDeactivatePopup=function(p){$("#deactivatePopup").modal("hide");
m.removeModalOpenCssAfterModalHide();
if(p===true){}else{n.config.status="A"
}};
n.hideDepartmentFlowPopup=function(){$("#departmentFlowPopup").modal("hide");
m.removeModalOpenCssAfterModalHide();
setTimeout(function(){n.config.displaySearchedDepartment="view"
})
};
n.showDepartmentFlowPopup=function(){$("#departmentFlowPopup").modal("show")
};
$("#departmentFlowPopup").on("hidden.bs.modal",function(p){n.config.displaySearchedDepartment="view"
});
n.checkStatusOfConfig=function(){if(n.config.status==="I"){$("#deactivatePopup").modal("show")
}};
n.checkValidationForm=function(p){var q=true;
angular.forEach(p.$error,function(r){angular.forEach(r,function(s){if(s.$name!=="addRuleForm"&&s.$name!=="configDesignationForm"){q=false
}})
});
return q
};
n.checkParameters=function(t){n.config.configSubmitted=true;
var u=true;
if(t.$valid){u=true
}else{u=n.checkValidationForm(t)
}if(u){var q=null;
if(n.associatedDeptList.length>0){for(var s=0;
s<n.associatedDeptList.length;
s++){if(n.associatedDeptList[s].isDefault===true){q=n.associatedDeptList[s].id;
break
}}}if(q===null&&n.associatedDeptList.length>0){$("#defaultWarningPopup").modal("show")
}else{n.prepareModelDataAndSave(q)
}}else{var p=$("input.ng-invalid, select.ng-invalid, textarea.ng-invalid");
var r;
for(r=0;
r<p.length;
++r){if(p.eq(r).parents(".rule-template").length===0){p.eq(r).focus();
break
}}}};
n.prepareModelDataAndSave=function(p){var s=[];
if(n.associatedDeptList.length>0){angular.forEach(n.associatedDeptList,function(t){var u={department:t.id,medium:t.medium,isDefaultDept:t.isDefault,rules:t.ruleSet};
s.push(u)
})
}var q=[];
var r=[];
if(n.config.designationList.length>0){angular.forEach(n.config.designationList,function(v){var t=v.id;
var w=[];
if(Object.keys(v.featureFieldMap).length>0){angular.forEach(v.featureFieldMap,function(x,y){if(x.length>0){angular.forEach(x,function(B){var z=B.field;
var A={id:t+"-"+y+"-"+B.sectionCode+"-"+z,designation:t,feature:y,searchFlag:B.searchFlag,parentViewFlag:B.parentViewFlag,readonlyFlag:B.readonlyFlag,editableFlag:B.editableFlag,isRequired:B.isRequired,sequenceNo:B.sequenceNo,entityName:B.entity,fieldId:z,sectionCode:B.sectionCode};
w.push(A)
})
}})
}if(Object.keys(v.featureModifierMap).length>0){angular.forEach(v.featureModifierMap,function(x,y){x.designation=t;
x.feature=y;
r.push(x)
})
}var u={designation:v.id,level:v.level,skipAssociatedDepartment:[],featureFieldPermissionDataBeans:w};
q.push(u)
})
}n.configToSend={configId:n.config.configId,department:n.selectedDepartment.id,departmentName:n.selectedDepartment.displayName,stockRoom:n.config.assocStckDept,defaultDepartment:p,flowMode:n.config.flowMode,status:n.config.status,noPhysicalDiamonds:n.config.noPhysicalDiamonds,ruleList:n.config.ruleSet,associatedDepartments:s,associatedDesignations:q,roleFeatureModifiers:r};
m.maskLoading();
d.updateDepartmentConfig(n.configToSend,function(t){m.unMaskLoading();
n.initializePage()
},function(t){m.unMaskLoading()
})
};
n.getSearchedDepartmentRecords=function(q){n.searchRecords=[];
var p=$("#scrollable-dropdown-menu.typeahead").typeahead("val");
if(p.length>0){if(p.length<3){n.searchRecords=[]
}else{if(q!=null&&angular.isDefined(q)&&q.length>0){angular.forEach(q,function(r){r.displayName=m.translateValue("DPT_NM."+r.displayName);
n.searchRecords.push(r)
})
}}n.config.displaySearchedDepartment="search";
n.selectedDepartment=undefined;
n.customLabel="";
if(n.selectedDept.currentNode!==undefined){n.selectedDept.currentNode.selected=undefined
}}};
n.editDepartmentFromSearchBox=function(q){if(q!==undefined&&q!==null){n.selectedDepartment=undefined;
var p=null;
angular.forEach(n.departmentListToSelect,function(r){if(r.id===q){p=r
}});
n.config.displaySearchedDepartment="view";
if(n.selectedDept.currentNode!==undefined){n.selectedDept.currentNode.selected=undefined
}if(p!==null){n.editDepartment({currentNode:p})
}}};
n.initializePage()
}])
});