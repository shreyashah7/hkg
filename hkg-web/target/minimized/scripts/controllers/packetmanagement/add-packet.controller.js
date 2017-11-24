define(["hkg","packetService","customFieldService","lotService","packetmanagement/edit-packet.controller","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicForm","accordionCollapse","ruleExecutionService"],function(a,b){a.register.controller("AddPacketController",["$rootScope","$scope","PacketService","$timeout","$filter","$location","$window","CustomFieldService","DynamicFormService","LotService","RuleExecutionService",function(l,o,f,d,h,e,c,n,m,j,i){l.mainMenu="stockLink";
l.childMenu="addPacket";
l.activateMenu();
var k={};
var g={};
o.afterLabel="A";
var p=function(q){if(q){o.nodeDetailsInfo=[];
o.lotDataBean={};
o.packetListTodisplay=[];
o.searchCustom={};
g={};
var r=m.retrieveSearchWiseCustomFieldInfo("packetAdd");
o.flag={};
o.flag.searchFieldNotAvailable=false;
o.flag.noResultFound=false;
if(!(o.dbType!==undefined&&o.dbType!==null)){o.dbType={}
}k={};
o.modelAndHeaderList=[];
o.modelAndHeaderListForLot=[];
o.labelListForUiGrid=[];
r.then(function(x){o.generalSearchTemplate=x.genralSection;
o.searchInvoiceTemplate=[];
o.searchParcelTemplate=[];
o.searchLotTemplate=[];
o.fieldIdNameMap={};
var s=[];
var w=[];
var v=[];
if(o.generalSearchTemplate!=null&&o.generalSearchTemplate.length>0){for(var t=0;
t<o.generalSearchTemplate.length;
t++){var u=o.generalSearchTemplate[t];
if(u.featureName.toLowerCase()==="invoice"){o.searchInvoiceTemplate.push(angular.copy(u));
s.push(angular.copy(u.model))
}else{if(u.featureName.toLowerCase()==="parcel"){o.searchParcelTemplate.push(angular.copy(u));
w.push(angular.copy(u.model))
}else{if(u.featureName.toLowerCase()==="lot"){o.searchLotTemplate.push(angular.copy(u));
v.push(angular.copy(u.model))
}}}if(u.fromModel){k[u.fromModel]=u.featureName;
o.labelListForUiGrid.push({name:u.fromModel,displayName:u.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.addPacketScreenRule(row, '"+u.fromModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
o.fieldIdNameMap[u.fieldId]=u.fromModel
}else{if(u.toModel){k[u.toModel]=u.featureName;
o.labelListForUiGrid.push({name:u.toModel,displayName:u.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.addPacketScreenRule(row, '"+u.toModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
o.fieldIdNameMap[u.fieldId]=u.toModel
}else{if(u.model){k[u.model]=u.featureName;
o.labelListForUiGrid.push({name:u.model,displayName:u.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.addPacketScreenRule(row, '"+u.model+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
o.fieldIdNameMap[u.fieldId]=u.model
}}}}if(s.length>0){g.invoice=s
}if(w.length>0){g.parcel=w
}if(v.length>0){g.lot=v
}}else{o.flag.searchFieldNotAvailable=true
}o.dataRetrieved=true;
o.searchResetFlag=true
},function(s){},function(s){})
}};
o.addPacketScreenRule=function(s,r){var q;
if(!!s.entity.screenRuleDetailsWithDbFieldName&&s.entity.screenRuleDetailsWithDbFieldName[r]!==undefined&&s.entity.screenRuleDetailsWithDbFieldName[r]!==null){if(o.selectedIssueStock.length===0||(o.selectedIssueStock[0].$$lotId$$!==s.entity.$$lotId$$)){q=s.entity.screenRuleDetailsWithDbFieldName[r].colorCode
}}return q
};
o.retrievedPacketsScreenRule=function(s,r){var q;
if(!!s.entity.screenRuleDetailsWithDbFieldName&&s.entity.screenRuleDetailsWithDbFieldName[r]!==undefined&&s.entity.screenRuleDetailsWithDbFieldName[r]!==null){q=s.entity.screenRuleDetailsWithDbFieldName[r].colorCode
}return q
};
o.retrieveSearchedData=function(){o.selectOneParameter=false;
o.searchedData=[];
o.issuedStockList=[];
o.listFilled=false;
if(Object.getOwnPropertyNames(o.searchCustom).length>0){var t=false;
for(var w in o.searchCustom){if(typeof o.searchCustom[w]==="object"&&o.searchCustom[w]!=null){var u=angular.copy(o.searchCustom[w].toString());
if(typeof u==="string"&&u!==null&&u!==undefined&&u.length>0){t=true;
break
}}if(typeof o.searchCustom[w]==="string"&&o.searchCustom[w]!==null&&o.searchCustom[w]!==undefined&&o.searchCustom[w].length>0){t=true;
break
}if(typeof o.searchCustom[w]==="number"&&!!(o.searchCustom[w])){t=true;
break
}if(typeof o.searchCustom[w]==="boolean"){t=true;
break
}}if(t){l.maskLoading();
o.lotDataBean.featureCustomMapValue={};
o.map={};
var s={};
var q=m.convertSearchData(o.searchInvoiceTemplate,o.searchParcelTemplate,o.searchLotTemplate,null,o.searchCustom);
o.searchCustom=angular.copy(q);
angular.forEach(k,function(B,z){var A=o.searchCustom[z];
if(A!==undefined){var y={};
if(!s[B]){y[z]=A;
s[B]=y
}else{var x=s[B];
x[z]=A;
s[B]=x
}}else{A=o.searchCustom["to"+z];
if(A!==undefined){var y={};
if(!s[B]){y["to"+z]=A;
s[B]=y
}else{var x=s[B];
x["to"+z]=A;
s[B]=x
}}A=o.searchCustom["from"+z];
if(A!==undefined){var y={};
if(!s[B]){y["from"+z]=A;
s[B]=y
}else{var x=s[B];
x["from"+z]=A;
s[B]=x
}}}});
o.lotDataBean.featureCustomMapValue=s;
o.lotDataBean.hasPacket=false;
o.lotDataBean.featureDbFieldMap=g;
o.lotDataBean.ruleConfigMap={fieldIdNameMap:o.fieldIdNameMap,featureName:"addPacketMenu"};
o.lotDataBean.loggedInFranchise=true;
j.search(o.lotDataBean,function(y){if(y!==undefined&&y!==null&&y.length>0){o.searchedDataFromDb=angular.copy(y);
o.issuedStock={};
o.issuedStock.enableFiltering=true;
o.issuedStock.multiSelect=false;
o.issuedStock.enableRowSelection=true;
o.issuedStock.enableSelectAll=true;
o.selectedIssueStock=[];
o.issuedStock.onRegisterApi=function(A){o.gridApi=A;
A.selection.on.rowSelectionChanged(o,function(B){if(o.selectedIssueStock.length>0){$.each(o.selectedIssueStock,function(D,C){if(C["$$hashKey"]===B.entity["$$hashKey"]){o.flag.repeatedRow=true;
o.selectedIssueStock.splice(D,1);
return false
}else{o.flag.repeatedRow=false
}});
if(!o.flag.repeatedRow){o.selectedIssueStock.push(B.entity)
}}else{o.selectedIssueStock.push(B.entity)
}if(o.selectedIssueStock.length>0){o.flag.rowSelectedflag=true
}else{o.flag.rowSelectedflag=false
}});
A.selection.on.rowSelectionChangedBatch(o,function(B){if(o.selectedIssueStock.length>0){angular.forEach(B,function(C){$.each(o.selectedIssueStock,function(E,D){if(D["$$hashKey"]===C.entity["$$hashKey"]){o.flag.repeatedRow=true;
o.selectedIssueStock=[];
return false
}else{o.flag.repeatedRow=false
}});
if(!o.flag.repeatedRow){o.selectedIssueStock.push(C.entity)
}})
}else{angular.forEach(B,function(C){o.selectedIssueStock.push(C.entity)
})
}if(o.selectedIssueStock.length>0){o.flag.rowSelectedflag=true
}else{o.flag.rowSelectedflag=false
}})
};
for(var x=0;
x<y.length;
x++){if(y[x].categoryCustom!=null){y[x].categoryCustom["$$$value"]=y[x].value
}}var z=function(A){o.searchedData=angular.copy(A);
o.issuedStockList=[];
angular.forEach(o.searchedData,function(B){angular.forEach(o.labelListForUiGrid,function(C){if(B.categoryCustom!=null&&!B.categoryCustom.hasOwnProperty(C.name)){B.categoryCustom[C.name]="NA"
}else{if(B.categoryCustom!=null&&B.categoryCustom.hasOwnProperty(C.name)){if(!(!!(B.categoryCustom[C.name]))){B.categoryCustom[C.name]="NA"
}}}});
if(B.categoryCustom!=null){B.categoryCustom.$$lotId$$=B.value;
B.categoryCustom.screenRuleDetailsWithDbFieldName=B.screenRuleDetailsWithDbFieldName;
o.issuedStockList.push(B.categoryCustom)
}});
o.issuedStock.data=o.issuedStockList;
o.issuedStock.columnDefs=o.labelListForUiGrid;
o.listFilled=true;
l.unMaskLoading()
};
m.convertorForCustomField(y,z);
window.setTimeout(function(){$(window).resize();
$(window).resize()
},200);
o.issueListFilled=true
}else{o.issuedStock={};
o.issuedStock.data=[];
o.listFilled=true;
o.flag.noResultFound=true;
l.unMaskLoading()
}},function(){var y="Could not retrieve, please try again.";
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
p(true);
o.initAddPacketForm=function(q){o.addPacketForm=q
};
o.addPacket=function(){o.caratOfLot=undefined;
o.invoiceCustom=undefined;
o.parcelCustom=undefined;
o.lotCustom=undefined;
o.flag.showAddPage=false;
if(o.selectedIssueStock!=null&&o.selectedIssueStock.length>0){o.preRuleSatisfied=false;
var q={featureName:"addPacketMenu",entityId:o.selectedIssueStock[0].$$$value,entityType:"lot"};
i.executePreRule(q,function(t){if(!!t.validationMessage){o.preRuleSatisfied=true;
var u=l.warning;
l.addMessage(t.validationMessage,u)
}else{o.count=0;
o.gridOptions={};
o.gridOptions.enableFiltering=true;
o.gridOptions.data=[];
o.packetLabelForUIGrid=[];
l.maskLoading();
o.workAllocationId=undefined;
o.lotIDWithSeqNumber=null;
o.stockCarat=angular.copy(o.selectedIssueStock[0].stockCarat);
o.stockPieces=angular.copy(o.selectedIssueStock[0].stockPieces);
o.lotIDWithSeqNumber=angular.copy(o.selectedIssueStock[0].lotID$AG$String);
for(var s=0;
s<o.searchedDataFromDb.length;
s++){if(o.searchedDataFromDb[s].value===o.selectedIssueStock[0].$$$value){o.invoiceIdForConstraint=o.searchedDataFromDb[s].description;
o.parcelIdForConstraint=o.searchedDataFromDb[s].label;
o.lotIdForConstraint=o.searchedDataFromDb[s].value;
break
}}o.nextSeqNumber=null;
f.getNextPacketSequence(o.lotIdForConstraint,function(w){if(w!==undefined&&w!==null){if(w.sequenceNumber.toString().length===1){o.nextSeqNumber="0"+w.sequenceNumber.toString()
}else{o.nextSeqNumber=w.sequenceNumber
}}},function(){l.unMaskLoading();
var x="Failed to retrieve sequence number, please try again.";
var w=l.error;
l.addMessage(x,w)
});
o.flag.showAddPage=true;
var v={};
var r=[];
o.packetDataBean={};
o.packetListToSave=[];
o.parcelDataBean={invoiceDataBean:{invoiceCustom:"",invoiceDbType:""}};
n.retrieveDesignationBasedFields("packetAdd",function(w){o.response=angular.copy(w);
var A=m.retrieveSectionWiseCustomFieldInfo("invoice");
o.invoiceDbType={};
A.then(function(B){o.generaInvoiceTemplate=B.genralSection;
o.generaInvoiceTemplate=m.retrieveCustomData(o.generaInvoiceTemplate,w);
if(o.generaInvoiceTemplate!=null&&o.generaInvoiceTemplate.length>0){angular.forEach(o.generaInvoiceTemplate,function(C){if(C.model){r.push(C.model)
}else{if(C.fromModel){r.push(C.fromModel)
}else{if(C.toModel){r.push(C.toModel)
}}}});
v.invoiceDbFieldName=r
}},function(B){},function(B){});
var z=m.retrieveSectionWiseCustomFieldInfo("parcel");
o.parcelDbType={};
z.then(function(C){o.generaParcelTemplate=C.genralSection;
o.generaParcelTemplate=m.retrieveCustomData(o.generaParcelTemplate,w);
var B=[];
if(o.generaParcelTemplate!=null&&o.generaParcelTemplate.length>0){angular.forEach(o.generaParcelTemplate,function(D){if(D.model){B.push(D.model)
}else{if(D.fromModel){B.push(D.fromModel)
}else{if(D.toModel){B.push(D.toModel)
}}}});
v.parcelDbFieldName=B
}},function(B){},function(B){});
var y=m.retrieveSectionWiseCustomFieldInfo("lot");
o.lotDbType={};
y.then(function(C){o.generalLotTemplate=C.genralSection;
o.generalLotTemplate=m.retrieveCustomData(o.generalLotTemplate,w);
var B=[];
if(o.generalLotTemplate!=null&&o.generalLotTemplate.length>0){angular.forEach(o.generalLotTemplate,function(D){if(D.model){B.push(D.model)
}else{if(D.fromModel){B.push(D.fromModel)
}else{if(D.toModel){B.push(D.toModel)
}}}});
v.lotDbFieldName=B;
var B=[];
B.push(o.lotIdForConstraint);
v.id=B
}},function(B){},function(B){});
o.categoryCustom={};
o.categoryCustom["packetID$AG$String"]=o.nextSeqNumber;
var x=m.retrieveSectionWiseCustomFieldInfo("packet");
if(!(o.packetDbType!=null)){o.packetDbType={}
}x.then(function(H){o.generalPacketTemplate=[];
o.fieldIdNameMap={};
o.generalPacketTemplate=H.genralSection;
var C=[];
var B=Object.keys(w).map(function(J,K){angular.forEach(this[J],function(L){if(J!=="Packet#P#"){C.push({Packet:L})
}})
},w);
o.generalPacketTemplate=m.retrieveCustomData(o.generalPacketTemplate,C);
o.fieldNotConfigured=false;
o.mandatoryFields=["carat_of_packet$NM$Double","no_of_pieces_of_packet$NM$Long","due_date_of_packet$DT$Date","packetID$AG$String"];
var D=[];
angular.forEach(o.generalPacketTemplate,function(J){if(J.model==="no_of_pieces_of_packet$NM$Long"){J.required=true
}else{if(J.model==="carat_of_packet$NM$Double"){J.required=true
}else{if(J.model==="due_date_of_packet$DT$Date"){J.required=true
}else{if(J.model==="in_stock_of_packet$UMS$String"){J.required=true
}else{if(J.model==="packetID$AG$String"){J.required=true
}}}}}if(J.model){D.push(J.model);
o.packetLabelForUIGrid.push({name:J.model,displayName:J.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.retrievedPacketsScreenRule(row, '"+J.model+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
o.fieldIdNameMap[J.fieldId]=J.model
}else{if(J.fromModel){o.packetLabelForUIGrid.push({name:J.fromModel,displayName:J.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.retrievedPacketsScreenRule(row, '"+J.fromModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
o.fieldIdNameMap[J.fieldId]=J.fromModel
}else{if(J.toModel){o.packetLabelForUIGrid.push({name:J.toModel,displayName:J.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.retrievedPacketsScreenRule(row, '"+J.toModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
o.fieldIdNameMap[J.fieldId]=J.toModel
}}}});
v.packetDbFieldName=D;
var E=[];
E.push(o.lotIdForConstraint);
v.lotObjectId=E;
f.retrievePacketByLotId(v,function(J){angular.forEach(J,function(L){});
if(J!=null){if(J[0].custom1!==undefined&&J[0].custom1!==null){o.invoiceCustom=J[0].custom1
}else{o.invoiceCustom={}
}if(J[0].custom3!==undefined&&J[0].custom3!==null){o.parcelCustom=J[0].custom3
}else{o.parcelCustom={}
}if(J[0].custom4!==undefined&&J[0].custom4!==null){o.lotCustom=J[0].custom4
}else{o.lotCustom={}
}var K=function(L){o.packetDataForUiGrid=[];
angular.forEach(L,function(M){if(M.categoryCustom!==undefined&&M.categoryCustom!==null){angular.forEach(o.packetLabelForUIGrid,function(N){if(!M.categoryCustom.hasOwnProperty(N.name)){M.categoryCustom[N.name]="NA"
}else{if(M.categoryCustom.hasOwnProperty(N.name)){if(M.categoryCustom[N.name]===null||M.categoryCustom[N.name]===""||M.categoryCustom[N.name]===undefined){M.categoryCustom[N.name]="NA"
}}}M.objectIdOfParcel={id:M.value}
});
M.categoryCustom.$$packetId$$=M.value;
M.categoryCustom.screenRuleDetailsWithDbFieldName=M.screenRuleDetailsWithDbFieldName;
o.packetDataForUiGrid.push(M.categoryCustom)
}});
o.gridOptions.data=o.packetDataForUiGrid;
if(o.packetDataForUiGrid.length>0){o.tempListFilled=true
}o.gridOptions.columnDefs=o.packetLabelForUIGrid;
if(o.editPacketFeature){o.gridOptions.columnDefs.push({name:"Action",cellTemplate:'<div class="ui-grid-cell-contents"> <a ng-click="grid.appScope.updatePacket(row.entity)">Update</i></a></div>',enableFiltering:false,minWidth:200})
}window.setTimeout(function(){$(window).resize();
$(window).resize();
l.unMaskLoading()
},100)
};
m.convertorForCustomField(J,K)
}else{o.invoiceCustom={};
o.parcelCustom={};
o.lotCustom={}
}},function(){var K="Could not retrieve, please try again.";
var J=l.error;
l.addMessage(K,J);
l.unMaskLoading()
});
if(D.length>0&&o.mandatoryFields!=null&&o.mandatoryFields.length>0){for(var G=0;
G<o.mandatoryFields.length;
G++){if(D.indexOf(o.mandatoryFields[G])===-1){o.fieldNotConfigured=true;
break
}}}else{o.fieldNotConfigured=true
}o.flag.customFieldGenerated=true;
o.AddPacketCtrl=this;
this.packetAddFlag=true;
l.unMaskLoading();
if(o.stockCarat===0||o.stockPieces===0){var I="Packet(s) already created for this lot";
var F=l.warning;
l.addMessage(I,F)
}},function(B){},function(B){})
},function(){l.unMaskLoading();
var x="Failed to retrieve data";
var w=l.error;
l.addMessage(x,w)
})
}},function(r){l.unMaskLoading();
var t="Failed to retrieve pre rule.";
var s=l.error;
l.addMessage(t,s)
})
}};
o.onCanel=function(){if(o.addPacketForm!=null){o.addPacketForm.$dirty=false
}o.submitted=false;
angular.forEach(o.fieldsToClear,function(q){delete o.categoryCustom[q]
})
};
o.onCanelOfSearch=function(q){if(q!=null){o.addPacketForm=q;
o.addPacketForm.$dirty=false
}o.searchedData=[];
o.searchResetFlag=false;
o.reset("searchCustom")
};
o.reset=function(s,q){if(s==="searchCustom"){o.searchCustom={};
var r=m.retrieveSearchWiseCustomFieldInfo("packetAdd");
r.then(function(t){o.generalSearchTemplate=t.genralSection;
o.searchResetFlag=true;
l.unMaskLoading();
o.flag.customFieldGenerated=true
},function(t){},function(t){})
}else{if(s==="categoryCustom"){angular.forEach(o.categoryCustom,function(u,t){if(q){if(t!=="carat_of_packet$NM$Double"){delete o.categoryCustom[t]
}}else{delete o.categoryCustom[t]
}});
var r=m.retrieveSectionWiseCustomFieldInfo("packet");
r.then(function(v){var u=[];
var t=Object.keys(o.response).map(function(w,x){angular.forEach(this[w],function(y){if(w!=="Packet#P#"){u.push({Packet:y})
}})
},o.response);
o.generalPacketTemplate=null;
o.generalPacketTemplate=v.genralSection;
o.generalPacketTemplate=m.retrieveCustomData(o.generalPacketTemplate,u);
d(function(){o.AddPacketCtrl=this;
this.packetAddFlag=true;
l.unMaskLoading()
},100);
console.log(o.generalPacketTemplate.length);
angular.forEach(o.generalPacketTemplate,function(w){if(w.model==="no_of_pieces_of_packet$NM$Long"){w.required=true
}else{if(w.model==="carat_of_packet$NM$Double"){w.required=true
}else{if(w.model==="due_date_of_packet$DT$Date"){w.required=true
}else{if(w.model==="in_stock_of_packet$UMS$String"){w.required=true
}else{if(w.model==="packetID$AG$String"){w.required=true
}}}}}});
o.flag.customFieldGenerated=true;
l.unMaskLoading()
},function(t){},function(t){})
}}};
o.resetAddForm=function(r,q){if(r){if(o.addLotForm!=null){o.addLotForm.$dirty=false
}o.flag.editPacket=false;
o.AddPacketCtrl=this;
this.packetAddFlag=false;
o.reset("categoryCustom",q)
}};
o.back=function(){if(o.addLotForm!=null){o.addLotForm.$dirty=false
}o.flag.showAddPage=false;
o.selectedIssueStock=[]
};
o.createPacket=function(u){if(Object.getOwnPropertyNames(o.categoryCustom).length>0){var s=false;
for(var x in o.categoryCustom){if(typeof o.categoryCustom[x]==="object"&&o.categoryCustom[x]!=null){var v=angular.copy(o.categoryCustom[x].toString());
if(typeof v==="string"&&v!==null&&v!==undefined&&v.length>0){s=true;
break
}}if(typeof o.categoryCustom[x]==="string"&&o.categoryCustom[x]!==null&&o.categoryCustom[x]!==undefined&&o.categoryCustom[x].length>0){s=true;
break
}if(typeof o.categoryCustom[x]==="number"&&!!(o.categoryCustom[x])){s=true;
break
}if(typeof o.categoryCustom[x]==="boolean"){s=true;
break
}}o.submitted=true;
if(u.$valid&&s){o.caratDoesNotMatch=false;
o.submitted=false;
if(o.categoryCustom["carat_of_packet$NM$Double"]>o.stockCarat||o.categoryCustom["no_of_pieces_of_packet$NM$Long"]>o.stockPieces){o.caratDoesNotMatch=true
}if(!o.caratDoesNotMatch){var q=o.categoryCustom["packetID$AG$String"];
if(q!==undefined&&q!==null){if(q!==null&&o.lotIDWithSeqNumber){var t={seqNumber:q,lotId:o.lotIdForConstraint};
f.isPacketSequenceNumberExist(t,function(y){if(y.data){l.unMaskLoading();
var C="Packet already exists for the same id.";
var z=l.error;
l.addMessage(C,z)
}else{var A=angular.copy(o.categoryCustom);
A["packetID$AG$String"]=o.lotIDWithSeqNumber.concat("-").concat(o.categoryCustom["packetID$AG$String"]).concat("A");
var B={featureName:"addPacketMenu",entityId:null,entityType:"packet",currentFieldValueMap:A,dbType:o.packetDbType,otherEntitysIdMap:{invoiceId:o.invoiceIdForConstraint,parcelId:o.parcelIdForConstraint,lotId:o.lotIdForConstraint}};
console.log("post rule dataToSend::::"+JSON.stringify(B));
i.executePostRule(B,function(E){console.log("post rule res::::"+JSON.stringify(E));
if(!!E.validationMessage){var F=l.warning;
l.addMessage(E.validationMessage,F);
l.unMaskLoading()
}else{var H=angular.copy(o.categoryCustom["packetID$AG$String"]);
var G=[];
G.push({categoryCustom:angular.copy(o.categoryCustom)});
var D={};
D.invoiceDataBean={};
D.parcelDataBean={};
D.lotDataBean={};
D.invoiceDataBean.id=o.invoiceIdForConstraint;
D.parcelDataBean.id=o.parcelIdForConstraint;
D.lotDataBean.id=o.lotIdForConstraint;
D.packetDbType=o.packetDbType;
D.packetCustom=angular.copy(o.categoryCustom);
D.packetCustom["packetID$AG$String"]=o.lotIDWithSeqNumber.concat("-").concat(o.categoryCustom["packetID$AG$String"]).concat("A");
D.sequenceNumber=H;
D.ruleConfigMap={fieldIdNameMap:o.fieldIdNameMap,featureName:"addPacketMenu"};
f.createPacket(D,function(I){o.tempListFilled=false;
if(I!==undefined&&I!==null){o.stockCarat=o.stockCarat-o.categoryCustom["carat_of_packet$NM$Double"];
o.stockPieces=o.stockPieces-o.categoryCustom["no_of_pieces_of_packet$NM$Long"];
o.selectedIssueStock[0].stockCarat=angular.copy(o.stockCarat);
o.selectedIssueStock[0].stockPieces=angular.copy(o.stockPieces);
f.getNextPacketSequence(o.lotIdForConstraint,function(L){if(L!==undefined&&L!==null){if(L.sequenceNumber.toString().length===1){o.categoryCustom["packetID$AG$String"]="0"+L.sequenceNumber.toString()
}else{o.categoryCustom["packetID$AG$String"]=L.sequenceNumber
}}},function(){l.unMaskLoading();
var M="Failed to delete packet, please try again.";
var L=l.error;
l.addMessage(M,L)
});
if(o.issuedStock.data!=null&&o.issuedStock.data.length>0){for(var J=0;
J<o.issuedStock.data.length;
J++){if(o.issuedStock.data[J].$$$value===o.selectedIssueStock[0].$$$value){o.issuedStock.data[J].carat_of_packet$NM$Double=o.issuedStock.data[J].carat_of_packet$NM$Double-o.categoryCustom["carat_of_packet$NM$Double"];
o.issuedStock.data[J].no_of_pieces_of_packet$NM$Long=o.issuedStock.data[J].no_of_pieces_of_packet$NM$Long-o.categoryCustom["no_of_pieces_of_packet$NM$Long"];
break
}}}var K=function(L){L[0].categoryCustom["$$objectId"]=o.count;
angular.forEach(o.packetLabelForUIGrid,function(M){if(!L[0].categoryCustom.hasOwnProperty(M.name)){L[0].categoryCustom[M.name]="NA"
}else{if(L[0].categoryCustom.hasOwnProperty(M.name)){if(L[0].categoryCustom[M.name]===null||L[0].categoryCustom[M.name]===""||L[0].categoryCustom[M.name]===undefined){L[0].categoryCustom[M.name]="NA"
}}}});
L[0].categoryCustom["packetID$AG$String"]=I[I.packetId];
L[0].categoryCustom.$$packetId$$=I.packetId;
L[0].categoryCustom.screenRuleDetailsWithDbFieldName=I.screenRuleDetailsWithDbFieldName;
o.gridOptions.data.push(angular.copy(L[0].categoryCustom));
o.gridOptions.columnDefs=o.packetLabelForUIGrid;
if(o.editPacketFeature){o.gridOptions.columnDefs.push({name:"Action",cellTemplate:'<div class="ui-grid-cell-contents"> <a ng-click="grid.appScope.updatePacket(row.entity)">Update</i></a></div>',enableFiltering:false,minWidth:200})
}};
m.convertorForCustomField(G,K);
o.resetAddForm(true,true);
o.addPacketForm.$dirty=false;
o.tempListFilled=true
}},function(){l.unMaskLoading();
var J="Could not create packet, please try again.";
var I=l.error;
l.addMessage(J,I)
})
}},function(D){l.unMaskLoading();
var F="Failed to authenticate post rule, please try again.";
var E=l.error;
l.addMessage(F,E)
})
}},function(){l.unMaskLoading();
var z="Could not retrieve sequence number, please try again.";
var y=l.error;
l.addMessage(z,y)
})
}}}else{o.tempListFilled=true;
l.unMaskLoading();
var w="Carat value does not match, please try again.";
var r=l.error;
l.addMessage(w,r)
}}}};
o.editPacketLocally=function(r){o.index=r.$$objectId;
o.oldObj=angular.copy(r,o.oldObj);
if(r!=null){o.flag.editPacket=true;
if(!!(o.packetListToSave&&o.packetListToSave.length>0)){for(var q=0;
q<o.packetListToSave.length;
q++){if(r.$$objectId!=null&&o.packetListToSave[q].$$objectId===r.$$objectId){o.categoryCustom=angular.copy(o.packetListToSave[q].categoryCustom);
break
}}}}};
o.savePacket=function(s){l.maskLoading();
o.tempListFilled=false;
o.submitted=true;
if(Object.getOwnPropertyNames(o.categoryCustom).length>0){var q=false;
for(var v in o.categoryCustom){if(typeof o.categoryCustom[v]==="object"&&o.categoryCustom[v]!=null){var t=angular.copy(o.categoryCustom[v].toString());
if(typeof t==="string"&&t!==null&&t!==undefined&&t.length>0){q=true;
break
}}if(typeof o.categoryCustom[v]==="string"&&o.categoryCustom[v]!==null&&o.categoryCustom[v]!==undefined&&o.categoryCustom[v].length>0){q=true;
break
}if(typeof o.categoryCustom[v]==="number"&&!!(o.categoryCustom[v])){q=true;
break
}if(typeof o.categoryCustom[v]==="boolean"){q=true;
break
}}if((q&&s.$valid)){var r=[];
r.push({categoryCustom:angular.copy(o.categoryCustom)});
angular.forEach(o.packetListToSave,function(w){if(w.$$objectId===o.index){w.categoryCustom=(o.categoryCustom)
}});
var u=function(w){for(var x=0;
x<o.gridOptions.data.length;
x++){var y=o.gridOptions.data[x];
if(y.$$objectId===o.index){o.gridOptions.data[x]=angular.copy(((w[0].categoryCustom)));
o.gridOptions.data[x].$$objectId=o.index;
break
}}};
m.convertorForCustomField(r,u);
o.tempListFilled=true;
o.flag.editPacket=false;
o.resetAddForm(true)
}}l.unMaskLoading()
};
o.showPopUp=function(q){o.index=q.$$objectId;
$("#deleteDialog").modal("show")
};
o.hidePopUp=function(){$("#deleteDialog").modal("hide");
l.removeModalOpenCssAfterModalHide()
};
o.deletePacket=function(){for(var q=0;
q<o.gridOptions.data.length;
q++){var r=o.gridOptions.data[q];
if(r.$$objectId===o.index){o.gridOptions.data.splice(q,1);
break
}}for(var q=0;
q<o.packetListToSave.length;
q++){var r=o.packetListToSave[q];
if(r.$$objectId===o.index){o.packetListToSave.splice(q,1);
break
}}o.resetAddForm(true);
o.flag.editPacket=false;
$("#deleteDialog").modal("hide");
l.removeModalOpenCssAfterModalHide()
};
o.onCancel=function(q){if(q!=null){l.maskLoading();
q.$dirty=false;
o.packetListToSave=[];
o.packetListTodisplay=[];
o.reset("categoryCustom");
o.AddPacketCtrl=this;
this.packetAddFlag=false
}};
o.saveAllPacket=function(u){o.caratDoesNotMatch=false;
var q=[];
var t=undefined;
angular.forEach(o.packetListToSave,function(w){if(!o.caratDoesNotMatch&&w.categoryCustom["carat_of_packet$NM$Double"]!==undefined&&w.categoryCustom["carat_of_packet$NM$Double"]!==null){if(t!==undefined){t=t+parseFloat(w.categoryCustom["carat_of_packet$NM$Double"])
}else{t=parseFloat(w.categoryCustom["carat_of_packet$NM$Double"])
}}else{o.caratDoesNotMatch=true
}q.push({packetCustom:w.categoryCustom,packetDbType:o.packetDbType})
});
if(!o.caratDoesNotMatch){if(o.caratOfLot!==t){o.caratDoesNotMatch=true
}}var r={};
r.invoiceDataBean={};
r.parcelDataBean={};
r.lotDataBean={};
r.invoiceDataBean.id=o.invoiceIdForConstraint;
r.parcelDataBean.id=o.parcelIdForConstraint;
r.lotDataBean.id=o.lotIdForConstraint;
r.packetList=q;
r.id=o.workAllocationId;
if(!o.caratDoesNotMatch){f.create(r,function(w){o.packetListToSave=[];
o.packetListTodisplay=[];
o.listFilled=false;
o.addPacketForm.$dirty=false;
p(true);
o.flag.showAddPage=false
},function(){l.unMaskLoading();
var x="Could not create packet, please try again.";
var w=l.error;
l.addMessage(x,w)
})
}else{l.unMaskLoading();
var v="Carat value does not match, please try again.";
var s=l.error;
l.addMessage(v,s)
}};
o.updatePacket=function(q){l.packetId=angular.copy(q["$$packetId$$"]);
if(!!(l.packetId)){l.unMaskLoading();
o.flag.showUpdatePage=true;
o.addPacketForm.$dirty=false;
e.path("/editpacket");
l.editPacket=true
}};
o.sequenceNumberExists=function(q,s){o.sequenceNumber=q;
s.sequenceNumber.$setValidity("exists",true);
if(q!==undefined&&q!==null){if(o.sequenceNumber!==null&&o.lotIDWithSeqNumber){var r={seqNumber:o.sequenceNumber,lotId:o.lotIdForConstraint};
f.isPacketSequenceNumberExist(r,function(t){if(t.data){s.sequenceNumber.$setValidity("exists",false)
}},function(){l.unMaskLoading();
var u="Could not retrieve sequence number, please try again.";
var t=l.error;
l.addMessage(u,t)
})
}}};
o.hasEditPacketPermission=function(q){o.editPacketFeature=q
}
}])
});