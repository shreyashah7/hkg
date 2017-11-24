define(["hkg","packetService","customFieldService","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicForm","accordionCollapse","ruleExecutionService"],function(a,b){a.register.controller("EditPacketController",["$rootScope","$scope","PacketService","$timeout","$filter","$location","$window","CustomFieldService","DynamicFormService","RuleExecutionService",function(k,n,f,d,h,e,c,m,l,i){k.mainMenu="stockLink";
k.childMenu="editPacket";
k.activateMenu();
n.Packet=this;
var j={};
var g={};
n.initializeData=function(o){if(o){k.maskLoading();
n.searchedData=[];
n.searchedDataFromDb=[];
n.listFilled=false;
n.lotListTodisplay=[];
n.searchCustom={};
var p=l.retrieveSearchWiseCustomFieldInfo("packetEdit");
n.flag={};
n.flag.searchFieldNotAvailable=false;
n.dbType={};
p.then(function(u){n.searchInvoiceTemplate=[];
n.searchParcelTemplate=[];
n.searchLotTemplate=[];
n.searchPacketTemplate=[];
n.fieldIdNameMap={};
var r=[];
var t=[];
var s=[];
var q=[];
n.generalSearchTemplate=u.genralSection;
n.labelListForUiGrid=[];
if(n.generalSearchTemplate!=null&&n.generalSearchTemplate.length>0){j={};
angular.forEach(n.generalSearchTemplate,function(v){var w=angular.copy(v);
if(w.featureName.toLowerCase()==="invoice"){n.searchInvoiceTemplate.push(angular.copy(w));
r.push(angular.copy(w.model))
}else{if(w.featureName.toLowerCase()==="parcel"){n.searchParcelTemplate.push(angular.copy(w));
t.push(angular.copy(w.model))
}else{if(w.featureName.toLowerCase()==="lot"){n.searchLotTemplate.push(angular.copy(w));
s.push(angular.copy(w.model))
}else{if(w.featureName.toLowerCase()==="packet"){n.searchPacketTemplate.push(angular.copy(w));
q.push(angular.copy(w.model))
}}}}j[w.model]=w.featureName;
if(w.fromModel){n.labelListForUiGrid.push({name:w.fromModel,displayName:w.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.editPacketScreenRule(row, '"+v.fromModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
n.fieldIdNameMap[w.fieldId]=w.fromModel
}else{if(w.toModel){n.labelListForUiGrid.push({name:w.toModel,displayName:w.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.editPacketScreenRule(row, '"+v.toModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
n.fieldIdNameMap[w.fieldId]=w.toModel
}else{if(w.model){n.labelListForUiGrid.push({name:w.model,displayName:w.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.editPacketScreenRule(row, '"+v.model+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
n.fieldIdNameMap[w.fieldId]=w.model
}}}});
if(r.length>0){g.invoice=r
}if(t.length>0){g.parcel=t
}if(s.length>0){g.lot=s
}if(q.length>0){g.packet=q
}}else{n.flag.searchFieldNotAvailable=true;
n.dataRetrieved=true
}n.searchResetFlag=true;
n.dataRetrieved=true;
k.unMaskLoading()
},function(q){},function(q){})
}};
n.initializeData(true);
n.editPacketScreenRule=function(q,p){var o;
if(!!q.entity.screenRuleDetailsWithDbFieldName&&q.entity.screenRuleDetailsWithDbFieldName[p]!==undefined&&q.entity.screenRuleDetailsWithDbFieldName[p]!==null){if(n.selectedStock.length===0||(n.selectedStock[0].$$packetId$$!==q.entity.$$packetId$$)){o=q.entity.screenRuleDetailsWithDbFieldName[p].colorCode
}}return o
};
n.retrieveSearchedData=function(){n.selectOneParameter=false;
n.searchedData=[];
n.listFilled=false;
if(Object.getOwnPropertyNames(n.searchCustom).length>0){var r=false;
for(var u in n.searchCustom){if(typeof n.searchCustom[u]==="object"&&n.searchCustom[u]!=null){var s=angular.copy(n.searchCustom[u].toString());
if(typeof s==="string"&&s!==null&&s!==undefined&&s.length>0){r=true;
break
}}if(typeof n.searchCustom[u]==="string"&&n.searchCustom[u]!==null&&n.searchCustom[u]!==undefined&&n.searchCustom[u].length>0){r=true;
break
}if(typeof n.searchCustom[u]==="number"&&!!(n.searchCustom[u])){r=true;
break
}if(typeof n.searchCustom[u]==="boolean"){r=true;
break
}}if(r){k.maskLoading();
n.packetDataBean={};
n.packetDataBean.featureCustomMapValue={};
var q={};
var o=l.convertSearchData(n.searchInvoiceTemplate,n.searchParcelTemplate,n.searchLotTemplate,n.searchPacketTemplate,n.searchCustom);
n.searchCustom=angular.copy(o);
angular.forEach(j,function(z,x){var y=n.searchCustom[x];
if(y!==undefined){var w={};
if(!q[z]){w[x]=y;
q[z]=w
}else{var v=q[z];
v[x]=y;
q[z]=v
}}else{y=n.searchCustom["to"+x];
if(y!==undefined){var w={};
if(!q[z]){w["to"+x]=y;
q[z]=w
}else{var v=q[z];
v["to"+x]=y;
q[z]=v
}}y=n.searchCustom["from"+x];
if(y!==undefined){var w={};
if(!q[z]){w["from"+x]=y;
q[z]=w
}else{var v=q[z];
v["from"+x]=y;
q[z]=v
}}}});
n.packetDataBean.featureCustomMapValue=q;
n.packetDataBean.hasPacket=false;
n.packetDataBean.featureDbFieldMap=g;
n.packetDataBean.ruleConfigMap={fieldIdNameMap:n.fieldIdNameMap,featureName:"editPacket"};
f.search(n.packetDataBean,function(w){n.searchedDataFromDb=angular.copy(w);
n.issuedStock={};
n.issuedStock.enableFiltering=true;
n.issuedStock.multiSelect=false;
n.issuedStock.enableRowSelection=true;
n.selectedStock=[];
n.issuedStock.onRegisterApi=function(y){n.gridApi=y;
y.selection.on.rowSelectionChanged(n,function(z){if(n.selectedStock.length>0){$.each(n.selectedStock,function(B,A){if(A["$$hashKey"]===z.entity["$$hashKey"]){n.flag.repeatedRow=true;
n.selectedStock.splice(B,1);
return false
}else{n.flag.repeatedRow=false
}});
if(!n.flag.repeatedRow){n.selectedStock.push(z.entity)
}}else{n.selectedStock.push(z.entity)
}if(n.selectedStock.length>0){n.flag.rowSelectedflag=true
}else{n.flag.rowSelectedflag=false
}});
y.selection.on.rowSelectionChangedBatch(n,function(z){if(n.selectedStock.length>0){angular.forEach(z,function(A){$.each(n.selectedStock,function(C,B){if(B["$$hashKey"]===A.entity["$$hashKey"]){n.flag.repeatedRow=true;
n.selectedStock=[];
return false
}else{n.flag.repeatedRow=false
}});
if(!n.flag.repeatedRow){n.selectedStock.push(A.entity)
}})
}else{angular.forEach(z,function(A){n.selectedStock.push(A.entity)
})
}if(n.selectedStock.length>0){n.flag.rowSelectedflag=true
}else{n.flag.rowSelectedflag=false
}})
};
n.issuedStockList=[];
for(var v=0;
v<w.length;
v++){if(w[v].categoryCustom!=null){w[v].categoryCustom["$$$value"]=(w[v].value)
}}var x=function(y){n.searchedData=angular.copy(w);
angular.forEach(n.searchedData,function(z){angular.forEach(n.labelListForUiGrid,function(A){if(z.custom1!=null&&!z.custom1.hasOwnProperty(A.name)){z.custom1[A.name]="NA"
}else{if(z.custom1!=null&&z.custom1.hasOwnProperty(A.name)){if(!(!!(z.custom1[A.name]))){z.custom1[A.name]="NA"
}}}});
if(z.categoryCustom!=null){z.categoryCustom.$$packetId$$=z.value;
z.categoryCustom.screenRuleDetailsWithDbFieldName=z.screenRuleDetailsWithDbFieldName;
console.log(z.categoryCustom["$$$value"]);
n.issuedStockList.push(z.categoryCustom)
}});
k.unMaskLoading();
n.issuedStock.data=n.issuedStockList;
n.issuedStock.columnDefs=n.labelListForUiGrid
};
l.convertorForCustomField(w,x);
window.setTimeout(function(){$(window).resize();
$(window).resize()
},200);
n.listFilled=true;
n.issueListFilled=true;
n.dataRetrieved=true
},function(){var w="Could not retrieve, please try again.";
var v=k.error;
k.addMessage(w,v);
k.unMaskLoading()
})
}else{var t="Please select atleast one search criteria for search";
var p=k.warning;
k.addMessage(t,p)
}}else{n.issuedStock={};
n.issuedStock.data=[];
n.dataRetrieved=true;
n.listFilled=true;
var t="Please select atleast one search criteria for search";
var p=k.warning;
k.addMessage(t,p)
}};
n.initEditPacketForm=function(o){n.editPacketForm=o
};
n.reset=function(p){if(p==="searchCustom"){n.searchCustom={};
var o=l.retrieveSearchWiseCustomFieldInfo("packetEdit");
n.dbType={};
o.then(function(q){n.generalSearchTemplate=q.genralSection;
n.searchResetFlag=true;
k.unMaskLoading();
n.flag.customFieldGenerated=true
},function(q){},function(q){})
}else{if(p==="packetCustom"){if(n.response!==undefined&&n.response!==null){n.packetCustom={};
var o=l.retrieveSectionWiseCustomFieldInfo("packet");
o.then(function(s){var r=[];
var q=Object.keys(n.response).map(function(t,u){angular.forEach(this[t],function(v){if(t!=="Packet#P#"){r.push({Packet:v})
}})
},n.response);
n.updatePacketTemplate=null;
n.updatePacketTemplate=s.genralSection;
n.updatePacketTemplate=l.retrieveCustomData(n.updatePacketTemplate,r);
d(function(){n.Packet=this;
this.packetEditFlag=true
},100);
n.lotEditShow=true;
k.unMaskLoading();
n.flag.customFieldGenerated=true
},function(q){},function(q){})
}}}};
n.onCanel=function(){if(n.editPacketForm!=null){n.editPacketForm.$dirty=false
}if(k.editPacket){n.initializeData(true)
}n.selectedStock=[];
n.flag.showAddPage=false;
k.editPacket=false;
n.flag.showUpdatePage=false;
k.packetId=null;
n.submitted=false;
n.reset("packetCustom");
n.flag.showUpdatePage=false
};
n.onCanelOfSearch=function(o){if(n.editPacketForm!=null){n.editPacketForm.$dirty=false
}n.searchedData=[];
n.searchResetFlag=false;
n.listFilled=false;
n.reset("searchCustom")
};
n.savePacket=function(p){n.caratDoesNotMatch=false;
angular.forEach(n.listOfModelsOfDateType,function(s){if(n.packetCustom.hasOwnProperty(s)){if(n.packetCustom[s]!==null&&n.packetCustom[s]!==undefined){n.packetCustom[s]=new Date(n.packetCustom[s])
}else{n.packetCustom[s]=""
}}});
n.submitted=true;
n.packetDataBean.packetCustom=n.packetCustom;
n.packetDataBean.packetDbType=angular.copy(n.packetDbType);
n.packetDataBean.id=n.packetIdForConstraint;
n.packetDataBean.workallocationId=n.workAllocationId;
if(p.$valid){n.submitted=false;
if(Object.getOwnPropertyNames(n.packetCustom).length>0){if((n.packetCustom["carat_of_packet$NM$Double"]!=null&&n.packetCustom["carat_of_packet$NM$Double"]>n.stockCaratOfLot)||(n.packetCustom["no_of_pieces_of_packet$NM$Long"]!=null&&n.packetCustom["no_of_pieces_of_packet$NM$Long"]>n.stockPiecesOfLot)){n.caratDoesNotMatch=true
}}if(!n.caratDoesNotMatch&&n.stockPiecesOfLot!==undefined&&n.stockCaratOfLot!==undefined){k.maskLoading();
var r={featureName:"editPacket",entityId:n.packetIdForConstraint,entityType:"packet",currentFieldValueMap:n.packetCustom,dbType:n.packetDbType,otherEntitysIdMap:{invoiceId:n.invoiceIdForConstraint,parcelId:n.parcelIdForConstraint,lotId:n.lotIdForConstraint}};
i.executePostRule(r,function(s){if(!!s.validationMessage){k.unMaskLoading();
var t=k.warning;
k.addMessage(s.validationMessage,t)
}else{f.update(n.packetDataBean,function(u){n.editPacketForm.$dirty=false;
k.editPacket=false;
k.packetByParcel=null;
n.flag.showAddPage=false;
n.onCanel();
k.unMaskLoading()
},function(){k.unMaskLoading();
var v="Could not update lot, please try again.";
var u=k.error;
k.addMessage(v,u)
})
}},function(s){k.unMaskLoading();
var u="Failed to authenticate post rule.";
var t=k.error;
k.addMessage(u,t)
})
}else{k.unMaskLoading();
var q="Carat value does not match, please try again.";
var o=k.error;
k.addMessage(q,o)
}}};
n.addPacket=function(){n.reset("packetCustom");
this.packetEditFlag=false;
n.flag.showAddPage=false;
if((n.selectedStock!==undefined&&n.selectedStock!==null&&n.selectedStock.length>0)||(k.editPacket)){k.maskLoading();
if(n.searchedDataFromDb!==undefined&&n.searchedDataFromDb!==null){for(var o=0;
o<n.searchedDataFromDb.length;
o++){if(n.searchedDataFromDb[o].value===n.selectedStock[0].$$$value){n.invoiceIdForConstraint=n.searchedDataFromDb[o].id;
n.parcelIdForConstraint=n.searchedDataFromDb[o].description;
n.lotIdForConstraint=n.searchedDataFromDb[o].label;
n.packetIdForConstraint=angular.copy(n.searchedDataFromDb[o].value);
break
}}}if(k.editPacket){n.packetIdForConstraint=k.packetId
}n.preRuleSatisfied=false;
var p={featureName:"editPacket",entityId:n.packetIdForConstraint,entityType:"packet"};
i.executePreRule(p,function(r){if(!!r.validationMessage){n.preRuleSatisfied=true;
var s=k.warning;
k.addMessage(r.validationMessage,s);
k.unMaskLoading()
}else{n.count=0;
n.flag.showUpdatePage=true;
n.gridOptions={};
n.gridOptions.enableFiltering=true;
n.gridOptions.data=[];
n.packetLabelForUIGrid=[];
n.flag.showAddPage=true;
var t={};
var q=[];
n.packetDataBean={};
n.packetListToSave=[];
n.parcelDataBean={invoiceDataBean:{invoiceCustom:"",invoiceDbType:""}};
m.retrieveDesignationBasedFields("packetEdit",function(u){n.response=angular.copy(u);
var z=l.retrieveSectionWiseCustomFieldInfo("invoice");
n.invoiceDbType={};
z.then(function(B){n.updateInvoiceTemplate=B.genralSection;
n.updateInvoiceTemplate=l.retrieveCustomData(n.updateInvoiceTemplate,u);
var A=[];
if(n.updateInvoiceTemplate!=null&&n.updateInvoiceTemplate.length>0){angular.forEach(n.updateInvoiceTemplate,function(C){if(C.model){A.push(C.model)
}else{if(C.fromModel){A.push(C.fromModel)
}else{if(C.toModel){A.push(C.toModel)
}}}});
t.invoice=A
}},function(A){},function(A){});
var x=l.retrieveSectionWiseCustomFieldInfo("parcel");
n.parcelDbType={};
x.then(function(B){n.updateParcelTemplate=B.genralSection;
n.updateParcelTemplate=l.retrieveCustomData(n.updateParcelTemplate,u);
var A=[];
if(n.updateParcelTemplate!=null&&n.updateParcelTemplate.length>0){angular.forEach(n.updateParcelTemplate,function(C){if(C.model){A.push(C.model)
}else{if(C.fromModel){A.push(C.fromModel)
}else{if(C.toModel){A.push(C.toModel)
}}}});
t.parcel=A
}},function(A){},function(A){});
var w=l.retrieveSectionWiseCustomFieldInfo("lot");
n.lotDbType={};
w.then(function(B){n.updateLotTemplate=B.genralSection;
n.updateLotTemplate=l.retrieveCustomData(n.updateLotTemplate,u);
var A=[];
if(n.updateLotTemplate!=null&&n.updateLotTemplate.length>0){angular.forEach(n.updateLotTemplate,function(C){if(C.model){A.push(C.model)
}else{if(C.fromModel){A.push(C.fromModel)
}else{if(C.toModel){A.push(C.toModel)
}}}});
t.lot=A;
var A=[];
A.push(n.lotIdForConstraint);
t.id=A
}},function(A){},function(A){});
n.parentDbFieldName=[];
var y=l.retrieveSectionWiseCustomFieldInfo("packet");
n.packetDbTypeParent={};
y.then(function(D){n.updatePacketParentTemplate=D.genralSection;
var C=[];
var A=Object.keys(u).map(function(E,F){angular.forEach(this[E],function(G){if(E==="Packet#P#"){C.push({Lot:G})
}})
},u);
n.updatePacketParentTemplate=l.retrieveCustomData(n.updatePacketParentTemplate,C);
var B=[];
angular.forEach(n.updatePacketParentTemplate,function(E){if(E.model){B.push(E.model)
}else{if(E.fromModel){B.push(E.fromModel)
}else{if(E.toModel){B.push(E.toModel)
}}}});
t.packet=B;
n.parentDbFieldName=angular.copy(B);
var B=[];
B.push(n.packetIdForConstraint);
t.packetId=B
},function(A){},function(A){});
n.categoryCustom=l.resetSection(n.updatePacketTemplate);
var v=l.retrieveSectionWiseCustomFieldInfo("packet");
if(!(n.packetDbType!=null)){n.packetDbType={}
}v.then(function(D){var C=[];
n.fieldIdNameMap={};
n.updatePacketTemplate=D.genralSection;
var B=[];
var A=Object.keys(u).map(function(E,F){angular.forEach(this[E],function(G){if(E!=="Packet#P#"){B.push({Packet:G})
}})
},u);
n.updatePacketTemplate=l.retrieveCustomData(n.updatePacketTemplate,B);
n.listOfModelsOfDateType=[];
if(n.updatePacketTemplate!==null&&n.updatePacketTemplate!==undefined){angular.forEach(n.updatePacketTemplate,function(E){if(E.type!==null&&E.type!==undefined&&E.type==="date"){n.listOfModelsOfDateType.push(angular.copy(E.model))
}})
}angular.forEach(n.updatePacketTemplate,function(E){if(E.model==="packetID$AG$String"){E.isViewFromDesignation=true
}if(E.model==="no_of_pieces_of_packet$NM$Long"){E.required=true
}else{if(E.model==="carat_of_packet$NM$Double"){E.required=true
}else{if(E.model==="due_date_of_packet$DT$Date"){E.required=true
}else{if(E.model==="in_stock_of_packet$UMS$String"){E.required=true
}}}}if(E.model){n.fieldIdNameMap[E.fieldId]=E.model;
n.packetLabelForUIGrid.push({name:E.model,displayName:E.label,minWidth:200});
n.parentDbFieldName.push(E.model)
}else{if(E.fromModel){n.fieldIdNameMap[E.fieldId]=E.fromModel;
n.packetLabelForUIGrid.push({name:E.fromModel,displayName:E.label,minWidth:200});
if(n.parentDbFieldName.length>0&&n.parentDbFieldName.indexOf(E.fromModel===-1)){n.parentDbFieldName.push(E.fromModel)
}}else{if(E.toModel){n.fieldIdNameMap[E.fieldId]=E.toModel;
n.packetLabelForUIGrid.push({name:E.toModel,displayName:E.label,minWidth:200});
if(n.parentDbFieldName.length>0&&n.parentDbFieldName.indexOf(E.toModel===-1)){n.parentDbFieldName.push(E.toModel)
}}}}});
t.packet=n.parentDbFieldName;
var C=[];
t.ruleConfigMap={fieldIdNameMap:n.fieldIdNameMap,featureName:"editPacket"};
console.log(JSON.stringify(t));
f.retrievePacketById(t,function(E){console.log(E);
n.stockCaratOfLot=undefined;
n.stockPiecesOfLot=undefined;
if(E!=null){E=angular.copy(E[0]);
n.invoiceCustom=E.custom1;
n.parcelCustom=E.custom3;
n.lotCustom=E.custom4;
if(E.custom5!==undefined&&E.custom5!==null){n.stockCaratOfLot=angular.copy(E.custom5.stockCarat);
n.stockPiecesOfLot=angular.copy(E.custom5.stockPieces);
delete E.custom5.stockPieces;
delete E.custom5.stockCarat;
n.packetParentCustom=E.custom5;
angular.forEach(n.listOfModelsOfDateType,function(F){if(E.custom5.hasOwnProperty(F)){if(E.custom5[F]!==null&&E.custom5[F]!==undefined){E.custom5[F]=new Date(E.custom5[F])
}else{E.custom5[F]=""
}}});
n.packetCustom=angular.copy(E.custom5);
console.log(">>>>>>>>>>>>>>>>>>>>>>>>>>>");
console.log(JSON.stringify(E.custom5));
n.packetScreenRules=E.screenRuleDetailsWithDbFieldName
}else{n.packetCustom={}
}}d(function(){n.Packet=this;
this.packetEditFlag=true;
k.unMaskLoading()
},100)
},function(){var F="Could not retrieve, please try again.";
var E=k.error;
k.addMessage(F,E);
k.unMaskLoading()
});
n.flag.customFieldGenerated=true;
n.packetAddFlag=true
},function(A){},function(A){})
},function(){k.unMaskLoading();
var v="Failed to retrieve data";
var u=k.error;
k.addMessage(v,u)
})
}},function(q){k.unMaskLoading();
var s="Failed to retrieve pre rule.";
var r=k.error;
k.addMessage(s,r)
})
}};
if(k.editPacket){n.addPacket()
}n.showPopUp=function(){$("#deletePacketPopUp").modal("show")
};
n.hidePacketPopUp=function(){$("#deletePacketPopUp").modal("hide");
k.removeModalOpenCssAfterModalHide()
};
n.deletePacket=function(){if(n.packetIdForConstraint!==undefined&&n.packetIdForConstraint!==null){k.maskLoading();
f.deletePacket(n.packetIdForConstraint,function(o){if(o.data){n.hidePacketPopUp();
n.onCanel(n.editLotForm);
n.searchedData=[];
n.listFilled=false;
k.unMaskLoading()
}else{n.hidePacketPopUp();
k.unMaskLoading();
var q="Failed to delete packet, please try again.";
var p=k.error
}},function(){n.hidePacketPopUp();
k.unMaskLoading();
var p="Failed to delete packet, please try again.";
var o=k.error
})
}}
}])
});