define(["hkg","customFieldService","printService","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicForm","accordionCollapse"],function(a){a.register.controller("PrintDynamicController",["$rootScope","$scope","DynamicFormService","PrintService","CustomFieldService",function(b,d,c,e,f){b.maskLoading();
b.activateMenu();
d.printdataflag=false;
d.printList=[];
d.featureMap={};
d.initializeData=function(){d.flag={};
d.flag.showprintPage=false;
d.printList=[];
d.listFilled=false;
d.nodeDetailsInfo=[];
d.printLabelListForUiGrid=[];
d.gridOptions={};
d.gridOptions.enableFiltering=true;
d.gridOptions.multiSelect=false;
d.gridOptions.columnDefs=[];
d.gridOptions.data=[];
d.gridOptions.onRegisterApi=function(h){d.gridApi=h;
h.selection.on.rowSelectionChanged(d,function(i){if(d.gridApi.selection.getSelectedRows().length>0){d.toPrint=d.gridApi.selection.getSelectedRows()[0];
if(!!d.toPrint["~@status"]){d.flag.isPacket=true
}else{d.flag.isPacket=false
}}})
};
d.searchedPrintList=[];
d.printLabelListForUiGrid=[];
var g=c.retrieveSearchWiseCustomFieldInfo("dynamicPrint");
g.then(function(l){d.generalSearchTemplate=l.genralSection;
if(d.generalSearchTemplate!==null&&d.generalSearchTemplate!==undefined){for(var h=0;
h<d.generalSearchTemplate.length;
h++){var k=d.generalSearchTemplate[h];
d.featureMap[k.model]=k.featureName;
if(k.fromModel){d.printLabelListForUiGrid.push({name:k.fromModel,displayName:k.label,minWidth:200})
}else{if(k.toModel){d.printLabelListForUiGrid.push({name:k.toModel,displayName:k.label,minWidth:200})
}else{if(k.model){d.printLabelListForUiGrid.push({name:k.model,displayName:k.label,minWidth:200})
}}}}}d.searchResetFlag=true;
d.map={};
var j={};
angular.forEach(d.featureMap,function(m,i){if(!j[m]){j[m]=[]
}j[m].push(i)
});
b.maskLoading();
e.retrieveSearchedLotsAndPackets(j,function(m){b.unMaskLoading();
d.nodeDetailsInfo=[];
d.searchedPrintList=angular.copy(m.printList);
d.dataRetrieved=true;
if(d.generalSearchTemplate===null||d.generalSearchTemplate===undefined){d.flag.configSearchFlag=true
}else{if(m.dynamicServiceInitBean!==null&&m.dynamicServiceInitBean!==undefined){var i=m.dynamicServiceInitBean;
if(i.nodeAndWorkAllocationIds!==null){d.nodeIdAndWorkAllocationIdsMap=angular.copy(i.nodeAndWorkAllocationIds)
}if(i.mandatoryFields!==null){d.stockStaticFields=i.mandatoryFields
}if(i.diamondsInQueue!==null){d.diamondsInQueue=i.diamondsInQueue
}if(!!i.dynamicServiceInitDataBeans){var n=i.dynamicServiceInitDataBeans;
if(angular.isArray(n)&&n.length>0){angular.forEach(n,function(p){var q={};
q.groupId=p.groupId;
q.groupName=p.groupName;
q.modifier=p.modifier;
q.nodeId=p.nodeId;
q.nodeName=p.nodeName;
d.nodeDetailsInfo.push(q)
})
}if(d.nodeDetailsInfo.length>1){d.flag.multipleIdInvolved=true
}else{d.flag.multipleIdInvolved=false
}}if(d.nodeDetailsInfo.length>0){d.currentActivityNode=d.nodeDetailsInfo[0].nodeId
}}var o=function(p){angular.forEach(d.searchedPrintList,function(q){angular.forEach(d.printLabelListForUiGrid,function(r){if(!q.categoryCustom.hasOwnProperty(r.name)){q.categoryCustom[r.name]="NA"
}else{if(q.categoryCustom.hasOwnProperty(r.name)){if(q.categoryCustom[r.name]===null||q.categoryCustom[r.name]===""||q.categoryCustom[r.name]===undefined){q.categoryCustom[r.name]="NA"
}}}if(q.hasOwnProperty("value")){q.categoryCustom["~@value"]=q.value
}if(q.hasOwnProperty("label")){q.categoryCustom["~@label"]=q.label
}if(q.hasOwnProperty("description")){q.categoryCustom["~@description"]=q.description
}if(q.hasOwnProperty("id")){q.categoryCustom["~@id"]=q.id
}if(q.hasOwnProperty("status")){q.categoryCustom["~@status"]=q.status
}});
d.printList.push(q.categoryCustom)
});
d.updateStockAccordingToNode(d.currentActivityNode);
d.flag.configSearchFlag=false
};
d.flag.configSearchFlag=false;
c.convertorForCustomField(d.searchedPrintList,o)
}},function(){b.unMaskLoading()
})
})
};
d.updateStockAccordingToNode=function(i){if(i!==null&&i!==undefined){d.currentActivityNode=i;
angular.forEach(d.nodeDetailsInfo,function(m){if(m.nodeId===i){var j=m.modifier;
if(j!==null){if(j.indexOf("|")>-1){var k=j.split("|");
for(var l=0;
l<k.length;
l++){if(k[l]==="Q"){d.isQuingRequired=true
}}}else{if(j==="AA"||j==="MA"){d.typeOfAllocation=j
}}}}});
console.log("asdasd:::"+JSON.stringify(d.nodeIdAndWorkAllocationIdsMap));
if(angular.isDefined(d.nodeIdAndWorkAllocationIdsMap)){var g=[];
var h=d.nodeIdAndWorkAllocationIdsMap[i];
console.log(h.length>0);
angular.forEach(d.printList,function(k,j){if(h.length>0&&h.indexOf(k["~@value"])!==-1){k["~@index"]=j+1;
g.push(k)
}});
d.currentNodeStocks=angular.copy(g);
if(angular.isDefined(d.gridApi)){d.gridApi.selection.clearSelectedRows()
}d.gridOptions.data=g;
d.gridOptions.columnDefs=d.printLabelListForUiGrid;
d.listFilled=true;
if(!!d.isQuingRequired){d.gridOptions.enableSorting=false;
d.gridOptions.isRowSelectable=function(j){if(!!d.diamondsInQueue){if(j.entity["~@index"]>d.diamondsInQueue){return false
}else{return true
}}else{if(j.entity["~@index"]>0){return true
}else{return false
}}}
}}else{console.log("Node map not initialized")
}}};
d.setToPrintData=function(g){if(!!g){d.toPrint=g
}};
d.onBack=function(){d.printdataflag=false;
d.initializeData();
delete d.toPrint
};
d.onCanelOfSearch=function(){delete d.toPrint;
d.gridApi.selection.clearSelectedRows()
};
d.printDataTemp=function(){var k={};
var h=[];
var j=[];
var i=[];
var g=[];
d.printdataflag=false;
if(!!d.toPrint){var l=d.toPrint["~@value"];
f.retrieveDesignationBasedFields("dynamicPrint",function(n){var p=c.retrieveSectionWiseCustomFieldInfo("invoice");
d.invoiceDbType={};
p.then(function(v){d.generaInvoiceTemplate=v.genralSection;
var u=[];
var t=Object.keys(n).map(function(w,x){angular.forEach(this[w],function(y){if(w==="Invoice#P#"){u.push({Invoice:y})
}})
},n);
d.generaInvoiceTemplate=c.retrieveCustomData(d.generaInvoiceTemplate,u);
angular.forEach(d.generaInvoiceTemplate,function(w){if(w.model){h.push(w.model)
}});
if(h.length>0){k.invoiceDbFieldName=h
}},function(t){},function(t){});
var s=c.retrieveSectionWiseCustomFieldInfo("parcel");
d.parcelDbType={};
s.then(function(v){d.generaParcelTemplate=v.genralSection;
var u=[];
var t=Object.keys(n).map(function(w,x){angular.forEach(this[w],function(y){if(w==="Parcel#P#"){u.push({Parcel:y})
}})
},n);
d.generaParcelTemplate=c.retrieveCustomData(d.generaParcelTemplate,u);
angular.forEach(d.generaParcelTemplate,function(w){if(w.model){j.push(w.model)
}});
if(j.length>0){k.parcelDbFieldName=j
}},function(t){},function(t){});
var m=c.retrieveSectionWiseCustomFieldInfo("lot");
d.lotDbType={};
m.then(function(u){d.generalLotTemplate=u.genralSection;
var v=[];
var t=Object.keys(n).map(function(w,x){angular.forEach(this[w],function(y){if(w==="Lot#P#"){v.push({Parcel:y})
}})
},n);
d.generalLotTemplate=c.retrieveCustomData(d.generalLotTemplate,v);
angular.forEach(d.generalLotTemplate,function(w){if(w.model){i.push(w.model)
}});
if(i.length>0){k.lotDbFieldName=i
}},function(t){},function(t){});
var o=c.retrieveSectionWiseCustomFieldInfo("packet");
d.packetDbType={};
o.then(function(v){d.generalPacketTemplate=v.genralSection;
var u=[];
var t=Object.keys(n).map(function(w,x){angular.forEach(this[w],function(y){if(w==="Packet#P#"){u.push({Packet:y})
}})
},n);
d.generalPacketTemplate=c.retrieveCustomData(d.generalPacketTemplate,u);
angular.forEach(d.generalPacketTemplate,function(w){if(w.model){g.push(w.model)
}});
if(g.length>0){k.packetDbFieldName=g
}},function(t){},function(t){});
d.finalPayload={};
var r=c.retrieveSectionWiseCustomFieldInfo("lot");
d.printLotDbType={};
r.then(function(u){d.generalPrintLotTemplate=[];
d.generalPrintLotTemplate=u.genralSection;
var v=[];
var t=Object.keys(n).map(function(w,x){angular.forEach(this[w],function(y){if(w==="Lot"){v.push({Lot:y})
}})
},n);
d.generalPrintLotTemplate=c.retrieveCustomData(d.generalPrintLotTemplate,v);
angular.forEach(d.generalPrintLotTemplate,function(w){if(w.model){i.push(w.model)
}});
if(i.length>0){k.lotDbFieldName=(k.lotDbFieldName||[]).concat(i)
}},function(t){},function(t){});
var q=c.retrieveSectionWiseCustomFieldInfo("packet");
d.printPacketDbType={};
q.then(function(w){d.generalPrintPacketTemplate=[];
d.generalPrintPacketTemplate=w.genralSection;
var u=[];
var t=Object.keys(n).map(function(x,y){angular.forEach(this[x],function(z){if(x==="Packet"){u.push({Packet:z})
}})
},n);
d.generalPrintPacketTemplate=c.retrieveCustomData(d.generalPrintPacketTemplate,u);
angular.forEach(d.generalPrintPacketTemplate,function(x){if(x.model){g.push(x.model)
}});
if(g.length>0){k.packetDbFieldName=(k.packetDbFieldName||[]).concat(g)
}var v={payload:l,fields:k};
e.retrieveInformationByWorkallotmentPrint(v,function(x){console.log("result1:::::"+JSON.stringify(x));
d.invoiceCustom=x.custom4;
d.parcelCustom=angular.copy(x.custom3);
d.categoryCustom=angular.copy(x.custom1);
d.printLotCustom=angular.copy(x.custom1);
d.printPacketCustom=angular.copy(x.custom5);
d.printdataflag=true
})
},function(t){},function(t){})
},function(){b.unMaskLoading();
var n="Failed to retrieve data";
var m=b.error;
b.addMessage(n,m)
})
}};
d.generatePDFFromData=function(){var h={};
var g=[];
g.push({categoryCustom:d.printLotCustom});
c.convertorForCustomField(g,function(j){angular.forEach(d.generalPrintLotTemplate,function(l){var m=j[0].categoryCustom[l.model];
if(!!m){h[l.label]=m
}else{h[l.label]="N/A"
}});
var i={};
var k=[];
k.push({categoryCustom:d.printPacketCustom});
c.convertorForCustomField(k,function(m){angular.forEach(d.generalPrintPacketTemplate,function(p){var q=null;
if(!!d.printPacketCustom){q=m[0].categoryCustom[p.model]
}if(!!q){i[p.label]=q
}else{i[p.label]="N/A"
}});
var l=[];
var o={};
o.payload=h;
o.payload1=i;
if(!!d.toPrint){var n=d.toPrint;
if(!!n["~@status"]){o.idToPrint={id:n["packetID$AG$String"],allotmentID:d.toPrint["~@value"]};
delete o.payload
}else{if(!!n["~@id"]){o.idToPrint={id:n["lotID$AG$String"],allotmentID:d.toPrint["~@value"]};
delete o.payload1
}}}b.maskLoading();
e.generatePrintData(o,function(p){b.unMaskLoading();
if(p!==undefined&&p!==null){d.fileName=p.filename;
window.location.href=b.appendAuthToken(b.centerapipath+"print/downloadPDFReport?fileName="+d.fileName);
var r="Stock printed successfully";
var q=b.success;
b.addMessage(r,q);
d.onBack()
}},function(){b.unMaskLoading();
var q="Error while printing stock";
var p=b.error;
b.addMessage(q,p)
})
})
})
};
b.unMaskLoading()
}])
});