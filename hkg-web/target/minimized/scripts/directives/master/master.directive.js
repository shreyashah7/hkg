define(["angular"],function(){globalProvider.compileProvider.directive("addMasterValue",["$parse","$compile","$rootScope","$filter","MasterService","CenterMasterService","MessagingService","$modal",function(a,b,h,e,k,g,d,i){var j={modelValue:"=",records:"=",masterCode:"@"};
var f=function(n,m,l){};
var c=["$scope","$element","$attrs",function(s,t,r){s.allMasters=[];
s.flags={};
s.forms={};
s.langNotSelected=true;
s.currentMaster={};
s.shortCutInDB=false;
s.masterValueInDB=false;
s.separator="_A_";
s.count=0;
s.forUserList=[];
s.exceptionList=[];
s.finalExceptionList=[];
s.valueException={};
function m(u){return u.replace(/(!|"|#|\$|%|\'|\(|\)|\*|\+|\,|\.|\/|\:|\;|\?|@)/g,function(v,w){return"\\"+w
})
}if(angular.isDefined(r.elementId)){s.elementId=r.elementId;
s.elementId=m(s.elementId)
}else{s.elementId=null
}if(angular.isDefined(r.isShortcutRequired)){try{s.isShortcutRequired=JSON.parse(r.isShortcutRequired.toLowerCase())
}catch(n){s.isShortcutRequired=false;
console.log("Can not parse isShortcutRequired"+n)
}}else{s.isShortcutRequired=false
}if(angular.isDefined(r.isCheckBox)){try{s.isCheckBox=JSON.parse(r.isCheckBox.toLowerCase())
}catch(n){s.isCheckBox=false;
console.log("Can not parse isCheckBox"+n)
}}else{s.isCheckBox=false
}if(angular.isDefined(r.modalName)){s.modalName=r.modalName;
if(r.modalName===""){s.modalName=null
}}else{s.modalName=null
}if(angular.isDefined(r.isCustom)){try{s.isCustom=JSON.parse(r.isCustom.toLowerCase())
}catch(n){s.isCustom=false;
console.log("Can not parse isCustom"+n)
}}else{s.isCustom=false
}if(angular.isDefined(r.isObject)){try{s.isObject=JSON.parse(r.isObject.toLowerCase())
}catch(n){s.isObject=false;
console.log("Can not parse isObject"+n)
}}else{s.isObject=false
}if(angular.isDefined(r.isMultiselect)){try{s.isMultiselect=JSON.parse(r.isMultiselect.toLowerCase())
}catch(n){s.isMultiselect=false;
console.log("Can not parse isMultiselect"+n)
}}else{s.isMultiselect=false
}if(angular.isDefined(r.isDropdown)){try{s.isDropdown=JSON.parse(r.isDropdown.toLowerCase())
}catch(n){s.isDropdown=false;
console.log("Can not parse isDropdown"+n)
}}else{s.isDropdown=false
}if(angular.isDefined(r.isDiamond)){s.isDiamond=r.isDiamond
}else{s.isDiamond=false
}var p;
p=k;
var q=e("orderBy");
s.initMasterChange=function(){s.submitted=false;
s.valueSubmitted=false;
s.flags={passwordEmpty:false,invalidPassword:false,shortcutExists:false,valueEmpty:false,valueExists:false};
if(s.records===undefined||s.records===null){s.records=[]
}if((s.masterCode!==undefined&&s.masterCode!==null&&s.masterCode!=="")){h.maskLoading();
p.retrieveListOfMaster(function(u){s.allMasters=u;
p.retrieveDetailsOfMaster({primaryKey:s.masterCode},function(v){h.unMaskLoading();
s.detailOfMaster=v;
s.shortCutCodes=[];
if(s.detailOfMaster.masterDataBeans===null){s.detailOfMaster.masterDataBeans=[]
}angular.forEach(s.detailOfMaster.masterDataBeans,function(w){s.shortCutCodes.push(w.shortcutCode)
});
angular.forEach(s.allMasters,function(w){if(w.code==s.masterCode){s.masterName=w.masterName;
s.showConfirmationPopup(w)
}});
s.addExceptionPopup(s.detailOfMaster)
},function(v){h.unMaskLoading()
})
},function(u){var w="Could not retrieve master, please try again";
var v=h.error;
h.addMessage(w,v);
h.unMaskLoading()
})
}};
s.passwordChange=function(){s.submitted=false;
s.flags.invalidPassword=false;
s.flags.passwordEmpty=false;
if(s.password===undefined||s.password===null||s.password===""){s.flags.passwordEmpty=true
}};
s.proceed=function(){s.submitted=false;
var u=s.password;
s.flags.passwordEmpty=false;
if(u!==undefined&&u!==null&&u.length>0){h.maskLoading();
p.authenticateForEditMaster(u,function(v){h.unMaskLoading();
if(v.data){s.flags.invalidPassword=false;
s.editMasterDetail();
s.showPopup=false
}else{s.flags.invalidPassword=true;
s.showPopup=true
}},function(v){h.unMaskLoading()
})
}else{s.flags.passwordEmpty=true
}};
function l(){setTimeout(function(){$(t).siblings().find("select:first, input:first").focus();
$(window).scrollTop($(t).siblings().find("select:first, input:first").offset())
})
}s.showConfirmationPopup=function(v){s.doNotScroll=false;
s.serchedList;
s.searchPage=false;
s.code=v.code;
s.masterName=v.masterName;
s.isSensitiveMaster=v.isSensitiveMaster;
if(v.isSensitiveMaster===true){s.showPopup=true;
var u="passwordPopUp.html";
s.passwordModal=i.open({templateUrl:u,scope:s,size:"lg"});
s.passwordModal.result.then(function(){},function(){if(s.doNotScroll!==true){l()
}})
}else{s.showPopup=false;
var u="passwordPopUp.html";
s.passwordModal=i.open({templateUrl:u,scope:s,size:"lg"});
s.passwordModal.result.then(function(){},function(){if(s.doNotScroll!==true){l()
}});
s.editMasterDetail()
}};
s.hideConfirmationPopup=function(){s.password="";
s.submitted=false;
s.inValidPassword=false;
s.flags={};
s.passwordModal.dismiss();
s.cancelExceptionPage();
h.removeModalOpenCssAfterModalHide()
};
s.languageChange=function(u){s.langNotSelected=true;
if(u){s.selectedId=u;
s.langNotSelected=false
}};
s.editMasterDetail=function(){s.submitted=false;
s.currentMaster={};
s.currentMaster.langaugeIdNameMap={};
if(s.shortCutCodes.length>0){var u=Math.max.apply(Math,s.shortCutCodes);
s.currentMaster.shortcutCode=u+1
}else{s.currentMaster.shortcutCode=1
}};
s.masterValueExists=function(x,u){s.masterValueInDB=false;
s.submitted=false;
s.valueSubmitted=false;
if(s.forms.editMasterForm.editMaster1){s.forms.editMasterForm.editMaster1.masterValue.$setValidity("exists",true)
}if(x.value!==undefined&&x.value!==null&&x.value.length>0){s.masterValueInUI=x.value.toUpperCase().trim();
var w=0;
for(var v=0;
v<s.detailOfMaster.masterDataBeans.length;
v++){if(s.detailOfMaster.masterDataBeans[v].value){if(s.detailOfMaster.masterDataBeans[v].value.toUpperCase().trim()===s.masterValueInUI){w++;
if(w===1){s.masterValueInDB=true;
s.forms.editMasterForm.editMaster1.masterValue.$setValidity("exists",false);
break
}}}}}if(s.forms.editMasterForm.editMaster1.$valid){var w=0;
angular.forEach(s.valueList,function(y){if(y.id==-1){y.text=x.value;
w++
}});
if(w==0){s.valueList.push({id:-1,text:x.value})
}}};
s.shortCutExists=function(w){s.shortCutInDB=false;
s.shortCutInUI=w.shortcutCode;
var v=0;
s.flags.shortcutExists=false;
if(s.shortCutInUI&&s.shortCutInUI!==""){for(var u=0;
u<s.detailOfMaster.masterDataBeans.length;
u++){if(s.detailOfMaster.masterDataBeans[u].shortcutCode){if(s.detailOfMaster.masterDataBeans[u].shortcutCode.toString()===s.shortCutInUI){v++;
if(v===1){s.shortCutInDB=true;
s.flags.shortcutExists=true;
break
}}}}}};
s.saveMaster=function(){console.log(s.forms.editMasterForm.editMaster1);
s.submitted=true;
s.valueSubmitted=true;
if(s.forms.editMasterForm.editMaster1){if(s.forms.editMasterForm.editMaster1.masterValue.$error.exists){s.valueSubmitted=false
}}if(s.forms.editMasterForm.$valid){s.valueSubmitted=false;
s.submitted=false;
if(s.shortCutInDB===false&&s.masterValueInDB===false){h.maskLoading();
var w=[];
angular.forEach(s.detailOfMaster.masterDataBeans,function(y){if(y.shortcutCode!==undefined&&y.shortcutCode!==null&&y.shortcutCode!==""){w.push(parseInt(y.shortcutCode))
}});
var v=s.currentMaster;
v.isArchieve=false;
delete v.newArchieve;
if(!(!!v.shortcutCode)){if(!!(w&&w.length>0)){var x=Math.max.apply(Math,w);
v.shortcutCode=x+1;
w.push(v.shortcutCode)
}else{v.shortcutCode=1;
w.push(parseInt(1))
}}s.detailOfMaster.masterDataBeans.push(v);
var u={code:s.code,isSensitiveMaster:s.isSensitiveMaster,masterDataBeans:s.detailOfMaster.masterDataBeans};
p.update(u,function(y){p.retrieveDetailsOfMaster({primaryKey:s.masterCode},function(A){h.unMaskLoading();
s.detailOfMaster=A;
s.shortCutCodes=[];
angular.forEach(s.detailOfMaster.masterDataBeans,function(L){s.shortCutCodes.push(L.shortcutCode)
});
s.detailOfMaster.masterDataBeans=e("orderBy")(s.detailOfMaster.masterDataBeans,["-isOftenUsed","shortcutCode","value"]);
angular.forEach(s.detailOfMaster.masterDataBeans,function(L){if(L.shortcutCode==s.currentMaster.shortcutCode){s.valueEntityId=L.valueEntityId
}});
var K=null;
if(s.isObject){angular.forEach(s.detailOfMaster.masterDataBeans,function(M){if(M.shortcutCode==s.currentMaster.shortcutCode){var L={};
L.id=M.valueEntityId;
L.text=M.value;
if(L.text.length>h.maxValueForTruncate){L.text=L.text.substring(0,h.maxValueForTruncate)+"..."
}L.displayName=M.value;
K=L;
s.records.push(L)
}})
}else{if(!s.isCustom){if(s.isShortcutRequired){if(!s.isCheckBox){angular.forEach(s.detailOfMaster.masterDataBeans,function(M){if(M.shortcutCode==s.currentMaster.shortcutCode){var L={};
L.value=M.valueEntityId;
L.label=h.translateValue(s.masterCode+"."+M.value);
if(L.label.length>h.maxValueForTruncate){L.label=L.label.substring(0,h.maxValueForTruncate)+"..."
}L.shortcutCode=M.shortcutCode;
K=M.valueEntityId;
s.records.push(L)
}})
}else{angular.forEach(s.detailOfMaster.masterDataBeans,function(M){if(M.shortcutCode==s.currentMaster.shortcutCode){var L={};
L.value=M.valueEntityId;
L.label=h.translateValue(s.masterCode+"."+M.value);
if(L.label.length>h.maxValueForTruncate){L.label=L.label.substring(0,h.maxValueForTruncate)+"..."
}L.shortcutCode=M.shortcutCode;
L.isActive=true;
s.records.push(L)
}})
}}else{angular.forEach(s.detailOfMaster.masterDataBeans,function(M){if(M.shortcutCode==s.currentMaster.shortcutCode){var L={};
L.id=M.valueEntityId;
L.text=M.value;
if(L.text.length>h.maxValueForTruncate){L.text=L.text.substring(0,h.maxValueForTruncate)+"..."
}L.shortcutCode=M.shortcutCode;
K=M.valueEntityId;
s.records.push(M)
}})
}}else{if(s.isMultiselect){var I=[];
var z=[];
angular.forEach(s.records,function(L){var M=false;
angular.forEach(z,function(N){if(L.id===N.id){M=true
}});
if(!M){z.push(L)
}});
if(s.modelValue!==null&&s.modelValue!==undefined&&s.modelValue!==""&&!(s.modelValue instanceof Object)){var E=s.modelValue.toString().split(",");
angular.forEach(E,function(L){angular.forEach(z,function(M){if(parseInt(M.id)===parseInt(L.trim())){I.push(M)
}})
})
}angular.forEach(s.detailOfMaster.masterDataBeans,function(M){if(M.shortcutCode==s.currentMaster.shortcutCode){var L={};
L.id=M.valueEntityId;
L.text=M.shortcutCode+"-"+M.value;
if(L.text.length>h.maxValueForTruncate){L.text=L.text.substring(0,h.maxValueForTruncate)+"..."
}I.push(L);
s.records.push(L)
}});
$("#"+s.elementId).select2("data",I);
if(I.length>0){var F=null;
angular.forEach(I,function(L){if(F===null){F=L.id
}else{F+=","+L.id
}});
K=F
}}else{angular.forEach(s.detailOfMaster.masterDataBeans,function(M){if(M.shortcutCode==s.currentMaster.shortcutCode){var L={};
L.id=M.valueEntityId;
L.text=M.shortcutCode+"-"+M.value;
if(L.text.length>h.maxValueForTruncate){L.text=L.text.substring(0,h.maxValueForTruncate)+"..."
}K=M.valueEntityId;
s.records.push(L)
}});
if(s.isDropdown){var J=s.separator+s.masterCode+s.separator;
var G="passwordPopUp"+s.separator+s.masterCode+s.separator+(s.modalName!==null?s.modalName:"");
var D=document.getElementsByTagName("*");
for(var C=D.length;
C--;
){if($(D[C])[0]!==$(t)[0]){var B=$(D[C]).attr("id");
if(B!==undefined){if(B.toString().indexOf(J)>-1&&B.toString()!==G){var H=$(D[C]).closest("[ng-controller]");
angular.element(H).scope().updateDropdownList(s.records)
}}}}}}}}s.saveException();
if(!s.isCheckBox){s.modelValue=K
}s.submitted=false;
s.hideConfirmationPopup()
},function(z){h.unMaskLoading()
})
},function(){s.doNotScroll=true;
s.hideConfirmationPopup();
var z="Could not update master, please try again.";
var y=h.warning;
h.addMessage(z,y);
h.unMaskLoading()
})
}}};
s.canAccess=function(u){return h.canAccess(u)
};
s.initAddExceptionForm=function(u){s.addExceptionForm=u
};
s.addExceptionPopup=function(u){h.maskLoading();
s.code=u.code;
s.instanceId=u.id;
s.masterName=u.masterName;
s.editMaster=false;
s.addExceptionPage=true;
s.searchPage=false;
s.masterList=false;
s.valueException={};
s.count=0;
s.initUsers();
s.retrieveValuesFromMasterCode();
if(s.instanceId!==undefined){s.retrievePrerequisite(s.instanceId)
}h.unMaskLoading()
};
s.retrievePrerequisite=function(u){s.finalFieldList=[];
p.retrievePrerequisite(u,function(v){if(v!==undefined&&v!==null&&v.data!==undefined&&v.data!==null){var w=v.data;
if(w.dependentFieldList!==undefined&&w.dependentFieldList!==null&&w.dependentFieldList.length>0){s.fieldDataBeanList=w.dependentFieldList;
if(s.fieldDataBeanList.length>0){angular.forEach(s.fieldDataBeanList,function(y){s.finalFieldList.push({id:y.value+"|"+y.description,text:y.label})
});
if(s.finalFieldList.length>0){s.initDependentOnValues()
}}}if(w.valueExceptionDataBeans!==undefined&&w.valueExceptionDataBeans!==null&&w.valueExceptionDataBeans.length>0){s.finalExceptionList=[];
var x=0;
angular.forEach(w.valueExceptionDataBeans,function(y){y.index=x++;
y.isArchive=false;
s.finalExceptionList.push(y);
s.exceptionList.push(y)
});
if(s.exceptionList!==undefined&&s.exceptionList.length>0){s.tempfinalFieldList=[];
angular.forEach(s.exceptionList,function(y){y.isUpdated=false;
angular.forEach(s.finalFieldList,function(A){if(y.dependentOnField==A.id){var z=0;
angular.forEach(s.tempfinalFieldList,function(B){if(A.id===B.id){z++
}});
if(z===0){s.tempfinalFieldList.push({id:A.id,text:A.text})
}}});
if(s.tempfinalFieldList.length>0){s.finalFieldList=angular.copy(s.tempfinalFieldList);
s.initDependentOnValues()
}})
}}}else{s.initDependentOnValues()
}})
};
s.retrieveValuesFromMasterCode=function(){p.retrieveDetailsOfMaster({primaryKey:s.code},function(u){s.detailOfMasters=u;
s.valueList=[];
s.valueList.push({id:0,text:"All Values"});
s.detailOfMasters.masterDataBeans=q(s.detailOfMasters.masterDataBeans,["-isOftenUsed","shortcutCode","value"]);
if(s.detailOfMasters.masterDataBeans){for(var v=0;
v<s.detailOfMasters.masterDataBeans.length;
v++){var w=s.detailOfMasters.masterDataBeans[v];
w.newArchieve=w.isArchieve;
s.valueList.push({id:w.valueEntityId,text:w.value})
}}if(s.multiValues===undefined){s.initValues()
}else{s.multiValues.data.splice(0,s.multiValues.data.length);
if(s.valueList.length>0){angular.forEach(s.valueList,function(x){s.multiValues.data.push(x)
})
}}h.unMaskLoading()
})
};
s.popover="<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td> \n '@D'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Departments</td></tr> <tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr> <tr><td>'@R'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Designations</td></tr> </table> ";
s.initUsers=function(){s.autoCompleteUsers={multiple:true,closeOnSelect:false,placeholder:"Select Users",initSelection:function(v,z){if(s.editExceptionFlag){var x=[];
var w=s.tempValueException.forUsers.split(",");
var y=s.tempValueException.userToBeDisplay.split(",");
for(var u=0;
u<w.length;
u++){x.push({id:w[u],text:y[u]})
}z(x)
}},formatResult:function(u){return u.text
},formatSelection:function(u){return u.text
},query:function(x){var w=x.term;
s.names=[];
var y=function(z){if(z.length!==0){if(w.substring(0,2)==="@E"||w.substring(0,2)==="@e"){z.push({value:"All",description:"All",label:"All Users"})
}s.names=z;
angular.forEach(z,function(B){var A=0;
angular.forEach(s.forUserList,function(C){if((B.value+":"+B.description)==(C.value+":"+C.description)){A++
}});
if(A===0){s.forUserList.push(B)
}});
angular.forEach(z,function(A){if(A.value==="All"&&A.description==="All"){s.names.push({id:"0:"+A.description,text:A.label})
}else{s.names.push({id:A.value+":"+A.description,text:A.label})
}})
}x.callback({results:s.names})
};
var u=function(){};
if(w.substring(0,2)==="@E"||w.substring(0,2)==="@e"){var v=x.term.slice(2);
d.retrieveUserList(v.trim(),y,u)
}else{if(w.substring(0,2)==="@R"||w.substring(0,2)==="@r"){var v=x.term.slice(2);
d.retrieveRoleList(v.trim(),y,u)
}else{if(w.substring(0,2)==="@D"||w.substring(0,2)==="@d"){var v=x.term.slice(2);
d.retrieveDepartmentList(v.trim(),y,u)
}else{if(w.length>0){var v=w;
d.retrieveUserList(v.trim(),y,u)
}else{x.callback({results:s.names})
}}}}}}
};
s.initValues=function(){s.multiValues={multiple:true,closeOnSelect:false,placeholder:"Select Values",data:s.valueList,initSelection:function(v,z){if(s.editExceptionFlag){var y=[];
var x=s.tempValueException.forValue.split(",");
var w=s.tempValueException.valueToBeDisplay.split(",");
for(var u=0;
u<x.length;
u++){y.push({id:x[u],text:w[u]})
}z(y)
}},formatResult:function(u){return u.text
}}
};
s.initDependentValueList=function(){s.multiDependentValueList={multiple:true,closeOnSelect:false,placeholder:"Select Values",data:s.fieldValueList,initSelection:function(x,A){if(s.editExceptionFlag){var y=[];
if(s.tempValueException!==undefined&&s.tempValueException.dependentOnField!==undefined&&s.tempValueException.dependentOnFieldValues!==undefined&&s.tempValueException.dependentOnFieldValues!==null&&s.tempValueException.dependentOnFieldValues!==""&&s.tempValueException.dependentOnFieldValues instanceof Object===false&&!angular.isArray(s.tempValueException.dependentOnFieldValues)){var z=s.tempValueException.dependentOnFieldValues.split(",");
var u=s.tempValueException.dependentOnFieldValuesToBeDisplay.split(",");
for(var v=0;
v<z.length;
v++){var w=0;
angular.forEach(s.fieldValueList,function(B){if(B.id==z[v]){w++
}});
if(w!==0){y.push({id:z[v],text:u[v].split("-")[1]})
}}A(y)
}}},formatResult:function(u){return u.text
}}
};
s.initDependentOnValues=function(){s.autoCompleteDependentOnFields={multiple:false,closeOnSelect:false,placeholder:"Select dependent On",allowClear:true,data:function(){if(s.finalFieldList.length>0){return{results:s.finalFieldList}
}else{return{results:[]}
}},formatResult:function(u){return u.text
}}
};
s.$watch("valueException.dependentOnField",function(){if(s.valueException.dependentOnField!==undefined&&s.valueException.dependentOnField instanceof Object===true&&angular.isArray(s.valueException.dependentOnField)){}else{if(s.fieldValueList!==undefined){s.fieldValueList.splice(0,s.fieldValueList.length)
}s.retrieveFieldValues()
}});
s.retrieveFieldValues=function(){if(s.valueException.dependentOnField!==undefined&&s.valueException.dependentOnField!==null&&s.valueException.dependentOnField instanceof Object===false&&s.valueException.dependentOnField!==""){s.dependentOn=s.valueException.dependentOnField.split("|");
s.payload={fieldId:s.dependentOn[0],componentType:s.dependentOn[1]};
p.retrieveCustomFieldsValueByKey(s.payload,function(u){s.fieldValueList=[];
if(u!==undefined&&u.data!==undefined&&u.data!==null&&u.data.length>0){angular.forEach(u.data,function(v){s.fieldValueList.push({id:v.value,text:v.label})
});
s.initDependentValueList()
}})
}};
s.retrieveFieldValuesForDisplay=function(u){s.payload={fieldId:u[0],componentType:u[1]};
p.retrieveCustomFieldsValueByKey(s.payload,function(v){s.fieldVals=[];
if(v!==undefined&&v.data!==undefined&&v.data!==null&&v.data.length>0){angular.forEach(v.data,function(w){s.fieldVals.push({id:w.value,text:w.label})
})
}})
};
s.addException=function(x){s.submitted=true;
if(!x.$valid){}else{if(((s.valueException.dependentOnFieldValues!==undefined&&s.valueException.dependentOnFieldValues!==null&&s.valueException.dependentOnFieldValues!=="")||(s.valueException.forUsers!==undefined&&s.valueException.forUsers!==null&&s.valueException.forUsers!==""))){s.submitted=false;
s.count=s.finalExceptionList.length;
s.valueException.index=s.count++;
s.valueException.instanceId=s.instanceId;
s.finalExceptionList.push(angular.copy(s.valueException));
s.valueExceptionToBeDisplayed=angular.copy(s.valueException);
if(s.valueException.forUsers!==undefined&&s.forUserList.length>0){var C=s.valueException.forUsers.split(",");
var A="";
for(var w=0;
w<C.length;
w++){angular.forEach(s.forUserList,function(E){var D;
if(E.value==="All"){D="0:"+E.description
}else{D=E.value+":"+E.description
}if(C[w]===D){if(A===""){A=A+E.label
}else{A=A+" , "+E.label
}}})
}s.valueExceptionToBeDisplayed.userToBeDisplay=A
}if(s.valueException.forValue!==undefined&&s.valueList.length>0){var B=s.valueException.forValue.split(",");
var u="";
for(var w=0;
w<B.length;
w++){angular.forEach(s.valueList,function(E){var D=E.id;
if(B[w]==D){if(u===""){u=u+E.text
}else{u=u+" , "+E.text
}}})
}s.valueExceptionToBeDisplayed.valueToBeDisplay=u
}if(s.valueException.dependentOnField!==undefined&&s.finalFieldList.length>0){var z="";
angular.forEach(s.finalFieldList,function(D){var E=D.id;
if(s.valueException.dependentOnField==E){if(z===""){z=z+D.text
}else{z=z+" , "+D.text
}}});
s.valueExceptionToBeDisplayed.dependentOnToBeDisplay=z
}if(s.valueException.dependentOnFieldValues!==undefined&&s.fieldValueList.length>0){var v=s.valueException.dependentOnFieldValues.split(",");
var y="";
for(var w=0;
w<v.length;
w++){angular.forEach(s.fieldValueList,function(E){var D=E.id;
if(v[w]==D){if(y===""){y=y+s.valueExceptionToBeDisplayed.dependentOnToBeDisplay+"-"+E.text
}else{y=y+" , "+s.valueExceptionToBeDisplayed.dependentOnToBeDisplay+"-"+E.text
}}})
}s.valueExceptionToBeDisplayed.dependentOnFieldValuesToBeDisplay=y
}s.valueExceptionToBeDisplayed.isArchive=false;
s.exceptionList.push(angular.copy(s.valueExceptionToBeDisplayed));
s.cancelException()
}else{h.addMessage("Select For User or Dependent On to apply Exception",h.failure);
s.submitted=false;
s.hideConfirmationPopup()
}}};
s.removeException=function(u){u.isArchive=true
};
s.undoException=function(u){u.isArchive=false
};
s.cancelExceptionPage=function(){s.masterName="";
s.editMaster=false;
s.addExceptionPage=false;
s.editExceptionFlag=false;
s.valueException={};
s.exceptionList=[];
s.count=0;
s.code="";
s.forUserList=[];
s.submitted=false;
s.masterValueInDB=false;
s.searchPage=false;
s.isSensitiveMaster=false;
if(s.addExceptionForm!==undefined){s.addExceptionForm.$setPristine()
}$("#scrollable-dropdown-menu.typeahead").typeahead("val","");
if(s.detailOfMasters&&s.detailOfMasters.lenth>0){s.detailOfMasters.masterDataBeans=[]
}s.masterList=true;
s.editMaster=false;
s.languageList=[]
};
s.cancelException=function(){s.valueException={};
s.submitted=false;
s.addExceptionForm.$setPristine();
$("#forUser").select2("data","");
$("#forValue").select2("data","");
$("#dependentOnField").select2("data","");
s.editExceptionFlag=false;
if(s.exceptionList!==undefined&&s.exceptionList.length>0){s.tempfinalFieldList=[];
angular.forEach(s.exceptionList,function(u){u.isUpdated=false;
angular.forEach(s.finalFieldList,function(w){if(u.dependentOnField==w.id){var v=0;
angular.forEach(s.tempfinalFieldList,function(x){if(w.id===x.id){v++
}});
if(v===0){s.tempfinalFieldList.push({id:w.id,text:w.text})
}}});
if(s.tempfinalFieldList.length>0){s.finalFieldList=angular.copy(s.tempfinalFieldList);
s.initDependentOnValues()
}})
}else{s.cancelExceptionPage()
}};
function o(u){return $.grep(u,function(v){return v.isArchive==false||(v.isArchive==true&&v.id!==undefined&&v.id!==null)
})
}s.saveException=function(){if(s.exceptionList!==undefined&&s.exceptionList.length>0){s.listToSend=[];
s.finalListToSend=[];
s.listToSend=o(s.exceptionList);
angular.forEach(s.listToSend,function(u){angular.forEach(s.finalExceptionList,function(v){if(u.index==v.index){if(v.forValue==-1&&s.valueEntityId!==undefined){v.forValue=s.valueEntityId
}s.finalListToSend.push(v)
}})
});
p.saveException(s.finalListToSend,function(){console.log("sucess");
s.cancelExceptionPage()
})
}};
s.retrieveExceptionForUpdate=function(u){s.editExceptionFlag=true;
angular.forEach(s.finalExceptionList,function(G){if(G.index==u.index){s.send={};
s.uiSelectRecipients=[];
u.isUpdated=true;
s.valueException=angular.copy(G);
s.tempValueException=angular.copy(u);
if(u.forUsers!==undefined&&u.forUsers!==null&&u.forUsers!==""){var C=u.forUsers.split(",");
var w=u.userToBeDisplay.split(",");
var v=[];
for(var B=0;
B<C.length;
B++){v.push({id:C[B],text:w[B]})
}$("#forUser").select2("val",v)
}var D=u.forValue.split(",");
var E=u.valueToBeDisplay.split(",");
var x=[];
for(var z=0;
z<D.length;
z++){x.push({id:D[z],text:E[z]})
}if(u.dependentOnFieldValues!==undefined&&u.dependentOnFieldValues!==null&&u.dependentOnFieldValues!==""){var y=u.dependentOnFieldValues.split(",");
var F=u.dependentOnFieldValuesToBeDisplay.split(",");
var A=[];
for(var z=0;
z<y.length;
z++){A.push({id:y[z],text:F[z]})
}$("#dependentOnFieldValues").select2("val",A)
}$("#forValue").select2("val",x);
$("#dependentOnField").select2("val",u.dependentOnField)
}})
};
s.updateException=function(v){s.submitted=true;
if(!v.$valid){s.submitted=false
}else{if(((s.valueException.dependentOnFieldValues!==undefined&&s.valueException.dependentOnFieldValues!==null&&s.valueException.dependentOnFieldValues!=="")||(s.valueException.forUsers!==undefined&&s.valueException.forUsers!==null&&s.valueException.forUsers!==""))){s.tempValueExceptionObj=angular.copy(s.valueException);
if(s.tempValueExceptionObj.forUsers!==undefined){var z="";
if(angular.isArray(s.tempValueExceptionObj.forUsers)){var x="";
angular.forEach(s.tempValueExceptionObj.forUsers,function(B){if(x!==""){x=x+","+B.id
}else{x=x+B.id
}});
z=x
}else{z=s.tempValueExceptionObj.forUsers
}s.tempValueExceptionObj.forUsers=z
}if(s.tempValueExceptionObj.forValue!==undefined){var A="";
if(angular.isArray(s.tempValueExceptionObj.forValue)){var u="";
angular.forEach(s.tempValueExceptionObj.forValue,function(B){if(u!==""){u=u+","+B.id
}else{u=u+B.id
}});
A=u
}else{A=s.tempValueExceptionObj.forValue
}s.tempValueExceptionObj.forValue=A
}if(s.tempValueExceptionObj.dependentOnFieldValues!==undefined){var w="";
if(angular.isArray(s.tempValueExceptionObj.dependentOnFieldValues)){var y="";
angular.forEach(s.tempValueExceptionObj.dependentOnFieldValues,function(B){if(y!==""){y=y+","+B.id
}else{y=y+B.id
}});
w=y
}else{w=s.tempValueExceptionObj.dependentOnFieldValues
}s.tempValueExceptionObj.dependentOnFieldValues=w
}angular.forEach(s.finalExceptionList,function(B){if(s.tempValueExceptionObj.index==B.index){B.forUsers=s.tempValueExceptionObj.forUsers;
B.forValue=s.tempValueExceptionObj.forValue;
B.dependentOnField=s.tempValueExceptionObj.dependentOnField;
B.dependentOnFieldValues=s.tempValueExceptionObj.dependentOnFieldValues
}});
angular.forEach(s.exceptionList,function(C){if(C.index==s.tempValueExceptionObj.index){C.isUpdated=false;
s.tempValueExceptionObj.isUpdated=false;
C.forUsers=s.tempValueExceptionObj.forUsers;
C.forValue=s.tempValueExceptionObj.forValue;
C.dependentOnField=s.tempValueExceptionObj.dependentOnField;
C.dependentOnFieldValues=s.tempValueExceptionObj.dependentOnFieldValues;
if(s.tempValueExceptionObj.forUsers!==undefined&&s.forUserList.length>0){var K="";
if(angular.isArray(s.tempValueExceptionObj.forUsers)){var G="";
angular.forEach(s.tempValueExceptionObj.forUsers,function(N){if(G!==""){G=G+","+N.id
}else{G=G+N.id
}});
K=G.split(",")
}else{K=s.tempValueExceptionObj.forUsers.split(",")
}var I="";
for(var E=0;
E<K.length;
E++){angular.forEach(s.forUserList,function(O){var N=O.value+":"+O.description;
if(K[E]===N){if(I===""){I=I+O.label
}else{I=I+" , "+O.label
}}})
}C.userToBeDisplay=I
}if(s.tempValueExceptionObj.forValue!==undefined&&s.valueList.length>0){var J="";
if(angular.isArray(s.tempValueExceptionObj.forValue)){var L="";
angular.forEach(s.tempValueExceptionObj.forValue,function(N){if(L!==""){L=L+","+N.id
}else{L=L+N.id
}});
J=L.split(",")
}else{J=s.tempValueExceptionObj.forValue.split(",")
}var B="";
for(var E=0;
E<J.length;
E++){angular.forEach(s.valueList,function(O){var N=O.id;
if(J[E]==N){if(B===""){B=B+O.text
}else{B=B+" , "+O.text
}}})
}C.valueToBeDisplay=B
}if(s.tempValueExceptionObj.dependentOnField!==undefined&&s.finalFieldList.length>0){var H="";
angular.forEach(s.finalFieldList,function(N){var O=N.id;
if(s.tempValueExceptionObj.dependentOnField==O){if(H===""){H=H+N.text
}else{H=H+" , "+N.text
}}});
C.dependentOnToBeDisplay=H
}if(s.tempValueExceptionObj.dependentOnFieldValues!==undefined&&s.fieldValueList!==undefined&&s.fieldValueList.length>0){var D="";
if(angular.isArray(s.tempValueExceptionObj.dependentOnFieldValues)){var M="";
angular.forEach(s.tempValueExceptionObj.dependentOnFieldValues,function(N){if(M!==""){M=M+","+N.id
}else{M=M+N.id
}});
D=M.split(",")
}else{D=s.tempValueExceptionObj.dependentOnFieldValues.split(",")
}var F="";
for(var E=0;
E<D.length;
E++){angular.forEach(s.fieldValueList,function(O){var N=O.id;
if(D[E]==N){if(F===""){F=F+C.dependentOnToBeDisplay+"-"+O.text
}else{F=F+" , "+C.dependentOnToBeDisplay+"-"+O.text
}}})
}C.dependentOnFieldValuesToBeDisplay=F
}}});
s.cancelException(v);
s.editExceptionFlag=false
}else{h.addMessage("Select For User or Dependent On to Apply Exception",h.failure);
s.submitted=false
}}}
}];
return{restrict:"E",scope:j,link:f,templateUrl:"scripts/directives/master/master.tmpl.html",controller:c}
}]);
globalProvider.provide.factory("MasterService",["$resource","$rootScope",function(c,a){var b=c(a.apipath+"master/:action",{},{retrieveListOfMaster:{method:"GET",isArray:true,params:{action:"retrieve"}},retrieveDetailsOfMaster:{method:"POST",params:{action:"retrieve"}},update:{method:"POST",params:{action:"update"}},create:{method:"PUT",params:{action:"create"}},deleteById:{method:"POST",params:{action:"delete"}},retrieveSystemFeatures:{method:"GET",isArray:true,params:{action:"retrieve/systemfeatures"}},authenticateForEditMaster:{method:"PUT",params:{action:"checkpassword"}},retrieveLanguage:{method:"GET",params:{action:"retrievelanguages"}},retrieveCustomOfMaster:{method:"POST",isArray:false,params:{action:"retrieveMasterValues"}},retrieveCustomFieldsValueByKey:{method:"POST",params:{action:"retrievecustomfieldsvaluebykey"}},saveException:{method:"POST",params:{action:"saveexception"}},retrieveValueExceptions:{method:"POST",params:{action:"retrievevalueexceptions"}},retrievePrerequisite:{method:"POST",params:{action:"retrieveprerequisites"}}});
return b
}]);
globalProvider.provide.factory("CenterMasterService",["$resource","$rootScope",function(c,a){var b=c(a.centerapipath+"master/:action",{},{retrieveListOfMaster:{method:"GET",isArray:true,params:{action:"retrieve"}},retrieveDetailsOfMaster:{method:"POST",params:{action:"retrieve"}},update:{method:"POST",params:{action:"update"}},create:{method:"PUT",params:{action:"create"}},deleteById:{method:"POST",params:{action:"delete"}},retrieveSystemFeatures:{method:"GET",isArray:true,params:{action:"retrieve/systemfeatures"}},authenticateForEditMaster:{method:"PUT",params:{action:"checkpassword"}},retrieveLanguage:{method:"GET",params:{action:"retrievelanguages"}},retrieveCustomOfMaster:{method:"POST",isArray:false,params:{action:"retrieveMasterValues"}}});
return b
}]);
globalProvider.provide.factory("MessagingService",["$resource","$rootScope",function(c,a){var b=c(a.apipath+"messaging/:action",{},{retrieveUserList:{method:"POST",isArray:true,params:{action:"retrieve/users"}},retrieveUserListFranchise:{method:"POST",isArray:true,params:{action:"retrieve/usersbyfranchise"}},retrieveRoleList:{method:"POST",isArray:true,params:{action:"retrieve/roles"}},retrieveRoleListFranchise:{method:"POST",isArray:true,params:{action:"retrieve/rolesbyfranchise"}},retrieveDepartmentList:{method:"POST",isArray:true,params:{action:"retrieve/departments"}},retrieveDepartmentListFranchise:{method:"POST",isArray:true,params:{action:"retrieve/departmentsbyfranchise"}},});
return b
}])
});