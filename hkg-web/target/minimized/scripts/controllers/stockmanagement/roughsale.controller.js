define(["hkg","roughsaleService","customFieldService","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicForm","accordionCollapse","ParcelTemplateController","ParcelTemplateService"],function(b,a){b.register.controller("RoughSaleController",["$rootScope","$scope","RoughSaleService","DynamicFormService","CustomFieldService","$timeout","ParcelTemplateService","$route",function(i,l,f,k,j,d,e,h){i.mainMenu="stockLink";
i.childMenu="roughSale";
i.activateMenu();
var g={};
var c={};
l.selectedSale=[];
l.retrieveSearchField=function(){l.searchParcelTemplate=[];
l.searchInvoiceTemplate=[];
l.search={};
l.generalSearchTemplate=[];
l.searchCustom={};
var n=k.retrieveSearchWiseCustomFieldInfo("saleRough");
n.then(function(p){var o=p.genralSection;
angular.forEach(o,function(q){if(q.featureName==="invoice"||q.featureName==="parcel"||q.featureName==="sell"){l.generalSearchTemplate.push(angular.copy(q))
}});
l.searchDirective=true;
angular.forEach(l.generalSearchTemplate,function(q){g[q.model]=q.featureName;
if(q.featureName.toLowerCase()==="invoice"){l.searchInvoiceTemplate.push(angular.copy(q));
invoiceDbFieldName.push(angular.copy(q.model))
}else{if(q.featureName.toLowerCase()==="parcel"){parcelDbFieldName.push(angular.copy(q.model));
l.searchParcelTemplate.push(angular.copy(q))
}}});
if(invoiceDbFieldName.length>0){c.invoice=invoiceDbFieldName
}if(parcelDbFieldName.length>0){c.parcel=parcelDbFieldName
}},function(o){},function(o){});
l.dbType={};
l.datePicker={};
l.open=function(o,p){o.preventDefault();
o.stopPropagation();
l.datePicker[p]=true
};
l.dateOptions={"year-format":"'yy'","starting-day":1};
var m=new Date();
m.setSeconds(0);
m.setMilliseconds(0);
m.setHours(0);
m.setMinutes(0);
l.search.toDate=new Date(m);
l.search.fromDate=new Date(m.setMonth(m.getMonth()-1));
l.format=i.dateFormat
};
l.sellScreenRule=function(o,n){var m;
if(!!o.entity.screenRuleDetailsWithDbFieldName&&o.entity.screenRuleDetailsWithDbFieldName[n]!==undefined&&o.entity.screenRuleDetailsWithDbFieldName[n]!==null){if(l.selectedSale.length===0||(l.selectedSale[0].$$sellId$$!==o.entity.$$sellId$$)){m=o.entity.screenRuleDetailsWithDbFieldName[n].colorCode
}}return m
};
l.initializeData=function(){i.maskLoading();
l.searchInvoiceTemplate=[];
l.searchParcelTemplate=[];
l.searchSellTemplate=[];
var m=[];
var o=[];
var n=[];
l.flag={};
l.sellDbType={};
if(l.roughSaleForm!=null){l.roughSaleForm.$dirty=false
}l.flag.template=false;
l.flag.rowSelectedflag=false;
l.flag.template=false;
l.flag.loadDynamicForm=false;
l.searchCustom={};
l.roughSale=this;
this.sellFieldAddShow=false;
l.labelListForUiGrid=[];
e.setFeatureName("saleRough");
l.parameters=["saleRough","GEN"];
j.retrieveDesignationBasedFieldsBySection(l.parameters,function(p){l.response=angular.copy(p);
l.categoryCustom={};
var q=k.retrieveSearchWiseCustomFieldInfo("saleRough");
l.generalSearchTemplate=[];
q.then(function(s){var r=s.genralSection;
angular.forEach(r,function(u){if(u.featureName==="invoice"||u.featureName==="parcel"||u.featureName==="sell"){l.generalSearchTemplate.push(angular.copy(u))
}});
l.searchDirective=true;
l.sellDbFieldName=[];
l.roughSale=this;
l.labelListForUiGrid.push({name:"totalPieces",displayName:"Total Pieces",minWidth:200});
l.labelListForUiGrid.push({name:"totalCarat",displayName:"Total Carat",minWidth:200});
l.labelListForUiGrid.push({name:"totalAmountInDollar",displayName:"Total Amount($)",minWidth:200});
l.labelListForUiGrid.push({name:"totalAmountInRs",displayName:"Total Amount(INR)",minWidth:200});
l.fieldIdNameMap={};
angular.forEach(l.generalSearchTemplate,function(u){if(u.fromModel){l.fieldIdNameMap[u.fieldId]=u.fromModel;
if(l.sellDbFieldName.indexOf(u.fromModel)===-1){l.sellDbFieldName.push(u.fromModel)
}if(u.featureName==="sell"){l.labelListForUiGrid.push({name:u.fromModel,displayName:u.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.sellScreenRule(row, '"+u.fromModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'})
}}else{if(u.toModel){l.fieldIdNameMap[u.fieldId]=u.toModel;
if(l.sellDbFieldName.indexOf(u.toModel)===-1){l.sellDbFieldName.push(u.toModel)
}if(u.featureName==="sell"){l.labelListForUiGrid.push({name:u.toModel,displayName:u.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.sellScreenRule(row, '"+u.toModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'})
}}else{if(u.model){l.fieldIdNameMap[u.fieldId]=u.model;
g[u.model]=u.featureName;
if(u.featureName.toLowerCase()==="invoice"){l.searchInvoiceTemplate.push(angular.copy(u));
m.push(angular.copy(u.model))
}else{if(u.featureName.toLowerCase()==="parcel"){o.push(angular.copy(u.model));
l.searchParcelTemplate.push(angular.copy(u))
}else{if(u.featureName.toLowerCase()==="sell"){n.push(angular.copy(u.model));
l.searchSellTemplate.push(angular.copy(u))
}}}if(l.sellDbFieldName.indexOf(u.model)===-1){l.sellDbFieldName.push(u.model)
}if(u.featureName==="sell"){l.labelListForUiGrid.push({name:u.model,displayName:u.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.sellScreenRule(row, '"+u.model+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'})
}}}}});
if(m.length>0){c.invoice=m
}if(o.length>0){c.parcel=o
}if(n.length>0){c.sell=n
}var t={};
l.map={};
l.map.dbFieldNames=l.sellDbFieldName;
t.uiFieldMap=l.map;
t.ruleConfigMap={fieldIdNameMap:l.fieldIdNameMap,featureName:"roughSale"};
l.listFilled=false;
l.gridOptions={};
l.searchedData=[];
l.searchedDataFromDbForUiGrid=[];
f.retrieveAllSellDocument(t,function(u){var v=k.retrieveSectionWiseCustomFieldInfo("lot");
v.then(function(w){l.lotCustom=k.resetSection(l.generalSearchTemplate);
l.lotTemplate=w.genralSection;
l.searchedDataFromDb=angular.copy(u);
var x=function(y){l.searchedData=angular.copy(y);
angular.forEach(l.searchedData,function(A){angular.forEach(l.labelListForUiGrid,function(B){if(!A.categoryCustom.hasOwnProperty(B.name)){A.categoryCustom[B.name]="NA"
}else{if(A.categoryCustom.hasOwnProperty(B.name)){if(A.categoryCustom[B.name]===null||A.categoryCustom[B.name]===""||A.categoryCustom[B.name]===undefined){A.categoryCustom[B.name]="NA"
}}}});
A.categoryCustom.$$sellId$$=A.value;
A.categoryCustom.screenRuleDetailsWithDbFieldName=A.screenRuleDetailsWithDbFieldName;
l.searchedDataFromDbForUiGrid.push(A.categoryCustom)
});
function z(){return'<div ng-dblclick="grid.appScope.rowDblClick(row)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }"  ui-grid-cell></div>'
}l.rowDblClick=function(A){l.showParcelTemplate("callMethod",A)
};
l.gridOptions={};
l.gridOptions.enableFiltering=true;
l.gridOptions.data=l.searchedDataFromDbForUiGrid;
l.gridOptions.columnDefs=l.labelListForUiGrid;
l.gridOptions.multiSelect=false;
l.gridOptions.enableRowSelection=true;
l.gridOptions.enableSelectAll=false;
l.gridOptions.rowTemplate=z();
l.gridOptions.onRegisterApi=function(A){l.gridApi=A;
A.selection.on.rowSelectionChanged(l,function(B){if(l.gridApi.selection.getSelectedRows().length>0){l.selectedSale=l.gridApi.selection.getSelectedRows();
l.flag.rowSelectedflag=true
}else{l.selectedSale=[];
l.flag.rowSelectedflag=false
}});
A.selection.on.rowSelectionChangedBatch(l,function(B){if(l.gridApi.selection.getSelectedRows().length>0){l.selectedSale=l.gridApi.selection.getSelectedRows();
l.flag.rowSelectedflag=true
}else{l.selectedSale=[];
l.flag.rowSelectedflag=false
}})
};
window.setTimeout(function(){$(window).resize();
$(window).resize()
},100);
l.listFilled=true;
l.dataRetrieved=true;
i.unMaskLoading()
};
k.convertorForCustomField(u,x)
},function(w){},function(w){})
},function(){var v="Could not retrieve, please try again.";
var u=i.error;
i.addMessage(v,u);
i.unMaskLoading()
})
},function(r){},function(r){})
},function(){i.unMaskLoading();
var q="Could not retrieve, please try again.";
var p=i.error;
i.addMessage(q,p)
})
};
l.initroughSaleForm=function(m){l.roughSaleForm=m
};
l.initializeData();
l.retrieveSearchedData=function(){l.selectOneParameter=false;
if(Object.getOwnPropertyNames(l.searchCustom).length>0){var p=false;
for(var s in l.searchCustom){if(typeof l.searchCustom[s]==="object"&&l.searchCustom[s]!=null){var q=angular.copy(l.searchCustom[s].toString());
if(typeof q==="string"&&q!==null&&q!==undefined&&q.length>0){p=true;
break
}}if(typeof l.searchCustom[s]==="string"&&l.searchCustom[s]!==null&&l.searchCustom[s]!==undefined&&l.searchCustom[s].length>0){p=true;
break
}if(typeof l.searchCustom[s]==="number"&&!!(l.searchCustom[s])){p=true;
break
}if(typeof l.searchCustom[s]==="boolean"){p=true;
break
}}if(p){l.gridOptionsForInvoice={};
l.searchedParcel=[];
l.searchedParcelForUiGrid=[];
l.listFilled=false;
l.parcelDataBean={};
l.parcelDataBean.featureCustomMapValue={};
i.maskLoading();
var o={};
var m=k.convertSearchData(l.searchInvoiceTemplate,l.searchParcelTemplate,l.searchSellTemplate,null,l.searchCustom);
angular.forEach(g,function(x,v){var w=m[v];
if(w!==undefined){var u={};
if(!o[x]){u[v]=w;
o[x]=u
}else{var t=o[x];
t[v]=w;
o[x]=t
}}else{w=l.searchCustom["to"+v];
if(w!==undefined){var u={};
if(!o[x]){u["to"+v]=w;
o[x]=u
}else{var t=o[x];
t["to"+v]=w;
o[x]=t
}}w=l.searchCustom["from"+v];
if(w!==undefined){var u={};
if(!o[x]){u["from"+v]=w;
o[x]=u
}else{var t=o[x];
t["from"+v]=w;
o[x]=t
}}}});
l.parcelDataBean.featureCustomMapValue=o;
l.parcelDataBean.parcelCustom=angular.copy(m);
l.parcelDataBean.featureDbFieldMap=c;
l.parcelDataBean.ruleConfigMap={fieldIdNameMap:l.fieldIdNameMap,featureName:"roughSale"};
f.search(l.parcelDataBean,function(t){var u=function(v){l.searchedParcel=angular.copy(v);
l.mapToArray=[];
angular.forEach(l.searchedParcel,function(w){angular.forEach(l.labelListForUiGrid,function(x){if(!w.categoryCustom.hasOwnProperty(x.name)){w.categoryCustom[x.name]="NA"
}else{if(w.categoryCustom.hasOwnProperty(x.name)){if(w.categoryCustom[x.name]===null||w.categoryCustom[x.name]===""||w.categoryCustom[x.name]===undefined){w.categoryCustom[x.name]="NA"
}}}w.categoryCustom.value=w.value
});
w.categoryCustom.$$sellId$$=w.value;
w.categoryCustom.screenRuleDetailsWithDbFieldName=w.screenRuleDetailsWithDbFieldName;
l.searchedParcelForUiGrid.push(w.categoryCustom)
});
l.gridOptions.data=l.searchedParcelForUiGrid;
window.setTimeout(function(){$(window).resize();
$(window).resize()
},100);
l.listFilled=true
};
k.convertorForCustomField(t,u);
i.unMaskLoading()
},function(){var u="Could not retrieve, please try again.";
var t=i.error;
i.addMessage(u,t);
i.unMaskLoading()
})
}else{var r="Please select atleast one search criteria for search";
var n=i.warning;
i.addMessage(r,n)
}}else{var r="Please select atleast one search criteria for search";
var n=i.warning;
i.addMessage(r,n)
}};
l.showParcelTemplate=function(o,n){i.maskLoading();
l.categoryCustom={};
e.setSelectedParcels(null);
l.flag.template=true;
l.generalSellTemplate=[];
l.roughSale=this;
this.sellFieldAddShow=true;
l.sellDataBean={};
var m=k.retrieveSectionWiseCustomFieldInfo("sell");
m.then(function(t){l.generalSellTemplate=t.genralSection;
var u=[];
var p=Object.keys(l.response).map(function(w,x){angular.forEach(this[w],function(y){if(w==="Sell"){u.push({Sell:y})
}})
},l.response);
l.generalSellTemplate=k.retrieveCustomData(l.generalSellTemplate,u);
var q=[];
l.sellTemplateFields=[];
l.fieldIdNameMap={};
angular.forEach(l.generalSellTemplate,function(w){if(w.model==="sell_to$DRP$Long"){w.required=true
}l.fieldIdNameMap[w.fieldId]=w.model;
l.sellTemplateFields.push(w.model);
q.push(w.model)
});
l.fieldNotConfigured=false;
if(q.length>0){if(q.indexOf("sell_to$DRP$Long")===-1){l.fieldNotConfigured=true
}}else{l.fieldNotConfigured=true
}if(o==="callMethod"&&l.map!==undefined&&l.map!==null){var s=n.entity;
var r=[];
r.push(s.sellObjectId);
l.sellDataBean.id=s.sellObjectId;
l.map.sellObjectId=r;
l.map.dbFieldNames=l.sellTemplateFields;
var v={};
v.uiFieldMap=l.map;
v.ruleConfigMap={fieldIdNameMap:l.fieldIdNameMap,featureName:"roughSale"};
f.retrieveselldocumentforparcelbyid(v,function(w){e.setSelectedParcels(w.custom6);
l.categoryCustom=w.categoryCustom;
l.screenRules=w.screenRuleDetailsWithDbFieldName;
l.flag.loadDynamicForm=true;
i.unMaskLoading()
},function(){l.flag.loadDynamicForm=true;
var x="Could not retrieve parcel ids, please try again.";
var w=i.error;
i.addMessage(x,w);
i.unMaskLoading()
})
}else{l.flag.loadDynamicForm=true
}l.roughSale=this;
this.sellFieldAddShow=true;
i.unMaskLoading()
},function(p){},function(p){})
};
l.sellParceool=function(o){l.submitted=true;
if(o!==null&&o.$valid){if(Object.getOwnPropertyNames(l.categoryCustom).length>0){l.submitted=false;
var n=e.getSelectedParcelData();
l.sellDataBean.sellCustom=l.categoryCustom;
l.sellDataBean.sellDbType=l.sellDbType;
l.sellDataBean.totalPieces=n.footerData.changedPieces;
l.sellDataBean.totalCarat=n.footerData.changedCarat;
l.sellDataBean.totalAmountInDollar=n.footerData.changedAmountInDollar;
l.sellDataBean.totalAmountInRs=n.footerData.changedAmountInRs;
l.sellDataBean.roughStockDetailDataBeans=[];
l.sellDataBean.roughStockDetailDataBeans=n.roughStock;
l.sellDataBean.parcels=[];
angular.forEach(n.roughStock,function(q){l.sellDataBean.parcels.push(q.parcel)
});
f.saleParcels(l.sellDataBean,function(q){l.searchDirective=false;
l.initializeData()
},function(){i.unMaskLoading();
var r="Could not sell parcel, please try again.";
var q=i.error;
i.addMessage(r,q)
})
}else{var p="Please enter atleast one value";
var m=i.warning;
i.addMessage(p,m)
}}};
l.resetAddForm=function(n){if(l.roughSaleForm!=null){l.roughSaleForm.$dirty=false
}l.flag.template=false;
l.flag.rowSelectedflag=false;
l.roughSale=this;
this.sellFieldAddShow=false;
l.categoryCustom={};
l.flag.loadDynamicForm=false;
var m=k.retrieveSectionWiseCustomFieldInfo("sell");
m.then(function(p){var q=[];
var o=Object.keys(l.response).map(function(r,s){angular.forEach(this[r],function(t){if(r==="Sell"){q.push({Invoice:t})
}})
},l.response);
l.generalSellTemplate=p.genralSection;
l.generalSellTemplate=k.retrieveCustomData(l.generalSellTemplate,q);
i.unMaskLoading()
},function(o){},function(o){})
};
l.onCancelOfSearch=function(m){h.reload()
};
l.isParcelSelected=function(){var m=e.getSelectedParcelData();
if(m!==undefined&&m.roughStock!==undefined&&m.roughStock.length>0&&m.footerData!==undefined){return false
}return true
}
}])
});