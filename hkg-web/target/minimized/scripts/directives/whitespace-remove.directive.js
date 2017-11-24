define(["angular"],function(){globalProvider.compileProvider.directive("whitespace",["$parse","$compile",function(b,a){return{require:["^form","ngModel"],link:function(g,e,d,c){var f=c[1];
e.bind("keydown keypress",function(h){if(h.which===32){h.preventDefault()
}})
}}
}])
});