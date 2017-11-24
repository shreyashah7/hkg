define(["hkg","transferstockService","customFieldService","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicForm","accordionCollapse"],function(b,a){b.register.controller("TransferStockController",["$rootScope","$scope","TransferStockService","$timeout","$filter","$location","$window","DynamicFormService","CustomFieldService",function(i,l,f,d,g,e,c,k,j){i.mainMenu="stockLink";
i.childMenu="stockTransfer";
i.activateMenu();
var h={};
l.transferstock=function(){var m=l.gridApi.selection.getSelectedRows()[0];
l.flag.showstockTransferPage=true;
l.flag.stockTransferFlag=false;
l.invoiceId=m["~@description"];
l.invoiceIdForConstraint=m["~@description"];
l.parcelId=m["~@parcelId"];
l.parcelIdForConstraint=m["~@parcelId"];
if(m["~@id"]!==undefined&&m["~@id"]!==null&&m["~@status"]!==undefined&&m["~@status"]!==null){l.packetId=m["~@status"];
l.isPacket=true
}else{l.lotId=m["~@id"];
l.isPacket=false
}l.lotIdForConstraint=m["~@id"];
l.workAllocationId=m["~@workAllocationId"];
l.payload={workAllotmentId:l.workAllocationId,isPacket:l.isPacket};
f.retrieveStockByWorkAllotmentId(l.payload,function(n){if(n.stock!=null){i.maskLoading();
l.transferStock=n.stock;
l.lotId=l.transferStock.id;
l.invoiceId=l.transferStock.description;
l.parcelId=l.transferStock.label;
l.packetId=l.transferStock.status;
l.invoiceCustomFieldData=l.transferStock.custom1;
l.parcelCustomFieldData=l.transferStock.custom3;
l.lotCustomFieldData=l.transferStock.custom4;
l.packetCustomFieldData=l.transferStock.custom5;
j.retrieveDesignationBasedFields("stocktransfer",function(o){l.flag.stockTransferFlag=true;
var t=k.retrieveSectionWiseCustomFieldInfo("invoice");
l.invoiceDbType={};
t.then(function(w){l.generaInvoiceTemplate=w.genralSection;
var v=[];
var u=Object.keys(o).map(function(x,y){angular.forEach(this[x],function(z){if(x==="Invoice#P#"){v.push({Invoice:z})
}})
},o);
l.generaInvoiceTemplate=k.retrieveCustomData(l.generaInvoiceTemplate,v);
l.invoiceCustom=l.invoiceCustomFieldData
},function(u){},function(u){});
var s=k.retrieveSectionWiseCustomFieldInfo("parcel");
l.parcelDbType={};
s.then(function(w){l.generaParcelTemplate=w.genralSection;
var v=[];
var u=Object.keys(o).map(function(x,y){angular.forEach(this[x],function(z){if(x==="Parcel#P#"){v.push({Parcel:z})
}})
},o);
l.generaParcelTemplate=k.retrieveCustomData(l.generaParcelTemplate,v);
l.parcelCustom=l.parcelCustomFieldData
},function(u){},function(u){});
var r=k.retrieveSectionWiseCustomFieldInfo("lot");
l.lotDbType={};
r.then(function(w){l.generalLotTemplate=w.genralSection;
var v=[];
var u=Object.keys(o).map(function(x,y){angular.forEach(this[x],function(z){if(x==="Lot#P#"){v.push({Lot:z})
}})
},o);
l.generalLotTemplate=k.retrieveCustomData(l.generalLotTemplate,v);
l.lotCustom=l.lotCustomFieldData
},function(u){},function(u){});
var q=k.retrieveSectionWiseCustomFieldInfo("packet");
l.packetDbType={};
q.then(function(w){l.generalPacketTemplate=w.genralSection;
var v=[];
var u=Object.keys(o).map(function(x,y){angular.forEach(this[x],function(z){if(x==="Packet#P#"){v.push({Lot:z})
}})
},o);
l.generalPacketTemplate=k.retrieveCustomData(l.generalPacketTemplate,v);
l.packetCustom=l.packetCustomFieldData
},function(u){},function(u){});
var p=k.retrieveSectionWiseCustomFieldInfo("transfer");
l.transferstockDbType={};
p.then(function(w){l.generalTransferstockTemplate=w.genralSection;
var v=[];
var u=Object.keys(o).map(function(x,y){angular.forEach(this[x],function(z){if(x==="Transfer"){v.push({Lot:z})
}})
},o);
l.generalTransferstockTemplate=k.retrieveCustomData(l.generalTransferstockTemplate,v);
if(l.generalTransferstockTemplate!==undefined){angular.forEach(l.stockStaticFields,function(x){if(!l.flag.staticFieldMissing){var y=false;
angular.forEach(l.generalTransferstockTemplate,function(z){if(z.model===x+"$DRP$String"){y=true
}});
if(!y){l.flag.staticFieldMissing=true
}}})
}l.flag.customFieldGenerated=true;
l.editResetFlag=true
},function(u){},function(u){});
i.unMaskLoading()
},function(){i.unMaskLoading();
var p="Failed to retrieve data";
var o=i.error;
i.addMessage(p,o)
})
}})
};
l.initializeData=function(m){if(m){l.currentNodeStocks=[];
l.nodeDetailsInfo=[];
l.stockStaticFields=[];
l.lotStaticFields=[];
l.packetStaticFields=[];
l.dataRetrieved=false;
l.flag={};
l.flag.showstockTransferPage=false;
l.flag.stockTransferFlag=false;
l.flag.staticFieldMissing=false;
l.stockList=[];
l.listFilled=false;
l.invoiceCustom={};
l.parcelCustom={};
l.lotCustom={};
l.packetCustom={};
l.stockCustom={};
l.transferstockCustom={};
l.stockLabelListForUiGrid=[];
l.gridOptions={};
l.gridOptions.multiSelect=false;
l.gridOptions.enableRowSelection=true;
l.gridOptions.enableSelectAll=false;
l.gridOptions.enableFiltering=true;
l.gridOptions.enableSorting=false;
l.gridOptions.columnDefs=[];
l.gridOptions.data=[];
l.flag.rowSelectedflag=false;
l.gridOptions.onRegisterApi=function(o){l.gridApi=o;
o.selection.on.rowSelectionChanged(l,function(p){if(l.gridApi.selection.getSelectedRows().length>0){l.flag.rowSelectedflag=true
}else{l.flag.rowSelectedflag=false
}});
o.selection.on.rowSelectionChangedBatch(l,function(p){if(l.gridApi.selection.getSelectedRows().length>0){l.flag.rowSelectedflag=true
}else{l.flag.rowSelectedflag=false
}})
};
l.gridOptions.isRowSelectable=function(o){if(o.entity.age>30){return false
}else{return true
}};
var n=k.retrieveSearchWiseCustomFieldInfo("stocktransfer");
n.then(function(q){l.dataRetrieved=true;
l.generalSearchTemplate=q.genralSection;
if(l.generalSearchTemplate!=null&&l.generalSearchTemplate.length>0){for(var o=0;
o<l.generalSearchTemplate.length;
o++){var p=l.generalSearchTemplate[o];
h[p.model]=p.featureName;
if(p.fromModel){l.stockLabelListForUiGrid.push({name:p.fromModel,displayName:p.label,minWidth:200})
}else{if(p.toModel){l.stockLabelListForUiGrid.push({name:p.toModel,displayName:p.label,minWidth:200})
}else{if(p.model){l.stockLabelListForUiGrid.push({name:p.model,displayName:p.label,minWidth:200})
}}}}}if(l.generalSearchTemplate!==undefined&&l.generalSearchTemplate!==null){i.maskLoading();
f.retrieveStocks(h,function(s){i.unMaskLoading();
l.flag.generalSearchTemplateFlag=true;
l.nodeDetailsInfo=[];
if(s.dynamicServiceInitBean!==null&&s.dynamicServiceInitBean!==undefined){var r=s.dynamicServiceInitBean;
if(r.nodeAndWorkAllocationIds!==null){l.nodeIdAndWorkAllocationIdsMap=angular.copy(r.nodeAndWorkAllocationIds)
}if(r.mandatoryFields!==null){l.stockStaticFields=r.mandatoryFields
}if(r.diamondsInQueue!==null){l.diamondsInQueue=r.diamondsInQueue
}if(!!r.dynamicServiceInitDataBeans){var t=r.dynamicServiceInitDataBeans;
if(angular.isArray(t)&&t.length>0){angular.forEach(t,function(v){var w={};
w.groupId=v.groupId;
w.groupName=v.groupName;
w.modifier=v.modifier;
w.nodeId=v.nodeId;
w.nodeName=v.nodeName;
l.nodeDetailsInfo.push(w)
})
}}else{l.nodeDetailsInfo=[]
}l.currentActivityNode=l.nodeDetailsInfo[0].nodeId
}if(s.stockList!=null){l.transferStockList=s.stockList;
var u=function(v){angular.forEach(l.transferStockList,function(w){angular.forEach(l.stockLabelListForUiGrid,function(x){if(!w.categoryCustom.hasOwnProperty(x.name)){w.categoryCustom[x.name]="NA"
}else{if(w.categoryCustom.hasOwnProperty(x.name)){if(w.categoryCustom[x.name]===null||w.categoryCustom[x.name]===""||w.categoryCustom[x.name]===undefined){w.categoryCustom[x.name]="NA"
}}}if(w.hasOwnProperty("value")){w.categoryCustom["~@workAllocationId"]=w.value
}if(w.hasOwnProperty("label")){w.categoryCustom["~@parcelId"]=w.label
}if(w.hasOwnProperty("description")){w.categoryCustom["~@description"]=w.description
}if(w.hasOwnProperty("id")){w.categoryCustom["~@id"]=w.id
}if(w.hasOwnProperty("status")){w.categoryCustom["~@status"]=w.status
}});
l.stockList.push(w.categoryCustom)
});
l.updateStockAccordingToNode(l.currentActivityNode)
};
k.convertorForCustomField(l.transferStockList,u)
}},function(r){},function(r){})
}else{l.flag.generalSearchTemplateFlag=false
}},function(o){},function(o){})
}};
l.initializeData(true);
l.initTransferstockForm=function(m){l.transferstockForm=m
};
l.transfer=function(o){i.maskLoading();
l.submitted=true;
l.flag.showstockSellPage=false;
var n=[];
var m={};
m.stockCustom=l.transferstockCustom;
m.stockDbType=l.transferstockDbType;
m.invoiceDataBean={id:l.invoiceId};
m.parcelDataBean={id:l.parcelId};
if(l.lotId!==undefined&&l.lotId!==null&&l.packetId!==undefined&&l.packetId!==null){m.packetDataBean={id:l.packetId};
m.lotDataBean={id:l.lotId}
}else{m.lotDataBean={id:l.lotId}
}l.gridOptions.columnDefs=[];
l.gridOptions.data=[];
l.transferstockForm.$dirty=false;
if(o.$valid){f.transferStock(m,function(p){console.log("success");
var r="Stock transfer successfully.";
var q=i.success;
l.initializeData(true);
i.addMessage(r,q);
l.editResetFlag=false;
l.reset();
i.unMaskLoading()
},function(){console.log("error");
var q="Could not transfer stock, please try again.";
var p=i.error;
l.initializeData(true);
i.addMessage(q,p);
l.editResetFlag=false;
l.reset();
i.unMaskLoading()
})
}};
l.onCanel=function(m){if(m!=null){m.$dirty=false;
l.flag.showstockTransferPage=false
}l.submitted=false;
l.editResetFlag=false;
l.reset()
};
l.reset=function(){var m={};
m=k.retrieveSectionWiseCustomFieldInfo("lot");
l.lotEditDbType={};
m.then(function(n){l.generalLotEditTemplate=n.genralSection;
l.lotEditCustom={};
l.editResetFlag=true
},function(n){},function(n){})
};
l.updateStockAccordingToNode=function(p){if(p!==null&&p!==undefined){l.currentActivityNode=p;
angular.forEach(l.nodeDetailsInfo,function(q){if(q.nodeId===p){l.currentSelectedNodeInfo=angular.copy(q)
}});
if(angular.isDefined(l.nodeIdAndWorkAllocationIdsMap)){var m=[];
var o=0;
var n=l.nodeIdAndWorkAllocationIdsMap[p];
angular.forEach(l.stockList,function(q){if(n.length>0&&n.indexOf(q["~@workAllocationId"])!==-1){q["~@index"]=++o;
m.push(q)
}});
l.currentNodeStocks=angular.copy(m);
if(l.currentNodeStocks.length>0){l.flag.valueRetrieved=true
}else{l.flag.valueRetrieved=false
}if(angular.isDefined(l.gridApi)){l.gridApi.selection.clearSelectedRows()
}l.gridOptions.data=m;
l.gridOptions.columnDefs=l.stockLabelListForUiGrid;
l.listFilled=true;
if(!!l.currentSelectedNodeInfo&&!!l.currentSelectedNodeInfo.modifier){l.gridOptions.isRowSelectable=function(q){if(!!l.diamondsInQueue){if(q.entity["~@index"]>l.diamondsInQueue){return false
}else{return true
}}}
}else{l.gridOptions.isRowSelectable=function(q){if(q.entity["~@index"]>0){return true
}else{return false
}}
}window.setTimeout(function(){$(window).resize();
$(window).resize()
},100)
}else{console.log("Node map not initialized")
}}}
}])
});