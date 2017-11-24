define(['angular', 'dateValidate'], function() {
    globalProvider.compileProvider.directive('agDatePickerCustom', ['$compile', '$filter', 'orderByFilter', '$document', function($compile, $filter, orderByFilter, $document) {
            return {
                restrict: 'E',
                template: '<div>' +
                        '<div ng-if="isDateTime" ng-init="initDateTimePopup()">' +
                        '<div  class="dropdown keep-open">' +
                        '<div class="input-group" >' +
                        '<input date-validate validate-format="{{directiveFormat}}" placeholder="{{directiveFormat}}" min="dateTimeMin" max="dateTimeMax" type="text" id="ngModel" name="ngModel" class="form-control" ng-model="$parent.$parent.ngModel">' +
//                        '<a style="cursor:default;text-decoration: none" ng-class="{disabled:isDisabled}" >' +
                        '<span class="input-group-btn" id="dateTimeSpan" data-toggle="dropdown"  ng-click="datePickerFlag.dateTimePicker = true;"><button ng-disabled="isDisabled" class="btn btn-default" style="border-bottom-left-radius: 0;border-top-left-radius: 0;">' +
                        '<i class="glyphicon glyphicon-calendar" id="dateTimeI"/></button>' +
                        '</span>' +
//                        '</a>' +

                        '<ul class="dropdown-menu">' +
                        '<li>' +
                        '<datepicker is-open="datePickerFlag.dateTimePicker" id="dd" ng-model="$parent.datepickerModel"  min="dateTimeMin" max="dateTimeMax" date-disabled="disabled(date,mode)" ng-change="setDate(datepickerModel,timeModel)"></datepicker>' +
                        '</li>' +
                        '<li class="text-center">' +
                        '<timepicker remove-required="{{ngRequired}}" align="center" ng-model="$parent.timeModel" hour-step="hstep" minute-step="mstep" show-meridian="true" ng-change="setTime(timeModel,datepickerModel)"></timepicker>' +
                        '<button class="btn btn-hkg" ng-click=setDateAndTime("datetime",timeModel)>Clear</button>&nbsp;&nbsp;' +
                        '<button class="btn btn-hkg" ng-click="setDateAndTime()">Close</button>' +
                        '</li>' +
                        '</ul>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '<div ng-if="isDate">' +
                        '<div ng-class="{\'input-group\': hideCalendar}">' +
                        '<input type="text" style="background-color:{{backgroundColor}}" id="ngModel" is-open="$parent.dtPicker.dt_open"  name="ngModel" class="form-control" datepicker-popup="{{dateFormat}}"  ng-model="ngModel" min="onlyMinDate" max="onlyMaxDate" date-disabled="disabled(date,mode)" ng-change=setOnlyDate(ngModel) placeholder="{{dateFormat}}">' +
                        '<span class="input-group-btn" ng-if="hideCalendar" ng-click=(isDisabled||open($event,"dt_open"))><button ng-disabled="isDisabled" class="btn btn-default">' +
                        '<i class="glyphicon glyphicon-calendar" ></i></button>' +
                        '</span>' +
                        '</div>' +
                        '</div>' +
                        '<div ng-if="isTime" ng-init="initDateTimePopup()">' +
                        '<div  class="dropdown keep-open">' +
                        '<a style="cursor:default;text-decoration: none"  data-toggle="dropdown">' +
                        '<div class="input-group" >' +
                        '<input type="text" id="ngModel"  name="ngModel" class="form-control"   ng-model="ngModel"  >' +
                        '<span id="timeSpan" class="input-group-addon">' +
                        '<i id="timeI" class="glyphicon glyphicon-time">' +
                        '</i>' +
                        '</span>' +
                        '</div>' +
                        '</a>' +
                        '<ul class="dropdown-menu">' +
                        '<li class="text-center">' +
                        '<timepicker  remove-required="{{ngRequired}}" align="center" ng-model="$parent.timeModel" defaultTime="false" hour-step="hstep" minute-step="mstep" show-meridian="true" ng-change=setTime(timeModel) ></timepicker>' +
                        '<button class="btn btn-hkg" ng-click=setDateAndTime("datetime")>Clear</button>' +
                        '</li>' +
                        '</ul>' +
                        '</div>\n\
                    </div>' +
                        '<div ng-if="isDateRange" ng-init="initDateTimePopup()">' +
                        '<div  class="row" >' +
                        '<div ng-class="inputclass">' +
                        '<div ng-form="fromForm">' +
                        '<div ng-class="{\'has-error\': fromForm.fromModel.$invalid}">' +
                        '<div class="input-group" >' +
                        '<input type="text" id="fromModel" is-open="$parent.dtPicker.open_from"  name="fromModel" class="form-control" placeholder="{{dateRangeFormat}}" datepicker-popup="{{dateRangeFormat}}"  ng-model="$parent.fromModel" min="fromMinDate" max="fromMaxDate" ng-change="setFromDate(fromModel)" ng-required="ngRequired">' +
                        '<span class="input-group-btn" ng-click=(isDisabled||open($event,"open_from"))><button ng-disabled="isDisabled" class="btn btn-default">' +
                        '<i class="glyphicon glyphicon-calendar"></i></button>' +
                        '</span>' +
                        '</div>' +
                        '<div ng-show="fromForm.fromModel.$invalid" class = "error,help-block ng-hide" >' +
                        '<span class="help-block" ng-show="fromForm.fromModel.$error.required && !(fromForm.fromModel.$error.date || fromForm.fromModel.$error.min || fromForm.fromModel.$error.max)">' +
                        '{{fromLabel}}{{ entity +  " not entered" | translate }}</span>' +
                        '<span class="help-block" ng-show="fromForm.fromModel.$error.date">{{ entity +  "Invalid date format" | translate }}</span>' +
                        '<span class="help-block" ng-show="fromForm.fromModel.$error.min">{{ entity +  "Violates minimum constraint" | translate }}</span>' +
                        '<span class="help-block" ng-show="fromForm.fromModel.$error.max">{{ entity +  "Violates maximum constraint" | translate }}</span>' +
                        '</span>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        ' <label ng-class="labelclass">{{toLabel}}</label>' +
                        '<div ng-class="inputclass">' +
                        '<div ng-form="toForm">' +
                        '<div ng-class="{\'has-error\': toForm.toModel.$invalid}">' +
                        '<div class="input-group" >' +
                        '<input type="text" id="toModel" is-open="$parent.dtPicker.open_to"  name="toModel" class="form-control" min="toMinDate" placeholder="{{dateRangeFormat}}" max="toMaxDate" datepicker-popup="{{dateRangeFormat}}"  ng-model="$parent.toModel" ng-change="setToDate(toModel)" ng-required="ngRequired">' +
                        '<span class="input-group-btn" ng-click=(isDisabled||open($event,"open_to"))><button class="btn btn-default" style="border-bottom-left-radius: 0;border-top-left-radius: 0;">' +
                        '<i class="glyphicon glyphicon-calendar"></i></button>' +
                        '</span>' +
                        '</div>' +
                        '<div ng-show="toForm.toModel.$invalid" class = "error,help-block ng-hide" >' +
                        '<span class="help-block" ng-show="toForm.toModel.$error.required && !(toForm.toModel.$error.date || toForm.toModel.$error.min || toForm.toModel.$error.max)">' +
                        '{{fromLabel}}{{ entity +  " not entered" | translate }}</span>' +
                        '<span class="help-block" ng-show="toForm.toModel.$error.date">{{ entity +  "Invalid date format" | translate }}</span>' +
                        '<span class="help-block" ng-show="toForm.toModel.$error.min">{{ entity +  "Violates minimum constraint" | translate }}</span>' +
                        '<span class="help-block" ng-show="toForm.toModel.$error.max">{{ entity +  "Violates maximum constraint" | translate }}</span>' +
                        '</span>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '<div ng-if="isTimeRange" ng-init="initDateTimePopup()">' +
                        '<div class="row">' +
                        '<div ng-class="inputclass">' +
                        '<div  class="dropdown keep-open">' +
                        '<a style="cursor:default;text-decoration: none" data-toggle="dropdown">' +
                        '<div ng-if="ngRequired" ng-form="fromForm">' +
                        '<div ng-class="{\'has-error\': submitted && fromForm.fromModel.$invalid}">' +
                        '<div class="input-group" >' +
                        '<input type="text" id="fromModel"  name="fromModel" class="form-control"   ng-model="fromModel"  required>' +
                        '<span id="fromTimeSpan" class="input-group-addon">' +
                        '<i id="fromTimeI" class="glyphicon glyphicon-time">' +
                        '</i>' +
                        '</span>' +
                        '</div>' +
                        '<div ng-show="submitted && fromForm.fromModel.$invalid" class = "error,help-block ng-hide" >' +
                        '<span class = "help-block" ng-show = "fromForm.fromModel.$error.required" > {{fromLabel}}{{ entity +  " not entered" | translate }}' +
                        '</span>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '<div ng-if="!ngRequired">' +
                        '<div class="input-group" >' +
                        '<input type="text" id="fromModel"  name="fromModel" class="form-control"   ng-model="fromModel">' +
                        '<span id="fromTimeSpan" class="input-group-addon">' +
                        '<i id="fromTimeI" class="glyphicon glyphicon-time">' +
                        '</i>' +
                        '</span>' +
                        '</div>' +
                        '</div>' +
                        '</a>' +
                        '<ul class="dropdown-menu">' +
                        '<li class="text-center">' +
                        '<timepicker remove-required="{{ngRequired}}" align="center" ng-model="$parent.fromTimeModel" defaultTime="false" hour-step="hstep" minute-step="mstep" show-meridian="true" ng-change=setFromTime(fromTimeModel) ></timepicker>' +
                        '<button class="btn btn-hkg" ng-click=setDateAndTime("fromTime")>Clear</button>' +
                        '</li>' +
                        '</ul>' +
                        '</div>' +
                        '</div>' +
                        ' <label ng-class="labelclass">{{toLabel}}</label>' +
                        '<div ng-class="inputclass">' +
                        '<div  class="dropdown keep-open">' +
                        '<a style="cursor:default;text-decoration: none"  data-toggle="dropdown">' +
                        '<div ng-if="ngRequired" ng-form="toForm">' +
                        '<div ng-class="{\'has-error\': submitted && toForm.toModel.$invalid}">' +
                        '<div class="input-group" >' +
                        '<input type="text" id="toModel"  name="toModel" class="form-control"   ng-model="toModel"  required>' +
                        '<span id="toTimeSpan" class="input-group-addon">' +
                        '<i id="toTimeI" class="glyphicon glyphicon-time">' +
                        '</i>' +
                        '</span>' +
                        '</div>' +
                        '<div ng-show="submitted && toForm.toModel.$invalid" class = "error,help-block ng-hide" >' +
                        '<span class = "help-block" ng-show = "toForm.toModel.$error.required" > {{toLabel}}{{ entity +  " not entered" | translate }}' +
                        '</span>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '<div ng-if="!ngRequired">' +
                        '<div class="input-group" >' +
                        '<input type="text" id="toModel"  name="toModel" class="form-control"   ng-model="toModel" >' +
                        '<span id="toTimeSpan" class="input-group-addon">' +
                        '<i id="toTimeI" class="glyphicon glyphicon-time">' +
                        '</i>' +
                        '</span>' +
                        '</div>' +
                        '</div>' +
                        '</a>' +
                        '<ul class="dropdown-menu">' +
                        '<li class="text-center">' +
                        '<div>' +
                        '<timepicker remove-required="{{ngRequired}}" align="center" ng-model="$parent.toTimeModel" defaultTime="false" hour-step="hstep" minute-step="mstep" show-meridian="true" ng-change=setToTime(toTimeModel) ></timepicker>' +
                        '<button class="btn btn-hkg" ng-click=setDateAndTime("toTime")>Clear</button>' +
                        '</div>' +
                        '</li>' +
                        '</ul>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '<div ng-if="isDateTimeRange" ng-init="initDateTimePopup()">' +
                        '<div  class="row" >' +
                        //'<label ng-class="labelclass">{{fromLabel}}</label>' +
                        '<div ng-class="inputclass">' +
                        '<div  class="dropdown keep-open">' +
//                        '<a style="cursor:default;text-decoration: none"   id="dLabel" role="button" ' +
//                        'data-toggle="dropdown" data-target="#" ng-click = "datePickerFlag.fromIsOpen = true;">' +

                        '<div ng-form="fromForm">' +
                        '<div ng-class="{\'has-error\': submitted && fromForm.fromModel.$invalid}">' +
                        '<div class="input-group" >' +
                        '<input date-validate validate-format="{{directiveFormat}}" placeholder="{{directiveFormat}}" min="minDate" max="toDatepickerModel" type="text" id="fromModel" name="fromModel" class="form-control"   ng-model="$parent.$parent.fromModel"  ng-required="ngRequired">' +
                        '<span id="frmDt" class="input-group-btn" data-toggle="dropdown" ng-click = "datePickerFlag.fromIsOpen = true;"><button class="btn btn-default" style="border-bottom-left-radius: 0;border-top-left-radius: 0;">' +
                        '<i id="fromI" class="glyphicon glyphicon-calendar">' +
                        '</i></button>' +
                        '</span>' +
                        '<ul class="dropdown-menu" role="menu" ' +
                        'aria-labelledby="dLabel">' +
                        '<li>' +
                        '<datepicker is-open="datePickerFlag.fromIsOpen" id="rangeFromDatepickerModel" ng-model="$parent.rangeFromDatepickerModel" min="minDate" max="toDatepickerModel" show-weeks="true" class="well well-sm" ng-change="setRangeFromDate(rangeFromDatepickerModel,rangFromTimeModel)"></datepicker>' +
                        '</li>' +
                        ' <li class="text-center">' +
                        '<timepicker remove-required="{{ngRequired}}" align="center" ng-model="$parent.rangFromTimeModel" hour-step=" " minute-step=" " show-meridian="true" ng-change="setRangeFromTime(rangFromTimeModel,rangeFromDatepickerModel)"></timepicker>' +
                        '<button class="btn btn-hkg" ng-click=setDateAndTime("rangeFromClear")>Clear</button>&nbsp;&nbsp;' +
                        '<button class="btn btn-hkg" ng-click="setDateAndTime()">Close</button>' +
                        '</li>' +
                        '</div>' +
                        '</ul>' +
                        '<div ng-show="ngRequired && submitted && fromForm.fromModel.$invalid" class = "error,help-block ng-hide" >' +
                        '<span class="help-block" ng-show="fromForm.fromModel.$error.required && !(fromForm.fromModel.$error.date || fromForm.fromModel.$error.min || fromForm.fromModel.$error.max)">' +
                        '{{fromLabel}}{{ entity +  "From date not entered" | translate }}</span>' +
                        '<span class="help-block" ng-show="fromForm.fromModel.$error.date">{{ entity +  "Invalid date format" | translate }}</span>' +
                        '<span class="help-block" ng-show="fromForm.fromModel.$error.min">{{ entity +  "Violates minimum constraint" | translate }}</span>' +
                        '<span class="help-block" ng-show="fromForm.fromModel.$error.max">{{ entity +  "Violates maximum constraint" | translate }}</span>' +
                        '</span>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
//                       

                        '</div>' +
                        '<label ng-class="labelclass">To</label> ' +
                        '<div ng-class="inputclass">' +
                        '<div  class="dropdown keep-open" id="toDDD">' +
//                        '<a style="cursor:default;text-decoration: none"  id="dLabel" role="button"' +
//                        'data-toggle="dropdown" data-target="#" ng-click = "datePickerFlag.toIsOpen = true;">' +

                        '<div ng-form="toForm">' +
                        '<div ng-class="{\'has-error\': submitted && toForm.toModel.$invalid}">' +
                        '<div class="input-group" >' +
                        '<input date-validate validate-format="{{directiveFormat}}" placeholder="{{directiveFormat}}" min="rangeFromDatepickerModel" max="maxDate" type="text" id="toModel" name="toModel" class="form-control"   ng-model="$parent.$parent.toModel"  ng-required="ngRequired">' +
                        '<span id="toDt" class="input-group-btn" data-toggle="dropdown" ng-click = "datePickerFlag.toIsOpen = true;"><button class="btn btn-default" style="border-bottom-left-radius: 0;border-top-left-radius: 0;">' +
                        '<i id="toI" class="glyphicon glyphicon-calendar" >' +
                        '</i></button>' +
                        '</span>' +
                        '<ul class="dropdown-menu" role="menu" ' +
                        'aria-labelledby="dLabel" >' +
                        '<li>' +
                        '<datepicker is-open="datePickerFlag.toIsOpen" id="toDatepickerModel" ng-model="$parent.toDatepickerModel" min="rangeFromDatepickerModel" max="maxDate" show-weeks="true" class="well well-sm" ng-change="setRangeToDate(toDatepickerModel,rangeToTimeModel)"></datepicker>' +
                        '</li>' +
                        ' <li class="text-center"> ' +
                        '<timepicker remove-required="{{ngRequired}]" align="center" ng-model="$parent.rangeToTimeModel" hour-step=" " minute-step=" " show-meridian="true" ng-change="setRangeToTime(rangeToTimeModel,toDatepickerModel)"></timepicker>' +
                        '<button class="btn btn-hkg" ng-click=setDateAndTime("rangeToClear")>Clear</button>&nbsp;&nbsp;' +
                        '<button class="btn btn-hkg" ng-click="setDateAndTime()">Close</button>' +
                        '</li>' +
                        '</div>' +
                        '</ul>' +
                        '<div ng-show="ngRequired && submitted && toForm.toModel.$invalid" class = "error,help-block ng-hide" >' +
                        '<span class="help-block" ng-show="toForm.toModel.$error.required && !(toForm.toModel.$error.date || toForm.toModel.$error.min || toForm.toModel.$error.max)">' +
                        '{{toLabel}}{{ entity +  "To date not entered" | translate }}</span>' +
                        '<span class="help-block" ng-show="toForm.toModel.$error.date">{{ entity +  "Invalid date format" | translate }}</span>' +
                        '<span class="help-block" ng-show="toForm.toModel.$error.min">{{ entity +  "Violates minimum constraint" | translate }}</span>' +
                        '<span class="help-block" ng-show="toForm.toModel.$error.max">{{ entity +  "Violates maximum constraint" | translate }}</span>' +
                        '</span>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>',
                scope: {"ngModel": "=", "fromModel": "=", "toModel": "=", "submitted": "=", "form": "=", "name": "=", "holidayList": "="},
                link: function(scope, element, attrs) {
                    scope.currentDate = new Date();
                    scope.datePickerFlag = {};
                    scope.datePickerFlag.dateTimePicker = false;
                    scope.datePickerFlag.fromIsOpen = false;
                    scope.datePickerFlag.toIsOpen = false;
                    if (attrs.format !== undefined) {
                        scope.directiveFormat = attrs.format;
                    } else {
                        scope.directiveFormat = "dd/MM/yyyy hh:mm a";
                    }
                    if (attrs.backgroundColor !== undefined)
                    {
                        scope.backgroundColor = attrs.backgroundColor;
                    }
                    if (attrs.hideCalendar !== undefined && attrs.hideCalendar !== null)
                    {
                        scope.hideCalendar =attrs.hideCalendar;
                    }else{
                        scope.hideCalendar = true;
                    }
                    console.log("hide :::::"+attrs.hideButton);
                    scope.$watch("ngModel", function(value) {
                        if (!value || value === null) {
                            scope.datepickerModel = undefined;
                            scope.timeModel = undefined;
//                            $(element).find("#ngModel").val('');
                        } else {
                            if (attrs.type === "time" || attrs.type === "datetime") {

                                if (!isNaN(scope.ngModel)) {
                                    scope.ngModel = getFormatedDate(value, attrs.format);
                                } else {
                                    scope.datepickerModel = new Date(getDateFromString(scope.ngModel, attrs.format));
                                    scope.timeModel = new Date(getDateFromString(scope.ngModel, attrs.format));
                                }
                            }
//                            $(element).find("#ngModel").val(getFormatedDate(value, attrs.format));
                        }
                    });
//                    scope.$watch("rangFromTimeModel",function(val){
//                        //alert("wat="+val);
//                    })
                    scope.$watch("fromModel", function(value) {
                        if (!value || value === null) {
                            scope.rangeFromDatepickerModel = undefined;
                            scope.rangFromTimeModel = undefined;
//                            $(element).find("#fromModel").val('');
                        } else {
                            if (attrs.type === "timerange" || attrs.type === "datetimerange") {
                                if (!isNaN(scope.fromModel)) {
                                    scope.fromModel = getFormatedDate(value, attrs.format);
                                } else {
                                    scope.rangeFromDatepickerModel = new Date(getDateFromString(scope.fromModel, attrs.format));
                                    scope.rangFromTimeModel = new Date(getDateFromString(scope.fromModel, attrs.format));
                                }
                            }
//                            $(element).find("#fromModel").val(getFormatedDate(value, attrs.format));
                        }
                    });
                    scope.$watch("toModel", function(value) {
                        if (!value || value === null) {
                            scope.rangeToTimeModel = undefined;
                            scope.toDatepickerModel = undefined;
//                            $(element).find("#toModel").val('');
                        } else {
                            if (attrs.type === "timerange" || attrs.type === "datetimerange") {
                                if (!isNaN(scope.toModel)) {
                                    scope.toModel = getFormatedDate(value, attrs.format);
                                } else {
                                    scope.rangeToTimeModel = new Date(getDateFromString(scope.toModel, attrs.format));
                                    scope.toDatepickerModel = new Date(getDateFromString(scope.toModel, attrs.format));
                                }
                            }
//                            $(element).find("#toModel").val(getFormatedDate(value, attrs.format));
                        }
                    });
                    if (angular.isDefined(attrs.backgroundColor))
                    {
                        console.log("inside")

                    }
                    if ((angular.isDefined(attrs.disableFlag) && attrs.disableFlag === 'true') || attrs.disableFlag === true) {
                        scope.isDisabled = true;
                    } else {
                        scope.isDisabled = false;
                    }
                    if (angular.isDefined(attrs.form)) {
                        scope.formName = attrs.form;
                    }
                    if (angular.isDefined(attrs.inputClass)) {
                        scope.inputclass = attrs.inputClass;
                    } else {
                        scope.inputclass = "col-md-3";
                    }
                    if (angular.isDefined(attrs.labelClass)) {
                        scope.labelclass = attrs.labelClass;
                    } else {
                        scope.labelclass = "col-md-1 col-lg-2 col-sm-4 control-label";
                    }
                    var getDateFromString = function(dateString, format) {
                        if (!angular.isDefined(format)) {
                            format = "dd/MM/yyyy hh:mm a";
                        }
                        return getDateFromFormat(dateString, format);
                    };
                    var getFormatedDate = function(date, format) {
                        if (format === undefined) {
                            format = "dd/MM/yyyy";
                        }

                        return $filter('date')(date, format);
                    };
                    var setCurrentTime = function(date) {
                        if (date !== undefined) {
                            scope.ngModel = getFormatedDate(date, 'shortTime'); //hours + ":" + minutes;

                        } else {
                            scope.ngModel = new Date();
                        }
                    };
                    //this is method append time to specified date .
                    var appendTimeIntoDate = function(time, date) {
                        if (angular.isDefined(time)) {
                            var selectedDate = new Date(time);
                            var hours = (new Date().getHours() < 10) ? '0' + new Date().getHours() : new Date().getHours();
                            var minutes = (new Date().getMinutes() < 10) ? '0' + new Date().getMinutes() : new Date().getMinutes();
                            selectedDate.setHours(hours);
                            selectedDate.setMinutes(minutes);
                        }
                        if (date !== undefined) {
                            //var hours = (time.getHours() < 10) ? '0' + time.getHours() : time.getHours();
                            //var minutes = (time.getMinutes() < 10) ? '0' + time.getMinutes() : time.getMinutes();
                            // alert(getFormatedDate(time, 'hh:m a'));
                            var s = getFormatedDate(time, 'H:m'),
                                    parts = s.match(/(\d+)\:(\d+)/),
                                    hours = parseInt(parts[1], 10),
                                    minutes = parseInt(parts[2], 10);

                            var updatedDateWithTime = new Date(date);
                            updatedDateWithTime.setHours(hours);
                            updatedDateWithTime.setMinutes(minutes);
                            return updatedDateWithTime;
                        }

                        return selectedDate;
                    };
                    if ((attrs.type && attrs.type === "date")) {
                       
                        scope.isDate = true;
                        if (attrs.format !== undefined) {
                            scope.dateFormat = attrs.format;
                        }
//                        if (attrs.defaultdate !== null && attrs.defaultdate !== undefined) {
//                            var today = new Date().getDate();
//                            scope.today = new Date();
//                            var defaultArray = [];
//                            defaultArray = attrs.defaultdate.toString().split("#@");
////                            console.log("ngModelll"+scope.ngModel);
//                            if (scope.ngModel === null || scope.ngModel === undefined) {
//                                if (defaultArray.length === 1)
//                                {
//                                    scope.ngModel = new Date();
//                                }
//                                else
//                                {
//                                    var nextDate;
//                                    if (defaultArray[1] === '+')
//                                    {
//                                        nextDate = parseInt(today) + parseInt(defaultArray[2]);
//                                    }
//                                    else
//                                    {
//                                        nextDate = parseInt(today) - parseInt(defaultArray[2]);
//                                    }
//                                    scope.today.setDate(nextDate);
//                                    scope.ngModel = angular.copy(scope.today);
//                                }
//                            }
//
//                        }
                        if (attrs.ngRequired && (attrs.ngRequired === 'true' || attrs.ngRequired === true)) {
                            scope.ngRequired = true;
                        } else {
                            scope.ngRequired = false;
                        }
                        if (angular.isDefined(attrs.label)) {
                            scope.dateLabel = attrs.label;
                        }
                        if (attrs.allow === "past") {
                            scope.onlyMaxDate = new Date();
                        } else if (attrs.allow === "future") {
                            scope.onlyMinDate = new Date();
                        } else if (attrs.allow === "onlyfuture") {
                            scope.onlyMinDate = new Date().setDate(new Date().getDate() + 1);
                        } else if (attrs.allow === "onlypast") {
                            scope.onlyMaxDate = new Date().setDate(new Date().getDate() - 1);
                        } else if (attrs.allow === "current") {
                            scope.onlyMaxDate = new Date();
                            scope.onlyMinDate = new Date();
                        }
                        else if (attrs.allow === "onlycurrentmonth")
                        {
                            var currentTime = new Date();
                            scope.onlyMinDate = new Date(2015, currentTime.getMonth(), 1); //one day next before month
                            scope.onlyMaxDate = new Date(2015, currentTime.getMonth() + 1, 0); // one day before next month

                        }
                        else {

                        }
                    } else if (attrs.type === "datetime") {

                        scope.isDateTime = true;
                        if (attrs.defaultdate !== null && attrs.defaultdate !== undefined) {
                            var today = new Date().getDate();
                            scope.today = new Date();
                            var defaultArray = [];
                            defaultArray = attrs.defaultdate.toString().split("#@");
//                            console.log("ngModelll"+scope.ngModel);
                            if (scope.ngModel === null || scope.ngModel === undefined) {
                                if (defaultArray.length === 1)
                                {
                                    scope.ngModel = new Date();
                                    scope.datepickerModel = new Date();
                                    scope.timeModel = new Date();
                                }
                                else
                                {
                                    var nextDate;
                                    if (defaultArray[1] === '+')
                                    {
                                        nextDate = parseInt(today) + parseInt(defaultArray[2]);
                                    }
                                    else
                                    {
                                        nextDate = parseInt(today) - parseInt(defaultArray[2]);
                                    }
                                    scope.today.setDate(nextDate);
                                    scope.ngModel = angular.copy(scope.today);
                                    scope.datepickerModel = angular.copy(scope.today);
                                    scope.timeModel = angular.copy(scope.today);
                                }
                            }

                        } else {
                            scope.datepickerModel = angular.copy(scope.ngModel);
                            scope.timeModel = angular.copy(scope.ngModel);
                        }
                        if (attrs.ngRequired && (attrs.ngRequired === 'true' || attrs.ngRequired === true)) {
                            scope.ngRequired = true;
                        } else {
                            scope.ngRequired = false;
                        }
                        if (angular.isDefined(attrs.label)) {
                            scope.dateTimeLabel = attrs.label;
                        }
                        if (attrs.allow === "past") {
                            scope.dateTimeMax = new Date();
                        } else if (attrs.allow === "future") {
                            scope.dateTimeMin = new Date();
                        } else if (attrs.allow === "onlyfuture") {
                            scope.dateTimeMin = new Date().setDate(new Date().getDate() + 1);
                        } else if (attrs.allow === "onlypast") {
                            scope.dateTimeMax = new Date().setDate(new Date().getDate() - 1);
                        } else if (attrs.allow === "current") {
                            scope.dateTimeMax = new Date();
                            scope.dateTimeMin = new Date();
                        }
                        else if (attrs.allow === "onlycurrentmonth")
                        {
                            var currentTime = new Date();
                            scope.onlyMinDate = new Date(2015, currentTime.getMonth(), 1); //one day next before month
                            scope.onlyMaxDate = new Date(2015, currentTime.getMonth() + 1, 0); // one day before next month

                        }
                        else {

                        }

                    } else if (attrs.type === "time") {
                        scope.isTime = true;
                        if (attrs.ngRequired && (attrs.ngRequired === 'true' || attrs.ngRequired === true)) {
                            scope.ngRequired = true;
                        } else {
                            scope.ngRequired = false;
                        }
                        if (angular.isDefined(attrs.defaulttime) && attrs.defaulttime === "true") {
                            scope.ngModel = getFormatedDate(new Date, 'shortTime');
                        }

                    } else if (attrs.type === "daterange") {
                        console.log("inside date range")
                        scope.isDateRange = true;
                        scope.name = attrs.name;
                        if (angular.isDefined(attrs.formName)) {
                            //  scope.formName=attrs.formName;
                        }
                        if (attrs.format !== undefined) {
                            scope.dateRangeFormat = attrs.format;
                        }
                        if (attrs.fromLabel !== undefined && attrs.fromLabel.length > 0) {
                            scope.fromLabel = attrs.fromLabel;
                        }
                        if (attrs.toLabel !== undefined && attrs.toLabel.length > 0) {
                            scope.toLabel = attrs.toLabel;
                        }
                        if (attrs.defaultdate !== undefined && attrs.defaultdate === "true") {
                            scope.fromModel = new Date();
                            scope.toModel = new Date(); //getFormatedDate(new Date(), attrs.format);
                        }
                        if (attrs.ngRequired && (attrs.ngRequired === 'true' || attrs.ngRequired === true)) {
                            scope.ngRequired = true;
                        } else {
                            scope.ngRequired = false;
                        }
                        if (attrs.allow === "past") {
                            scope.fromMaxDate = new Date();
                            scope.toMaxDate = new Date();
                        } else if (attrs.allow === "future") {
                            scope.fromMinDate = new Date();
                            scope.toMinDate = new Date();
                        } else if (attrs.allow === "onlyfuture") {
                            scope.fromMinDate = new Date().setDate(new Date().getDate() + 1);
                            scope.toMinDate = new Date().setDate(new Date().getDate() + 1);
                        } else if (attrs.allow === "onlypast") {
                            scope.fromMaxDate = new Date().setDate(new Date().getDate() - 1);
                            scope.toMaxDate = new Date().setDate(new Date().getDate() - 1);
                        } else if (attrs.allow === "current") {
                            scope.fromMaxDate = new Date();
                            scope.toMaxDate = new Date();
                            scope.fromMinDate = new Date();
                            scope.toMinDate = new Date();
                        }
                        else if (attrs.allow === "onlycurrentmonth")
                        {
                            var currentTime = new Date();
                            scope.onlyMinDate = new Date(2015, currentTime.getMonth(), 1); //one day next before month
                            scope.onlyMaxDate = new Date(2015, currentTime.getMonth() + 1, 0); // one day before next month

                        }
                        else {

                        }
                    } else if (attrs.type === "timerange") {
                        scope.isTimeRange = true;
                        if (attrs.ngRequired && (attrs.ngRequired === 'true' || attrs.ngRequired === true)) {
                            scope.ngRequired = true;
                        } else {
                            scope.ngRequired = false;
                        }
                        if (attrs.fromLabel !== undefined && attrs.fromLabel.length > 0) {
                            scope.fromLabel = attrs.fromLabel;
                        }
                        if (attrs.toLabel !== undefined && attrs.toLabel.length > 0) {
                            scope.toLabel = attrs.toLabel;
                        }
                        if (attrs.defaulttime !== undefined && attrs.defaulttime === "true") {
                            scope.fromModel = getFormatedDate(new Date(), "shortTime");
                            scope.toModel = getFormatedDate(new Date(), "shortTime");
                        }
                    } else if (attrs.type === "datetimerange") {
                        scope.isDateTimeRange = true;
                        if (attrs.ngRequired && (attrs.ngRequired === 'true' || attrs.ngRequired === true)) {
                            scope.ngRequired = true;
                        } else {
                            scope.ngRequired = false;
                        }
                        if (attrs.fromLabel !== undefined && attrs.fromLabel.length > 0) {
                            scope.fromLabel = attrs.fromLabel;
                        }
                        if (attrs.toLabel !== undefined && attrs.toLabel.length > 0) {
                            scope.toLabel = attrs.toLabel;
                        }
                        if (attrs.defaultdate !== undefined && attrs.defaultdate === "true" && !scope.fromModel) {
                            var someDate = new Date();

                            if (angular.isDefined(attrs.adddaystodefaultdate)) {
                                var numberOfDaysToAdd = parseInt(attrs.adddaystodefaultdate);
                                someDate.setDate(someDate.getDate() + numberOfDaysToAdd);
                            }
                            if (angular.isDefined(attrs.defaultstarttime)) {
                                // var d = new Date(),
                                var s = attrs.defaultstarttime,
                                        parts = s.match(/(\d+)\.(\d+) (\w+)/),
                                        hours = /am/i.test(parts[3]) ? parseInt(parts[1], 10) : parseInt(parts[1], 10) + 12,
                                        minutes = parseInt(parts[2], 10);

                                someDate.setHours(hours);
                                someDate.setMinutes(minutes);
                                scope.rangFromTimeModel = new Date(someDate);//new Date(", 2008 00:00:00");
                                scope.rangeFromDatepickerModel = new Date(someDate);

                            }
                            scope.fromModel = getFormatedDate(someDate, attrs.format);
                            if (angular.isDefined(attrs.defaultendtime)) {
                                // var d = new Date(),
                                //  someDate = new Date();
                                var s = attrs.defaultendtime,
                                        parts = s.match(/(\d+)\.(\d+) (\w+)/),
                                        hours = /am/i.test(parts[3]) ? parseInt(parts[1], 10) : parseInt(parts[1], 10) + 12,
                                        minutes = parseInt(parts[2], 10);

                                someDate.setHours(hours);
                                someDate.setMinutes(minutes);
                                scope.rangeToTimeModel = someDate;
                                scope.toDatepickerModel = someDate;

                            }
                            scope.toModel = getFormatedDate(someDate, attrs.format);
                        } else {

                            if (scope.fromModel) {
                                scope.rangFromTimeModel = new Date(scope.fromModel);
                                scope.rangeFromDatepickerModel = new Date(scope.fromModel);
                                scope.fromModel = getFormatedDate(scope.fromModel, attrs.format);
                                // scope.minDate = scope.rangeFromDatepickerModel;
                            }
                            if (scope.toModel) {
                                scope.rangeToTimeModel = new Date(scope.toModel);
                                scope.toModel = getFormatedDate(scope.toModel, attrs.format);
                                scope.toDatepickerModel = new Date(scope.rangeToTimeModel);
                            }

                        }
                        if (attrs.allow === "past" && !scope.toDatepickerModel) {
                            scope.maxDate = new Date();
                            scope.toDatepickerModel = new Date();
                        } else if (attrs.allow === "future" && !scope.rangeFromDatepickerModel) {
                            scope.minDate = new Date();
                            scope.rangeFromDatepickerModel = scope.minDate;
                        } else if (attrs.allow === "onlyfuture" && !scope.rangeFromDatepickerModel) {
                            scope.minDate = new Date().setDate(new Date().getDate() + 1);
                            scope.rangeFromDatepickerModel = scope.minDate;
                        } else if (attrs.allow === "onlypast" && !scope.toDatepickerModel) {
                            scope.maxDate = new Date().setDate(new Date().getDate() - 1);
                            scope.toDatepickerModel = new Date().setDate(new Date().getDate() - 1);
                        } else if (attrs.allow === "current") {
                            scope.maxDate = new Date();
                            scope.minDate = new Date();
                        } else {

                        }
                    }

                    scope.initDateTimePopup = function() {
                        $(element).bind('click', function(e) {
                            e = e || window.event;
                            var target = e.target;

                            //$(event.target).parent();
//                            $('#ui-datepicker-div').hide();
                            //dateSelection();
                            // //alert("node="+target.nodeName +":id="+target.id);
//                            if ((target.nodeName === "INPUT" && !target.id === "") ||
//                                    (target.nodeName === "I" && target.id === "toI") ||
//                                    (target.nodeName === "I" && target.id === "fromI") ||
//                                    (target.nodeName === "SPAN" && target.id === "frmDt") ||
//                                    (target.nodeName === "SPAN" && target.id === "toDt") ||
//                                    (target.nodeName === "I" && target.id === "dateTimeI") ||
//                                    (target.nodeName === "SPAN" && target.id === "dateTimeSpan") ||
//                                    (target.nodeName === "SPAN" && target.id === "fromTimeSpan") ||
//                                    (target.nodeName === "I" && target.id === "fromTimeI") ||
//                                    (target.nodeName === "SPAN" && target.id === "toTimeSpan") ||
//                                    (target.nodeName === "I" && target.id === "toTimeI") ||
//                                    (target.nodeName === "SPAN" && target.id === "timeSpan") ||
//                                    (target.nodeName === "INPUT" && target.id === "fromModel") ||
//                                    (target.nodeName === "INPUT" && target.id === "toModel") ||
//                                    (target.nodeName === "INPUT" && target.id === "ngModel")// ||
//                                    (target.nodeName === "I" && target.id === "timeI")
//                                    ) {
//                                e.stopPropagation();
//                            } else {
//
//                            }
                        });

                        $(element).find('.dropdown.keep-open').on({
                            "shown.bs.dropdown": function() {
                                this.closable = false;
                            },
                            "click": function(e) {
                                if (scope.closePopup) {
                                    this.closable = true;
                                    scope.closePopup = false;
                                } else {
                                    this.closable = false;
                                }

                            },
                            "hide.bs.dropdown": function() {
                                return this.closable;
                            }
                        });
                    };
                    scope.setDateAndTime = function(clear, model) {
                        switch (clear) {
                            case "datetime":
                                scope.ngModel = undefined;
                                //   scope.timeModel = undefined;
                                scope.datepickerModel = undefined;
                                scope.timeModel = undefined;
                                break;
                            case "fromTime":
                                scope.fromTimeModel = new Date();
                                scope.fromModel = undefined;
                                break;
                            case "toTime":
                                scope.toTimeModel = new Date();
                                scope.toModel = undefined;
                            case "rangeFromClear":
                                scope.rangFromTimeModel = new Date();
                                scope.fromModel = undefined;
                                break;
                            case "rangeToClear":
                                scope.rangeToTimeModel = new Date();
                                scope.toModel = undefined;

                        }
                        scope.closePopup = true;
                        // $(element).find($(this)).data('closable', true);
//                         $(element).find($('.dropdown.keep-open')).data('closable');
                    };

                    function clickEvent(e) {
                        e = e || window.event;
                        var target = e.target;
                        //alert("div");
                        if (!$(element).is(e.target)
                                && $(element).has(e.target).length === 0
                                && $('.open').has(e.target).length === 0
                                && document.contains(target)) {
                            scope.datePickerFlag.dateTimePicker = false;
                            scope.datePickerFlag.fromIsOpen = false;
                            scope.datePickerFlag.toIsOpen = false;
                            scope.$digest();
                            $(".open").removeClass("open");
                        } else {
                        }
                    }
                    $("body").bind('click', clickEvent);
                    scope.$on('$destroy', function() {
                        $("body").unbind('click', clickEvent);
                    });
                    scope.setOnlyDate = function(date) {
                        scope.ngModel = date;
                    };

                    //For Range Only Date
                    scope.setFromDate = function(date) {
                        scope.fromModel = date;
                        scope.toMinDate = date;
                    };
                    scope.setToDate = function(date) {
                        scope.toModel = date;
                        scope.fromMaxDate = date;
                    };
                    scope.setFromTime = function(time) {
                        //alert("setFrom Tme");
                        scope.fromModel = getFormatedDate(time, "shortTime");
                    };

                    scope.setToTime = function(time) {
                        //alert("set to time");
                        scope.toModel = getFormatedDate(time, "shortTime");
                    };
                    //For Range Date and time Both
                    scope.setRangeFromDate = function(date, time) {
                        //alert("in set range from date");
                        var selectedDate;
                        if (angular.isDefined(time)) {
                            //alert("append time"+time);
                            selectedDate = appendTimeIntoDate(time, date);
                        } else {
                            var selectedDate = appendTimeIntoDate(date);
                        }
                        scope.fromModel = getFormatedDate(selectedDate, attrs.format);
                        scope.rangeFromDatepickerModel = selectedDate;
                    };
                    scope.setRangeToDate = function(date, time) {
                        var selectedDate;
                        if (angular.isDefined(time)) {
                            selectedDate = appendTimeIntoDate(time, date);
                        } else {
                            var selectedDate = appendTimeIntoDate(date);
                        }
                        scope.toModel = getFormatedDate(selectedDate, attrs.format);
                        scope.rangToTimeModel = selectedDate;
                        scope.toDatepickerModel = selectedDate;
                    }
                    scope.setRangeFromTime = function(time, date) {
                        if (!date) {
                            date = new Date();
                        }
                        var selectedDate = appendTimeIntoDate(time, date);
                        scope.fromModel = getFormatedDate(selectedDate, attrs.format);
                        scope.rangeFromDatepickerModel = selectedDate;// getFormatedDate(selectedDate, attrs.format);
                    };
                    scope.setRangeToTime = function(time, date) {
                        if (!date) {
                            date = new Date();
                        }
                        var selectedDate = appendTimeIntoDate(time, date);
                        scope.toModel = getFormatedDate(selectedDate, attrs.format);
                        scope.toDatepickerModel = selectedDate;
                    };
                    scope.dtPicker = [];
                    scope.open = function(event, opened) {
                        scope.dtPicker = [];
                        //scope.format = attrs.format;
                        event.preventDefault();
                        event.stopPropagation();
                        scope.dtPicker[opened] = true;

                        $(".open").removeClass("open");
                    };
                    scope.setTime = function(timevalue, datevalue) {
                        if (!datevalue) {
                            datevalue = new Date();
                        }
                        if (datevalue !== undefined) {
                            var dateWithTime = new Date(timevalue);
                            var hours = (dateWithTime.getHours() < 10) ? '0' + dateWithTime.getHours() : dateWithTime.getHours();
                            var minutes = (dateWithTime.getMinutes() < 10) ? '0' + dateWithTime.getMinutes() : dateWithTime.getMinutes();
                            var updatedDateWithTime = new Date(datevalue);
                            updatedDateWithTime.setHours(hours);
                            updatedDateWithTime.setMinutes(minutes);
                            scope.ngModel = getFormatedDate(updatedDateWithTime, attrs.format);
                        } else {
                            setCurrentTime(timevalue);
                        }

                    };


                    scope.daysInMonth = function(month, year) {
                        return new Date(year, month, 0).getDate();
                    };

                    scope.setDate = function(date, time) {
                        var selectedDate = appendTimeIntoDate(date);
                        if (time) {
                            var hours = (time.getHours() < 10) ? '0' + time.getHours() : time.getHours();
                            var minutes = (time.getMinutes() < 10) ? '0' + time.getMinutes() : time.getMinutes();
                            selectedDate.setHours(hours);
                            selectedDate.setMinutes(minutes);
                        }
                        scope.ngModel = getFormatedDate(selectedDate, attrs.format);
                        scope.timeModel = selectedDate;
                    };
                    scope.hstep = 1;
                    scope.mstep = 30;
                    scope.ismeridian = false;
                    //Check if a date is a holiday or sunday.
                    scope.disabled = function(date, mode) {

                        var isAHoliday = false;

                        var month = date.getMonth() + 1; //months from 1-12
                        var day = date.getDate();
                        var year = date.getFullYear();
                        if (scope.holidayList !== null && scope.holidayList !== undefined) {
                            for (var j = 0; j < scope.holidayList.length; j++) {
                                var month1 = scope.holidayList[j].getMonth() + 1; //months from 1-12
                                var day1 = scope.holidayList[j].getDate();
                                var year1 = scope.holidayList[j].getFullYear();
                                if (month === month1 && day === day1 && year === year1) {
                                    isAHoliday = true;
                                    break;
                                }
                            }
                        }


                        return (mode === 'day' && (date.getDay() === 0 || isAHoliday));
                    };
                }

            };
        }]);

});
