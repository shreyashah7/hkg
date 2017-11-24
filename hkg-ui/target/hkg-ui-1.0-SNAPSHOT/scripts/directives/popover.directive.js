/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['angular'], function() {
    /**
     * @author anita
     * agPopover - Directive for displaying popover 
     * 
     * Usage : <any-element ag-popover placement='bottom' trigger='hover' title='abc' data='htmlFilePath'></any-element>
     * different option for trigger- hover,manual
     * different option for pacement-left,right,bottom,top
     * element id is mendatory for close button purpose
     */
    angular.module('hkg.directives').directive('agPopover', ['$http', function($http) {
            return {
                restrict: 'A',
                transclude: true,
                template: '<span ng-transclude></span>',
                link: function(scope, element, attrs) {
                    scope.popoverObj = $(element).popover({
                        placement: 'bottom',
                        container: 'body',
                        
                        html: true,
                        content: function() {
                            return $(element).next('.popper-content').html();
                        }
                    });
                    if (attrs.agPopover !== undefined && attrs.agPopover === 'mouseover') {
                        scope.popoverObj.on("mouseenter", function() {
                            var _this = this;
                            $(this).popover("show");
                            $(this).siblings(".popover").on("mouseleave", function() {
                                    $(_this).popover('hide');
                            });
                        }).on("mouseleave", function() {
                            var _this = this;
                            setTimeout(function() {
                                  $(_this).popover("hide");
//                                if (!$(".popover:hover").length) {
//                                    $(_this).popover("hide")
//                                }
                            }, 100);
                        });
                    }
                }
            };
        }]);
});