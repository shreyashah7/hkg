/**
 * 
 * Author: Gautam
 * 
 * Objective : Add style of accordion(Collapsible Division) to parent details in diamond pages.
 * 
 * Implementation:
 * 
 * <div id="invoiceParent" title="Invoice Details" accordion-collapse>...contents...</div>
 * 
 * id: (Compulsory) need a unique id in case more elements present.
 * title: (Compulsory) Title need to be shown in the header element.
 * 
 */

define(['angular'], function () {
    globalProvider.compileProvider.directive('accordionCollapse',
            ['$rootScope', '$filter', '$templateCache', function ($rootScope, $filter, $templateCache) {

                    var link = function (scope, element, attrs) {
                        scope.collapsed = true;
                        scope.isOpen = false;
                        attrs.$observe('id', function (value) {
                            if (angular.isDefined(value)) {
                                scope.parentId = value + '-unique';
                            } else {
                                scope.parentId = 'unique-one';
                            }
                            scope.collapseId = scope.parentId + '-collapse';
                        });
                        attrs.$observe('title', function (value) {
                            if (angular.isDefined(value)) {
                                scope.title = value;
                            }
                        });
                        if (attrs.isOpen === 'true') {
                            scope.isOpen = true;
                            scope.collapsed = false;
                        }
                        element.on('hidden.bs.collapse', function (e) {
                            scope.collapsed = true;
                            scope.$apply();
                        });
                        element.on('shown.bs.collapse', function (e) {
                            scope.collapsed = false;
                            scope.$apply();
                        });
                    };

                    return {
                        restrict: 'A',
                        link: link,
                        transclude: true,
                        templateUrl: 'scripts/directives/accordion/accordion.tmpl.html'
                    };
                }]);


});
