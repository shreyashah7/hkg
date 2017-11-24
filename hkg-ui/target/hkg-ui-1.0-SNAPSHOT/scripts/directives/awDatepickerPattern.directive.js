define(['angular'], function () {
    angular.module('hkg.directives').directive('awDatepickerPattern',
            ['$parse', '$compile', function ($parse, $compile) {
                   console.log("inside directive");
                    return {
                        restrict: 'A',
                        require: 'ngModel',
                        link: function (scope, elem, attrs, ngModelCtrl) {
                            console.log("patterns");
                            var dRegex = new RegExp(attrs.pattern);
                            ngModelCtrl.$parsers.unshift(function (value) {

                                if (typeof value === 'string') {
                                    var isValid = dRegex.test(value);
                                    ngModelCtrl.$setValidity('date', isValid);
                                    if (!isValid) {
                                        return undefined;
                                    }
                                }

                                return value;
                            });
                        }
                    };
                }]);
});
