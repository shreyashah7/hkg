define(["hkg","customFieldService","allotService","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicForm","datepickercustom.directive","accordionCollapse"],function(a){a.register.controller("OldAllotmentController",["$rootScope","$scope","$timeout","DynamicFormService","AllotService","CustomFieldService",function(b,e,g,d,c,h){b.maskLoading();
b.mainMenu="stockLink";
b.childMenu="allotMenu";
b.activateMenu();
e.stockdataflag=false;
e.stockList=[];
e.currentNodeStocks=undefined;
e.flag={};
e.forms={};
e.manualAllocation="MA";
e.automaticAllocation="AA";
e.typeOfAllocation="MA";
var f={};
e.stockStaticFields=[];
e.lotStaticFields=[];
e.packetStaticFields=[];
e.suggestionData={};
e.suggestionParameters=[{id:"cut",text:"Cut"},{id:"color",text:"Color"},{id:"carat",text:"Carat"},{id:"clarity",text:"Clarity"}];
e.suggestionData.parameter="cut";
e.suggestionList=[];
e.initializeData=function(){e.flag={};
e.flag.showstockPage=false;
e.flag.staticFieldMissing=false;
e.stockList=[];
e.listFilled=false;
e.submitted=false;
e.flag.rowSelectedflag=false;
e.stockLabelListForUiGrid=[];
e.nodeDetailsInfo=[];
e.currentNodeStocks=undefined;
e.gridOptions={};
e.gridOptions.enableFiltering=true;
e.gridOptions.multiSelect=true;
e.gridOptions.columnDefs=[];
e.gridOptions.data=[];
e.gridOptions.onRegisterApi=function(j){e.gridApi=j;
j.selection.on.rowSelectionChanged(e,function(k){if(e.typeOfAllocation===e.manualAllocation){if(e.gridApi.selection.getSelectedRows().length>0){e.flag.rowSelectedflag=true
}else{e.flag.rowSelectedflag=false
}}else{if(e.gridApi.selection.getSelectedRows().length===1){e.flag.rowSelectedflag=true
}else{e.flag.rowSelectedflag=false
}}});
j.selection.on.rowSelectionChangedBatch(e,function(k){if(e.typeOfAllocation===e.manualAllocation){if(e.gridApi.selection.getSelectedRows().length>0){e.flag.rowSelectedflag=true
}else{e.flag.rowSelectedflag=false
}}else{if(e.gridApi.selection.getSelectedRows().length===1){e.flag.rowSelectedflag=true
}else{e.flag.rowSelectedflag=false
}}})
};
e.searchedStockList=[];
e.generalSearchTemplate=[];
e.stockLabelListForUiGrid=[];
var i=d.retrieveSearchWiseCustomFieldInfo("allot");
e.modelAndHeaderList=[];
i.then(function(m){e.generalSearchTemplate=m.genralSection;
if(e.generalSearchTemplate!=null&&e.generalSearchTemplate.length>0){e.flag.configSearchFlag=false;
for(var j=0;
j<e.generalSearchTemplate.length;
j++){var l=e.generalSearchTemplate[j];
e.modelAndHeaderList.push({model:l.model,header:l.label});
if(l.fromModel){f[l.fromModel]=l.featureName;
e.stockLabelListForUiGrid.push({name:l.fromModel,displayName:l.label,minWidth:200})
}else{if(l.toModel){f[l.toModel]=l.featureName;
e.stockLabelListForUiGrid.push({name:l.toModel,displayName:l.label,minWidth:200})
}else{if(l.model){f[l.model]=l.featureName;
e.stockLabelListForUiGrid.push({name:l.model,displayName:l.label,minWidth:200})
}}}}var k={};
if(f!=null){angular.forEach(f,function(o,n){if(!k[o]){k[o]=[]
}k[o].push(n)
})
}b.maskLoading();
c.retrieveSearchedLotsAndPackets(k,function(o){e.dataRetrieved=true;
e.nodeDetailsInfo=[];
e.flag.multipleIdInvolved=false;
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
}if(e.nodeDetailsInfo.length>1){e.flag.multipleIdInvolved=true
}else{e.flag.multipleIdInvolved=false
}}if(e.nodeDetailsInfo.length>0){e.currentActivityNode=e.nodeDetailsInfo[0].nodeId
}}if(o.stockList!==null&&o.stockList!==undefined&&o.stockList.length>0){e.searchedStockList=o.stockList;
var q=function(r){angular.forEach(e.searchedStockList,function(s){angular.forEach(e.stockLabelListForUiGrid,function(t){if(!s.categoryCustom.hasOwnProperty(t.name)){s.categoryCustom[t.name]="NA"
}else{if(s.categoryCustom.hasOwnProperty(t.name)){if(s.categoryCustom[t.name]===null||s.categoryCustom[t.name]===""||s.categoryCustom[t.name]===undefined){s.categoryCustom[t.name]="NA"
}}}if(s.hasOwnProperty("value")){s.categoryCustom["~@workallotmentid"]=s.value
}if(s.hasOwnProperty("label")){s.categoryCustom["~@parcelid"]=s.label
}if(s.hasOwnProperty("description")){s.categoryCustom["~@invoiceid"]=s.description
}if(s.hasOwnProperty("id")){s.categoryCustom["~@lotid"]=s.id
}if(s.hasOwnProperty("status")){s.categoryCustom["~@packetid"]=s.status
}});
e.stockList.push(s.categoryCustom)
});
e.updateStockAccordingToNode(e.currentActivityNode);
b.unMaskLoading()
};
d.convertorForCustomField(e.searchedStockList,q)
}else{e.currentNodeStocks=[];
b.unMaskLoading()
}},function(){b.unMaskLoading()
})
}else{e.flag.configSearchFlag=true
}},function(j){},function(j){})
};
e.updateStockAccordingToNode=function(k){if(k!==null&&k!==undefined){e.currentActivityNode=k;
angular.forEach(e.nodeDetailsInfo,function(o){if(o.nodeId===k){var l=o.modifier;
if(l!==null){if(l.indexOf("|")>-1){var m=l.split("|");
for(var n=0;
n<m.length;
n++){if(m[n]==="AA"||m[n]==="MA"){e.typeOfAllocation=m[n]
}if(m[n]==="Q"){e.isQuingRequired=true
}}}else{if(l==="AA"||l==="MA"){e.typeOfAllocation=l
}}}}});
if(angular.isDefined(e.nodeIdAndWorkAllocationIdsMap)){var i=[];
var j=e.nodeIdAndWorkAllocationIdsMap[k];
angular.forEach(e.stockList,function(m,l){if(j.length>0&&j.indexOf(m["~@workallotmentid"])!==-1){m["~@index"]=l+1;
i.push(m)
}});
e.currentNodeStocks=angular.copy(i);
if(angular.isDefined(e.gridApi)){e.gridApi.selection.clearSelectedRows()
}e.gridOptions.data=i;
e.gridOptions.columnDefs=e.stockLabelListForUiGrid;
e.listFilled=true;
if(!!e.isQuingRequired){e.gridOptions.enableSorting=false;
e.gridOptions.isRowSelectable=function(l){if(!!e.diamondsInQueue){if(l.entity["~@index"]>e.diamondsInQueue){return false
}else{return true
}}else{if(l.entity["~@index"]>0){return true
}else{return false
}}}
}}else{console.log("Node map not initialized")
}c.retrieveNextNodeDesignationIds(e.currentActivityNode,function(l){e.designationIdForAllot=undefined;
e.designationIdForInStock=undefined;
if(l.data!==null){if(l.data.forAllotTo!==undefined&&l.data.forAllotTo!==null){e.designationIdForAllot=l.data.forAllotTo.toString()
}if(l.data.forIssue!==undefined&&l.data.forIssue!==null){e.designationIdForInStock=l.data.forIssue.toString()
}}},function(l){console.log("failure----")
})
}};
e.updateAllotTo=function(i){if(i!==null){if(e.flag.type==="Lot"){e.flag.displayEditLotflag=false;
e.lotEditCustom["allot_to_lot$UMS$String"]=i+":E";
g(function(){e.flag.displayEditLotflag=true
},100)
}else{e.flag.displayEditPacketflag=false;
e.packetEditCustom["allot_to_packet$UMS$String"]=i+":E";
g(function(){e.flag.displayEditPacketflag=true
},100)
}}};
e.setToPrintData=function(i){if(!!i){e.toPrint=i
}};
e.onBack=function(){e.stockdataflag=false;
e.initializeData();
delete e.toPrint
};
e.onCanelOfSearch=function(){delete e.toPrint;
e.gridApi.selection.clearSelectedRows()
};
e.allotStockNext=function(){e.selectedRows=e.gridApi.selection.getSelectedRows();
e.noOfLots=0;
e.noOfPackets=0;
for(var o=0;
o<e.selectedRows.length;
o++){if(e.selectedRows[o]["~@packetid"]!=null){e.noOfPackets=e.noOfPackets+1
}else{if(e.selectedRows[o]["~@packetid"]===null&&e.selectedRows[o]["~@lotid"]!=null){e.noOfLots=e.noOfLots+1
}}if(e.noOfLots>0&&e.noOfPackets>0){break
}}if(e.noOfLots>0&&e.noOfPackets>0){delete e.selectedRows;
e.selectedRows=[];
e.gridApi.selection.clearSelectedRows();
var n="Select either lots or packets";
var k=b.warning;
b.addMessage(n,k)
}else{if(e.noOfLots>0&&e.noOfPackets===0){e.flag.type="Lot"
}else{if(e.noOfPackets>0&&e.noOfLots===0){e.flag.type="Packet"
}}}if((e.noOfLots>0&&e.noOfPackets===0)||(e.noOfPackets>0&&e.noOfLots===0)){e.allotmentIds=[];
e.stockIds=[];
for(var o=0;
o<e.selectedRows.length;
o++){if(e.noOfLots>0){e.stockIds.push(e.selectedRows[o]["~@lotid"])
}else{if(e.noOfPackets>0){e.stockIds.push(e.selectedRows[o]["~@packetid"])
}}e.allotmentIds.push(e.selectedRows[o]["~@workallotmentid"])
}if(e.selectedRows.length>=1){e.selectedWorkAllotmentIds=[];
if(e.noOfLots>0){for(var i=0;
i<e.selectedRows.length;
i++){angular.forEach(e.searchedStockList,function(j){if(j.categoryCustom["~@lotid"]===e.selectedRows[i]["~@lotid"]){e.selectedWorkAllotmentIds.push(j.categoryCustom["~@workallotmentid"])
}})
}}else{if(e.noOfPackets>0){for(var i=0;
i<e.selectedRows.length;
i++){angular.forEach(e.searchedStockList,function(j){if(j.categoryCustom["~@packetid"]===e.selectedRows[i]["~@packetid"]){e.selectedWorkAllotmentIds.push(j.categoryCustom["~@workallotmentid"])
}})
}}}if(e.selectedRows.length===1){e.flag.multipleParents=false
}else{e.flag.multipleParents=true
}e.flag.showParent=true
}else{e.flag.showParent=false
}if(e.typeOfAllocation===e.automaticAllocation){var l={};
l.allocationPropertyName=e.suggestionData.parameter;
l.currentActivityNode=e.currentActivityNode;
c.retrieveAllotmentSuggestion(l,function(j){e.suggestionList=angular.copy(j);
angular.forEach(e.suggestionList,function(p){p.isSelected=false
})
})
}if(e.flag.showParent){var m={};
m.workAllotmentId=e.selectedWorkAllotmentIds;
if(e.flag.type==="Lot"){m.isPacket=false
}else{m.isPacket=true
}b.maskLoading();
c.retrieveLotOrPacketByAllotmentId(m,function(j){e.parentGridOptions={};
e.parentGridOptions.columnDefs=[];
e.parentGridOptions.data=[];
e.currentStockDetailsList=angular.copy(j.data);
if(!e.flag.multipleParents&&e.currentStockDetailsList.custom2!==null&&e.currentStockDetailsList.custom2!==undefined){e.currentStockDetails=angular.copy(e.currentStockDetailsList.custom2[0])
}h.retrieveDesignationBasedFields("allot",function(q){var w=d.retrieveSectionWiseCustomFieldInfo("invoice");
w.then(function(A){e.generalParentInvoiceTemplate=A.genralSection;
var B=[];
var x=Object.keys(q).map(function(C,D){angular.forEach(this[C],function(E){if(C==="Invoice#P#"){B.push({Invoice:E})
}})
},q);
e.generalParentInvoiceTemplate=d.retrieveCustomData(e.generalParentInvoiceTemplate,B);
e.parentInvoiceCustom=d.resetSection(e.generalParentInvoiceTemplate);
if(angular.isDefined(e.currentStockDetails)&&!e.flag.multipleParents){if(e.currentStockDetails.custom1!==undefined&&e.currentStockDetails.custom1!==null){e.parentInvoiceCustom=angular.copy(e.currentStockDetails.custom1)
}}if(e.generalParentInvoiceTemplate&&e.generalParentInvoiceTemplate.length>0&&e.flag.multipleParents){for(var y=0;
y<e.generalParentInvoiceTemplate.length;
y++){var z=e.generalParentInvoiceTemplate[y];
if(z.fromModel){e.parentGridOptions.columnDefs.push({name:z.fromModel,displayName:z.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}else{if(z.toModel){e.parentGridOptions.columnDefs.push({name:z.toModel,displayName:z.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}else{if(z.model){e.parentGridOptions.columnDefs.push({name:z.model,displayName:z.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}}}}}},function(x){},function(x){});
var v=d.retrieveSectionWiseCustomFieldInfo("parcel");
v.then(function(B){e.generalParentParcelTemplate=B.genralSection;
var y=[];
var x=Object.keys(q).map(function(C,D){angular.forEach(this[C],function(E){if(C==="Parcel#P#"){y.push({Parcel:E})
}})
},q);
e.generalParentParcelTemplate=d.retrieveCustomData(e.generalParentParcelTemplate,y);
e.parentParcelCustom=d.resetSection(e.generalParentParcelTemplate);
if(angular.isDefined(e.currentStockDetails)&&!e.flag.multipleParents){if(e.currentStockDetails.custom3!==undefined&&e.currentStockDetails.custom3!==null){e.parentParcelCustom=angular.copy(e.currentStockDetails.custom3)
}}if(e.generalParentParcelTemplate&&e.generalParentParcelTemplate.length>0&&e.flag.multipleParents){for(var z=0;
z<e.generalParentParcelTemplate.length;
z++){var A=e.generalParentParcelTemplate[z];
if(A.fromModel){e.parentGridOptions.columnDefs.push({name:A.fromModel,displayName:A.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}else{if(A.toModel){e.parentGridOptions.columnDefs.push({name:A.toModel,displayName:A.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}else{if(A.model){e.parentGridOptions.columnDefs.push({name:A.model,displayName:A.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}}}}}},function(x){},function(x){});
var p=d.retrieveSectionWiseCustomFieldInfo("lot");
p.then(function(B){e.generalParentLotTemplate=B.genralSection;
var A=[];
var x=Object.keys(q).map(function(C,D){angular.forEach(this[C],function(E){if(C==="Lot#P#"){A.push({Lot:E})
}})
},q);
e.generalParentLotTemplate=d.retrieveCustomData(e.generalParentLotTemplate,A);
e.parentLotCustom=d.resetSection(e.generalParentLotTemplate);
if(angular.isDefined(e.currentStockDetails)&&!e.flag.multipleParents){if(e.currentStockDetails.custom4!==undefined&&e.currentStockDetails.custom4!==null){e.parentLotCustom=angular.copy(e.currentStockDetails.custom4)
}}if(e.generalParentLotTemplate&&e.generalParentLotTemplate.length>0&&e.flag.multipleParents){for(var y=0;
y<e.generalParentLotTemplate.length;
y++){var z=e.generalParentLotTemplate[y];
if(z.fromModel){e.parentGridOptions.columnDefs.push({name:z.fromModel,displayName:z.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}else{if(z.toModel){e.parentGridOptions.columnDefs.push({name:z.toModel,displayName:z.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}else{if(z.model){e.parentGridOptions.columnDefs.push({name:z.model,displayName:z.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}}}}}},function(x){},function(x){});
if(e.flag.type==="Packet"){var r=d.retrieveSectionWiseCustomFieldInfo("packet");
r.then(function(B){e.generalParentPacketTemplate=B.genralSection;
var A=[];
var x=Object.keys(q).map(function(C,D){angular.forEach(this[C],function(E){if(C==="Packet#P#"){A.push({Packet:E})
}})
},q);
e.generalParentPacketTemplate=d.retrieveCustomData(e.generalParentPacketTemplate,A);
e.parentPacketCustom=d.resetSection(e.generalParentPacketTemplate);
if(angular.isDefined(e.currentStockDetails)&&!e.flag.multipleParents){if(e.currentStockDetails.custom5!==undefined&&e.currentStockDetails.custom5!==null){e.parentPacketCustom=angular.copy(e.currentStockDetails.custom5)
}}if(e.generalParentPacketTemplate&&e.generalParentPacketTemplate.length>0&&e.flag.multipleParents){for(var y=0;
y<e.generalParentPacketTemplate.length;
y++){var z=e.generalParentPacketTemplate[y];
if(z.fromModel){e.parentGridOptions.columnDefs.push({name:z.fromModel,displayName:z.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}else{if(z.toModel){e.parentGridOptions.columnDefs.push({name:z.toModel,displayName:z.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}else{if(z.model){e.parentGridOptions.columnDefs.push({name:z.model,displayName:z.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}}}}}},function(x){},function(x){})
}if(e.flag.type==="Lot"){var s=d.retrieveSectionWiseCustomFieldInfo("lot");
e.lotEditDbType={};
s.then(function(A){e.generalLotEditTemplate=A.genralSection;
var y=[];
var z=[];
var x=Object.keys(q).map(function(B,C){angular.forEach(this[B],function(D){if(B==="Lot"){y.push({Lot:D});
if(!D.isEditable){z.push(D.fieldId)
}}})
},q);
e.generalLotEditTemplate=d.retrieveCustomData(e.generalLotEditTemplate,y);
e.lotEditCustom=d.resetSection(e.generalLotEditTemplate);
if(e.generalLotEditTemplate){angular.forEach(e.stockStaticFields,function(B){if(!e.flag.staticFieldMissing){var C=false;
angular.forEach(e.generalLotEditTemplate,function(D){if(D.model===B+"_lot$UMS$String"){C=true
}});
if(!C){e.flag.staticFieldMissing=true
}else{if(z.length>0){angular.forEach(e.generalLotEditTemplate,function(D){if(z.indexOf(D.fieldId)>-1){e.flag.staticFieldMissing=true
}})
}}}})
}e.flag.displayEditLotflag=true
},function(x){},function(x){})
}if(e.flag.type==="Packet"){var u=d.retrieveSectionWiseCustomFieldInfo("packet");
e.packetEditDbType={};
u.then(function(A){e.generalPacketEditTemplate=A.genralSection;
var y=[];
var z=[];
var x=Object.keys(q).map(function(B,C){angular.forEach(this[B],function(D){if(B==="Packet"){y.push({Packet:D});
if(!D.isEditable){z.push(D.fieldId)
}}})
},q);
e.generalPacketEditTemplate=d.retrieveCustomData(e.generalPacketEditTemplate,y);
e.packetEditCustom=d.resetSection(e.generalPacketEditTemplate);
if(e.generalPacketEditTemplate){angular.forEach(e.stockStaticFields,function(B){var C=false;
angular.forEach(e.generalPacketEditTemplate,function(D){if(D.model===B+"_packet$UMS$String"){C=true
}});
if(!C){e.flag.staticFieldMissing=true
}else{if(z.length>0){angular.forEach(e.generalPacketEditTemplate,function(D){if(z.indexOf(D.fieldId)>-1){e.flag.staticFieldMissing=true
}})
}}})
}e.flag.displayEditPacketflag=true
},function(x){})
}if(e.currentStockDetailsList.custom2!==undefined&&e.currentStockDetailsList.custom2!==null&&e.flag.multipleParents){e.gridRecords=[];
angular.forEach(e.currentStockDetailsList.custom2,function(x){var y={};
if(x.custom1!==undefined&&x.custom1!==null){y=angular.copy(x.custom1)
}if(x.custom3!==undefined&&x.custom3!==null){angular.forEach(x.custom3,function(A,z){y[z]=A
})
}if(x.custom4!==undefined&&x.custom4!==null){angular.forEach(x.custom4,function(A,z){y[z]=A
})
}if(x.custom5!==undefined&&x.custom5!==null){angular.forEach(x.custom5,function(A,z){y[z]=A
})
}e.gridRecords.push({categoryCustom:y})
});
var t=function(x){angular.forEach(e.gridRecords,function(y){e.parentGridOptions.data.push(y.categoryCustom)
})
};
d.convertorForCustomField(e.gridRecords,t)
}e.flag.showstockPage=true;
e.stockdataflag=true;
b.unMaskLoading()
},function(){b.unMaskLoading();
var q="Failed to retrieve data";
var p=b.error;
b.addMessage(q,p)
})
})
}else{b.maskLoading();
h.retrieveDesignationBasedFields("allot",function(j){if(e.flag.type==="Lot"){var p=d.retrieveSectionWiseCustomFieldInfo("lot");
e.lotEditDbType={};
p.then(function(u){e.generalLotEditTemplate=u.genralSection;
var s=[];
var t=[];
var r=Object.keys(j).map(function(v,w){angular.forEach(this[v],function(x){if(v==="Lot"){s.push({Lot:x});
if(!x.isEditable){t.push(x.fieldId)
}}})
},j);
e.generalLotEditTemplate=d.retrieveCustomData(e.generalLotEditTemplate,s);
e.lotEditCustom=d.resetSection(e.generalLotEditTemplate);
if(e.generalLotEditTemplate){angular.forEach(e.stockStaticFields,function(v){if(!e.flag.staticFieldMissing){var w=false;
angular.forEach(e.generalLotEditTemplate,function(x){if(x.model===v+"_lot$UMS$String"){w=true
}});
if(!w){e.flag.staticFieldMissing=true
}else{if(t.length>0){angular.forEach(e.generalLotEditTemplate,function(x){if(t.indexOf(x.fieldId)>-1){e.flag.staticFieldMissing=true
}})
}}}})
}e.flag.displayEditLotflag=true
},function(r){},function(r){})
}if(e.flag.type==="Packet"){var q=d.retrieveSectionWiseCustomFieldInfo("packet");
e.packetEditDbType={};
q.then(function(u){e.generalPacketEditTemplate=u.genralSection;
var s=[];
var t=[];
var r=Object.keys(j).map(function(v,w){angular.forEach(this[v],function(x){if(v==="Packet"){s.push({Packet:x});
if(!x.isEditable){t.push(x.fieldId)
}}})
},j);
e.generalPacketEditTemplate=d.retrieveCustomData(e.generalPacketEditTemplate,s);
e.packetEditCustom=d.resetSection(e.generalPacketEditTemplate);
if(e.generalPacketEditTemplate){angular.forEach(e.stockStaticFields,function(v){var w=false;
angular.forEach(e.generalPacketEditTemplate,function(x){if(x.model===v+"_packet$UMS$String"){w=true
}});
if(!w){e.flag.staticFieldMissing=true
}else{if(t.length>0){angular.forEach(e.generalPacketEditTemplate,function(x){if(t.indexOf(x.fieldId)>-1){e.flag.staticFieldMissing=true
}})
}}})
}e.flag.displayEditPacketflag=true
},function(r){})
}e.flag.showstockPage=true;
e.stockdataflag=true;
b.unMaskLoading()
},function(){b.unMaskLoading();
var p="Failed to retrieve data";
var j=b.error;
b.addMessage(p,j)
})
}}};
e.updateSuggestionList=function(){if(e.typeOfAllocation===e.automaticAllocation){var i={};
i.allocationPropertyName=e.suggestionData.parameter;
i.currentActivityNode=e.currentActivityNode;
c.retrieveAllotmentSuggestion(i,function(j){e.suggestionList=angular.copy(j);
angular.forEach(e.suggestionList,function(k){k.isSelected=false
})
})
}};
e.openAttendance=function(i){e.userDetails=angular.copy(i);
$("#attendanceModal").modal("show")
};
e.hideAttendancePopUp=function(){$("#attendanceModal").modal("hide");
b.removeModalOpenCssAfterModalHide()
};
e.allotStock=function(m){e.submitted=true;
if(m.$valid){var k={};
k.allotmentIds=e.allotmentIds;
k.type=e.flag.type;
k.stockIds=e.stockIds;
var i=true;
var j=[];
if(e.flag.type==="Lot"){k.stockCustom=e.lotEditCustom;
k.stockDbType=e.lotEditDbType;
if(e.stockStaticFields.length>0){angular.forEach(e.stockStaticFields,function(o){if(k.stockCustom[o+"_lot$UMS$String"]===null||k.stockCustom[o+"_lot$UMS$String"]===undefined||k.stockCustom[o+"_lot$UMS$String"].length===0){i=false;
j.push(o+"_lot$UMS$String")
}})
}}else{if(e.flag.type==="Packet"){k.stockCustom=e.packetEditCustom;
k.stockDbType=e.packetEditDbType;
if(e.stockStaticFields.length>0){angular.forEach(e.stockStaticFields,function(o){if(k.stockCustom[o+"_packet$UMS$String"]===null||k.stockCustom[o+"_packet$UMS$String"]===undefined||k.stockCustom[o+"_packet$UMS$String"].length===0){i=false;
j.push(o+"_packet$UMS$String")
}})
}}}if(!!k.stockIds&&i){b.maskLoading();
c.saveAllotment(k,function(){b.unMaskLoading();
e.onBack();
var p="Stock alloted successfully";
var o=b.success;
b.addMessage(p,o)
},function(){b.unMaskLoading();
e.onBack();
var p="Failed to allot stock";
var o=b.error;
b.addMessage(p,o)
})
}else{if(!i){var n="Mandatory fields can't be left blank";
var l=b.error;
b.addMessage(n,l)
}}}};
b.unMaskLoading()
}])
});