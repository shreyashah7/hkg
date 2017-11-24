define(["angular","dateValidate"],function(){globalProvider.compileProvider.directive("agDatePickerCustom",["$compile","$filter","orderByFilter","$document",function(b,c,a,d){return{restrict:"E",template:'<div><div ng-if="isDateTime" ng-init="initDateTimePopup()"><div  class="dropdown keep-open"><div class="input-group" ><input date-validate validate-format="{{directiveFormat}}" placeholder="{{directiveFormat}}" min="dateTimeMin" max="dateTimeMax" type="text" id="ngModel" name="ngModel" class="form-control" ng-model="$parent.$parent.ngModel"><span class="input-group-btn" id="dateTimeSpan" data-toggle="dropdown"  ng-click="datePickerFlag.dateTimePicker = true;"><button ng-disabled="isDisabled" class="btn btn-default" style="border-bottom-left-radius: 0;border-top-left-radius: 0;"><i class="glyphicon glyphicon-calendar" id="dateTimeI"/></button></span><ul class="dropdown-menu"><li><datepicker is-open="datePickerFlag.dateTimePicker" id="dd" ng-model="$parent.datepickerModel"  min="dateTimeMin" max="dateTimeMax" date-disabled="disabled(date,mode)" ng-change="setDate(datepickerModel,timeModel)"></datepicker></li><li class="text-center"><timepicker remove-required="{{ngRequired}}" align="center" ng-model="$parent.timeModel" hour-step="hstep" minute-step="mstep" show-meridian="true" ng-change="setTime(timeModel,datepickerModel)"></timepicker><button class="btn btn-hkg" ng-click=setDateAndTime("datetime",timeModel)>Clear</button>&nbsp;&nbsp;<button class="btn btn-hkg" ng-click="setDateAndTime()">Close</button></li></ul></div></div></div><div ng-if="isDate"><div ng-class="{\'input-group\': hideCalendar}"><input type="text" style="background-color:{{backgroundColor}}" id="ngModel" is-open="$parent.dtPicker.dt_open"  name="ngModel" class="form-control" datepicker-popup="{{dateFormat}}"  ng-model="ngModel" min="onlyMinDate" max="onlyMaxDate" date-disabled="disabled(date,mode)" ng-change=setOnlyDate(ngModel) placeholder="{{dateFormat}}"><span class="input-group-btn" ng-if="hideCalendar" ng-click=(isDisabled||open($event,"dt_open"))><button ng-disabled="isDisabled" class="btn btn-default"><i class="glyphicon glyphicon-calendar" ></i></button></span></div></div><div ng-if="isTime" ng-init="initDateTimePopup()"><div  class="dropdown keep-open"><a style="cursor:default;text-decoration: none"  data-toggle="dropdown"><div class="input-group" ><input type="text" id="ngModel"  name="ngModel" class="form-control"   ng-model="ngModel"  ><span id="timeSpan" class="input-group-addon"><i id="timeI" class="glyphicon glyphicon-time"></i></span></div></a><ul class="dropdown-menu"><li class="text-center"><timepicker  remove-required="{{ngRequired}}" align="center" ng-model="$parent.timeModel" defaultTime="false" hour-step="hstep" minute-step="mstep" show-meridian="true" ng-change=setTime(timeModel) ></timepicker><button class="btn btn-hkg" ng-click=setDateAndTime("datetime")>Clear</button></li></ul></div>\n                    </div><div ng-if="isDateRange" ng-init="initDateTimePopup()"><div  class="row" ><div ng-class="inputclass"><div ng-form="fromForm"><div ng-class="{\'has-error\': fromForm.fromModel.$invalid}"><div class="input-group" ><input type="text" id="fromModel" is-open="$parent.dtPicker.open_from"  name="fromModel" class="form-control" placeholder="{{dateRangeFormat}}" datepicker-popup="{{dateRangeFormat}}"  ng-model="$parent.fromModel" min="fromMinDate" max="fromMaxDate" ng-change="setFromDate(fromModel)" ng-required="ngRequired"><span class="input-group-btn" ng-click=(isDisabled||open($event,"open_from"))><button ng-disabled="isDisabled" class="btn btn-default"><i class="glyphicon glyphicon-calendar"></i></button></span></div><div ng-show="fromForm.fromModel.$invalid" class = "error,help-block ng-hide" ><span class="help-block" ng-show="fromForm.fromModel.$error.required && !(fromForm.fromModel.$error.date || fromForm.fromModel.$error.min || fromForm.fromModel.$error.max)">{{fromLabel}}{{ entity +  " not entered" | translate }}</span><span class="help-block" ng-show="fromForm.fromModel.$error.date">{{ entity +  "Invalid date format" | translate }}</span><span class="help-block" ng-show="fromForm.fromModel.$error.min">{{ entity +  "Violates minimum constraint" | translate }}</span><span class="help-block" ng-show="fromForm.fromModel.$error.max">{{ entity +  "Violates maximum constraint" | translate }}</span></span></div></div></div></div> <label ng-class="labelclass">{{toLabel}}</label><div ng-class="inputclass"><div ng-form="toForm"><div ng-class="{\'has-error\': toForm.toModel.$invalid}"><div class="input-group" ><input type="text" id="toModel" is-open="$parent.dtPicker.open_to"  name="toModel" class="form-control" min="toMinDate" placeholder="{{dateRangeFormat}}" max="toMaxDate" datepicker-popup="{{dateRangeFormat}}"  ng-model="$parent.toModel" ng-change="setToDate(toModel)" ng-required="ngRequired"><span class="input-group-btn" ng-click=(isDisabled||open($event,"open_to"))><button class="btn btn-default" style="border-bottom-left-radius: 0;border-top-left-radius: 0;"><i class="glyphicon glyphicon-calendar"></i></button></span></div><div ng-show="toForm.toModel.$invalid" class = "error,help-block ng-hide" ><span class="help-block" ng-show="toForm.toModel.$error.required && !(toForm.toModel.$error.date || toForm.toModel.$error.min || toForm.toModel.$error.max)">{{fromLabel}}{{ entity +  " not entered" | translate }}</span><span class="help-block" ng-show="toForm.toModel.$error.date">{{ entity +  "Invalid date format" | translate }}</span><span class="help-block" ng-show="toForm.toModel.$error.min">{{ entity +  "Violates minimum constraint" | translate }}</span><span class="help-block" ng-show="toForm.toModel.$error.max">{{ entity +  "Violates maximum constraint" | translate }}</span></span></div></div></div></div></div></div><div ng-if="isTimeRange" ng-init="initDateTimePopup()"><div class="row"><div ng-class="inputclass"><div  class="dropdown keep-open"><a style="cursor:default;text-decoration: none" data-toggle="dropdown"><div ng-if="ngRequired" ng-form="fromForm"><div ng-class="{\'has-error\': submitted && fromForm.fromModel.$invalid}"><div class="input-group" ><input type="text" id="fromModel"  name="fromModel" class="form-control"   ng-model="fromModel"  required><span id="fromTimeSpan" class="input-group-addon"><i id="fromTimeI" class="glyphicon glyphicon-time"></i></span></div><div ng-show="submitted && fromForm.fromModel.$invalid" class = "error,help-block ng-hide" ><span class = "help-block" ng-show = "fromForm.fromModel.$error.required" > {{fromLabel}}{{ entity +  " not entered" | translate }}</span></div></div></div><div ng-if="!ngRequired"><div class="input-group" ><input type="text" id="fromModel"  name="fromModel" class="form-control"   ng-model="fromModel"><span id="fromTimeSpan" class="input-group-addon"><i id="fromTimeI" class="glyphicon glyphicon-time"></i></span></div></div></a><ul class="dropdown-menu"><li class="text-center"><timepicker remove-required="{{ngRequired}}" align="center" ng-model="$parent.fromTimeModel" defaultTime="false" hour-step="hstep" minute-step="mstep" show-meridian="true" ng-change=setFromTime(fromTimeModel) ></timepicker><button class="btn btn-hkg" ng-click=setDateAndTime("fromTime")>Clear</button></li></ul></div></div> <label ng-class="labelclass">{{toLabel}}</label><div ng-class="inputclass"><div  class="dropdown keep-open"><a style="cursor:default;text-decoration: none"  data-toggle="dropdown"><div ng-if="ngRequired" ng-form="toForm"><div ng-class="{\'has-error\': submitted && toForm.toModel.$invalid}"><div class="input-group" ><input type="text" id="toModel"  name="toModel" class="form-control"   ng-model="toModel"  required><span id="toTimeSpan" class="input-group-addon"><i id="toTimeI" class="glyphicon glyphicon-time"></i></span></div><div ng-show="submitted && toForm.toModel.$invalid" class = "error,help-block ng-hide" ><span class = "help-block" ng-show = "toForm.toModel.$error.required" > {{toLabel}}{{ entity +  " not entered" | translate }}</span></div></div></div><div ng-if="!ngRequired"><div class="input-group" ><input type="text" id="toModel"  name="toModel" class="form-control"   ng-model="toModel" ><span id="toTimeSpan" class="input-group-addon"><i id="toTimeI" class="glyphicon glyphicon-time"></i></span></div></div></a><ul class="dropdown-menu"><li class="text-center"><div><timepicker remove-required="{{ngRequired}}" align="center" ng-model="$parent.toTimeModel" defaultTime="false" hour-step="hstep" minute-step="mstep" show-meridian="true" ng-change=setToTime(toTimeModel) ></timepicker><button class="btn btn-hkg" ng-click=setDateAndTime("toTime")>Clear</button></div></li></ul></div></div></div></div><div ng-if="isDateTimeRange" ng-init="initDateTimePopup()"><div  class="row" ><div ng-class="inputclass"><div  class="dropdown keep-open"><div ng-form="fromForm"><div ng-class="{\'has-error\': submitted && fromForm.fromModel.$invalid}"><div class="input-group" ><input date-validate validate-format="{{directiveFormat}}" placeholder="{{directiveFormat}}" min="minDate" max="toDatepickerModel" type="text" id="fromModel" name="fromModel" class="form-control"   ng-model="$parent.$parent.fromModel"  ng-required="ngRequired"><span id="frmDt" class="input-group-btn" data-toggle="dropdown" ng-click = "datePickerFlag.fromIsOpen = true;"><button class="btn btn-default" style="border-bottom-left-radius: 0;border-top-left-radius: 0;"><i id="fromI" class="glyphicon glyphicon-calendar"></i></button></span><ul class="dropdown-menu" role="menu" aria-labelledby="dLabel"><li><datepicker is-open="datePickerFlag.fromIsOpen" id="rangeFromDatepickerModel" ng-model="$parent.rangeFromDatepickerModel" min="minDate" max="toDatepickerModel" show-weeks="true" class="well well-sm" ng-change="setRangeFromDate(rangeFromDatepickerModel,rangFromTimeModel)"></datepicker></li> <li class="text-center"><timepicker remove-required="{{ngRequired}}" align="center" ng-model="$parent.rangFromTimeModel" hour-step=" " minute-step=" " show-meridian="true" ng-change="setRangeFromTime(rangFromTimeModel,rangeFromDatepickerModel)"></timepicker><button class="btn btn-hkg" ng-click=setDateAndTime("rangeFromClear")>Clear</button>&nbsp;&nbsp;<button class="btn btn-hkg" ng-click="setDateAndTime()">Close</button></li></div></ul><div ng-show="ngRequired && submitted && fromForm.fromModel.$invalid" class = "error,help-block ng-hide" ><span class="help-block" ng-show="fromForm.fromModel.$error.required && !(fromForm.fromModel.$error.date || fromForm.fromModel.$error.min || fromForm.fromModel.$error.max)">{{fromLabel}}{{ entity +  "From date not entered" | translate }}</span><span class="help-block" ng-show="fromForm.fromModel.$error.date">{{ entity +  "Invalid date format" | translate }}</span><span class="help-block" ng-show="fromForm.fromModel.$error.min">{{ entity +  "Violates minimum constraint" | translate }}</span><span class="help-block" ng-show="fromForm.fromModel.$error.max">{{ entity +  "Violates maximum constraint" | translate }}</span></span></div></div></div></div></div><label ng-class="labelclass">To</label> <div ng-class="inputclass"><div  class="dropdown keep-open" id="toDDD"><div ng-form="toForm"><div ng-class="{\'has-error\': submitted && toForm.toModel.$invalid}"><div class="input-group" ><input date-validate validate-format="{{directiveFormat}}" placeholder="{{directiveFormat}}" min="rangeFromDatepickerModel" max="maxDate" type="text" id="toModel" name="toModel" class="form-control"   ng-model="$parent.$parent.toModel"  ng-required="ngRequired"><span id="toDt" class="input-group-btn" data-toggle="dropdown" ng-click = "datePickerFlag.toIsOpen = true;"><button class="btn btn-default" style="border-bottom-left-radius: 0;border-top-left-radius: 0;"><i id="toI" class="glyphicon glyphicon-calendar" ></i></button></span><ul class="dropdown-menu" role="menu" aria-labelledby="dLabel" ><li><datepicker is-open="datePickerFlag.toIsOpen" id="toDatepickerModel" ng-model="$parent.toDatepickerModel" min="rangeFromDatepickerModel" max="maxDate" show-weeks="true" class="well well-sm" ng-change="setRangeToDate(toDatepickerModel,rangeToTimeModel)"></datepicker></li> <li class="text-center"> <timepicker remove-required="{{ngRequired}]" align="center" ng-model="$parent.rangeToTimeModel" hour-step=" " minute-step=" " show-meridian="true" ng-change="setRangeToTime(rangeToTimeModel,toDatepickerModel)"></timepicker><button class="btn btn-hkg" ng-click=setDateAndTime("rangeToClear")>Clear</button>&nbsp;&nbsp;<button class="btn btn-hkg" ng-click="setDateAndTime()">Close</button></li></div></ul><div ng-show="ngRequired && submitted && toForm.toModel.$invalid" class = "error,help-block ng-hide" ><span class="help-block" ng-show="toForm.toModel.$error.required && !(toForm.toModel.$error.date || toForm.toModel.$error.min || toForm.toModel.$error.max)">{{toLabel}}{{ entity +  "To date not entered" | translate }}</span><span class="help-block" ng-show="toForm.toModel.$error.date">{{ entity +  "Invalid date format" | translate }}</span><span class="help-block" ng-show="toForm.toModel.$error.min">{{ entity +  "Violates minimum constraint" | translate }}</span><span class="help-block" ng-show="toForm.toModel.$error.max">{{ entity +  "Violates maximum constraint" | translate }}</span></span></div></div></div></div></div></div></div></div>',scope:{ngModel:"=",fromModel:"=",toModel:"=",submitted:"=",form:"=",name:"=",holidayList:"="},link:function(e,i,q){e.currentDate=new Date();
e.datePickerFlag={};
e.datePickerFlag.dateTimePicker=false;
e.datePickerFlag.fromIsOpen=false;
e.datePickerFlag.toIsOpen=false;
if(q.format!==undefined){e.directiveFormat=q.format
}else{e.directiveFormat="dd/MM/yyyy hh:mm a"
}if(q.backgroundColor!==undefined){e.backgroundColor=q.backgroundColor
}if(q.hideCalendar!==undefined&&q.hideCalendar!==null){e.hideCalendar=q.hideCalendar
}else{e.hideCalendar=true
}console.log("hide :::::"+q.hideButton);
e.$watch("ngModel",function(s){if(!s||s===null){e.datepickerModel=undefined;
e.timeModel=undefined
}else{if(q.type==="time"||q.type==="datetime"){if(!isNaN(e.ngModel)){e.ngModel=n(s,q.format)
}else{e.datepickerModel=new Date(p(e.ngModel,q.format));
e.timeModel=new Date(p(e.ngModel,q.format))
}}}});
e.$watch("fromModel",function(s){if(!s||s===null){e.rangeFromDatepickerModel=undefined;
e.rangFromTimeModel=undefined
}else{if(q.type==="timerange"||q.type==="datetimerange"){if(!isNaN(e.fromModel)){e.fromModel=n(s,q.format)
}else{e.rangeFromDatepickerModel=new Date(p(e.fromModel,q.format));
e.rangFromTimeModel=new Date(p(e.fromModel,q.format))
}}}});
e.$watch("toModel",function(s){if(!s||s===null){e.rangeToTimeModel=undefined;
e.toDatepickerModel=undefined
}else{if(q.type==="timerange"||q.type==="datetimerange"){if(!isNaN(e.toModel)){e.toModel=n(s,q.format)
}else{e.rangeToTimeModel=new Date(p(e.toModel,q.format));
e.toDatepickerModel=new Date(p(e.toModel,q.format))
}}}});
if(angular.isDefined(q.backgroundColor)){console.log("inside")
}if((angular.isDefined(q.disableFlag)&&q.disableFlag==="true")||q.disableFlag===true){e.isDisabled=true
}else{e.isDisabled=false
}if(angular.isDefined(q.form)){e.formName=q.form
}if(angular.isDefined(q.inputClass)){e.inputclass=q.inputClass
}else{e.inputclass="col-md-3"
}if(angular.isDefined(q.labelClass)){e.labelclass=q.labelClass
}else{e.labelclass="col-md-1 col-lg-2 col-sm-4 control-label"
}var p=function(s,x){if(!angular.isDefined(x)){x="dd/MM/yyyy hh:mm a"
}return getDateFromFormat(s,x)
};
var n=function(s,x){if(x===undefined){x="dd/MM/yyyy"
}return c("date")(s,x)
};
var m=function(s){if(s!==undefined){e.ngModel=n(s,"shortTime")
}else{e.ngModel=new Date()
}};
var l=function(E,z){if(angular.isDefined(E)){var B=new Date(E);
var x=(new Date().getHours()<10)?"0"+new Date().getHours():new Date().getHours();
var A=(new Date().getMinutes()<10)?"0"+new Date().getMinutes():new Date().getMinutes();
B.setHours(x);
B.setMinutes(A)
}if(z!==undefined){var C=n(E,"H:m"),D=C.match(/(\d+)\:(\d+)/),x=parseInt(D[1],10),A=parseInt(D[2],10);
var y=new Date(z);
y.setHours(x);
y.setMinutes(A);
return y
}return B
};
if((q.type&&q.type==="date")){e.isDate=true;
if(q.format!==undefined){e.dateFormat=q.format
}if(q.ngRequired&&(q.ngRequired==="true"||q.ngRequired===true)){e.ngRequired=true
}else{e.ngRequired=false
}if(angular.isDefined(q.label)){e.dateLabel=q.label
}if(q.allow==="past"){e.onlyMaxDate=new Date()
}else{if(q.allow==="future"){e.onlyMinDate=new Date()
}else{if(q.allow==="onlyfuture"){e.onlyMinDate=new Date().setDate(new Date().getDate()+1)
}else{if(q.allow==="onlypast"){e.onlyMaxDate=new Date().setDate(new Date().getDate()-1)
}else{if(q.allow==="current"){e.onlyMaxDate=new Date();
e.onlyMinDate=new Date()
}else{if(q.allow==="onlycurrentmonth"){var v=new Date();
e.onlyMinDate=new Date(2015,v.getMonth(),1);
e.onlyMaxDate=new Date(2015,v.getMonth()+1,0)
}else{}}}}}}}else{if(q.type==="datetime"){e.isDateTime=true;
if(q.defaultdate!==null&&q.defaultdate!==undefined){var w=new Date().getDate();
e.today=new Date();
var j=[];
j=q.defaultdate.toString().split("#@");
if(e.ngModel===null||e.ngModel===undefined){if(j.length===1){e.ngModel=new Date();
e.datepickerModel=new Date();
e.timeModel=new Date()
}else{var k;
if(j[1]==="+"){k=parseInt(w)+parseInt(j[2])
}else{k=parseInt(w)-parseInt(j[2])
}e.today.setDate(k);
e.ngModel=angular.copy(e.today);
e.datepickerModel=angular.copy(e.today);
e.timeModel=angular.copy(e.today)
}}}else{e.datepickerModel=angular.copy(e.ngModel);
e.timeModel=angular.copy(e.ngModel)
}if(q.ngRequired&&(q.ngRequired==="true"||q.ngRequired===true)){e.ngRequired=true
}else{e.ngRequired=false
}if(angular.isDefined(q.label)){e.dateTimeLabel=q.label
}if(q.allow==="past"){e.dateTimeMax=new Date()
}else{if(q.allow==="future"){e.dateTimeMin=new Date()
}else{if(q.allow==="onlyfuture"){e.dateTimeMin=new Date().setDate(new Date().getDate()+1)
}else{if(q.allow==="onlypast"){e.dateTimeMax=new Date().setDate(new Date().getDate()-1)
}else{if(q.allow==="current"){e.dateTimeMax=new Date();
e.dateTimeMin=new Date()
}else{if(q.allow==="onlycurrentmonth"){var v=new Date();
e.onlyMinDate=new Date(2015,v.getMonth(),1);
e.onlyMaxDate=new Date(2015,v.getMonth()+1,0)
}else{}}}}}}}else{if(q.type==="time"){e.isTime=true;
if(q.ngRequired&&(q.ngRequired==="true"||q.ngRequired===true)){e.ngRequired=true
}else{e.ngRequired=false
}if(angular.isDefined(q.defaulttime)&&q.defaulttime==="true"){e.ngModel=n(new Date,"shortTime")
}}else{if(q.type==="daterange"){console.log("inside date range");
e.isDateRange=true;
e.name=q.name;
if(angular.isDefined(q.formName)){}if(q.format!==undefined){e.dateRangeFormat=q.format
}if(q.fromLabel!==undefined&&q.fromLabel.length>0){e.fromLabel=q.fromLabel
}if(q.toLabel!==undefined&&q.toLabel.length>0){e.toLabel=q.toLabel
}if(q.defaultdate!==undefined&&q.defaultdate==="true"){e.fromModel=new Date();
e.toModel=new Date()
}if(q.ngRequired&&(q.ngRequired==="true"||q.ngRequired===true)){e.ngRequired=true
}else{e.ngRequired=false
}if(q.allow==="past"){e.fromMaxDate=new Date();
e.toMaxDate=new Date()
}else{if(q.allow==="future"){e.fromMinDate=new Date();
e.toMinDate=new Date()
}else{if(q.allow==="onlyfuture"){e.fromMinDate=new Date().setDate(new Date().getDate()+1);
e.toMinDate=new Date().setDate(new Date().getDate()+1)
}else{if(q.allow==="onlypast"){e.fromMaxDate=new Date().setDate(new Date().getDate()-1);
e.toMaxDate=new Date().setDate(new Date().getDate()-1)
}else{if(q.allow==="current"){e.fromMaxDate=new Date();
e.toMaxDate=new Date();
e.fromMinDate=new Date();
e.toMinDate=new Date()
}else{if(q.allow==="onlycurrentmonth"){var v=new Date();
e.onlyMinDate=new Date(2015,v.getMonth(),1);
e.onlyMaxDate=new Date(2015,v.getMonth()+1,0)
}else{}}}}}}}else{if(q.type==="timerange"){e.isTimeRange=true;
if(q.ngRequired&&(q.ngRequired==="true"||q.ngRequired===true)){e.ngRequired=true
}else{e.ngRequired=false
}if(q.fromLabel!==undefined&&q.fromLabel.length>0){e.fromLabel=q.fromLabel
}if(q.toLabel!==undefined&&q.toLabel.length>0){e.toLabel=q.toLabel
}if(q.defaulttime!==undefined&&q.defaulttime==="true"){e.fromModel=n(new Date(),"shortTime");
e.toModel=n(new Date(),"shortTime")
}}else{if(q.type==="datetimerange"){e.isDateTimeRange=true;
if(q.ngRequired&&(q.ngRequired==="true"||q.ngRequired===true)){e.ngRequired=true
}else{e.ngRequired=false
}if(q.fromLabel!==undefined&&q.fromLabel.length>0){e.fromLabel=q.fromLabel
}if(q.toLabel!==undefined&&q.toLabel.length>0){e.toLabel=q.toLabel
}if(q.defaultdate!==undefined&&q.defaultdate==="true"&&!e.fromModel){var f=new Date();
if(angular.isDefined(q.adddaystodefaultdate)){var g=parseInt(q.adddaystodefaultdate);
f.setDate(f.getDate()+g)
}if(angular.isDefined(q.defaultstarttime)){var o=q.defaultstarttime,r=o.match(/(\d+)\.(\d+) (\w+)/),u=/am/i.test(r[3])?parseInt(r[1],10):parseInt(r[1],10)+12,t=parseInt(r[2],10);
f.setHours(u);
f.setMinutes(t);
e.rangFromTimeModel=new Date(f);
e.rangeFromDatepickerModel=new Date(f)
}e.fromModel=n(f,q.format);
if(angular.isDefined(q.defaultendtime)){var o=q.defaultendtime,r=o.match(/(\d+)\.(\d+) (\w+)/),u=/am/i.test(r[3])?parseInt(r[1],10):parseInt(r[1],10)+12,t=parseInt(r[2],10);
f.setHours(u);
f.setMinutes(t);
e.rangeToTimeModel=f;
e.toDatepickerModel=f
}e.toModel=n(f,q.format)
}else{if(e.fromModel){e.rangFromTimeModel=new Date(e.fromModel);
e.rangeFromDatepickerModel=new Date(e.fromModel);
e.fromModel=n(e.fromModel,q.format)
}if(e.toModel){e.rangeToTimeModel=new Date(e.toModel);
e.toModel=n(e.toModel,q.format);
e.toDatepickerModel=new Date(e.rangeToTimeModel)
}}if(q.allow==="past"&&!e.toDatepickerModel){e.maxDate=new Date();
e.toDatepickerModel=new Date()
}else{if(q.allow==="future"&&!e.rangeFromDatepickerModel){e.minDate=new Date();
e.rangeFromDatepickerModel=e.minDate
}else{if(q.allow==="onlyfuture"&&!e.rangeFromDatepickerModel){e.minDate=new Date().setDate(new Date().getDate()+1);
e.rangeFromDatepickerModel=e.minDate
}else{if(q.allow==="onlypast"&&!e.toDatepickerModel){e.maxDate=new Date().setDate(new Date().getDate()-1);
e.toDatepickerModel=new Date().setDate(new Date().getDate()-1)
}else{if(q.allow==="current"){e.maxDate=new Date();
e.minDate=new Date()
}else{}}}}}}}}}}}e.initDateTimePopup=function(){$(i).bind("click",function(x){x=x||window.event;
var s=x.target
});
$(i).find(".dropdown.keep-open").on({"shown.bs.dropdown":function(){this.closable=false
},click:function(s){if(e.closePopup){this.closable=true;
e.closePopup=false
}else{this.closable=false
}},"hide.bs.dropdown":function(){return this.closable
}})
};
e.setDateAndTime=function(s,x){switch(s){case"datetime":e.ngModel=undefined;
e.datepickerModel=undefined;
e.timeModel=undefined;
break;
case"fromTime":e.fromTimeModel=new Date();
e.fromModel=undefined;
break;
case"toTime":e.toTimeModel=new Date();
e.toModel=undefined;
case"rangeFromClear":e.rangFromTimeModel=new Date();
e.fromModel=undefined;
break;
case"rangeToClear":e.rangeToTimeModel=new Date();
e.toModel=undefined
}e.closePopup=true
};
function h(x){x=x||window.event;
var s=x.target;
if(!$(i).is(x.target)&&$(i).has(x.target).length===0&&$(".open").has(x.target).length===0&&document.contains(s)){e.datePickerFlag.dateTimePicker=false;
e.datePickerFlag.fromIsOpen=false;
e.datePickerFlag.toIsOpen=false;
e.$digest();
$(".open").removeClass("open")
}else{}}$("body").bind("click",h);
e.$on("$destroy",function(){$("body").unbind("click",h)
});
e.setOnlyDate=function(s){e.ngModel=s
};
e.setFromDate=function(s){e.fromModel=s;
e.toMinDate=s
};
e.setToDate=function(s){e.toModel=s;
e.fromMaxDate=s
};
e.setFromTime=function(s){e.fromModel=n(s,"shortTime")
};
e.setToTime=function(s){e.toModel=n(s,"shortTime")
};
e.setRangeFromDate=function(s,y){var x;
if(angular.isDefined(y)){x=l(y,s)
}else{var x=l(s)
}e.fromModel=n(x,q.format);
e.rangeFromDatepickerModel=x
};
e.setRangeToDate=function(s,y){var x;
if(angular.isDefined(y)){x=l(y,s)
}else{var x=l(s)
}e.toModel=n(x,q.format);
e.rangToTimeModel=x;
e.toDatepickerModel=x
};
e.setRangeFromTime=function(y,s){if(!s){s=new Date()
}var x=l(y,s);
e.fromModel=n(x,q.format);
e.rangeFromDatepickerModel=x
};
e.setRangeToTime=function(y,s){if(!s){s=new Date()
}var x=l(y,s);
e.toModel=n(x,q.format);
e.toDatepickerModel=x
};
e.dtPicker=[];
e.open=function(s,x){e.dtPicker=[];
s.preventDefault();
s.stopPropagation();
e.dtPicker[x]=true;
$(".open").removeClass("open")
};
e.setTime=function(B,z){if(!z){z=new Date()
}if(z!==undefined){var A=new Date(B);
var s=(A.getHours()<10)?"0"+A.getHours():A.getHours();
var y=(A.getMinutes()<10)?"0"+A.getMinutes():A.getMinutes();
var x=new Date(z);
x.setHours(s);
x.setMinutes(y);
e.ngModel=n(x,q.format)
}else{m(B)
}};
e.daysInMonth=function(x,s){return new Date(s,x,0).getDate()
};
e.setDate=function(x,A){var z=l(x);
if(A){var s=(A.getHours()<10)?"0"+A.getHours():A.getHours();
var y=(A.getMinutes()<10)?"0"+A.getMinutes():A.getMinutes();
z.setHours(s);
z.setMinutes(y)
}e.ngModel=n(z,q.format);
e.timeModel=z
};
e.hstep=1;
e.mstep=30;
e.ismeridian=false;
e.disabled=function(x,A){var s=false;
var B=x.getMonth()+1;
var D=x.getDate();
var C=x.getFullYear();
if(e.holidayList!==null&&e.holidayList!==undefined){for(var y=0;
y<e.holidayList.length;
y++){var F=e.holidayList[y].getMonth()+1;
var E=e.holidayList[y].getDate();
var z=e.holidayList[y].getFullYear();
if(B===F&&D===E&&C===z){s=true;
break
}}}return(A==="day"&&(x.getDay()===0||s))
}
}}
}])
});