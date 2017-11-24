define(["angular"],function(){angular.module("hkg.directives").factory("mouseCapture",["$rootScope",function(b){var d=document;
var c=null;
var e=function(f){if(c&&c.mouseMove){c.mouseMove(f);
b.$digest()
}};
var a=function(f){if(c&&c.mouseUp){c.mouseUp(f);
b.$digest()
}};
return{registerElement:function(f){d=f
},acquire:function(f,g){this.release();
c=g;
d.mousemove(e);
d.mouseup(a)
},release:function(){if(c){if(c.released){c.released()
}c=null
}d.unbind("mousemove",e);
d.unbind("mouseup",a)
}}
}]).directive("mouseCapture",function(){return{restrict:"A",controller:["$scope","$element","$attrs","mouseCapture",function(c,b,a,d){d.registerElement(b)
}]}
})
});
angular.module("dragging",["mouseCapture"]).factory("dragging",["$rootScope","mouseCapture",function(b,c){var a=5;
return{startDrag:function(f,h){var j=false;
var e=f.pageX;
var k=f.pageY;
var g=function(l){if(!j){if(l.pageX-e>a||l.pageY-k>a){j=true;
if(h.dragStarted){h.dragStarted(e,k,l)
}if(h.dragging){h.dragging(l.pageX,l.pageY,l)
}}}else{if(h.dragging){h.dragging(l.pageX,l.pageY,l)
}e=l.pageX;
k=l.pageY
}};
var i=function(){if(j){if(h.dragEnded){h.dragEnded()
}}else{if(h.clicked){h.clicked()
}}};
var d=function(l){c.release();
l.stopPropagation();
l.preventDefault()
};
c.acquire(f,{mouseMove:g,mouseUp:d,released:i});
f.stopPropagation();
f.preventDefault()
}}
}]);