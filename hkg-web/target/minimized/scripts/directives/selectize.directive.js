define(["angular"],function(){globalProvider.compileProvider.directive("agSelectize",["$filter",function(a){return{restrict:"A",require:"?ngModel",link:function(i,g,e,b){var d;
i.selectize=i.selectizeOptions;
i.selectize.options=i.options;
i.selectize.items=i.items;
var h=b;
var c=g.selectize(i.selectize);
d=c[0].selectize;
i.$watch("options",function(j,k){if(j.length<k.length){angular.forEach(k,function(m,l){if(JSON.stringify(j).indexOf(JSON.stringify(m))<0){}})
}else{}},true);
i.$watch("i",function(){var j=[];
if(i.items!==null&&i.items!==undefined&&i.items.length>0){j=i.items.split("|");
angular.forEach(j,function(k){var l=a("filter")(i.options,function(m){return m.id.toString()===k.toString()
})[0];
d.addOption(l);
d.addItem(l.id)
})
}});
i.$watch("optgroups",function(j,k){if(j!==undefined){if(j.length<k.length){angular.forEach(k,function(m,l){if(JSON.stringify(j).indexOf(JSON.stringify(m))<0){angular.forEach(i.selectize.options,function(o,n){if(o[i.selectize.optgroupField]===m.id){d.removeOption(o.id)
}})
}})
}else{angular.forEach(j,function(l,m){d.addOptionGroup(l.id,l)
})
}}},true);
var f=e.ngModel
}}
}])
});