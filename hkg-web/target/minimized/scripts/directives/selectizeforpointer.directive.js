define(["angular"],function(){globalProvider.compileProvider.directive("agSelectizepointer",["$filter",function(a){return{restrict:"A",require:"?ngModel",link:function(h,f,e,b){var d;
h.selectize=h.selectizeOptionsForPointer;
h.selectize.options=h.options;
h.selectize.items=h.items;
var g=b;
var c=f.selectize(h.selectize);
d=c[0].selectize;
h.$watch("options",function(i,j){if(i.length<j.length){angular.forEach(j,function(l,k){if(JSON.stringify(i).indexOf(JSON.stringify(l))<0){}})
}else{}},true);
h.$watch("pointerIdIncrement",function(){console.log("pomiter d");
var i=[];
if(h.items!==null&&h.items!==undefined&&h.items.length>0){i=h.items.split("|");
angular.forEach(i,function(j){var k=a("filter")(h.options,function(l){return l.id.toString()===j.toString()
})[0];
d.addOption(k);
d.addItem(k.id)
})
}});
h.$watch("optgroups",function(i,j){if(i!==undefined){if(i.length<j.length){angular.forEach(j,function(l,k){if(JSON.stringify(i).indexOf(JSON.stringify(l))<0){angular.forEach(h.selectize.options,function(n,m){if(n[h.selectize.optgroupField]===l.id){d.removeOption(n.id)
}})
}})
}else{angular.forEach(i,function(k,l){d.addOptionGroup(k.id,k)
})
}}},true)
}}
}])
});