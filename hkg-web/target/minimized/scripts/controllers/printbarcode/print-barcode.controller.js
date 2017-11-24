define(["hkg","printBarcodeService","customFieldService","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicForm"],function(a,b){a.register.controller("PrintBarcodeController",["$rootScope","$scope","PrintBarcodeService","DynamicFormService","$q",function(c,f,h,e,d){c.maskLoading();
c.mainMenu="stockLink";
c.childMenu="printBarcode";
c.activateMenu();
var g={};
f.initializeData=function(){f.printBarcodeDataBean={};
f.submitted=false;
f.flag={};
f.searchResetFlag=false;
f.flag.rowSelectedflag=false;
f.searchedData=[];
f.searchedDataFromDb=[];
f.listFilled=false;
f.fieldList=[{id:"Invoice",text:"Invoice"},{id:"Parcel",text:"Parcel"},{id:"Lot",text:"Lot"},{id:"Packet",text:"Packet"},{id:"Sell",text:"Sell"},{id:"Transfer",text:"Transfer"},{id:"Lot Slip",text:"Lot Slip"},{id:"Packet Slip",text:"Packet Slip"}];
f.searchCustom={};
var i=e.retrieveSearchWiseCustomFieldInfo("barcodePrint");
f.dbType={};
f.stockLabelListForUiGrid=[];
f.gridOptions={};
f.gridOptions.enableFiltering=true;
f.gridOptions.multiSelect=true;
f.gridOptions.enableRowSelection=true;
f.gridOptions.enableSelectAll=true;
f.gridOptions.columnDefs=[];
f.gridOptions.data=[];
f.gridOptions.selectedItems=[];
f.gridOptions.onRegisterApi=function(j){f.gridApi=j;
j.selection.on.rowSelectionChanged(f,function(k){if(f.gridApi.selection.getSelectedRows().length>0){f.flag.rowSelectedflag=true
}else{f.flag.rowSelectedflag=false
}});
j.selection.on.rowSelectionChangedBatch(f,function(k){if(f.gridApi.selection.getSelectedRows().length>0){f.flag.rowSelectedflag=true
}else{f.flag.rowSelectedflag=false
}})
};
i.then(function(l){f.searchInvoiceTemplate=[];
f.searchParcelTemplate=[];
f.searchLotTemplate=[];
f.searchPacketTemplate=[];
f.generalSearchTemplate=l.genralSection;
if(f.generalSearchTemplate!==undefined&&f.generalSearchTemplate!==null&&f.generalSearchTemplate.length>0){f.flag.configSearchFlag=false;
for(var j=0;
j<f.generalSearchTemplate.length;
j++){var k=f.generalSearchTemplate[j];
if(k.featureName.toLowerCase()==="invoice"){f.searchInvoiceTemplate.push(angular.copy(k))
}else{if(k.featureName.toLowerCase()==="parcel"){f.searchParcelTemplate.push(angular.copy(k))
}else{if(k.featureName.toLowerCase()==="lot"){f.searchLotTemplate.push(angular.copy(k))
}else{if(k.featureName.toLowerCase()==="packet"){f.searchPacketTemplate.push(angular.copy(k))
}}}}g[k.model]=k.featureName;
if(k.fromModel){f.stockLabelListForUiGrid.push({name:k.fromModel,displayName:k.label,minWidth:200})
}else{if(k.toModel){f.stockLabelListForUiGrid.push({name:k.toModel,displayName:k.label,minWidth:200})
}else{if(k.model){f.stockLabelListForUiGrid.push({name:k.model,displayName:k.label,minWidth:200})
}}}}}else{f.flag.configSearchFlag=true
}f.searchResetFlag=true
},function(j){},function(j){})
};
f.initPrintBarcodeForm=function(i){f.printBarcodeForm=i
};
f.reset=function(j){if(j==="searchCustom"){f.searchCustom={};
var i=e.retrieveSearchWiseCustomFieldInfo("barcodePrint");
f.dbType={};
i.then(function(k){f.generalSearchTemplate=k.genralSection;
f.searchResetFlag=true
},function(k){console.log("reason :"+k)
},function(k){console.log("update :"+k)
})
}};
f.onCancelOfSearch=function(){if(f.printBarcodeForm!==null){f.printBarcodeForm.$dirty=false
}f.listFilled=false;
f.searchResetFlag=false;
f.reset("searchCustom");
f.initializeData();
f.printBarcodeForm.$setPristine()
};
f.retrieveSearchedData=function(){f.selectOneParameter=false;
f.searchedData=[];
f.searchedDataFromDb=[];
f.listFilled=false;
f.stockList=[];
f.submitted=true;
f.gridOptions.columnDefs=[];
f.gridOptions.data=[];
if(f.printBarcodeForm.$valid){if(Object.getOwnPropertyNames(f.searchCustom).length>0){c.maskLoading();
var l=false;
for(var o in f.searchCustom){if(typeof f.searchCustom[o]==="object"&&f.searchCustom[o]!==null){var m=angular.copy(f.searchCustom[o].toString());
if(typeof m==="string"&&m!==null&&m!==undefined&&m.length>0){l=true;
break
}}if(typeof f.searchCustom[o]==="string"&&f.searchCustom[o]!==null&&f.searchCustom[o]!==undefined&&f.searchCustom[o].length>0){l=true;
break
}if(typeof f.searchCustom[o]==="number"&&!!(f.searchCustom[o])){l=true;
break
}if(typeof f.searchCustom[o]==="boolean"){l=true;
break
}}if(l){f.printBarcodeDataBean.featureCustomMapValue={};
f.map={};
var k={};
var i=e.convertSearchData(f.searchInvoiceTemplate,f.searchParcelTemplate,f.searchLotTemplate,f.searchPacketTemplate,f.searchCustom);
angular.forEach(g,function(t,r){var s=i[r];
if(s!==undefined){var q={};
if(!k[t]){q[r]=s;
k[t]=q
}else{var p=k[t];
p[r]=s;
k[t]=p
}}else{s=i["to"+r];
if(s!==undefined){var q={};
if(!k[t]){q["to"+r]=s;
k[t]=q
}else{var p=k[t];
p["to"+r]=s;
k[t]=p
}}s=i["from"+r];
if(s!==undefined){var q={};
if(!k[t]){q["from"+r]=s;
k[t]=q
}else{var p=k[t];
p["from"+r]=s;
k[t]=p
}}}});
f.printBarcodeDataBean.featureCustomMapValue=k;
f.printBarcodeDataBean.featureMap=g;
h.retrieveSearchedStock(f.printBarcodeDataBean,function(p){f.searchedDataFromDb=angular.copy(p);
var q=function(){angular.forEach(f.searchedDataFromDb,function(r){angular.forEach(f.stockLabelListForUiGrid,function(s){if(!r.categoryCustom.hasOwnProperty(s.name)){r.categoryCustom[s.name]="NA"
}else{if(r.categoryCustom.hasOwnProperty(s.name)){if(r.categoryCustom[s.name]===null||r.categoryCustom[s.name]===""||r.categoryCustom[s.name]===undefined){r.categoryCustom[s.name]="NA"
}}}if(r.hasOwnProperty("commonId")){r.categoryCustom["~@commonId"]=r.commonId
}});
f.stockList.push(r.categoryCustom)
});
f.gridOptions.data=f.stockList;
f.gridOptions.columnDefs=f.stockLabelListForUiGrid;
f.listFilled=true;
f.flag.configSearchFlag=false;
window.setTimeout(function(){$(window).resize();
$(window).resize()
},100)
};
e.convertorForCustomField(f.searchedDataFromDb,q);
c.unMaskLoading()
},function(){var q="Could not retrieve, please try again.";
var p=c.error;
c.addMessage(q,p);
c.unMaskLoading()
})
}else{var n="Please select atleast one search criteria for search";
var j=c.warning;
c.addMessage(n,j);
c.unMaskLoading()
}}else{var n="Please select atleast one search criteria for search";
var j=c.warning;
c.addMessage(n,j);
c.unMaskLoading()
}}c.unMaskLoading()
};
f.printBarcode=function(){f.selectedRows=f.gridApi.selection.getSelectedRows();
if(f.selectedRows.length>=1){f.fields=[];
angular.forEach(f.selectedRows,function(i){f.fields.push(i["~@commonId"])
},function(){console.log("failure")
});
f.printBarcodeDataBean={};
f.printBarcodeDataBean.fieldIds=f.fields;
c.maskLoading();
h.prepareBarcode(f.printBarcodeDataBean,function(i){c.unMaskLoading();
if(!!i){if(i[0]==="T"){window.location.href=c.appendAuthToken(c.centerapipath+"printbarcode/downloadpdfofbarcodes");
f.onCancelOfSearch()
}}else{var k="Failed to download report";
var j=c.error;
c.addMessage(k,j)
}})
}};
c.unMaskLoading()
}])
});