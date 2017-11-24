define(["angular","loadmask"],function(){angular.module("hkg.directives").directive("agMask",[function(){function a(d,c,b){d.$watch(b.agMask,function(e){if(e.mask===true){if(e.template){c.mask("","",e.template,"","")
}else{if(e.templateUrl){c.mask("","","",e.templateUrl,"")
}else{if(e.message&&e.imagePath){c.mask(e.message,"","","",e.imagePath)
}else{if(e.imagePath){c.mask("","","","",e.imagePath)
}else{if(e.message){c.mask(e.message,"","","","")
}else{c.mask("","","","","")
}}}}}}else{if(e.mask===false){c.unmask()
}}},true)
}return{restrict:"A",link:a}
}]);
angular.module("hkg.directives").directive("inputMinlength",function(){return{require:"ngModel",link:function(d,f,b,e){var c=0;
var a=function(h){var g=e.$isEmpty(h)||h.length>=c;
e.$setValidity("minlength",g);
return g?h:undefined
};
b.$observe("inputMinlength",function(g){c=parseInt(g,10);
a(e.$viewValue)
});
e.$parsers.push(a)
}}
});
angular.module("hkg.directives").directive("inputMaxlength",function(){return{require:"ngModel",link:function(d,f,b,e){var c=0;
var a=function(h){var g=!e.$isEmpty(h)&&h.length<=c;
e.$setValidity("maxlength",g);
return g?h:undefined
};
b.$observe("inputMaxlength",function(g){c=parseInt(g,10);
a(e.$viewValue)
});
e.$parsers.push(a)
}}
})
});