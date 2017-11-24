define(["hkg","writeService","customFieldService","parcelService","lotmanagement/edit-lot.controller","ngload!uiGrid","addMasterValue","dynamicForm","accordionCollapse","finalizeService","printBarcodeValue"],function(b,a){b.register.controller("WriteServiceController",["$rootScope","$scope","WriteService","$timeout","$filter","$location","$window","CustomFieldService","DynamicFormService","ParcelService","FinalizeService","$route",function(o,r,m,e,l,g,d,q,p,k,c,n){o.mainMenu="stockLink";
o.childMenu="writeService";
o.activateMenu();
var f=p.retrieveSearchWiseCustomFieldInfo("writeService");
r.flag={};
r.flag.editMode=false;
var i=[];
r.planFlag={};
r.writeDbType={};
r.labelListForUiGrid=[];
r.nodeDetailsInfo=[];
r.submitted=false;
r.modifyName="";
f.then(function(v){r.generalSearchTemplate=v.genralSection;
if(v.genralSection===undefined||v.genralSection===null){r.generalSearchTemplate=[]
}var w={};
var t=[];
if(r.generalSearchTemplate!=null&&r.generalSearchTemplate.length>0){var s={};
angular.forEach(r.generalSearchTemplate,function(x){if(x.model){s[x.model]=x.featureName;
r.labelListForUiGrid.push({name:x.model,displayName:x.label,minWidth:200})
}else{if(x.fromModel){s[x.fromModel]=x.featureName;
r.labelListForUiGrid.push({name:x.fromModel,displayName:x.label,minWidth:200})
}else{if(x.toModel){s[x.toModel]=x.featureName;
r.labelListForUiGrid.push({name:x.toModel,displayName:x.label,minWidth:200})
}}}});
var u={};
if(s!=null){angular.forEach(s,function(y,x){if(!u[y]){u[y]=[]
}u[y].push(x)
})
}}r.retrieveFromWorkAllocation=function(){r.flag.showAddPage=false;
if(u!=null){r.plantype=undefined;
m.searchLotFromWorkAllocation(u,function(z){r.nodeDetailsInfo=[];
if(z!=null&&z.custom2&&z.custom2.length>0){r.modifierMap={};
if(z.serviceInitDataBean!==null&&z.serviceInitDataBean!==undefined){r.modifierMap.nodeId=z.serviceInitDataBean.dynamicServiceInitDataBeans[0].nodeId;
r.modiferList=z.serviceInitDataBean.dynamicServiceInitDataBeans[0].modifier.split("|");
r.dynamicServiceDtaBeans=z.serviceInitDataBean.dynamicServiceInitDataBeans;
r.modifierMap.plan=r.modiferList[0];
r.modifyName="Write ";
if(r.modifierMap.plan==="PL"){r.modifyName+="Plan"
}else{if(r.modifierMap.plan==="ES"){r.modifyName+="Estimation"
}else{if(r.modifierMap.plan==="PA"){r.modifyName+="Parameter"
}}}r.plantype=r.modiferList[0];
if(r.modiferList[1]!==undefined&&r.modiferList[1]!==null){r.nonMandatoryModifierList=r.modiferList[1].split(",")
}var x=z.serviceInitDataBean;
if(!!x){if(x.nodeAndWorkAllocationIds!==null){r.nodeIdAndWorkAllocationIdsMap=angular.copy(x.nodeAndWorkAllocationIds)
}if(x.mandatoryFields!==null){r.stockStaticFields=x.mandatoryFields
}if(x.diamondsInQueue!==null){r.diamondsInQueue=x.diamondsInQueue
}if(!!x.dynamicServiceInitDataBeans){var A=x.dynamicServiceInitDataBeans;
if(angular.isArray(A)&&A.length>0){angular.forEach(A,function(C){var D={};
D.groupId=C.groupId;
D.groupName=C.groupName;
D.modifier=C.modifier;
D.nodeId=C.nodeId;
D.nodeName=C.nodeName;
r.nodeDetailsInfo.push(D)
})
}if(r.nodeDetailsInfo.length>1){r.flag.multipleIdInvolved=true
}else{r.flag.multipleIdInvolved=false
}}if(r.nodeDetailsInfo.length>0){r.currentActivityNode=r.nodeDetailsInfo[0].nodeId
}}}z=angular.copy(z.custom2);
r.searchedDataFromDb=angular.copy(z);
r.issuedStock={};
r.issuedStock.enableFiltering=true;
r.issuedStock.multiSelect=false;
r.issuedStock.enableRowSelection=true;
r.selectedStock=[];
r.issuedStock.onRegisterApi=function(C){r.gridApi=C;
C.selection.on.rowSelectionChanged(r,function(D){if(r.selectedStock.length>0){$.each(r.selectedStock,function(F,E){if(E["$$hashKey"]===D.entity["$$hashKey"]){r.flag.repeatedRow=true;
r.selectedStock.splice(F,1);
return false
}else{r.flag.repeatedRow=false
}});
if(!r.flag.repeatedRow){r.selectedStock.push(D.entity)
}}else{r.selectedStock.push(D.entity)
}if(r.selectedStock.length>0){r.flag.rowSelectedflag=true
}else{r.flag.rowSelectedflag=false
}});
C.selection.on.rowSelectionChangedBatch(r,function(D){if(r.selectedStock.length>0){angular.forEach(D,function(E){$.each(r.selectedStock,function(G,F){if(F["$$hashKey"]===E.entity["$$hashKey"]){r.flag.repeatedRow=true;
r.selectedStock=[];
return false
}else{r.flag.repeatedRow=false
}});
if(!r.flag.repeatedRow){r.selectedStock.push(E.entity)
}})
}else{angular.forEach(D,function(E){r.selectedStock.push(E.entity)
})
}if(r.selectedStock.length>0){r.flag.rowSelectedflag=true
}else{r.flag.rowSelectedflag=false
}})
};
r.issuedStockList=[];
for(var y=0;
y<z.length;
y++){if(z[y].custom1!=null){z[y].categoryCustom=(z[y].custom1);
z[y].categoryCustom["$$$value"]=(z[y].value)
}}var B=function(C){r.searchedData=angular.copy(z);
angular.forEach(r.searchedData,function(D){angular.forEach(r.labelListForUiGrid,function(E){if(D.custom1!=null&&!D.custom1.hasOwnProperty(E.name)){D.custom1[E.name]="NA"
}else{if(D.custom1!=null&&D.custom1.hasOwnProperty(E.name)){if(!(!!(D.custom1[E.name]))){D.custom1[E.name]="NA"
}}}if(D.hasOwnProperty("status")){D.custom1["~@status"]=D.status
}});
if(D.custom1!=null){r.issuedStockList.push(D.custom1)
}});
r.updateStockAccordingToNode(r.currentActivityNode)
};
p.convertorForCustomField(z,B);
window.setTimeout(function(){$(window).resize();
$(window).resize()
},200);
r.listFilled=true;
r.issueListFilled=true;
r.dataRetrieved=true
}else{r.dataRetrieved=true;
r.listFilled=true
}},function(){var y="Could not retrieve, please try again.";
var x=o.error;
o.addMessage(y,x);
o.unMaskLoading()
})
}else{r.dataRetrieved=true
}};
r.retrieveFromWorkAllocation()
},function(s){},function(s){});
r.updateStockAccordingToNode=function(w){console.log("updateStockAccordingToNode called");
if(w!==null&&w!==undefined){r.currentActivityNode=w;
angular.forEach(r.nodeDetailsInfo,function(A){if(A.nodeId===w){var x=A.modifier;
if(x!==null){if(x.indexOf("|")>-1){var y=x.split("|");
for(var z=0;
z<y.length;
z++){if(y[z]==="Q"){r.isQuingRequired=true
}}}else{if(x==="AA"||x==="MA"){r.typeOfAllocation=x
}}}}});
if(angular.isDefined(r.nodeIdAndWorkAllocationIdsMap)){var t=[];
var u=r.nodeIdAndWorkAllocationIdsMap[w];
angular.forEach(r.issuedStockList,function(y,x){if(u.length>0&&u.indexOf(y["~@status"])!==-1){y["~@index"]=x+1;
t.push(y)
}});
r.currentNodeStocks=angular.copy(t);
if(angular.isDefined(r.gridApi)){r.gridApi.selection.clearSelectedRows()
}r.issuedStock.data=t;
r.issuedStock.columnDefs=r.labelListForUiGrid;
r.listFilled=true;
if(!!r.isQuingRequired){r.issuedStock.enableSorting=false;
r.issuedStock.isRowSelectable=function(x){if(!!r.diamondsInQueue){if(x.entity["~@index"]>r.diamondsInQueue){return false
}else{return true
}}else{if(x.entity["~@index"]>0){return true
}else{return false
}}}
}}else{console.log("Node map not initialized")
}if(r.dynamicServiceDtaBeans!==null&&r.dynamicServiceDtaBeans!==undefined&&r.dynamicServiceDtaBeans.length>0){for(var v=0;
v<r.dynamicServiceDtaBeans.length;
v++){if(r.dynamicServiceDtaBeans[v].nodeId===r.currentActivityNode){console.log("updateStockAccordingToNode called"+r.dynamicServiceDtaBeans[v].nodeId);
r.modifierMap.nodeId=r.dynamicServiceDtaBeans[v].nodeId;
r.modiferList=r.dynamicServiceDtaBeans[v].modifier.split("|");
console.log(r.dynamicServiceDtaBeans[v].modifier.split("|"));
var s=r.dynamicServiceDtaBeans[v];
r.modifierMap.plan=r.modiferList[0];
r.modifyName="Write ";
if(r.modifierMap.plan==="PL"){r.modifyName+="Plan"
}else{if(r.modifierMap.plan==="ES"){r.modifyName+="Estimation"
}else{if(r.modifierMap.plan==="PA"){r.modifyName+="Parameter"
}}}r.plantype=r.modiferList[0];
if(r.modiferList[1]!==undefined&&r.modiferList[1]!==null){r.nonMandatoryModifierList=r.modiferList[1].split(",")
}else{r.nonMandatoryModifierList=[]
}if(!!s){if(s.nodeAndWorkAllocationIds!==null){r.nodeIdAndWorkAllocationIdsMap=angular.copy(s.nodeAndWorkAllocationIds)
}if(s.mandatoryFields!==null){r.stockStaticFields=s.mandatoryFields
}if(s.diamondsInQueue!==null){r.diamondsInQueue=s.diamondsInQueue
}}}}}}};
r.initWriteForm=function(s){r.writeForm=s
};
r.numberOfTagChanged=function(C){r.submitted=true;
C=parseInt(C);
if(C!==null&&((C>r.tagList.length&&r.writeForm.$valid)||C<=r.tagList.length||r.count===0)){r.submitted=false;
var z=0;
var y=1;
var u=["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"];
var x=[""];
while(y<=C){var A=x[z];
for(var s in u){var t=A+u[s];
x.push(t);
y++;
if(C===1&&r.count===0){r.count++;
r.tagList.push({code:t,colorRadioList:(r.colorRadioList),carat:0,cents:0,clarity:angular.copy(r.clarityRadioList),cut:r.cutRadioList,fluorescence:r.fluorescenceRadioList,colorGroup:1000,clarityGroup:1001,cutGroup:1002,fluorescenceGroup:1003,selectColor:r.colorRadioList[0].color,selectClarity:r.clarityRadioList[0].clarity,selectCut:r.cutRadioList[0].cut,selectFluorescence:r.fluorescenceRadioList[0].fluorescence,price:null,currencyCode:r.currencyCode,breakage:false})
}if(y===C+1){break
}}z++
}x.splice(0,1);
if(x.length>r.tagList.length){for(var w=r.tagList.length;
w<=x.length;
w++){if(x[w]!==undefined){r.tagList.push({code:x[w],colorRadioList:(r.colorRadioList),carat:0,cents:0,clarity:angular.copy(r.clarityRadioList),cut:r.cutRadioList,fluorescence:r.fluorescenceRadioList,colorGroup:w,clarityGroup:w,cutGroup:w,fluorescenceGroup:w,selectColor:r.colorRadioList[0].color,selectClarity:r.clarityRadioList[0].clarity,selectCut:r.cutRadioList[0].cut,selectFluorescence:r.fluorescenceRadioList[0].fluorescence,price:null,currencyCode:r.currencyCode,breakage:false});
r.tabAndDynamicFormModel[x[w]]=null
}}}else{if(x.length<r.tagList.length){r.tagToRemove=[];
for(var v=0;
v<=r.tagList.length;
v++){if(r.tagList[v]!==undefined&&x.indexOf(r.tagList[v].code)===-1){r.tagToRemove.push(r.tagList[v].code)
}}if(r.tagToRemove.length>0){for(var v=0;
v<r.tagList.length;
v++){for(var B=0;
B<r.tagToRemove.length;
B++){if(r.tagList[v]!==undefined&&(r.tagToRemove[B])===r.tagList[v].code){delete r.tabAndDynamicFormModel[r.tagList[v].code];
r.tagList.splice(v,1)
}}}}}}}else{r.tag=r.tagList.length
}};
r.addPlan=function(){r.fieldNotConfigured=false;
r.flag.editMode=false;
r.numberOfMainTag=[];
if(r.selectedStock!==undefined&&r.selectedStock!==null&&r.selectedStock.length>0){r.writeAddFlag=false;
r.plansToBeSubmitted=r.selectedStock[0].plansToBeSubmitted;
console.log(r.selectedStock[0]);
r.hasPriceCalculatorRight=false;
o.maskLoading();
i=[];
r.workAllocationId=undefined;
r.count=0;
r.gridOptions={};
r.selectedLotOrPacket=[];
r.gridOptions.onRegisterApi=function(v){r.gridApi1=v;
v.selection.on.rowSelectionChanged(r,function(w){if(r.selectedLotOrPacket.length>0){$.each(r.selectedLotOrPacket,function(y,x){if(x["$$hashKey"]===w.entity["$$hashKey"]){r.flag.repeatedRow=true;
r.selectedLotOrPacket.splice(y,1);
return false
}else{r.flag.repeatedRow=false
}});
if(!r.flag.repeatedRow){r.selectedLotOrPacket.push(w.entity)
}}else{r.selectedLotOrPacket.push(w.entity)
}if(r.selectedLotOrPacket.length>0){r.flag.rowSelectedflag=true
}else{r.flag.rowSelectedflag=false
}});
v.selection.on.rowSelectionChangedBatch(r,function(w){if(r.selectedLotOrPacket.length>0){angular.forEach(w,function(x){$.each(r.selectedLotOrPacket,function(z,y){if(y["$$hashKey"]===x.entity["$$hashKey"]){r.flag.repeatedRow=true;
r.selectedLotOrPacket=[];
return false
}else{r.flag.repeatedRow=false
}});
if(!r.flag.repeatedRow){r.selectedLotOrPacket.push(x.entity)
}})
}else{angular.forEach(w,function(x){r.selectedLotOrPacket.push(x.entity)
})
}if(r.selectedLotOrPacket.length>0){r.flag.rowSelectedflag=true
}else{r.flag.rowSelectedflag=false
}})
};
r.labelForUIGrid=[];
r.lotIdForConstraint=undefined;
r.packetIdForConstraint=undefined;
for(var t=0;
t<r.searchedDataFromDb.length;
t++){if(r.searchedDataFromDb[t].value===r.selectedStock[0].$$$value){r.invoiceIdForConstraint=r.searchedDataFromDb[t].description;
r.parcelIdForConstraint=r.searchedDataFromDb[t].label;
r.lotIdForConstraint=angular.copy(r.searchedDataFromDb[t].value);
r.modifierMap.objectId=angular.copy(r.searchedDataFromDb[t].value);
r.modifierMap.isPacket="false";
if(r.searchedDataFromDb[t].id!=null){r.packetIdForConstraint=angular.copy(r.searchedDataFromDb[t].id);
r.modifierMap.objectId=angular.copy(r.searchedDataFromDb[t].value);
r.modifierMap.isPacket="true"
}r.workAllocationId=angular.copy(r.searchedDataFromDb[t].status);
break
}}c.retrievePriceList(function(v){r.pricelistDtl=JSON.parse(angular.toJson(v));
for(var x in r.pricelistDtl){if(r.pricelistDtl.hasOwnProperty(x)){var w=r.pricelistDtl[x];
r.pricelistDtl[x]=new Date(w).toUTCString().replace(/\s*(GMT|UTC)$/,"")
}}});
m.retrieveCurrency(function(v){r.currencyCodeList=[];
if(v!==undefined&&v!==null){angular.forEach(v,function(w){r.currencyCodeList.push({currencyCode:w.value,currency:w.label})
})
}},function(){var w="Could not retrieve currency, please try again.";
var v=o.error;
o.addMessage(w,v);
o.unMaskLoading()
});
m.retrieveValuesOfMaster(function(w){r.flag.masterValueNotExist=false;
r.colorRadioList=[];
r.clarityRadioList=[];
r.cutRadioList=[];
r.fluorescenceRadioList=[];
if(w!=null&&w.length>0){var x=angular.copy(w);
angular.forEach(x,function(y){if(y.masterName==="Color"){r.colorRadioList.push({color:angular.copy(y.valueEntityId),name:angular.copy(y.value)})
}else{if(y.masterName==="Cut"){r.cutRadioList.push({cut:angular.copy(y.valueEntityId),name:angular.copy(y.value)})
}else{if(y.masterName==="Clarity"){r.clarityRadioList.push({clarity:angular.copy(y.valueEntityId),name:angular.copy(y.value)})
}else{if(y.masterName==="Fluroscene"){r.fluorescenceRadioList.push({fluorescence:angular.copy(y.valueEntityId),name:angular.copy(y.value)})
}}}}});
if(r.colorRadioList.length===0||r.cutRadioList.length===0||r.clarityRadioList.length===0||r.fluorescenceRadioList.length===0){r.flag.masterEmpty=true
}else{r.flag.masterEmpty=false;
r.selectedRadio=angular.copy({color:r.colorRadioList[0].color,cut:r.cutRadioList[0].cut,clarity:r.clarityRadioList[0].clarity,fluorescence:r.fluorescenceRadioList[0].fluorescence});
r.numberOfTags=[];
for(var v=1;
v<1000;
v++){r.numberOfTags.push(angular.copy({title:v}))
}r.tabAndDynamicFormModel={};
r.selectedNoOfTag=[];
r.checkDrop=true;
r.tag=1;
r.currencyCode="USD($)";
r.tabAndDynamicFormModel.A=null;
r.previousTab="A";
r.selectedNoOfTag.push(1);
r.tabManager={};
r.tagList=[];
r.numberOfTagChanged(1);
r.tabManager.tagList=r.tagList
}r.detailsFilled=true
}else{r.flag.masterValueNotExist=true
}r.gridOptionForSubmit={};
r.submittedPlan=[];
r.planSubmit=false;
console.log("$scope.nonMandatoryModifierList"+JSON.stringify(r.nonMandatoryModifierList));
if(!!r.nonMandatoryModifierList&&r.nonMandatoryModifierList.length>0&&r.nonMandatoryModifierList.indexOf("SP")!==-1){r.modifierMap.workallotmentId=r.workAllocationId;
m.retrieveAccessiblePlan(r.modifierMap,function(z){angular.forEach(z,function(D){D.carat=D.caratValue;
D.cent=0;
if(D.caratValue!==null&&D.caratValue.toString().indexOf(".")!==-1){var C=D.caratValue.toString().split(".");
D.carat=parseInt(C[0]);
D.cent=parseInt(C[1])
}});
r.submittedPlan=angular.copy(z);
angular.forEach(z,function(C){C.categoryCustom={};
C.categoryCustom=angular.copy(C.writeCustom)
});
r.submiitedPlanColum=[];
r.submiitedPlanDataForUIGrid=[];
r.submiitedPlanColum.push({name:"carate_range_of_plan",displayName:"Carat",minWidth:200});
r.submiitedPlanColum.push({name:"tag",displayName:"Tag",minWidth:200});
r.submiitedPlanColum.push({name:"cut$DRP$Long",displayName:"Cut",minWidth:200});
r.submiitedPlanColum.push({name:"color$DRP$Long",displayName:"Color",minWidth:200});
r.submiitedPlanColum.push({name:"clarity$DRP$Long",displayName:"Clarity",minWidth:200});
if(r.hasPriceCalculatorRight){r.submiitedPlanColum.push({name:"price",displayName:"Value",minWidth:200})
}else{r.submiitedPlanColum.push({name:"value_of_plan$CRN$Double",displayName:"Value Of Plan",minWidth:200})
}r.submiitedPlanColum.push({name:"createdByName",displayName:"Plan By",minWidth:200});
r.submiitedPlanColum.push({name:"Action",cellTemplate:'<div class="ui-grid-cell-contents"> <a ng-click="grid.appScope.editPlan(row.entity,true)">Improve Plan</i></a></div>',enableFiltering:false,minWidth:200});
var y=p.retrieveSectionWiseCustomFieldInfo("plan");
var A=undefined;
y.then(function(C){A=C.genralSection
},function(C){},function(C){});
var B=function(C){r.submittedPlanData=angular.copy(C);
angular.forEach(r.submittedPlanData,function(D){angular.forEach(r.submiitedPlanColum,function(E){if(!D.categoryCustom.hasOwnProperty(E.name)){D.categoryCustom[E.name]="NA"
}else{if(D.categoryCustom.hasOwnProperty(E.name)){if(D.categoryCustom[E.name]===null||D.categoryCustom[E.name]===""||D.categoryCustom[E.name]===undefined){D.categoryCustom[E.name]="NA"
}}}});
r.submiitedPlanDataForUIGrid.push(D.categoryCustom)
});
r.gridOptionForSubmit.data=r.submiitedPlanDataForUIGrid;
r.gridOptionForSubmit.columnDefs=r.submiitedPlanColum;
r.gridOptionForSubmit.enableFiltering=true;
r.planSubmit=true;
window.setTimeout(function(){$(window).resize();
$(window).resize()
},100)
};
p.convertorForCustomField(z,B)
},function(){var z="Could not retrieve, please try again.";
var y=o.error;
o.addMessage(z,y);
o.unMaskLoading()
})
}r.searchedDataFromDbForUiGrid=[];
m.retrieveSavedPlan(r.workAllocationId,function(z){angular.forEach(z,function(D){D.carat=D.caratValue;
D.cent=0;
if(D.caratValue!==null&&D.caratValue.toString().indexOf(".")!==-1){var C=D.caratValue.toString().split(".");
D.carat=parseInt(C[0]);
D.cent=parseInt(C[1])
}});
r.enteredPlan=angular.copy(z);
angular.forEach(z,function(C){C.categoryCustom={};
C.categoryCustom=angular.copy(C.writeCustom)
});
r.planListForUiGrid=[];
r.planListForUiGrid.push({name:"planID$AG$String",displayName:"Plan ID",minWidth:200});
r.planListForUiGrid.push({name:"tag",displayName:"Tag",minWidth:200});
r.planListForUiGrid.push({name:"cut$DRP$Long",displayName:"Cut",minWidth:200});
r.planListForUiGrid.push({name:"color$DRP$Long",displayName:"Color",minWidth:200});
r.planListForUiGrid.push({name:"clarity$DRP$Long",displayName:"Clarity",minWidth:200});
if(r.hasPriceCalculatorRight){r.planListForUiGrid.push({name:"price",displayName:"Value",minWidth:200})
}else{r.planListForUiGrid.push({name:"value_of_plan$CRN$Double",displayName:"Value Of Plan",minWidth:200})
}r.planListForUiGrid.push({name:"Action",cellTemplate:'<div class="ui-grid-cell-contents"> <a ng-click="grid.appScope.editPlan(row.entity,editPlan)">Edit</i></a>&nbsp;<a ng-click="grid.appScope.openDeletePlanPopup(row.entity)">Delete</i></a></div>',enableFiltering:false,minWidth:200});
var y=p.retrieveSectionWiseCustomFieldInfo("plan");
var A=undefined;
y.then(function(C){A=C.genralSection
},function(C){},function(C){});
var B=function(C){r.searchedData=angular.copy(C);
angular.forEach(r.searchedData,function(D){angular.forEach(r.planListForUiGrid,function(E){if(!D.categoryCustom.hasOwnProperty(E.name)){D.categoryCustom[E.name]="NA"
}else{if(D.categoryCustom.hasOwnProperty(E.name)){if(D.categoryCustom[E.name]===null||D.categoryCustom[E.name]===""||D.categoryCustom[E.name]===undefined){D.categoryCustom[E.name]="NA"
}}}});
r.searchedDataFromDbForUiGrid.push(D.categoryCustom)
});
r.gridOptions.data=r.searchedDataFromDbForUiGrid;
r.numberOfMainTag=[];
if(C!==null&&C!==undefined&&C.length>0){angular.forEach(C,function(D){if(D.tag==="A"){r.numberOfMainTag.push(D.tag)
}})
}r.gridOptions.columnDefs=r.planListForUiGrid;
r.gridOptions.enableFiltering=true;
r.gridOptions.multiSelect=false;
r.gridOptions.enableRowSelection=true;
r.gridOptions.enableSelectAll=false;
window.setTimeout(function(){$(window).resize();
$(window).resize()
},100)
};
p.convertorForCustomField(z,B)
},function(){var z="Could not retrieve, please try again.";
var y=o.error;
o.addMessage(z,y);
o.unMaskLoading()
})
},function(){var w="Could not retrieve, please try again.";
var v=o.error;
o.addMessage(w,v);
o.unMaskLoading()
});
r.flag.showAddPage=true;
var u={};
var s=[];
r.packetDataBean={};
r.packetListToSave=[];
r.parcelDataBean={invoiceDataBean:{invoiceCustom:"",invoiceDbType:""}};
q.retrieveDesignationBasedFields("writeService",function(w){r.response=angular.copy(w);
var A=p.retrieveSectionWiseCustomFieldInfo("invoice");
r.invoiceDbType={};
A.then(function(B){r.generaInvoiceTemplate=B.genralSection;
r.generaInvoiceTemplate=p.retrieveCustomData(r.generaInvoiceTemplate,w);
if(r.generaInvoiceTemplate!=null&&r.generaInvoiceTemplate.length>0){angular.forEach(r.generaInvoiceTemplate,function(C){if(C.model){s.push(C.model)
}else{if(C.fromModel){s.push(C.fromModel)
}else{if(C.toModel){s.push(C.toModel)
}}}});
if(s!=null&&s.length>0){u.invoice=s
}}},function(B){},function(B){});
var x=p.retrieveSectionWiseCustomFieldInfo("parcel");
r.parcelDbType={};
x.then(function(C){r.generaParcelTemplate=C.genralSection;
r.generaParcelTemplate=p.retrieveCustomData(r.generaParcelTemplate,w);
var B=[];
if(r.generaParcelTemplate!=null&&r.generaParcelTemplate.length>0){angular.forEach(r.generaParcelTemplate,function(D){if(D.model){B.push(D.model)
}else{if(D.fromModel){B.push(D.fromModel)
}else{if(D.toModel){B.push(D.toModel)
}}}});
if(B!=null&&B.length>0){u.parcel=B
}}},function(B){},function(B){});
var z=p.retrieveSectionWiseCustomFieldInfo("lot");
r.lotDbType={};
z.then(function(C){r.generalLotTemplate=C.genralSection;
r.generalLotTemplate=p.retrieveCustomData(r.generalLotTemplate,w);
var B=[];
if(r.generalLotTemplate!=null&&r.generalLotTemplate.length>0){angular.forEach(r.generalLotTemplate,function(D){if(D.model){B.push(D.model)
}else{if(D.fromModel){B.push(D.fromModel)
}else{if(D.toModel){B.push(D.toModel)
}}}});
if(B!=null&&B.length>0){u.lot=B
}}},function(B){},function(B){});
var y=p.retrieveSectionWiseCustomFieldInfo("packet");
r.packetDbType={};
y.then(function(D){r.generalPacketTemplate=D.genralSection;
r.generalPacketTemplate=p.retrieveCustomData(r.generalPacketTemplate,w);
var C=[];
if(r.generalPacketTemplate!=null&&r.generalPacketTemplate.length>0){if(r.nonMandatoryModifierList!==undefined&&r.nonMandatoryModifierList!==null&&r.nonMandatoryModifierList.indexOf("SHPC")){for(var B=0;
B<r.generalPacketTemplate.length;
B++){}}angular.forEach(r.generalPacketTemplate,function(E){if(E.model){C.push(E.model)
}else{if(E.fromModel){C.push(E.fromModel)
}else{if(E.toModel){C.push(E.toModel)
}}}});
if(C!=null&&C.length>0){u.packet=C
}}var C=[];
if(r.packetIdForConstraint!=null){C.push(r.packetIdForConstraint);
u.packetId=C
}else{C.push(r.lotIdForConstraint);
u.lotId=C
}m.retrieveStockById(u,function(E){if(E!=null){r.invoiceCustom=E.custom1;
r.parcelCustom=E.custom3;
r.lotCustom=E.custom4;
r.packetCustom=E.custom5;
if(E.custom1===null||E.custom1===undefined){r.invoiceCustom={}
}if(E.custom3==null||E.custom3===undefined){r.parcelCustom={}
}if(E.custom4==null||E.custom4===undefined){r.lotCustom={}
}if(E.custom5==null||E.custom5===undefined){r.packetCustom={}
}}},function(){var F="Could not retrieve, please try again.";
var E=o.error;
o.addMessage(F,E);
o.unMaskLoading()
})
},function(B){},function(B){});
r.categoryCustom=p.resetSection(r.generalWriteTemplate);
var v=p.retrieveSectionWiseCustomFieldInfo("plan");
if(!(r.writeDbType!=null)){r.writeDbType={}
}v.then(function(G){r.generalWriteTemplate=G.genralSection;
var E=[];
var B=Object.keys(w).map(function(H,I){angular.forEach(this[H],function(J){if(H!=="Plan#P#"){E.push({Plan:J})
}})
},w);
r.generalWriteTemplate=p.retrieveCustomData(r.generalWriteTemplate,E);
var C=[];
angular.forEach(r.generalWriteTemplate,function(H){C.push(H.model)
});
console.log("condition");
console.log(!!r.nonMandatoryModifierList&&r.nonMandatoryModifierList.length>0&&r.nonMandatoryModifierList.toString().indexOf("SHPC")!==-1);
if(!!r.nonMandatoryModifierList&&r.nonMandatoryModifierList.length>0&&r.nonMandatoryModifierList.toString().indexOf("SHPC")!==-1){r.hasPriceCalculatorRight=true;
console.log("$scope.hasPriceCalculatorRight"+r.hasPriceCalculatorRight);
if(r.hasPriceCalculatorRight&&r.generalWriteTemplate!==null&&r.generalWriteTemplate!==undefined&&r.generalWriteTemplate.length>0){for(var D=r.generalWriteTemplate.length-1;
D>=0;
D--){if(r.generalWriteTemplate[D].model==="color$DRP$Long"){r.generalWriteTemplate.splice(D,1)
}else{if(r.generalWriteTemplate[D].model==="cut$DRP$Long"){r.generalWriteTemplate.splice(D,1)
}else{if(r.generalWriteTemplate[D].model==="clarity$DRP$Long"){r.generalWriteTemplate.splice(D,1)
}else{if(r.generalWriteTemplate[D].model==="fluroscene$DRP$Long"){r.generalWriteTemplate.splice(D,1)
}else{if(r.generalWriteTemplate[D].model==="carat$NM$Double"){r.generalWriteTemplate.splice(D,1)
}else{if(r.generalWriteTemplate[D].model==="carate_range_of_plan$DRP$Long"){r.generalWriteTemplate.splice(D,1)
}else{if(r.generalWriteTemplate[D].model==="breakge$CB$Boolean"){r.generalWriteTemplate.splice(D,1)
}else{if(r.generalWriteTemplate[D].model==="value_of_plan$CRN$Double"){r.generalWriteTemplate.splice(D,1)
}}}}}}}}}r.writeAddFlag=true
}}else{if(!r.hasPriceCalculatorRight&&r.generalWriteTemplate.length>0){r.mandatoryFields=["color$DRP$Long","cut$DRP$Long","clarity$DRP$Long","carat_of_plan$NM$Double","fluroscene$DRP$Long","value_of_plan$CRN$Double"];
if(C.length>0&&r.mandatoryFields!=null&&r.mandatoryFields.length>0){for(var F=0;
F<r.mandatoryFields.length;
F++){if(C.indexOf(r.mandatoryFields[F])===-1){r.fieldNotConfigured=true;
break
}}}else{r.fieldNotConfigured=true
}r.writeAddFlag=true
}else{if(r.hasPriceCalculatorRight===false&&r.generalWriteTemplate.length===0){r.generalWriteTemplate=[];
r.writeAddFlag=true;
r.fieldNotConfigured=true
}}}angular.forEach(r.generalWriteTemplate,function(H){if(H.model){r.labelForUIGrid.push({name:H.model,displayName:H.label,minWidth:200})
}else{if(H.fromModel){r.labelForUIGrid.push({name:H.fromModel,displayName:H.label,minWidth:200})
}else{if(H.toModel){r.labelForUIGrid.push({name:H.toModel,displayName:H.label,minWidth:200})
}}}});
r.flag.customFieldGenerated=true;
r.planSelect=true;
o.unMaskLoading()
},function(B){},function(B){})
},function(){o.unMaskLoading();
var w="Failed to retrieve data";
var v=o.error;
o.addMessage(w,v)
})
}};
r.caratChange=function(s,w,t,v){if(!(s!=null)){if(r.tabManager!=null){if(r.tabManager.tagList!=null){for(var u=0;
u<r.tabManager.tagList.length;
u++){if(r.tabManager.tagList[u].code=v){r.tabManager.tagList[u].carat=0;
break
}}}}}};
r.calculatePrice=function(w){r.finalCarat=undefined;
var v={};
if(w!=null&&w.length>0){if(r.tabManager.tagList!=null&&r.tabManager.tagList.length>0){for(var t=0;
t<r.tagList.length;
t++){if(r.tabManager.tagList[t].code===w){var s=angular.copy(r.tabManager.tagList[t].carat);
var u=angular.copy(r.tabManager.tagList[t].cents);
r.finalCarat=parseFloat(s.toString().concat(".",u.toString()));
v.color=angular.copy(r.tabManager.tagList[t].selectColor);
v.cut=angular.copy(r.tabManager.tagList[t].selectCut);
v.clarity=angular.copy(r.tabManager.tagList[t].selectClarity);
v.fluroscene=angular.copy(r.tabManager.tagList[t].selectFluorescence);
v.carat=r.finalCarat;
m.checkCaratRange(v,function(x){if(x!=null&&x.price!=null){r.tabManager.tagList[t].price=x.price
}else{x.price=0;
r.tabManager.tagList[t].price=x.price
}},function(){var y="Could not retrieve, please try again.";
var x=o.error;
o.addMessage(y,x);
o.unMaskLoading()
});
break
}}}}};
r.centChange=function(v,w,s,u){if(!(v!=null)){if(r.tabManager!=null){if(r.tabManager.tagList!=null){for(var t=0;
t<r.tabManager.tagList.length;
t++){if(r.tabManager.tagList[t].code==u){r.tabManager.tagList[t].cents=0;
break
}}}}}};
r.reset=function(t){if(t==="categoryCustom"){r.categoryCustom={};
var s=p.retrieveSectionWiseCustomFieldInfo("plan");
s.then(function(x){var v=[];
var u=Object.keys(r.response).map(function(y,z){angular.forEach(this[y],function(A){if(y!=="Plan#P#"){v.push({Plan:A})
}})
},r.response);
r.generalWriteTemplate=null;
r.generalWriteTemplate=x.genralSection;
r.generalWriteTemplate=p.retrieveCustomData(r.generalWriteTemplate,v);
if(r.hasPriceCalculatorRight&&r.generalWriteTemplate.length>0){for(var w=r.generalWriteTemplate.length-1;
w>=0;
w--){if(r.generalWriteTemplate[w].model==="color$DRP$Long"){r.generalWriteTemplate.splice(w,1)
}else{if(r.generalWriteTemplate[w].model==="cut$DRP$Long"){r.generalWriteTemplate.splice(w,1)
}else{if(r.generalWriteTemplate[w].model==="clarity$DRP$Long"){r.generalWriteTemplate.splice(w,1)
}else{if(r.generalWriteTemplate[w].model==="fluroscene$DRP$Long"){r.generalWriteTemplate.splice(w,1)
}else{if(r.generalWriteTemplate[w].model==="carat$NM$Double"){r.generalWriteTemplate.splice(w,1)
}else{if(r.generalWriteTemplate[w].model==="carate_range_of_plan$DRP$Long"){r.generalWriteTemplate.splice(w,1)
}else{if(r.generalWriteTemplate[w].model==="breakge$CB$Boolean"){r.generalWriteTemplate.splice(w,1)
}else{if(r.generalWriteTemplate[w].model==="value_of_plan$NM$Integer"){r.generalWriteTemplate.splice(w,1)
}}}}}}}}}console.log(r.generalWriteTemplate.length);
r.writeAddFlag=true
}r.writeAddFlag=true;
o.unMaskLoading();
r.flag.customFieldGenerated=true
},function(u){},function(u){})
}};
r.tabChanged=function(s){console.log(r.tabAndDynamicFormModel);
console.log("previousTab"+r.previousTab);
console.log("tab"+s);
for(prop in r.tabAndDynamicFormModel){if(prop===r.previousTab){console.log("tab found");
r.tabAndDynamicFormModel[prop]=angular.copy(r.categoryCustom);
r.writeAddFlag=false;
r.reset("categoryCustom");
r.previousTab=angular.copy(s);
break
}}if(r.tabAndDynamicFormModel[s]){r.categoryCustom=angular.copy(r.tabAndDynamicFormModel[s])
}};
r.savePlan=function(s){r.submitted=true;
if(s.$valid){o.maskLoading();
r.submitted=false;
for(prop in r.tabAndDynamicFormModel){if(prop===r.previousTab){r.tabAndDynamicFormModel[prop]=angular.copy(r.categoryCustom);
break
}}angular.forEach(r.tabAndDynamicFormModel,function(w,v){for(var u=0;
u<r.tagList.length;
u++){if(r.tagList[u].code===v){r.tagList[u].dynamicForm=w
}}});
r.writeServiceDataBean={};
r.writeServiceDataBean.planType=r.plantype;
if(!!r.improveFrom){r.writeServiceDataBean.improveFromId=r.improveFrom
}else{r.writeServiceDataBean.improveFromId=null
}r.writeServiceDataBean.writeDbType=r.writeDbType;
if(r.packetIdForConstraint!==undefined){r.writeServiceDataBean.id=r.packetIdForConstraint;
r.writeServiceDataBean.hasPacket=true
}else{r.writeServiceDataBean.id=r.lotIdForConstraint;
r.writeServiceDataBean.hasPacket=false
}var t=[];
if(r.hasPriceCalculatorRight){angular.forEach(r.tagList,function(u){t.push({tag:u.code,colorId:u.selectColor,clarityId:u.selectClarity,cutId:u.selectCut,flurosceneId:u.selectFluorescence,price:u.price,writeCustom:u.dynamicForm,currencyCode:u.currencyCode,caratValue:parseFloat(u.carat.toString().concat(".",u.cents.toString())),breakage:u.breakage})
})
}else{if(r.hasPriceCalculatorRight===false){angular.forEach(r.tagList,function(u){t.push({tag:u.code,writeCustom:u.dynamicForm})
})
}}r.writeServiceDataBean.writeServiceDataBeans=t;
console.log(JSON.stringify(t)+"length"+t.length);
m.savePlan(r.writeServiceDataBean,function(u){r.checkDrop=false;
r.addPlan()
},function(){var v="Could not save, please try again.";
var u=o.error;
o.addMessage(v,u);
o.unMaskLoading()
})
}};
var j=function(s){if(s){}};
r.editPlan=function(u,x){var A=undefined;
r.improveFrom=undefined;
if(u!==undefined&&u!==null){o.maskLoading();
r.checkDrop=false;
r.flag.editMode=true;
r.deleteIdsFromDb=[];
var C=false;
if(r.enteredPlan!==undefined&&r.enteredPlan!==null&&r.enteredPlan.length>0){for(var v=0;
v<r.enteredPlan.length;
v++){if(r.enteredPlan[v].sequentialPlanId===u.planID$AG$String){if(x){r.improveFrom=r.enteredPlan[v].planObjectId
}C=true;
A=angular.copy(r.enteredPlan);
r.checkDrop=true
}}}if(!C&&r.submittedPlan!==undefined&&r.submittedPlan!==null&&r.submittedPlan.length>0){for(var v=0;
v<r.submittedPlan.length;
v++){if(r.submittedPlan[v].sequentialPlanId===u.planID$AG$String){if(x){r.improveFrom=r.submittedPlan[v].planObjectId
}C=true;
A=angular.copy(r.submittedPlan);
r.checkDrop=true
}}}}r.currentObject=undefined;
if(u!==null&&u!==undefined&&A!==null&&A!==undefined&&A.length>0){var t=false;
for(var v=0;
v<A.length;
v++){if(A[v].sequentialPlanId===u.planID$AG$String){r.currentObject=angular.copy(A[v]);
u=r.currentObject;
if(r.currentObject.referencePlan===null){t=true;
r.detailsFilled=false;
r.selectedNoOfTag=[];
r.tag=1;
r.checkDrop=true;
r.currencyCode=u.currencyCode;
r.selectedRadio=angular.copy({color:u.colorId,cut:u.cutId,clarity:u.clarityId,fluorescence:u.flurosceneId});
r.categoryCustom=u.writeCustom;
r.previousTab="A";
r.selectedNoOfTag.push(1);
r.tabManager={};
r.tagList=[];
var z=angular.copy(u.cent);
var s=angular.copy(u.carat);
r.tagList.push({code:String.fromCharCode(65),colorRadioList:r.colorRadioList,carat:s,cents:z,clarity:r.clarityRadioList,cut:r.cutRadioList,fluorescence:r.fluorescenceRadioList,colorGroup:1,clarityGroup:2,cutGroup:3,fluorescenceGroup:4,selectColor:u.colorId,selectClarity:u.clarityId,selectCut:u.cutId,selectFluorescence:u.flurosceneId,price:u.price,currencyCode:r.currencyCode,breakage:u.breakage});
r.tabManager.tagList=r.tagList;
r.detailsFilled=true
}break
}}if(r.currentObject!==undefined){console.log("here");
r.multipleCurrentObjects=[];
for(var v=0;
v<A.length;
v++){if(!t&&r.currentObject.referencePlan===A[v].referencePlan){r.multipleCurrentObjects.push(A[v])
}else{if(t&&r.currentObject.planObjectId===A[v].referencePlan){r.multipleCurrentObjects.push(A[v])
}}if(!t&&r.currentObject.referencePlan===A[v].planObjectId){u=angular.copy(A[v])
}}if(r.multipleCurrentObjects.length>0){r.detailsFilled=false;
r.tag=r.multipleCurrentObjects.length+1;
for(var v=0;
v<r.multipleCurrentObjects.length;
v++){r.selectedNoOfTag.push(v+2)
}r.currencyCode=u.currencyCode;
r.selectedRadio=angular.copy({color:u.colorId,cut:u.cutId,clarity:u.clarityId,fluorescence:u.flurosceneId});
r.previousTab="A";
r.tabManager={};
r.tagList=[];
var w=u.cent;
var y=u.carat;
r.tagList.push({code:String.fromCharCode(65),colorRadioList:r.colorRadioList,carat:y,cents:w,clarity:r.clarityRadioList,cut:r.cutRadioList,fluorescence:r.fluorescenceRadioList,colorGroup:27,clarityGroup:28,cutGroup:29,fluorescenceGroup:30,selectColor:u.colorId,selectClarity:u.clarityId,selectCut:u.cutId,selectFluorescence:u.flurosceneId,price:u.price,currencyCode:r.currencyCode,breakage:u.breakage});
r.tabManager.tagList=r.tagList;
for(var B=0;
B<r.multipleCurrentObjects.length;
B++){r.tagList.push({code:r.multipleCurrentObjects[B].tag,colorRadioList:(r.colorRadioList),carat:r.multipleCurrentObjects[B].carat,cents:r.multipleCurrentObjects[B].cent,clarity:angular.copy(r.clarityRadioList),cut:r.cutRadioList,fluorescence:r.fluorescenceRadioList,colorGroup:B,clarityGroup:B,cutGroup:B,fluorescenceGroup:B,selectColor:r.multipleCurrentObjects[B].colorId,selectClarity:r.multipleCurrentObjects[B].clarityId,selectCut:r.multipleCurrentObjects[B].cutId,selectFluorescence:r.multipleCurrentObjects[B].flurosceneId,price:r.multipleCurrentObjects[B].price,currencyCode:r.multipleCurrentObjects[B].currencyCode,breakage:r.multipleCurrentObjects[B].breakage})
}r.detailsFilled=true
}}}o.unMaskLoading()
};
r.editPlanWithoutCalc=function(t,w){var y=undefined;
r.improveFrom=undefined;
if(t!==undefined&&t!==null){r.checkDrop=false;
r.flag.editMode=true;
r.deleteIdsFromDb=[];
var A=false;
if(r.enteredPlan!==undefined&&r.enteredPlan!==null&&r.enteredPlan.length>0){for(var u=0;
u<r.enteredPlan.length;
u++){if(r.enteredPlan[u].sequentialPlanId===t.planID$AG$String){if(w){r.improveFrom=r.enteredPlan[u].planObjectId
}A=true;
y=angular.copy(r.enteredPlan);
r.checkDrop=true
}}}if(!A&&r.submittedPlan!==undefined&&r.submittedPlan!==null&&r.submittedPlan.length>0){for(var u=0;
u<r.submittedPlan.length;
u++){if(r.submittedPlan[u].sequentialPlanId===t.planID$AG$String){if(w){r.improveFrom=r.submittedPlan[u].planObjectId
}A=true;
y=angular.copy(r.submittedPlan);
r.checkDrop=true
}}}}r.currentObject=undefined;
if(t!==null&&t!==undefined&&y!==null&&y!==undefined&&y.length>0){var s=false;
r.writeAddFlag=false;
for(var u=0;
u<y.length;
u++){if(y[u].sequentialPlanId===t.planID$AG$String){r.currentObject=angular.copy(y[u]);
t=r.currentObject;
if(r.currentObject.referencePlan===null){s=true;
r.detailsFilled=false;
r.selectedNoOfTag=[];
r.tag=1;
r.checkDrop=true;
r.categoryCustom={};
r.categoryCustom=t.writeCustom;
r.previousTab="A";
r.selectedNoOfTag.push(1);
r.tabManager={};
r.tagList=[];
r.tagList.push({code:String.fromCharCode(65)});
r.tabManager.tagList=r.tagList;
r.writeAddFlag=true;
r.detailsFilled=true;
console.log("here"+JSON.stringify(t.writeCustom))
}break
}}if(r.currentObject!==undefined){console.log("here");
r.multipleCurrentObjects=[];
for(var u=0;
u<y.length;
u++){if(!s&&r.currentObject.referencePlan===y[u].referencePlan){r.multipleCurrentObjects.push(y[u])
}else{if(s&&r.currentObject.planObjectId===y[u].referencePlan){r.multipleCurrentObjects.push(y[u])
}}if(!s&&r.currentObject.referencePlan===y[u].planObjectId){t=angular.copy(y[u])
}}if(r.multipleCurrentObjects.length>0){console.log("multipleCurrentObjects");
r.detailsFilled=false;
r.tag=r.multipleCurrentObjects.length+1;
for(var u=0;
u<r.multipleCurrentObjects.length;
u++){r.selectedNoOfTag.push(u+2)
}r.currencyCode=t.currencyCode;
r.selectedRadio=angular.copy({color:t.colorId,cut:t.cutId,clarity:t.clarityId,fluorescence:t.flurosceneId});
r.previousTab="A";
r.tabManager={};
r.tagList=[];
var v=t.cent;
var x=t.carat;
r.tagList.push({code:String.fromCharCode(65),colorRadioList:r.colorRadioList,carat:x,cents:v,clarity:r.clarityRadioList,cut:r.cutRadioList,fluorescence:r.fluorescenceRadioList,colorGroup:27,clarityGroup:28,cutGroup:29,fluorescenceGroup:30,selectColor:t.colorId,selectClarity:t.clarityId,selectCut:t.cutId,selectFluorescence:t.flurosceneId,price:t.price,currencyCode:r.currencyCode,breakage:t.breakage});
r.tabManager.tagList=r.tagList;
for(var z=0;
z<r.multipleCurrentObjects.length;
z++){r.tagList.push({code:r.multipleCurrentObjects[z].tag,colorRadioList:(r.colorRadioList),carat:r.multipleCurrentObjects[z].carat,cents:r.multipleCurrentObjects[z].cent,clarity:angular.copy(r.clarityRadioList),cut:r.cutRadioList,fluorescence:r.fluorescenceRadioList,colorGroup:z,clarityGroup:z,cutGroup:z,fluorescenceGroup:z,selectColor:r.multipleCurrentObjects[z].colorId,selectClarity:r.multipleCurrentObjects[z].clarityId,selectCut:r.multipleCurrentObjects[z].cutId,selectFluorescence:r.multipleCurrentObjects[z].flurosceneId,price:r.multipleCurrentObjects[z].price,currencyCode:r.multipleCurrentObjects[z].currencyCode,breakage:r.multipleCurrentObjects[z].breakage})
}r.detailsFilled=true
}}}};
r.deletePlan=function(){var t=angular.copy(r.objectToDelete);
var v=undefined;
var s=false;
var w=undefined;
if(t!==undefined&&t!==null&&r.enteredPlan!==undefined&&r.enteredPlan!==null&&r.enteredPlan.length>0){for(var u=0;
u<r.enteredPlan.length;
u++){if(r.enteredPlan[u].sequentialPlanId===t.planID$AG$String){w=r.enteredPlan[u];
if(r.enteredPlan[u].referencePlan===null){v=r.enteredPlan[u].planObjectId;
s=true;
break
}}}if(!s){for(var u=0;
u<r.enteredPlan.length;
u++){if(r.enteredPlan[u].sequentialPlanId===t.planID$AG$String){v=angular.copy(r.enteredPlan[u].planObjectId);
s=true;
break
}}}console.log(JSON.stringify("planId"+v));
if(s){r.objectToDelete={};
$("#deleteDialog").modal("hide");
o.removeModalOpenCssAfterModalHide();
m.deletePlan(v,function(x){r.addPlan()
},function(){var y="Could not save, please try again.";
var x=o.error;
o.addMessage(y,x);
o.unMaskLoading()
})
}}};
var h=function(s){if(s){r.flag.showAddPage=false;
r.retrieveFromWorkAllocation();
r.issuedStock={};
r.issuedStock.data=[]
}};
r.submitPlan=function(w){var x={};
var t=[];
var s=[];
if(r.selectedLotOrPacket!==undefined&&r.selectedLotOrPacket!==null&&r.selectedLotOrPacket.length>0){for(var v=0;
v<r.enteredPlan.length;
v++){if(r.enteredPlan[v].sequentialPlanId===r.selectedLotOrPacket[0].planID$AG$String){var u=[];
u.push(r.enteredPlan[v].planObjectId);
x.bestPlanId=u;
break
}}}angular.forEach(r.enteredPlan,function(y){t.push(y.planObjectId)
});
x.plan=t;
s.push(angular.copy(r.workAllocationId));
x.work=s;
console.log(x);
m.submitPlan(x,function(y){o.maskLoading();
e(function(){n.reload()
},3000);
o.unMaskLoading()
},function(){var z="Could not save, please try again.";
var y=o.error;
o.addMessage(z,y);
o.unMaskLoading()
})
};
r.saveEditedPlan=function(s){r.submitted=true;
if(s.$valid){r.submitted=false;
for(prop in r.tabAndDynamicFormModel){if(prop===r.previousTab){r.tabAndDynamicFormModel[prop]=angular.copy(r.categoryCustom);
r.writeAddFlag=false;
r.reset("categoryCustom");
break
}}angular.forEach(r.tabAndDynamicFormModel,function(x,w){for(var v=0;
v<r.tagList.length;
v++){if(r.tagList[v].code===w){r.tagList[v].dynamicForm=x
}}});
r.writeServiceDataBean={};
r.writeServiceDataBean.planType=r.planType;
r.writeServiceDataBean.writeDbType=r.writeDbType;
if(r.packetIdForConstraint!==undefined){r.writeServiceDataBean.id=r.packetIdForConstraint;
r.writeServiceDataBean.hasPacket=true
}else{r.writeServiceDataBean.id=r.lotIdForConstraint;
r.writeServiceDataBean.hasPacket=false
}var u=[];
var t={};
angular.forEach(r.enteredPlan,function(v){if(i.indexOf(v.planObjectId)===-1){t[v.tag]=v.planObjectId
}});
r.mainPlanObjectId=undefined;
angular.forEach(r.tagList,function(v){if(v.code==="A"){r.mainPlanObjectId=t[v.code]
}u.push({planObjectId:t[v.code],tag:v.code,colorId:v.selectColor,clarityId:v.selectClarity,cutId:v.selectCut,flurosceneId:v.selectFluorescence,price:v.price,writeCustom:v.dynamicForm,currencyCode:v.currencyCode,caratValue:parseFloat(v.carat.toString().concat(".",v.cents.toString())),breakage:v.breakage})
});
r.writeServiceDataBean.writeServiceDataBeans=u;
r.writeServiceDataBean.deletedIds=i;
r.writeServiceDataBean.planObjectId=r.mainPlanObjectId;
console.log("------>>>"+JSON.stringify(r.writeServiceDataBean.writeServiceDataBeans));
m.saveEditedPlan(r.writeServiceDataBean,function(v){r.addPlan()
},function(){var w="Could not save, please try again.";
var v=o.error;
o.addMessage(w,v);
o.unMaskLoading()
})
}};
r.retrievePreviousValue=function(){if(!!r.flag&&!!r.flag.pricelist){console.log("$scope.gridOptions.data:::"+JSON.stringify(r.gridOptions.data));
var s={};
angular.forEach(r.gridOptions.data,function(v){var t={};
for(var u=0;
u<r.enteredPlan.length;
u++){if(v.planID$AG$String===r.enteredPlan[u].sequentialPlanId){t.cut=r.enteredPlan[u].cutId;
t.color=r.enteredPlan[u].colorId;
t.clarity=r.enteredPlan[u].clarityId;
t.fluorescence=r.enteredPlan[u].flurosceneId;
t.carat=r.enteredPlan[u].caratId;
s[r.enteredPlan[u].sequentialPlanId]=t;
break
}}});
s.pricelist={id:r.flag.pricelist};
c.retrievevaluefrompricelist(s,function(u){angular.forEach(r.gridOptions.data,function(v){v.previousPrice=u[v.planID$AG$String]
});
var t={name:"previousPrice",displayName:"Previous Price",cellTemplate:'<div><span class="col-md-3">{{row.entity.previousPrice}}</span><div class="col-md-9" ng-show="row.entity.previousPrice!==N/A"><span ng-if="row.entity.previousPrice<row.entity.price" class="glyphicon glyphicon-arrow-down" style="color: red"></span><span ng-if="row.entity.previousPrice>row.entity.price" class="glyphicon glyphicon-arrow-up" style="color: green"></span>{{((row.entity.previousPrice-row.entity.price)/row.entity.price)*100 | number:2}}%</div></div>'};
r.gridOptions.columnDefs=r.gridOptions.columnDefs.filter(function(v){return v.name!=="previousPrice"
});
r.gridOptions.columnDefs.splice(r.gridOptions.columnDefs.length-1,0,t)
})
}else{r.gridOptions.columnDefs=r.gridOptions.columnDefs.filter(function(t){return t.name!=="previousPrice"
})
}};
r.openDeletePlanPopup=function(s){$("#deleteDialog").modal("show");
r.objectToDelete=s
};
r.hidePopUp=function(){$("#deleteDialog").modal("hide");
o.removeModalOpenCssAfterModalHide()
};
r.onCancel=function(s){r.writeForm.$setPristine();
r.writeForm.$valid=true;
r.detailsFilled=false;
r.tabAndDynamicFormModel={};
r.selectedNoOfTag=[];
r.tag=1;
r.checkDrop=true;
r.tabAndDynamicFormModel.A=null;
r.previousTab="A";
r.selectedNoOfTag.push(1);
r.tabManager={};
r.tagList=[];
r.numberOfTagChanged(1);
r.tabManager.tagList=r.tagList;
r.detailsFilled=true
}
}])
});