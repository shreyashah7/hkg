define(["angular"],function(){globalProvider.compileProvider.directive("agChecklistmodal",["$parse","$compile",function(e,c){function b(g,j){if(angular.isArray(g)){for(var h=0;
h<g.length;
h++){if(angular.equals(g[h],j)){return true
}}}return false
}function f(g,j){g=angular.isArray(g)?g:[];
for(var h=0;
h<g.length;
h++){if(angular.equals(g[h],j)){return g
}}g.push(j);
return g
}function a(g,j){if(angular.isArray(g)){for(var h=0;
h<g.length;
h++){if(angular.equals(g[h],j)){g.splice(h,1);
break
}}}return g
}function d(j,k,h){c(k)(j);
var g=e(h.agChecklistmodal);
var m=g.assign;
var i=e(h.checklistChange);
var l=e(h.agChecklistvalue)(j.$parent);
j.$watch("checked",function(p,n){if(p===n){return
}var o=g(j.$parent);
if(p===true){m(j.$parent,f(o,l))
}else{m(j.$parent,a(o,l))
}if(i){i(j)
}});
j.$parent.$watch(h.agChecklistmodal,function(o,n){j.checked=b(o,l)
},true)
}return{restrict:"A",priority:1000,terminal:true,scope:true,compile:function(g,h){if(g[0].tagName!=="INPUT"||!g.attr("type","checkbox")){throw'checklist-model should be applied to `input[type="checkbox"]`.'
}if(!h.agChecklistvalue){throw"You should provide `ag-checklistvalue`."
}g.removeAttr("ag-checklistmodal");
g.attr("ng-model","checked");
return d
}}
}])
});