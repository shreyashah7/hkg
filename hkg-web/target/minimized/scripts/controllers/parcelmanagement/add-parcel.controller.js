define(["hkg","parcelService","customFieldService","invoiceService","parcelmanagement/edit-parcel.controller","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicForm","accordionCollapse"],function(a,b,d,c){a.register.controller("AddParcelController",["$rootScope","$scope","ParcelService","$timeout","$filter","$location","$window","DynamicFormService","CustomFieldService","InvoiceService",function(l,p,i,f,j,h,e,o,n,m){l.mainMenu="stockLink";
l.childMenu="addParcel";
l.activateMenu();
p.AddParcel=this;
p.flag={};
p.flag.searchFieldNotAvailable=false;
p.remCaratMsg="";
p.fieldsToClear=["carat_of_parcel$NM$Double","no_of_pieces_of_parcel$NM$Long"];
l.maskLoading();
p.invoiceLabelListForUiGrid=[];
p.searchedDataFromDbForUiGrid=[];
p.parcelDataForUiGrid=[];
p.parcelListForUIGrid=[];
p.parcelDataBean={};
p.lotListTodisplay=[];
p.modifiedListToDisplay=[];
p.searchCustom=o.resetSection(p.generalSearchTemplate);
p.parcelDataBean.featureCustomMap={};
var g=o.retrieveSearchWiseCustomFieldInfo("parcelAdd");
if(p.parcelDbType!=null){}else{p.parcelDbType={}
}var k={};
p.modelAndHeaderList=[];
p.modelAndHeaderListForLot=[];
g.then(function(s){p.generalSearchTemplate=s.genralSection;
if(p.generalSearchTemplate!=null&&p.generalSearchTemplate.length>0){for(var q=0;
q<p.generalSearchTemplate.length;
q++){var r=p.generalSearchTemplate[q];
k[r.model]=r.featureName;
p.modelAndHeaderList.push({model:r.model,header:r.label})
}}else{p.flag.searchFieldNotAvailable=true
}p.generalTemplate=angular.copy(s.genralSection);
p.invoiceLabelList=[];
p.dbFieldNames=[];
angular.forEach(p.generalTemplate,function(t){p.invoiceLabelList.push({model:t.model,label:t.label});
if(t.fromModel){p.dbFieldNames.push(t.fromModel);
p.invoiceLabelListForUiGrid.push({name:t.fromModel,displayName:t.label,minWidth:200})
}else{if(t.toModel){p.dbFieldNames.push(t.toModel);
p.invoiceLabelListForUiGrid.push({name:t.toModel,displayName:t.label,minWidth:200})
}else{if(t.model){p.dbFieldNames.push(t.model);
p.invoiceLabelListForUiGrid.push({name:t.model,displayName:t.label,minWidth:200})
}}}});
p.searchResetFlag=true;
p.dataRetrieved=true;
l.unMaskLoading()
},function(q){},function(q){});
p.initAddParcelForm=function(q){p.addParcelForm=q
};
p.retrieveSearchedData=function(t){p.selectOneParameter=false;
p.gridOptionsForInvoice={};
p.searchedData=[];
p.searchedDataFromDbForUiGrid=[];
p.listFilled=false;
if(Object.getOwnPropertyNames(p.searchCustom).length>0||l.createdInvoiceId){p.searchedDataFromDbForUiGrid=[];
var s=false;
for(var w in p.searchCustom){if(typeof p.searchCustom[w]==="object"&&p.searchCustom[w]!=null){var u=angular.copy(p.searchCustom[w].toString());
if(typeof u==="string"&&u!==null&&u!==undefined&&u.length>0){s=true;
break
}}if(typeof p.searchCustom[w]==="string"&&p.searchCustom[w]!==null&&p.searchCustom[w]!==undefined&&p.searchCustom[w].length>0){s=true;
break
}if(typeof p.searchCustom[w]==="number"&&!!(p.searchCustom[w])){s=true;
break
}if(typeof p.searchCustom[w]==="boolean"){s=true;
break
}}if(s||l.createdInvoiceId){p.gridOptionsForInvoice.enableFiltering=true;
p.gridOptionsForInvoice.data=[];
p.gridOptionsForInvoice.multiSelect=false;
p.gridOptionsForInvoice.enableRowSelection=true;
p.gridOptionsForInvoice.enableSelectAll=false;
p.selectedInvoice=[];
p.gridOptionsForInvoice.onRegisterApi=function(x){p.gridApi=x;
x.selection.on.rowSelectionChanged(p,function(y){if(p.selectedInvoice.length>0){$.each(p.selectedInvoice,function(A,z){if(z["$$hashKey"]===y.entity["$$hashKey"]){p.flag.repeatedRow=true;
p.selectedInvoice.splice(A,1);
return false
}else{p.flag.repeatedRow=false
}});
if(!p.flag.repeatedRow){p.selectedInvoice.push(y.entity)
}}else{p.selectedInvoice.push(y.entity)
}if(p.selectedInvoice.length>0){p.flag.rowSelectedflag=true
}else{p.flag.rowSelectedflag=false
}});
x.selection.on.rowSelectionChangedBatch(p,function(y){if(p.selectedInvoice.length>0){angular.forEach(y,function(z){$.each(p.selectedInvoice,function(B,A){if(A["$$hashKey"]===z.entity["$$hashKey"]){p.flag.repeatedRow=true;
p.selectedInvoice=[];
return false
}else{p.flag.repeatedRow=false
}});
if(!p.flag.repeatedRow){p.selectedInvoice.push(z.entity)
}})
}else{angular.forEach(y,function(z){p.selectedInvoice.push(z.entity)
})
}if(p.selectedInvoice.length>0){p.flag.rowSelectedflag=true
}else{p.flag.rowSelectedflag=false
}})
};
p.searchedDataFromDbForUiGrid=[];
p.parcelDataBean={};
p.parcelDataBean.featureCustomMap={};
angular.forEach(p.searchCustom,function(y,x){if(y!=null){angular.forEach(k,function(A,z){if(x===z){if(!p.parcelDataBean.featureCustomMap[A]){p.parcelDataBean.featureCustomMap[A]=[]
}p.parcelDataBean.featureCustomMap[A].push(x+":"+y)
}else{if(x==="to"+z){if(!p.parcelDataBean.featureCustomMap[A]){p.parcelDataBean.featureCustomMap[A]=[]
}p.parcelDataBean.featureCustomMap[A].push(x+":"+y)
}if(x==="from"+z){if(!p.parcelDataBean.featureCustomMap[A]){p.parcelDataBean.featureCustomMap[A]=[]
}p.parcelDataBean.featureCustomMap[A].push(x+":"+y)
}}})
}});
l.maskLoading();
p.invoiceDataBean={};
var q=o.convertSearchData(p.generalSearchTemplate,null,null,null,p.searchCustom);
p.invoiceDataBean.invoiceCustom=angular.copy(q);
p.invoiceDataBean.dbFieldNames=angular.copy(p.dbFieldNames);
p.invoiceDataBean.searchOnParameter=true;
p.invoiceDataBean.id=null;
if(l.createdInvoiceId){p.invoiceDataBean.searchOnParameter=false;
p.invoiceDataBean.id=angular.copy(l.createdInvoiceId);
l.createdInvoiceId=undefined
}console.log(p.invoiceDataBean.id);
console.log(p.invoiceDataBean.searchOnParameter);
console.log(p.invoiceDataBean.dbFieldNames);
m.search(p.invoiceDataBean,function(x){p.searchedDataFromDb=angular.copy(x);
console.log("res:"+JSON.stringify(x[0]));
var y=function(z){p.searchedData=angular.copy(z);
p.searchedDataFromDbForUiGrid=[];
p.mapToArray=[];
angular.forEach(p.searchedData,function(A){angular.forEach(p.invoiceLabelListForUiGrid,function(B){if(!A.categoryCustom.hasOwnProperty(B.name)){A.categoryCustom[B.name]="NA"
}else{if(A.categoryCustom.hasOwnProperty(B.name)){if(A.categoryCustom[B.name]===null||A.categoryCustom[B.name]===""||A.categoryCustom[B.name]===undefined){A.categoryCustom[B.name]="NA"
}}}A.categoryCustom.value=A.value
});
p.searchedDataFromDbForUiGrid.push(A.categoryCustom)
});
p.gridOptionsForInvoice.data=p.searchedDataFromDbForUiGrid;
p.gridOptionsForInvoice.columnDefs=p.invoiceLabelListForUiGrid;
window.setTimeout(function(){$(window).resize();
$(window).resize()
},100);
p.listFilled=true
};
o.convertorForCustomField(x,y);
l.unMaskLoading()
},function(){var y="Could not retrieve, please try again.";
var x=l.error;
l.addMessage(y,x);
l.unMaskLoading()
})
}else{var v="Please select atleast one search criteria for search";
var r=l.warning;
l.addMessage(v,r)
}}else{var v="Please select atleast one search criteria for search";
var r=l.warning;
l.addMessage(v,r)
}};
if(l.createdInvoiceId!==undefined){f(function(){p.retrieveSearchedData()
},1000)
}p.addParcel=function(){var q=angular.copy(p.selectedInvoice[0]);
p.parcelDataForUiGrid=[];
p.gridOptions={};
p.gridOptions.enableFiltering=true;
p.modelAndHeaderListForLot=[];
p.lotListToSave=[];
p.count=0;
p.caratOfInvoice=undefined;
if(q!=null){p.caratOfInvoice=parseFloat(q.carat_of_invoice$NM$Double);
p.remainingCarat=parseFloat(q.carat_of_invoice$NM$Double);
if(p.remainingCarat<0){p.remCaratMsg="Carat value limit exceeded"
}else{p.remCaratMsg="Carat remaining is "+p.remainingCarat
}if(p.caratOfInvoice!==undefined&&p.caratOfInvoice!==null&&p.caratOfInvoice.toString()!=="NaN"){p.caratDoesNotMatch=false
}else{p.caratDoesNotMatch=true
}console.log(q);
console.log(p.caratOfInvoice);
p.invoiceId=q.value;
p.invoiceIdForConstraint=q.value;
p.flag.showAddPage=true;
l.maskLoading();
p.parcelDataBean={invoiceDataBean:{id:p.invoiceId,invoiceCustom:"",invoiceDbType:""}};
n.retrieveDesignationBasedFields("parcelAdd",function(r){p.response=angular.copy(r);
this.parcelAddShow=false;
p.reset("categoryCustom",false);
var s=o.retrieveSectionWiseCustomFieldInfo("invoice");
p.invoiceDbType={};
p.invoiceDbFieldName=[];
s.then(function(w){p.generaInvoiceTemplate=w.genralSection;
var v=[];
var u=Object.keys(r).map(function(x,y){angular.forEach(this[x],function(z){if(x==="Invoice#P#"){v.push({Invoice:z})
}})
},r);
p.generaInvoiceTemplate=o.retrieveCustomData(p.generaInvoiceTemplate,v);
angular.forEach(p.generaInvoiceTemplate,function(x){if(x.fromModel){if(p.invoiceDbFieldName.indexOf(x.fromModel)===-1){p.invoiceDbFieldName.push(x.fromModel)
}}else{if(x.toModel){if(p.invoiceDbFieldName.indexOf(x.toModel)===-1){p.invoiceDbFieldName.push(x.tiModel)
}}else{if(x.model){if(p.invoiceDbFieldName.indexOf(x.model)===-1){p.invoiceDbFieldName.push(x.model)
}}}}})
},function(u){},function(u){});
p.parcelDbFieldName=[];
p.categoryCustom=o.resetSection(p.templateToSent);
var t=o.retrieveSectionWiseCustomFieldInfo("parcel");
t.then(function(A){p.generalParcelTemplate=A.genralSection;
var w=[];
var v=Object.keys(r).map(function(B,C){angular.forEach(this[B],function(D){if(B==="Parcel"){w.push({Parcel:D})
}})
},r);
p.generalParcelTemplate=o.retrieveCustomData(p.generalParcelTemplate,w);
var u=[];
p.fieldNotConfigured=false;
p.mandatoryFields=["carat_of_parcel$NM$Double","parcelNumber$TF$String","no_of_pieces_of_parcel$NM$Long"];
p.flag.customFieldGenerated=true;
angular.forEach(p.generalParcelTemplate,function(B){if(B.model==="carat_of_parcel$NM$Double"){B.required=true
}else{if(B.model==="parcelNumber$TF$String"){B.required=true
}else{if(B.model==="no_of_pieces_of_parcel$NM$Long"){B.required=true
}}}u.push(angular.copy(B.model));
p.modelAndHeaderListForLot.push({model:B.model,header:B.label});
if(B.fromModel){if(p.parcelDbFieldName.indexOf(B.fromModel)===-1){p.parcelDbFieldName.push(B.fromModel)
}p.parcelListForUIGrid.push({name:B.fromModel,displayName:B.label,minWidth:200})
}else{if(B.toModel){if(p.parcelDbFieldName.indexOf(B.toModel)===-1){p.parcelDbFieldName.push(B.toModel)
}p.parcelListForUIGrid.push({name:B.toModel,displayName:B.label,minWidth:200})
}else{if(B.model){if(p.parcelDbFieldName.indexOf(B.model)===-1){p.parcelDbFieldName.push(B.model)
}p.parcelListForUIGrid.push({name:B.model,displayName:B.label,minWidth:200})
}}}});
if(u.length>0&&p.mandatoryFields!=null&&p.mandatoryFields.length>0){for(var z=0;
z<p.mandatoryFields.length;
z++){if(u.indexOf(p.mandatoryFields[z])===-1){p.fieldNotConfigured=true;
break
}}}else{p.fieldNotConfigured=true
}if(!p.fieldNotConfigured){p.parcelDataBean.parcelDbType=angular.copy(p.parcelDbType);
console.log(p.parcelDbFieldName+"---------"+q.value);
var y={};
var x=[];
x.push(q.value);
y.invoiceObjectId=x;
y.parcelDbFieldName=p.parcelDbFieldName;
y.invoiceDbFieldName=p.invoiceDbFieldName;
i.retrieveParcelByInvoiceId(y,function(C){console.log(JSON.stringify(C));
p.flag.parcelsAlreadyCreated=false;
if(C!==undefined){if(C.length>1){}if(C[0]!==undefined){console.log("flag set from here");
if(C[0].custom1!==null&&C[0].custom1!==undefined){p.invoiceCustom=angular.copy(C[0].custom1)
}else{p.invoiceCustom={}
}}else{p.invoiceCustom={}
}p.lotInDb=angular.copy(C)
}var B=function(F){angular.forEach(F,function(G){if(G.categoryCustom!==undefined&&G.categoryCustom!==null){G.categoryCustom["$$objectId"]=G.value;
angular.forEach(p.parcelListForUIGrid,function(H){if(!G.categoryCustom.hasOwnProperty(H.name)){G.categoryCustom[H.name]="NA"
}else{if(G.categoryCustom.hasOwnProperty(H.name)){if(G.categoryCustom[H.name]===null||G.categoryCustom[H.name]===""||G.categoryCustom[H.name]===undefined){G.categoryCustom[H.name]="NA"
}}}G.objectIdOfParcel={id:G.value}
});
p.parcelDataForUiGrid.push(G.categoryCustom)
}});
p.gridOptions.data=p.parcelDataForUiGrid;
p.gridOptions.columnDefs=p.parcelListForUIGrid;
p.gridOptions.columnDefs.push({name:"Action",cellTemplate:'<div class="ui-grid-cell-contents"> <a ng-click="grid.appScope.updateLot(row.entity)">Update</i></a>',enableFiltering:false,minWidth:200});
p.countRemainingCarat()
};
o.convertorForCustomField(C,B);
window.setTimeout(function(){$(window).resize();
$(window).resize()
},100);
if(p.flag.parcelsAlreadyCreated){var E="Parcel(s) already created for current invoice";
var D=l.error;
l.addMessage(E,D)
}p.listFilled=true
},function(){l.unMaskLoading();
var C="Could not retrieve parcel, please try again.";
var B=l.error;
l.addMessage(C,B)
});
l.unMaskLoading()
}else{l.unMaskLoading()
}},function(u){},function(u){})
},function(){l.unMaskLoading();
var s="Failed to retrieve data";
var r=l.error;
l.addMessage(s,r)
})
}};
p.reset=function(s,q){if(s==="searchCustom"){p.searchCustom={};
var r=o.retrieveSearchWiseCustomFieldInfo("parcelAdd");
p.dbType={};
r.then(function(t){p.generalSearchTemplate=t.genralSection;
p.searchResetFlag=true;
l.unMaskLoading();
p.flag.customFieldGenerated=true
},function(t){},function(t){})
}else{if(s==="categoryCustom"){if(!q){p.categoryCustom={}
}else{angular.forEach(p.fieldsToClear,function(t){delete p.categoryCustom[t]
})
}var r=o.retrieveSectionWiseCustomFieldInfo("parcel");
r.then(function(v){var u=[];
var t=Object.keys(p.response).map(function(w,x){angular.forEach(this[w],function(y){if(w!=="Parcel#P#"){u.push({Parcel:y})
}})
},p.response);
p.generalParcelTemplate=v.genralSection;
p.generalParcelTemplate=o.retrieveCustomData(p.generalParcelTemplate,u);
f(function(){console.log("categoryCustom::::::::;"+JSON.stringify(p.categoryCustom));
p.AddParcel=this;
this.parcelAddShow=true
},100);
l.unMaskLoading();
p.flag.customFieldGenerated=true;
p.parcelDataBean.parcelDbType=p.parcelDbType
},function(t){},function(t){})
}}};
p.resetAddForm=function(q){if(p.addParcelForm!=null){p.addParcelForm.$dirty=false
}p.AddParcel=this;
this.parcelAddShow=false;
p.flag.editLot=false;
p.reset("categoryCustom",q)
};
p.onCanelOfSearch=function(q){if(p.addParcelForm!=null){p.addParcelForm.$dirty=false
}p.searchedData=[];
p.searchResetFlag=false;
p.listFilled=false;
p.selectedInvoice=[];
p.gridOptionsForInvoice={};
p.reset("searchCustom")
};
p.createParcel=function(s){p.parcelDbTypeToSent=angular.copy(p.parcelDbType);
p.submitted=true;
if(s.$valid){p.submitted=false;
var u=[];
u.push({categoryCustom:angular.copy(p.categoryCustom)});
p.count++;
p.lotListToSave.push(angular.copy({$$objectId:p.count,categoryCustom:p.categoryCustom}));
p.caratDoesNotMatch=false;
var r=[];
var q={};
var t=undefined;
angular.forEach(p.lotListToSave,function(v){if(v.category!==null&&v.category!==undefined){}else{if(!p.caratDoesNotMatch&&v.categoryCustom["carat_of_parcel$NM$Double"]!==undefined&&v.categoryCustom["carat_of_parcel$NM$Double"]!==null){if(t!==undefined){t=t+parseFloat(v.categoryCustom["carat_of_parcel$NM$Double"])
}else{t=parseFloat(v.categoryCustom["carat_of_parcel$NM$Double"])
}}else{p.caratDoesNotMatch=true
}r.push({parcelCustom:v.categoryCustom,invoiceDataBean:{id:p.invoiceId},parcelDbType:p.parcelDbTypeToSent})
}});
if(!p.caratDoesNotMatch){if(p.caratOfInvoice!==t){p.caratDoesNotMatch=true
}}q.parcelList=r;
q.invoiceDataBean={id:p.invoiceId};
l.maskLoading();
l.createdParcelIds=undefined;
p.createdParcelIds=undefined;
i.create(q,function(v){console.log(JSON.stringify(v));
p.createdParcelIds=angular.copy(v.primaryKey);
console.log(JSON.stringify(p.createdParcelIds));
l.showLotLink=true;
p.lotListToSave=[];
p.listFilled=false;
p.addParcelForm.$dirty=false;
p.AddParcel=this;
this.parcelAddShow=true;
f(function(){l.showLotLink=false
},30000);
p.listFilled=true;
var w=function(x){x[0].categoryCustom.value=p.createdParcelIds.substring(1,p.createdParcelIds.length-1);
x[0].categoryCustom.label=angular.copy(p.invoiceIdForConstraint);
angular.forEach(p.parcelListForUIGrid,function(y){if(!x[0].categoryCustom.hasOwnProperty(y.name)){x[0].categoryCustom[y.name]="NA"
}else{if(x[0].categoryCustom.hasOwnProperty(y.name)){if(x[0].categoryCustom[y.name]===null||x[0].categoryCustom[y.name]===""||x[0].categoryCustom[y.name]===undefined||x[0].categoryCustom[y.name]==="undefined"){x[0].categoryCustom[y.name]="NA"
}}}});
p.gridOptions.data.push(angular.copy(x[0].categoryCustom));
p.gridOptions.columnDefs.push({name:"Action",cellTemplate:'<div class="ui-grid-cell-contents"> <a ng-click="grid.appScope.updateLot(row.entity)">Update</i></a>',enableFiltering:false,minWidth:200});
p.countRemainingCarat()
};
o.convertorForCustomField(u,w);
p.resetAddForm(true);
l.unMaskLoading()
},function(){l.unMaskLoading();
var w="Could not create parcel, please try again.";
var v=l.error;
l.addMessage(w,v)
})
}};
p.editParcelLocally=function(s,q){p.index=s.$$objectId;
p.oldObj=angular.copy(s,p.oldObj);
if(s!=null){p.AddParcel=this;
this.parcelAddShow=false;
p.flag.editLot=true;
p.lotDataBean={};
if(!!(p.lotListToSave&&p.lotListToSave.length>0)){for(var r=0;
r<p.lotListToSave.length;
r++){if(s.$$objectId!=null){if(p.lotListToSave[r].$$objectId===s.$$objectId){p.categoryCustom=angular.copy(p.lotListToSave[r].categoryCustom);
p.lotDataBean.lotCustom=p.categoryCustom;
break
}}else{if(p.lotListToSave[r].id===s.id){p.categoryCustom=angular.copy(p.lotListToSave[r].categoryCustom);
p.lotDataBean.lotCustom=p.categoryCustom;
break
}}}}p.lotDataBean.lotDbType=p.lotDbType
}f(function(){p.AddParcel=this;
this.parcelAddShow=true
},100)
};
p.saveLot=function(q){p.submitted=true;
if(q.$valid){var r=[];
r.push({categoryCustom:angular.copy(p.categoryCustom)});
angular.forEach(p.lotListToSave,function(t){if(t.$$objectId===p.index){t.categoryCustom=(p.categoryCustom)
}});
var s=function(t){var w=angular.copy(t[0].categoryCustom);
if(w!==undefined&&w!==null){angular.forEach(p.parcelListForUIGrid,function(x){if(!w.hasOwnProperty(x.name)){w[x.name]="NA"
}else{if(w.hasOwnProperty(x.name)){console.log(w[x.name]);
if(w[x.name]===null||w[x.name]===""||w[x.name]===undefined||w[x.name]==="undefined"){w[x.name]="NA"
}}}})
}for(var u=0;
u<p.gridOptions.data.length;
u++){var v=p.gridOptions.data[u];
if(v.$$objectId===p.index){p.gridOptions.data[u]=angular.copy(w);
p.gridOptions.data[u].$$objectId=p.index;
break
}}};
o.convertorForCustomField(r,s);
p.categoryCustom=o.resetSection(p.generalParcelTemplate);
p.listFilled=true;
p.submitted=false;
p.flag.editLot=false;
p.resetAddForm(false)
}};
p.showPopUp=function(q){p.lotObjectToDelete=q;
p.index=q.$$objectId;
$("#deleteDialog").modal("show")
};
p.hidePopUp=function(){$("#deleteDialog").modal("hide");
l.removeModalOpenCssAfterModalHide()
};
p.deleteLot=function(){for(var q=0;
q<p.gridOptions.data.length;
q++){var r=p.gridOptions.data[q];
if(r.$$objectId===p.index){p.gridOptions.data.splice(q,1);
break
}}for(var q=0;
q<p.lotListToSave.length;
q++){var r=p.lotListToSave[q];
if(r.$$objectId===p.index){p.lotListToSave.splice(q,1);
break
}}p.categoryCustom=o.resetSection(p.generalLotTemplate);
p.flag.editLot=false;
$("#deleteDialog").modal("hide");
l.removeModalOpenCssAfterModalHide()
};
p.onCancel=function(q){if(q!=null){p.flag.showAddPage=false;
p.submitted=false;
p.selectedInvoice=[];
p.listFilled=true;
p.flag.parcelsAlreadyCreated=false;
p.reset("categoryCustom",false)
}};
p.saveAllLot=function(t){p.submitted=false;
p.caratDoesNotMatch=false;
var r=[];
var q={};
var u=undefined;
angular.forEach(p.lotListToSave,function(w){if(w.category!==null&&w.category!==undefined){}else{if(!p.caratDoesNotMatch&&w.categoryCustom["carat_of_parcel$NM$Double"]!==undefined&&w.categoryCustom["carat_of_parcel$NM$Double"]!==null){if(u!==undefined){u=u+parseFloat(w.categoryCustom["carat_of_parcel$NM$Double"])
}else{u=parseFloat(w.categoryCustom["carat_of_parcel$NM$Double"])
}}else{p.caratDoesNotMatch=true
}r.push({parcelCustom:w.categoryCustom,invoiceDataBean:{id:p.invoiceId},parcelDbType:p.parcelDbTypeToSent})
}});
if(!p.caratDoesNotMatch){if(p.caratOfInvoice!==u){p.caratDoesNotMatch=true
}}q.parcelList=r;
q.invoiceDataBean={id:p.invoiceId};
if(!p.caratDoesNotMatch){l.maskLoading();
l.createdParcelIds=undefined;
p.createdParcelIds=undefined;
i.create(q,function(w){console.log(JSON.stringify(w));
p.createdParcelIds=angular.copy(w.primaryKey);
l.showLotLink=true;
p.lotListToSave=[];
p.lotListTodisplay=[];
p.modifiedListToDisplay=[];
p.searchedData=[];
p.searchedDataFromDb=[];
p.listFilled=false;
p.addParcelForm.$dirty=false;
p.flag.showAddPage=false;
p.onCancel();
p.onCanelOfSearch();
p.AddParcel=this;
this.parcelAddShow=true;
f(function(){l.showLotLink=false
},30000);
l.unMaskLoading()
},function(){l.unMaskLoading();
var x="Could not create parcel, please try again.";
var w=l.error;
l.addMessage(x,w)
})
}else{var v="Carat value does not match, please try again.";
var s=l.error;
l.addMessage(v,s)
}};
p.updateLot=function(q){if((!!(q))){for(var r=0;
r<p.lotInDb.length;
r++){if(p.createdParcelIds===null&&p.lotInDb[r]!==undefined&&p.lotInDb[r]!==null&&q.parcelDbObjectId===p.lotInDb[r].value){q=angular.copy(p.lotInDb[r]);
console.log("JSON.stringify(searchDataObj)");
break
}}console.log(JSON.stringify(q));
h.path("/editparcel");
l.unMaskLoading();
p.flag.showUpdatePage=true;
p.addParcelForm.$dirty=false;
l.parcelByInvoice=angular.copy(q);
l.editParcel=true
}};
p.addLotDirectly=function(){if(p.createdParcelIds!==undefined){l.maskLoading();
l.createdParcelIds=angular.copy(p.createdParcelIds);
p.addParcelForm.$dirty=false;
h.path("/addlot");
l.showLotLink=false
}};
p.countRemainingCarat=function(){if(p.gridOptions.data!==undefined&&p.gridOptions.data!==null){var q=0;
angular.forEach(p.gridOptions.data,function(r){if(r["carat_of_parcel$NM$Double"]!==undefined&&r["carat_of_parcel$NM$Double"]!==null){q+=parseFloat(r["carat_of_parcel$NM$Double"])
}});
p.remainingCarat=parseFloat(p.caratOfInvoice)-parseFloat(q);
if(p.remainingCarat<0){p.remCaratMsg="Carat value limit exceeded"
}else{p.remCaratMsg="Carat remaining is "+p.remainingCarat
}}}
}])
});