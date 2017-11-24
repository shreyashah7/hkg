define(["hkg","endOfTheDayService","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicForm","assetService","accordionCollapse"],function(b,c,a){b.register.controller("EndOfTheDayController",["$rootScope","$scope","EndOfTheDayService","DynamicFormService","AssetService","$q","$timeout",function(d,h,k,g,f,e,j){var i={};
d.mainMenu="stockLink";
d.childMenu="endofDay";
d.activateMenu();
h.fieldSequenceToDataBeanMap={"lotID$AG$String":"lotId","lotSlipID$AG$String":"lotSlipId","packetID$AG$String":"packetId","packetSlipID$AG$String":"packetSlipId"};
h.initializeData=function(m){h.recieveDataBean={};
h.recieveDataBean.barcodes=[];
h.returnDataBean={};
h.returnDataBean.barcodes=[];
h.uniqueScannedIds=[];
if(m!==true){h.selectedTab="Issue"
}h.finallotIdsOrPacketIds=[];
h.totalLotIdsOrPacketIdsOrLotSlipIdsOrPacketSlipIds=[];
h.flag={};
h.issueDataBean={};
h.issueDataBean.barcodes=[];
h.dataRetrieved=false;
h.flag.homePage=true;
h.flag.submitted=false;
h.flag.recieveSubmitted=false;
h.flag.returnSubmitted=false;
h.searchedIssueDataFromDb=[];
h.searchedRecieveDataFromDb=[];
h.searchedReturnDataFromDb=[];
h.issueListFilled=false;
h.recieveListFilled=false;
h.returnListFilled=false;
var l=g.retrieveSearchWiseCustomFieldInfo("dayEnd");
h.stockLabelListForUiGrid=[];
h.issueGridOptions={};
h.recieveGridOptions={};
h.returnGridOptions={};
h.issueBarcodeGridOptions=undefined;
h.receiveBarcodeGridOptions=undefined;
h.returnBarcodeGridOptions=undefined;
h.issueGridOptions.enableFiltering=true;
h.issueGridOptions.columnDefs=[];
h.issueGridOptions.data=[];
h.recieveGridOptions.enableFiltering=true;
h.recieveGridOptions.columnDefs=[];
h.recieveGridOptions.data=[];
h.returnGridOptions.enableFiltering=true;
h.returnGridOptions.columnDefs=[];
h.returnGridOptions.data=[];
h.scannedIds=[];
h.lotIds=[];
h.packetIds=[];
h.issueStockList=[];
h.recieveStockList=[];
h.returnStockList=[];
h.issueStockListDb=[];
h.recieveStockListDb=[];
h.returnStockListDb=[];
h.issueBarcodeList=[];
h.recieveBarcodeList=[];
h.returnBarcodeList=[];
h.issueSelectedRows=[];
h.recieveSelectedRows=[];
h.returnSelectedRows=[];
h.issueListFilled=false;
h.returnListFilled=false;
h.recieveListFilled=false;
l.then(function(n){h.dataRetrieved=true;
h.generalSearchTemplate=n.genralSection;
h.generateFeatureMap=function(q){var o=e.defer();
var p=o.promise;
p.then(function(){if(h.generalSearchTemplate!=null&&h.generalSearchTemplate.length>0){for(var r=0;
r<h.generalSearchTemplate.length;
r++){var s=h.generalSearchTemplate[r];
i[s.model]=s.featureName;
if(s.fromModel){h.stockLabelListForUiGrid.push({name:s.fromModel,displayName:s.label,minWidth:200})
}else{if(s.toModel){h.stockLabelListForUiGrid.push({name:s.toModel,displayName:s.label,minWidth:200})
}else{if(s.model){h.stockLabelListForUiGrid.push({name:s.model,displayName:s.label,minWidth:200})
}}}}}}).then(function(){q()
});
o.resolve()
};
h.generateFeatureMap(function(){if(!!h.generalSearchTemplate){d.maskLoading();
k.retrieveIssuestocks(i,function(o){h.searchedIssueDataFromDb=angular.copy(o);
var p=function(q){angular.forEach(h.searchedIssueDataFromDb,function(r){angular.forEach(h.stockLabelListForUiGrid,function(s){if(!r.categoryCustom.hasOwnProperty(s.name)){r.categoryCustom[s.name]="NA"
}else{if(r.categoryCustom.hasOwnProperty(s.name)){if(r.categoryCustom[s.name]===null||r.categoryCustom[s.name]===""||r.categoryCustom[s.name]===undefined){r.categoryCustom[s.name]="NA"
}}}if(r.hasOwnProperty("lotId")){r.categoryCustom["~@lotid"]=r.lotId
}if(r.hasOwnProperty("label")){r.categoryCustom["~@parcelid"]=r.label
}if(r.hasOwnProperty("description")){r.categoryCustom["~@invoiceid"]=r.description
}if(r.hasOwnProperty("packetId")){r.categoryCustom["~@packetid"]=r.packetId
}if(r.hasOwnProperty("status")){r.categoryCustom["~@status"]=r.status
}});
h.issueStockList.push(r.categoryCustom);
h.issueStockListDb.push(r)
});
h.issueGridOptions.data=h.issueStockList;
h.issueGridOptions.columnDefs=h.stockLabelListForUiGrid;
h.issueListFilled=true
};
g.convertorForCustomField(h.searchedIssueDataFromDb,p);
k.retrieveRecievestocks(i,function(q){h.searchedRecieveDataFromDb=angular.copy(q);
var r=function(s){angular.forEach(h.searchedRecieveDataFromDb,function(t){angular.forEach(h.stockLabelListForUiGrid,function(u){if(!t.categoryCustom.hasOwnProperty(u.name)){t.categoryCustom[u.name]="NA"
}else{if(t.categoryCustom.hasOwnProperty(u.name)){if(t.categoryCustom[u.name]===null||t.categoryCustom[u.name]===""||t.categoryCustom[u.name]===undefined){t.categoryCustom[u.name]="NA"
}}}if(t.hasOwnProperty("lotId")){t.categoryCustom["~@lotid"]=t.lotId
}if(t.hasOwnProperty("label")){t.categoryCustom["~@parcelid"]=t.label
}if(t.hasOwnProperty("description")){t.categoryCustom["~@invoiceid"]=t.description
}if(t.hasOwnProperty("packetId")){t.categoryCustom["~@packetid"]=t.packetId
}if(t.hasOwnProperty("status")){t.categoryCustom["~@status"]=t.status
}});
h.recieveStockList.push(t.categoryCustom);
h.recieveStockListDb.push(t)
});
h.recieveGridOptions.data=h.recieveStockList;
h.recieveGridOptions.columnDefs=h.stockLabelListForUiGrid;
h.recieveListFilled=true
};
g.convertorForCustomField(h.searchedRecieveDataFromDb,r);
k.retrieveReturnstocks(i,function(s){h.searchedReturnDataFromDb=angular.copy(s);
var t=function(u){angular.forEach(h.searchedReturnDataFromDb,function(v){angular.forEach(h.stockLabelListForUiGrid,function(w){if(!v.categoryCustom.hasOwnProperty(w.name)){v.categoryCustom[w.name]="NA"
}else{if(v.categoryCustom.hasOwnProperty(w.name)){if(v.categoryCustom[w.name]===null||v.categoryCustom[w.name]===""||v.categoryCustom[w.name]===undefined){v.categoryCustom[w.name]="NA"
}}}if(v.hasOwnProperty("lotId")){v.categoryCustom["~@lotid"]=v.lotId
}if(v.hasOwnProperty("label")){v.categoryCustom["~@parcelid"]=v.label
}if(v.hasOwnProperty("description")){v.categoryCustom["~@invoiceid"]=v.description
}if(v.hasOwnProperty("packetId")){v.categoryCustom["~@packetid"]=v.packetId
}if(v.hasOwnProperty("status")){v.categoryCustom["~@status"]=v.status
}});
h.returnStockList.push(v.categoryCustom);
h.returnStockListDb.push(v)
});
h.returnGridOptions.data=h.returnStockList;
h.returnGridOptions.columnDefs=h.stockLabelListForUiGrid;
h.returnListFilled=true
};
g.convertorForCustomField(h.searchedReturnDataFromDb,t);
h.tabChangeEvent(h.selectedTab);
d.unMaskLoading()
},function(s){d.unMaskLoading()
},function(s){})
},function(q){d.unMaskLoading()
},function(q){})
},function(o){d.unMaskLoading()
},function(o){})
}})
},function(n){},function(n){})
};
h.tabChangeEvent=function(l){h.flag.rowSelectedflag=false;
h.issueListFilled=false;
h.recieveListFilled=false;
h.returnListFilled=false;
if(l==="Recieve"){h.recieveListFilled=true;
if(h.recieveStockList.length>0){h.flag.rowSelectedflag=true;
h.selectedTab="Recieve"
}}else{if(l==="Return"){h.returnListFilled=true;
if(h.returnStockList.length>0){h.flag.rowSelectedflag=true;
h.selectedTab="Return"
}}else{if(l==="Issue"){h.issueListFilled=true;
if(h.issueStockList.length>0){h.flag.rowSelectedflag=true;
h.selectedTab="Issue"
}}}}if(h.flag.rowSelectedflag===true){j(function(){h.nextScreen()
})
}};
h.nextScreen=function(){h.flag.issueSecondScreen=false;
h.flag.recieveSecondScreen=false;
h.flag.returnSecondScreen=false;
if(h.selectedTab==="Issue"){h.flag.issueSecondScreen=true
}else{if(h.selectedTab==="Recieve"){h.flag.recieveSecondScreen=true
}else{if(h.selectedTab==="Return"){h.flag.returnSecondScreen=true
}}}k.retrieveFieldSequenceMap(function(l){h.fieldSequenceMap=angular.copy(l.data);
console.log("success----"+JSON.stringify(l.data))
},function(l){console.log("some error occoured")
});
if(h.issueBarcodeGridOptions===undefined){h.issueBarcodeGridOptions={};
h.issueBarcodeGridOptions.enableFiltering=true;
h.issueBarcodeGridOptions.columnDefs=angular.copy(h.stockLabelListForUiGrid);
h.issueBarcodeGridOptions.columnDefs.push({name:"Action",cellTemplate:'<div class="ui-grid-cell-contents"><span style="cursor:pointer;" class="glyphicon glyphicon-remove text-danger" ng-click="grid.appScope.showPopUp(row.entity)"></span></div>',enableFiltering:false,minWidth:200});
h.issueBarcodeGridOptions.data=[]
}if(h.receiveBarcodeGridOptions===undefined){h.receiveBarcodeGridOptions={};
h.receiveBarcodeGridOptions.enableFiltering=true;
h.receiveBarcodeGridOptions.columnDefs=angular.copy(h.stockLabelListForUiGrid);
h.receiveBarcodeGridOptions.columnDefs.push({name:"Action",cellTemplate:'<div class="ui-grid-cell-contents"><span style="cursor:pointer;" class="glyphicon glyphicon-remove text-danger" ng-click="grid.appScope.showPopUp(row.entity)"></span></div>',enableFiltering:false,minWidth:200});
h.receiveBarcodeGridOptions.data=[]
}if(h.returnBarcodeGridOptions===undefined){h.returnBarcodeGridOptions={};
h.returnBarcodeGridOptions.enableFiltering=true;
h.returnBarcodeGridOptions.columnDefs=angular.copy(h.stockLabelListForUiGrid);
h.returnBarcodeGridOptions.columnDefs.push({name:"Action",cellTemplate:'<div class="ui-grid-cell-contents"><span style="cursor:pointer;" class="glyphicon glyphicon-remove text-danger" ng-click="grid.appScope.showPopUp(row.entity)"></span></div>',enableFiltering:false,minWidth:200});
h.returnBarcodeGridOptions.data=[]
}};
h.showPopUp=function(l){h.lotObjectToDelete=l;
if(l["~@lotid"]){h.stockId=l["~@lotid"]
}else{h.stockId=l["~@packetid"]
}$("#deleteDialog").modal("show")
};
h.hidePopUp=function(){$("#deleteDialog").modal("hide");
d.removeModalOpenCssAfterModalHide()
};
h.deleteStock=function(){h.barcodeGridOptions={};
if(h.flag.issueSecondScreen===true){h.barcodeGridOptions=h.issueBarcodeGridOptions
}else{if(h.flag.recieveSecondScreen===true){h.barcodeGridOptions=h.receiveBarcodeGridOptions
}else{if(h.flag.returnSecondScreen===true){h.barcodeGridOptions=h.returnBarcodeGridOptions
}}}for(var n=0;
n<h.barcodeGridOptions.data.length;
n++){var o=h.barcodeGridOptions.data[n];
var l;
if(o["~@lotid"]){l=o["~@lotid"]
}else{l=o["~@packetid"]
}if(l===h.stockId){h.barcodeGridOptions.data.splice(n,1);
if(h.flag.issueSecondScreen===true){if(h.issueDataBean.barcodes.length>0){for(var m=0;
m<h.issueDataBean.barcodes.length;
m++){if(o["~@barcode"]===h.issueDataBean.barcodes[m]){h.issueDataBean.barcodes.splice(m,1);
break
}}}}else{if(h.flag.recieveSecondScreen===true){if(h.recieveDataBean.barcodes.length>0){for(var m=0;
m<h.recieveDataBean.barcodes.length;
m++){if(o["~@barcode"]===h.recieveDataBean.barcodes[m]){h.recieveDataBean.barcodes.splice(m,1);
break
}}}}else{if(h.flag.returnSecondScreen===true){if(h.returnDataBean.barcodes.length>0){for(var m=0;
m<h.returnDataBean.barcodes.length;
m++){if(o["~@barcode"]===h.returnDataBean.barcodes[m]){h.returnDataBean.barcodes.splice(m,1);
break
}}}}}}break
}}$("#deleteDialog").modal("hide");
d.removeModalOpenCssAfterModalHide()
};
h.autoCompleteIssueTo={multiple:true,closeOnSelect:false,maximumSelectionSize:1,placeholder:"Select",initSelection:function(l,m){},formatResult:function(l){return l.text
},formatSelection:function(l){return l.text
},query:function(n){var m=n.term;
h.names=[];
var o=function(p){h.names=[];
if(p.length!==0){h.names=p;
angular.forEach(p,function(q){h.names.push({id:q.value+":"+q.description,text:q.label})
})
}n.callback({results:h.names})
};
var l=function(){};
if(m.substring(0,2)=="@E"||m.substring(0,2)=="@e"){m=n.term.slice(2)
}k.retrieveUsersForCarrierBoysId(m.trim(),o,l)
}};
h.autoCompleteInStockOfDepartment={multiple:true,closeOnSelect:false,maximumSelectionSize:1,placeholder:"Select",initSelection:function(l,m){},formatResult:function(l){return l.text
},formatSelection:function(l){return l.text
},query:function(n){var m=n.term;
h.names=[];
var o=function(p){h.names=[];
if(p.length!==0){h.names=p;
angular.forEach(p,function(q){h.names.push({id:q.value+":"+q.description,text:q.label})
})
}n.callback({results:h.names})
};
var l=function(){};
if(m.substring(0,2)=="@D"||m.substring(0,2)=="@d"){m=n.term.slice(2)
}f.retrieveDepartmentList(m.trim(),o,l)
}};
h.issueSubmit=function(l){h.flag.submitted=true;
if(l.$valid){var m=[];
if(h.issueBarcodeGridOptions.data.length>0){angular.forEach(h.issueBarcodeGridOptions.data,function(p){var o="";
if(p["~@lotid"]){o=p["~@lotid"]
}else{o=p["~@packetid"]
}if(o.length!==0){m.push(o)
}})
}d.maskLoading();
var n={};
n.uniqueScannedIds=m;
n.carrierBoy=h.issueDataBean.carrierBoy;
n.department=h.issueDataBean.inStockOfDepartment;
k.issueLotsOrPackets(n,function(o){h.flag.homePage=true;
if(o.hasOwnProperty("success")){var q="Issue lots and packets successfully.";
var p=d.success;
h.initializeData(true);
l.$dirty=false;
d.addMessage(q,p);
d.unMaskLoading()
}else{if(o.hasOwnProperty("failure")){var q=o.failure+" are not present in current List";
var p=d.warning;
h.initializeData(true);
d.addMessage(q,p);
d.unMaskLoading()
}}},function(){var p="Could not issue lots and packets.please try after sometime";
var o=d.error;
d.addMessage(p,o);
d.unMaskLoading()
})
}};
h.recieveSubmit=function(l){h.flag.recieveSubmitted=true;
if(l.$valid){var m=[];
if(h.receiveBarcodeGridOptions.data.length>0){angular.forEach(h.receiveBarcodeGridOptions.data,function(p){var o="";
if(p["~@lotid"]){o=p["~@lotid"]
}else{o=p["~@packetid"]
}if(o.length!==0){m.push(o)
}})
}d.maskLoading();
var n={};
n.uniqueScannedIds=m;
k.recieveLotsOrPackets(n,function(o){h.flag.homePage=true;
if(o.hasOwnProperty("success")){var q="Recieve lots and packets successfully.";
var p=d.success;
h.initializeData(true);
l.$dirty=false;
d.addMessage(q,p);
d.unMaskLoading()
}else{if(o.hasOwnProperty("failure")){var q=o.failure+" are not present in current List";
var p=d.warning;
h.initializeData(true);
d.addMessage(q,p);
d.unMaskLoading()
}}},function(){var p="Could not recieve lots and packets.please try after sometime";
var o=d.error;
d.addMessage(p,o);
d.unMaskLoading()
})
}};
h.returnSubmit=function(l){h.flag.returnSubmitted=true;
if(l.$valid){var m=[];
if(h.returnBarcodeGridOptions.data.length>0){angular.forEach(h.returnBarcodeGridOptions.data,function(p){var o="";
if(p["~@lotid"]){o=p["~@lotid"]
}else{o=p["~@packetid"]
}if(o.length!==0){m.push(o)
}})
}d.maskLoading();
var n={};
n.uniqueScannedIds=m;
k.returnLotsOrPackets(n,function(o){h.flag.homePage=true;
if(o.hasOwnProperty("success")){var q="Return lots and packets successfully.";
var p=d.success;
h.initializeData(true);
l.$dirty=false;
d.addMessage(q,p);
d.unMaskLoading()
}else{if(o.hasOwnProperty("failure")){var q=o.failure+" are not present in current List";
var p=d.warning;
h.initializeData(true);
d.addMessage(q,p);
d.unMaskLoading()
}}},function(){var p="Could not return lots and packets.please try after sometime";
var o=d.error;
d.addMessage(p,o);
d.unMaskLoading()
})
}};
h.changeBarcodeFormation=function(l,t){if(l.keyCode===13){j(function(){$(l.target).eq(0).focus()
});
if(t==="issue"){if(h.issueDataBean.barcode!==undefined&&h.issueDataBean.barcode!==null&&h.issueDataBean.barcode!==""){var m="";
var q=h.issueDataBean.barcode;
var s=h.issueDataBean.barcode.split("-")[0];
angular.forEach(h.fieldSequenceMap,function(v,u){if(s.trim()===v){m=u
}});
if(h.fieldSequenceToDataBeanMap[m]!==undefined){var n=h.fieldSequenceToDataBeanMap[m];
if(h.issueDataBean.barcodes.indexOf(h.issueDataBean.barcode)===-1){var r=false;
if(h.issueStockListDb.length>0){angular.forEach(h.issueStockListDb,function(u){if(u[n]!==null&&u[n]!==undefined&&u[n]===h.issueDataBean.barcode.trim()){r=true;
u.categoryCustom["~@barcode"]=h.issueDataBean.barcode;
h.issueBarcodeList.push(u.categoryCustom)
}})
}if(!r){d.maskLoading();
var o=false;
var p=[];
h.issueStockListDb=[];
k.retrieveIssuestocks(i,function(u){h.searchedIssueDataFromDb=angular.copy(u);
var v=function(w){angular.forEach(h.searchedIssueDataFromDb,function(x){angular.forEach(h.stockLabelListForUiGrid,function(y){if(!x.categoryCustom.hasOwnProperty(y.name)){x.categoryCustom[y.name]="NA"
}else{if(x.categoryCustom.hasOwnProperty(y.name)){if(x.categoryCustom[y.name]===null||x.categoryCustom[y.name]===""||x.categoryCustom[y.name]===undefined){x.categoryCustom[y.name]="NA"
}}}if(x.hasOwnProperty("lotId")){x.categoryCustom["~@lotid"]=x.lotId
}if(x.hasOwnProperty("label")){x.categoryCustom["~@parcelid"]=x.label
}if(x.hasOwnProperty("description")){x.categoryCustom["~@invoiceid"]=x.description
}if(x.hasOwnProperty("packetId")){x.categoryCustom["~@packetid"]=x.packetId
}if(x.hasOwnProperty("status")){x.categoryCustom["~@status"]=x.status
}});
p.push(x.categoryCustom);
if(x[n]!==null&&x[n]!==undefined&&x[n]===h.issueDataBean.barcode.trim()){x.categoryCustom["~@barcode"]=h.issueDataBean.barcode;
h.issueBarcodeList.push(x.categoryCustom);
h.issueDataBean.barcodes.push(h.issueDataBean.barcode);
h.issueDataBean.barcode="";
o=true
}h.issueStockListDb.push(x)
});
h.issueStockList=angular.copy(p);
h.issueGridOptions.data=h.issueStockList;
h.issueBarcodeGridOptions.data=h.issueBarcodeList;
if(!o){d.addMessage("No stock found for "+q,d.error);
h.issueDataBean.barcode=""
}};
g.convertorForCustomField(h.searchedIssueDataFromDb,v);
d.unMaskLoading()
},function(u){d.unMaskLoading()
},function(u){})
}else{h.issueBarcodeGridOptions.data=h.issueBarcodeList;
h.issueDataBean.barcodes.push(h.issueDataBean.barcode);
h.issueDataBean.barcode=""
}}else{h.issueDataBean.barcode=""
}}else{d.addMessage("Invalid barcode "+q,d.error);
h.issueDataBean.barcode=""
}}}else{if(t==="receive"){if(h.recieveDataBean.barcode!==undefined&&h.recieveDataBean.barcode!==null&&h.recieveDataBean.barcode!==""){var m="";
var q=h.recieveDataBean.barcode;
var s=h.recieveDataBean.barcode.split("-")[0];
angular.forEach(h.fieldSequenceMap,function(v,u){if(s.trim()===v){m=u
}});
if(h.fieldSequenceToDataBeanMap[m]!==undefined){var n=h.fieldSequenceToDataBeanMap[m];
if(h.recieveDataBean.barcodes.indexOf(h.recieveDataBean.barcode)===-1){var r=false;
if(h.recieveStockListDb.length>0){angular.forEach(h.recieveStockListDb,function(u){if(u[n]!==null&&u[n]!==undefined&&u[n]===h.recieveDataBean.barcode.trim()){r=true;
u.categoryCustom["~@barcode"]=h.recieveDataBean.barcode;
h.recieveBarcodeList.push(u.categoryCustom)
}})
}if(!r){d.maskLoading();
var o=false;
var p=[];
h.recieveStockListDb=[];
k.retrieveRecievestocks(i,function(u){h.searchedRecieveDataFromDb=angular.copy(u);
var v=function(w){angular.forEach(h.searchedRecieveDataFromDb,function(x){angular.forEach(h.stockLabelListForUiGrid,function(y){if(!x.categoryCustom.hasOwnProperty(y.name)){x.categoryCustom[y.name]="NA"
}else{if(x.categoryCustom.hasOwnProperty(y.name)){if(x.categoryCustom[y.name]===null||x.categoryCustom[y.name]===""||x.categoryCustom[y.name]===undefined){x.categoryCustom[y.name]="NA"
}}}if(x.hasOwnProperty("lotId")){x.categoryCustom["~@lotid"]=x.lotId
}if(x.hasOwnProperty("label")){x.categoryCustom["~@parcelid"]=x.label
}if(x.hasOwnProperty("description")){x.categoryCustom["~@invoiceid"]=x.description
}if(x.hasOwnProperty("packetId")){x.categoryCustom["~@packetid"]=x.packetId
}if(x.hasOwnProperty("status")){x.categoryCustom["~@status"]=x.status
}});
p.push(x.categoryCustom);
if(x[n]!==null&&x[n]!==undefined&&x[n]===h.recieveDataBean.barcode.trim()){x.categoryCustom["~@barcode"]=h.recieveDataBean.barcode;
h.recieveBarcodeList.push(x.categoryCustom);
h.recieveDataBean.barcodes.push(h.recieveDataBean.barcode);
h.recieveDataBean.barcode="";
o=true
}h.recieveStockListDb.push(x)
});
h.recieveStockList=angular.copy(p);
h.recieveGridOptions.data=h.recieveStockList;
h.receiveBarcodeGridOptions.data=h.recieveBarcodeList;
if(!o){d.addMessage("No stock found for "+q,d.error);
h.recieveDataBean.barcode=""
}};
g.convertorForCustomField(h.searchedRecieveDataFromDb,v);
d.unMaskLoading()
},function(u){d.unMaskLoading()
},function(u){})
}else{h.receiveBarcodeGridOptions.data=h.recieveBarcodeList;
h.recieveDataBean.barcodes.push(h.recieveDataBean.barcode);
h.recieveDataBean.barcode=""
}}else{h.recieveDataBean.barcode=""
}}else{d.addMessage("Invalid barcode "+q,d.error);
h.recieveDataBean.barcode=""
}}}else{if(t==="return"){if(h.returnDataBean.barcode!==undefined&&h.returnDataBean.barcode!==null&&h.returnDataBean.barcode!==""){var m="";
var q=h.returnDataBean.barcode;
var s=h.returnDataBean.barcode.split("-")[0];
angular.forEach(h.fieldSequenceMap,function(v,u){if(s.trim()===v){m=u
}});
if(h.fieldSequenceToDataBeanMap[m]!==undefined){var n=h.fieldSequenceToDataBeanMap[m];
if(h.returnDataBean.barcodes.indexOf(h.returnDataBean.barcode)===-1){var r=false;
if(h.returnStockListDb.length>0){angular.forEach(h.returnStockListDb,function(u){if(u[n]!==null&&u[n]!==undefined&&u[n]===h.returnDataBean.barcode.trim()){r=true;
u.categoryCustom["~@barcode"]=h.returnDataBean.barcode;
h.returnBarcodeList.push(u.categoryCustom)
}})
}if(!r){d.maskLoading();
var o=false;
var p=[];
h.returnStockListDb=[];
k.retrieveReturnstocks(i,function(u){h.searchedIssueDataFromDb=angular.copy(u);
var v=function(w){angular.forEach(h.searchedIssueDataFromDb,function(x){angular.forEach(h.stockLabelListForUiGrid,function(y){if(!x.categoryCustom.hasOwnProperty(y.name)){x.categoryCustom[y.name]="NA"
}else{if(x.categoryCustom.hasOwnProperty(y.name)){if(x.categoryCustom[y.name]===null||x.categoryCustom[y.name]===""||x.categoryCustom[y.name]===undefined){x.categoryCustom[y.name]="NA"
}}}if(x.hasOwnProperty("lotId")){x.categoryCustom["~@lotid"]=x.lotId
}if(x.hasOwnProperty("label")){x.categoryCustom["~@parcelid"]=x.label
}if(x.hasOwnProperty("description")){x.categoryCustom["~@invoiceid"]=x.description
}if(x.hasOwnProperty("packetId")){x.categoryCustom["~@packetid"]=x.packetId
}if(x.hasOwnProperty("status")){x.categoryCustom["~@status"]=x.status
}});
p.push(x.categoryCustom);
if(x[n]!==null&&x[n]!==undefined&&x[n]===h.returnDataBean.barcode.trim()){x.categoryCustom["~@barcode"]=h.returnDataBean.barcode;
h.returnBarcodeList.push(x.categoryCustom);
h.returnDataBean.barcodes.push(h.returnDataBean.barcode);
h.returnDataBean.barcode="";
o=true
}h.returnStockListDb.push(x)
});
h.returnStockList=angular.copy(p);
h.returnGridOptions.data=h.returnStockList;
h.returnBarcodeGridOptions.data=h.returnBarcodeList;
if(!o){d.addMessage("No stock found for "+q,d.error);
h.returnDataBean.barcode=""
}};
g.convertorForCustomField(h.searchedIssueDataFromDb,v);
d.unMaskLoading()
},function(u){d.unMaskLoading()
},function(u){})
}else{h.returnBarcodeGridOptions.data=h.returnBarcodeList;
h.returnDataBean.barcodes.push(h.returnDataBean.barcode);
h.returnDataBean.barcode=""
}}else{h.returnDataBean.barcode=""
}}else{d.addMessage("Invalid barcode "+q,d.error);
h.returnDataBean.barcode=""
}}}}}l.stopPropagation();
l.preventDefault()
}};
h.onBack=function(l){if(l!=null){l.$dirty=false;
h.flag.homePage=true;
h.flag.issueSecondScreen=false;
h.flag.recieveSecondScreen=false;
h.flag.returnSecondScreen=false;
h.initializeData(true)
}}
}])
});