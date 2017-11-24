define(["hkg","customFieldService","ngload!uiGrid","subentity.directive","messageService"],function(a){a.register.controller("SubEntityController",["$rootScope","$scope","$timeout","$filter","$location","$window","DynamicFormService","CustomFieldService","Messaging",function(i,l,c,h,e,b,k,j,g){i.mainMenu="manageLink";
i.childMenu="manageSubEntity";
l.entity="SUBENTITY.";
i.activateMenu();
l.$on("$viewContentLoaded",function(){l.initializePage();
l.retrieveTreeViewFeatures();
l.subEntity={};
l.featureFieldList=[];
l.SubEntityValuesList=[];
l.addSubmit=false;
l.subId=0;
l.subEntityShow=false;
l.enableAddFlag=false;
l.cancelFlag=true;
l.displayCustomPage="view";
l.reloadSubEntityField=true;
l.addExceptionPage=false;
l.editExceptionFlag=false;
l.exceptionList=[];
l.finalExceptionList=[];
l.forUserList=[];
l.flag={};
l.subEntityValueException={};
l.gridOptions={};
l.gridOptions.enableFiltering=true;
l.editSubOpen=false
});
l.select2allFeature={allowClear:true,data:[],placeholder:"Select"};
l.select2allSubEntity={allowClear:true,data:[],placeholder:"Select"};
l.initializePage=function(){i.maskLoading();
l.select2allFeature.data=[];
j.retrievePrerequisite(function(m){var n=m.customfieldvalues;
l.featureDetails=n.entityFeature;
f(n.entityFeature);
l.featureWithCustomField=n.exitingFeature;
if(!angular.isDefined(l.featureWithCustomField)){l.featureWithCustomField={}
}i.unMaskLoading()
},function(){var n="Failed to retrieve Feature";
var m=i.error;
i.addMessage(n,m);
i.unMaskLoading()
})
};
function f(m){l.allfeatures=m.sort(l.predicateBy("label"));
angular.forEach(l.allfeatures,function(n){l.select2allFeature.data.push({id:n.value,text:n.label})
})
}l.retrieveTreeViewFeatures=function(){j.retrieveSubentityTreeViewFeatures(function(m){l.featureFieldList=angular.copy(m)
})
};
l.selectFeaturedata=function(m){if(m.currentNode.parentId.toString()!=="0"){l.retrieveSubentityFieldFromSearch(m.currentNode.id+"-"+m.currentNode.parentId)
}else{l.cancelExceptionPage();
l.searchRecords=[];
if(m.currentNode.children!==null&&m.currentNode.children!==undefined){angular.forEach(m.currentNode.children,function(n){var o={};
o.id=n.id+"-"+n.parentId;
o.label=n.displayName;
o.feature=n.parentName;
l.searchRecords.push(o)
})
}l.setViewFlag("search");
$("#scrollable-dropdown-menu.typeahead").typeahead("val","");
l.customLabel=""
}};
l.retrieveFieldOfTypeSubEntityForFeature=function(o,n){var m;
if(o instanceof Object){m=o
}else{j.retrieveCustomFieldOfTypeSubEntityByFeatureId(o,function(p){l.subEntityCustom.subEntityId="";
if(l.subEntityCustom.subEntityId===null||l.subEntityCustom.subEntityId===undefined||l.subEntityCustom.subEntityId.length===0){l.subEntity={};
l.subFieldTemplate={};
l.subEntityShow=false;
c(function(){l.subEntityShow=true;
l.SubEntityValuesList={};
if(n!==undefined){l.subEntityCustom.subEntityId=n;
l.retrieveListOfSubEntities(n);
l.enableAddButton()
}},100)
}l.select2allSubEntity.data=[];
angular.forEach(p,function(q){l.select2allSubEntity.data.push({id:q.value,text:q.label})
});
if(n!==undefined){l.subEntityCustom.featureId=o
}})
}};
l.retrieveListOfSubEntities=function(m){if(m!==null&&m!==undefined){l.subEntityShow=false;
if(m instanceof Object){}else{l.subEntity={};
l.subFieldTemplate={};
l.subEntityShow=false;
c(function(){l.subEntityShow=true
},100);
l.modelAndHeaderList=[];
l.columnDefs=[];
l.removedSubEntityList=[];
var n=k.retrieveSubEntities(m);
n.then(function(r){l.subFieldTemplate=r;
l.dateFields=[];
l.dateFieldsToEdit=[];
l.dateTimeFieldsToEdit=[];
var q=new Object();
if(l.subFieldTemplate!=null&&l.subFieldTemplate.length>0){for(var o=0;
o<l.subFieldTemplate.length;
o++){var p=l.subFieldTemplate[o];
if(p.dbType==="Date"||p.dbType==="datetime"){l.dateFields.push(p.model);
if(p.dbType==="Date"){l.dateFieldsToEdit.push(p.model);
l.columnDefs.push({name:p.model,displayName:p.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD | date:"dd/MM/yyyy"}}</div>',minWidth:200})
}else{if(p.dbType==="datetime"){l.dateTimeFieldsToEdit.push(p.model);
l.columnDefs.push({name:p.model,displayName:p.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD | date:"dd/MM/yyyy hh:mm a"}}</div>',minWidth:200})
}}}else{l.columnDefs.push({name:p.model,displayName:p.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}l.modelAndHeaderList.push({model:p.model,header:p.label});
q[p.model]=p.dbType
}l.dbTypeMap=q
}l.subEntityShow=true;
l.gridOptions.columnDefs=l.columnDefs;
l.gridOptions.columnDefs.push({name:"Action",cellTemplate:'<div class="ui-grid-cell-contents"> <a ng-show="row.entity.isArchive !== true"  ng-click="grid.appScope.openEdit(row.entity)"><i class="glyphicon glyphicon-edit" title="Edit"></i></a>\n                                                <a ng-hide="row.entity.isRemoveHide === true || row.entity.isArchive === true" ng-click="grid.appScope.deleteSubFieldValues(row.entity.tempId, row.entity)"><i class="text-danger glyphicon glyphicon-remove" title="Remove"></i></a>\n                                                <span class="text-warning" ng-show="row.entity.isArchive === true">removed  </span><a class="text-info" ng-show="row.entity.isArchive === true" ng-click="grid.appScope.undoDelete(row.entity)"><i class="fa fa-undo" title="Undo"></i></a>\n                                                <span  ng-show="row.entity.isRemoveHide === true">&nbsp;&nbsp;&nbsp;&nbsp;</span></div>',enableFiltering:false,minWidth:200});
l.gridOptions.data=l.SubEntityValuesList;
l.cancelFlag=true;
l.retrieveValuesFromMongo(m)
},function(o){},function(o){})
}}};
l.addSubEntityValues=function(){l.addSubmit=true;
if(l.managesubentity.$valid){var r=false;
if(!jQuery.isEmptyObject(l.subEntity)){var n=0;
var m=0;
for(var o in l.subEntity){m++;
if(l.subEntity[o]===""||l.subEntity[o]===null||l.subEntity[o]===undefined){n++
}}if(parseInt(n)===parseInt(m)){r=true
}if(!r){var p=l.SubEntityValuesList.indexOf(l.subEntity);
if(l.subEntity.tempId==null){l.subEntity.tempId=angular.copy(l.subId++);
l.tempList=angular.copy(l.subEntity);
l.SubEntityValuesList.push(l.tempList)
}else{l.tempListForUpdate=angular.copy(l.SubEntityValuesList);
for(var q in l.tempListForUpdate){if(l.tempListForUpdate[q].tempId.toString()===l.subEntity.tempId.toString()){l.tempListForUpdate[q]=angular.copy(l.subEntity)
}}l.SubEntityValuesList=angular.copy(l.tempListForUpdate)
}l.gridOptions.data=l.SubEntityValuesList;
if(l.gridOptions.data.length>0){angular.forEach(l.gridOptions.data,function(s){s.isRemoveHide=false
})
}l.resetForAdd()
}}}};
l.resetForAdd=function(){l.addSubmit=false;
l.editSubOpen=false;
l.subEntity={};
l.subEntityShow=false;
c(function(){l.subEntityShow=true
},100)
};
l.resetPage=function(){l.subEntityCustom={};
l.subEntity={};
l.SubEntityValuesList=[];
l.addSubmit=false;
l.subId=0;
l.reloadSubEntityField=false;
c(function(){l.reloadSubEntityField=true;
l.select2allSubEntity.data=[]
},100)
};
l.openEdit=function(m){if(l.gridOptions.data.length>0){angular.forEach(l.gridOptions.data,function(n){n.isRemoveHide=false
})
}m.isRemoveHide=true;
l.editSubOpen=true;
l.subEntity=angular.copy(m)
};
l.deleteSubFieldValues=function(m,n){n.isArchive=true
};
l.undoDelete=function(m){angular.forEach(l.SubEntityValuesList,function(o,n){if(o.tempId==m.tempId){o.isArchive=undefined
}})
};
l.saveSubEntityInMongo=function(){if(l.SubEntityValuesList.length>0){for(var n=l.SubEntityValuesList.length-1;
n>=0;
n--){if(l.SubEntityValuesList[n].isArchive===true&&l.SubEntityValuesList[n].id===undefined){l.SubEntityValuesList.splice(n,1)
}}}if(angular.isDefined(l.SubEntityValuesList)&&l.SubEntityValuesList.length>0){l.subEntityListForMongo=[];
angular.forEach(l.SubEntityValuesList,function(r){var p={};
if(l.dateFieldsToEdit!==null&&l.dateFieldsToEdit!==undefined&&l.dateFieldsToEdit.length>0){for(var q in r){if(r.hasOwnProperty(q)){if(l.dateFieldsToEdit.indexOf(q)!==-1){r[q]=new Date(r[q])
}}}}p.dbMap=angular.copy(r);
p.dbType=l.dbTypeMap;
p.instanceId=angular.copy(l.subEntityCustom.subEntityId);
p.componentType=l.subEntity.subFieldType;
p.id=r.id;
l.subEntityListForMongo.push(p)
});
if(angular.isDefined(l.subEntityListForMongo)&&l.subEntityListForMongo.length>0){var o=function(){l.resetPage();
l.cancel()
};
var m=function(){var q="Failed to add the subentities values";
var p=i.failure;
i.addMessage(q,p)
};
j.saveSubEntitiesValue(l.subEntityListForMongo,o,m)
}}};
l.retrieveValuesFromMongo=function(m){if(Object.prototype.toString.call(m)!=="[object Object]"&&m!==undefined){j.retrievelistofSubEntitiesValuesByInstanceId(m,function(n){l.tempListForDisplay=[];
angular.forEach(n,function(o){if(o.dbMap!==null){if(Object.keys(o.dbMap).length){var p={};
p=angular.copy(o.dbMap);
p.id=angular.copy(o.id);
p.tempId=angular.copy(o.tempId);
l.tempListForDisplay.push(p)
}}});
l.SubEntityValuesList=angular.copy(l.tempListForDisplay);
if(l.dateFieldsToEdit!==null&&l.dateFieldsToEdit!==undefined&&l.dateFieldsToEdit.length>0){angular.forEach(l.SubEntityValuesList,function(o){angular.forEach(l.dateFieldsToEdit,function(p){if(o[p]!==null&&o[p]!==undefined){o[p]=new Date(o[p])
}})
})
}if(l.dateTimeFieldsToEdit!==null&&l.dateTimeFieldsToEdit!==undefined&&l.dateTimeFieldsToEdit.length>0){angular.forEach(l.SubEntityValuesList,function(o){angular.forEach(l.dateTimeFieldsToEdit,function(p){if(o[p]!==null&&o[p]!==undefined){o[p]=h("date")(o[p],"dd/MM/yyyy hh:mm a")
}})
})
}l.gridOptions.data=l.SubEntityValuesList
})
}};
l.getSearchedCustomPages=function(n){l.isTreeView=false;
if(l.selectedFeatureData!==undefined&&l.selectedFeatureData.currentNode!==undefined){l.selectedFeatureData.currentNode.selected=undefined
}var m=$("#searchSubentityField.typeahead").typeahead("val");
if(m.length>0){if(m.length<3){l.searchRecords=[]
}else{l.searchRecords=[];
angular.forEach(n,function(p){var o=p.text.split(",");
p.label=o[0];
p.feature=o[1];
l.searchRecords.push(p)
})
}l.setViewFlag("search");
if(l.selectedFeature.currentNode!==undefined){l.selectedFeature.currentNode.selected=undefined
}}};
l.setViewFlag=function(m){if(m!=="search"){$("#scrollable-dropdown-menu.typeahead").typeahead("val","");
l.customLabel=""
}l.displayCustomPage=m
};
l.retrieveSubentityFieldFromSearch=function(o){if(o.indexOf("-")>-1){var n=o.split("-");
if(n.length===2){var p=parseInt(n[1]);
var m=parseInt(n[0]);
if(!angular.isDefined(l.subEntityCustom)){l.subEntityCustom={}
}l.retrieveFieldOfTypeSubEntityForFeature(p,m);
l.setViewFlag("view");
if(l.selectedFeature.currentNode!==undefined&&l.selectedFeature.currentNode.parentId==0){l.selectedFeature.currentNode.selected=undefined
}}}};
l.enableAddButton=function(){l.enableAddFlag=true;
l.editSubOpen=false
};
l.cancel=function(){l.subEntityCustom={};
l.subEntityCustom.featureId="";
l.subEntityCustom.subEntityId="";
l.subEntity={};
l.SubEntityValuesList=[];
l.addSubmit=false;
l.subId=0;
l.cancelFlag=false;
l.enableAddFlag=false;
l.reloadSubEntityField=false;
l.setViewFlag("view");
l.subEntity={};
l.subFieldTemplate={};
l.subEntityShow=false;
if(l.selectedFeature.currentNode!==undefined){l.selectedFeature.currentNode.selected=undefined
}c(function(){l.subEntityShow=true;
l.reloadSubEntityField=true;
l.select2allSubEntity.data=[]
},100)
};
l.initAddExceptionForm=function(m){l.addSubEntityExceptionForm=m
};
l.addExceptionPopup=function(n){i.maskLoading();
l.subEntityName=n.label;
var o=n.id;
var m=o.split("-")[0];
l.instanceId=m;
l.addExceptionPage=true;
l.subEntityValueException={};
l.count=0;
l.initUsers();
l.retrieveValuesForSubEntity(m);
l.retrieveDependentOnFields();
l.retrieveValueExceptions(l.instanceId);
i.unMaskLoading()
};
l.retrievePrerequisiteForException=function(m){l.finalFieldList=[];
j.retrievePrerequisiteForException(m,function(n){if(n!==undefined&&n!==null&&n.data!==undefined&&n.data!==null){var o=n.data;
if(o.dependentFieldList!==undefined&&o.dependentFieldList!==null&&o.dependentFieldList.length>0){l.fieldDataBeanList=o.dependentFieldList;
if(l.fieldDataBeanList.length>0){angular.forEach(l.fieldDataBeanList,function(q){l.finalFieldList.push({id:q.value+"|"+q.description,text:q.label})
})
}}if(o.subEntityValueExceptionDataBeans!==undefined&&o.subEntityValueExceptionDataBeans!==null&&o.subEntityValueExceptionDataBeans.length>0){l.finalExceptionList=[];
var p=0;
angular.forEach(o.subEntityValueExceptionDataBeans,function(q){q.index=p++;
q.isArchive=false;
l.finalExceptionList.push(q);
l.exceptionList.push(q)
});
if(l.exceptionList!==undefined&&l.exceptionList.length>0){l.tempfinalFieldList=[];
angular.forEach(l.exceptionList,function(q){q.isUpdated=false;
angular.forEach(l.finalFieldList,function(s){if(q.dependentOnField==s.id){var r=0;
angular.forEach(l.tempfinalFieldList,function(t){if(s.id===t.id){r++
}});
if(r===0){l.tempfinalFieldList.push({id:s.id,text:s.text})
}}});
if(l.tempfinalFieldList.length>0){l.finalFieldList=angular.copy(l.tempfinalFieldList)
}})
}}}l.initDependentOnValues()
})
};
l.popover="<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td> \n '@D'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Departments</td></tr> <tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr> <tr><td>'@R'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Designations</td></tr> </table> ";
l.initUsers=function(){l.autoCompleteUsers={multiple:true,closeOnSelect:false,placeholder:"Select Users",initSelection:function(n,r){if(l.editExceptionFlag){var p=[];
var o=l.tempSubEntityValueException.forUsers.split(",");
var q=l.tempSubEntityValueException.userToBeDisplay.split(",");
for(var m=0;
m<o.length;
m++){p.push({id:o[m],text:q[m]})
}r(p)
}},formatResult:function(m){return m.text
},formatSelection:function(m){return m.text
},query:function(p){var o=p.term;
l.names=[];
var q=function(r){if(r.length!==0){if(o.substring(0,2)==="@E"||o.substring(0,2)==="@e"){r.push({value:"All",description:"All",label:"All Users"})
}l.names=r;
angular.forEach(r,function(t){var s=0;
angular.forEach(l.forUserList,function(u){if((t.value+":"+t.description)==(u.value+":"+u.description)){s++
}});
if(s===0){l.forUserList.push(t)
}});
angular.forEach(r,function(s){if(s.value==="All"&&s.description==="All"){l.names.push({id:"0:"+s.description,text:s.label})
}else{l.names.push({id:s.value+":"+s.description,text:s.label})
}})
}p.callback({results:l.names})
};
var m=function(){};
if(o.substring(0,2)==="@E"||o.substring(0,2)==="@e"){var n=p.term.slice(2);
g.retrieveUserList(n.trim(),q,m)
}else{if(o.substring(0,2)==="@R"||o.substring(0,2)==="@r"){var n=p.term.slice(2);
g.retrieveRoleList(n.trim(),q,m)
}else{if(o.substring(0,2)==="@D"||o.substring(0,2)==="@d"){var n=p.term.slice(2);
g.retrieveDepartmentList(n.trim(),q,m)
}else{if(o.length>0){var n=o;
g.retrieveUserList(n.trim(),q,m)
}else{p.callback({results:l.names})
}}}}}}
};
l.initValues=function(){l.multiValues={multiple:true,closeOnSelect:false,placeholder:"Select Values",data:l.valueList,initSelection:function(n,r){if(l.editExceptionFlag){var q=[];
var p=l.tempSubEntityValueException.forValue.split(",");
var o=l.tempSubEntityValueException.valueToBeDisplay.split(",");
for(var m=0;
m<p.length;
m++){q.push({id:p[m],text:o[m]})
}r(q)
}},formatResult:function(m){return m.text
}}
};
l.initDependentOnValues=function(){l.autoCompleteDependentOnFields={multiple:false,closeOnSelect:false,placeholder:"Select dependent On",allowClear:true,data:function(){if(l.finalFieldList.length>0){return{results:l.finalFieldList}
}else{return{results:[]}
}},formatResult:function(m){return m.text
}}
};
l.initDependentValueList=function(){l.multiDependentValueList={multiple:true,closeOnSelect:false,placeholder:"Select Values",data:l.fieldValueList,initSelection:function(p,s){if(l.editExceptionFlag){var q=[];
if(l.tempSubEntityValueException!==undefined&&l.tempSubEntityValueException.dependentOnField!==undefined&&l.tempSubEntityValueException.dependentOnFieldValues!==undefined&&l.tempSubEntityValueException.dependentOnFieldValues!==null&&l.tempSubEntityValueException.dependentOnFieldValues!==""&&l.tempSubEntityValueException.dependentOnFieldValues instanceof Object===false&&!angular.isArray(l.tempSubEntityValueException.dependentOnFieldValues)){var r=l.tempSubEntityValueException.dependentOnFieldValues.split(",");
var m=l.tempSubEntityValueException.dependentOnFieldValuesToBeDisplay.split(",");
for(var n=0;
n<r.length;
n++){var o=0;
angular.forEach(l.fieldValueList,function(t){if(t.id==r[n]){o++
}});
if(o!==0){q.push({id:r[n],text:m[n].split("-")[1]})
}}s(q)
}}},formatResult:function(m){return m.text
}}
};
l.$watch("subEntityValueException.dependentOnField",function(){if(l.subEntityValueException.dependentOnField!==undefined&&l.subEntityValueException.dependentOnField instanceof Object===true&&angular.isArray(l.subEntityValueException.dependentOnField)){}else{if(l.fieldValueList!==undefined){l.fieldValueList.splice(0,l.fieldValueList.length)
}l.retrieveFieldValues()
}});
l.retrieveValuesForSubEntity=function(m){l.valueList=[];
l.valueList.push({id:0,text:"All Values"});
j.createDropDownListForSubEntity(m,function(n){if(n!==undefined&&n!==null){angular.forEach(n,function(o){l.valueList.push({id:o.label,text:o.value})
});
if(l.valueList!==undefined&&l.valueList.length>0){l.flag.valueRetrieved=true
}if(l.multiValues===undefined){l.initValues()
}else{l.multiValues.data.splice(0,l.multiValues.data.length);
if(l.valueList.length>0){angular.forEach(l.valueList,function(o){l.multiValues.data.push(o)
})
}}}})
};
l.retrieveDependentOnFields=function(){j.retrieveCustomFields(function(m){l.fieldDataBeanList=m.data;
l.finalFieldList=[];
if(l.fieldDataBeanList.length>0){angular.forEach(l.fieldDataBeanList,function(n){l.finalFieldList.push({id:n.value+"|"+n.description,text:n.label})
});
if(l.finalFieldList.length>0){l.initDependentOnValues()
}}})
};
l.retrieveFieldValues=function(){if(l.subEntityValueException.dependentOnField!==undefined&&l.subEntityValueException.dependentOnField!==null&&l.subEntityValueException.dependentOnField instanceof Object===false&&l.subEntityValueException.dependentOnField!==""){l.dependentOn=l.subEntityValueException.dependentOnField.split("|");
l.payload={fieldId:l.dependentOn[0],componentType:l.dependentOn[1]};
j.retrieveCustomFieldsValueByKey(l.payload,function(m){l.fieldValueList=[];
if(m!==undefined&&m.data!==undefined&&m.data!==null&&m.data.length>0){angular.forEach(m.data,function(n){l.fieldValueList.push({id:n.value,text:n.label})
});
l.initDependentValueList()
}})
}};
l.exceptionList=[];
l.finalExceptionList=[];
l.addException=function(q){l.submitted=true;
if(!q.$valid){}else{if(((l.subEntityValueException.dependentOnFieldValues!==undefined&&l.subEntityValueException.dependentOnFieldValues!==null&&l.subEntityValueException.dependentOnFieldValues!=="")||(l.subEntityValueException.forUsers!==undefined&&l.subEntityValueException.forUsers!==null&&l.subEntityValueException.forUsers!==""))){l.submitted=false;
l.count=l.finalExceptionList.length;
l.subEntityValueException.index=l.count++;
l.subEntityValueException.instanceId=l.instanceId;
l.finalExceptionList.push(angular.copy(l.subEntityValueException));
l.subEntityValueExceptionToBeDisplayed=angular.copy(l.subEntityValueException);
if(l.subEntityValueException.forUsers!==undefined&&l.forUserList.length>0){var u=l.subEntityValueException.forUsers.split(",");
var s="";
for(var o=0;
o<u.length;
o++){angular.forEach(l.forUserList,function(w){var v;
if(w.value==="All"){v="0:"+w.description
}else{v=w.value+":"+w.description
}if(u[o]===v){if(s===""){s=s+w.label
}else{s=s+" , "+w.label
}}})
}l.subEntityValueExceptionToBeDisplayed.userToBeDisplay=s
}if(l.subEntityValueException.forValue!==undefined&&l.valueList.length>0){var t=l.subEntityValueException.forValue.split(",");
var m="";
for(var o=0;
o<t.length;
o++){angular.forEach(l.valueList,function(w){var v=w.id;
if(t[o]==v){if(m===""){m=m+w.text
}else{m=m+" , "+w.text
}}})
}l.subEntityValueExceptionToBeDisplayed.valueToBeDisplay=m
}if(l.subEntityValueException.dependentOnField!==undefined&&l.finalFieldList.length>0){var r="";
angular.forEach(l.finalFieldList,function(v){var w=v.id;
if(l.subEntityValueException.dependentOnField==w){if(r===""){r=r+v.text
}else{r=r+" , "+v.text
}}});
l.subEntityValueExceptionToBeDisplayed.dependentOnToBeDisplay=r
}if(l.subEntityValueException.dependentOnFieldValues!==undefined&&l.fieldValueList.length>0){var n=l.subEntityValueException.dependentOnFieldValues.split(",");
var p="";
for(var o=0;
o<n.length;
o++){angular.forEach(l.fieldValueList,function(w){var v=w.id;
if(n[o]==v){if(p===""){p=p+l.subEntityValueExceptionToBeDisplayed.dependentOnToBeDisplay+"-"+w.text
}else{p=p+" , "+l.subEntityValueExceptionToBeDisplayed.dependentOnToBeDisplay+"-"+w.text
}}})
}l.subEntityValueExceptionToBeDisplayed.dependentOnFieldValuesToBeDisplay=p
}l.subEntityValueExceptionToBeDisplayed.isArchive=false;
l.exceptionList.push(angular.copy(l.subEntityValueExceptionToBeDisplayed));
l.cancelException(q)
}else{i.addMessage("Select For User or Dependent On to apply Exception",i.failure);
l.submitted=false
}}};
l.cancelException=function(){l.subEntityValueException={};
l.submitted=false;
l.addSubEntityExceptionForm.$setPristine();
$("#forUser").select2("data","");
$("#forValue").select2("data","");
$("#dependentOnField").select2("data","");
l.editExceptionFlag=false;
if(l.exceptionList!==undefined&&l.exceptionList.length>0){l.tempfinalFieldList=[];
angular.forEach(l.exceptionList,function(m){m.isUpdated=false;
angular.forEach(l.finalFieldList,function(o){if(m.dependentOnField==o.id){var n=0;
angular.forEach(l.tempfinalFieldList,function(p){if(o.id===p.id){n++
}});
if(n===0){l.tempfinalFieldList.push({id:o.id,text:o.text})
}}});
if(l.tempfinalFieldList.length>0){l.finalFieldList=angular.copy(l.tempfinalFieldList);
l.initDependentOnValues()
}})
}else{l.cancelExceptionPage()
}};
l.removeException=function(m){m.isArchive=true
};
l.undoException=function(m){m.isArchive=false
};
l.cancelExceptionPage=function(){l.subEntityCustom={};
l.subEntityCustom.featureId="";
l.subEntityCustom.subEntityId="";
l.subEntity={};
l.SubEntityValuesList=[];
l.addSubmit=false;
l.subId=0;
l.cancelFlag=false;
l.enableAddFlag=false;
l.reloadSubEntityField=false;
l.setViewFlag("view");
l.subEntity={};
l.subFieldTemplate={};
l.subEntityShow=false;
l.addExceptionPage=false;
l.editExceptionFlag=false;
l.subEntityValueException={};
l.forUserList=[];
l.submitted=false;
l.exceptionList=[];
l.count=0;
if(l.addExceptionForm!==undefined){l.addExceptionForm.$setPristine()
}c(function(){l.subEntityShow=true;
l.reloadSubEntityField=true;
l.select2allSubEntity.data=[]
},100)
};
function d(m){return $.grep(m,function(n){return n.isArchive==false||(n.isArchive==true&&n.id!==undefined&&n.id!==null)
})
}l.saveException=function(){if(l.exceptionList!==undefined&&l.exceptionList.length>0){l.listToSend=[];
l.finalListToSend=[];
l.listToSend=d(l.exceptionList);
angular.forEach(l.listToSend,function(m){angular.forEach(l.finalExceptionList,function(n){if(m.index==n.index){l.finalListToSend.push(n)
}})
});
j.saveException(l.finalListToSend,function(){l.cancelExceptionPage()
})
}};
l.retrieveExceptionForUpdate=function(m){l.editExceptionFlag=true;
angular.forEach(l.finalExceptionList,function(y){if(y.index==m.index){l.send={};
l.uiSelectRecipients=[];
m.isUpdated=true;
l.subEntityValueException=angular.copy(y);
l.tempSubEntityValueException=angular.copy(m);
if(m.forUsers!==undefined&&m.forUsers!==null&&m.forUsers!==""){var u=m.forUsers.split(",");
var o=m.userToBeDisplay.split(",");
var n=[];
for(var t=0;
t<u.length;
t++){n.push({id:u[t],text:o[t]})
}$("#forUser").select2("val",n)
}var v=m.forValue.split(",");
var w=m.valueToBeDisplay.split(",");
var p=[];
for(var r=0;
r<v.length;
r++){p.push({id:v[r],text:w[r]})
}if(m.dependentOnFieldValues!==undefined&&m.dependentOnFieldValues!==null&&m.dependentOnFieldValues!==""){var q=m.dependentOnFieldValues.split(",");
var x=m.dependentOnFieldValuesToBeDisplay.split(",");
var s=[];
for(var r=0;
r<q.length;
r++){s.push({id:q[r],text:x[r]})
}$("#dependentOnFieldValues").select2("val",s)
}$("#forValue").select2("val",p);
$("#dependentOnField").select2("val",m.dependentOnField)
}})
};
l.updateException=function(n){l.submitted=true;
if(!n.$valid){l.submitted=false
}else{if(((l.subEntityValueException.dependentOnFieldValues!==undefined&&l.subEntityValueException.dependentOnFieldValues!==null&&l.subEntityValueException.dependentOnFieldValues!=="")||(l.subEntityValueException.forUsers!==undefined&&l.subEntityValueException.forUsers!==null&&l.subEntityValueException.forUsers!==""))){l.tempValueExceptionObj=angular.copy(l.subEntityValueException);
if(l.tempValueExceptionObj.forUsers!==undefined){var r="";
if(angular.isArray(l.tempValueExceptionObj.forUsers)){var p="";
angular.forEach(l.tempValueExceptionObj.forUsers,function(t){if(p!==""){p=p+","+t.id
}else{p=p+t.id
}});
r=p
}else{r=l.tempValueExceptionObj.forUsers
}l.tempValueExceptionObj.forUsers=r
}if(l.tempValueExceptionObj.forValue!==undefined){var s="";
if(angular.isArray(l.tempValueExceptionObj.forValue)){var m="";
angular.forEach(l.tempValueExceptionObj.forValue,function(t){if(m!==""){m=m+","+t.id
}else{m=m+t.id
}});
s=m
}else{s=l.tempValueExceptionObj.forValue
}l.tempValueExceptionObj.forValue=s
}if(l.tempValueExceptionObj.dependentOnFieldValues!==undefined){var o="";
if(angular.isArray(l.tempValueExceptionObj.dependentOnFieldValues)){var q="";
angular.forEach(l.tempValueExceptionObj.dependentOnFieldValues,function(t){if(q!==""){q=q+","+t.id
}else{q=q+t.id
}});
o=q
}else{o=l.tempValueExceptionObj.dependentOnFieldValues
}l.tempValueExceptionObj.dependentOnFieldValues=o
}angular.forEach(l.finalExceptionList,function(t){if(l.tempValueExceptionObj.index==t.index){t.forUsers=l.tempValueExceptionObj.forUsers;
t.forValue=l.tempValueExceptionObj.forValue;
t.dependentOnField=l.tempValueExceptionObj.dependentOnField;
t.dependentOnFieldValues=l.tempValueExceptionObj.dependentOnFieldValues
}});
angular.forEach(l.exceptionList,function(u){if(u.index==l.tempValueExceptionObj.index){u.isUpdated=false;
l.tempValueExceptionObj.isUpdated=false;
u.forUsers=l.tempValueExceptionObj.forUsers;
u.forValue=l.tempValueExceptionObj.forValue;
u.dependentOnField=l.tempValueExceptionObj.dependentOnField;
u.dependentOnFieldValues=l.tempValueExceptionObj.dependentOnFieldValues;
if(l.tempValueExceptionObj.forUsers!==undefined&&l.forUserList.length>0){var C="";
if(angular.isArray(l.tempValueExceptionObj.forUsers)){var y="";
angular.forEach(l.tempValueExceptionObj.forUsers,function(F){if(y!==""){y=y+","+F.id
}else{y=y+F.id
}});
C=y.split(",")
}else{C=l.tempValueExceptionObj.forUsers.split(",")
}var A="";
for(var w=0;
w<C.length;
w++){angular.forEach(l.forUserList,function(G){var F=G.value+":"+G.description;
if(C[w]===F){if(A===""){A=A+G.label
}else{A=A+" , "+G.label
}}})
}u.userToBeDisplay=A
}if(l.tempValueExceptionObj.forValue!==undefined&&l.valueList.length>0){var B="";
if(angular.isArray(l.tempValueExceptionObj.forValue)){var D="";
angular.forEach(l.tempValueExceptionObj.forValue,function(F){if(D!==""){D=D+","+F.id
}else{D=D+F.id
}});
B=D.split(",")
}else{B=l.tempValueExceptionObj.forValue.split(",")
}var t="";
for(var w=0;
w<B.length;
w++){angular.forEach(l.valueList,function(G){var F=G.id;
if(B[w]==F){if(t===""){t=t+G.text
}else{t=t+" , "+G.text
}}})
}u.valueToBeDisplay=t
}if(l.tempValueExceptionObj.dependentOnField!==undefined&&l.finalFieldList.length>0){var z="";
angular.forEach(l.finalFieldList,function(F){var G=F.id;
if(l.tempValueExceptionObj.dependentOnField==G){if(z===""){z=z+F.text
}else{z=z+" , "+F.text
}}});
u.dependentOnToBeDisplay=z
}if(l.tempValueExceptionObj.dependentOnFieldValues!==undefined&&l.fieldValueList.length>0){var v="";
if(angular.isArray(l.tempValueExceptionObj.dependentOnFieldValues)){var E="";
angular.forEach(l.tempValueExceptionObj.dependentOnFieldValues,function(F){if(E!==""){E=E+","+F.id
}else{E=E+F.id
}});
v=E.split(",")
}else{v=l.tempValueExceptionObj.dependentOnFieldValues.split(",")
}var x="";
for(var w=0;
w<v.length;
w++){angular.forEach(l.fieldValueList,function(G){var F=G.id;
if(v[w]==F){if(x===""){x=x+u.dependentOnToBeDisplay+"-"+G.text
}else{x=x+" , "+u.dependentOnToBeDisplay+"-"+G.text
}}})
}u.dependentOnFieldValuesToBeDisplay=x
}}});
l.cancelException(n);
l.editExceptionFlag=false
}else{i.addMessage("Select For User or Dependent On to Apply Exception",i.failure);
l.submitted=false
}}};
l.retrieveValueExceptions=function(n){if(n!==undefined){l.finalExceptionList=[];
var m=0;
j.retrieveValueExceptions(n,function(o){if(o.data!==undefined&&o.data!==null&&o.data.length>0){angular.forEach(o.data,function(p){p.index=m++;
p.isArchive=false;
l.finalExceptionList.push(p);
l.exceptionList.push(p)
})
}})
}}
}])
});