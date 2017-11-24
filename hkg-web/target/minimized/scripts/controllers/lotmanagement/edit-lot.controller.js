define(["hkg","lotService","customFieldService","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicForm","accordionCollapse","sublotService","ruleExecutionService"],function(b,a){b.register.controller("EditLotController",["$rootScope","$scope","LotService","$timeout","$filter","$location","$window","DynamicFormService","CustomFieldService","SublotService","RuleExecutionService",function(j,r,l,v,p,h,o,q,s,e,t){j.mainMenu="stockLink";
j.childMenu="editLot";
r.entity="EDITLOT.";
j.activateMenu();
r.searchedDataFromDbForUiGrid=[];
r.lotLabelListForUiGrid=[];
r.selectedSubLot=[];
var g={},w={},c=[],n,d={carat:"carat$TF$Double",pieces:"no_of_pieces_of_sublot$NM$Long"},u,f,m=0,k;
r.initializeData=function(x){if(x){r.lotEditShow=false;
w={};
r.lotDataBean={};
r.searchedData=[];
r.searchedDataFromDb=[];
r.listFilled=false;
r.lotListTodisplay=[];
r.searchCustom=q.resetSection(r.generalSearchTemplate);
r.lotDataBean.featureCustomMap={};
var y=q.retrieveSearchWiseCustomFieldInfo("lotEdit");
r.flag={};
r.flag.searchFieldNotAvailable=false;
r.dbType={};
r.modelAndHeaderList=[];
r.modelAndHeaderListForLot=[];
y.then(function(E){r.generalSearchTemplate=E.genralSection;
r.searchInvoiceTemplate=[];
r.searchParcelTemplate=[];
r.searchLotTemplate=[];
var z=[];
var D=[];
var C=[];
r.fieldIdNameMap={};
if(r.generalSearchTemplate!=null&&r.generalSearchTemplate.length>0){for(var A=0;
A<r.generalSearchTemplate.length;
A++){var B=r.generalSearchTemplate[A];
if(B.featureName.toLowerCase()==="invoice"){r.searchInvoiceTemplate.push(angular.copy(B));
z.push(angular.copy(B.model))
}else{if(B.featureName.toLowerCase()==="parcel"){r.searchParcelTemplate.push(angular.copy(B));
D.push(angular.copy(B.model))
}else{if(B.featureName.toLowerCase()==="lot"){r.searchLotTemplate.push(angular.copy(B));
C.push(angular.copy(B.model))
}}}g[B.model]=B.featureName;
r.modelAndHeaderList.push({model:B.model,header:B.label});
if(B.fromModel){r.lotLabelListForUiGrid.push({name:B.fromModel,displayName:B.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.editLotScreenRule(row, '"+B.fromModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
r.fieldIdNameMap[B.fieldId]=B.fromModel
}else{if(B.toModel){r.lotLabelListForUiGrid.push({name:B.toModel,displayName:B.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.editLotScreenRule(row, '"+B.toModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
r.fieldIdNameMap[B.fieldId]=B.toModel
}else{if(B.model){r.lotLabelListForUiGrid.push({name:B.model,displayName:B.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.editLotScreenRule(row, '"+B.model+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
r.fieldIdNameMap[B.fieldId]=B.model
}}}}if(z.length>0){w.invoice=z
}if(D.length>0){w.parcel=D
}if(C.length>0){w.lot=C
}}else{r.flag.searchFieldNotAvailable=true
}r.searchResetFlag=true;
r.dataRetrieved=true
},function(z){},function(z){})
}};
r.editLotScreenRule=function(z,y){var x;
if(!!z.entity.screenRuleDetailsWithDbFieldName&&z.entity.screenRuleDetailsWithDbFieldName[y]!==undefined&&z.entity.screenRuleDetailsWithDbFieldName[y]!==null){if(r.selectedLot.length===0||(r.selectedLot[0].$$lotId$$!==z.entity.$$lotId$$)){x=z.entity.screenRuleDetailsWithDbFieldName[y].colorCode
}}return x
};
r.editSublotScreenRule=function(z,y){var x;
if(!!z.entity.screenRuleDetailsWithDbFieldName&&z.entity.screenRuleDetailsWithDbFieldName[y]!==undefined&&z.entity.screenRuleDetailsWithDbFieldName[y]!==null){if(c.length===0||(c.indexOf(z.entity.$$subLotId$$))===-1){x=z.entity.screenRuleDetailsWithDbFieldName[y].colorCode
}}return x
};
r.initializeData(true);
r.initEditLotForm=function(x){r.editLotForm=x
};
r.retrieveSearchedData=function(C){r.selectOneParameter=false;
r.gridOptions={};
r.searchedData=[];
r.searchedDataFromDbForUiGrid=[];
r.listFilled=false;
if(Object.getOwnPropertyNames(r.searchCustom).length>0){var A=false;
for(var E in r.searchCustom){if(typeof r.searchCustom[E]==="object"&&r.searchCustom[E]!=null){var B=angular.copy(r.searchCustom[E].toString());
if(typeof B==="string"&&B!==null&&B!==undefined&&B.length>0){A=true;
break
}}if(typeof r.searchCustom[E]==="string"&&r.searchCustom[E]!==null&&r.searchCustom[E]!==undefined&&r.searchCustom[E].length>0){A=true;
break
}if(typeof r.searchCustom[E]==="number"&&!!(r.searchCustom[E])){A=true;
break
}if(typeof r.searchCustom[E]==="boolean"){A=true;
break
}}if(A){j.maskLoading();
r.lotDataBean.featureCustomMap={};
r.lotDataBean.featureCustomMapValue={};
r.map={};
var z={};
var x=q.convertSearchData(r.searchInvoiceTemplate,r.searchParcelTemplate,r.searchLotTemplate,null,r.searchCustom);
r.searchCustom=angular.copy(x);
angular.forEach(g,function(J,H){var I=r.searchCustom[H];
if(I!==undefined){var G={};
if(!z[J]){G[H]=I;
z[J]=G
}else{var F=z[J];
F[H]=I;
z[J]=F
}}else{I=r.searchCustom["to"+H];
if(I!==undefined){var G={};
if(!z[J]){G["to"+H]=I;
z[J]=G
}else{var F=z[J];
F["to"+H]=I;
z[J]=F
}}I=r.searchCustom["from"+H];
if(I!==undefined){var G={};
if(!z[J]){G["from"+H]=I;
z[J]=G
}else{var F=z[J];
F["from"+H]=I;
z[J]=F
}}}});
r.lotDataBean.featureCustomMapValue=z;
r.lotDataBean.hasPacket=false;
r.lotDataBean.featureDbFieldMap=w;
r.lotDataBean.ruleConfigMap={fieldIdNameMap:r.fieldIdNameMap,featureName:"editLot"};
l.search(r.lotDataBean,function(F){var G=q.retrieveSectionWiseCustomFieldInfo("lot");
G.then(function(H){r.lotCustom=q.resetSection(r.generalSearchTemplate);
r.lotTemplate=H.genralSection;
r.searchedDataFromDb=angular.copy(F);
var I=function(J){r.searchedData=angular.copy(J);
angular.forEach(r.searchedData,function(K){angular.forEach(r.lotLabelListForUiGrid,function(L){if(!K.categoryCustom.hasOwnProperty(L.name)){K.categoryCustom[L.name]="NA"
}else{if(K.categoryCustom.hasOwnProperty(L.name)){if(K.categoryCustom[L.name]===null||K.categoryCustom[L.name]===""||K.categoryCustom[L.name]===undefined){K.categoryCustom[L.name]="NA"
}}}});
K.categoryCustom.$$lotId$$=K.value;
K.categoryCustom.screenRuleDetailsWithDbFieldName=K.screenRuleDetailsWithDbFieldName;
r.searchedDataFromDbForUiGrid.push(K.categoryCustom)
});
r.gridOptions={};
r.gridOptions.enableFiltering=true;
r.gridOptions.data=r.searchedDataFromDbForUiGrid;
r.gridOptions.columnDefs=r.lotLabelListForUiGrid;
r.gridOptions.multiSelect=false;
r.gridOptions.enableRowSelection=true;
r.gridOptions.enableSelectAll=false;
r.selectedLot=[];
r.gridOptions.onRegisterApi=function(K){r.gridApi=K;
K.selection.on.rowSelectionChanged(r,function(L){r.selectedLot=K.selection.getSelectedRows()
});
K.selection.on.rowSelectionChangedBatch(r,function(L){r.selectedLot=K.selection.getSelectedRows()
})
};
window.setTimeout(function(){$(window).resize();
$(window).resize()
},100);
r.listFilled=true;
j.unMaskLoading()
};
q.convertorForCustomField(F,I)
},function(H){},function(H){})
},function(){var G="Could not retrieve, please try again.";
var F=j.error;
j.addMessage(G,F);
j.unMaskLoading()
})
}else{var D="Please select atleast one search criteria for search";
var y=j.warning;
j.addMessage(D,y)
}}else{var D="Please select atleast one search criteria for search";
var y=j.warning;
j.addMessage(D,y)
}};
r.reset=function(y){if(y==="searchCustom"){r.searchCustom={};
var x=q.retrieveSearchWiseCustomFieldInfo("lotEdit");
r.dbType={};
x.then(function(z){r.generalSearchTemplate=z.genralSection;
r.searchResetFlag=true;
j.unMaskLoading();
r.flag.customFieldGenerated=true
},function(z){},function(z){})
}else{if(y==="lotCustom"){r.lotCustom={};
var x=q.retrieveSectionWiseCustomFieldInfo("lot");
x.then(function(A){var B=[];
var z=Object.keys(r.response).map(function(C,D){angular.forEach(this[C],function(E){if(C!=="Lot#P#"){B.push({Lot:E})
}})
},r.response);
r.updateLotTemplate=null;
r.updateLotTemplate=A.genralSection;
r.updateLotTemplate=q.retrieveCustomData(r.updateLotTemplate,B);
r.lotEditShow=true;
j.unMaskLoading();
r.flag.customFieldGenerated=true
},function(z){},function(z){})
}}};
r.onCanel=function(x){if(x!=null){x.$dirty=false;
r.flag.showUpdatePage=false
}j.editLot=false;
j.lotByParcel=null;
r.submitted=false;
r.lotEditShow=false;
r.selectedLot=[];
r.reset("lotCustom")
};
r.onCanelOfSearch=function(x){if(r.editLotForm!=null){r.editLotForm.$dirty=false
}r.searchedData=[];
r.listFilled=false;
r.searchResetFlag=false;
r.selectedLot=[];
r.gridOptions={};
r.reset("searchCustom")
};
r.saveLot=function(x){r.submitted=true;
angular.forEach(r.listOfModelsOfDateType,function(z){if(r.lotCustom.hasOwnProperty(z)){if(r.lotCustom[z]!==null&&r.lotCustom[z]!==undefined){r.lotCustom[z]=new Date(r.lotCustom[z])
}else{r.lotCustom[z]=""
}}});
console.log("form validation :"+JSON.stringify(x.$valid));
if(x.$valid&&r.checkAddLotFormExtraValidation()&&r.checkAddLotFormExtraValidationWihoutSublot()){j.maskLoading();
var y={featureName:"editLot",entityId:r.lotIdForConstraint,entityType:"lot",currentFieldValueMap:r.lotCustom,dbType:r.lotDbType,otherEntitysIdMap:{invoiceId:r.invoiceIdForConstraint,parcelId:r.parcelIdForConstraint}};
console.log("post rule dataToSend::::"+JSON.stringify(y));
t.executePostRule(y,function(z){if(!!z.validationMessage){var A=j.warning;
j.addMessage(z.validationMessage,A);
j.unMaskLoading()
}else{r.lotDataBean.lotCustom=r.lotCustom;
r.lotDataBean.lotDbType=angular.copy(r.lotDbType);
r.lotDataBean.subLots=c;
l.update(r.lotDataBean,function(B){r.submitted=false;
r.editLotForm.$dirty=false;
r.initializeData(true);
j.editLot=false;
j.lotByParcel=null;
r.selectedLot=[];
r.gridOptions={};
j.unMaskLoading()
},function(){j.unMaskLoading();
var C="Could not update lot, please try again.";
var B=j.error;
j.addMessage(C,B)
})
}},function(z){j.unMaskLoading();
var B="Failed to authenticate post rule.";
var A=j.error;
j.addMessage(B,A)
})
}};
r.checkAddLotFormExtraValidation=function(){if(c.length>0){if(u!=r.lotCustom["carat_of_lot$NM$Double"]||f!=r.lotCustom["no_of_pieces_of_lot$NM$Long"]){j.addMessage("Carat and pieces value should be equal to selected sub lot total carat and pieces",j.error);
return false
}else{return true
}}else{return true
}};
r.checkAddLotFormExtraValidationWihoutSublot=function(){if(c.length<=0){if(r.lotCustom.stockCarat<r.lotCustom["carat_of_lot$NM$Double"]||r.lotCustom.stockPieces<r.lotCustom["no_of_pieces_of_lot$NM$Long"]){j.addMessage("Carat or pieces values should not be greater than stock carat and stock pieces",j.error);
return false
}else{return true
}}else{return true
}};
r.editLot=function(A){var x=[];
var C=[];
var z=[];
var B={};
if((A===undefined||A===null)){A=r.selectedLot[0]
}if(A!=null){j.maskLoading();
if(r.searchedDataFromDb!==undefined&&r.searchedDataFromDb!==null){for(var y=0;
y<r.searchedDataFromDb.length;
y++){if(A.lotID$AG$String===r.searchedDataFromDb[y].categoryCustom.lotID$AG$String){A=angular.copy(r.searchedDataFromDb[y]);
break
}}}r.invoiceIdForConstraint=A.description;
r.parcelIdForConstraint=A.label;
r.lotIdForConstraint=A.value;
r.flag.showUpdatePage=false;
r.preRuleSatisfied=false;
var D={featureName:"editLot",entityId:A.value,entityType:"lot"};
t.executePreRule(D,function(E){if(!!E.validationMessage){r.preRuleSatisfied=true;
var F=j.warning;
j.addMessage(E.validationMessage,F);
j.unMaskLoading()
}else{r.flag.showUpdatePage=true;
s.retrieveDesignationBasedFieldsBySection(["lotEdit","GEN"],function(G){if(G.Lot==null||G.Lot.length==0){r.noFieldConfiguredForLot=true
}r.lotDataBean={};
var I=q.retrieveSectionWiseCustomFieldInfo("lot");
I.then(function(L){r.updateLotParentTemplate=L.genralSection;
var K=[];
r.response=angular.copy(G);
var J=Object.keys(G).map(function(M,N){angular.forEach(this[M],function(O){if(M==="Lot#P#"){K.push({Lot:O})
}})
},G);
r.updateLotParentTemplate=q.retrieveCustomData(r.updateLotParentTemplate,K);
angular.forEach(r.updateLotParentTemplate,function(M){if(M.model){z.push(M.model)
}});
if(z.length>0){B.lotDbFieldName=z
}},function(J){},function(J){});
var I=q.retrieveSectionWiseCustomFieldInfo("invoice");
r.dbTypeForInvoice={};
I.then(function(L){r.updateInvoiceTemplate=L.genralSection;
var K=[];
var J=Object.keys(G).map(function(M,N){angular.forEach(this[M],function(O){if(M==="Invoice#P#"){K.push({Invoice:O})
}})
},G);
r.updateInvoiceTemplate=q.retrieveCustomData(r.updateInvoiceTemplate,K);
angular.forEach(r.updateInvoiceTemplate,function(M){if(M.model){x.push(M.model)
}});
if(x.length>0){B.invoiceDbFieldName=x
}if(A.custom3!=null){r.invoiceDataBean={};
r.invoiceDataBean.invoiceDbType=r.dbTypeForInvoice
}},function(J){},function(J){});
var H=q.retrieveSectionWiseCustomFieldInfo("parcel");
r.dbTypeForParcel={};
H.then(function(L){r.updateParcelTemplate=L.genralSection;
var K=[];
var J=Object.keys(G).map(function(M,N){angular.forEach(this[M],function(O){if(M==="Parcel#P#"){K.push({Parcel:O})
}})
},G);
r.updateParcelTemplate=q.retrieveCustomData(r.updateParcelTemplate,K);
angular.forEach(r.updateParcelTemplate,function(M){if(M.model){C.push(M.model)
}});
if(C.length>0){B.parcelDbFieldName=C
}r.flag.customFieldGeneratedForUpdate=true;
if(A.custom4!=null){r.parcelDataBean={};
r.parcelDataBean.parcelDbType=r.dbTypeForParcel
}},function(J){},function(J){});
r.lotCustom=q.resetSection(r.updateLotTemplate);
var I=q.retrieveSectionWiseCustomFieldInfo("lot");
I.then(function(L){if(!(r.lotDbType!=null)){r.lotDbType={}
}r.updateLotTemplate=L.genralSection;
r.listOfModelsOfDateType=[];
if(r.updateLotTemplate!==null&&r.updateLotTemplate!==undefined){angular.forEach(r.updateLotTemplate,function(N){if(N.type!==null&&N.type!==undefined&&N.type==="date"){r.listOfModelsOfDateType.push(N.model)
}if(N.model==="in_stock_of_lot$UMS$String"||N.model==="allot_to_lot$UMS$String"||N.model==="lotID$AG$String"){N.isViewFromDesignation=true
}})
}var M=[];
var J=Object.keys(G).map(function(N,O){angular.forEach(this[N],function(P){if(N==="Lot"){M.push({Lot:P})
}})
},G);
r.updateLotTemplate=q.retrieveCustomData(r.updateLotTemplate,M);
r.fieldIdNameMap={};
angular.forEach(r.updateLotTemplate,function(N){if(N.model&&z.indexOf(N.model)===-1){z.push(N.model);
r.fieldIdNameMap[N.fieldId]=N.model
}});
var K=[];
K.push(A.value);
B.lotObjectId=K;
if(z.length>0){B.lotDbFieldName=z
}B.ruleConfigMap={fieldIdNameMap:r.fieldIdNameMap,featureName:"editLot"};
l.retrieveLotById(B,function(N){r.flag.customFieldGeneratedForUpdate=true;
r.lotDataBean={};
r.lotDataBean.id=r.lotIdForConstraint;
r.lotDataBean.franchise=N.custom7;
if(N.custom3==null){r.invoiceCustom={}
}if(N.custom4==null){r.parcelCustom={}
}r.invoiceCustom=N.custom3;
r.parcelCustom=angular.copy(N.custom4);
if(N.custom1!==null&&N.custom1!==undefined){angular.forEach(r.listOfModelsOfDateType,function(O){if(N.custom1.hasOwnProperty(O)){if(N.custom1[O]!==null&&N.custom1[O]!==undefined){N.custom1[O]=new Date(N.custom1[O])
}else{N.custom1[O]=""
}}});
N.custom1.stockPieces+=angular.copy(N.custom1["no_of_pieces_of_lot$NM$Long"]);
N.custom1.stockCarat+=angular.copy(N.custom1["carat_of_lot$NM$Double"]);
delete N.custom1.stockPieces;
delete N.custom1.stockCarat;
r.lotCustom=angular.copy(N.custom1);
r.lotParentCustom=angular.copy(N.custom1);
r.lotScreenRules=N.screenRuleDetailsWithDbFieldName
}else{r.lotParentCustom={};
r.lotCustom={}
}r.lotDataBean.lotDbType=angular.copy(r.lotDbType);
r.lotDataBean.subLots=N.otherValues;
k=N.label;
console.log("calling you....................."+A.value);
i(A.value);
r.lotEditShow=true;
j.unMaskLoading()
},function(){j.unMaskLoading();
var O="Could not retrieve parcel, please try again.";
var N=j.error;
j.addMessage(O,N)
})
},function(J){},function(J){});
r.flag.customFieldGenerated=true
},function(){j.unMaskLoading();
var H="Failed to retrieve data";
var G=j.error;
j.addMessage(H,G)
})
}},function(E){j.unMaskLoading();
var G="Failed to retrieve pre rule.";
var F=j.error;
j.addMessage(G,F)
})
}};
var i=function(z){r.gridOptionsSubLot={enableRowSelection:true,enableSelectAll:true,enableFiltering:true,multiSelect:true,data:[],columnDefs:[],onRegisterApi:function(A){n=A;
A.selection.on.rowSelectionChanged(r,function(B){y(A.selection.getSelectedRows())
});
A.selection.on.rowSelectionChangedBatch(r,function(B){y(A.selection.getSelectedRows())
})
}};
var y=function(B){c=[];
u=0;
f=0;
for(var A in B){c.push(B[A].id);
if(!!B[A][d.carat]){u+=B[A][d.carat]
}if(!!B[A][d.pieces]){f+=B[A][d.pieces]
}}r.lotCustom["carat_of_lot$NM$Double"]=u;
r.lotCustom["no_of_pieces_of_lot$NM$Long"]=f
};
var x={};
s.retrieveDesignationBasedFieldsBySection(["lotEdit","ASL"],function(A){console.log(JSON.stringify(A));
if(!!A.SubLot&&A.SubLot.length>0){r.noFieldConfiguredForSubLot=true
}r.sublotDataBean={};
var B=q.retrieveSectionWiseCustomFieldInfo("subLotEntity");
if(!(r.dbTypeForUpdate)){r.dbTypeForUpdate={}
}B.then(function(H){x.template=null;
x.template=H.genralSection;
r.listOfModelsOfDateType=[];
var D=[];
Object.keys(A).map(function(I,J){angular.forEach(this[I],function(K){if(I==="SubLot"){D.push({Sublot:K})
}})
},A);
console.log("check 1");
r.editFieldName=[];
r.fieldIdNameMap={};
x.template=q.retrieveCustomData(x.template,D);
angular.forEach(x.template,function(I){if(I.fromModel){r.fieldIdNameMap[I.fieldId]=I.fromModel;
r.gridOptionsSubLot.columnDefs.push({name:I.fromModel,displayName:I.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.editSublotScreenRule(row, '"+I.fromModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
if(r.editFieldName.indexOf(I.fromModel)===-1){r.editFieldName.push(I.fromModel)
}}else{if(I.toModel){r.fieldIdNameMap[I.fieldId]=I.toModel;
r.gridOptionsSubLot.columnDefs.push({name:I.toModel,displayName:I.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.editSublotScreenRule(row, '"+I.toModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
if(r.editFieldName.indexOf(I.toModel)===-1){r.editFieldName.push(I.toModel)
}}else{if(I.model){r.fieldIdNameMap[I.fieldId]=I.model;
r.gridOptionsSubLot.columnDefs.push({name:I.model,displayName:I.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.editSublotScreenRule(row, '"+I.model+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
if(r.editFieldName.indexOf(I.model)===-1){r.editFieldName.push(I.model)
}}}}});
r.fieldNotConfigured=false;
r.showConfigMsg=false;
var C=[];
if(x.template!==undefined&&x.template!==null&&x.template.length>0){for(var E=0;
E<x.template.length;
E++){C.push(angular.copy(x.template[E].model))
}}if(C.length>0&&r.mandatorySublotFields&&r.mandatorySublotFields.length>0){for(var G=0;
G<r.mandatorySublotFields.length;
G++){if(C.indexOf(r.mandatorySublotFields[G])===-1){r.fieldNotConfigured=true;
break
}}}else{r.fieldNotConfigured=false
}if(r.fieldNotConfigured){r.showConfigMsg=true
}r.map={};
r.map.dbFieldNames=r.editFieldName;
console.log(r.editFieldName.length>0);
if(r.editFieldName.length>0){var F={parcelId:k,map:r.map,ruleConfigMap:{fieldIdNameMap:r.fieldIdNameMap,featureName:"addLot"},excludeSubLotWithAssociatedLot:true,includeSubLotWithAssociatedLot:z};
e.retrieveSublotsbyParcel(F,function(J){var I;
var L=function(N){I=angular.copy(N);
for(var O in I){var M={};
I[O].categoryCustom.$$subLotId$$=I[O].value;
I[O].categoryCustom.screenRuleDetailsWithDbFieldName=I[O].screenRuleDetailsWithDbFieldName;
angular.extend(M,I[O].categoryCustom,{id:I[O].value});
r.gridOptionsSubLot.data.push(M)
}v(function(){for(var P in I){if(!!r.lotDataBean.subLots&&r.lotDataBean.subLots.indexOf(I[P].value)>-1&&n){n.selection.selectRow(r.gridOptionsSubLot.data[P])
}}})
};
var K=angular.copy(J);
if(K!==undefined){q.convertorForCustomField(K,L)
}j.unMaskLoading()
},function(){var J="Could not save, please try again.";
var I=j.error;
j.addMessage(J,I);
j.unMaskLoading()
})
}},function(C){},function(C){})
},function(){j.unMaskLoading();
var B="Failed to retrieve data";
var A=j.error;
j.addMessage(B,A)
})
};
if(j.editLot){r.editLot(j.lotByParcel)
}r.deleteLot=function(){$("#deleteLotPopUp").modal("show")
};
r.deleteLotFromPopup=function(){if(r.lotIdForConstraint!==undefined&&r.lotIdForConstraint!==null){j.maskLoading();
l.deleteLot(r.lotIdForConstraint,function(){j.unMaskLoading();
r.hideLotPopUp();
r.onCanel(r.editLotForm);
r.searchedData=[];
r.listFilled=false
},function(){j.unMaskLoading()
})
}};
r.hideLotPopUp=function(){$("#deleteLotPopUp").modal("hide");
j.removeModalOpenCssAfterModalHide()
}
}])
});