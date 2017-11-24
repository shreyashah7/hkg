define(["hkg","finalizeService","customFieldService","parcelService","lotmanagement/edit-lot.controller","ngload!uiGrid","addMasterValue","printBarcodeValue","dynamicForm","accordionCollapse"],function(a){a.register.controller("FinalizeServiceController",["$rootScope","$scope","FinalizeService","CustomFieldService","DynamicFormService","ParcelService",function(g,j,b,h,i,f){g.mainMenu="stockLink";
g.childMenu="finalize";
g.activateMenu();
var c=i.retrieveSearchWiseCustomFieldInfo("finalize");
j.flag={};
j.flag.editMode=false;
var d=[];
j.planFlag={};
j.tag=1;
j.modifyName="";
j.finalizeDbType={};
j.nodeDetailsInfo=[];
j.labelListForUiGrid=[];
j.submitted=false;
c.then(function(m){j.generalSearchTemplate=m.genralSection;
if(m.genralSection===undefined||m.genralSection===null){j.generalSearchTemplate=[]
}if(j.generalSearchTemplate!==null&&j.generalSearchTemplate.length>0){var k={};
angular.forEach(j.generalSearchTemplate,function(n){if(n.model){k[n.model]=n.featureName;
j.labelListForUiGrid.push({name:n.model,displayName:n.label,minWidth:200})
}else{if(n.fromModel){k[n.fromModel]=n.featureName;
j.labelListForUiGrid.push({name:n.fromModel,displayName:n.label,minWidth:200})
}else{if(n.toModel){k[n.toModel]=n.featureName;
j.labelListForUiGrid.push({name:n.toModel,displayName:n.label,minWidth:200})
}}}});
var l={};
if(k!==null){angular.forEach(k,function(o,n){if(!l[o]){l[o]=[]
}l[o].push(n)
})
}}j.retrieveFromWorkAllocation=function(){if(!!l){j.listFilled=false;
j.plantype=undefined;
g.maskLoading();
b.searchLotFromWorkAllocation(l,function(p){g.unMaskLoading();
j.nodeDetailsInfo=[];
if(p!==null&&p.custom2&&p.custom2.length>0){j.modifierMap={};
if(p.serviceInitDataBean!==null&&p.serviceInitDataBean!==undefined){j.modifierMap.nodeId=p.serviceInitDataBean.dynamicServiceInitDataBeans[0].nodeId;
j.modiferList=p.serviceInitDataBean.dynamicServiceInitDataBeans[0].modifier.split("|");
j.dynamicServiceDtaBeans=p.serviceInitDataBean.dynamicServiceInitDataBeans;
j.modifierMap.plan=j.modiferList[0];
j.modifyName="Write ";
if(j.modifierMap.plan==="PL"){j.modifyName+="Plan"
}else{if(j.modifierMap.plan==="ES"){j.modifyName+="Estimation"
}else{if(j.modifierMap.plan==="PA"){j.modifyName+="Parameter"
}}}j.plantype=j.modiferList[0];
if(j.modiferList[1]!==undefined&&j.modiferList[1]!==null){j.nonMandatoryModifierList=j.modiferList[1].split(",")
}var n=p.serviceInitDataBean;
if(!!n){if(n.nodeAndWorkAllocationIds!==null){j.nodeIdAndWorkAllocationIdsMap=angular.copy(n.nodeAndWorkAllocationIds)
}if(n.mandatoryFields!==null){j.stockStaticFields=n.mandatoryFields
}if(n.diamondsInQueue!==null){j.diamondsInQueue=n.diamondsInQueue
}if(!!n.dynamicServiceInitDataBeans){var q=n.dynamicServiceInitDataBeans;
if(angular.isArray(q)&&q.length>0){angular.forEach(q,function(s){var t={};
t.groupId=s.groupId;
t.groupName=s.groupName;
t.modifier=s.modifier;
t.nodeId=s.nodeId;
t.nodeName=s.nodeName;
j.nodeDetailsInfo.push(t)
})
}if(j.nodeDetailsInfo.length>1){j.flag.multipleIdInvolved=true
}else{j.flag.multipleIdInvolved=false
}}if(j.nodeDetailsInfo.length>0){j.currentActivityNode=j.nodeDetailsInfo[0].nodeId
}}}p=angular.copy(p.custom2);
j.searchedDataFromDb=angular.copy(p);
j.issuedStock={};
j.issuedStock.enableFiltering=true;
j.issuedStock.multiSelect=false;
j.issuedStock.enableRowSelection=true;
j.selectedStock=[];
j.issuedStock.onRegisterApi=function(s){j.gridApi=s;
s.selection.on.rowSelectionChanged(j,function(t){if(j.selectedStock.length>0){$.each(j.selectedStock,function(v,u){if(u["$$hashKey"]===t.entity["$$hashKey"]){j.flag.repeatedRow=true;
j.selectedStock.splice(v,1);
return false
}else{j.flag.repeatedRow=false
}});
if(!j.flag.repeatedRow){j.selectedStock.push(t.entity)
}}else{j.selectedStock.push(t.entity)
}if(j.selectedStock.length>0){j.flag.rowSelectedflag=true
}else{j.flag.rowSelectedflag=false
}});
s.selection.on.rowSelectionChangedBatch(j,function(t){if(j.selectedStock.length>0){angular.forEach(t,function(u){$.each(j.selectedStock,function(w,v){if(v["$$hashKey"]===u.entity["$$hashKey"]){j.flag.repeatedRow=true;
j.selectedStock=[];
return false
}else{j.flag.repeatedRow=false
}});
if(!j.flag.repeatedRow){j.selectedStock.push(u.entity)
}})
}else{angular.forEach(t,function(u){j.selectedStock.push(u.entity)
})
}if(j.selectedStock.length>0){j.flag.rowSelectedflag=true
}else{j.flag.rowSelectedflag=false
}})
};
j.issuedStockList=[];
for(var o=0;
o<p.length;
o++){if(p[o].custom1!==null){p[o].categoryCustom=(p[o].custom1);
p[o].categoryCustom["$$$value"]=(p[o].value)
}}var r=function(s){j.searchedData=angular.copy(p);
angular.forEach(j.searchedData,function(t){angular.forEach(j.labelListForUiGrid,function(u){if(t.custom1!==null&&!t.custom1.hasOwnProperty(u.name)){t.custom1[u.name]="NA"
}else{if(t.custom1!==null&&t.custom1.hasOwnProperty(u.name)){if(!(!!(t.custom1[u.name]))){t.custom1[u.name]="NA"
}}}if(t.hasOwnProperty("status")){t.custom1["~@status"]=t.status
}});
if(t.custom1!==null){j.issuedStockList.push(t.custom1)
}});
j.updateStockAccordingToNode(j.currentActivityNode)
};
i.convertorForCustomField(p,r);
window.setTimeout(function(){$(window).resize();
$(window).resize()
},200);
j.issueListFilled=true;
j.dataRetrieved=true
}else{j.dataRetrieved=true;
j.listFilled=true
}},function(){var o="Could not retrieve, please try again.";
var n=g.error;
g.addMessage(o,n);
g.unMaskLoading()
})
}else{j.dataRetrieved=true
}};
j.retrieveFromWorkAllocation()
},function(k){},function(k){});
j.updateStockAccordingToNode=function(o){if(o!==null&&o!==undefined){j.currentActivityNode=o;
angular.forEach(j.nodeDetailsInfo,function(s){if(s.nodeId===o){var p=s.modifier;
if(p!==null){if(p.indexOf("|")>-1){var q=p.split("|");
for(var r=0;
r<q.length;
r++){if(q[r]==="Q"){j.isQuingRequired=true
}}}else{if(p==="AA"||p==="MA"){j.typeOfAllocation=p
}}}}});
if(angular.isDefined(j.nodeIdAndWorkAllocationIdsMap)){var l=[];
var m=j.nodeIdAndWorkAllocationIdsMap[o];
angular.forEach(j.issuedStockList,function(q,p){if(m.length>0&&m.indexOf(q["~@status"])!==-1){q["~@index"]=p+1;
l.push(q)
}});
j.currentNodeStocks=angular.copy(l);
if(angular.isDefined(j.gridApi)){j.gridApi.selection.clearSelectedRows()
}j.issuedStock.data=l;
j.issuedStock.columnDefs=j.labelListForUiGrid;
j.listFilled=true;
if(!!j.isQuingRequired){j.issuedStock.enableSorting=false;
j.issuedStock.isRowSelectable=function(p){if(!!j.diamondsInQueue){if(p.entity["~@index"]>j.diamondsInQueue){return false
}else{return true
}}else{if(p.entity["~@index"]>0){return true
}else{return false
}}}
}}else{}if(j.dynamicServiceDtaBeans!==null&&j.dynamicServiceDtaBeans!==undefined&&j.dynamicServiceDtaBeans.length>0){for(var n=0;
n<j.dynamicServiceDtaBeans.length;
n++){if(j.dynamicServiceDtaBeans[n].nodeId===j.currentActivityNode){console.log("updateStockAccordingToNode called"+j.dynamicServiceDtaBeans[n].nodeId);
j.modifierMap.nodeId=j.dynamicServiceDtaBeans[n].nodeId;
j.modiferList=j.dynamicServiceDtaBeans[n].modifier.split("|");
console.log(j.dynamicServiceDtaBeans[n].modifier.split("|"));
var k=j.dynamicServiceDtaBeans[n];
j.modifierMap.plan=j.modiferList[0];
j.modifyName="Write ";
if(j.modifierMap.plan==="PL"){j.modifyName+="Plan"
}else{if(j.modifierMap.plan==="ES"){j.modifyName+="Estimation"
}else{if(j.modifierMap.plan==="PA"){j.modifyName+="Parameter"
}}}j.plantype=j.modiferList[0];
if(j.modiferList[1]!==undefined&&j.modiferList[1]!==null){j.nonMandatoryModifierList=j.modiferList[1].split(",")
}else{j.nonMandatoryModifierList=[]
}if(!!k){if(k.nodeAndWorkAllocationIds!==null){j.nodeIdAndWorkAllocationIdsMap=angular.copy(k.nodeAndWorkAllocationIds)
}if(k.mandatoryFields!==null){j.stockStaticFields=k.mandatoryFields
}if(k.diamondsInQueue!==null){j.diamondsInQueue=k.diamondsInQueue
}}}}}}};
j.initFinalizeForm=function(k){j.finalizeForm=k
};
j.addPlan=function(){j.flag.editMode=false;
if(j.selectedStock!==undefined&&j.selectedStock!==null&&j.selectedStock.length>0){g.maskLoading();
j.hasPriceCalculatorRight=false;
d=[];
j.workAllocationId=undefined;
j.count=0;
j.gridOptions={};
j.selectedLotOrPacket=[];
j.gridOptions.onRegisterApi=function(n){j.gridApi1=n;
n.selection.on.rowSelectionChanged(j,function(o){if(!!j.gridApiSubmit&&!!j.gridApiSubmit.selection){j.gridApiSubmit.selection.clearSelectedRows()
}j.selectedLotOrPacket=[];
j.selectedLotOrPacket.push(o.entity)
})
};
j.labelForUIGrid=[];
j.lotIdForConstraint=undefined;
j.packetIdForConstraint=undefined;
for(var l=0;
l<j.searchedDataFromDb.length;
l++){if(j.searchedDataFromDb[l].value===j.selectedStock[0].$$$value){j.invoiceIdForConstraint=j.searchedDataFromDb[l].description;
j.parcelIdForConstraint=j.searchedDataFromDb[l].label;
j.lotIdForConstraint=angular.copy(j.searchedDataFromDb[l].value);
j.modifierMap.objectId=angular.copy(j.searchedDataFromDb[l].value);
j.modifierMap.isPacket="false";
if(j.searchedDataFromDb[l].id!==null){j.packetIdForConstraint=angular.copy(j.searchedDataFromDb[l].id);
j.modifierMap.objectId=angular.copy(j.searchedDataFromDb[l].value);
j.modifierMap.isPacket="true"
}j.workAllocationId=angular.copy(j.searchedDataFromDb[l].status);
break
}}b.retrievePriceList(function(n){j.pricelistDtl=JSON.parse(angular.toJson(n));
for(var p in j.pricelistDtl){if(j.pricelistDtl.hasOwnProperty(p)){var o=j.pricelistDtl[p];
j.pricelistDtl[p]=new Date(o).toUTCString().replace(/\s*(GMT|UTC)$/,"")
}}});
b.retrieveCurrency(function(n){j.currencyCodeList=[];
if(n!==undefined&&n!==null){angular.forEach(n,function(o){j.currencyCodeList.push({currencyCode:o.value,currency:o.label})
})
}},function(){var o="Could not retrieve currency, please try again.";
var n=g.error;
g.addMessage(o,n);
g.unMaskLoading()
});
b.retrieveValuesOfMaster(function(o){g.unMaskLoading();
j.flag.masterValueNotExist=false;
j.colorRadioList=[];
j.clarityRadioList=[];
j.cutRadioList=[];
j.fluorescenceRadioList=[];
if(o!==null&&o.length>0){var q=angular.copy(o);
angular.forEach(q,function(r){if(r.masterName==="Color"){j.colorRadioList.push({color:angular.copy(r.valueEntityId),name:angular.copy(r.value)})
}else{if(r.masterName==="Cut"){j.cutRadioList.push({cut:angular.copy(r.valueEntityId),name:angular.copy(r.value)})
}else{if(r.masterName==="Clarity"){j.clarityRadioList.push({clarity:angular.copy(r.valueEntityId),name:angular.copy(r.value)})
}else{if(r.masterName==="Fluroscene"){j.fluorescenceRadioList.push({fluorescence:angular.copy(r.valueEntityId),name:angular.copy(r.value)})
}}}}});
if(j.colorRadioList.length===0||j.cutRadioList.length===0||j.clarityRadioList.length===0||j.fluorescenceRadioList.length===0){j.flag.masterEmpty=true
}else{j.selectedRadio=angular.copy({color:j.colorRadioList[0].color,cut:j.cutRadioList[0].cut,clarity:j.clarityRadioList[0].clarity,fluorescence:j.fluorescenceRadioList[0].fluorescence});
j.numberOfTags=[];
for(var n=1;
n<1000;
n++){j.numberOfTags.push(angular.copy({title:n}))
}j.tabAndDynamicFormModel={};
j.selectedNoOfTag=[];
j.tag=1;
j.checkDrop=true;
j.currencyCode="USD($)";
j.tabAndDynamicFormModel.A=null;
j.previousTab="A";
j.selectedNoOfTag.push(1);
j.tabManager={};
j.tagList=[];
j.numberOfTagChanged(1);
j.tabManager.tagList=j.tagList
}j.detailsFilled=true
}else{j.flag.masterValueNotExist=true
}j.gridOptionForSubmit={};
j.submittedPlan=[];
j.planSubmit=false;
if(!j.flag.masterEmpty&&!!j.nonMandatoryModifierList&&j.nonMandatoryModifierList.length>0&&j.nonMandatoryModifierList.indexOf("SP")!==-1){b.retrieveAccessiblePlan(j.modifierMap,function(r){if(r!==null&&r.length>0){angular.forEach(r,function(u){u.carat=u.caratValue;
u.cent=0;
if(u.caratValue!==null&&u.caratValue.toString().indexOf(".")!==-1){var t=u.caratValue.toString().split(".");
u.carat=parseInt(t[0]);
u.cent=parseInt(t[1])
}});
j.submittedPlan=angular.copy(r);
angular.forEach(r,function(t){t.categoryCustom={};
t.categoryCustom=angular.copy(t.finalizeCustom)
});
j.submiitedPlanColum=[];
j.submiitedPlanDataForUIGrid=[];
j.submiitedPlanColum.push({name:"planID$AG$String",displayName:"Plan ID",minWidth:200});
j.submiitedPlanColum.push({name:"carat",displayName:"Carat",minWidth:200});
j.submiitedPlanColum.push({name:"tag",displayName:"Tag",minWidth:200});
j.submiitedPlanColum.push({name:"cut$DRP$Long",displayName:"Cut",minWidth:200});
j.submiitedPlanColum.push({name:"color$DRP$Long",displayName:"Color",minWidth:200});
j.submiitedPlanColum.push({name:"clarity$DRP$Long",displayName:"Clarity",minWidth:200});
j.submiitedPlanColum.push({name:"price",displayName:"Value",minWidth:200});
j.submiitedPlanColum.push({name:"createdByName",displayName:"Plan By",minWidth:200});
j.submiitedPlanColum.push({name:"Action",cellTemplate:'<div class="ui-grid-cell-contents"> <a ng-click="grid.appScope.editPlan(row.entity,true)">Improve Plan</i></a></div>',enableFiltering:false,minWidth:200});
var s=function(t){j.submittedPlanData=angular.copy(t);
angular.forEach(j.submittedPlanData,function(u){angular.forEach(j.submiitedPlanColum,function(v){if(!u.categoryCustom.hasOwnProperty(v.name)){u.categoryCustom[v.name]="NA"
}else{if(u.categoryCustom.hasOwnProperty(v.name)){if(u.categoryCustom[v.name]===null||u.categoryCustom[v.name]===""||u.categoryCustom[v.name]===undefined){u.categoryCustom[v.name]="NA"
}}}});
if(u.id!==null){u.categoryCustom["~@id"]=u.id
}if(u.sequentialPlanId!==null){u.categoryCustom["~@sequentialPlanId"]=u.sequentialPlanId
}j.submiitedPlanDataForUIGrid.push(u.categoryCustom)
});
j.gridOptionForSubmit.data=j.submiitedPlanDataForUIGrid;
j.gridOptionForSubmit.columnDefs=j.submiitedPlanColum;
j.gridOptionForSubmit.enableFiltering=true;
j.gridOptionForSubmit.multiSelect=false;
j.gridOptionForSubmit.onRegisterApi=function(u){j.gridApiSubmit=u;
u.selection.on.rowSelectionChanged(j,function(v){if(!!j.gridApi1&&!!j.gridApi1.selection){j.gridApi1.selection.clearSelectedRows()
}j.selectedLotOrPacket=[];
j.selectedLotOrPacket.push(v.entity)
})
};
j.planSubmit=true;
window.setTimeout(function(){$(window).resize();
$(window).resize()
},100)
};
i.convertorForCustomField(r,s)
}},function(){var s="Could not retrieve, please try again.";
var r=g.error;
g.addMessage(s,r);
g.unMaskLoading()
})
}j.searchedDataFromDbForUiGrid=[];
if(!j.flag.masterEmpty){var p=j.selectedStock[0]["~@status"];
b.retrieveSavedPlan(p.toString(),function(r){angular.forEach(r,function(u){u.carat=u.caratValue;
u.cent=0;
if(u.caratValue!==null&&u.caratValue.toString().indexOf(".")!==-1){var t=u.caratValue.toString().split(".");
u.carat=parseInt(t[0]);
u.cent=parseInt(t[1])
}});
j.enteredPlan=angular.copy(r);
angular.forEach(r,function(t){t.categoryCustom={};
t.categoryCustom=angular.copy(t.finalizeCustom)
});
j.planListForUiGrid=[];
j.planListForUiGrid.push({name:"planID$AG$String",displayName:"Plan ID",minWidth:200});
j.planListForUiGrid.push({name:"tag",displayName:"Tag",minWidth:200});
j.planListForUiGrid.push({name:"cut$DRP$Long",displayName:"Cut",minWidth:200});
j.planListForUiGrid.push({name:"color$DRP$Long",displayName:"Color",minWidth:200});
j.planListForUiGrid.push({name:"clarity$DRP$Long",displayName:"Clarity",minWidth:200});
j.planListForUiGrid.push({name:"price",displayName:"Value",minWidth:200});
j.planListForUiGrid.push({name:"Action",cellTemplate:'<div class="ui-grid-cell-contents"> <a ng-click="grid.appScope.editPlan(row.entity,false)">Edit</i></a>&nbsp;<a ng-click="grid.appScope.openDeletePlanPopup(row.entity)">Delete</i></a></div>',enableFiltering:false,minWidth:200});
var s=function(t){j.searchedData=angular.copy(t);
angular.forEach(j.searchedData,function(u){angular.forEach(j.planListForUiGrid,function(v){if(!u.categoryCustom.hasOwnProperty(v.name)){u.categoryCustom[v.name]="NA"
}else{if(u.categoryCustom.hasOwnProperty(v.name)){if(u.categoryCustom[v.name]===null||u.categoryCustom[v.name]===""||u.categoryCustom[v.name]===undefined){u.categoryCustom[v.name]="NA"
}}}});
j.searchedDataFromDbForUiGrid.push(u.categoryCustom)
});
j.gridOptions.data=j.searchedDataFromDbForUiGrid;
j.gridOptions.columnDefs=j.planListForUiGrid;
j.gridOptions.enableFiltering=true;
j.gridOptions.multiSelect=false;
j.gridOptions.enableRowSelection=true;
j.gridOptions.enableSelectAll=false;
window.setTimeout(function(){$(window).resize();
$(window).resize()
},100)
};
i.convertorForCustomField(r,s);
g.unMaskLoading()
},function(){var s="Could not retrieve, please try again.";
var r=g.error;
g.addMessage(s,r);
g.unMaskLoading()
})
}},function(){var o="Could not retrieve, please try again.";
var n=g.error;
g.addMessage(o,n);
g.unMaskLoading()
});
j.flag.showAddPage=true;
var m={};
var k=[];
j.packetDataBean={};
j.packetListToSave=[];
j.parcelDataBean={invoiceDataBean:{invoiceCustom:"",invoiceDbType:""}};
h.retrieveDesignationBasedFields("finalize",function(n){j.response=angular.copy(n);
var s=i.retrieveSectionWiseCustomFieldInfo("invoice");
j.invoiceDbType={};
s.then(function(t){j.generaInvoiceTemplate=t.genralSection;
j.generaInvoiceTemplate=i.retrieveCustomData(j.generaInvoiceTemplate,n);
if(j.generaInvoiceTemplate!==null&&j.generaInvoiceTemplate.length>0){angular.forEach(j.generaInvoiceTemplate,function(u){if(u.model){k.push(u.model)
}else{if(u.fromModel){k.push(u.fromModel)
}else{if(u.toModel){k.push(u.toModel)
}}}});
if(k!==null&&k.length>0){m.invoice=k
}}},function(t){},function(t){});
var o=i.retrieveSectionWiseCustomFieldInfo("parcel");
j.parcelDbType={};
o.then(function(u){j.generaParcelTemplate=u.genralSection;
j.generaParcelTemplate=i.retrieveCustomData(j.generaParcelTemplate,n);
var t=[];
if(j.generaParcelTemplate!==null&&j.generaParcelTemplate.length>0){angular.forEach(j.generaParcelTemplate,function(v){if(v.model){t.push(v.model)
}else{if(v.fromModel){t.push(v.fromModel)
}else{if(v.toModel){t.push(v.toModel)
}}}});
if(t!==null&&t.length>0){m.parcel=t
}}},function(t){},function(t){});
var r=i.retrieveSectionWiseCustomFieldInfo("lot");
j.lotDbType={};
r.then(function(u){j.generalLotTemplate=u.genralSection;
j.generalLotTemplate=i.retrieveCustomData(j.generalLotTemplate,n);
var t=[];
if(j.generalLotTemplate!==null&&j.generalLotTemplate.length>0){angular.forEach(j.generalLotTemplate,function(v){if(v.model){t.push(v.model)
}else{if(v.fromModel){t.push(v.fromModel)
}else{if(v.toModel){t.push(v.toModel)
}}}});
if(t!==null&&t.length>0){m.lot=t
}}},function(t){},function(t){});
var q=i.retrieveSectionWiseCustomFieldInfo("packet");
j.packetDbType={};
q.then(function(u){j.generalPacketTemplate=u.genralSection;
j.generalPacketTemplate=i.retrieveCustomData(j.generalPacketTemplate,n);
var t=[];
if(j.generalPacketTemplate!==null&&j.generalPacketTemplate.length>0){angular.forEach(j.generalPacketTemplate,function(v){if(v.model){t.push(v.model)
}else{if(v.fromModel){t.push(v.fromModel)
}else{if(v.toModel){t.push(v.toModel)
}}}});
if(t!==null&&t.length>0){m.packet=t
}}var t=[];
if(j.packetIdForConstraint!=null){t.push(j.packetIdForConstraint);
m.packetId=t
}else{t.push(j.lotIdForConstraint);
m.lotId=t
}b.retrieveStockById(m,function(v){console.log("hereeeeeeeee--------------------------------"+JSON.stringify(v));
if(v!==null){j.invoiceCustom=v.custom1;
j.parcelCustom=v.custom3;
j.lotCustom=v.custom4;
j.packetCustom=v.custom5;
if(v.custom1===null||v.custom1===undefined){j.invoiceCustom={}
}if(v.custom3===null||v.custom3===undefined){j.parcelCustom={}
}if(v.custom4===null||v.custom4===undefined){j.lotCustom={}
}if(v.custom5===null||v.custom5===undefined){j.packetCustom={}
}}},function(){var w="Could not retrieve, please try again.";
var v=g.error;
g.addMessage(w,v);
g.unMaskLoading()
})
},function(t){},function(t){});
j.categoryCustom=i.resetSection(j.generalFinalizeTemplate);
var p=i.retrieveSectionWiseCustomFieldInfo("plan");
if(!(j.finalizeDbType!==null)){j.finalizeDbType={}
}p.then(function(x){j.generalFinalizeTemplate=x.genralSection;
var w=[];
var t=Object.keys(n).map(function(y,z){angular.forEach(this[y],function(A){if(y!=="Plan#P#"){w.push({Plan:A})
}})
},n);
j.generalFinalizeTemplate=i.retrieveCustomData(j.generalFinalizeTemplate,w);
if(!!j.nonMandatoryModifierList&&j.nonMandatoryModifierList.length>0&&j.nonMandatoryModifierList.toString().indexOf("SHPC")!==-1){j.hasPriceCalculatorRight=true;
if(j.hasPriceCalculatorRight&&j.generalFinalizeTemplate!==null&&j.generalFinalizeTemplate!==undefined&&j.generalFinalizeTemplate.length>0){var v=0;
for(var u=0;
u<j.generalFinalizeTemplate.length;
u++){if(j.generalFinalizeTemplate[u].model==="color$DRP$Long"){j.generalFinalizeTemplate.splice(u,1);
u=u-1;
v++
}else{if(j.generalFinalizeTemplate[u].model==="cut$DRP$Long"){j.generalFinalizeTemplate.splice(u,1);
u=u-1;
v++
}else{if(j.generalFinalizeTemplate[u].model==="clarity$DRP$Long"){j.generalFinalizeTemplate.splice(u,1);
u=u-1;
v++
}else{if(j.generalFinalizeTemplate[u].model==="fluroscene$DRP$Long"){j.generalFinalizeTemplate.splice(u,1);
u=u-1;
v++
}else{if(j.generalFinalizeTemplate[u].model==="carat$NM$Double"){j.generalFinalizeTemplate.splice(u,1);
u=u-1;
v++
}else{if(j.generalFinalizeTemplate[u].model==="carate_range_of_plan$DRP$Long"){j.generalFinalizeTemplate.splice(u,1);
u=u-1;
v++
}else{if(j.generalFinalizeTemplate[u].model==="breakage$PRC$Double"){j.generalFinalizeTemplate.splice(u,1);
u=u-1;
v++
}}}}}}}}}}else{if(!j.hasPriceCalculatorRight&&j.generalFinalizeTemplate.length>0){j.finalizeAddFlag=true
}else{if(j.hasPriceCalculatorRight===false&&j.generalFinalizeTemplate.length===0){j.generalFinalizeTemplate=[];
j.finalizeAddFlag=true
}}}angular.forEach(j.generalFinalizeTemplate,function(y){if(y.model){j.labelForUIGrid.push({name:y.model,displayName:y.label,minWidth:200})
}else{if(y.fromModel){j.labelForUIGrid.push({name:y.fromModel,displayName:y.label,minWidth:200})
}else{if(y.toModel){j.labelForUIGrid.push({name:y.toModel,displayName:y.label,minWidth:200})
}}}});
j.flag.customFieldGenerated=true;
j.finalizeAddFlag=true;
j.planSelect=true
},function(t){},function(t){})
},function(){g.unMaskLoading();
var o="Failed to retrieve data";
var n=g.error;
g.addMessage(o,n)
})
}};
j.numberOfTagChanged=function(u){j.submitted=true;
u=parseInt(u);
if(u!==null&&((u>j.tagList.length&&j.finalizeForm.$valid)||u<=j.tagList.length)){j.submitted=false;
var r=0;
var q=1;
var m=["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"];
var p=[""];
while(q<=u){var s=p[r];
for(var k in m){var l=s+m[k];
p.push(l);
q++;
if(u===1&&j.count===0){j.count++;
j.tagList.push({code:l,colorRadioList:(j.colorRadioList),carat:0,cents:0,clarity:angular.copy(j.clarityRadioList),cut:j.cutRadioList,fluorescence:j.fluorescenceRadioList,colorGroup:1000,clarityGroup:1001,cutGroup:1002,fluorescenceGroup:1003,selectColor:j.colorRadioList[0].color,selectClarity:j.clarityRadioList[0].clarity,selectCut:j.cutRadioList[0].cut,selectFluorescence:j.fluorescenceRadioList[0].fluorescence,price:null,currencyCode:j.currencyCode,breakage:false});
j.flag.showAddPage=true
}if(q===u+1){break
}}r++
}p.splice(0,1);
if(p.length>j.tagList.length){for(var o=j.tagList.length;
o<=p.length;
o++){if(p[o]!==undefined){j.tagList.push({code:p[o],colorRadioList:(j.colorRadioList),carat:0,cents:0,clarity:angular.copy(j.clarityRadioList),cut:j.cutRadioList,fluorescence:j.fluorescenceRadioList,colorGroup:o,clarityGroup:o,cutGroup:o,fluorescenceGroup:o,selectColor:j.colorRadioList[0].color,selectClarity:j.clarityRadioList[0].clarity,selectCut:j.cutRadioList[0].cut,selectFluorescence:j.fluorescenceRadioList[0].fluorescence,price:null,currencyCode:j.currencyCode,breakage:false});
j.tabAndDynamicFormModel[p[o]]=null
}}}else{if(p.length<j.tagList.length){j.tagToRemove=[];
for(var n=0;
n<=j.tagList.length;
n++){if(j.tagList[n]!==undefined&&p.indexOf(j.tagList[n].code)===-1){j.tagToRemove.push(j.tagList[n].code)
}}if(j.tagToRemove.length>0){for(var n=0;
n<j.tagList.length;
n++){for(var t=0;
t<j.tagToRemove.length;
t++){if(j.tagList[n]!==undefined&&(j.tagToRemove[t])===j.tagList[n].code){delete j.tabAndDynamicFormModel[j.tagList[n].code];
j.tagList.splice(n,1)
}}}}}}}else{j.tag=j.tagList.length
}};
j.caratChange=function(k,o,l,n){if(!(k!==null)){if(j.tabManager!==null){if(j.tabManager.tagList!==null){for(var m=0;
m<j.tabManager.tagList.length;
m++){if(j.tabManager.tagList[m].code==n){j.tabManager.tagList[m].carat=0;
break
}}}}}};
j.calculatePrice=function(o){j.finalCarat=undefined;
var n={};
if(o!==null&&o.length>0){if(j.tabManager.tagList!==null&&j.tabManager.tagList.length>0){for(var l=0;
l<j.tagList.length;
l++){if(j.tabManager.tagList[l].code===o){var k=angular.copy(j.tabManager.tagList[l].carat);
var m=angular.copy(j.tabManager.tagList[l].cents);
j.finalCarat=parseFloat(k.toString().concat(".",m.toString()));
n.color=angular.copy(j.tabManager.tagList[l].selectColor);
n.cut=angular.copy(j.tabManager.tagList[l].selectCut);
n.clarity=angular.copy(j.tabManager.tagList[l].selectClarity);
n.fluroscene=angular.copy(j.tabManager.tagList[l].selectFluorescence);
n.carat=j.finalCarat;
b.checkCaratRange(n,function(p){if(p!==null){j.tabManager.tagList[l].price=p.price
}},function(){var q="Could not retrieve, please try again.";
var p=g.error;
g.addMessage(q,p);
g.unMaskLoading()
});
break
}}}}};
j.centChange=function(n,o,k,m){if(!(n!==null)){if(j.tabManager!==null){if(j.tabManager.tagList!==null){for(var l=0;
l<j.tabManager.tagList.length;
l++){if(j.tabManager.tagList[l].code==m){j.tabManager.tagList[l].cents=0;
break
}}}}}};
j.reset=function(l){if(l==="categoryCustom"){j.categoryCustom={};
var k=i.retrieveSectionWiseCustomFieldInfo("plan");
k.then(function(o){var n=[];
var m=Object.keys(j.response).map(function(p,q){angular.forEach(this[p],function(r){if(p!=="Plan#P#"){n.push({Plan:r})
}})
},j.response);
j.generalFinalizeTemplate=null;
j.generalFinalizeTemplate=o.genralSection;
j.generalFinalizeTemplate=i.retrieveCustomData(j.generalFinalizeTemplate,n);
j.finalizeAddFlag=true;
g.unMaskLoading();
j.flag.customFieldGenerated=true
},function(m){},function(m){})
}};
j.tabChanged=function(k){j.submitted=false;
for(prop in j.tabAndDynamicFormModel){if(prop===j.previousTab){j.tabAndDynamicFormModel[prop]=angular.copy(j.categoryCustom);
j.finalizeAddFlag=false;
j.reset("categoryCustom");
j.previousTab=angular.copy(k);
break
}}if(j.tabAndDynamicFormModel[k]){j.categoryCustom=angular.copy(j.tabAndDynamicFormModel[k])
}};
j.savePlan=function(k){j.submitted=true;
if(k.$valid){j.submitted=false;
for(prop in j.tabAndDynamicFormModel){if(prop===j.previousTab){j.tabAndDynamicFormModel[prop]=angular.copy(j.categoryCustom);
j.finalizeAddFlag=false;
j.reset("categoryCustom");
break
}}angular.forEach(j.tabAndDynamicFormModel,function(o,n){for(var m=0;
m<j.tagList.length;
m++){if(j.tagList[m].code===n){j.tagList[m].dynamicForm=o
}}});
j.finalizeServiceDataBean={};
j.finalizeServiceDataBean.planType=j.plantype;
if(!!j.improveFrom){j.finalizeServiceDataBean.improveFromId=j.improveFrom
}else{j.finalizeServiceDataBean.improveFromId=null
}j.finalizeServiceDataBean.finalizeDbType=j.finalizeDbType;
if(j.packetIdForConstraint!==undefined){j.finalizeServiceDataBean.id=j.packetIdForConstraint;
j.finalizeServiceDataBean.hasPacket=true
}else{j.finalizeServiceDataBean.id=j.lotIdForConstraint;
j.finalizeServiceDataBean.hasPacket=false
}var l=[];
if(j.hasPriceCalculatorRight){angular.forEach(j.tagList,function(m){l.push({tag:m.code,colorId:m.selectColor,clarityId:m.selectClarity,cutId:m.selectCut,flurosceneId:m.selectFluorescence,price:m.price,finalizeCustom:m.dynamicForm,currencyCode:m.currencyCode,caratValue:parseFloat(m.carat.toString().concat(".",m.cents.toString())),breakage:m.breakage})
})
}else{if(j.hasPriceCalculatorRight===false){angular.forEach(j.tagList,function(m){l.push({tag:m.code,finalizeCustom:m.dynamicForm})
})
}}j.finalizeServiceDataBean.finalizeServiceDataBeans=l;
b.savePlan(j.finalizeServiceDataBean,function(m){j.addPlan()
},function(){var n="Could not save, please try again.";
var m=g.error;
g.addMessage(n,m);
g.unMaskLoading()
})
}};
var e=function(k){if(k){}};
j.editPlan=function(m,p){var s=undefined;
j.improveFrom=undefined;
if(m!==undefined&&m!==null){j.checkDrop=false;
j.flag.editMode=true;
j.deleteIdsFromDb=[];
var u=false;
if(j.enteredPlan!==undefined&&j.enteredPlan!==null&&j.enteredPlan.length>0){for(var n=0;
n<j.enteredPlan.length;
n++){if(j.enteredPlan[n].sequentialPlanId===m.planID$AG$String){u=true;
s=angular.copy(j.enteredPlan);
if(p){j.improveFrom=j.enteredPlan[n].planObjectId
}}}}if(!u&&j.submittedPlan!==undefined&&j.submittedPlan!==null&&j.submittedPlan.length>0){for(var n=0;
n<j.submittedPlan.length;
n++){if(j.submittedPlan[n].sequentialPlanId===m.planID$AG$String){u=true;
s=angular.copy(j.submittedPlan);
if(p){j.improveFrom=j.submittedPlan[n].planObjectId
}}}}}j.currentObject=undefined;
if(m!==null&&m!==undefined&&s!==null&&s!==undefined&&s.length>0){var l=false;
for(var n=0;
n<s.length;
n++){if(s[n].sequentialPlanId===m.planID$AG$String){j.currentObject=angular.copy(s[n]);
m=j.currentObject;
if(j.currentObject.referencePlan===null){l=true;
j.detailsFilled=false;
j.selectedNoOfTag=[];
j.checkDrop=true;
j.tag=1;
j.currencyCode=m.currencyCode;
j.selectedRadio=angular.copy({color:m.colorId,cut:m.cutId,clarity:m.clarityId,fluorescence:m.flurosceneId});
j.categoryCustom=m.finalizeCustom;
j.previousTab="A";
j.selectedNoOfTag.push(1);
j.tabManager={};
j.tagList=[];
var r=angular.copy(m.cent);
var k=angular.copy(m.carat);
j.tagList.push({code:String.fromCharCode(65),colorRadioList:j.colorRadioList,carat:k,cents:r,clarity:j.clarityRadioList,cut:j.cutRadioList,fluorescence:j.fluorescenceRadioList,colorGroup:1000,clarityGroup:1001,cutGroup:1002,fluorescenceGroup:1003,selectColor:m.colorId,selectClarity:m.clarityId,selectCut:m.cutId,selectFluorescence:m.flurosceneId,price:m.price,currencyCode:j.currencyCode,breakage:m.breakage});
j.tabManager.tagList=j.tagList;
j.detailsFilled=true
}break
}}if(j.currentObject!==undefined){j.multipleCurrentObjects=[];
for(var n=0;
n<s.length;
n++){if(!l&&j.currentObject.referencePlan===s[n].referencePlan){j.multipleCurrentObjects.push(s[n])
}else{if(l&&j.currentObject.planObjectId===s[n].referencePlan){j.multipleCurrentObjects.push(s[n])
}}if(!l&&j.currentObject.referencePlan===s[n].planObjectId){m=angular.copy(s[n])
}}if(j.multipleCurrentObjects.length>0){j.detailsFilled=false;
j.tag=j.multipleCurrentObjects.length+1;
for(var n=0;
n<j.multipleCurrentObjects.length;
n++){j.selectedNoOfTag.push(n+2)
}j.currencyCode=m.currencyCode;
j.selectedRadio=angular.copy({color:m.colorId,cut:m.cutId,clarity:m.clarityId,fluorescence:m.flurosceneId});
j.previousTab="A";
j.tabManager={};
j.tagList=[];
var o=m.cent;
var q=m.carat;
j.checkDrop=true;
j.tagList.push({code:String.fromCharCode(65),colorRadioList:j.colorRadioList,carat:q,cents:o,clarity:j.clarityRadioList,cut:j.cutRadioList,fluorescence:j.fluorescenceRadioList,colorGroup:27,clarityGroup:28,cutGroup:29,fluorescenceGroup:30,selectColor:m.colorId,selectClarity:m.clarityId,selectCut:m.cutId,selectFluorescence:m.flurosceneId,price:m.price,currencyCode:j.currencyCode,breakage:m.breakage});
j.tabManager.tagList=j.tagList;
for(var t=0;
t<j.multipleCurrentObjects.length;
t++){j.tagList.push({code:j.multipleCurrentObjects[t].tag,colorRadioList:(j.colorRadioList),carat:j.multipleCurrentObjects[t].carat,cents:j.multipleCurrentObjects[t].cent,clarity:angular.copy(j.clarityRadioList),cut:j.cutRadioList,fluorescence:j.fluorescenceRadioList,colorGroup:t,clarityGroup:t,cutGroup:t,fluorescenceGroup:t,selectColor:j.multipleCurrentObjects[t].colorId,selectClarity:j.multipleCurrentObjects[t].clarityId,selectCut:j.multipleCurrentObjects[t].cutId,selectFluorescence:j.multipleCurrentObjects[t].flurosceneId,price:j.multipleCurrentObjects[t].price,currencyCode:j.multipleCurrentObjects[t].currencyCode,breakage:j.multipleCurrentObjects[t].breakage})
}j.detailsFilled=true
}}}};
j.deletePlan=function(){var l=angular.copy(j.objectToDelete);
var n=undefined;
var k=false;
var o=undefined;
if(l!==undefined&&l!==null&&j.enteredPlan!==undefined&&j.enteredPlan!==null&&j.enteredPlan.length>0){for(var m=0;
m<j.enteredPlan.length;
m++){if(j.enteredPlan[m].sequentialPlanId===l.planID$AG$String){o=j.enteredPlan[m];
if(j.enteredPlan[m].referencePlan===null){n=j.enteredPlan[m].planObjectId;
k=true;
break
}}}if(!k){for(var m=0;
m<j.enteredPlan.length;
m++){if(j.enteredPlan[m].sequentialPlanId===l.planID$AG$String){n=angular.copy(j.enteredPlan[m].planObjectId);
k=true;
break
}}}if(k){j.objectToDelete={};
$("#deleteDialog").modal("hide");
g.removeModalOpenCssAfterModalHide();
g.maskLoading();
b.deletePlan(n,function(p){g.unMaskLoading();
j.addPlan()
},function(){g.unMaskLoading();
var q="Could not save, please try again.";
var p=g.error;
g.addMessage(q,p);
g.unMaskLoading()
})
}}};
j.resetPage=function(k){if(k){j.flag.showAddPage=false;
j.retrieveFromWorkAllocation();
j.issuedStock={};
j.issuedStock.data=[]
}};
j.submitPlan=function(l){var m={};
for(var k=0;
k<j.enteredPlan.length;
k++){if(j.selectedLotOrPacket[0].planID$AG$String===j.enteredPlan[k].sequentialPlanId){m.plan=j.enteredPlan[k].planObjectId;
break
}}if(m.plan===null||m.plan===undefined){for(var k=0;
k<j.submiitedPlanDataForUIGrid.length;
k++){if(j.selectedLotOrPacket[0].planID$AG$String===j.submiitedPlanDataForUIGrid[k]["~@sequentialPlanId"]){m.plan=j.submiitedPlanDataForUIGrid[k]["~@id"];
break
}}}m.workallocation=j.workAllocationId;
b.submitPlan(m,function(n){j.resetPage(true)
},function(){var o="Could not save, please try again.";
var n=g.error;
g.addMessage(o,n);
g.unMaskLoading()
})
};
j.saveEditedPlan=function(k){j.submitted=true;
if(k.$valid){j.submitted=false;
for(prop in j.tabAndDynamicFormModel){if(prop===j.previousTab){j.tabAndDynamicFormModel[prop]=angular.copy(j.categoryCustom);
j.finalizeAddFlag=false;
j.reset("categoryCustom");
break
}}angular.forEach(j.tabAndDynamicFormModel,function(p,o){for(var n=0;
n<j.tagList.length;
n++){if(j.tagList[n].code===o){j.tagList[n].dynamicForm=p
}}});
j.finalizeServiceDataBean={};
j.finalizeServiceDataBean.planType=j.planType;
j.finalizeServiceDataBean.finalizeDbType=j.finalizeDbType;
if(j.packetIdForConstraint!==undefined){j.finalizeServiceDataBean.id=j.packetIdForConstraint;
j.finalizeServiceDataBean.hasPacket=true
}else{j.finalizeServiceDataBean.id=j.lotIdForConstraint;
j.finalizeServiceDataBean.hasPacket=false
}var m=[];
var l={};
angular.forEach(j.enteredPlan,function(n){if(d.indexOf(n.planObjectId)===-1){l[n.tag]=n.planObjectId
}});
j.mainPlanObjectId=undefined;
angular.forEach(j.tagList,function(o){if(o.code==="A"){j.mainPlanObjectId=l[o.code]
}var n=null;
if(o.carat!==null&&o.carat!==undefined&&o.cents!==null&&o.cents!==undefined){n=parseFloat(o.carat.toString().concat(".",o.cents.toString()))
}m.push({planObjectId:l[o.code],tag:o.code,colorId:o.selectColor,clarityId:o.selectClarity,cutId:o.selectCut,flurosceneId:o.selectFluorescence,price:o.price,finalizeCustom:o.dynamicForm,currencyCode:o.currencyCode,caratValue:n,breakage:o.breakage})
});
j.finalizeServiceDataBean.finalizeServiceDataBeans=m;
j.finalizeServiceDataBean.deletedIds=d;
j.finalizeServiceDataBean.planObjectId=j.mainPlanObjectId;
b.saveEditedPlan(j.finalizeServiceDataBean,function(n){j.addPlan()
},function(){var o="Could not save, please try again.";
var n=g.erro;
g.addMessage(o,n);
g.unMaskLoading()
})
}};
j.openDeletePlanPopup=function(k){$("#deleteDialog").modal("show");
j.objectToDelete=k
};
j.hidePopUp=function(){$("#deleteDialog").modal("hide");
g.removeModalOpenCssAfterModalHide()
};
j.retrievePreviousValue=function(){if(!!j.flag&&!!j.flag.pricelist){var k={};
angular.forEach(j.gridOptions.data,function(n){var l={};
for(var m=0;
m<j.enteredPlan.length;
m++){if(n.planID$AG$String===j.enteredPlan[m].sequentialPlanId){l.cut=j.enteredPlan[m].cutId;
l.color=j.enteredPlan[m].colorId;
l.clarity=j.enteredPlan[m].clarityId;
l.fluorescence=j.enteredPlan[m].flurosceneId;
l.carat=j.enteredPlan[m].caratId;
k[j.enteredPlan[m].sequentialPlanId]=l;
break
}}});
k.pricelist={id:j.flag.pricelist};
b.retrievevaluefrompricelist(k,function(m){angular.forEach(j.gridOptions.data,function(n){n.previousPrice=m[n.planID$AG$String]
});
var l={name:"previousPrice",displayName:"Previous Price",cellTemplate:'<div><span class="col-md-3">{{row.entity.previousPrice}}</span><div class="col-md-9" ng-show="row.entity.previousPrice!==\'N/A\'"><span ng-if="row.entity.previousPrice<row.entity.price" class="glyphicon glyphicon-arrow-down" style="color: red"></span><span ng-if="row.entity.previousPrice>row.entity.price" class="glyphicon glyphicon-arrow-up" style="color: green"></span>{{((row.entity.previousPrice-row.entity.price)/row.entity.price)*100 | number:2}}%</div></div>'};
j.gridOptions.columnDefs=j.gridOptions.columnDefs.filter(function(n){return n.name!=="previousPrice"
});
j.gridOptions.columnDefs.splice(j.gridOptions.columnDefs.length-1,0,l)
})
}else{j.gridOptions.columnDefs=j.gridOptions.columnDefs.filter(function(l){return l.name!=="previousPrice"
})
}};
j.onCancel=function(k){j.finalizeForm.$setPristine();
j.finalizeForm.$valid=true;
j.detailsFilled=false;
j.tabAndDynamicFormModel={};
j.selectedNoOfTag=[];
j.tag=1;
j.checkDrop=true;
j.tabAndDynamicFormModel.A=null;
j.previousTab="A";
j.selectedNoOfTag.push(1);
j.tabManager={};
j.tagList=[];
j.numberOfTagChanged(1);
j.tabManager.tagList=j.tagList;
j.detailsFilled=true
}
}])
});