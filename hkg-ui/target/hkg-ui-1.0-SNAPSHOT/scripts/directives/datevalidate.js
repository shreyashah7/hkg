/**
 * 
 * Author: Gautam
 * 
 * Objective : To validate and enable manual entry of date and time
 * 
 */

define(['angular'], function() {

    angular.module('hkg.directives').directive('dateValidate', ['$compile', '$filter', 'orderByFilter', '$parse', 'dateFilter', function($compile, $filter, orderByFilter, $parse, dateFilter) {
            return {
                restrict: 'A',
                require: 'ngModel',
                scope: true,
                link: function(scope, element, attr, modelCtrl) {
                    if (attr.validateFormat !== undefined && attr.validateFormat !== null && attr.validateFormat !== '') {
                        scope.validateFormat = attr.validateFormat;
                    } else {
                        scope.validateFormat = 'dd/MM/yyyy hh:mm a';
                    }

                    scope.watchData = {};
                    // Watchable date attributes
                    angular.forEach(['min', 'max'], function(key) {
                        if (attr[key]) {
                            scope.$watch($parse(attr[key]), function(value) {
                                scope.watchData[key] = value ? new Date(value) : null;
                            });
                        } else {
                        }
                    });
                    scope.parsers = {};

                    var formatCodeToRegex = {
                        'yyyy': {
                            regex: '\\d{4}',
                            apply: function(value) {
                                this.year = +value;
                            }
                        },
                        'yy': {
                            regex: '\\d{2}',
                            apply: function(value) {
                                this.year = +value + 2000;
                            }
                        },
                        'y': {
                            regex: '\\d{1,4}',
                            apply: function(value) {
                                this.year = +value;
                            }
                        },
                        'MM': {
                            regex: '0[1-9]|1[0-2]',
                            apply: function(value) {
                                this.month = value - 1;
                            }
                        },
                        'M': {
                            regex: '[1-9]|1[0-2]',
                            apply: function(value) {
                                this.month = value - 1;
                            }
                        },
                        'dd': {
                            regex: '[0-2][0-9]{1}|3[0-1]{1}',
                            apply: function(value) {
                                this.date = +value;
                            }
                        },
                        'd': {
                            regex: '[1-2]?[0-9]{1}|3[0-1]{1}',
                            apply: function(value) {
                                this.date = +value;
                            }
                        },
                        'hh': {
                            regex: '0?[0-9]|1[0-2]',
                            apply: function(value) {
                                this.hours = +value;
                            }
                        },
                        'mm': {
                            regex: '0?[0-9]|[1-5][0-9]',
                            apply: function(value) {
                                this.minutes = +value;
                            }
                        },
                        'a': {
                            regex: 'AM|am|PM|pm',
                            apply: function(value) {
                                this.meridiem = value;
                            }
                        }
                    };
                    function customParser(format) {
                        var map = [], regex = format.split('');

                        angular.forEach(formatCodeToRegex, function(data, code) {
                            var index = format.indexOf(code);
                            var tempApply = data.apply;
                            var tempRegx = data.regex;
                            if (code === 'dd') {
                                tempRegx = '0?[1-2]?[0-9]{1}|3[0-1]{1}';
                            }
                            if (code === 'MM') {
                                tempRegx = '0?[1-9]|1[0-2]';
                            }
                            if (code === 'yyyy') {
                                tempRegx = '(?:[0-9]{2})?[0-9]{2}';
                                tempApply = function(value) {
                                    if (value.length === 2) {
                                        this.year = +value + 2000;
                                    } else {
                                        this.year = +value;
                                    }
                                };
                            }

                            if (index > -1) {
                                format = format.split('');

                                regex[index] = '(' + tempRegx + ')';
                                format[index] = '$'; // Custom symbol to define consumed part of format
                                for (var i = index + 1, n = index + code.length; i < n; i++) {
                                    regex[i] = '';
                                    format[i] = '$';
                                }
                                format = format.join('');

                                map.push({index: index, apply: tempApply});
                            }
                        });

                        return {
                            regex: '^' + regex.join('') + '$',
                            map: orderByFilter(map, 'index')
                        };
                    }

                    scope.parse = function(input, format) {
                        if (!angular.isString(input) || !format) {
                            return input;
                        }


                        if (!scope.parsers[format]) {
                            scope.parsers[format] = customParser(format);
                        }

                        var parser = this.parsers[format],
                                regex = new RegExp(parser.regex.toString().split(' ').join('\\s')),
                                map = parser.map,
                                results = input.match(regex);
                        if (results && results.length) {
                            var fields = {year: 1900, month: 0, date: 1, hours: 0, minutes: 0, meridiem: null}, dt;

                            for (var i = 1, n = results.length; i < n; i++) {
                                var mapper = map[i - 1];
                                if (mapper.apply) {
                                    mapper.apply.call(fields, results[i]);
                                }
                            }
                            if (isValid(fields.year, fields.month, fields.date, fields.hours, fields.meridiem)) {
                                if (fields.meridiem !== null && fields.meridiem !== undefined) {
                                    if ((fields.meridiem === 'pm' || fields.meridiem === 'PM') && fields.hours < 12) {
                                        fields.hours = fields.hours + 12;
                                    }
                                    if ((fields.meridiem === 'am' || fields.meridiem === 'AM') && fields.hours === 12) {
                                        fields.hours = 0;
                                    }
                                }
                                dt = new Date(fields.year, fields.month, fields.date, fields.hours, fields.minutes);
                            }

                            return dt;
                        }
                    };

                    // Check if date is valid for specific month (and year for February).
                    // Month: 0 = Jan, 1 = Feb, etc
                    function isValid(year, month, date, hour, meridiem) {
                        if (meridiem !== undefined && meridiem !== null && hour === 0) {
                            return false;
                        }
                        if (date === 0) {
                            return false;
                        }
                        if (month === 1 && date > 28) {
                            return date === 29 && ((year % 4 === 0 && year % 100 !== 0) || year % 400 === 0);
                        }

                        if (month === 3 || month === 5 || month === 8 || month === 10) {
                            return date < 31;
                        }


                        return true;
                    }

                    //Returing null incase when user clears the selection by pop-over menu, we can assign it to undefined.
                    function parseDate(viewValue) {
                        modelCtrl.$setValidity('min', true);
                        modelCtrl.$setValidity('max', true);
                        modelCtrl.$setValidity('date', true);
                        if (!viewValue) {
                            modelCtrl.$setValidity('date', true);
                            return null;
                        } else if (angular.isDate(viewValue) && !isNaN(viewValue)) {
                            modelCtrl.$setValidity('date', true);
                            if (scope.watchData['min'] !== undefined && viewValue >= new Date(scope.watchData['min']).setHours(0, 0, 0, 0)) {
                                modelCtrl.$setValidity('min', true);
                            }
                            if (scope.watchData['max'] !== undefined && viewValue <= new Date(scope.watchData['max']).setHours(0, 0, 0, 0)) {
                                modelCtrl.$setValidity('max', true);
                            }
                            return getFormatedDate(viewValue, scope.validateFormat);
                        } else if (angular.isString(viewValue) || !isNaN(viewValue)) {
                            var date = scope.parse(viewValue, scope.validateFormat);
                            if (isNaN(date)) {
                                modelCtrl.$setValidity('date', false);
                                return null;
                            } else {
                                if (scope.watchData['min'] && date < new Date(scope.watchData['min']).setHours(0, 0, 0, 0)) {
                                    modelCtrl.$setValidity('min', false);
                                    return null;
                                }
                                if (scope.watchData['max']) {
                                    var maxDate = new Date(new Date(scope.watchData['max']).setHours(0, 0, 0, 0));
                                    maxDate = maxDate.setDate(maxDate.getDate() + 1);
                                    if (date >= maxDate) {
                                        modelCtrl.$setValidity('max', false);
                                        return null;
                                    }
                                }
                                return getFormatedDate(date, scope.validateFormat);
                            }
                        } else {
                            modelCtrl.$setValidity('date', false);
                            return null;
                        }
                    }
                    modelCtrl.$parsers.unshift(parseDate);
                    
                    //Remove all validation if model is valid. ** Selection from pop-over is assumed valid.
                    function formatDate(value){
                        modelCtrl.$setValidity('min', true);
                        modelCtrl.$setValidity('max', true);
                        modelCtrl.$setValidity('date', true);
                        return value;
                    }
                    modelCtrl.$formatters.unshift(formatDate);
                    
                    var getFormatedDate = function(date, format) {
                        if (format === undefined) {
                            format = "dd/MM/yyyy";
                        }

                        return $filter('date')(date, format);
                    };

                }
            };
        }]);
});
