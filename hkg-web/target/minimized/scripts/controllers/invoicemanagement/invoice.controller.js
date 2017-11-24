define(["hkg","invoiceService","parcelService","roughPurchaseService","customFieldService","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicFormGrid","editableCustomGrid","accordionCollapse"],function(a){a.register.controller("InvoiceController",["$rootScope","$scope","InvoiceService","ParcelService","RoughPurchaseService","DynamicFormService","CustomFieldService","$q",function(h,l,i,f,m,k,j,e){h.mainMenu="stockLink";
h.childMenu="roughInvoice";
l.entity="INVOICE.";
l.invoiceDataBean={};
h.activateMenu();
var g={};
l.searchCustom=k.resetSection(l.generalInvoiceTemplate);
var c=k.retrieveSearchWiseCustomFieldInfo("invoiceAddEdit");
l.dbType={};
var b=new Date();
l.isSearch=false;
var d=b.getFullYear();
l.selectedAssociatedPurchases=[];
l.selectedAssociatedParcels=[];
l.initInvoiceForm=function(n){l.invoiceForm=n
};
l.createOrUpdateInvoice=function(n,o,p){l.isInvoiceIdSequence(n,function(q){if(q!==undefined&&(q.invoiceExist==false||q.invoiceExist=="false")){l.invoiceDataBean.invoiceCustom=n.categoryCustom;
l.invoiceDataBean.id=n.value;
l.invoiceDataBean.invoiceDbType=o;
l.invoiceDataBean.isArchive=false;
l.invoiceDataBean.uiFieldMap=l.map;
l.invoiceDataBean.ruleConfigMap={fieldIdNameMap:l.invoiceFieldIdNameMap,featureName:"roughInvoice"};
l.invoiceDataBean.year=n.year;
if(n.categoryCustom["invoiceId$AG$String"]!=null){l.invoiceDataBean.sequenceNumber=n.categoryCustom["invoiceId$AG$String"]
}h.maskLoading();
if(l.invoiceDataBean.id==null||l.invoiceDataBean.id==undefined){i.createInvoice(l.invoiceDataBean,function(w){var v=e.defer();
var x=v.promise;
x.then(function(){p(w)
});
v.resolve();
h.unMaskLoading()
},function(){h.unMaskLoading();
var w="Could not create invoice, please try again.";
var v=h.error;
h.addMessage(w,v)
})
}else{i.updateInvoice(l.invoiceDataBean,function(w){var v=e.defer();
var x=v.promise;
x.then(function(){p(w)
});
v.resolve();
h.unMaskLoading()
},function(){h.unMaskLoading();
var w="Could not update invoice, please try again.";
var v=h.error;
h.addMessage(w,v)
})
}}else{var u="Invoice already exists for the same id.";
var s=h.error;
h.addMessage(u,s);
var r=e.defer();
var t=r.promise;
t.then(function(){p(null)
});
r.resolve()
}})
};
l.deleteInvoice=function(n){i.deleteInvoice(n.value,function(o){l.retrieveInvoices();
h.unMaskLoading()
},function(){h.unMaskLoading();
var p="Could not delete invoice, please try again.";
var o=h.error;
h.addMessage(p,o)
})
};
l.getNextInvoiceSequence=function(s){var p=0;
if(l.editableGridInvoiceOptions.template!==undefined&&l.editableGridInvoiceOptions.template!==null&&l.editableGridInvoiceOptions.template.length>0){for(var q=0;
q<l.editableGridInvoiceOptions.template.length;
q++){if(l.editableGridInvoiceOptions.template[q].model==="invoiceId$AG$String"){p++
}}}if(p>0){i.getNextInvoiceSequence(function(u){var t=e.defer();
var v=t.promise;
v.then(function(){s(u)
});
t.resolve()
})
}else{var o=null;
var n=e.defer();
var r=n.promise;
r.then(function(){s(o)
});
n.resolve()
}};
l.isInvoiceIdSequence=function(o,t){if("invoiceId$AG$String" in o.categoryCustom&&o.categoryCustom["invoiceId$AG$String"]!==null){var q;
if(o.value!==null&&o.value!==undefined){q=o.value
}else{q=null
}var r={invoiceId:o.categoryCustom["invoiceId$AG$String"],invoiceObjectId:q};
i.isInvoiceIdExists(r,function(u){var v=e.defer();
var w=v.promise;
w.then(function(){t(u)
});
v.resolve()
})
}else{var n={invoiceExist:false};
var p=e.defer();
var s=p.promise;
s.then(function(){t(n)
});
p.resolve()
}};
l.retrieveInvoicePaginatedData=function(){if(!l.isSearch){l.parameters=["invoiceAddEdit","GEN"];
j.retrieveDesignationBasedFieldsBySection(l.parameters,function(p){l.response=angular.copy(p);
l.invoiceDataBean={};
var q=k.retrieveSectionWiseCustomFieldInfo("invoice");
if(!(l.dbTypeForUpdate!=null)){l.dbTypeForUpdate={}
}q.then(function(v){l.editableGridInvoiceOptions.template=null;
l.editableGridInvoiceOptions.template=v.genralSection;
l.listOfModelsOfDateType=[];
var u=[];
var s=Object.keys(p).map(function(w,x){angular.forEach(this[w],function(y){if(w==="Invoice"){u.push({Invoice:y})
}})
},p);
l.editFieldName=[];
l.invoiceFieldIdNameMap={};
l.editableGridInvoiceOptions.template=k.retrieveCustomData(l.editableGridInvoiceOptions.template,u);
angular.forEach(l.editableGridInvoiceOptions.template,function(w){if(w.fromModel){l.invoiceFieldIdNameMap[w.fieldId]=w.fromModel;
if(l.editFieldName.indexOf(w.fromModel)===-1){l.editFieldName.push(w.fromModel)
}}else{if(w.toModel){l.invoiceFieldIdNameMap[w.fieldId]=w.toModel;
if(l.editFieldName.indexOf(w.toModel)===-1){l.editFieldName.push(w.toModel)
}}else{if(w.model){l.invoiceFieldIdNameMap[w.fieldId]=w.model;
if(l.editFieldName.indexOf(w.model)===-1){l.editFieldName.push(w.model)
}}}}});
l.invoiceShowConfigMsg=false;
var r=[];
if(l.editableGridInvoiceOptions.template!==undefined&&l.editableGridInvoiceOptions.template!==null&&l.editableGridInvoiceOptions.template.length>0){for(var t=0;
t<l.editableGridInvoiceOptions.template.length;
t++){r.push(angular.copy(l.editableGridInvoiceOptions.template[t].model))
}}if(r.length==0){l.invoiceShowConfigMsg=true
}l.map={};
l.map.dbFieldNames=l.editFieldName;
l.invoiceDataBean={};
l.invoiceDataBean.uiFieldMap=l.map;
l.invoiceDataBean.offset=l.editableGridInvoiceOptions.datarows.length-1;
l.invoiceDataBean.limit=10;
l.invoiceDataBean.ruleConfigMap={fieldIdNameMap:l.invoiceFieldIdNameMap,featureName:"roughInvoice"};
if(l.editFieldName.length>0){i.retrieveInvoices(l.invoiceDataBean,function(w){if(w!==undefined){angular.forEach(w,function(C){var z=angular.copy(C);
z.isEditGridFlag=false;
z.invoiceId=C.value;
if(z.categoryCustom!=null){for(var B in z.categoryCustom){var A=B.split("$");
if(A[1]=="AG"){var D=z.categoryCustom[B];
if(D!=null&&D){var y=D.split("-");
z.categoryCustom[B]=y[1]
}}}}l.editableGridInvoiceOptions.datarows.push(z);
l.editableGridInvoiceOptions.datarowsFromDb.push(z)
})
}var x=function(y){angular.forEach(y,function(z){l.editableGridInvoiceOptions.labelrows.push(angular.copy(z));
l.editableGridInvoiceOptions.labelrowsFromDb.push(angular.copy(z))
});
if(l.editableGridInvoiceOptions.labelrows.length>0){l.allInvoiceDataRetrieved=true
}else{l.allInvoiceDataRetrieved=false
}};
l.newVar=angular.copy(w);
if(l.newVar!==undefined){k.convertorForCustomField(l.newVar,x,true)
}h.unMaskLoading()
},function(){var x="Could not save, please try again.";
var w=h.error;
h.addMessage(x,w);
h.unMaskLoading()
})
}},function(r){},function(r){})
},function(){h.unMaskLoading();
var q="Failed to retrieve data";
var p=h.error;
h.addMessage(q,p)
})
}else{if(Object.getOwnPropertyNames(l.searchCustom).length>0){l.searchFinal=angular.copy(l.searchCustom);
var o={};
var n=k.convertSearchData(l.generalInvoiceTemplate,null,null,null,l.searchFinal);
angular.forEach(g,function(t,r){var s=n[r];
if(s!==undefined){var q={};
if(!o[t]){q[r]=s;
o[t]=q
}else{var p=o[t];
p[r]=s;
o[t]=p
}}else{s=l.searchCustom["to"+r];
if(s!==undefined){var q={};
if(!o[t]){q["to"+r]=s;
o[t]=q
}else{var p=o[t];
p["to"+r]=s;
o[t]=p
}}s=l.searchCustom["from"+r];
if(s!==undefined){var q={};
if(!o[t]){q["from"+r]=s;
o[t]=q
}else{var p=o[t];
p["from"+r]=s;
o[t]=p
}}}});
l.invoiceDataBean.featureCustomMapValue=o;
l.invoiceDataBean.searchOnParameter=true;
l.invoiceDataBean.uiFieldMap=l.map;
l.invoiceDataBean.offset=l.editableGridInvoiceOptions.datarows.length-1;
l.invoiceDataBean.limit=10;
l.invoiceDataBean.ruleConfigMap={fieldIdNameMap:l.invoiceFieldIdNameMap,featureName:"roughInvoice"};
i.search(l.invoiceDataBean,function(p){if(p!==undefined&&p!==null&&p.length>0){angular.forEach(p,function(v){var s=angular.copy(v);
s.isEditGridFlag=false;
s.invoiceId=v.value;
if(s.categoryCustom!=null){for(var u in s.categoryCustom){var t=u.split("$");
if(t[1]=="AG"){var w=s.categoryCustom[u];
if(w!=null&&w){var r=w.split("-");
s.categoryCustom[u]=r[1]
}}}}l.editableGridInvoiceOptions.datarows.push(s)
});
l.newVar=angular.copy(p);
var q=function(r){l.editableGridInvoiceOptions.labelrows=angular.copy(r)
};
if(l.newVar!==undefined){k.convertorForCustomField(l.newVar,q,true)
}}if(l.editableGridInvoiceOptions.labelrows.length>0){l.allInvoiceDataRetrieved=true
}else{l.allInvoiceDataRetrieved=false
}},function(){var q="Could not retrieve, please try again.";
var p=h.error;
h.addMessage(q,p);
h.unMaskLoading()
})
}else{l.isSearch=false;
this.retrieveInvoicePaginatedData();
h.unMaskLoading()
}}};
l.getTotalInvoiceCount=function(n){if(!l.isSearch){i.getTotalCountOfInvoices(function(o){l.editableGridInvoiceOptions.totalItems=o.totalItems+1;
l.invoiceTotalItems=o.totalItems+1
})
}else{i.getTotalCountOfSearchInvoices(n,function(o){l.editableGridInvoiceOptions.totalItems=o.totalItems+1;
l.invoiceTotalItems=o.totalItems+1
})
}};
l.getTotalInvoiceCount();
l.editableGridInvoiceOptions={datarows:[],template:{},labelrows:[],labelrowsFromDb:[],datarowsFromDb:[],dbType:{},createOrUpdateRecord:l.createOrUpdateInvoice,deleteRecord:l.deleteInvoice,retrievePaginatedRecord:l.retrieveInvoicePaginatedData,totalItems:l.invoiceTotalItems,deleteModalId:"invoiceModalPanel",cancelModalId:"invoiceCancelModalPanel",featureName:"Rough Invoice",seqId:l.getNextInvoiceSequence,tableName:"invoiceTable",linked:false,};
c.then(function(p){featureDbFieldMap={};
l.generalInvoiceTemplate=[];
l.generalInvoiceTemplate=p.genralSection;
l.searchInvoiceTemplate=[];
l.searchParcelTemplate=[];
if(l.generalInvoiceTemplate!=null&&l.generalInvoiceTemplate.length>0){for(var n=0;
n<l.generalInvoiceTemplate.length;
n++){var o=l.generalInvoiceTemplate[n];
if(o.featureName.toLowerCase()==="invoice"){l.searchInvoiceTemplate.push(angular.copy(o))
}else{if(o.featureName.toLowerCase()==="parcel"){l.searchParcelTemplate.push(angular.copy(o))
}}g[o.model]=o.featureName
}}l.invoiceAddEditFlag=true
},function(n){},function(n){});
l.retrieveInvoices=function(){l.parameters=["invoiceAddEdit","GEN"];
j.retrieveDesignationBasedFieldsBySection(l.parameters,function(n){l.response=angular.copy(n);
l.invoiceDataBean={};
var o=k.retrieveSectionWiseCustomFieldInfo("invoice");
if(!(l.dbTypeForUpdate!=null)){l.dbTypeForUpdate={}
}o.then(function(t){l.editableGridInvoiceOptions.template=null;
l.editableGridInvoiceOptions.template=t.genralSection;
l.listOfModelsOfDateType=[];
var s=[];
var q=Object.keys(n).map(function(u,v){angular.forEach(this[u],function(w){if(u==="Invoice"){s.push({Invoice:w})
}})
},n);
l.editFieldName=[];
l.invoiceFieldIdNameMap={};
l.editableGridInvoiceOptions.template=k.retrieveCustomData(l.editableGridInvoiceOptions.template,s);
angular.forEach(l.editableGridInvoiceOptions.template,function(u){if(u.fromModel){l.invoiceFieldIdNameMap[u.fieldId]=u.fromModel;
if(l.editFieldName.indexOf(u.fromModel)===-1){l.editFieldName.push(u.fromModel)
}}else{if(u.toModel){l.invoiceFieldIdNameMap[u.fieldId]=u.toModel;
if(l.editFieldName.indexOf(u.toModel)===-1){l.editFieldName.push(u.toModel)
}}else{if(u.model){l.invoiceFieldIdNameMap[u.fieldId]=u.model;
if(l.editFieldName.indexOf(u.model)===-1){l.editFieldName.push(u.model)
}}}}});
l.invoiceShowConfigMsg=false;
var p=[];
if(l.editableGridInvoiceOptions.template!==undefined&&l.editableGridInvoiceOptions.template!==null&&l.editableGridInvoiceOptions.template.length>0){for(var r=0;
r<l.editableGridInvoiceOptions.template.length;
r++){p.push(angular.copy(l.editableGridInvoiceOptions.template[r].model))
}}if(p.length==0){l.invoiceShowConfigMsg=true
}l.map={};
l.map.dbFieldNames=l.editFieldName;
l.invoiceDataBean={};
l.invoiceDataBean.uiFieldMap=l.map;
l.invoiceDataBean.offset=0;
l.invoiceDataBean.limit=9;
l.invoiceDataBean.ruleConfigMap={fieldIdNameMap:l.invoiceFieldIdNameMap,featureName:"roughInvoice"};
if(l.editFieldName.length>0){l.getTotalInvoiceCount();
i.retrieveInvoices(l.invoiceDataBean,function(w){if(w!==undefined){l.editableGridInvoiceOptions.datarows=[];
l.editableGridInvoiceOptions.labelrows=[];
var u=0;
if(l.editableGridInvoiceOptions.template!==undefined&&l.editableGridInvoiceOptions.template!==null&&l.editableGridInvoiceOptions.template.length>0){for(var v=0;
v<l.editableGridInvoiceOptions.template.length;
v++){if(l.editableGridInvoiceOptions.template[v].model==="invoiceId$AG$String"){u++
}}}if(u>0){i.getNextInvoiceSequence(function(y){l.editableGridInvoiceOptions.datarows.push({isEditGridFlag:false,categoryCustom:{"invoiceId$AG$String":y["invoiceId$AG$String"]},beforeLabel:d+"-"});
angular.forEach(w,function(D){var A=angular.copy(D);
A.isEditGridFlag=false;
A.invoiceId=D.value;
if(A.categoryCustom!=null){for(var C in A.categoryCustom){var B=C.split("$");
if(B[1]=="AG"){var E=A.categoryCustom[C];
if(E!=null&&E){var z=E.split("-");
A.categoryCustom[C]=z[1]
}}}}l.editableGridInvoiceOptions.datarows.push(A);
l.editableGridInvoiceOptions.datarowsFromDb.push(A)
})
})
}else{l.editableGridInvoiceOptions.datarows.push({isEditGridFlag:false,categoryCustom:{},beforeLabel:d+"-"});
angular.forEach(w,function(C){var z=angular.copy(C);
z.isEditGridFlag=false;
z.invoiceId=C.value;
if(z.categoryCustom!=null){for(var B in z.categoryCustom){var A=B.split("$");
if(A[1]=="AG"){var D=z.categoryCustom[B];
if(D!=null&&D){var y=D.split("-");
z.categoryCustom[B]=y[1]
}}}}l.editableGridInvoiceOptions.datarows.push(z);
l.editableGridInvoiceOptions.datarowsFromDb.push(z)
})
}}var x=function(y){l.editableGridInvoiceOptions.labelrows.push({isEditGridFlag:false});
angular.forEach(y,function(z){l.editableGridInvoiceOptions.labelrows.push(angular.copy(z));
l.editableGridInvoiceOptions.labelrowsFromDb.push(angular.copy(z))
});
if(l.editableGridInvoiceOptions.labelrows.length>0){l.allInvoiceDataRetrieved=true
}else{l.allInvoiceDataRetrieved=false
}};
l.newVar=angular.copy(w);
if(l.newVar!==undefined){k.convertorForCustomField(l.newVar,x,true)
}h.unMaskLoading()
},function(){var v="Could not save, please try again.";
var u=h.error;
h.addMessage(v,u);
h.unMaskLoading()
})
}},function(p){},function(p){})
},function(){h.unMaskLoading();
var o="Failed to retrieve data";
var n=h.error;
h.addMessage(o,n)
})
};
l.retrieveSearchedData=function(){l.isSearch=true;
l.selectOneParameter=false;
l.showParcels=false;
l.searchedData=[];
l.listFilled=false;
if(Object.getOwnPropertyNames(l.searchCustom).length>0){l.searchFinal=angular.copy(l.searchCustom);
var o={};
var n=k.convertSearchData(l.generalInvoiceTemplate,null,null,null,l.searchFinal);
angular.forEach(g,function(t,r){var s=n[r];
if(s!==undefined){var q={};
if(!o[t]){q[r]=s;
o[t]=q
}else{var p=o[t];
p[r]=s;
o[t]=p
}}else{s=l.searchCustom["to"+r];
if(s!==undefined){var q={};
if(!o[t]){q["to"+r]=s;
o[t]=q
}else{var p=o[t];
p["to"+r]=s;
o[t]=p
}}s=l.searchCustom["from"+r];
if(s!==undefined){var q={};
if(!o[t]){q["from"+r]=s;
o[t]=q
}else{var p=o[t];
p["from"+r]=s;
o[t]=p
}}}});
l.invoiceDataBean.featureCustomMapValue=o;
l.invoiceDataBean.searchOnParameter=true;
l.invoiceDataBean.uiFieldMap=l.map;
l.invoiceDataBean.offset=1;
l.invoiceDataBean.limit=10;
l.invoiceDataBean.ruleConfigMap={fieldIdNameMap:l.invoiceFieldIdNameMap,featureName:"roughInvoice"};
l.getTotalInvoiceCount();
i.search(l.invoiceDataBean,function(r){l.editableGridInvoiceOptions.datarows=[];
l.editableGridInvoiceOptions.labelrows=[];
var p=0;
if(l.editableGridInvoiceOptions.template!==undefined&&l.editableGridInvoiceOptions.template!==null&&l.editableGridInvoiceOptions.template.length>0){for(var q=0;
q<l.editableGridInvoiceOptions.template.length;
q++){if(l.editableGridInvoiceOptions.template[q].model==="invoiceId$AG$String"){p++
}}}if(p>0){i.getNextInvoiceSequence(function(t){l.editableGridInvoiceOptions.datarows.push({isEditGridFlag:false,categoryCustom:{"invoiceId$AG$String":t["invoiceId$AG$String"]},beforeLabel:d+"-"});
if(r!==undefined&&r!==null&&r.length>0){angular.forEach(r,function(z){var w=angular.copy(z);
w.isEditGridFlag=false;
w.invoiceId=z.value;
if(w.categoryCustom!=null){for(var y in w.categoryCustom){var x=y.split("$");
if(x[1]=="AG"){var A=w.categoryCustom[y];
if(A!=null&&A){var v=A.split("-");
w.categoryCustom[y]=v[1]
}}}}l.editableGridInvoiceOptions.datarows.push(w);
l.editableGridInvoiceOptions.datarowsFromDb.push(w)
});
l.newVar=angular.copy(r);
var u=function(v){l.editableGridInvoiceOptions.labelrows.push({isEditGridFlag:false});
angular.forEach(v,function(w){l.editableGridInvoiceOptions.labelrows.push(angular.copy(w));
l.editableGridInvoiceOptions.labelrowsFromDb.push(angular.copy(w))
})
};
if(l.newVar!==undefined){k.convertorForCustomField(l.newVar,u,true)
}}})
}else{l.editableGridInvoiceOptions.datarows.push({isEditGridFlag:false,categoryCustom:{},beforeLabel:d+"-"});
if(r!==undefined&&r!==null&&r.length>0){angular.forEach(r,function(x){var u=angular.copy(x);
u.isEditGridFlag=false;
u.invoiceId=x.value;
if(u.categoryCustom!=null){for(var w in u.categoryCustom){var v=w.split("$");
if(v[1]=="AG"){var y=u.categoryCustom[w];
if(y!=null&&y){var t=y.split("-");
u.categoryCustom[w]=t[1]
}}}}l.editableGridInvoiceOptions.datarows.push(u);
l.editableGridInvoiceOptions.datarowsFromDb.push(u)
});
l.newVar=angular.copy(r);
var s=function(t){l.editableGridInvoiceOptions.labelrows.push({isEditGridFlag:false});
angular.forEach(t,function(u){l.editableGridInvoiceOptions.labelrows.push(angular.copy(u));
l.editableGridInvoiceOptions.labelrowsFromDb.push(angular.copy(u))
})
};
if(l.newVar!==undefined){k.convertorForCustomField(l.newVar,s,true)
}}}if(l.editableGridInvoiceOptions.labelrows.length>0){l.allInvoiceDataRetrieved=true
}else{l.allInvoiceDataRetrieved=false
}},function(){var q="Could not retrieve, please try again.";
var p=h.error;
h.addMessage(q,p);
h.unMaskLoading()
})
}else{this.retrieveInvoices()
}};
l.retrieveInvoices();
l.createOrUpdateParcel=function(n,o,p){l.isParcelIdSequence(n,function(q){if(q!==undefined&&(q.parcelExist==false||q.parcelExist=="false")){l.parcelDataBean={};
l.parcelDataBean.parcelCustom=n.categoryCustom;
l.parcelDataBean.id=n.value;
l.parcelDataBean.parcelDbType=o;
l.parcelDataBean.invoiceDataBean={id:l.selectedInvoice.value};
l.parcelDataBean.featureDbFieldMap=l.parcelMap;
l.parcelDataBean.ruleConfigMap={fieldIdNameMap:l.parcelFieldIdNameMap,featureName:"roughInvoice"};
if(n.categoryCustom["parcelId$AG$String"]!=null){l.parcelDataBean.sequenceNumber=n.categoryCustom["parcelId$AG$String"]
}l.parcelDataBean.year=n.year;
h.maskLoading();
if(l.parcelDataBean.id==null||l.parcelDataBean.id==undefined){f.createParcel(l.parcelDataBean,function(w){var v=e.defer();
var x=v.promise;
x.then(function(){p(w)
});
v.resolve();
h.unMaskLoading()
},function(){h.unMaskLoading();
var w="Could not create parcel, please try again.";
var v=h.error;
h.addMessage(w,v)
})
}else{f.updateParcel(l.parcelDataBean,function(w){var v=e.defer();
var x=v.promise;
x.then(function(){p(w)
});
v.resolve();
h.unMaskLoading()
},function(){h.unMaskLoading();
var w="Could not update parcel, please try again.";
var v=h.error;
h.addMessage(w,v)
})
}}else{var u="Parcel already exists for the same id.";
var s=h.error;
h.addMessage(u,s);
var r=e.defer();
var t=r.promise;
t.then(function(){p(null)
});
r.resolve()
}})
};
l.isParcelIdSequence=function(p,t){if("parcelId$AG$String" in p.categoryCustom&&p.categoryCustom["parcelId$AG$String"]!==null){var o;
if(p.value!==null&&p.value!==undefined){o=p.value
}else{o=null
}var r={parcelId:p.categoryCustom["parcelId$AG$String"],parcelObjectId:o};
f.isParcelIdExists(r,function(u){var v=e.defer();
var w=v.promise;
w.then(function(){t(u)
});
v.resolve()
})
}else{var n={parcelExist:false};
var q=e.defer();
var s=q.promise;
s.then(function(){t(n)
});
q.resolve()
}};
l.deleteParcel=function(n){f.deleteParcel(n.value,function(o){l.retrieveParcelByInvoiceId();
h.unMaskLoading()
},function(){h.unMaskLoading();
var p="Could not delete parcel, please try again.";
var o=h.error;
h.addMessage(p,o)
})
};
l.getNextParcelSequence=function(s){var r=0;
if(l.editableGridParcelOptions.template!==undefined&&l.editableGridParcelOptions.template!==null&&l.editableGridParcelOptions.template.length>0){for(var p=0;
p<l.editableGridParcelOptions.template.length;
p++){if(l.editableGridParcelOptions.template[p].model==="parcelId$AG$String"){r++
}}}if(r>0){f.getNextParcelSequence(function(u){var t=e.defer();
var v=t.promise;
v.then(function(){s(u)
});
t.resolve()
})
}else{var o=null;
var n=e.defer();
var q=n.promise;
q.then(function(){s(o)
});
n.resolve()
}};
l.delinkRoughPurchase=function(n){if(n&&n!=null){f.deLinkRoughParcelWithPurchase(n.value,function(o){console.log("result")
})
}};
l.getTotalParcelCount=function(n){f.getTotalCountOfParcels(n,function(o){l.editableGridParcelOptions.totalItems=o.totalItems+1;
l.parcelTotalItems=o.totalItems+1
})
};
l.retrieveParcelPaginatedData=function(){l.selectedInvoice=l.editableGridInvoiceOptions.getSelectedTableRows();
if(l.selectedInvoice!==undefined&&l.selectedInvoice!==null&&l.selectedInvoice.value!==undefined&&l.selectedInvoice.value!==null){l.parameters=["invoiceAddEdit","MRP"];
j.retrieveDesignationBasedFieldsBySection(l.parameters,function(n){l.response=angular.copy(n);
var o=k.retrieveSectionWiseCustomFieldInfo("parcel");
if(!(l.dbTypeForUpdate!=null)){l.dbTypeForUpdate={}
}o.then(function(s){l.editableGridParcelOptions.template=angular.copy(s.genralSection);
var q=[];
var p=Object.keys(n).map(function(t,u){angular.forEach(this[t],function(v){if(t==="Parcel"){q.push({Parcel:v})
}})
},n);
l.pareclEditFieldName=[];
l.parcelFieldIdNameMap={};
l.editableGridParcelOptions.template=k.retrieveCustomData(l.editableGridParcelOptions.template,q);
angular.forEach(l.editableGridParcelOptions.template,function(t){if(t.fromModel){l.parcelFieldIdNameMap[t.fieldId]=t.fromModel;
if(l.pareclEditFieldName.indexOf(t.fromModel)===-1){l.pareclEditFieldName.push(t.fromModel)
}}else{if(t.toModel){l.parcelFieldIdNameMap[t.fieldId]=t.toModel;
if(l.pareclEditFieldName.indexOf(t.toModel)===-1){l.pareclEditFieldName.push(t.toModel)
}}else{if(t.model){l.parcelFieldIdNameMap[t.fieldId]=t.model;
if(l.pareclEditFieldName.indexOf(t.model)===-1){l.pareclEditFieldName.push(t.model)
}}}}});
l.parcelMap={};
var r=[];
r.push(l.selectedInvoice.value);
l.editableGridParcelOptions.invoiceId=l.selectedInvoice.value;
l.parcelMap.dbFieldNames=angular.copy(l.pareclEditFieldName);
l.parcelMap.invoiceObjectId=angular.copy(r);
l.parcelDataBean={};
l.parcelDataBean.featureDbFieldMap=l.parcelMap;
l.parcelDataBean.offset=l.editableGridParcelOptions.datarows.length-1;
l.parcelDataBean.limit=10;
l.parcelDataBean.ruleConfigMap={fieldIdNameMap:l.parcelFieldIdNameMap,featureName:"roughInvoice"};
if(l.pareclEditFieldName.length>0){f.retrieveParcelByInvoiceId(l.parcelDataBean,function(t){if(t!==undefined){angular.forEach(t,function(z){var w=angular.copy(z);
w.isEditGridFlag=false;
w.beforeLabel=d+"-";
w.invoiceId=l.selectedInvoice;
w.parcelId=z.value;
if(w.categoryCustom!=null){for(var y in w.categoryCustom){var x=y.split("$");
if(x[1]=="AG"){var A=w.categoryCustom[y];
if(A!=null&&A){var v=A.split("-");
w.categoryCustom[y]=v[1]
}}if(y=="linkPurchase"){w.isLinked=w.categoryCustom[y]
}}}l.editableGridParcelOptions.datarows.push(w);
l.editableGridParcelOptions.datarowsFromDb.push(w)
})
}var u=function(v){angular.forEach(v,function(w){l.editableGridParcelOptions.labelrows.push(w);
l.editableGridParcelOptions.labelrowsFromDb.push(w)
});
if(l.editableGridParcelOptions.labelrows.length>0){l.allParcelDataRetrieved=true
}else{l.allParcelDataRetrieved=false
}};
l.newVar=angular.copy(t);
if(l.newVar!==undefined){k.convertorForCustomField(l.newVar,u,true)
}h.unMaskLoading()
},function(){var u="Could not save, please try again.";
var t=h.error;
h.addMessage(u,t);
h.unMaskLoading()
})
}},function(p){},function(p){})
},function(){h.unMaskLoading();
var o="Failed to retrieve data";
var n=h.error;
h.addMessage(o,n)
})
}};
l.editableGridParcelOptions={datarows:[],datarowsFromDb:[],template:{},labelrows:[],labelrowsFromDb:[],dbType:{},createOrUpdateRecord:l.createOrUpdateParcel,deleteRecord:l.deleteParcel,deleteModalId:"parcelModalPanel",cancelModalId:"parcelCancelModalPanel",enableselection:false,featureName:"Rough Parcel",seqId:l.getNextParcelSequence,tableName:"parcelTable",linked:true,linkEntity:l.delinkRoughPurchase,invoiceId:l.selectedInvoice,retrievePaginatedRecord:l.retrieveParcelPaginatedData,totalItems:l.parcelTotalItems};
l.$watch(function(){if(!!l.editableGridInvoiceOptions.getSelectedTableRows){return l.editableGridInvoiceOptions.getSelectedTableRows()
}},function(n,o){if(!!l.editableGridInvoiceOptions.getSelectedTableRows){if(angular.isArray(l.editableGridInvoiceOptions.getSelectedTableRows())){if(l.editableGridInvoiceOptions.getSelectedTableRows().length>0){l.showParcels=true;
l.retrieveParcelByInvoiceId()
}else{l.showParcels=false
}}else{if(l.editableGridInvoiceOptions.getSelectedTableRows()!=null){l.showParcels=true;
l.retrieveParcelByInvoiceId()
}else{l.showParcels=false
}}}});
l.retrieveParcelByInvoiceId=function(){l.selectedInvoice=l.editableGridInvoiceOptions.getSelectedTableRows();
if(l.selectedInvoice!==undefined&&l.selectedInvoice!==null&&l.selectedInvoice.value!==undefined&&l.selectedInvoice.value!==null){l.parameters=["invoiceAddEdit","MRP"];
j.retrieveDesignationBasedFieldsBySection(l.parameters,function(n){l.response=angular.copy(n);
var o=k.retrieveSectionWiseCustomFieldInfo("parcel");
if(!(l.dbTypeForUpdate!=null)){l.dbTypeForUpdate={}
}o.then(function(v){l.editableGridParcelOptions.template=angular.copy(v.genralSection);
var r=[];
var q=Object.keys(n).map(function(w,x){angular.forEach(this[w],function(y){if(w==="Parcel"){r.push({Parcel:y})
}})
},n);
l.pareclEditFieldName=[];
l.parcelFieldIdNameMap={};
l.editableGridParcelOptions.template=k.retrieveCustomData(l.editableGridParcelOptions.template,r);
angular.forEach(l.editableGridParcelOptions.template,function(w){if(w.fromModel){l.parcelFieldIdNameMap[w.fieldId]=w.fromModel;
if(l.pareclEditFieldName.indexOf(w.fromModel)===-1){l.pareclEditFieldName.push(w.fromModel)
}}else{if(w.toModel){l.parcelFieldIdNameMap[w.fieldId]=w.toModel;
if(l.pareclEditFieldName.indexOf(w.toModel)===-1){l.pareclEditFieldName.push(w.toModel)
}}else{if(w.model){l.parcelFieldIdNameMap[w.fieldId]=w.model;
if(l.pareclEditFieldName.indexOf(w.model)===-1){l.pareclEditFieldName.push(w.model)
}}}}});
l.fieldNotConfigured=false;
l.parcelShowConfigMsg=false;
var p=[];
l.mandatoryParcelFields=["origPieces$NM$Long","origCarat$NM$Double","origRateInDollar$NM$Double","origAmountInDollar$FRM$Double"];
if(l.editableGridParcelOptions.template!==undefined&&l.editableGridParcelOptions.template!==null&&l.editableGridParcelOptions.template.length>0){for(var s=0;
s<l.editableGridParcelOptions.template.length;
s++){if(l.editableGridParcelOptions.template[s].model==="origPieces$NM$Long"){l.editableGridParcelOptions.template[s].required=true
}else{if(l.editableGridParcelOptions.template[s].model==="origCarat$NM$Double"){l.editableGridParcelOptions.template[s].required=true
}else{if(l.editableGridParcelOptions.template[s].model==="origRateInDollar$NM$Double"){l.editableGridParcelOptions.template[s].required=true
}else{if(l.editableGridParcelOptions.template[s].model==="origAmountInDollar$FRM$Double"){l.editableGridParcelOptions.template[s].required=true
}}}}p.push(angular.copy(l.editableGridParcelOptions.template[s].model))
}}if(p.length>0&&l.mandatoryParcelFields!=null&&l.mandatoryParcelFields.length>0){for(var u=0;
u<l.mandatoryParcelFields.length;
u++){if(p.indexOf(l.mandatoryParcelFields[u])===-1){l.fieldNotConfigured=true;
break
}}}else{l.fieldNotConfigured=true
}if(l.fieldNotConfigured){l.parcelShowConfigMsg=true
}l.parcelMap={};
var t=[];
t.push(l.selectedInvoice.value);
l.editableGridParcelOptions.invoiceId=l.selectedInvoice.value;
l.parcelMap.dbFieldNames=angular.copy(l.pareclEditFieldName);
l.parcelMap.invoiceObjectId=angular.copy(t);
l.parcelDataBean={};
l.parcelDataBean.featureDbFieldMap=l.parcelMap;
l.parcelDataBean.offset=0;
l.parcelDataBean.limit=9;
l.parcelDataBean.ruleConfigMap={fieldIdNameMap:l.parcelFieldIdNameMap,featureName:"roughInvoice"};
if(l.pareclEditFieldName.length>0){l.getTotalParcelCount(l.selectedInvoice.value);
f.retrieveParcelByInvoiceId(l.parcelDataBean,function(x){if(x!==undefined){l.editableGridParcelOptions.datarows=[];
l.editableGridParcelOptions.labelrows=[];
var z=0;
if(l.editableGridParcelOptions.template!==undefined&&l.editableGridParcelOptions.template!==null&&l.editableGridParcelOptions.template.length>0){for(var w=0;
w<l.editableGridParcelOptions.template.length;
w++){if(l.editableGridParcelOptions.template[w].model==="parcelId$AG$String"){z++
}}}if(z>0){f.getNextParcelSequence(function(A){l.editableGridParcelOptions.datarows.push({isEditGridFlag:false,categoryCustom:{"parcelId$AG$String":A["parcelId$AG$String"]},beforeLabel:d+"-",isLinked:false});
angular.forEach(x,function(F){var C=angular.copy(F);
C.isEditGridFlag=false;
C.beforeLabel=d+"-";
C.invoiceId=l.selectedInvoice;
C.parcelId=F.value;
if(C.categoryCustom!=null){for(var E in C.categoryCustom){var D=E.split("$");
if(D[1]=="AG"){var G=C.categoryCustom[E];
if(G!=null&&G){var B=G.split("-");
C.categoryCustom[E]=B[1]
}}if(E=="linkPurchase"){C.isLinked=C.categoryCustom[E]
}}}l.editableGridParcelOptions.datarows.push(C);
l.editableGridParcelOptions.datarowsFromDb.push(C)
})
})
}else{l.editableGridParcelOptions.datarows.push({isEditGridFlag:false,categoryCustom:{},beforeLabel:d+"-",isLinked:false});
angular.forEach(x,function(E){var B=angular.copy(E);
B.isEditGridFlag=false;
B.beforeLabel=d+"-";
B.invoiceId=l.selectedInvoice;
B.parcelId=E.value;
if(B.categoryCustom!=null){for(var D in B.categoryCustom){var C=D.split("$");
if(C[1]=="AG"){var F=B.categoryCustom[D];
if(F!=null&&F){var A=F.split("-");
B.categoryCustom[D]=A[1]
}}if(D=="linkPurchase"){B.isLinked=B.categoryCustom[D]
}}}l.editableGridParcelOptions.datarows.push(B);
l.editableGridParcelOptions.datarowsFromDb.push(B)
})
}}var y=function(A){l.editableGridParcelOptions.labelrows.push({isEditGridFlag:false});
angular.forEach(A,function(B){l.editableGridParcelOptions.labelrows.push(B);
l.editableGridParcelOptions.labelrowsFromDb.push(B)
});
if(l.editableGridParcelOptions.labelrows.length>0){l.allParcelDataRetrieved=true
}else{l.allParcelDataRetrieved=false
}};
l.newVar=angular.copy(x);
if(l.newVar!==undefined){k.convertorForCustomField(l.newVar,y,true)
}h.unMaskLoading()
},function(){var x="Could not save, please try again.";
var w=h.error;
h.addMessage(x,w);
h.unMaskLoading()
})
}},function(p){},function(p){})
},function(){h.unMaskLoading();
var o="Failed to retrieve data";
var n=h.error;
h.addMessage(o,n)
})
}};
l.onCancelOfSearch=function(){if(l.invoiceForm!==null&&l.invoiceForm!==undefined){l.invoiceForm.$dirty=false
}l.searchedData=[];
l.isSearch=false;
l.submitted=false;
l.selectedInvoice=[];
l.reset("searchCustom");
l.retrieveInvoices()
};
l.reset=function(o){if(o==="searchCustom"){l.searchCustom={};
var n=k.retrieveSearchWiseCustomFieldInfo("invoiceAddEdit");
n.then(function(p){l.generalInvoiceTemplate=p.genralSection;
l.invoiceAddEditFlag=true;
h.unMaskLoading()
},function(p){},function(p){})
}};
l.linkRoughPurchase=function(){l.invoiceAddEditFlag=false;
l.flag={};
l.retrieveAssociatedRoughPurchase();
l.retrieveAssociatedRoughParcel()
};
l.associatedParcelScreenRule=function(q,p,o){var n;
if(!!q.entity.screenRuleDetailsWithDbFieldName&&q.entity.screenRuleDetailsWithDbFieldName[p]!==undefined&&q.entity.screenRuleDetailsWithDbFieldName[p]!==null){if(l.selectedAssociatedParcels.length===0||(l.selectedAssociatedParcels.indexOf(q.entity.$$parcelId$$)===-1)){n=q.entity.screenRuleDetailsWithDbFieldName[p].colorCode
}if(o===true){n=q.entity.screenRuleDetailsWithDbFieldName[p].tooltipMessage
}}return n
};
l.associatedPurchaseScreenRule=function(q,p,o){var n;
if(!!q.entity.screenRuleDetailsWithDbFieldName&&q.entity.screenRuleDetailsWithDbFieldName[p]!==undefined&&q.entity.screenRuleDetailsWithDbFieldName[p]!==null){if(l.selectedAssociatedPurchases.length===0||(l.selectedAssociatedPurchases.indexOf(q.entity.$$purchaseId$$)===-1)){n=q.entity.screenRuleDetailsWithDbFieldName[p].colorCode
}if(o===true){n=q.entity.screenRuleDetailsWithDbFieldName[p].tooltipMessage
}}return n
};
l.retrieveAssociatedRoughPurchase=function(){l.searchedPurchaseData=[];
l.searchedPurchaseDataFromDbForUiGrid=[];
l.associatedPurchaseLabelListForUiGrid=[];
l.associatedPurchaseListFilled=false;
l.parameters=["roughPurchaseLink","GEN"];
j.retrieveDesignationBasedFieldsBySection(l.parameters,function(o){l.response=angular.copy(o);
var n=k.retrieveSectionWiseCustomFieldInfo("purchase");
if(!(l.dbTypeForAssociatedPurchase!=null)){l.dbTypeForAssociatedPurchase={}
}n.then(function(r){l.associatedRoughPurchaseTemplate=angular.copy(r.genralSection);
var q=[];
var p=Object.keys(o).map(function(t,u){angular.forEach(this[t],function(v){if(t==="Purchase"){q.push({Purchase:v})
}})
},o);
l.associatedPurchaseFieldName=[];
l.purchaseFieldIdToNameMap={};
l.associatedRoughPurchaseTemplate=k.retrieveCustomData(l.associatedRoughPurchaseTemplate,q);
angular.forEach(l.associatedRoughPurchaseTemplate,function(t){if(t.fromModel){l.purchaseFieldIdToNameMap[t.fieldId]=t.fromModel;
if(l.associatedPurchaseFieldName.indexOf(t.fromModel)===-1){l.associatedPurchaseFieldName.push(t.fromModel)
}l.associatedPurchaseLabelListForUiGrid.push({name:t.fromModel,displayName:t.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.associatedPurchaseScreenRule(row, '"+t.fromModel+"') }\" title=\"{{grid.appScope.associatedPurchaseScreenRule(row, '"+t.fromModel+"',true)}}\"> {{COL_FIELD CUSTOM_FILTERS}} </div>"})
}else{if(t.toModel){l.purchaseFieldIdToNameMap[t.fieldId]=t.toModel;
if(l.associatedPurchaseFieldName.indexOf(t.toModel)===-1){l.associatedPurchaseFieldName.push(t.toModel)
}l.associatedPurchaseLabelListForUiGrid.push({name:t.toModel,displayName:t.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.associatedPurchaseScreenRule(row, '"+t.toModel+"') }\" title=\"{{grid.appScope.associatedPurchaseScreenRule(row, '"+t.toModel+"',true)}}\"> {{COL_FIELD CUSTOM_FILTERS}} </div>"})
}else{if(t.model){l.purchaseFieldIdToNameMap[t.fieldId]=t.model;
if(l.associatedPurchaseFieldName.indexOf(t.model)===-1){l.associatedPurchaseFieldName.push(t.model)
}l.associatedPurchaseLabelListForUiGrid.push({name:t.model,displayName:t.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.associatedPurchaseScreenRule(row, '"+t.model+"') }\" title=\"{{grid.appScope.associatedPurchaseScreenRule(row, '"+t.model+"',true)}}\"> {{COL_FIELD CUSTOM_FILTERS}} </div>"})
}}}});
l.fieldNotConfiguredForPurchase=false;
l.showConfigMsgForPurchase=false;
if(l.associatedRoughPurchaseTemplate.length<=0){l.showConfigMsgForPurchase=true
}var s={};
l.associatedPurchaseMap={};
l.associatedPurchaseMap.dbFieldNames=angular.copy(l.associatedPurchaseFieldName);
s.uiFieldMap=l.associatedPurchaseMap;
s.ruleConfigMap={fieldIdNameMap:l.purchaseFieldIdToNameMap,featureName:"roughInvoice"};
if(l.associatedPurchaseFieldName.length>0){m.retrieveAssociatedRoughPurchase(s,function(t){if(t!==undefined&&t.length>0){var u=function(v){l.searchedPurchaseData=angular.copy(v);
angular.forEach(l.searchedPurchaseData,function(w){angular.forEach(l.associatedPurchaseLabelListForUiGrid,function(x){if(!w.categoryCustom.hasOwnProperty(x.name)){w.categoryCustom[x.name]="NA"
}else{if(w.categoryCustom.hasOwnProperty(x.name)){if(w.categoryCustom[x.name]===null||w.categoryCustom[x.name]===""||w.categoryCustom[x.name]===undefined){w.categoryCustom[x.name]="NA"
}}}});
w.categoryCustom.$$purchaseId$$=w.value;
w.categoryCustom.screenRuleDetailsWithDbFieldName=w.screenRuleDetailsWithDbFieldName;
l.searchedPurchaseDataFromDbForUiGrid.push(w.categoryCustom)
});
l.associatedPurchaseGridOptions={};
l.associatedPurchaseGridOptions.enableFiltering=true;
l.associatedPurchaseGridOptions.multiSelect=true;
l.associatedPurchaseGridOptions.enableRowSelection=true;
l.associatedPurchaseGridOptions.enableSelectAll=true;
l.associatedPurchaseGridOptions.data=l.searchedPurchaseDataFromDbForUiGrid;
l.associatedPurchaseGridOptions.columnDefs=l.associatedPurchaseLabelListForUiGrid;
l.associatedPurchaseGridOptions.onRegisterApi=function(w){l.associatedPurchaseGridApi=w;
w.selection.on.rowSelectionChanged(l,function(x){l.flag.purchaseRowSelectedflag=true;
l.selectedAssociatedPurchases=[];
angular.forEach(w.selection.getSelectedRows(),function(y){l.selectedAssociatedPurchases.push(y.$$purchaseId$$)
})
});
w.selection.on.rowSelectionChangedBatch(l,function(x){l.flag.purchaseRowSelectedflag=true;
l.selectedAssociatedPurchases=[];
angular.forEach(w.selection.getSelectedRows(),function(y){l.selectedAssociatedPurchases.push(y.$$purchaseId$$)
})
})
};
window.setTimeout(function(){$(window).resize();
$(window).resize()
},100);
l.associatedPurchaseListFilled=true;
h.unMaskLoading()
};
k.convertorForCustomField(t,u,false)
}h.unMaskLoading()
},function(){var u="Could not save, please try again.";
var t=h.error;
h.addMessage(u,t);
h.unMaskLoading()
})
}},function(p){},function(p){})
},function(){h.unMaskLoading();
var o="Failed to retrieve data";
var n=h.error;
h.addMessage(o,n)
})
};
l.retrieveAssociatedRoughParcel=function(){l.searchedParcelData=[];
l.searchedParcelDataFromDbForUiGrid=[];
l.associatedParcelLabelListForUiGrid=[];
l.associatedParcelListFilled=false;
l.parameters=["roughPurchaseLink","RPA"];
j.retrieveDesignationBasedFieldsBySection(l.parameters,function(n){l.response=angular.copy(n);
var o=k.retrieveSectionWiseCustomFieldInfo("parcel");
if(!(l.dbTypeForAssociatedParcel!=null)){l.dbTypeForAssociatedParcel={}
}o.then(function(s){l.associatedRoughParcelTemplate=angular.copy(s.genralSection);
var r=[];
var q=Object.keys(n).map(function(t,u){angular.forEach(this[t],function(v){if(t==="Parcel"){r.push({Parcel:v})
}})
},n);
l.associatedParcelFieldName=[];
l.associatedParcelFieldIdName={};
l.associatedRoughParcelTemplate=k.retrieveCustomData(l.associatedRoughParcelTemplate,r);
angular.forEach(l.associatedRoughParcelTemplate,function(t){if(t.fromModel){l.associatedParcelFieldIdName[t.fieldId]=t.fromModel;
if(l.associatedParcelFieldName.indexOf(t.fromModel)===-1){l.associatedParcelFieldName.push(t.fromModel)
}l.associatedParcelLabelListForUiGrid.push({name:t.fromModel,displayName:t.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.associatedParcelScreenRule(row, '"+t.fromModel+"') }\" title=\"{{grid.appScope.associatedParcelScreenRule(row, '"+t.fromModel+"',true)}}\"> {{COL_FIELD CUSTOM_FILTERS}} </div>"})
}else{if(t.toModel){l.associatedParcelFieldIdName[t.fieldId]=t.toModel;
if(l.associatedParcelFieldName.indexOf(t.toModel)===-1){l.associatedParcelFieldName.push(t.toModel)
}l.associatedParcelLabelListForUiGrid.push({name:t.toModel,displayName:t.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.associatedParcelScreenRule(row, '"+t.toModel+"') }\" title=\"{{grid.appScope.associatedParcelScreenRule(row, '"+t.toModel+"',true)}}\"> {{COL_FIELD CUSTOM_FILTERS}} </div>"})
}else{if(t.model){l.associatedParcelFieldIdName[t.fieldId]=t.model;
if(l.associatedParcelFieldName.indexOf(t.model)===-1){l.associatedParcelFieldName.push(t.model)
}l.associatedParcelLabelListForUiGrid.push({name:t.model,displayName:t.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.associatedParcelScreenRule(row, '"+t.model+"') }\" title=\"{{grid.appScope.associatedParcelScreenRule(row, '"+t.model+"',true)}}\"> {{COL_FIELD CUSTOM_FILTERS}} </div>"})
}}}});
l.fieldNotConfiguredForAssParcel=false;
l.showConfigMsgForAssParcel=false;
if(l.associatedRoughParcelTemplate.length<=0){l.showConfigMsgForAssParcel=true
}var p={};
l.associatedParcelMap={};
l.associatedParcelMap.dbFieldNames=angular.copy(l.associatedParcelFieldName);
p.featureDbFieldMap=l.associatedParcelMap;
p.ruleConfigMap={fieldIdNameMap:l.associatedParcelFieldIdName,featureName:"roughInvoice"};
if(l.associatedParcelFieldName.length>0){f.retrieveAssociatedRoughParcels(p,function(t){if(t!==undefined&&t.length>0){var u=function(v){l.searchedParcelData=angular.copy(v);
angular.forEach(l.searchedParcelData,function(w){angular.forEach(l.associatedParcelLabelListForUiGrid,function(x){if(!w.categoryCustom.hasOwnProperty(x.name)){w.categoryCustom[x.name]="NA"
}else{if(w.categoryCustom.hasOwnProperty(x.name)){if(w.categoryCustom[x.name]===null||w.categoryCustom[x.name]===""||w.categoryCustom[x.name]===undefined){w.categoryCustom[x.name]="NA"
}}}});
w.categoryCustom.$$parcelId$$=w.value;
w.categoryCustom.screenRuleDetailsWithDbFieldName=w.screenRuleDetailsWithDbFieldName;
l.searchedParcelDataFromDbForUiGrid.push(w.categoryCustom)
});
l.associatedParcelGridOptions={};
l.associatedParcelGridOptions.enableFiltering=true;
l.associatedParcelGridOptions.multiSelect=true;
l.associatedParcelGridOptions.enableRowSelection=true;
l.associatedParcelGridOptions.enableSelectAll=true;
l.associatedParcelGridOptions.data=l.searchedParcelDataFromDbForUiGrid;
l.associatedParcelGridOptions.columnDefs=l.associatedParcelLabelListForUiGrid;
l.associatedParcelGridOptions.onRegisterApi=function(w){l.associatedParcelGridApi=w;
w.selection.on.rowSelectionChanged(l,function(x){l.flag.parcelRowSelectedflag=true;
l.selectedAssociatedParcels=[];
angular.forEach(w.selection.getSelectedRows(),function(y){l.selectedAssociatedParcels.push(y.$$parcelId$$)
})
});
w.selection.on.rowSelectionChangedBatch(l,function(x){l.flag.parcelRowSelectedflag=true;
l.selectedAssociatedParcels=[];
angular.forEach(w.selection.getSelectedRows(),function(y){l.selectedAssociatedParcels.push(y.$$parcelId$$)
})
})
};
window.setTimeout(function(){$(window).resize();
$(window).resize()
},100);
l.associatedParcelListFilled=true;
h.unMaskLoading()
};
k.convertorForCustomField(t,u,false)
}h.unMaskLoading()
},function(){var u="Could not save, please try again.";
var t=h.error;
h.addMessage(u,t);
h.unMaskLoading()
})
}},function(p){},function(p){})
},function(){h.unMaskLoading();
var o="Failed to retrieve data";
var n=h.error;
h.addMessage(o,n)
})
};
l.cancelLinkingRoughPurchase=function(){l.retrieveAssociatedRoughParcel();
l.retrieveAssociatedRoughPurchase()
};
l.backToMainPage=function(){l.invoiceAddEditFlag=true
};
l.linkPurchase=function(){if(l.associatedParcelGridApi.selection.getSelectedRows().length>0&&l.associatedPurchaseGridApi.selection.getSelectedRows().length>0){l.selectedParcels=angular.copy(l.associatedParcelGridApi.selection.getSelectedRows());
l.selectedPurchases=angular.copy(l.associatedPurchaseGridApi.selection.getSelectedRows());
l.associatedParcelIds=[];
l.associatedPurchaseIds=[];
angular.forEach(l.selectedParcels,function(n){l.associatedParcelIds.push(n["$$parcelId$$"])
});
angular.forEach(l.selectedPurchases,function(n){l.associatedPurchaseIds.push(n["$$purchaseId$$"])
});
l.sendData={parcelIds:l.associatedParcelIds,purchaseIds:l.associatedPurchaseIds};
i.linkRoughParcelWithPurchase(l.sendData,function(n){l.retrieveAssociatedRoughParcel();
l.retrieveAssociatedRoughPurchase()
})
}}
}])
});