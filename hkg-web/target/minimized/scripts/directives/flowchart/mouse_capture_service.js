angular.module("mouseCapture",[]).factory("mouseCapture",["$rootScope",function(b){var d=document;
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
});