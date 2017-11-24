/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


define(['angular'], function() {
    angular.module('hkg.directives')
            .directive('tabs', function() {
                return {
                    restrict: 'A',
                    transclude: true,
                    scope: {},
                    controller: ["$scope", "$element", function($scope, $element) {
                        var panes = $scope.panes = [];

                        $scope.select = function(pane) {
                            angular.forEach(panes, function(pane) {
                                pane.selected = false;
                            });
                            pane.selected = true;
                        };
                        this.addPane = function(pane) {
                            if (panes.length === 0)
                                $scope.select(pane);
                            panes.push(pane);
                        };
                    }],
                    template:
                            '<div class="tabbable">' +
                            '<ul class="nav nav-tabs">' +
                            '<li ng-repeat="pane in panes" ng-class="{active:pane.$parent.tabInfo.selected}">' +
                            '<a href="" ng-click="pane.$parent.tabManager.select($index)">{{pane.title}}</a>' +
                            '</li>' +
                            '</ul>' +
                            '<div class="tab-content" ng-transclude></div>' +
                            '</div>',
                    replace: true
                };
            });

    angular.module('hkg.directives').directive('pane', function() {
        return {
            require: '^tabs',
            restrict: 'A',
            transclude: true,
            scope: {title: '@'},
            link: function(scope, element, attrs, tabsCtrl) {
                tabsCtrl.addPane(scope);
            },
            template:
                    '<div class="tab-pane" ng-class="{active: $parent.tabInfo.selected}" ng-transclude>' +
                    '</div>',
            replace: true
        };
    });
});