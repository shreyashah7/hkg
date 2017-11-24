define(["angular"],function(){angular.module("hkg.directives").factory("optionParser",["$parse",function(b){var a=/^\s*(.*?)(?:\s+as\s+(.*?))?\s+for\s+(?:([\$\w][\$\w\d]*))\s+in\s+(.*)$/;
return{parse:function(e){var f=e.match(a),c,d,g;
if(!f){throw new Error("Expected typeahead specification in form of '_modelValue_ (as _label_)? for _item_ in _collection_' but got '"+e+"'.")
}return{itemName:f[3],source:b(f[4]),viewMapper:b(f[2]||f[1]),modelMapper:b(f[1])}
}}
}]).directive("agMultiselect",["$parse","$document","$compile","optionParser",function(b,d,a,c){return{restrict:"E",require:"ngModel",link:function(n,g,s,e){var o=s.options,t=c.parse(o),h=s.multiple?true:false,r=s.filter?true:false,q=false,f=n.$new(),v=s.change||anguler.noop;
f.items=[];
if(s.header!==undefined){f.header=s.header
}else{f.header="Select"
}f.multiple=h;
f.disabled=false;
f.isFilter=r;
n.$on("$destroy",function(){f.$destroy()
});
var l=angular.element("<multiselect-popup></multiselect-popup>");
if(s.required||s.ngRequired){q=true
}s.$observe("required",function(y){q=y
});
f.$watch(function(){return b(s.disabled)(n)
},function(y){f.disabled=y
});
f.$watch(function(){return b(s.multiple)(n)
},function(y){h=y||false
});
f.$watch(function(){return t.source(n)
},function(y){if(angular.isDefined(y)){k()
}},true);
f.$watch(function(){return e.$modelValue
},function(z,y){if(angular.isDefined(z)){u(z);
f.$eval(v)
}m();
e.$setValidity("required",f.valid())
},true);
function k(){f.items.length=0;
var y=t.source(n);
if(!angular.isDefined(y)){return
}for(var A=0;
A<y.length;
A++){var z={};
z[t.itemName]=y[A];
f.items.push({label:t.viewMapper(z),model:y[A],checked:false})
}}k();
g.append(a(l)(f));
function m(){if(x(e.$modelValue)){if(s.header!==undefined){f.header=s.header
}else{f.header="Select"
}return f.header
}if(h){f.header=e.$modelValue.length+" selected"
}else{var y={};
y[t.itemName]=e.$modelValue;
f.header=t.viewMapper(y)
}}function x(y){if(!y){return true
}if(y.length&&y.length>0){return false
}for(var z in y){if(y[z]){return false
}}return true
}f.valid=function j(){if(!q){return true
}var y=e.$modelValue;
return(angular.isArray(y)&&y.length>0)||(!angular.isArray(y)&&y!=null)
};
function i(y){if(y.checked){f.uncheckAll()
}else{f.uncheckAll();
y.checked=!y.checked
}p(false)
}function w(y){y.checked=!y.checked;
p(true)
}function p(z){var y;
if(z){y=[];
angular.forEach(f.items,function(A){if(A.checked){y.push(A.model)
}})
}else{angular.forEach(f.items,function(A){if(A.checked){y=A.model;
return false
}})
}e.$setViewValue(y)
}function u(y){if(!angular.isArray(y)){angular.forEach(f.items,function(z){if(angular.equals(z.model,y)){z.checked=true;
return false
}})
}else{angular.forEach(y,function(z){angular.forEach(f.items,function(A){if(angular.equals(A.model,z)){A.checked=true
}})
})
}}f.checkAll=function(){if(!h){return
}angular.forEach(f.items,function(y){y.checked=true
});
p(true)
};
f.uncheckAll=function(){angular.forEach(f.items,function(y){y.checked=false
});
p(true)
};
f.select=function(y){if(h===false){i(y);
f.toggleSelect()
}else{w(y)
}}
}}
}]).directive("multiselectPopup",["$document",function(a){return{restrict:"E",scope:false,replace:true,templateUrl:"scripts/directives/multiselect/multiselect.tmpl.html",link:function(e,d,c){e.isVisible=false;
e.toggleSelect=function(){if(d.hasClass("open")){d.removeClass("open");
a.unbind("click",g)
}else{d.addClass("open");
a.bind("click",g);
e.focus()
}};
function g(h){if(f(h.target,d.find(h.target.tagName))){return
}d.removeClass("open");
a.unbind("click",g);
e.$apply()
}e.focus=function b(){var h=d.find("input")[0];
h.focus()
};
var f=function(k,h){for(var j=0;
j<h.length;
j++){if(k==h[j]){return true
}}return false
}
}}
}])
});