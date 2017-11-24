/**
 * @author Kuldeep Vithlani
 * 
 * Added for multiselect with tagging support
 * 
 * Example:
 * 
 *  <input
 type="text"
 ui-select2="select2Options"
 ng-model="list_of_string"
 > 
 
 
 In controller
 ----------------
 $scope.list_of_string = ['tag1', 'tag2']
 $scope.select2Options = {
 'multiple': true,
 'closeOnSelect':false,
 'simple_tags': true,
 'tags': ['tag1', 'tag2', 'tag3', 'tag4']  // Can be empty list.
 };
 * 
 * Enhanced Select2 Dropmenus
 *
 * @AJAX Mode - When in this mode, your value will be an object (or array of objects) of the data used by Select2
 *     This change is so that you do not have to do an additional query yourself on top of Select2's own query
 * @params [options] {object} The configuration options passed to $.fn.select2(). Refer to the documentation
 */
define(['angular'], function() {
    angular.module('hkg.directives').directive('uiSelect2', ['$timeout', function($timeout) {
            var options = {};
//            if (uiConfig.select2) {
//                angular.extend(options, uiConfig.select2);
//            }
            return {
                require: ['^form', '?ngModel'],
                compile: function(tElm, tAttrs) {
                    var watch,
                            repeatOption,
                            repeatAttr,
                            isSelect = tElm.is('select'),
                            isMultiple = (tAttrs.multiple !== undefined);

                    // Enable watching of the options dataset if in use
                    if (tElm.is('select')) {
                        repeatOption = tElm.find('option[ng-repeat], option[data-ng-repeat]');

                        if (repeatOption.length) {
                            repeatAttr = repeatOption.attr('ng-repeat') || repeatOption.attr('data-ng-repeat');
                            watch = jQuery.trim(repeatAttr.split('|')[0]).split(' ').pop();
                        }
                    }

                    return function(scope, elm, attrs, ctrls) {
                        var controller = ctrls[1];
//                        console.log(controller);
                        // instance-specific options
                        var opts = angular.extend({}, options, scope.$eval(attrs.uiSelect2));

                        if (isSelect) {
                            // Use <select multiple> instead
                            delete opts.multiple;
                            delete opts.initSelection;
                        } else if (isMultiple) {
                            opts.multiple = true;
                        }

                        if (controller) {
                            // Watch the model for programmatic changes
                            controller.$render = function() {
//                                console.log("isSelect=" + isSelect)
                                if (isSelect) {
                                    elm.select2('val', controller.$modelValue);
                                } else {
                                    if (isMultiple) {
//                                        console.log(" if (isMultiple) {")
                                        if (!controller.$modelValue) {
                                            elm.select2('data', []);
                                        } else if (angular.isArray(controller.$modelValue)) {
                                            elm.select2('data', controller.$modelValue);
                                        } else {
                                            elm.select2('val', controller.$modelValue);
                                        }
                                    } else {
//                                        console.log("else {")
                                        if (angular.isObject(controller.$modelValue)) {
                                            elm.select2('data', controller.$modelValue);
                                        } else {
                                            elm.select2('val', controller.$modelValue);
                                        }
                                    }
                                }
                            };

                            // Watch the options dataset for changes
                            if (watch) {
                                scope.$watch(watch, function(newVal, oldVal, scope) {
                                    if (!newVal)
                                        return;
                                    // Delayed so that the options have time to be rendered
                                    $timeout(function() {
                                        elm.select2('val', controller.$viewValue);
                                        // Refresh angular to remove the superfluous option
                                        elm.trigger('change');
                                        controller.$setPristine();
                                        ctrls[0].$setPristine();
                                    });
                                });
                            }

                            if (!isSelect) {
                                // Set the view and model value and update the angular template manually for the ajax/multiple select2.
                                elm.bind("change", function() {
                                    scope.$apply(function() {
                                        controller.$setViewValue(elm.select2('data'));
                                    });
                                });

                                if (opts.initSelection) {
                                    var initSelection = opts.initSelection;
                                    opts.initSelection = function(element, callback) {
                                        initSelection(element, function(value) {
                                            controller.$setViewValue(value);
                                            callback(value);
                                        });
                                    };
                                }
                            }
                        }

                        attrs.$observe('disabled', function(value) {
                            elm.select2(value && 'disable' || 'enable');
                        });

                        if (attrs.ngMultiple) {
                            scope.$watch(attrs.ngMultiple, function(newVal) {
                                elm.select2(opts);
                            });
                        }

                        // Set initial value since Angular doesn't
                        elm.val(scope.$eval(attrs.ngModel));

                        // Initialize the plugin late so that the injected DOM does not disrupt the template compiler
                        $timeout(function() {
//                            elm.select2(opts);
                            //Changed by kvithlani to call initSelection method.
                            elm.select2(opts).select2('val', elm.select2('val'));
                            // Not sure if I should just check for !isSelect OR if I should check for 'tags' key
                            if (!opts.initSelection && !isSelect)
                                controller.$setViewValue(elm.select2('data'));
                            controller.$setPristine();
                            ctrls[0].$setPristine();
                        });

                        //Added by kvithlani for changing selected values on model value change
                        var modelVar = attrs.ngModel;
                        scope.$watch(modelVar, function(value) {
//                            console.log("value=" + JSON.stringify(value) + "condition : " + (value instanceof Object) + "instance of" + typeof value);
//                            console.log("isMultiple="+isMultiple);
//                            if (value !== undefined && value instanceof Object === true) {
//                                console.log("if")
//                                $(elm).select2("data", value);
//                            } else if (value instanceof Object === false && value === '') {
//                                console.log("else")
//                                $(elm).select2("val", value);
//                            }
                            if (value instanceof Object === false && value === '') {
                                $(elm).select2("val", value);
                            }
                            if (scope[attrs.uiSelect2]) {
                                if (scope[attrs.uiSelect2]['multiple'] && angular.isArray(value)) {
//                                    console.log("multiple");
                                    $(elm).select2("data", value);
                                }
                                else if (!scope[attrs.uiSelect2]['multiple'] && value !== undefined) {
//                                    console.log("not multiple===" + JSON.stringify(value));
                                    if (value instanceof Object === true) {
                                        $(elm).select2("data", value);
                                    } else if (attrs.selectById) {
                                        $(elm).select2("val", value);
                                    }
                                }
                            } else if (value !== undefined && value instanceof Object === true) {
//                                console.log("if")
                                $(elm).select2("data", value);
                            }
                        });
                        //Added by Akta Kalariya to watch uiSelect2 options variable for change
                        if (angular.isDefined(attrs.dynamicOptions)) {
                            scope.$watch(attrs.uiSelect2, function(data) {
                                if (angular.isDefined(data)) {
                                    opts = angular.extend({}, options, scope.$eval(attrs.uiSelect2));
                                    $(elm).select2(opts);
                                }
                            }, true);
                        }

                    };
                }
            };
        }]);
});