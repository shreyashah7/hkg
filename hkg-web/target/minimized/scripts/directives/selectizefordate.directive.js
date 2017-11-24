define(["angular"],function(){globalProvider.compileProvider.directive("agSelectizedate",["$filter",function(a){return{restrict:"A",require:"?ngModel",link:function(h,f,e,b){var d;
h.selectize=h.selectizeOptionsForDateField;
console.log("date "+h.dateoptions+"itemssss"+h.dateitems);
h.selectize.options=h.dateoptions;
h.selectize.items=h.dateitems;
var g=b;
var c=f.selectize(h.selectize);
d=c[0].selectize;
h.$watch("options",function(i,j){if(i.length<j.length){angular.forEach(j,function(l,k){if(JSON.stringify(i).indexOf(JSON.stringify(l))<0){}})
}else{}},true);
h.$watch("pointerIdIncrement",function(){console.log("pomiter d"+h.dateitems);
var i=[];
if(h.dateitems!==null&&h.dateitems!==undefined&&h.dateitems.length>0){i=h.dateitems.split("|");
console.log("itemsss."+i);
angular.forEach(i,function(j){console.log("optiosss"+h.dateoptions);
var k=a("filter")(h.dateoptions,function(l){console.log("tag id.."+l.id+"---"+j);
return l.id.toString()===j.toString()
})[0];
console.log("option..."+k);
if(k!==null&&k!==undefined){d.addOption(k);
d.addItem(k.id)
}})
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