define(["hkg","customFieldService","allotmentService","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicForm","datepickercustom.directive","accordionCollapse"],function(a){a.register.controller("AllotmentController",["$rootScope","$scope","DynamicFormService","AllotmentService","$timeout",function(b,e,d,c,g){b.maskLoading();
b.mainMenu="stockLink";
b.childMenu="allotmentMenu";
b.activateMenu();
var f={};
e.popover="<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr> </table> ";
e.initializeData=function(){e.flag={};
e.allotmentDataBean={};
e.searchResetFlag=false;
e.flag.rowSelectedflag=false;
e.searchedData=[];
e.searchedDataFromDb=[];
e.listFilled=false;
e.flag.allotflag=false;
e.searchCustom={};
var h=d.retrieveSearchWiseCustomFieldInfo("allotment");
e.dbType={};
e.stockLabelListForUiGrid=[];
e.gridOptions={};
e.gridOptions.enableFiltering=true;
e.gridOptions.multiSelect=true;
e.gridOptions.enableRowSelection=true;
e.gridOptions.enableSelectAll=true;
e.gridOptions.columnDefs=[];
e.gridOptions.data=[];
e.gridOptions.selectedItems=[];
e.manualAllocation={};
e.gridOptions.onRegisterApi=function(i){e.gridApi=i;
i.selection.on.rowSelectionChanged(e,function(j){if(e.gridApi.selection.getSelectedRows().length>0){e.flag.rowSelectedflag=true
}else{e.flag.rowSelectedflag=false
}});
i.selection.on.rowSelectionChangedBatch(e,function(j){if(e.gridApi.selection.getSelectedRows().length>0){e.flag.rowSelectedflag=true
}else{e.flag.rowSelectedflag=false
}})
};
h.then(function(l){e.searchInvoiceTemplate=[];
e.searchParcelTemplate=[];
e.searchLotTemplate=[];
e.searchPacketTemplate=[];
e.generalSearchTemplate=l.genralSection;
if(e.generalSearchTemplate!==undefined&&e.generalSearchTemplate!==null&&e.generalSearchTemplate.length>0){e.flag.configSearchFlag=false;
for(var j=0;
j<e.generalSearchTemplate.length;
j++){var k=e.generalSearchTemplate[j];
if(k.featureName.toLowerCase()==="invoice"){e.searchInvoiceTemplate.push(angular.copy(k))
}else{if(k.featureName.toLowerCase()==="parcel"){e.searchParcelTemplate.push(angular.copy(k))
}else{if(k.featureName.toLowerCase()==="lot"){e.searchLotTemplate.push(angular.copy(k))
}else{if(k.featureName.toLowerCase()==="packet"){e.searchPacketTemplate.push(angular.copy(k))
}}}}f[k.model]=k.featureName;
e.stockLabelListForUiGrid.push({name:"packetID$AG$String",displayName:"Packet ID",minWidth:200});
e.stockLabelListForUiGrid.push({name:"lotID$AG$String",displayName:"Lot ID",minWidth:200});
if(k.fromModel){e.stockLabelListForUiGrid.push({name:k.fromModel,displayName:k.label,minWidth:200})
}else{if(k.toModel){e.stockLabelListForUiGrid.push({name:k.toModel,displayName:k.label,minWidth:200})
}else{if(k.model){e.stockLabelListForUiGrid.push({name:k.model,displayName:k.label,minWidth:200})
}}}}}else{e.flag.configSearchFlag=true
}e.searchResetFlag=true
},function(i){},function(i){})
};
e.retrieveSearchedData=function(){e.selectOneParameter=false;
e.searchedData=[];
e.searchedDataFromDb=[];
e.listFilled=false;
e.stockList=[];
e.submitted=true;
e.gridOptions.columnDefs=[];
e.gridOptions.data=[];
if(e.allotmentForm.$valid){if(Object.getOwnPropertyNames(e.searchCustom).length>0){b.maskLoading();
var k=false;
for(var n in e.searchCustom){if(typeof e.searchCustom[n]==="object"&&e.searchCustom[n]!==null){var l=angular.copy(e.searchCustom[n].toString());
if(typeof l==="string"&&l!==null&&l!==undefined&&l.length>0){k=true;
break
}}if(typeof e.searchCustom[n]==="string"&&e.searchCustom[n]!==null&&e.searchCustom[n]!==undefined&&e.searchCustom[n].length>0){k=true;
break
}if(typeof e.searchCustom[n]==="number"&&!!(e.searchCustom[n])){k=true;
break
}if(typeof e.searchCustom[n]==="boolean"){k=true;
break
}}if(k){e.allotmentDataBean.featureCustomMapValue={};
e.map={};
var j={};
var h=d.convertSearchData(e.searchInvoiceTemplate,e.searchParcelTemplate,e.searchLotTemplate,e.searchPacketTemplate,e.searchCustom);
angular.forEach(f,function(s,q){var r=h[q];
if(r!==undefined){var p={};
if(!j[s]){p[q]=r;
j[s]=p
}else{var o=j[s];
o[q]=r;
j[s]=o
}}else{r=h["to"+q];
if(r!==undefined){var p={};
if(!j[s]){p["to"+q]=r;
j[s]=p
}else{var o=j[s];
o["to"+q]=r;
j[s]=o
}}r=h["from"+q];
if(r!==undefined){var p={};
if(!j[s]){p["from"+q]=r;
j[s]=p
}else{var o=j[s];
o["from"+q]=r;
j[s]=o
}}}});
e.allotmentDataBean.featureCustomMapValue=j;
e.allotmentDataBean.stockCustom=f;
c.retrieveSearchedData(e.allotmentDataBean,function(o){console.log("res :"+JSON.stringify(o));
e.searchedDataFromDb=angular.copy(o);
var p=function(){e.stockLabelListForUiGrid.push({name:"status",displayName:"Status",minWidth:200});
angular.forEach(e.searchedDataFromDb,function(q){angular.forEach(e.stockLabelListForUiGrid,function(r){if(!q.categoryCustom.hasOwnProperty(r.name)){q.categoryCustom[r.name]="NA"
}else{if(q.categoryCustom.hasOwnProperty(r.name)){if(q.categoryCustom[r.name]===null||q.categoryCustom[r.name]===""||q.categoryCustom[r.name]===undefined){q.categoryCustom[r.name]="NA"
}}}if(q.hasOwnProperty("value")){q.categoryCustom["~@packetid"]=q.value
}if(q.hasOwnProperty("label")){q.categoryCustom["~@lotid"]=q.label
}if(q.hasOwnProperty("description")){q.categoryCustom["~@parcelid"]=q.description
}if(q.hasOwnProperty("id")){q.categoryCustom["~@invoiceid"]=q.id
}});
e.stockList.push(q.categoryCustom)
});
e.gridOptions.data=e.stockList;
e.gridOptions.columnDefs=e.stockLabelListForUiGrid;
e.gridOptions.isRowSelectable=function(q){console.log(q);
if(q.entity.status=="Already Alloted"){return false
}else{return true
}};
e.listFilled=true;
e.flag.configSearchFlag=false;
window.setTimeout(function(){$(window).resize();
$(window).resize()
},100)
};
d.convertorForCustomField(e.searchedDataFromDb,p);
b.unMaskLoading()
},function(){var p="Could not retrieve, please try again.";
var o=b.error;
b.addMessage(p,o);
b.unMaskLoading()
})
}else{var m="Please select atleast one search criteria for search";
var i=b.warning;
b.addMessage(m,i);
b.unMaskLoading()
}}else{var m="Please select atleast one search criteria for search";
var i=b.warning;
b.addMessage(m,i);
b.unMaskLoading()
}}b.unMaskLoading()
};
e.clearForm=function(h){e.allotSubmitted=false;
h.$setPristine()
};
e.changeUser=function(j,h){if(j&&!(j instanceof Array)){var i=j.split(":")[0];
if(i){c.retrieveUserGradeSuggestionByUserId(i,function(l){if(l&&l!==null){var m=e.userGradeSuggestions[h];
var n=-1;
for(var k=e.userGradeSuggestions.length-1;
k>=0;
k--){if(e.userGradeSuggestions[k].userId==i){e.userGradeSuggestions[k].newStock=e.userGradeSuggestions[k].newStock+1;
m.newStock=e.userGradeSuggestions[k].newStock+1;
n=h
}if(e.userGradeSuggestions[k].userId==m.userId){e.userGradeSuggestions[k].newStock=e.userGradeSuggestions[k].newStock-1
}}if(n===-1){m.newStock=1
}m.userId=i;
m.userName=l.data.userName;
m.grade=l.data.grade;
m.goingToGrade=l.data.goingToGrade;
m.gradeName=l.data.gradeName;
m.goingToGradeName=l.data.goingToGradeName;
e.userGradeSuggestions[h]=m
}})
}}};
e.retrieveUserDetails=function(h){if(h&&!(h instanceof Array)){var i=h.split(":")[0];
if(i){c.retrieveUserGradeSuggestionByUserId(i,function(j){if(j&&j!==null){var l=e.manualAllocation.packetObjectId;
var k=e.manualAllocation.userValue;
e.manualAllocation=j.data;
e.manualAllocation.userValue=k;
e.manualAllocation.packetObjectId=l;
e.manualAllocation.newStock=1;
e.manualAllocation.totalStock=1+e.manualAllocation.currentStock;
angular.forEach(e.packetList,function(m){if(m.value==l){e.manualAllocation.packetId=angular.copy(m.label)
}})
}})
}}};
e.openAttendance=function(h){e.userDetails=angular.copy(h);
$("#attendanceModal").modal("show")
};
e.hideAttendancePopUp=function(){$("#attendanceModal").modal("hide");
b.removeModalOpenCssAfterModalHide()
};
e.allotPacket=function(){e.allotSubmitted=false;
e.selectedRows=e.gridApi.selection.getSelectedRows();
if(e.selectedRows.length>0){e.flag.allotflag=true;
e.packetIds=[];
angular.forEach(e.selectedRows,function(h){e.packetIds.push(h["~@packetid"])
});
if(e.packetIds.length>0){c.retrieveUserGradeSuggestion(e.packetIds,function(h){if(h!=undefined&&h.length>0){e.userGradeSuggestions=angular.copy(h);
angular.forEach(e.userGradeSuggestions,function(i){i.userValue=i.userId+":E";
i.select2Config={multiple:true,closeOnSelect:false,allowClear:true,placeholder:"Select Employees",maximumSelectionSize:1,initSelection:function(j,l){if(i.userId!==null&&i.userName!==null){var k=[];
k.push({id:i.userId+":E",text:i.userName});
l(k)
}},formatResult:function(j){return j.text
},formatSelection:function(j){return j.text
},query:function(l){var k=l.term;
e.names=[];
if(k.substring(0,2)=="@E"||k.substring(0,2)=="@e"){var j=l.term.slice(2);
c.retrieveUsers(j.trim(),function(m){if(m.length==0){e.names.push({id:i.userId+":E",text:i.userName});
l.callback({results:e.names})
}else{e.names=m.data;
angular.forEach(m.data,function(n){e.names.push({id:n.value+":E",text:n.label})
});
l.callback({results:e.names})
}},function(){console.log("failure")
})
}else{l.callback({results:e.names})
}}}
})
}e.retrievePacketsAvailableInStock()
})
}}};
e.reset=function(i){if(i==="searchCustom"){e.searchCustom={};
var h=d.retrieveSearchWiseCustomFieldInfo("allotment");
e.dbType={};
h.then(function(j){e.generalSearchTemplate=j.genralSection;
e.searchResetFlag=true
},function(j){console.log("reason :"+j)
},function(j){console.log("update :"+j)
})
}};
e.onCancelOfSearch=function(){if(e.allotmentForm!==null){e.allotmentForm.$dirty=false;
e.listFilled=false;
e.searchResetFlag=false;
e.reset("searchCustom");
e.initializeData();
e.allotmentForm.$setPristine()
}};
e.selectedTableRows=[];
e.removePacketViewRow=function(h){e.index=-1;
var i=e.userGradeSuggestions[h];
angular.forEach(e.userGradeSuggestions,function(j){if(j.userId===i.userId){j.newStock=j.newStock-1
}});
e.userGradeSuggestions.splice(h,1);
e.retrievePacketsAvailableInStock()
};
e.removeEmployeeViewRow=function(j){for(var h=e.userGradeSuggestions.length-1;
h>=0;
h--){if(e.userGradeSuggestions[h].userId===j){e.userGradeSuggestions.splice(h,1)
}}e.retrievePacketsAvailableInStock()
};
e.backToHomeScreen=function(){e.flag.allotflag=false;
e.userGradeSuggestions=[];
e.onCancelOfSearch()
};
e.initAllotmentForm=function(h){e.allotmentForm=h
};
e.retrievePacketsAvailableInStock=function(){c.retrievePacketsAvailableInStock(function(h){if(h&&h!==null&&h.data){e.packetList=angular.copy(h.data);
angular.forEach(e.userGradeSuggestions,function(k){for(var j=e.packetList.length-1;
j>=0;
j--){if(e.packetList[j].value==k.packetObjectId){e.packetList.splice(j,1)
}}})
}})
};
e.allotment=function(){console.log("$scope.userGradeSuggestions :"+JSON.stringify(e.userGradeSuggestions));
c.allotPacket(e.userGradeSuggestions,function(h){console.log("success");
e.backToHomeScreen()
})
};
e.addManualAllocation=function(j){e.allotSubmitted=true;
if(j.$valid){if(e.manualAllocation!=undefined&&e.manualAllocation!=null){e.manualAllocation.userValue=e.manualAllocation.userId+":E";
e.manualAllocation.select2Config={multiple:true,closeOnSelect:false,allowClear:true,placeholder:"Select Employees",maximumSelectionSize:1,initSelection:function(i,m){if(e.manualAllocation.userId!==null&&e.manualAllocation.userName!==null){var l=[];
l.push({id:e.manualAllocation.userId+":E",text:e.manualAllocation.userName});
m(l)
}},formatResult:function(i){return i.text
},formatSelection:function(i){return i.text
},query:function(m){var l=m.term;
e.names=[];
if(l.substring(0,2)=="@E"||l.substring(0,2)=="@e"){var i=m.term.slice(2);
c.retrieveUsers(i.trim(),function(n){if(n.length==0){e.names.push({id:e.manualAllocation.userId+":E",text:e.manualAllocation.userName});
m.callback({results:e.names})
}else{e.names=n.data;
angular.forEach(n.data,function(o){e.names.push({id:o.value+":E",text:o.label})
});
m.callback({results:e.names})
}},function(){console.log("failure")
})
}else{m.callback({results:e.names})
}}};
var k=-1;
for(var h=e.userGradeSuggestions.length-1;
h>=0;
h--){if(e.userGradeSuggestions[h].userId==e.manualAllocation.userId){e.userGradeSuggestions[h].newStock=e.userGradeSuggestions[h].newStock+1;
e.manualAllocation.newStock=e.userGradeSuggestions[h].newStock+1;
k=h
}}if(k===-1){e.manualAllocation.newStock=1
}e.userGradeSuggestions.push(angular.copy(e.manualAllocation));
g(function(){e.manualAllocation={};
$("#manualPacket").select2("data","");
$("#manualUser").select2("data","");
e.allotSubmitted=false;
j.$setPristine()
},500);
e.retrievePacketsAvailableInStock()
}}};
a.register.filter("unique",function(){return function(h,j){if(j===false){return h
}if((j||angular.isUndefined(j))&&angular.isArray(h)){var i=[];
var k=function(l){if(angular.isObject(l)&&angular.isString(j)){return l[j]
}else{return l
}};
angular.forEach(h,function(n){var l=false;
for(var m=0;
m<i.length;
m++){if(angular.equals(k(i[m]),k(n))){l=true;
break
}}if(!l){i.push(n)
}});
h=i
}return h
}
});
e.autoCompleteUser={multiple:true,closeOnSelect:false,allowClear:true,placeholder:"Select Employees",maximumSelectionSize:1,initSelection:function(h,i){},formatResult:function(h){return h.text
},formatSelection:function(h){return h.text
},query:function(j){var i=j.term;
e.names=[];
if(i.substring(0,2)=="@E"||i.substring(0,2)=="@e"){var h=j.term.slice(2);
c.retrieveUsers(h.trim(),function(k){if(k.data.length>0){e.names=k.data;
angular.forEach(k.data,function(l){e.names.push({id:l.value+":E",text:l.label})
});
j.callback({results:e.names})
}},function(){console.log("failure")
})
}else{j.callback({results:e.names})
}}};
e.autoCompletePackets={allowClear:true,multiple:true,closeOnSelect:false,placeholder:"Select Packet",maximumSelectionSize:1,initSelection:function(h,i){},formatResult:function(h){return h.text
},formatSelection:function(h){return h.text
},query:function(h){e.names=[];
if(e.packetList.length!==0){angular.forEach(e.packetList,function(i){e.names.push({id:i.value,text:i.label})
})
}h.callback({results:e.names})
}};
b.unMaskLoading()
}])
});