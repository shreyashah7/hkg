angular.module("dndLists",[]).directive("dndDraggable",["$parse","$timeout","dndDropEffectWorkaround","dndDragTypeWorkaround",function(d,b,c,a){return function(g,f,e){f.attr("draggable","true");
if(e.dndDisableIf){g.$watch(e.dndDisableIf,function(h){f.attr("draggable",!h)
})
}f.on("dragstart",function(h){h=h.originalEvent||h;
h.dataTransfer.setData("Text",angular.toJson(g.$eval(e.dndDraggable)));
h.dataTransfer.effectAllowed=e.dndEffectAllowed||"move";
f.addClass("dndDragging");
b(function(){f.addClass("dndDraggingSource")
},0);
c.dropEffect="none";
a.isDragging=true;
a.dragType=e.dndType?g.$eval(e.dndType):undefined;
d(e.dndDragstart)(g,{event:h});
h.stopPropagation()
});
f.on("dragend",function(h){h=h.originalEvent||h;
var i=c.dropEffect;
g.$apply(function(){switch(i){case"move":d(e.dndMoved)(g,{event:h});
break;
case"copy":d(e.dndCopied)(g,{event:h});
break
}});
f.removeClass("dndDragging");
f.removeClass("dndDraggingSource");
a.isDragging=false;
h.stopPropagation()
});
f.on("click",function(h){h=h.originalEvent||h;
g.$apply(function(){if(e.dndSelected=="models.selected = item"){$("#renameElements").modal("show")
}d(e.dndSelected)(g,{event:h})
});
h.stopPropagation()
});
f.on("selectstart",function(){if(this.dragDrop){this.dragDrop()
}return false
})
}
}]).directive("dndList",["$parse","$timeout","dndDropEffectWorkaround","dndDragTypeWorkaround",function(d,b,c,a){return function(t,i,j){var p=angular.element("<li class='dndPlaceholder'></li>");
var g=p[0];
var h=i[0];
var e=j.dndHorizontalList&&t.$eval(j.dndHorizontalList);
var r=j.dndExternalSources&&t.$eval(j.dndExternalSources);
i.on("dragover",function(u){u=u.originalEvent||u;
if(!m(u)){return true
}if(g.parentNode!==h){i.append(p)
}if(u.target!==h){var v=u.target;
while(v.parentNode!==h&&v.parentNode){v=v.parentNode
}if(v.parentNode===h&&v!==g){if(o(u,v)){h.insertBefore(g,v)
}else{h.insertBefore(g,v.nextSibling)
}}}else{if(o(u,g,true)){while(g.previousElementSibling&&(o(u,g.previousElementSibling,true)||g.previousElementSibling.offsetHeight===0)){h.insertBefore(g,g.previousElementSibling)
}}else{while(g.nextElementSibling&&!o(u,g.nextElementSibling,true)){h.insertBefore(g,g.nextElementSibling.nextElementSibling)
}}}if(j.dndDragover&&!f(j.dndDragover,u)){return k()
}i.addClass("dndDragover");
u.preventDefault();
u.stopPropagation();
return false
});
i.on("drop",function(v){v=v.originalEvent||v;
if(!m(v)){return true
}v.preventDefault();
var w=v.dataTransfer.getData("Text")||v.dataTransfer.getData("text/plain");
var z;
try{z=JSON.parse(w)
}catch(x){return k()
}if(j.dndDrop){z=f(j.dndDrop,v,z);
if(!z){return k()
}}var y=t.$eval(j.dndList);
if(z!==undefined){angular.forEach(y,function(B,A){if(B.colField==="column"){y.splice(A,1)
}});
if(v.dataTransfer.effectAllowed==="copy"){var u=0;
if(z.type!=="container"){angular.forEach(y,function(A){if(A.colField===z.colField&&A.colField!=="column"){u++;
v.preventDefault()
}});
if(u>0){return k()
}}}}t.$apply(function(){if(typeof z==="object"&&z.length!==undefined){angular.forEach(z,function(A){y.splice(q(),0,A)
})
}else{y.splice(q(),0,z)
}});
if(j.dndCallback){l(j.dndCallback,v,z)
}if(v.dataTransfer.dropEffect==="none"){if(v.dataTransfer.effectAllowed==="copy"||v.dataTransfer.effectAllowed==="move"){c.dropEffect=v.dataTransfer.effectAllowed
}else{c.dropEffect=v.ctrlKey?"copy":"move"
}}else{c.dropEffect=v.dataTransfer.dropEffect
}k();
v.stopPropagation();
return false
});
i.on("dragleave",function(u){u=u.originalEvent||u;
i.removeClass("dndDragover");
b(function(){if(!i.hasClass("dndDragover")){p.remove()
}},100)
});
function s(w){var v=w.target.offsetParent,u=0,z=0;
while(v&&!isNaN(v.offsetLeft)&&!isNaN(v.offsetTop)){u+=v.offsetLeft-v.scrollLeft;
z+=v.offsetTop-v.scrollTop;
v=v.offsetParent
}u=w.clientX-u;
z=w.clientY-z;
return{x:u,y:z}
}function o(y,z,u){var v=e?(s(y).x):(s(y).y);
var x=e?z.offsetWidth:z.offsetHeight;
var w=e?z.offsetLeft:z.offsetTop;
w=u?w:0;
return v<w+x/2
}function q(){return Array.prototype.indexOf.call(h.children,g)
}function m(u){if(!a.isDragging&&!r){return false
}if(!n(u.dataTransfer.types)){return false
}if(j.dndAllowedTypes&&a.isDragging){var v=t.$eval(j.dndAllowedTypes);
if(angular.isArray(v)&&v.indexOf(a.dragType)===-1){return false
}}if(j.dndDisableIf&&t.$eval(j.dndDisableIf)){return false
}return true
}function k(){p.remove();
i.removeClass("dndDragover");
return true
}function f(w,v,u){return d(w)(t,{event:v,index:q(),item:u||undefined,external:!a.isDragging,type:a.isDragging?a.dragType:undefined})
}function l(w,v,u){return d(w)(t,{event:v,index:q(),item:u||undefined,external:!a.isDragging,type:a.isDragging?a.dragType:undefined})
}function n(v){if(!v){return true
}for(var u=0;
u<v.length;
u++){if(v[u]==="Text"||v[u]==="text/plain"){return true
}}return false
}}
}]).factory("dndDragTypeWorkaround",function(){return{}
}).factory("dndDropEffectWorkaround",function(){return{}
});