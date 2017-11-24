/* ngTable v0.3.0 by Vitalii Savchuk(esvit666@gmail.com) - https://github.com/esvit/ng-table - New BSD License */
!function(d,c){return"function"==typeof define&&define.amd?(define("ngTable",["jquery","angular"],function(b,e){return c(e)
}),void 0):c(d)
}(angular||null,function(e){e=angular;
var d=e.module("ngTable",[]);
d.factory("ngTableParams",["$q",function(a){var h=function(b){return !isNaN(parseFloat(b))&&isFinite(b)
},g=function(l,k){var j=this;
this.parameters=function(w,v){if(v=v||!1,e.isDefined(w)){for(var u in w){var t=w[u];
if(v&&u.indexOf("[")>=0){for(var s=u.split(/\[(.*)\]/).reverse(),r="",q=0,p=s.length;
p>q;
q++){var o=s[q];
if(""!==o){var n=t;
t={},t[r=o]=h(n)?parseFloat(n):n
}}"sorting"===r&&(c[r]={}),c[r]=e.extend(c[r]||{},t[r])
}else{c[u]=h(w[u])?parseFloat(w[u]):w[u]
}}return this
}return c
},this.settings=function(m){return e.isDefined(m)?(b=e.extend(b,m),this):b
},this.page=function(m){return e.isDefined(m)?this.parameters({page:m}):c.page
},this.total=function(m){var n=e.isDefined(m)?this.settings({total:m}):b.total;
return e.isFunction(n)?n():n
},this.count=function(m){return e.isDefined(m)?this.parameters({count:m,page:1}):c.count
},this.filter=function(m){return e.isDefined(m)?this.parameters({filter:m}):c.filter
},this.sorting=function(m){return e.isDefined(m)?this.parameters({sorting:m}):c.sorting
},this.orderBy=function(){var n=[];
for(var m in c.sorting){n.push(("asc"===c.sorting[m]?"+":"-")+m)
}return n
},this.getData=function(m){m.resolve([])
},this.getGroups=function(o,n){var m=a.defer();
m.promise.then(function(p){var v={};
for(var u in p){var t=p[u],s=e.isFunction(n)?n(t):t[n];
v[s]=v[s]||{data:[]},v[s].value=s,v[s].data.push(t)
}var r=[];
for(var q in v){r.push(v[q])
}o.resolve(r)
}),this.getData(m,j)
},this.generatePagesArray=function(u,t,s){var r,q,p,o,n,m;
if(r=11,m=[],n=Math.ceil(t/s),n>1){for(m.push({type:"prev",number:Math.max(1,u-1),active:u>1}),m.push({type:"first",number:1,active:u>1}),p=Math.round((r-5)/2),o=Math.max(2,u-p),q=Math.min(n-1,u+2*p-(u-o)),o=Math.max(2,o-(2*p-(q-o))),i=o;
q>=i;
){i===o&&2!==i||i===q&&i!==n-1?m.push({type:"more",active:!1}):m.push({type:"page",number:i,active:u!==i}),i++
}m.push({type:"last",number:n,active:u!==n}),m.push({type:"next",number:Math.min(n,u+1),active:n>u})
}return m
},this.url=function(m){m=m||!1;
var r=m?[]:{};
for(key in c){if(c.hasOwnProperty(key)){var q=c[key],p=encodeURIComponent(key);
if("object"==typeof q){for(var o in q){if(!e.isUndefined(q[o])&&""!==q[o]){var n=p+"["+encodeURIComponent(o)+"]";
m?r.push(n+"="+encodeURIComponent(q[o])):r[n]=encodeURIComponent(q[o])
}}}else{e.isFunction(q)||e.isUndefined(q)||""===q||(m?r.push(p+"="+encodeURIComponent(q)):r[p]=encodeURIComponent(q))
}}}return r
},this.reload=function(){var m=a.defer(),n=this;
b.$loading=!0,b.groupBy?b.getGroups(m,b.groupBy,this):b.getData(m,this),m.promise.then(function(o){b.$loading=!1,b.groupBy?b.$scope.$groups=o:b.$scope.$data=o,b.$scope.pages=n.generatePagesArray(n.page(),n.total(),n.count())
})
};
var c=this.$params={page:1,count:1,filter:{},sorting:{},group:{},groupBy:null},b={$scope:null,$loading:!1,total:0,counts:[10,25,50,100],getGroups:this.getGroups,getData:this.getData};
return this.settings(k),this.parameters(l,!0),this
};
return g
}]);
var f=["$scope","ngTableParams","$q",function(g,c){g.$loading=!1,g.params||(g.params=new c),g.params.settings().$scope=g,g.$watch("params.$params",function(){g.params.settings().$scope=g,g.params.reload()
},!0),g.sortBy=function(a){var j,h;
a.sortable&&(j=g.params.sorting()&&g.params.sorting()[a.sortable]&&"desc"===g.params.sorting()[a.sortable],h={},h[a.sortable]=j?"asc":"desc",g.params.parameters({sorting:h}))
}
}];
return d.directive("ngTable",["$compile","$q","$parse",function(a,g,c){return{restrict:"A",priority:1001,scope:!0,controller:f,compile:function(j){var h=[],b=0;
return e.forEach(e.element(j[0].querySelector("tr:not(.ng-table-group)")).find("td"),function(k){var p=e.element(k);
if(!p.attr("ignore-cell")||"true"!==p.attr("ignore-cell")){var o=function(q){return c(p.attr("x-data-title")||p.attr("data-title")||p.attr("title"))(q,{$columns:h})||" "
};
p.attr("data-title-text",o());
var n=function(q){return c(p.attr("x-data-header")||p.attr("data-header")||p.attr("header"))(q,{$columns:h})||!1
},m=p.attr("filter")?c(p.attr("filter"))():!1,l=!1;
m&&m.templateURL&&(l=m.templateURL,delete m.templateURL),h.push({id:b++,title:o,sortable:p.attr("sortable")?p.attr("sortable"):!1,filter:m,filterTemplateURL:l,headerTemplateURL:n,filterData:p.attr("filter-data")?p.attr("filter-data"):null,show:p.attr("ng-show")?function(q){return c(p.attr("ng-show"))(q)
}:function(){return !0
}})
}}),function(o,n,m){if(o.$loading=!1,o.$columns=h,o.$watch(m.ngTable,function(p){e.isUndefined(p)||(o.paramsModel=c(m.ngTable),o.params=p)
},!0),o.parse=function(p){return p(o)
},m.showFilter&&o.$parent.$watch(m.showFilter,function(p){o.show_filter=p
}),e.forEach(h,function(p){var q;
if(p.filterData){if(q=c(p.filterData)(o,{$column:p}),!e.isObject(q)||!e.isObject(q.promise)){throw new Error("Function "+p.filterData+" must be instance of $q.defer()")
}return delete p.filterData,q.promise.then(function(r){e.isArray(r)||(r=[]),r.unshift({title:"-",id:""}),p.data=r
})
}}),!n.hasClass("ng-table")){o.templates={header:m.templateHeader?m.templateHeader:"ng-table/header.html",pagination:m.templatePagination?m.templatePagination:"ng-table/pager.html"};
var l=e.element(document.createElement("thead")).attr("ng-include","templates.header"),k=e.element(document.createElement("div")).attr("ng-include","templates.pagination");
return n.find("thead").remove(),n.find("tbody"),n.prepend(l),a(l)(o),a(k)(o),n.addClass("ng-table"),n.after(k)
}}
}}
}]),angular.module("ngTable").run(["$templateCache",function(b){b.put("ng-table/filters/select.html",'<select ng-options="data.id as data.title for data in column.data" ng-model="params.filter()[name]" ng-show="filter==\'select\'" class="filter filter-select form-control"> </select>'),b.put("ng-table/filters/text.html",'<input type="text" ng-model="params.filter()[name]" ng-if="filter==\'text\'" class="input-filter form-control"/>'),b.put("ng-table/header.html",'<tr> <th ng-repeat="column in $columns" ng-class="{ \'sortable\': column.sortable, \'sort-asc\': params.sorting()[column.sortable]==\'asc\', \'sort-desc\': params.sorting()[column.sortable]==\'desc\', column.class: true }" ng-click="sortBy(column)" ng-show="column.show(this)" ng-init="template=column.headerTemplateURL(this)" class="header"> <div ng-if="!template" ng-bind="parse(column.title)"></div> <div ng-if="template"><div ng-include="template"></div></div> </th> </tr> <tr ng-show="show_filter" class="ng-table-filters"> <th ng-repeat="column in $columns" ng-show="column.show(this)" data-title-text="{{column.title}}" class="filter"> <form ng-submit="doFilter()"> <input type="submit" tabindex="-1" style="position: absolute; left: -9999px; width: 1px; height: 1px;"/> <div ng-repeat="(name, filter) in column.filter"> <div ng-if="column.filterTemplateURL"> <div ng-include="column.filterTemplateURL"></div> </div> <div ng-if="!column.filterTemplateURL"> <div ng-include="\'ng-table/filters/\' + filter + \'.html\'"></div> </div> </div> </form> </th> </tr>'),b.put("ng-table/pager.html",'<div class="ng-cloak"> <div ng-if="params.settings().counts.length" class="btn-group pull-right"> <button ng-repeat="count in params.settings().counts" type="button" ng-class="{\'active\':params.count()==count}" ng-click="params.count(count)" class="btn btn-default btn-xs"> {{count}} </button> </div> <ul class="pagination"> <li ng-class="{\'disabled\': !page.active}" ng-repeat="page in pages" ng-switch="page.type"> <a ng-switch-when="prev" ng-click="params.page(page.number)" href="">&laquo;</a> <a ng-switch-when="first" ng-click="params.page(page.number)" href="">{{page.number}}</a> <a ng-switch-when="page" ng-click="params.page(page.number)" href="">{{page.number}}</a> <a ng-switch-when="more" ng-click="params.page(page.number)" href="">&#8230;</a> <a ng-switch-when="last" ng-click="params.page(page.number)" href="">{{page.number}}</a> <a ng-switch-when="next" ng-click="params.page(page.number)" href="">&raquo;</a> </li> </ul> </div>')
}]),d
});