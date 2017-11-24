define(["hkg","lotService","customFieldService","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicForm"],function(b,a){b.register.controller("SplitLotController",["$rootScope","$scope","LotService","$timeout","$filter","$location","$window","DynamicFormService","CustomFieldService",function(i,l,g,d,f,e,c,k,j){i.mainMenu="stockLink";
i.childMenu="splitLot";
i.activateMenu();
var h={};
l.initializeData=function(m){if(m){l.tempMergeList=[];
l.lotDataBean={};
l.searchedData=[];
l.searchedDataFromDb=[];
l.listFilled=false;
l.lotListTodisplay=[];
l.searchCustom=k.resetSection(l.generalSearchTemplate);
l.lotDataBean.featureCustomMap={};
var n=k.retrieveSearchWiseCustomFieldInfo("lotSplit");
l.flag={};
l.dbType={};
l.modelAndHeaderList=[];
l.modelAndHeaderListForLot=[];
n.then(function(q){l.generalSearchTemplate=q.genralSection;
if(l.generalSearchTemplate!=null&&l.generalSearchTemplate.length>0){for(var o=0;
o<l.generalSearchTemplate.length;
o++){var p=l.generalSearchTemplate[o];
h[p.model]=p.featureName;
l.modelAndHeaderList.push({model:p.model,header:p.label})
}}l.dataRetrieved=true
},function(o){},function(o){})
}};
l.initializeData(true);
l.initSplitLotForm=function(m){l.splitLotForm=m
};
l.saveAllLot=function(o){var m=[];
var n={};
angular.forEach(l.lotListToSave,function(p){m.push({lotCustom:p.categoryCustom,invoiceDataBean:{id:l.invoiceId},lotDbType:l.splitLotDbType,parcelDataBean:{id:l.parcelId}})
});
n.lotList=m;
n.invoiceDataBean={id:l.invoiceId};
n.parcelDataBean={id:l.parcelId};
n.id=l.lotId;
if(o.$valid){g.splitLot(n,function(p){l.lotListToSave=[];
l.lotListTodisplay=[];
l.invoiceCustom=k.resetSection(l.generaInvoiceTemplate);
l.parcelCustom=k.resetSection(l.generaParcelTemplate);
l.categoryCustom=k.resetSection(l.generalLotTemplate);
l.searchedData=[];
l.searchedDataFromDb=[];
l.listFilled=false;
l.addLotForm.$dirty=false;
l.flag.showAddPage=false
},function(){i.unMaskLoading();
var q="Could not create lot, please try again.";
var p=i.error;
i.addMessage(q,p)
})
}};
l.retrieveSearchedData=function(o){l.lotDataBean.featureCustomMap={};
if(Object.getOwnPropertyNames(l.searchCustom).length>0){var n=false;
for(var p in l.searchCustom){if(!!(l.searchCustom[p])){n=true;
break
}}if(n){i.maskLoading();
var m={};
angular.forEach(h,function(u,s){var t=l.searchCustom[s];
if(t!==undefined){var r={};
if(!m[u]){r[s]=t;
m[u]=r
}else{var q=m[u];
q[s]=t;
m[u]=q
}}else{t=l.searchCustom["to"+s];
if(t!==undefined){var r={};
if(!m[u]){r["to"+s]=t;
m[u]=r
}else{var q=m[u];
q["to"+s]=t;
m[u]=q
}}t=l.searchCustom["from"+s];
if(t!==undefined){var r={};
if(!m[u]){r["from"+s]=t;
m[u]=r
}else{var q=m[u];
q["from"+s]=t;
m[u]=q
}}}});
l.lotDataBean.featureCustomMapValue=m;
l.lotDataBean.lotCustom=l.searchCustom;
l.lotDataBean.hasPacket=false;
g.search(l.lotDataBean,function(q){l.searchedDataFromDb=angular.copy(q);
var r=k.getValuesOfComponentFromId(q,l.generalSearchTemplate);
l.searchedData=angular.copy(r);
l.listFilled=true;
l.searchCustom=k.resetSection(l.generalSearchTemplate);
i.unMaskLoading()
},function(){var r="Could not retrieve, please try again.";
var q=i.error;
i.addMessage(r,q);
i.unMaskLoading()
})
}}};
l.onCanelOfSearch=function(m){if(l.splitLotForm!=null){l.splitLotForm.$dirty=false
}l.searchedData=[];
l.searchCustom=k.resetSection(l.generalSearchTemplate);
l.listFilled=false
};
l.addLot=function(m){l.lotListToSave=[];
l.count=0;
l.invoiceId=m.description;
l.parcelId=m.label;
l.lotId=m.value;
if(m!=null){if(l.searchedDataFromDb!=null&&l.searchedDataFromDb.length>0){for(var n=0;
n<l.searchedDataFromDb.length;
n++){if(l.searchedDataFromDb[n].id===m.id){m=angular.copy(l.searchedDataFromDb[n]);
break
}}}l.flag.showAddPage=true;
i.maskLoading();
l.parcelDataBean={invoiceDataBean:{invoiceCustom:"",invoiceDbType:""}};
j.retrieveDesignationBasedFields("lotSplit",function(o){l.invoiceCustom=k.resetSection(l.generaInvoiceTemplate);
var q=k.retrieveSectionWiseCustomFieldInfo("invoice");
l.invoiceDbType={};
q.then(function(s){l.generaInvoiceTemplate=s.genralSection;
l.generaInvoiceTemplate=k.retrieveCustomData(l.generaInvoiceTemplate,o);
l.invoiceCustom=m.custom3
},function(s){},function(s){});
l.parcelCustom=k.resetSection(l.generaParcelTemplate);
var q=k.retrieveSectionWiseCustomFieldInfo("parcel");
q.then(function(s){l.generaParcelTemplate=s.genralSection;
l.generaParcelTemplate=k.retrieveCustomData(l.generaParcelTemplate,o);
l.parcelCustom=m.custom4
},function(s){},function(s){});
var p=[];
l.lotCustom=k.resetSection(l.generalLotTemplate);
var q=k.retrieveSectionWiseCustomFieldInfo("lot");
q.then(function(u){var t=[];
var s=Object.keys(o).map(function(v,w){angular.forEach(this[v],function(x){if(v==="Lot#P#"){t.push({"Lot#P#":x})
}else{if(v==="Lot"){p.push({Lot:x})
}}})
},o);
l.generalLotTemplate=u.genralSection;
l.generalLotTemplate=k.retrieveCustomData(l.generalLotTemplate,t);
l.lotCustom=m.custom1
},function(s){},function(s){});
l.categoryCustom=k.resetSection(l.generalSplitLotTemplate);
var r=k.retrieveSectionWiseCustomFieldInfo("lot");
l.splitLotDbType={};
r.then(function(s){l.generalSplitLotTemplate=s.genralSection;
l.generalSplitLotTemplate=k.retrieveCustomData(l.generalSplitLotTemplate,p);
l.flag.customFieldGenerated=true;
angular.forEach(l.generalSplitLotTemplate,function(t){l.modelAndHeaderListForLot.push({model:t.model,header:t.label})
});
l.lotDataBean.lotDbType=l.splitLotDbType
},function(s){},function(s){});
i.unMaskLoading()
},function(){i.unMaskLoading();
var p="Failed to retrieve data";
var o=i.error;
i.addMessage(p,o)
})
}};
l.onCancel=function(m){if(m!=null){l.categoryCustom=k.resetSection(l.generalLotTemplate);
l.flag.showAddPage=false;
l.lotListToSave=[];
l.lotListTodisplay=[]
}};
l.createLot=function(n){if(Object.getOwnPropertyNames(l.categoryCustom).length>0){var m=false;
for(var q in l.categoryCustom){if(!!(l.categoryCustom[q])){m=true;
break
}}if(n.$valid&&m){var p=[];
p.push({categoryCustom:angular.copy(l.categoryCustom)});
l.count++;
l.lotListToSave.push(angular.copy({id:l.count,categoryCustom:l.categoryCustom}));
var o=k.getValuesOfComponentFromId(p,l.generalSplitLotTemplate);
l.lotListTodisplay.push(angular.copy({id:l.count,category:o[0]}));
l.categoryCustom=k.resetSection(l.generalSplitLotTemplate);
l.listFilled=true
}}};
l.editLotLocally=function(n){l.oldObj=angular.copy(n,l.oldObj);
if(n!=null){l.flag.editLot=true;
l.lotDataBean={};
if(!!(l.lotListToSave&&l.lotListToSave.length>0)){for(var m=0;
m<l.lotListToSave.length;
m++){if(l.lotListToSave[m].id===n.id){l.categoryCustom=angular.copy(l.lotListToSave[m].categoryCustom);
l.lotDataBean.lotCustom=l.categoryCustom;
break
}}}l.lotDataBean.lotDbType=l.splitLotDbType
}};
l.showPopUp=function(m){l.lotObjectToDelete=m;
$("#deletePopUp").modal("show")
};
l.hidePopUp=function(){$("#deletePopUp").modal("hide");
i.removeModalOpenCssAfterModalHide()
};
l.deleteLot=function(){if(!!(l.lotListToSave&&l.lotListTodisplay&&l.lotObjectToDelete&&l.lotListToSave.length>0&&l.lotListTodisplay.length>0)){var n=l.lotObjectToDelete.id;
var m=l.lotListTodisplay.indexOf(l.lotObjectToDelete);
if(m!==-1){l.lotListTodisplay.splice(m,1)
}angular.forEach(l.lotListToSave,function(p){if(p.id===n){var o=l.lotListToSave.indexOf(p);
if(o!==-1){l.lotListToSave.splice(o,1)
}}})
}l.categoryCustom=k.resetSection(l.generalSplitLotTemplate);
l.flag.editLot=false;
$("#deletePopUp").modal("hide");
i.removeModalOpenCssAfterModalHide()
};
l.saveLot=function(p){if(Object.getOwnPropertyNames(l.categoryCustom).length>0){var m=false;
for(var r in l.categoryCustom){if(!!(l.categoryCustom[r])){m=true;
break
}}if(!!(m&&p.$valid&&l.lotListToSave&&l.lotListTodisplay&&l.oldObj&&l.lotListToSave.length>0&&l.lotListTodisplay.length>0)){var q=l.oldObj.id;
angular.forEach(l.lotListTodisplay,function(t){if(t.id===q){var s=l.lotListTodisplay.indexOf(t);
if(s!==-1){l.lotListTodisplay.splice(s,1)
}}});
angular.forEach(l.lotListToSave,function(t){if(t.id===q){var s=l.lotListToSave.indexOf(t);
if(s!==-1){l.lotListToSave.splice(s,1)
}}});
var o=[];
o.push({categoryCustom:angular.copy(l.categoryCustom)});
l.count++;
l.lotListToSave.push(angular.copy({id:l.count,categoryCustom:l.categoryCustom}));
var n=k.getValuesOfComponentFromId(o,l.generalSplitLotTemplate);
l.lotListTodisplay.push(angular.copy({id:l.count,category:n[0]}));
l.categoryCustom=k.resetSection(l.generalSplitLotTemplate);
l.listFilled=true
}l.categoryCustom=k.resetSection(l.generalSplitLotTemplate);
l.flag.editLot=false
}}
}])
});