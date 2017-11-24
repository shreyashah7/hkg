define(["hkg","reportBuilderService","selectize","manageMasterService","messageService","ngload!uiGrid","colorpicker.directive","addMasterValue"],function(a){a.register.controller("ReportBuilder",["$rootScope","$scope","$timeout","Messaging","$filter","$location","ReportBuilderService","ManageMasterService",function(o,s,d,n,m,h,g,r){s.entity="REPORTBUILDER.";
s.columns=[];
s.columnFormats=[];
s.rows=[];
s.featureList=[];
s.tempFeatureList=[];
s.fieldList=[];
s.totalFeatures=[];
s.subbmited=false;
s.orderFields=[];
s.primaryOrderFields=[];
s.secondaryOrderFields=[];
s.primaryOrder={};
s.secondaryOrder={};
s.primaryOrder.orderValue="asc";
s.secondaryOrder.orderValue="asc";
s.primaryOrder.isPrimaryOrderRequired=false;
s.selectedFeatures="";
s.models={};
s.selectedFields="";
s.AllFieldDetailList=[];
s.sectionList=[];
s.AllSectionDetailList=[];
s.isEditMode=false;
s.submitted=false;
s.columnsResponse=false;
s.orderSaved=false;
s.invalidColumn=false;
s.updateReportFlag=false;
s.isAnyRecordsExist=undefined;
s.order=[];
s.savedOrderMap=[];
s.localFilter=[];
s.selectedFeaturesList=[];
s.featureDetailsList=[];
s.innerDocumentBaseNameSet=["lot_status_history","lot_allotment_history","packet_status_history","packet_allotment_history","issue_status_history","plan_status_history"];
s.caratRaangeFieldSet=["carate_range_of_lot$DRP$Long","carate_range_of_packet$DRP$Long","carate_range_of_plan$DRP$Long"];
s.tabs=[{id:1},{id:2,disabled:true},{id:3,disabled:true},{id:4,disabled:true}];
s.joinTypes=["=","<",">","<=",">="];
s.availabeFilterTypesByDataType={int8:[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"},{id:"<",text:"less than"},{id:">",text:"greater than"},{id:"<=",text:"less than equal to"},{id:">=",text:"greater than equal to"},{id:"between",text:"in between"},{id:"is not null",text:"has any value"},{id:"is null",text:"has no value"}],varchar:[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"},{id:"in",text:"in"},{id:"not in",text:"not in"},{id:"like",text:"like"},{id:"is not null",text:"has any value"},{id:"is null",text:"has no value"}],timestamp:[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"},{id:"<",text:"less than"},{id:">",text:"greater than"},{id:"between",text:"in between"},{id:"is not null",text:"has any value"},{id:"is null",text:"has no value"}],"double precision":[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"},{id:"<",text:"less than"},{id:">",text:"greater than"},{id:"<=",text:"less than equal to"},{id:">=",text:"greater than equal to"},{id:"between",text:"in between"},{id:"is not null",text:"has any value"},{id:"is null",text:"has no value"}],"boolean":[{id:"=",text:"equal to"},{id:"is not null",text:"has any value"},{id:"is null",text:"has no value"}]};
s.filterValueNameMap={"=":"equal to","!=":"not equal to","<":"less than",">":"greater than","<=":"less than equal to",">=":"greater than equal to",between:"in between","in":"in","not in":"not in",like:"like","is null":"has no value","is not null":"has any value"};
s.skipOrderComponentTypes=["Image","Upload","UserMultiSelect","MultiSelect"];
s.dt={};
s.selectedId;
s.isGroupByCheck=false;
s.groups=[];
s.groupByColumnsList=[];
s.currentGroupIndex=0;
s.groupInfo={};
s.groupInfo.selectedGroup="";
s.groupInfo.groupName="";
s.groupInfo.groupByColumns="";
s.groupByLevels=[];
s.remainingFields=[];
s.groupByColumns=[];
s.groupsColumns=[];
s.otherColumns=[];
s.totalLevel=0;
s.levelMap=[];
s.columnOrderMap={};
s.currentColor={};
s.colorConfigData=[];
s.colorConfig={};
s.isColorInitialized=false;
s.colorApplied=false;
s.isEditModeColorInitialized=false;
s.isGroupInitialized=false;
s.isEditModeFilterInitialized=false;
s.isEditModeColumnsInitialized=false;
s.isEditModeOrderInitialized=false;
s.isEditModeGroupInitialized=false;
s.isGlobalFilterAdded=false;
s.reverse=false;
s.currencyMap={};
s.code="RG";
s.popover="<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td> \n '@D'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Departments</td></tr> <tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr> <tr><td>'@R'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Roles</td></tr> </table> ";
s.recipientMap={};
s.recipientMap.E="Employee";
s.recipientMap.D="Deparment";
s.recipientMap.R="Designation";
s.recipientMap.A="Activity";
s.recipientMap.F="Franchise";
s.recipientMap.X="Franchise Department";
s.recipientMap.G="Group";
s.recipientMap.S="Service";
s.recipientCodes=[];
s.checkEditMode=function(){var t=localStorage.getItem("reportId");
if(t!==null){s.selectedId=t;
s.isEditMode=true;
s.updateReportFlag=false
}else{s.isEditMode=false;
s.models={selected:null,templates:[{type:"container",id:0,text:"New-Table",columns:[[{text:"Column",colField:"column"}]],searchData:[[{column:""}]]}],dropzones:{A:[{type:"container",allowedTypes:["col"],id:1,text:"User",columns:[[{text:"Column",colField:"column"}]],searchData:[[{column:""}]]}]}}
}};
s.checkEditMode();
s.open=function(t,u,v){t.preventDefault();
t.stopPropagation();
u[v]=true
};
s.dateOptions={"year-format":"'yy'","starting-day":1};
s.format=o.dateFormat;
s.today=new Date();
s.paginationOptions={pageNumber:1,pageSize:50,sortDirection:null,sortColumn:null,sortColumnType:null};
s.filterOptions=[];
s.gridOptions={};
s.gridOptions.enableFiltering=true;
s.gridOptions.useExternalFiltering=true;
s.gridOptions.paginationPageSizes=[50,100];
s.gridOptions.paginationPageSize=50;
s.gridOptions.useExternalPagination=true;
s.gridOptions.useExternalSorting=true;
s.gridOptions.rowTemplate='<div ng-style="{ \'background-color\': grid.appScope.rowFormatter(row) }">  <div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }"  ui-grid-cell></div></div>';
s.gridOptions.columnDefs=[];
s.gridOptions.data=[];
s.gridOptions.onRegisterApi=function(t){s.gridApi=t;
s.gridApi.core.on.sortChanged(s,function(v,u){if(u.length===0){s.paginationOptions.sortDirection=null;
s.paginationOptions.sortColumn=null;
s.paginationOptions.sortColumnType=null
}else{s.paginationOptions.sortDirection=u[0].sort.direction;
s.paginationOptions.sortColumn=u[0].field;
s.paginationOptions.sortColumnType=u[0].colDef.type
}s.getPage()
});
s.gridApi.core.on.filterChanged(s,function(){var u=this.grid;
if(u.columns.length>0){s.filterOptions=[];
angular.forEach(u.columns,function(v){if(v.filters[0].term!==undefined&&v.filters[0].term!==null&&v.filters[0].term!==""){var w={};
w.filterColumn=v.field;
w.filterColumnType=v.colDef.type;
w.filterValue=v.filters[0].term;
s.filterOptions.push(w)
}});
s.getPage()
}});
t.pagination.on.paginationChanged(s,function(v,u){s.paginationOptions.pageNumber=v;
s.paginationOptions.pageSize=u;
s.getPage()
})
};
s.getPage=function(){s.send={offSet:s.paginationOptions.pageNumber,limit:s.paginationOptions.pageSize,isFilter:true,isGrouped:s.isGroupByCheck,sortColumn:s.paginationOptions.sortColumn,sortDirection:s.paginationOptions.sortDirection,sortColumnType:s.paginationOptions.sortColumnType,filterOptions:s.filterOptions};
g.retrievePaginatedData(s.send,function(t){if(t.data!==undefined){if(t.data.queryValid!==undefined){var v="Invalid Query.";
var u=o.failure;
o.addMessage(v,u)
}else{s.previewData=t.data.records;
s.totalItems=(t.data.totalRecords);
s.gridOptions.totalItems=s.totalItems;
if(!s.isGroupByCheck){s.UIPreviewData=angular.copy(s.previewData);
console.log("previwDate----in paginated non filter"+JSON.stringify(s.UIPreviewData));
s.gridColumnDef=[];
angular.forEach(s.columns,function(y){y.total=undefined;
if(y.showTotal!==undefined&&y.showTotal===true){var x=0;
angular.forEach(s.previewData,function(A){var z=A[y.alias]===undefined?(A[y.fieldLabel]===undefined?0:A[y.fieldLabel]):A[y.alias];
x+=Number(z)
});
if(isNaN(x)){x=0
}y.total=x
}if(y.componentType==="Currency"){if(s.currencyMap[y.associatedCurrency]!==undefined){y.currencySymbol=s.currencyMap[y.associatedCurrency].symbol;
y.format=s.currencyMap[y.associatedCurrency].format
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
}s.gridColumnDef.push({name:y.alias,displayName:y.alias,type:w,enableHiding:false,cellTemplate:"<div class=\"ui-grid-cell-contents\" title=\"{{(COL_FIELD === null || COL_FIELD === '') ? 'N/A':COL_FIELD}}\">{{(COL_FIELD === null || COL_FIELD === \"\") ? 'N/A':COL_FIELD}}</div>",minWidth:200})
})
}if(s.isGroupByCheck){s.groupRecordsForPreview(s.previewData)
}s.gridOptions.columnDefs=s.gridColumnDef;
s.gridOptions.data=s.UIPreviewData;
if(s.gridApi.pagination.getPage()>s.gridApi.pagination.getTotalPages()){s.gridApi.pagination.seek(1)
}}}},function(){o.addMessage("Some error occurred while retrieving data, try different fields and criteria",o.failure)
})
};
s.resetGrid=function(){s.gridOptions.paginationCurrentPage=1
};
s.rowFormatter=function(u){var t="white";
if(s.colorConfigData.length>0){angular.forEach(s.colorConfigData,function(v){if(v.columns.length>0){var w=s.executeFilterOnRow(u.entity,v);
if(w){t=v.colorName
}}})
}return t
};
r.retrieveDetailsOfMaster({primaryKey:s.code},function(t){s.detailOfMaster=t;
s.detailOfMaster.masterDataBeans=m("orderBy")(s.detailOfMaster.masterDataBeans,["-isOftenUsed","shortcutCode","value"])
});
s.checkReportNameExists=function(t,u){if(u!==undefined&&u.length>0){g.checkReportNameExists(u,function(v){if(v.data===true){t.$setValidity("exists",false)
}},function(v){console.log("Report name check failed")
})
}};
s.retrieveReportForEdit=function(){if(s.isEditMode){g.retrieveReport(s.selectedId,function(u){s.report=u;
if(s.report.externalReport!==true){var t=[];
angular.forEach(s.report.columns,function(v){if(t.indexOf(v.feature)===-1){t.push(v.feature)
}});
s.selectedFieldsList=[];
s.report.columns.sort(k);
s.models={selected:null,templates:[{type:"container",id:0,text:"New-Table",columns:[[{text:"Column",colField:"column",feature:0}]],searchData:[[{column:""}]]}],dropzones:{A:[]}};
angular.forEach(s.report.tableDtls,function(v){s.obj={type:"container",allowedTypes:["col"],text:v.tableName,id:v.id,columns:[[]],searchData:[[{column:""}]]};
s.models.dropzones.A.push(angular.copy(s.obj))
});
angular.forEach(s.report.columns,function(v){if(s.models.dropzones.A!=undefined){var w=0;
angular.forEach(s.models.dropzones.A,function(y){var x=v.tableName.split(",");
if(x.indexOf(y.text)!==-1){w++;
y.columns[0].push({text:v.alias,colField:v.dbBaseName+"."+v.colName,newField:true,type:"col",feature:v.feature})
}})
}})
}})
}};
s.retrieveReportForEdit();
s.multiFeatures={allowClear:true,multiple:true,closeOnSelect:false,placeholder:"Select Features",data:s.featureList,initSelection:function(t,v){if(s.isEditMode){var u=[];
g.retrieveReport(s.selectedId,function(x){s.report=x;
var w=[];
var y=[];
if(s.report.externalReport!==true){s.report.columns.sort(k);
angular.forEach(s.report.columns,function(z){if(!(z.isSubFormValue===true&&z.dbBaseName==="subformvalue")){if(w.indexOf(z.feature)===-1){w.push(z.feature)
}}else{if(y.indexOf(z.parentDbBaseName+"."+z.parentFieldLabel)===-1){y.push(z.parentDbBaseName+"."+z.parentFieldLabel)
}}});
g.retrieveFeatureNameByIds(w,function(A){var z=[];
angular.forEach(w,function(B){angular.forEach(A.data,function(D,C){if(B==C){z.push({id:D,text:D})
}})
});
angular.forEach(z,function(B){u.push(B)
});
if(y.length>0){angular.forEach(y,function(B){u.push({id:B,text:B})
})
}v(u)
})
}},function(){})
}},formatResult:function(t){return t.text
},formatSelection:function(t){return t.text
}};
s.joinSelect={placeholder:"Select Columns",formatSelection:q};
function q(t){return t.id
}s.setReportColumnsAndGenearteQuery=function(t){s.reportDuplicate=angular.copy(s.report);
s.reportDuplicate.columns=angular.copy(s.columns);
if(!s.isEditMode){s.report.columns=angular.copy(s.columns)
}angular.forEach(s.reportDuplicate.columns,function(u){u.availableFilterTypes=undefined;
u.availableFilterValues=undefined;
u.availableFilterValueSelect=undefined;
u.availableFilterValueSelectEqual=undefined;
u.availableFilterValueSelectNotEqual=undefined;
u.filterValFirst=undefined;
u.selectedFilterType=undefined;
u.selectedFilterTypesList=undefined;
u.filterValSecond=undefined;
u.selectedFilterTypes=undefined;
u.order=undefined;
u.total=undefined;
u.checked=undefined;
u.filter=undefined;
if(u.columnFilter!==undefined){u.columnFilter=undefined
}if(u.availableColumnFormats!==undefined){u.availableColumnFormats=undefined
}u.isGroupBy=undefined;
u.filterValFirstInvalid=undefined;
u.filterValSecondInvalid=undefined;
u.level=undefined;
u.currencySymbol=undefined;
u.format=undefined;
u.associatedCurrency=undefined;
u.userMultiSelection=undefined
});
if(t!==undefined&&t===true){s.generateQuery()
}};
s.generateQuery=function(){var t=0;
angular.forEach(s.reportDuplicate.columns,function(u){u.fieldSequence=++t
});
if(s.reportDuplicate.columns!==undefined){g.generateQuery(s.reportDuplicate,function(u){if(u.query){s.report.query=u.query
}})
}};
s.columnForAlias=function(){s.columnNameForAlias=[];
if(s.selectedFields.length!==0){var t=s.selectedFields.split(",");
angular.forEach(s.fieldList,function(u){angular.forEach(u.children,function(v){for(var w=0;
w<t.length;
w++){if(t[w]===v.id){s.columnNameForAlias.push({id:v.id,text:v.text})
}}})
})
}};
s.setPage=function(t){console.log("pageNo : "+t);
s.currentPage=t;
s.pageChanged()
};
s.pageChanged=function(){console.log("Page changed to: "+s.currentPage);
s.retrievePaginatedData()
};
s.multiFields={multiple:true,closeOnSelect:false,placeholder:"Select Fields",data:s.fieldList,formatSelection:j,initSelection:function(t,v){if(s.isEditMode){var u=[];
g.retrieveReport(s.selectedId,function(x){s.report=x;
if(s.report.externalReport!==true){var w=[];
angular.forEach(s.report.columns,function(y){if(w.indexOf(y.feature)===-1){w.push(y.feature)
}});
s.selectedFieldsList=[];
s.report.columns.sort(k);
angular.forEach(s.report.columns,function(y){s.selectedFieldsList.push({id:y.dbBaseName+"."+y.colName,text:y.fieldLabel})
});
if(s.selectedFieldsList!==undefined&&s.selectedFieldsList!==null&&s.selectedFieldsList.length>0){angular.forEach(s.selectedFieldsList,function(y){u.push(y)
});
v(u)
}}},function(){})
}},formatResult:function(t){return t.text
}};
function j(t){if(s.selectedFields!==undefined){if(s.selectedFields.indexOf(t.text)>0){return t.id
}else{return t.text
}}}var l={numberFormats:[{format:"00.00"},{format:"00.000"}],dateFormats:[{format:"dd/MM/yyyy"},{format:"dd-MM-yyyy"},{format:"dd-MM-yy"},{format:"dd-MM-yyyy hh:mm:ss"},{format:"dd Mon,yyyy"}],textFormats:[{format:"Minimum Text"},{format:"Full Text"}]};
s.report={};
s.report.externalReport=false;
var f=[];
s.loadColumnValues=function(v){s.submitted=true;
if($("#selectFeatures").select2("data").length===0){v.internalForm.selectFeatures.$invalid=true;
v.internalForm.selectFeatures.$dirty=true;
v.internalForm.selectFeatures.$error.required=true
}else{v.internalForm.selectFeatures.$invalid=false;
v.internalForm.selectFeatures.$dirty=true;
v.internalForm.selectFeatures.$error.required=false
}var u=true;
for(var t in v){if(t!=="passwordPopUpForm"&&t!=="editMasterForm"){if(v[t].$invalid){u=false;
break
}}}if(u){s.tabs[1].disabled=false;
s.tabs[1].active=true;
if(s.isEditMode){s.retrieveReportForEdit();
angular.forEach(s.report.columns,function(w){if(s.selectedFields!==undefined&&s.selectedFields!==""){s.selectedFields=s.selectedFields+","+w.dbBaseName+"."+w.colName
}else{s.selectedFields=w.dbBaseName+"."+w.colName
}s.selectedFieldsList.push({id:w.dbBaseName+"."+w.colName,text:w.fieldLabel})
});
d(function(){s.loadColumnValues1()
},500)
}}};
s.dropColumnCallbackAfterSave=function(x,u,w,y,v,t){if(s.models.dropzones.A!==undefined&&s.models.dropzones.A.length>0){s.selectedFields="";
angular.forEach(s.models.dropzones.A,function(z){angular.forEach(z.columns[0],function(C){if(s.selectedFields!=undefined&&s.selectedFields.length>0){var A=angular.copy(s.selectedFields.split(","));
if(A.length>0){var B=0;
angular.forEach(A,function(D){if(D==C.colField){B++
}});
if(B==0){s.selectedFields=s.selectedFields+","+C.colField
}}s.loadColumnValues1();
s.configureRelationship()
}else{s.selectedFields=C.colField;
s.loadColumnValues1();
s.configureRelationship()
}})
})
}};
s.loadColumnValues1=function(){s.primaryOrder.isPrimaryOrderRequired=false;
if(s.secondaryOrder.column!==undefined&&s.secondaryOrder.column!==null){if(s.primaryOrder.column===undefined||s.primaryOrder.column===null){s.primaryOrder.isPrimaryOrderRequired=true
}}if(!s.primaryOrder.isPrimaryOrderRequired){s.fieldToDataTypeMap={};
s.fieldToComponentTypeMap={};
if(s.selectedFeatures!==undefined&&s.selectedFields!==undefined){var y=s.selectedFeatures.split(",");
var u=s.selectedFields.split(",");
var w=[];
var z=0;
angular.forEach(u,function(A){if(s.models.dropzones.A!==undefined&&s.models.dropzones.A.length>0){angular.forEach(s.models.dropzones.A,function(B){angular.forEach(B.columns[0],function(E){if(E.colField===A){var D=0;
angular.forEach(w,function(F){if((F.dbBaseName+"."+F.dbFieldName)===A){D++
}});
if(D===0){var C={tableName:B.text,dbBaseName:A.split(".")[0],dbFieldName:A.split(".")[1],fieldSequence:++z};
w.push(C)
}}})
})
}});
angular.forEach(w,function(A){if(s.models.dropzones.A!=undefined&&s.models.dropzones.A.length>0){angular.forEach(s.models.dropzones.A,function(B){angular.forEach(B.columns[0],function(D){if(D.colField==(A.dbBaseName+"."+A.dbFieldName)){var C=A.tableName.split(",");
if(C.indexOf(B.text)==-1){A.tableName=A.tableName+","+B.text
}}})
})
}});
s.associatedFieldList=[];
angular.forEach(y,function(B){for(var A in s.featureFieldMap){if(A===B){s.sectionList=[];
s.sectionFieldMap=s.featureFieldMap[A];
for(var C in s.sectionFieldMap){s.fieldNames=[];
angular.forEach(w,function(D){angular.forEach(s.sectionFieldMap[C],function(E){if(E.dbBaseName===D.dbBaseName&&E.dbFieldName===D.dbFieldName){E.fieldSequence=D.fieldSequence;
E.sectionName=C;
E.orderType=null;
E.tableName=D.tableName;
s.associatedFieldList.push(E)
}var G=false;
if(E.validationPattern!==null&&E.validationPattern!==undefined){try{var H=JSON.parse(E.validationPattern);
if(H.isDff!==null||H.isDff!==undefined){if(H.isDff){G=true
}}}catch(F){console.log("Can not parse JSON validationPattern"+F)
}}if(!G&&C==="General"&&E.dbBaseType==="MDB"){s.fieldToDataTypeMap[E.dbBaseName+"."+E.dbFieldName]=E.fieldType;
s.fieldToComponentTypeMap[E.dbBaseName+"."+E.dbFieldName]=E.componentType
}})
})
}}}})
}if(s.associatedFieldList.length===0){s.columnEmptyError=true;
o.addMessage("Fields are not configured,Please contact administrator",o.failure);
return
}else{s.columnEmptyError=false
}s.associatedFieldList.sort(k);
s.existingColumns;
if(s.columns===undefined||s.columns.length===0){s.existingColumns=[]
}else{s.existingColumns=angular.copy(s.columns)
}s.columns=[];
var x=0;
angular.forEach(s.associatedFieldList,function(H){var B={};
B.tableName=H.tableName;
B.colName=H.dbFieldName;
B.isSubFormValue=H.isSubFormValue;
B.parentDbFieldName=H.parentDbFieldName;
B.parentDbBaseName=H.parentDbBaseName;
B.parentFieldLabel=H.parentFieldLabel;
var A=H.fieldType;
if(A==="String"){B.dataType="varchar";
if(H.validationPattern!==null&&H.validationPattern!==undefined){try{var G=JSON.parse(H.validationPattern);
if(G.allowedTypes!==null||G.allowedTypes!==undefined){if(G.allowedTypes==="Numeric"){B.dataType="int8"
}}}catch(C){console.log("Can not parse JSON validationPattern"+C)
}}if(H.componentType==="AutoGenerated"){B.dataType="varchar"
}}else{if(A==="Date"||A==="timestamp"){B.dataType="timestamp"
}else{if(A==="StringArray"||A==="ObjectId"){B.dataType="varchar"
}else{if(A==="Double"){B.dataType="double precision"
}else{if(A==="Boolean"){B.dataType="boolean"
}else{if(A==="Image"||A==="File"){B.dataType="varchar"
}else{B.dataType="int8"
}}}}}}B.masterCode=null;
if(H.validationPattern!==null&&H.validationPattern!==undefined){try{var G=JSON.parse(H.validationPattern);
if(G.masterCode!==null&&G.masterCode!==undefined){B.masterCode=G.masterCode;
B.dataType="varchar"
}}catch(C){console.log("Can not parse JSON validationPattern"+C)
}}if(H.validationPattern!==null&&H.validationPattern!==undefined){try{var G=JSON.parse(H.validationPattern);
if(G.isDff!==null||G.isDff!==undefined){if(G.isDff){B.isDff=true
}else{B.isDff=false
}}else{B.isDff=false
}if(G.includeTime!==null||G.includeTime!==undefined){if(G.includeTime){B.includeTime=true
}else{B.includeTime=false
}}else{B.includeTime=false
}if(G.isRule!==null||G.isRule!==undefined){if(G.isRule){B.isRule=true
}else{B.isRule=false
}}else{B.isRule=false
}}catch(C){console.log("Can not parse JSON validationPattern"+C)
}}B.alias=H.fieldLabel;
B.associatedCurrency=H.associatedCurrency;
B.checked=true;
B.id=x++;
B.fieldLabel=H.fieldLabel;
B.editedFieldLabel=H.editedFieldLabel;
B.dbBaseName=H.dbBaseName;
B.dbBaseType=(H.dbBaseType===null||H.dbBaseType==="")?"RDB":H.dbBaseType;
B.feature=H.feature;
B.orderType=H.orderType;
B.showTotal=H.showTotal;
B.sectionName=H.sectionName;
B.componentType=H.componentType;
B.hkFieldId=H.id;
if(B.dbBaseType==="MDB"){if(B.componentType==="Dropdown"||B.componentType==="MultiSelect"||B.componentType==="UserMultiSelect"||B.componentType==="Radio"){B.dataType="varchar"
}}var D=s.getAvailableFormat(B.dataType);
var F=(B.dataType==="timestamp"?"dd/MM/yyyy":null);
B.availableColumnFormats=D;
B.format=F;
var E=s.availabeFilterTypesByDataType[B.dataType]===undefined?[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"},,{id:"is not null",text:"has any value"},{id:"is null",text:"has no value"}]:s.availabeFilterTypesByDataType[B.dataType];
if(B.componentType==="Date range"){E=[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"}]
}if(B.componentType==="UserMultiSelect"){E=[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"},{id:"in",text:"in"},{id:"not in",text:"not in"},{id:"is not null",text:"has any value"},{id:"is null",text:"has no value"}]
}s.filterTypes=[];
angular.forEach(E,function(I){s.filterTypes.push({id:I.id,text:I.text})
});
B.availableFilterTypes=angular.copy(s.filterTypes);
B.availableFilterValues=[];
B.selectedFilterTypesList=[];
if(B.componentType==="UserMultiSelect"){B.userMultiSelection={multiple:true,closeOnSelect:false,placeholder:"Filter Value",initSelection:function(I,K){var J=[];
if(s.existingColumns.length>0){angular.forEach(s.existingColumns,function(L){if(B.colName===L.colName&&B.dbBaseName===L.dbBaseName){if(L.filterValFirst!==undefined){if(L.filterValFirst instanceof Object===true&&angular.isArray(L.filterValFirst)){angular.forEach(L.filterValFirst,function(N){J.push({id:N.id,text:N.text})
});
K(J)
}else{var M=L.filterValFirst.split(",");
angular.forEach(M,function(N){var O=N;
angular.forEach(s.recipientCodes,function(P){if(N===P.id){O=P.text
}});
J.push({id:N,text:O})
});
K(J)
}}}})
}},formatResult:function(I){return I.text
},formatSelection:function(I){return I.text
},query:function(L){var K=L.term;
s.names=[];
var M=function(N){if(N.length!==0){s.names=N;
angular.forEach(N,function(P){s.names.push({id:P.value+":"+P.description,text:P.label});
var O=false;
angular.forEach(s.recipientCodes,function(Q){if(Q.id===P.value+":"+P.description){O=true
}});
if(!O){s.recipientCodes.push({id:P.value+":"+P.description,text:P.label})
}})
}L.callback({results:s.names})
};
var I=function(){};
if(K.substring(0,2)==="@E"||K.substring(0,2)==="@e"){var J=L.term.slice(2);
n.retrieveUserList(J.trim(),M,I)
}else{if(K.substring(0,2)==="@D"||K.substring(0,2)==="@d"){var J=L.term.slice(2);
n.retrieveDepartmentList(J.trim(),M,I)
}else{if(K.substring(0,2)==="@R"||K.substring(0,2)==="@r"){var J=L.term.slice(2);
n.retrieveRoleList(J.trim(),M,I)
}else{if(K.length>0){var J=K;
n.retrieveUserList(J.trim(),M,I)
}else{L.callback({results:s.names})
}}}}}}
}s.columns.push(B)
});
if(s.existingColumns.length>0){var v=[];
angular.forEach(s.existingColumns,function(A){angular.forEach(s.columns,function(C,B){if(C.colName===A.colName&&C.dbBaseName===A.dbBaseName){s.columns.splice(B,1);
v.push(A)
}})
});
angular.forEach(v,function(A){s.columns.push(A)
});
angular.forEach(s.associatedFieldList,function(A){angular.forEach(s.columns,function(B){if(B.colName===A.dbFieldName&&B.dbBaseName===A.dbBaseName){B.fieldSequence=A.fieldSequence;
B.tableName=A.tableName;
B.orderType=A.orderType
}})
});
s.columns.sort(k);
s.associatedFieldList.sort(k)
}if(s.isEditMode&&!s.isEditModeColumnsInitialized){s.isEditModeColumnsInitialized=true;
s.tempColumns=[];
var t=1;
angular.forEach(s.columns,function(A){A.id=null;
angular.forEach(s.report.columns,function(B){if(B.colName===A.colName&&B.dbBaseName===A.dbBaseName){A.alias=B.alias;
A.format=B.format;
A.colI18nRequired=B.colI18nRequired;
A.converterDataBeanList=B.converterDataBeanList;
A.id=B.id;
A.fieldSequence=B.fieldSequence;
A.fieldLabel=B.fieldLabel;
A.showTotal=B.showTotal;
A.filter=B.filter;
A.tableName=B.tableName
}})
});
angular.forEach(s.associatedFieldList,function(A){angular.forEach(s.columns,function(B){if(B.colName===A.dbFieldName&&B.dbBaseName===A.dbBaseName){A.fieldSequence=B.fieldSequence
}})
});
s.columns.sort(k);
s.associatedFieldList.sort(k)
}s.columnForAlias();
s.tabs[1].disabled=false;
s.tabs[1].active=true;
s.retrieveTableRelation()
}};
s.setDatabaseDatatype=function(t){var u="text";
if(t==="String"){u="text"
}else{if(t==="Date"||t==="timestamp"){u="timestamp"
}else{if(t==="StringArray"||t==="ObjectId"){u="text"
}else{if(t==="Double"){u="numeric"
}else{if(t==="Boolean"){u="boolean"
}else{u="numeric"
}}}}}return u
};
function k(t,u){return t.fieldSequence-u.fieldSequence
}function b(t,u){return t.sequence-u.sequence
}s.$watch("report.externalReport",function(t){if(t===true){s.tabs[1].disabled=true;
s.tabs[1].active=false;
s.tabs[2].disabled=true;
s.tabs[2].active=false
}});
s.retrieveColumnMetaData=function(){s.setReportColumnsAndGenearteQuery(false);
g.retreiveColumnMetadata(s.reportDuplicate,function(t){if(t.data!==null&&t.data!==undefined){angular.forEach(t.data.columns,function(y){var x=y.dbBaseName;
var w=y.colName;
if(y.componentType==="SubEntity"){angular.forEach(s.columns,function(B){if(B.dbBaseName===x&&B.colName===w){B.dataType=y.dataType;
var A=s.getAvailableFormat(B.dataType);
var D=(B.dataType==="timestamp"?"dd/MM/yyyy":null);
B.availableColumnFormats=A;
B.format=D;
var C=s.availabeFilterTypesByDataType[B.dataType]===undefined?[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"}]:s.availabeFilterTypesByDataType[B.dataType];
B.availableFilterTypes=angular.copy(C)
}})
}else{if(y.convertedColumn!==null&&y.convertedColumn!==undefined){var z=y.convertedColumn;
var v=z.split(".")[0];
var u=z.split(".")[1];
if(u.indexOf(":")===-1){angular.forEach(s.AllFieldsEntitys,function(B){if(B.dbBaseName===v&&B.dbFieldName===u){var A=B.fieldType;
if(A==="String"){y.dataType="varchar";
if(B.validationPattern!==null&&B.validationPattern!==undefined){try{var D=JSON.parse(B.validationPattern);
if(D.allowedTypes!==null||D.allowedTypes!==undefined){if(D.allowedTypes==="Numeric"){y.dataType="int8"
}}}catch(C){console.log("Can not parse JSON validationPattern"+C)
}}if(B.componentType==="AutoGenerated"){y.dataType="varchar"
}}else{if(A==="Date"||A==="timestamp"){y.dataType="timestamp"
}else{if(A==="StringArray"||A==="ObjectId"){y.dataType="varchar"
}else{if(A==="Double"){y.dataType="double precision"
}else{if(A==="Boolean"){y.dataType="boolean"
}else{if(A==="Image"||A==="File"){y.dataType="varchar"
}else{y.dataType="int8"
}}}}}}}})
}else{y.dataType="varchar"
}angular.forEach(s.columns,function(B){if(B.dbBaseName===x&&B.colName===w){B.dataType=y.dataType;
var A=s.getAvailableFormat(B.dataType);
var D=(B.dataType==="timestamp"?"dd/MM/yyyy":null);
B.availableColumnFormats=A;
B.format=D;
var C=s.availabeFilterTypesByDataType[B.dataType]===undefined?[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"}]:s.availabeFilterTypesByDataType[B.dataType];
B.availableFilterTypes=angular.copy(C)
}})
}else{if(y.dbBaseType==="MDB"){if(y.componentType==="Dropdown"||y.componentType==="MultiSelect"){angular.forEach(s.columns,function(B){if(B.dbBaseName===x&&B.colName===w){B.dataType=y.dataType;
var A=s.getAvailableFormat(B.dataType);
var D=(B.dataType==="timestamp"?"dd/MM/yyyy":null);
B.availableColumnFormats=A;
B.format=D;
var C=s.availabeFilterTypesByDataType[B.dataType]===undefined?[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"}]:s.availabeFilterTypesByDataType[B.dataType];
B.availableFilterTypes=angular.copy(C)
}})
}}}}})
}if(s.isGroupByCheck||s.isEditMode){s.updateGroupConfigurations()
}else{s.updateExistingFilters()
}},function(t){if(s.isGroupByCheck||s.isEditMode){s.updateGroupConfigurations()
}else{s.updateExistingFilters()
}})
};
s.retrieveTableRelation=function(){s.setReportColumnsAndGenearteQuery(false);
g.retrieveTableRelationship(s.reportDuplicate,function(t){s.relationMap=angular.copy(t.data);
s.relationList=angular.copy(t.data);
angular.forEach(s.columns,function(u){angular.forEach(s.relationList,function(v){if(u.dbBaseName===v.table1){var w={FK_TABLE:v.table2,FK_COLUMN:v.table2Column,LOCAL_COLUMN:v.table1Column,REL_TYPE:v.relationLeftToRight,JOIN_TABLE:v.joinTable,JOINED_LOCAL_COLUMN:v.joinColumnTable1,JOINED_FK_COLUMN:v.joinColumnTable2};
u.joinAttributes=JSON.stringify(w)
}else{if(u.dbBaseName===v.table2){var w={FK_TABLE:v.table1,FK_COLUMN:v.table1Column,LOCAL_COLUMN:v.table2Column,REL_TYPE:v.relationLeftToRight,JOIN_TABLE:v.joinTable,JOINED_LOCAL_COLUMN:v.joinColumnTable2,JOINED_FK_COLUMN:v.joinColumnTable1};
u.joinAttributes=JSON.stringify(w)
}}})
});
s.retrieveColumnMetaData()
},function(){console.log("failure----")
})
};
s.addFilter=function(y,w,x){s.isGlobalFilterAdded=true;
if(s[x][w].selectedFilterType!=="between"&&!(s[x][w].selectedFilterType==="is null"||s[x][w].selectedFilterType==="is not null")){if(s[x][w].filterValFirst!==undefined&&(s[x][w].filterValFirst.length>0||s[x][w].filterValFirst!==null)){s[x][w].filterValFirstInvalid=false;
var z=s[x][w].filterValFirst;
var u={};
var v="";
if(y.componentType==="UserMultiSelect"){var A;
if(z instanceof Object&&angular.isArray(z)){var t="";
angular.forEach(z,function(B){if(t.length===0){t=B.id
}else{t+=","+B.id
}});
A=t.split(",")
}else{A=z.split(",")
}angular.forEach(A,function(B){angular.forEach(s.recipientCodes,function(C){if(C.id===B){var E=B.split(":")[1];
var D=s.recipientMap[E];
if(u[D]===undefined||u[D]===null){u[D]=C.text
}else{u[D]+=", "+C.text
}}})
});
angular.forEach(u,function(C,B){if(v.length===0){v=B+": "+C
}else{v+="; "+B+": "+C
}})
}s[x][w].selectedFilterTypesList.push({filter:s[x][w].selectedFilterType,filterLabel:s.filterValueNameMap[s[x][w].selectedFilterType],filterValFirst:z,userMultiSelectLabel:v});
if(y.componentType!=="UserMultiSelect"){s[x][w].filterValFirst=undefined
}angular.forEach(s[x][w].availableFilterTypes,function(C,B){if(C.id===s[x][w].selectedFilterType){s[x][w].selectedFilterType=undefined;
s[x][w].availableFilterTypes.splice(B,1)
}})
}else{s[x][w].filterValFirstInvalid=true
}}else{if(s[x][w].selectedFilterType==="is null"||s[x][w].selectedFilterType==="is not null"){s[x][w].selectedFilterTypesList.push({filter:s[x][w].selectedFilterType,filterLabel:s.filterValueNameMap[s[x][w].selectedFilterType]});
angular.forEach(s[x][w].availableFilterTypes,function(C,B){if(C.id===s[x][w].selectedFilterType){s[x][w].selectedFilterType=undefined;
s[x][w].availableFilterTypes.splice(B,1)
}})
}else{if(s[x][w].filterValFirst!==undefined&&s[x][w].filterValSecond!==undefined&&(s[x][w].filterValFirst.length>0||s[x][w].filterValFirst!==null)&&(s[x][w].filterValSecond.length>0||s[x][w].filterValSecond!==null)){switch(s[x][w].dataType){case"int8":s[x][w].filterValFirst=parseInt(s[x][w].filterValFirst);
s[x][w].filterValSecond=parseInt(s[x][w].filterValSecond);
break;
case"double precision":s[x][w].filterValFirst=parseFloat(s[x][w].filterValFirst);
s[x][w].filterValSecond=parseFloat(s[x][w].filterValSecond);
break;
case"timestamp":s[x][w].filterValFirst=new Date(s[x][w].filterValFirst);
s[x][w].filterValSecond=new Date(s[x][w].filterValSecond);
break
}if(s[x][w].filterValFirst<=s[x][w].filterValSecond){s[x][w].filterValFirstInvalid=false;
s[x][w].filterValSecondInvalid=false;
s[x][w].filterValFirstMinInvalid=false;
s[x][w].selectedFilterTypesList.push({filter:s[x][w].selectedFilterType,filterLabel:s.filterValueNameMap[s[x][w].selectedFilterType],filterValFirst:s[x][w].filterValFirst,filterValSecond:s[x][w].filterValSecond});
s[x][w].filterValFirst=undefined;
s[x][w].filterValSecond=undefined;
angular.forEach(s[x][w].availableFilterTypes,function(C,B){if(C.id===s[x][w].selectedFilterType){s[x][w].selectedFilterType=undefined;
s[x][w].availableFilterTypes.splice(B,1)
}})
}else{s[x][w].filterValFirstMinInvalid=true
}}if(s[x][w].filterValFirst===undefined||s[x][w].filterValFirst===null||s[x][w].filterValFirst.length===0){s[x][w].filterValFirstInvalid=true
}if(s[x][w].filterValSecond===undefined||s[x][w].filterValSecond===null||s[x][w].filterValSecond.length===0){s[x][w].filterValSecondInvalid=true
}}}s.persistGroupChanges()
};
s.removeFilter=function(w,v,u){if(s[u][v].selectedFilterTypesList.length>0){var t=s[u][v].selectedFilterTypesList[w];
s[u][v].selectedFilterTypesList.splice(w,1);
s[u][v].availableFilterTypes.push({id:t.filter,text:s.filterValueNameMap[t.filter]})
}s.persistGroupChanges()
};
s.updateExistingFilters=function(){if(s.existingColumns!==undefined){if(s.isEditMode&&!s.isEditModeFilterInitialized){s.isEditModeFilterInitialized=true;
angular.forEach(s.report.columns,function(t){angular.forEach(s.columns,function(u){if(t.colName===u.colName&&t.dbBaseName===u.dbBaseName){if(t.filter!==undefined){u.filter=JSON.parse(t.filter);
angular.forEach(u.filter,function(v){angular.forEach(u.availableFilterTypes,function(x,w){if(x.id===v.filter){u.selectedFilterTypesList.push({filter:x.id,filterLabel:s.filterValueNameMap[x.id]});
u.availableFilterTypes.splice(w,1)
}})
})
}}})
});
s.updatePreFilterValues()
}else{if(s.existingColumns.length>0){angular.forEach(s.existingColumns,function(t){angular.forEach(s.columns,function(u){if(u.colName===t.colName&&u.dbBaseName===t.dbBaseName){if(t.selectedFilterTypesList.length>0){angular.forEach(t.selectedFilterTypesList,function(v){angular.forEach(u.availableFilterTypes,function(x,w){if(v.filter===x.id){u.availableFilterTypes.splice(w,1)
}})
})
}}})
})
}}}};
s.updatePreFilterValues=function(){angular.forEach(s.columns,function(t){angular.forEach(t.selectedFilterTypesList,function(u){if(t.filter instanceof Object===false||!angular.isArray(t.filter)){t.filter=JSON.parse(t.filter)
}angular.forEach(t.filter,function(v){if(v.filter===u.filter){if(v.filterValFirst!==undefined){u.filterValFirst=v.filterValFirst
}if(v.filterValSecond!==undefined){u.filterValSecond=v.filterValSecond
}if(v.userMultiSelectLabel!==undefined){u.userMultiSelectLabel=v.userMultiSelectLabel
}}})
})
});
if(s.isGroupByCheck){s.updateGroupConfig(true)
}};
s.loadEditData=function(){if(s.selectedFields!==undefined&&s.selectedFields instanceof Object===true&&angular.isArray(s.selectedFields)){var t="";
angular.forEach(s.selectedFields,function(v,u){if(u===0){t=v.id
}else{t+=","+v.id
}});
s.selectedFields=t
}};
s.$watch("report.query",function(t){if(t!==undefined){s.queryChanged=true
}});
s.$watch("selectedFeatures",function(t){if(s.selectedFeatures!==undefined&&s.selectedFeatures instanceof Object===true&&angular.isArray(s.selectedFeatures)){var t="";
angular.forEach(s.selectedFeatures,function(v,u){if(u===0){t=v.id
}else{t+=","+v.id
}});
s.selectedFeatures=t;
s.fieldList.splice(0,s.fieldList.length);
s.retrieveSectionFields(s.selectedFeatures);
s.removeColumnsFromTableModel(s.selectedFeatures)
}else{s.fieldList.splice(0,s.fieldList.length);
s.retrieveSectionFields(s.selectedFeatures);
s.configureRelationship()
}});
s.removeColumnsFromTableModel=function(u){if(u!==undefined){var t=u.split(",");
if(s.models!=undefined&&s.models.dropzones!=undefined&&s.models.dropzones.A!==undefined&&s.models.dropzones.A.length>0){angular.forEach(s.models.dropzones.A,function(w){var v;
for(v=w.columns[0].length;
v--;
){var x=0;
if(w.columns[0][v].feature===0){x++
}else{angular.forEach(t,function(y){if(y===w.columns[0][v].feature){x++
}})
}if(x===0){w.columns[0].splice(v,1)
}}if(w.columns[0].length===0){w.columns[0].push({text:"Column",colField:"column"})
}})
}}};
s.$watch("selectedFields",function(t){if(s.selectedFields!==undefined&&s.selectedFields instanceof Object===true&&angular.isArray(s.selectedFields)){var u="";
angular.forEach(s.selectedFields,function(w,v){if(v===0){u=w.id
}else{u+=","+w.id
}});
s.extraFields=[];
angular.forEach(s.AllFieldDetailList,function(v){angular.forEach(s.selectedFields,function(x){if(x.id===v.id){var w=0;
angular.forEach(s.extraFields,function(y){if(y.text==v.text){w++
}});
if(w==0){s.extraFields.push({text:v.text,colField:v.id,newField:true})
}}})
});
s.selectedFields=u
}else{s.updateOrders();
s.configureRelationship()
}});
$(document).on("select2-removing","#selectFeatures",function(x){tableRemovedFlag=true;
var w=0;
var u=$("#fieldSelect").select2("data");
var z=[];
s.selectedFields="";
var y=[];
var t=[];
for(var B in s.featureFieldMap){if(B===x.val){s.sectionFieldMap=s.featureFieldMap[B];
for(var C in s.sectionFieldMap){angular.forEach(s.sectionFieldMap[C],function(D){var E=D.dbBaseName+"."+D.dbFieldName;
angular.forEach(u,function(F){if(F.id===E){for(var G=0;
G<z.length;
G++){if(z[G]===F){w++
}}if(w===0){z.push(F);
y.push(F.id)
}}})
})
}}}Array.prototype.remove=function(){var G,E=arguments,D=E.length,F;
while(D&&this.length){G=E[--D];
while((F=this.indexOf(G))!==-1){this.splice(F,1)
}}return this
};
var A=[];
angular.forEach(u,function(D){A.push(D.id);
t.push(D)
});
angular.forEach(u,function(D){angular.forEach(y,function(E){if(D.id===E){for(var F=0;
F<A.length;
F++){if(A[F]===E){A.remove(E)
}}for(var F=0;
F<t.length;
F++){if(t[F].id===E){t.remove(t[F])
}}}})
});
for(var v=0;
v<A.length;
v++){if(s.selectedFields.length===0){s.selectedFields=s.selectedFields+A[v]
}else{s.selectedFields=s.selectedFields+","+A[v]
}}$("#fieldSelect").select2("data",t)
});
s.configureRelationship=function(){if(s.selectedFeatures!==undefined&&s.selectedFields!==undefined&&s.selectedFeatures!==""&&s.selectedFields!==""&&!angular.isArray(s.selectedFeatures)&&!angular.isArray(s.selectedFields)){var v=s.selectedFeatures.split(",");
var u=s.selectedFields.split(",");
s.associatedFieldList=[];
angular.forEach(u,function(w){angular.forEach(v,function(y){for(var x in s.featureFieldMap){if(x===y){s.sectionList=[];
s.sectionFieldMap=s.featureFieldMap[x];
for(var z in s.sectionFieldMap){s.fieldNames=[];
angular.forEach(s.sectionFieldMap[z],function(A){A.orderType=null;
if(A.dbBaseName===w.split(".")[0]&&A.dbFieldName===w.split(".")[1]){s.associatedFieldList.push(A)
}})
}}}})
});
var t=[];
angular.forEach(s.associatedFieldList,function(w){if(t.indexOf(w.feature)===-1){t.push(w.feature)
}});
if(angular.isArray(t)&&t.length>0){g.retrieveFeatureNameByIds(t,function(z){var x=0;
var w=[];
angular.forEach(t,function(C){angular.forEach(z.data,function(F,E){if(C===parseInt(E)){var D=false;
angular.forEach(s.selectedFeaturesList,function(G){if(G.featureId===C){D=true
}});
if(!D){s.selectedFeaturesList.push({featureName:F,featureId:C});
s.featureDetailsList.push({featureName:F,featureId:C});
w.push(C)
}}})
});
for(var y=s.selectedFeaturesList.length-1;
y>=0;
y--){if(t.indexOf(s.selectedFeaturesList[y].featureId)===-1){s.selectedFeaturesList.splice(y,1)
}}for(var y=s.featureDetailsList.length-1;
y>=0;
y--){if(t.indexOf(s.featureDetailsList[y].featureId)===-1){s.featureDetailsList.splice(y,1)
}}if(s.selectedFeaturesList.length>0){var B=0;
angular.forEach(s.selectedFeaturesList,function(C){C.sequence=++B
})
}s.selectedFeaturesList.sort(b);
angular.forEach(s.selectedFeaturesList,function(F,D){if(w.indexOf(F.featureId)>-1){var E=s.retrieveSectionFieldsOfFeature(F.featureName);
F.fieldsList=E;
var G={multiple:false,closeOnSelect:true,placeholder:"Select Field",data:E,initSelection:function(H,K){if(s.isEditMode){var I={};
if(s.report.joinAttributes!==undefined){var J=JSON.parse(s.report.joinAttributes);
if(J!==null&&J!==undefined){angular.forEach(J,function(N){if(N.featureId===F.featureId){if(N.joinAttributes!==null){var M=N.joinAttributes.split("=")[0];
var L=N.joinAttributes.split("=")[1];
angular.forEach(F.fieldsList,function(O){angular.forEach(O.children,function(P){if(P.id===M){I=P
}});
K(I)
})
}}})
}}}else{var I=[];
K(I)
}},formatResult:function(H){return H.text
},formatSelection:function(H){return H.text
}};
F.localFields=G;
var C=angular.copy(G);
C.data=[];
C.initSelection=undefined;
C.initSelection=function(H,K){if(s.isEditMode){var I={};
if(s.report.joinAttributes!==undefined){var J=JSON.parse(s.report.joinAttributes);
if(J!==null&&J!==undefined){angular.forEach(J,function(N){if(N.featureId===F.featureId){if(N.joinAttributes!==null){var M=N.joinAttributes.split("=")[0];
var L=N.joinAttributes.split("=")[1];
angular.forEach(F.referenceFields.data,function(O){angular.forEach(O.children,function(P){if(P.id===L){I=P
}});
K(I)
})
}}})
}}}else{var I=[];
K(I)
}};
F.referenceFields=C
}});
if(s.isEditMode){if(s.report.joinAttributes!==undefined){var A=JSON.parse(s.report.joinAttributes);
if(A!==null&&A!==undefined){A.sort(b);
angular.forEach(A,function(C){angular.forEach(s.selectedFeaturesList,function(G,D){if(C.featureId===G.featureId){G.sequence=C.sequence;
if(C.refFeatureId!==null){G.referenceFeature=C.refFeatureId;
s.updateReferenceFields(C.refFeatureId,D)
}if(C.joinAttributes!==null){var F=C.joinAttributes.split("=")[0];
var E=C.joinAttributes.split("=")[1];
angular.forEach(G.fieldsList,function(H){angular.forEach(H.children,function(I){if(I.id===F){G.localField=I
}})
});
angular.forEach(G.referenceFields.data,function(H){angular.forEach(H.children,function(I){if(I.id===E){G.referenceField=I
}})
})
}}})
})
}s.selectedFeaturesList.sort(b)
}}})
}}else{s.selectedFeaturesList=[]
}};
s.updateReferenceFields=function(v,u){var t;
angular.forEach(s.selectedFeaturesList,function(w){if(w.featureId===parseInt(v)){t=w.localFields.data
}});
s.selectedFeaturesList[u].referenceField="";
s.selectedFeaturesList[u].referenceFields.data.splice(0,s.selectedFeaturesList[u].referenceFields.data.length);
angular.forEach(t,function(w){s.selectedFeaturesList[u].referenceFields.data.push(w)
})
};
s.shiftRelationshipUp=function(t){if(t!==0){var u=s.selectedFeaturesList[t-1];
s.selectedFeaturesList[t-1]=s.selectedFeaturesList[t];
s.selectedFeaturesList[t]=u
}};
s.shiftRelationshipDown=function(t){if(t!==s.selectedFeaturesList.length-1){var u=s.selectedFeaturesList[t+1];
s.selectedFeaturesList[t+1]=s.selectedFeaturesList[t];
s.selectedFeaturesList[t]=u
}};
s.retrieveSectionFields=function(t){if(t!==undefined&&t instanceof Object===false&&t!==""){var u=t.split(",");
s.extraFields=[];
s.fieldsByFeature=[];
angular.forEach(u,function(w){for(var v in s.featureFieldMap){if(v===w){s.sectionList=[];
s.sectionFieldMap=s.featureFieldMap[v];
for(var x in s.sectionFieldMap){s.fieldNames=[];
angular.forEach(s.sectionFieldMap[x],function(y){s.fieldsByFeature.push(y.dbBaseName+"."+y.dbFieldName);
s.extraFields.push({text:y.fieldLabel,colField:y.dbBaseName+"."+y.dbFieldName,newField:true,type:"col",feature:w});
s.fieldNames.push({id:y.dbBaseName+"."+y.dbFieldName,text:y.fieldLabel})
});
s.sectionList.push({});
s.sectionList[s.sectionList.length-1].text=x;
s.sectionList[s.sectionList.length-1].children=s.fieldNames
}s.fieldList.push({});
s.fieldList[s.fieldList.length-1].text=w;
s.fieldList[s.fieldList.length-1].children=s.sectionList
}}})
}};
s.retrieveSectionFieldsOfFeature=function(u){if(u!==undefined&&u instanceof Object===false&&u!==""){var v=u.split(",");
var t=[];
angular.forEach(v,function(x){for(var w in s.featureFieldMap){if(w===x){var y=[];
var A=s.featureFieldMap[w];
for(var B in A){var z=[];
angular.forEach(A[B],function(C){var E=false;
if(C.validationPattern!==null&&C.validationPattern!==undefined){try{var F=JSON.parse(C.validationPattern);
if(F.isDff!==null||F.isDff!==undefined){if(F.isDff){E=true
}}}catch(D){console.log("Can not parse JSON validationPattern"+D)
}}if(C.dbBaseType!=="MDB"&&C.componentType!=="UserMultiSelect"){z.push({id:C.dbBaseName+"."+C.dbFieldName,text:C.fieldLabel})
}else{if(C.dbBaseName==="invoice"&&C.dbFieldName==="invoiceId$AG$String"){z.push({id:C.dbBaseName+"._id",text:C.fieldLabel})
}else{if(C.dbBaseName==="parcel"&&C.dbFieldName==="parcelId$AG$String"){z.push({id:C.dbBaseName+"._id",text:C.fieldLabel})
}else{if(C.dbBaseName==="lot"&&C.dbFieldName==="lotID$AG$String"){z.push({id:C.dbBaseName+"._id",text:C.fieldLabel})
}else{if(C.dbBaseName==="packet"&&C.dbFieldName==="packetID$AG$String"){z.push({id:C.dbBaseName+"._id",text:C.fieldLabel})
}else{if(E&&s.innerDocumentBaseNameSet.indexOf(C.dbBaseName)===-1&&s.caratRaangeFieldSet.indexOf(C.dbFieldName)===-1){z.push({id:C.dbBaseName+'."'+C.dbFieldName+'"',text:C.fieldLabel})
}else{if(B==="General"&&C.dbBaseType==="MDB"&&C.isSubFormValue!==true&&C.componentType!=="SubEntity"){z.push({id:C.dbBaseName+"."+C.dbFieldName+"",text:C.fieldLabel})
}}}}}}}});
if(z.length>0){y.push({});
y[y.length-1].text=B;
y[y.length-1].children=z
}}t=y
}}});
return t
}};
s.getAvailableFormat=function(t){if(t==="int8"){return l.numberFormats
}else{if(t==="timestamp"){return l.dateFormats
}else{return l.textFormats
}}};
s.updateOrders=function(){s.orderFields=[];
if(s.selectedFields!==undefined&&s.selectedFields.length!==0){if(s.isEditMode&&!s.isEditModeOrderInitialized){s.isEditModeOrderInitialized=true;
if(s.report.orderAttributes!==undefined&&s.report.orderAttributes!==null){var u=JSON.parse(s.report.orderAttributes);
angular.forEach(u,function(y){if(y.sequence===1){s.primaryOrder.column=y.columnName;
s.primaryOrder.orderValue=y.orderValue
}else{s.secondaryOrder.column=y.columnName;
s.secondaryOrder.orderValue=y.orderValue
}})
}}var t=s.selectedFields.split(",");
angular.forEach(s.AllFieldsEntitys,function(z){for(var y=0;
y<t.length;
y++){if(t[y]===z.dbBaseName+"."+z.dbFieldName&&s.skipOrderComponentTypes.indexOf(z.componentType)===-1){s.orderFields.push({id:z.dbBaseName+"."+z.dbFieldName,text:z.fieldLabel})
}}});
if(s.orderFields.length>0){if(s.primaryOrder.column!==undefined){var x=false;
angular.forEach(s.orderFields,function(y){if(!x){if(s.primaryOrder.column===y.id){x=true
}}});
if(!x){s.primaryOrder.column=undefined
}}if(s.secondaryOrder.column!==undefined){var x=false;
angular.forEach(s.orderFields,function(y){if(!x){if(s.secondaryOrder.column===y.id){x=true
}}});
if(!x){s.secondaryOrder.column=undefined
}}}if(s.primaryOrderFields.length>0){angular.forEach(s.orderFields,function(z){var y=false;
angular.forEach(s.primaryOrderFields,function(A){if(z.id===A.id){y=true
}});
if(!y){s.primaryOrderFields.push(z)
}});
for(var w=s.primaryOrderFields.length-1;
w>=0;
w--){var v=false;
angular.forEach(s.orderFields,function(y){if(s.primaryOrderFields[w].id===y.id){v=true
}});
if(!v){s.primaryOrderFields.splice(w,1)
}}}else{s.primaryOrderFields=angular.copy(s.orderFields)
}if(s.secondaryOrderFields.length>0){angular.forEach(s.orderFields,function(z){var y=false;
angular.forEach(s.secondaryOrderFields,function(A){if(z.id===A.id){y=true
}});
if(!y){s.secondaryOrderFields.push(z)
}});
for(var w=s.secondaryOrderFields.length-1;
w>=0;
w--){var v=false;
angular.forEach(s.orderFields,function(y){if(s.secondaryOrderFields[w].id===y.id){v=true
}});
if(!v){s.secondaryOrderFields.splice(w,1)
}}}else{s.secondaryOrderFields=angular.copy(s.orderFields)
}if(s.primaryOrder.column!==undefined){for(var w=s.secondaryOrderFields.length-1;
w>=0;
w--){if(s.primaryOrder.column===s.secondaryOrderFields[w].id){s.secondaryOrderFields.splice(w,1)
}}}if(s.secondaryOrder.column!==undefined){for(var w=s.primaryOrderFields.length-1;
w>=0;
w--){if(s.secondaryOrder.column===s.primaryOrderFields[w].id){s.primaryOrderFields.splice(w,1)
}}}}else{s.primaryOrder.column=undefined;
s.secondaryOrder.column=undefined;
s.primaryOrderFields=[];
s.secondaryOrderFields=[]
}};
s.doesAliasExists=function(u,w,t,v){if(u.alias===""){u.alias=u.fieldLabel
}s.persistGroupChanges();
var x=true;
angular.forEach(s.columns,function(z,y){if((z.alias!==undefined&&z.alias!=="")&&z.alias===u.alias){if(u.dbBaseName+"."+u.colName!==z.dbBaseName+"."+z.colName){x=false
}}});
if(x){console.log("if--------------------");
v.$setValidity("exists",true)
}else{console.log("else----------");
v.$setValidity("exists",false);
v.$dirty=true
}};
s.doesTableNameExists=function(t,v){var u=true;
if(s.models.dropzones.A!==undefined&&s.models.dropzones.A.length>0){angular.forEach(s.models.dropzones.A,function(w){if(w.text==t){u=false
}})
}if(u){console.log("if--------------------");
v.changeName.$setValidity("exists",true)
}else{console.log("else----------");
v.changeName.$setValidity("exists",false)
}};
s.submitColumnConfigurations=function(u,B){s.colorApplied=false;
s.subbmited=true;
s.registerGrid=false;
s.groupInfo.isGroupAdded=false;
s.isGlobalFilterAdded=false;
s.persistGroupChanges();
var t=0;
var F;
var w=s.selectedFields.split(",");
var E=[];
angular.forEach(w,function(G){if(s.models.dropzones.A!==undefined&&s.models.dropzones.A.length>0){angular.forEach(s.models.dropzones.A,function(H){angular.forEach(H.columns[0],function(K){if(K.colField===G){var J=0;
angular.forEach(E,function(L){if((L.dbBaseName+"."+L.dbFieldName)===G){J++
}});
if(J===0){var I={tableName:H.text,dbBaseName:G.split(".")[0],dbFieldName:G.split(".")[1]};
E.push(I)
}}})
})
}});
angular.forEach(E,function(G){if(s.models.dropzones.A!=undefined&&s.models.dropzones.A.length>0){angular.forEach(s.models.dropzones.A,function(H){angular.forEach(H.columns[0],function(J){if(J.colField==(G.dbBaseName+"."+G.dbFieldName)){var I=G.tableName.split(",");
if(I.indexOf(H.text)==-1){G.tableName=G.tableName+","+H.text
}}})
})
}});
angular.forEach(s.columns,function(G){angular.forEach(E,function(H){if(H.dbBaseName+"."+H.dbFieldName===G.dbBaseName+"."+G.colName){G.tableName=H.tableName
}})
});
angular.forEach(s.columns,function(G){F=G;
angular.forEach(s.columns,function(H){if((F.alias===""&&H.alias==="")||(F.alias===H.alias)){if(F.dbBaseName+"."+F.colName!==H.dbBaseName+"."+H.colName){t++
}}})
});
var z=[];
angular.forEach(s.selectedFeaturesList,function(K,G){var I={};
I.featureId=K.featureId;
if(K.localField===undefined||K.referenceField===undefined){I.joinAttributes=null;
I.refFeatureId=null
}else{var O;
var M;
if(K.localField instanceof Object===true){O=K.localField.id
}else{O=K.localField
}if(K.referenceField instanceof Object===true){M=K.referenceField.id
}else{M=K.referenceField
}var H=null;
var N=null;
var L=null;
var J=null;
if(s.fieldToDataTypeMap[O.replace(/"/g,"")]!==undefined&&s.fieldToDataTypeMap[O.replace(/"/g,"")]!==null){O=O.replace(/"/g,"");
H=s.setDatabaseDatatype(s.fieldToDataTypeMap[O.replace(/"/g,"")]);
L=s.fieldToComponentTypeMap[O]
}if(s.fieldToDataTypeMap[M.replace(/"/g,"")]!==undefined&&s.fieldToDataTypeMap[M.replace(/"/g,"")]!==null){M=M.replace(/"/g,"");
N=s.setDatabaseDatatype(s.fieldToDataTypeMap[M.replace(/"/g,"")]);
J=s.fieldToComponentTypeMap[M]
}I.joinAttributes=O+"="+M;
I.refFeatureId=parseInt(K.referenceFeature);
I.localDataType=H;
I.refDataType=N;
I.localComponentType=L;
I.refComponentType=J
}if(G===0){I.joinAttributes=null;
I.refFeatureId=null
}I.sequence=G+1;
z.push(I)
});
s.report.joinAttributes=JSON.stringify(z);
s.columnForAlias();
if(t>0){B[2].active=false;
B[2].disabled=true;
o.addMessage("Unique alias name required",o.failure)
}else{if(s.isGroupByCheck&&s.groupByLevels.length===0){B[2].active=false;
B[2].disabled=true;
o.addMessage("Select at least one level grouping",o.failure)
}else{if(u.$valid){o.validations.splice(0,o.validations.length);
s.report.columns=angular.copy(s.columns);
angular.forEach(s.report.columns,function(G){G.filter=null;
if(G.selectedFilterTypesList!==undefined&&G.selectedFilterTypesList!==null){var H=[];
angular.forEach(G.selectedFilterTypesList,function(L){var K={};
K.filter=L.filter;
K.filterValFirst=L.filterValFirst;
K.filterValSecond=L.filterValSecond;
if(G.dataType==="varchar"&&angular.isArray(K.filterValFirst)){var J="";
angular.forEach(K.filterValFirst,function(Q,P){if(P===0){J=Q.id
}else{J+=","+Q.id
}});
K.filterValFirst=J
}if(G.dataType==="timestamp"&&K.filterValFirst!==undefined){var N=new Date(K.filterValFirst);
var O;
var M;
var I;
if((N.getMonth()+1)<10){M="0"+(N.getMonth()+1)
}else{M=(N.getMonth()+1)
}if(N.getDate()<10){I="0"+N.getDate()
}else{I=N.getDate()
}O=N.getFullYear()+"-"+M+"-"+I;
K.filterValFirst=O;
if(K.filterValSecond!==undefined){N=new Date(K.filterValSecond);
if((N.getMonth()+1)<10){M="0"+(N.getMonth()+1)
}else{M=(N.getMonth()+1)
}if(N.getDate()<10){I="0"+N.getDate()
}else{I=N.getDate()
}O=N.getFullYear()+"-"+M+"-"+I;
K.filterValSecond=O
}}if(G.componentType==="UserMultiSelect"){K.userMultiSelectLabel=L.userMultiSelectLabel
}H.push(K)
});
if(H.length>0){G.filter=JSON.stringify(H)
}}G.availableFilterTypes=undefined;
G.availableFilterValues=undefined;
G.availableFilterValueSelect=undefined;
G.availableFilterValueSelectEqual=undefined;
G.availableFilterValueSelectNotEqual=undefined;
G.filterValFirst=undefined;
G.selectedFilterType=undefined;
G.selectedFilterTypesList=undefined;
G.filterValSecond=undefined;
G.selectedFilterTypes=undefined;
G.order=undefined;
G.total=undefined;
G.checked=undefined;
if(G.columnFilter!==undefined){G.columnFilter=undefined
}if(G.availableColumnFormats!==undefined){G.availableColumnFormats=undefined
}G.isGroupBy=undefined;
G.filterValFirstInvalid=undefined;
G.filterValSecondInvalid=undefined;
G.level=undefined;
G.currencySymbol=undefined;
G.format=undefined;
G.associatedCurrency=undefined;
G.userMultiSelection=undefined;
G.first_dt_inp_opnd=undefined;
G.second_dt_inp_opnd=undefined
});
var A=0;
angular.forEach(s.report.columns,function(G){G.fieldSequence=++A
});
angular.forEach(s.columns,function(G){G.fieldSequence=++A
});
s.report.groupAttributes=null;
if(s.isGroupByCheck){var x={};
if(s.groups.length>0){x.groups=[];
angular.forEach(s.groups,function(M,H){var L={};
L.groupName=M.groupName;
L.sequence=H+1;
if(M.sequence===1&&s.groups.length===1){L.groupName=null
}if(M.groupItems.length>0){var J=M.groupItems.split(",");
for(var I=0;
I<J.length;
I++){var K=false;
if(!K){angular.forEach(s.report.columns,function(N){if(J[I].split(".")[0]===N.dbBaseName&&J[I].split(".")[1]===N.colName){if(N.alias===undefined||N.alias.length===0){J[I]=N.fieldLabel
}else{J[I]=N.alias
}K=true
}})
}}var G="";
angular.forEach(s.report.columns,function(O){var N;
if(O.alias===undefined||O.alias.length===0){N=O.fieldLabel
}else{N=O.alias
}angular.forEach(J,function(P){if(P===N){if(G.length===0){G=P
}else{G+=","+P
}}})
});
L.groupItems=G
}x.groups.push(L)
})
}if(s.groupByLevels.length>0){x.groupBy=[];
angular.forEach(s.groupByLevels,function(M,H){var L={};
L.level=H+1;
L.sequence=H+1;
if(M.groupByItems.length>0){var J=M.groupByItems.split(",");
for(var I=0;
I<J.length;
I++){var K=false;
if(!K){angular.forEach(s.report.columns,function(N){if(J[I].split(".")[0]===N.dbBaseName&&J[I].split(".")[1]===N.colName){if(N.alias===undefined||N.alias.length===0){J[I]=N.fieldLabel
}else{J[I]=N.alias
}K=true
}})
}}var G="";
angular.forEach(s.report.columns,function(O){var N;
if(O.alias===undefined||O.alias.length===0){N=O.fieldLabel
}else{N=O.alias
}angular.forEach(J,function(P){if(P===N){if(G.length===0){G=P
}else{G+=","+P
}}})
});
L.fields=G
}x.groupBy.push(L)
})
}s.report.groupAttributes=JSON.stringify(x);
s.groupInfo.groupAttr=x
}s.report.orderAttributes=null;
s.columnOrderMap={};
if(s.primaryOrder.column!==undefined&&s.primaryOrder.column!==null){var D=[];
var v={};
v.columnName=s.primaryOrder.column;
v.orderValue=s.primaryOrder.orderValue;
v.sequence=1;
D.push(v);
var C="";
angular.forEach(s.report.columns,function(G){if(v.columnName===G.dbBaseName+"."+G.colName){if(G.alias!==undefined&&G.alias!==""){C=G.alias
}else{C=G.fieldLabel
}}});
s.columnOrderMap[C]=v.orderValue;
if(s.secondaryOrder.column!==undefined&&s.secondaryOrder.column!==null){var y={};
y.columnName=s.secondaryOrder.column;
y.orderValue=s.secondaryOrder.orderValue;
y.sequence=2;
D.push(y);
var C="";
angular.forEach(s.report.columns,function(G){if(y.columnName===G.dbBaseName+"."+G.colName){if(G.alias!==undefined&&G.alias!==""){C=G.alias
}else{C=G.fieldLabel
}}});
s.columnOrderMap[C]=y.orderValue
}s.report.orderAttributes=JSON.stringify(D)
}if(s.report.columns!==undefined){g.generateQuery(s.report,function(G){console.log("response.query : "+G.query);
if(G.query){s.report.query=G.query;
s.convertedQuery=G.query;
s.clearReportData();
s.currentPage=1;
s.executeConvertedQuery()
}})
}}else{console.log("invalid"+u);
B[2].active=false;
B[2].disabled=true
}}}};
s.numPages=function(){return Math.ceil(s.totalItems/10)
};
s.clearReportData=function(){g.clearReportData(function(){},function(){})
};
s.executeConvertedQuery=function(){s.previewData=[];
o.maskLoading();
s.report.convertedQuery=s.convertedQuery;
s.currencyIdList=[];
angular.forEach(s.columns,function(t){if(t.componentType==="Currency"){s.currencyIdList.push(t.associatedCurrency)
}});
s.resultModels=angular.copy(s.models);
s.resultList=[];
g.retrieveReportTable(s.report,function(t){console.log("res :"+JSON.stringify(t));
if(s.isGroupByCheck){angular.forEach(t.data.records,function(v,u){angular.forEach(v,function(w){s.resultList.push(angular.copy(w))
})
});
t.data.records=angular.copy(s.resultList)
}console.log("$scope.resultList :"+JSON.stringify(s.resultList));
if(s.resultModels.dropzones.A!=undefined&&s.resultModels.dropzones.A.length>0&&t.data!=undefined){angular.forEach(s.resultModels.dropzones.A,function(u){u.searchData[0]=[];
if(u.columns[0].length>0){angular.forEach(t.data.records,function(v){var w={};
angular.forEach(u.columns[0],function(x){if(v[x.text]!==undefined){if(v[x.text]!=null){w[x.text]={value:v[x.text]}
}else{w[x.text]={value:"N/A"}
}}});
if(w!=null&&w!==undefined){u.searchData[0].push(w)
}})
}})
}s.retrievePaginatedData();
o.unMaskLoading()
},function(){o.unMaskLoading()
})
};
s.retrieveViewCurrencyDataRightsForUser=function(){g.retrieveViewCurrencyDataRightsOfLoggedInUser({},function(t){s.viewCurrencyDataPermission=t.data
})
};
s.retrieveViewCurrencyDataRightsForUser();
s.retrievePaginatedData=function(){s.previewData=[];
o.maskLoading();
s.send={offSet:s.paginationOptions.pageNumber,limit:s.paginationOptions.pageSize,isFilter:true,isGrouped:s.isGroupByCheck,sortColumn:s.paginationOptions.sortColumn,sortDirection:s.paginationOptions.sortDirection,sortColumnType:s.paginationOptions.sortColumnType,filterOptions:s.filterOptions};
g.retrieveCurrencyConfiguration({},function(t){console.log("----------------------"+JSON.stringify(t));
s.isCurrencyVisible=t.data;
s.modifiedCurrencyColumns=[];
if(t.data===false||(t.data===true&&s.viewCurrencyDataPermission===false)){angular.forEach(s.columns,function(u){if(u.componentType!=="Currency"){s.modifiedCurrencyColumns.push(u)
}})
}else{s.modifiedCurrencyColumns=angular.copy(s.columns)
}g.retrievePaginatedData(s.send,function(u){if(u.data!==undefined){if(u.data.queryValid!==undefined){var w="Invalid Query.";
var v=o.failure;
o.addMessage(w,v);
o.unMaskLoading()
}else{s.previewData=u.data.records;
s.totalItems=(u.data.totalRecords);
s.gridOptions.totalItems=s.totalItems;
if(s.totalItems>0){s.isAnyRecordsExist=true
}else{s.isAnyRecordsExist=false
}if(!s.isGroupByCheck){s.UIPreviewData=angular.copy(s.previewData);
s.gridColumnDef=[];
angular.forEach(s.modifiedCurrencyColumns,function(z){z.total=undefined;
if(z.showTotal!==undefined&&z.showTotal===true){var y=0;
angular.forEach(s.previewData,function(B){var A=B[z.alias]===undefined?(B[z.fieldLabel]===undefined?0:B[z.fieldLabel]):B[z.alias];
y+=Number(A)
});
if(isNaN(y)){y=0
}z.total=y
}if(z.componentType==="Currency"){if(s.currencyMap[z.associatedCurrency]!==undefined){z.currencySymbol=s.currencyMap[z.associatedCurrency].symbol;
z.format=s.currencyMap[z.associatedCurrency].format
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
}s.gridColumnDef.push({name:z.alias,displayName:z.alias,type:x,enableHiding:false,cellTemplate:"<div class=\"ui-grid-cell-contents\" title=\"{{(COL_FIELD === null || COL_FIELD === '') ? 'N/A':COL_FIELD}}\">{{(COL_FIELD === null || COL_FIELD === \"\") ? 'N/A':COL_FIELD}}</div>",minWidth:200})
});
s.gridOptions.columnDefs=s.gridColumnDef;
s.gridOptions.data=s.UIPreviewData;
s.registerGrid=true
}if(s.isGroupByCheck&&s.groupInfo.groupAttr!==undefined){s.groupRecordsForPreview(s.previewData)
}else{if(s.isEditMode&&!s.isEditModeColorInitialized){s.initializeColor();
s.updateColorConfiguration()
}else{s.initializeColor();
if(s.colorConfigData.length>0){s.applyColor(s.colorConfigForm,true)
}}}o.unMaskLoading()
}}o.unMaskLoading()
},function(){o.addMessage("Some error occurred while retrieving data, try different fields and criteria",o.failure);
o.unMaskLoading()
})
},function(t){o.addMessage("Some error occurred while retrieving data, try different fields and criteria",o.failure);
o.unMaskLoading()
})
};
s.saveReport=function(t,v){s.colorApplied=true;
var x=true;
var w=t;
if(s.report.externalReport===true){t=t.reportBasicForm
}s.submitted=true;
for(var u in t){if(u!=="internalForm"&&u!=="relationshipForm"){if(t[u].$invalid){x=false;
break
}}}if((t!==undefined&&x)||(v!==undefined&&v===true)){s.clearReportData();
t=w;
if(s.report.externalReport!==true){s.report.query=s.convertedQuery
}else{var z=angular.copy(s.report);
s.report={};
s.report.id=z.id;
s.report.query=z.query;
s.report.description=z.description;
s.report.reportName=z.reportName;
s.report.externalReport=z.externalReport;
s.report.status=z.status;
s.report.featureId=z.featureId;
s.report.reportGroup=z.reportGroup
}s.report.colorAttributes=null;
var y=[];
if(s.colorConfigData.length>0){angular.forEach(s.colorConfigData,function(C){var B=[];
var A={};
A.combinationType=C.combinationType;
A.colorName=C.colorName;
angular.forEach(C.columns,function(D){if(s.checkIsValidColorConfig(D)){var H={};
H.label=D.label;
H.type=D.type;
H.componentType=D.componentType;
H.operator=D.operator;
H.isGroupBy=D.isGroupBy;
if(D.filterValue instanceof Object===true){if(angular.isArray(D.filterValue)){var E="";
angular.forEach(D.filterValue,function(K){if(E.length===0){E=K.id
}else{E+=","+K.id
}});
H.filterValue=E
}else{if(H.type!=="timestamp"){H.filterValue=D.filterValue.id
}else{if(D.filterValue!==undefined){var I=new Date(D.filterValue);
var J;
var G;
var F;
if((I.getMonth()+1)<10){G="0"+(I.getMonth()+1)
}else{G=(I.getMonth()+1)
}if(I.getDate()<10){F="0"+I.getDate()
}else{F=I.getDate()
}J=I.getFullYear()+"-"+G+"-"+F;
H.filterValue=J;
if(D.filterValueSecond!==undefined){I=new Date(D.filterValueSecond);
if((I.getMonth()+1)<10){G="0"+(I.getMonth()+1)
}else{G=(I.getMonth()+1)
}if(I.getDate()<10){F="0"+I.getDate()
}else{F=I.getDate()
}J=I.getFullYear()+"-"+G+"-"+F;
H.filterValueSecond=J
}}}}}else{H.filterValue=D.filterValue
}if(D.filterValueSecond!==undefined&&D.filterValueSecond!==null&&H.type!=="timestamp"){H.filterValueSecond=D.filterValueSecond
}B.push(H)
}});
if(B.length>0){A.columns=B;
y.push(A)
}})
}if(y.length>0){s.report.colorAttributes=JSON.stringify(y)
}if(s.isEditMode){angular.forEach(s.resultModels.dropzones.A,function(C,B){var A=B;
if(C.id===0){s.report.tableDtls.push({tableName:C.text,tableSeq:++A})
}else{angular.forEach(s.report.tableDtls,function(D){if(D.id==C.id){D.tableSeq=++A
}})
}});
if(s.report.status==="A"){s.updateReportFlag=true
}if(s.updateReportFlag){g.updateReport(s.report,function(A){console.log("Report UPDATED--"+JSON.stringify(A));
if(t!==undefined){t.$setPristine()
}localStorage.removeItem("reportId");
h.path("/managereport")
},function(){var B="Report cannot be updated.";
var A=o.failure;
o.addMessage(B,A)
})
}else{$("#removePopup").modal("show")
}}else{s.tableDtls=[];
angular.forEach(s.resultModels.dropzones.A,function(C,B){var A=B;
s.tableDtls.push({tableName:C.text,tableSeq:++A})
});
s.report.tableDtls=angular.copy(s.tableDtls);
g.saveReport(s.report,function(A){if(t!==undefined){t.$setPristine()
}localStorage.removeItem("reportId");
h.path("/managereport")
},function(){var B="Report cannot be saved.";
var A=o.failure;
o.addMessage(B,A)
})
}}};
s.renameSubmitted=false;
s.cancelRenamePopup=function(t){s.renameSubmitted=false;
s.changeText="";
t.$setPristine();
$("#renameElements").modal("hide")
};
s.renameOk=function(t){s.renameSubmitted=true;
if(t.$valid){s.models.selected.text=angular.copy(s.changeText);
angular.forEach(s.report.tableDtls,function(u){if(u.id==s.models.selected.id){u.tableName=s.changeText
}});
s.cancelRenamePopup(t)
}};
s.reportConfig={};
s.reportConfig.joins=[];
s.addJoin=function(){if(s.reportConfig.joins.length===0){s.reportConfig.joins.push({firstColumn:"",secondColumn:"",joinType:"="})
}else{var t=s.reportConfig.joins[s.reportConfig.joins.length-1];
if(t.firstColumn!==undefined&&t.firstColumn!==""){s.applyJoin();
s.reportConfig.joins.push({firstColumn:"",secondColumn:"",joinType:"="})
}}};
s.removeJoin=function(t){var v=s.reportConfig.joins[t];
var u=v.firstColumn+v.joinType+v.secondColumn;
s.report.query=s.report.query.toLowerCase().replace(" and "+u,"");
if(s.reportConfig.joins.length===1||(s.reportConfig.joins.length===2&&s.reportConfig.joins[i].firstColumn=="")){s.report.query=s.report.query.toLowerCase().replace(" where "+u,"")
}s.report.query=s.report.query.toLowerCase().replace(u+" and ","");
s.reportConfig.joins.splice(t,1)
};
s.applyJoin=function(){var v="";
if(s.report.query.toLowerCase().indexOf("where")>0){v=s.report.query.substring(s.report.query.toLowerCase().indexOf("where"))
}var t="";
if(v.lastIndexOf("where")<0){t+=" where "
}else{t+=" and "
}angular.forEach(s.reportConfig.joins,function(x,w){var y=x.firstColumn+x.joinType+x.secondColumn;
if(v.indexOf(y)<0){t+=y;
if(w<s.reportConfig.joins.length-1){t+=" and "
}}x.disabled=true
});
if(v!==""){if(t!=" and "){s.report.query+=t
}}else{var u="";
if(s.report.query.toLowerCase().lastIndexOf("order by")>0){u=s.report.query.substring(s.report.query.toLowerCase().lastIndexOf(" order by"));
s.report.query=s.report.query.substring(0,s.report.query.toLowerCase().lastIndexOf(" order by"))
}s.report.query+=t+u
}};
s.initializeGroupByFieldsList=function(v){if(v!==undefined&&v instanceof Object===false&&v!==""){var u=v.split(",");
var w=s.selectedFeatures.split(",");
var t=[];
angular.forEach(w,function(y){for(var x in s.featureFieldMap){if(x===y){var z=[];
var B=s.featureFieldMap[x];
for(var C in B){var A=[];
angular.forEach(B[C],function(D){angular.forEach(u,function(E){if(D.dbBaseName===E.split(".")[0]&&D.dbFieldName===E.split(".")[1]){A.push({id:D.dbBaseName+"."+D.dbFieldName,text:D.fieldLabel})
}})
});
if(A.length>0){z.push({});
z[z.length-1].text=C;
z[z.length-1].children=A
}}t.push({});
t[t.length-1].text=y;
t[t.length-1].children=z
}}});
return t
}};
s.initializeGroupConfiguration=function(){s.groupByColumnsList=s.initializeGroupByFieldsList(s.selectedFields);
s.selectGroupByFields={allowClear:true,multiple:true,closeOnSelect:false,placeholder:"Select Fields",data:s.groupByColumnsList,initSelection:function(u,w){if(s.isEditMode&&!s.isGroupInitialized){s.isGroupInitialized=true;
var v=[];
w(v)
}else{var v=[];
if(s.groupInfo.groupByColumns!==undefined||s.groupInfo.groupByColumns.length>0){var t;
if(!angular.isArray(s.groupInfo.groupByColumns)){t=s.groupInfo.groupByColumns.split(",");
angular.forEach(t,function(x){angular.forEach(s.columns,function(y){if(x===y.dbBaseName+"."+y.colName){v.push({id:y.dbBaseName+"."+y.colName,text:y.fieldLabel})
}})
});
w(v)
}else{t=angular.copy(s.groupInfo.groupByColumns);
angular.forEach(t,function(x){angular.forEach(s.columns,function(y){if(x.id===y.dbBaseName+"."+y.colName){v.push({id:y.dbBaseName+"."+y.colName,text:y.fieldLabel})
}})
});
w(v)
}}}},formatResult:function(t){return t.text
},formatSelection:function(t){return t.text
}};
s.selectGroups={allowClear:true,multiple:true,closeOnSelect:false,placeholder:"Select Fields",data:s.groupByColumnsList,initSelection:function(u,w){if(s.isEditMode){var v=[];
w(v)
}else{var v=[];
if(s.groupInfo.selectedGroup!==undefined||s.groupInfo.selectedGroup.length>0){var t;
if(!angular.isArray(s.groupInfo.selectedGroup)){t=s.groupInfo.selectedGroup.split(",");
angular.forEach(t,function(x){angular.forEach(s.columns,function(y){if(x===y.dbBaseName+"."+y.colName){v.push({id:y.dbBaseName+"."+y.colName,text:y.fieldLabel})
}})
});
w(v)
}else{t=angular.copy(s.groupInfo.selectedGroup);
angular.forEach(t,function(x){angular.forEach(s.columns,function(y){if(x.id===y.dbBaseName+"."+y.colName){v.push({id:y.dbBaseName+"."+y.colName,text:y.fieldLabel})
}})
});
w(v)
}}}},formatResult:function(t){return t.text
},formatSelection:function(t){return t.text
}};
s.currentGroupIndex=1
};
s.updateGroupConfig=function(z){s.groupInfo.isGroupAdded=false;
s.groupInfo.isGroupFieldsEmpty=false;
s.groupInfo.isGroupNameEmpty=false;
s.groupInfo.isGroupNameDuplicate=false;
s.isGroupByCheck=z;
if(s.groupInfo.groupByColumns!==undefined){if(s.groupInfo.groupByColumns.length>0){var t;
if(!angular.isArray(s.groupInfo.groupByColumns)){t=s.groupInfo.groupByColumns.split(",");
for(var w=t.length-1;
w>=0;
w--){var u=false;
angular.forEach(s.columns,function(A){if(t[w]===A.dbBaseName+"."+A.colName){u=true
}});
if(!u){t.splice(w,1)
}}}else{t=s.groupInfo.groupByColumns;
for(var w=t.length-1;
w>=0;
w--){var u=false;
angular.forEach(s.columns,function(A){if(t[w]===A.dbBaseName+"."+A.colName){u=true
}});
if(!u){t.splice(w,1)
}}}if(t.length>0){var v="";
angular.forEach(t,function(A){if(v.length===0){v=A
}else{v+=","+A
}});
s.groupInfo.groupByColumns=v
}else{s.groupInfo.groupByColumns=""
}}}if(s.groupInfo.selectedGroup!==undefined||s.groupInfo.selectedGroup.length>0){var t;
if(!angular.isArray(s.groupInfo.selectedGroup)){t=s.groupInfo.selectedGroup.split(",");
for(var w=t.length-1;
w>=0;
w--){var u=false;
angular.forEach(s.columns,function(A){if(t[w]===A.dbBaseName+"."+A.colName){u=true
}});
if(!u){t.splice(w,1)
}}}else{t=s.groupInfo.selectedGroup;
for(var w=t.length-1;
w>=0;
w--){var u=false;
angular.forEach(s.columns,function(A){if(t[w]===A.dbBaseName+"."+A.colName){u=true
}});
if(!u){t.splice(w,1)
}}}if(t.length>0){var v="";
angular.forEach(t,function(A){if(v.length===0){v=A
}else{v+=","+A
}});
s.groupInfo.selectedGroup=v
}else{s.groupInfo.selectedGroup=""
}}if(s.groups.length>0){for(var w=s.groups.length-1;
w>=0;
w--){var x=s.groups[w].groupItems.split(",");
var u=true;
angular.forEach(x,function(A){if(u){var B=false;
angular.forEach(s.columns,function(C){if(A===C.dbBaseName+"."+C.colName){B=true
}});
if(!B){u=false
}}});
if(!u){s.groups.splice(w,1)
}}}if(s.groupByLevels.length>0){for(var w=s.groupByLevels.length-1;
w>=0;
w--){var x=s.groupByLevels[w].groupByItems.split(",");
var u=true;
angular.forEach(x,function(A){if(u){var B=false;
angular.forEach(s.columns,function(C){if(A===C.dbBaseName+"."+C.colName){B=true
}});
if(!B){u=false
}}});
if(!u){s.groupByLevels.splice(w,1)
}}}if(z){if(s.selectGroupByFields===undefined||s.selectGroups===undefined){s.initializeGroupConfiguration()
}else{var y=[];
if(s.groupInfo.selectedGroup!==undefined||s.groupInfo.selectedGroup.length>0){var t;
if(!angular.isArray(s.groupInfo.selectedGroup)){t=s.groupInfo.selectedGroup.split(",");
angular.forEach(t,function(A){angular.forEach(s.columns,function(B){if(A===B.dbBaseName+"."+B.colName){y.push({id:B.dbBaseName+"."+B.colName,text:B.fieldLabel})
}})
})
}else{t=angular.copy(s.groupInfo.selectedGroup);
angular.forEach(t,function(A){angular.forEach(s.columns,function(B){if(A.id===B.dbBaseName+"."+B.colName){y.push({id:B.dbBaseName+"."+B.colName,text:B.fieldLabel})
}})
})
}$("#groupFields").select2("data",y)
}y=[];
if(s.groupInfo.groupByColumns!==undefined||s.groupInfo.groupByColumns.length>0){var t;
if(!angular.isArray(s.groupInfo.groupByColumns)){t=s.groupInfo.groupByColumns.split(",");
angular.forEach(t,function(A){angular.forEach(s.columns,function(B){if(A===B.dbBaseName+"."+B.colName){y.push({id:B.dbBaseName+"."+B.colName,text:B.fieldLabel})
}})
})
}else{t=angular.copy(s.groupInfo.groupByColumns);
angular.forEach(t,function(A){angular.forEach(s.columns,function(B){if(A.id===B.dbBaseName+"."+B.colName){y.push({id:B.dbBaseName+"."+B.colName,text:B.fieldLabel})
}})
})
}$("#groupByFields").select2("data",y)
}}}s.updateGroupColumns(true);
s.updateGroups()
};
s.updateGroupConfigurations=function(){if(s.isEditMode&&s.isEditModeGroupInitialized===false){s.isEditModeGroupInitialized=true;
if(s.report.groupAttributes!==undefined&&s.report.groupAttributes!==null){var u=JSON.parse(s.report.groupAttributes);
if(u.groups!==undefined){var t=u.groups;
s.groups=[];
angular.forEach(t,function(v){var z={groupName:v.groupName,sequence:v.sequence};
var y=v.groupItems.split(",");
var x=null;
var w=null;
var A=false;
angular.forEach(y,function(B){var C=null;
var D=null;
angular.forEach(s.columns,function(F){var E;
if(F.alias===undefined||F.alias===null||F.alias===""){E=F.fieldLabel
}else{E=F.alias
}if(E===B){C=F.dbBaseName+"."+F.colName;
D=F.fieldLabel
}});
if(C!==null&&D!==null){if(x===null){x=C
}else{x+=","+C
}if(w===null){w=D
}else{w+=", "+D
}}else{A=true
}});
if((x!==null&&w!==null)&&A!==true){z.groupItems=x;
z.groupItemLabels=w;
s.groups.push(z)
}});
if(s.groups.length===1){s.groups[0].groupName="Default Group"
}}if(u.groupBy!==undefined){var t=u.groupBy;
s.groupByLevels=[];
angular.forEach(t,function(w){var z={sequence:w.level};
var v=w.fields.split(",");
var y=null;
var x=null;
var A=false;
angular.forEach(v,function(B){var C=null;
var D=null;
angular.forEach(s.columns,function(F){var E;
if(F.alias===undefined||F.alias===null||F.alias===""){E=F.fieldLabel
}else{E=F.alias
}if(E===B){C=F.dbBaseName+"."+F.colName;
D=F.fieldLabel
}});
if(C!==null&&D!==null){if(y===null){y=C
}else{y+=","+C
}if(x===null){x=D
}else{x+=", "+D
}}else{A=true
}});
if((y!==null&&x!==null)&&A!==true){z.groupByItems=y;
z.groupByItemLabels=x;
s.groupByLevels.push(z)
}})
}s.updateGroupConfig(true)
}else{if(s.isGroupByCheck){s.updateGroupConfig(true)
}else{s.updateExistingFilters()
}}}else{if(s.isGroupByCheck){s.updateGroupConfig(true)
}}};
s.updateGroups=function(w){if(s.selectGroups!==undefined&&s.selectGroupByFields!==undefined){s.remainingFields=angular.copy(s.selectedFields.split(","));
var t=[];
if(s.groupInfo.groupByColumns!==undefined&&s.groupInfo.groupByColumns.length>0&&s.groupInfo.groupByColumns.length>0&&s.groupInfo.groupByColumns instanceof Object===false&&!angular.isArray(s.groupInfo.groupByColumns)){t=s.groupInfo.groupByColumns.split(",")
}if(s.groupInfo.selectedGroup!==undefined&&s.groupInfo.selectedGroup.length>0&&s.groupInfo.selectedGroup.length>0&&s.groupInfo.selectedGroup instanceof Object===false&&!angular.isArray(s.groupInfo.selectedGroup)){var v=s.groupInfo.selectedGroup.split(",");
if(v.length>0){angular.forEach(v,function(y){t.push(y)
})
}}if(s.groups.length>0){angular.forEach(s.groups,function(y){var z=y.groupItems.split(",");
if(z.length>0){angular.forEach(z,function(A){t.push(A)
})
}})
}if(s.groupByLevels.length>0){angular.forEach(s.groupByLevels,function(y){var z=y.groupByItems.split(",");
if(z.length>0){angular.forEach(z,function(A){t.push(A)
})
}})
}angular.forEach(t,function(y){angular.forEach(s.remainingFields,function(A,z){if(y===A){s.remainingFields.splice(z,1)
}})
});
var u="";
angular.forEach(s.remainingFields,function(y){if(u.length===0){u=y
}else{u+=","+y
}});
var x=s.initializeGroupByFieldsList(u);
s.selectGroups.data.splice(0,s.selectGroups.data.length);
angular.forEach(x,function(y){s.selectGroups.data.push(y)
});
s.selectGroupByFields.data.splice(0,s.selectGroupByFields.data.length);
angular.forEach(x,function(y){s.selectGroupByFields.data.push(y)
})
}if(w!==undefined&&w===true){s.updateGroupColumns()
}};
s.$watch("groupInfo.groupByColumns",function(u){if(s.groupInfo.groupByColumns!==undefined&&s.groupInfo.groupByColumns instanceof Object===true&&angular.isArray(s.groupInfo.groupByColumns)){var t="";
angular.forEach(s.groupInfo.groupByColumns,function(w,v){if(v===0){t=w.id
}else{t+=","+w.id
}});
s.groupInfo.groupByColumns=t
}else{s.updateGroups(false)
}});
s.$watch("groupInfo.selectedGroup",function(u){if(s.groupInfo.selectedGroup!==undefined&&s.groupInfo.selectedGroup instanceof Object===true&&angular.isArray(s.groupInfo.selectedGroup)){var t="";
angular.forEach(s.groupInfo.selectedGroup,function(w,v){if(v===0){t=w.id
}else{t+=","+w.id
}});
s.groupInfo.selectedGroup=t
}else{s.updateGroups(false)
}});
s.addGroup=function(t){s.groupInfo.isGroupAdded=true;
s.groupInfo.isGroupFieldsEmpty=false;
s.groupInfo.isGroupNameEmpty=false;
if(t.$valid){if(s.groupInfo.selectedGroup===undefined||s.groupInfo.selectedGroup===""||s.groupInfo.selectedGroup.length===0){s.groupInfo.isGroupFieldsEmpty=true
}if(s.groupInfo.groupName===undefined||s.groupInfo.groupName===""){s.groupInfo.isGroupNameEmpty=true
}if(s.groups.length===0){s.groupInfo.isGroupNameEmpty=false
}if(s.groupInfo.isGroupFieldsEmpty===false&&s.groupInfo.isGroupNameEmpty===false&&s.groupInfo.isGroupNameDuplicate===false){if(s.currentGroupIndex===1&&s.remainingFields.length===0){s.remainingFields=angular.copy(s.selectedFields.split(","));
s.currentGroupIndex++
}var w="";
var v;
if(s.groupInfo.selectedGroup!==undefined){v=s.groupInfo.selectedGroup.split(",");
angular.forEach(v,function(y){angular.forEach(s.AllFieldDetailList,function(z){if(z.id===y){if(w.length===0){w=z.text
}else{w+=", "+z.text
}}})
})
}if(s.groups.length===0){s.groupInfo.groupName="Default Group"
}s.groups.push({sequence:s.groups.length+1,groupName:s.groupInfo.groupName,groupItems:s.groupInfo.selectedGroup,groupItemLabels:w});
s.groupInfo.groupName="";
s.groupInfo.selectedGroup="";
s.groupInfo.isGroupAdded=false;
angular.forEach(v,function(y){angular.forEach(s.remainingFields,function(A,z){if(A===y){s.remainingFields.splice(z,1)
}})
});
var u="";
angular.forEach(s.remainingFields,function(y){if(u.length===0){u=y
}else{u+=","+y
}});
var x=s.initializeGroupByFieldsList(u);
s.selectGroups.data.splice(0,s.selectGroups.data.length);
angular.forEach(x,function(y){s.selectGroups.data.push(y)
});
s.selectGroupByFields.data.splice(0,s.selectGroupByFields.data.length);
angular.forEach(x,function(y){s.selectGroupByFields.data.push(y)
});
t.$setPristine()
}s.updateGroupColumns()
}};
s.removeGroup=function(w){var v=s.groups[w];
s.groups.splice(w,1);
var u=v.groupItems.split(",");
angular.forEach(u,function(y){s.remainingFields.push(y)
});
var t="";
angular.forEach(s.remainingFields,function(y){if(t.length===0){t=y
}else{t+=","+y
}});
var x=s.initializeGroupByFieldsList(t);
s.selectGroups.data.splice(0,s.selectGroups.data.length);
angular.forEach(x,function(y){s.selectGroups.data.push(y)
});
s.selectGroupByFields.data.splice(0,s.selectGroupByFields.data.length);
angular.forEach(x,function(y){s.selectGroupByFields.data.push(y)
});
if(s.groups.length>0){angular.forEach(s.groups,function(z,y){z.sequence=y+1
})
}s.updateGroupColumns()
};
s.addGroupByLevel=function(){if(s.groupInfo.groupByColumns!==undefined&&s.groupInfo.groupByColumns.length>0){var u="";
var t;
t=s.groupInfo.groupByColumns.split(",");
angular.forEach(t,function(v){angular.forEach(s.AllFieldDetailList,function(w){if(w.id===v){if(u.length===0){u=w.text
}else{u+=", "+w.text
}}})
});
s.groupByLevels.push({sequence:s.groupByLevels.length+1,groupByItems:s.groupInfo.groupByColumns,groupByItemLabels:u});
s.groupInfo.groupByColumns="";
s.updateGroups(true)
}};
s.removeGroupByLevel=function(t){s.groupByLevels.splice(t,1);
s.updateGroups(true)
};
s.updateGroupColumns=function(w){if(w!==undefined&&w===true){if(!s.isGroupByCheck){var t=[];
angular.forEach(s.groupByColumns,function(A){angular.forEach(A.columns,function(B){t.push(B)
})
});
angular.forEach(s.groupsColumns,function(A){angular.forEach(A.groupColumns,function(B){t.push(B)
})
});
angular.forEach(s.otherColumns,function(A){t.push(A)
});
if(t.length>0){s.existingColumns=angular.copy(t);
s.columns=angular.copy(t)
}}}else{s.persistGroupChanges()
}if(s.groups.length>0){if(s.groupsColumns.length===0){angular.forEach(s.groups,function(E,A){var D={};
D.groupName=E.groupName;
if(A===0){D.groupName="Default Group"
}var C=E.groupItems.split(",");
var B=[];
angular.forEach(C,function(F){angular.forEach(s.columns,function(G){if(F===G.dbBaseName+"."+G.colName){B.push(G)
}})
});
D.groupColumns=B;
D.sequence=A+1;
s.groupsColumns.push(D)
})
}else{angular.forEach(s.groups,function(C){var B=false;
angular.forEach(s.groupsColumns,function(E,D){if(C.groupName===E.groupName){B=true;
angular.forEach(E.groupColumns,function(G,F){angular.forEach(s.columns,function(H){if(G.dbBaseName+"."+G.colName===H.dbBaseName+"."+H.colName){s.groupsColumns[D].groupColumns[F]=angular.copy(H)
}})
})
}});
if(!B){var A=[];
angular.forEach(C.groupItems.split(","),function(D){angular.forEach(s.columns,function(E){if(D===E.dbBaseName+"."+E.colName){A.push(E)
}})
});
s.groupsColumns.push({groupName:C.groupName,groupColumns:A,sequence:s.groupsColumns.length+1})
}});
for(var x=s.groupsColumns.length-1;
x>=0;
x--){var y=false;
angular.forEach(s.groups,function(A){if(s.groupsColumns[x].groupName===A.groupName){y=true
}});
if(!y){s.groupsColumns.splice(x,1)
}}if(s.groups.length===1){var z=s.groups[0].groupName;
s.groups[0].groupName="Dafult Group";
angular.forEach(s.groupsColumns,function(B,A){if(B.groupName===z){s.groupsColumns[A].groupName="Default Group"
}})
}}}else{s.groupsColumns=[]
}if(s.groupByLevels.length>0){angular.forEach(s.groupByLevels,function(C){var B=false;
angular.forEach(s.groupByColumns,function(E,D){if(C.groupByItems===E.groupByItems){B=true;
angular.forEach(E.columns,function(G,F){angular.forEach(s.columns,function(H){if(G.dbBaseName+"."+G.colName===H.dbBaseName+"."+H.colName){s.groupByColumns[D].columns[F]=angular.copy(H)
}})
})
}});
if(!B){var A=[];
angular.forEach(C.groupByItems.split(","),function(D){angular.forEach(s.columns,function(E){if(D===E.dbBaseName+"."+E.colName){A.push(E)
}})
});
s.groupByColumns.push({columns:A,sequence:s.groupByColumns.length+1,groupByItems:C.groupByItems})
}});
for(var x=s.groupByColumns.length-1;
x>=0;
x--){var y=false;
angular.forEach(s.groupByLevels,function(A){if(s.groupByColumns[x].groupByItems===A.groupByItems){y=true
}});
if(!y){s.groupByColumns.splice(x,1)
}}}else{s.groupByColumns=[]
}var v=angular.copy(s.selectedFields.split(","));
if(v.length>0){var u=[];
if(s.groups.length>0){angular.forEach(s.groups,function(A){var B=A.groupItems.split(",");
if(B.length>0){angular.forEach(B,function(C){u.push(C)
})
}})
}if(s.groupByLevels.length>0){angular.forEach(s.groupByLevels,function(A){var B=A.groupByItems.split(",");
if(B.length>0){angular.forEach(B,function(C){u.push(C)
})
}})
}angular.forEach(u,function(A){angular.forEach(v,function(C,B){if(A===C){v.splice(B,1)
}})
});
angular.forEach(v,function(B){var A=false;
angular.forEach(s.otherColumns,function(C,D){if(B===C.dbBaseName+"."+C.colName){A=true;
angular.forEach(s.columns,function(E){if(B===E.dbBaseName+"."+E.colName){s.otherColumns[D]=angular.copy(E)
}})
}});
if(!A){angular.forEach(s.columns,function(C){if(B===C.dbBaseName+"."+C.colName){s.otherColumns.push(C)
}})
}});
for(var x=s.otherColumns.length-1;
x>=0;
x--){var y=false;
angular.forEach(v,function(A){if(A===s.otherColumns[x].dbBaseName+"."+s.otherColumns[x].colName){y=true
}});
if(!y){s.otherColumns.splice(x,1)
}}}else{s.otherColumns=[]
}angular.forEach(s.otherColumns,function(B,A){B.fieldSequence=A+1
});
s.otherColumns.sort(k);
if(s.groups.length>0){angular.forEach(s.groups,function(B,A){angular.forEach(s.groupsColumns,function(C){if(B.groupName===C.groupName){C.sequence=A+1
}})
});
s.groupsColumns.sort(b)
}if(s.groupByLevels.length>0){angular.forEach(s.groupByLevels,function(B,A){angular.forEach(s.groupByColumns,function(C){if(B.groupByItems===C.groupByItems){C.sequence=A+1
}})
});
s.groupByColumns.sort(b)
}var t=[];
angular.forEach(s.groupByColumns,function(A){angular.forEach(A.columns,function(B){t.push(B)
})
});
angular.forEach(s.groupsColumns,function(A){angular.forEach(A.groupColumns,function(B){t.push(B)
})
});
angular.forEach(s.otherColumns,function(A){t.push(A)
});
if(t.length>0){s.existingColumns=angular.copy(t);
s.columns=angular.copy(t)
}s.updateExistingFilters()
};
s.checkGroupNameExists=function(x,v,y,u){if(x!==undefined){if(y===false){angular.forEach(s.groups,function(z){if(z.groupName!==null&&z.groupName!==undefined&&z.groupName!==""){if(x===z.groupName){s.groupInfo.isGroupNameDuplicate=true
}}})
}else{var w=true;
angular.forEach(s.groups,function(A,z){if(A.groupName!==null&&A.groupName!==undefined&&A.groupName!==""&&z!==0){if(x===A.groupName){w=false;
v.$setValidity("exists",false)
}}});
if(w){var t=[];
angular.forEach(s.groupByColumns,function(z){angular.forEach(z.columns,function(A){t.push(A)
})
});
angular.forEach(s.groupsColumns,function(z){angular.forEach(z.groupColumns,function(A){t.push(A)
})
});
angular.forEach(s.otherColumns,function(z){t.push(z)
});
if(t.length>0){s.existingColumns=angular.copy(t);
s.columns=angular.copy(t)
}s.updateGroupColumns()
}}}};
s.persistGroupChanges=function(){if(s.isGroupByCheck){var t=[];
angular.forEach(s.groupByColumns,function(u){angular.forEach(u.columns,function(v){t.push(v)
})
});
angular.forEach(s.groupsColumns,function(u){angular.forEach(u.groupColumns,function(v){t.push(v)
})
});
angular.forEach(s.otherColumns,function(u){t.push(u)
});
if(t.length>0){s.existingColumns=angular.copy(t);
s.columns=angular.copy(t)
}}};
s.addGroupFilter=function(w,v){var u;
var t=false;
if(w!==undefined){angular.forEach(s.groupsColumns,function(y,x){if(!t){angular.forEach(y.groupColumns,function(z){if(z.dbBaseName===w.dbBaseName&&z.colName===w.colName){u=x;
t=true
}})
}})
}s.isGlobalFilterAdded=true;
if(s.groupsColumns[u].groupColumns[v].selectedFilterType!=="between"&&!(s.groupsColumns[u].groupColumns[v].selectedFilterType==="is null"||s.groupsColumns[u].groupColumns[v].selectedFilterType==="is not null")){if(s.groupsColumns[u].groupColumns[v].filterValFirst!==undefined&&(s.groupsColumns[u].groupColumns[v].filterValFirst.length>0||s.groupsColumns[u].groupColumns[v].filterValFirst!==null)){s.groupsColumns[u].groupColumns[v].filterValFirstInvalid=false;
s.groupsColumns[u].groupColumns[v].selectedFilterTypesList.push({filter:s.groupsColumns[u].groupColumns[v].selectedFilterType,filterLabel:s.filterValueNameMap[s.groupsColumns[u].groupColumns[v].selectedFilterType],filterValFirst:s.groupsColumns[u].groupColumns[v].filterValFirst});
s.groupsColumns[u].groupColumns[v].filterValFirst=undefined;
angular.forEach(s.groupsColumns[u].groupColumns[v].availableFilterTypes,function(y,x){if(y.id===s.groupsColumns[u].groupColumns[v].selectedFilterType){s.groupsColumns[u].groupColumns[v].selectedFilterType=undefined;
s.groupsColumns[u].groupColumns[v].availableFilterTypes.splice(x,1)
}})
}else{s.groupsColumns[u].groupColumns[v].filterValFirstInvalid=true
}}else{if((s.groupsColumns[u].groupColumns[v].selectedFilterType==="is null"||s.groupsColumns[u].groupColumns[v].selectedFilterType==="is not null")){s.groupsColumns[u].groupColumns[v].selectedFilterTypesList.push({filter:s.groupsColumns[u].groupColumns[v].selectedFilterType,filterLabel:s.filterValueNameMap[s.groupsColumns[u].groupColumns[v].selectedFilterType]});
angular.forEach(s.groupsColumns[u].groupColumns[v].availableFilterTypes,function(y,x){if(y.id===s.groupsColumns[u].groupColumns[v].selectedFilterType){s.groupsColumns[u].groupColumns[v].selectedFilterType=undefined;
s.groupsColumns[u].groupColumns[v].availableFilterTypes.splice(x,1)
}})
}else{if(s.groupsColumns[u].groupColumns[v].filterValFirst!==undefined&&s.groupsColumns[u].groupColumns[v].filterValSecond!==undefined&&(s.groupsColumns[u].groupColumns[v].filterValFirst.length>0||s.groupsColumns[u].groupColumns[v].filterValFirst!==null)&&(s.groupsColumns[u].groupColumns[v].filterValSecond.length>0||s.groupsColumns[u].groupColumns[v].filterValSecond!==null)){switch(s.groupsColumns[u].groupColumns[v].dataType){case"int8":s.groupsColumns[u].groupColumns[v].filterValFirst=parseInt(s.groupsColumns[u].groupColumns[v].filterValFirst);
s.groupsColumns[u].groupColumns[v].filterValSecond=parseInt(s.groupsColumns[u].groupColumns[v].filterValSecond);
break;
case"double precision":s.groupsColumns[u].groupColumns[v].filterValFirst=parseFloat(s.groupsColumns[u].groupColumns[v].filterValFirst);
s.groupsColumns[u].groupColumns[v].filterValSecond=parseFloat(s.groupsColumns[u].groupColumns[v].filterValSecond);
break;
case"timestamp":s.groupsColumns[u].groupColumns[v].filterValFirst=new Date(s.groupsColumns[u].groupColumns[v].filterValFirst);
s.groupsColumns[u].groupColumns[v].filterValSecond=new Date(s.groupsColumns[u].groupColumns[v].filterValSecond);
break
}if(s.groupsColumns[u].groupColumns[v].filterValFirst<=s.groupsColumns[u].groupColumns[v].filterValSecond){s.groupsColumns[u].groupColumns[v].filterValFirstInvalid=false;
s.groupsColumns[u].groupColumns[v].filterValSecondInvalid=false;
s.groupsColumns[u].groupColumns[v].filterValFirstMinInvalid=false;
s.groupsColumns[u].groupColumns[v].selectedFilterTypesList.push({filter:s.groupsColumns[u].groupColumns[v].selectedFilterType,filterLabel:s.filterValueNameMap[s.groupsColumns[u].groupColumns[v].selectedFilterType],filterValFirst:s.groupsColumns[u].groupColumns[v].filterValFirst,filterValSecond:s.groupsColumns[u].groupColumns[v].filterValSecond});
s.groupsColumns[u].groupColumns[v].filterValFirst=undefined;
s.groupsColumns[u].groupColumns[v].filterValSecond=undefined;
angular.forEach(s.groupsColumns[u].groupColumns[v].availableFilterTypes,function(y,x){if(y.id===s.groupsColumns[u].groupColumns[v].selectedFilterType){s.groupsColumns[u].groupColumns[v].selectedFilterType=undefined;
s.groupsColumns[u].groupColumns[v].availableFilterTypes.splice(x,1)
}})
}else{s.groupsColumns[u].groupColumns[v].filterValFirstMinInvalid=true
}}if(s.groupsColumns[u].groupColumns[v].filterValFirst===undefined||s.groupsColumns[u].groupColumns[v].filterValFirst===null||s.groupsColumns[u].groupColumns[v].filterValFirst.length===0){s.groupsColumns[u].groupColumns[v].filterValFirstInvalid=true
}if(s.groupsColumns[u].groupColumns[v].filterValSecond===undefined||s.groupsColumns[u].groupColumns[v].filterValSecond===null||s.groupsColumns[u].groupColumns[v].filterValSecond.length===0){s.groupsColumns[u].groupColumns[v].filterValSecondInvalid=true
}}}s.persistGroupChanges()
};
s.removeGroupFilter=function(x,w,v){var y;
var t=false;
if(v!==undefined){angular.forEach(s.groupsColumns,function(A,z){if(!t){angular.forEach(A.groupColumns,function(B){if(B.dbBaseName===v.dbBaseName&&B.colName===v.colName){y=z;
t=true
}})
}})
}if(s.groupsColumns[y].groupColumns[w].selectedFilterTypesList.length>0){var u=s.groupsColumns[y].groupColumns[w].selectedFilterTypesList[x];
s.groupsColumns[y].groupColumns[w].selectedFilterTypesList.splice(x,1);
s.groupsColumns[y].groupColumns[w].availableFilterTypes.push({id:u.filter,text:s.filterValueNameMap[u.filter]})
}s.persistGroupChanges()
};
s.addGroupByLevelFilter=function(w,v){var u;
var t=false;
if(w!==undefined){angular.forEach(s.groupByColumns,function(y,x){if(!t){angular.forEach(y.columns,function(z){if(z.dbBaseName===w.dbBaseName&&z.colName===w.colName){u=x;
t=true
}})
}})
}s.isGlobalFilterAdded=true;
if(s.groupByColumns[u].columns[v].selectedFilterType!=="between"&&!(s.groupByColumns[u].columns[v].selectedFilterType==="is null"||s.groupByColumns[u].columns[v].selectedFilterType==="is not null")){if(s.groupByColumns[u].columns[v].filterValFirst!==undefined&&(s.groupByColumns[u].columns[v].filterValFirst.length>0||s.groupByColumns[u].columns[v].filterValFirst!==null)){s.groupByColumns[u].columns[v].filterValFirstInvalid=false;
s.groupByColumns[u].columns[v].selectedFilterTypesList.push({filter:s.groupByColumns[u].columns[v].selectedFilterType,filterLabel:s.filterValueNameMap[s.groupByColumns[u].columns[v].selectedFilterType],filterValFirst:s.groupByColumns[u].columns[v].filterValFirst});
s.groupByColumns[u].columns[v].filterValFirst=undefined;
angular.forEach(s.groupByColumns[u].columns[v].availableFilterTypes,function(y,x){if(y.id===s.groupByColumns[u].columns[v].selectedFilterType){s.groupByColumns[u].columns[v].selectedFilterType=undefined;
s.groupByColumns[u].columns[v].availableFilterTypes.splice(x,1)
}})
}else{s.groupByColumns[u].columns[v].filterValFirstInvalid=true
}}else{if((s.groupByColumns[u].columns[v].selectedFilterType==="is null"||s.groupByColumns[u].columns[v].selectedFilterType==="is not null")){s.groupByColumns[u].columns[v].selectedFilterTypesList.push({filter:s.groupByColumns[u].columns[v].selectedFilterType,filterLabel:s.filterValueNameMap[s.groupByColumns[u].columns[v].selectedFilterType]});
angular.forEach(s.groupByColumns[u].columns[v].availableFilterTypes,function(y,x){if(y.id===s.groupByColumns[u].columns[v].selectedFilterType){s.groupByColumns[u].columns[v].selectedFilterType=undefined;
s.groupByColumns[u].columns[v].availableFilterTypes.splice(x,1)
}})
}else{if(s.groupByColumns[u].columns[v].filterValFirst!==undefined&&s.groupByColumns[u].columns[v].filterValSecond!==undefined&&(s.groupByColumns[u].columns[v].filterValFirst.length>0||s.groupByColumns[u].columns[v].filterValFirst!==null)&&(s.groupByColumns[u].columns[v].filterValSecond.length>0||s.groupByColumns[u].columns[v].filterValSecond!==null)){switch(s.groupByColumns[u].columns[v].dataType){case"int8":s.groupByColumns[u].columns[v].filterValFirst=parseInt(s.groupByColumns[u].columns[v].filterValFirst);
s.groupByColumns[u].columns[v].filterValSecond=parseInt(s.groupByColumns[u].columns[v].filterValSecond);
break;
case"double precision":s.groupByColumns[u].columns[v].filterValFirst=parseFloat(s.groupByColumns[u].columns[v].filterValFirst);
s.groupByColumns[u].columns[v].filterValSecond=parseFloat(s.groupByColumns[u].columns[v].filterValSecond);
break;
case"timestamp":s.groupByColumns[u].columns[v].filterValFirst=new Date(s.groupByColumns[u].columns[v].filterValFirst);
s.groupByColumns[u].columns[v].filterValSecond=new Date(s.groupByColumns[u].columns[v].filterValSecond);
break
}if(s.groupByColumns[u].columns[v].filterValFirst<=s.groupByColumns[u].columns[v].filterValSecond){s.groupByColumns[u].columns[v].filterValFirstInvalid=false;
s.groupByColumns[u].columns[v].filterValSecondInvalid=false;
s.groupByColumns[u].columns[v].filterValFirstMinInvalid=true;
s.groupByColumns[u].columns[v].selectedFilterTypesList.push({filter:s.groupByColumns[u].columns[v].selectedFilterType,filterLabel:s.filterValueNameMap[s.groupByColumns[u].columns[v].selectedFilterType],filterValFirst:s.groupByColumns[u].columns[v].filterValFirst,filterValSecond:s.groupByColumns[u].columns[v].filterValSecond});
s.groupByColumns[u].columns[v].filterValFirst=undefined;
s.groupByColumns[u].columns[v].filterValSecond=undefined;
angular.forEach(s.groupByColumns[u].columns[v].availableFilterTypes,function(y,x){if(y.id===s.groupByColumns[u].columns[v].selectedFilterType){s.groupByColumns[u].columns[v].selectedFilterType=undefined;
s.groupByColumns[u].columns[v].availableFilterTypes.splice(x,1)
}})
}else{s.groupByColumns[u].columns[v].filterValFirstMinInvalid=true
}}if(s.groupByColumns[u].columns[v].filterValFirst===undefined||s.groupByColumns[u].columns[v].filterValFirst===null||s.groupByColumns[u].columns[v].filterValFirst.length===0){s.groupByColumns[u].columns[v].filterValFirstInvalid=true
}if(s.groupByColumns[u].columns[v].filterValSecond===undefined||s.groupByColumns[u].columns[v].filterValSecond===null||s.groupByColumns[u].columns[v].filterValSecond.length===0){s.groupByColumns[u].columns[v].filterValSecondInvalid=true
}}}s.persistGroupChanges()
};
s.removeGroupByLevelFilter=function(x,w,v){var y;
var t=false;
if(v!==undefined){angular.forEach(s.groupByColumns,function(A,z){if(!t){angular.forEach(A.columns,function(B){if(B.dbBaseName===v.dbBaseName&&B.colName===v.colName){y=z;
t=true
}})
}})
}if(s.groupByColumns[y].columns[w].selectedFilterTypesList.length>0){var u=s.groupByColumns[y].columns[w].selectedFilterTypesList[x];
s.groupByColumns[y].columns[w].selectedFilterTypesList.splice(x,1);
s.groupByColumns[y].columns[w].availableFilterTypes.push({id:u.filter,text:s.filterValueNameMap[u.filter]})
}s.persistGroupChanges()
};
s.shiftGroupColUp=function(t,u){if(u!==0){var v=s.groupsColumns[t].groupColumns[u-1];
s.groupsColumns[t].groupColumns[u-1]=s.groupsColumns[t].groupColumns[u];
s.groupsColumns[t].groupColumns[u]=v
}};
s.shiftGroupColDown=function(t,u){if(u!==s.groupsColumns[t].groupColumns.length-1){var v=s.groupsColumns[t].groupColumns[u+1];
s.groupsColumns[t].groupColumns[u+1]=s.groupsColumns[t].groupColumns[u];
s.groupsColumns[t].groupColumns[u]=v
}};
s.shiftGroupByLevelColUp=function(t,u){if(u!==0){var v=s.groupByColumns[t].columns[u-1];
s.groupByColumns[t].columns[u-1]=s.groupByColumns[t].columns[u];
s.groupByColumns[t].columns[u]=v
}};
s.shiftGroupByLevelColDown=function(t,u){if(u!==s.groupByColumns[t].columns.length-1){var v=s.groupByColumns[t].columns[u+1];
s.groupByColumns[t].columns[u+1]=s.groupByColumns[t].columns[u];
s.groupByColumns[t].columns[u]=v
}};
s.shiftOtherColUp=function(t){if(t!==0){var u=s.otherColumns[t-1];
s.otherColumns[t-1]=s.otherColumns[t];
s.otherColumns[t]=u
}};
s.shiftOtherColDown=function(t){if(t!==s.otherColumns.length-1){var u=s.otherColumns[t+1];
s.otherColumns[t+1]=s.otherColumns[t];
s.otherColumns[t]=u
}};
s.shiftGroupUp=function(t){if(t!==0){var u=s.groups[t-1];
s.groups[t-1]=s.groups[t];
s.groups[t]=u;
s.updateGroupColumns()
}};
s.shiftGroupDown=function(t){if(t!==s.groups.length-1){var u=s.groups[t+1];
s.groups[t+1]=s.groups[t];
s.groups[t]=u;
s.updateGroupColumns()
}};
s.shiftGroupByLevelUp=function(t){if(t!==0){var u=s.groupByLevels[t-1];
s.groupByLevels[t-1]=s.groupByLevels[t];
s.groupByLevels[t]=u;
s.updateGroupColumns()
}};
s.shiftGroupByLevelDown=function(t){if(t!==s.groupByLevels.length-1){var u=s.groupByLevels[t+1];
s.groupByLevels[t+1]=s.groupByLevels[t];
s.groupByLevels[t]=u;
s.updateGroupColumns()
}};
s.groupRecordsForPreview=function(u){s.modifiedColumns=angular.copy(s.modifiedCurrencyColumns);
s.modifiedColumns.sort(k);
if(s.isGroupByCheck&&u!==undefined){var z=[];
var v=0;
angular.forEach(u,function(B,A){z.push(B)
});
s.previewData=angular.copy(z);
var y=[];
var x=[];
s.groupItemListForPreviw=[];
if(s.groupInfo.groupAttr.groupBy!==undefined&&s.groupInfo.groupAttr.groupBy.length>0){s.groupInfo.groupAttr.groupBy.sort(b);
var w=0;
s.levelMap=[];
angular.forEach(s.groupInfo.groupAttr.groupBy,function(A){s.levelMap.push({level:A.level,levelItems:A.fields});
w++;
var B=A.fields.split(",");
angular.forEach(B,function(C){angular.forEach(s.modifiedColumns,function(E,D){var F;
if(E.alias===undefined||E.alias===""){F=E.fieldLabel
}else{F=E.alias
}if(F===C){E.level=A.level;
y.push(E);
x.push(E);
s.modifiedColumns.splice(D,1)
}})
})
});
s.totalLevel=w;
var t=[];
angular.forEach(s.modifiedColumns,function(A){var B;
if(A.alias===undefined||A.alias===""){B=A.fieldLabel
}else{B=A.alias
}t.push(B)
});
angular.forEach(s.levelMap,function(B){var A=B.levelItems.split(",");
angular.forEach(A,function(C){angular.forEach(t,function(E,D){if(E===C){t.splice(D,1)
}})
})
});
s.UIPreviewData=angular.copy(s.previewData)
}if(s.groupInfo.groupAttr.groups!==undefined&&s.groupInfo.groupAttr.groups.length>0){s.groupInfo.groupAttr.groups.sort(b);
if(s.groupInfo.groupAttr.groups.length>1){angular.forEach(s.groupInfo.groupAttr.groups,function(B){var E=B.groupItems.split(",");
var H=B.groupName;
var G={};
G.groupName=H;
G.isGroup=true;
var F=[];
angular.forEach(s.modifiedColumns,function(J){var I;
if(J.alias!==undefined&&J.alias!==""){I=J.alias
}else{I=J.fieldLabel
}angular.forEach(E,function(K){if(K===I){s.groupItemListForPreviw.push(K);
F.push(K)
}})
});
G.groupItems=F;
angular.forEach(s.previewData,function(J){var I=[];
angular.forEach(J.groupRows,function(L){var K=[];
angular.forEach(F,function(M){var N=L[M]===undefined?"-":L[M];
K.push(N)
});
I.push(K)
});
J[H]=I
});
y.push(G);
for(var A=s.modifiedColumns.length-1;
A>=0;
A--){var D=s.modifiedColumns[A];
var C;
if(D.alias!==undefined&&D.alias!==""){C=D.alias
}else{C=D.fieldLabel
}if(E.indexOf(C)>-1){x.push(s.modifiedColumns[A]);
s.modifiedColumns.splice(A,1)
}}})
}else{angular.forEach(s.groupInfo.groupAttr.groups,function(B){var E=B.groupItems.split(",");
for(var A=s.modifiedColumns.length-1;
A>=0;
A--){var D=s.modifiedColumns[A];
var C;
if(D.alias!==undefined&&D.alias!==""){C=D.alias
}else{C=D.fieldLabel
}if(E.indexOf(C)>-1){x.push(s.modifiedColumns[A]);
s.modifiedColumns.splice(A,1)
}}})
}}if(s.modifiedColumns.length>0){angular.forEach(s.modifiedColumns,function(A){y.push(A);
x.push(A)
})
}y.sort(k);
x.sort(k);
s.modifiedColumns=angular.copy(y);
s.modifiedCurrencyColumns=angular.copy(x);
s.gridColumnDef=[];
angular.forEach(s.modifiedCurrencyColumns,function(C){C.total=undefined;
if(C.showTotal!==undefined&&C.showTotal===true){var B=0;
angular.forEach(s.previewData,function(D){angular.forEach(D.groupRows,function(F){var E=F[C.alias]===undefined?(F[C.fieldLabel]===undefined?0:F[C.fieldLabel]):F[C.alias];
B+=Number(E)
})
});
if(isNaN(B)){B=0
}C.total=B
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
}s.gridColumnDef.push({name:C.alias,displayName:C.alias,type:A,enableHiding:false,cellTemplate:"<div class=\"ui-grid-cell-contents\" title=\"{{(COL_FIELD === null || COL_FIELD === '') ? 'N/A':COL_FIELD}}\">{{(COL_FIELD === null || COL_FIELD === \"\") ? 'N/A':COL_FIELD}}</div>",minWidth:200})
});
s.gridOptions.columnDefs=s.gridColumnDef;
s.gridOptions.data=s.UIPreviewData;
s.registerGrid=true;
if(s.isEditMode&&!s.isEditModeColorInitialized){s.initializeColor();
s.updateColorConfiguration()
}else{s.initializeColor();
if(s.colorConfigData.length>0){s.applyColor(s.colorConfigForm,true)
}}}};
s.predicate=function(t){return function(u){return u[t]
}
};
s.applyMultipleOrdering=function c(x,w){var v=angular.copy(w);
var t=[];
if(x.value.length>0){var z=false;
if(s.columnOrderMap[v[0]]!==undefined&&s.columnOrderMap[v[0]]==="desc"){z=true
}x.value=m("orderBy")(x.value,s.predicate(v[0]),z);
var u="****NO MATCH****";
var y=[];
angular.forEach(x.value,function(A,C){var B="";
if(v.length>0){var D=v[0];
if(D.indexOf("-")===0){D=D.substr(1,D.length-1)
}if(B.length===0){if(A[D]===null||A[D]===undefined){B="null"
}else{B=A[D]
}}else{if(A[D]===null||A[D]===undefined){B+=",null"
}else{B+=","+A[D]
}}}if(u!==B){if(C!==0){var E={};
E.key=u;
E.value=angular.copy(y);
t.push(E)
}u=B;
y=[];
y.push(A)
}else{y.push(A)
}if(C===x.value.length-1){var F={};
F.key=u;
F.value=angular.copy(y);
t.push(F)
}});
v.splice(0,1);
if(v.length>0){angular.forEach(t,function(A){A.value=c(A,v)
})
}}return t
};
s.combineMultipleOrdering=function p(v,w){var t=w;
var u=[];
if(t>1){angular.forEach(v,function(y){var x=p(y.value,(t-1));
angular.forEach(x,function(z){u.push(z)
})
})
}else{angular.forEach(v,function(x){angular.forEach(x.value,function(y){u.push(y)
})
})
}return u
};
s.divideGroup=function(z,w,t){var v=[];
if(z.value.length>0){var y=w.split(",");
var u=s.applyMultipleOrdering(z,y);
z.value=s.combineMultipleOrdering(u,y.length);
var x="****NO MATCH****";
var A=[];
angular.forEach(z.value,function(B,D){var C="";
if(y.length>0){angular.forEach(y,function(G){if(G.indexOf("-")===0){G=G.substr(1,G.length-1)
}if(C.length===0){if(B[G]===null||B[G]===undefined){C="null"
}else{C=B[G]
}}else{if(B[G]===null||B[G]===undefined){C+=",null"
}else{C+=","+B[G]
}}})
}if(x!==C){if(D!==0){var E={};
E.key=x;
angular.forEach(t,function(H){if(s.columnOrderMap[H]!==undefined){var G=false;
if(s.columnOrderMap[H]==="desc"){G=true
}A=m("orderBy")(A,s.predicate(H),G)
}});
E.value=angular.copy(A);
v.push(E)
}x=C;
A=[];
A.push(B)
}else{A.push(B)
}if(D===z.value.length-1){var F={};
F.key=x;
angular.forEach(t,function(H){if(s.columnOrderMap[H]!==undefined){var G=false;
if(s.columnOrderMap[H]==="desc"){G=true
}A=m("orderBy")(A,s.predicate(H),G)
}});
F.value=angular.copy(A);
v.push(F)
}})
}return v
};
s.recursiveGroup=function e(u,w,t){var v;
if(w!==undefined){angular.forEach(s.levelMap,function(x){if(w===x.level){v=x.levelItems
}})
}angular.forEach(u,function(y){y.rowCount=y.value.length;
if(v!==undefined){var x=s.divideGroup(y,v,t);
if(w<=s.totalLevel){y.value=e(x,w+1,t)
}else{y.value=x
}}});
return u
};
s.combineRows=function(){angular.forEach(s.previewData,function(A){var B=[];
for(var z=1;
z<=A.rowCount;
z++){var t=A;
var v=2;
var y=z;
var C=[];
while(v<=s.totalLevel){var x=t.value;
var w=false;
var u=0;
angular.forEach(x,function(D){if(w===false){var E=D.rowCount;
if(u+E>=y){w=true;
v++;
t=D;
y-=u
}else{u+=E
}}})
}C=t.value[y-1];
B.push(C)
}A.groupRows=B
})
};
s.checkRowSpan=function(C,D,t,B){if(t!==undefined){var u=C;
var z=2;
var y=B+1;
var A=1;
if(t===1){A=C.rowCount
}while(t>=z){var x=u.value;
var w=false;
var v=0;
angular.forEach(x,function(E){if(w===false){var F=E.rowCount;
if(v+F>=y){w=true;
t--;
u=E;
y-=v;
A=E.rowCount
}else{v+=F
}}})
}return A
}else{return 1
}};
s.checkToRender=function(C,D,t,B){var A=s.checkRowSpan(C,D,t,B);
if(A===1){return true
}if(t!==undefined){var u=C;
var z=2;
var y=B+1;
while(t>=z){var x=u.value;
var w=false;
var v=0;
angular.forEach(x,function(E){if(w===false){var F=E.rowCount;
if(v+F>=y){w=true;
t--;
u=E;
y-=v
}else{v+=F
}}})
}if((y-1)%A===0){return true
}else{return false
}}else{return true
}};
s.initializeColor=function(){s.colorColumns=[];
if(!s.isGroupByCheck){angular.forEach(s.modifiedCurrencyColumns,function(A){var z;
if(A.alias!==null&&A.alias!==""){z=A.alias
}else{z=A.fieldLabel
}var y=A.dataType;
s.colorColumns.push({label:z,type:y,componentType:A.componentType,isGroupBy:false})
})
}else{s.groupAttributes=JSON.parse(s.report.groupAttributes);
var w=[];
angular.forEach(s.groupAttributes.groupBy,function(y){if(y.level===1){w=y.fields.split(",")
}});
angular.forEach(w,function(y){angular.forEach(s.modifiedCurrencyColumns,function(B){var A;
if(B.alias!==null&&B.alias!==""){A=B.alias
}else{A=B.fieldLabel
}var z=B.dataType;
if(A===y){s.colorColumns.push({label:A,type:z,componentType:B.componentType,isGroupBy:true})
}})
})
}if(!s.isColorInitialized){s.isColorInitialized=true;
s.colorConfigData=[];
var x={};
x.combinationType="ANY";
x.columns=[];
x.colorColumns=s.colorColumns;
x.combinationTypes=[{id:"ALL",text:"ALL"},{id:"ANY",text:"ANY"}];
s.colorConfigData.push(x)
}else{for(var u=s.colorConfigData.length-1;
u>=0;
u--){s.colorConfigData[u].colorColumns=angular.copy(s.colorColumns);
for(var t=s.colorConfigData[u].columns.length-1;
t>=0;
t--){var v=false;
angular.forEach(s.colorColumns,function(y){if(y.label===s.colorConfigData[u].columns[t].label){v=true
}});
if(!v){s.removeColor(t,u)
}}}}};
s.updateColorConfiguration=function(){s.isEditModeColorInitialized=true;
if(s.report.colorAttributes!==undefined&&s.report.colorAttributes!==null&&s.report.colorAttributes!==""){var t=JSON.parse(s.report.colorAttributes);
if(t.length>0){s.colorConfigData=[];
angular.forEach(t,function(w){var v={};
v.combinationType=w.combinationType;
v.colorColumns=s.colorColumns;
v.combinationTypes=[{id:"ALL",text:"ALL"},{id:"ANY",text:"ANY"}];
var u=[];
angular.forEach(w.columns,function(y){var B=false;
var z=null;
angular.forEach(s.colorColumns,function(D){if(D.label===y.label){B=true;
z=D
}});
if(B){var C={};
C.label=y.label;
C.type=y.type;
C.componentType=y.componentType;
C.label=y.label;
C.isGroupBy=z.isGroupBy;
var A=s.availabeFilterTypesByDataType[y.type]===undefined?[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"}]:s.availabeFilterTypesByDataType[y.type];
if(y.componentType==="Date range"){A=[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"}]
}C.filters=A;
C.filterValue=y.filterValue;
C.filterValueSecond=y.filterValueSecond;
var x;
angular.forEach(A,function(D){if(D.id===y.operator){x=D
}});
C.operator=x.id;
if(C.type==="varchar"){s.colorColumnData=[];
C.availableFilterValueSelectEqual={multiple:false,placeholder:"  Filter Value",closeOnSelect:true,initSelection:function(D,F){if(s.isEditMode){var E={};
E={id:y.filterValue,text:y.filterValue};
F(E)
}},query:function(F){var E=F.page-1;
var G={q:F.term,page_limit:10,page:E,col_name:C.label,isGrouped:s.isGroupByCheck};
s.names=[];
var H=function(J){s.names=[];
if(J.length!==0){s.names=[];
angular.forEach(J.columnValues,function(K){s.names.push({id:K,text:K})
})
}var I=(E*10)<J.total;
F.callback({results:s.names,more:I})
};
var D=function(){};
g.retrieveLimitedColumnValues(G,H,D)
}};
C.availableFilterValueSelectNotEqual={multiple:false,placeholder:"  Filter Value",closeOnSelect:true,initSelection:function(D,F){if(s.isEditMode){var E={};
E={id:y.filterValue,text:y.filterValue};
F(E)
}},query:function(F){var E=F.page-1;
var G={q:F.term,page_limit:10,page:E,col_name:C.label,isGrouped:s.isGroupByCheck};
s.names=[];
var H=function(J){s.names=[];
if(J.length!==0){s.names=[];
angular.forEach(J.columnValues,function(K){s.names.push({id:K,text:K})
})
}var I=(E*10)<J.total;
F.callback({results:s.names,more:I})
};
var D=function(){};
g.retrieveLimitedColumnValues(G,H,D)
}};
C.availableFilterValueSelectIn={multiple:true,placeholder:"  Filter Value",closeOnSelect:false,initSelection:function(D,F){if(s.isEditMode){var E=[];
angular.forEach(y.filterValue.split(","),function(G){E.push({id:G,text:G})
});
F(E)
}},query:function(F){var E=F.page-1;
var G={q:F.term,page_limit:10,page:E,col_name:C.label,isGrouped:s.isGroupByCheck};
s.names=[];
var H=function(J){s.names=[];
if(J.length!==0){s.names=[];
angular.forEach(J.columnValues,function(K){s.names.push({id:K,text:K})
})
}var I=(E*10)<J.total;
F.callback({results:s.names,more:I})
};
var D=function(){};
g.retrieveLimitedColumnValues(G,H,D)
}};
C.availableFilterValueSelectNotIn={multiple:true,placeholder:"  Filter Value",closeOnSelect:false,initSelection:function(D,F){if(s.isEditMode){var E=[];
angular.forEach(y.filterValue.split(","),function(G){E.push({id:G,text:G})
});
F(E)
}},query:function(F){var E=F.page-1;
var G={q:F.term,page_limit:10,page:E,col_name:C.label,isGrouped:s.isGroupByCheck};
s.names=[];
var H=function(J){s.names=[];
if(J.length!==0){s.names=[];
angular.forEach(J.columnValues,function(K){s.names.push({id:K,text:K})
})
}var I=(E*10)<J.total;
F.callback({results:s.names,more:I})
};
var D=function(){};
g.retrieveLimitedColumnValues(G,H,D)
}}
}u.push(C)
}});
v.columns=angular.copy(u);
if(u.length>0){v.colorName=w.colorName
}s.colorConfigData.push(v)
});
if(s.colorConfigData.length>0){s.applyColor(s.filterForm,true)
}}}};
s.updateColorColumnFilter=function(u,t){s.colorApplied=false;
if(u!==undefined&&u!==null){var v=s.availabeFilterTypesByDataType[u.type]===undefined?[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"}]:s.availabeFilterTypesByDataType[u.type];
var w={};
w.label=u.label;
w.type=u.type;
w.isGroupBy=u.isGroupBy;
w.componentType=u.componentType;
if(u.componentType==="Date range"){v=[{id:"=",text:"equal to"},{id:"!=",text:"not equal to"}]
}w.filters=v;
if(u.type==="varchar"){s.colorColumnData=[];
w.availableFilterValueSelectEqual={multiple:false,placeholder:"  Filter Value",closeOnSelect:true,initSelection:function(x,z){var y=undefined;
z(y)
},query:function(z){var y=z.page-1;
var A={q:z.term,page_limit:10,page:y,col_name:w.label,isGrouped:s.isGroupByCheck};
s.names=[];
var B=function(D){s.names=[];
if(D.length!==0){s.names=[];
angular.forEach(D.columnValues,function(E){s.names.push({id:E,text:E})
})
}var C=(y*10)<D.total;
z.callback({results:s.names,more:C})
};
var x=function(){};
g.retrieveLimitedColumnValues(A,B,x)
}};
w.availableFilterValueSelectNotEqual={multiple:false,placeholder:"  Filter Value",closeOnSelect:true,initSelection:function(x,z){var y=undefined;
z(y)
},query:function(z){var y=z.page-1;
var A={q:z.term,page_limit:10,page:y,col_name:w.label,isGrouped:s.isGroupByCheck};
s.names=[];
var B=function(D){s.names=[];
if(D.length!==0){s.names=[];
angular.forEach(D.columnValues,function(E){s.names.push({id:E,text:E})
})
}var C=(y*10)<D.total;
z.callback({results:s.names,more:C})
};
var x=function(){};
g.retrieveLimitedColumnValues(A,B,x)
}};
w.availableFilterValueSelectIn={multiple:true,placeholder:"  Filter Value",closeOnSelect:false,initSelection:function(x,z){var y=[];
z(y)
},query:function(z){var y=z.page-1;
var A={q:z.term,page_limit:10,page:y,col_name:w.label,isGrouped:s.isGroupByCheck};
s.names=[];
var B=function(D){s.names=[];
if(D.length!==0){s.names=[];
angular.forEach(D.columnValues,function(E){s.names.push({id:E,text:E})
})
}var C=(y*10)<D.total;
z.callback({results:s.names,more:C})
};
var x=function(){};
g.retrieveLimitedColumnValues(A,B,x)
}};
w.availableFilterValueSelectNotIn={multiple:true,placeholder:"  Filter Value",closeOnSelect:false,initSelection:function(x,z){var y=[];
z(y)
},query:function(z){var y=z.page-1;
var A={q:z.term,page_limit:10,page:y,col_name:w.label,isGrouped:s.isGroupByCheck};
s.names=[];
var B=function(D){s.names=[];
if(D.length!==0){s.names=[];
angular.forEach(D.columnValues,function(E){s.names.push({id:E,text:E})
})
}var C=(y*10)<D.total;
z.callback({results:s.names,more:C})
};
var x=function(){};
g.retrieveLimitedColumnValues(A,B,x)
}}
}s.colorConfigData[t].columns.push(w);
s.colorConfigData[t].currentColumn=undefined
}};
s.removeColor=function(u,t){s.colorApplied=false;
if(s.colorConfigData[t].columns.length>0){s.colorConfigData[t].columns.splice(u,1)
}if(s.colorConfigData[t].columns.length===0){s.removeColorCombination(t)
}s.applyColor(s.filterForm)
};
s.removeColorCombination=function(t){s.colorApplied=false;
if(s.colorConfigData.length===1){s.colorConfigData[0].combinationType="ANY";
s.colorConfigData[0].columns=[];
s.colorConfigData[0].colorColumns=s.colorColumns;
s.colorConfigData[0].combinationTypes=[{id:"ALL",text:"ALL"},{id:"ANY",text:"ANY"}];
s.colorConfigData[0].colorName=undefined
}else{s.colorConfigData.splice(t,1)
}};
s.addColorCombination=function(){s.colorApplied=false;
if(s.colorConfigData[s.colorConfigData.length-1].columns.length>0){var t={};
t.combinationType="ANY";
t.columns=[];
t.colorColumns=s.colorColumns;
t.combinationTypes=[{id:"ALL",text:"ALL"},{id:"ANY",text:"ANY"}];
s.colorConfigData.push(t)
}else{o.addMessage("Select atleast one criteria field to add new.",o.failure)
}};
s.applyColor=function(v,u){if((v!==undefined&&v.$valid)||(u!==undefined&&u===true)){s.filterForm=v;
if(s.colorConfigData.length>0){if(s.previewData.length>0){if(true){var t=angular.copy(s.previewData);
angular.forEach(s.resultModels.dropzones.A,function(w){if(w.columns[0].length>0){angular.forEach(w.searchData[0],function(x,y){w.searchData[0][y].rowColor="white";
angular.forEach(x,function(A,z){w.searchData[0][y][z].color="white"
})
})
}});
angular.forEach(s.resultModels.dropzones.A,function(w){if(w.columns[0].length>0){angular.forEach(w.searchData[0],function(y,A){var x=false;
var z="white";
angular.forEach(s.colorConfigData,function(B){if(B.columns.length>0){var C=s.executeFilterOnRow(y,B);
if(C){x=true;
z=B.colorName
}}});
if(x){angular.forEach(y,function(C,B){w.searchData[0][A][B].color=z
});
w.searchData[0][A].rowColor=z
}})
}})
}else{var t=angular.copy(s.previewData);
angular.forEach(t,function(x,w){angular.forEach(x.groupRows,function(y,z){t[w].groupRows[z].rowColor="white";
angular.forEach(y,function(B,A){t[w].groupRows[z][A]={value:B,color:"white"}
})
})
});
angular.forEach(t,function(A,y){var x=A.groupRows;
var C=false;
var z;
s.groupAttributes=JSON.parse(s.report.groupAttributes);
var B;
angular.forEach(s.groupAttributes.groupBy,function(D){if(D.level===1){B=D.fields.split(",")
}});
var w={};
angular.forEach(B,function(D,F){var E;
angular.forEach(x,function(G){E=G[D]
});
w[D]=E
});
angular.forEach(s.colorConfigData,function(D){if(D.columns.length>0){var E=s.executeFilterOnRow(w,D);
if(E){C=true;
z=D.colorName
}}});
if(C){angular.forEach(A.groupRows,function(D,E){angular.forEach(D,function(G,F){t[y].groupRows[E][F].color=z
});
t[y].groupRows[E].rowColor=z
})
}});
s.UIPreviewData=angular.copy(t)
}}}else{if(!s.isGroupByCheck){angular.forEach(s.UIPreviewData,function(w,x){s.UIPreviewData[x].rowColor="white";
angular.forEach(w,function(z,y){s.UIPreviewData[x][y].color="white"
})
})
}else{angular.forEach(s.UIPreviewData,function(x,w){angular.forEach(x.value,function(z,y){s.UIPreviewData[w].groupRows[y].rowColor="white";
angular.forEach(z,function(B,A){s.UIPreviewData[w].groupRows[y][A].color="white"
})
})
})
}}}};
s.checkIsValidColorConfig=function(t){if(t!==undefined){if(t.operator===undefined||t.operator===null){return false
}if(t.filterValue===undefined&&t.operator!=="is null"&&t.operator!=="is not null"){return false
}if(t.operator==="between"&&(t.filterValueSecond===undefined||t.filterValueSecond===null)){return false
}return true
}return false
};
s.executeFilterOnRow=function(w,t){if(t.combinationType==="ANY"){var u=false;
angular.forEach(w,function(y,x){if(u===false){angular.forEach(t.columns,function(z){if(x===z.label&&u===false){if(s.checkIsValidColorConfig(z)){var B=angular.copy(z);
if(B.type==="timestamp"){var C=new Date(B.filterValue);
B.filterValue=new Date(C.getFullYear(),C.getMonth(),C.getDate());
if(B.filterValueSecond!==undefined){C=new Date(B.filterValueSecond);
B.filterValueSecond=new Date(C.getFullYear(),C.getMonth(),C.getDate())
}}var A=s.executeFilter(y.value,B);
if(A){u=true
}}}})
}});
return u
}else{if(t.columns.length>0){var v=true;
angular.forEach(t.columns,function(x){if(v===true){angular.forEach(w,function(z,y){if(y===x.label&&v===true){if(s.checkIsValidColorConfig(x)){var B=angular.copy(x);
if(B.type==="timestamp"){var C=new Date(B.filterValue);
B.filterValue=new Date(C.getFullYear(),C.getMonth(),C.getDate());
if(B.filterValueSecond!==undefined){C=new Date(B.filterValueSecond);
B.filterValueSecond=new Date(C.getFullYear(),C.getMonth(),C.getDate())
}}var A=s.executeFilter(z.value,B);
if(A===false){v=false
}}}})
}});
return v
}else{return false
}}};
s.executeFilter=function(C,B){if(((C!==undefined&&C!==null&&C!=="")||(B.type==="double precision")||(B.type==="int8"))&&(B.operator!=="is null"&&B.operator!=="is not null")){var w=false;
var z=[];
if(!(C!==undefined&&C!==null&&C!=="")){C=0
}if(B.type==="timestamp"){if(B.componentType!=="Date range"){C=C.substring(0,10);
var v=C.split("/");
C=new Date(parseInt(v[2]),parseInt(v[1])-1,parseInt(v[0]))
}else{var y=C.split("to");
var x=y[0].trim().substring(0,10).split("/");
C=new Date(x[2],parseInt(x[1])-1,x[0]);
z.push(C);
var A=y[1].trim().substring(0,10).split("/");
C=new Date(A[2],parseInt(A[1])-1,A[0]);
z.push(C)
}}else{if(B.componentType==="Currency"){C=parseFloat(C.split(" ")[0].trim())
}else{if(B.filterValue instanceof Object){if(!angular.isArray(B.filterValue)){B.filterValue=B.filterValue.id
}else{var D="";
angular.forEach(B.filterValue,function(E){if(D.length===0){D=E.id
}else{D+=","+E.id
}});
B.filterValue=D
}}}}switch(B.type){case"varchar":switch(B.operator){case"=":if(C===B.filterValue){w=true
}break;
case"!=":if(C!==B.filterValue){w=true
}break;
case"in":var t=B.filterValue.split(",");
if(t.indexOf(C)>-1){w=true
}break;
case"not in":var t=B.filterValue.split(",");
if(t.indexOf(C)===-1){w=true
}break;
case"like":B.filterValue="%"+B.filterValue+"%";
var u=B.filterValue.replace(/%/g,".*");
u=u.replace(/_/g,".");
if(C.match(u)){w=true
}break
}break;
case"int8":switch(B.operator){case"=":if(parseInt(C)===parseInt(B.filterValue)){w=true
}break;
case"!=":if(parseInt(C)!==parseInt(B.filterValue)){w=true
}break;
case">":if(parseInt(C)>parseInt(B.filterValue)){w=true
}break;
case">=":if(parseInt(C)>=parseInt(B.filterValue)){w=true
}break;
case"<":if(parseInt(C)<parseInt(B.filterValue)){w=true
}break;
case"<=":if(parseInt(C)<=parseInt(B.filterValue)){w=true
}break;
case"between":if(parseInt(C)>=parseInt(B.filterValue)&&parseInt(C)<=parseInt(B.filterValueSecond)){w=true
}break
}break;
case"double precision":switch(B.operator){case"=":if(parseFloat(C)===parseFloat(B.filterValue)){w=true
}break;
case"!=":if(parseFloat(C)!==parseFloat(B.filterValue)){w=true
}break;
case">":if(parseFloat(C)>parseFloat(B.filterValue)){w=true
}break;
case">=":if(parseFloat(C)>=parseFloat(B.filterValue)){w=true
}break;
case"<":if(parseFloat(C)<parseFloat(B.filterValue)){w=true
}break;
case"<=":if(parseFloat(C)<=parseFloat(B.filterValue)){w=true
}break;
case"between":if(parseFloat(C)>=parseFloat(B.filterValue)&&parseFloat(C)<=parseFloat(B.filterValueSecond)){w=true
}break
}break;
case"timestamp":if(B.componentType==="Date range"){if(B.operator==="="){if(z[0].getTime()<=B.filterValue.getTime()&&z[1].getTime()>=B.filterValue.getTime()){w=true
}}else{if(B.operator==="!="){if(z[0].getTime()>B.filterValue.getTime()||z[1].getTime()<B.filterValue.getTime()){w=true
}}}}else{switch(B.operator){case"=":if(C.getTime()===B.filterValue.getTime()){w=true
}break;
case"!=":if(C.getTime()!==B.filterValue.getTime()){w=true
}break;
case">":if(C.getTime()>B.filterValue.getTime()){w=true
}break;
case"<":if(C.getTime()<B.filterValue.getTime()){w=true
}break;
case"between":if(C.getTime()>=B.filterValue.getTime()&&C.getTime()<=B.filterValueSecond.getTime()){w=true
}break
}}break;
case"boolean":switch(B.operator){case"=":if(C===B.filterValue){w=true
}break;
case"!=":if(C!==B.filterValue){w=true
}break
}break
}return w
}else{if(B.operator==="is null"||B.operator==="is not null"){if((C===undefined||C===null||C==="")&&B.operator==="is null"){return true
}else{if(!(C===undefined||C===null||C==="")&&B.operator==="is not null"){return true
}else{return false
}}}else{return false
}}};
s.checkValidColorRange=function(v,u,y,A,z,t){z.$setValidity("invalidRange",true);
if(t!==undefined&&t!==null){t.$setValidity("invalidRange",true)
}var x;
var w;
if(v==="between"){if(y===undefined||y===null||y.length===0||A===undefined||A===null||A.length===0){return
}switch(u){case"int8":x=parseInt(y);
w=parseInt(A);
break;
case"double precision":x=parseFloat(y);
w=parseFloat(A);
break;
case"timestamp":x=angular.copy(new Date(y));
w=angular.copy(new Date(A));
break
}if(x>w){z.$setValidity("invalidRange",false);
t.$setValidity("invalidRange",false)
}}};
s.shiftColUp=function(t){if(t!=0){var u=s.columns[t-1];
s.columns[t-1]=s.columns[t];
s.columns[t]=u;
var v=s.report.columns[t-1];
s.report.columns[t-1]=s.report.columns[t];
s.report.columns[t]=v;
var w=s.associatedFieldList[t-1];
s.associatedFieldList[t-1]=s.associatedFieldList[t];
s.associatedFieldList[t]=w;
selectedFields="";
angular.forEach(s.associatedFieldList,function(x){if(s.selectedFields.length===0){s.selectedFields+=x.dbBaseName+"."+x.dbFieldName
}else{s.selectedFields+=","+x.dbBaseName+"."+x.dbFieldName
}})
}};
s.shiftColDown=function(t){if(t!=s.columns.length-1){var w=s.columns[t+1];
s.columns[t+1]=s.columns[t];
s.columns[t]=w;
var u=s.report.columns[t+1];
s.report.columns[t+1]=s.report.columns[t];
s.report.columns[t]=u;
var v=s.associatedFieldList[t+1];
s.associatedFieldList[t+1]=s.associatedFieldList[t];
s.associatedFieldList[t]=v;
s.selectedFields="";
angular.forEach(s.associatedFieldList,function(x){if(s.selectedFields.length===0){s.selectedFields+=x.dbBaseName+"."+x.dbFieldName
}else{s.selectedFields+=","+x.dbBaseName+"."+x.dbFieldName
}})
}};
s.retrieveFeatureSectionFieldMap=function(){s.featureFieldMap=[];
s.AllFieldsEntitys=[];
g.retrieveFeatureSectionField(function(u){s.featureFieldMap=angular.copy(u.data);
for(var t in u.data){s.tempFeatureList.push({id:t,text:t});
s.sectionFieldMap=u.data[t];
for(var v in s.sectionFieldMap){s.AllSectionDetailList.push({id:v,text:v});
angular.forEach(s.sectionFieldMap[v],function(w){s.AllFieldDetailList.push({id:w.dbBaseName+"."+w.dbFieldName,text:w.fieldLabel});
s.AllFieldsEntitys.push(w)
})
}}s.entityFeatureList=[];
s.subEntityFeatureList=[];
angular.forEach(s.tempFeatureList,function(x){var w=x.id;
var y=w.split(".");
if(y.length>1){s.subEntityFeatureList.push(x)
}else{s.entityFeatureList.push(x)
}});
if(s.entityFeatureList.length>0){s.featureList.push({});
s.featureList[s.featureList.length-1].text="Entity";
s.featureList[s.featureList.length-1].children=s.entityFeatureList
}if(s.subEntityFeatureList.length>0){s.featureList.push({});
s.featureList[s.featureList.length-1].text="Sub-Entity";
s.featureList[s.featureList.length-1].children=s.subEntityFeatureList
}s.fieldList.splice(0,s.fieldList.length);
s.retrieveSectionFields(s.selectedFeatures);
s.updateOrders();
s.configureRelationship()
})
};
s.resetReport=function(t){t.$setPristine();
s.report={};
s.report.externalReport=false;
s.columns=[];
s.clearReportData();
h.path("managereport")
};
s.retrieveFeatureSectionFieldMap();
s.updateCancel=function(){$("#removePopup").modal("hide");
o.removeModalOpenCssAfterModalHide();
$(".modal-backdrop").remove()
};
s.updateOk=function(){$("#removePopup").modal("hide");
o.removeModalOpenCssAfterModalHide();
$(".modal-backdrop").remove();
s.updateReportFlag=true;
s.saveReport(undefined,true)
};
s.address1=false;
s.address2=false;
s.reverse=false;
s.city=false;
s.state=false;
s.orderByField="null";
s.reverseSort=null;
s.showCol=false;
s.showChecked=false;
s.extraFields=[];
s.allowedColumns=["column"];
s.sort=function(t,u){s.reverse=!s.reverse;
if(s.resultModels!==undefined&&s.resultModels.dropzones!==undefined&&s.resultModels.dropzones.A[t].searchData[0]!==undefined){s.resultModels.dropzones.A[t].searchData[0].sort(function(w,v){if(s.reverse){return v[u]["value"]>w[u]["value"]
}else{return v[u]["value"]<w[u]["value"]
}})
}};
s.renameMainList=function(t){if(s.models.dropzones.A!=undefined&&s.models.dropzones.A.length>0){angular.forEach(s.models.dropzones.A,function(u){angular.forEach(u.columns[0],function(v){if(v.colField===t.dbBaseName+"."+t.colName){v.text=angular.copy(t.alias)
}})
})
}};
s.dropTrashCallback=function(x,u,w,y,v,t){if(s.models.dropzones.A!==undefined&&s.models.dropzones.A.length>0){s.selectedFields="";
angular.forEach(s.models.dropzones.A,function(z){angular.forEach(z.columns[0],function(B,A){if(B.colField===w.colField){z.columns[0].splice(A,1)
}})
});
angular.forEach(s.models.dropzones.A,function(z){angular.forEach(z.columns[0],function(C){if(s.selectedFields!=undefined&&s.selectedFields.length>0){var A=angular.copy(s.selectedFields.split(","));
if(A.length>0){var B=0;
angular.forEach(A,function(D){if(D==C.colField){B++
}});
if(B==0){s.selectedFields=s.selectedFields+","+C.colField
}}s.loadColumnValues1();
s.configureRelationship()
}else{s.selectedFields=C.colField;
s.loadColumnValues1();
s.configureRelationship()
}})
})
}return w
};
s.dropColumnCallback=function(y,v,x,A,w,t){var u;
var z=[];
if(x.newCol){x.newCol=false;
angular.forEach(s.models.dropzones.A[0].searchData[0],function(B){B[x.colField]=null
})
}else{if(x.newField){angular.forEach(s.models.dropzones.A[0].searchData[0],function(B){if(x.grpElement&&x.grpElement.grp){angular.forEach(s.overviewList,function(C){var D={text:C,colField:C,newField:true,type:"col"};
if(s.overViewMap[C].grpElement.grp){D.grpElement=s.overViewMap[C].grpElement.values
}z.push(D);
B[C]=C
})
}else{B[x.colField]=x.value
}})
}}if(z.length>0){return z
}else{return x
}};
s.content="kfjhgdfj";
s.dropRowCallback=function(x,u,w,y,v,t){if(w.newRow){w.newRow=false;
angular.forEach(s.models.dropzones.A[0].searchData[0][0],function(A,z){w[z]=null
})
}else{}return w
};
s.exportAction=function(t){switch(t){case"pdf":s.$broadcast("export-pdf",{});
break;
case"excel":console.log("----excel-----");
s.$broadcast("export-excel",{});
break;
case"doc":s.$broadcast("export-doc",{});
break;
default:console.log("no event caught")
}};
s.$watch("resultModels.dropzones",function(t){s.modelAsJson=angular.toJson(t,true)
},true);
s.toPdf=function(){var t=$("#exportZone");
html2canvas(t,{onrendered:function(u){var w=new jsPDF("p","mm");
var v=u.toDataURL("image/jpeg");
w.setFontSize(30);
w.text(15,15,"Report");
w.addImage(v,"PNG",10,30,190,60);
w.save("Report.pdf")
}})
};
s.expandedFeatures=[];
s.toggle=function(v,u){var t="tblFeatureId"+(v+"T")+(u+1);
if(s.expandedFeatures.indexOf(t)>-1){$("."+t).collapse("hide");
s.expandedFeatures.splice(s.expandedFeatures.indexOf(t),1)
}else{s.expandedFeatures.push(t);
$("."+t).collapse("show")
}};
s.check=function(t){if(t.colField=="fulladdress"&&!s.showChecked){s.showChecked=true
}else{if(t.colField=="fulladdress"&&s.showChecked){s.showChecked=false
}}};
s.overviewList=[];
s.overViewMap={};
s.selectOverView=function(t){if(t.checked){s.overviewList.push(t.value);
s.overViewMap[t.value]=t
}else{s.overviewList.splice(s.overviewList.indexOf(t.value),1);
delete s.overViewMap[t.value]
}}
}])
});