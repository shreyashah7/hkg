define(["angular"],function(){angular.module("hkg.directives").directive("agDataTable",["DT_LAST_ROW_KEY","$timeout","$compile",function(d,b,a){var c=angular.element("<h5>Loading...</h5>");
return{restrict:"A",scope:{agDataTable:"=",sourceFunction:"=",dtOptions:"=",dtSearch:"=",dtTotalRecords:"="},link:function(i,f,g,e){i.dtable=undefined;
var k=function(l,m){if(g.dtTotalRecords!==undefined){i.$watch("dtTotalRecords",function(o,p){if(o!==p){if(o!==undefined&&o>0){if(i.dtable===undefined){b(function(){i.dtable=l.dataTable(m)
},100)
}else{var n=l.find($("tbody tr"));
i.dtable.fnClearTable();
i.dtable._fnAddTr(n);
i.dtable.fnDraw()
}}else{if(o===0){if(i.dtable!==undefined){i.dtable.fnClearTable();
i.dtable.fnDestroy()
}i.dtable=undefined
}}}})
}b(function(){l.show();
if(i.dtable===undefined){if(l.closest(".modal").html()!==undefined){b(function(){i.dtable=l.dataTable(m);
l.parent().parent().parent().find("div.DTS_Loading").hide()
},250)
}else{i.dtable=l.dataTable(m);
l.parent().parent().parent().find("div.DTS_Loading").hide()
}}})
};
$.fn.dataTable.ext.order["dom-text-numeric"]=function(m,l){return this.api().column(l,{order:"index"}).nodes().map(function(o,n){return $("input",o).val()*1
})
};
$(window).resize(function(){if(i.dtable!==undefined){b(function(){i.dtable.fnAdjustColumnSizing()
})
}});
if(g.dtOptions!==undefined){i.dataTableOptions=i.dtOptions;
i.dataTableOptions.scrollCollapse=true;
if(angular.isUndefined(i.dtOptions.dom)){i.dataTableOptions.dom="rtiS"
}if(angular.isUndefined(i.dtOptions.scrollY)){i.dataTableOptions.scrollY=370
}if(angular.isUndefined(i.dtOptions.scroller)){i.dataTableOptions.scroller={loadingIndicator:true}
}}else{if(g.sourceFunction!==undefined){i.dataTableOptions={serverSide:true,ordering:true,searching:true,dom:"rtiS",scrollY:370,scrollCollapse:true,scroller:{loadingIndicator:true}}
}else{i.dataTableOptions={dom:"rtiS",scrollY:370,scrollCollapse:true,scroller:{loadingIndicator:true}}
}}var j=document.body,h=j.getElementsByClassName("menu-trigger")[0];
if(typeof h!=="undefined"){h.addEventListener("click",function(){if(i.dtable!==undefined){b(function(){i.dtable.fnAdjustColumnSizing()
},300)
}})
}if(g.sourceFunction!==undefined){i.dataTableFunction=i.sourceFunction;
i.dataTableOptions.ajax=function(m,n,l){i.recordsConfig={draw:m.draw,data:[],recordsTotal:0,recordsFiltered:0};
i.responseComplete=function(o){n(o);
if(i.dataTableOptions.dom!==undefined&&i.dataTableOptions.dom.indexOf("S")>=0){a(f.find($("tbody")))(i);
if(o.data.length===0){f.parent().parent().parent().find("div.DTS_Loading").hide();
f.parent().parent().parent().find(".dataTables_info").hide();
f.parent().parent().parent().find(".dataTables_scroll").css("background","none")
}}else{if(o.recordsTotal>0){$(".dataTables_empty").hide()
}}};
i.data=m;
i.dataTableFunction(i.data,i.recordsConfig,i.responseComplete)
};
if(g.dtSearch!==undefined){i.$watch("dtSearch",function(l){if(l!==undefined&&l!==""){i.dtable.fnFilter(l)
}})
}i.dtable=f.dataTable(i.dataTableOptions)
}if(g.sourceFunction===undefined){i.$watchCollection("agDataTable",function(l){if(l!==undefined&&l!==null&&l.length===0&&i.dtable!==undefined){}else{f.show();
if(i.dtable!==undefined){b(function(){var r=f.find($("tbody tr"));
var q;
var p=i.dtable.fnSettings();
var s=p._iDisplayStart,m=p._iDisplayLength,n=p.fnRecordsDisplay(),o=m===-1;
q=o?0:Math.floor(s/m);
i.dtable.fnClearTable();
i.dtable._fnAddTr(r);
i.dtable.fnDraw();
i.dtable.fnPageChange(q)
},1)
}}if(g.dtSearch!==undefined){i.$watch("dtSearch",function(m){if(i.dtable!==undefined){if(m!==undefined&&m!==""){i.dtable.fnDraw()
}else{if(m===""){i.dtable.fnDraw(true)
}}}})
}})
}i.$on(d,function(){k(f,i.dataTableOptions)
})
}}
}]);
angular.module("hkg.directives").directive("dtRows",["$rootScope","DT_LAST_ROW_KEY",function(a,b){return{restrict:"A",link:function(c){if(c.$last===true){a.$broadcast(b)
}}}
}])
});
(function(a){a.module("hkg.directives").value("DT_LAST_ROW_KEY","datatable:lastRow")
}(angular));