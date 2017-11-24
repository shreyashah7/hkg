var globalProvider = {};
define(['angular'], function() {
    //To initialize hkg.directive module for AMD.
    angular.module('hkg.directives', [])
            .config(["$controllerProvider", "$compileProvider", "$routeProvider", "$filterProvider", "$provide",
                function($controllerProvider, $compileProvider, $routeProvider, $filterProvider, $provide) {
                    globalProvider.controllerProvider = $controllerProvider;
                    globalProvider.compileProvider = $compileProvider;
                    globalProvider.routeProvider = $routeProvider;
                    globalProvider.filterProvider = $filterProvider;
                    globalProvider.provide = $provide;
                }]);
});

/**
 * Methods to rigister various component using above providers.//To module hkg.directives
 * Controllers:
 * globalProvider.controllerProvider.register("mycontroller",["$scope,function($scope){....}]);
 * 
 * Directives:
 * globalProvider.compileProvider.directive("myDirective",["$scope,function($scope){....}]);
 * 
 * Factory:
 * globalProvider.provide.factory("myService",["$scope,function($scope){....}]);
 * Can be used for constant, value and service too.
 * 
 * Filter:
 * globalProvider.filterProvider.register("name");
 */