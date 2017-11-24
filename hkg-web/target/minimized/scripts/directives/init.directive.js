var globalProvider={};
define(["angular"],function(){angular.module("hkg.directives",[]).config(["$controllerProvider","$compileProvider","$routeProvider","$filterProvider","$provide",function(d,a,b,e,c){globalProvider.controllerProvider=d;
globalProvider.compileProvider=a;
globalProvider.routeProvider=b;
globalProvider.filterProvider=e;
globalProvider.provide=c
}])
});