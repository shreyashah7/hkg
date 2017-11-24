define(['angular', 'loadmask'], function() {
    /**
     * @author akta
     * agMask - Directive for displaying loading mask over any eliment
     * 
     * Usage : <any-element ag-mask='var'></any-element>
     * Now you have to define variable 'var' in corresponding scope as boolean and set it's properties like template, templateUrl,
     * message, imagePath, mask
     * you must have to set mask property to true or false.
     * other properties can be set at time one only. You can combine only imagePath and message means you can set both and will get 
     * output also for both
     * Ex.mask = {mask: true,
     template: "<img src='imagepath/base64Data'>",
     templateUrl: "mytemp.html",
     message:"message",
     imagePath:"images/loading_bar3.gif"
     };
     
     */
    angular.module('hkg.directives').directive('agMask', [function() {
            function linkFn(scope, element, attrs) {
                scope.$watch(attrs.agMask, function(mask) {
                    if (mask.mask === true) {
                        if (mask.template) {
                            element.mask("", "", mask.template, "", "");
                        }
                        else if (mask.templateUrl) {
                            element.mask("", "", "", mask.templateUrl, "");
                        }
                        else if (mask.message && mask.imagePath) {
                            element.mask(mask.message, "", "", "", mask.imagePath);
                        }
                        else if (mask.imagePath) {
                            element.mask("", "", "", "", mask.imagePath);
                        }
                        else if (mask.message) {
                            element.mask(mask.message, "", "", "", "");
                        }
                        else {
                            element.mask("", "", "", "", "");
                        }
                    } else if (mask.mask === false) {
                        element.unmask();
                    }
                }, true);
            }
            return{
                restrict: 'A',
                link: linkFn
            };
        }]);

    /**
     * @author dhwani
     * inputMinlength - Directive for setting dynamic minlength of input
     * 
     * Usage : <input class="form-control" input-minlength="{{min_length}}" ng-model="something"/>
     * <span class="help-block" ng-show="addAssetForm.serialNumber.$error.minlength">Too short</span>             
     */
    angular.module('hkg.directives').directive('inputMinlength', function() {
        return {
            require: 'ngModel',
            link: function(scope, elm, attr, ngModel) {

                var minlength = 0;

                var minLengthValidator = function(value) {
                    var validity = ngModel.$isEmpty(value) || value.length >= minlength;
                    ngModel.$setValidity('minlength', validity);
                    return validity ? value : undefined;
                };

                attr.$observe('inputMinlength', function(val) {
                    minlength = parseInt(val, 10);
                    minLengthValidator(ngModel.$viewValue);
                   
                });


                ngModel.$parsers.push(minLengthValidator);
//                ngModel.$formatters.push(minLengthValidator);
            }
        };
    });

    angular.module('hkg.directives').directive('inputMaxlength', function() {
        return {
            require: 'ngModel',
            link: function(scope, elm, attr, ngModel) {

                var maxlength = 0;

                var maxLengthValidator = function(value) {
                    var validity = !ngModel.$isEmpty(value) && value.length <= maxlength;
                    ngModel.$setValidity('maxlength', validity);
                    return validity ? value : undefined;
                };

                attr.$observe('inputMaxlength', function(val) {
                    maxlength = parseInt(val, 10);
                    maxLengthValidator(ngModel.$viewValue);
                });


                ngModel.$parsers.push(maxLengthValidator);
//                ngModel.$formatters.push(maxLengthValidator);
            }
        };
    });
//                attrs.$observe("inputMaxlength", function(newval) {
//                    var maxlength = parseInt(newval, 10);
//                    var name = "srMaxLengthValidator";
//
//                    for (var i = ctrl.$parsers.length - 1; i >= 0; i--) {
//                        if (ctrl.$parsers[i].name !== undefined && ctrl.$parsers[i].name == name) {
//                            ctrl.$parsers.splice(i, 1);
//                        }
//                    }
//
//                    for (var j = ctrl.$formatters.length - 1; j >= 0; j--) {
//                        if (ctrl.$formatters[j].name !== undefined && ctrl.$formatters[j].name == name) {
//                            ctrl.$formatters.splice(j, 1);
//                        }
//                    }
//
//                    ctrl.$parsers.push(maxLengthValidator);
//                    ctrl.$formatters.push(maxLengthValidator);
//
//                    //name the function so we can find it always by the name
//                    maxLengthValidator.name = name;
//
//                    function maxLengthValidator(value) {
//                        if (!ctrl.$isEmpty(value) && value.length > maxlength) {
//                            ctrl.$setValidity('maxlength', false);
//                            return undefined;
//                        } else {
//                            ctrl.$setValidity('maxlength', true);
//                            return value;
//                        }
//                    }
//                });
//        }

});