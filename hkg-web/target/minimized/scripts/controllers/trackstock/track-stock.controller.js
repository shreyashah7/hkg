define(["hkg","customFieldService","trackstockService","ruleService","activityFlowService","lotService","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicForm","accordionCollapse","ngload!flowChart"],function(a){a.register.controller("TrackStockController",["$rootScope","$scope","DynamicFormService","TrackStockService","RuleService","ActivityFlowService","CustomFieldService","LotService","$filter","$location",function(h,l,k,j,c,e,i,f,d,b){h.maskLoading();
h.mainMenu="stockLink";
h.childMenu="trackstock";
h.activateMenu();
console.log("loaded controller");
var g={};
l.initializeData=function(){l.submitted=false;
l.flag={};
l.flag.rowSelectedflag=false;
l.flag.multipleLotflag=false;
l.searchedDataFromDb=[];
l.trackStockDataBean={};
l.searchCustom={};
var m=k.retrieveSearchWiseCustomFieldInfo("stocktrack");
l.dbType={};
l.stockLabelListForUiGrid=[];
l.gridOptions={};
l.gridOptions.enableFiltering=true;
l.gridOptions.multiSelect=false;
l.gridOptions.enableRowSelection=true;
l.gridOptions.enableSelectAll=true;
l.gridOptions.columnDefs=[];
l.gridOptions.data=[];
l.gridOptions.selectedItems=[];
l.stockList=[];
l.selectedRows=[];
l.lotIds=[];
l.packetsIds=[];
l.flag.displayEditPacketflag=false;
l.flag.displayEditLotflag=false;
l.flag.trackStockflag=false;
l.gridOptions.onRegisterApi=function(n){l.gridApi=n;
n.selection.on.rowSelectionChanged(l,function(o){if(l.gridApi.selection.getSelectedRows().length>0){l.flag.rowSelectedflag=true
}else{l.flag.rowSelectedflag=false
}});
n.selection.on.rowSelectionChangedBatch(l,function(o){if(l.gridApi.selection.getSelectedRows().length>0){l.flag.rowSelectedflag=true
}else{l.flag.rowSelectedflag=false
}})
};
m.then(function(p){l.searchInvoiceTemplate=[];
l.searchParcelTemplate=[];
l.searchLotTemplate=[];
l.searchPacketTemplate=[];
l.generalSearchTemplate=p.genralSection;
if(l.generalSearchTemplate!==undefined&&l.generalSearchTemplate!==null&&l.generalSearchTemplate.length>0){for(var n=0;
n<l.generalSearchTemplate.length;
n++){var o=l.generalSearchTemplate[n];
if(o.featureName.toLowerCase()==="invoice"){l.searchInvoiceTemplate.push(angular.copy(o))
}else{if(o.featureName.toLowerCase()==="parcel"){l.searchParcelTemplate.push(angular.copy(o))
}else{if(o.featureName.toLowerCase()==="lot"){l.searchLotTemplate.push(angular.copy(o))
}else{if(o.featureName.toLowerCase()==="packet"){l.searchPacketTemplate.push(angular.copy(o))
}}}}g[o.model]=o.featureName;
if(o.fromModel){l.stockLabelListForUiGrid.push({name:o.fromModel,displayName:o.label,minWidth:200})
}else{if(o.toModel){l.stockLabelListForUiGrid.push({name:o.toModel,displayName:o.label,minWidth:200})
}else{if(o.model){l.stockLabelListForUiGrid.push({name:o.model,displayName:o.label,minWidth:200})
}}}}}l.searchResetFlag=true
},function(n){console.log("reason :"+n)
},function(n){console.log("update :"+n)
})
};
l.$on("$viewContentLoaded",function(){if(b.path()==="/trackstockprint"){l.displayActivityFlow()
}else{if(b.path()==="/totalstock"){l.retrieveVersionList()
}}});
l.initTrackStockForm=function(m){l.trackStockForm=m
};
l.retrieveSearchedData=function(){l.selectOneParameter=false;
l.searchedData=[];
l.searchedDataFromDb=[];
l.listFilled=false;
l.stockList=[];
l.gridOptions.columnDefs=[];
l.gridOptions.data=[];
if(Object.getOwnPropertyNames(l.searchCustom).length>0){h.maskLoading();
var p=false;
for(var s in l.searchCustom){if(typeof l.searchCustom[s]==="object"&&l.searchCustom[s]!==null){var q=angular.copy(l.searchCustom[s].toString());
if(typeof q==="string"&&q!==null&&q!==undefined&&q.length>0){p=true;
break
}}if(typeof l.searchCustom[s]==="string"&&l.searchCustom[s]!==null&&l.searchCustom[s]!==undefined&&l.searchCustom[s].length>0){p=true;
break
}if(typeof l.searchCustom[s]==="number"&&!!(l.searchCustom[s])){p=true;
break
}if(typeof l.searchCustom[s]==="boolean"){p=true;
break
}}if(p){l.trackStockDataBean.featureCustomMapValue={};
l.map={};
var o={};
var m=k.convertSearchData(l.searchInvoiceTemplate,l.searchParcelTemplate,l.searchLotTemplate,l.searchPacketTemplate,l.searchCustom);
angular.forEach(g,function(x,v){var w=m[v];
if(w!==undefined){var u={};
if(!o[x]){u[v]=w;
o[x]=u
}else{var t=o[x];
t[v]=w;
o[x]=t
}}else{w=m["to"+v];
if(w!==undefined){var u={};
if(!o[x]){u["to"+v]=w;
o[x]=u
}else{var t=o[x];
t["to"+v]=w;
o[x]=t
}}w=m["from"+v];
if(w!==undefined){var u={};
if(!o[x]){u["from"+v]=w;
o[x]=u
}else{var t=o[x];
t["from"+v]=w;
o[x]=t
}}}});
l.trackStockDataBean.featureCustomMapValue=o;
l.trackStockDataBean.featureMap=g;
console.log("$scope.trackStockDataBean :"+JSON.stringify(l.trackStockDataBean));
j.search(l.trackStockDataBean,function(t){console.log("result ::::"+JSON.stringify(t));
l.searchedDataFromDb=angular.copy(t);
if(l.generalSearchTemplate===null||l.generalSearchTemplate===undefined){l.flag.configSearchFlag=true
}else{var u=function(){angular.forEach(l.searchedDataFromDb,function(v){angular.forEach(l.stockLabelListForUiGrid,function(w){if(!v.categoryCustom.hasOwnProperty(w.name)){v.categoryCustom[w.name]="NA"
}else{if(v.categoryCustom.hasOwnProperty(w.name)){if(v.categoryCustom[w.name]===null||v.categoryCustom[w.name]===""||v.categoryCustom[w.name]===undefined){v.categoryCustom[w.name]="NA"
}}}if(v.hasOwnProperty("value")){v.categoryCustom["~@lotid"]=v.value
}if(v.hasOwnProperty("label")){v.categoryCustom["~@parcelid"]=v.label
}if(v.hasOwnProperty("description")){v.categoryCustom["~@invoiceid"]=v.description
}if(v.hasOwnProperty("id")){v.categoryCustom["~@packetid"]=v.id
}if(v.hasOwnProperty("status")){v.categoryCustom.status=v.status
}});
l.stockList.push(v.categoryCustom)
});
l.gridOptions.data=l.stockList;
l.gridOptions.columnDefs=l.stockLabelListForUiGrid;
l.listFilled=true;
l.flag.configSearchFlag=false;
window.setTimeout(function(){$(window).resize();
$(window).resize()
},100)
};
k.convertorForCustomField(l.searchedDataFromDb,u)
}h.unMaskLoading()
},function(){var u="Could not retrieve, please try again.";
var t=h.error;
h.addMessage(u,t);
h.unMaskLoading()
})
}else{var r="Please select atleast one search criteria for search";
var n=h.warning;
h.addMessage(r,n);
h.unMaskLoading()
}}else{var r="Please select atleast one search criteria for search";
var n=h.warning;
h.addMessage(r,n);
h.unMaskLoading()
}h.unMaskLoading()
};
l.onCancelOfSearch=function(){if(l.trackStockForm!==null){l.trackStockForm.$dirty=false
}l.listFilled=false;
l.searchResetFlag=false;
l.reset("searchCustom");
l.initializeData();
l.trackStockForm.$setPristine()
};
l.reset=function(n){if(n==="searchCustom"){l.searchCustom={};
var m=k.retrieveSearchWiseCustomFieldInfo("stocktrack");
l.dbType={};
m.then(function(o){l.generalSearchTemplate=o.genralSection;
l.searchResetFlag=true
},function(o){console.log("reason :"+o)
},function(o){console.log("update :"+o)
})
}};
l.getDateWithoutTimeStamp=function(m){if(m!==undefined){m.setHours(0);
m.setMinutes(0);
m.setSeconds(0);
m.setMilliseconds(0);
return m.getTime()
}return null
};
l.addDaysToDate=function(n){var m=new Date(l.getDateWithoutTimeStamp(h.getCurrentServerDate()));
m.setDate(m.getDate()+n);
return m.getTime()
};
l.formatDate=function(m){if(l.getDateWithoutTimeStamp(new Date(m))===l.addDaysToDate(0)){return"Today"
}else{if(l.getDateWithoutTimeStamp(new Date(m))===l.addDaysToDate(-1)){return"Yesterday"
}else{if(l.getDateWithoutTimeStamp(new Date(m))===l.addDaysToDate(1)){return"Tomorrow"
}else{return d("date")(m,h.dateFormat)
}}}};
l.trackStock=function(){l.selectedRows=l.gridApi.selection.getSelectedRows();
l.submitted=true;
l.flag.trackStockflag=true;
if(l.selectedRows.length===1){l.payload={};
l.payload.lotid=l.selectedRows[0]["~@lotid"];
l.payload.packetid=l.selectedRows[0]["~@packetid"];
var n={stock:{lotId:null},invoiceDataBean:{id:null}};
var m={};
if(l.selectedRows[0]["~@lotid"]!==undefined&&l.selectedRows[0]["~@lotid"]!==null&&(l.selectedRows[0]["~@packetid"]===undefined||l.selectedRows[0]["~@packetid"]===null)){n.stock.lotId=l.selectedRows[0]["~@lotid"];
n.invoiceDataBean.id="lot"
}else{if(l.selectedRows[0]["~@packetid"]!==undefined&&l.selectedRows[0]["~@packetid"]!==null){n.stock.lotId=l.selectedRows[0]["~@packetid"];
n.invoiceDataBean.id="packet"
}}j.retrieveLotOrPacketActivites(l.payload,function(o){l.stockTrackDetails=angular.copy(o);
localStorage.setItem("stockDts",JSON.stringify(l.stockTrackDetails))
},function(){});
h.maskLoading();
i.retrieveDesignationBasedFields("stocktrack",function(o){var s={};
s=k.retrieveSectionWiseCustomFieldInfo("invoice");
l.invoiceDbType={};
s.then(function(v){l.generaInvoiceTemplate=v.genralSection;
var u=[];
var t=Object.keys(o).map(function(w,x){angular.forEach(this[w],function(y){if(w==="Invoice#P#"){u.push({Invoice:y})
}})
},o);
l.generaInvoiceTemplate=k.retrieveCustomData(l.generaInvoiceTemplate,u);
angular.forEach(l.generaInvoiceTemplate,function(w){m[w.model]=w.featureName
})
},function(t){},function(t){});
var r={};
r=k.retrieveSectionWiseCustomFieldInfo("parcel");
l.parcelDbType={};
r.then(function(v){l.generaParcelTemplate=v.genralSection;
var u=[];
var t=Object.keys(o).map(function(w,x){angular.forEach(this[w],function(y){if(w==="Parcel#P#"){u.push({Parcel:y})
}})
},o);
l.generaParcelTemplate=k.retrieveCustomData(l.generaParcelTemplate,u);
angular.forEach(l.generaParcelTemplate,function(w){m[w.model]=w.featureName
})
},function(t){},function(t){});
var p={};
p=k.retrieveSectionWiseCustomFieldInfo("packet");
l.packetDbType={};
p.then(function(v){l.generalPacketTemplate=v.genralSection;
var u=[];
var t=Object.keys(o).map(function(w,x){angular.forEach(this[w],function(y){if(w==="Packet#P#"){u.push({Packet:y})
}})
},o);
l.generalPacketTemplate=k.retrieveCustomData(l.generalPacketTemplate,u);
angular.forEach(l.generalPacketTemplate,function(w){m[w.model]=w.featureName
})
},function(t){},function(t){});
var q={};
q=k.retrieveSectionWiseCustomFieldInfo("lot");
l.lotDbType={};
q.then(function(v){l.generalLotTemplate=v.genralSection;
var u=[];
var t=Object.keys(o).map(function(w,x){angular.forEach(this[w],function(y){if(w==="Lot#P#"){u.push({Lot:y})
}})
},o);
l.generalLotTemplate=k.retrieveCustomData(l.generalLotTemplate,u);
angular.forEach(l.generalLotTemplate,function(w){m[w.model]=w.featureName
});
n.featureMap=m;
j.retrieveStockByLotIdOrPacketId(n,function(w){console.log("result :"+JSON.stringify(w));
l.stock=w.stock;
l.invoiceCustom=l.stock.custom3;
l.parcelCustom=l.stock.custom4;
l.lotCustom=l.stock.custom1;
l.packetCustom=l.stock.custom5;
console.log("packetCustom :"+JSON.stringify(l.packetCustom));
h.unMaskLoading()
},function(){var x="Could not retrieve, please try again.";
var w=h.error;
h.addMessage(x,w);
h.unMaskLoading()
});
l.lotCustom=l.lotCustomFieldData
},function(t){},function(t){});
l.searchResetFlag=false;
l.reset("searchCustom")
},function(){h.unMaskLoading();
var p="Failed to retrieve data";
var o=h.error;
h.addMessage(p,o)
})
}};
l.onBack=function(m){if(m!==null){m.$dirty=false;
l.flag.trackStockflag=false
}l.listFilled=false;
l.initializeData();
l.submitted=false
};
l.generateReportOfActivity=function(m){l.payload={};
l.payload.lotid=l.selectedRows[0]["~@lotid"];
l.payload.packetid=l.selectedRows[0]["~@packetid"];
l.payload.extension=m;
l.generateReport(l.payload,m)
};
l.generateReport=function(m,n){h.maskLoading();
j.generateReportOfActivities(m,function(o){h.unMaskLoading();
if(!!o){if(o[0]==="T"){window.location.href=h.appendAuthToken(h.centerapipath+"trackstock/downloadpdfreportofactivity?extension="+n)
}}else{var q="Failed to download report";
var p=h.error;
h.addMessage(q,p)
}},function(){h.unMaskLoading();
var p="Failed to download report";
var o=h.error;
h.addMessage(p,o)
})
};
l.displayActivityFlow=function(){if(localStorage.getItem("stockDts")!==undefined){l.stockDts=JSON.parse(localStorage.getItem("stockDts"))
}if(l.stockDts!==undefined&&l.stockDts!==null&&l.stockDts.length>0){if(l.stockDts[l.stockDts.length-1].activityVersion!==undefined||l.stockDts[l.stockDts.length-1].activityVersion!==null){var m=l.stockDts[l.stockDts.length-1].activityVersion;
l.nodeIds=[];
angular.forEach(l.stockDts,function(n){l.nodeIds.push(n.nodeId)
});
l.send={nodeIds:l.nodeIds,versionId:m};
l.retrieveActivityFlowVersion(l.send)
}}};
l.retrieveActivityFlowVersion=function(m){chartDataModel={isreadonly:true,groups:[],connections:[]};
if(m.versionId!==null){h.maskLoading();
j.retrieveActivityFlowVersion(m,function(n){h.unMaskLoading();
if(n.data!==null&&n.data.activityFlowGroups!==null&&n.data.activityFlowGroups.length>0){angular.forEach(n.data.activityFlowGroups,function(p){if(p.nodeDataBeanList!==null){l.nodes=[];
angular.forEach(p.nodeDataBeanList,function(q){if(q.designationId){l.nodeDataModel={name:q.associatedServiceName,id:q.nodeId,serviceCode:q.associatedServiceCode,x:q.x,y:q.y,designation:q.designationId,modifier:q.modifier,nonModifierCodes:q.nonMandatoryModifier,plans:q.plans,noOfPlans:q.noOfPlans,inputConnectors:[{name:"X"}],outputConnectors:[{name:"1"}],active:q.selected}
}else{l.nodeDataModel={name:q.associatedServiceName,id:q.nodeId,serviceCode:q.associatedServiceCode,x:q.x,y:q.y,designation:q.designationId,modifier:q.modifier,nonModifierCodes:q.nonMandatoryModifier,plans:q.plans,noOfPlans:q.noOfPlans,inputConnectors:[{name:"X"}],outputConnectors:[{name:"1"}],active:q.selected}
}l.nodes.push(l.nodeDataModel)
});
var o={name:p.flowGroupName,description:p.description,id:p.groupId,x:p.x,y:p.y,nodes:[],active:p.selected};
angular.forEach(l.nodes,function(q){o.nodes.push(q)
});
chartDataModel.groups.push(o)
}else{var o={name:p.flowGroupName,description:p.description,id:p.groupId,x:p.x,y:p.y,nodes:[],active:p.selected};
chartDataModel.groups.push(o)
}});
if(n.data.activityFlowNodeRoutes!==null){angular.forEach(n.data.activityFlowNodeRoutes,function(o){var p={id:o.nodeRouteId,ruleset:o.nodeRouteStatus,source:{nodeID:o.curentNode,connectorIndex:0},dest:{nodeID:o.nextNode,connectorIndex:0},active:o.selected};
chartDataModel.connections.push(p)
})
}l.onlyViewModel=new flowchart.ChartViewModel(chartDataModel)
}else{l.chartViewModel=new flowchart.ChartViewModel(chartDataModel)
}})
}};
l.retrieveVersionList=function(){l.totalStock={};
j.retrieveVersionList(function(m){if(m.data!==undefined){l.versionList=m.data.activityflowbycompany;
l.totalStock.version=l.versionList[0].value;
l.retrieveTotalStockByVersion()
}})
};
l.retrieveTotalStockByVersion=function(){var m=l.totalStock.version;
l.totalStockDtls=[];
j.retrieveTotalStockByVersion(m,function(n){l.totalStockDtls=angular.copy(n.data)
})
};
l.onVersionChange=function(){l.retrieveTotalStockByVersion()
};
l.openStockDetails=function(m){if(m!==undefined){l.selectedNodeId=m.nodeId;
l.activityName=m.activityName;
l.serviceName=m.serviceName;
l.totalDtls=[];
j.retrieveTotalStockByNode(m.nodeId,function(n){if(n!==undefined){l.totalDtls=angular.copy(n.data);
$("#totalStockDetailsPopup").modal("show")
}})
}};
l.hideDetailsPopup=function(){l.totalDtls=[];
$("#totalStockDetailsPopup").modal("hide");
h.removeModalOpenCssAfterModalHide()
};
l.previousPage=function(){l.totalDtls=[];
l.initializeData();
b.path("/trackStock")
};
l.generateStockDetailsReport=function(m){l.payload={};
l.payload.versionId=l.totalStock.version;
l.payload.extension=m;
l.generateReportOfStockDetails(l.payload,m)
};
l.generateReportOfStockDetails=function(m,n){h.maskLoading();
j.generateStockDetailsReport(m,function(o){h.unMaskLoading();
if(!!o){if(o[0]==="T"){window.location.href=h.appendAuthToken(h.centerapipath+"trackstock/downloadpdfofstockdetails?extension="+n)
}}else{var q="Failed to download report";
var p=h.error;
h.addMessage(q,p)
}},function(){h.unMaskLoading();
var p="Failed to download report";
var o=h.error;
h.addMessage(p,o)
})
};
l.generateTotalStockDetailReport=function(m){console.log("insie");
l.payload={};
l.payload.nodeId=l.selectedNodeId;
l.payload.extension=m;
l.payload.activity=l.activityName;
l.payload.service=l.serviceName;
l.totalStockDetailReport(l.payload,m)
};
l.totalStockDetailReport=function(m,n){h.maskLoading();
j.generateTotalStockReport(m,function(o){h.unMaskLoading();
if(!!o){if(o[0]==="T"){window.location.href=h.appendAuthToken(h.centerapipath+"trackstock/downloadpdfoftotalstockdetails?extension="+n)
}}else{var q="Failed to download report";
var p=h.error;
h.addMessage(q,p)
}},function(){h.unMaskLoading();
var p="Failed to download report";
var o=h.error;
h.addMessage(p,o)
})
};
h.unMaskLoading()
}])
});