define(["hkg","transactionLogService","ngload!uiGrid"],function(a){a.register.controller("TransactionLogController",["$rootScope","$scope","TransactionLogService",function(c,e,f){e.entity="TRANSACTIONLOG.";
e.transactionlog={};
e.transactionlog.fromDate=null;
e.fromdate=null;
e.transactionlog.toDate=null;
e.todate=null;
e.logRecords=[];
e.statusList=[];
e.transactionlog.status="";
e.franchiseList={};
e.isEmptyRecordSet=true;
e.transactionlog.franchise="";
e.init=true;
var g="";
e.$on("$viewContentLoaded",function(){d()
});
var d=function(){b()
};
var b=function(){e.isEmptyRecordSet=true;
f.retrievePrerequisite(function(h){e.statusList=h.status;
e.franchiseList=h.franchise;
g=h.currentFranchise
});
e.gridOptions={};
e.gridOptions.columnDefs=[];
e.gridOptions.columnDefs.push({name:"transactionId",displayName:"Transaction Id",minWidth:200});
e.gridOptions.columnDefs.push({name:"receiverJid",displayName:"Receiver",minWidth:200});
e.gridOptions.columnDefs.push({name:"sentDate",displayName:"Sent Time",cellTemplate:"<span> &nbsp; {{row.entity.sentDate | date:'yyyy-MM-dd HH:mm:ss'}}</span>",minWidth:200});
e.gridOptions.columnDefs.push({name:"noOfRetry",displayName:"Retries",minWidth:200});
e.gridOptions.columnDefs.push({name:"status",displayName:"Status",minWidth:200});
e.gridOptions.columnDefs.push({name:"errorFile",displayName:"Error File",cellTemplate:"<div><button class='btn btn-link' ng-disabled='row.entity.errorFile==null || row.entity.errorFile==undefined' ng-click='grid.appScope.clickHandler(row.entity.errorFile)'> <span class='glyphicon glyphicon-save'></span></button></div>",minWidth:200});
e.gridOptions.enableFiltering=true;
e.gridOptions.expandableRowTemplate='<div ui-grid="row.entity.subGridOptions" ui-grid-resize-columns style="height:150px;"></div>';
e.gridOptions.expandableRowScope={subGridVariable:"subGridScopeVariable"};
e.gridOptions.multiSelect=true;
e.gridOptions.enableRowSelection=true;
e.gridOptions.enableSelectAll=true;
e.gridOptions.paginationPageSizes=[10,20,30];
e.gridOptions.paginationPageSize=10;
e.gridOptions.data=[];
e.gridOptions.isRowSelectable=function(h){if(h.entity.status=="Delivered"){return false
}else{return true
}};
e.gridOptions.onRegisterApi=function(h){e.gridApi=h
}
};
e.clickHandler=function(k){var h=document.createElement("a");
e.fileName=k;
h.href="api/transactionlog/retrieve";
h.target="_blank";
h.download=k;
h.innerHTML="abc";
h.click()
};
e.selectAll=function(){e.gridApi.selection.selectAllRows()
};
e.clearAll=function(){e.gridApi.selection.clearSelectedRows()
};
e.expandAllRows=function(){e.gridApi.expandable.expandAllRows()
};
e.collapseAllRows=function(){e.gridApi.expandable.collapseAllRows()
};
e.execute=function(){var h=e.gridApi.selection.getSelectedRows();
f.resend(h,function(){f.retrieveTransactionLogsBycriteria(e.transactionlog,function(k){e.logRecords=k;
for(i=0;
i<k.length;
i++){k[i].subGridOptions={columnDefs:[{name:"className",displayName:"className"},{name:"isSqlEntity",displayName:"isSqlEntity"}],data:k[i].entityMetadataList}
}e.gridOptions.data=k
})
})
};
e.filterTransactionLog=function(){e.init=false;
if(e.fromdate){e.transactionlog.fromDate=getDateFromFormat(e.fromdate,c.dateFormatWithTime)
}if(e.todate){e.transactionlog.toDate=getDateFromFormat(e.todate,c.dateFormatWithTime)
}c.maskLoading();
f.retrieveTransactionLogsBycriteria(e.transactionlog,function(h){if(!h||h.length===0){e.isEmptyRecordSet=true
}else{e.isEmptyRecordSet=false
}e.logRecords=h;
e.gridOptions.data=h;
for(i=0;
i<h.length;
i++){h[i].subGridOptions={columnDefs:[{name:"className",displayName:"className"},{name:"idMap",displayName:"idMap"},{name:"isSqlEntity",displayName:"isSqlEntity"}],data:h[i].entityMetadataList};
if(h[i].entityMetadataList.idMap){for(j=0;
j<h[i].entityMetadataList.idMap.length;
j++){h[i].subGridOptions[j].subMapOptions={columnDefs:[{name:"id",displayName:"id"},{name:"id",displayName:"id"}],data:h[i].entityMetadataList.idMap}
}}}c.unMaskLoading();
e.gridOptions.data=h
},c.unMaskLoading())
}
}])
});