define(["angular"],function(){angular.module("hkg.directives").directive("uiSelect2",["$timeout",function(b){var a={};
return{require:["^form","?ngModel"],compile:function(i,d){var h,c,f,g=i.is("select"),e=(d.multiple!==undefined);
if(i.is("select")){c=i.find("option[ng-repeat], option[data-ng-repeat]");
if(c.length){f=c.attr("ng-repeat")||c.attr("data-ng-repeat");
h=jQuery.trim(f.split("|")[0]).split(" ").pop()
}}return function(o,q,m,j){var l=j[1];
var p=angular.extend({},a,o.$eval(m.uiSelect2));
if(g){delete p.multiple;
delete p.initSelection
}else{if(e){p.multiple=true
}}if(l){l.$render=function(){if(g){q.select2("val",l.$modelValue)
}else{if(e){if(!l.$modelValue){q.select2("data",[])
}else{if(angular.isArray(l.$modelValue)){q.select2("data",l.$modelValue)
}else{q.select2("val",l.$modelValue)
}}}else{if(angular.isObject(l.$modelValue)){q.select2("data",l.$modelValue)
}else{q.select2("val",l.$modelValue)
}}}};
if(h){o.$watch(h,function(s,r,t){if(!s){return
}b(function(){q.select2("val",l.$viewValue);
q.trigger("change");
l.$setPristine();
j[0].$setPristine()
})
})
}if(!g){q.bind("change",function(){o.$apply(function(){l.$setViewValue(q.select2("data"))
})
});
if(p.initSelection){var k=p.initSelection;
p.initSelection=function(r,s){k(r,function(t){l.$setViewValue(t);
s(t)
})
}
}}}m.$observe("disabled",function(r){q.select2(r&&"disable"||"enable")
});
if(m.ngMultiple){o.$watch(m.ngMultiple,function(r){q.select2(p)
})
}q.val(o.$eval(m.ngModel));
b(function(){q.select2(p).select2("val",q.select2("val"));
if(!p.initSelection&&!g){l.$setViewValue(q.select2("data"))
}l.$setPristine();
j[0].$setPristine()
});
var n=m.ngModel;
o.$watch(n,function(r){if(r instanceof Object===false&&r===""){$(q).select2("val",r)
}if(o[m.uiSelect2]){if(o[m.uiSelect2]["multiple"]&&angular.isArray(r)){$(q).select2("data",r)
}else{if(!o[m.uiSelect2]["multiple"]&&r!==undefined){if(r instanceof Object===true){$(q).select2("data",r)
}else{if(m.selectById){$(q).select2("val",r)
}}}}}else{if(r!==undefined&&r instanceof Object===true){$(q).select2("data",r)
}}});
if(angular.isDefined(m.dynamicOptions)){o.$watch(m.uiSelect2,function(r){if(angular.isDefined(r)){p=angular.extend({},a,o.$eval(m.uiSelect2));
$(q).select2(p)
}},true)
}}
}}
}])
});