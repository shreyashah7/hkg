define(["hkg","customFieldService","editvalueService","ngload!uiGrid","finalizeService"],function(a){a.register.controller("EditValueController",["$rootScope","$scope","DynamicFormService","CustomFieldService","EditValueService","FinalizeService",function(b,e,d,g,c,f){b.maskLoading();
b.mainMenu="stockLink";
b.childMenu="editValue";
b.activateMenu();
e.headerListForPlans=[{name:"planNumber",displayName:"Plan Id"},{name:"stockNumber",displayName:"Lot/Packet Id"},{name:"cut",displayName:"Cut"},{name:"color",displayName:"Color"},{name:"clarity",displayName:"Clarity"},{name:"carat",displayName:"Carat"},{name:"price",displayName:"Price"}];
e.initializeData=function(){e.stockdataflag=false;
e.lotPacketMap={};
e.featureMap={};
e.flag={};
e.flag={};
e.flag.showstockPage=false;
e.stockList=[];
e.listFilled=false;
e.flag.rowSelectedflag=false;
e.stockLabelListForUiGrid=[];
e.gridOptions={};
e.gridOptions.enableFiltering=true;
e.gridOptions.multiSelect=true;
e.gridOptions.columnDefs=[];
e.gridOptions.data=[];
e.gridOptions.onRegisterApi=function(i){e.gridApi=i;
i.selection.on.rowSelectionChanged(e,function(l){e.lotPacketMap={};
for(var j=0;
j<e.gridApi.selection.getSelectedRows().length;
j++){var k=e.gridApi.selection.getSelectedRows()[j];
if(k["~@type"]==="lot"){if(!!!e.lotPacketMap.lot){e.lotPacketMap.lot=[]
}e.lotPacketMap.lot.push({id:k["~@stockid"],value:k["~@stocknumber"]})
}else{if(k["~@type"]==="packet"){if(!!!e.lotPacketMap.packet){e.lotPacketMap.packet=[]
}e.lotPacketMap.packet.push({id:k["~@stockid"],value:k["~@stocknumber"]})
}}}})
};
e.searchedStockList=[];
e.generalSearchTemplate=[];
e.stockLabelListForUiGrid=[];
var h=d.retrieveSearchWiseCustomFieldInfo("valueEdit");
h.then(function(m){e.generalSearchTemplate=m.genralSection;
if(!!e.generalSearchTemplate&&e.generalSearchTemplate.length>0){for(var j=0;
j<e.generalSearchTemplate.length;
j++){var l=e.generalSearchTemplate[j];
e.featureMap[l.model]=l.featureName;
if(l.fromModel){e.stockLabelListForUiGrid.push({name:l.fromModel,displayName:l.label,minWidth:200})
}else{if(l.toModel){e.stockLabelListForUiGrid.push({name:l.toModel,displayName:l.label,minWidth:200})
}else{if(l.model){e.stockLabelListForUiGrid.push({name:l.model,displayName:l.label,minWidth:200})
}}}}}e.searchResetFlag=true;
e.map={};
var k={};
angular.forEach(e.featureMap,function(n,i){if(!k[n]){k[n]=[]
}k[n].push(i)
});
b.maskLoading();
c.retrieveLotsPacketsEditValue(k,function(i){e.searchedStockList=angular.copy(i.stockList);
if(!e.generalSearchTemplate){e.flag.configSearchFlag=true;
b.unMaskLoading()
}else{var n=function(o){angular.forEach(e.searchedStockList,function(p){angular.forEach(e.stockLabelListForUiGrid,function(q){if(!p.categoryCustom.hasOwnProperty(q.name)){p.categoryCustom[q.name]="NA"
}else{if(p.categoryCustom.hasOwnProperty(q.name)){if(p.categoryCustom[q.name]===null||p.categoryCustom[q.name]===""||p.categoryCustom[q.name]===undefined){p.categoryCustom[q.name]="NA"
}}}if(p.hasOwnProperty("description")){p.categoryCustom["~@type"]=p.description
}if(p.hasOwnProperty("value")){p.categoryCustom["~@stocknumber"]=p.value
}if(p.hasOwnProperty("label")){p.categoryCustom["~@stockid"]=p.label
}});
e.stockList.push(p.categoryCustom)
});
e.gridOptions.data=e.stockList;
e.gridOptions.columnDefs=e.stockLabelListForUiGrid;
e.listFilled=true;
e.flag.configSearchFlag=false;
b.unMaskLoading()
};
d.convertorForCustomField(e.searchedStockList,n,function(){b.unMaskLoading()
})
}},function(){b.unMaskLoading()
})
},function(i){},function(i){})
};
e.onCanelOfSearch=function(){e.gridApi.selection.clearSelectedRows()
};
e.editvalueNext=function(){e.flag.showstockPage=true;
e.stockdataflag=true;
e.listFilled1=false;
f.retrievePriceList(function(i){e.pricelistDtl=JSON.parse(angular.toJson(i));
for(var k in e.pricelistDtl){if(e.pricelistDtl.hasOwnProperty(k)){var j=e.pricelistDtl[k];
e.pricelistDtl[k]=new Date(j).toUTCString().replace(/\s*(GMT|UTC)$/,"")
}}});
var h={};
if(!!e.lotPacketMap.lot){angular.forEach(e.lotPacketMap.lot,function(i){if(!!!h.lot){h.lot=[]
}h.lot.push(i.id)
})
}if(!!e.lotPacketMap.packet){angular.forEach(e.lotPacketMap.packet,function(i){if(!!!h.packet){h.packet=[]
}h.packet.push(i.id)
})
}e.gridForPlan={};
e.gridForPlan.enableFiltering=true;
e.gridForPlan.multiSelect=true;
e.gridForPlan.columnDefs=[];
e.gridForPlan.data=[];
e.gridForPlan.onRegisterApi=function(i){e.gridApi1=i;
i.selection.on.rowSelectionChanged(e,function(l){e.planIdsToChange=[];
for(var j=0;
j<e.gridApi1.selection.getSelectedRows().length;
j++){var k=e.gridApi1.selection.getSelectedRows()[j];
e.planIdsToChange.push({planId:k.planId,previousPrice:k.previousPrice})
}})
};
b.maskLoading();
c.retrievePlansByLotOrPacket(h,function(i){b.unMaskLoading();
angular.forEach(i,function(k){if(k.stockType==="lot"){var j=$.grep(e.lotPacketMap.lot,function(l){return l.id===k.stockId
});
if(j.length===1){k.stockNumber=j[0].value
}}});
e.gridForPlan.data=i;
e.gridForPlan.columnDefs=e.headerListForPlans;
e.listFilled1=true
},function(){e.listFilled1=true;
b.unMaskLoading()
})
};
e.retrievePreviousValue=function(){if(!!e.gridApi1){e.gridApi1.selection.clearSelectedRows();
if(!!e.flag&&!!e.flag.pricelist){var h={};
angular.forEach(e.gridForPlan.data,function(j){var i={};
i.cut=j.cutId;
i.color=j.colorId;
i.clarity=j.clarityId;
i.fluorescence=j.flurosceneId;
i.carat=j.caratId;
h.planNumber=i
});
h.pricelist={id:e.flag.pricelist};
f.retrievevaluefrompricelist(h,function(j){angular.forEach(e.gridForPlan.data,function(k){k.previousPrice=j[k.planID$AG$String]
});
var i={name:"previousPrice",displayName:"Previous Price",cellTemplate:'<div><span class="col-md-3">{{row.entity.previousPrice}}</span><div class="col-md-9" ng-if="row.entity.previousPrice!==\'N/A\'"><span ng-if="row.entity.previousPrice<row.entity.price" class="glyphicon glyphicon-arrow-down" style="color: red"></span><span ng-if="row.entity.previousPrice>row.entity.price" class="glyphicon glyphicon-arrow-up" style="color: green"></span>{{((row.entity.previousPrice-row.entity.price)/row.entity.price)*100 | number:2}}</div><div class="col-md-9" ng-if="row.entity.previousPrice===N/A || !row.entity.previousPrice">{{entity +"N/A" | translate}}</div></div>'};
e.gridForPlan.columnDefs=e.gridForPlan.columnDefs.filter(function(k){return k.name!=="previousPrice"
});
e.gridForPlan.columnDefs.splice(e.gridForPlan.columnDefs.length,0,i)
})
}else{e.gridForPlan.columnDefs=e.gridForPlan.columnDefs.filter(function(i){return i.name!=="previousPrice"
})
}}};
e.editPlans=function(){if(!!e.planIdsToChange){var h=[];
angular.forEach(e.planIdsToChange,function(i){if(i.previousPrice!==null&&e.previousPrice!=="N/A"){h.push(i)
}});
c.editValues(h,function(i){e.initializeData()
},function(){e.initializeData()
})
}};
b.unMaskLoading()
}])
});