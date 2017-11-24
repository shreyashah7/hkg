define(["hkg","customFieldService","generateSlipService","lotService","ngload!uiGrid","addMasterValue","customsearch.directive","dynamicForm","accordionCollapse"],function(a){a.register.controller("GenerateSlipController",["$rootScope","$scope","DynamicFormService","GenerateSlipService","CustomFieldService","LotService","$filter",function(b,e,d,c,h,f,g){b.maskLoading();
e.generateSlipList=[];
e.initializeData=function(){var i=d.retrieveSearchWiseCustomFieldInfo("generateSlip");
i.then(function(j){e.generalSearchTemplate=j.genralSection
},function(j){console.log("reason :"+j)
},function(j){console.log("update :"+j)
});
e.flag={};
e.generateSlipList=[];
e.listFilled=false;
e.generateSlipDataBean={};
e.slipLabelListForUiGrid=[];
e.gridOptions={};
e.gridOptions.enableFiltering=true;
e.gridOptions.multiSelect=true;
e.gridOptions.columnDefs=[];
e.gridOptions.data=[];
e.gridOptions.onRegisterApi=function(j){e.gridApi=j;
j.selection.on.rowSelectionChanged(e,function(k){if(e.gridApi.selection.getSelectedRows().length>0){e.flag.rowSelectedflag=true
}else{e.flag.rowSelectedflag=false
}});
j.selection.on.rowSelectionChangedBatch(e,function(k){if(e.gridApi.selection.getSelectedRows().length>0){e.flag.rowSelectedflag=true
}else{e.flag.rowSelectedflag=false
}})
};
e.searchedSlipList=[];
e.slipLabelListForUiGrid=[];
b.maskLoading();
c.retrieveLotsAndPackets(function(k){b.unMaskLoading();
e.dataRetrieved=true;
if(k.generateSlipList!==null&&k.tabelHeaders!==null&&k.tabelHeaders.length>0){e.searchedSlipList=k.generateSlipList;
e.nodeDetailsInfo=[];
e.slipLabelListForUiGrid=k.tabelHeaders;
if(e.generalSearchTemplate===null||e.generalSearchTemplate===undefined){e.flag.configSearchFlag=true
}else{if(k.dynamicServiceInitBean!==null&&k.dynamicServiceInitBean!==undefined){var j=k.dynamicServiceInitBean;
if(j.nodeAndWorkAllocationIds!==null){e.nodeIdAndWorkAllocationIdsMap=angular.copy(j.nodeAndWorkAllocationIds)
}if(j.mandatoryFields!==null){e.stockStaticFields=j.mandatoryFields
}if(j.diamondsInQueue!==null){e.diamondsInQueue=j.diamondsInQueue
}if(!!j.dynamicServiceInitDataBeans){var l=j.dynamicServiceInitDataBeans;
if(angular.isArray(l)&&l.length>0){angular.forEach(l,function(n){var o={};
o.groupId=n.groupId;
o.groupName=n.groupName;
o.modifier=n.modifier;
o.nodeId=n.nodeId;
o.nodeName=n.nodeName;
e.nodeDetailsInfo.push(o)
})
}if(e.nodeDetailsInfo.length>1){e.flag.multipleIdInvolved=true
}else{e.flag.multipleIdInvolved=false
}}if(e.nodeDetailsInfo.length>0){e.currentActivityNode=e.nodeDetailsInfo[0].nodeId
}}var m=function(n){angular.forEach(e.searchedSlipList,function(o){angular.forEach(e.slipLabelListForUiGrid,function(p){if(!o.categoryCustom.hasOwnProperty(p.name)){o.categoryCustom[p.name]="NA"
}else{if(o.categoryCustom.hasOwnProperty(p.name)){if(o.categoryCustom[p.name]===null||o.categoryCustom[p.name]===""||o.categoryCustom[p.name]===undefined){o.categoryCustom[p.name]="NA"
}}}if(o.hasOwnProperty("value")){o.categoryCustom["~@value"]=o.value
}if(o.hasOwnProperty("label")){o.categoryCustom["~@label"]=o.label
}if(o.hasOwnProperty("description")){o.categoryCustom["~@description"]=o.description
}if(o.hasOwnProperty("id")){o.categoryCustom["~@id"]=o.id
}if(o.hasOwnProperty("status")){o.categoryCustom["~@status"]=o.status
}});
e.generateSlipList.push(o.categoryCustom)
});
e.updateStockAccordingToNode(e.currentActivityNode);
e.flag.configSearchFlag=false
};
d.convertorForCustomField(e.searchedSlipList,m)
}}},function(){b.unMaskLoading()
})
};
e.updateStockAccordingToNode=function(k){if(k!==null&&k!==undefined){e.currentActivityNode=k;
angular.forEach(e.nodeDetailsInfo,function(o){if(o.nodeId===k){var l=o.modifier;
if(l!==null){if(l.indexOf("|")>-1){var m=l.split("|");
for(var n=0;
n<m.length;
n++){if(m[n]==="Q"){e.isQuingRequired=true
}}}else{if(l==="AA"||l==="MA"){e.typeOfAllocation=l
}}}}});
if(angular.isDefined(e.nodeIdAndWorkAllocationIdsMap)){var i=[];
var j=e.nodeIdAndWorkAllocationIdsMap[k];
console.log(j.length>0);
angular.forEach(e.generateSlipList,function(m,l){if(j.length>0&&j.indexOf(m["~@value"])!==-1){m["~@index"]=l+1;
i.push(m)
}});
e.currentNodeStocks=angular.copy(i);
if(angular.isDefined(e.gridApi)){e.gridApi.selection.clearSelectedRows()
}e.gridOptions.data=i;
e.gridOptions.columnDefs=e.slipLabelListForUiGrid;
e.listFilled=true;
if(!!e.isQuingRequired){e.gridOptions.enableSorting=false;
e.gridOptions.isRowSelectable=function(l){if(!!e.diamondsInQueue){if(l.entity["~@index"]>e.diamondsInQueue){return false
}else{return true
}}else{if(l.entity["~@index"]>0){return true
}else{return false
}}}
}}else{console.log("Node map not initialized")
}}};
e.initGenerateSlipForm=function(i){e.generateSlipForm=i
};
e.onCancelOfSearch=function(){e.gridApi.selection.clearSelectedRows()
};
e.reset=function(j){if(j==="searchCustom"){e.searchCustom={};
var i=d.retrieveSearchWiseCustomFieldInfo("generateslip");
e.dbType={};
i.then(function(k){e.generalSearchTemplate=k.genralSection;
e.searchResetFlag=true
},function(k){},function(k){})
}};
e.generateSlip=function(){if(e.gridApi.selection.getSelectedRows().length>1){e.generateSlipDataBean.workAllotmentIds=[];
e.noOfPackets=0;
e.noOfLots=0;
e.selectedRows=e.gridApi.selection.getSelectedRows();
e.slipObj=angular.copy(e.selectedRows);
for(var k=0;
k<e.selectedRows.length;
k++){if(e.selectedRows[k]["~@status"]!==null){e.noOfPackets=e.noOfPackets+1
}else{if(e.selectedRows[k]["~@status"]===null&&e.selectedRows[k]["~@id"]!==null){e.noOfLots=e.noOfLots+1
}}}if(e.noOfLots>0&&e.noOfPackets>0){delete e.selectedRows;
e.selectedRows=[];
e.gridApi.selection.clearSelectedRows();
var j="Only select either lots or packets";
var i=b.warning;
b.addMessage(j,i)
}else{if(e.selectedRows[0]["~@status"]!==null){e.generateSlipDataBean.isPacket=true
}else{e.generateSlipDataBean.isPacket=false
}for(var k=0;
k<e.selectedRows.length;
k++){if(e.selectedRows[k]["~@value"]!==null){e.generateSlipDataBean.workAllotmentIds.push(e.selectedRows[k]["~@value"].toString())
}}if(e.generateSlipDataBean.workAllotmentIds!==undefined&&e.generateSlipDataBean.workAllotmentIds!==null){c.generateSlip(e.generateSlipDataBean,function(l){e.initializeData()
},function(){console.log("faliure")
})
}}}else{e.gridApi.selection.clearSelectedRows();
var j="Please select more than one stock to generate slip";
var i=b.warning;
b.addMessage(j,i)
}};
b.unMaskLoading()
}])
});