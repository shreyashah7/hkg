define(["hkg","packetService","splitstockService","customFieldService","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicForm","accordionCollapse","ruleExecutionService"],function(a,b){a.register.controller("SplitPacketController",["$rootScope","$scope","PacketService","$timeout","$filter","$location","$window","CustomFieldService","DynamicFormService","RuleExecutionService","SplitStockService",function(l,p,g,e,i,f,d,o,n,j,m){l.mainMenu="stockLink";
l.childMenu="splitPacket";
l.activateMenu();
p.Packet=this;
var k={};
var c;
var h={};
p.initializeData=function(q){if(q){l.maskLoading();
p.searchedData=[];
p.searchedDataFromDb=[];
p.listFilled=false;
p.lotListTodisplay=[];
p.searchCustom={};
var r=n.retrieveSearchWiseCustomFieldInfo("packetsplit");
p.flag={};
p.flag.searchFieldNotAvailable=false;
p.dbType={};
r.then(function(w){p.searchInvoiceTemplate=[];
p.searchParcelTemplate=[];
p.searchLotTemplate=[];
p.searchPacketTemplate=[];
p.fieldIdNameMap={};
var t=[];
var v=[];
var u=[];
var s=[];
p.generalSearchTemplate=w.genralSection;
p.labelListForUiGrid=[];
if(p.generalSearchTemplate!=null&&p.generalSearchTemplate.length>0){k={};
angular.forEach(p.generalSearchTemplate,function(x){var y=angular.copy(x);
if(y.featureName.toLowerCase()==="invoice"){p.searchInvoiceTemplate.push(angular.copy(y));
t.push(angular.copy(y.model))
}else{if(y.featureName.toLowerCase()==="parcel"){p.searchParcelTemplate.push(angular.copy(y));
v.push(angular.copy(y.model))
}else{if(y.featureName.toLowerCase()==="lot"){p.searchLotTemplate.push(angular.copy(y));
u.push(angular.copy(y.model))
}else{if(y.featureName.toLowerCase()==="packet"){p.searchPacketTemplate.push(angular.copy(y));
s.push(angular.copy(y.model))
}}}}k[y.model]=y.featureName;
if(y.fromModel){p.labelListForUiGrid.push({name:y.fromModel,displayName:y.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.editPacketScreenRule(row, '"+x.fromModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
p.fieldIdNameMap[y.fieldId]=y.fromModel
}else{if(y.toModel){p.labelListForUiGrid.push({name:y.toModel,displayName:y.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.editPacketScreenRule(row, '"+x.toModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
p.fieldIdNameMap[y.fieldId]=y.toModel
}else{if(y.model){p.labelListForUiGrid.push({name:y.model,displayName:y.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.editPacketScreenRule(row, '"+x.model+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
p.fieldIdNameMap[y.fieldId]=y.model
}}}});
if(t.length>0){h.invoice=t
}if(v.length>0){h.parcel=v
}if(u.length>0){h.lot=u
}if(s.length>0){h.packet=s
}}else{p.flag.searchFieldNotAvailable=true;
p.dataRetrieved=true
}p.searchResetFlag=true;
p.dataRetrieved=true;
l.unMaskLoading()
},function(s){},function(s){})
}};
p.initializeData(true);
p.editPacketScreenRule=function(s,r){var q;
if(!!s.entity.screenRuleDetailsWithDbFieldName&&s.entity.screenRuleDetailsWithDbFieldName[r]!==undefined&&s.entity.screenRuleDetailsWithDbFieldName[r]!==null){if(p.selectedStock.length===0||(p.selectedStock[0].$$packetId$$!==s.entity.$$packetId$$)){q=s.entity.screenRuleDetailsWithDbFieldName[r].colorCode
}}return q
};
p.retrieveSearchedData=function(){p.selectOneParameter=false;
p.searchedData=[];
p.listFilled=false;
if(Object.getOwnPropertyNames(p.searchCustom).length>0){var t=false;
for(var w in p.searchCustom){if(typeof p.searchCustom[w]==="object"&&p.searchCustom[w]!=null){var u=angular.copy(p.searchCustom[w].toString());
if(typeof u==="string"&&u!==null&&u!==undefined&&u.length>0){t=true;
break
}}if(typeof p.searchCustom[w]==="string"&&p.searchCustom[w]!==null&&p.searchCustom[w]!==undefined&&p.searchCustom[w].length>0){t=true;
break
}if(typeof p.searchCustom[w]==="number"&&!!(p.searchCustom[w])){t=true;
break
}if(typeof p.searchCustom[w]==="boolean"){t=true;
break
}}if(t){l.maskLoading();
p.packetDataBean={};
p.packetDataBean.featureCustomMapValue={};
var s={};
var q=n.convertSearchData(p.searchInvoiceTemplate,p.searchParcelTemplate,p.searchLotTemplate,p.searchPacketTemplate,p.searchCustom);
p.searchCustom=angular.copy(q);
angular.forEach(k,function(B,z){var A=p.searchCustom[z];
if(A!==undefined){var y={};
if(!s[B]){y[z]=A;
s[B]=y
}else{var x=s[B];
x[z]=A;
s[B]=x
}}else{A=p.searchCustom["to"+z];
if(A!==undefined){var y={};
if(!s[B]){y["to"+z]=A;
s[B]=y
}else{var x=s[B];
x["to"+z]=A;
s[B]=x
}}A=p.searchCustom["from"+z];
if(A!==undefined){var y={};
if(!s[B]){y["from"+z]=A;
s[B]=y
}else{var x=s[B];
x["from"+z]=A;
s[B]=x
}}}});
p.packetDataBean.featureCustomMapValue=s;
p.packetDataBean.hasPacket=false;
p.packetDataBean.featureDbFieldMap=h;
p.packetDataBean.ruleConfigMap={fieldIdNameMap:p.fieldIdNameMap,featureName:"editPacket"};
p.packetDataBean.isInStockOfLoggedInUser=true;
if(p.packetDataBean.featureCustomMapValue.packet!==undefined&&p.packetDataBean.featureCustomMapValue.packet!==null){delete p.packetDataBean.featureCustomMapValue.packet.in_stock_of_packet$UMS$String
}g.search(p.packetDataBean,function(x){p.searchedDataFromDb=angular.copy(x);
p.issuedStock={};
p.issuedStock.enableFiltering=true;
p.issuedStock.multiSelect=false;
p.issuedStock.enableRowSelection=true;
p.selectedStock=[];
p.issuedStock.onRegisterApi=function(z){p.gridApi=z;
z.selection.on.rowSelectionChanged(p,function(A){if(p.selectedStock.length>0){$.each(p.selectedStock,function(C,B){if(B["$$hashKey"]===A.entity["$$hashKey"]){p.flag.repeatedRow=true;
p.selectedStock.splice(C,1);
return false
}else{p.flag.repeatedRow=false
}});
if(!p.flag.repeatedRow){p.selectedStock.push(A.entity)
}}else{p.selectedStock.push(A.entity)
}if(p.selectedStock.length>0){p.flag.rowSelectedflag=true
}else{p.flag.rowSelectedflag=false
}});
z.selection.on.rowSelectionChangedBatch(p,function(A){if(p.selectedStock.length>0){angular.forEach(A,function(B){$.each(p.selectedStock,function(D,C){if(C["$$hashKey"]===B.entity["$$hashKey"]){p.flag.repeatedRow=true;
p.selectedStock=[];
return false
}else{p.flag.repeatedRow=false
}});
if(!p.flag.repeatedRow){p.selectedStock.push(B.entity)
}})
}else{angular.forEach(A,function(B){p.selectedStock.push(B.entity)
})
}if(p.selectedStock.length>0){p.flag.rowSelectedflag=true
}else{p.flag.rowSelectedflag=false
}})
};
p.issuedStockList=[];
var y=function(z){p.searchedData=angular.copy(x);
angular.forEach(p.searchedData,function(A){angular.forEach(p.labelListForUiGrid,function(B){if(A.custom1!=null&&!A.custom1.hasOwnProperty(B.name)){A.custom1[B.name]="NA"
}else{if(A.custom1!=null&&A.custom1.hasOwnProperty(B.name)){if(!(!!(A.custom1[B.name]))){A.custom1[B.name]="NA"
}}}});
if(A.categoryCustom!=null){A.categoryCustom.packetIdForConstraint=A.value;
A.categoryCustom.screenRuleDetailsWithDbFieldName=A.screenRuleDetailsWithDbFieldName;
p.issuedStockList.push(A.categoryCustom)
}});
l.unMaskLoading();
p.issuedStock.data=p.issuedStockList;
p.issuedStock.columnDefs=p.labelListForUiGrid
};
n.convertorForCustomField(x,y);
window.setTimeout(function(){$(window).resize();
$(window).resize()
},200);
p.listFilled=true;
p.issueListFilled=true;
p.dataRetrieved=true
},function(){var y="Could not retrieve, please try again.";
var x=l.error;
l.addMessage(y,x);
l.unMaskLoading()
})
}else{var v="Please select atleast one search criteria for search";
var r=l.warning;
l.addMessage(v,r)
}}else{p.issuedStock={};
p.issuedStock.data=[];
p.dataRetrieved=true;
p.listFilled=true;
var v="Please select atleast one search criteria for search";
var r=l.warning;
l.addMessage(v,r)
}};
p.initEditPacketForm=function(q){p.editPacketForm=q
};
p.reset=function(r){if(r==="searchCustom"){p.searchCustom={};
var q=n.retrieveSearchWiseCustomFieldInfo("packetsplit");
p.dbType={};
q.then(function(s){p.generalSearchTemplate=s.genralSection;
p.searchResetFlag=true;
l.unMaskLoading();
p.flag.customFieldGenerated=true
},function(s){},function(s){})
}else{if(r==="packetCustom"){if(p.response!==undefined&&p.response!==null){angular.forEach(p.packetCustom,function(t,s){if(!s!=="no_of_pieces_of_packet$NM$Long"){delete p.packetCustom[s]
}});
var q=n.retrieveSectionWiseCustomFieldInfo("packet");
q.then(function(w){var t=[];
var s=Object.keys(p.response).map(function(x,y){angular.forEach(this[x],function(z){if(x!=="Packet#P#"){t.push({Packet:z})
}})
},p.response);
p.updatePacketTemplate=null;
p.updatePacketTemplate=w.genralSection;
p.updatePacketTemplate=n.retrieveCustomData(p.updatePacketTemplate,t);
p.finalUpdatePacketTemplate=[];
if(p.updatePacketTemplate!==undefined&&p.updatePacketTemplate!==null&&p.updatePacketTemplate.length>0){var v=0;
for(var u=0;
u<p.updatePacketTemplate.length;
u++){if(v<=3){if(p.updatePacketTemplate[u].model==="no_of_pieces_of_packet$NM$Long"){p.updatePacketTemplate[u].required=true;
p.finalUpdatePacketTemplate.push(p.updatePacketTemplate[u]);
v++
}else{if(p.updatePacketTemplate[u].model==="carat_of_packet$NM$Double"){p.updatePacketTemplate[u].required=true;
p.finalUpdatePacketTemplate.push(p.updatePacketTemplate[u]);
v++
}else{if(p.updatePacketTemplate[u].model==="isCheck"){p.finalUpdatePacketTemplate.push(p.updatePacketTemplate[u]);
v++
}}}}else{break
}}}e(function(){p.Packet=this;
this.packetSplitFlag=true
},100);
p.lotEditShow=true;
l.unMaskLoading();
p.flag.customFieldGenerated=true
},function(s){},function(s){})
}}}};
p.onCanel=function(){if(p.editPacketForm!=null){p.editPacketForm.$dirty=false
}p.selectedStock=[];
p.flag.showAddPage=false;
p.flag.showUpdatePage=false;
p.submitted=false;
p.reset("packetCustom");
p.flag.showUpdatePage=false;
p.flag.rowSelectedflag=false
};
p.onCanelOfSearch=function(q){if(p.editPacketForm!=null){p.editPacketForm.$dirty=false
}p.searchedData=[];
p.searchResetFlag=false;
p.listFilled=false;
p.reset("searchCustom")
};
p.addPacket=function(){p.reset("packetCustom");
p.Packet=this;
this.packetSplitFlag=false;
p.flag.showAddPage=false;
if((p.selectedStock!==undefined&&p.selectedStock!==null&&p.selectedStock.length>0)||(l.editPacket)){l.maskLoading();
if(p.searchedDataFromDb!==undefined&&p.searchedDataFromDb!==null){for(var q=0;
q<p.searchedDataFromDb.length;
q++){if(p.searchedDataFromDb[q].value===p.selectedStock[0].packetIdForConstraint){p.invoiceIdForConstraint=p.searchedDataFromDb[q].id;
p.parcelIdForConstraint=p.searchedDataFromDb[q].description;
p.lotIdForConstraint=p.searchedDataFromDb[q].label;
p.packetIdForConstraint=angular.copy(p.searchedDataFromDb[q].value);
break
}}}p.preRuleSatisfied=false;
var r={featureName:"editPacket",entityId:p.packetIdForConstraint,entityType:"packet"};
j.executePreRule(r,function(t){if(!!t.validationMessage){p.preRuleSatisfied=true;
var u=l.warning;
l.addMessage(t.validationMessage,u);
l.unMaskLoading()
}else{p.count=0;
p.flag.showUpdatePage=true;
p.gridOptions={};
p.gridOptions.enableFiltering=true;
p.gridOptions.data=[];
p.packetLabelForUIGrid=[];
p.flag.showAddPage=true;
var v={};
var s=[];
p.packetDataBean={};
p.packetListToSave=[];
p.parcelDataBean={invoiceDataBean:{invoiceCustom:"",invoiceDbType:""}};
o.retrieveDesignationBasedFields("packetsplit",function(w){p.response=angular.copy(w);
var B=n.retrieveSectionWiseCustomFieldInfo("invoice");
p.invoiceDbType={};
B.then(function(D){p.updateInvoiceTemplate=D.genralSection;
p.updateInvoiceTemplate=n.retrieveCustomData(p.updateInvoiceTemplate,w);
var C=[];
if(p.updateInvoiceTemplate!=null&&p.updateInvoiceTemplate.length>0){angular.forEach(p.updateInvoiceTemplate,function(E){if(E.model){C.push(E.model)
}else{if(E.fromModel){C.push(E.fromModel)
}else{if(E.toModel){C.push(E.toModel)
}}}});
v.invoice=C
}},function(C){},function(C){});
var z=n.retrieveSectionWiseCustomFieldInfo("parcel");
p.parcelDbType={};
z.then(function(D){p.updateParcelTemplate=D.genralSection;
p.updateParcelTemplate=n.retrieveCustomData(p.updateParcelTemplate,w);
var C=[];
if(p.updateParcelTemplate!=null&&p.updateParcelTemplate.length>0){angular.forEach(p.updateParcelTemplate,function(E){if(E.model){C.push(E.model)
}else{if(E.fromModel){C.push(E.fromModel)
}else{if(E.toModel){C.push(E.toModel)
}}}});
v.parcel=C
}},function(C){},function(C){});
var y=n.retrieveSectionWiseCustomFieldInfo("lot");
p.lotDbType={};
y.then(function(D){p.updateLotTemplate=D.genralSection;
p.updateLotTemplate=n.retrieveCustomData(p.updateLotTemplate,w);
var C=[];
if(p.updateLotTemplate!=null&&p.updateLotTemplate.length>0){angular.forEach(p.updateLotTemplate,function(E){if(E.model){C.push(E.model)
}else{if(E.fromModel){C.push(E.fromModel)
}else{if(E.toModel){C.push(E.toModel)
}}}});
v.lot=C;
var C=[];
C.push(p.lotIdForConstraint);
v.id=C
}},function(C){},function(C){});
p.parentDbFieldName=[];
var A=n.retrieveSectionWiseCustomFieldInfo("packet");
p.packetDbTypeParent={};
A.then(function(F){p.updatePacketParentTemplate=F.genralSection;
var E=[];
var C=Object.keys(w).map(function(G,H){angular.forEach(this[G],function(I){if(G==="Packet#P#"){E.push({Lot:I})
}})
},w);
p.updatePacketParentTemplate=n.retrieveCustomData(p.updatePacketParentTemplate,E);
var D=[];
angular.forEach(p.updatePacketParentTemplate,function(G){if(G.model){D.push(G.model)
}else{if(G.fromModel){D.push(G.fromModel)
}else{if(G.toModel){D.push(G.toModel)
}}}});
v.packet=D;
p.parentDbFieldName=angular.copy(D);
var D=[];
D.push(p.packetIdForConstraint);
v.packetId=D
},function(C){},function(C){});
p.categoryCustom=n.resetSection(p.updatePacketTemplate);
var x=n.retrieveSectionWiseCustomFieldInfo("packet");
if(!(p.packetDbType!=null)){p.packetDbType={}
}x.then(function(J){var G=[];
p.fieldIdNameMap={};
p.updatePacketTemplate=J.genralSection;
var E=[];
var D=Object.keys(w).map(function(K,L){angular.forEach(this[K],function(M){if(K!=="Packet#P#"){E.push({Packet:M})
}})
},w);
p.updatePacketTemplate=n.retrieveCustomData(p.updatePacketTemplate,E);
p.listOfModelsOfDateType=[];
var C=[];
if(p.updatePacketTemplate!==null&&p.updatePacketTemplate!==undefined){angular.forEach(p.updatePacketTemplate,function(K){if(K.type!==null&&K.type!==undefined&&K.type==="date"){p.listOfModelsOfDateType.push(angular.copy(K.model))
}C.push(K.model)
})
}p.parcelLabelListForUiGrid=[];
angular.forEach(p.updatePacketTemplate,function(K){if(K.fromModel){p.parcelLabelListForUiGrid.push({name:K.fromModel,displayName:K.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.searchedParcelScreenRule(row, '"+K.fromModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'})
}else{if(K.toModel){p.parcelLabelListForUiGrid.push({name:K.toModel,displayName:K.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.searchedParcelScreenRule(row, '"+K.toModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'})
}else{if(K.model){p.parcelLabelListForUiGrid.push({name:K.model,displayName:K.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.searchedParcelScreenRule(row, '"+K.model+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'})
}}}if(K.model==="no_of_pieces_of_packet$NM$Long"){K.required=true
}else{if(K.model==="carat_of_packet$NM$Double"){K.required=true
}else{K.isViewFromDesignation=true
}}if(K.model){p.fieldIdNameMap[K.fieldId]=K.model;
p.packetLabelForUIGrid.push({name:K.model,displayName:K.label,minWidth:200});
p.parentDbFieldName.push(K.model)
}else{if(K.fromModel){p.fieldIdNameMap[K.fieldId]=K.fromModel;
p.packetLabelForUIGrid.push({name:K.fromModel,displayName:K.label,minWidth:200});
if(p.parentDbFieldName.length>0&&p.parentDbFieldName.indexOf(K.fromModel===-1)){p.parentDbFieldName.push(K.fromModel)
}}else{if(K.toModel){p.fieldIdNameMap[K.fieldId]=K.toModel;
p.packetLabelForUIGrid.push({name:K.toModel,displayName:K.label,minWidth:200});
if(p.parentDbFieldName.length>0&&p.parentDbFieldName.indexOf(K.toModel===-1)){p.parentDbFieldName.push(K.toModel)
}}}}});
p.fieldNotConfigured=false;
p.mandatoryFields=["carat_of_packet$NM$Double","no_of_pieces_of_packet$NM$Long"];
if(C.length>0&&p.mandatoryFields!=null&&p.mandatoryFields.length>0){for(var I=0;
I<C.length;
I++){if(p.mandatoryFields[I]!==undefined){if(C.indexOf(p.mandatoryFields[I])===-1){p.fieldNotConfigured=true;
break
}}else{break
}}}else{p.fieldNotConfigured=true
}if(!p.fieldNotConfigured){if(p.updatePacketTemplate!==undefined&&p.updatePacketTemplate!==null&&p.updatePacketTemplate.length>0){p.finalUpdatePacketTemplate=[];
var H=0;
for(var F=0;
F<p.updatePacketTemplate.length;
F++){if(H<=3){if(p.updatePacketTemplate[F].model==="no_of_pieces_of_packet$NM$Long"){p.finalUpdatePacketTemplate.push(p.updatePacketTemplate[F]);
H++
}else{if(p.updatePacketTemplate[F].model==="carat_of_packet$NM$Double"){p.finalUpdatePacketTemplate.push(p.updatePacketTemplate[F]);
H++
}else{if(p.updatePacketTemplate[F].model==="isCheck"){p.finalUpdatePacketTemplate.push(p.updatePacketTemplate[F]);
H++
}}}}else{break
}}}}v.packet=p.parentDbFieldName;
var G=[];
v.ruleConfigMap={fieldIdNameMap:p.fieldIdNameMap,featureName:"editPacket"};
console.log(JSON.stringify("--------------------------"));
console.log(JSON.stringify(v.packetId));
g.retrievePacketById(v,function(K){p.stockCaratOfPacket=undefined;
p.stockPiecesOfPacket=undefined;
if(K!==undefined&&K!==null&&K[0]!==null){p.invoiceCustom=K[0].custom1;
p.parcelCustom=K[0].custom3;
p.lotCustom=K[0].custom4;
if(K[0].custom5!==undefined&&K[0].custom5!==null){p.stockCaratOfPacket=angular.copy(K[0].custom5.stockCarat);
p.stockPiecesOfPacket=angular.copy(K[0].custom5.stockPieces);
delete K[0].custom5.stockPieces;
delete K[0].custom5.stockCarat;
p.packetParentCustom=K[0].custom5;
angular.forEach(p.listOfModelsOfDateType,function(M){if(K[0].custom5.hasOwnProperty(M)){if(K[0].custom5[M]!==null&&K[0].custom5[M]!==undefined){K[0].custom5[M]=new Date(K[0].custom5[M])
}else{K[0].custom5[M]=""
}}});
p.packetCustom={};
p.packetCustom=angular.copy(K[0].custom5);
p.packetCustom.no_of_pieces_of_packet$NM$Long=1;
p.packetCustom.packetID$AG$String=K[0].custom5["packetID$AG$String"];
p.packetCustom["carat_of_packet$NM$Double"]=null;
console.log(p.packetCustom)
}else{p.packetCustom={};
p.packetCustom=angular.copy(K[0].custom5);
p.packetCustom.no_of_pieces_of_packet$NM$Long=1;
p.packetCustom.packetID$AG$String=K[0].custom5["packetID$AG$String"];
p.packetCustom["carat_of_packet$NM$Double"]=null
}}e(function(){p.Packet=this;
this.packetSplitFlag=true;
l.unMaskLoading()
},100);
p.dataToConvert=angular.copy(K);
p.dataToConvert=p.dataToConvert.splice(1,1);
var L=function(M){p.searchedSplitData=angular.copy(M);
p.searchedDataFromDbForUiGrid=[];
angular.forEach(p.searchedSplitData,function(N){angular.forEach(p.parcelLabelListForUiGrid,function(O){if(!!N.categoryCustom&&!N.categoryCustom.hasOwnProperty(O.name)){N.categoryCustom[O.name]="NA"
}else{if(!!N.categoryCustom&&N.categoryCustom.hasOwnProperty(O.name)){if(N.categoryCustom[O.name]===null||N.categoryCustom[O.name]===""||N.categoryCustom[O.name]===undefined){N.categoryCustom[O.name]="NA"
}}}});
if(!!N.categoryCustom){p.searchedDataFromDbForUiGrid.push(N.categoryCustom)
}});
p.gridOptionsForParcel={};
p.gridOptionsForParcel.enableFiltering=true;
p.gridOptionsForParcel.multiSelect=false;
p.gridOptionsForParcel.enableRowSelection=true;
p.gridOptionsForParcel.enableSelectAll=false;
p.selectedParcel=[];
p.gridOptionsForParcel.data=p.searchedDataFromDbForUiGrid;
p.gridOptionsForParcel.columnDefs=p.parcelLabelListForUiGrid
};
n.convertorForCustomField(K,L);
window.setTimeout(function(){$(window).resize();
$(window).resize()
},100);
p.splitlistFilled=true
},function(){var L="Could not retrieve, please try again.";
var K=l.error;
l.addMessage(L,K);
l.unMaskLoading()
});
p.flag.customFieldGenerated=true;
p.packetAddFlag=true
},function(C){},function(C){})
},function(){l.unMaskLoading();
var x="Failed to retrieve data";
var w=l.error;
l.addMessage(x,w)
})
}},function(s){l.unMaskLoading();
var u="Failed to retrieve pre rule.";
var t=l.error;
l.addMessage(u,t)
})
}};
p.splitStock=function(u){var s={};
var r=true;
if(u.$valid){if(p.stockCaratOfPacket&&p.stockPiecesOfPacket&&p.stockCaratOfPacket>p.packetCustom["carat_of_packet$NM$Double"]&&p.stockPiecesOfPacket>1){s.type="Packet";
s.id=p.packetIdForConstraint;
s.parentID=p.lotIdForConstraint;
s.stockDbType=angular.copy(p.packetDbType);
if(p.gridOptionsForParcel!==undefined&&p.gridOptionsForParcel!==null&&p.gridOptionsForParcel.data!==undefined&&p.gridOptionsForParcel.data!==null&&p.gridOptionsForParcel.data.length>0){s.databeanOfPacket={packetCustom:{packetID$AG$String:p.gridOptionsForParcel.data[p.gridOptionsForParcel.data.length-1].packetID$AG$String}}
}else{s.databeanOfPacket={packetCustom:{packetID$AG$String:p.packetCustom.packetID$AG$String}}
}s.stockDbType["packetID$AG$String"]="String";
var q=[];
q.push(p.packetCustom);
s.stockDataForSplit=q;
l.maskLoading();
console.log(JSON.stringify(s.databeanOfPacket.packetCustom.packetID$AG$String));
m.splitStock(s,function(x){console.log(JSON.stringify(x.data));
if(x!==undefined&&x!==null&&x.data!==undefined&&x.data!==null){var y=[];
x.data=x.data.toString().substring(1,x.data.toString().length-1);
var w={};
w=angular.copy(p.packetCustom);
w["packetID$AG$String"]=x.data.toString();
y.push({categoryCustom:angular.copy(w)});
p.stockCaratOfPacket=p.stockCaratOfPacket-p.packetCustom["carat_of_packet$NM$Double"];
p.stockPiecesOfPacket=p.stockPiecesOfPacket-p.packetCustom["no_of_pieces_of_packet$NM$Long"];
p.packetCustom["carat_of_packet$NM$Double"]=null;
p.packetCustom["no_of_pieces_of_packet$NM$Long"]=1;
if(p.packetParentCustom!==undefined&&p.packetParentCustom!==null){p.packetParentCustom["carat_of_packet$NM$Double"]=angular.copy(p.stockCaratOfPacket);
p.packetParentCustom["no_of_pieces_of_packet$NM$Long"]=angular.copy(p.stockPiecesOfPacket)
}var z=function(A){angular.forEach(p.parcelLabelListForUiGrid,function(B){if(!A[0].categoryCustom.hasOwnProperty(B.name)){A[0].categoryCustom[B.name]="NA"
}else{if(A[0].categoryCustom.hasOwnProperty(B.name)){if(A[0].categoryCustom[B.name]===null||A[0].categoryCustom[B.name]===""||A[0].categoryCustom[B.name]===undefined||A[0].categoryCustom[B.name]==="undefined"){A[0].categoryCustom[B.name]="NA"
}}}});
p.gridOptionsForParcel.data.push(angular.copy(A[0].categoryCustom))
};
n.convertorForCustomField(y,z)
}l.unMaskLoading()
},function(){l.unMaskLoading();
var x="Failed to split packet";
var w=l.error;
l.addMessage(x,w)
})
}else{var v="Carat or Pieces value does not match, please try again.";
var t=l.error;
l.addMessage(v,t)
}}}
}])
});