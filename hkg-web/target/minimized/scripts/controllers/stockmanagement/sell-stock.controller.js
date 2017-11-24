define(["hkg","sellstockService","customFieldService","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicForm","accordionCollapse"],function(a,b){a.register.controller("SellStockController",["$rootScope","$scope","SellStockService","DynamicFormService","CustomFieldService",function(c,e,g,d,h){c.mainMenu="stockLink";
c.childMenu="sellstock";
c.activateMenu();
var f={};
e.sellstock=function(){var i=e.gridApi.selection.getSelectedRows()[0];
e.flag.showstockSellPage=true;
e.flag.stockSellFlag=false;
e.invoiceId=i["~@description"];
e.invoiceIdForConstraint=i["~@description"];
e.parcelId=i["~@parcelId"];
e.parcelIdForConstraint=i["~@parcelId"];
if(i["~@id"]!==undefined&&i["~@id"]!==null&&i["~@status"]!==undefined&&i["~@status"]!==null){e.packetId=i["~@status"];
e.isPacket=true
}else{e.lotId=i["~@id"];
e.isPacket=false
}e.lotIdForConstraint=i["~@id"];
e.workAllocationId=i["~@workAllocationId"];
e.payload={workAllotmentId:e.workAllocationId,isPacket:e.isPacket};
g.retrieveStockByWorkAllotmentId(e.payload,function(j){if(j.stock!==null){c.maskLoading();
e.sellStock=j.stock;
e.lotId=e.sellStock.id;
e.invoiceId=e.sellStock.description;
e.parcelId=e.sellStock.label;
e.packetId=e.sellStock.status;
e.invoiceCustomFieldData=e.sellStock.custom1;
e.parcelCustomFieldData=e.sellStock.custom3;
e.lotCustomFieldData=e.sellStock.custom4;
e.packetCustomFieldData=e.sellStock.custom5;
h.retrieveDesignationBasedFields("stocksell",function(k){e.flag.stockSellFlag=true;
var p=d.retrieveSectionWiseCustomFieldInfo("invoice");
e.invoiceDbType={};
p.then(function(s){e.generaInvoiceTemplate=s.genralSection;
var r=[];
var q=Object.keys(k).map(function(t,u){angular.forEach(this[t],function(v){if(t==="Invoice#P#"){r.push({Invoice:v})
}})
},k);
e.generaInvoiceTemplate=d.retrieveCustomData(e.generaInvoiceTemplate,r);
e.invoiceCustom=e.invoiceCustomFieldData
},function(q){},function(q){});
var n=d.retrieveSectionWiseCustomFieldInfo("parcel");
e.parcelDbType={};
n.then(function(s){e.generaParcelTemplate=s.genralSection;
var r=[];
var q=Object.keys(k).map(function(t,u){angular.forEach(this[t],function(v){if(t==="Parcel#P#"){r.push({Parcel:v})
}})
},k);
e.generaParcelTemplate=d.retrieveCustomData(e.generaParcelTemplate,r);
e.parcelCustom=e.parcelCustomFieldData
},function(q){},function(q){});
var m=d.retrieveSectionWiseCustomFieldInfo("lot");
e.lotDbType={};
m.then(function(s){e.generalLotTemplate=s.genralSection;
var r=[];
var q=Object.keys(k).map(function(t,u){angular.forEach(this[t],function(v){if(t==="Lot#P#"){r.push({Lot:v})
}})
},k);
e.generalLotTemplate=d.retrieveCustomData(e.generalLotTemplate,r);
e.lotCustom=e.lotCustomFieldData
},function(q){},function(q){});
var l=d.retrieveSectionWiseCustomFieldInfo("packet");
e.packetDbType={};
l.then(function(s){e.generalPacketTemplate=s.genralSection;
var r=[];
var q=Object.keys(k).map(function(t,u){angular.forEach(this[t],function(v){if(t==="Packet#P#"){r.push({Lot:v})
}})
},k);
e.generalPacketTemplate=d.retrieveCustomData(e.generalPacketTemplate,r);
e.packetCustom=e.packetCustomFieldData
},function(q){},function(q){});
var o=d.retrieveSectionWiseCustomFieldInfo("sell");
e.sellstockDbType={};
o.then(function(s){e.generalSellstockTemplate=s.genralSection;
var r=[];
var q=Object.keys(k).map(function(t,u){angular.forEach(this[t],function(v){if(t==="Sell"){r.push({Lot:v})
}})
},k);
e.generalSellstockTemplate=d.retrieveCustomData(e.generalSellstockTemplate,r);
if(e.generalSellstockTemplate!==undefined){angular.forEach(e.stockStaticFields,function(t){if(!e.flag.staticFieldMissing){var u=false;
angular.forEach(e.generalSellstockTemplate,function(v){if(v.model===t+"$DRP$String"){u=true
}});
if(!u){e.flag.staticFieldMissing=true
}}})
}e.flag.customFieldGenerated=true;
e.editResetFlag=true
},function(q){},function(q){});
c.unMaskLoading()
},function(){c.unMaskLoading();
var l="Failed to retrieve data";
var k=c.error;
c.addMessage(l,k)
})
}})
};
e.initializeData=function(i){if(i){e.currentNodeStocks=[];
e.nodeDetailsInfo=[];
e.stockStaticFields=[];
e.lotStaticFields=[];
e.packetStaticFields=[];
e.dataRetrieved=false;
e.flag={};
e.flag.showstockSellPage=false;
e.flag.stockSellFlag=false;
e.flag.staticFieldMissing=false;
e.stockList=[];
e.listFilled=false;
e.invoiceCustom={};
e.parcelCustom={};
e.lotCustom={};
e.packetCustom={};
e.sellstockCustom={};
e.stockLabelListForUiGrid=[];
e.gridOptions={};
e.gridOptions.multiSelect=false;
e.gridOptions.enableRowSelection=true;
e.gridOptions.enableSelectAll=false;
e.gridOptions.enableSorting=false;
e.gridOptions.enableFiltering=true;
e.gridOptions.columnDefs=[];
e.gridOptions.data=[];
e.sellStockList=[];
e.generalSearchTemplate=[];
e.stockLabelListForUiGrid=[];
e.flag.rowSelectedflag=false;
e.gridOptions.onRegisterApi=function(k){e.gridApi=k;
k.selection.on.rowSelectionChanged(e,function(l){if(e.gridApi.selection.getSelectedRows().length>0){e.flag.rowSelectedflag=true
}else{e.flag.rowSelectedflag=false
}});
k.selection.on.rowSelectionChangedBatch(e,function(l){if(e.gridApi.selection.getSelectedRows().length>0){e.flag.rowSelectedflag=true
}else{e.flag.rowSelectedflag=false
}})
};
var j=d.retrieveSearchWiseCustomFieldInfo("stocksell");
j.then(function(m){e.dataRetrieved=true;
e.generalSearchTemplate=m.genralSection;
if(e.generalSearchTemplate!=null&&e.generalSearchTemplate.length>0){for(var k=0;
k<e.generalSearchTemplate.length;
k++){var l=e.generalSearchTemplate[k];
f[l.model]=l.featureName;
if(l.fromModel){e.stockLabelListForUiGrid.push({name:l.fromModel,displayName:l.label,minWidth:200})
}else{if(l.toModel){e.stockLabelListForUiGrid.push({name:l.toModel,displayName:l.label,minWidth:200})
}else{if(l.model){e.stockLabelListForUiGrid.push({name:l.model,displayName:l.label,minWidth:200})
}}}}}if(!!e.generalSearchTemplate){e.flag.generalSearchTemplateFlag=true;
c.maskLoading();
g.retrieveSellstocks(f,function(o){c.unMaskLoading();
e.nodeDetailsInfo=[];
if(o.dynamicServiceInitBean!==null&&o.dynamicServiceInitBean!==undefined){var n=o.dynamicServiceInitBean;
if(n.nodeAndWorkAllocationIds!==null){e.nodeIdAndWorkAllocationIdsMap=angular.copy(n.nodeAndWorkAllocationIds)
}if(n.mandatoryFields!==null){e.stockStaticFields=n.mandatoryFields
}if(n.diamondsInQueue!==null){e.diamondsInQueue=n.diamondsInQueue
}if(!!n.dynamicServiceInitDataBeans){var p=n.dynamicServiceInitDataBeans;
if(angular.isArray(p)&&p.length>0){angular.forEach(p,function(r){var s={};
s.groupId=r.groupId;
s.groupName=r.groupName;
s.modifier=r.modifier;
s.nodeId=r.nodeId;
s.nodeName=r.nodeName;
e.nodeDetailsInfo.push(s)
})
}}else{e.nodeDetailsInfo=[]
}e.currentActivityNode=e.nodeDetailsInfo[0].nodeId
}if(o.stockList!=null){e.sellStockList=o.stockList;
var q=function(r){angular.forEach(e.sellStockList,function(s){angular.forEach(e.stockLabelListForUiGrid,function(t){if(!s.categoryCustom.hasOwnProperty(t.name)){s.categoryCustom[t.name]="NA"
}else{if(s.categoryCustom.hasOwnProperty(t.name)){if(s.categoryCustom[t.name]===null||s.categoryCustom[t.name]===""||s.categoryCustom[t.name]===undefined){s.categoryCustom[t.name]="NA"
}}}if(s.hasOwnProperty("value")){s.categoryCustom["~@workAllocationId"]=s.value
}if(s.hasOwnProperty("label")){s.categoryCustom["~@parcelId"]=s.label
}if(s.hasOwnProperty("description")){s.categoryCustom["~@description"]=s.description
}if(s.hasOwnProperty("id")){s.categoryCustom["~@id"]=s.id
}if(s.hasOwnProperty("status")){s.categoryCustom["~@status"]=s.status
}});
e.stockList.push(s.categoryCustom)
});
e.updateStockAccordingToNode(e.currentActivityNode)
};
d.convertorForCustomField(e.sellStockList,q)
}},function(n){},function(n){})
}else{e.flag.generalSearchTemplateFlag=false
}},function(k){},function(k){})
}};
e.initializeData(true);
e.initSellstockForm=function(i){e.sellstockForm=i
};
e.sell=function(i){c.maskLoading();
e.submitted=true;
e.flag.showstockSellPage=false;
var k=[];
var j={};
j.stockCustom=e.sellstockCustom;
j.stockDbType=e.sellstockDbType;
j.invoiceDataBean={id:e.invoiceId};
j.parcelDataBean={id:e.parcelId};
if(e.lotId!==undefined&&e.lotId!==null&&e.packetId!==undefined&&e.packetId!==null){j.packetDataBean={id:e.packetId};
j.lotDataBean={id:e.lotId}
}else{j.lotDataBean={id:e.lotId}
}e.gridOptions.columnDefs=[];
e.gridOptions.data=[];
e.sellstockForm.$dirty=false;
if(i.$valid){g.sellStock(j,function(l){var n="Stock sell successfully.";
var m=c.success;
e.initializeData(true);
c.addMessage(n,m);
e.editResetFlag=false;
e.reset();
c.unMaskLoading()
},function(){var m="Could not sell stock, please try again.";
var l=c.error;
e.initializeData(true);
c.addMessage(m,l);
e.editResetFlag=false;
e.reset();
c.unMaskLoading()
})
}};
e.onCanel=function(i){if(i!=null){i.$dirty=false;
e.flag.showstockSellPage=false
}e.submitted=false;
e.editResetFlag=false;
e.reset()
};
e.reset=function(){var i={};
i=d.retrieveSectionWiseCustomFieldInfo("lot");
e.lotEditDbType={};
i.then(function(j){e.generalLotEditTemplate=j.genralSection;
e.lotEditCustom={};
e.editResetFlag=true
},function(j){},function(j){})
};
e.updateStockAccordingToNode=function(l){if(l!==null&&l!==undefined){e.currentActivityNode=l;
angular.forEach(e.nodeDetailsInfo,function(m){if(m.nodeId===l){e.currentSelectedNodeInfo=angular.copy(m)
}});
if(angular.isDefined(e.nodeIdAndWorkAllocationIdsMap)){var i=[];
var k=0;
var j=e.nodeIdAndWorkAllocationIdsMap[l];
angular.forEach(e.stockList,function(m){if(j.length>0&&j.indexOf(m["~@workAllocationId"])!==-1){m["~@index"]=++k;
i.push(m)
}});
e.currentNodeStocks=angular.copy(i);
if(e.currentNodeStocks.length>0){e.flag.valueRetrieved=true
}else{e.flag.valueRetrieved=false
}if(angular.isDefined(e.gridApi)){e.gridApi.selection.clearSelectedRows()
}e.gridOptions.data=i;
e.gridOptions.columnDefs=e.stockLabelListForUiGrid;
e.listFilled=true;
if(!!e.currentSelectedNodeInfo&&!!e.currentSelectedNodeInfo.modifier){e.gridOptions.isRowSelectable=function(m){if(!!e.diamondsInQueue){if(m.entity["~@index"]>e.diamondsInQueue){return false
}else{return true
}}}
}else{e.gridOptions.isRowSelectable=function(m){if(m.entity["~@index"]>0){return true
}else{return false
}}
}window.setTimeout(function(){$(window).resize();
$(window).resize()
},100)
}else{console.log("Node map not initialized")
}}}
}])
});