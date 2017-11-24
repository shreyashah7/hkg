define(["hkg","reRoutStockService","customFieldService","activityFlowService","changestausService","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicForm","accordionCollapse","assetService"],function(b,a){b.register.controller("ReRoutStockController",["$rootScope","$scope","ReRoutStockService","ActivityFlowService","DynamicFormService","CustomFieldService","ChangeStausService","$filter","AssetService",function(l,o,c,j,n,m,p,g,i){l.maskLoading();
l.mainMenu="stockLink";
l.childMenu="rerouteStock";
l.activateMenu();
var d=n.retrieveSectionWiseCustomFieldInfo("invoice");
d.then(function(q){o.invoiceCustomData=angular.copy(q.genralSection)
});
var e=n.retrieveSectionWiseCustomFieldInfo("parcel");
e.then(function(q){o.parcelCustomData=angular.copy(q.genralSection)
});
var f=n.retrieveSectionWiseCustomFieldInfo("lot");
f.then(function(q){o.lotCustomData=angular.copy(q.genralSection)
});
var h=n.retrieveSectionWiseCustomFieldInfo("packet");
h.then(function(q){o.packetCustomData=angular.copy(q.genralSection)
});
var k={};
o.initializeData=function(){o.flag={};
o.popover="<NOBR><font color='red;'>Please select Activity and Service <br/>Than use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr></table>";
o.searchResetFlag=false;
o.status={};
o.activityServiceChanged=false;
o.lotIds=[];
o.packetIds=[];
o.proposedStatus={};
o.proposedstatusList=[];
o.changedReRoutStockDataBean={};
o.reRoutStockDataBean={};
o.tempReRoutStockDataBean={};
o.submitted=false;
o.flag.statusChangeflag=false;
o.flag.rowSelectedflag=false;
o.flag.multipleLotflag=false;
o.searchedData=[];
o.searchedDataFromDb=[];
o.listFilled=false;
o.lotListTodisplay=[];
o.searchCustom={};
var q=n.retrieveSearchWiseCustomFieldInfo("stockreroute");
o.dbType={};
o.stockLabelListForUiGrid=[];
o.gridOptions={};
o.gridOptions.enableFiltering=true;
o.gridOptions.multiSelect=true;
o.gridOptions.enableRowSelection=true;
o.gridOptions.enableSelectAll=true;
o.gridOptions.columnDefs=[];
o.gridOptions.data=[];
o.gridOptions.selectedItems=[];
o.sellStockList=[];
o.stockList=[];
o.selectedRows=[];
o.flag.displayEditPacketflag=false;
o.flag.displayEditLotflag=false;
o.gridOptions.onRegisterApi=function(r){o.gridApi=r;
r.selection.on.rowSelectionChanged(o,function(s){if(o.gridApi.selection.getSelectedRows().length>0){o.flag.rowSelectedflag=true
}else{o.flag.rowSelectedflag=false
}});
r.selection.on.rowSelectionChangedBatch(o,function(s){if(o.gridApi.selection.getSelectedRows().length>0){o.flag.rowSelectedflag=true
}else{o.flag.rowSelectedflag=false
}})
};
q.then(function(t){o.searchInvoiceTemplate=[];
o.searchParcelTemplate=[];
o.searchLotTemplate=[];
o.searchPacketTemplate=[];
o.generalSearchTemplate=t.genralSection;
if(o.generalSearchTemplate!=null&&o.generalSearchTemplate.length>0){for(var r=0;
r<o.generalSearchTemplate.length;
r++){var s=o.generalSearchTemplate[r];
if(s.featureName.toLowerCase()==="invoice"){o.searchInvoiceTemplate.push(angular.copy(s))
}else{if(s.featureName.toLowerCase()==="parcel"){o.searchParcelTemplate.push(angular.copy(s))
}else{if(s.featureName.toLowerCase()==="lot"){o.searchLotTemplate.push(angular.copy(s))
}else{if(s.featureName.toLowerCase()==="packet"){o.searchPacketTemplate.push(angular.copy(s))
}}}}k[s.model]=s.featureName;
if(s.fromModel){o.stockLabelListForUiGrid.push({name:s.fromModel,displayName:s.label,minWidth:200})
}else{if(s.toModel){o.stockLabelListForUiGrid.push({name:s.toModel,displayName:s.label,minWidth:200})
}else{if(s.model){o.stockLabelListForUiGrid.push({name:s.model,displayName:s.label,minWidth:200})
}}}}}o.searchResetFlag=true;
o.stockLabelListForUiGrid.push({name:"activity",displayName:"Activity",minWidth:200});
o.stockLabelListForUiGrid.push({name:"service",displayName:"Service",minWidth:200})
},function(r){},function(r){});
j.retrievePrerequisite(function(s){if(!!s){var r=s.activityflowbycompany;
if(angular.isDefined(r[0])&&r[0]!==null){o.versionList=r[0]["custom2"]
}}})
};
o.onVersionChange=function(q){o.mapOfActivitySerice={};
o.tempReRoutStockDataBean.activity={};
j.retrieveServices(function(r){o.serviceList=r;
l.unMaskLoading();
j.retrieveDesignations(function(s){o.designationList=s.data;
j.retrieveActivityFlowVersion(q.value,function(t){angular.forEach(t.data.activityFlowGroups,function(u){o.mapOfActivitySerice[u.flowGroupName]=[];
angular.forEach(u.nodeDataBeanList,function(w){var v=g("filter")(o.serviceList,function(y){return y.id===w.associatedService
})[0];
var x=g("filter")(o.designationList,function(y){return y.value===w.designationId
})[0];
v.designation=x.value;
v.groupId=w.groupId;
v.nodeId=w.nodeId;
v.activityName=u.flowGroupName;
o.mapOfActivitySerice[u.flowGroupName].push(angular.copy(v))
})
})
})
})
})
};
o.retrieveSearchedData=function(r){o.submitted=true;
if(!!o.tempReRoutStockDataBean.service&&r.$valid){l.maskLoading();
o.stockList=[];
o.gridOptions.columnDefs=[];
o.gridOptions.data=[];
o.reRoutStockDataBean.featureCustomMapValue={};
o.reRoutStockDataBean.activityId=o.tempReRoutStockDataBean.service.groupId;
o.reRoutStockDataBean.serviceId=o.tempReRoutStockDataBean.service.nodeId;
o.map={};
var s={};
var q=n.convertSearchData(o.invoiceCustomData,o.parcelCustomData,o.lotCustomData,o.packetCustomData,angular.copy(o.searchCustom));
angular.forEach(k,function(x,v){var w=q[v];
if(w!==undefined){var u={};
if(!s[x]){u[v]=w;
s[x]=u
}else{var t=s[x];
t[v]=w;
s[x]=t
}}else{w=q["to"+v];
if(w!==undefined){var u={};
if(!s[x]){u["to"+v]=w;
s[x]=u
}else{var t=s[x];
t["to"+v]=w;
s[x]=t
}}w=q["from"+v];
if(w!==undefined){var u={};
if(!s[x]){u["from"+v]=w;
s[x]=u
}else{var t=s[x];
t["from"+v]=w;
s[x]=t
}}}});
o.reRoutStockDataBean.featureCustomMapValue=s;
o.reRoutStockDataBean.featureMap=k;
c.search(o.reRoutStockDataBean,function(t){o.searchedDataFromDb=angular.copy(t);
var u=function(v){angular.forEach(o.searchedDataFromDb,function(w){angular.forEach(o.stockLabelListForUiGrid,function(x){if(!w.categoryCustom.hasOwnProperty(x.name)){w.categoryCustom[x.name]="NA"
}else{if(w.categoryCustom.hasOwnProperty(x.name)){if(w.categoryCustom[x.name]===null||w.categoryCustom[x.name]===""||w.categoryCustom[x.name]===undefined){w.categoryCustom[x.name]="NA"
}}}if(w.hasOwnProperty("value")){w.categoryCustom["~@lotid"]=w.value
}if(w.hasOwnProperty("label")){w.categoryCustom["~@parcelid"]=w.label
}if(w.hasOwnProperty("description")){w.categoryCustom["~@invoiceid"]=w.description
}if(w.hasOwnProperty("id")){w.categoryCustom["~@packetid"]=w.id
}if(w.hasOwnProperty("status")){w.categoryCustom["~@status"]=w.status
}});
w.categoryCustom.activity=o.tempReRoutStockDataBean.service.activityName;
w.categoryCustom.service=o.tempReRoutStockDataBean.service.serviceName;
o.stockList.push(w.categoryCustom)
});
o.gridOptions.data=o.stockList;
o.gridOptions.columnDefs=o.stockLabelListForUiGrid;
o.listFilled=true;
window.setTimeout(function(){$(window).resize();
$(window).resize()
},100)
};
n.convertorForCustomField(o.searchedDataFromDb,u);
r.$dirty=false;
r.$setPristine();
l.unMaskLoading()
},function(){var u="Could not retrieve, please try again.";
var t=l.error;
l.addMessage(u,t);
l.unMaskLoading()
})
}};
o.onCanelOfSearch=function(q){if(q!=null){q.$dirty=false
}o.listFilled=false;
o.searchResetFlag=false;
o.reset("searchCustom");
o.initializeData();
q.$setPristine()
};
o.next=function(s){o.selectedRows=o.gridApi.selection.getSelectedRows();
o.submitted=true;
o.isMultipleLotsOrPackets=true;
o.generalStatus={};
o.flag.generalStatusFlag=true;
o.generaInvoiceTemplate=[];
o.generaParcelTemplate=[];
o.generalLotTemplate=[];
o.noOfLots=0;
o.noOfPackets=0;
o.reRoutStockDataBean={};
if(s.$valid){o.flag.reroutstockflag=true;
if(o.selectedRows.length>=1){o.flag.singlePacket=false;
if(o.selectedRows.length>1){o.flag.multipleLotflag=true;
if(o.selectedRows[0]["~@packetid"]!=null){o.flag.singlePacket=true
}}else{o.flag.multipleLotflag=false
}o.parentGridOptions={};
o.parentGridOptions.columnDefs=[];
o.parentGridOptions.data=[];
o.isMultipleLotsOrPackets=false;
for(var w=0;
w<o.selectedRows.length;
w++){if(o.selectedRows[w]["~@packetid"]!=null){o.packetIds.push(o.selectedRows[w]["~@packetid"])
}else{if(o.selectedRows[w]["~@packetid"]===null&&o.selectedRows[w]["~@lotid"]!=null){o.lotIds.push(o.selectedRows[w]["~@lotid"])
}}}o.payload={};
o.payload.lotids=o.lotIds;
o.payload.packetids=o.packetIds;
l.maskLoading();
var v={};
var r=[];
var u=[];
var t=[];
var q=[];
m.retrieveDesignationBasedFields("stockreroute",function(x){var y=n.retrieveSectionWiseCustomFieldInfo("invoice");
o.invoiceDbType={};
y.then(function(B){o.generaInvoiceTemplate=B.genralSection;
var A=[];
var z=Object.keys(x).map(function(D,E){angular.forEach(this[D],function(F){if(D==="Invoice#P#"){A.push({Invoice:F})
}})
},x);
o.generaInvoiceTemplate=n.retrieveCustomData(o.generaInvoiceTemplate,A);
angular.forEach(o.generaInvoiceTemplate,function(D){if(D.model){r.push(D.model);
o.parentGridOptions.columnDefs.push({name:D.model,displayName:D.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}});
if(r.length>0){v.invoiceDbFieldName=r
}var C=n.retrieveSectionWiseCustomFieldInfo("parcel");
o.parcelDbType={};
C.then(function(G){o.generaParcelTemplate=G.genralSection;
var F=[];
var D=Object.keys(x).map(function(H,I){angular.forEach(this[H],function(J){if(H==="Parcel#P#"){F.push({Parcel:J})
}})
},x);
o.generaParcelTemplate=n.retrieveCustomData(o.generaParcelTemplate,F);
angular.forEach(o.generaParcelTemplate,function(H){if(H.model){u.push(H.model);
o.parentGridOptions.columnDefs.push({name:H.model,displayName:H.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}});
if(u.length>0){v.parcelDbFieldName=u
}var E=n.retrieveSectionWiseCustomFieldInfo("packet");
o.packetDbType={};
E.then(function(K){o.generaPacketTemplate=K.genralSection;
var I=[];
var H=Object.keys(x).map(function(L,M){angular.forEach(this[L],function(N){if(L==="Packet#P#"){I.push({Packet:N})
}})
},x);
o.generaPacketTemplate=n.retrieveCustomData(o.generaPacketTemplate,I);
angular.forEach(o.generaPacketTemplate,function(L){if(L.model){q.push(L.model);
o.parentGridOptions.columnDefs.push({name:L.model,displayName:L.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}});
if(q.length>0){v.packetDbFieldName=q
}var J=n.retrieveSectionWiseCustomFieldInfo("lot");
o.lotDbType={};
J.then(function(O){o.generalLotTemplate=O.genralSection;
var M=[];
var L=Object.keys(x).map(function(P,Q){angular.forEach(this[P],function(R){if(P==="Lot#P#"){M.push({Lot:R})
}})
},x);
o.generalLotTemplate=n.retrieveCustomData(o.generalLotTemplate,M);
angular.forEach(o.generalLotTemplate,function(P){if(P.model){t.push(P.model);
o.parentGridOptions.columnDefs.push({name:P.model,displayName:P.label,cellTemplate:'<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>',minWidth:200})
}});
if(t.length>0){v.lotDbFieldName=t
}var N={payload:o.payload,fields:v};
p.retrieveStockByLotIdOrPacketId(N,function(P){o.stocks=P;
if(o.flag.multipleLotflag===false){var Q=o.stocks[0];
o.invoiceCustom=Q.stock.custom3;
o.parcelCustom=Q.stock.custom4;
o.lotCustom=Q.stock.custom1;
o.packetCustom=Q.stock.custom5
}else{if(o.stocks.length>1&&o.flag.multipleLotflag===true){o.gridRecords=[];
angular.forEach(o.stocks,function(S){var T={};
if(S.stock.custom1!==undefined&&S.stock.custom1!==null){T=angular.copy(S.stock.custom1)
}if(S.stock.custom3!==undefined&&S.stock.custom3!==null){angular.forEach(S.stock.custom3,function(V,U){T[U]=V
})
}if(S.stock.custom4!==undefined&&S.stock.custom4!==null){angular.forEach(S.stock.custom4,function(V,U){T[U]=V
})
}if(S.stock.custom5!==undefined&&S.stock.custom5!==null){angular.forEach(S.stock.custom5,function(V,U){T[U]=V
})
}o.gridRecords.push({categoryCustom:T})
});
var R=function(S){angular.forEach(o.gridRecords,function(T){o.parentGridOptions.data.push(T.categoryCustom)
})
};
n.convertorForCustomField(o.gridRecords,R)
}}})
},function(L){},function(L){})
},function(H){},function(H){})
},function(D){},function(D){})
},function(z){},function(z){});
l.unMaskLoading()
},function(){l.unMaskLoading();
var y="Failed to retrieve data";
var x=l.error;
l.addMessage(y,x)
})
}o.searchResetFlag=false;
o.reset("searchCustom")
}};
o.onBack=function(q){if(q!=null){q.$dirty=false;
q.$setPristine();
o.flag.reroutstockflag=false
}o.initializeData();
o.submitted=false
};
o.stockReRout=function(s){o.changedsubmitted=true;
if(s.$valid){l.maskLoading();
o.flag.reroutstockflag=false;
var q=[];
var r={};
r.activityId=o.changedReRoutStockDataBean.service.groupId;
r.serviceId=o.changedReRoutStockDataBean.service.nodeId;
r.allotTo=o.changedReRoutStockDataBean.allotTo;
r.lotIds=o.lotIds;
r.packetIds=o.packetIds;
c.stockReRout(r,function(t){var v="Stock Re-Routed Succesfully.";
var u=l.success;
o.initializeData();
s.$dirty=false;
s.$setPristine();
l.addMessage(v,u);
l.unMaskLoading()
},function(){var u="Could not re-route stock, please try again.";
var t=l.error;
o.initializeData();
l.addMessage(u,t);
l.unMaskLoading()
})
}};
o.userSingleselectComponent={multiple:true,closeOnSelect:false,placeholder:"Select",maximumSelectionSize:1,initSelection:function(q,s){var r=[];
s(r)
},formatResult:function(q){return q.text
},formatSelection:function(q){return q.text
},query:function(t){var s=t.term;
o.allotTo=[];
var u=function(v){if(v.length!==0){angular.forEach(v,function(w){o.allotTo.push({id:w.value+":"+w.description,text:w.label})
})
}t.callback({results:o.allotTo})
};
var q=function(){};
if(s.substring(0,2)==="@E"||s.substring(0,2)==="@e"){var r=t.term.slice(2);
if(!!o.changedReRoutStockDataBean&&!!o.changedReRoutStockDataBean.service&&!!o.changedReRoutStockDataBean.service.nodeId){c.retrieveAllotToByActivityFlowNodeId(o.changedReRoutStockDataBean.service.nodeId,u,q)
}}}};
o.userSingleselectComponentForInStockOf={multiple:true,closeOnSelect:false,placeholder:"Select",maximumSelectionSize:1,initSelection:function(q,s){var r=[];
s(r)
},formatResult:function(q){return q.text
},formatSelection:function(q){return q.text
},query:function(t){var s=t.term;
o.inStockOf=[];
var u=function(v){if(v.length!==0){angular.forEach(v,function(w){o.inStockOf.push({id:w.value+":"+w.description,text:w.label})
})
}t.callback({results:o.inStockOf})
};
var q=function(){};
if(s.substring(0,2)==="@E"||s.substring(0,2)==="@e"){var r=t.term.slice(2);
if(!!o.changedReRoutStockDataBean&&!!o.changedReRoutStockDataBean.service&&!!o.changedReRoutStockDataBean.service.nodeId){i.retrieveUserList(r.trim(),u,q)
}}}};
o.removeAllotTo=function(){o.changedReRoutStockDataBean.allotTo={};
o.allotTo=[];
$("#allotTo").select2("data",[])
};
o.reset=function(r){if(r==="searchCustom"){o.searchCustom={};
var q=n.retrieveSearchWiseCustomFieldInfo("stockreroute");
o.dbType={};
q.then(function(s){o.generalSearchTemplate=s.genralSection;
o.searchResetFlag=true
},function(s){},function(s){})
}};
l.unMaskLoading()
}])
});