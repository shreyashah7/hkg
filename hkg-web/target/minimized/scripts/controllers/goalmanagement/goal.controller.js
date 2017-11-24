define(["hkg","goalService","activityFlowService","leaveWorkflowService","designationService","departmentService","addMasterValue","ruleField","dynamicForm"],function(a){a.register.controller("GoalController",["$rootScope","$scope","GoalService","ActivityFlowService","LeaveWorkflow","Designation","DepartmentService","$location",function(c,e,i,g,b,d,f,h){c.maskLoading();
c.mainMenu="manageLink";
c.childMenu="manageGoals";
c.activateMenu();
e.goal={};
e.entity="GOAL.";
e.flag=false;
e.submitted=false;
e.initData=function(){e.displaySearchedGoalTemplate="view";
if(c.clearGoalData===undefined||c.clearGoalData===true){delete c.goalTemplatePayload
}if(!!c.goalTemplatePayload){e.goal.service=c.goalTemplatePayload.service;
e.goal.designation=c.goalTemplatePayload.designation;
e.selectedParent=c.goalTemplatePayload.selectedDepartment;
e.goal.type=c.goalTemplatePayload.type
}c.maskLoading();
i.retrieveGoalPermissionByDesignations({designations:c.session.roleIds},function(k){e.mapOfGoalPermission=k;
if(e.mapOfGoalPermission.S!=null){var j=[];
g.retrieveServicesWithActivity({q:""},function(l){angular.forEach(l.data,function(m){angular.forEach(e.mapOfGoalPermission.S,function(n){if(n==m.nodeId){j.push({id:m.nodeId,text:m.associatedServiceName+"("+m.activityName+")",designationName:m.designationName})
}})
});
e.serviceList=j;
angular.forEach(e.serviceList,function(m){if(m.id==e.goal.service){e.designationName=m.designationName
}})
})
}if(e.mapOfGoalPermission.R!=null){var j=[];
d.retrieveDesignations(function(m){var l=[];
if(m!==null){angular.forEach(e.mapOfGoalPermission.R,function(n){angular.forEach(m,function(o){if(n==o.id){l.push(o)
}})
})
}e.designationList=l
})
}if(e.mapOfGoalPermission.D!=null){f.retrieveDepartmentSimpleTreeView(function(m){var l=[];
if(m!==null){angular.forEach(e.mapOfGoalPermission.D,function(n){angular.forEach(m,function(o){if(n==o.id){l.push(o)
}})
})
}e.departmentList=l
})
}if(e.mapOfGoalPermission.S===null&&e.mapOfGoalPermission.R===null&&e.mapOfGoalPermission.D===null){c.addMessage("Configure goal add edit permission from designation page first  ",c.warning)
}c.unMaskLoading()
},function(){console.log("Failed to retrieve...");
c.unMaskLoading()
});
c.clearGoalData=true
};
e.setSelectedParent=function(j){if(!angular.equals(j,{})){e.selectedParent=j.currentNode;
j.currentNode.displayName=c.translateValue("DPT_NM."+j.currentNode.displayName);
e.retrieveGoalTemplatesByDepartment()
}};
e.clearSelection=function(){e.goal.service="";
e.goal.designation="";
e.selectedDepartmentDropdown.currentNode="";
delete e.selectedParent;
e.clearTreeSelect(e.departmentList)
};
e.clearTreeSelect=function(j){angular.forEach(j,function(k){k.selected=undefined;
if(!!k.children){e.clearTreeSelect(k.children)
}})
};
e.addNewTemplate=function(){var j=e.goal;
j.selectedDepartment=e.selectedParent;
j.goalTemplatesSelected=e.goal.goalTemplates;
c.goalTemplatePayload=j;
e.flag=false;
h.path("/managegoaltemplate")
};
e.$watch("goal.goalTemplates",function(j){console.log("newVal:::::"+JSON.stringify(j));
if(typeof j==="string"){e.goalTemplatesList=[];
if(j!==undefined&&j!==null&&j.length>0){angular.forEach(j.split(","),function(k){angular.forEach(e.allGoalTemplates,function(l){if(k==l.id){e.goalTemplatesList.push(l)
}})
})
}}else{delete e.goalTemplatesList
}},true);
e.autoCompleteApprover={allowClear:true,multiple:true,closeOnSelect:true,placeholder:"Select goal templates",initSelection:function(j,k){if(!e.flag){c.maskLoading();
i.retrieveAllGoalTemplates(function(n){if(!!n){e.allGoalTemplates=n
}var m=[];
var l=[];
e.goalTemplatesList=[];
if(!!c.goalTemplatePayload){angular.forEach(c.goalTemplatePayload.goalTemplatesSelected.split(","),function(o){angular.forEach(e.allGoalTemplates,function(p){if(o==p.id){m.push({id:p.id,text:p.name+", "+p.period+" days, "+p.nameOfAssociation});
l.push(p.id);
e.goalTemplatesList.push(p)
}})
})
}k(m);
e.goal.goalTemplates=l.toString();
c.unMaskLoading()
},function(){console.log("Failed");
c.unMaskLoading()
})
}else{k(e.goalTemplatesValue)
}},formatSelection:function(j){return j.text.split(",")[0]
},query:function(l){var k=l.term;
e.names=[];
var m=function(n){if(n.length===0){l.callback({results:e.names})
}else{angular.forEach(n,function(o){e.names.push({id:o.id,text:o.name+", "+o.period+" days, "+o.nameOfAssociation})
});
l.callback({results:e.names})
}};
var j=function(){};
if(l.term!=null&&l.term!=""){i.retrieveGoalTemplatesBySearch({q:l.term.trim()},m,j)
}}};
e.saveGoals=function(){var j=[];
e.submitted=true;
if(e.goal!==undefined&&e.goal!==null&&e.goal.goalTemplates!==undefined&&e.goal.goalTemplates!==null&&e.goal.goalTemplates.length>=0){angular.forEach(e.goal.goalTemplates.split(","),function(k){angular.forEach(e.allGoalTemplates,function(l){if(k==l.id){if(e.goal.type=="1"){l.for_service=e.goal.service
}else{l.for_service=""
}if(e.goal.type=="2"){l.for_designation=e.goal.designation
}else{l.for_designation=""
}if(e.goal.type=="3"){l.for_department=e.selectedParent.id
}else{l.for_department=""
}j.push(l)
}})
});
c.maskLoading();
i.saveGoalTemplates(j,function(){c.unMaskLoading();
e.goal={};
var l="Goal template configured successfully";
var k=c.success;
c.addMessage(l,k);
c.goalTemplatePayload={};
delete e.selectedParent;
e.selectedDepartmentDropdown.currentNode="";
$("#scrollable-dropdown-menu.typeahead").typeahead("val","");
e.submitted=false;
e.goalForm.$setPristine()
},function(){c.unMaskLoading();
c.addMessage("Problem in configuring goal template",c.failure)
})
}};
e.retrieveGoalTemplatesByService=function(j){angular.forEach(e.serviceList,function(k){if(k.id==e.goal.service){e.designationName=k.designationName
}});
c.maskLoading();
i.retrieveActiveGoalTemplatesByService(e.goal.service,function(m){c.unMaskLoading();
if(!!m){var l=[];
var k=[];
e.goalTemplatesList=[];
angular.forEach(m,function(n){l.push({id:n.id,text:n.name+", "+n.period+" days, "+n.nameOfAssociation});
k.push(n.id);
e.goalTemplatesList.push(n)
});
e.flag=true;
e.goalTemplatesValue=l;
$("#goalTemplates").select2("val",l);
e.goal.goalTemplates=k.toString()
}},function(){c.unMaskLoading();
console.log("Fialure")
})
};
e.retrieveGoalTemplatesByDesignation=function(){c.maskLoading();
i.retrieveActiveGoalTemplatesByDesignation(e.goal.designation,function(l){c.unMaskLoading();
if(!!l){var k=[];
var j=[];
e.goalTemplatesList=[];
angular.forEach(l,function(m){k.push({id:m.id,text:m.name+", "+m.period+" days, "+m.nameOfAssociation});
j.push(m.id);
e.goalTemplatesList.push(m)
});
e.flag=true;
e.goalTemplatesValue=k;
$("#goalTemplates").select2("val",k);
e.goal.goalTemplates=j.toString()
}},function(){c.unMaskLoading();
console.log("Fialure")
})
};
e.retrieveGoalTemplatesByDepartment=function(){c.maskLoading();
i.retrieveActiveGoalTemplatesByDepartment(e.selectedParent.id,function(l){c.unMaskLoading();
if(!!l){var k=[];
var j=[];
e.goalTemplatesList=[];
angular.forEach(l,function(m){k.push({id:m.id,text:m.name+", "+m.period+" days, "+m.nameOfAssociation});
j.push(m.id);
e.goalTemplatesList.push(m)
});
e.flag=true;
e.goalTemplatesValue=k;
$("#goalTemplates").select2("val",k);
e.goal.goalTemplates=j.toString()
}},function(){c.unMaskLoading();
console.log("Fialure")
})
};
e.editGoalTemplate=function(k){var j=e.goal;
j.selectedDepartment=e.selectedParent;
j.goalTemplatesSelected=e.goal.goalTemplates;
j.editGoalTemplateId=k;
c.goalTemplatePayload=j;
e.flag=false;
h.path("/managegoaltemplate")
};
e.editFromSearch=function(j){c.maskLoading();
i.retrieveGoalTemplateById(j,function(m){c.unMaskLoading();
e.goal={};
if(!!m){if(!!m.for_service){e.goal.type=1;
e.goal.service=m.for_service;
e.retrieveGoalTemplatesByService()
}if(!!m.for_designation){e.goal.type=2;
e.goal.designation=m.for_designation;
e.retrieveGoalTemplatesByDesignation()
}if(!!m.for_department){e.goal.type=3;
var l={};
for(var k=0;
k<e.departmentList.length;
k++){l=e.searchInDepartment(e.departmentList[k],m.for_department);
if(!!m){break
}}var n={currentNode:l};
e.selectedDepartmentDropdown=n;
e.setSelectedParent(e.selectedDepartmentDropdown);
e.retrieveGoalTemplatesByDepartment()
}}},function(){c.unMaskLoading();
console.log("Failed..")
})
};
e.searchInDepartment=function(j,k){if(j.id==k){j.selected=true;
return j
}else{if(!!j.children){return e.searchInDepartment(j.children,k)
}}};
e.getSearchedDesignationRecords=function(k){e.searchRecords=[];
var j=$("#scrollable-dropdown-menu.typeahead").typeahead("val");
if(j.length>0){if(j.length<3){e.searchRecords=[]
}else{if(k!==null&&angular.isDefined(k)&&k.length>0){angular.forEach(k,function(l){e.searchRecords.push(l)
})
}}e.displaySearchedGoalTemplate="search"
}};
e.editGoalTemplateFromSearch=function(j){e.displaySearchedGoalTemplate="view";
e.editFromSearch(j)
};
e.cancelGoal=function(){e.displaySearchedGoalTemplate="view";
e.goal={};
e.selectedDepartmentDropdown.currentNode="";
delete e.selectedParent;
e.clearTreeSelect(e.departmentList);
$("#scrollable-dropdown-menu.typeahead").typeahead("val","");
e.goalForm.$setPristine()
};
c.unMaskLoading()
}])
});