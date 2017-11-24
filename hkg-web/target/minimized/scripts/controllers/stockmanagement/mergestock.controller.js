define(["hkg","customFieldService","mergestockService","ngload!uiGrid","addMasterValue","customsearch.directive","splitstockService","printBarcodeValue","dynamicForm","lotService","accordionCollapse"],function(a){a.register.controller("MergeStockController",["$rootScope","$scope","DynamicFormService","MergeStockService","CustomFieldService","LotService","SplitStockService",function(b,d,c,h,g,f,e){b.maskLoading();
b.activateMenu();
d.stockdataflag=false;
d.featureMap={};
d.flag={};
d.searchCustom={};
d.initializeData=function(){d.nodeDetailsInfo=[];
d.flag={};
d.flag.showstockPage=false;
d.stockList=[];
d.listFilled=false;
d.flag.rowSelectedflag=false;
d.stockLabelListForUiGrid=[];
d.gridOptions={};
d.gridOptions.enableFiltering=true;
d.gridOptions.multiSelect=true;
d.gridOptions.columnDefs=[];
d.gridOptions.data=[];
d.gridOptions.onRegisterApi=function(j){d.gridApi=j;
j.selection.on.rowSelectionChanged(d,function(n){if(d.gridApi.selection.getSelectedRows().length>1){d.flag.rowSelectedflag=true
}else{d.flag.rowSelectedflag=false
}if(d.gridApi.selection.getSelectedRows().length>1){var k=null;
for(var l=0;
l<d.gridApi.selection.getSelectedRows().length;
l++){var m=d.gridApi.selection.getSelectedRows()[l];
if(k===null){k=m["~@haveValue"]
}else{if(k!==m["~@haveValue"]){d.flag.haveValueFlag=false;
break
}else{d.flag.haveValueFlag=true
}}}}});
j.selection.on.rowSelectionChangedBatch(d,function(n){if(d.gridApi.selection.getSelectedRows().length>1){d.flag.rowSelectedflag=true
}else{d.flag.rowSelectedflag=false
}if(d.gridApi.selection.getSelectedRows().length>1){var k=null;
for(var l=0;
l<d.gridApi.selection.getSelectedRows().length;
l++){var m=d.gridApi.selection.getSelectedRows()[l];
if(k===null){k=m["~@haveValue"]
}else{if(k!==m["~@haveValue"]){d.flag.haveValueFlag=false;
break
}else{d.flag.haveValueFlag=true
}}}}})
};
d.searchedStockList=[];
d.generalSearchTemplate=[];
d.stockLabelListForUiGrid=[];
var i=c.retrieveSearchWiseCustomFieldInfo("stockmerge");
i.then(function(m){d.generalSearchTemplate=m.genralSection;
if(d.generalSearchTemplate!==null&&d.generalSearchTemplate!==undefined){for(var j=0;
j<d.generalSearchTemplate.length;
j++){var l=d.generalSearchTemplate[j];
d.featureMap[l.model]=l.featureName;
if(l.fromModel){d.stockLabelListForUiGrid.push({name:l.fromModel,displayName:l.label,minWidth:200})
}else{if(l.toModel){d.stockLabelListForUiGrid.push({name:l.toModel,displayName:l.label,minWidth:200})
}else{if(l.model){d.stockLabelListForUiGrid.push({name:l.model,displayName:l.label,minWidth:200})
}}}}}d.searchResetFlag=true;
d.map={};
var k={};
angular.forEach(d.featureMap,function(o,n){if(!k[o]){k[o]=[]
}k[o].push(n)
});
h.retrieveSearchedLotsAndPacketsNew(k,function(o){d.nodeDetailsInfo=[];
d.searchedStockList=angular.copy(o.stockList);
if(d.generalSearchTemplate===null||d.generalSearchTemplate===undefined){d.flag.configSearchFlag=true
}else{if(o.dynamicServiceInitBean!==null&&o.dynamicServiceInitBean!==undefined){var n=o.dynamicServiceInitBean;
if(n.nodeAndWorkAllocationIds!==null){d.nodeIdAndWorkAllocationIdsMap=angular.copy(n.nodeAndWorkAllocationIds)
}if(n.mandatoryFields!==null){d.stockStaticFields=n.mandatoryFields
}if(n.diamondsInQueue!==null){d.diamondsInQueue=n.diamondsInQueue
}if(!!n.dynamicServiceInitDataBeans){var p=n.dynamicServiceInitDataBeans;
if(angular.isArray(p)&&p.length>0){angular.forEach(p,function(r){var s={};
s.groupId=r.groupId;
s.groupName=r.groupName;
s.modifier=r.modifier;
s.nodeId=r.nodeId;
s.nodeName=r.nodeName;
d.nodeDetailsInfo.push(s)
})
}if(d.nodeDetailsInfo.length>1){d.flag.multipleIdInvolved=true
}else{d.flag.multipleIdInvolved=false
}}if(d.nodeDetailsInfo.length>0){d.currentActivityNode=d.nodeDetailsInfo[0].nodeId
}if(d.currentActivityNode!==undefined&&d.currentActivityNode!==null){e.retrieveNextNodeDesignationIds(d.currentActivityNode,function(r){d.designationIdForAllot=undefined;
d.designationIdForInStock=undefined;
if(r.data!==null){if(r.data.forAllotTo!==undefined&&r.data.forAllotTo!==null){d.designationIdForAllot=r.data.forAllotTo.toString()
}if(r.data.forIssue!==undefined&&r.data.forIssue!==null){d.designationIdForInStock=r.data.forIssue.toString()
}}},function(r){console.log("failure----")
})
}}var q=function(r){angular.forEach(d.searchedStockList,function(s){angular.forEach(d.stockLabelListForUiGrid,function(t){if(!s.categoryCustom.hasOwnProperty(t.name)){s.categoryCustom[t.name]="NA"
}else{if(s.categoryCustom.hasOwnProperty(t.name)){if(s.categoryCustom[t.name]===null||s.categoryCustom[t.name]===""||s.categoryCustom[t.name]===undefined){s.categoryCustom[t.name]="NA"
}}}if(s.hasOwnProperty("value")){s.categoryCustom["~@workallotmentid"]=s.value
}if(s.hasOwnProperty("label")){s.categoryCustom["~@parcelid"]=s.label
}if(s.hasOwnProperty("description")){s.categoryCustom["~@invoiceid"]=s.description
}if(s.hasOwnProperty("id")){s.categoryCustom["~@lotid"]=s.id
}if(s.hasOwnProperty("status")){s.categoryCustom["~@packetid"]=s.status
}if(s.hasOwnProperty("lotId")){s.categoryCustom["~@lotNumber"]=s.lotId
}if(s.hasOwnProperty("parcelId")){s.categoryCustom["~@parcelNumber"]=s.parcelId
}if(s.hasOwnProperty("custom1")){s.categoryCustom["~@haveValue"]=s.custom1.haveValue
}});
d.stockList.push(s.categoryCustom)
});
d.updateStockAccordingToNode(d.currentActivityNode);
d.flag.configSearchFlag=false
};
c.convertorForCustomField(d.searchedStockList,q)
}})
},function(j){},function(j){})
};
d.updateStockAccordingToNode=function(k){if(k!==null&&k!==undefined){d.currentActivityNode=k;
angular.forEach(d.nodeDetailsInfo,function(o){if(o.nodeId===k){var l=o.modifier;
if(l!==null){if(l.indexOf("|")>-1){var m=l.split("|");
for(var n=0;
n<m.length;
n++){if(m[n]==="Q"){d.isQuingRequired=true
}}}else{if(l==="AA"||l==="MA"){d.typeOfAllocation=l
}}}}});
if(angular.isDefined(d.nodeIdAndWorkAllocationIdsMap)){var i=[];
var j=d.nodeIdAndWorkAllocationIdsMap[k];
angular.forEach(d.stockList,function(m,l){if(j.length>0&&j.indexOf(m["~@workallotmentid"])!==-1){m["~@index"]=l+1;
i.push(m)
}});
d.currentNodeStocks=angular.copy(i);
if(angular.isDefined(d.gridApi)){d.gridApi.selection.clearSelectedRows()
}d.gridOptions.data=i;
d.gridOptions.columnDefs=d.stockLabelListForUiGrid;
d.listFilled=true;
if(!!d.isQuingRequired){d.gridOptions.enableSorting=false;
d.gridOptions.isRowSelectable=function(l){if(!!d.diamondsInQueue){if(l.entity["~@index"]>d.diamondsInQueue){return false
}else{return true
}}else{if(l.entity["~@index"]>0){return true
}else{return false
}}}
}}else{console.log("Node map not initialized")
}if(d.currentActivityNode!==undefined&&d.currentActivityNode!==null){e.retrieveNextNodeDesignationIds(d.currentActivityNode,function(l){d.designationIdForAllot=undefined;
d.designationIdForInStock=undefined;
if(l.data!==null){if(l.data.forAllotTo!==undefined&&l.data.forAllotTo!==null){d.designationIdForAllot=l.data.forAllotTo.toString()
}if(l.data.forIssue!==undefined&&l.data.forIssue!==null){d.designationIdForInStock=l.data.forIssue.toString()
}}},function(l){console.log("failure----")
})
}}};
d.setToPrintData=function(i){if(!!i){d.toPrint=i
}};
d.onBack=function(){d.stockdataflag=false;
d.initializeData();
delete d.toPrint
};
d.onCanelOfSearch=function(){delete d.toPrint;
d.gridApi.selection.clearSelectedRows()
};
d.mergeStockNext=function(){if(d.flag.haveValueFlag){d.selectedRows=d.gridApi.selection.getSelectedRows();
d.noOfLots=0;
d.noOfPackets=0;
for(var l=0;
l<d.selectedRows.length;
l++){if(d.selectedRows[l]["~@packetid"]!==null){d.noOfPackets=d.noOfPackets+1
}else{if(d.selectedRows[l]["~@packetid"]===null&&d.selectedRows[l]["~@lotid"]!==null){d.noOfLots=d.noOfLots+1
}}if(d.noOfLots>0&&d.noOfPackets>0){break
}}if(d.noOfLots>0&&d.noOfPackets>0){delete d.selectedRows;
d.selectedRows=[];
d.gridApi.selection.clearSelectedRows();
var k="Select either lots or packets";
var i=b.warning;
b.addMessage(k,i)
}else{if(d.noOfLots>0&&d.noOfPackets===0){d.flag.type="Lot";
d.flag.displayEditLotflag=true
}else{if(d.noOfPackets>0&&d.noOfLots===0){d.flag.type="Packet";
d.flag.displayEditPacketflag=true
}}}if((d.noOfLots>0&&d.noOfPackets===0)||(d.noOfPackets>0&&d.noOfLots===0)){d.parentList=[];
var m=[];
d.allotmentIds=[];
d.idsToMerge=[];
for(var l=0;
l<d.selectedRows.length;
l++){if(d.noOfLots>0){if(m.indexOf(d.selectedRows[l]["~@parcelNumber"])===-1){d.parentList.push({id:d.selectedRows[l]["~@parcelid"],text:d.selectedRows[l]["~@parcelNumber"]});
m.push(d.selectedRows[l]["~@parcelNumber"])
}d.idsToMerge.push(d.selectedRows[l]["~@lotid"])
}else{if(d.noOfPackets>0){if(m.indexOf(d.selectedRows[l]["~@lotNumber"])===-1){d.parentList.push({id:d.selectedRows[l]["~@lotid"],text:d.selectedRows[l]["~@lotNumber"]});
m.push(d.selectedRows[l]["~@lotNumber"])
}d.idsToMerge.push(d.selectedRows[l]["~@packetid"])
}}d.flag.multipleParents=true;
d.allotmentIds.push(d.selectedRows[l]["~@workallotmentid"])
}var j={};
j.workAllotmentId=d.allotmentIds;
if(d.flag.type==="Lot"){j.isPacket=false
}else{j.isPacket=true
}h.retrieveLotOrPacketByAllotmentId(j,function(n){d.parentGridOptions={};
d.parentGridOptions.columnDefs=[];
d.parentGridOptions.data=[];
d.currentStockDetailsList=angular.copy(n.data);
if(!d.flag.multipleParents&&d.currentStockDetailsList.custom2!==null&&d.currentStockDetailsList.custom2!==undefined){d.currentStockDetails=angular.copy(d.currentStockDetailsList.custom2[0])
}g.retrieveDesignationBasedFields("stockmerge",function(p){var u=c.retrieveSectionWiseCustomFieldInfo("invoice");
u.then(function(y){d.generalParentInvoiceTemplate=y.genralSection;
var z=[];
var v=Object.keys(p).map(function(A,B){angular.forEach(this[A],function(C){if(A==="Invoice#P#"){z.push({Invoice:C})
}})
},p);
d.generalParentInvoiceTemplate=c.retrieveCustomData(d.generalParentInvoiceTemplate,z);
d.parentInvoiceCustom=c.resetSection(d.generalParentInvoiceTemplate);
if(d.generalParentInvoiceTemplate&&d.generalParentInvoiceTemplate.length>0&&d.flag.multipleParents){for(var w=0;
w<d.generalParentInvoiceTemplate.length;
w++){var x=d.generalParentInvoiceTemplate[w];
if(x.fromModel){d.parentGridOptions.columnDefs.push({name:x.fromModel,displayName:x.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}else{if(x.toModel){d.parentGridOptions.columnDefs.push({name:x.toModel,displayName:x.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}else{if(x.model){d.parentGridOptions.columnDefs.push({name:x.model,displayName:x.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}}}}}},function(v){},function(v){});
var t=c.retrieveSectionWiseCustomFieldInfo("parcel");
t.then(function(z){d.generalParentParcelTemplate=z.genralSection;
var w=[];
var v=Object.keys(p).map(function(A,B){angular.forEach(this[A],function(C){if(A==="Parcel#P#"){w.push({Parcel:C})
}})
},p);
d.generalParentParcelTemplate=c.retrieveCustomData(d.generalParentParcelTemplate,w);
d.parentParcelCustom=c.resetSection(d.generalParentParcelTemplate);
if(d.generalParentParcelTemplate&&d.generalParentParcelTemplate.length>0&&d.flag.multipleParents){for(var x=0;
x<d.generalParentParcelTemplate.length;
x++){var y=d.generalParentParcelTemplate[x];
if(y.fromModel){d.parentGridOptions.columnDefs.push({name:y.fromModel,displayName:y.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}else{if(y.toModel){d.parentGridOptions.columnDefs.push({name:y.toModel,displayName:y.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}else{if(y.model){d.parentGridOptions.columnDefs.push({name:y.model,displayName:y.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}}}}}},function(v){},function(v){});
var o=c.retrieveSectionWiseCustomFieldInfo("lot");
o.then(function(z){d.generalParentLotTemplate=z.genralSection;
var y=[];
var v=Object.keys(p).map(function(A,B){angular.forEach(this[A],function(C){if(A==="Lot#P#"){y.push({Lot:C})
}})
},p);
d.generalParentLotTemplate=c.retrieveCustomData(d.generalParentLotTemplate,y);
d.parentLotCustom=c.resetSection(d.generalParentLotTemplate);
if(d.generalParentLotTemplate&&d.generalParentLotTemplate.length>0&&d.flag.multipleParents){for(var w=0;
w<d.generalParentLotTemplate.length;
w++){var x=d.generalParentLotTemplate[w];
if(x.fromModel){d.parentGridOptions.columnDefs.push({name:x.fromModel,displayName:x.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}else{if(x.toModel){d.parentGridOptions.columnDefs.push({name:x.toModel,displayName:x.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}else{if(x.model){d.parentGridOptions.columnDefs.push({name:x.model,displayName:x.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}}}}}},function(v){},function(v){});
var q=c.retrieveSectionWiseCustomFieldInfo("lot");
d.lotEditDbType={};
q.then(function(y){d.generalLotEditTemplate=y.genralSection;
var w=[];
var v=Object.keys(p).map(function(z,A){angular.forEach(this[z],function(B){if(z==="Lot"){w.push({Lot:B})
}})
},p);
d.generalLotEditTemplate=c.retrieveCustomData(d.generalLotEditTemplate,w);
d.fieldConfiguredForLot=false;
if(d.generalLotEditTemplate!==undefined&&d.generalLotEditTemplate!==null){for(var x=0;
x<d.generalLotEditTemplate.length;
x++){if(d.generalLotEditTemplate[x].model==="in_stock_of_lot$UMS$String"){d.generalLotEditTemplate[x].required=true;
d.fieldConfiguredForLot=true;
break
}}}d.lotEditCustom=c.resetSection(d.generalLotEditTemplate)
},function(v){},function(v){});
var s=c.retrieveSectionWiseCustomFieldInfo("packet");
d.packetEditDbType={};
s.then(function(y){d.generalPacketEditTemplate=y.genralSection;
var w=[];
var v=Object.keys(p).map(function(z,A){angular.forEach(this[z],function(B){if(z==="Packet"){w.push({Packet:B})
}})
},p);
d.generalPacketEditTemplate=c.retrieveCustomData(d.generalPacketEditTemplate,w);
d.fieldConfiguredForPacket=false;
if(d.generalPacketEditTemplate!==undefined&&d.generalPacketEditTemplate!==null){for(var x=0;
x<d.generalPacketEditTemplate.length;
x++){if(d.generalPacketEditTemplate[x].model==="in_stock_of_packet$UMS$String"){d.generalPacketEditTemplate[x].required=true;
d.fieldConfiguredForPacket=true;
break
}}}d.packetEditCustom=c.resetSection(d.generalPacketEditTemplate)
},function(v){});
if(d.currentStockDetailsList.custom2!==undefined&&d.currentStockDetailsList.custom2!==null&&d.flag.multipleParents){d.gridRecords=[];
angular.forEach(d.currentStockDetailsList.custom2,function(v){var w={};
if(v.custom1!==undefined&&v.custom1!==null){w=angular.copy(v.custom1)
}if(v.custom3!==undefined&&v.custom3!==null){angular.forEach(v.custom3,function(y,x){w[x]=y
})
}if(v.custom4!==undefined&&v.custom4!==null){angular.forEach(v.custom4,function(y,x){w[x]=y
})
}if(v.custom5!==undefined&&v.custom5!==null){angular.forEach(v.custom5,function(y,x){w[x]=y
})
}d.gridRecords.push({categoryCustom:w})
});
var r=function(v){angular.forEach(d.gridRecords,function(w){d.parentGridOptions.data.push(w.categoryCustom)
})
};
c.convertorForCustomField(d.gridRecords,r)
}d.flag.showstockPage=true;
d.stockdataflag=true;
b.unMaskLoading()
},function(){b.unMaskLoading();
var p="Failed to retrieve data";
var o=b.error;
b.addMessage(p,o)
})
})
}}else{var k="Cannot merge stock with different visibilities.";
var i=b.error;
b.addMessage(k,i)
}};
d.mergeStock=function(){d.submitted=true;
if(d.mergeForm.$valid){var i={};
i.allotmentIds=d.allotmentIds;
i.parentID=d.flag.parentId;
i.type=d.flag.type;
i.idsToMerge=d.idsToMerge;
if(d.flag.type==="Lot"){i.stockCustom=d.lotEditCustom;
i.stockDbType=d.lotEditDbType
}else{if(d.flag.type==="Packet"){i.stockCustom=d.packetEditCustom;
i.stockDbType=d.packetEditDbType
}}if(!!i.idsToMerge){b.maskLoading();
console.log("stockdataBean:::::"+JSON.stringify(i));
h.mergeStock(i,function(){console.log("Success");
b.unMaskLoading();
d.onBack()
},function(){b.unMaskLoading();
d.onBack();
var k="Failed to merge stock";
var j=b.error;
b.addMessage(k,j)
})
}}};
b.unMaskLoading()
}])
});