define(["hkg","sublotService","parcelService","customFieldService","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicFormGrid","editableCustomGrid","accordionCollapse","ParcelTemplateService"],function(a){a.register.controller("SublotController",["$rootScope","$scope","SublotService","ParcelService","DynamicFormService","CustomFieldService","$q","ParcelTemplateService",function(h,n,d,r,k,o,t,m){h.mainMenu="stockLink";
h.childMenu="SubLot";
n.entity="SUBLOT.";
n.sublotDataBean={};
n.sublotDataBean.parcelDataBean={};
var p="";
n.search={};
n.valueCustom={};
n.isParcelSelected=false;
n.searchedDataFromDbForUiGrid=[];
n.selectedParcel=[];
m.setFeatureName("lotSub");
h.activateMenu();
n.searchCustom=k.resetSection(n.generalSublotTemplate);
var e=k.retrieveSearchWiseCustomFieldInfo("lotSub");
n.caratValueSublotTemplate=[];
var q=[];
n.dbType={};
n.datePicker={};
n.showConfigMsg=true;
n.showSublotConfigMsg=true;
n.isParcelSelected=false;
n.open=function(u,v){u.preventDefault();
u.stopPropagation();
n.datePicker[v]=true
};
var f=["shape","quality_from","quality_to","color_from","color_to","cut","carat"];
n.dateOptions={"year-format":"'yy'","starting-day":1};
var g=new Date();
g.setSeconds(0);
g.setMilliseconds(0);
g.setHours(0);
g.setMinutes(0);
n.search.toDate=new Date(g);
n.search.fromDate=new Date(g.setMonth(g.getMonth()-1));
n.format=h.dateFormat;
n.initSublotForm=function(u){n.sublotForm=u
};
n.$watch("valueCustom",function(){if(n.editableGridSublotOptions.getRowIndexInEditMode&&n.editableGridSublotOptions.getRowIndexInEditMode()>=0){s(n.editableGridSublotOptions.datarows[n.editableGridSublotOptions.getRowIndexInEditMode()],n.valueCustom)
}},true);
var s=function(v,u){angular.forEach(u,function(x,w){v.categoryCustom[w]=x
})
};
n.createOrUpdateSubLot=function(u,v,w){n.sublotDataBean.subLotCustom=u.categoryCustom;
n.sublotDataBean.id=u.value;
n.sublotDataBean.subLotDbType=v;
n.sublotDataBean.isArchive=false;
n.sublotDataBean.uiFieldMap=n.map;
n.sublotDataBean.ruleConfigMap={fieldIdNameMap:n.fieldIdNameMap,featureName:"subLot"};
n.sublotDataBean.parcelDataBean={};
n.sublotDataBean.parcelDataBean.id=p;
h.maskLoading();
if(n.sublotDataBean.id==undefined||n.sublotDataBean.id==null){d.createSublot(n.sublotDataBean,function(y){var x=t.defer();
var z=x.promise;
z.then(function(){console.log(JSON.stringify(y));
w(y)
});
x.resolve();
h.unMaskLoading()
},function(){h.unMaskLoading();
var y="Could not create sublot, please try again.";
var x=h.error;
h.addMessage(y,x)
})
}else{d.updateSublot(n.sublotDataBean,function(y){var x=t.defer();
var z=x.promise;
z.then(function(){w(y)
});
x.resolve();
h.unMaskLoading()
},function(){h.unMaskLoading();
var y="Could not update sublot, please try again.";
var x=h.error;
h.addMessage(y,x)
})
}};
n.deleteSublot=function(u){d.deleteSublot(u.value,function(v){n.retrieveSublots();
h.unMaskLoading()
},function(){h.unMaskLoading();
var w="Could not delete Sublot, please try again.";
var v=h.error;
h.addMessage(w,v)
})
};
var l=function(){if(n.valueCustom){s(n.editableGridSublotOptions.datarows[n.editableGridSublotOptions.getRowIndexInEditMode()],n.valueCustom)
}};
n.editableGridSublotOptions={datarows:[],template:{},labelrows:[],labelrowsFromDb:[],datarowsFromDb:[],dbType:{},createOrUpdateRecord:n.createOrUpdateSubLot,deleteRecord:n.deleteSublot,deleteModalId:"sublotModalPanel",cancelModalId:"sublotCancelModalPanel",featureName:"Sublot",tableName:"sublotTable",newRowAdded:l};
e.then(function(u){n.generalsublotTemplate=[];
n.editableGridSublotOptions.template=[];
n.generalSublotTemplate=u.genralSection;
n.editableGridSublotOptions.template=angular.copy(u.genralSection);
n.searchResetFlag=true
},function(u){},function(u){});
var c=function(u){u.forEach(function(v){if(f.indexOf(v.modelWithoutSeperators)!==-1){n.caratValueSublotTemplate.push(v)
}})
};
n.resetCommonConfig=function(){n.valueCustom={}
};
var b=function(){o.retrieveDesignationBasedFields("lotSub",function(u){n.response=angular.copy(u);
n.sublotDataBean={};
var v=k.retrieveSectionWiseCustomFieldInfo("subLotEntity");
if(!(n.dbTypeForUpdate)){n.dbTypeForUpdate={}
}v.then(function(w){n.editableGridSublotOptions.template=null;
n.editableGridSublotOptions.template=w.genralSection;
c(n.editableGridSublotOptions.template)
})
},function(){h.unMaskLoading();
var v="Failed to retrieve template data";
var u=h.error;
h.addMessage(v,u)
})
};
n.retrieveSublots=function(){o.retrieveDesignationBasedFields("lotSub",function(u){n.response=angular.copy(u);
n.sublotDataBean={};
var v=k.retrieveSectionWiseCustomFieldInfo("subLotEntity");
if(!(n.dbTypeForUpdate)){n.dbTypeForUpdate={}
}v.then(function(z){n.editableGridSublotOptions.template=null;
n.editableGridSublotOptions.template=z.genralSection;
n.listOfModelsOfDateType=[];
var x=[];
var w=Object.keys(u).map(function(A,B){angular.forEach(this[A],function(C){if(A==="SubLot"){x.push({Sublot:C})
}})
},u);
n.editFieldName=[];
n.fieldIdNameMap={};
n.editableGridSublotOptions.template=k.retrieveCustomData(n.editableGridSublotOptions.template,x);
angular.forEach(n.editableGridSublotOptions.template,function(A){if(A.fromModel){n.fieldIdNameMap[A.fieldId]=A.fromModel;
if(n.editFieldName.indexOf(A.fromModel)===-1){n.editFieldName.push(A.fromModel)
}}else{if(A.toModel){n.fieldIdNameMap[A.fieldId]=A.toModel;
if(n.editFieldName.indexOf(A.toModel)===-1){n.editFieldName.push(A.toModel)
}}else{if(A.model){n.fieldIdNameMap[A.fieldId]=A.model;
if(n.editFieldName.indexOf(A.model)===-1){n.editFieldName.push(A.model)
}}}}});
n.fieldNotConfigured=false;
n.showSublotConfigMsg=false;
if(n.fieldNotConfigured){n.showSublotConfigMsg=true
}n.map={};
n.map.dbFieldNames=n.editFieldName;
if(n.editFieldName.length>0){var y={parcelId:p,map:n.map,ruleConfigMap:{fieldIdNameMap:n.fieldIdNameMap,featureName:"subLot"}};
d.retrieveSublotsbyParcel(y,function(A){if(A!==undefined){n.editableGridSublotOptions.datarows=[];
n.editableGridSublotOptions.labelrows=[];
angular.forEach(A,function(D){D.isEditGridFlag=false;
n.editableGridSublotOptions.datarows.push(D)
});
var C=0;
n.editableGridSublotOptions.datarowsFromDb=angular.copy(n.editableGridSublotOptions.datarows);
n.editableGridSublotOptions.datarows.push({isEditGridFlag:false,categoryCustom:{}})
}var B=function(D){n.editableGridSublotOptions.labelrows=angular.copy(D);
n.editableGridSublotOptions.labelrows.push({isEditGridFlag:false,categoryCustom:{}});
n.editableGridSublotOptions.labelrowsFromDb=angular.copy(D)
};
n.newVar=angular.copy(A);
if(n.newVar!==undefined){k.convertorForCustomField(n.newVar,B)
}h.unMaskLoading()
},function(){var B="Could not save, please try again.";
var A=h.error;
h.addMessage(B,A);
h.unMaskLoading()
})
}},function(w){},function(w){})
},function(){h.unMaskLoading();
var v="Failed to retrieve data";
var u=h.error;
h.addMessage(v,u)
})
};
var j=function(u){p=q[u["parcelId$AG$String"]];
n.retrieveSublots();
n.isParcelSelected=true
};
n.gridOptions={enableRowSelection:true,enableSelectAll:false,enableFiltering:true,multiSelect:false,data:[]};
var i=function(u){n.showSublotConfigMsg=true;
n.isParcelSelected=false
};
n.gridOptions.onRegisterApi=function(u){n.gridApi=u;
u.selection.on.rowSelectionChanged(n,function(v){n.selectedParcel=u.selection.getSelectedRows();
if(v.isSelected){j(v.entity)
}else{i(v.entity)
}})
};
n.subLotParcelScreenRule=function(w,v){var u;
if(!!w.entity.screenRuleDetailsWithDbFieldName&&w.entity.screenRuleDetailsWithDbFieldName[v]!==undefined&&w.entity.screenRuleDetailsWithDbFieldName[v]!==null){if(n.selectedParcel.length===0||(n.selectedParcel[0].$$parcelId$$!==w.entity.$$parcelId$$)){u=w.entity.screenRuleDetailsWithDbFieldName[v].colorCode
}}return u
};
n.parcelLabelListForUiGrid=[];
n.invoiceLabelListForUiGrid=[];
n.retrieveParcels=function(){h.maskLoading();
o.retrieveDesignationBasedFieldsBySection(["lotSub","INP"],function(u){n.response=angular.copy(u);
var v=k.retrieveSectionWiseCustomFieldInfo("parcel");
if(!(n.dbTypeForUpdate!==null)){n.dbTypeForUpdate={}
}v.then(function(x){var w=k.retrieveSectionWiseCustomFieldInfo("invoice");
w.then(function(A){n.parcelCommonTemplate=null;
n.parcelCommonTemplate=x.genralSection;
n.invoiceCommonTemplate=A.genralSection;
n.listOfModelsOfDateType=[];
var z=[];
var B=[];
var y=Object.keys(u).map(function(D,E){angular.forEach(this[D],function(F){if(D==="Parcel"){z.push({Parcel:F})
}else{if(D==="Invoice"){B.push({Invoice:F})
}}})
},u);
n.editFieldName=[];
n.invoiceEditFieldName=[];
n.fieldIdNameMap={};
n.invoiceFieldIdNameMap={};
n.parcelCommonTemplate=k.retrieveCustomData(n.parcelCommonTemplate,z);
n.invoiceCommonTemplate=k.retrieveCustomData(n.invoiceCommonTemplate,B);
angular.forEach(n.parcelCommonTemplate,function(D){if(D.fromModel){n.fieldIdNameMap[D.fieldId]=D.fromModel;
if(n.editFieldName.indexOf(D.fromModel)===-1){n.editFieldName.push(D.fromModel)
}}else{if(D.toModel){n.fieldIdNameMap[D.fieldId]=D.toModel;
if(n.editFieldName.indexOf(D.toModel)===-1){n.editFieldName.push(D.toModel)
}}else{if(D.model){n.fieldIdNameMap[D.fieldId]=D.model;
if(n.editFieldName.indexOf(D.model)===-1){n.editFieldName.push(D.model)
}}}}});
angular.forEach(n.invoiceCommonTemplate,function(D){if(D.fromModel){n.invoiceFieldIdNameMap[D.fieldId]=D.fromModel;
if(n.invoiceEditFieldName.indexOf(D.fromModel)===-1){n.invoiceEditFieldName.push(D.fromModel)
}}else{if(D.toModel){n.invoiceFieldIdNameMap[D.fieldId]=D.toModel;
if(n.invoiceEditFieldName.indexOf(D.toModel)===-1){n.invoiceEditFieldName.push(D.toModel)
}}else{if(D.model){n.invoiceFieldIdNameMap[D.fieldId]=D.model;
if(n.invoiceEditFieldName.indexOf(D.model)===-1){n.invoiceEditFieldName.push(D.model)
}}}}});
n.fieldNotConfigured=false;
n.showConfigMsg=false;
if(n.fieldNotConfigured){n.showConfigMsg=true
}angular.forEach(n.parcelCommonTemplate,function(D){if(D.fromModel){n.parcelLabelListForUiGrid.push({name:D.fromModel,displayName:D.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.subLotParcelScreenRule(row, '"+D.fromModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'})
}else{if(D.toModel){n.parcelLabelListForUiGrid.push({name:D.toModel,displayName:D.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.subLotParcelScreenRule(row, '"+D.toModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'})
}else{if(D.model){n.parcelLabelListForUiGrid.push({name:D.model,displayName:D.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.subLotParcelScreenRule(row, '"+D.model+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'})
}}}});
angular.forEach(n.invoiceCommonTemplate,function(D){if(D.fromModel){n.parcelLabelListForUiGrid.push({name:D.fromModel,displayName:D.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.subLotParcelScreenRule(row, '"+D.fromModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'})
}else{if(D.toModel){n.parcelLabelListForUiGrid.push({name:D.toModel,displayName:D.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.subLotParcelScreenRule(row, '"+D.toModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'})
}else{if(D.model){n.parcelLabelListForUiGrid.push({name:D.model,displayName:D.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.subLotParcelScreenRule(row, '"+D.model+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'})
}}}});
var C={};
n.map={};
n.parcelDataBean={};
n.map.dbFieldNames=n.editFieldName;
n.parcelDataBean.featureDbFieldMap=n.map;
n.parcelDataBean.ruleConfigMap={fieldIdNameMap:n.fieldIdNameMap,featureName:"subLot"};
n.invoiceMap={};
n.invoiceDataBean={};
n.invoiceDataBean.dbFieldNames=n.invoiceEditFieldName;
n.invoiceDataBean.ruleConfigMap={fieldIdNameMap:n.invoiceFieldIdNameMap,featureName:"subLot"};
C.parcel=JSON.stringify(n.parcelDataBean);
C.invoice=JSON.stringify(n.invoiceDataBean);
if(n.editFieldName.length>0){d.retrieveAllottedParcelAndInvoice(C,function(D){if(!(D!==undefined&&D!==null&&D.length>0)){n.searchedDataFromDbForUiGrid=undefined;
h.unMaskLoading()
}var E=function(F){n.parcelData=angular.copy(F);
n.parcelLabelListForUiGrid.push({name:"Stock Carat",displayName:"Stock Carat",minWidth:200},{name:"Stock Pieces",displayName:"Stock Pieces",minWidth:200});
n.searchedDataFromDbForUiGrid=[];
q={};
angular.forEach(n.parcelData,function(G){q[G.categoryCustom["parcelId$AG$String"]]=G.value;
angular.forEach(n.parcelLabelListForUiGrid,function(H){if(!G.categoryCustom.hasOwnProperty(H.name)){G.categoryCustom[H.name]="NA"
}else{if(G.categoryCustom.hasOwnProperty(H.name)){if(G.categoryCustom[H.name]===null||G.categoryCustom[H.name]===""||G.categoryCustom[H.name]===undefined){G.categoryCustom[H.name]="NA"
}}}G.categoryCustom["$$$value"]=G.value
});
G.categoryCustom["$$parcelId$$"]=G.value;
G.categoryCustom.screenRuleDetailsWithDbFieldName=G.screenRuleDetailsWithDbFieldName;
n.searchedDataFromDbForUiGrid.push(G.categoryCustom)
});
if(n.searchedDataFromDbForUiGrid.length===0){n.searchedDataFromDbForUiGrid=undefined
}n.gridOptions.data=n.searchedDataFromDbForUiGrid;
n.gridOptions.columnDefs=n.parcelLabelListForUiGrid;
if(!!m.getSelectedParcels()&&Object.keys(m.getSelectedParcels()).length>0){$timeout(function(){for(var H in m.getSelectedParcels()){for(var G=0;
G<n.gridOptions.data.length;
G++){if(n.gridOptions.data[G]["$$$value"]==H){n.gridApi.selection.selectRow(n.gridOptions.data[G]);
n.gridOptions.data[G]["Stock Carat"]+=m.getSelectedParcels()[H].carat;
n.gridOptions.data[G]["Stock Pieces"]+=m.getSelectedParcels()[H].pieces;
n.gridOptions.data[G]["exchangeRate"]+=m.getSelectedParcels()[H].exRate;
break
}}}})
}window.setTimeout(function(){$(window).resize();
$(window).resize()
},100);
h.unMaskLoading()
};
k.convertorForCustomField(D,E)
},function(){var E="Could not save, please try again.";
var D=h.error;
h.addMessage(E,D);
h.unMaskLoading()
})
}else{h.unMaskLoading()
}})
},function(w){},function(w){})
},function(){h.unMaskLoading();
var v="Failed to retrieve data";
var u=h.error;
h.addMessage(v,u)
})
};
n.retrieveParcels();
b()
}])
});