define(["hkg","ruleConfigService","colorpicker.directive","designationService","ruleTemplate"],function(a){a.register.controller("RuleConfigController",["$rootScope","$scope","RuleConfigService","Designation","$filter","$q","$timeout",function(b,f,i,h,e,d,g){f.showExceptionLink=true;
f.statusList=["Active","Remove"];
b.mainMenu="manageLink";
b.childMenu="configureRule";
b.activateMenu();
f.entity="CONFIG_RULE.";
var c=this;
f.open=function(j){j.preventDefault();
j.stopPropagation()
};
f.openToDate=function(j){j.preventDefault();
j.stopPropagation()
};
f.dateOptions={formatYear:"yy",startingDay:1};
f.format=b.dateFormat;
f.$on("$viewContentLoaded",function(){f.reset();
i.retrievePrerequisite(function(j){f.fieldList=[];
if(j!==null&&j!==undefined&&j.data!==null&&j.data!==undefined){f.featureList=j.data.featureList;
f.ruleTypes=j.data.ruleTypes;
f.ruleTypesFromDb=angular.copy(j.data.ruleTypes);
console.log("==========="+JSON.stringify(j.data.ruleTypes));
f.rules=j.data.rules;
if(j.data.ruleTypes!==null&&j.data.ruleTypes!==undefined){f.ruleTypes=[];
f.ruleTypesForTree=[];
for(var k in j.data.ruleTypes){f.ruleTypes.push({id:k,text:j.data.ruleTypes[k]})
}f.ruleTypesForTree=angular.copy(f.ruleTypes);
f.ruleTypesForTree.push({id:"All",text:"All"});
f.ruleTypeForTree="All";
console.log("asd"+JSON.stringify(f.ruleTypesForTree))
}if(j.data.featureList!==null&&j.data.featureList!==undefined){f.autoCompleteFeature={allowClear:true,multiple:true,closeOnSelect:false,placeholder:"Select purchased by",initSelection:function(l,m){if(f.isEditFlag){f.names=[];
console.log("in eit"+f.rule.features);
f.temp=f.rule.features;
f.fArray=[];
f.fArray=f.rule.features.split(",");
angular.forEach(f.featureList,function(o){for(var n in f.fArray){if(o.value.toString()===f.fArray[n]){f.names.push({id:o.value,text:o.label})
}}});
f.rule.features=f.temp;
m(f.names)
}},formatResult:function(l){return l.text
},formatSelection:function(l){return l.text
},query:function(m){var l=m.term;
f.names=[];
if(f.featureList.length!==0){angular.forEach(f.featureList,function(n){if(l!==null&&l!==undefined&&l.length>0){if(n.label.toString().toUpperCase().indexOf(l.toString().toUpperCase())>-1){f.names.push({id:n.value,text:n.label})
}}else{f.names.push({id:n.value,text:n.label})
}})
}m.callback({results:f.names})
}}
}if(j.data.fieldFeatureMap!==null&&j.data.fieldFeatureMap!==undefined){angular.forEach(j.data.fieldFeatureMap,function(l){if(l.length>0){angular.forEach(l,function(m){f.fieldList.push(m)
})
}});
f.fieldListToSend=[];
f.fieldListToSend=angular.copy(f.fieldList);
f.invoiceList=[];
f.lotList=[];
f.parcelList=[];
f.packetList=[];
f.coatedRoughList=[];
f.issueList=[];
f.planList=[];
f.diamondList=[];
f.allotmentList=[];
f.transferList=[];
f.sellList=[];
f.roughCalcyList=[];
f.purchaseList=[];
f.subLotList=[];
f.selectedFieldsForSearch=[];
f.selectedFieldsForParent=[];
f.combinedFieldListForSearch=[];
f.combinedFieldListForParent=[];
angular.forEach(f.fieldList,function(l){if(l.entity==="Invoice"){f.invoiceList.push(l)
}else{if(l.entity==="Parcel"){f.parcelList.push(l)
}else{if(l.entity==="Packet"){f.packetList.push(l)
}else{if(l.entity==="Lot"){f.lotList.push(l)
}else{if(l.entity==="Coated Rough"){f.coatedRoughList.push(l)
}else{if(l.entity==="Issue"){f.issueList.push(l)
}else{if(l.entity==="Plan"){f.planList.push(l)
}else{if(l.entity==="Diamond"){f.diamondList.push(l)
}else{if(l.entity==="Allotment"){f.allotmentList.push(l)
}else{if(l.entity==="Transfer"){f.transferList.push(l)
}else{if(l.entity==="Sell"){f.sellList.push(l)
}else{if(l.entity==="RoughCalcy"){f.roughCalcyList.push(l)
}else{if(l.entity==="SubLot"){f.subLotList.push(l)
}else{if(l.entity==="Purchase"){f.purchaseList.push(l)
}}}}}}}}}}}}}}});
if(f.invoiceList.length>0){f.combinedFieldListForSearch.push({modelName:"<strong>Invoice</strong>",multiSelectGroup:true});
if(f.invoiceList.length>0){angular.forEach(f.invoiceList,function(n){if(n.dbFieldName!==undefined){var m=n.dbFieldName.split("$");
if(m.length>0){var l=m[1];
if(l!=="IMG"&&l!=="UPD"){f.combinedFieldListForSearch.push(n)
}}}})
}}if(f.invoiceList.length!==0){f.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(f.parcelList.length>0){f.combinedFieldListForSearch.push({modelName:"<strong>Parcel</strong>",multiSelectGroup:true});
if(f.parcelList.length>0){angular.forEach(f.parcelList,function(n){if(n.dbFieldName!==undefined){var m=n.dbFieldName.split("$");
if(m.length>0){var l=m[1];
if(l!=="IMG"&&l!=="UPD"){f.combinedFieldListForSearch.push(n)
}}}})
}}if(f.parcelList.length!==0){f.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(f.lotList.length>0){f.combinedFieldListForSearch.push({modelName:"<strong>Lot</strong>",multiSelectGroup:true});
if(f.lotList.length>0){angular.forEach(f.lotList,function(n){if(n.dbFieldName!==undefined){var m=n.dbFieldName.split("$");
if(m.length>0){var l=m[1];
if(l!=="IMG"&&l!=="UPD"){f.combinedFieldListForSearch.push(n)
}}}})
}}if(f.lotList.length!==0){f.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(f.packetList.length>0){f.combinedFieldListForSearch.push({modelName:"<strong>Packet</strong>",multiSelectGroup:true});
if(f.packetList.length>0){angular.forEach(f.packetList,function(n){if(n.dbFieldName!==undefined){var m=n.dbFieldName.split("$");
if(m.length>0){var l=m[1];
if(l!=="IMG"&&l!=="UPD"){f.combinedFieldListForSearch.push(n)
}}}})
}}if(f.packetList.length!==0){f.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(f.issueList.length>0){f.combinedFieldListForSearch.push({modelName:"<strong>Issue</strong>",multiSelectGroup:true});
if(f.issueList.length>0){angular.forEach(f.issueList,function(n){if(n.dbFieldName!==undefined){var m=n.dbFieldName.split("$");
if(m.length>0){var l=m[1];
if(l!=="IMG"&&l!=="UPD"){f.combinedFieldListForSearch.push(n)
}}}})
}}if(f.issueList.length!==0){f.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(f.transferList.length>0){f.combinedFieldListForSearch.push({modelName:"<strong>Transfer</strong>",multiSelectGroup:true});
if(f.transferList.length>0){angular.forEach(f.transferList,function(n){if(n.dbFieldName!==undefined){var m=n.dbFieldName.split("$");
if(m.length>0){var l=m[1];
if(l!=="IMG"&&l!=="UPD"){f.combinedFieldListForSearch.push(n)
}}}})
}}if(f.transferList.length!==0){f.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(f.sellList.length>0){f.combinedFieldListForSearch.push({modelName:"<strong>Sell</strong>",multiSelectGroup:true});
if(f.sellList.length>0){angular.forEach(f.sellList,function(n){if(n.dbFieldName!==undefined){var m=n.dbFieldName.split("$");
if(m.length>0){var l=m[1];
if(l!=="IMG"&&l!=="UPD"){f.combinedFieldListForSearch.push(n)
}}}})
}}if(f.sellList.length!==0){f.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(f.roughCalcyList.length>0){f.combinedFieldListForSearch.push({modelName:"<strong>Rough Calcy</strong>",multiSelectGroup:true});
if(f.roughCalcyList.length>0){angular.forEach(f.roughCalcyList,function(n){if(n.dbFieldName!==undefined){var m=n.dbFieldName.split("$");
if(m.length>0){var l=m[1];
if(l!=="IMG"&&l!=="UPD"){f.combinedFieldListForSearch.push(n)
}}}})
}}if(f.roughCalcyList.length!==0){f.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(f.subLotList.length>0){f.combinedFieldListForSearch.push({modelName:"<strong>Sub Lot</strong>",multiSelectGroup:true});
if(f.subLotList.length>0){angular.forEach(f.subLotList,function(n){if(n.dbFieldName!==undefined){var m=n.dbFieldName.split("$");
if(m.length>0){var l=m[1];
if(l!=="IMG"&&l!=="UPD"){f.combinedFieldListForSearch.push(n)
}}}})
}}if(f.subLotList.length!==0){f.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(f.purchaseList.length>0){f.combinedFieldListForSearch.push({modelName:"<strong>Rough Purchase</strong>",multiSelectGroup:true});
if(f.purchaseList.length>0){angular.forEach(f.purchaseList,function(n){if(n.dbFieldName!==undefined){var m=n.dbFieldName.split("$");
if(m.length>0){var l=m[1];
if(l!=="IMG"&&l!=="UPD"){f.combinedFieldListForSearch.push(n)
}}}})
}}if(f.purchaseList.length!==0){f.combinedFieldListForSearch.push({multiSelectGroup:false})
}f.valList={Invoice:f.invoiceList,Parcel:f.parcelList,Lot:f.lotList,Packet:f.packetList,Issue:f.issueList,Plan:f.planList,Allotment:f.allotmentList,Transfer:f.transferList,Sell:f.sellList}
}if(j.data.rules!==null&&j.data.rules!==undefined){f.ruleList=j.data.rules.R;
f.finalRuleList=j.data.rules.R;
f.exceptionList=j.data.rules.E;
f.autoCompleteSkipRule={allowClear:true,multiple:true,closeOnSelect:false,placeholder:"Select purchased by",initSelection:function(l,m){if(f.isEditFlag){f.names=[];
console.log("in eit"+f.rule.features);
f.tempRules=f.rule.skipRules;
f.rArray=[];
f.rArray=f.rule.skipRules.split(",");
angular.forEach(f.ruleList,function(o){for(var n in f.rArray){if(o.id.toString()===f.rArray[n]){f.names.push({id:o.id,text:o.id+"-"+o.ruleName})
}}});
f.rule.features=f.temp;
m(f.names)
}},formatResult:function(l){return l.text
},formatSelection:function(l){return l.text
},query:function(n){var m=n.term;
console.log("ss"+m);
var o=function(p){f.names=[];
console.log("rs.."+JSON.stringify(p));
if(p.length!==0){angular.forEach(p,function(q){if(q.category!==null&&q.category==="General"){f.names.push({id:q.id,text:q.id+"-"+q.ruleName})
}})
}n.callback({results:f.names})
};
var l=function(){};
i.searchrules({q:m},o,l,function(p){})
}}
}}})
});
f.clearTreeNodeSelectedForRule=function(){console.log("in treeeee..."+JSON.stringify(f.selectedRule));
if(f.selectedRule!==undefined&&f.selectedRule.currentNode!==undefined){f.selectedRule.currentNode.selected=undefined
}};
f.clearTreeNodeSelectedForExcption=function(){if(f.selectedException!==undefined&&f.selectedException.currentNode!==undefined){f.selectedException.currentNode.selected=undefined
}};
f.cancel=function(){f.setRuleAsFeature()
};
f.clearSelectedSearchFields=function(){if(f.combinedFieldListForSearch!==null&&f.combinedFieldListForSearch!==undefined&&f.combinedFieldListForSearch.length>0){angular.forEach(f.combinedFieldListForSearch,function(j){j.ticked=false
})
}};
f.reset=function(){f.rule={};
f.submittedForm=false;
f.isRulePage=true;
f.isEditFlag=false;
f.reloadFeature=false;
g(function(){f.reloadFeature=true
},50);
f.reloadSkipRule=false;
g(function(){f.reloadSkipRule=true
},50);
f.displayRulepage="view";
f.reloadTree=true;
f.clearSelectedSearchFields()
};
f.setRuleAsFeature=function(){f.reset();
f.setRuleParameters();
f.clearTreeNodeSelectedForExcption();
f.clearTreeNodeSelectedForRule()
};
f.setRuleParameters=function(){f.showRuleLink=false;
f.showExceptionLink=true;
f.isRulePage=true;
f.isExceptionPage=false
};
f.setExceptionAsFeature=function(){f.reset();
f.setExceptionParameters();
f.clearTreeNodeSelectedForExcption();
f.clearTreeNodeSelectedForRule()
};
f.setExceptionParameters=function(){f.showRuleLink=true;
f.showExceptionLink=false;
f.isExceptionPage=true;
f.isRulePage=false
};
f.saveRule=function(n){f.submittedForm=true;
if(f.rule.status==="Remove"){f.confirmationForRemoveRule()
}else{if(f.rule.ruleList!==null&&f.rule.ruleList!==undefined){angular.forEach(f.rule.ruleList,function(p){if(p.rowsubmitted===false){p.rowsubmitted=true
}})
}if(n.$valid){var o={};
if(f.isRulePage){o.category="R"
}if(f.isExceptionPage){o.category="E";
var k=[];
if(f.rule.skipRules!==null&&f.rule.skipRules!==undefined){k=f.rule.skipRules.split(",");
o.skipRules=k
}}o.ruleName=f.rule.ruleName;
o.ruleNumber=f.rule.ruleNumber;
o.type=f.rule.type;
o.description=f.rule.description;
o.fromDate=f.rule.fromDate;
o.toDate=f.rule.toDate;
o.status="A";
o.tooltipMsg=f.rule.tooltipMsg;
o.validationMessage=f.rule.validationMessage;
o.colorCode=f.rule.colorCode;
console.log(JSON.stringify(f.rule.criterias));
o.criterias=f.rule.criterias;
if(f.entityId!==undefined&&f.entityId!==null&&f.entityId<1){o.apply=new Date(b.getCurrentServerDate());
o.entityName="Login";
var l=[];
l.push(-1);
o.features=l;
console.log(o.features)
}else{o.apply=f.rule.apply
}var j=[];
var m=[];
if(f.rule.features!==null&&f.rule.features!==undefined&&f.rule.features.length>0){j=f.rule.features.split(",");
console.log("----------------------"+j);
o.features=j
}if(f.selectedFieldsForSearch!==null&&f.selectedFieldsForSearch!==undefined){angular.forEach(f.selectedFieldsForSearch,function(p){m.push(p.field)
});
o.fieldsToBeApplied=angular.copy(m)
}console.log("in save method"+JSON.stringify(o));
i.saveRule(o,function(){f.cancel();
f.reset();
f.clearTreeNodeSelectedForExcption();
f.clearTreeNodeSelectedForRule();
f.reteieveAllRules()
})
}}};
f.editRule=function(k,j){f.reset();
f.reloadFeature=false;
f.reloadSkipRule=false;
f.isEditFlag=true;
console.log("ruleObj..."+JSON.stringify(k));
console.log("edit rule///true"+f.isEditFlag);
var l;
if(j==="tree"){l=k.currentNode.id
}if(j==="exceptionLink"){l=k.value
}if(j==="editRuleFromSearch"){l=k
}console.log("as"+l);
console.log("id"+l);
i.retrieveRulebyRulenumber(l,function(n){var m=angular.copy(n.data);
if(m!==undefined&&m!==null&&m.criterias!==undefined&&m.criterias!==null){if(m.criterias[0].entity<0){f.entityId=m.criterias[0].entity
}}f.rule={};
if(m.category!==null&&m.category==="R"){f.setRuleParameters();
f.rule.features=n.data.features.toString().replace("[","").replace("]","");
f.reloadFeature=true;
if(m.exceptionList!==null&&m.exceptionList!==undefined){f.exceptionListForRule=angular.copy(m.exceptionList)
}if(f.selectedException!==undefined&&f.selectedException.currentNode!==undefined){f.selectedException.currentNode.selected=undefined
}f.searchedRuleTree=n.data.ruleNumber;
f.searchedExceptionTree=""
}if(m.category!==null&&m.category==="E"){f.setExceptionParameters();
f.rule.skipRules=n.data.skipRules.toString().replace("[","").replace("]","");
console.log("dsfds"+f.rule.skipRules);
f.reloadSkipRule=true;
console.log("exc selcted"+JSON.stringify(f.selectedRule));
if(f.selectedRule!==undefined&&f.selectedRule.currentNode!==undefined){f.selectedRule.currentNode.selected=undefined
}f.searchedExceptionTree=n.data.ruleNumber;
f.searchedRuleTree=""
}f.rule.id=n.data.id;
f.rule.ruleName=n.data.ruleName;
f.rule.ruleNumber=n.data.ruleNumber;
f.rule.category=n.data.category;
f.rule.description=n.data.description;
f.rule.type=n.data.type;
f.rule.fromDate=n.data.fromDate;
f.rule.toDate=n.data.toDate;
f.rule.apply=n.data.apply;
if(n.data.status==="A"){f.rule.status="Active"
}f.rule.criterias=n.data.criterias;
f.rule.validationMessage=n.data.validationMessage;
f.rule.colorCode=n.data.colorCode;
f.rule.tooltipMsg=n.data.tooltipMsg;
f.rule.apply=n.data.apply;
var o=[];
o=n.data.fieldsToBeApplied;
f.modelName=[];
if(f.combinedFieldListForSearch!==null&&f.combinedFieldListForSearch!==undefined){angular.forEach(f.combinedFieldListForSearch,function(q){if(q.modelName!==null&&q.modelName!==undefined&&q.multiSelectGroup!==null&&q.multiSelectGroup!==undefined){}else{if(q.field!==null&&q.field!==undefined){for(var p in o){if(q.field.toString()===o[p].toString()){q.ticked=true;
f.modelName.push(q)
}}}}})
}f.selectedFieldsForSearch=angular.copy(f.modelName);
console.log("edit rule///true after...."+f.isEditFlag)
})
};
f.setSearchField=function(j){f.selectedFieldsForSearch=angular.copy(j)
};
f.redirectToException=function(j){console.log("exception///"+JSON.stringify(j));
if(f.selectedRule!==undefined&&f.selectedRule.currentNode!==undefined){f.selectedRule.currentNode.selected=undefined
}f.editRule(j,"exceptionLink")
};
f.sortTreeByType=function(j){f.ruleList=[];
if(f.finalRuleList!==null&&f.finalRuleList!==undefined){console.log("rrr"+j);
console.log("tttt"+JSON.stringify(f.finalRuleList));
if(j!=="All"){angular.forEach(f.finalRuleList,function(k){if(k.type.toString()===j.toString()){f.ruleList.push(k)
}})
}else{f.ruleList=angular.copy(f.finalRuleList)
}}};
f.editRuleFromSearch=function(j){console.log("id. in serachhhh.."+j);
f.editRule(j,"editRuleFromSearch")
};
f.setViewFlag=function(j){if(j!=="search"){$("#scrollable-dropdown-menu.typeahead").typeahead("val","")
}f.displayRulepage=j
};
f.getSearchedRules=function(k){f.clearTreeNodeSelectedForExcption();
f.clearTreeNodeSelectedForRule();
f.customLabel="";
var j=$("#searchCustomField.typeahead").typeahead("val");
if(j.length>0){if(j.length<3){f.searchRecords=[]
}else{f.searchRecords=[];
angular.forEach(k,function(l){console.log("itemmmm"+JSON.stringify(l));
f.searchRecords.push(l)
})
}f.setViewFlag("search")
}};
f.confirmationForRemoveRule=function(){$("#messageModal").modal("show")
};
f.removeRule=function(){b.maskLoading();
f.disableOkForRemove=true;
i.removeRule(f.rule.ruleNumber,function(){$("#messageModal").modal("hide");
b.removeModalOpenCssAfterModalHide();
$(".modal-backdrop").remove();
b.unMaskLoading();
f.disableOkForRemove=false;
f.reset();
f.clearTreeNodeSelectedForExcption();
f.clearTreeNodeSelectedForRule();
var k="Rule removed successfully";
var j=b.success;
b.addMessage(k,j)
})
};
f.hideconformationForRemoveRule=function(){$("#messageModal").modal("hide");
b.removeModalOpenCssAfterModalHide();
$(".modal-backdrop").remove();
f.rule.status="Active"
};
f.reteieveAllRules=function(){i.retrieveAllRuleList(function(j){if(j.R!==null&&j.R!==undefined){f.ruleList=j.R;
f.finalRuleList=j.R;
f.exceptionList=j.E;
f.reloadTree=false;
g(function(){f.reloadTree=true
},50)
}})
}
}])
});