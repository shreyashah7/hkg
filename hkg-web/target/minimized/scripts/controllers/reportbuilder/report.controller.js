define(["hkg","reportBuilderService","messageService","franchiseService","ngload!uiGrid","colorpicker.directive"],function(a){a.register.controller("ManageReport",["$rootScope","$scope","$location","$filter","$timeout","$filter","ReportBuilderService","FranchiseService","Messaging",function(n,p,i,d,e,m,h,k,l){n.maskLoading();
p.entity="REPORTBUILDER.";
n.mainMenu="manageLink";
if(i.path()==="/managereport"){n.childMenu="manageReports";
p.isReportBuilder=true;
p.reportCurrentPage=1
}else{n.childMenu="reports";
p.isReportBuilder=false
}n.activateMenu();
var g=[];
p.subbmited=false;
p.isPreview=false;
p.dt={};
p.isFilter=true;
p.isAnyColumnHidden=false;
p.hiddenFieldDetail=[];
p.displayName="";
p.isHKAdmin=false;
p.displayLocalData=[];
p.isFranchiseChange=false;
p.isAnyRecordsExist=false;
p.levelMap=[];
p.totalLevel=0;
p.models={};
p.columnOrderMap={};
p.currentColor={};
p.colorConfigData=[];
p.colorConfig={};
p.isEditModeColorInitialized=false;
p.currencyMap={};
p.open=function(q,s,r){q.preventDefault();
q.stopPropagation();
r[s]=true
};
p.dateOptions={"year-format":"'yy'","starting-day":1};
p.format=n.dateFormat;
p.today=new Date();
p.paginationOptions={pageNumber:1,pageSize:50,sortDirection:null,sortColumn:null,sortColumnType:null};
p.filterOptions=[];
p.gridOptions={};
p.gridOptions.enableFiltering=true;
p.gridOptions.useExternalFiltering=true;
p.gridOptions.paginationPageSizes=[50,100];
p.gridOptions.paginationPageSize=50;
p.gridOptions.useExternalPagination=true;
p.gridOptions.useExternalSorting=true;
p.gridOptions.rowTemplate='<div ng-style="{ \'background-color\': grid.appScope.rowFormatter(row) }">  <div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }"  ui-grid-cell></div></div>';
p.gridOptions.columnDefs=[];
p.gridOptions.data=[];
p.gridOptions.onRegisterApi=function(q){p.gridApi=q;
p.gridApi.core.on.sortChanged(p,function(s,r){if(r.length===0){p.paginationOptions.sortDirection=null;
p.paginationOptions.sortColumn=null;
p.paginationOptions.sortColumnType=null
}else{p.paginationOptions.sortDirection=r[0].sort.direction;
p.paginationOptions.sortColumn=r[0].field;
p.paginationOptions.sortColumnType=r[0].colDef.type
}p.getPage()
});
p.gridApi.core.on.filterChanged(p,function(){var r=this.grid;
if(r.columns.length>0){p.filterOptions=[];
angular.forEach(r.columns,function(s){if(s.filters[0].term!==undefined&&s.filters[0].term!==null&&s.filters[0].term!==""){var t={};
t.filterColumn=s.field;
t.filterColumnType=s.colDef.type;
t.filterValue=s.filters[0].term;
p.filterOptions.push(t)
}});
p.getPage()
}});
q.pagination.on.paginationChanged(p,function(s,r){p.paginationOptions.pageNumber=s;
p.paginationOptions.pageSize=r;
p.getPage()
})
};
p.getPage=function(){p.send={offSet:p.paginationOptions.pageNumber,limit:p.paginationOptions.pageSize,isFilter:p.isFilter,isGrouped:p.isGroupByCheck,sortColumn:p.paginationOptions.sortColumn,sortDirection:p.paginationOptions.sortDirection,sortColumnType:p.paginationOptions.sortColumnType,filterOptions:p.filterOptions};
h.retrievePaginatedData(p.send,function(q){if(q.data!==undefined){if(q.data.queryValid!==undefined){var s="Invalid Query.";
var r=n.failure;
n.addMessage(s,r)
}else{p.previewData=q.data.records;
p.totalItems=(q.data.totalRecords);
p.hiddenFieldDetail=[];
p.gridOptions.totalItems=p.totalItems;
if(!p.isGroupByCheck){p.UIPreviewData=angular.copy(p.previewData);
p.gridColumnDef=[];
angular.forEach(p.columns,function(y){y.total=undefined;
if(y.showTotal!==undefined&&y.showTotal===true){var x=0;
angular.forEach(p.previewData,function(A){var z=A[y.alias]===undefined?(A[y.fieldLabel]===undefined?0:A[y.fieldLabel]):A[y.alias];
x+=Number(z)
});
if(isNaN(x)){x=0
}y.total=x
}if(y.isHide===true){var v=(y.alias===undefined||y.alias==="")?y.fieldLabel:y.alias;
var u=[];
angular.forEach(p.previewData,function(B){var z=(B[v]===undefined||B[v]===null)?"N/A":B[v];
var A=false;
angular.forEach(u,function(C){if(C===z){A=true
}});
if(!A){u.push(z)
}});
var t="";
angular.forEach(u,function(z){if(t.length===0){t=z
}else{t+=","+z
}});
p.hiddenFieldDetail.push({label:v,value:t})
}if(y.componentType==="Currency"){if(p.currencyMap[y.associatedCurrency]!==undefined){y.currencySymbol=p.currencyMap[y.associatedCurrency].symbol;
y.format=p.currencyMap[y.associatedCurrency].format
}}var w;
switch(y.dataType){case"int8":w="number";
break;
case"varchar":w="string";
break;
case"timestamp":w="date";
break;
case"boolean":w="boolean";
break;
case"double precision":w="double";
break;
default:w="object";
break
}if(y.isHide!==true){p.gridColumnDef.push({name:y.alias,displayName:y.alias,type:w,enableHiding:false,cellTemplate:"<div class=\"ui-grid-cell-contents\" title=\"{{(COL_FIELD === null || COL_FIELD === '') ? 'N/A':COL_FIELD}}\">{{(COL_FIELD === null || COL_FIELD === \"\") ? 'N/A':COL_FIELD}}</div>",minWidth:200})
}})
}if(p.isGroupByCheck){p.groupRecordsForPreview(p.previewData)
}p.gridOptions.columnDefs=p.gridColumnDef;
p.gridOptions.data=p.UIPreviewData;
if(p.isFranchiseChange&&p.isFilter){p.applyFilter(p.filterForm,true);
p.isFranchiseChange=false
}}}},function(){n.addMessage("Some error occurred while retrieving data, try different fields and criteria",n.failure)
})
};
p.$on("$viewContentLoaded",function(){e(function(){p.initializeFranchise()
},0)
});
p.availabeFilterTypesByDataType={int8:[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"},{id:"<",text:"less than"},{id:">",text:"greater than"},{id:"<=",text:"less than equal to"},{id:">=",text:"greater than equal to"},{id:"between",text:"in between"},{id:"is not null",text:"has any value"},{id:"is null",text:"has no value"}],varchar:[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"},{id:"in",text:"in"},{id:"not in",text:"not in"},{id:"like",text:"like"},{id:"is not null",text:"has any value"},{id:"is null",text:"has no value"}],timestamp:[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"},{id:"<",text:"less than"},{id:">",text:"greater than"},{id:"between",text:"in between"},{id:"is not null",text:"has any value"},{id:"is null",text:"has no value"}],"double precision":[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"},{id:"<",text:"less than"},{id:">",text:"greater than"},{id:"<=",text:"less than equal to"},{id:">=",text:"greater than equal to"},{id:"between",text:"in between"},{id:"is not null",text:"has any value"},{id:"is null",text:"has no value"}],"boolean":[{id:"=",text:"equal to"},{id:"is not null",text:"has any value"},{id:"is null",text:"has no value"}]};
p.retrieveReportPaginatedData=function(){n.maskLoading();
p.send={offSet:p.reportCurrentPage,limit:10,showAll:p.isReportBuilder};
h.retrieveAllReports(p.send,function(q){if(q!==undefined&&q!==null){p.reportList=[];
angular.forEach(q.records,function(r){r.displayName=r.reportName;
p.reportList.push(r)
});
p.reportTotalItems=(q.totalRecords);
p.indexReport=p.reportCurrentPage
}else{p.reportList=[]
}n.unMaskLoading()
},function(){n.unMaskLoading();
n.addMessage("Failed to load reports.",n.failure)
})
};
p.editReport=function(q){localStorage.setItem("reportId",q.id);
i.path("/reportbuilder")
};
p.goToReportBuilder=function(){localStorage.removeItem("reportId");
i.path("/reportbuilder")
};
function j(q,r){return q.fieldSequence-r.fieldSequence
}p.previewReport=function(q){p.currentPage=1;
p.clearReportData();
p.retrieveReportById(q)
};
p.isGroupedReport=function(){if(p.report.groupAttributes!==undefined&&p.report.groupAttributes!==null){p.isGroupByCheck=true
}else{p.isGroupByCheck=false
}};
p.retrievePaginatedData=function(){n.maskLoading();
p.isPreview=true;
p.tempData=[];
p.isGroupedReport();
p.hiddenFieldDetail=[];
p.gridOptions.totalItems=p.totalItems;
if(p.totalItems>0){p.isAnyRecordsExist=true
}if(!p.isGroupByCheck){p.UIPreviewData=angular.copy(p.previewData);
p.gridColumnDef=[];
angular.forEach(p.columns,function(v){v.total=undefined;
if(v.showTotal!==undefined&&v.showTotal===true){var u=0;
angular.forEach(p.previewData,function(x){var w=x[v.alias]===undefined?(x[v.fieldLabel]===undefined?0:x[v.fieldLabel]):x[v.alias];
u+=Number(w)
});
if(isNaN(u)){u=0
}v.total=u
}if(v.isHide===true){var s=(v.alias===undefined||v.alias==="")?v.fieldLabel:v.alias;
var r=[];
angular.forEach(p.previewData,function(y){var w=(y[s]===undefined||y[s]===null)?"N/A":y[s];
var x=false;
angular.forEach(r,function(z){if(z===w){x=true
}});
if(!x){r.push(w)
}});
var q="";
angular.forEach(r,function(w){if(q.length===0){q=w
}else{q+=","+w
}});
p.hiddenFieldDetail.push({label:s,value:q})
}if(v.componentType==="Currency"){if(p.currencyMap[v.associatedCurrency]!==undefined){v.currencySymbol=p.currencyMap[v.associatedCurrency].symbol;
v.format=p.currencyMap[v.associatedCurrency].format
}}var t;
switch(v.dataType){case"int8":t="number";
break;
case"varchar":t="string";
break;
case"timestamp":t="date";
break;
case"boolean":t="boolean";
break;
case"double precision":t="double";
break;
default:t="object";
break
}p.gridColumnDef.push({name:v.alias,displayName:v.alias,type:t,enableHiding:false,cellTemplate:"<div class=\"ui-grid-cell-contents\" title=\"{{(COL_FIELD === null || COL_FIELD === '') ? 'N/A':COL_FIELD}}\">{{(COL_FIELD === null || COL_FIELD === \"\") ? 'N/A':COL_FIELD}}</div>",minWidth:200})
});
p.gridOptions.columnDefs=p.gridColumnDef;
p.gridOptions.data=p.UIPreviewData
}if(p.isGroupByCheck){p.groupRecordsForPreview(p.previewData)
}else{if(!p.isEditModeColorInitialized){p.updateColorConfiguration()
}else{if(p.colorConfigData.length>0){p.applyColor(p.filterForm,true)
}}}n.unMaskLoading();
if(p.isFranchiseChange&&p.isFilter){p.applyFilter(p.filterForm,true)
}n.unMaskLoading()
};
p.groupRecordsForPreviewTemp=function(t){p.columns.sort(j);
p.modifiedColumns=angular.copy(p.columns);
angular.forEach(p.modifiedColumns,function(D){if(D.isHide===true){var C=(D.alias===undefined||D.alias==="")?D.fieldLabel:D.alias;
var B=[];
angular.forEach(p.previewData,function(F,E){angular.forEach(F,function(I){var G=(I[C]===undefined||I[C]===null)?"N/A":I[C];
var H=false;
angular.forEach(B,function(J){if(J===G){H=true
}});
if(!H){B.push(G)
}})
});
var A="";
angular.forEach(B,function(E){if(A.length===0){A=E
}else{A+=","+E
}});
p.hiddenFieldDetail.push({label:C,value:A})
}if(D.componentType==="Currency"){if(p.currencyMap[D.associatedCurrency]!==undefined){D.currencySymbol=p.currencyMap[D.associatedCurrency].symbol;
D.format=p.currencyMap[D.associatedCurrency].format
}}});
if(p.isGroupByCheck&&t!==undefined){var r=[];
var x=0;
angular.forEach(t,function(B,A){var C={};
C.key=A;
C.value=B;
C.index=++x;
r.push(C)
});
p.previewData=angular.copy(r);
var z=[];
var u=[];
p.groupItemListForPreviw=[];
p.groupAttributes=JSON.parse(p.report.groupAttributes);
if(p.groupAttributes.groupBy!==undefined&&p.groupAttributes.groupBy.length>0){p.groupAttributes.groupBy.sort(b);
var s=0;
p.levelMap=[];
angular.forEach(p.groupAttributes.groupBy,function(A){p.levelMap.push({level:A.level,levelItems:A.fields});
s++;
var B=A.fields.split(",");
angular.forEach(B,function(C){angular.forEach(p.modifiedColumns,function(E,D){var F;
if(E.alias===undefined||E.alias===""){F=E.fieldLabel
}else{F=E.alias
}if(F===C){E.level=A.level;
z.push(E);
u.push(E);
p.modifiedColumns.splice(D,1)
}})
})
});
p.totalLevel=s;
var q=[];
angular.forEach(p.modifiedColumns,function(A){var B;
if(A.alias===undefined||A.alias===""){B=A.fieldLabel
}else{B=A.alias
}q.push(B)
});
angular.forEach(p.levelMap,function(B){var A=B.levelItems.split(",");
angular.forEach(A,function(C){angular.forEach(q,function(E,D){if(E===C){q.splice(D,1)
}})
})
});
p.previewData=p.recursiveGroup(p.previewData,2,q);
p.combineRows();
var w=angular.copy(p.previewData);
var v=[];
var y=0;
angular.forEach(w,function(A){var B={};
B.key=A.key;
B.value=A.value;
B.groupRows=A.groupRows;
B.rowCount=A.rowCount;
angular.forEach(B.groupRows,function(C,D){B.groupRows[D].rowColor="white";
angular.forEach(C,function(F,E){B.groupRows[D][E]={value:F,color:"white"}
})
});
B.index=++y;
v.push(B)
});
p.UIPreviewData=angular.copy(v)
}if(p.groupAttributes.groups!==undefined&&p.groupAttributes.groups.length>0){p.groupAttributes.groups.sort(b);
if(p.groupAttributes.groups.length>1){angular.forEach(p.groupAttributes.groups,function(B){var E=B.groupItems.split(",");
var H=B.groupName;
var G={};
G.groupName=H;
G.isGroup=true;
var F=[];
angular.forEach(p.modifiedColumns,function(J){var I;
if(J.alias!==undefined&&J.alias!==""){I=J.alias
}else{I=J.fieldLabel
}angular.forEach(E,function(K){if(K===I&&(J.isHide===undefined||J.isHide===false)){p.groupItemListForPreviw.push(K);
F.push(K)
}})
});
G.groupItems=F;
angular.forEach(p.previewData,function(J){var I=[];
angular.forEach(J.groupRows,function(L){var K=[];
angular.forEach(F,function(M){var N=L[M]===undefined?"-":L[M];
K.push(N)
});
I.push(K)
});
J[H]=I
});
if(F.length>0){z.push(G)
}for(var A=p.modifiedColumns.length-1;
A>=0;
A--){var D=p.modifiedColumns[A];
var C;
if(D.alias!==undefined&&D.alias!==""){C=D.alias
}else{C=D.fieldLabel
}if(E.indexOf(C)>-1){u.push(p.modifiedColumns[A]);
p.modifiedColumns.splice(A,1)
}}})
}else{angular.forEach(p.groupAttributes.groups,function(B){var E=B.groupItems.split(",");
for(var A=p.modifiedColumns.length-1;
A>=0;
A--){var D=p.modifiedColumns[A];
var C;
if(D.alias!==undefined&&D.alias!==""){C=D.alias
}else{C=D.fieldLabel
}if(E.indexOf(C)>-1){u.push(p.modifiedColumns[A]);
p.modifiedColumns.splice(A,1)
}}})
}}if(p.modifiedColumns.length>0){angular.forEach(p.modifiedColumns,function(A){z.push(A);
u.push(A)
})
}z.sort(j);
u.sort(j);
p.modifiedColumns=angular.copy(z);
p.columns=angular.copy(u);
angular.forEach(p.columns,function(B){B.total=undefined;
if(B.showTotal!==undefined&&B.showTotal===true){var A=0;
angular.forEach(p.previewData,function(C){angular.forEach(C.groupRows,function(E){var D=E[B.alias]===undefined?(E[B.fieldLabel]===undefined?0:E[B.fieldLabel]):E[B.alias];
A+=Number(D)
})
});
if(isNaN(A)){A=0
}B.total=A
}});
if(!p.isEditModeColorInitialized){p.updateColorConfiguration()
}else{if(p.colorConfigData.length>0){p.applyColor(p.filterForm,true)
}}}};
p.groupRecordsForPreview=function(r){p.columns.sort(j);
p.modifiedColumns=angular.copy(p.columns);
if(p.isGroupByCheck&&r!==undefined){var w=[];
var s=0;
angular.forEach(r,function(y,x){w.push(y)
});
p.previewData=angular.copy(w);
var v=[];
var u=[];
p.groupItemListForPreviw=[];
p.groupAttributes=JSON.parse(p.report.groupAttributes);
if(p.groupAttributes.groupBy!==undefined&&p.groupAttributes.groupBy.length>0){p.groupAttributes.groupBy.sort(b);
var t=0;
p.levelMap=[];
angular.forEach(p.groupAttributes.groupBy,function(x){p.levelMap.push({level:x.level,levelItems:x.fields});
t++;
var y=x.fields.split(",");
angular.forEach(y,function(z){angular.forEach(p.modifiedColumns,function(B,A){var C;
if(B.alias===undefined||B.alias===""){C=B.fieldLabel
}else{C=B.alias
}if(C===z){B.level=x.level;
v.push(B);
u.push(B);
p.modifiedColumns.splice(A,1)
}})
})
});
p.totalLevel=t;
var q=[];
angular.forEach(p.modifiedColumns,function(x){var y;
if(x.alias===undefined||x.alias===""){y=x.fieldLabel
}else{y=x.alias
}q.push(y)
});
angular.forEach(p.levelMap,function(y){var x=y.levelItems.split(",");
angular.forEach(x,function(z){angular.forEach(q,function(B,A){if(B===z){q.splice(A,1)
}})
})
});
p.UIPreviewData=angular.copy(p.previewData)
}if(p.groupAttributes.groups!==undefined&&p.groupAttributes.groups.length>0){p.groupAttributes.groups.sort(b);
if(p.groupAttributes.groups.length>1){angular.forEach(p.groupAttributes.groups,function(y){var B=y.groupItems.split(",");
var E=y.groupName;
var D={};
D.groupName=E;
D.isGroup=true;
var C=[];
angular.forEach(p.modifiedColumns,function(G){var F;
if(G.alias!==undefined&&G.alias!==""){F=G.alias
}else{F=G.fieldLabel
}angular.forEach(B,function(H){if(H===F&&(G.isHide===undefined||G.isHide===false)){p.groupItemListForPreviw.push(H);
C.push(H)
}})
});
D.groupItems=C;
angular.forEach(p.previewData,function(G){var F=[];
angular.forEach(G.groupRows,function(I){var H=[];
angular.forEach(C,function(J){var K=I[J]===undefined?"-":I[J];
H.push(K)
});
F.push(H)
});
G[E]=F
});
if(C.length>0){v.push(D)
}for(var x=p.modifiedColumns.length-1;
x>=0;
x--){var A=p.modifiedColumns[x];
var z;
if(A.alias!==undefined&&A.alias!==""){z=A.alias
}else{z=A.fieldLabel
}if(B.indexOf(z)>-1){u.push(p.modifiedColumns[x]);
p.modifiedColumns.splice(x,1)
}}})
}else{angular.forEach(p.groupAttributes.groups,function(y){var B=y.groupItems.split(",");
for(var x=p.modifiedColumns.length-1;
x>=0;
x--){var A=p.modifiedColumns[x];
var z;
if(A.alias!==undefined&&A.alias!==""){z=A.alias
}else{z=A.fieldLabel
}if(B.indexOf(z)>-1){u.push(p.modifiedColumns[x]);
p.modifiedColumns.splice(x,1)
}}})
}}if(p.modifiedColumns.length>0){angular.forEach(p.modifiedColumns,function(x){v.push(x);
u.push(x)
})
}v.sort(j);
u.sort(j);
p.modifiedColumns=angular.copy(v);
p.columns=angular.copy(u);
p.gridColumnDef=[];
angular.forEach(p.columns,function(C){C.total=undefined;
if(C.showTotal!==undefined&&C.showTotal===true){var B=0;
angular.forEach(p.previewData,function(D){var E=D[C.alias]===undefined?(D[C.fieldLabel]===undefined?0:D[C.fieldLabel]):D[C.alias];
B+=Number(E)
});
if(isNaN(B)){B=0
}C.total=B
}if(C.isHide===true){var z=(C.alias===undefined||C.alias==="")?C.fieldLabel:C.alias;
var y=[];
angular.forEach(p.previewData,function(F){var D=(F[z]===undefined||F[z]===null)?"N/A":F[z];
var E=false;
angular.forEach(y,function(G){if(G===D){E=true
}});
if(!E){y.push(D)
}});
var x="";
angular.forEach(y,function(D){if(x.length===0){x=D
}else{x+=","+D
}});
p.hiddenFieldDetail.push({label:z,value:x})
}var A;
switch(C.dataType){case"int8":A="number";
break;
case"varchar":A="string";
break;
case"timestamp":A="date";
break;
case"boolean":A="boolean";
break;
case"double precision":A="double";
break;
default:A="object";
break
}if(C.isHide!==true){p.gridColumnDef.push({name:C.alias,displayName:C.alias,type:A,enableHiding:false,cellTemplate:"<div class=\"ui-grid-cell-contents\" title=\"{{(COL_FIELD === null || COL_FIELD === '') ? 'N/A':COL_FIELD}}\">{{(COL_FIELD === null || COL_FIELD === \"\") ? 'N/A':COL_FIELD}}</div>",minWidth:200})
}});
p.gridOptions.columnDefs=p.gridColumnDef;
p.gridOptions.data=p.UIPreviewData;
if(!p.isEditModeColorInitialized){p.updateColorConfiguration()
}}};
p.rowFormatter=function(r){var q="white";
if(p.colorConfigData.length>0){angular.forEach(p.colorConfigData,function(s){if(s.columns.length>0){var t=p.executeFilterOnRow(r.entity,s);
if(t){q=s.colorName
}}})
}return q
};
function b(q,r){return q.sequence-r.sequence
}p.clearReportData=function(){h.clearReportData(function(){},function(){})
};
p.retrieveViewCurrencyDataRightsForUser=function(){h.retrieveViewCurrencyDataRightsOfLoggedInUser({},function(q){p.viewCurrencyDataPermission=q.data
})
};
p.retrieveViewCurrencyDataRightsForUser();
p.retrieveReportById=function(q){h.retrieveCurrencyConfiguration({},function(r){p.isCurrencyVisible=r.data;
p.modifiedCurrencyColumns=[];
q.columns.sort(j);
if(r.data===false||(r.data===true&&p.viewCurrencyDataPermission===false)){angular.forEach(q.columns,function(t){if(t.componentType!=="Currency"){p.modifiedCurrencyColumns.push(t)
}})
}else{p.modifiedCurrencyColumns=angular.copy(q.columns)
}p.columns=angular.copy(p.modifiedCurrencyColumns);
angular.forEach(p.columns,function(t){t.fieldDisplayName=n.translateValue("RPRT_NM."+t.alias)
});
p.report=angular.copy(q);
p.models={selected:null,templates:[{type:"container",id:2,text:"New-Table",columns:[[{text:"Column",colField:"column",feature:0}]],searchData:[[{column:""}]]}],dropzones:{A:[]}};
angular.forEach(p.report.columns,function(u){if(p.models.dropzones.A!=undefined){var w=0;
angular.forEach(p.models.dropzones.A,function(x){var t=u.tableName.split(",");
if(t.indexOf(x.text)!==-1){w++;
x.columns[0].push({text:u.alias,colField:u.dbBaseName+"."+u.colName,newField:true,type:"col",feature:u.feature})
}});
if(w==0){var v=u.tableName.split(",");
angular.forEach(v,function(t){p.obj={type:"container",allowedTypes:["col"],id:1,columns:[[]],searchData:[[{column:""}]]};
p.obj.text=t;
var x={text:u.alias,colField:u.dbBaseName+"."+u.colName,newField:true,type:"col",feature:u.feature};
p.obj.columns[0].push(x);
p.models.dropzones.A.push(p.obj)
})
}}});
p.displayName=p.report.reportName;
p.convertedQuery=q.query;
if(!p.isFranchiseChange){p.filterColumns=[];
p.filterAttributes=[];
p.initializeFilter();
p.initializeColor()
}n.maskLoading();
p.report.convertedQuery=p.convertedQuery;
p.report.franchiseIds=[];
if(p.franchise!==undefined&&p.franchise!==null){p.report.franchiseIds.push(p.franchise)
}if(p.report.orderAttributes!==undefined&&p.report.orderAttributes!==null){var s=JSON.parse(p.report.orderAttributes);
angular.forEach(s,function(t){angular.forEach(p.columns,function(u){var v;
if(u.alias===undefined||u.alias===""){v=u.fieldLabel
}else{v=u.alias
}if(t.columnName===u.dbBaseName+"."+u.colName){p.columnOrderMap[v]=t.orderValue
}})
})
}p.currencyIdList=[];
angular.forEach(p.columns,function(t){if(t.componentType==="Currency"){p.currencyIdList.push(t.associatedCurrency)
}});
p.resultList=[];
h.retrieveReportTable(p.report,function(t){if(t.data!==undefined){if(t.data.queryValid!==undefined){var v="Invalid Query.";
var u=n.failure;
n.addMessage(v,u);
n.unMaskLoading()
}else{p.previewData=t.data.records;
p.totalItems=(t.data.totalRecords);
if(p.isGroupByCheck){angular.forEach(t.data.records,function(x,w){angular.forEach(x,function(y){p.resultList.push(angular.copy(y))
})
});
t.data.records=angular.copy(p.resultList)
}p.resultModels=angular.copy(p.models);
if(p.resultModels.dropzones.A!=undefined&&p.resultModels.dropzones.A.length>0&&t.data!=undefined){angular.forEach(p.resultModels.dropzones.A,function(w){w.searchData[0]=[];
if(w.columns[0].length>0){angular.forEach(t.data.records,function(x){var y={};
angular.forEach(w.columns[0],function(z){if(x[z.text]!==undefined){if(x[z.text]!=null){y[z.text]={value:x[z.text]}
}else{y[z.text]={value:"N/A"}
}}});
if(y!=null&&y!==undefined){w.searchData[0].push(y)
}})
}})
}n.unMaskLoading()
}}p.isFilter=true;
if(!p.isFranchiseChange){p.retrievePaginatedData()
}n.unMaskLoading()
},function(){n.unMaskLoading()
})
},function(r){n.addMessage("Some error occurred while retrieving data, try different fields and criteria",n.failure)
})
};
p.setPage=function(q){p.currentPage=q;
p.pageChanged()
};
p.setReportPage=function(q){p.reportCurrentPage=q;
p.retrieveReportPaginatedData()
};
p.pageChanged=function(){p.retrievePaginatedData()
};
p.initializeFilter=function(){angular.forEach(p.columns,function(s){var r;
if(s.alias!==null&&s.alias!==""){r=s.alias
}else{r=s.fieldLabel
}var q=s.dataType;
p.filterColumns.push({label:r,type:q,componentType:s.componentType})
})
};
p.updateFilter=function(s){p.subbmited=false;
if(s!==undefined){var q=p.availabeFilterTypesByDataType[s.type]===undefined?[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"}]:p.availabeFilterTypesByDataType[s.type];
p.filterTypes=[];
angular.forEach(q,function(t){p.filterTypes.push({id:t.id,text:t.text})
});
var r={};
r.label=s.label;
r.type=s.type;
r.componentType=s.componentType;
r.filters=p.filterTypes;
if(r.type==="varchar"){p.columnData=[];
r.availableFilterValueSelectEqual={multiple:false,placeholder:"  Filter Value",closeOnSelect:true,initSelection:function(t,v){var u=undefined;
v(u)
},query:function(v){var u=v.page-1;
var w={q:v.term,page_limit:10,page:u,col_name:r.label,isGrouped:p.isGroupByCheck};
p.names=[];
var x=function(z){p.names=[];
if(z.length!==0){p.names=[];
angular.forEach(z.columnValues,function(A){p.names.push({id:A,text:A})
})
}var y=(u*10)<z.total;
v.callback({results:p.names,more:y})
};
var t=function(){};
h.retrieveLimitedColumnValues(w,x,t)
}};
r.availableFilterValueSelectNotEqual={multiple:false,placeholder:"  Filter Value",closeOnSelect:true,initSelection:function(t,v){var u=undefined;
v(u)
},query:function(v){var u=v.page-1;
var w={q:v.term,page_limit:10,page:u,col_name:r.label,isGrouped:p.isGroupByCheck};
p.names=[];
var x=function(z){p.names=[];
if(z.length!==0){p.names=[];
angular.forEach(z.columnValues,function(A){p.names.push({id:A,text:A})
})
}var y=(u*10)<z.total;
v.callback({results:p.names,more:y})
};
var t=function(){};
h.retrieveLimitedColumnValues(w,x,t)
}};
r.availableFilterValueSelectIn={multiple:true,placeholder:"  Filter Value",closeOnSelect:false,initSelection:function(t,v){var u=[];
v(u)
},query:function(v){var u=v.page-1;
var w={q:v.term,page_limit:10,page:u,col_name:r.label,isGrouped:p.isGroupByCheck};
p.names=[];
var x=function(z){p.names=[];
if(z.length!==0){p.names=[];
angular.forEach(z.columnValues,function(A){p.names.push({id:A,text:A})
})
}var y=(u*10)<z.total;
v.callback({results:p.names,more:y})
};
var t=function(){};
h.retrieveLimitedColumnValues(w,x,t)
}};
r.availableFilterValueSelectNotIn={multiple:true,placeholder:"  Filter Value",closeOnSelect:false,initSelection:function(t,v){var u=[];
v(u)
},query:function(v){var u=v.page-1;
var w={q:v.term,page_limit:10,page:u,col_name:r.label,isGrouped:p.isGroupByCheck};
p.names=[];
var x=function(z){p.names=[];
if(z.length!==0){p.names=[];
angular.forEach(z.columnValues,function(A){p.names.push({id:A,text:A})
})
}var y=(u*10)<z.total;
v.callback({results:p.names,more:y})
};
var t=function(){};
h.retrieveLimitedColumnValues(w,x,t)
}}
}p.filterAttributes.push(r);
angular.forEach(p.filterColumns,function(u,t){if(u.label===s.label){p.filterColumns.splice(t,1)
}})
}};
p.removeReport=function(q){h.retrieveReport(q,function(r){p.report=r;
p.report.status="RM";
h.updateReport(p.report,function(s){p.retrieveReportPaginatedData()
},function(){var t="Report cannot be updated.";
var s=n.failure;
n.addMessage(t,s)
})
})
};
p.showRemovePopup=function(q){p.selectedId=q;
$("#removePopup").modal("show")
};
p.removeOk=function(){p.removeReport(p.selectedId);
$("#removePopup").modal("hide");
n.removeModalOpenCssAfterModalHide();
$(".modal-backdrop").remove()
};
p.removeCancel=function(){$("#removePopup").modal("hide");
n.removeModalOpenCssAfterModalHide();
$(".modal-backdrop").remove()
};
p.removeFilterByColumn=function(q,r){p.subbmited=false;
var s=p.filterAttributes[q];
p.filterAttributes.splice(q,1);
p.filterColumns.push({label:s.label,type:s.type,componentType:s.componentType});
p.applyFilter(r)
};
p.applyFilter=function(u,r){p.filterForm=u;
if((u!==undefined&&u.$valid)||(r!==undefined&&r===true)){n.maskLoading();
p.filter={};
p.isAnyColumnHidden=false;
p.hiddenFieldDetail=[];
angular.forEach(p.columns,function(v){v.isHide=false
});
var s=false;
if(p.columns.length===p.filterAttributes.length){var t=0;
angular.forEach(p.filterAttributes,function(v){if(v.hideColumn!==undefined&&v.hideColumn===true){t++
}});
if(t===p.columns.length){s=true
}}if(!s){console.log("$scope.filterAttributes :"+JSON.stringify(p.filterAttributes));
angular.forEach(p.filterAttributes,function(x){if(x.type==="timestamp"){var z=new Date(x.filterValue);
var A;
var y;
var v;
if((z.getMonth()+1)<10){y="0"+(z.getMonth()+1)
}else{y=(z.getMonth()+1)
}if(z.getDate()<10){v="0"+z.getDate()
}else{v=z.getDate()
}A=z.getFullYear()+"-"+y+"-"+v;
x.filterValue=A;
if(x.filterValueSecond!==undefined){z=new Date(x.filterValueSecond);
if((z.getMonth()+1)<10){y="0"+(z.getMonth()+1)
}else{y=(z.getMonth()+1)
}if(z.getDate()<10){v="0"+z.getDate()
}else{v=z.getDate()
}A=z.getFullYear()+"-"+y+"-"+v;
x.filterValueSecond=A
}}var w=x.label;
p.filterObj={operator:x.operator,type:x.type,componentType:x.componentType,filterValue:x.filterValue,filterValueSecond:x.filterValueSecond};
p.filter[x.label]=JSON.stringify(p.filterObj);
if(x.hideColumn!==undefined&&x.hideColumn===true){angular.forEach(p.columns,function(C){var B;
if(C.alias!==null&&C.alias!==""){B=C.alias
}else{B=C.fieldLabel
}if(B===x.label){C.isHide=true;
p.isAnyColumnHidden=true
}})
}});
var q="";
if(p.groupAttributes!==undefined){angular.forEach(p.groupAttributes.groupBy,function(v){if(v.level===1){q=v.fields
}})
}p.send={filters:p.filter,isGrouped:p.isGroupByCheck,groupBy:p.groupAttributes===undefined?null:q};
p.currentPage=1;
p.resultList=[];
h.retrieveFilteredData(p.send,function(v){if(p.isGroupByCheck){angular.forEach(v.data.records,function(x,w){angular.forEach(x,function(y){p.resultList.push(angular.copy(y))
})
});
v.data.records=angular.copy(p.resultList)
}if(v.data.records.length>0){p.previewData=v.data.records;
angular.forEach(p.columns,function(z){if(z.isHide===true){var y=(z.alias===undefined||z.alias==="")?z.fieldLabel:z.alias;
var x=[];
angular.forEach(p.previewData,function(C){var A=(C[y]===undefined||C[y]===null)?"N/A":C[y];
var B=false;
angular.forEach(x,function(D){if(D===A){B=true
}});
if(!B){x.push(A)
}});
var w="";
angular.forEach(x,function(A){if(w.length===0){w=A
}else{w+=","+A
}});
p.hiddenFieldDetail.push({label:y,value:w})
}});
p.isAnyRecordsExist=true;
p.isFilter=false;
p.resultModels=angular.copy(p.models);
if(p.resultModels.dropzones.A!=undefined&&p.resultModels.dropzones.A.length>0&&v.data!=undefined){angular.forEach(p.resultModels.dropzones.A,function(w){w.searchData[0]=[];
if(w.columns[0].length>0){angular.forEach(v.data.records,function(x){var y={};
angular.forEach(w.columns[0],function(z){if(x[z.text]!==undefined){if(x[z.text]!=null){y[z.text]={value:x[z.text]}
}else{y[z.text]={value:"N/A"}
}}});
if(y!=null&&y!==undefined){w.searchData[0].push(y)
}})
}})
}}else{p.isAnyRecordsExist=false
}n.unMaskLoading()
},function(){p.currentPage=1;
n.addMessage("Some error occurred while retrieving data, try different fields and criteria",n.failure);
n.unMaskLoading()
})
}else{n.addMessage("Can not hide all fields.",n.failure);
n.unMaskLoading()
}}};
p.generateFilteredPdf=function(){var r;
r=p.tempData;
p.map={records:r};
var s=[];
if(p.colorConfigData.length>0){angular.forEach(p.colorConfigData,function(v){var u=[];
var t={};
t.combinationType=v.combinationType;
t.colorName=v.colorName;
angular.forEach(v.columns,function(w){if(p.checkIsValidColorConfig(w)){var B={};
B.label=w.label;
var z=false;
angular.forEach(p.levelMap,function(F){var E=F.levelItems.split(",");
if(E.indexOf(w.label)>-1){z=true
}});
B.isGroupBy=z;
B.type=w.type;
B.componentType=w.componentType;
B.operator=w.operator;
if(w.filterValue instanceof Object===true){if(angular.isArray(w.filterValue)){var x="";
angular.forEach(w.filterValue,function(E){if(x.length===0){x=E.id
}else{x+=","+E.id
}});
B.filterValue=x
}else{if(B.type!=="timestamp"){B.filterValue=w.filterValue.id
}else{if(w.filterValue!==undefined){var C=new Date(w.filterValue);
var D;
var A;
var y;
if((C.getMonth()+1)<10){A="0"+(C.getMonth()+1)
}else{A=(C.getMonth()+1)
}if(C.getDate()<10){y="0"+C.getDate()
}else{y=C.getDate()
}D=C.getFullYear()+"-"+A+"-"+y;
B.filterValue=D;
if(w.filterValueSecond!==undefined){C=new Date(w.filterValueSecond);
if((C.getMonth()+1)<10){A="0"+(C.getMonth()+1)
}else{A=(C.getMonth()+1)
}if(C.getDate()<10){y="0"+C.getDate()
}else{y=C.getDate()
}D=C.getFullYear()+"-"+A+"-"+y;
B.filterValueSecond=D
}}}}}else{B.filterValue=w.filterValue
}if(w.filterValueSecond!==undefined&&w.filterValueSecond!==null&&B.type!=="timestamp"){B.filterValueSecond=w.filterValueSecond
}u.push(B)
}});
t.columns=u;
if(u.length>0){s.push(t)
}})
}var q=[];
if(p.hiddenFieldDetail.length>0){angular.forEach(p.hiddenFieldDetail,function(t){q.push(t.label)
})
}console.log("hiddenFields :"+JSON.stringify(q));
p.send={records:p.map,reportId:p.report.id,extension:".pdf",filterAttributes:p.filterAttributes,colorAttributes:s,hiddenFields:q};
n.maskLoading();
h.generateFilteredPdf(p.send,function(){window.location.href=n.appendAuthToken(n.apipath+"report/downloadfilteredpdf?reportId="+p.report.id+"&extension=.pdf");
n.unMaskLoading()
})
};
p.generateFilteredXls=function(){var r;
r=p.tempData;
p.map={records:r};
var s=[];
if(p.colorConfigData.length>0){angular.forEach(p.colorConfigData,function(v){var u=[];
var t={};
t.combinationType=v.combinationType;
t.colorName=v.colorName;
angular.forEach(v.columns,function(w){if(p.checkIsValidColorConfig(w)){var z={};
z.label=w.label;
var y=false;
angular.forEach(p.levelMap,function(B){var A=B.levelItems.split(",");
if(A.indexOf(w.label)>-1){y=true
}});
z.isGroupBy=y;
z.type=w.type;
z.componentType=w.componentType;
z.operator=w.operator;
if(w.filterValue instanceof Object===true){if(angular.isArray(w.filterValue)){var x="";
angular.forEach(w.filterValue,function(A){if(x.length===0){x=A.id
}else{x+=","+A.id
}});
z.filterValue=x
}else{z.filterValue=w.filterValue.id
}}else{z.filterValue=w.filterValue
}if(w.filterValueSecond!==undefined||w.filterValueSecond!==null){z.filterValueSecond=w.filterValueSecond
}u.push(z)
}});
t.columns=u;
if(u.length>0){s.push(t)
}})
}var q=[];
if(p.hiddenFieldDetail.length>0){angular.forEach(p.hiddenFieldDetail,function(t){q.push(t.label)
})
}p.send={records:p.map,reportId:p.report.id,extension:".xls",filterAttributes:p.filterAttributes,colorAttributes:s,hiddenFields:q};
n.maskLoading();
h.generateFilteredPdf(p.send,function(){window.location.href=n.appendAuthToken(n.apipath+"report/downloadfilteredpdf?reportId="+p.report.id+"&extension=.xls");
n.unMaskLoading()
})
};
p.generateFullPdf=function(q){n.maskLoading();
window.location.href=n.appendAuthToken(n.apipath+"report/downloadpdf?reportId="+q.id);
n.unMaskLoading()
};
p.generateFullExcel=function(q){n.maskLoading();
window.location.href=n.appendAuthToken(n.apipath+"report/downloadexcel?reportId="+q.id);
n.unMaskLoading()
};
p.generateFullXml=function(q){n.maskLoading();
window.location.href=n.appendAuthToken(n.apipath+"report/downloadxml?reportId="+q.id);
n.unMaskLoading()
};
p.cancelPreview=function(){p.isPreview=false;
p.isFilter=true;
p.isHKAdmin=false;
p.isFranchiseChange=false;
p.currentColor={};
p.colorConfigData=[];
p.colorConfig={};
p.clearReportData();
p.isEditModeColorInitialized=false;
p.isAnyColumnHidden=false;
p.isAnyRecordsExist=false
};
p.initializeFranchise=function(){if(n.session!==undefined){if(n.session.isHKAdmin){k.retrieveAllFranchise({tree:false},function(s){var r=[];
angular.forEach(s,function(u){u.franchiseName=n.translateValue("FRNCSE_NM."+u.franchiseName);
r.push(u)
});
p.franchiseModelList=r;
p.existingFranchiseList=[];
var t=[];
for(var q=0;
q<r.length;
q++){p.existingFranchiseList.push({id:r[q].id,displayName:r[q].franchiseName});
t.push({id:r[q].id,text:r[q].franchiseName})
}p.isHKAdmin=true;
p.franchiseSelectCombo={multiple:true,placeholder:"Select Franchise",closeOnSelect:false,data:t}
},function(){n.addMessage("Failed to retrieve franchises",1)
})
}else{p.isHKAdmin=false
}}};
p.repopulateData=function(q){p.franchise=q;
p.isFranchiseChange=true;
p.retrieveReportById(p.report)
};
p.initializeColor=function(){p.colorColumns=[];
p.isGroupedReport();
if(!p.isGroupByCheck){angular.forEach(p.columns,function(u){var t;
if(u.alias!==null&&u.alias!==""){t=u.alias
}else{t=u.fieldLabel
}var s=u.dataType;
p.colorColumns.push({label:t,type:s,componentType:u.componentType})
})
}else{p.groupAttributes=JSON.parse(p.report.groupAttributes);
var q=[];
angular.forEach(p.groupAttributes.groupBy,function(s){if(s.level===1){q=s.fields.split(",")
}});
angular.forEach(q,function(s){angular.forEach(p.columns,function(v){var u;
if(v.alias!==null&&v.alias!==""){u=v.alias
}else{u=v.fieldLabel
}var t=v.dataType;
if(u===s){p.colorColumns.push({label:u,type:t,componentType:v.componentType})
}})
})
}p.colorConfigData=[];
var r={};
r.combinationType="ANY";
r.columns=[];
r.colorColumns=p.colorColumns;
r.combinationTypes=[{id:"ALL",text:"ALL"},{id:"ANY",text:"ANY"}];
p.colorConfigData.push(r)
};
p.updateColorConfiguration=function(){p.isEditModeColorInitialized=true;
if(p.report.colorAttributes!==undefined&&p.report.colorAttributes!==null&&p.report.colorAttributes!==""){var q=JSON.parse(p.report.colorAttributes);
if(q.length>0){p.colorConfigData=[];
angular.forEach(q,function(t){var s={};
s.combinationType=t.combinationType;
s.colorName=t.colorName;
s.colorColumns=p.colorColumns;
s.combinationTypes=[{id:"ALL",text:"ALL"},{id:"ANY",text:"ANY"}];
var r=[];
angular.forEach(t.columns,function(v){var x=false;
angular.forEach(p.colorColumns,function(z){if(z.label===v.label){x=true
}});
if(x){var y={};
y.label=v.label;
y.type=v.type;
y.componentType=v.componentType;
y.label=v.label;
var w=p.availabeFilterTypesByDataType[v.type]===undefined?[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"}]:p.availabeFilterTypesByDataType[v.type];
if(v.componentType==="Date range"){w=[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"}]
}y.filters=w;
y.filterValue=v.filterValue;
y.filterValueSecond=v.filterValueSecond;
var u;
angular.forEach(w,function(z){if(z.id===v.operator){u=z
}});
y.operator=u.id;
if(y.type==="varchar"){p.colorColumnData=[];
y.availableFilterValueSelectEqual={multiple:false,placeholder:"  Filter Value",closeOnSelect:true,initSelection:function(z,B){var A={};
A={id:v.filterValue,text:v.filterValue};
B(A)
},query:function(B){var A=B.page-1;
var C={q:B.term,page_limit:10,page:A,col_name:y.label,isGrouped:p.isGroupByCheck};
p.names=[];
var D=function(F){p.names=[];
if(F.length!==0){p.names=[];
angular.forEach(F.columnValues,function(G){p.names.push({id:G,text:G})
})
}var E=(A*10)<F.total;
B.callback({results:p.names,more:E})
};
var z=function(){};
h.retrieveLimitedColumnValues(C,D,z)
}};
y.availableFilterValueSelectNotEqual={multiple:false,placeholder:"  Filter Value",closeOnSelect:true,initSelection:function(z,B){var A={};
A={id:v.filterValue,text:v.filterValue};
B(A)
},query:function(B){var A=B.page-1;
var C={q:B.term,page_limit:10,page:A,col_name:y.label,isGrouped:p.isGroupByCheck};
p.names=[];
var D=function(F){p.names=[];
if(F.length!==0){p.names=[];
angular.forEach(F.columnValues,function(G){p.names.push({id:G,text:G})
})
}var E=(A*10)<F.total;
B.callback({results:p.names,more:E})
};
var z=function(){};
h.retrieveLimitedColumnValues(C,D,z)
}};
y.availableFilterValueSelectIn={multiple:true,placeholder:"  Filter Value",closeOnSelect:false,initSelection:function(z,B){var A=[];
angular.forEach(v.filterValue.split(","),function(C){A.push({id:C,text:C})
});
B(A)
},query:function(B){var A=B.page-1;
var C={q:B.term,page_limit:10,page:A,col_name:y.label,isGrouped:p.isGroupByCheck};
p.names=[];
var D=function(F){p.names=[];
if(F.length!==0){p.names=[];
angular.forEach(F.columnValues,function(G){p.names.push({id:G,text:G})
})
}var E=(A*10)<F.total;
B.callback({results:p.names,more:E})
};
var z=function(){};
h.retrieveLimitedColumnValues(C,D,z)
}};
y.availableFilterValueSelectNotIn={multiple:true,placeholder:"  Filter Value",closeOnSelect:false,initSelection:function(z,B){var A=[];
angular.forEach(v.filterValue.split(","),function(C){A.push({id:C,text:C})
});
B(A)
},query:function(B){var A=B.page-1;
var C={q:B.term,page_limit:10,page:A,col_name:y.label,isGrouped:p.isGroupByCheck};
p.names=[];
var D=function(F){p.names=[];
if(F.length!==0){p.names=[];
angular.forEach(F.columnValues,function(G){p.names.push({id:G,text:G})
})
}var E=(A*10)<F.total;
B.callback({results:p.names,more:E})
};
var z=function(){};
h.retrieveLimitedColumnValues(C,D,z)
}}
}r.push(y)
}});
s.columns=angular.copy(r);
p.colorConfigData.push(s)
});
if(p.colorConfigData.length>0){p.applyColor(p.filterForm,true)
}}}};
p.updateColorColumnFilter=function(r,q){p.subbmited=false;
if(r!==undefined&&r!==null){var s=p.availabeFilterTypesByDataType[r.type]===undefined?[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"}]:p.availabeFilterTypesByDataType[r.type];
var t={};
t.label=r.label;
t.type=r.type;
t.componentType=r.componentType;
if(r.componentType==="Date range"){s=[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"}]
}t.filters=s;
if(r.type==="varchar"){p.colorColumnData=[];
t.availableFilterValueSelectEqual={multiple:false,placeholder:"  Filter Value",closeOnSelect:true,initSelection:function(u,w){var v=undefined;
w(v)
},query:function(w){var v=w.page-1;
var x={q:w.term,page_limit:10,page:v,col_name:t.label,isGrouped:p.isGroupByCheck};
p.names=[];
var y=function(A){p.names=[];
if(A.length!==0){p.names=[];
angular.forEach(A.columnValues,function(B){p.names.push({id:B,text:B})
})
}var z=(v*10)<A.total;
w.callback({results:p.names,more:z})
};
var u=function(){};
h.retrieveLimitedColumnValues(x,y,u)
}};
t.availableFilterValueSelectNotEqual={multiple:false,placeholder:"  Filter Value",closeOnSelect:true,initSelection:function(u,w){var v=undefined;
w(v)
},query:function(w){var v=w.page-1;
var x={q:w.term,page_limit:10,page:v,col_name:t.label,isGrouped:p.isGroupByCheck};
p.names=[];
var y=function(A){p.names=[];
if(A.length!==0){p.names=[];
angular.forEach(A.columnValues,function(B){p.names.push({id:B,text:B})
})
}var z=(v*10)<A.total;
w.callback({results:p.names,more:z})
};
var u=function(){};
h.retrieveLimitedColumnValues(x,y,u)
}};
t.availableFilterValueSelectIn={multiple:true,placeholder:"  Filter Value",closeOnSelect:false,initSelection:function(u,w){var v=[];
w(v)
},query:function(w){var v=w.page-1;
var x={q:w.term,page_limit:10,page:v,col_name:t.label,isGrouped:p.isGroupByCheck};
p.names=[];
var y=function(A){p.names=[];
if(A.length!==0){p.names=[];
angular.forEach(A.columnValues,function(B){p.names.push({id:B,text:B})
})
}var z=(v*10)<A.total;
w.callback({results:p.names,more:z})
};
var u=function(){};
h.retrieveLimitedColumnValues(x,y,u)
}};
t.availableFilterValueSelectNotIn={multiple:true,placeholder:"  Filter Value",closeOnSelect:false,initSelection:function(u,w){var v=[];
w(v)
},query:function(w){var v=w.page-1;
var x={q:w.term,page_limit:10,page:v,col_name:t.label,isGrouped:p.isGroupByCheck};
p.names=[];
var y=function(A){p.names=[];
if(A.length!==0){p.names=[];
angular.forEach(A.columnValues,function(B){p.names.push({id:B,text:B})
})
}var z=(v*10)<A.total;
w.callback({results:p.names,more:z})
};
var u=function(){};
h.retrieveLimitedColumnValues(x,y,u)
}}
}p.colorConfigData[q].columns.push(t);
p.colorConfigData[q].currentColumn=undefined
}};
p.removeColor=function(r,q){p.subbmited=false;
if(p.colorConfigData[q].columns.length>0){p.colorConfigData[q].columns.splice(r,1)
}if(p.colorConfigData[q].columns.length===0&&q!==0){p.removeColorCombination(q)
}p.applyColor(p.filterForm)
};
p.removeColorCombination=function(q){p.subbmited=false;
if(p.colorConfigData.length===1){p.colorConfigData[0].combinationType="ANY";
p.colorConfigData[0].columns=[];
p.colorConfigData[0].colorColumns=p.colorColumns;
p.colorConfigData[0].combinationTypes=[{id:"ALL",text:"ALL"},{id:"ANY",text:"ANY"}];
p.colorConfigData[0].colorName=undefined
}else{p.colorConfigData.splice(q,1)
}};
p.addColorCombination=function(){p.subbmited=false;
if(p.colorConfigData[p.colorConfigData.length-1].columns.length>0){var q={};
q.combinationType="ANY";
q.columns=[];
q.colorColumns=p.colorColumns;
q.combinationTypes=[{id:"ALL",text:"ALL"},{id:"ANY",text:"ANY"}];
p.colorConfigData.push(q)
}else{n.addMessage("Select atleast one criteria field to add new.",n.failure)
}};
p.applyColor=function(s,r){if((s!==undefined&&s.$valid)||(r!==undefined&&r===true)){p.filterForm=s;
if(p.colorConfigData.length>0){console.log("$scope.previewData :"+JSON.stringify(p.previewData));
if(p.previewData.length>0){if(true){var q=angular.copy(p.previewData);
angular.forEach(p.resultModels.dropzones.A,function(t){if(t.columns[0].length>0){angular.forEach(t.searchData[0],function(u,v){t.searchData[0][v].rowColor="white";
angular.forEach(u,function(x,w){t.searchData[0][v][w].color="white"
})
})
}});
angular.forEach(p.resultModels.dropzones.A,function(t){if(t.columns[0].length>0){angular.forEach(t.searchData[0],function(v,x){var u=false;
var w="white";
angular.forEach(p.colorConfigData,function(y){if(y.columns.length>0){var z=p.executeFilterOnRow(v,y);
if(z){u=true;
w=y.colorName
}}});
console.log("data :"+JSON.stringify(v));
console.log("isRowSelected :"+JSON.stringify(u));
if(u){angular.forEach(v,function(z,y){t.searchData[0][x][y].color=w
});
t.searchData[0][x].rowColor=w
}})
}})
}else{var q=angular.copy(p.previewData);
angular.forEach(q,function(u,t){angular.forEach(u.groupRows,function(v,w){q[t].groupRows[w].rowColor="white";
angular.forEach(v,function(y,x){q[t].groupRows[w][x]={value:y,color:"white"}
})
})
});
angular.forEach(q,function(x,v){var u=x.groupRows;
var z=false;
var w;
p.groupAttributes=JSON.parse(p.report.groupAttributes);
var y;
angular.forEach(p.groupAttributes.groupBy,function(A){if(A.level===1){y=A.fields.split(",")
}});
var t={};
angular.forEach(y,function(A,C){var B;
angular.forEach(u,function(D){B=D[A]
});
t[A]=B
});
angular.forEach(p.colorConfigData,function(A){if(A.columns.length>0){var B=p.executeFilterOnRow(t,A);
if(B){z=true;
w=A.colorName
}}});
if(z){angular.forEach(x.groupRows,function(A,B){angular.forEach(A,function(D,C){q[v].groupRows[B][C].color=w
});
q[v].groupRows[B].rowColor=w
})
}});
p.UIPreviewData=angular.copy(q)
}}}else{if(!p.isGroupByCheck){angular.forEach(p.UIPreviewData,function(t,u){p.UIPreviewData[u].rowColor="white";
angular.forEach(t,function(w,v){p.UIPreviewData[u][v].color="white"
})
})
}else{angular.forEach(p.UIPreviewData,function(u,t){angular.forEach(u.value,function(w,v){p.UIPreviewData[t].groupRows[v].rowColor="white";
angular.forEach(w,function(y,x){p.UIPreviewData[t].groupRows[v][x].color="white"
})
})
})
}}}};
p.checkIsValidColorConfig=function(q){if(q!==undefined){if(q.operator===undefined||q.operator===null){return false
}if(q.filterValue===undefined&&q.operator!=="is null"&&q.operator!=="is not null"){return false
}if(q.operator==="between"&&(q.filterValueSecond===undefined||q.filterValueSecond===null)){return false
}return true
}return false
};
p.executeFilterOnRow=function(t,q){if(q.combinationType==="ANY"){var r=false;
angular.forEach(t,function(v,u){if(r===false){angular.forEach(q.columns,function(w){if(u===w.label&&r===false){if(p.checkIsValidColorConfig(w)){var y=angular.copy(w);
if(y.type==="timestamp"){var z=new Date(y.filterValue);
y.filterValue=new Date(z.getFullYear(),z.getMonth(),z.getDate());
if(y.filterValueSecond!==undefined){z=new Date(y.filterValueSecond);
y.filterValueSecond=new Date(z.getFullYear(),z.getMonth(),z.getDate())
}}var x=p.executeFilter(v.value,y);
if(x){r=true
}}}})
}});
return r
}else{if(q.columns.length>0){var s=true;
angular.forEach(q.columns,function(u){if(s===true){angular.forEach(t,function(w,v){if(v===u.label&&s===true){if(p.checkIsValidColorConfig(u)){var y=angular.copy(u);
if(y.type==="timestamp"){var z=new Date(y.filterValue);
y.filterValue=new Date(z.getFullYear(),z.getMonth(),z.getDate());
if(y.filterValueSecond!==undefined){z=new Date(y.filterValueSecond);
y.filterValueSecond=new Date(z.getFullYear(),z.getMonth(),z.getDate())
}}var x=p.executeFilter(w.value,y);
if(x===false){s=false
}}}})
}});
return s
}else{return false
}}};
p.executeFilter=function(z,y){if(((z!==undefined&&z!==null&&z!=="")||(y.type==="double precision")||(y.type==="int8"))&&(y.operator!=="is null"&&y.operator!=="is not null")){var t=false;
var w=[];
if(!(z!==undefined&&z!==null&&z!=="")){z=0
}if(y.type==="timestamp"){if(y.componentType!=="Date range"){var s=z.substring(0,10).split("/");
z=new Date(parseInt(s[2]),parseInt(s[1])-1,parseInt(s[0]))
}else{var v=z.split("to");
var u=v[0].trim().substring(0,10).split("/");
z=new Date(u[2],parseInt(u[1])-1,u[0]);
w.push(z);
var x=v[1].trim().substring(0,10).split("/");
z=new Date(x[2],parseInt(x[1])-1,x[0]);
w.push(z)
}}else{if(y.componentType==="Currency"){z=parseFloat(z.split(" ")[0].trim())
}else{if(y.filterValue instanceof Object){if(!angular.isArray(y.filterValue)){y.filterValue=y.filterValue.id
}else{var A="";
angular.forEach(y.filterValue,function(B){if(A.length===0){A=B.id
}else{A+=","+B.id
}});
y.filterValue=A
}}}}switch(y.type){case"varchar":switch(y.operator){case"=":if(z===y.filterValue){t=true
}break;
case"!=":if(z!==y.filterValue){t=true
}break;
case"in":var q=y.filterValue.split(",");
if(q.indexOf(z)>-1){t=true
}break;
case"not in":var q=y.filterValue.split(",");
if(q.indexOf(z)===-1){t=true
}break;
case"like":y.filterValue="%"+y.filterValue+"%";
var r=y.filterValue.replace(/%/g,".*");
r=r.replace(/_/g,".");
if(z.match(r)){t=true
}break
}break;
case"int8":switch(y.operator){case"=":if(parseInt(z)===parseInt(y.filterValue)){t=true
}break;
case"!=":if(parseInt(z)!==parseInt(y.filterValue)){t=true
}break;
case">":if(parseInt(z)>parseInt(y.filterValue)){t=true
}break;
case">=":if(parseInt(z)>=parseInt(y.filterValue)){t=true
}break;
case"<":if(parseInt(z)<parseInt(y.filterValue)){t=true
}break;
case"<=":if(parseInt(z)<=parseInt(y.filterValue)){t=true
}break;
case"between":if(parseInt(z)>=parseInt(y.filterValue)&&parseInt(z)<=parseInt(y.filterValueSecond)){t=true
}break
}break;
case"double precision":switch(y.operator){case"=":if(parseFloat(z)===parseFloat(y.filterValue)){t=true
}break;
case"!=":if(parseFloat(z)!==parseFloat(y.filterValue)){t=true
}break;
case">":if(parseFloat(z)>parseFloat(y.filterValue)){t=true
}break;
case">=":if(parseFloat(z)>=parseFloat(y.filterValue)){t=true
}break;
case"<":if(parseFloat(z)<parseFloat(y.filterValue)){t=true
}break;
case"<=":if(parseFloat(z)<=parseFloat(y.filterValue)){t=true
}break;
case"between":if(parseFloat(z)>=parseFloat(y.filterValue)&&parseFloat(z)<=parseFloat(y.filterValueSecond)){t=true
}break
}break;
case"timestamp":if(y.componentType==="Date range"){if(y.operator==="="){if(w[0].getTime()<=y.filterValue.getTime()&&w[1].getTime()>=y.filterValue.getTime()){t=true
}}else{if(y.operator==="!="){if(w[0].getTime()>y.filterValue.getTime()||w[1].getTime()<y.filterValue.getTime()){t=true
}}}}else{switch(y.operator){case"=":if(z.getTime()===y.filterValue.getTime()){t=true
}break;
case"!=":if(z.getTime()!==y.filterValue.getTime()){t=true
}break;
case">":if(z.getTime()>y.filterValue.getTime()){t=true
}break;
case"<":if(z.getTime()<y.filterValue.getTime()){t=true
}break;
case"between":if(z.getTime()>=y.filterValue.getTime()&&z.getTime()<=y.filterValueSecond.getTime()){t=true
}break
}}break;
case"boolean":switch(y.operator){case"=":if(z===y.filterValue){t=true
}break;
case"!=":if(z!==y.filterValue){t=true
}break
}break
}return t
}else{if(y.operator==="is null"||y.operator==="is not null"){if((z===undefined||z===null||z==="")&&y.operator==="is null"){return true
}else{if(!(z===undefined||z===null||z==="")&&y.operator==="is not null"){return true
}else{return false
}}}else{return false
}}};
p.checkValidColorRange=function(s,r,v,x,w,q){w.$setValidity("invalidRange",true);
if(q!==undefined&&q!==null){q.$setValidity("invalidRange",true)
}var u;
var t;
if(s==="between"){if(v===undefined||v===null||v.length===0||x===undefined||x===null||x.length===0){return
}switch(r){case"int8":u=parseInt(v);
t=parseInt(x);
break;
case"double precision":u=parseFloat(v);
t=parseFloat(x);
break;
case"timestamp":u=angular.copy(new Date(v));
t=angular.copy(new Date(x));
break
}if(u>t){w.$setValidity("invalidRange",false);
q.$setValidity("invalidRange",false)
}}};
p.predicate=function(q){return function(r){return r[q]
}
};
p.applyMultipleOrdering=function c(u,t){var s=angular.copy(t);
var q=[];
if(u.value.length>0){var w=false;
if(p.columnOrderMap[s[0]]!==undefined&&p.columnOrderMap[s[0]]==="desc"){w=true
}u.value=m("orderBy")(u.value,p.predicate(s[0]),w);
var r="****NO MATCH****";
var v=[];
angular.forEach(u.value,function(x,z){var y="";
if(s.length>0){var A=s[0];
if(A.indexOf("-")===0){A=A.substr(1,A.length-1)
}if(y.length===0){if(x[A]===null||x[A]===undefined){y="null"
}else{y=x[A]
}}else{if(x[A]===null||x[A]===undefined){y+=",null"
}else{y+=","+x[A]
}}}if(r!==y){if(z!==0){var B={};
B.key=r;
B.value=angular.copy(v);
q.push(B)
}r=y;
v=[];
v.push(x)
}else{v.push(x)
}if(z===u.value.length-1){var C={};
C.key=r;
C.value=angular.copy(v);
q.push(C)
}});
s.splice(0,1);
if(s.length>0){angular.forEach(q,function(x){x.value=c(x,s)
})
}}return q
};
p.combineMultipleOrdering=function o(s,t){var q=t;
var r=[];
if(q>1){angular.forEach(s,function(v){var u=o(v.value,(q-1));
angular.forEach(u,function(w){r.push(w)
})
})
}else{angular.forEach(s,function(u){angular.forEach(u.value,function(v){r.push(v)
})
})
}return r
};
p.divideGroup=function(w,t,q){var s=[];
if(w.value.length>0){var v=t.split(",");
var r=p.applyMultipleOrdering(w,v);
w.value=p.combineMultipleOrdering(r,v.length);
var u="****NO MATCH****";
var x=[];
angular.forEach(w.value,function(y,A){var z="";
if(v.length>0){angular.forEach(v,function(D){if(D.indexOf("-")===0){D=D.substr(1,D.length-1)
}if(z.length===0){if(y[D]===null||y[D]===undefined){z="null"
}else{z=y[D]
}}else{if(y[D]===null||y[D]===undefined){z+=",null"
}else{z+=","+y[D]
}}})
}if(u!==z){if(A!==0){var B={};
B.key=u;
angular.forEach(q,function(E){if(p.columnOrderMap[E]!==undefined){var D=false;
if(p.columnOrderMap[E]==="desc"){D=true
}x=m("orderBy")(x,p.predicate(E),D)
}});
B.value=angular.copy(x);
s.push(B)
}u=z;
x=[];
x.push(y)
}else{x.push(y)
}if(A===w.value.length-1){var C={};
C.key=u;
angular.forEach(q,function(E){if(p.columnOrderMap[E]!==undefined){var D=false;
if(p.columnOrderMap[E]==="desc"){D=true
}x=m("orderBy")(x,p.predicate(E),D)
}});
C.value=angular.copy(x);
s.push(C)
}})
}return s
};
p.recursiveGroup=function f(r,t,q){var s;
if(t!==undefined){angular.forEach(p.levelMap,function(u){if(t===u.level){s=u.levelItems
}})
}angular.forEach(r,function(v){v.rowCount=v.value.length;
if(s!==undefined){var u=p.divideGroup(v,s,q);
if(t<=p.totalLevel){v.value=f(u,t+1,q)
}else{v.value=u
}}});
return r
};
p.combineRows=function(){angular.forEach(p.previewData,function(x){var y=[];
for(var w=1;
w<=x.rowCount;
w++){var q=x;
var s=2;
var v=w;
var z=[];
while(s<=p.totalLevel){var u=q.value;
var t=false;
var r=0;
angular.forEach(u,function(A){if(t===false){var B=A.rowCount;
if(r+B>=v){t=true;
s++;
q=A;
v-=r
}else{r+=B
}}})
}z=q.value[v-1];
y.push(z)
}x.groupRows=y
})
};
p.checkRowSpan=function(z,A,q,y){if(q!==undefined){var r=z;
var w=2;
var v=y+1;
var x=1;
if(q===1){x=z.rowCount
}while(q>=w){var u=r.value;
var t=false;
var s=0;
angular.forEach(u,function(B){if(t===false){var C=B.rowCount;
if(s+C>=v){t=true;
q--;
r=B;
v-=s;
x=B.rowCount
}else{s+=C
}}})
}return x
}else{return 1
}};
p.checkToRender=function(z,A,q,y){var x=p.checkRowSpan(z,A,q,y);
if(x===1){return true
}if(q!==undefined){var r=z;
var w=2;
var v=y+1;
while(q>=w){var u=r.value;
var t=false;
var s=0;
angular.forEach(u,function(B){if(t===false){var C=B.rowCount;
if(s+C>=v){t=true;
q--;
r=B;
v-=s
}else{s+=C
}}})
}if((v-1)%x===0){return true
}else{return false
}}else{return true
}};
p.applyAll=function(q){p.subbmited=true;
if(q!==undefined&&q.$valid){p.filterForm=q;
if(p.filterAttributes.length>0){p.applyFilter(q)
}}};
localStorage.removeItem("reportId");
n.unMaskLoading();
p.retrieveReportPaginatedData()
}])
});