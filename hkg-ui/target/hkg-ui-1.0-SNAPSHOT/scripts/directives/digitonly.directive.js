define(['angular'], function() {
    angular.module('hkg.directives').directive('numbersOnly',
            ['$parse', '$compile', function($parse, $compile) {
                    return {
                        require: ['^form', 'ngModel'],
                        link: function(scope, element, attrs, ctrls) {
                            var modelCtrl = ctrls[1];

                            scope.$watch(attrs.ngModel, function(newValue, oldValue) {

                                var mdlctrl = ctrls[1];
                                var model = $parse(attrs.ngModel);
                                var arr = String(newValue).split("");
                                if (arr.length === 0) {
                                    // This condition added because when I enter and then clear then also it was showing message
                                    mdlctrl.$setValidity('min', true);
                                    mdlctrl.$setValidity('max', true);

                                    return;
                                }
                                if (arr.length === 1 && (((arr[0] === '-') &&
                                        (attrs.negativeallowed !== undefined &&
                                                attrs.negativeallowed.toString() === "true")) || ((arr[0] === '.') &&
                                        (attrs.decimalallowed !== undefined && attrs.decimalallowed.toString()
                                                === "true"))))
                                {
                                    return;
                                }
                                // This condition added as if after typing numbers then i go back and add minus sign,it should not allow
                                if (arr.length > 1 && (((arr[0] === '-') &&
                                        (attrs.negativeallowed !== undefined &&
                                                (attrs.negativeallowed.toString() === 'false') && (attrs.negativeallowed !== true) && (attrs.negativeallowed.toString() !== 'true')))))
                                {

                                    if ((newValue !== undefined)) {
                                        transformedNewValue =
                                                newValue.toString().replace('-', '');
                                        if (newValue !== transformedNewValue) {
                                            model.assign(scope,
                                                    transformedNewValue);
                                        }
                                    }
                                }

                                if ((arr.length === 2 && newValue === '-.') &&
                                        ((attrs.decimalallowed !== undefined &&
                                                attrs.decimalallowed.toString() === "true"))) {
                                    return;
                                }
                                if (attrs.currencyallowed === undefined)
                                {

                                    if (attrs.negativeallowed === undefined ||
                                            attrs.negativeallowed.toString() === false) {
                                        if ((newValue !== undefined && newValue !== null)) {
                                            transformedNewValue =
                                                    newValue.toString().replace('-', '');
                                            if (newValue !== transformedNewValue) {
                                                model.assign(scope,
                                                        transformedNewValue);
                                            }
                                        }
                                    }
                                    if (attrs.decimalallowed === undefined ||
                                            attrs.decimalallowed.toString() === false) {
                                        if ((newValue !== undefined && newValue !== null)) {
                                            transformedNewValue =
                                                    newValue.toString().replace('.', '');
                                            if (newValue !== transformedNewValue) {
                                                model.assign(scope,
                                                        transformedNewValue);
                                            }
                                        }
                                    }
                                }
                                if (attrs.currencyallowed !== undefined &&
                                        attrs.currencyallowed.toString() === "true") {
                                    consoel.log("6")
                                    if (newValue === undefined)
                                        return '';
                                    var transformedInput =
                                            newValue.replace(/[^0-9.]/g, '');
                                    while (transformedInput.charAt(0) === '0') {
                                        transformedInput = transformedInput.substr(1);
                                    }
                                    var totalcurrency;
                                    var decimalInput =
                                            transformedInput.toString().split(".")[0];
                                    var afterdecimalInput =
                                            transformedInput.toString().split(".")[1];
                                    var currencyInput = decimalInput.replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
                                    if (afterdecimalInput === undefined)
                                    {

                                        totalcurrency = currencyInput;
                                    }
                                    else
                                    {
                                        totalcurrency = "" + currencyInput + "." +
                                                afterdecimalInput;
                                    }
                                    if (totalcurrency !== newValue) {
                                        modelCtrl.$setViewValue(totalcurrency);
                                        modelCtrl.$render();
                                    }

                                }
                                if (attrs.currencyallowed === undefined) {
                                    if (isNaN(newValue)) {
                                        if ((newValue === undefined)) {
                                            model.assign(scope, newValue);
                                        }
                                        else {
                                            model.assign(scope, oldValue);
                                        }

                                    } else {
                                        if (attrs.min !== undefined &&
                                                ((parseInt(attrs.min) <= parseInt(newValue)))) {
                                            mdlctrl.$setValidity('min', true);
                                        }

                                        else if (attrs.min === undefined) {

                                        }
                                        else {
                                            mdlctrl.$setValidity('min', false);
                                        }

                                        if (attrs.max !== undefined &&
                                                ((parseInt(attrs.max) >= parseInt(newValue)))) {
                                            mdlctrl.$setValidity('max', true);
                                        }
                                        else if (attrs.max === undefined) {

                                        }
                                        else {
                                            mdlctrl.$setValidity('max', false);
                                        }
                                    }
                                }
                            });
                        }
                    };
                }])
            .directive("onlyDigits", function()
            {
                return {
                    restrict: 'EA',
                    require: '?ngModel',
                    scope: {
                        allowDecimal: '@',
                        allowNegative: '@',
                        minNum: '@',
                        maxNum: '@'
                    },
                    link: function(scope, element, attrs, ngModel)
                    {
                        if (!ngModel)
                            return;
                        ngModel.$parsers.unshift(function(inputValue)
                        {
                            var decimalFound = false;
                            var digits = inputValue.split('').filter(function(s, i)
                            {
                                var b = (!isNaN(s) && s != ' ');
                                if (!b && attrs.allowDecimal && attrs.allowDecimal == "true")
                                {
                                    if (s == "." && decimalFound == false)
                                    {
                                        decimalFound = true;
                                        b = true;
                                    }
                                }
                                if (!b && attrs.allowNegative && attrs.allowNegative == "true")
                                {
                                    b = (s == '-' && i == 0);
                                }

                                return b;
                            }).join('');
                            if (attrs.maxNum && !isNaN(attrs.maxNum) && parseFloat(digits) > parseFloat(attrs.maxNum))
                            {
                                digits = attrs.maxNum;
                            }
                            if (attrs.minNum && !isNaN(attrs.minNum) && parseFloat(digits) < parseFloat(attrs.minNum))
                            {
                                digits = attrs.minNum;
                            }
                            ngModel.$viewValue = digits;
                            ngModel.$render();

                            return digits;
                        });
                    }
                };
            });
});