/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//
define(['angular'], function () {
    angular.module('hkg.directives').directive("agMenu", ['$compile','$rootScope', function (compile,$rootScope) {

            return {
                restrict: 'AE',
                replace: true,
                scope: {
                    'menu': '=',
                    'class': '@',
                    'parentName': '='
                },
                template: '<ul class="$class"><li ng-repeat="item in menu" ng-if="item.menuCategory == parentName">' +
                        '<a ng-controller="MenuController" ng-click="retrieveReportByFeature(item)" id="{{item.featureName}}" href="#{{item.featureURL}}" ng-if="item.menuLabel && item.menuCategory !== \'stock\' || (item.menuCategory === \'stock\' && $root.allocationMap[item.menuLabel] === undefined)"><span class="glyphicon glyphicon-edit">&nbsp;</span>{{"Menu."+item.menuLabel | translate}}</a>'+
                        '<a ng-controller="MenuController" ng-click="retrieveReportByFeature(item)" id="{{item.featureName}}" href="#{{item.featureURL}}" ng-if="item.menuLabel && item.menuCategory === \'stock\' && $root.allocationMap[item.menuLabel] !== undefined"><span class="glyphicon glyphicon-edit">&nbsp;{{"Menu."+item.menuLabel | translate}}</span> <span class="menu-count">({{$root.allocationMap[item.menuLabel]}})<span></a>' +
                        '</li></ul>',
                compile: function (el) {
                    var contents = el.contents().remove();
                    var compiled;
                    return function (scope, el) {
                        if (!compiled)
                            compiled = compile(contents);

                        compiled(scope, function (clone) {
                            el.append(clone);
                        });
                    };
                }

            };

        }])
            .controller('MenuController', ["$rootScope", "$scope","$location",
                "ReportMaster", function ($rootScope, $scope,$location,
                        ReportMaster) {
                    $scope.retrieveReportByFeature = function (feature) {
                        localStorage.setItem('featureName',feature.featureName);
                        document.body.getElementsByClassName('menu-trigger')[0].click();
                        if (feature.menuType === "RMI") {
                            ReportMaster.retrieveReportByFeature(feature.id, function (res) {
                                if(res.data !== undefined){
                                   localStorage.setItem('menuReportId', res.data.id);
                                   $rootScope.childMenu = feature.featureName;
                                   $location.path('/viewreports');
                                }
                                
                            }, function () {
                            });
                        }
                    };
                }])
            .factory('ReportMaster', ['$resource', '$rootScope', function (resource, rootScope) {
                    var ReportBuilder = resource(rootScope.apipath + 'report/:action', {}, {
                        retrieveReportByFeature: {
                            method: 'POST',
                            params: {
                                action: 'retrievereportbyfeature'
                            }
                        }
                    });
                    return ReportBuilder;
                }]);

});




