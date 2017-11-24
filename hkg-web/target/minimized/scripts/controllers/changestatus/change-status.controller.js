define(["hkg","customFieldService","changestausService","ruleService","activityFlowService","lotService","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicForm","accordionCollapse"],function(a){a.register.controller("ChangeStatusController",["$rootScope","$scope","DynamicFormService","ChangeStausService","CustomFieldService","LotService","$filter",function(i,l,k,m,j,g,e){i.maskLoading();
i.mainMenu="stockLink";
i.childMenu="changestatus";
i.activateMenu();
var b=k.retrieveSectionWiseCustomFieldInfo("invoice");
b.then(function(n){l.invoiceCustomData=angular.copy(n.genralSection)
});
var c=k.retrieveSectionWiseCustomFieldInfo("parcel");
c.then(function(n){l.parcelCustomData=angular.copy(n.genralSection)
});
var d=k.retrieveSectionWiseCustomFieldInfo("lot");
d.then(function(n){l.lotCustomData=angular.copy(n.genralSection)
});
var f=k.retrieveSectionWiseCustomFieldInfo("packet");
f.then(function(n){l.packetCustomData=angular.copy(n.genralSection)
});
var h={};
l.initializeData=function(){var o=function(p){l.rootNodeDesignationIds=p.data.toString()
};
g.retrieveRootNodeDesignationIds(o);
l.flag={};
l.status={};
l.lotIds=[];
l.searchResetFlag=false;
l.packetIds=[];
l.proposedStatus={};
l.proposedstatusList=[];
l.statusChangeDataBean={};
l.submitted=false;
l.submittedFlag=false;
l.flag.statusChangeflag=false;
l.flag.rowSelectedflag=false;
l.flag.multipleLotflag=false;
l.searchedData=[];
l.searchedDataFromDb=[];
l.listFilled=false;
l.lotListTodisplay=[];
l.searchCustom={};
var n=k.retrieveSearchWiseCustomFieldInfo("statuschange");
l.dbType={};
l.stockLabelListForUiGrid=[];
l.gridOptions={};
l.gridOptions.enableFiltering=true;
l.gridOptions.multiSelect=true;
l.gridOptions.enableRowSelection=true;
l.gridOptions.enableSelectAll=true;
l.gridOptions.columnDefs=[];
l.gridOptions.data=[];
l.gridOptions.selectedItems=[];
l.sellStockList=[];
l.stockList=[];
l.mandatoryFieldsByStatusForLot={Rejected:"reason_to_reject_lot$DRP$Long"};
l.mandatoryFieldsByStatusForPacket={Rejected:"reason_to_reject_packet$DRP$Long$POI"};
l.selectedRows=[];
l.flag.displayEditPacketflag=false;
l.flag.displayEditLotflag=false;
l.gridOptions.onRegisterApi=function(p){l.gridApi=p;
p.selection.on.rowSelectionChanged(l,function(q){if(l.gridApi.selection.getSelectedRows().length>0){l.flag.rowSelectedflag=true
}else{l.flag.rowSelectedflag=false
}});
p.selection.on.rowSelectionChangedBatch(l,function(q){if(l.gridApi.selection.getSelectedRows().length>0){l.flag.rowSelectedflag=true
}else{l.flag.rowSelectedflag=false
}})
};
n.then(function(r){l.generalSearchTemplate=r.genralSection;
if(l.generalSearchTemplate!=null&&l.generalSearchTemplate.length>0){for(var p=0;
p<l.generalSearchTemplate.length;
p++){var q=l.generalSearchTemplate[p];
h[q.model]=q.featureName;
if(q.fromModel){l.stockLabelListForUiGrid.push({name:q.fromModel,displayName:q.label,minWidth:200})
}else{if(q.toModel){l.stockLabelListForUiGrid.push({name:q.toModel,displayName:q.label,minWidth:200})
}else{if(q.model){l.stockLabelListForUiGrid.push({name:q.model,displayName:q.label,minWidth:200})
}}}}}l.searchResetFlag=true;
l.stockLabelListForUiGrid.push({name:"status",displayName:"Status",minWidth:200})
},function(p){},function(p){});
l.statusMap=[];
m.retrieveStatusMapAndPraposedStatusMap(function(p){l.statusMap=p;
angular.forEach(l.statusMap,function(r,q){if(q!="Sold"&&q!="Transferred"&&q!="New/Rough"&&q!="$promise"&&q!="$resolved"){l.proposedstatusList.push(q)
}})
})
};
l.retrieveSearchedData=function(){var u=false;
if(Object.getOwnPropertyNames(l.searchCustom).length>0){i.maskLoading();
var q=false;
for(var t in l.searchCustom){if(typeof l.searchCustom[t]==="object"&&l.searchCustom[t]!==null){var r=angular.copy(l.searchCustom[t].toString());
if(typeof r==="string"&&r!==null&&r!==undefined&&r.length>0){q=true;
break
}}if(typeof l.searchCustom[t]==="string"&&l.searchCustom[t]!==null&&l.searchCustom[t]!==undefined&&l.searchCustom[t].length>0){q=true;
break
}if(typeof l.searchCustom[t]==="number"&&!!(l.searchCustom[t])){q=true;
break
}if(typeof l.searchCustom[t]==="boolean"){q=true;
break
}}}if((l.statusChangeDataBean.status!==undefined&&l.statusChangeDataBean.status!==null)||(l.statusChangeDataBean.proposedStatus!==undefined&&l.statusChangeDataBean.proposedStatus!==null)||q){i.maskLoading();
l.stockList=[];
l.gridOptions.columnDefs=[];
l.gridOptions.data=[];
l.statusChangeDataBean.featureCustomMapValue={};
l.map={};
var p={};
var n=k.convertSearchData(l.invoiceCustomData,l.parcelCustomData,l.lotCustomData,l.packetCustomData,angular.copy(l.searchCustom));
angular.forEach(h,function(z,x){var y=n[x];
if(y!==undefined){var w={};
if(!p[z]){w[x]=y;
p[z]=w
}else{var v=p[z];
v[x]=y;
p[z]=v
}}else{y=n["to"+x];
if(y!==undefined){var w={};
if(!p[z]){w["to"+x]=y;
p[z]=w
}else{var v=p[z];
v["to"+x]=y;
p[z]=v
}}y=n["from"+x];
if(y!==undefined){var w={};
if(!p[z]){w["from"+x]=y;
p[z]=w
}else{var v=p[z];
v["from"+x]=y;
p[z]=v
}}}});
l.statusChangeDataBean.featureCustomMapValue=p;
l.statusChangeDataBean.featureMap=h;
m.search(l.statusChangeDataBean,function(v){l.searchedDataFromDb=angular.copy(v);
var w=function(){angular.forEach(l.searchedDataFromDb,function(x){angular.forEach(l.stockLabelListForUiGrid,function(y){if(!x.categoryCustom.hasOwnProperty(y.name)){x.categoryCustom[y.name]="NA"
}else{if(x.categoryCustom.hasOwnProperty(y.name)){if(x.categoryCustom[y.name]===null||x.categoryCustom[y.name]===""||x.categoryCustom[y.name]===undefined){x.categoryCustom[y.name]="NA"
}}}if(x.hasOwnProperty("value")){x.categoryCustom["~@lotid"]=x.value
}if(x.hasOwnProperty("label")){x.categoryCustom["~@parcelid"]=x.label
}if(x.hasOwnProperty("description")){x.categoryCustom["~@invoiceid"]=x.description
}if(x.hasOwnProperty("id")){x.categoryCustom["~@packetid"]=x.id
}if(x.hasOwnProperty("status")){x.categoryCustom.status=x.status
}});
l.stockList.push(x.categoryCustom)
});
l.gridOptions.data=l.stockList;
l.gridOptions.columnDefs=l.stockLabelListForUiGrid;
l.listFilled=true;
l.flag.configSearchFlag=false;
window.setTimeout(function(){$(window).resize();
$(window).resize()
},100)
};
k.convertorForCustomField(l.searchedDataFromDb,w);
i.unMaskLoading()
},function(){var w="Could not retrieve, please try again.";
var v=i.error;
i.addMessage(w,v);
i.unMaskLoading()
})
}else{l.flag.configSearchFlag=true;
var s="Please select atleast one search criteria for search";
var o=i.warning;
i.addMessage(s,o);
i.unMaskLoading()
}};
l.initChangeStatusForm=function(n){l.statusChangeForm=n
};
l.onCanelOfSearch=function(){if(l.statusChangeForm!=null){l.statusChangeForm.$dirty=false
}l.statusChangeForm.$setPristine();
l.listFilled=false;
l.searchResetFlag=false;
l.reset("searchCustom");
l.initializeData();
l.statusChangeForm.$setPristine()
};
l.checkMandatoryFields=function(n){if(n==="Rejected"){if(l.noOfLots>0&&(l.noOfPackets===undefined||l.noOfPackets===0)){angular.forEach(l.mandatoryFieldsByStatusForLot,function(o){if(!l.flag.staticFieldMissing){var p=false;
angular.forEach(l.generalLotEditTemplate,function(q){if(q.model===o){p=true
}});
if(!p){l.flag.staticFieldMissing=true
}}})
}else{if(l.noOfPackets>0&&(l.noOfLots===undefined||l.noOfLots===0)){angular.forEach(l.mandatoryFieldsByStatusForPacket,function(o){if(!l.flag.staticFieldMissing){var p=false;
angular.forEach(l.generalPacketEditTemplate,function(q){if(q.model===o){p=true
}});
if(!p){l.flag.staticFieldMissing=true
}}})
}}}else{l.flag.staticFieldMissing=false
}};
l.changeStatus=function(){l.selectedRows=l.gridApi.selection.getSelectedRows();
l.submitted=true;
l.isMultipleLotsOrPackets=true;
l.generalStatus={};
l.flag.generalStatusFlag=true;
l.generaInvoiceTemplate=[];
l.generaParcelTemplate=[];
l.generalLotTemplate=[];
l.generalLotEditTemplate=[];
l.generalPacketEditTemplate=[];
l.flag.staticFieldMissing=false;
l.noOfLots=0;
l.noOfPackets=0;
l.lotEditCustom={};
l.packetEditCustom={};
l.statusChangeDataBean={};
if(l.statusChangeForm.$valid){l.generalStatus=l.selectedRows[0]["status"];
l.statusListToBeChange=l.statusMap[l.generalStatus];
for(var u=0;
u<l.selectedRows.length;
u++){if(l.selectedRows[u]["~@packetid"]!=null){l.noOfPackets=l.noOfPackets+1
}else{if(l.selectedRows[u]["~@packetid"]===null&&l.selectedRows[u]["~@lotid"]!=null){l.noOfLots=l.noOfLots+1
}}if(!!l.generalStatus&&l.generalStatus!=l.selectedRows[u]["status"]){delete l.selectedRows;
l.selectedRows=[];
l.gridApi.selection.clearSelectedRows();
l.flag.generalStatusFlag=false;
var t="Only select either lots or packets of same status";
var p=i.warning;
i.addMessage(t,p);
l.flag.statusChangeflag=false
}}if(l.noOfLots>0&&l.noOfPackets>0){delete l.selectedRows;
l.selectedRows=[];
l.gridApi.selection.clearSelectedRows();
var t="Only select either lots or packets";
var p=i.warning;
i.addMessage(t,p);
l.flag.statusChangeflag=false
}else{if(l.noOfLots>0&&l.noOfPackets===0&&l.flag.generalStatusFlag){l.flag.displayEditLotflag=true;
l.flag.statusChangeflag=true
}else{if(l.noOfPackets>0&&l.noOfLots===0&&l.flag.generalStatusFlag){l.flag.displayEditPacketflag=true;
l.flag.statusChangeflag=true
}}}if(l.selectedRows.length>=1){l.flag.singlePacket=false;
if(l.selectedRows.length>1){l.flag.multipleLotflag=true;
if(l.selectedRows[0]["~@packetid"]!=null){l.flag.singlePacket=true
}}else{l.flag.multipleLotflag=false
}l.parentGridOptions={};
l.parentGridOptions.columnDefs=[];
l.parentGridOptions.data=[];
var s={};
var o=[];
var r=[];
var q=[];
var n=[];
l.isMultipleLotsOrPackets=false;
for(var u=0;
u<l.selectedRows.length;
u++){if(l.selectedRows[u]["~@packetid"]!=null){l.packetIds.push(l.selectedRows[u]["~@packetid"])
}else{if(l.selectedRows[u]["~@packetid"]===null&&l.selectedRows[u]["~@lotid"]!=null){l.lotIds.push(l.selectedRows[u]["~@lotid"])
}}}l.payload={};
l.payload.lotids=l.lotIds;
l.payload.packetids=l.packetIds;
i.maskLoading();
j.retrieveDesignationBasedFields("statuschange",function(v){var w={};
w=k.retrieveSectionWiseCustomFieldInfo("invoice");
l.invoiceDbType={};
w.then(function(z){l.generaInvoiceTemplate=z.genralSection;
var y=[];
var x=Object.keys(v).map(function(B,C){angular.forEach(this[B],function(D){if(B==="Invoice#P#"){y.push({Invoice:D})
}})
},v);
l.generaInvoiceTemplate=k.retrieveCustomData(l.generaInvoiceTemplate,y);
angular.forEach(l.generaInvoiceTemplate,function(B){if(B.model){o.push(B.model);
l.parentGridOptions.columnDefs.push({name:B.model,displayName:B.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}});
if(o.length>0){s.invoiceDbFieldName=o
}var A={};
A=k.retrieveSectionWiseCustomFieldInfo("parcel");
l.parcelDbType={};
A.then(function(E){l.generaParcelTemplate=E.genralSection;
var D=[];
var B=Object.keys(v).map(function(F,G){angular.forEach(this[F],function(H){if(F==="Parcel#P#"){D.push({Parcel:H})
}})
},v);
l.generaParcelTemplate=k.retrieveCustomData(l.generaParcelTemplate,D);
angular.forEach(l.generaParcelTemplate,function(F){if(F.model){r.push(F.model);
l.parentGridOptions.columnDefs.push({name:F.model,displayName:F.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}});
if(r.length>0){s.parcelDbFieldName=r
}var C={};
C=k.retrieveSectionWiseCustomFieldInfo("lot");
l.lotDbType={};
C.then(function(I){l.generalLotTemplate=I.genralSection;
var G=[];
var F=Object.keys(v).map(function(J,K){angular.forEach(this[J],function(L){if(J==="Lot#P#"){G.push({Lot:L})
}})
},v);
l.generalLotTemplate=k.retrieveCustomData(l.generalLotTemplate,G);
angular.forEach(l.generalLotTemplate,function(J){if(J.model){q.push(J.model);
l.parentGridOptions.columnDefs.push({name:J.model,displayName:J.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}});
if(q.length>0){s.lotDbFieldName=q
}var H={};
H=k.retrieveSectionWiseCustomFieldInfo("packet");
l.packetDbType={};
H.then(function(M){l.generalPacketTemplate=M.genralSection;
var K=[];
var J=Object.keys(v).map(function(N,O){angular.forEach(this[N],function(P){if(N==="Packet#P#"){K.push({Packet:P})
}})
},v);
l.generalPacketTemplate=k.retrieveCustomData(l.generalPacketTemplate,K);
angular.forEach(l.generalPacketTemplate,function(N){if(N.model){n.push(N.model);
l.parentGridOptions.columnDefs.push({name:N.model,displayName:N.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}});
if(n.length>0){s.packetDbFieldName=n
}var L={};
L=k.retrieveSectionWiseCustomFieldInfo("lot");
l.lotEditDbType={};
L.then(function(P){l.generalLotEditTemplate=P.genralSection;
var O=[];
var N=Object.keys(v).map(function(R,S){angular.forEach(this[R],function(T){if(R==="Lot"){O.push({Lot:T})
}})
},v);
l.generalLotEditTemplate=k.retrieveCustomData(l.generalLotEditTemplate,O);
l.editResetFlag=true;
if(l.generalLotEditTemplate.length>0){l.flag.customFieldGenerated=true
}var Q={};
Q=k.retrieveSectionWiseCustomFieldInfo("packet");
l.packetEditDbType={};
Q.then(function(T){l.generalPacketEditTemplate=T.genralSection;
var U=[];
var R=Object.keys(v).map(function(V,W){angular.forEach(this[V],function(X){if(V==="Packet"){U.push({Packet:X})
}})
},v);
l.generalPacketEditTemplate=k.retrieveCustomData(l.generalPacketEditTemplate,U);
l.editResetFlag=true;
if(l.generalPacketEditTemplate.length>0){l.flag.customFieldGenerated=true
}var S={payload:l.payload,fields:s};
m.retrieveStockByLotIdOrPacketId(S,function(V){l.stocks=V;
if(l.flag.multipleLotflag===false){var W=l.stocks[0];
l.invoiceCustom=W.stock.custom3;
l.parcelCustom=W.stock.custom4;
l.lotCustom=W.stock.custom1;
l.packetCustom=W.stock.custom5
}else{if(l.stocks.length>1&&l.flag.multipleLotflag===true){l.gridRecords=[];
angular.forEach(l.stocks,function(Y){console.log("currentStockDetails :"+JSON.stringify(Y.stock));
var Z={};
if(Y.stock.custom1!==undefined&&Y.stock.custom1!==null){Z=angular.copy(Y.stock.custom1)
}if(Y.stock.custom3!==undefined&&Y.stock.custom3!==null){angular.forEach(Y.stock.custom3,function(ab,aa){Z[aa]=ab
})
}if(Y.stock.custom4!==undefined&&Y.stock.custom4!==null){angular.forEach(Y.stock.custom4,function(ab,aa){Z[aa]=ab
})
}if(Y.stock.custom5!==undefined&&Y.stock.custom5!==null){angular.forEach(Y.stock.custom5,function(ab,aa){Z[aa]=ab
})
}l.gridRecords.push({categoryCustom:Z})
});
console.log("$scope.gridRecords :"+JSON.stringify(l.gridRecords));
var X=function(Y){angular.forEach(l.gridRecords,function(Z){l.parentGridOptions.data.push(Z.categoryCustom)
})
};
k.convertorForCustomField(l.gridRecords,X)
}}})
},function(R){},function(R){})
},function(N){},function(N){})
},function(J){},function(J){})
},function(F){},function(F){})
},function(B){},function(B){})
},function(x){},function(x){});
l.searchResetFlag=false;
l.reset("searchCustom");
i.unMaskLoading()
},function(){i.unMaskLoading();
var w="Failed to retrieve data";
var v=i.error;
i.addMessage(w,v)
})
}}};
l.onBack=function(n){if(n!=null){n.$dirty=false;
l.flag.statusChangeflag=false
}l.initializeData();
l.editResetFlag=false;
l.reset("changeStatusTemplate");
l.submitted=false
};
l.onSave=function(q){l.submittedFlag=true;
if((!jQuery.isEmptyObject(l.lotEditCustom)||!jQuery.isEmptyObject(l.packetEditCustom))||(l.statusChangeDataBean.statusToBeChange!==undefined&&l.statusChangeDataBean.statusToBeChange!==null)){i.maskLoading();
l.submitted=true;
l.submittedFlag=true;
l.flag.statusChangeflag=false;
var n=[];
var p={};
p.lotCustom=l.lotEditCustom;
p.packetCustom=l.packetEditCustom;
p.lotdbType=l.lotEditDbType;
p.packetdbType=l.packetEditDbType;
p.lotIds=l.lotIds;
p.packetIds=l.packetIds;
p.status=l.statusChangeDataBean.statusToBeChange;
l.statusChangeForm.$dirty=false;
if(q.$valid){m.onSave(p,function(s){var u="Status changed successfully.";
var t=i.success;
l.initializeData();
l.editResetFlag=false;
l.reset("changeStatusTemplate");
i.addMessage(u,t);
i.unMaskLoading()
},function(){var t="Could not changed status, please try again.";
var s=i.error;
l.initializeData();
i.addMessage(t,s);
i.unMaskLoading()
})
}}else{var r="Please select Status to be change.";
var o=i.warning;
i.addMessage(r,o)
}};
l.reset=function(p){if(p==="searchCustom"){l.searchCustom={};
var n=k.retrieveSearchWiseCustomFieldInfo("statuschange");
l.dbType={};
n.then(function(r){l.generalSearchTemplate=r.genralSection;
l.searchResetFlag=true
},function(r){},function(r){})
}else{if(p==="changeStatusTemplate"){var o={};
o=k.retrieveSectionWiseCustomFieldInfo("lot");
l.lotEditDbType={};
o.then(function(r){l.generalLotEditTemplate=r.genralSection;
l.lotEditCustom={};
l.editResetFlag=true
},function(r){},function(r){});
var q={};
q=k.retrieveSectionWiseCustomFieldInfo("packet");
l.packetEditDbType={};
q.then(function(r){l.generalPacketEditTemplate=r.genralSection;
l.packetEditCustom={};
l.editResetFlag=true
},function(r){},function(r){})
}}};
i.unMaskLoading()
}])
});