define(["hkg","lotService","customFieldService","parcelService","lotmanagement/edit-lot.controller","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicForm","accordionCollapse","sublotService","ruleExecutionService"],function(b,a){b.register.controller("AddLotController",["$rootScope","$scope","LotService","$timeout","$filter","$location","$window","CustomFieldService","DynamicFormService","ParcelService","SublotService","$q","RuleExecutionService",function(l,r,m,y,p,k,o,t,q,v,f,x,u){l.mainMenu="stockLink";
l.childMenu="addLot";
r.entity="ADDLOT.";
l.activateMenu();
r.flag={};
r.flag.searchFieldNotAvailable=false;
r.remCaratMsg="";
var e={},g={},d=[],n,s,c={carat:"carat$TF$Double",pieces:"no_of_pieces_of_sublot$NM$Long"},w,j;
r.currentYear=l.getCurrentServerDate().getFullYear().toString().substring(2,4);
r.fieldsToClear=["carat_of_lot$NM$Double","no_of_pieces_of_lot$NM$Long"];
l.maskLoading();
r.searchedDataFromDbForUiGrid=[];
r.lotDataForUiGrid=[];
r.lotListForUIGrid=[];
r.parcelLabelListForUiGrid=[];
r.lotDataBean={};
r.parcelDataBean={};
r.lotListTodisplay=[];
r.modifiedLotListToDisplay=[];
r.AddLot=this;
r.searchCustom=q.resetSection(r.generalSearchTemplate);
var h=q.retrieveSearchWiseCustomFieldInfo("lotAdd");
r.dbType={};
var i={};
r.modelAndHeaderList=[];
r.modelAndHeaderListForLot=[];
r.processingFlag={};
h.then(function(D){r.generalSearchTemplate=angular.copy(D.genralSection);
if(D.genralSection===undefined||D.genralSection===null){r.generalSearchTemplate=[]
}r.searchInvoiceTemplate=[];
r.searchParcelTemplate=[];
var z=[];
var C=[];
g={};
if(r.generalSearchTemplate!=null&&r.generalSearchTemplate.length>0){for(var A=0;
A<r.generalSearchTemplate.length;
A++){var B=r.generalSearchTemplate[A];
if(B.featureName.toLowerCase()==="invoice"){r.searchInvoiceTemplate.push(angular.copy(B));
z.push(angular.copy(B.model));
g[B.fieldId]=B.model
}else{if(B.featureName.toLowerCase()==="parcel"){r.searchParcelTemplate.push(angular.copy(B));
C.push(angular.copy(B.model));
g[B.fieldId]=B.model
}}i[B.model]=B.featureName;
r.modelAndHeaderList.push({model:B.model,header:B.label});
if(B.fromModel){r.parcelLabelListForUiGrid.push({name:B.fromModel,displayName:B.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.searchedParcelScreenRule(row, '"+B.fromModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'})
}else{if(B.toModel){r.parcelLabelListForUiGrid.push({name:B.toModel,displayName:B.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.searchedParcelScreenRule(row, '"+B.toModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'})
}else{if(B.model){r.parcelLabelListForUiGrid.push({name:B.model,displayName:B.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.searchedParcelScreenRule(row, '"+B.model+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'})
}}}}if(z.length>0){e.invoice=z
}if(C.length>0){e.parcel=C
}}else{r.flag.searchFieldNotAvailable=true
}r.searchResetFlag=true;
r.dataRetrieved=true;
l.unMaskLoading()
},function(z){},function(z){});
r.searchedParcelScreenRule=function(B,A){var z;
if(!!B.entity.screenRuleDetailsWithDbFieldName&&B.entity.screenRuleDetailsWithDbFieldName[A]!==undefined&&B.entity.screenRuleDetailsWithDbFieldName[A]!==null){if(r.selectedParcel.length===0||(r.selectedParcel[0].$$parcelId$$!==B.entity.$$parcelId$$)){z=B.entity.screenRuleDetailsWithDbFieldName[A].colorCode
}}return z
};
r.addSublotScreenRule=function(B,A){var z;
if(!!B.entity.screenRuleDetailsWithDbFieldName&&B.entity.screenRuleDetailsWithDbFieldName[A]!==undefined&&B.entity.screenRuleDetailsWithDbFieldName[A]!==null){if(d.length===0||(d.indexOf(B.entity.$$subLotId$$)===-1)){z=B.entity.screenRuleDetailsWithDbFieldName[A].colorCode
}}return z
};
r.addedLotScreenRule=function(B,A){var z;
if(!!B.entity.screenRuleDetailsWithDbFieldName&&B.entity.screenRuleDetailsWithDbFieldName[A]!==undefined&&B.entity.screenRuleDetailsWithDbFieldName[A]!==null){z=B.entity.screenRuleDetailsWithDbFieldName[A].colorCode
}return z
};
r.initAddLotForm=function(z){r.addLotForm=z
};
r.retrieveSearchedData=function(F){r.processingFlag.retrieveSearchedDataCompleted=false;
r.selectOneParameter=false;
r.searchedDataFromDbForUiGrid=[];
r.gridOptionsForParcel={};
r.searchedData=[];
r.listFilled=false;
if(Object.getOwnPropertyNames(r.searchCustom).length>0||l.createdParcelIds){var C=false;
for(var G in r.searchCustom){if(typeof r.searchCustom[G]==="object"&&r.searchCustom[G]!=null){var D=angular.copy(r.searchCustom[G].toString());
if(typeof D==="string"&&D!==null&&D!==undefined&&D.length>0){C=true;
break
}}if(typeof r.searchCustom[G]==="string"&&r.searchCustom[G]!==null&&r.searchCustom[G]!==undefined&&r.searchCustom[G].length>0){C=true;
break
}if(typeof r.searchCustom[G]==="number"&&!!(r.searchCustom[G])){C=true;
break
}if(typeof r.searchCustom[G]==="boolean"){C=true;
break
}}if(C||l.createdParcelIds){r.parcelDataBean.featureCustomMapValue={};
r.map={};
var B={};
var z=q.convertSearchData(r.searchInvoiceTemplate,r.searchParcelTemplate,null,null,r.searchCustom);
r.searchCustom=angular.copy(z);
angular.forEach(i,function(L,J){var K=r.searchCustom[J];
if(K!==undefined){var I={};
if(!B[L]){I[J]=K;
B[L]=I
}else{var H=B[L];
H[J]=K;
B[L]=H
}}else{K=r.searchCustom["to"+J];
if(K!==undefined){var I={};
if(!B[L]){I["to"+J]=K;
B[L]=I
}else{var H=B[L];
H["to"+J]=K;
B[L]=H
}}K=r.searchCustom["from"+J];
if(K!==undefined){var I={};
if(!B[L]){I["from"+J]=K;
B[L]=I
}else{var H=B[L];
H["from"+J]=K;
B[L]=H
}}}});
r.parcelDataBean.featureCustomMapValue=B;
l.maskLoading();
r.parcelDataBean.featureDbFieldMap=e;
r.parcelDataBean.ruleConfigMap={fieldIdNameMap:g,featureName:"addLot"};
delete r.parcelDataBean.invoiceDataBean;
r.parcelDataBean.searchOnParameter=true;
if(l.createdParcelIds){r.parcelDataBean.searchOnParameter=false;
r.parcelDataBean.id=angular.copy(l.createdParcelIds);
r.parcelDataBean.id=r.parcelDataBean.id.substring(1,r.parcelDataBean.id.length-1);
console.log(JSON.stringify(r.parcelDataBean.id));
l.createdParcelIds=undefined
}v.search(r.parcelDataBean,function(H){r.searchedDataFromDb=angular.copy(H);
var I=function(J){r.searchedData=angular.copy(J);
angular.forEach(r.searchedData,function(K){angular.forEach(r.parcelLabelListForUiGrid,function(L){if(!K.categoryCustom.hasOwnProperty(L.name)){K.categoryCustom[L.name]="NA"
}else{if(K.categoryCustom.hasOwnProperty(L.name)){if(K.categoryCustom[L.name]===null||K.categoryCustom[L.name]===""||K.categoryCustom[L.name]===undefined){K.categoryCustom[L.name]="NA"
}}}});
K.categoryCustom.screenRuleDetailsWithDbFieldName=K.screenRuleDetailsWithDbFieldName;
K.categoryCustom.$$parcelId$$=K.value;
r.searchedDataFromDbForUiGrid.push(K.categoryCustom)
});
r.gridOptionsForParcel.enableFiltering=true;
r.gridOptionsForParcel.multiSelect=false;
r.gridOptionsForParcel.enableRowSelection=true;
r.gridOptionsForParcel.enableSelectAll=false;
r.selectedParcel=[];
r.gridOptionsForParcel.onRegisterApi=function(K){s=K;
K.selection.on.rowSelectionChanged(r,function(L){r.selectedParcel=K.selection.getSelectedRows()
});
K.selection.on.rowSelectionChangedBatch(r,function(L){r.selectedParcel=K.selection.getSelectedRows()
})
};
r.gridOptionsForParcel.data=r.searchedDataFromDbForUiGrid;
r.gridOptionsForParcel.columnDefs=r.parcelLabelListForUiGrid;
r.processingFlag.retrieveSearchedDataCompleted=true
};
q.convertorForCustomField(H,I,false);
window.setTimeout(function(){$(window).resize();
$(window).resize()
},100);
r.listFilled=true;
r.lotDataBean.lotCustom=q.resetSection(r.generalSearchTemplate);
l.unMaskLoading()
},function(){var I="Could not retrieve, please try again.";
var H=l.error;
l.addMessage(I,H);
r.processingFlag.retrieveSearchedDataCompleted=true;
l.unMaskLoading()
})
}else{var E="Please select atleast one search criteria for search";
var A=l.warning;
l.addMessage(E,A);
r.processingFlag.retrieveSearchedDataCompleted=true
}}else{var E="Please select atleast one search criteria for search";
var A=l.warning;
l.addMessage(E,A);
r.processingFlag.retrieveSearchedDataCompleted=true
}};
if(l.createdParcelIds!==undefined){y(function(){r.retrieveSearchedData()
},1000)
}r.addLot=function(z){r.preRuleSatisfied=false;
r.flag.showAddPage=false;
var z=angular.copy(r.selectedParcel[0]);
var A={featureName:"addLot",entityId:z.$$parcelId$$,entityType:"parcel",};
u.executePreRule(A,function(E){if(!!E.validationMessage){r.preRuleSatisfied=true;
var G=l.warning;
l.addMessage(E.validationMessage,G)
}else{r.mapToSent={};
g={};
this.lotAddFlag=false;
r.lotDataForUiGrid=[];
r.gridOptions={};
r.gridOptions.enableFiltering=true;
r.caratOfParcel=undefined;
if(z!=null){r.caratOfParcel=parseFloat(z.carat_of_parcel$NM$Double);
r.remainingCarat=parseFloat(z.carat_of_parcel$NM$Double);
if(r.remainingCarat<0){r.remCaratMsg="Carat value limit exceeded"
}else{r.remCaratMsg="Carat remaining is "+r.remainingCarat
}r.piecesOfParcel=parseFloat(z.no_of_pieces_of_parcel$NM$Long);
if(r.caratOfParcel!==undefined&&r.caratOfParcel!==null&&r.caratOfParcel.toString()!=="NaN"){r.caratDoesNotMatch=false
}else{r.caratDoesNotMatch=true
}if(r.searchedDataFromDb!=null&&r.searchedDataFromDb.length>0){for(var F=0;
F<r.searchedDataFromDb.length;
F++){if(r.searchedDataFromDb[F].categoryCustom.parcelDbObjectId===z.$$parcelId$$){z=angular.copy(r.searchedDataFromDb[F]);
break
}}}r.lotListToSave=[];
r.count=0;
r.invoiceId=z.description;
r.invoiceIdForConstraint=z.description;
r.parcelId=z.value;
r.parcelIdForConstraint=z.value;
r.flag.showAddPage=true;
l.maskLoading();
r.parcelDataBean={invoiceDataBean:{invoiceCustom:"",invoiceDbType:""}};
var D=[];
m.retrieveFranchiseDetails(function(H){r.franchiseList=angular.copy(H)
},function(){l.unMaskLoading();
var I="Could not retrieve franchise detail, please try again.";
var H=l.error;
l.addMessage(I,H)
});
t.retrieveDesignationBasedFieldsBySection(["lotAdd","GEN"],function(H){if(H.Lot==null||H.Lot.length==0){r.noFieldConfiguredForLot=true
}r.AddLot=this;
this.lotAddFlag=true;
r.invoiceCustomData=q.resetSection(r.generaInvoiceTemplate);
var I=q.retrieveSectionWiseCustomFieldInfo("invoice");
r.invoiceDbType={};
I.then(function(M){r.generaInvoiceTemplate=M.genralSection;
var N=[];
r.response=angular.copy(H);
var L=Object.keys(H).map(function(O,P){angular.forEach(this[O],function(Q){if(O==="Invoice#P#"){N.push({Lot:Q})
}})
},H);
r.generaInvoiceTemplate=q.retrieveCustomData(r.generaInvoiceTemplate,N);
angular.forEach(r.generaInvoiceTemplate,function(O){if(O.model){D.push(O.model)
}})
},function(L){},function(L){});
var J=[];
var I=q.retrieveSectionWiseCustomFieldInfo("parcel");
r.parcelDbType={};
I.then(function(O){r.generaParcelTemplate=O.genralSection;
var M=[];
var L=Object.keys(H).map(function(P,Q){angular.forEach(this[P],function(R){if(P==="Parcel#P#"){M.push({Parcel:R})
}})
},H);
r.generaParcelTemplate=q.retrieveCustomData(r.generaParcelTemplate,M);
angular.forEach(r.generaParcelTemplate,function(P){if(P.model){J.push(P.model)
}});
var N=[];
N.push(z.value);
r.mapToSent.parcelObjectId=N;
r.mapToSent.parcelDbFieldName=J;
r.mapToSent.invoiceDbFieldName=D
},function(L){},function(L){});
r.categoryCustom=q.resetSection(r.generaParcelTemplate);
var K=q.retrieveSectionWiseCustomFieldInfo("lot");
if(r.lotDbType!=null){}else{r.lotDbType={}
}K.then(function(Q){var O=[];
r.generalLotTemplate=Q.genralSection;
var R=[];
var L=Object.keys(H).map(function(S,T){angular.forEach(this[S],function(U){if(S==="Lot"){R.push({Parcel:U})
}})
},H);
r.generalLotTemplate=q.retrieveCustomData(r.generalLotTemplate,R);
r.fieldNotConfigured=false;
for(var M in r.generalLotTemplate){var N=r.generalLotTemplate[M];
if(N.model=="issued_to_franchise$UMS$String"){r.generalLotTemplate.splice(M,1);
continue
}if(N.model==="carat_of_lot$NM$Double"){N.required=true
}else{if(N.model==="allot_to_lot$UMS$String"){N.required=true
}else{if(N.model==="in_stock_of_lot$UMS$String"){N.required=true
}else{if(N.model==="due_date_of_lot$DT$Date"){N.required=true
}}}}g[N.fieldId]=N.model;
O.push(N.model)
}r.mandatoryFields=["carat_of_lot$NM$Double","due_date_of_lot$DT$Date","no_of_pieces_of_lot$NM$Long"];
r.mapToSent.lotDbFieldName=O;
if(O.length>0&&r.mandatoryFields!=null&&r.mandatoryFields.length>0){for(var P=0;
P<r.mandatoryFields.length;
P++){if(O.indexOf(r.mandatoryFields[P])===-1){r.fieldNotConfigured=true;
break
}}}else{r.fieldNotConfigured=true
}angular.forEach(r.generalLotTemplate,function(S){r.modelAndHeaderListForLot.push({model:S.model,header:S.label});
if(S.fromModel){r.lotListForUIGrid.push({name:S.fromModel,displayName:S.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.addedLotScreenRule(row, '"+S.fromModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'})
}else{if(S.toModel){r.lotListForUIGrid.push({name:S.toModel,displayName:S.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.addedLotScreenRule(row, '"+S.toModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'})
}else{if(S.model){r.lotListForUIGrid.push({name:S.model,displayName:S.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.addedLotScreenRule(row, '"+S.model+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'})
}}}});
r.lotDataBean.lotDbType=r.lotDbType;
r.mapToSent.ruleConfigMap={fieldIdNameMap:g,featureName:"addLot"};
m.retrieveLotByParcelId(r.mapToSent,function(S){if(S!==undefined){if(S[0]!==undefined){if(S[0].custom3!==null&&S[0].custom3!==undefined){r.invoiceCustom=angular.copy(S[0].custom3)
}else{r.invoiceCustom={}
}if(S[0].custom1!==null&&S[0].custom1!==undefined){r.parcelCustom=angular.copy(S[0].custom1)
}else{r.parcelCustom={}
}}r.lotInDb=angular.copy(S)
}else{}r.flag.customFieldGenerated=true;
var T=function(U){angular.forEach(U,function(V){if(V.categoryCustom!==undefined&&V.categoryCustom!==null){angular.forEach(r.lotListForUIGrid,function(W){if(!V.categoryCustom.hasOwnProperty(W.name)){V.categoryCustom[W.name]="NA"
}else{if(V.categoryCustom.hasOwnProperty(W.name)){if(V.categoryCustom[W.name]===null||V.categoryCustom[W.name]===""||V.categoryCustom[W.name]===undefined){V.categoryCustom[W.name]="NA"
}}}V.objectIdOfParcel={id:V.value}
});
V.categoryCustom.$$lotId$$=V.value;
V.categoryCustom.screenRuleDetailsWithDbFieldName=V.screenRuleDetailsWithDbFieldName;
r.lotDataForUiGrid.push(V.categoryCustom)
}});
r.gridOptions.data=r.lotDataForUiGrid;
r.gridOptions.columnDefs=r.lotListForUIGrid;
if(r.editLotFeature){r.gridOptions.columnDefs.push({name:"Action",cellTemplate:'<div class="ui-grid-cell-contents"> <a ng-click="grid.appScope.updateLot(row.entity)">Update</a></div>',enableFiltering:false,minWidth:200})
}r.countRemainingCarat();
window.setTimeout(function(){$(window).resize();
$(window).resize();
l.unMaskLoading()
},100);
r.listFilled=true
};
q.convertorForCustomField(S,T,false)
},function(){l.unMaskLoading();
var T="Could not retrieve lot, please try again.";
var S=l.error;
l.addMessage(T,S)
})
},function(L){},function(L){})
},function(){l.unMaskLoading();
var I="Failed to retrieve data";
var H=l.error;
l.addMessage(I,H)
});
r.gridOptionsSubLot={enableRowSelection:true,enableSelectAll:true,enableFiltering:true,multiSelect:true,data:[],columnDefs:[],onRegisterApi:function(H){n=H;
H.selection.on.rowSelectionChanged(r,function(I){C(H.selection.getSelectedRows())
});
H.selection.on.rowSelectionChangedBatch(r,function(I){C(H.selection.getSelectedRows())
})
}};
var C=function(I){d=[];
w=0;
j=0;
for(var H in I){d.push(I[H].id);
if(!!I[H][c.carat]){w+=I[H][c.carat]
}if(!!I[H][c.pieces]){j+=I[H][c.pieces]
}}r.categoryCustom["carat_of_lot$NM$Double"]=w;
r.categoryCustom["no_of_pieces_of_lot$NM$Long"]=j
};
var B={};
t.retrieveDesignationBasedFieldsBySection(["lotAdd","ASL"],function(H){if(H.SubLot==null||H.SubLot.length==0){r.noFieldConfiguredForSubLot=true
}r.sublotDataBean={};
var I=q.retrieveSectionWiseCustomFieldInfo("subLotEntity");
if(!(r.dbTypeForUpdate)){r.dbTypeForUpdate={}
}I.then(function(O){B.template=null;
B.template=O.genralSection;
r.listOfModelsOfDateType=[];
var K=[];
Object.keys(H).map(function(P,Q){angular.forEach(this[P],function(R){if(P==="SubLot"){K.push({Sublot:R})
}})
},H);
r.editFieldName=[];
r.fieldIdNameMap={};
B.template=q.retrieveCustomData(B.template,K);
angular.forEach(B.template,function(P){if(P.fromModel){r.fieldIdNameMap[P.fieldId]=P.fromModel;
r.gridOptionsSubLot.columnDefs.push({name:P.fromModel,displayName:P.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.addSublotScreenRule(row, '"+P.fromModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
if(r.editFieldName.indexOf(P.fromModel)===-1){r.editFieldName.push(P.fromModel)
}}else{if(P.toModel){r.fieldIdNameMap[P.fieldId]=P.toModel;
r.gridOptionsSubLot.columnDefs.push({name:P.toModel,displayName:P.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.addSublotScreenRule(row, '"+P.toModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
if(r.editFieldName.indexOf(P.toModel)===-1){r.editFieldName.push(P.toModel)
}}else{if(P.model){r.fieldIdNameMap[P.fieldId]=P.model;
r.gridOptionsSubLot.columnDefs.push({name:P.model,displayName:P.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.addSublotScreenRule(row, '"+P.model+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
if(r.editFieldName.indexOf(P.model)===-1){r.editFieldName.push(P.model)
}}}}});
r.fieldNotConfigured=false;
r.showConfigMsg=false;
var J=[];
if(B.template!==undefined&&B.template!==null&&B.template.length>0){for(var L=0;
L<B.template.length;
L++){J.push(angular.copy(B.template[L].model))
}}if(J.length>0&&r.mandatorySublotFields&&r.mandatorySublotFields.length>0){for(var N=0;
N<r.mandatorySublotFields.length;
N++){if(J.indexOf(r.mandatorySublotFields[N])===-1){r.fieldNotConfigured=true;
break
}}}else{r.fieldNotConfigured=false
}if(r.fieldNotConfigured){r.showConfigMsg=true
}r.map={};
r.map.dbFieldNames=r.editFieldName;
if(r.editFieldName.length>0){var M={parcelId:r.parcelId,map:r.map,ruleConfigMap:{fieldIdNameMap:r.fieldIdNameMap,featureName:"addLot"},excludeSubLotWithAssociatedLot:true};
f.retrieveSublotsbyParcel(M,function(Q){var P;
var S=function(U){P=angular.copy(U);
for(var V in P){var T={};
P[V].categoryCustom.$$subLotId$$=P[V].value;
P[V].categoryCustom.screenRuleDetailsWithDbFieldName=P[V].screenRuleDetailsWithDbFieldName;
angular.extend(T,P[V].categoryCustom,{id:P[V].value});
r.gridOptionsSubLot.data.push(T)
}};
var R=angular.copy(Q);
if(R!==undefined){q.convertorForCustomField(R,S)
}l.unMaskLoading()
},function(){var Q="Could not save, please try again.";
var P=l.error;
l.addMessage(Q,P);
l.unMaskLoading()
})
}},function(J){},function(J){})
},function(){l.unMaskLoading();
var I="Failed to retrieve data";
var H=l.error;
l.addMessage(I,H)
})
}}},function(B){l.unMaskLoading();
var D="Failed to retrieve pre rule.";
var C=l.error;
l.addMessage(D,C)
})
};
r.reset=function(B,z){if(B==="parcelCustom"){r.parcelCustom={};
var A=q.retrieveSearchWiseCustomFieldInfo("lotAdd");
r.dbType={};
A.then(function(C){r.generalSearchTemplate=C.genralSection;
r.searchResetFlag=true;
l.unMaskLoading();
r.flag.customFieldGenerated=true
},function(C){},function(C){})
}else{if(B==="categoryCustom"){r.franchiseCode=null;
r.franchiseCodeYear=null;
if(!z){r.categoryCustom={}
}else{angular.forEach(r.fieldsToClear,function(C){delete r.categoryCustom[C]
})
}var A=q.retrieveSectionWiseCustomFieldInfo("lot");
A.then(function(F){var G=[];
var C=Object.keys(r.response).map(function(H,I){angular.forEach(this[H],function(J){if(H==="Lot"){G.push({Lot:J})
}})
},r.response);
r.generalLotTemplate=F.genralSection;
r.generalLotTemplate=q.retrieveCustomData(r.generalLotTemplate,G);
for(var D in r.generalLotTemplate){var E=r.generalLotTemplate[D];
if(E.model=="issued_to_franchise$UMS$String"){r.generalLotTemplate.splice(D,1);
continue
}if(E.model==="carat_of_lot$NM$Double"){E.required=true
}else{if(E.model==="no_of_pieces_of_lot$NM$Long"){E.required=true
}else{if(E.model==="due_date_of_lot$DT$Date"){E.required=true
}else{if(E.model==="in_stock_of_lot$UMS$String"){E.required=true
}}}}}y(function(){r.AddLot=this;
this.lotAddFlag=true
},100);
l.unMaskLoading();
r.flag.customFieldGenerated=true;
r.lotDataBean.lotDbType=r.lotDbType
},function(C){},function(C){})
}}};
r.resetAddForm=function(z){r.submitted=false;
if(r.addLotForm!=null){r.addLotForm.$dirty=false
}r.flag.editLot=false;
r.AddLot=this;
this.lotAddFlag=false;
r.reset("categoryCustom",z)
};
r.onCanelOfSearch=function(z){if(r.addLotForm!=null){r.addLotForm.$dirty=false
}r.searchedData=[];
r.searchCustom={};
r.listFilled=false;
r.searchResetFlag=false;
r.selectedParcel=[];
r.gridOptionsForParcel={};
r.reset("parcelCustom")
};
r.createLot=function(A){r.submitted=true;
r.lotDbTypeToSent=angular.copy(r.lotDbType);
if(A.$valid&&r.checkAddLotFormExtraValidationIfSubLot()&&r.checkAddLotFormExtraValidationWihoutSublot()){var z=r.sequenceNumberExists(r.categoryCustom["lotID$AG$String"]);
z.then(function(){var B={featureName:"addLot",entityId:null,entityType:"lot",currentFieldValueMap:r.categoryCustom,dbType:r.lotDbType,otherEntitysIdMap:{invoiceId:r.invoiceIdForConstraint,parcelId:r.parcelIdForConstraint}};
r.submitted=false;
console.log("post rule dataToSend::::"+JSON.stringify(B));
u.executePostRule(B,function(D){console.log("post rule res::::"+JSON.stringify(D));
if(!!D.validationMessage){var E=l.warning;
l.addMessage(D.validationMessage,E)
}else{var G=[];
G.push({categoryCustom:angular.copy(r.categoryCustom)});
var F={};
F.lotCustom=r.categoryCustom;
F.lotDbType=angular.copy(r.lotDbType);
F.lotDbType["lotID$AG$String"]="String";
F.hasPacket=false;
F.subLots=d;
F.invoiceDataBean={id:r.invoiceId};
F.parcelDataBean={id:r.parcelId};
F.sequenceNumber=r.categoryCustom["lotID$AG$String"];
F.ruleConfigMap=r.mapToSent.ruleConfigMap;
F["issued_to_franchise$UMS$String"]=r.franchiseId;
for(var C=0;
C<r.franchiseList.length;
C++){if(r.franchiseList[C].description===r.franchiseCode){F.franchise=r.franchiseList[C].value;
break
}}r.categoryCustom["lotID$AG$String"]=r.currentYear.concat(r.franchiseCode).concat(r.categoryCustom["lotID$AG$String"]);
m.create(F,function(H){if(H!==null){r.listFilled=false;
r.AddLot=this;
this.lotAddFlag=false;
r.addLotForm.$dirty=false;
var M=function(N){r.selectedParcel[0].stockCarat=Number(r.selectedParcel[0].stockCarat)-Number(F.lotCustom["carat_of_lot$NM$Double"]);
r.selectedParcel[0].stockPieces=Number(r.selectedParcel[0].stockPieces)-Number(F.lotCustom["no_of_pieces_of_lot$NM$Long"]);
for(var O in H){if(O==="lotId"){N[0].categoryCustom["$$lotId$$"]=O;
break
}}angular.forEach(r.lotListForUIGrid,function(P){if(!N[0].categoryCustom.hasOwnProperty(P.name)){N[0].categoryCustom[P.name]="NA"
}else{if(N[0].categoryCustom.hasOwnProperty(P.name)){if(N[0].categoryCustom[P.name]===null||N[0].categoryCustom[P.name]===""||N[0].categoryCustom[P.name]===undefined||N[0].categoryCustom[P.name]==="undefined"){N[0].categoryCustom[P.name]="NA"
}}}});
N[0].categoryCustom["lotID$AG$String"]=H[H.lotId];
N[0].categoryCustom.$$lotId$$=H.lotId;
N[0].categoryCustom.screenRuleDetailsWithDbFieldName=H.screenRuleDetailsWithDbFieldName;
r.gridOptions.data.push(angular.copy(N[0].categoryCustom));
if(r.editLotFeature){r.gridOptions.columnDefs.push({name:"Action",cellTemplate:'<div class="ui-grid-cell-contents"> <a ng-click="grid.appScope.updateLot(row.entity)">Update</i></a></div>',enableFiltering:false,minWidth:200})
}r.countRemainingCarat();
r.listFilled=true
};
q.convertorForCustomField(G,M);
r.resetAddForm(false);
if(n!=null){var K=n.selection.getSelectedRows();
for(var J in K){r.gridOptionsSubLot.data.splice(r.gridOptionsSubLot.data.indexOf(K[J]),1)
}}}else{l.unMaskLoading();
var L="Could not create lot, please try again.";
var I=l.error;
l.addMessage(L,I)
}},function(){l.unMaskLoading();
var I="Could not create lot, please try again.";
var H=l.error;
l.addMessage(I,H)
})
}},function(C){l.unMaskLoading();
var E="Failed to authenticate post rule.";
var D=l.error;
l.addMessage(E,D)
})
})
}};
r.checkAddLotFormExtraValidationIfSubLot=function(){if(d.length>0){if(w!=Number(r.categoryCustom["carat_of_lot$NM$Double"])||j!=Number(r.categoryCustom["no_of_pieces_of_lot$NM$Long"])){l.addMessage("Carat and pieces value should be equal to selected sub lot total carat and pieces",l.error);
return false
}else{return true
}}else{return true
}};
r.checkAddLotFormExtraValidationWihoutSublot=function(){if(d.length<=0){if(Number(r.selectedParcel[0].stockCarat)<Number(r.categoryCustom["carat_of_lot$NM$Double"])||Number(r.selectedParcel[0].stockPieces)<Number(r.categoryCustom["no_of_pieces_of_lot$NM$Long"])){l.addMessage("Carat or pieces values should not be greater than stock carat and stock pieces",l.error);
return false
}else{return true
}}else{return true
}};
r.editLotLocally=function(A){r.index=A.$$objectId;
r.oldObj=angular.copy(A,r.oldObj);
if(A!=null){r.flag.editLot=true;
r.lotDataBean={};
r.AddLot=this;
this.lotAddFlag=false;
if(!!(r.lotListToSave&&r.lotListToSave.length>0)){for(var z=0;
z<r.lotListToSave.length;
z++){if(A.$$objectId!=null&&r.lotListToSave[z].$$objectId===A.$$objectId){r.categoryCustom=angular.copy(r.lotListToSave[z].categoryCustom);
r.lotDataBean.lotCustom=r.categoryCustom;
break
}}}r.lotDataBean.lotDbType=r.lotDbType
}y(function(){r.AddLot=this;
this.lotAddFlag=true
},100)
};
r.saveLot=function(B){r.submitted=true;
if(B.$valid){var z=[];
z.push({categoryCustom:angular.copy(r.categoryCustom)});
angular.forEach(r.lotListToSave,function(C){if(C.$$objectId===r.index){C.categoryCustom=(r.categoryCustom)
}});
var A=function(C){var F=angular.copy(C[0].categoryCustom);
if(F!==undefined&&F!==null){angular.forEach(r.lotListForUIGrid,function(G){if(!F.hasOwnProperty(G.name)){F[G.name]="NA"
}else{if(F.hasOwnProperty(G.name)){console.log(F[G.name]);
if(F[G.name]===null||F[G.name]===""||F[G.name]===undefined||F[G.name]==="undefined"){F[G.name]="NA"
}}}})
}for(var D=0;
D<r.gridOptions.data.length;
D++){var E=r.gridOptions.data[D];
if(E.$$objectId===r.index){r.gridOptions.data[D]=angular.copy(F);
r.gridOptions.data[D].$$objectId=r.index;
break
}}r.countRemainingCarat()
};
q.convertorForCustomField(z,A,false);
r.categoryCustom=q.resetSection(r.generalLotTemplate);
r.listFilled=true;
r.flag.editLot=false;
r.resetAddForm()
}};
r.showPopUp=function(z){r.lotObjectToDelete=z;
r.index=z.$$objectId;
$("#deleteDialog").modal("show")
};
r.hidePopUp=function(){$("#deleteDialog").modal("hide");
l.removeModalOpenCssAfterModalHide()
};
r.deleteLot=function(){for(var z=0;
z<r.gridOptions.data.length;
z++){var A=r.gridOptions.data[z];
if(A.$$objectId===r.index){r.gridOptions.data.splice(z,1);
break
}}for(var z=0;
z<r.lotListToSave.length;
z++){var A=r.lotListToSave[z];
if(A.$$objectId===r.index){r.lotListToSave.splice(z,1);
break
}}r.categoryCustom=q.resetSection(r.generalLotTemplate);
r.flag.editLot=false;
$("#deleteDialog").modal("hide");
l.removeModalOpenCssAfterModalHide()
};
r.onCancel=function(z){if(z!=null){r.flag.showAddPage=false;
r.lotListToSave=[];
r.modifiedLotListToDisplay=[];
r.submitted=false;
r.selectedParcel=[];
r.flag.lotsAlreadyCreated=false;
r.reset("categoryCustom")
}};
r.saveAllLot=function(E){r.caratDoesNotMatch=false;
var A=[];
var C={};
var B=undefined;
angular.forEach(r.lotListToSave,function(F){if(!r.caratDoesNotMatch&&F.categoryCustom["carat_of_lot$NM$Double"]!==undefined&&F.categoryCustom["carat_of_lot$NM$Double"]!==null){if(B!==undefined){B=B+parseFloat(F.categoryCustom["carat_of_lot$NM$Double"])
}else{B=parseFloat(F.categoryCustom["carat_of_lot$NM$Double"])
}}else{r.caratDoesNotMatch=true
}A.push({lotCustom:F.categoryCustom,invoiceDataBean:{id:r.invoiceId},lotDbType:r.lotDbTypeToSent,parcelDataBean:{id:r.parcelId}})
});
if(!r.caratDoesNotMatch){if(r.caratOfParcel!==B){r.caratDoesNotMatch=true
}}C.lotList=A;
C.invoiceDataBean={id:r.invoiceId};
C.parcelDataBean={id:r.parcelId};
if(!r.caratDoesNotMatch){l.maskLoading();
m.create(C,function(F){r.lotListToSave=[];
r.lotListTodisplay=[];
r.modifiedLotListToDisplay=[];
r.onCancel();
r.onCanelOfSearch();
r.searchedData=[];
r.searchedDataFromDb=[];
r.listFilled=false;
r.addLotForm.$dirty=false;
r.flag.showAddPage=false;
l.unMaskLoading()
},function(){l.unMaskLoading();
var G="Could not create lot, please try again.";
var F=l.error;
l.addMessage(G,F)
})
}else{var D="Carat value does not match, please try again.";
var z=l.error;
l.addMessage(D,z)
}};
r.updateLot=function(z){console.log(JSON.stringify(z));
z.value=angular.copy(z["$$lotId$$"]);
if(!!(z)){l.unMaskLoading();
r.flag.showUpdatePage=true;
r.addLotForm.$dirty=false;
k.path("/editlot");
l.editLot=true;
l.lotByParcel=angular.copy(z)
}};
r.countRemainingCarat=function(){if(r.gridOptions.data!==undefined&&r.gridOptions.data!==null){var A=0;
angular.forEach(r.gridOptions.data,function(B){if(B["carat_of_lot$NM$Double"]!==undefined&&B["carat_of_lot$NM$Double"]!==null){A+=parseFloat(B["carat_of_lot$NM$Double"])
}});
r.remainingCarat=parseFloat(r.caratOfParcel)-parseFloat(A);
if(r.remainingCarat<0){r.remCaratMsg="Carat value limit exceeded"
}else{r.remCaratMsg="Carat remaining is "+r.remainingCarat
}var z=0;
angular.forEach(r.gridOptions.data,function(B){if(B["no_of_pieces_of_lot$NM$Long"]!==undefined&&B["no_of_pieces_of_lot$NM$Long"]!==null){z+=parseFloat(B["no_of_pieces_of_lot$NM$Long"])
}})
}};
r.sequenceNumberExists=function(A,D){var z=x.defer();
if(A!==undefined&&A!==null){for(var B=0;
B<r.franchiseList.length;
B++){if(r.franchiseList[B].description===r.franchiseCode){r.franchiseId=r.franchiseList[B].value;
var C={franchise:r.franchiseId,seqNumber:A};
m.isLotSequenceNumberExist(C,function(E){if(E.data==true){var G="Lot already exists for the same id.";
var F=l.error;
l.addMessage(G,F);
z.reject()
}else{z.resolve()
}},function(){l.unMaskLoading();
var F="Could not retrieve sequence number, please try again.";
var E=l.error;
l.addMessage(F,E);
z.reject()
});
break
}}}return z.promise
};
r.startItemChanged=function(B,A){r.franchiseCode=B;
r.franchiseCodeYear=r.currentYear+B;
if(B!==undefined&&B!==null){for(var z=0;
z<r.franchiseList.length;
z++){if(r.franchiseList[z].description===r.franchiseCode){r.franchiseId=r.franchiseList[z].value;
m.getnextlotsequence(r.franchiseId,function(C){r.categoryCustom["lotID$AG$String"]=C.sequenceNumber
},function(){l.unMaskLoading();
var D="Could not retrieve sequence number, please try again.";
var C=l.error;
l.addMessage(D,C)
});
break
}}}else{r.categoryCustom["lotID$AG$String"]=null
}};
r.hasEditLotPermission=function(z){r.editLotFeature=z
}
}])
});