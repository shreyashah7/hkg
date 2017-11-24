define(["hkg","parcelService","customFieldService","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicForm","accordionCollapse"],function(a,b,c){a.register.controller("EditParcelController",["$rootScope","$scope","ParcelService","DynamicFormService","CustomFieldService","$location",function(e,g,k,f,i,j){e.mainMenu="stockLink";
e.childMenu="editParcel";
e.activateMenu();
g.searchedDataFromDbForUiGrid=[];
g.parcelDataForUiGrid=[];
g.parcelListForUIGrid=[];
g.parcelLabelListForUiGrid=[];
var h={};
var d={};
g.initializeData=function(l){if(l){d={};
g.parcelDataBean={};
g.searchedData=[];
g.searchedDataFromDb=[];
g.listFilled=false;
g.lotListTodisplay=[];
g.searchCustom=f.resetSection(g.generalSearchTemplate);
var m=f.retrieveSearchWiseCustomFieldInfo("parcelEdit");
g.flag={};
g.flag.searchFieldNotAvailable=false;
g.dbType={};
g.modelAndHeaderList=[];
g.modelAndHeaderListForLot=[];
m.then(function(r){g.generalSearchTemplate=r.genralSection;
g.searchInvoiceTemplate=[];
g.searchParcelTemplate=[];
var n=[];
var q=[];
if(g.generalSearchTemplate!=null&&g.generalSearchTemplate.length>0){for(var o=0;
o<g.generalSearchTemplate.length;
o++){var p=g.generalSearchTemplate[o];
if(p.featureName.toLowerCase()==="invoice"){g.searchInvoiceTemplate.push(angular.copy(p));
n.push(angular.copy(p.model))
}else{if(p.featureName.toLowerCase()==="parcel"){q.push(angular.copy(p.model));
g.searchParcelTemplate.push(angular.copy(p))
}}h[p.model]=p.featureName;
g.modelAndHeaderList.push({model:p.model,header:p.label});
if(p.fromModel){g.parcelLabelListForUiGrid.push({name:p.fromModel,displayName:p.label,minWidth:200})
}else{if(p.toModel){g.parcelLabelListForUiGrid.push({name:p.toModel,displayName:p.label,minWidth:200})
}else{if(p.model){g.parcelLabelListForUiGrid.push({name:p.model,displayName:p.label,minWidth:200})
}}}}if(n.length>0){d.invoice=n
}if(q.length>0){d.parcel=q
}}else{g.flag.searchFieldNotAvailable=true
}g.searchResetFlag=true;
g.dataRetrieved=true
},function(n){},function(n){})
}};
g.initializeData(true);
g.initEditParcelForm=function(l){g.editParcelForm=l
};
g.retrieveSearchedData=function(r){g.selectOneParameter=false;
g.gridOptions={};
g.searchedData=[];
g.listFilled=false;
g.searchedDataFromDbForUiGrid=[];
if(Object.getOwnPropertyNames(g.searchCustom).length>0){var o=false;
for(var s in g.searchCustom){if(typeof g.searchCustom[s]==="object"&&g.searchCustom[s]!=null){var p=angular.copy(g.searchCustom[s].toString());
if(typeof p==="string"&&p!==null&&p!==undefined&&p.length>0){o=true;
break
}}if(typeof g.searchCustom[s]==="string"&&g.searchCustom[s]!==null&&g.searchCustom[s]!==undefined&&g.searchCustom[s].length>0){o=true;
break
}if(typeof g.searchCustom[s]==="number"&&!!(g.searchCustom[s])){o=true;
break
}if(typeof g.searchCustom[s]==="boolean"){o=true;
break
}}if(o){e.maskLoading();
g.parcelDataBean.featureCustomMapValue={};
g.map={};
var n={};
var l=f.convertSearchData(g.searchInvoiceTemplate,g.searchParcelTemplate,null,null,g.searchCustom);
angular.forEach(h,function(x,v){var w=l[v];
if(w!==undefined){var u={};
if(!n[x]){u[v]=w;
n[x]=u
}else{var t=n[x];
t[v]=w;
n[x]=t
}}else{w=g.searchCustom["to"+v];
if(w!==undefined){var u={};
if(!n[x]){u["to"+v]=w;
n[x]=u
}else{var t=n[x];
t["to"+v]=w;
n[x]=t
}}w=g.searchCustom["from"+v];
if(w!==undefined){var u={};
if(!n[x]){u["from"+v]=w;
n[x]=u
}else{var t=n[x];
t["from"+v]=w;
n[x]=t
}}}});
g.parcelDataBean.featureCustomMapValue=n;
g.parcelDataBean.featureDbFieldMap=d;
g.parcelDataBean.searchOnParameter=true;
k.search(g.parcelDataBean,function(u){var t=f.retrieveSectionWiseCustomFieldInfo("parcel");
t.then(function(v){g.parcelCustom=f.resetSection(g.generalSearchTemplate);
g.parcelTemplate=v.genralSection;
g.searchedDataFromDb=angular.copy(u);
var w=function(x){g.searchedData=angular.copy(x);
angular.forEach(g.searchedData,function(y){angular.forEach(g.parcelLabelListForUiGrid,function(z){if(!y.categoryCustom.hasOwnProperty(z.name)){y.categoryCustom[z.name]="NA"
}else{if(y.categoryCustom.hasOwnProperty(z.name)){if(y.categoryCustom[z.name]===null||y.categoryCustom[z.name]===""||y.categoryCustom[z.name]===undefined){y.categoryCustom[z.name]="NA"
}}}});
g.searchedDataFromDbForUiGrid.push(y.categoryCustom)
});
g.gridOptions={};
g.gridOptions.enableFiltering=true;
g.gridOptions.multiSelect=false;
g.gridOptions.enableRowSelection=true;
g.gridOptions.enableSelectAll=false;
g.selectedParcel=[];
g.gridOptions.onRegisterApi=function(y){g.gridApi=y;
y.selection.on.rowSelectionChanged(g,function(z){if(g.selectedParcel.length>0){$.each(g.selectedParcel,function(B,A){if(A["$$hashKey"]===z.entity["$$hashKey"]){g.flag.repeatedRow=true;
g.selectedParcel.splice(B,1);
return false
}else{g.flag.repeatedRow=false
}});
if(!g.flag.repeatedRow){g.selectedParcel.push(z.entity)
}}else{g.selectedParcel.push(z.entity)
}if(g.selectedParcel.length>0){g.flag.rowSelectedflag=true
}else{g.flag.rowSelectedflag=false
}});
y.selection.on.rowSelectionChangedBatch(g,function(z){if(g.selectedParcel.length>0){angular.forEach(z,function(A){$.each(g.selectedParcel,function(C,B){if(B["$$hashKey"]===A.entity["$$hashKey"]){g.flag.repeatedRow=true;
g.selectedParcel=[];
return false
}else{g.flag.repeatedRow=false
}});
if(!g.flag.repeatedRow){g.selectedParcel.push(A.entity)
}})
}else{angular.forEach(z,function(A){g.selectedParcel.push(A.entity)
})
}if(g.selectedParcel.length>0){g.flag.rowSelectedflag=true
}else{g.flag.rowSelectedflag=false
}})
};
g.gridOptions.data=g.searchedDataFromDbForUiGrid;
g.gridOptions.columnDefs=g.parcelLabelListForUiGrid;
window.setTimeout(function(){$(window).resize();
$(window).resize()
},100);
g.listFilled=true;
e.unMaskLoading()
};
f.convertorForCustomField(u,w)
},function(v){},function(v){})
},function(){var u="Could not retrieve, please try again.";
var t=e.error;
e.addMessage(u,t);
e.unMaskLoading()
})
}else{var q="Please select atleast one search criteria for search";
var m=e.warning;
e.addMessage(q,m)
}}else{var q="Please select atleast one search criteria for search";
var m=e.warning;
e.addMessage(q,m)
}};
g.reset=function(m){if(m==="searchCustom"){g.searchCustom={};
var l=f.retrieveSearchWiseCustomFieldInfo("parcelEdit");
g.dbType={};
l.then(function(n){g.generalSearchTemplate=n.genralSection;
g.searchResetFlag=true;
e.unMaskLoading();
g.flag.customFieldGenerated=true
},function(n){},function(n){})
}else{if(m==="parcelCustom"){g.parcelCustom={};
var l=f.retrieveSectionWiseCustomFieldInfo("parcel");
l.then(function(p){var o=[];
var n=Object.keys(g.response).map(function(q,r){angular.forEach(this[q],function(s){if(q!=="Parcel#P#"){o.push({Parcel:s})
}})
},g.response);
g.updateParcelTemplate=null;
g.updateParcelTemplate=p.genralSection;
g.updateParcelTemplate=f.retrieveCustomData(g.updateParcelTemplate,o);
g.parcelAddShow=true;
e.unMaskLoading();
g.flag.customFieldGenerated=true;
g.parcelDataBean.parcelDbType=angular.copy(g.parcelDbType)
},function(n){},function(n){})
}}};
g.onCanel=function(l){if(g.editParcelForm!=null){g.editParcelForm.$dirty=false
}if(g.addParcelForm!=null){g.addParcelForm.$dirty=false;
j.path("/editparcel")
}g.parcelCustom=f.resetSection(g.updateParcelTemplate);
e.editParcel=false;
e.parcelByInvoice=null;
g.flag.showUpdatePage=false;
g.submitted=false;
g.parcelAddShow=false;
g.selectedParcel=[];
g.reset("parcelCustom")
};
g.onCanelOfSearch=function(l){if(g.addLotForm!=null){g.addLotForm.$dirty=false
}g.searchResetFlag=false;
g.listFilled=false;
g.searchedData=[];
g.selectedParcel=[];
g.gridOptions={};
g.reset("searchCustom")
};
g.saveParcel=function(l){g.submitted=true;
angular.forEach(g.listOfModelsOfDateType,function(m){if(g.parcelCustom.hasOwnProperty(m)){if(g.parcelCustom[m]!==null&&g.parcelCustom[m]!==undefined){g.parcelCustom[m]=new Date(g.parcelCustom[m])
}else{g.parcelCustom[m]=""
}}});
g.parcelDataBean={};
g.parcelDataBean.parcelCustom=g.parcelCustom;
g.parcelDataBean.parcelDbType=angular.copy(g.parcelDbType);
g.parcelDataBean.id=g.parcelId;
if(l.$valid){e.maskLoading();
g.submitted=false;
k.update(g.parcelDataBean,function(m){g.editParcelForm.$dirty=false;
g.selectedParcel=[];
g.gridOptions={};
if(g.addParcelForm!=null){g.addParcelForm.$dirty=false;
j.path("/editparcel")
}else{g.initializeData(true)
}e.editParcel=false;
e.parcelByInvoice=null;
e.unMaskLoading()
},function(){e.unMaskLoading();
var n="Could not update parcel, please try again.";
var m=e.error;
e.addMessage(n,m)
})
}};
g.editParcel=function(n){var o={};
if((n===undefined||n===null)){n=g.selectedParcel[0]
}if(n!=null){if(g.searchedDataFromDb!==undefined&&g.searchedDataFromDb.length>0){for(var l=0;
l<g.searchedDataFromDb.length;
l++){var m=g.searchedDataFromDb[l];
if(m.categoryCustom.parcelDbObjectId===n.parcelDbObjectId){n=angular.copy(g.searchedDataFromDb[l]);
break
}}}g.parcelAddShow=true;
g.flag.showUpdatePage=true;
g.parcelId=n.value;
g.invoiceIdForConstraint=n.label;
g.parcelIdIdForConstraint=n.value;
i.retrieveDesignationBasedFields("parcelEdit",function(q){var s=f.retrieveSectionWiseCustomFieldInfo("invoice");
g.dbTypeForInvoice={};
s.then(function(x){g.updateInvoiceTemplate=x.genralSection;
var w=[];
var v=[];
var u=Object.keys(q).map(function(y,z){angular.forEach(this[y],function(A){if(y==="Invoice#P#"){v.push({Invoice:A})
}})
},q);
g.response=angular.copy(q);
g.updateInvoiceTemplate=f.retrieveCustomData(g.updateInvoiceTemplate,v);
angular.forEach(g.updateInvoiceTemplate,function(y){if(y.model){w.push(y.model)
}});
if(w.length>0){o.invoiceDbFieldName=w
}},function(u){},function(u){});
var t=[];
var p=f.retrieveSectionWiseCustomFieldInfo("parcel");
g.parcelDbTypeParent={};
p.then(function(w){g.updateParcelParentTemplate=w.genralSection;
var v=[];
var u=Object.keys(q).map(function(x,y){angular.forEach(this[x],function(z){if(x==="Parcel#P#"){v.push({Parcel:z})
}})
},q);
g.updateParcelParentTemplate=f.retrieveCustomData(g.updateParcelParentTemplate,v);
angular.forEach(g.updateParcelParentTemplate,function(x){if(x.model){t.push(x.model)
}});
g.flag.customFieldGeneratedForUpdate=true
},function(u){},function(u){});
var r=f.retrieveSectionWiseCustomFieldInfo("parcel");
if(!(g.parcelDbType!=null)){g.parcelDbType={}
}r.then(function(w){g.updateParcelTemplate=w.genralSection;
g.listOfModelsOfDateType=[];
if(g.updateParcelTemplate!==null&&g.updateParcelTemplate!==undefined){angular.forEach(g.updateParcelTemplate,function(y){if(y.type!==null&&y.type!==undefined&&y.type==="date"){g.listOfModelsOfDateType.push(y.model)
}if(y.model==="in_stock_of_parcel$UMS$String"){y.isViewFromDesignation=true
}})
}var x=[];
var u=Object.keys(q).map(function(y,z){angular.forEach(this[y],function(A){if(y==="Parcel"){x.push({Parcel:A})
}})
},q);
g.updateParcelTemplate=f.retrieveCustomData(g.updateParcelTemplate,x);
angular.forEach(g.updateParcelTemplate,function(y){if(y.model&&t.indexOf(y.model)===-1){t.push(y.model)
}});
e.unMaskLoading();
if(n.custom1!=null){g.parcelDataBean={};
g.parcelCustom=angular.copy(n.custom1);
g.parcelDataBean.parcelDbType=angular.copy(g.parcelDbType)
}var v=[];
v.push(n.value);
o.parcelObjectId=v;
if(t.length>0){o.parcelDbFieldName=t
}k.retrieveParcelById(o,function(y){g.flag.customFieldGeneratedForUpdate=true;
g.parcelDataBean={};
if(y.custom1!==null&&y.custom1!==undefined){angular.forEach(g.listOfModelsOfDateType,function(z){if(y.custom1.hasOwnProperty(z)){if(y.custom1[z]!==null&&y.custom1[z]!==undefined){y.custom1[z]=new Date(y.custom1[z])
}else{y.custom1[z]=""
}}});
g.parcelCustom=angular.copy(y.custom1);
g.parcelParentCustom=angular.copy(y.custom1)
}else{g.parcelCustom={};
g.parcelParentCustom={}
}g.parcelDataBean.parcelDbType=angular.copy(g.parcelDbType);
if(y.custom3!==null&&y.custom3!==undefined){angular.forEach(g.listOfModelsOfDateType,function(z){if(y.custom3.hasOwnProperty(z)){if(y.custom3[z]!==null&&y.custom3[z]!==undefined){y.custom3[z]=new Date(y.custom3[z])
}else{y.custom3[z]=""
}}});
g.invoiceCustom=y.custom3
}else{g.invoiceCustom={}
}e.unMaskLoading()
},function(){e.unMaskLoading();
var z="Could not retrieve parcel, please try again.";
var y=e.error;
e.addMessage(z,y)
})
},function(u){},function(u){});
g.flag.customFieldGenerated=true
},function(){e.unMaskLoading();
var q="Failed to retrieve data";
var p=e.error;
e.addMessage(q,p)
})
}};
if(e.editParcel){g.editParcel(e.parcelByInvoice)
}g.deleteParcel=function(){$("#deleteParcelPopUp").modal("show")
};
g.deleteParcelFromPopup=function(){console.log(g.parcelId);
if(g.parcelId!==undefined&&g.parcelId!==null){e.maskLoading();
k.deleteParcel(g.parcelId,function(){e.unMaskLoading();
g.hideParcelPopUp();
g.onCanel();
g.searchedData=[];
g.listFilled=false
},function(){e.unMaskLoading()
})
}};
g.hideParcelPopUp=function(){$("#deleteParcelPopUp").modal("hide");
e.removeModalOpenCssAfterModalHide()
}
}])
});