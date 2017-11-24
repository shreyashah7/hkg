define(["hkg","manageMasterService","ngload!ngTable","messageService"],function(a,b){a.register.controller("ManageMaster",["$rootScope","$scope","$filter","ManageMasterService","ngTableParams","Messaging","$timeout",function(i,k,f,j,c,g,d){i.maskLoading();
var h=f("orderBy");
i.mainMenu="manageLink";
i.childMenu="manageMasters";
i.activateMenu();
k.listFilled=false;
k.masterList=true;
k.shortCutInDB=false;
k.masterValueInDB=false;
k.shortCutCodeLength=false;
k.editMaster=false;
k.addExceptionPage=false;
k.editablle=false;
k.editExceptionFlag=false;
k.isEditing=false;
k.detailsFilled=false;
k.submitted=false;
k.searchPage=false;
k.count=0;
k.forUserList=[];
k.exceptionList=[];
k.finalExceptionList=[];
k.valueException={};
k.selected=null;
k.masterType=[{id:1,text:null,displayName:"Built- in masters",children:null}];
k.hasCustomMasterRigth=function(l){if(l){k.masterType.push({id:2,text:null,displayName:"Custom masters",children:null})
}};
k.tableParams=new c({page:1,count:5});
k.entity="MASTER.";
k.initMasterForm=function(l){k.editMasterForm=l
};
k.sort=function(m){if(m){k.selected=m;
if(k.listOfMaster){k.listOfMaster=[];
if(m!=="All"){for(var l=0;
l<k.masters.length;
l++){if(m===k.masters[l].masterName.charAt(0).toUpperCase()){k.listOfMaster.push(k.masters[l]);
k.listOfMasterFilled=true
}}}else{k.listOfMaster=angular.copy(k.masters);
k.listOfMasterFilled=true
}}}};
k.showSelectedMasters=function(m){k.editMaster=false;
k.masterList=true;
k.letterList=[];
k.selected="All";
k.addExceptionPage=false;
k.editExceptionFlag=false;
k.cancelExceptionPage();
if(m.currentNode.id.toString()==="1"){k.listOfMaster=angular.copy(k.builtInMaster);
k.masters=angular.copy(k.builtInMaster);
k.selectedType="1";
for(var l=65;
l<=90;
l++){k.letterList.push(String.fromCharCode(l));
if(l===90){k.letterList.push("All");
k.listFilled=true
}}}else{k.listOfMaster=angular.copy(k.customMaster);
k.selectedType="2";
k.masters=angular.copy(k.customMaster);
for(var l=65;
l<=90;
l++){k.letterList.push(String.fromCharCode(l));
if(l===90){k.letterList.push("All");
k.listFilled=true
}}}k.sort()
};
j.retrieveListOfMaster(function(o){i.maskLoading();
o=h(o,["masterName"]);
k.letterList=[];
var n=0;
for(var l=65;
l<=90;
l++){k.letterList.push(String.fromCharCode(l));
if(l===90){k.letterList.push("All");
k.listFilled=true;
k.selected="All"
}}k.listOfMaster=o;
k.listOfMasterFilled=true;
k.langNotSelected=true;
i.unMaskLoading();
k.masters=angular.copy(k.listOfMaster);
k.builtInMaster=[];
k.customMaster=[];
for(var l=0;
l<k.masters.length;
l++){var m=k.masters[l];
if(m.masterType==="B"||m.masterType==="b"){k.builtInMaster.push(m)
}else{k.customMaster.push(m)
}}},function(){var m="Failed to retrieve master";
var l=i.error;
i.addMessage(m,l)
});
k.showConfirmationPopup=function(l){i.maskLoading();
k.serchedList;
k.searchPage=false;
k.addExceptionPage=false;
k.editExceptionFlag=false;
k.code=l.code;
k.masterName=l.masterName;
k.isSensitiveMaster=l.isSensitiveMaster;
if(l.isSensitiveMaster===true){$("#passwordPopUp").modal("show");
k.showPopup=true;
i.unMaskLoading()
}else{k.editMasterDetail();
k.showPopup=false;
i.unMaskLoading()
}};
k.proceed=function(m){k.submitted=true;
if(m.$valid){k.submitted=false;
var l=k.password;
j.authenticateForEditMaster(l,function(n){if(n.data){k.inValidPassword=false;
k.editMasterDetail();
k.showPopup=false;
i.unMaskLoading()
}else{k.inValidPassword=true;
i.unMaskLoading()
}})
}};
k.hideConfirmationPopup=function(){k.password="";
k.submitted=false;
k.inValidPassword=false;
k.passwordPopUpForm.$setPristine();
$("#passwordPopUp").modal("hide");
i.removeModalOpenCssAfterModalHide()
};
k.editMasterDetail=function(){i.maskLoading();
j.retrieveDetailsOfMaster({primaryKey:k.code},function(l){k.detailOfMaster=l;
if(k.detailOfMaster.masterDataBeans){for(var m=0;
m<k.detailOfMaster.masterDataBeans.length;
m++){var n=k.detailOfMaster.masterDataBeans[m];
n.newArchieve=n.isArchieve
}}k.detailOfMaster.masterDataBeans=h(k.detailOfMaster.masterDataBeans,["-isOftenUsed","shortcutCode","value"]);
k.detailOfMasterInDB=angular.copy(l);
k.detailsFilled=true;
i.unMaskLoading()
});
k.editMasterForm.$dirty=false;
k.masterList=false;
k.editMaster=true;
if(k.showPopup===true||k.showPopup==="true"){$("#passwordPopUp").modal("hide");
i.removeModalOpenCssAfterModalHide()
}j.retrieveLanguage(function(m){k.languageList=[];
var l=[];
for(var o in m.data){var n={id:o,title:m.data[o]};
k.languageList.push(n)
}k.languageList=k.languageList.sort(i.predicateBy("title"))
})
};
k.shortCutExists=function(q,l){k.shortCutInDB=false;
if(l){l.shortcutCode.$setValidity("exists",true)
}k.shortCutInUI=q.shortcutCode;
var p=0;
var o=false;
for(var n=0;
n<k.detailOfMaster.masterDataBeans.length;
n++){if(k.detailOfMaster.masterDataBeans[n].shortcutCode!==undefined&&k.detailOfMaster.masterDataBeans[n].shortcutCode!==null){for(var r=0;
r<n;
r++){if(parseInt(k.detailOfMaster.masterDataBeans[n].shortcutCode)===parseInt(k.detailOfMaster.masterDataBeans[r].shortcutCode)){o=true;
break
}}}}if(!o){for(var m=0;
m<k.refArr.length;
m++){if(k.refArr[m]!==undefined&&k.refArr[m]!==null){k.refArr[m].shortcutCode.$setValidity("exists",true)
}}}if(k.shortCutInUI&&k.shortCutInUI!==""){for(var m=0;
m<k.detailOfMaster.masterDataBeans.length;
m++){if(k.detailOfMaster.masterDataBeans[m].shortcutCode){if(k.detailOfMaster.masterDataBeans[m].shortcutCode.toString()===k.shortCutInUI){p++;
if(p===2){k.shortCutInDB=true;
l.shortcutCode.$setValidity("exists",false);
break
}}}}}};
k.masterValueExists=function(r,l,m){k.masterValueInDB=false;
k.submitted=false;
if(l){l.masterValue.$setValidity("exists",true)
}var p=false;
for(var o=0;
o<k.detailOfMaster.masterDataBeans.length;
o++){if(k.detailOfMaster.masterDataBeans[o].value!==undefined&&k.detailOfMaster.masterDataBeans[o].value!==null){for(var s=0;
s<o;
s++){if(k.detailOfMaster.masterDataBeans[s].value!==undefined&&k.detailOfMaster.masterDataBeans[s].value!==null){if((k.detailOfMaster.masterDataBeans[o].value.toUpperCase())===(k.detailOfMaster.masterDataBeans[s].value.toUpperCase())){p=true;
break
}}}}}if(!p){for(var n=0;
n<k.refArr.length;
n++){if(k.refArr[n]!==undefined&&k.refArr[n]!==null){k.refArr[n].masterValue.$setValidity("exists",true)
}}}if(r.value!==undefined&&r.value!==null&&r.value.length>0){k.masterValueInUI=r.value.toUpperCase();
var q=0;
for(var n=0;
n<k.detailOfMaster.masterDataBeans.length;
n++){if(k.detailOfMaster.masterDataBeans[n].value){if(k.detailOfMaster.masterDataBeans[n].value.toUpperCase()===k.masterValueInUI){q++;
if(q===2){k.masterValueInDB=true;
l.masterValue.$setValidity("exists",false);
break
}}}}}};
k.refArr=[];
k.getReference=function(m,l){k.refArr.push(m)
};
k.removeReference=function(m){var l;
$.each(k.refArr,function(o,n){if(n===m){l=o
}});
if(l){k.refArr.splice(l,1)
}};
k.saveMaster=function(l){k.submitted=true;
if(k.editMasterForm.editMaster1){if(k.editMasterForm.editMaster1.masterValue.$error.exists){k.submitted=false
}}if(l.$valid){k.submitted=false;
if(k.shortCutInDB===false&&k.masterValueInDB===false&&k.shortCutCodeLength===false){i.maskLoading();
if(k.detailOfMaster.masterDataBeans){var p=[];
angular.forEach(k.detailOfMaster.masterDataBeans,function(r){if(!!(r.shortcutCode&&r.shortcutCode.length>0)){p.push(parseInt(r.shortcutCode))
}});
for(var m=0;
m<k.detailOfMaster.masterDataBeans.length;
m++){var o=k.detailOfMaster.masterDataBeans[m];
o.isArchieve=o.newArchieve;
delete o.newArchieve;
if(!(!!o.shortcutCode)){if(!!(p&&p.length>0)){var q=Math.max.apply(Math,p);
o.shortcutCode=q+1;
p.push(o.shortcutCode)
}else{o.shortcutCode=1;
p.push(parseInt(1))
}}}}else{k.detailOfMaster.masterDataBeans=[]
}var n={code:k.code,isSensitiveMaster:k.isSensitiveMaster,masterDataBeans:k.detailOfMaster.masterDataBeans};
j.update(n,function(r){k.editMasterForm.$dirty=false;
k.cancel();
var t="Master updated successfully";
var s=i.success;
i.addMessage(t,s);
j.retrieveListOfMaster(function(u){u=h(u,["masterName"]);
if(k.selectedType){if(k.selectedType==="1"){k.listOfMaster=angular.copy(k.builtInMaster)
}else{k.listOfMaster=angular.copy(k.customMaster)
}}else{k.listOfMaster=u
}k.masters=angular.copy(k.listOfMaster);
if(k.selected){k.sort(k.selected)
}else{k.selected="All";
k.sort(k.selected)
}k.refArr=[];
i.unMaskLoading()
},function(){var v="Failed to retrieve master";
var u=i.error;
i.addMessage(v,u);
i.unMaskLoading()
})
},function(){var s="Could not update master, please try again.";
var r=i.error;
i.addMessage(s,r);
i.unMaskLoading()
})
}}};
k.cancel=function(){k.passwordPopUpForm.$setPristine();
k.refArr=[];
k.code="";
k.submitted=false;
k.password="";
k.masterValueInDB=false;
k.searchPage=false;
k.isSensitiveMaster=false;
k.inValidPassword=false;
$("#scrollable-dropdown-menu.typeahead").typeahead("val","");
if(k.detailOfMaster&&k.detailOfMaster.lenth>0){k.detailOfMaster.masterDataBeans=[]
}k.masterList=true;
k.editMaster=false;
k.languageList=[]
};
k.retrieve=function(l){j.retrieveDetailsOfMaster({primaryKey:l},function(m){k.detailOfMaster=m;
k.detailOfMasterInDB=angular.copy(m)
})
};
k.isShowEditForm=function(n,l,m){if(n&&n!==""){k.isEditing=l;
k.isCopy=m;
for(var o=0;
o<k.listOfMaster.length;
o++){if(k.listOfMaster[o].code.toUpperCase()===n.toString().toUpperCase()){k.listOfMaster=k.listOfMaster[o]
}}}};
k.filterTypeahead=function(){var l=f("filter")(k.listOfMaster,{masterName:k.searchMaster});
if(l.length>0){k.isInValidSearchDesig=false
}else{k.isInValidSearchDesig=true
}};
k.setSearchflag=function(l){var m=$("#scrollable-dropdown-menu.typeahead").typeahead("val");
if(m.length>0){if(m.length<3){k.searchedMasterRecord=[];
k.searchPage=true
}else{k.searchedMasterRecord=angular.copy(l);
k.searchPage=true;
k.masterList=true
}}};
k.setMasterForEdit=function(n){k.serchedList;
var m={};
if(n.id){m.code=n.id
}else{m.code=n
}for(var l=0;
l<k.masters.length;
l++){if(m.code===k.masters[l].code){m.isSensitiveMaster=k.masters[l].isSensitiveMaster;
m.masterName=k.masters[l].masterName
}}k.showConfirmationPopup(m)
};
k.addRow=function(){k.submitted=false;
if(k.detailOfMaster.masterDataBeans===undefined||k.detailOfMaster.masterDataBeans===null){k.detailOfMaster={};
k.detailOfMaster.masterDataBeans=[];
var l=0
}else{var n=parseInt(k.detailOfMaster.masterDataBeans[0].shortcutCode);
if(!(!!(n))){n=0
}for(var m=1;
m<k.detailOfMaster.masterDataBeans.length;
m++){if(k.detailOfMaster.masterDataBeans[m].shortcutCode!==undefined&&k.detailOfMaster.masterDataBeans[m].shortcutCode!==null){if(n<parseInt(k.detailOfMaster.masterDataBeans[m].shortcutCode)){n=k.detailOfMaster.masterDataBeans[m].shortcutCode
}}}l=parseInt(n)
}k.detailsFilled=true;
k.detailOfMaster.masterDataBeans.push({id:null,code:null,masterName:null,usedInFeature:null,password:null,masterType:null,isSensitiveMaster:false,shortcutCode:l+1,value:null,isOftenUsed:false,valueEntityId:null,masterDataBeans:null,isArchieve:false,newArchieve:false,langaugeIdNameMap:{}})
};
k.removeRow=function(n,m){k.removeReference(m);
var l;
$.each(k.detailOfMaster.masterDataBeans,function(p,o){if(o===n){l=p
}});
if(l){k.detailOfMaster.masterDataBeans.splice(l,1)
}};
k.languageChange=function(l){k.langNotSelected=true;
if(l){k.selectedId=l;
k.langNotSelected=false
}};
k.passwordChange=function(){k.submitted=false
};
k.initAddExceptionForm=function(l){k.addExceptionForm=l
};
k.addExceptionPopup=function(l){i.maskLoading();
k.code=l.code;
k.instanceId=l.id;
k.masterName=l.masterName;
k.editMaster=false;
k.addExceptionPage=true;
k.searchPage=false;
k.masterList=false;
k.valueException={};
k.count=0;
k.initUsers();
k.retrieveValuesFromMasterCode();
if(k.instanceId!==undefined){k.retrievePrerequisite(k.instanceId)
}i.unMaskLoading()
};
k.retrievePrerequisite=function(l){k.finalFieldList=[];
j.retrievePrerequisite(l,function(m){if(m!==undefined&&m!==null&&m.data!==undefined&&m.data!==null){var n=m.data;
if(n.dependentFieldList!==undefined&&n.dependentFieldList!==null&&n.dependentFieldList.length>0){k.fieldDataBeanList=n.dependentFieldList;
if(k.fieldDataBeanList.length>0){angular.forEach(k.fieldDataBeanList,function(p){k.finalFieldList.push({id:p.value+"|"+p.description,text:p.label})
});
if(k.finalFieldList.length>0){k.initDependentOnValues()
}}}if(n.valueExceptionDataBeans!==undefined&&n.valueExceptionDataBeans!==null&&n.valueExceptionDataBeans.length>0){k.finalExceptionList=[];
var o=0;
angular.forEach(n.valueExceptionDataBeans,function(p){p.index=o++;
p.isArchive=false;
k.finalExceptionList.push(p);
k.exceptionList.push(p)
});
if(k.exceptionList!==undefined&&k.exceptionList.length>0){k.tempfinalFieldList=[];
angular.forEach(k.exceptionList,function(p){p.isUpdated=false;
angular.forEach(k.finalFieldList,function(r){if(p.dependentOnField==r.id){var q=0;
angular.forEach(k.tempfinalFieldList,function(s){if(r.id===s.id){q++
}});
if(q===0){k.tempfinalFieldList.push({id:r.id,text:r.text})
}}});
if(k.tempfinalFieldList.length>0){k.finalFieldList=angular.copy(k.tempfinalFieldList);
k.initDependentOnValues()
}})
}}}else{k.initDependentOnValues()
}})
};
k.retrieveValuesFromMasterCode=function(){j.retrieveDetailsOfMaster({primaryKey:k.code},function(l){k.detailOfMaster=l;
k.valueList=[];
k.valueList.push({id:0,text:"All Values"});
k.detailOfMaster.masterDataBeans=h(k.detailOfMaster.masterDataBeans,["-isOftenUsed","shortcutCode","value"]);
if(k.detailOfMaster.masterDataBeans){for(var m=0;
m<k.detailOfMaster.masterDataBeans.length;
m++){var n=k.detailOfMaster.masterDataBeans[m];
n.newArchieve=n.isArchieve;
k.valueList.push({id:n.valueEntityId,text:n.value})
}}if(k.multiValues===undefined){k.initValues()
}else{k.multiValues.data.splice(0,k.multiValues.data.length);
if(k.valueList.length>0){angular.forEach(k.valueList,function(o){k.multiValues.data.push(o)
})
}}i.unMaskLoading()
})
};
k.popover="<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td> \n '@D'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Departments</td></tr> <tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr> <tr><td>'@R'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Designations</td></tr> </table> ";
k.initUsers=function(){k.autoCompleteUsers={multiple:true,closeOnSelect:false,placeholder:"Select Users",initSelection:function(m,q){if(k.editExceptionFlag){var o=[];
var n=k.tempValueException.forUsers.split(",");
var p=k.tempValueException.userToBeDisplay.split(",");
for(var l=0;
l<n.length;
l++){o.push({id:n[l],text:p[l]})
}q(o)
}},formatResult:function(l){return l.text
},formatSelection:function(l){return l.text
},query:function(o){var n=o.term;
k.names=[];
var p=function(q){if(q.length!==0){if(n.substring(0,2)==="@E"||n.substring(0,2)==="@e"){q.push({value:"All",description:"All",label:"All Users"})
}k.names=q;
angular.forEach(q,function(s){var r=0;
angular.forEach(k.forUserList,function(t){if((s.value+":"+s.description)==(t.value+":"+t.description)){r++
}});
if(r===0){k.forUserList.push(s)
}});
angular.forEach(q,function(r){if(r.value==="All"&&r.description==="All"){k.names.push({id:"0:"+r.description,text:r.label})
}else{k.names.push({id:r.value+":"+r.description,text:r.label})
}})
}o.callback({results:k.names})
};
var l=function(){};
if(n.substring(0,2)==="@E"||n.substring(0,2)==="@e"){var m=o.term.slice(2);
g.retrieveUserList(m.trim(),p,l)
}else{if(n.substring(0,2)==="@R"||n.substring(0,2)==="@r"){var m=o.term.slice(2);
g.retrieveRoleList(m.trim(),p,l)
}else{if(n.substring(0,2)==="@D"||n.substring(0,2)==="@d"){var m=o.term.slice(2);
g.retrieveDepartmentList(m.trim(),p,l)
}else{if(n.length>0){var m=n;
g.retrieveUserList(m.trim(),p,l)
}else{o.callback({results:k.names})
}}}}}}
};
k.initValues=function(){k.multiValues={multiple:true,closeOnSelect:false,placeholder:"Select Values",data:k.valueList,initSelection:function(m,q){if(k.editExceptionFlag){var p=[];
var o=k.tempValueException.forValue.split(",");
var n=k.tempValueException.valueToBeDisplay.split(",");
for(var l=0;
l<o.length;
l++){p.push({id:o[l],text:n[l]})
}q(p)
}},formatResult:function(l){return l.text
}}
};
k.initDependentValueList=function(){k.multiDependentValueList={multiple:true,closeOnSelect:false,placeholder:"Select Values",data:k.fieldValueList,initSelection:function(o,r){if(k.editExceptionFlag){var p=[];
if(k.tempValueException!==undefined&&k.tempValueException.dependentOnField!==undefined&&k.tempValueException.dependentOnFieldValues!==undefined&&k.tempValueException.dependentOnFieldValues!==null&&k.tempValueException.dependentOnFieldValues!==""&&k.tempValueException.dependentOnFieldValues instanceof Object===false&&!angular.isArray(k.tempValueException.dependentOnFieldValues)){var q=k.tempValueException.dependentOnFieldValues.split(",");
var l=k.tempValueException.dependentOnFieldValuesToBeDisplay.split(",");
for(var m=0;
m<q.length;
m++){var n=0;
angular.forEach(k.fieldValueList,function(s){if(s.id==q[m]){n++
}});
if(n!==0){p.push({id:q[m],text:l[m].split("-")[1]})
}}r(p)
}}},formatResult:function(l){return l.text
}}
};
k.initDependentOnValues=function(){k.autoCompleteDependentOnFields={multiple:false,closeOnSelect:false,placeholder:"Select dependent On",allowClear:true,data:function(){if(k.finalFieldList.length>0){return{results:k.finalFieldList}
}else{return{results:[]}
}},formatResult:function(l){return l.text
}}
};
k.$watch("valueException.dependentOnField",function(){if(k.valueException.dependentOnField!==undefined&&k.valueException.dependentOnField instanceof Object===true&&angular.isArray(k.valueException.dependentOnField)){}else{if(k.fieldValueList!==undefined){k.fieldValueList.splice(0,k.fieldValueList.length)
}k.retrieveFieldValues()
}});
k.retrieveFieldValues=function(){if(k.valueException.dependentOnField!==undefined&&k.valueException.dependentOnField!==null&&k.valueException.dependentOnField instanceof Object===false&&k.valueException.dependentOnField!==""){k.dependentOn=k.valueException.dependentOnField.split("|");
k.payload={fieldId:k.dependentOn[0],componentType:k.dependentOn[1]};
j.retrieveCustomFieldsValueByKey(k.payload,function(l){k.fieldValueList=[];
if(l!==undefined&&l.data!==undefined&&l.data!==null&&l.data.length>0){angular.forEach(l.data,function(m){k.fieldValueList.push({id:m.value,text:m.label})
});
k.initDependentValueList()
}})
}};
k.retrieveFieldValuesForDisplay=function(l){k.payload={fieldId:l[0],componentType:l[1]};
j.retrieveCustomFieldsValueByKey(k.payload,function(m){k.fieldVals=[];
if(m!==undefined&&m.data!==undefined&&m.data!==null&&m.data.length>0){angular.forEach(m.data,function(n){k.fieldVals.push({id:n.value,text:n.label})
})
}})
};
k.addException=function(o){k.submitted=true;
if(!o.$valid){}else{if(((k.valueException.dependentOnFieldValues!==undefined&&k.valueException.dependentOnFieldValues!==null&&k.valueException.dependentOnFieldValues!=="")||(k.valueException.forUsers!==undefined&&k.valueException.forUsers!==null&&k.valueException.forUsers!==""))){k.submitted=false;
k.count=k.finalExceptionList.length;
k.valueException.index=k.count++;
k.valueException.instanceId=k.instanceId;
k.finalExceptionList.push(angular.copy(k.valueException));
k.valueExceptionToBeDisplayed=angular.copy(k.valueException);
if(k.valueException.forUsers!==undefined&&k.forUserList.length>0){var t=k.valueException.forUsers.split(",");
var r="";
for(var n=0;
n<t.length;
n++){angular.forEach(k.forUserList,function(v){var u;
if(v.value==="All"){u="0:"+v.description
}else{u=v.value+":"+v.description
}if(t[n]===u){if(r===""){r=r+v.label
}else{r=r+" , "+v.label
}}})
}k.valueExceptionToBeDisplayed.userToBeDisplay=r
}if(k.valueException.forValue!==undefined&&k.valueList.length>0){var s=k.valueException.forValue.split(",");
var l="";
for(var n=0;
n<s.length;
n++){angular.forEach(k.valueList,function(v){var u=v.id;
if(s[n]==u){if(l===""){l=l+v.text
}else{l=l+" , "+v.text
}}})
}k.valueExceptionToBeDisplayed.valueToBeDisplay=l
}if(k.valueException.dependentOnField!==undefined&&k.finalFieldList.length>0){var q="";
angular.forEach(k.finalFieldList,function(u){var v=u.id;
if(k.valueException.dependentOnField==v){if(q===""){q=q+u.text
}else{q=q+" , "+u.text
}}});
k.valueExceptionToBeDisplayed.dependentOnToBeDisplay=q
}if(k.valueException.dependentOnFieldValues!==undefined&&k.fieldValueList.length>0){var m=k.valueException.dependentOnFieldValues.split(",");
var p="";
for(var n=0;
n<m.length;
n++){angular.forEach(k.fieldValueList,function(v){var u=v.id;
if(m[n]==u){if(p===""){p=p+k.valueExceptionToBeDisplayed.dependentOnToBeDisplay+"-"+v.text
}else{p=p+" , "+k.valueExceptionToBeDisplayed.dependentOnToBeDisplay+"-"+v.text
}}})
}k.valueExceptionToBeDisplayed.dependentOnFieldValuesToBeDisplay=p
}k.valueExceptionToBeDisplayed.isArchive=false;
k.exceptionList.push(angular.copy(k.valueExceptionToBeDisplayed));
k.cancelException(o)
}else{i.addMessage("Select For User or Dependent On to apply Exception",i.failure);
k.submitted=false
}}};
k.removeException=function(l){l.isArchive=true
};
k.undoException=function(l){l.isArchive=false
};
k.cancelExceptionPage=function(){k.passwordPopUpForm.$setPristine();
k.masterName="";
k.editMaster=false;
k.addExceptionPage=false;
k.editExceptionFlag=false;
k.valueException={};
k.exceptionList=[];
k.count=0;
k.code="";
k.forUserList=[];
k.submitted=false;
k.masterValueInDB=false;
k.searchPage=false;
k.isSensitiveMaster=false;
if(k.addExceptionForm!==undefined){k.addExceptionForm.$setPristine()
}$("#scrollable-dropdown-menu.typeahead").typeahead("val","");
if(k.detailOfMaster&&k.detailOfMaster.lenth>0){k.detailOfMaster.masterDataBeans=[]
}k.masterList=true;
k.editMaster=false;
k.languageList=[]
};
k.cancelException=function(){k.valueException={};
k.submitted=false;
k.addExceptionForm.$setPristine();
$("#forUser").select2("data","");
$("#forValue").select2("data","");
$("#dependentOnField").select2("data","");
k.editExceptionFlag=false;
if(k.exceptionList!==undefined&&k.exceptionList.length>0){k.tempfinalFieldList=[];
angular.forEach(k.exceptionList,function(l){l.isUpdated=false;
angular.forEach(k.finalFieldList,function(n){if(l.dependentOnField==n.id){var m=0;
angular.forEach(k.tempfinalFieldList,function(o){if(n.id===o.id){m++
}});
if(m===0){k.tempfinalFieldList.push({id:n.id,text:n.text})
}}});
if(k.tempfinalFieldList.length>0){k.finalFieldList=angular.copy(k.tempfinalFieldList);
k.initDependentOnValues()
}})
}else{k.cancelExceptionPage()
}};
function e(l){return $.grep(l,function(m){return m.isArchive==false||(m.isArchive==true&&m.id!==undefined&&m.id!==null)
})
}k.saveException=function(){if(k.exceptionList!==undefined&&k.exceptionList.length>0){k.listToSend=[];
k.finalListToSend=[];
k.listToSend=e(k.exceptionList);
angular.forEach(k.listToSend,function(l){angular.forEach(k.finalExceptionList,function(m){if(l.index==m.index){k.finalListToSend.push(m)
}})
});
j.saveException(k.finalListToSend,function(){console.log("sucess");
k.cancelExceptionPage()
})
}};
k.retrieveExceptionForUpdate=function(l){k.editExceptionFlag=true;
angular.forEach(k.finalExceptionList,function(x){if(x.index==l.index){k.send={};
k.uiSelectRecipients=[];
l.isUpdated=true;
k.valueException=angular.copy(x);
k.tempValueException=angular.copy(l);
if(l.forUsers!==undefined&&l.forUsers!==null&&l.forUsers!==""){var t=l.forUsers.split(",");
var n=l.userToBeDisplay.split(",");
var m=[];
for(var s=0;
s<t.length;
s++){m.push({id:t[s],text:n[s]})
}$("#forUser").select2("val",m)
}var u=l.forValue.split(",");
var v=l.valueToBeDisplay.split(",");
var o=[];
for(var q=0;
q<u.length;
q++){o.push({id:u[q],text:v[q]})
}if(l.dependentOnFieldValues!==undefined&&l.dependentOnFieldValues!==null&&l.dependentOnFieldValues!==""){var p=l.dependentOnFieldValues.split(",");
var w=l.dependentOnFieldValuesToBeDisplay.split(",");
var r=[];
for(var q=0;
q<p.length;
q++){r.push({id:p[q],text:w[q]})
}$("#dependentOnFieldValues").select2("val",r)
}$("#forValue").select2("val",o);
$("#dependentOnField").select2("val",l.dependentOnField)
}})
};
k.updateException=function(m){k.submitted=true;
if(!m.$valid){k.submitted=false
}else{if(((k.valueException.dependentOnFieldValues!==undefined&&k.valueException.dependentOnFieldValues!==null&&k.valueException.dependentOnFieldValues!=="")||(k.valueException.forUsers!==undefined&&k.valueException.forUsers!==null&&k.valueException.forUsers!==""))){k.tempValueExceptionObj=angular.copy(k.valueException);
if(k.tempValueExceptionObj.forUsers!==undefined){var q="";
if(angular.isArray(k.tempValueExceptionObj.forUsers)){var o="";
angular.forEach(k.tempValueExceptionObj.forUsers,function(s){if(o!==""){o=o+","+s.id
}else{o=o+s.id
}});
q=o
}else{q=k.tempValueExceptionObj.forUsers
}k.tempValueExceptionObj.forUsers=q
}if(k.tempValueExceptionObj.forValue!==undefined){var r="";
if(angular.isArray(k.tempValueExceptionObj.forValue)){var l="";
angular.forEach(k.tempValueExceptionObj.forValue,function(s){if(l!==""){l=l+","+s.id
}else{l=l+s.id
}});
r=l
}else{r=k.tempValueExceptionObj.forValue
}k.tempValueExceptionObj.forValue=r
}if(k.tempValueExceptionObj.dependentOnFieldValues!==undefined){var n="";
if(angular.isArray(k.tempValueExceptionObj.dependentOnFieldValues)){var p="";
angular.forEach(k.tempValueExceptionObj.dependentOnFieldValues,function(s){if(p!==""){p=p+","+s.id
}else{p=p+s.id
}});
n=p
}else{n=k.tempValueExceptionObj.dependentOnFieldValues
}k.tempValueExceptionObj.dependentOnFieldValues=n
}angular.forEach(k.finalExceptionList,function(s){if(k.tempValueExceptionObj.index==s.index){s.forUsers=k.tempValueExceptionObj.forUsers;
s.forValue=k.tempValueExceptionObj.forValue;
s.dependentOnField=k.tempValueExceptionObj.dependentOnField;
s.dependentOnFieldValues=k.tempValueExceptionObj.dependentOnFieldValues
}});
angular.forEach(k.exceptionList,function(t){if(t.index==k.tempValueExceptionObj.index){t.isUpdated=false;
k.tempValueExceptionObj.isUpdated=false;
t.forUsers=k.tempValueExceptionObj.forUsers;
t.forValue=k.tempValueExceptionObj.forValue;
t.dependentOnField=k.tempValueExceptionObj.dependentOnField;
t.dependentOnFieldValues=k.tempValueExceptionObj.dependentOnFieldValues;
if(k.tempValueExceptionObj.forUsers!==undefined&&k.forUserList.length>0){var B="";
if(angular.isArray(k.tempValueExceptionObj.forUsers)){var x="";
angular.forEach(k.tempValueExceptionObj.forUsers,function(E){if(x!==""){x=x+","+E.id
}else{x=x+E.id
}});
B=x.split(",")
}else{B=k.tempValueExceptionObj.forUsers.split(",")
}var z="";
for(var v=0;
v<B.length;
v++){angular.forEach(k.forUserList,function(F){var E=F.value+":"+F.description;
if(B[v]===E){if(z===""){z=z+F.label
}else{z=z+" , "+F.label
}}})
}t.userToBeDisplay=z
}if(k.tempValueExceptionObj.forValue!==undefined&&k.valueList.length>0){var A="";
if(angular.isArray(k.tempValueExceptionObj.forValue)){var C="";
angular.forEach(k.tempValueExceptionObj.forValue,function(E){if(C!==""){C=C+","+E.id
}else{C=C+E.id
}});
A=C.split(",")
}else{A=k.tempValueExceptionObj.forValue.split(",")
}var s="";
for(var v=0;
v<A.length;
v++){angular.forEach(k.valueList,function(F){var E=F.id;
if(A[v]==E){if(s===""){s=s+F.text
}else{s=s+" , "+F.text
}}})
}t.valueToBeDisplay=s
}if(k.tempValueExceptionObj.dependentOnField!==undefined&&k.finalFieldList.length>0){var y="";
angular.forEach(k.finalFieldList,function(E){var F=E.id;
if(k.tempValueExceptionObj.dependentOnField==F){if(y===""){y=y+E.text
}else{y=y+" , "+E.text
}}});
t.dependentOnToBeDisplay=y
}if(k.tempValueExceptionObj.dependentOnFieldValues!==undefined&&k.fieldValueList.length>0){var u="";
if(angular.isArray(k.tempValueExceptionObj.dependentOnFieldValues)){var D="";
angular.forEach(k.tempValueExceptionObj.dependentOnFieldValues,function(E){if(D!==""){D=D+","+E.id
}else{D=D+E.id
}});
u=D.split(",")
}else{u=k.tempValueExceptionObj.dependentOnFieldValues.split(",")
}var w="";
for(var v=0;
v<u.length;
v++){angular.forEach(k.fieldValueList,function(F){var E=F.id;
if(u[v]==E){if(w===""){w=w+t.dependentOnToBeDisplay+"-"+F.text
}else{w=w+" , "+t.dependentOnToBeDisplay+"-"+F.text
}}})
}t.dependentOnFieldValuesToBeDisplay=w
}}});
k.cancelException(m);
k.editExceptionFlag=false
}else{i.addMessage("Select For User or Dependent On to Apply Exception",i.failure);
k.submitted=false
}}}
}])
});