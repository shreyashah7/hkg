define(["angular"],function(){globalProvider.compileProvider.directive("agSelectizeconstraints",["$filter",function(a){return{restrict:"A",require:"?ngModel",link:function(g,f,e,b){var d;
g.selectize=g.selectizeOptionsForConstraints;
g.selectize.options=g.options;
g.selectize.items=g.items;
var c=f.selectize(g.selectize);
d=c[0].selectize;
g.$watch("options",function(h,i){if(h.length<i.length){angular.forEach(i,function(k,j){if(JSON.stringify(h).indexOf(JSON.stringify(k))<0){}})
}else{angular.forEach(h,function(j,k){d.addOption(j)
})
}},true);
g.$watch("constraintIncrement",function(){var h=[];
if(g.items!==null&&g.items!==undefined&&g.items.length>0){h=g.items.split("|");
angular.forEach(h,function(i){var j=a("filter")(g.options,function(k){return k.id.toString()===i.toString()
})[0];
d.addOption(j);
d.addItem(j.id)
})
}});
g.$watch("optgroups",function(h,i){if(h!==undefined){if(h.length<i.length){angular.forEach(i,function(k,j){if(JSON.stringify(h).indexOf(JSON.stringify(k))<0){angular.forEach(g.selectize.options,function(m,l){if(m[g.selectize.optgroupField]===k.id){d.removeOption(m.id)
}})
}})
}else{angular.forEach(h,function(j,k){d.addOptionGroup(j.id,j)
})
}}},true)
}}
}])
});