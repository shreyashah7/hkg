define(["hkg","reportBuilderService","franchiseService","ngload!uiGrid","colorpicker.directive"],function(a){a.register.controller("PreviewReport",["$rootScope","$scope","$filter","ReportBuilderService","$sce","$location","$timeout","$q","$http","FranchiseService",function(o,q,l,g,n,h,e,k,m,j){q.subbmited=false;
q.reportList=[];
q.isPreview=false;
o.mainMenu="reportLink";
if(localStorage.getItem("featureName")!=="null"&&localStorage.getItem("featureName")!==""){var c=localStorage.getItem("featureName");
o.childMenu=c
}o.activateMenu();
q.isScheduler=false;
q.isExternalReport=false;
q.dt={};
q.isFilter=true;
q.isAnyColumnHidden=false;
q.hiddenFieldDetail=[];
q.isHKAdmin=false;
q.displayName="";
q.entity="REPORTBUILDER.";
q.displayLocalData=[];
q.isFranchiseChange=false;
q.isAnyRecordsExist=false;
q.columnOrderMap={};
q.currentColor={};
q.colorConfigData=[];
q.colorConfig={};
q.isEditModeColorInitialized=false;
q.currencyMap={};
q.open=function(r,t,s){r.preventDefault();
r.stopPropagation();
s[t]=true
};
q.dateOptions={"year-format":"'yy'","starting-day":1};
q.format=o.dateFormat;
q.today=new Date();
q.paginationOptions={pageNumber:1,pageSize:50,sortDirection:null,sortColumn:null,sortColumnType:null};
q.filterOptions=[];
q.gridOptions={};
q.gridOptions.enableFiltering=true;
q.gridOptions.useExternalFiltering=true;
q.gridOptions.paginationPageSizes=[50,100];
q.gridOptions.paginationPageSize=50;
q.gridOptions.useExternalPagination=true;
q.gridOptions.useExternalSorting=true;
q.gridOptions.rowTemplate='<div ng-style="{ \'background-color\': grid.appScope.rowFormatter(row) }">  <div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }"  ui-grid-cell></div></div>';
q.gridOptions.columnDefs=[];
q.gridOptions.data=[];
q.gridOptions.onRegisterApi=function(r){q.gridApi=r;
q.gridApi.core.on.sortChanged(q,function(t,s){if(s.length===0){q.paginationOptions.sortDirection=null;
q.paginationOptions.sortColumn=null;
q.paginationOptions.sortColumnType=null
}else{q.paginationOptions.sortDirection=s[0].sort.direction;
q.paginationOptions.sortColumn=s[0].field;
q.paginationOptions.sortColumnType=s[0].colDef.type
}q.getPage()
});
q.gridApi.core.on.filterChanged(q,function(){var s=this.grid;
if(s.columns.length>0){q.filterOptions=[];
angular.forEach(s.columns,function(t){if(t.filters[0].term!==undefined&&t.filters[0].term!==null&&t.filters[0].term!==""){var u={};
u.filterColumn=t.field;
u.filterColumnType=t.colDef.type;
u.filterValue=t.filters[0].term;
q.filterOptions.push(u)
}});
q.getPage()
}});
r.pagination.on.paginationChanged(q,function(t,s){q.paginationOptions.pageNumber=t;
q.paginationOptions.pageSize=s;
q.getPage()
})
};
q.getPage=function(){q.send={offSet:q.paginationOptions.pageNumber,limit:q.paginationOptions.pageSize,isFilter:q.isFilter,isGrouped:q.isGroupByCheck,sortColumn:q.paginationOptions.sortColumn,sortDirection:q.paginationOptions.sortDirection,sortColumnType:q.paginationOptions.sortColumnType,filterOptions:q.filterOptions};
g.retrievePaginatedData(q.send,function(r){if(r.data!==undefined){if(r.data.queryValid!==undefined){var t="Invalid Query.";
var s=o.failure;
o.addMessage(t,s)
}else{q.previewData=r.data.records;
q.totalItems=(r.data.totalRecords);
q.hiddenFieldDetail=[];
q.gridOptions.totalItems=q.totalItems;
if(!q.isGroupByCheck){q.UIPreviewData=angular.copy(q.previewData);
q.gridColumnDef=[];
angular.forEach(q.columns,function(z){z.total=undefined;
if(z.showTotal!==undefined&&z.showTotal===true){var y=0;
angular.forEach(q.previewData,function(B){var A=B[z.alias]===undefined?(B[z.fieldLabel]===undefined?0:B[z.fieldLabel]):B[z.alias];
y+=Number(A)
});
if(isNaN(y)){y=0
}z.total=y
}if(z.isHide===true){var w=(z.alias===undefined||z.alias==="")?z.fieldLabel:z.alias;
var v=[];
angular.forEach(q.previewData,function(C){var A=(C[w]===undefined||C[w]===null)?"N/A":C[w];
var B=false;
angular.forEach(v,function(D){if(D===A){B=true
}});
if(!B){v.push(A)
}});
var u="";
angular.forEach(v,function(A){if(u.length===0){u=A
}else{u+=","+A
}});
q.hiddenFieldDetail.push({label:w,value:u})
}if(z.componentType==="Currency"){if(q.currencyMap[z.associatedCurrency]!==undefined){z.currencySymbol=q.currencyMap[z.associatedCurrency].symbol;
z.format=q.currencyMap[z.associatedCurrency].format
}}var x;
switch(z.dataType){case"int8":x="number";
break;
case"varchar":x="string";
break;
case"timestamp":x="date";
break;
case"boolean":x="boolean";
break;
case"double precision":x="double";
break;
default:x="object";
break
}if(z.isHide!==true){q.gridColumnDef.push({name:z.alias,displayName:z.alias,type:x,enableHiding:false,cellTemplate:"<div class=\"ui-grid-cell-contents\" title=\"{{(COL_FIELD === null || COL_FIELD === '') ? 'N/A':COL_FIELD}}\">{{(COL_FIELD === null || COL_FIELD === \"\") ? 'N/A':COL_FIELD}}</div>",minWidth:200})
}})
}if(q.isGroupByCheck){q.groupRecordsForPreview(q.previewData)
}q.gridOptions.columnDefs=q.gridColumnDef;
q.gridOptions.data=q.UIPreviewData;
if(q.isFranchiseChange&&q.isFilter){q.applyFilter(q.filterForm,true);
q.isFranchiseChange=false
}else{if(q.gridApi.pagination.getPage()>q.gridApi.pagination.getTotalPages()){q.gridApi.pagination.seek(1)
}}}}},function(){o.addMessage("Some error occurred while retrieving data, try different fields and criteria",o.failure)
})
};
q.resetGrid=function(){q.gridOptions.paginationCurrentPage=1
};
q.$on("$viewContentLoaded",function(){e(function(){q.previewReport();
q.initializeFranchise()
},0)
});
q.availabeFilterTypesByDataType={int8:[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"},{id:"<",text:"less than"},{id:">",text:"greater than"},{id:"<=",text:"less than equal to"},{id:">=",text:"greater than equal to"},{id:"between",text:"in between"},{id:"is not null",text:"has any value"},{id:"is null",text:"has no value"}],varchar:[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"},{id:"in",text:"in"},{id:"not in",text:"not in"},{id:"like",text:"like"},{id:"is not null",text:"has any value"},{id:"is null",text:"has no value"}],timestamp:[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"},{id:"<",text:"less than"},{id:">",text:"greater than"},{id:"between",text:"in between"},{id:"is not null",text:"has any value"},{id:"is null",text:"has no value"}],"double precision":[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"},{id:"<",text:"less than"},{id:">",text:"greater than"},{id:"<=",text:"less than equal to"},{id:">=",text:"greater than equal to"},{id:"between",text:"in between"},{id:"is not null",text:"has any value"},{id:"is null",text:"has no value"}],"boolean":[{id:"=",text:"equal to"},{id:"is not null",text:"has any value"},{id:"is null",text:"has no value"}]};
q.previewReport=function(){if(localStorage.getItem("menuReportId")!=="null"&&localStorage.getItem("menuReportId")!==""){var r=localStorage.getItem("menuReportId");
q.currentPage=1;
q.clearReportData();
q.retrieveReport(r)
}else{h.path("/accessdenied")
}};
q.setPage=function(r){q.currentPage=r;
q.pageChanged()
};
q.pageChanged=function(){q.retrievePaginatedData()
};
q.removeFilterByColumn=function(r,s){q.subbmited=false;
var t=q.filterAttributes[r];
q.filterAttributes.splice(r,1);
q.filterColumns.push({label:t.label,type:t.type,componentType:t.componentType});
q.applyFilter(s)
};
q.clearReportData=function(){g.clearReportData(function(){},function(){})
};
q.user={};
q.user.username="prabhat";
q.user.password="testing123";
q.testLogin=function(u,r){var t="<title>Pentaho Business Analytics</title>";
var s="<title>Pentaho User Console</title>";
if(u.toString().indexOf(t)>=0||u.toString().indexOf(s)>=0){r.resolve(u)
}else{q.invalidCredential=true;
r.resolve(u)
}};
q.login=function(r){var t=$.param({j_username:q.user.username,j_password:q.user.password,_spring_security_remember_me:true});
var s={headers:{"Content-Type":"application/x-www-form-urlencoded; charset=UTF-8"}};
m.post(o.analyticsLoginUrl,t,s).success(function(w,u,x,v){q.testLogin(w,r)
}).error(function(w,u,x,v){o.addMessage("Unable to connect analytics server",o.failure);
r.resolve(w)
})
};
q.testPingResult=function(u,r){var t="<title>Pentaho Business Analytics</title>";
var s="<title>Pentaho User Console</title>";
if(u.toString().indexOf(t)>=0||u.toString().indexOf(s)>=0){r.resolve(u)
}else{q.login(r)
}};
q.pingAnalyticServer=function(){var r=k.defer();
m.get(o.analyticsPingUrl).success(function(u,s,v,t){q.testPingResult(u,r)
}).error(function(u,s,v,t){o.addMessage("Unable to connect analytics server",o.failure);
r.resolve(u)
});
return r.promise
};
q.retrieveReport=function(r){g.retrieveReport(r,function(s){q.report=s;
q.displayName=q.report.reportName;
if(q.report.externalReport===true){q.isExternalReport=true;
g.retrieveDashboardStatus(q.report.id,function(t){q.report.isDashboard=t.data
},function(t){});
g.retrieveAnalyticsCrendentials(function(u){if(u.data!==undefined&&u.data!==null&&u.data.hasOwnProperty("ANALYTICS_ENGINE_USERNAME")){q.user.username=u.data.ANALYTICS_ENGINE_USERNAME;
q.user.password=u.data.ANALYTICS_ENGINE_PWD;
var t=q.pingAnalyticServer();
t.then(function(v){q.iframeUrl=n.trustAsResourceUrl(q.report.query)
})
}else{o.addMessage("Analytics credentials are not set, contact administrator",o.failure)
}},function(t){})
}else{q.isExternalReport=false
}q.retrieveReportById(q.report)
})
};
function i(r,s){return r.fieldSequence-s.fieldSequence
}q.retrieveViewCurrencyDataRightsForUser=function(){g.retrieveViewCurrencyDataRightsOfLoggedInUser({},function(r){q.viewCurrencyDataPermission=r.data
})
};
q.retrieveViewCurrencyDataRightsForUser();
q.retrieveReportById=function(r){if(!q.isExternalReport){g.retrieveCurrencyConfiguration({},function(s){q.isCurrencyVisible=s.data;
q.modifiedCurrencyColumns=[];
r.columns.sort(i);
if(s.data===false||(s.data===true&&q.viewCurrencyDataPermission===false)){angular.forEach(r.columns,function(u){if(u.componentType!=="Currency"){q.modifiedCurrencyColumns.push(u)
}})
}else{q.modifiedCurrencyColumns=angular.copy(r.columns)
}q.columns=angular.copy(q.modifiedCurrencyColumns);
angular.forEach(q.columns,function(u){u.fieldDisplayName=o.translateValue("RPRT_NM."+u.alias)
});
q.report=angular.copy(r);
q.models={selected:null,templates:[{type:"container",id:2,text:"New-Table",columns:[[{text:"Column",colField:"column",feature:0}]],searchData:[[{column:""}]]}],dropzones:{A:[]}};
angular.forEach(q.report.columns,function(u){if(q.models.dropzones.A!=undefined){var w=0;
angular.forEach(q.models.dropzones.A,function(y){var x=u.tableName.split(",");
if(x.indexOf(y.text)!==-1){w++;
y.columns[0].push({text:u.alias,colField:u.dbBaseName+"."+u.colName,newField:true,type:"col",feature:u.feature})
}});
if(w==0){var v=u.tableName.split(",");
angular.forEach(v,function(x){q.obj={type:"container",allowedTypes:["col"],id:1,columns:[[]],searchData:[[{column:""}]]};
q.obj.text=x;
var y={text:u.alias,colField:u.dbBaseName+"."+u.colName,newField:true,type:"col",feature:u.feature};
q.obj.columns[0].push(y);
q.models.dropzones.A.push(q.obj)
})
}}});
q.displayName=q.report.reportName;
q.convertedQuery=r.query;
if(!q.isFranchiseChange){q.filterColumns=[];
q.filterAttributes=[];
q.initializeFilter();
q.initializeColor()
}o.maskLoading();
q.report.convertedQuery=q.convertedQuery;
q.report.franchiseIds=[];
if(q.franchise!==undefined&&q.franchise!==null){q.report.franchiseIds.push(q.franchise)
}if(q.report.orderAttributes!==undefined&&q.report.orderAttributes!==null){var t=JSON.parse(q.report.orderAttributes);
angular.forEach(t,function(u){angular.forEach(q.columns,function(v){var w;
if(v.alias===undefined||v.alias===""){w=v.fieldLabel
}else{w=v.alias
}if(u.columnName===v.dbBaseName+"."+v.colName){q.columnOrderMap[w]=u.orderValue
}})
})
}q.currencyIdList=[];
angular.forEach(q.columns,function(u){if(u.componentType==="Currency"){q.currencyIdList.push(u.associatedCurrency)
}});
g.retrieveReportTable(q.report,function(u){if(u.data!==undefined){if(u.data.queryValid!==undefined){var w="Invalid Query.";
var v=o.failure;
o.addMessage(w,v);
o.unMaskLoading()
}else{q.resultModels=angular.copy(q.models);
if(q.resultModels.dropzones.A!=undefined&&q.resultModels.dropzones.A.length>0&&u.data!=undefined){angular.forEach(q.resultModels.dropzones.A,function(x){x.searchData[0]=[];
if(x.columns[0].length>0){angular.forEach(u.data.records,function(y){var z={};
angular.forEach(x.columns[0],function(A){if(y[A.text]!==undefined){if(y[A.text]!=null){z[A.colField]={value:y[A.text]}
}else{z[A.colField]={value:"N/A"}
}}});
if(z!=null&&z!==undefined){x.searchData[0].push(z)
}})
}})
}o.unMaskLoading()
}}q.isFilter=true;
if(!q.isFranchiseChange){q.retrievePaginatedData()
}else{q.resetGrid()
}o.unMaskLoading()
},function(){o.unMaskLoading()
})
},function(s){o.addMessage("Some error occurred while retrieving data, try different fields and criteria",o.failure)
})
}};
q.initializeFilter=function(){angular.forEach(q.columns,function(t){var s;
if(t.alias!==null&&t.alias!==""){s=t.alias
}else{s=t.fieldLabel
}var r=t.dataType;
q.filterColumns.push({label:s,type:r,componentType:t.componentType})
})
};
q.updateFilter=function(t){q.subbmited=false;
if(t!==undefined){var r=q.availabeFilterTypesByDataType[t.type]===undefined?[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"}]:q.availabeFilterTypesByDataType[t.type];
q.filterTypes=[];
angular.forEach(r,function(u){q.filterTypes.push({id:u.id,text:u.text})
});
var s={};
s.label=t.label;
s.type=t.type;
s.componentType=t.componentType;
s.filters=q.filterTypes;
if(s.type==="varchar"){q.columnData=[];
s.availableFilterValueSelectEqual={multiple:false,placeholder:"  Filter Value",closeOnSelect:true,initSelection:function(u,w){var v=undefined;
w(v)
},query:function(w){var v=w.page-1;
var x={q:w.term,page_limit:10,page:v,col_name:s.label,isGrouped:q.isGroupByCheck};
q.names=[];
var y=function(A){q.names=[];
if(A.length!==0){q.names=[];
angular.forEach(A.columnValues,function(B){q.names.push({id:B,text:B})
})
}var z=(v*10)<A.total;
w.callback({results:q.names,more:z})
};
var u=function(){};
g.retrieveLimitedColumnValues(x,y,u)
}};
s.availableFilterValueSelectNotEqual={multiple:false,placeholder:"  Filter Value",closeOnSelect:true,initSelection:function(u,w){var v=undefined;
w(v)
},query:function(w){var v=w.page-1;
var x={q:w.term,page_limit:10,page:v,col_name:s.label,isGrouped:q.isGroupByCheck};
q.names=[];
var y=function(A){q.names=[];
if(A.length!==0){q.names=[];
angular.forEach(A.columnValues,function(B){q.names.push({id:B,text:B})
})
}var z=(v*10)<A.total;
w.callback({results:q.names,more:z})
};
var u=function(){};
g.retrieveLimitedColumnValues(x,y,u)
}};
s.availableFilterValueSelectIn={multiple:true,placeholder:"  Filter Value",closeOnSelect:false,initSelection:function(u,w){var v=[];
w(v)
},query:function(w){var v=w.page-1;
var x={q:w.term,page_limit:10,page:v,col_name:s.label,isGrouped:q.isGroupByCheck};
q.names=[];
var y=function(A){q.names=[];
if(A.length!==0){q.names=[];
angular.forEach(A.columnValues,function(B){q.names.push({id:B,text:B})
})
}var z=(v*10)<A.total;
w.callback({results:q.names,more:z})
};
var u=function(){};
g.retrieveLimitedColumnValues(x,y,u)
}};
s.availableFilterValueSelectNotIn={multiple:true,placeholder:"  Filter Value",closeOnSelect:false,initSelection:function(u,w){var v=[];
w(v)
},query:function(w){var v=w.page-1;
var x={q:w.term,page_limit:10,page:v,col_name:s.label,isGrouped:q.isGroupByCheck};
q.names=[];
var y=function(A){q.names=[];
if(A.length!==0){q.names=[];
angular.forEach(A.columnValues,function(B){q.names.push({id:B,text:B})
})
}var z=(v*10)<A.total;
w.callback({results:q.names,more:z})
};
var u=function(){};
g.retrieveLimitedColumnValues(x,y,u)
}}
}q.filterAttributes.push(s);
angular.forEach(q.filterColumns,function(v,u){if(v.label===t.label){q.filterColumns.splice(u,1)
}})
}};
q.applyFilter=function(v,s){q.filterForm=v;
if((v!==undefined&&v.$valid)||(s!==undefined&&s===true)){o.maskLoading();
q.filter={};
q.isAnyColumnHidden=false;
q.hiddenFieldDetail=[];
angular.forEach(q.columns,function(w){w.isHide=false
});
var t=false;
if(q.columns.length===q.filterAttributes.length){var u=0;
angular.forEach(q.filterAttributes,function(w){if(w.hideColumn!==undefined&&w.hideColumn===true){u++
}});
if(u===q.columns.length){t=true
}}if(!t){angular.forEach(q.filterAttributes,function(y){if(y.type==="timestamp"){var A=new Date(y.filterValue);
var B;
var z;
var w;
if((A.getMonth()+1)<10){z="0"+(A.getMonth()+1)
}else{z=(A.getMonth()+1)
}if(A.getDate()<10){w="0"+A.getDate()
}else{w=A.getDate()
}B=A.getFullYear()+"-"+z+"-"+w;
y.filterValue=B;
if(y.filterValueSecond!==undefined){A=new Date(y.filterValueSecond);
if((A.getMonth()+1)<10){z="0"+(A.getMonth()+1)
}else{z=(A.getMonth()+1)
}if(A.getDate()<10){w="0"+A.getDate()
}else{w=A.getDate()
}B=A.getFullYear()+"-"+z+"-"+w;
y.filterValueSecond=B
}}var x=y.label;
q.filterObj={operator:y.operator,type:y.type,componentType:y.componentType,filterValue:y.filterValue,filterValueSecond:y.filterValueSecond};
q.filter[y.label]=JSON.stringify(q.filterObj);
if(y.hideColumn!==undefined&&y.hideColumn===true){angular.forEach(q.columns,function(D){var C;
if(D.alias!==null&&D.alias!==""){C=D.alias
}else{C=D.fieldLabel
}if(C===y.label){D.isHide=true;
q.isAnyColumnHidden=true
}})
}});
var r="";
if(q.groupAttributes!==undefined){angular.forEach(q.groupAttributes.groupBy,function(w){if(w.level===1){r=w.fields
}})
}q.send={filters:q.filter,isGrouped:q.isGroupByCheck,groupBy:q.groupAttributes===undefined?null:r};
q.currentPage=1;
g.retrieveFilteredData(q.send,function(w){q.isFilter=false;
q.previewData=w.data.records;
if(q.paginationOptions.pageNumber===1){q.getPage()
}else{q.resetGrid()
}o.unMaskLoading()
},function(){q.currentPage=1;
o.addMessage("Some error occurred while retrieving data, try different fields and criteria",o.failure);
o.unMaskLoading()
})
}else{o.addMessage("Can not hide all fields.",o.failure);
o.unMaskLoading()
}}};
q.generateFilteredPdf=function(){var s;
s=q.tempData;
q.map={records:s};
var t=[];
if(q.colorConfigData.length>0){angular.forEach(q.colorConfigData,function(w){var v=[];
var u={};
u.combinationType=w.combinationType;
u.colorName=w.colorName;
angular.forEach(w.columns,function(x){if(q.checkIsValidColorConfig(x)){var C={};
C.label=x.label;
var A=false;
angular.forEach(q.levelMap,function(G){var F=G.levelItems.split(",");
if(F.indexOf(x.label)>-1){A=true
}});
C.isGroupBy=A;
C.type=x.type;
C.componentType=x.componentType;
C.operator=x.operator;
if(x.filterValue instanceof Object===true){if(angular.isArray(x.filterValue)){var y="";
angular.forEach(x.filterValue,function(F){if(y.length===0){y=F.id
}else{y+=","+F.id
}});
C.filterValue=y
}else{if(C.type!=="timestamp"){C.filterValue=x.filterValue.id
}else{if(x.filterValue!==undefined){var D=new Date(x.filterValue);
var E;
var B;
var z;
if((D.getMonth()+1)<10){B="0"+(D.getMonth()+1)
}else{B=(D.getMonth()+1)
}if(D.getDate()<10){z="0"+D.getDate()
}else{z=D.getDate()
}E=D.getFullYear()+"-"+B+"-"+z;
C.filterValue=E;
if(x.filterValueSecond!==undefined){D=new Date(x.filterValueSecond);
if((D.getMonth()+1)<10){B="0"+(D.getMonth()+1)
}else{B=(D.getMonth()+1)
}if(D.getDate()<10){z="0"+D.getDate()
}else{z=D.getDate()
}E=D.getFullYear()+"-"+B+"-"+z;
C.filterValueSecond=E
}}}}}else{C.filterValue=x.filterValue
}if(x.filterValueSecond!==undefined&&x.filterValueSecond!==null&&C.type!=="timestamp"){C.filterValueSecond=x.filterValueSecond
}v.push(C)
}});
u.columns=v;
if(v.length>0){t.push(u)
}})
}var r=[];
if(q.hiddenFieldDetail.length>0){angular.forEach(q.hiddenFieldDetail,function(u){r.push(u.label)
})
}q.send={records:q.map,reportId:q.report.id,extension:".pdf",filterAttributes:q.filterAttributes,colorAttributes:t,hiddenFields:r};
o.maskLoading();
g.generateFilteredPdf(q.send,function(){window.location.href=o.appendAuthToken(o.apipath+"report/downloadfilteredpdf?reportId="+q.report.id+"&extension=.pdf");
o.unMaskLoading()
})
};
q.generateFilteredXls=function(){var s;
s=q.tempData;
q.map={records:s};
var t=[];
if(q.colorConfigData.length>0){angular.forEach(q.colorConfigData,function(w){var v=[];
var u={};
u.combinationType=w.combinationType;
u.colorName=w.colorName;
angular.forEach(w.columns,function(x){if(q.checkIsValidColorConfig(x)){var A={};
A.label=x.label;
var z=false;
angular.forEach(q.levelMap,function(C){var B=C.levelItems.split(",");
if(B.indexOf(x.label)>-1){z=true
}});
A.isGroupBy=z;
A.type=x.type;
A.componentType=x.componentType;
A.operator=x.operator;
if(x.filterValue instanceof Object===true){if(angular.isArray(x.filterValue)){var y="";
angular.forEach(x.filterValue,function(B){if(y.length===0){y=B.id
}else{y+=","+B.id
}});
A.filterValue=y
}else{A.filterValue=x.filterValue.id
}}else{A.filterValue=x.filterValue
}if(x.filterValueSecond!==undefined||x.filterValueSecond!==null){A.filterValueSecond=x.filterValueSecond
}v.push(A)
}});
u.columns=v;
if(v.length>0){t.push(u)
}})
}var r=[];
if(q.hiddenFieldDetail.length>0){angular.forEach(q.hiddenFieldDetail,function(u){r.push(u.label)
})
}q.send={records:q.map,reportId:q.report.id,extension:".xls",filterAttributes:q.filterAttributes,colorAttributes:t,hiddenFields:r};
o.maskLoading();
g.generateFilteredPdf(q.send,function(){window.location.href=o.appendAuthToken(o.apipath+"report/downloadfilteredpdf?reportId="+q.report.id+"&extension=.xls");
o.unMaskLoading()
})
};
q.retrievePaginatedData=function(){o.maskLoading();
q.previewData=[];
q.isPreview=true;
q.tempData=[];
q.isGroupedReport();
q.send={offSet:q.paginationOptions.pageNumber,limit:q.paginationOptions.pageSize,isFilter:q.isFilter,isGrouped:q.isGroupByCheck,sortColumn:q.paginationOptions.sortColumn,sortDirection:q.paginationOptions.sortDirection,sortColumnType:q.paginationOptions.sortColumnType,filterOptions:q.filterOptions};
g.retrievePaginatedData(q.send,function(r){if(r.data!==undefined){if(r.data.queryValid!==undefined){var t="Invalid Query.";
var s=o.failure;
o.addMessage(t,s);
o.unMaskLoading()
}else{q.previewData=r.data.records;
q.totalItems=(r.data.totalRecords);
q.hiddenFieldDetail=[];
q.gridOptions.totalItems=q.totalItems;
if(q.totalItems>0){q.isAnyRecordsExist=true
}if(!q.isGroupByCheck){q.UIPreviewData=angular.copy(q.previewData);
q.gridColumnDef=[];
angular.forEach(q.columns,function(z){z.total=undefined;
if(z.showTotal!==undefined&&z.showTotal===true){var y=0;
angular.forEach(q.previewData,function(B){var A=B[z.alias]===undefined?(B[z.fieldLabel]===undefined?0:B[z.fieldLabel]):B[z.alias];
y+=Number(A)
});
if(isNaN(y)){y=0
}z.total=y
}if(z.isHide===true){var w=(z.alias===undefined||z.alias==="")?z.fieldLabel:z.alias;
var v=[];
angular.forEach(q.previewData,function(C){var A=(C[w]===undefined||C[w]===null)?"N/A":C[w];
var B=false;
angular.forEach(v,function(D){if(D===A){B=true
}});
if(!B){v.push(A)
}});
var u="";
angular.forEach(v,function(A){if(u.length===0){u=A
}else{u+=","+A
}});
q.hiddenFieldDetail.push({label:w,value:u})
}if(z.componentType==="Currency"){if(q.currencyMap[z.associatedCurrency]!==undefined){z.currencySymbol=q.currencyMap[z.associatedCurrency].symbol;
z.format=q.currencyMap[z.associatedCurrency].format
}}var x;
switch(z.dataType){case"int8":x="number";
break;
case"varchar":x="string";
break;
case"timestamp":x="date";
break;
case"boolean":x="boolean";
break;
case"double precision":x="double";
break;
default:x="object";
break
}q.gridColumnDef.push({name:z.alias,displayName:z.alias,type:x,enableHiding:false,cellTemplate:"<div class=\"ui-grid-cell-contents\" title=\"{{(COL_FIELD === null || COL_FIELD === '') ? 'N/A':COL_FIELD}}\">{{(COL_FIELD === null || COL_FIELD === \"\") ? 'N/A':COL_FIELD}}</div>",minWidth:200})
});
q.gridOptions.columnDefs=q.gridColumnDef;
q.gridOptions.data=q.UIPreviewData
}if(q.isGroupByCheck){q.groupRecordsForPreview(q.previewData)
}else{if(!q.isEditModeColorInitialized){q.updateColorConfiguration()
}}o.unMaskLoading()
}}if(q.isFranchiseChange&&q.isFilter){q.applyFilter(q.filterForm,true)
}o.unMaskLoading()
},function(){o.addMessage("Some error occurred while retrieving data, try different fields and criteria",o.failure);
o.unMaskLoading()
})
};
q.isGroupedReport=function(){if(q.report.groupAttributes!==undefined&&q.report.groupAttributes!==null){q.isGroupByCheck=true
}else{q.isGroupByCheck=false
}};
q.groupRecordsForPreview=function(s){q.columns.sort(i);
q.modifiedColumns=angular.copy(q.columns);
if(q.isGroupByCheck&&s!==undefined){var x=[];
var t=0;
angular.forEach(s,function(z,y){x.push(z)
});
q.previewData=angular.copy(x);
var w=[];
var v=[];
q.groupItemListForPreviw=[];
q.groupAttributes=JSON.parse(q.report.groupAttributes);
if(q.groupAttributes.groupBy!==undefined&&q.groupAttributes.groupBy.length>0){q.groupAttributes.groupBy.sort(b);
var u=0;
q.levelMap=[];
angular.forEach(q.groupAttributes.groupBy,function(y){q.levelMap.push({level:y.level,levelItems:y.fields});
u++;
var z=y.fields.split(",");
angular.forEach(z,function(A){angular.forEach(q.modifiedColumns,function(C,B){var D;
if(C.alias===undefined||C.alias===""){D=C.fieldLabel
}else{D=C.alias
}if(D===A){C.level=y.level;
w.push(C);
v.push(C);
q.modifiedColumns.splice(B,1)
}})
})
});
q.totalLevel=u;
var r=[];
angular.forEach(q.modifiedColumns,function(y){var z;
if(y.alias===undefined||y.alias===""){z=y.fieldLabel
}else{z=y.alias
}r.push(z)
});
angular.forEach(q.levelMap,function(z){var y=z.levelItems.split(",");
angular.forEach(y,function(A){angular.forEach(r,function(C,B){if(C===A){r.splice(B,1)
}})
})
});
q.UIPreviewData=angular.copy(q.previewData)
}if(q.groupAttributes.groups!==undefined&&q.groupAttributes.groups.length>0){q.groupAttributes.groups.sort(b);
if(q.groupAttributes.groups.length>1){angular.forEach(q.groupAttributes.groups,function(z){var C=z.groupItems.split(",");
var F=z.groupName;
var E={};
E.groupName=F;
E.isGroup=true;
var D=[];
angular.forEach(q.modifiedColumns,function(H){var G;
if(H.alias!==undefined&&H.alias!==""){G=H.alias
}else{G=H.fieldLabel
}angular.forEach(C,function(I){if(I===G&&(H.isHide===undefined||H.isHide===false)){q.groupItemListForPreviw.push(I);
D.push(I)
}})
});
E.groupItems=D;
angular.forEach(q.previewData,function(H){var G=[];
angular.forEach(H.groupRows,function(J){var I=[];
angular.forEach(D,function(K){var L=J[K]===undefined?"-":J[K];
I.push(L)
});
G.push(I)
});
H[F]=G
});
if(D.length>0){w.push(E)
}for(var y=q.modifiedColumns.length-1;
y>=0;
y--){var B=q.modifiedColumns[y];
var A;
if(B.alias!==undefined&&B.alias!==""){A=B.alias
}else{A=B.fieldLabel
}if(C.indexOf(A)>-1){v.push(q.modifiedColumns[y]);
q.modifiedColumns.splice(y,1)
}}})
}else{angular.forEach(q.groupAttributes.groups,function(z){var C=z.groupItems.split(",");
for(var y=q.modifiedColumns.length-1;
y>=0;
y--){var B=q.modifiedColumns[y];
var A;
if(B.alias!==undefined&&B.alias!==""){A=B.alias
}else{A=B.fieldLabel
}if(C.indexOf(A)>-1){v.push(q.modifiedColumns[y]);
q.modifiedColumns.splice(y,1)
}}})
}}if(q.modifiedColumns.length>0){angular.forEach(q.modifiedColumns,function(y){w.push(y);
v.push(y)
})
}w.sort(i);
v.sort(i);
q.modifiedColumns=angular.copy(w);
q.columns=angular.copy(v);
q.gridColumnDef=[];
angular.forEach(q.columns,function(D){D.total=undefined;
if(D.showTotal!==undefined&&D.showTotal===true){var C=0;
angular.forEach(q.previewData,function(E){var F=E[D.alias]===undefined?(E[D.fieldLabel]===undefined?0:E[D.fieldLabel]):E[D.alias];
C+=Number(F)
});
if(isNaN(C)){C=0
}D.total=C
}if(D.isHide===true){var A=(D.alias===undefined||D.alias==="")?D.fieldLabel:D.alias;
var z=[];
angular.forEach(q.previewData,function(G){var E=(G[A]===undefined||G[A]===null)?"N/A":G[A];
var F=false;
angular.forEach(z,function(H){if(H===E){F=true
}});
if(!F){z.push(E)
}});
var y="";
angular.forEach(z,function(E){if(y.length===0){y=E
}else{y+=","+E
}});
q.hiddenFieldDetail.push({label:A,value:y})
}var B;
switch(D.dataType){case"int8":B="number";
break;
case"varchar":B="string";
break;
case"timestamp":B="date";
break;
case"boolean":B="boolean";
break;
case"double precision":B="double";
break;
default:B="object";
break
}if(D.isHide!==true){q.gridColumnDef.push({name:D.alias,displayName:D.alias,type:B,enableHiding:false,cellTemplate:"<div class=\"ui-grid-cell-contents\" title=\"{{(COL_FIELD === null || COL_FIELD === '') ? 'N/A':COL_FIELD}}\">{{(COL_FIELD === null || COL_FIELD === \"\") ? 'N/A':COL_FIELD}}</div>",minWidth:200})
}});
q.gridOptions.columnDefs=q.gridColumnDef;
q.gridOptions.data=q.UIPreviewData;
if(!q.isEditModeColorInitialized){q.updateColorConfiguration()
}}};
q.rowFormatter=function(s){var r="white";
if(q.colorConfigData.length>0){angular.forEach(q.colorConfigData,function(t){if(t.columns.length>0){var u=q.executeFilterOnRow(s.entity,t);
if(u){r=t.colorName
}}})
}return r
};
q.repopulateData=function(r){q.franchise=r;
q.isFranchiseChange=true;
q.retrieveReportById(q.report)
};
q.initializeFranchise=function(){if(o.session!==undefined){if(o.session.isHKAdmin){j.retrieveAllFranchise({tree:false},function(t){var s=[];
angular.forEach(t,function(v){v.franchiseName=o.translateValue("FRNCSE_NM."+v.franchiseName);
s.push(v)
});
q.franchiseModelList=s;
q.existingFranchiseList=[];
var u=[];
for(var r=0;
r<s.length;
r++){q.existingFranchiseList.push({id:s[r].id,displayName:s[r].franchiseName});
u.push({id:s[r].id,text:s[r].franchiseName})
}q.isHKAdmin=true;
q.franchiseSelectCombo={multiple:true,placeholder:"Select Franchise",closeOnSelect:false,data:u}
},function(){o.addMessage("Failed to retrieve franchises",1)
})
}else{q.isHKAdmin=false
}}};
q.initializeColor=function(){q.colorColumns=[];
q.isGroupedReport();
if(!q.isGroupByCheck){angular.forEach(q.columns,function(v){var u;
if(v.alias!==null&&v.alias!==""){u=v.alias
}else{u=v.fieldLabel
}var t=v.dataType;
q.colorColumns.push({label:u,type:t,componentType:v.componentType})
})
}else{q.groupAttributes=JSON.parse(q.report.groupAttributes);
var r=[];
angular.forEach(q.groupAttributes.groupBy,function(t){if(t.level===1){r=t.fields.split(",")
}});
angular.forEach(r,function(t){angular.forEach(q.columns,function(w){var v;
if(w.alias!==null&&w.alias!==""){v=w.alias
}else{v=w.fieldLabel
}var u=w.dataType;
if(v===t){q.colorColumns.push({label:v,type:u,componentType:w.componentType})
}})
})
}q.colorConfigData=[];
var s={};
s.combinationType="ANY";
s.columns=[];
s.colorColumns=q.colorColumns;
s.combinationTypes=[{id:"ALL",text:"ALL"},{id:"ANY",text:"ANY"}];
q.colorConfigData.push(s)
};
q.updateColorConfiguration=function(){q.isEditModeColorInitialized=true;
if(q.report.colorAttributes!==undefined&&q.report.colorAttributes!==null&&q.report.colorAttributes!==""){var r=JSON.parse(q.report.colorAttributes);
if(r.length>0){q.colorConfigData=[];
angular.forEach(r,function(u){var t={};
t.combinationType=u.combinationType;
t.colorName=u.colorName;
t.colorColumns=q.colorColumns;
t.combinationTypes=[{id:"ALL",text:"ALL"},{id:"ANY",text:"ANY"}];
var s=[];
angular.forEach(u.columns,function(w){var y=false;
angular.forEach(q.colorColumns,function(A){if(A.label===w.label){y=true
}});
if(y){var z={};
z.label=w.label;
z.type=w.type;
z.componentType=w.componentType;
z.label=w.label;
var x=q.availabeFilterTypesByDataType[w.type]===undefined?[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"}]:q.availabeFilterTypesByDataType[w.type];
if(w.componentType==="Date range"){x=[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"}]
}z.filters=x;
z.filterValue=w.filterValue;
z.filterValueSecond=w.filterValueSecond;
var v;
angular.forEach(x,function(A){if(A.id===w.operator){v=A
}});
z.operator=v.id;
if(z.type==="varchar"){q.colorColumnData=[];
z.availableFilterValueSelectEqual={multiple:false,placeholder:"  Filter Value",closeOnSelect:true,initSelection:function(A,C){var B={};
B={id:w.filterValue,text:w.filterValue};
C(B)
},query:function(C){var B=C.page-1;
var D={q:C.term,page_limit:10,page:B,col_name:z.label,isGrouped:q.isGroupByCheck};
q.names=[];
var E=function(G){q.names=[];
if(G.length!==0){q.names=[];
angular.forEach(G.columnValues,function(H){q.names.push({id:H,text:H})
})
}var F=(B*10)<G.total;
C.callback({results:q.names,more:F})
};
var A=function(){};
g.retrieveLimitedColumnValues(D,E,A)
}};
z.availableFilterValueSelectNotEqual={multiple:false,placeholder:"  Filter Value",closeOnSelect:true,initSelection:function(A,C){var B={};
B={id:w.filterValue,text:w.filterValue};
C(B)
},query:function(C){var B=C.page-1;
var D={q:C.term,page_limit:10,page:B,col_name:z.label,isGrouped:q.isGroupByCheck};
q.names=[];
var E=function(G){q.names=[];
if(G.length!==0){q.names=[];
angular.forEach(G.columnValues,function(H){q.names.push({id:H,text:H})
})
}var F=(B*10)<G.total;
C.callback({results:q.names,more:F})
};
var A=function(){};
g.retrieveLimitedColumnValues(D,E,A)
}};
z.availableFilterValueSelectIn={multiple:true,placeholder:"  Filter Value",closeOnSelect:false,initSelection:function(A,C){var B=[];
angular.forEach(w.filterValue.split(","),function(D){B.push({id:D,text:D})
});
C(B)
},query:function(C){var B=C.page-1;
var D={q:C.term,page_limit:10,page:B,col_name:z.label,isGrouped:q.isGroupByCheck};
q.names=[];
var E=function(G){q.names=[];
if(G.length!==0){q.names=[];
angular.forEach(G.columnValues,function(H){q.names.push({id:H,text:H})
})
}var F=(B*10)<G.total;
C.callback({results:q.names,more:F})
};
var A=function(){};
g.retrieveLimitedColumnValues(D,E,A)
}};
z.availableFilterValueSelectNotIn={multiple:true,placeholder:"  Filter Value",closeOnSelect:false,initSelection:function(A,C){var B=[];
angular.forEach(w.filterValue.split(","),function(D){B.push({id:D,text:D})
});
C(B)
},query:function(C){var B=C.page-1;
var D={q:C.term,page_limit:10,page:B,col_name:z.label,isGrouped:q.isGroupByCheck};
q.names=[];
var E=function(G){q.names=[];
if(G.length!==0){q.names=[];
angular.forEach(G.columnValues,function(H){q.names.push({id:H,text:H})
})
}var F=(B*10)<G.total;
C.callback({results:q.names,more:F})
};
var A=function(){};
g.retrieveLimitedColumnValues(D,E,A)
}}
}s.push(z)
}});
t.columns=angular.copy(s);
q.colorConfigData.push(t)
})
}}};
q.updateColorColumnFilter=function(s,r){q.subbmited=false;
if(s!==undefined&&s!==null){var t=q.availabeFilterTypesByDataType[s.type]===undefined?[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"}]:q.availabeFilterTypesByDataType[s.type];
var u={};
u.label=s.label;
u.type=s.type;
u.componentType=s.componentType;
if(s.componentType==="Date range"){t=[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"}]
}u.filters=t;
if(s.type==="varchar"){q.colorColumnData=[];
u.availableFilterValueSelectEqual={multiple:false,placeholder:"  Filter Value",closeOnSelect:true,initSelection:function(v,x){var w=undefined;
x(w)
},query:function(x){var w=x.page-1;
var y={q:x.term,page_limit:10,page:w,col_name:u.label,isGrouped:q.isGroupByCheck};
q.names=[];
var z=function(B){q.names=[];
if(B.length!==0){q.names=[];
angular.forEach(B.columnValues,function(C){q.names.push({id:C,text:C})
})
}var A=(w*10)<B.total;
x.callback({results:q.names,more:A})
};
var v=function(){};
g.retrieveLimitedColumnValues(y,z,v)
}};
u.availableFilterValueSelectNotEqual={multiple:false,placeholder:"  Filter Value",closeOnSelect:true,initSelection:function(v,x){var w=undefined;
x(w)
},query:function(x){var w=x.page-1;
var y={q:x.term,page_limit:10,page:w,col_name:u.label,isGrouped:q.isGroupByCheck};
q.names=[];
var z=function(B){q.names=[];
if(B.length!==0){q.names=[];
angular.forEach(B.columnValues,function(C){q.names.push({id:C,text:C})
})
}var A=(w*10)<B.total;
x.callback({results:q.names,more:A})
};
var v=function(){};
g.retrieveLimitedColumnValues(y,z,v)
}};
u.availableFilterValueSelectIn={multiple:true,placeholder:"  Filter Value",closeOnSelect:false,initSelection:function(v,x){var w=[];
x(w)
},query:function(x){var w=x.page-1;
var y={q:x.term,page_limit:10,page:w,col_name:u.label,isGrouped:q.isGroupByCheck};
q.names=[];
var z=function(B){q.names=[];
if(B.length!==0){q.names=[];
angular.forEach(B.columnValues,function(C){q.names.push({id:C,text:C})
})
}var A=(w*10)<B.total;
x.callback({results:q.names,more:A})
};
var v=function(){};
g.retrieveLimitedColumnValues(y,z,v)
}};
u.availableFilterValueSelectNotIn={multiple:true,placeholder:"  Filter Value",closeOnSelect:false,initSelection:function(v,x){var w=[];
x(w)
},query:function(x){var w=x.page-1;
var y={q:x.term,page_limit:10,page:w,col_name:u.label,isGrouped:q.isGroupByCheck};
q.names=[];
var z=function(B){q.names=[];
if(B.length!==0){q.names=[];
angular.forEach(B.columnValues,function(C){q.names.push({id:C,text:C})
})
}var A=(w*10)<B.total;
x.callback({results:q.names,more:A})
};
var v=function(){};
g.retrieveLimitedColumnValues(y,z,v)
}}
}q.colorConfigData[r].columns.push(u);
q.colorConfigData[r].currentColumn=undefined
}};
q.removeColor=function(s,r){q.subbmited=false;
if(q.colorConfigData[r].columns.length>0){q.colorConfigData[r].columns.splice(s,1)
}if(q.colorConfigData[r].columns.length===0&&r!==0){q.removeColorCombination(r)
}};
q.removeColorCombination=function(r){q.subbmited=false;
if(q.colorConfigData.length===1){q.colorConfigData[0].combinationType="ANY";
q.colorConfigData[0].columns=[];
q.colorConfigData[0].colorColumns=q.colorColumns;
q.colorConfigData[0].combinationTypes=[{id:"ALL",text:"ALL"},{id:"ANY",text:"ANY"}];
q.colorConfigData[0].colorName=undefined
}else{q.colorConfigData.splice(r,1)
}};
q.addColorCombination=function(){q.subbmited=false;
if(q.colorConfigData[q.colorConfigData.length-1].columns.length>0){var r={};
r.combinationType="ANY";
r.columns=[];
r.colorColumns=q.colorColumns;
r.combinationTypes=[{id:"ALL",text:"ALL"},{id:"ANY",text:"ANY"}];
q.colorConfigData.push(r)
}else{o.addMessage("Select atleast one criteria field to add new.",o.failure)
}};
q.applyColor=function(t,s){if((t!==undefined&&t.$valid)||(s!==undefined&&s===true)){q.filterForm=t;
if(q.colorConfigData.length>0){if(q.previewData.length>0){if(!q.isGroupByCheck){var r=angular.copy(q.previewData);
angular.forEach(r,function(u,v){r[v].rowColor="white";
angular.forEach(u,function(x,w){r[v][w]={value:x,color:"white"}
})
});
angular.forEach(r,function(v,x){var u=false;
var w="white";
angular.forEach(q.colorConfigData,function(y){if(y.columns.length>0){var z=q.executeFilterOnRow(v,y);
if(z){u=true;
w=y.colorName
}}});
if(u){angular.forEach(v,function(z,y){r[x][y].color=w
});
r[x].rowColor=w
}});
q.UIPreviewData=angular.copy(r)
}else{var r=angular.copy(q.previewData);
angular.forEach(r,function(v,u){angular.forEach(v.groupRows,function(w,x){r[u].groupRows[x].rowColor="white";
angular.forEach(w,function(z,y){r[u].groupRows[x][y]={value:z,color:"white"}
})
})
});
angular.forEach(r,function(y,w){var v=y.groupRows;
var A=false;
var x;
q.groupAttributes=JSON.parse(q.report.groupAttributes);
var z;
angular.forEach(q.groupAttributes.groupBy,function(B){if(B.level===1){z=B.fields.split(",")
}});
var u={};
angular.forEach(z,function(B,D){var C;
angular.forEach(v,function(E){C=E[B]
});
u[B]=C
});
angular.forEach(q.colorConfigData,function(B){if(B.columns.length>0){var C=q.executeFilterOnRow(u,B);
if(C){A=true;
x=B.colorName
}}});
if(A){angular.forEach(y.groupRows,function(B,C){angular.forEach(B,function(E,D){r[w].groupRows[C][D].color=x
});
r[w].groupRows[C].rowColor=x
})
}});
q.UIPreviewData=angular.copy(r)
}}}else{if(!q.isGroupByCheck){angular.forEach(q.UIPreviewData,function(u,v){q.UIPreviewData[v].rowColor="white";
angular.forEach(u,function(x,w){q.UIPreviewData[v][w].color="white"
})
})
}else{angular.forEach(q.UIPreviewData,function(v,u){angular.forEach(v.value,function(x,w){q.UIPreviewData[u].groupRows[w].rowColor="white";
angular.forEach(x,function(z,y){q.UIPreviewData[u].groupRows[w][y].color="white"
})
})
})
}}}};
q.checkIsValidColorConfig=function(r){if(r!==undefined){if(r.operator===undefined||r.operator===null){return false
}if(r.filterValue===undefined&&r.operator!=="is null"&&r.operator!=="is not null"){return false
}if(r.operator==="between"&&(r.filterValueSecond===undefined||r.filterValueSecond===null)){return false
}return true
}return false
};
q.executeFilterOnRow=function(u,r){if(r.combinationType==="ANY"){var s=false;
angular.forEach(u,function(w,v){if(s===false){angular.forEach(r.columns,function(x){if(v===x.label&&s===false){if(q.checkIsValidColorConfig(x)){var z=angular.copy(x);
if(z.type==="timestamp"){var A=new Date(z.filterValue);
z.filterValue=new Date(A.getFullYear(),A.getMonth(),A.getDate());
if(z.filterValueSecond!==undefined){A=new Date(z.filterValueSecond);
z.filterValueSecond=new Date(A.getFullYear(),A.getMonth(),A.getDate())
}}var y=q.executeFilter(w,z);
if(y){s=true
}}}})
}});
return s
}else{if(r.columns.length>0){var t=true;
angular.forEach(r.columns,function(v){if(t===true){angular.forEach(u,function(x,w){if(w===v.label&&t===true){if(q.checkIsValidColorConfig(v)){var z=angular.copy(v);
if(z.type==="timestamp"){var A=new Date(z.filterValue);
z.filterValue=new Date(A.getFullYear(),A.getMonth(),A.getDate());
if(z.filterValueSecond!==undefined){A=new Date(z.filterValueSecond);
z.filterValueSecond=new Date(A.getFullYear(),A.getMonth(),A.getDate())
}}var y=q.executeFilter(x,z);
if(y===false){t=false
}}}})
}});
return t
}else{return false
}}};
q.executeFilter=function(A,z){if(((A!==undefined&&A!==null&&A!=="")||(z.type==="double precision")||(z.type==="int8"))&&(z.operator!=="is null"&&z.operator!=="is not null")){var u=false;
var x=[];
if(!(A!==undefined&&A!==null&&A!=="")){A=0
}if(z.type==="timestamp"){if(z.componentType!=="Date range"){var t=A.split("/");
A=new Date(parseInt(t[2]),parseInt(t[1])-1,parseInt(t[0]))
}else{var w=A.split("to");
var v=w[0].trim().split("/");
A=new Date(v[2],parseInt(v[1])-1,v[0]);
x.push(A);
var y=w[1].trim().split("/");
A=new Date(y[2],parseInt(y[1])-1,y[0]);
x.push(A)
}}else{if(z.componentType==="Currency"){A=parseFloat(A.split(" ")[0].trim())
}else{if(z.filterValue instanceof Object){if(!angular.isArray(z.filterValue)){z.filterValue=z.filterValue.id
}else{var B="";
angular.forEach(z.filterValue,function(C){if(B.length===0){B=C.id
}else{B+=","+C.id
}});
z.filterValue=B
}}}}switch(z.type){case"varchar":switch(z.operator){case"=":if(A===z.filterValue){u=true
}break;
case"!=":if(A!==z.filterValue){u=true
}break;
case"in":var r=z.filterValue.split(",");
if(r.indexOf(A)>-1){u=true
}break;
case"not in":var r=z.filterValue.split(",");
if(r.indexOf(A)===-1){u=true
}break;
case"like":z.filterValue="%"+z.filterValue+"%";
var s=z.filterValue.replace(/%/g,".*");
s=s.replace(/_/g,".");
if(A.match(s)){u=true
}break
}break;
case"int8":switch(z.operator){case"=":if(parseInt(A)===parseInt(z.filterValue)){u=true
}break;
case"!=":if(parseInt(A)!==parseInt(z.filterValue)){u=true
}break;
case">":if(parseInt(A)>parseInt(z.filterValue)){u=true
}break;
case">=":if(parseInt(A)>=parseInt(z.filterValue)){u=true
}break;
case"<":if(parseInt(A)<parseInt(z.filterValue)){u=true
}break;
case"<=":if(parseInt(A)<=parseInt(z.filterValue)){u=true
}break;
case"between":if(parseInt(A)>=parseInt(z.filterValue)&&parseInt(A)<=parseInt(z.filterValueSecond)){u=true
}break
}break;
case"double precision":switch(z.operator){case"=":if(parseFloat(A)===parseFloat(z.filterValue)){u=true
}break;
case"!=":if(parseFloat(A)!==parseFloat(z.filterValue)){u=true
}break;
case">":if(parseFloat(A)>parseFloat(z.filterValue)){u=true
}break;
case">=":if(parseFloat(A)>=parseFloat(z.filterValue)){u=true
}break;
case"<":if(parseFloat(A)<parseFloat(z.filterValue)){u=true
}break;
case"<=":if(parseFloat(A)<=parseFloat(z.filterValue)){u=true
}break;
case"between":if(parseFloat(A)>=parseFloat(z.filterValue)&&parseFloat(A)<=parseFloat(z.filterValueSecond)){u=true
}break
}break;
case"timestamp":if(z.componentType==="Date range"){if(z.operator==="="){if(x[0].getTime()<=z.filterValue.getTime()&&x[1].getTime()>=z.filterValue.getTime()){u=true
}}else{if(z.operator==="!="){if(x[0].getTime()>z.filterValue.getTime()||x[1].getTime()<z.filterValue.getTime()){u=true
}}}}else{switch(z.operator){case"=":if(A.getTime()===z.filterValue.getTime()){u=true
}break;
case"!=":if(A.getTime()!==z.filterValue.getTime()){u=true
}break;
case">":if(A.getTime()>z.filterValue.getTime()){u=true
}break;
case"<":if(A.getTime()<z.filterValue.getTime()){u=true
}break;
case"between":if(A.getTime()>=z.filterValue.getTime()&&A.getTime()<=z.filterValueSecond.getTime()){u=true
}break
}}break;
case"boolean":switch(z.operator){case"=":if(A===z.filterValue){u=true
}break;
case"!=":if(A!==z.filterValue){u=true
}break
}break
}return u
}else{if(z.operator==="is null"||z.operator==="is not null"){if((A===undefined||A===null||A==="")&&z.operator==="is null"){return true
}else{if(!(A===undefined||A===null||A==="")&&z.operator==="is not null"){return true
}else{return false
}}}else{return false
}}};
q.checkValidColorRange=function(t,s,w,y,x,r){x.$setValidity("invalidRange",true);
if(r!==undefined&&r!==null){r.$setValidity("invalidRange",true)
}var v;
var u;
if(t==="between"){if(w===undefined||w===null||w.length===0||y===undefined||y===null||y.length===0){return
}switch(s){case"int8":v=parseInt(w);
u=parseInt(y);
break;
case"double precision":v=parseFloat(w);
u=parseFloat(y);
break;
case"timestamp":v=angular.copy(new Date(w));
u=angular.copy(new Date(y));
break
}if(v>u){x.$setValidity("invalidRange",false);
r.$setValidity("invalidRange",false)
}}};
q.predicate=function(r){return function(s){return s[r]
}
};
q.applyMultipleOrdering=function d(v,u){var t=angular.copy(u);
var r=[];
if(v.value.length>0){var x=false;
if(q.columnOrderMap[t[0]]!==undefined&&q.columnOrderMap[t[0]]==="desc"){x=true
}v.value=l("orderBy")(v.value,q.predicate(t[0]),x);
var s="****NO MATCH****";
var w=[];
angular.forEach(v.value,function(y,A){var z="";
if(t.length>0){var B=t[0];
if(B.indexOf("-")===0){B=B.substr(1,B.length-1)
}if(z.length===0){if(y[B]===null||y[B]===undefined){z="null"
}else{z=y[B]
}}else{if(y[B]===null||y[B]===undefined){z+=",null"
}else{z+=","+y[B]
}}}if(s!==z){if(A!==0){var C={};
C.key=s;
C.value=angular.copy(w);
r.push(C)
}s=z;
w=[];
w.push(y)
}else{w.push(y)
}if(A===v.value.length-1){var D={};
D.key=s;
D.value=angular.copy(w);
r.push(D)
}});
t.splice(0,1);
if(t.length>0){angular.forEach(r,function(y){y.value=d(y,t)
})
}}return r
};
q.combineMultipleOrdering=function p(t,u){var r=u;
var s=[];
if(r>1){angular.forEach(t,function(w){var v=p(w.value,(r-1));
angular.forEach(v,function(x){s.push(x)
})
})
}else{angular.forEach(t,function(v){angular.forEach(v.value,function(w){s.push(w)
})
})
}return s
};
q.divideGroup=function(x,u,r){var t=[];
if(x.value.length>0){var w=u.split(",");
var s=q.applyMultipleOrdering(x,w);
x.value=q.combineMultipleOrdering(s,w.length);
var v="****NO MATCH****";
var y=[];
angular.forEach(x.value,function(z,B){var A="";
if(w.length>0){angular.forEach(w,function(E){if(E.indexOf("-")===0){E=E.substr(1,E.length-1)
}if(A.length===0){if(z[E]===null||z[E]===undefined){A="null"
}else{A=z[E]
}}else{if(z[E]===null||z[E]===undefined){A+=",null"
}else{A+=","+z[E]
}}})
}if(v!==A){if(B!==0){var C={};
C.key=v;
angular.forEach(r,function(F){if(q.columnOrderMap[F]!==undefined){var E=false;
if(q.columnOrderMap[F]==="desc"){E=true
}y=l("orderBy")(y,q.predicate(F),E)
}});
C.value=angular.copy(y);
t.push(C)
}v=A;
y=[];
y.push(z)
}else{y.push(z)
}if(B===x.value.length-1){var D={};
D.key=v;
angular.forEach(r,function(F){if(q.columnOrderMap[F]!==undefined){var E=false;
if(q.columnOrderMap[F]==="desc"){E=true
}y=l("orderBy")(y,q.predicate(F),E)
}});
D.value=angular.copy(y);
t.push(D)
}})
}return t
};
q.recursiveGroup=function f(s,u,r){var t;
if(u!==undefined){angular.forEach(q.levelMap,function(v){if(u===v.level){t=v.levelItems
}})
}angular.forEach(s,function(w){w.rowCount=w.value.length;
if(t!==undefined){var v=q.divideGroup(w,t,r);
if(u<=q.totalLevel){w.value=f(v,u+1,r)
}else{w.value=v
}}});
return s
};
q.combineRows=function(){angular.forEach(q.previewData,function(y){var z=[];
for(var x=1;
x<=y.rowCount;
x++){var r=y;
var t=2;
var w=x;
var A=[];
while(t<=q.totalLevel){var v=r.value;
var u=false;
var s=0;
angular.forEach(v,function(B){if(u===false){var C=B.rowCount;
if(s+C>=w){u=true;
t++;
r=B;
w-=s
}else{s+=C
}}})
}A=r.value[w-1];
z.push(A)
}y.groupRows=z
})
};
q.checkRowSpan=function(A,B,r,z){if(r!==undefined){var s=A;
var x=2;
var w=z+1;
var y=1;
if(r===1){y=A.rowCount
}while(r>=x){var v=s.value;
var u=false;
var t=0;
angular.forEach(v,function(C){if(u===false){var D=C.rowCount;
if(t+D>=w){u=true;
r--;
s=C;
w-=t;
y=C.rowCount
}else{t+=D
}}})
}return y
}else{return 1
}};
q.checkToRender=function(A,B,r,z){var y=q.checkRowSpan(A,B,r,z);
if(y===1){return true
}if(r!==undefined){var s=A;
var x=2;
var w=z+1;
while(r>=x){var v=s.value;
var u=false;
var t=0;
angular.forEach(v,function(C){if(u===false){var D=C.rowCount;
if(t+D>=w){u=true;
r--;
s=C;
w-=t
}else{t+=D
}}})
}if((w-1)%y===0){return true
}else{return false
}}else{return true
}};
q.applyAll=function(r){q.subbmited=true;
if(r!==undefined&&r.$valid){q.filterForm=r;
if(q.filterAttributes.length>0){q.applyFilter(r)
}else{}}};
function b(r,s){return r.sequence-s.sequence
}q.notification={};
q.endRepeat={};
q.submitted=false;
q.popover="<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td> \n '@D'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Departments</td></tr> <tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr> <tr><td>'@R'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Roles</td></tr> <tr><td>'@G'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Groups</td></tr> <tr><td>'@A'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Activities</td></tr> </table> ";
q.minEndRepeatDate=o.getCurrentServerDate();
q.minDueDate=o.getCurrentServerDate();
q.operationFlag=="add";
q.hstep=1;
q.mstep=1;
q.options={hstep:[1,2,3],mstep:[1,5,10,15,25,30]};
q.ismeridian=true;
q.datePicker={};
q.openDatePicker=function(r,s){r.preventDefault();
r.stopPropagation();
q.datePicker[s]=true
};
q.setEditTaskEndRepeatMinDate=function(){q.minEndRepeatDate=o.getCurrentServerDate()
};
q.showSchedulerMode=function(){q.endRepeat={};
q.notification={};
q.notification.reportId=q.report.id;
q.notification.atTime=o.getCurrentServerDate();
q.notification.repeatativeMode="D";
q.notification.endRepeatMode="OD";
q.isScheduler=true;
q.weekList=[{code:"M",value:"2"},{code:"Tu",value:"3"},{code:"W",value:"4"},{code:"Th",value:"5"},{code:"F",value:"6"},{code:"Sa",value:"7"},{code:"Su",value:"1"}];
q.dayList=new Array();
for(var r=0;
r<31;
r++){q.dayList.push(r+1)
}g.retrieveEmailConfiguration(q.report.id,function(s){if(s.data===null){q.operationFlag="add"
}else{q.operationFlag="edit";
q.notification=angular.copy(s.data);
if(q.notification.endDate!==null&&q.notification.endDate!==undefined){q.notification.endDate=new Date(q.notification.endDate)
}q.setModelValuesInUI()
}},function(s){})
};
q.cancelNotification=function(r){q.notification={};
q.notificationToSave={};
q.endRepeat={};
q.submitted=false;
q.recipients=[];
r.$setPristine();
q.isScheduler=false
};
q.clearFromRadio=function(r){if(q.notification.endRepeatMode==="OD"){q.endRepeat.afterDaysUnits="";
q.endRepeat.afterRepititionsUnits=""
}else{if(q.notification.endRepeatMode==="AD"){q.notification.endDate=null;
q.endRepeat.afterRepititionsUnits=""
}else{if(q.notification.endRepeatMode==="AR"){q.notification.endDate=null;
q.endRepeat.afterDaysUnits=""
}}}};
q.setModelValues=function(){if(q.notificationToSave.repeatativeMode==="W"){q.notificationToSave.weeklyOnDays="";
q.notificationToSave.monthlyOnDay=null;
angular.forEach(q.weekList,function(s,r){if(s.isChecked){q.notificationToSave.weeklyOnDays+=s.value;
q.notificationToSave.weeklyOnDays+="|"
}});
q.notificationToSave.weeklyOnDays=q.notificationToSave.weeklyOnDays.substring(0,q.notificationToSave.weeklyOnDays.length-1)
}else{if(q.notificationToSave.repeatativeMode==="M"){q.notificationToSave.weeklyOnDays=null
}}if(q.notificationToSave.endRepeatMode==="AD"){q.notificationToSave.afterUnits=q.endRepeat.afterDaysUnits
}else{if(q.notificationToSave.endRepeatMode==="AR"){q.notificationToSave.afterUnits=q.endRepeat.afterRepititionsUnits
}}};
q.setModelValuesInUI=function(){if(q.notification.repeatativeMode==="W"){var s=q.notification.weeklyOnDays.split("|");
var r=0;
angular.forEach(q.weekList,function(u,t){if(u.value===s[r]){u.isChecked="true";
r++
}})
}else{if(q.notification.repeatativeMode==="D"){q.notification.weeklyOnDays=null
}}if(q.notification.endRepeatMode==="AD"){q.endRepeat.afterDaysUnits=q.notification.afterUnits
}else{if(q.notification.endRepeatMode==="AR"){q.endRepeat.afterRepititionsUnits=q.notification.afterUnits
}}};
q.saveEmailConfiguration=function(t){q.submitted=true;
q.notificationToSave=angular.copy(q.notification);
q.setModelValues();
if(t.$valid){var r=false;
if(q.notificationToSave.xlsAttachment===true||q.notificationToSave.pdfAttachment===true){r=true
}if(r){if(q.operationFlag=="add"){o.maskLoading();
g.saveEmailConfiguration(q.notificationToSave,function(w){o.unMaskLoading();
q.notification={};
q.notificationToSave={};
q.endRepeat={};
q.submitted=false;
var x="Email configured successfully";
var v=o.success;
o.addMessage(x,v);
q.operationFlag="add";
q.cancelNotification(t);
t.$setPristine()
},function(){o.unMaskLoading();
var w="Error while creating email configuration";
var v=o.failure;
o.addMessage(w,v)
})
}else{if(q.operationFlag=="edit"){o.maskLoading();
g.saveEmailConfiguration(q.notificationToSave,function(){o.unMaskLoading();
q.notification={};
q.notificationToSave={};
q.endRepeat={};
q.submitted=false;
var w="Email configuration updated successfully";
var v=o.success;
o.addMessage(w,v);
q.operationFlag="add";
q.cancelNotification(t);
t.$setPristine()
},function(){o.unMaskLoading();
var w="Error while updating email configuration";
var v=o.failure;
o.addMessage(w,v)
})
}}}else{var u="Select atleast one attachment";
var s=o.failure;
o.addMessage(u,s)
}}else{console.log("Invalid...."+JSON.stringify(t))
}};
q.updateDashboardStatus=function(){var r={};
r.reportId=q.report.id;
r.dashboardStatus=q.report.isDashboard;
o.maskLoading();
g.updateDashboardStatus(r,function(s){o.unMaskLoading();
var u="Dashboard Status updated successfully";
var t=o.success
},function(s){o.unMaskLoading();
var u="Error in updating dashboard status";
var t=o.failure;
o.addMessage(u,t)
})
}
}])
});