angular.module("ui.sortable",[]).value("uiSortableConfig",{}).directive("uiSortable",["uiSortableConfig","$timeout","$log",function(b,a,c){return{require:"?ngModel",link:function(m,g,l,f){var e;
function k(o,n){if(n&&(typeof n==="function")){return function(q,p){o(q,p);
n(q,p)
}
}return o
}function h(n,p){var o=n.sortable("option","helper");
return o==="clone"||(typeof o==="function"&&p.item.sortable.isCustomHelperUsed())
}var d={};
var i={receive:null,remove:null,start:null,stop:null,update:null};
var j={helper:null};
angular.extend(d,b,m.$eval(l.uiSortable));
if(!angular.element.fn||!angular.element.fn.jquery){c.error("ui.sortable: jQuery should be included before AngularJS!");
return
}if(f){m.$watch(l.ngModel+".length",function(){a(function(){if(!!g.data("ui-sortable")){g.sortable("refresh")
}})
});
i.start=function(o,n){n.item.sortable={index:n.item.index(),cancel:function(){n.item.sortable._isCanceled=true
},isCanceled:function(){return n.item.sortable._isCanceled
},isCustomHelperUsed:function(){return !!n.item.sortable._isCustomHelperUsed
},_isCanceled:false,_isCustomHelperUsed:n.item.sortable._isCustomHelperUsed}
};
i.activate=function(){e=g.contents();
var o=g.sortable("option","placeholder");
if(o&&o.element&&typeof o.element==="function"){var n=o.element();
n=angular.element(n);
var p=g.find('[class="'+n.attr("class")+'"]');
e=e.not(p)
}};
i.update=function(o,n){if(!n.item.sortable.received){n.item.sortable.dropindex=n.item.index();
n.item.sortable.droptarget=n.item.parent();
g.sortable("cancel")
}if(h(g,n)&&!n.item.sortable.received&&g.sortable("option","appendTo")==="parent"){e=e.not(e.last())
}e.appendTo(g);
if(n.item.sortable.received){e=null
}if(n.item.sortable.received&&!n.item.sortable.isCanceled()){m.$apply(function(){f.$modelValue.splice(n.item.sortable.dropindex,0,n.item.sortable.moved)
})
}};
i.stop=function(o,n){if(!n.item.sortable.received&&("dropindex" in n.item.sortable)&&!n.item.sortable.isCanceled()){m.$apply(function(){f.$modelValue.splice(n.item.sortable.dropindex,0,f.$modelValue.splice(n.item.sortable.index,1)[0])
})
}else{if((!("dropindex" in n.item.sortable)||n.item.sortable.isCanceled())&&!h(g,n)){e.appendTo(g)
}}e=null
};
i.receive=function(o,n){n.item.sortable.received=true
};
i.remove=function(o,n){if(!("dropindex" in n.item.sortable)){g.sortable("cancel");
n.item.sortable.cancel()
}if(!n.item.sortable.isCanceled()){m.$apply(function(){n.item.sortable.moved=f.$modelValue.splice(n.item.sortable.index,1)[0]
})
}};
j.helper=function(n){if(n&&typeof n==="function"){return function(q,o){var p=n(q,o);
o.sortable._isCustomHelperUsed=o!==p;
return p
}
}return n
};
m.$watch(l.uiSortable,function(n){if(!!g.data("ui-sortable")){angular.forEach(n,function(p,o){if(i[o]){if(o==="stop"){p=k(p,function(){m.$apply()
})
}p=k(i[o],p)
}else{if(j[o]){p=j[o](p)
}}g.sortable("option",o,p)
})
}},true);
angular.forEach(i,function(o,n){d[n]=k(o,d[n])
})
}else{c.info("ui.sortable: ngModel not provided!",g)
}g.sortable(d)
}}
}]);