define(["hkg","accordionCollapse","ruleService","designationService","designationConfigService","ruleTemplate","designationTemplate","departmentFlowService"],function(a){a.register.controller("DesignConfigController",["$rootScope","$scope","Designation","DesignationConfigService","$filter","$q","$location",function(h,i,j,b,f,e,d){h.maskLoading();
h.mainMenu="manageLink";
h.childMenu="configureDesignation";
h.activateMenu();
i.entity="CONFIG_DESIGN.";
i.initializePage=function(){h.maskLoading();
i.selectedDesignation=undefined;
i.designation=undefined;
b.retrieveAllDesignations(function(k){h.unMaskLoading();
i.designationList=k
},function(k){h.unMaskLoading()
})
};
i.retrieveReportGroupNames=function(){i.groupIds=[];
if(i.reportSysFeatures!==undefined){for(var l=0;
l<i.reportSysFeatures.length;
l++){if(i.reportSysFeatures[l].description!==null&&i.reportSysFeatures[l].description!==""&&i.reportSysFeatures[l].type==="RMI"){var k=false;
angular.forEach(i.groupIds,function(m){if(i.reportSysFeatures[l].description===m){k=true
}});
if(!k){i.groupIds.push(i.reportSysFeatures[l].description)
}}}}j.retrieveReportGroupNames(i.groupIds,function(p){i.groupFeatureMap=[];
i.defaultFeatures=[];
i.reportGroupIdNameMap=p.data;
if(!!p.data){for(var o in i.reportGroupIdNameMap){i.groupFeatures=[];
for(var n=0;
n<i.reportSysFeatures.length;
n++){var m=false;
if(i.reportSysFeatures[n].description!==null&&i.reportSysFeatures[n].type==="RMI"){if(i.reportSysFeatures[n].description===o){i.groupFeatures.push(i.reportSysFeatures[n])
}}else{angular.forEach(i.defaultFeatures,function(q){if(q.id===i.reportSysFeatures[n].id){m=true
}});
if(!m){i.defaultFeatures.push(i.reportSysFeatures[n])
}}}i.groupFeatureMap.push({groupName:i.reportGroupIdNameMap[o],features:i.groupFeatures})
}}else{for(var n=0;
n<i.reportSysFeatures.length;
n++){var m=false;
if(i.reportSysFeatures[n].description!==null&&i.reportSysFeatures[n].type==="RMI"){}else{angular.forEach(i.defaultFeatures,function(q){if(q.id===i.reportSysFeatures[n].id){m=true
}});
if(!m){i.defaultFeatures.push(i.reportSysFeatures[n])
}}}}if(i.defaultFeatures!==undefined&&i.defaultFeatures!==null&&i.defaultFeatures.length>0){i.groupFeatureMap.push({groupName:"Default",features:i.defaultFeatures})
}},function(){})
};
i.configureDesignation=function(k){if(k.currentNode===null||k.currentNode===undefined||(k.currentNode.parentId!==null&&k.currentNode.parentId!==undefined)){return
}if(i.selectedDesignation!==undefined&&i.selectedDesignation.id===k.currentNode.id){return
}i.isDepartmentSelected=true;
i.selectedDesignation=k.currentNode;
i.customLabel="";
var l=i.retrieveAllSysFeatures();
l.then(function(){h.maskLoading();
var m=[];
m.push(i.selectedDesignation.id);
j.retrieveFeaturesSelectedForDesignation(m,function(o){if(o.data!==null&&o.data!==undefined){}else{o.data={}
}var n=[];
i.nonDiamondSysFeatures=[];
i.diamondSysFeatures=[];
i.reportSysFeatures=[];
i.designation={id:i.selectedDesignation.id,name:i.selectedDesignation.displayName,features:o.data[i.selectedDesignation.id],featureFieldMap:{},featureModifierMap:{}};
angular.forEach(i.designation.features,function(q){if(q.type=="DMI"){q.checked=true;
i.diamondSysFeatures.push(q)
}if(q.type=="MI"){i.nonDiamondSysFeatures.push(q)
}if(q.type=="RMI"){i.reportSysFeatures.push(q)
}n.push(q.id)
});
i.retrieveReportGroupNames();
var p={featureIds:n,designationId:i.selectedDesignation.id};
b.retrieveDesignationConfiguration(p,function(q){g(i.designation,q.featureFieldPermissionDataBeans);
var r=c(i.designation,q.roleFeatureModifiers);
i.designation.featureModifierMap=r;
h.unMaskLoading()
},function(q){h.addMessage("Some error occured, please try again",h.failure);
h.unMaskLoading()
})
})
})
};
function c(m,k){if(k!==null&&k!==undefined){var l={};
angular.forEach(k,function(n){if(n.designation===m.id){l[n.feature]=n
}});
return l
}else{return{}
}}function g(k,m){if(m!==undefined&&m!==null&&m.length>0){var l={};
angular.forEach(m,function(o){var n={designation:o.designation,feature:o.feature,searchFlag:o.searchFlag,parentViewFlag:o.parentViewFlag,readonlyFlag:o.readonlyFlag,editableFlag:o.editableFlag,isRequired:o.isRequired,sequenceNo:o.sequenceNo,entity:o.entityName,field:o.fieldId,sectionCode:o.sectionCode};
if(l[o.feature]===undefined||l[o.feature]===null){l[o.feature]=[]
}l[o.feature].push(n)
});
angular.forEach(l,function(o,n){angular.forEach(k.features,function(p){if(p.id==n){p.configure=true
}})
});
k.featureFieldMap=angular.copy(l)
}}i.retrieveAllSysFeatures=function(){var k=e.defer();
var l=k.promise;
h.maskLoading();
j.retrieveSystemFeatures(function(m){h.unMaskLoading();
i.systemFeaturesList=m;
if(!i.systemFeaturesList||i.systemFeaturesList===null){i.systemFeaturesList=[]
}else{i.tempSystemFeatureList=[];
i.diamondSystemFeatureList=[];
i.reportSystemFeatureList=[];
angular.forEach(i.systemFeaturesList,function(n){if(n.type==="DMI"||n.type==="DEI"){i.diamondSystemFeatureList.push(n)
}else{if(n.type==="RMI"){i.reportSystemFeatureList.push(n)
}else{i.tempSystemFeatureList.push(n)
}}});
i.systemFeaturesList=[];
i.systemFeaturesList=angular.copy(i.tempSystemFeatureList)
}k.resolve()
},function(){h.unMaskLoading();
k.reject()
});
return l
};
i.checkValidationForm=function(k){var l=true;
angular.forEach(k.$error,function(m){angular.forEach(m,function(n){if(n.$name!=="configDesignationForm"){l=false
}})
});
return l
};
i.checkParameters=function(k){var l=true;
if(k.$valid){l=true
}else{l=i.checkValidationForm(k)
}if(l){i.prepareModelDataAndSave(i.designation)
}else{console.log("invlaid")
}};
i.prepareModelDataAndSave=function(m){var l=[];
var k=m.id;
var n=[];
if(Object.keys(m.featureFieldMap).length>0){angular.forEach(m.featureFieldMap,function(o,p){if(o.length>0){angular.forEach(o,function(s){var q=s.field;
var r={id:k+"-"+p+"-"+s.sectionCode+"-"+q,designation:k,feature:p,searchFlag:s.searchFlag,parentViewFlag:s.parentViewFlag,readonlyFlag:s.readonlyFlag,editableFlag:s.editableFlag,isRequired:s.isRequired,sequenceNo:s.sequenceNo,entityName:s.entity,fieldId:q,sectionCode:s.sectionCode};
n.push(r)
})
}})
}if(Object.keys(m.featureModifierMap).length>0){console.log("designation.featureModifierMap:::"+JSON.stringify(m.featureModifierMap));
angular.forEach(m.featureModifierMap,function(o,p){o.designation=k;
o.feature=p;
l.push(o)
})
}m.featureFieldPermissionDataBeans=n;
m.roleFeatureModifiers=l;
console.log("roleFeatureModifiers:::;"+JSON.stringify(l));
i.designationConfigToSend={featureFieldPermissionDataBeans:n,roleFeatureModifiers:l};
h.maskLoading();
b.updateDesignationConfiguration(m,function(o){h.unMaskLoading();
console.log("success-----------");
i.initializePage()
},function(o){h.unMaskLoading()
})
};
i.getSearchedDesignationRecords=function(l){i.searchRecords=[];
var k=$("#scrollable-dropdown-menu.typeahead").typeahead("val");
if(k.length>0){if(k.length<3){i.searchRecords=[]
}else{if(l!=null&&angular.isDefined(l)&&l.length>0){angular.forEach(l,function(m){m.displayName=h.translateValue("DPT_NM."+m.displayName);
i.searchRecords.push(m)
})
}}i.config.displaySearchedDepartment="search";
i.selectedDepartment=undefined;
i.customLabel="";
if(i.selectedDept.currentNode!==undefined){i.selectedDept.currentNode.selected=undefined
}}};
i.editDesignationFromSearchBox=function(k){var l=null;
if(k!==undefined&&k!==null){i.selectedDesignation=undefined;
angular.forEach(i.designationList,function(m){if(m.designationDataBeans!==null&&m.designationDataBeans!==undefined){angular.forEach(m.designationDataBeans,function(n){if(n.id===k){l=n
}})
}});
if(i.selectedDesign.currentNode!==undefined){i.selectedDesign.currentNode.selected=undefined
}if(l!==null){i.configureDesignation({currentNode:l})
}}};
i.initializePage();
h.unMaskLoading()
}])
});