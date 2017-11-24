define(["hkg","reportBuilderService"],function(a){a.register.controller("ViewReport",["$rootScope","$scope","$filter","ngTableParams","ReportBuilderService","$sce",function(b,d,g,f,e,c){b.maskLoading();
d.entity="REPORTBUILDER.";
d.reportList=[];
d.reportColumns=[];
d.dt={};
d.open=function(h,i){h.preventDefault();
h.stopPropagation();
d.dt[i]=true
};
d.dateOptions={"year-format":"'yy'","starting-day":1};
d.format=b.dateFormat;
d.today=new Date();
d.availabeFilterTypesByDataType={int8:["=","!=","<",">","<=",">=","between"],varchar:["=","!="],timestamp:["=","!=","<",">","between"]};
d.selectFilterColumns={multiple:true,placeholder:"Add Filter",data:d.reportColumns};
e.retrieveReportTitles(function(h){d.reportList=h
},function(){b.addMessage("Failed to load reports.",b.failure)
});
d.previewTableParams=new f({page:1,count:10},{total:0,getData:function(h,i){if(d.report!==undefined){d.retrieveParams.count=d.previewTableParams.count();
d.retrieveParams.page=d.previewTableParams.page();
e.retrieveReportTable(d.retrieveParams,function(j){if(j.queryValid!==undefined){var l="Invalid Query.";
var k=b.failure;
b.addMessage(l,k)
}else{d.previewData=j.records;
d.previewTableParams.total(j.totalRecords);
h.resolve(d.previewData);
b.unMaskLoading()
}},function(){})
}}});
d.executeQuery=function(){d.previewData=[];
d.filterColumns=[];
d.reportColumns.splice(0,d.reportColumns.length);
d.convertedQuery=undefined;
if(d.report.selectedReport!==null&&d.report.selectedReport!==""){d.filterColumns=[];
console.log("$scope.report.selectedReport--"+JSON.stringify(d.report.selectedReport));
e.retrieveReport(d.report.selectedReport,function(h){d.report.selectedReportJSON=h;
d.reportColumns.splice(0,d.reportColumns.length);
d.selectedFilterColumns="";
$("#reportFilterColumns").select2("data","");
angular.forEach(d.report.selectedReportJSON.columns,function(j,k){var i=0;
angular.forEach(d.report.selectedReportJSON.columns,function(m,l){var n=j.colName.substring(j.colName.indexOf(".")+1);
var o=m.colName.substring(m.colName.indexOf(".")+1);
if(n===o){i++;
if(i>1){m.joinAlias=m.colName.replace(".","_");
if(m.alias===""){m.alias=m.colName
}}}});
d.reportColumns.push({id:k,text:(j.alias==="")?j.colName.substring(j.colName.indexOf(".")+1):j.alias})
});
if(d.report.selectedReportJSON.externalReport){e.retrieveReportLink({reportCode:d.report.selectedReportJSON.reportCode,editable:d.report.selectedReportJSON.editable},function(i){d.externalReportLink=c.trustAsResourceUrl(i.reportLink)
},function(){})
}else{d.retrieveParams={query:d.report.selectedReportJSON.query,count:d.previewTableParams.count(),page:d.previewTableParams.page(),saveQuery:false};
d.previewTableParams.page(1);
d.previewTableParams.reload()
}},function(){})
}};
$("#reportFilterColumns").on("select2-selecting",function(i){var h=d.report.selectedReportJSON.columns[i.val];
h.id=i.val;
h.availableFilterTypes=d.availabeFilterTypesByDataType[h.dataType]===undefined?["=","!="]:d.availabeFilterTypesByDataType[h.dataType];
h.availableFilterValues=[];
if(h.dataType==="varchar"){h.availableFilterValueSelect={multiple:true,placeholder:"  Filter Value",closeOnSelect:false,ajax:{url:b.apipath+"report/retrievelimitedcolumnvalues",dataType:"json",data:function(j,k){return{q:j,page_limit:10,page:k-1,col_name:h.colName.substring(h.colName.indexOf(".")+1)}
},results:function(m,l){m=m.data;
var j=(l*10)<m.total;
var k=[];
angular.forEach(m.columnValues,function(o,n){k.push({id:o,text:o})
});
return{results:k,more:j}
}}}
}d.filterColumns.push(h)
});
$("#reportFilterColumns").on("select2-removing",function(h){angular.forEach(d.filterColumns,function(i,j){if(i.id===h.val){d.filterColumns.splice(j,1);
i.availableFilterTypes=undefined;
i.availableFilterValues=undefined;
i.availableFilterValueSelect=undefined;
i.filterValFirst=undefined;
i.selectedFilterType=undefined;
i.filterValSecond=undefined;
i.filter=null
}if(d.filterColumns.length===0){d.executeQuery()
}})
});
d.changeOrder=function(h){if(h.order===undefined||h.order==="desc"){h.order="asc"
}else{h.order="desc"
}var i=d.report.selectedReportJSON.query;
var j=false;
if(d.convertedQuery!==undefined){i=d.convertedQuery;
j=true
}if(i.toLowerCase().indexOf("order by")>0){i=i.substring(0,i.toLowerCase().indexOf("order by"))
}i+=" order by "+h.colName+" "+h.order;
d.retrieveParams={query:i,count:d.previewTableParams.count(),page:d.previewTableParams.page(),saveQuery:j};
d.previewTableParams.reload();
if(d.prevCol!==undefined&&d.prevCol.colName!==h.colName){d.prevCol.order=undefined
}d.prevCol=h
};
d.applyFilters=function(){d.columnsCopy=angular.copy(d.report.selectedReportJSON.columns);
angular.forEach(d.columnsCopy,function(j,i){if(j.selectedFilterType!==undefined){if(j.dataType==="varchar"){var l=j.filterValFirst.replace(/,/gi,"','");
j.filter=" IN ('"+l+"')";
if(j.selectedFilterType==="!="){j.filter=" NOT "+j.filter
}}else{if(j.dataType==="timestamp"){if(j.selectedFilterType==="="||j.selectedFilterType==="!="||j.selectedFilterType==="between"){j.filter=" BETWEEN '"+g("date")(j.filterValFirst,"MMM dd yyyy")+"' AND ";
if(j.selectedFilterType==="between"){var h=new Date(j.filterValSecond);
h.setDate(h.getDate()+1);
j.filter+="'"+g("date")(h,"MMM dd yyyy")+"'"
}else{var h=new Date(j.filterValFirst);
h.setDate(h.getDate()+1);
j.filter+="'"+g("date")(h,"MMM dd yyyy")+"'";
if(j.selectedFilterType==="!="){j.filter=" NOT "+j.filter
}}}else{if(j.selectedFilterType==="<"){var k=new Date(j.filterValFirst);
k.setDate(k.getDate()+1);
j.filter=j.selectedFilterType+"'"+g("date")(k,"MMM dd yyyy")+"'"
}else{j.filter=j.selectedFilterType+"'"+g("date")(j.filterValFirst,"MMM dd yyyy")+"'"
}}}else{j.filter=j.selectedFilterType+j.filterValFirst
}}}j.availableFilterTypes=undefined;
j.availableFilterValues=undefined;
j.availableFilterValueSelect=undefined;
j.filterValFirst=undefined;
j.selectedFilterType=undefined;
j.filterValSecond=undefined;
j.id=undefined;
j.order=undefined;
j.joinAlias=undefined
});
d.reportWithFilter=angular.copy(d.report.selectedReportJSON);
d.reportWithFilter.columns=angular.copy(d.columnsCopy);
d.reportWithFilter.$promise=undefined;
d.reportWithFilter.$resolved=undefined;
console.log(JSON.stringify(d.reportWithFilter));
e.configureReport(d.reportWithFilter,function(h){d.convertedQuery=h.convertedQuery;
d.retrieveParams={query:d.convertedQuery,count:d.previewTableParams.count(),page:d.previewTableParams.page(),saveQuery:true};
d.previewTableParams.page(1);
d.previewTableParams.reload()
},function(){})
};
d.downloadExcel=function(){b.maskLoading();
e.generateExcel(function(h){window.location.href=b.appendAuthToken(b.apipath+"report/downloadexcel");
b.unMaskLoading()
},function(){b.unMaskLoading()
})
};
d.downloadPdf=function(){b.maskLoading();
console.log("selectedReport---"+JSON.stringify(d.report.selectedReport));
e.generatePdf(d.report.selectedReport,function(h){window.location.href=b.appendAuthToken(b.apipath+"report/downloadpdf");
b.unMaskLoading()
},function(){b.unMaskLoading()
})
};
b.unMaskLoading()
}])
});