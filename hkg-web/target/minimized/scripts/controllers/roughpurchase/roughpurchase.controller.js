define(["hkg","customFieldService","roughPurchaseService","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicFormGrid","editableCustomGrid","accordionCollapse"],function(a){a.register.controller("RoughPurchaseController",["$rootScope","$scope","RoughPurchaseService","DynamicFormService","CustomFieldService","$q",function(g,j,k,i,h,e){g.maskLoading();
g.mainMenu="stockLink";
g.childMenu="RoughPurchase";
j.roughPurchaseDataBean={};
j.search={};
g.activateMenu();
j.isSearch=false;
j.dbType={};
j.initPurchaseForm=function(l){j.purchaseForm=l
};
var f={};
var b=new Date();
var d=b.getFullYear();
j.searchCustom=i.resetSection(j.generalPurchaseTemplate);
var c=i.retrieveSearchWiseCustomFieldInfo("roughPurchaseAddEdit");
j.createOrUpdateRoughPurchase=function(l,m,n){j.isRoughPurchaseIdExists(l,function(o){if(o!==undefined&&(o.roughPurchaseExist==false||o.roughPurchaseExist=="false")){j.roughPurchaseDataBean.roughPurchaseCustom=l.categoryCustom;
j.roughPurchaseDataBean.id=l.value;
j.roughPurchaseDataBean.roughPurchaseDbType=m;
j.roughPurchaseDataBean.isArchive=false;
j.roughPurchaseDataBean.uiFieldMap=j.map;
j.roughPurchaseDataBean.ruleConfigMap={fieldIdNameMap:j.purchaseFieldIdName,featureName:"roughPurchase"};
j.roughPurchaseDataBean.year=l.year;
if(l.categoryCustom["roughPurchaseID$AG$String"]!=null){j.roughPurchaseDataBean.sequenceNumber=l.categoryCustom["roughPurchaseID$AG$String"]
}g.maskLoading();
if(j.roughPurchaseDataBean.id==null||j.roughPurchaseDataBean.id==undefined){k.createRoughPurchase(j.roughPurchaseDataBean,function(u){var t=e.defer();
var v=t.promise;
v.then(function(){n(u)
});
t.resolve();
g.unMaskLoading()
},function(){g.unMaskLoading();
var u="Could not create purchase, please try again.";
var t=g.error;
g.addMessage(u,t)
})
}else{k.updateRoughPurchase(j.roughPurchaseDataBean,function(u){var t=e.defer();
var v=t.promise;
v.then(function(){n(u)
});
t.resolve();
g.unMaskLoading()
},function(){g.unMaskLoading();
var u="Could not update rough purchase, please try again.";
var t=g.error;
g.addMessage(u,t)
})
}}else{var s="Rough Purchase already exists for the same id.";
var q=g.error;
g.addMessage(s,q);
var p=e.defer();
var r=p.promise;
r.then(function(){n(null)
});
p.resolve()
}})
};
j.getNextPurchaseSequence=function(q){var l=0;
if(j.editableGridPuchaseOptions.template!==undefined&&j.editableGridPuchaseOptions.template!==null&&j.editableGridPuchaseOptions.template.length>0){for(var o=0;
o<j.editableGridPuchaseOptions.template.length;
o++){if(j.editableGridPuchaseOptions.template[o].model==="roughPurchaseID$AG$String"){l++
}}}if(l>0){k.getNextRoughPurchaseSequence(function(s){var r=e.defer();
var t=r.promise;
t.then(function(){q(s)
});
r.resolve()
})
}else{var n=null;
var m=e.defer();
var p=m.promise;
p.then(function(){q(n)
});
m.resolve()
}};
j.deleteRoughPurchase=function(l){k.deleteRoughPurchase(l.value,function(m){j.retrieveRoughPurchase();
g.unMaskLoading()
},function(){g.unMaskLoading();
var n="Could not delete purchase, please try again.";
var m=g.error;
g.addMessage(n,m)
})
};
j.retrieveRoughPurchasePaginatedData=function(){if(!j.isSearch){var m={};
j.map={};
j.map.dbFieldNames=j.editFieldName;
m.uiFieldMap=j.map;
m.offset=j.editableGridPuchaseOptions.datarows.length-1;
m.limit=10;
m.ruleConfigMap={fieldIdNameMap:j.purchaseFieldIdName,featureName:"roughPurchase"};
if(j.editFieldName.length>0){k.retrieveRoughPurchases(m,function(n){if(n!==undefined){angular.forEach(n,function(t){var q=angular.copy(t);
q.isEditGridFlag=false;
if(q.categoryCustom!=null){for(var s in q.categoryCustom){var r=s.split("$");
if(r[1]==="AG"){var u=q.categoryCustom[s];
if(u!=null||u){var p=u.split("-");
q.categoryCustom[s]=p[1]
}}}}j.editableGridPuchaseOptions.datarows.push(angular.copy(q));
j.editableGridPuchaseOptions.datarowsFromDb.push(angular.copy(q))
})
}var o=function(p){angular.forEach(p,function(q){j.editableGridPuchaseOptions.labelrows.push(q);
j.editableGridPuchaseOptions.labelrowsFromDb.push(q)
});
if(j.editableGridPuchaseOptions.labelrows.length>0){j.allDataRetrieved=true
}else{j.allDataRetrieved=false
}};
j.newVar=angular.copy(n);
if(j.newVar!==undefined){i.convertorForCustomField(j.newVar,o)
}g.unMaskLoading()
},function(){var o="Could not save, please try again.";
var n=g.error;
g.addMessage(o,n);
g.unMaskLoading()
})
}}else{if(Object.getOwnPropertyNames(j.searchCustom).length>0){j.searchFinal=angular.copy(j.searchCustom);
var l=i.convertSearchData(j.generalPurchaseTemplate,null,null,null,j.searchFinal);
j.roughPurchaseDataBean.roughPurchaseCustom=l;
j.roughPurchaseDataBean.searchOnParameter=true;
j.roughPurchaseDataBean.offset=j.editableGridPuchaseOptions.datarows.length-1;
j.roughPurchaseDataBean.limit=10;
j.roughPurchaseDataBean.uiFieldMap=j.map;
k.retrieveSearchedRoughPurchases(j.roughPurchaseDataBean,function(n){if(n!==undefined&&n!==null&&n.length>0){angular.forEach(n,function(t){var q=angular.copy(t);
q.isEditGridFlag=false;
if(q.categoryCustom!=null){for(var s in q.categoryCustom){var r=s.split("$");
if(r[1]=="AG"){var u=q.categoryCustom[s];
if(u!=null&&u){var p=u.split("-");
q.categoryCustom[s]=p[1]
}}}}j.editableGridPuchaseOptions.datarows.push(q);
j.editableGridPuchaseOptions.datarowsFromDb.push(q)
});
j.newVar=angular.copy(n);
var o=function(p){angular.forEach(p,function(q){j.editableGridPuchaseOptions.labelrows.push(q);
j.editableGridPuchaseOptions.labelrowsFromDb.push(q)
})
};
if(j.newVar!==undefined){i.convertorForCustomField(j.newVar,o)
}}},function(){var o="Could not retrieve, please try again.";
var n=g.error;
g.addMessage(o,n);
g.unMaskLoading()
})
}else{j.isSearch=false;
this.retrieveRoughPurchasePaginatedData()
}}};
j.getTotalPurchaseCount=function(l){if(!j.isSearch){k.getTotalCountOfPurchases(function(m){j.editableGridPuchaseOptions.totalItems=m.totalItems+1;
j.roughPurchaseTotalItems=m.totalItems+1
})
}else{k.getTotalCountOfSearchPurchases(l,function(m){j.editableGridPuchaseOptions.totalItems=m.totalItems+1;
j.roughPurchaseTotalItems=m.totalItems+1
})
}};
j.getTotalPurchaseCount();
j.editableGridPuchaseOptions={datarows:[],template:{},labelrows:[],labelrowsFromDb:[],datarowsFromDb:[],dbType:{},enableselection:false,createOrUpdateRecord:j.createOrUpdateRoughPurchase,deleteRecord:j.deleteRoughPurchase,deleteModalId:"roughPurchaseModalPanel",cancelModalId:"roughPurchaseCancelModalPanel",featureName:"Rough Purchase",seqId:j.getNextPurchaseSequence,tableName:"roughPurchaseTable",linked:false,retrievePaginatedRecord:j.retrieveRoughPurchasePaginatedData,totalItems:j.roughPurchaseTotalItems};
c.then(function(l){j.generalPurchaseTemplate=[];
j.sectionData=[];
angular.forEach(l.genralSection,function(m){if(m.featureName=="purchase"){j.sectionData.push(m);
f[m.model]=m.featureName
}});
j.generalPurchaseTemplate=angular.copy(j.sectionData);
j.searchResetFlag=true
},function(l){console.log("reason :"+l)
},function(l){console.log("update :"+l)
});
j.retrieveRoughPurchase=function(){h.retrieveDesignationBasedFields("roughPurchaseAddEdit",function(l){j.response=angular.copy(l);
j.roughPurchaseDataBean={};
var m=i.retrieveSectionWiseCustomFieldInfo("purchase");
if(!(j.dbTypeForUpdate!=null)){j.dbTypeForUpdate={}
}m.then(function(p){j.editableGridPuchaseOptions.template=null;
j.editableGridPuchaseOptions.template=p.genralSection;
j.listOfModelsOfDateType=[];
var o=[];
var n=Object.keys(l).map(function(r,s){angular.forEach(this[r],function(t){if(r==="Purchase"){o.push({Purchase:t})
}})
},l);
j.editFieldName=[];
j.purchaseFieldIdName={};
j.editableGridPuchaseOptions.template=i.retrieveCustomData(j.editableGridPuchaseOptions.template,o);
angular.forEach(j.editableGridPuchaseOptions.template,function(r){if(r.fromModel){j.purchaseFieldIdName[r.fieldId]=r.fromModel;
if(j.editFieldName.indexOf(r.fromModel)===-1){j.editFieldName.push(r.fromModel)
}}else{if(r.toModel){j.purchaseFieldIdName[r.fieldId]=r.toModel;
if(j.editFieldName.indexOf(r.toModel)===-1){j.editFieldName.push(r.toModel)
}}else{if(r.model){j.purchaseFieldIdName[r.fieldId]=r.model;
if(j.editFieldName.indexOf(r.model)===-1){j.editFieldName.push(r.model)
}}}}});
j.fieldNotConfigured=false;
j.showConfigMsg=false;
if(j.editableGridPuchaseOptions.template.length<=0){j.showConfigMsg=true
}var q={};
j.map={};
j.map.dbFieldNames=j.editFieldName;
q.uiFieldMap=j.map;
q.offset=0;
q.limit=9;
q.ruleConfigMap={fieldIdNameMap:j.purchaseFieldIdName,featureName:"roughPurchase"};
if(j.editFieldName.length>0){j.getTotalPurchaseCount();
k.retrieveRoughPurchases(q,function(t){if(t!==undefined){j.editableGridPuchaseOptions.datarows=[];
j.editableGridPuchaseOptions.labelrows=[];
angular.forEach(t,function(z){var w=angular.copy(z);
w.isEditGridFlag=false;
if(w.categoryCustom!=null){for(var y in w.categoryCustom){var x=y.split("$");
if(x[1]==="AG"){var A=w.categoryCustom[y];
if(A!=null||A){var v=A.split("-");
w.categoryCustom[y]=v[1]
}}}}j.editableGridPuchaseOptions.datarows.push(angular.copy(w))
});
var r=0;
if(j.editableGridPuchaseOptions.template!==undefined&&j.editableGridPuchaseOptions.template!==null&&j.editableGridPuchaseOptions.template.length>0){for(var s=0;
s<j.editableGridPuchaseOptions.template.length;
s++){if(j.editableGridPuchaseOptions.template[s].model==="roughPurchaseID$AG$String"){r++
}}}if(r>0){k.getNextRoughPurchaseSequence(function(v){j.editableGridPuchaseOptions.datarowsFromDb=angular.copy(j.editableGridPuchaseOptions.datarows);
j.editableGridPuchaseOptions.datarows.push({isEditGridFlag:false,categoryCustom:{"roughPurchaseID$AG$String":v["roughPurchaseID$AG$String"]},beforeLabel:d+"-"})
})
}else{j.editableGridPuchaseOptions.datarowsFromDb=angular.copy(j.editableGridPuchaseOptions.datarows);
j.editableGridPuchaseOptions.datarows.push({isEditGridFlag:false,categoryCustom:{},beforeLabel:d+"-"})
}}var u=function(v){j.editableGridPuchaseOptions.labelrows=angular.copy(v);
j.editableGridPuchaseOptions.labelrows.push({isEditGridFlag:false});
j.editableGridPuchaseOptions.labelrowsFromDb=angular.copy(v);
if(j.editableGridPuchaseOptions.labelrows.length>0){j.allDataRetrieved=true
}else{j.allDataRetrieved=false
}};
j.newVar=angular.copy(t);
if(j.newVar!==undefined){i.convertorForCustomField(j.newVar,u)
}g.unMaskLoading()
},function(){var s="Could not save, please try again.";
var r=g.error;
g.addMessage(s,r);
g.unMaskLoading()
})
}},function(n){},function(n){})
},function(){g.unMaskLoading();
var m="Failed to retrieve data";
var l=g.error;
g.addMessage(m,l)
})
};
j.retrieveRoughPurchase();
j.isRoughPurchaseIdExists=function(m,r){if("roughPurchaseID$AG$String" in m.categoryCustom&&m.categoryCustom["roughPurchaseID$AG$String"]!==null){var o;
if(m.value!==null&&m.value!==undefined){o=m.value
}else{o=null
}var p={roughPurchaseId:m.categoryCustom["roughPurchaseID$AG$String"],roughPurchaseObjectId:o};
k.isRoughPurchaseIdExists(p,function(s){var t=e.defer();
var u=t.promise;
u.then(function(){r(s)
});
t.resolve()
})
}else{var l={roughPurchaseExist:false};
var n=e.defer();
var q=n.promise;
q.then(function(){r(l)
});
n.resolve()
}};
j.retrieveSearchedData=function(){j.isSearch=true;
if(Object.getOwnPropertyNames(j.searchCustom).length>0){j.searchFinal=angular.copy(j.searchCustom);
var l=i.convertSearchData(j.generalPurchaseTemplate,null,null,null,j.searchFinal);
j.roughPurchaseDataBean.roughPurchaseCustom=l;
j.roughPurchaseDataBean.searchOnParameter=true;
j.roughPurchaseDataBean.offset=0;
j.roughPurchaseDataBean.limit=9;
j.roughPurchaseDataBean.uiFieldMap=j.map;
j.getTotalPurchaseCount(roughPurchaseDataBean);
k.retrieveSearchedRoughPurchases(j.roughPurchaseDataBean,function(o){j.editableGridPuchaseOptions.datarows=[];
j.editableGridPuchaseOptions.labelrows=[];
var m=0;
if(j.editableGridPuchaseOptions.template!==undefined&&j.editableGridPuchaseOptions.template!==null&&j.editableGridPuchaseOptions.template.length>0){for(var n=0;
n<j.editableGridPuchaseOptions.template.length;
n++){if(j.editableGridPuchaseOptions.template[n].model==="roughPurchaseID$AG$String"){m++
}}}if(m>0){k.getNextRoughPurchaseSequence(function(q){j.editableGridPuchaseOptions.datarows.push({isEditGridFlag:false,categoryCustom:{"roughPurchaseID$AG$String":q["roughPurchaseID$AG$String"]},beforeLabel:d+"-"});
angular.forEach(o,function(v){var s=angular.copy(v);
s.isEditGridFlag=false;
if(s.categoryCustom!=null){for(var u in s.categoryCustom){var t=u.split("$");
if(t[1]=="AG"){var w=s.categoryCustom[u];
if(w!=null&&w){var r=w.split("-");
s.categoryCustom[u]=r[1]
}}}}j.editableGridPuchaseOptions.datarows.push(s);
j.editableGridPuchaseOptions.datarowsFromDb.push(s)
})
})
}else{j.editableGridPuchaseOptions.datarows.push({isEditGridFlag:false,categoryCustom:{},beforeLabel:d+"-"});
angular.forEach(o,function(u){var r=angular.copy(u);
r.isEditGridFlag=false;
if(r.categoryCustom!=null){for(var t in r.categoryCustom){var s=t.split("$");
if(s[1]=="AG"){var v=r.categoryCustom[t];
if(v!=null&&v){var q=v.split("-");
r.categoryCustom[t]=q[1]
}}}}j.editableGridPuchaseOptions.datarows.push(r);
j.editableGridPuchaseOptions.datarowsFromDb.push(r)
})
}if(o!==undefined&&o!==null&&o.length>0){j.newVar=angular.copy(o);
var p=function(q){j.editableGridPuchaseOptions.labelrows.push({isEditGridFlag:false});
angular.forEach(q,function(r){j.editableGridPuchaseOptions.labelrows.push(r);
j.editableGridPuchaseOptions.labelrowsFromDb.push(r)
})
};
if(j.newVar!==undefined){i.convertorForCustomField(j.newVar,p)
}}if(j.editableGridPuchaseOptions.labelrows.length>0){j.allDataRetrieved=true
}else{j.allDataRetrieved=false
}},function(){var n="Could not retrieve, please try again.";
var m=g.error;
g.addMessage(n,m);
g.unMaskLoading()
})
}else{this.retrieveRoughPurchase()()
}};
j.onCancelOfSearch=function(){if(j.purchaseForm!==null&&j.purchaseForm!==undefined){j.purchaseForm.$dirty=false
}j.isSearch=false;
j.reset("searchCustom");
j.retrieveRoughPurchase()
};
j.reset=function(m){if(m==="searchCustom"){j.searchCustom={};
var l=i.retrieveSearchWiseCustomFieldInfo("roughPurchaseAddEdit");
l.then(function(n){j.generalPurchaseTemplate=n.genralSection;
g.unMaskLoading()
},function(n){},function(n){})
}};
g.unMaskLoading()
}])
});