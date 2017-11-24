define(["angular"],function(){angular.module("hkg.directives").directive("awDatepickerPattern",["$parse","$compile",function(b,a){console.log("inside directive");
return{restrict:"A",require:"ngModel",link:function(f,g,e,d){console.log("patterns");
var c=new RegExp(e.pattern);
d.$parsers.unshift(function(h){if(typeof h==="string"){var i=c.test(h);
d.$setValidity("date",i);
if(!i){return undefined
}}return h
})
}}
}])
});