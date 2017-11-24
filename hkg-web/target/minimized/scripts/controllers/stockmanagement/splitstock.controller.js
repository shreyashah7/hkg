define(["hkg","customFieldService","splitstockService","printService","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicForm","accordionCollapse","lotService"],function(a){a.register.controller("SplitStockController",["$rootScope","$scope","DynamicFormService","SplitStockService","CustomFieldService","PrintService","$timeout","LotService",function(b,d,c,g,i,f,e,h){b.maskLoading();
b.activateMenu();
d.stockdataflag=false;
d.stockList=[];
d.flag={};
d.SplitStock=this;
d.featureMap={};
d.tempDbMap={};
d.initializeData=function(){d.nodeDetailsInfo=[];
d.flag={};
d.flag.showstockPage=false;
d.stockList=[];
d.listFilled=false;
d.flag.rowSelectedflag=false;
d.stockLabelListForUiGrid=[];
this.lotedtflg=false;
this.packetedtflg=false;
d.gridOptions1={};
d.gridOptions1.enableFiltering=true;
d.gridOptions1.columnDefs=[];
d.gridOptions1.data=[];
d.gridOptions={};
d.gridOptions.enableFiltering=true;
d.gridOptions.multiSelect=false;
d.gridOptions.columnDefs=[];
d.gridOptions.data=[];
d.gridOptions.onRegisterApi=function(k){d.gridApi=k;
k.selection.on.rowSelectionChanged(d,function(l){d.toSplit=d.gridApi.selection.getSelectedRows()[0]
})
};
d.searchedStockList=[];
d.generalSearchTemplate=[];
d.stockLabelListForUiGrid=[];
b.maskLoading();
var j=c.retrieveSearchWiseCustomFieldInfo("stocksplit");
j.then(function(n){d.generalSearchTemplate=n.genralSection;
if(d.generalSearchTemplate!==null&&d.generalSearchTemplate!==undefined){for(var k=0;
k<d.generalSearchTemplate.length;
k++){var m=d.generalSearchTemplate[k];
d.featureMap[m.model]=m.featureName;
if(m.fromModel){d.stockLabelListForUiGrid.push({name:m.fromModel,displayName:m.label,minWidth:200})
}else{if(m.toModel){d.stockLabelListForUiGrid.push({name:m.toModel,displayName:m.label,minWidth:200})
}else{if(m.model){d.stockLabelListForUiGrid.push({name:m.model,displayName:m.label,minWidth:200})
}}}}}d.searchResetFlag=true;
d.map={};
var l={};
angular.forEach(d.featureMap,function(p,o){if(!l[p]){l[p]=[]
}l[p].push(o)
});
g.retrieveSearchedLotsAndPacketsForSplit(l,function(p){b.unMaskLoading();
d.nodeDetailsInfo=[];
d.dataRetrieved=true;
d.searchedStockList=angular.copy(p.stockList);
if(d.generalSearchTemplate===null||d.generalSearchTemplate===undefined){d.flag.configSearchFlag=true
}else{if(p.dynamicServiceInitBean!==null&&p.dynamicServiceInitBean!==undefined){var o=p.dynamicServiceInitBean;
if(o.nodeAndWorkAllocationIds!==null){d.nodeIdAndWorkAllocationIdsMap=angular.copy(o.nodeAndWorkAllocationIds)
}if(o.mandatoryFields!==null){d.stockStaticFields=o.mandatoryFields
}if(o.diamondsInQueue!==null){d.diamondsInQueue=o.diamondsInQueue
}if(!!o.dynamicServiceInitDataBeans){var q=o.dynamicServiceInitDataBeans;
if(angular.isArray(q)&&q.length>0){angular.forEach(q,function(s){var t={};
t.groupId=s.groupId;
t.groupName=s.groupName;
t.modifier=s.modifier;
t.nodeId=s.nodeId;
t.nodeName=s.nodeName;
d.nodeDetailsInfo.push(t)
})
}if(d.nodeDetailsInfo.length>1){d.flag.multipleIdInvolved=true
}else{d.flag.multipleIdInvolved=false
}}if(d.nodeDetailsInfo.length>0){d.currentActivityNode=d.nodeDetailsInfo[0].nodeId
}if(d.currentActivityNode!==undefined&&d.currentActivityNode!==null){g.retrieveNextNodeDesignationIds(d.currentActivityNode,function(s){d.designationIdForAllot=undefined;
d.designationIdForInStock=undefined;
if(s.data!==null){if(s.data.forAllotTo!==undefined&&s.data.forAllotTo!==null){d.designationIdForAllot=s.data.forAllotTo.toString()
}if(s.data.forIssue!==undefined&&s.data.forIssue!==null){d.designationIdForInStock=s.data.forIssue.toString()
}}},function(s){console.log("failure----")
})
}}var r=function(s){angular.forEach(d.searchedStockList,function(t){angular.forEach(d.stockLabelListForUiGrid,function(u){if(!t.categoryCustom.hasOwnProperty(u.name)){t.categoryCustom[u.name]="NA"
}else{if(t.categoryCustom.hasOwnProperty(u.name)){if(t.categoryCustom[u.name]===null||t.categoryCustom[u.name]===""||t.categoryCustom[u.name]===undefined){t.categoryCustom[u.name]="NA"
}}}if(t.hasOwnProperty("value")){t.categoryCustom["~@workallotmentid"]=t.value
}if(t.hasOwnProperty("label")){t.categoryCustom["~@parcelid"]=t.label
}if(t.hasOwnProperty("description")){t.categoryCustom["~@invoiceid"]=t.description
}if(t.hasOwnProperty("id")){t.categoryCustom["~@lotid"]=t.id
}if(t.hasOwnProperty("status")){t.categoryCustom["~@packetid"]=t.status
}if(t.hasOwnProperty("lotId")){t.categoryCustom["~@lotNumber"]=t.lotId
}if(t.hasOwnProperty("parcelId")){t.categoryCustom["~@parcelNumber"]=t.parcelId
}});
d.stockList.push(t.categoryCustom)
});
d.updateStockAccordingToNode(d.currentActivityNode);
d.flag.configSearchFlag=false;
window.setTimeout(function(){$(window).resize();
$(window).resize()
},100)
};
c.convertorForCustomField(d.searchedStockList,r)
}},function(){b.unMaskLoading()
})
},function(k){},function(k){})
};
d.updateStockAccordingToNode=function(l){if(l!==null&&l!==undefined){d.currentActivityNode=l;
angular.forEach(d.nodeDetailsInfo,function(p){if(p.nodeId===l){var m=p.modifier;
if(m!==null){if(m.indexOf("|")>-1){var n=m.split("|");
for(var o=0;
o<n.length;
o++){if(n[o]==="Q"){d.isQuingRequired=true
}}}else{if(m==="AA"||m==="MA"){d.typeOfAllocation=m
}}}}});
if(angular.isDefined(d.nodeIdAndWorkAllocationIdsMap)){var j=[];
var k=d.nodeIdAndWorkAllocationIdsMap[l];
angular.forEach(d.stockList,function(n,m){if(k.length>0&&k.indexOf(n["~@workallotmentid"])!==-1){n["~@index"]=m+1;
j.push(n)
}});
d.currentNodeStocks=angular.copy(j);
if(angular.isDefined(d.gridApi)){d.gridApi.selection.clearSelectedRows()
}d.gridOptions.data=j;
d.gridOptions.columnDefs=d.stockLabelListForUiGrid;
d.listFilled=true;
if(!!d.isQuingRequired){d.gridOptions.enableSorting=false;
d.gridOptions.isRowSelectable=function(m){if(!!d.diamondsInQueue){if(m.entity["~@index"]>d.diamondsInQueue){return false
}else{return true
}}else{if(m.entity["~@index"]>0){return true
}else{return false
}}}
}}else{console.log("Node map not initialized")
}g.retrieveNextNodeDesignationIds(d.currentActivityNode,function(m){d.designationIdForAllot=undefined;
d.designationIdForInStock=undefined;
if(m.data!==null){if(m.data.forAllotTo!==undefined&&m.data.forAllotTo!==null){d.designationIdForAllot=m.data.forAllotTo.toString()
}if(m.data.forIssue!==undefined&&m.data.forIssue!==null){d.designationIdForInStock=m.data.forIssue.toString()
}}},function(m){console.log("failure----")
})
}};
d.setToPrintData=function(j){if(!!j){d.toPrint=j
}};
d.onBack=function(){d.stockdataflag=false;
d.initializeData();
delete d.toPrint
};
d.onCanelOfSearch=function(){delete d.toPrint;
d.gridApi.selection.clearSelectedRows()
};
d.splitStockNext=function(){console.log("$scope.toSplit::::"+JSON.stringify(d.toSplit));
var j=d.toSplit["~@workallotmentid"];
if(!!d.toSplit["~@packetid"]){d.flag.displayEditPacketflag=true;
d.totalCaratMain=d.toSplit.carat_of_packet$NM$Double;
this.lotedtflg=true
}else{if(!!d.toSplit["~@lotid"]){d.flag.displayEditLotflag=true;
d.totalCaratMain=d.toSplit.carat_of_lot$NM$Double;
this.packetedtflg=true
}}d.gridOptions1={};
d.gridOptions1.enableFiltering=true;
d.gridOptions1.columnDefs=[];
d.gridOptions1.data=[];
d.stockListToSave=[];
d.stockListTodisplay=[];
d.count=0;
d.flag.showstockPage=true;
d.flag.editStock=false;
d.stockdataflag=true;
f.retrieveInformationByWorkallotment(j,function(k){d.printdataflag=true;
i.retrieveDesignationBasedFields("stocksplit",function(m){d.SplitStock=this;
delete d.response;
d.response=m;
d.invoiceCustom=c.resetSection(d.generaInvoiceTemplate);
var o=c.retrieveSectionWiseCustomFieldInfo("invoice");
d.invoiceDbType={};
o.then(function(u){d.generaInvoiceTemplate=u.genralSection;
var t=[];
var s=Object.keys(m).map(function(v,w){angular.forEach(this[v],function(x){if(v==="Invoice#P#"){t.push({Invoice:x})
}})
},m);
d.generaInvoiceTemplate=c.retrieveCustomData(d.generaInvoiceTemplate,t);
d.invoiceCustom=k
},function(s){},function(s){});
d.parcelCustom=c.resetSection(d.generaParcelTemplate);
var r=c.retrieveSectionWiseCustomFieldInfo("parcel");
d.parcelDbType={};
r.then(function(u){d.generaParcelTemplate=u.genralSection;
var t=[];
var s=Object.keys(m).map(function(v,w){angular.forEach(this[v],function(x){if(v==="Parcel#P#"){t.push({Parcel:x})
}})
},m);
d.generaParcelTemplate=c.retrieveCustomData(d.generaParcelTemplate,t);
d.parcelCustom=k
},function(s){},function(s){});
d.lotCustom=c.resetSection(d.generaLotTemplate);
var l=c.retrieveSectionWiseCustomFieldInfo("lot");
d.lotDbType={};
l.then(function(t){d.generalLotTemplate=t.genralSection;
var u=[];
var s=Object.keys(m).map(function(v,w){angular.forEach(this[v],function(x){if(v==="Lot#P#"){u.push({Parcel:x})
}})
},m);
d.generalLotTemplate=c.retrieveCustomData(d.generalLotTemplate,u);
d.lotCustom=k
},function(s){},function(s){});
d.packetCustom=c.resetSection(d.generaPacketTemplate);
var n=c.retrieveSectionWiseCustomFieldInfo("packet");
d.packetDbType={};
n.then(function(u){d.generalPacketTemplate=u.genralSection;
var t=[];
var s=Object.keys(m).map(function(v,w){angular.forEach(this[v],function(x){if(v==="Packet#P#"){t.push({Packet:x})
}})
},m);
d.generalPacketTemplate=c.retrieveCustomData(d.generalPacketTemplate,t);
d.packetCustom=k
},function(s){},function(s){});
d.finalPayload={};
d.modelAndHeaderListForStock=[];
d.fieldNotConfigured=false;
if(d.flag.displayEditLotflag){d.lotEditCustom={};
d.reset();
var q=c.retrieveSectionWiseCustomFieldInfo("lot");
d.lotEditDbType={};
q.then(function(v){d.generalLotEditTemplate=v.genralSection;
var w=[];
var s=Object.keys(m).map(function(x,y){angular.forEach(this[x],function(z){if(x==="Lot"){w.push({Lot:z})
}})
},m);
d.generalLotEditTemplate=c.retrieveCustomData(d.generalLotEditTemplate,w);
angular.forEach(d.generalLotEditTemplate,function(x){d.modelAndHeaderListForStock.push({name:x.model,displayName:x.label,minWidth:200})
});
d.gridOptions1.columnDefs=d.modelAndHeaderListForStock;
d.gridOptions1.columnDefs.push({name:"Action",cellTemplate:'<div class="ui-grid-cell-contents"> <a ng-click="grid.appScope.updateLot(row.entity)">Update</a></div>',enableFiltering:false,minWidth:200});
d.mandatoryFields=["carat_of_lot$NM$Double"];
var t=[];
angular.forEach(d.generalLotEditTemplate,function(x){t.push(x.model)
});
if(t.length>0&&d.mandatoryFields!=null&&d.mandatoryFields.length>0){for(var u=0;
u<d.mandatoryFields.length;
u++){if(t.indexOf(d.mandatoryFields[u])===-1){d.fieldNotConfigured=true;
break
}}}else{d.fieldNotConfigured=true
}console.log("fieldNotConfigured::::"+d.fieldNotConfigured)
},function(s){},function(s){})
}d.fieldNotConfiguredForPacket=false;
if(d.flag.displayEditPacketflag){d.packetEditCustom={};
d.reset();
var p=c.retrieveSectionWiseCustomFieldInfo("packet");
d.packetEditDbType={};
p.then(function(w){d.generalPacketEditTemplate=w.genralSection;
var u=[];
var s=Object.keys(m).map(function(x,y){angular.forEach(this[x],function(z){if(x==="Packet"){u.push({Packet:z})
}})
},m);
d.generalPacketEditTemplate=c.retrieveCustomData(d.generalPacketEditTemplate,u);
angular.forEach(d.generalPacketEditTemplate,function(x){d.modelAndHeaderListForStock.push({name:x.model,displayName:x.label,minWidth:200})
});
d.gridOptions1.columnDefs=d.modelAndHeaderListForStock;
d.gridOptions1.columnDefs.push({name:"Action",cellTemplate:'<div class="ui-grid-cell-contents"> <a ng-click="grid.appScope.updateLot(row.entity)">Update</a></div>',enableFiltering:false,minWidth:200});
d.mandatoryFieldsForPacket=["carat_of_packet$NM$Double"];
var t=[];
angular.forEach(d.generalPacketEditTemplate,function(x){t.push(x.model)
});
if(t.length>0&&d.mandatoryFieldsForPacket!=null&&d.mandatoryFieldsForPacket.length>0){for(var v=0;
v<d.mandatoryFieldsForPacket.length;
v++){if(t.indexOf(d.mandatoryFieldsForPacket[v])===-1){d.fieldNotConfiguredForPacket=true;
break
}}}else{d.fieldNotConfiguredForPacket=true
}},function(s){},function(s){})
}b.unMaskLoading()
},function(){b.unMaskLoading();
var m="Failed to retrieve data";
var l=b.error;
b.addMessage(m,l)
})
},function(){})
};
d.createStock=function(){d.categoryCustom={};
if(d.flag.displayEditPacketflag){d.categoryCustom=d.packetEditCustom;
d.tempDbMap=angular.copy(d.packetEditDbType)
}else{if(d.flag.displayEditLotflag){d.categoryCustom=d.lotEditCustom;
d.tempDbMap=angular.copy(d.lotEditDbType)
}}if(Object.getOwnPropertyNames(d.categoryCustom).length>0){var j=false;
for(var m in d.categoryCustom){if(!!(d.categoryCustom[m])){j=true;
break
}}d.submitted=true;
if(d.stockform.$valid&&j){d.submitted=false;
var k=[];
k.push({categoryCustom:angular.copy(d.categoryCustom)});
d.count++;
d.stockListToSave.push(angular.copy({id:d.count,categoryCustom:d.categoryCustom}));
if(d.flag.displayEditPacketflag){this.packetedtflg=false
}else{if(d.flag.displayEditLotflag){this.lotedtflg=false
}}var l=function(n){if(d.flag.displayEditPacketflag){d.packetEditDbType=d.tempDbMap
}else{if(d.flag.displayEditLotflag){d.lotEditDbType=d.tempDbMap
}}angular.forEach(d.modelAndHeaderListForStock,function(o){if(!n[0]["categoryCustom"].hasOwnProperty(o.name)){n[0]["categoryCustom"][o.name]="NA"
}else{if(n[0]["categoryCustom"].hasOwnProperty(o.name)){if(n[0]["categoryCustom"][o.name]===null||n[0]["categoryCustom"][o.name]===""||n[0]["categoryCustom"][o.name]===undefined){n[0]["categoryCustom"][o.name]="NA"
}}}});
n[0]["categoryCustom"]["~@index"]=d.count;
d.stockListTodisplay.push(angular.copy({id:d.count,category:n[0]}));
d.gridOptions1.data.push(angular.copy(n[0]["categoryCustom"]));
d.gridOptions1.columnDefs.push({name:"Action",cellTemplate:'<div class="ui-grid-cell-contents"> <a ng-click="grid.appScope.editStockLocally(row.entity)">Edit</i></a>&nbsp;<a ng-click="grid.appScope.showPopUp(row.entity)">Delete</i></a></div>',enableFiltering:false,minWidth:200})
};
if(d.flag.displayEditPacketflag){c.convertorForCustomField(k,l)
}else{if(d.flag.displayEditLotflag){c.convertorForCustomField(k,l)
}}d.reset();
d.listFilled=true
}}};
d.editStockLocally=function(k){if(d.flag.displayEditPacketflag){d.SplitStock=this;
this.packetedtflg=false
}else{if(d.flag.displayEditLotflag){d.SplitStock=this;
this.lotedtflg=false
}}d.index=k["~@index"];
d.oldObj=angular.copy(k,d.oldObj);
if(k!==null){d.flag.editStock=true;
if(!!(d.stockListToSave&&d.stockListToSave.length>0)){for(var j=0;
j<d.stockListToSave.length;
j++){if(k["~@index"]!==null&&d.stockListToSave[j].id===k["~@index"]){if(d.flag.displayEditPacketflag){d.categoryCustom=angular.copy(d.stockListToSave[j].categoryCustom);
d.packetEditCustom=angular.copy(d.stockListToSave[j].categoryCustom)
}else{if(d.flag.displayEditLotflag){d.categoryCustom=angular.copy(d.stockListToSave[j].categoryCustom);
d.lotEditCustom=angular.copy(d.stockListToSave[j].categoryCustom)
}}break
}}}}e(function(){if(d.flag.displayEditPacketflag){d.SplitStock=this;
this.packetedtflg=true
}else{if(d.flag.displayEditLotflag){d.SplitStock=this;
this.lotedtflg=true
}}},50)
};
d.onCancel=function(){d.flag.editStock=false;
if(d.flag.displayEditPacketflag){this.packetedtflg=false
}else{if(d.flag.displayEditLotflag){this.lotedtflg=false
}}d.reset()
};
d.showPopUp=function(j){d.stockObjectToDelete=j;
d.index=j["~@index"];
$("#deleteDialog").modal("show")
};
d.hidePopUp=function(){$("#deleteDialog").modal("hide");
b.removeModalOpenCssAfterModalHide()
};
d.deleteStock=function(){for(var j=0;
j<d.gridOptions1.data.length;
j++){var k=d.gridOptions1.data[j];
if(k["~@index"]===d.index){d.gridOptions1.data.splice(j,1);
break
}}for(var j=0;
j<d.stockListToSave.length;
j++){var k=d.stockListToSave[j];
if(k.id===d.index){d.stockListToSave.splice(j,1);
break
}}if(d.flag.displayEditPacketflag){this.packetedtflg=false
}else{if(d.flag.displayEditLotflag){this.lotedtflg=false
}}d.reset();
d.flag.editStock=false;
$("#deleteDialog").modal("hide");
b.removeModalOpenCssAfterModalHide()
};
d.saveStock=function(){d.submitted=true;
if(d.flag.displayEditPacketflag){d.categoryCustom=d.packetEditCustom
}else{if(d.flag.displayEditLotflag){d.categoryCustom=d.lotEditCustom
}}if(Object.getOwnPropertyNames(d.categoryCustom).length>0){var j=false;
for(var m in d.categoryCustom){if(!!(d.categoryCustom[m])){j=true;
break
}}if((j&&d.stockform.$valid)){var k=[];
k.push({categoryCustom:angular.copy(d.categoryCustom)});
angular.forEach(d.stockListToSave,function(n){if(n.id===d.index){n.categoryCustom=(d.categoryCustom)
}});
var l=function(n){angular.forEach(d.modelAndHeaderListForStock,function(q){if(!n[0]["categoryCustom"].hasOwnProperty(q.name)){n[0]["categoryCustom"][q.name]="NA"
}else{if(n[0]["categoryCustom"].hasOwnProperty(q.name)){if(n[0]["categoryCustom"][q.name]===null||n[0]["categoryCustom"][q.name]===""||n[0]["categoryCustom"][q.name]===undefined){n[0]["categoryCustom"][q.name]="NA"
}}}});
for(var o=0;
o<d.gridOptions1.data.length;
o++){var p=d.gridOptions1.data[o];
if(p["~@index"]===d.index){d.gridOptions1.data[o]=angular.copy(((n[0].categoryCustom)));
d.gridOptions1.data[o]["~@index"]=d.index;
break
}}};
if(d.flag.displayEditPacketflag){c.convertorForCustomField(k,l);
this.packetedtflg=false
}else{if(d.flag.displayEditLotflag){c.convertorForCustomField(k,l);
this.lotedtflg=false
}}d.reset();
d.listFilled=true;
d.flag.editStock=false
}}};
d.reset=function(){d.categoryCustom={};
if(d.flag.displayEditLotflag){d.lotEditCustom={};
var k=c.retrieveSectionWiseCustomFieldInfo("lot");
d.lotEditDbType={};
d.flag.displayEditLotflag=false;
k.then(function(m){d.generalLotEditTemplate=m.genralSection;
var n=[];
var l=Object.keys(d.response).map(function(o,p){angular.forEach(this[o],function(q){if(o==="Lot"){n.push({Lot:q})
}})
},d.response);
d.generalLotEditTemplate=c.retrieveCustomData(d.generalLotEditTemplate,n);
e(function(){this.lotedtflg=true;
d.flag.displayEditLotflag=true
},100)
},function(l){},function(l){})
}if(d.flag.displayEditPacketflag){d.packetEditCustom={};
var j=c.retrieveSectionWiseCustomFieldInfo("packet");
d.packetEditDbType={};
d.flag.displayEditPacketflag=false;
j.then(function(n){d.generalPacketEditTemplate=n.genralSection;
var m=[];
var l=Object.keys(d.response).map(function(o,p){angular.forEach(this[o],function(q){if(o==="Packet"){m.push({Packet:q})
}})
},d.response);
d.generalPacketEditTemplate=c.retrieveCustomData(d.generalPacketEditTemplate,m);
e(function(){this.packetedtflg=true;
d.flag.displayEditPacketflag=true
},100)
},function(l){},function(l){})
}};
d.splitStock=function(){var l={};
l.allotmentIds=[];
l.allotmentIds.push(d.toSplit["~@workallotmentid"]);
var k=undefined;
for(var m=0;
m<d.gridOptions1.data.length;
m++){var o=d.gridOptions1.data[m];
if(d.flag.displayEditLotflag){if(k!==undefined){k=k+parseFloat(o.carat_of_lot$NM$Double)
}else{k=parseFloat(o.carat_of_lot$NM$Double)
}}else{if(d.flag.displayEditPacketflag){if(k!==undefined){k=k+parseFloat(o.carat_of_packet$NM$Double)
}else{k=parseFloat(o.carat_of_packet$NM$Double)
}}}}if(d.totalCaratMain===k){if(d.flag.displayEditPacketflag){l.type="Packet";
l.id=d.toSplit["~@packetid"];
l.parentID=d.toSplit["~@lotid"];
l.stockDbType=d.packetEditDbType
}else{if(d.flag.displayEditLotflag){l.type="Lot";
l.id=d.toSplit["~@lotid"];
l.parentID=d.toSplit["~@parcelid"];
l.stockDbType=d.lotEditDbType
}}if(!!d.stockListToSave){var j=[];
angular.forEach(d.stockListToSave,function(q){j.push(q.categoryCustom)
});
l.stockDataForSplit=j;
b.maskLoading();
g.splitStock(l,function(q){b.unMaskLoading();
d.onBack();
delete d.toSplit
},function(){b.unMaskLoading();
d.onBack();
delete d.toSplit;
var r="Failed to split stock";
var q=b.error;
b.addMessage(r,q)
})
}else{}}else{var p="Carat value does not match, please try again.";
var n=b.error;
b.addMessage(p,n)
}};
b.unMaskLoading()
}])
});